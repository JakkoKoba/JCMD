package org.jcmd.core;

public interface CommandInterface {
    String getName();
    String getDescription();
    String getCategory();
    void execute(String[] args);
}