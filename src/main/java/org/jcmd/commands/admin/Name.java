package org.jcmd.commands.admin;

import org.jcmd.core.Command;
import org.jcmd.core.JCMD;

public class Name implements Command {
    protected final JCMD engine;

    private final String NAME = "name";
    private final String DESCRIPTION = "Print the project name.";
    private final String CATEGORY = "Admin";

    public Name(JCMD engine) {
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
        System.out.println("Project name: " + engine.PROJECT_NAME);
    }
}