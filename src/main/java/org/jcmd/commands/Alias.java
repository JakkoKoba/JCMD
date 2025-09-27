package org.jcmd.commands;

import org.jcmd.core.*;

public class Alias implements Command {
    private final JCMD engine;
    private final String ALIAS_CATEGORY = "Alias";
    private final String DELETE_FLAG = "-del";

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
        if (args.length < 1) {
            System.out.println("Usage: " + NAME + " <newName> <existingCommand>");
            System.out.println("       " + NAME + " " + DELETE_FLAG + " <aliasName>");
            return;
        }

        // Deletion mode
        if (DELETE_FLAG.equals(args[0])) {
            if (args.length < 2) {
                System.out.println("Usage: " + NAME + " " + DELETE_FLAG + " <aliasName>");
                return;
            }
            String aliasName = args[1];
            Command alias = engine.getCommand(aliasName);

            if (alias == null) {
                System.out.println("No such alias: " + aliasName);
                return;
            }
            if (!ALIAS_CATEGORY.equals(alias.getCategory())) {
                System.out.println(aliasName + " is not an alias and cannot be deleted.");
                return;
            }

            engine.unregister(aliasName);
            System.out.println("Alias removed: " + aliasName);
            return;
        }

        // Creation mode
        if (args.length < 2) {
            System.out.println("Usage: " + NAME + " <newName> <existingCommand>");
            return;
        }

        String newName = args[0];
        String targetName = args[1];
        Command target = engine.getCommand(targetName);

        if (target == null) {
            System.out.println("No such command: " + targetName);
            return;
        }

        // Create a lightweight wrapper command
        Command alias = new Command() {
            @Override
            public String getName() { return newName; }
            @Override
            public String getCategory() { return ALIAS_CATEGORY; }
            @Override
            public String getDescription() {
                return "Alias for '" + targetName + "'";
            }
            @Override
            public void execute(String[] a) {
                try {
                    target.execute(a);
                } catch (Exception e) {
                    System.out.println("Error in alias target: " + e.getMessage());
                }
            }
        };

        engine.register(alias);
        System.out.println("Alias created: " + newName + " -> " + targetName);
    }
}
