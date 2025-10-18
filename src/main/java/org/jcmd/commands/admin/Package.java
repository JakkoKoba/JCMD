package org.jcmd.commands.admin;

import org.jcmd.core.CommandInterface;
import org.jcmd.core.JCMD;
import org.jcmd.core.Variables;
import org.jquill.Debug;

import java.util.Objects;

public class Package implements CommandInterface {
    private final JCMD engine;

    private static final String NAME = "package";
    private static final String DESCRIPTION = "Register and unregister packages.";
    private static final String CATEGORY = "Admin";

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

    private void warn() {
        Debug.warn("Unknown usage of '" + NAME + "'");
        Debug.info(NAME + " " + Variables.REGISTER_FLAG + " <package>");
        Debug.info(NAME + " " + Variables.UNREGISTER_FLAG + " <package>");
    }

    @Override
    public void execute(String[] args) {
        if (args == null || args.length < 2) {
            warn();
            return;
        }

        String flag = args[0];
        String packageName = args[1].toLowerCase();

        boolean knownPackage = engine.pack.registry.containsKey(packageName);
        if (!knownPackage) {
            Debug.warn("Unknown package: " + packageName);
            return;
        }

        if (Objects.equals(flag, Variables.REGISTER_FLAG)) {
            engine.registerPackage(packageName, true);
            return;
        }

        if (Objects.equals(flag, Variables.UNREGISTER_FLAG)) {
            engine.unregisterPackage(packageName, true);
            return;
        }

        warn();
    }
}