package org.jcmd.core;

import java.time.LocalTime;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.UnsupportedTemporalTypeException;
import java.util.ArrayList;
import java.util.List;

public class Variables {

    // Formatting and Prefix Variables
    public static String TIME_FORMAT = "HH:mm:ss";
    public static String DATE_FORMAT = "yyyy-MM-dd";
    public static String VAR_PREFIX = "${ }"; // Prefix must include space
    public static String NEW_COMMAND_PREFIX = "||";

    // CommandInterface Registration Flags
    public static String REGISTER_FLAG = "-reg";
    public static String UNREGISTER_FLAG = "-unreg";

    // Local Variables
    public final static String USER_NAME = System.getProperty("user.name");

    public String time(String[] args) {
        String timeFormat = (args.length > 0) ? String.join(" ", args) : "HH:mm:ss"; // default
        try {
            return LocalTime.now().format(DateTimeFormatter.ofPattern(timeFormat));
        } catch (UnsupportedTemporalTypeException e) {
            return "[Invalid date format pattern for LocalDate: " + timeFormat + "]";
        } catch (IllegalArgumentException e) {
            return "[Invalid date format syntax: " + timeFormat + "]";
        }
    }
    public String date(String[] args) {
        String dateFormat = (args.length > 0) ? String.join(" ", args) : "yyyy-MM-dd"; // default
        try {
            return LocalDate.now().format(DateTimeFormatter.ofPattern(dateFormat));
        } catch (UnsupportedTemporalTypeException e) {
            return "[Invalid date format pattern for LocalDate: " + dateFormat + "]";
        } catch (IllegalArgumentException e) {
            return "[Invalid date format syntax: " + dateFormat + "]";
        }
    }
    public String user(String[] args) {
        return USER_NAME;
    }
}