package org.jcmd.core;

import java.io.InputStream;
import java.io.IOException;
import java.util.Properties;

public class BuildInfo {

    private static final Properties props = new Properties();

    // Load properties from the build.properties file
    static {
        try (InputStream is = BuildInfo.class.getResourceAsStream("/build.properties")) {
            if (is != null) {
                props.load(is);
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to load configuration file '/build.properties'", e);
        }
    }

    public static final String MAVEN_VERSION = props.getProperty("maven.version", "unknown");
    public static final String JAVA_VERSION = props.getProperty("java.version", System.getProperty("java.version"));
    public static final String JCMD_VERSION = props.getProperty("jcmd.version", "unknown");

    public static final String PROJECT_NAME = props.getProperty("project.name", "JCMD");
    public static final String PROJECT_DESCRIPTION = props.getProperty("project.description", "A Java command line tool.");
}