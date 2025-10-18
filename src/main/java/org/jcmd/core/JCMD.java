package org.jcmd.core;

import org.jcmd.commands.templates.CommandWrapper;
import org.jquill.Debug;

import java.io.PrintStream;
import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.regex.Pattern;

public class JCMD {
    private final Map<String, CommandInterface> commands = new TreeMap<>();
    private final Scanner scanner = new Scanner(System.in);
    public static final PrintStream out = System.out;
    private boolean running = true;
    private final CountDownLatch ready = new CountDownLatch(1);

    public static final String JCMD_VERSION = BuildInfo.JCMD_VERSION;
    public static final String JAVA_VERSION = BuildInfo.JAVA_VERSION;
    public final String MAVEN_VERSION = BuildInfo.MAVEN_VERSION;
    public final String JQUILL_VERSION = BuildInfo.JQUILL_VERSION;

    public static final String PROJECT_NAME = BuildInfo.PROJECT_NAME;
    public static final String PROJECT_DESCRIPTION = BuildInfo.PROJECT_DESCRIPTION;
    public static final String HEADER = "Hello ${user}";

    public final PackageReg pack = new PackageReg();

    // ------------------ Run Methods ------------------

    public void run() {
        run(true);
    }
    public void run(boolean showHeader) {
        if (getCommand("exit") == null) {
            Debug.error("No core package registered.");
            return;
        }

        if (showHeader) execute("echo " + HEADER, false, false);
        ready.countDown(); // Signal that JCMD is ready

        while (running) {
            out.print("> ");
            String input = scanner.nextLine().trim();
            if (input.isEmpty()) continue; // Ignore empty input

            // Split input into multiple commands using your prefix
            String[] commandsSplit = input.split("\\s*" + Pattern.quote(Variables.NEW_COMMAND_PREFIX) + "\\s*");

            for (String cmdLine : commandsSplit) {
                String[] parts = cmdLine.trim().split("\\s+");
                if (parts.length == 0) continue;

                String name = parts[0];
                String[] args = Arrays.copyOfRange(parts, 1, parts.length);

                CommandInterface command = getCommand(name);
                if (command != null) {
                    try {
                        command.execute(args);
                    } catch (Exception e) {
                        Debug.error("Error: " + e.getMessage());
                    }
                } else {
                    Debug.error("Unknown command: " + name);
                }
            }
        }
    }

    // ------------------ Run Async Methods ------------------

    public Thread runAsync() {
        return runAsync(false, true);
    }
    public Thread runAsync(boolean autoClose, boolean showHeader) {
        Thread thread = new Thread(() -> run(showHeader), "JCMD-Thread");
        thread.setDaemon(autoClose);
        thread.start();
        try {
            ready.await(); // Wait until JCMD is ready
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        return thread;
    }

    // ------------------ Execute Methods ------------------

    public void execute(String input) {
        execute(input, true, true);
    }
    public void execute(String input, boolean showInput, boolean newLinePrefix) {
        // Basic validation
        if (input == null || input.trim().isEmpty() || !running) return;

        if (showInput) out.println(input);

        // Parse input
        String[] parts = input.trim().split("\\s+");
        String name = parts[0];
        String[] args = Arrays.copyOfRange(parts, 1, parts.length);

        // Find and execute command
        CommandInterface command = commands.get(name);
        if (command != null) {
            try {
                command.execute(args);
            } catch (Exception e) {
                Debug.error(e.getMessage());
            }
        } else {
            Debug.error("Unknown command: " + name + ". Use 'list' to see all commands.");
        }

        if (newLinePrefix) out.print("> ");
    }

    // ------------------ CommandInterface Management ------------------

    public void register(CommandInterface command, String packageKey) {
        if (command == null) throw new IllegalArgumentException("CommandInterface cannot be null");

        // Validate package key
        String name = command.getName();
        if (name == null || name.isEmpty())
            throw new IllegalArgumentException("CommandInterface name cannot be null or empty");

        // If name already exists under a different package, namespace it
        if (commands.containsKey(name)) {
            CommandInterface existing = commands.get(name);

            // Only namespace if the existing command is from a different package
            if (!Objects.equals(existing.getCategory().toLowerCase(), packageKey)) {
                String namespaced = packageKey + "." + name;
                Debug.error("CommandInterface '" + name + "' conflicts. Registered as '" + namespaced + "'.");

                // Wrap the original command so getName() returns the namespaced name
                command = new CommandWrapper(command, namespaced);
                name = namespaced;
                return;
            }

        }


        commands.put(name, command);
    }
    public void unregister(String name) {
        CommandInterface removed = commands.remove(name);

        if (removed == null) {
            Debug.warn("No such command registered: " + name);
            return;
        }

        // Also remove any aliases pointing to this command
        List<String> aliasesToRemove = getStrings(name);

        for (String aliasName : aliasesToRemove) {
            commands.remove(aliasName);
            Debug.success("Removed alias: " + aliasName);
        }
    }

    public Collection<CommandInterface> getCommands() {
        return commands.values();
    }
    public CommandInterface getCommand(String name) {
        return commands.get(name);
    }

    // ------------------ Helper Commands ------------------

    private List<String> getStrings(String name) {
        List<String> aliasesToRemove = new ArrayList<>();
        for (Map.Entry<String, CommandInterface> entry : commands.entrySet()) { // Iterate over a copy to avoid ConcurrentModificationException
            CommandInterface command = entry.getValue();
            if ("Alias".equals(command.getCategory())) {
                String desc = command.getDescription();
                if (desc != null && desc.contains("'" + name + "'")) {
                    aliasesToRemove.add(entry.getKey());
                }
            }
        }
        return aliasesToRemove;
    } // Used by unregister
    public CommandInterface stringToCommand(String className) // Used by register
            throws ReflectiveOperationException {

        // Ensure fully-qualified name
        if (!className.contains(".")) {
            throw new IllegalArgumentException("Class name must be fully qualified, e.g., org.jcmd.commands.base.Time");
        }

        // Automatically capitalize the simple class name
        int lastDot = className.lastIndexOf('.');
        if (lastDot >= 0 && lastDot < className.length() - 1) {
            String packageName = className.substring(0, lastDot + 1);
            String simpleName = className.substring(lastDot + 1);
            // Capitalize first letter if it's lowercase
            if (Character.isLowerCase(simpleName.charAt(0))) {
                simpleName = Character.toUpperCase(simpleName.charAt(0)) + simpleName.substring(1);
            }
            className = packageName + simpleName;
        } else if (lastDot == -1) {
            // No package, just capitalize
            className = Character.toUpperCase(className.charAt(0)) + className.substring(1);
        }

        // Load the class
        Class<?> clazz = Class.forName(className);

        // Verify it implements CommandInterface
        if (!CommandInterface.class.isAssignableFrom(clazz)) {
            throw new IllegalArgumentException(className + " does not implement CommandInterface");
        }

        CommandInterface instance;

        try {
            // Try constructor with JCMD parameter
            instance = (CommandInterface) clazz.getConstructor(JCMD.class).newInstance(this);
        } catch (NoSuchMethodException e) {
            // Fall back to no-arg constructor
            instance = (CommandInterface) clazz.getConstructor().newInstance();
        }
        return instance;
    }

    // ------------------ Package Registration ------------------

    private void message(String className) {
        Debug.info("Registered " + className);
    }

    public void registerPackage(String key) {
        registerPackage(key, false);
    }
    public void registerPackage(String key, boolean showRegister) {
        String[] classNames = pack.registry.get(key.toLowerCase());
        String packageName = pack.names.get(key.toLowerCase());

        // Validate package key
        if (classNames == null || packageName == null) {
            Debug.error("Unknown package key: " + key);
            return;
        }

        // Register each command in the package
        for (String className : classNames) {
            try {
                CommandInterface command = stringToCommand(packageName + "." + className);
                register(command, key.toLowerCase()); // Pass package key for namespacing
                if (showRegister) {
                    message(className);
                }
            } catch (Exception e) {
                Debug.error("Failed to register: " + className + " -> " + e.getMessage());
            }
        }
    }

    public void unregisterPackage(String key) {
        unregisterPackage(key, false);
    }
    public void unregisterPackage(String key, boolean showUnregister) {
        String[] classNames = pack.registry.get(key.toLowerCase());
        String packageName = pack.names.get(key.toLowerCase());

        // Validate package key
        if (classNames == null || packageName == null) {
            Debug.error("Unknown package key: " + key);
            return;
        }

        // Register each command in the package
        for (String className : classNames) {
            try {
                unregister(className); // Pass package key for namespacing
                if (showUnregister) {
                    message(className);
                }
            } catch (Exception e) {
                Debug.error("Failed to unregister: " + className + " -> " + e.getMessage());
            }
        }
    }

    // ------------------ Stop Method ------------------

    public void stop() {
        running = false;
    }
}