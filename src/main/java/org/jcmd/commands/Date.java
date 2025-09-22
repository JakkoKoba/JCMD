package org.jcmd.commands;

import org.jcmd.core.Command;

public class Date implements Command {
    @Override
    public String getName() {
        return "date";
    }

    @Override
    public String getDescription() {
        return "Displays the current system date.";
    }

    @Override
    public String getCategory() {
        return "Base";
    }

    @Override
    public void execute(String[] args) {
        if (args.length > 1) {
            System.out.println("Error: Too many arguments. Usage: date [format]");
            return;
        }
        String format = "dd-MM-yyyy";
        if (args.length == 1) {
            format = args[0];
        }
        try {
            String date = java.time.LocalDate.now().format(java.time.format.DateTimeFormatter.ofPattern(format));
            System.out.println(date);
        } catch (IllegalArgumentException e) {
            System.err.println("Invalid date format pattern: " + format);
        }
    }
}
