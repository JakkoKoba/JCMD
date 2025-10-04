package org.jcmd;

import org.jcmd.core.JCMD;

public class Main {

    final static JCMD engine = new JCMD();

    public static void init() {
        engine.registerPackage("admin");
        engine.registerPackage("base");
        engine.registerPackage("core");
    }

    public static void main(String[] args) {
        init();
        engine.run();
    }
}