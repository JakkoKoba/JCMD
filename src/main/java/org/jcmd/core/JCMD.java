package org.jcmd.core;

import java.util.*;

public class JCMD {
    private final Map<String, Command> commands = new HashMap<>();
    private final Scanner scanner = new Scanner(System.in);
    private boolean running = true;

    private String jcmdVersion = "0.1.0";

    public String getJcmdVersion() {
        return jcmdVersion;
    }
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

    public void stop() {
        running = false;
    }

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
}

