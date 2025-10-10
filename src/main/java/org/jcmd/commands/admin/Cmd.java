package org.jcmd.commands.admin;

import org.jcmd.core.Command;
import org.jcmd.core.JCMD;
import org.jcmd.core.Variables;
import org.jquill.Debug;

import java.util.Objects;

public class Cmd implements Command {
    private final JCMD engine;

    private final String NAME = "command";
    private final String DESCRIPTION = "Register or unregister commands.";
    private final String CATEGORY = "Admin";

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
            Debug.warn("Unknown usage of '" + NAME + "'");
            Debug.info(NAME + " " + REGISTER_FLAG + " org.jcmd.commands.<package>.<name>");
            Debug.info(NAME + " " + UNREGISTER_FLAG + " <commandName>");
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
                Debug.success("Registered command: " + cmdInstance.getName());

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
            Command cmd = engine.getCommand(target);
            if (cmd == null) {
                Debug.warn("No such command registered: " + target);
                return;
            }
            if (Objects.equals(cmd.getCategory(), "Core") || Objects.equals(cmd.getName(), "command")) {
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