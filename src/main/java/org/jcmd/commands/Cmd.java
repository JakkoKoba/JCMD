package org.jcmd.commands;

import org.jcmd.core.*;

public class Cmd implements Command {
    private final JCMD engine;

    public Cmd(JCMD engine) {
        this.engine = engine;
    }

    @Override
    public String getName() { return "command"; }

    @Override
    public String getCategory() {
        return "Base";
    }

    @Override
    public String getDescription() { return "Register or unregister commands"; }

    @Override
    public void execute(String[] args) {
        if (args.length < 2) {
            System.out.println("Usage: command -reg <className>");
            System.out.println("       command -unreg <commandName>");
            return;
        }
        String action = args[0];
        String target = args[1];

        switch (action) {
            case "-reg":
                try {
                    // Convert string to Command instance
                    Command cmdInstance = engine.stringToCommand(target);

                    // Register the command
                    engine.register(cmdInstance);
                    System.out.println("Registered command: " + cmdInstance.getName());

                } catch (ClassNotFoundException e) {
                    System.out.println("Class not found: " + target);
                } catch (IllegalArgumentException e) {
                    System.out.println("Invalid command class: " + e.getMessage());
                } catch (ReflectiveOperationException e) {
                    System.out.println("Error creating command instance: " + e.getMessage());
                }
                break;

            case "-unreg":
                if (engine.getCommand(target) != null) {
                    engine.unregister(target);
                    System.out.println("Unregistered command: " + target);
                } else {
                    System.out.println("No such command registered: " + target);
                }
                break;

            default:
                System.out.println("Unknown action: " + action);
                System.out.println("Use -reg or -unreg");
        }
    }
}
