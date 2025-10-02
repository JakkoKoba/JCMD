package org.jcmd.core;

public interface CommandTemplate {
    String getName();
    String getDescription();
    String getCategory();
    void execute(String[] args);
}
