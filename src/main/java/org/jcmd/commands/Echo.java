package org.jcmd.commands;

import org.jcmd.core.Command;

public class Echo implements Command {
    @Override
    public String getName() {
        return "echo";
    }

    @Override
    public String getDescription() {
        return "Prints back the input text.";
    }

    @Override
    public String getCategory() {
        return "Base";
    }

    @Override
    public void execute(String[] args) {
        if (args.length == 0) {
            System.out.println(); // prints a blank line if no arguments
        } else {
            System.out.println(String.join(" ", args));
        }
    }
}
