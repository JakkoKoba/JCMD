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
    public String getDescription() { return "Lists all available commands."; }

    @Override
    public void execute(String[] args) {
        System.out.println("Available commands:");
        for (Command c : engine.getCommands()) {
            System.out.printf("  %-10s : %s%n", c.getName(), c.getDescription());
        }
    }
}
