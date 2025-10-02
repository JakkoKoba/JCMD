package org.jcmd.core;

import java.time.LocalTime;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.UnsupportedTemporalTypeException;

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
        String timeFormat = (args.length > 0) ? String.join(" ", args) : "HH:mm:ss"; // default
        try {
            return LocalTime.now().format(DateTimeFormatter.ofPattern(timeFormat));
        } catch (UnsupportedTemporalTypeException e) {
            System.out.print("Invalid time format pattern for LocalTime: " + timeFormat);
        } catch (IllegalArgumentException e) {
            System.out.print("Invalid time format syntax: " + timeFormat);
        }
        return "";
    }

    public String date(String[] args) {
        String dateFormat = (args.length > 0) ? String.join(" ", args) : "yyyy-MM-dd"; // default
        try {
            return LocalDate.now().format(DateTimeFormatter.ofPattern(dateFormat));
        } catch (UnsupportedTemporalTypeException e) {
            System.out.print("Invalid date format pattern for LocalDate: " + dateFormat);
        } catch (IllegalArgumentException e) {
            System.out.print("Invalid date format syntax: " + dateFormat);
        }
        return "";
    }
}

