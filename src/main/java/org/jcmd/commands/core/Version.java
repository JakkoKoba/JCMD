package org.jcmd.commands.core;

import org.jcmd.core.Command;
import org.jcmd.core.JCMD;

public class Version implements Command {
    private final JCMD engine;

    private final String NAME = "version";
    private final String DESCRIPTION = "Shows the current version of JCMD, Java, and Maven.";
    private final String CATEGORY = "Core";

    public Version(JCMD engine) {
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
        System.out.println("Java Version: " + engine.JAVA_VERSION);
        System.out.println("Maven Version: " + engine.MAVEN_VERSION);
        System.out.println("Current JCMD version: " + engine.JCMD_VERSION);
    }
}
