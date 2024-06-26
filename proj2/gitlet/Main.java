package gitlet;

import java.io.IOException;

/**
 * Driver class for Gitlet, a subset of the Git version-control system.
 *
 * @author TODO
 */
public class Main {

    /**
     * Usage: java gitlet.Main ARGS, where ARGS contains
     * <COMMAND> <OPERAND1> <OPERAND2> ...
     */
    public static void main(String[] args)  {
        // the args is empty
        if (args.length == 0) {
            System.out.println("Please enter a command");
            System.exit(0);
        }
        String firstArg = args[0];
        switch (firstArg) {
            case "init":
                Repository.init();
                break;
            case "add":
                Repository.addCommand(args[1]);
                break;
            case "commit":
                Repository.commitCommand(args[1]);
                break;
            case "rm":
                Repository.rmCommand(args[1]);
                break;
            case "global-log":
                Repository.logCommand();
                break;
        }
    }
}
