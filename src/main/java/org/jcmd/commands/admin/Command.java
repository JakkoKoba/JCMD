package org.jcmd.commands.admin;

import org.jcmd.core.CommandInterface;
import org.jcmd.core.JCMD;
import org.jcmd.core.Variables;
import org.jquill.Debug;

import java.util.Objects;

public class Command implements CommandInterface {
    private final JCMD engine;

    private final String NAME = "command";
    private final String DESCRIPTION = "Register and unregister commands.";
    private final String CATEGORY = "Admin";

    public Command(JCMD engine) {
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
        String REGISTER_FLAG = Variables.REGISTER_FLAG;
        String UNREGISTER_FLAG = Variables.UNREGISTER_FLAG;

        if (args.length == 2 && REGISTER_FLAG.equalsIgnoreCase(args[0])) {
            String target = args[1];
            // Extract the package part (before the last dot)
            int dotIndex = target.lastIndexOf('.');
            if (dotIndex > 0) {
                String packageName = target.substring(0, dotIndex);
                // If package doesn't exist in your registry, warn
                if (engine.pack.registry.get(packageName.toLowerCase()) == null) {
                    Debug.warn("Unknown package: " + packageName);
                    return;
                }
            }
            if (engine.pack.registry.get(target.toLowerCase()) == null) {
                Debug.warn("Unknown package: " + target);
                return;
            }
        }

        if (args.length < 2) {
            Debug.warn("Unknown usage of '" + NAME + "'");
            Debug.info(NAME + " " + REGISTER_FLAG + " <package>.<name>");
            Debug.info(NAME + " " + UNREGISTER_FLAG + " <commandName>");
            return;
        }

        String action = args[0];
        String target = args[1];

        // Register flag
        if (action.equals(REGISTER_FLAG)) {
            try {
                // Convert string to CommandInterface instance
                CommandInterface commandInstance = engine.stringToCommand("org.jcmd.commands." + target);

                // Determine package key
                String packageKey = commandInstance.getClass().getPackage().getName();
                packageKey = packageKey.substring(packageKey.lastIndexOf('.') + 1).toLowerCase();

                // Register the command
                engine.register(commandInstance, packageKey);
                Debug.success("Registered command: " + commandInstance.getName());

            } catch (ClassNotFoundException e) {
                Debug.warn("Class not found: " + target);
            } catch (IllegalArgumentException e) {
                Debug.error("Invalid command class: " + e.getMessage());
            } catch (ReflectiveOperationException e) {
                Debug.error("Error creating command instance: " + e.getMessage());
            }
            return;
        }

        // Unregister flag
        if (action.equals(UNREGISTER_FLAG)) {
            CommandInterface command = engine.getCommand(target);
            if (command == null) {
                Debug.warn("No such command registered: " + target);
                return;
            }
            if (Objects.equals(command.getCategory(), "Core") || Objects.equals(command.getName(), "command")) {
                Debug.warn("Cannot unregister core commands.");
                return;
            }
            engine.unregister(target);
            Debug.success("Unregistered command: " + target);
            return;
        }

        // Unknown flag
        Debug.warn("Unknown action: " + action);
        Debug.info("Use " + REGISTER_FLAG + " or " + UNREGISTER_FLAG + ".");
    }
}