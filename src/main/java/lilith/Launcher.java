package lilith;

/**
 * Launcher class to start either GUI or CLI.
 */
public class Launcher {
    public static void main(String[] args) {
        if (args.length > 0 && args[0].equalsIgnoreCase("cli")) {
            LilithCli.main(args);
        } else {
            Lilith.main(args);
        }
    }
}

