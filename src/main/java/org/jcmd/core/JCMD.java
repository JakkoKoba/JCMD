package org.jcmd.core;

import org.jcmd.commands.templates.CommandWrapper;

import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.regex.Pattern;

public class JCMD {

    private final Map<String, Command> commands = new TreeMap<>();
    private final Scanner scanner = new Scanner(System.in);
    private boolean running = true;
    private final CountDownLatch ready = new CountDownLatch(1);

    public static final String JCMD_VERSION = BuildInfo.JCMD_VERSION;
    public static final String JAVA_VERSION = BuildInfo.JAVA_VERSION;
    public final String MAVEN_VERSION = BuildInfo.MAVEN_VERSION;

    public static final String PROJECT_NAME = BuildInfo.PROJECT_NAME;
    public static final String PROJECT_DESCRIPTION = BuildInfo.PROJECT_DESCRIPTION;
    public static final String HEADER = "Hello ${user}";

    private final PackageReg pack = new PackageReg();

    // ------------------ Run Methods ------------------

    public void run() {
        run(true);
    }
    public void run(boolean showHeader) {
        if (getCommand("exit") == null) {
            System.out.println("No core package registered.");
            return;
        }

        if (showHeader) execute("echo " + HEADER, false, false);
        ready.countDown(); // Signal that JCMD is ready

        while (running) {
            System.out.print("> ");
            String input = scanner.nextLine().trim();
            if (input.isEmpty()) continue; // Ignore empty input

            // Split input into multiple commands using your prefix
            String[] commandsSplit = input.split("\\s*" + Pattern.quote(Variables.NEW_COMMAND_PREFIX) + "\\s*");

            for (String cmdLine : commandsSplit) {
                String[] parts = cmdLine.trim().split("\\s+");
                if (parts.length == 0) continue;

                String name = parts[0];
                String[] args = Arrays.copyOfRange(parts, 1, parts.length);

                Command command = getCommand(name);
                if (command != null) {
                    try {
                        command.execute(args);
                    } catch (Exception e) {
                        System.out.println("Error: " + e.getMessage());
                    }
                } else {
                    System.out.println("Unknown command: " + name);
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

        if (showInput) System.out.println(input);

        // Parse input
        String[] parts = input.trim().split("\\s+");
        String name = parts[0];
        String[] args = Arrays.copyOfRange(parts, 1, parts.length);

        // Find and execute command
        Command command = commands.get(name);
        if (command != null) {
            try {
                command.execute(args);
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        } else {
            System.out.println("Unknown command: " + name + ". Use 'help' to see all commands.");
        }

        if (newLinePrefix) System.out.print("> ");
    }

    // ------------------ Command Management ------------------

    public void register(Command command, String packageKey) {
        if (command == null) throw new IllegalArgumentException("Command cannot be null");

        // Validate package key
        String name = command.getName();
        if (name == null || name.isEmpty())
            throw new IllegalArgumentException("Command name cannot be null or empty");

        // If name already exists, automatically namespace it
        if (commands.containsKey(name)) {
            String namespaced = packageKey + "." + name;
            System.out.println("Warning: Command '" + name + "' conflicts. Registered as '" + namespaced + "'.");
            name = namespaced;
            // Wrap the command to override getName
            command = new CommandWrapper(command, name);
        }

        commands.put(name, command);
    }
    public void unregister(String name) {
        Command removed = commands.remove(name);

        if (removed == null) {
            System.out.println("No such command registered: " + name);
            return;
        }

        // Also remove any aliases pointing to this command
        List<String> aliasesToRemove = getStrings(name);

        for (String aliasName : aliasesToRemove) {
            commands.remove(aliasName);
            System.out.println("Removed alias: " + aliasName);
        }
    }

    public Collection<Command> getCommands() {
        return commands.values();
    }
    public Command getCommand(String name) {
        return commands.get(name);
    }

    // ------------------ Reflection Utilities ------------------

    public Command stringToCommand(String className)
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

        // Verify it implements Cmd
        if (!Command.class.isAssignableFrom(clazz)) {
            throw new IllegalArgumentException(className + " does not implement Command");
        }

        Command instance;

        try {
            // Try constructor with JCMD parameter
            instance = (Command) clazz.getConstructor(JCMD.class).newInstance(this);
        } catch (NoSuchMethodException e) {
            // Fall back to no-arg constructor
            instance = (Command) clazz.getConstructor().newInstance();
        }
        return instance;
    }


    // ------------------ Helper Commands ------------------

    private List<String> getStrings(String name) {
        List<String> aliasesToRemove = new ArrayList<>();
        for (Map.Entry<String, Command> entry : commands.entrySet()) { // Iterate over a copy to avoid ConcurrentModificationException
            Command cmd = entry.getValue();
            if ("Alias".equals(cmd.getCategory())) {
                String desc = cmd.getDescription();
                if (desc != null && desc.contains("'" + name + "'")) {
                    aliasesToRemove.add(entry.getKey());
                }
            }
        }
        return aliasesToRemove;
    } // Used by unregister

    // ------------------ Package Registration ------------------

    public void registerPackage(String key) {
        String[] classNames = pack.registry.get(key.toLowerCase());
        String packageName = pack.names.get(key.toLowerCase());

        // Validate package key
        if (classNames == null || packageName == null) {
            System.out.println("Unknown package key: " + key);
            return;
        }

        // Register each command in the package
        for (String className : classNames) {
            try {
                Command cmd = stringToCommand(packageName + "." + className);
                register(cmd, key.toLowerCase()); // Pass package key for namespacing
            } catch (Exception e) {
                System.out.println("Failed to register: " + className + " -> " + e.getMessage());
            }
        }
    }

    // ------------------ Stop Method ------------------

    public void stop() {
        running = false;
    }
}