package org.jcmd.core;

import java.util.Map;

public class PackageReg {

    public final Map<String, String> names = Map.of(
            "core", "org.jcmd.commands.core",
            "base", "org.jcmd.commands.base"
    );

    // Registry of available packages and their commands
    public final Map<String, String[]> registry = Map.of(
            "core", new String[]{
                    "Alias", "Cmd", "Env", "Exit", "Help", "Version"
            },
            "base", new String[]{
                    "Date", "Description", "Echo", "Name", "Time"
            }
    );
}
