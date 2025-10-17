package org.jcmd.commands.admin;

import org.jcmd.core.CommandInterface;
import org.jcmd.core.JCMD;
import org.jcmd.core.Variables;
import org.jquill.Debug;

public class Package implements CommandInterface {
    private final JCMD engine;

    private final String NAME = "package";
    private final String DESCRIPTION = "Register and unregister packages.";
    private final String CATEGORY = "Admin";

    public Package(JCMD engine) {
        this.engine = engine;
    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public String getDescription() {
        return DESCRIPTION;
    }

    @Override
    public String getCategory() {
        return CATEGORY;
    }

    @Override
    public void execute(String[] args) {
        String REGISTER_FLAG = Variables.REGISTER_FLAG;
        String UNREGISTER_FLAG = Variables.UNREGISTER_FLAG;

        String packageName = args[1];

        if (args.length == 2 && REGISTER_FLAG.equalsIgnoreCase(args[0])) {
            // If package doesn't exist in your registry, warn
            if (engine.pack.registry.get(packageName.toLowerCase()) == null) {
                Debug.warn("Unknown package: " + packageName);
                return;
            }
            if (engine.pack.registry.get(packageName.toLowerCase()) == null) {
                Debug.warn("Unknown package: " + packageName);
                return;
            }
        }

        engine.registerPackage(packageName);
    }
}