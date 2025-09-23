package org.jcmd.commands;

import org.jcmd.core.*;

public class Version implements Command {
    private final JCMD engine;

    private final String NAME = "version";
    private final String DESCRIPTION = "Shows the JCMD version.";
    private final String CATEGORY = "Base";

    public Version(JCMD engine) {
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
        System.out.println("Current JCMD version: " + engine.getJcmdVersion());
    }
}
