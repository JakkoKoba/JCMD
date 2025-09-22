package org.jcmd.commands;

import org.jcmd.core.Command;

public class Time implements Command {
    @Override
    public String getName() {
        return "time";
    }

    @Override
    public String getDescription() {
        return "Displays the current system time.";
    }

    @Override
    public String getCategory() {
        return "Base";
    }

    @Override
        public void execute(String[] args) {
        if (args.length > 1) {
            System.err.println("Error: Too many arguments. Usage: time [format]");
            return;
        }
        String format = "HH:mm:ss";
        if (args.length == 1) {
            format = args[0];
        }
        try {
            String time = java.time.LocalTime.now().format(java.time.format.DateTimeFormatter.ofPattern(format));
            System.out.println(time);
        } catch (IllegalArgumentException e) {
            System.err.println("Invalid time format pattern: \"" + format + "\"");
        }
    }
}
