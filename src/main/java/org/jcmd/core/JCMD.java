package org.jcmd.core;

import org.jcmd.commands.*;
import org.jcmd.commands.Date;

import java.util.*;

public class JCMD {
    private final Map<String, Command> commands = new TreeMap<>();
    private final Scanner scanner = new Scanner(System.in);
    private boolean running = true;

    private static final String JCMD_VERSION = "0.1.2";

    public String getJcmdVersion() {
        return JCMD_VERSION;
    }

    // Run JCMD
    public void run() {
        System.out.println("JCMD started. Type 'exit' to leave.");
        while (running) {
            System.out.print("> ");
            String input = scanner.nextLine().trim();
            if (input.isEmpty()) continue; // Ignore empty input

            String[] parts = input.split("\\s+"); // Split input into command and arguments
            String name = parts[0]; // Command name
            String[] args = Arrays.copyOfRange(parts, 1, parts.length); // Command arguments

            Command command = commands.get(name);
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

    // Get a collection of all registered commands
    public Collection<Command> getCommands() {
        return commands.values();
    }

    // Fetch a command by name
    public Command getCommand(String name) {
        return commands.get(name);
    }

    // Stop JCMD
    public void stop() {
        running = false;
    }

    // Register a command
    public void register(Command command) {
        if (command == null) {
            throw new IllegalArgumentException("Command cannot be null");
        }

        String name = command.getName();
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("Command name cannot be null or empty");
        }

        // Check for duplicate command names
        if (commands.containsKey(name)) {
                        System.out.println("Warning: Command already registered: " + name + ". Registration skipped.");
            return;
        }
        commands.put(name, command);
    }

    // Unregister a command
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

    // Find aliases that reference the given command name
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
    }

    public Command stringToCommand(String className)
            throws ReflectiveOperationException {

        // Ensure fully-qualified name
        if (!className.contains(".")) {
            throw new IllegalArgumentException("Class name must be fully qualified, e.g., org.jcmd.commands.Time");
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

        // Verify it implements Command
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


    // Register a set of base commands
    public void registerBase() {
        register(new Alias(this));
        register(new Cmd(this));
        register(new Date());
        register(new Echo());
        register(new Exit(this));
        register(new Help(this));
        register(new Time());
        register(new Version(this));
    }
}

