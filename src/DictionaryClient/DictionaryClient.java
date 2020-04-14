/*
 * @Author: Renjie Meng
 * @Student ID: 877396
 * */

package DictionaryClient;

import org.apache.commons.cli.*;
import org.json.simple.JSONObject;

import java.io.*;
import java.net.*;

/**
 * This class is responsible for all the functionality that a dictionary client should handle.
 */
public class DictionaryClient {
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

    private int port;
    private String address;
    private Socket socket;
    private ObjectOutputStream writer;
    private ObjectInputStream reader;
    private ReadingThread readingThread;

    public DictionaryClient(String address, int port) {
        this.address = address;
        this.port = port;

        try {
            this.connectToServer();
            ClientController.getClientController().setGUIConnectivity("Connected ");
        } catch (IOException e) {
            IOExceptionHandler(e);
        }
    }

    /**
     * @throws IOException The IO exception.
     *                     <p>
     *                     This method aims to connect to a server by the address and port.
     */
    private void connectToServer() throws IOException {
        this.socket = new Socket(this.address, this.port);
        this.writer = new ObjectOutputStream(this.socket.getOutputStream());
        this.reader = new ObjectInputStream(this.socket.getInputStream());
        this.readingThread = new ReadingThread(this.reader);
        this.readingThread.start();
        ClientController.getClientController().setGUIConnectivity("Connected ");
    }

    private void reconnectToWrite(JSONObject request) throws IOException {
        // send request to the server, reconnect if necessary
        try {
            this.writer.writeObject(request);
        } catch (IOException e) {
            System.out.println("reconnect to server.");
            ClientController.getClientController().setGUIConnectivity("Reconnecting ... ");
            this.connectToServer();
            this.writer.writeObject(request);
        } catch (NullPointerException e) {
            // when the client failed to connect for the first time, then writer would be null
            System.out.println("reconnect to server.");
            ClientController.getClientController().setGUIConnectivity("Reconnecting ... ");
            this.connectToServer();
            this.writer.writeObject(request);
        }
    }


    /**
     * This method aims to close all the IO stuff's safely.
     */
    public void disconnect() {
        try {
            if (!this.socket.isClosed()) {

                this.reader.close();
                this.writer.close();
                this.socket.close();
                System.out.println("The connection is closed now.");
            } else {
                System.out.println("Nothing to close, the client and server are disconnect.");
                ClientController.getClientController().setGUIConnectivity("NOTHING TO DISCONNECT.");
            }

        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Teardown error");
        } catch (NullPointerException e) {
            ClientController.getClientController().setGUIConnectivity("NOTHING TO DISCONNECT.");
        }
    }

    private void sendRequest(JSONObject request) {
        try {
            // send request to the server, reconnect if necessary
            reconnectToWrite(request);
        } catch (IOException e) {
            IOExceptionHandler(e);
        }
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
        this.sendRequest(add_request);
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
        this.sendRequest(delete_request);
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
        this.sendRequest(search_request);
    }

    /**
     * This method is resonsible for handling the IOException.
     * @param e The IOException object
     */
    private void IOExceptionHandler(IOException e) {
        if (e instanceof UnknownHostException) {
            // construct the failure reply
            System.out.println(RESPONSE_MESSAGE_KEY + "Failed to connect to the server: " + e.getMessage() + " is unknown.");
            ClientController.getClientController().setGUIConnectivity("Server addr is unknown.");
        } else if (e instanceof ConnectException) {
            System.out.println(RESPONSE_MESSAGE_KEY + "Failed to connect to the server: " + e.getMessage());
            ClientController.getClientController().setGUIConnectivity("Server is unavailable now, try later.");
        } else {
            // construct the failure reply
            System.out.println("Client side IO exception");
            ClientController.getClientController().setGUIConnectivity("Client side IO exception");
        }
    }

    private static void addOption(Options options, String opt, String longOpt, Boolean hasArg, String description) {
        Option input = new Option(opt, longOpt, hasArg, description);
        input.setRequired(true);
        options.addOption(input);
    }

    /**
     * @param args The commandline args
     *             This is the main method of the Dictionary client, controls the execution flow of the client.
     */
    public static void main(String[] args) {
        Options options = new Options();
        addOption(options, "a", "address", true, "Server address");
        addOption(options, "p", "port", true, "Server port");

        CommandLineParser parser = new DefaultParser();
        HelpFormatter formatter = new HelpFormatter();
        try {
            CommandLine cmd = parser.parse(options, args);
            // parse command line args

            String address = cmd.getOptionValue("address-path");
            int port = Integer.parseInt(cmd.getOptionValue("port"));

            ClientGUI clientGUI = new ClientGUI("Multi-Threading Dictionary Client");
            clientGUI.setVisible(true);
            ClientController clientController = ClientController.getClientController();
            clientController.setClientGUI(clientGUI);
            DictionaryClient client = new DictionaryClient(address, port);
            clientController.setDictionaryClient(client);

        } catch (ParseException e) {
            System.out.println("Client fail to start.");
            System.out.println(e.getMessage());
            formatter.printHelp("utility-name", options);

            System.exit(1);
        }
    }
}
