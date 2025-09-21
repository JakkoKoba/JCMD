import org.jcmd.core.*;
import org.jcmd.commands.*;

public class Main {
    public static JCMD engine = new JCMD();

    public static void main(String[] args) {
        engine.registerBase();
        engine.run();
    }
}
