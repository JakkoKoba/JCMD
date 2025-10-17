package org.jcmd.commands.base;

import org.jcmd.core.CommandInterface;
import org.jcmd.core.Variables;
import org.jquill.Debug;
import static org.jcmd.core.IO.out;

public class Time implements CommandInterface {

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
        String format = Variables.TIME_FORMAT;
        if (args.length > 0) {
            // Join all arguments into one string separated by spaces
            format = String.join(" ", args);
        }
        try {
            String time = java.time.LocalTime.now().format(java.time.format.DateTimeFormatter.ofPattern(format));
            out.println(time);
        } catch (IllegalArgumentException e) {
            Debug.error("Invalid time format pattern: " + format);
        }
    }
}