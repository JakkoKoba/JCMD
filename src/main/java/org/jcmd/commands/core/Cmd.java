package org.jcmd.commands.core;

import org.jcmd.core.Command;
import org.jcmd.core.JCMD;
import org.jcmd.core.Variables;

import java.util.Objects;

public class Cmd implements Command {
    private final JCMD engine;

    private final String NAME = "command";
    private final String DESCRIPTION = "Register or unregister commands.";
    private final String CATEGORY = "Core";

    public Cmd(JCMD engine) {
        this.engine = engine;
    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public String getDescription() {
        return DESCRIPTION;
    }

    @Override
    public String getCategory() {
        return CATEGORY;
    }

    @Override
    public void execute(String[] args) {
        String REGISTER_FLAG = Variables.COMMAND_REGISTER_FLAG;
        String UNREGISTER_FLAG = Variables.COMMAND_UNREGISTER_FLAG;
        if (args.length < 2) {
            System.out.println("Usage: " + NAME + " " + REGISTER_FLAG + " <className>");
            System.out.println("       " + NAME + " " + UNREGISTER_FLAG + " <commandName>");
            return;
        }

        String action = args[0];
        String target = args[1];

        // Register flag
        if (action.equals(REGISTER_FLAG)) {
            try {
                // Convert string to Cmd instance
                Command cmdInstance = engine.stringToCommand(target);

                // Determine package key
                String packageKey = cmdInstance.getClass().getPackage().getName();
                packageKey = packageKey.substring(packageKey.lastIndexOf('.') + 1).toLowerCase();

                // Register the command
                engine.register(cmdInstance, packageKey);
                System.out.println("Registered command: " + cmdInstance.getName());

            } catch (ClassNotFoundException e) {
                System.out.println("Class not found: " + target);
            } catch (IllegalArgumentException e) {
                System.out.println("Invalid command class: " + e.getMessage());
            } catch (ReflectiveOperationException e) {
                System.out.println("Error creating command instance: " + e.getMessage());
            }
            return;
        }

        // Unregister flag
        if (action.equals(UNREGISTER_FLAG)) {
            Command cmd = engine.getCommand(target);
            if (cmd == null) {
                System.out.println("No such command registered: " + target);
                return;
            }
            if (Objects.equals(engine.getCommand(target).getCategory(), "Core")) {
                System.out.println("Cannot unregister core commands.");
                return;
            }
            engine.unregister(target);
            System.out.println("Unregistered command: " + target);
            return;
        }
        // Unknown flag
        System.out.println("Unknown action: " + action);
        System.out.println("Use " + REGISTER_FLAG + " or " + UNREGISTER_FLAG + ".");
    }
}