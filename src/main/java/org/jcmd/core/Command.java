package org.jcmd.core;

public interface Command {
    String getName();
    String getDescription();
    String getCategory();
    void execute(String[] args);
}
