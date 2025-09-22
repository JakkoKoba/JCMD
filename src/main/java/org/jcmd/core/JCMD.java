package org.jcmd.core;

import org.jcmd.commands.*;
import java.util.*;

public class JCMD {
    private final Map<String, Command> commands = new TreeMap<>();
    private final Scanner scanner = new Scanner(System.in);
    private boolean running = true;

    private static final String JCMD_VERSION = "0.1.1";

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

        if (commands.containsKey(name)) {
            System.out.println("Warning: Overwriting existing command: " + name);
        }

        commands.put(name, command);
    }

    // Unregister a command
    public void unregister(String name) {
        if (commands.remove(name) != null) {
            System.out.println("Command unregistered: " + name);
        } else {
            System.out.println("No such command registered: " + name);
        }
    }

    // Register a set of base commands
    public void registerBase() {
        register(new Alias(this));
        register(new Echo());
        register(new Exit(this));
        register(new Help(this));
        register(new Version(this));
    }
}

