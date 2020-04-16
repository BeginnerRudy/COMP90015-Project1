/*
 * @Author: Renjie Meng
 * @Student ID: 877396
 * @Date: 2020 April
 * */

package DictionaryClient;

import org.apache.commons.cli.*;
import org.json.simple.JSONObject;

/**
 * This class is responsible for all the functionality that a dictionary client should handle.
 * This is a singleton class.
 */
public class DictionaryClient {
    private static DictionaryClient client = new DictionaryClient();

    // Below are message exchange protocol's constant, which is agreed with the server side.
    public static final String ADD_METHOD = "A";
    public static final String DELETE_METHOD = "D";
    public static final String SEARCH_METHOD = "S";
    public static final String SUCCESS_ADD = "201";
    public static final String SUCCESS_DELETE = "202";
    public static final String SUCCESS_SEARCH = "203";
    public static final String FAILURE_CODE = "404";
    public static final String REQUEST_HEADER = "request_method";
    public static final String WORD_KEY = "word";
    public static final String MEANING_KEY = "meaning";
    public static final String RESPONSE_CODE_KEY = "response_code";
    public static final String RESPONSE_MESSAGE_KEY = "response_message";

    private ClientConnection connection;

    /**
     * This is the singleton getter method.
     *
     * @return The singleton instance
     */
    public static DictionaryClient getClient() {
        return DictionaryClient.client;
    }

    /**
     * @param address The server IP address
     * @param port    The server port
     */
    public void init(String address, int port) {
        // connect to the server
        this.connection = new ClientConnection();
        this.connection.init(address, port);
    }

    /**
     * @return The Connection object represents connection between server and client.
     */
    public ClientConnection getConnection() {
        return connection;
    }

    /**
     * This method disconnect the client and the server
     */
    public void disconnect(){
        this.connection.disconnect();
    }

    /**
     * @param word The word to be added.
     * @return The response message.
     * <p>
     * This method handles the add function both the success and failure cases.
     */
    public void add(String word, String meaning) {
        JSONObject add_request = new JSONObject();
        // construct the add request
        add_request.put(WORD_KEY, word.toLowerCase()); // lower casing the word, for better match
        add_request.put(MEANING_KEY, meaning);
        add_request.put(REQUEST_HEADER, ADD_METHOD);
        this.connection.sendRequest(add_request);
    }


    /**
     * @param word The word to be deleted.
     * @return The response message.
     * <p>
     * This method handles the delete function both the success and failure cases.
     */
    public void delete(String word) {
        JSONObject delete_request = new JSONObject();
        // construct the add request
        delete_request.put(REQUEST_HEADER, DELETE_METHOD);
        delete_request.put(WORD_KEY, word.toLowerCase()); // lower casing the word, for better match
        // send request to the server, reconnect if necessary
        this.connection.sendRequest(delete_request);
    }

    /**
     * @param word The word to be searched for.
     * @return The response message.
     * <p>
     * This method handles the search function both the success and failure cases.
     */
    public void search(String word) {
        JSONObject search_request = new JSONObject();
        // construct the add request
        search_request.put(REQUEST_HEADER, SEARCH_METHOD);
        search_request.put(WORD_KEY, word.toLowerCase());// lower casing the word, for better match
        // send request to the server
        this.connection.sendRequest(search_request);
    }

    /**
     * @param args The commandline args
     *             This is the main method of the Dictionary client, controls the execution flow of the client.
     */
    public static void main(String[] args) {
        //  setup command line
        Options options = new Options();
        Utility.addOption(options, "a", "address", true, "Server address");
        Utility.addOption(options, "p", "port", true, "Server port");

        CommandLineParser parser = new DefaultParser();
        HelpFormatter formatter = new HelpFormatter();
        try {
            CommandLine cmd = parser.parse(options, args);
            // parse command line args
            String address = cmd.getOptionValue("address");
            int port = Integer.parseInt(cmd.getOptionValue("port"));

            // setup the client
            ClientGUI clientGUI = new ClientGUI("Multi-Threading Dictionary Client");
            clientGUI.setVisible(true);
            ClientController.getClientController().init(clientGUI);
            DictionaryClient.getClient().init(address, port);

        } catch (ParseException e) {
            Utility.printClientMsg("Client", "Client fail to start.");
            Utility.printClientMsg("Client", e.getMessage());
            formatter.printHelp("utility-name", options);
            System.exit(1);
        }
    }

}
