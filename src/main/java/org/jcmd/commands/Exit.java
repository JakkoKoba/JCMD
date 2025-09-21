package org.jcmd.commands;

import org.jcmd.core.*;

public class Exit implements Command {
    private final JCMD engine;

    public Exit(JCMD engine) {
        this.engine = engine;
    }

    @Override
    public String getName() { return "exit"; }

    @Override
    public String getCategory() {
        return "Base";
    }

    @Override
    public String getDescription() { return "Exit JCMD."; }

    @Override
    public void execute(String[] args) {
        System.out.println("Exiting JCMD...");
        engine.stop();
    }
}
