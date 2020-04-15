/*
 * @Author: Renjie Meng
 * @Student ID: 877396
 * @Date: 2020 April
 * */

package DictionaryServer;

import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;

public class Utility {
    /**
     * @param msg The message to be print out
     *            This method aims to provide a consistent format of server message, every class
     *            which wants to send output should use this method.
     */
    public static void printServerMsg(String header, String msg) {
        System.out.println(header + ": " + msg);
    }

    /**
     * This method provides a consistent way to show message.
     *
     * @param messageHeader The message hea
     * @param exceptionType The message code
     * @param msg           The message body
     */
    public static void printServerExceptionMsg(String messageHeader, String exceptionType, String msg) {
        System.out.println(messageHeader + "(" + exceptionType + ")" + ": " + msg);
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
