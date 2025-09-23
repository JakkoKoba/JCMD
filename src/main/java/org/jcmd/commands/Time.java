package org.jcmd.commands;

import org.jcmd.core.Command;

public class Time implements Command {

    private final String NAME = "time";
    private final String DESCRIPTION = "Displays the current system time.";
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
        String format = "HH:mm:ss";
        if (args.length > 0) {
            // Join all arguments into one string separated by spaces
            format = String.join(" ", args);
        }
        try {
            String time = java.time.LocalTime.now().format(java.time.format.DateTimeFormatter.ofPattern(format));
            System.out.println(time);
        } catch (IllegalArgumentException e) {
            System.err.println("Invalid time format pattern: \"" + format + "\"");
        }
    }
}
