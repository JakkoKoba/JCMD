package org.jcmd.commands.templates;

import org.jcmd.core.*;

public class CommandWrapper implements CommandInterface {
    private final CommandInterface original;
    private final String overriddenName;

    public CommandWrapper(CommandInterface original, String overriddenName) {
        this.original = original;
        this.overriddenName = overriddenName;
    }

    @Override
    public String getName() { return overriddenName; }

    @Override
    public String getCategory() { return original.getCategory(); }

    @Override
    public String getDescription() { return original.getDescription(); }

    @Override
    public void execute(String[] args) { original.execute(args); }
}