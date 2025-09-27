package org.jcmd.commands;

import org.jcmd.core.Command;
import org.jcmd.core.JCMD;

public class Description implements Command {
    protected final JCMD engine;

    private final String NAME = "desc";
    private final String DESCRIPTION = "Print the project description.";
    private final String CATEGORY = "Base";

    public Description(JCMD engine) {
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
        System.out.println("Project description: " + engine.PROJECT_DESCRIPTION);
    }
}