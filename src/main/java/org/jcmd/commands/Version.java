package org.jcmd.commands;

import org.jcmd.core.*;

public class Version implements Command {
    private final JCMD engine;

    public Version(JCMD engine) {
        this.engine = engine;
    }

    @Override
    public String getName() { return "version"; }

    @Override
        public String getDescription() { return "Shows the JCMD version."; }

    @Override
    public void execute(String[] args) {
        System.out.println("Current JCMD version: " + engine.getJcmdVersion());
    }
}
