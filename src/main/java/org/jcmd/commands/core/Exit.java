package org.jcmd.commands.core;

import org.jcmd.core.CommandInterface;
import org.jcmd.core.JCMD;
import static org.jcmd.core.IO.out;

public class Exit implements CommandInterface {
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
        out.println("Exiting JCMD...");
        engine.stop();
    }
}