package org.jcmd.core;

import java.util.Map;

public class PackageReg {

    public final Map<String, String> names = Map.of(
            "admin", "org.jcmd.commands.admin",
            "base", "org.jcmd.commands.base",
            "core", "org.jcmd.commands.core"
    );

    // Registry of available packages and their commands
    public final Map<String, String[]> registry = Map.of(
            "admin", new String[] {
                    "Cmd", "Description", "Env", "Name", "Version"
            },
            "base", new String[]{
                    "Alias", "Date", "Echo", "Time"
            },
            "core", new String[]{
                    "Exit", "Help", "List"
            }
    );
}