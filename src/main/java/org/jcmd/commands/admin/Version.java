package org.jcmd.commands.admin;

import org.jcmd.core.CommandInterface;
import org.jcmd.core.JCMD;
import static org.jcmd.core.IO.out;

public class Version implements CommandInterface {
    private final JCMD engine;

    private final String NAME = "version";
    private final String DESCRIPTION = "Shows the current version of JCMD, Java, and Maven.";
    private final String CATEGORY = "Admin";

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
        out.println("Java Version: " + engine.JAVA_VERSION);
        out.println("Maven Version: " + engine.MAVEN_VERSION);
        out.println("Current JCMD version: " + engine.JCMD_VERSION);
        out.println("Current JQuill version: " + engine.JQUILL_VERSION);
    }
}