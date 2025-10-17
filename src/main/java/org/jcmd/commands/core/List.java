package org.jcmd.commands.core;

import org.jcmd.core.CommandInterface;
import org.jcmd.core.JCMD;
import org.jquill.Debug;
import static org.jcmd.core.IO.out;

public class List implements CommandInterface {
    private final JCMD engine;

    private final String NAME = "list";
    private final String DESCRIPTION = "Lists all available commands.";
    private final String CATEGORY = "Core";

    public List(JCMD engine) {
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
        if (args.length == 0) {
            // No category specified, list all commands
            out.println("Available commands:");
            for (CommandInterface c : engine.getCommands()) {
                out.printf("  %-10s : (%s)%n", c.getName(), c.getCategory());
            }
        } else {
            // Filter by category
            String filterCategory = args[0];
            out.println("Commands in category '" + filterCategory + "':");
            boolean found = false;
            for (CommandInterface c : engine.getCommands()) {
                if (c.getCategory() != null && c.getCategory().equalsIgnoreCase(filterCategory)) {
                    out.printf("  %-10s %n", c.getName());
                    found = true;
                }
            }
            if (!found) {
                Debug.warn("  No commands found in this category.");
            }
        }
    }
}