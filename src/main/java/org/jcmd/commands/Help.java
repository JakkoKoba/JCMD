package org.jcmd.commands;

import org.jcmd.core.*;

public class Help implements Command {
    private final JCMD engine;

    public Help(JCMD engine) {
        this.engine = engine;
    }

    @Override
    public String getName() { return "help"; }

    @Override
    public String getCategory() {
        return "Base";
    }

    @Override
    public String getDescription() { return "Lists all available commands."; }

    @Override
    public void execute(String[] args) {
        if (args.length == 0) {
            // No category specified, list all commands
            System.out.println("Available commands:");
            for (Command c : engine.getCommands()) {
                System.out.printf("  %-10s : (%s) %s%n", c.getName(), c.getCategory(), c.getDescription());
            }
        } else {
            // Filter by category
            String filterCategory = args[0];
            System.out.println("Commands in category '" + filterCategory + "':");
            boolean found = false;
            for (Command c : engine.getCommands()) {
                if (c.getCategory() != null && c.getCategory().equalsIgnoreCase(filterCategory)) {
                    System.out.printf("  %-10s : %s%n", c.getName(), c.getDescription());
                    found = true;
                }
            }
            if (!found) {
                System.out.println("  No commands found in this category.");
            }
        }
    }
}
