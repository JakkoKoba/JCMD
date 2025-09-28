package org.jcmd.commands;

import org.jcmd.core.Command;
import org.jcmd.core.JCMD;

public class Env implements Command {
    protected final JCMD engine;

    private final String NAME = "env";
    private final String DESCRIPTION = "Get the current environment variables.";
    private final String CATEGORY = "Core";

    public Env(JCMD engine) {
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
        if (args.length == 0) {
            System.out.println("Java Version: " + engine.JAVA_VERSION);
            System.out.println("Maven Version: " + engine.MAVEN_VERSION);
        } else {
            System.err.println("Invalid arguments. Usage: env");
        }
    }
}