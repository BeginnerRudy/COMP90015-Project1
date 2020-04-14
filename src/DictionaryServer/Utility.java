package DictionaryServer;

public class Utility {
    /**
     * @param msg The message to be print out
     *            This method aims to provide a consistent format of server message, every class
     *            which wants to send output should use this method.
     */
    public static void printServerMsg(String header, String msg) {
        System.out.println(header + ": " +msg);
    }

    public static void printServerExceptionMsg(String messageHeader, String exceptionType, String msg) {
        System.out.println(messageHeader + "(" + exceptionType + ")" + ": "  + msg);
    }
}
