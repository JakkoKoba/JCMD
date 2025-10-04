package org.jcmd.commands.core;

import org.jcmd.core.Command;
import org.jcmd.core.JCMD;

public class Exit implements Command {
    private final JCMD engine;

    private final String NAME = "exit";
    private final String DESCRIPTION = "Exit JCMD.";
    private final String CATEGORY = "Core";

    public Exit(JCMD engine) {
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
        System.out.println("Exiting JCMD...");
        engine.stop();
    }
}