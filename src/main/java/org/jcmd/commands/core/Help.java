package org.jcmd.commands.core;

import org.jcmd.core.CommandInterface;
import org.jcmd.core.JCMD;
import org.jquill.Debug;
import static org.jcmd.core.IO.out;

public class Help implements CommandInterface {
    private final JCMD engine;

    private final String NAME = "help";
    private final String DESCRIPTION = "Get information about a specific command.";
    private final String CATEGORY = "Core";

    public Help(JCMD engine) {
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
            // No command specified
            out.println("Welcome to the JCMD Framework! \n" +
                    "- Type commands to perform actions.\n" +
                    "- Use 'list' to see what commands are available.\n" +
                    "- Use 'help <command>' to learn how a specific command works.\n" +
                    "- Use 'exit' to quit the program.\n" +
                    "\n" +
                    "Start by typing 'list' to discover commands you can try.");
        } else {
            String commandName = args[0];
            CommandInterface c;
            if (engine.getCommand(commandName) == null) {
                Debug.warn(commandName + " not found! Try again.");
                return;
            }
            c = engine.getCommand(commandName);
            if (c.getName() != null) {
                out.println(c.getName() + ": " + c.getCategory() + "\n" +
                        c.getDescription());
            }
        }
    }
}