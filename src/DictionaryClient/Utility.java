package DictionaryClient;

import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;

/**
 * This class provides tools for other classesgi
 */
public class Utility {
    public static void printClientMsg(String header, String msg){
        System.out.println(header + ": " + msg);
    }

    /**
     * This method is responsible for add option to options.
     *
     * @param options     The options objecy
     * @param opt         The short flag
     * @param longOpt     The long flag
     * @param hasArg
     * @param description The description of option
     */
    public static void addOption(Options options, String opt, String longOpt, Boolean hasArg, String description) {
        Option input = new Option(opt, longOpt, hasArg, description);
        input.setRequired(true);
        options.addOption(input);
    }

}
