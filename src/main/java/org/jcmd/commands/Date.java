package org.jcmd.commands;

import org.jcmd.core.*;

public class Date implements Command {

    private final String NAME = "date";
    private final String DESCRIPTION = "Displays the current system date.";
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
        String format = "dd-MM-yyyy";
        if (args.length > 0) {
            // Join all arguments into one string separated by spaces
            format = String.join(" ", args);
        }
        try {
            String date = java.time.LocalDate.now().format(java.time.format.DateTimeFormatter.ofPattern(format));
            System.out.println(date);
        } catch (IllegalArgumentException e) {
            System.err.println("Invalid date format pattern: " + format);
        }
    }
}
