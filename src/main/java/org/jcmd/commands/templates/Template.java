package org.jcmd.commands.templates;

import org.jcmd.core.CommandTemplate;
import org.jcmd.core.JCMD;

/**
 * Universal starting point for creating new commands.
 * Copy this file into `org.jcmd.commands` and rename the class.
 */

public class Template implements CommandTemplate {
    protected final JCMD engine;

    private final String NAME = "template";
    private final String DESCRIPTION = "Template command - replace with your own description.";
    private final String CATEGORY = "Template";

    public Template(JCMD engine) {
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
        System.out.println("This is a template command. Replace with your own implementation.");
    }
}