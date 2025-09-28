package org.jcmd.core;

import java.time.LocalTime;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Variables {

    // Formatting and Prefix Variables
    public static String TIME_FORMAT = "HH:mm:ss";
    public static String DATE_FORMAT = "yyyy-MM-dd";
    public static String VAR_PREFIX = "${ }"; // Prefix must include space

    // Command Registration Flags
    public static String COMMAND_REGISTER_FLAG = "-reg";
    public static String COMMAND_UNREGISTER_FLAG = "-unreg";

    // Local Variables
    public final static String USER_NAME = System.getProperty("user.name");
    public final static String OS_NAME = System.getProperty("os.name");
    public final static String JAVA_VERSION = System.getProperty("java.version");
    public final static String JCMD_VERSION = BuildInfo.JCMD_VERSION;
    public final static String MAVEN_VERSION = BuildInfo.MAVEN_VERSION;

    public String time(String[] args) {
        String time = "";
        String timeFormat = TIME_FORMAT;
        
        if (args.length > 0) {
            // Join all arguments into one string separated by spaces
            timeFormat = String.join(" ", args);
        }
        try {
            time = LocalTime.now().format(DateTimeFormatter.ofPattern(timeFormat));
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid time format pattern: " + timeFormat);
        }
        return time;
    }

    public String date(String[] args) {
        String date = "";
        String dateFormat = DATE_FORMAT;

        if (args.length > 0) {
            // Join all arguments into one string separated by spaces
            dateFormat = String.join(" ", args);
        }
        try {
            date = LocalDate.now().format(DateTimeFormatter.ofPattern(DATE_FORMAT));
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid time format pattern: " + dateFormat);
        }
        return date;
    }
}

