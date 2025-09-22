package org.jcmd;

import org.jcmd.core.JCMD;

public class Main {

    public static void main(String[] args) {
                final JCMD engine = new JCMD();
        engine.registerBase();
        engine.run();
    }
}
