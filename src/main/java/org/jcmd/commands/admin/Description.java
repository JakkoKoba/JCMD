package org.jcmd.commands.admin;

import org.jcmd.core.CommandInterface;
import org.jcmd.core.JCMD;
import static org.jcmd.core.IO.*;

public class Description implements CommandInterface {
    protected final JCMD engine;

    private final String NAME = "desc";
    private final String DESCRIPTION = "Print the project description.";
    private final String CATEGORY = "Admin";

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
        out.println("Project description: " + engine.PROJECT_DESCRIPTION);
    }
}