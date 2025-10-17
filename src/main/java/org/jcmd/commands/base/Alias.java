package org.jcmd.commands.base;

import org.jcmd.core.CommandInterface;
import org.jcmd.core.JCMD;
import org.jquill.Debug;

import java.util.Arrays;
import java.util.Objects;

public class Alias implements CommandInterface {
    private final JCMD engine;
    private final String ALIAS_CATEGORY = "Alias";

    private final String NAME = "alias";
    private final String DESCRIPTION = "Creates an alias for an existing command.";
    private final String CATEGORY = "Base";

    public Alias(JCMD engine) {
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
        if (args.length < 2) {
            Debug.warn("Usage: " + NAME + " <newName> <existingCommand>");
            return;
        }

        String newName = args[0];
        String targetName = args[1];
        String[] bakedArgs = Arrays.copyOfRange(args, 2, args.length);
        CommandInterface target = engine.getCommand(targetName);

        // Check if target command exists
        if (target == null) {
            Debug.warn("No such command: " + targetName);
            return;
        }

        // Prevent aliasing an alias
        if (Objects.equals(target.getCategory(), "Alias")) {
            Debug.warn("Cannot create an alias to another alias.");
            return;
        }

        // Create a lightweight wrapper command
        CommandInterface alias = new CommandInterface() {
            @Override
            public String getName() {
                return newName;
            }
            @Override
            public String getCategory() {
                return ALIAS_CATEGORY;
            }
            @Override
            public String getDescription() {
                return "Alias for '" + targetName + "'";
            }
            @Override
            public void execute(String[] runtimeArgs) {
                // Start with the target command name
                StringBuilder commandBuilder = new StringBuilder(targetName);

                // Append baked arguments
                for (String arg : bakedArgs) {
                    commandBuilder.append(" ").append(arg);
                }

                // Append runtime arguments
                for (String arg : runtimeArgs) {
                    commandBuilder.append(" ").append(arg);
                }

                String combinedCommand = commandBuilder.toString();

                try {
                    engine.execute(combinedCommand, false, false); // delegate to engine with full command string
                } catch (Exception e) {
                    Debug.error("Error in alias target: " + e.getMessage());
                }
            }
        };

        engine.register(alias, CATEGORY);
        Debug.success("Alias created: " + newName + " -> " + targetName + (bakedArgs.length > 0 ? " " + String.join(" ", bakedArgs) : ""));
    }
}