package org.jcmd.core;

public interface Command {
    String getName();
    String getDescription();
    void execute(String[] args);
}
