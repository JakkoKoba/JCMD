package org.jcmd.commands;

import org.jcmd.core.*;

public class Echo implements Command {

    private final String NAME = "echo";
    private final String DESCRIPTION = "Prints back the input text.";
    private final String CATEGORY = "Base";

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
            System.out.println();
        } else {
            System.out.println(String.join(" ", args));
        }
    }
}
