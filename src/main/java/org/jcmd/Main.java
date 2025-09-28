package org.jcmd;

import org.jcmd.core.JCMD;

public class Main {

    final static JCMD engine = new JCMD();

    public static void init() {
        engine.registerCoreCommands();
        engine.registerBaseCommands();
    }

    public static void main(String[] args) {
        init();
        engine.run();
    }
}