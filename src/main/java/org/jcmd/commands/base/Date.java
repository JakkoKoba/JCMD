package org.jcmd.commands.base;

import org.jcmd.core.CommandInterface;
import org.jcmd.core.Variables;
import org.jquill.Debug;
import static org.jcmd.core.IO.out;

public class Date implements CommandInterface {

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
        String format = Variables.DATE_FORMAT;
        if (args.length > 0) {
            // Join all arguments into one string separated by spaces
            format = String.join(" ", args);
        }
        try {
            String date = java.time.LocalDate.now().format(java.time.format.DateTimeFormatter.ofPattern(format));
            out.println(date);
        } catch (IllegalArgumentException e) {
            Debug.warn("Invalid date format pattern: " + format);
        }
    }
}