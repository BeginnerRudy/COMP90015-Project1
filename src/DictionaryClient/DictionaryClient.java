/*
 * @Author: Renjie Meng
 * @Student ID: 877396
 * */

package DictionaryClient;

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
            ClientController.getClientController().setGUISystemMsg("Connected ");
        } catch (UnknownHostException e) {
            System.out.println(FAILURE_CODE + "Failed to connect to the server: " + e.getMessage() + " is unknown.");
            e.printStackTrace();

        } catch (ConnectException e) {
            System.out.println("Failed to connect to the server: " + e.getMessage());
            e.printStackTrace();
            ClientController.getClientController().setGUISystemMsg("Server is unavailable now, try later.");

        } catch (IOException e) {
            System.out.println("Failed to add a word to the server");
            e.printStackTrace();
        }
    }

    /**
     * @throws IOException The IO exception.
     *                     <p>
     *                     This method aims to connect to a server by the address and port.
     */
    private void connectToServer() throws IOException {
        this.socket = new Socket(this.address, this.port);
        this.socket.setSoTimeout(3000);
        this.writer = new ObjectOutputStream(this.socket.getOutputStream());
        this.reader = new ObjectInputStream(this.socket.getInputStream());
        this.readingThread = new ReadingThread(this.reader);
        this.readingThread.start();
        ClientController.getClientController().setGUISystemMsg("Connected ");
    }

    private void reconnectToWrite(JSONObject request) throws IOException {
        // send request to the server, reconnect if necessary
        try {
            this.writer.writeObject(request);
        } catch (IOException e) {
            System.out.println("reconnect to server.");
            ClientController.getClientController().setGUISystemMsg("Reconnecting ... ");
            this.connectToServer();
            this.writer.writeObject(request);
        } catch (NullPointerException e){
            // when the client failed to connect for the first time, then writer would be null
            System.out.println("reconnect to server.");
            ClientController.getClientController().setGUISystemMsg("Reconnecting ... ");
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
                ClientController.getClientController().setGUISystemMsg("NOTHING TO DISCONNECT.");
            }

        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Teardown error");
        } catch (NullPointerException e){
            ClientController.getClientController().setGUISystemMsg("NOTHING TO DISCONNECT.");
        }
    }


    /**
     * @param word The word to be added.
     * @return The response message.
     * <p>
     * This method handles the add function both the success and failure cases.
     */
    public void add(String word, String meaning) {
        // If the input is not valid, notify the user this error.
        if (word.strip() != "" && meaning.strip() != "") {

            try {
                JSONObject add_request = new JSONObject();
                // construct the add request
                add_request.put(WORD_KEY, word.toLowerCase()); // lower casing the word, for better match
                add_request.put(MEANING_KEY, meaning);
                add_request.put(REQUEST_HEADER, ADD_METHOD);

                // send request to the server, reconnect if necessary
                reconnectToWrite(add_request);
                // handle failure cases on the client side
            } catch (UnknownHostException e) {
                System.out.println(FAILURE_CODE + "Failed to connect to the server: " + e.getMessage() + " is unknown.");
                // construct the failure reply
                JSONObject reply = new JSONObject();
                reply.put(RESPONSE_CODE_KEY, FAILURE_CODE);
                reply.put(RESPONSE_MESSAGE_KEY, e.getMessage() + " is unknown.");
                e.printStackTrace();
            } catch (ConnectException e) {
                // construct the failure reply
                JSONObject reply = new JSONObject();
                reply.put(RESPONSE_CODE_KEY, FAILURE_CODE);
                reply.put(RESPONSE_MESSAGE_KEY, "Failed to connect to the server: " + e.getMessage());                ClientController.getClientController().setGUISystemMsg("Server is unavailable now, try later.");
                ClientController.getClientController().setGUISystemMsg("Server is unavailable now, try later.");
                e.printStackTrace();
                System.out.println();
            } catch (IOException e) {
                // construct the failure reply
                JSONObject reply = new JSONObject();
                reply.put(RESPONSE_CODE_KEY, FAILURE_CODE);
                reply.put(RESPONSE_MESSAGE_KEY, "Failed to add a word to the server");
                e.printStackTrace();
            }

        }

        // construct the failure reply
        JSONObject reply = new JSONObject();
        reply.put(RESPONSE_CODE_KEY, FAILURE_CODE);
        reply.put(RESPONSE_MESSAGE_KEY, "Please enter both word and meaning.");
    }


    /**
     * @param word The word to be deleted.
     * @return The response message.
     * <p>
     * This method handles the delete function both the success and failure cases.
     */
    public void delete(String word) {
        if (word.strip() != "") {
            try {
                JSONObject delete_request = new JSONObject();
                // construct the add request
                delete_request.put(REQUEST_HEADER, DELETE_METHOD);
                delete_request.put(WORD_KEY, word.toLowerCase()); // lower casing the word, for better match
                // send request to the server, reconnect if necessary
                reconnectToWrite(delete_request);
                // handle failure cases on the client side
            } catch (UnknownHostException e) {
                System.out.println(FAILURE_CODE + "Failed to connect to the server: " + e.getMessage() + " is unknown.");
                // construct the failure reply
                JSONObject reply = new JSONObject();
                reply.put(RESPONSE_CODE_KEY, FAILURE_CODE);
                reply.put(RESPONSE_MESSAGE_KEY, e.getMessage() + " is unknown.");
                e.printStackTrace();
            } catch (ConnectException e) {
                // construct the failure reply
                JSONObject reply = new JSONObject();
                reply.put(RESPONSE_CODE_KEY, FAILURE_CODE);
                reply.put(RESPONSE_MESSAGE_KEY, "Failed to connect to the server: " + e.getMessage());
                ClientController.getClientController().setGUISystemMsg("Server is unavailable now, try later.");
                e.printStackTrace();
            } catch (IOException e) {
                // construct the failure reply
                JSONObject reply = new JSONObject();
                reply.put(RESPONSE_CODE_KEY, FAILURE_CODE);
                reply.put(RESPONSE_MESSAGE_KEY, "Failed to delete a word from the server, due to client IO side exception");
            }
        }
        // construct the failure reply
        JSONObject reply = new JSONObject();
        reply.put(RESPONSE_CODE_KEY, FAILURE_CODE);
        reply.put(RESPONSE_MESSAGE_KEY, "Please enter non-empty word");
    }

    /**
     * @param word The word to be searched for.
     * @return The response message.
     * <p>
     * This method handles the search function both the success and failure cases.
     */
    public void search(String word) {
        if (!word.strip().equals("")) {
            try {
                JSONObject search_request = new JSONObject();
                // construct the add request
                search_request.put(REQUEST_HEADER, SEARCH_METHOD);
                search_request.put(WORD_KEY, word.toLowerCase());// lower casing the word, for better match

                // send request to the server
                reconnectToWrite(search_request);
                // handle failure cases on the client side
            } catch (UnknownHostException e) {
                // construct the failure reply
                JSONObject reply = new JSONObject();
                reply.put(RESPONSE_CODE_KEY, FAILURE_CODE);
                reply.put(RESPONSE_MESSAGE_KEY, "Failed to connect to the server: " + e.getMessage() + " is unknown.");
            } catch (ConnectException e) {
                // construct the failure reply
                JSONObject reply = new JSONObject();
                reply.put(RESPONSE_CODE_KEY, FAILURE_CODE);
                reply.put(RESPONSE_MESSAGE_KEY, "Failed to connect to the server: " + e.getMessage());
                e.printStackTrace();
                System.out.println(RESPONSE_MESSAGE_KEY + "Failed to connect to the server: " + e.getMessage());
                ClientController.getClientController().setGUISystemMsg("Server is unavailable now, try later.");
            } catch (IOException e) {
                // construct the failure reply
                JSONObject reply = new JSONObject();
                reply.put(RESPONSE_CODE_KEY, FAILURE_CODE);
                reply.put(RESPONSE_MESSAGE_KEY, "Failed to search a word from the server due to client side IO exception");
                e.printStackTrace();
            }
        }
        // construct the failure reply
        JSONObject reply = new JSONObject();
        reply.put(RESPONSE_CODE_KEY, FAILURE_CODE);
        reply.put(RESPONSE_MESSAGE_KEY, "Please enter non-empty word");

    }


    /**
     * @param args The commandline args
     *             This is the main method of the Dictionary client, controls the execution flow of the client.
     */
    public static void main(String[] args) {
        ClientGUI clientGUI = new ClientGUI("Multi-Threading Dictionary Client");
        clientGUI.setVisible(true);
        ClientController clientController = ClientController.getClientController();
        clientController.setClientGUI(clientGUI);
        DictionaryClient client = new DictionaryClient("localhost", 5000);
        clientController.setDictionaryClient(client);
    }
}
