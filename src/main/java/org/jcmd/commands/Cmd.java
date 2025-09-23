package org.jcmd.commands;

import org.jcmd.core.*;

public class Cmd implements Command {
    private final JCMD engine;
    private final String REGISTER_FLAG = "-reg";
    private final String UNREGISTER_FLAG = "-unreg";

    private final String NAME = "command";
    private final String DESCRIPTION = "Register or unregister commands.";
    private final String CATEGORY = "Base";

    public Cmd(JCMD engine) {
        this.engine = engine;
    }

    @Override
    public String getName() { return NAME; }

    @Override
    public String getDescription() { return DESCRIPTION; }

    @Override
    public String getCategory() {
        return CATEGORY;
    }

    @Override
    public void execute(String[] args) {
        if (args.length < 2) {
            System.out.println("Usage: " + NAME + " " + REGISTER_FLAG + " <className>");
            System.out.println("       " + NAME + " " + UNREGISTER_FLAG + " <commandName>");
            return;
        }
        String action = args[0];
        String target = args[1];

        switch (action) {
            case REGISTER_FLAG:
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

            case UNREGISTER_FLAG:
                if (engine.getCommand(target) != null) {
                    engine.unregister(target);
                    System.out.println("Unregistered command: " + target);
                } else {
                    System.out.println("No such command registered: " + target);
                }
                break;

            default:
                System.out.println("Unknown action: " + action);
                System.out.println("Use " + REGISTER_FLAG + " or " + UNREGISTER_FLAG + ".");
        }
    }
}
