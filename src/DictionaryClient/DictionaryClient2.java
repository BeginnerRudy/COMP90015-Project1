/*
 * @Author: Renjie Meng
 * @Student ID: 877396
 * */

package DictionaryClient;

import org.json.simple.JSONObject;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ConnectException;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * This class is responsible for all the functionality that a dictionary client should handle.
 */
public class DictionaryClient2 {
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

    public DictionaryClient2(String address, int port) {
        this.address = address;
        this.port = port;


        try {
            connectToServer();
        } catch (UnknownHostException e) {
            System.out.println(FAILURE_CODE + "Failed to connect to the server: " + e.getMessage() + " is unknown.");
            e.printStackTrace();
        } catch (ConnectException e) {
            System.out.println("Failed to connect to the server: " + e.getMessage());
            e.printStackTrace();
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
        this.writer = new ObjectOutputStream(this.socket.getOutputStream());
        this.reader = new ObjectInputStream(this.socket.getInputStream());
    }

    private void reconnectToWrite(JSONObject request) throws IOException {
        // send request to the server, reconnect if necessary
        try {
            this.writer.writeObject(request);
        } catch (IOException e) {
            this.connectToServer();
            this.writer.writeObject(request);
        }
    }


    private JSONObject reconnectToRead() throws IOException, ClassNotFoundException {
        // send request to the server, reconnect if necessary
        try {
            return (JSONObject) this.reader.readObject();
        } catch (IOException e) {
            this.connectToServer();
            return (JSONObject) this.reader.readObject();
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
            }

        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Teardown error");
        }
    }


    /**
     * @param word The word to be added.
     * @return The response message.
     * <p>
     * This method handles the add function both the success and failure cases.
     */
    public JSONObject add(String word, String meaning) {
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

                // TODO socket exception: connection rest
                JSONObject reply = reconnectToRead();
                return reply;
                // handle failure cases on the client side
            } catch (UnknownHostException e) {
                System.out.println(FAILURE_CODE + "Failed to connect to the server: " + e.getMessage() + " is unknown.");
                // construct the failure reply
                JSONObject reply = new JSONObject();
                reply.put(RESPONSE_CODE_KEY, FAILURE_CODE);
                reply.put(RESPONSE_MESSAGE_KEY, e.getMessage() + " is unknown.");
                e.printStackTrace();
                return reply;
            } catch (ConnectException e) {
                // construct the failure reply
                JSONObject reply = new JSONObject();
                reply.put(RESPONSE_CODE_KEY, FAILURE_CODE);
                reply.put(RESPONSE_MESSAGE_KEY, "Failed to connect to the server: " + e.getMessage());
                e.printStackTrace();
                return reply;
            } catch (IOException e) {
                // construct the failure reply
                JSONObject reply = new JSONObject();
                reply.put(RESPONSE_CODE_KEY, FAILURE_CODE);
                reply.put(RESPONSE_MESSAGE_KEY, "Failed to add a word to the server");
                e.printStackTrace();
                return reply;
            } catch (ClassNotFoundException e) {
                // construct the failure reply
                JSONObject reply = new JSONObject();
                reply.put(RESPONSE_CODE_KEY, FAILURE_CODE);
                reply.put(RESPONSE_MESSAGE_KEY, "ClassException error.");
                e.printStackTrace();
                return reply;

            }

        }

        // construct the failure reply
        JSONObject reply = new JSONObject();
        reply.put(RESPONSE_CODE_KEY, FAILURE_CODE);
        reply.put(RESPONSE_MESSAGE_KEY, "Please enter both word and meaning.");
        return reply;
    }


    /**
     * @param word The word to be deleted.
     * @return The response message.
     * <p>
     * This method handles the delete function both the success and failure cases.
     */
    public JSONObject delete(String word) {
        if (word.strip() != "") {
            try {
                JSONObject delete_request = new JSONObject();
                // construct the add request
                delete_request.put(REQUEST_HEADER, DELETE_METHOD);
                delete_request.put(WORD_KEY, word.toLowerCase()); // lower casing the word, for better match
                // send request to the server, reconnect if necessary
                reconnectToWrite(delete_request);
                JSONObject reply = reconnectToRead();
                return reply;
                // handle failure cases on the client side
            } catch (UnknownHostException e) {
                System.out.println(FAILURE_CODE + "Failed to connect to the server: " + e.getMessage() + " is unknown.");
                // construct the failure reply
                JSONObject reply = new JSONObject();
                reply.put(RESPONSE_CODE_KEY, FAILURE_CODE);
                reply.put(RESPONSE_MESSAGE_KEY, e.getMessage() + " is unknown.");
                e.printStackTrace();
                return reply;
            } catch (ConnectException e) {
                // construct the failure reply
                JSONObject reply = new JSONObject();
                reply.put(RESPONSE_CODE_KEY, FAILURE_CODE);
                reply.put(RESPONSE_MESSAGE_KEY, "Failed to connect to the server: " + e.getMessage());
                e.printStackTrace();
                return reply;
            } catch (IOException e) {
                // construct the failure reply
                JSONObject reply = new JSONObject();
                reply.put(RESPONSE_CODE_KEY, FAILURE_CODE);
                reply.put(RESPONSE_MESSAGE_KEY, "Failed to delete a word from the server, due to client IO side exception");
                return reply;
            } catch (ClassNotFoundException e) {
                // construct the failure reply
                JSONObject reply = new JSONObject();
                reply.put(RESPONSE_CODE_KEY, FAILURE_CODE);
                reply.put(RESPONSE_MESSAGE_KEY, "ClassException error.");
                e.printStackTrace();
                return reply;

            }
        }
        // construct the failure reply
        JSONObject reply = new JSONObject();
        reply.put(RESPONSE_CODE_KEY, FAILURE_CODE);
        reply.put(RESPONSE_MESSAGE_KEY, "Please enter non-empty word");
        return reply;
    }

    /**
     * @param word The word to be searched for.
     * @return The response message.
     * <p>
     * This method handles the search function both the success and failure cases.
     */
    public JSONObject search(String word) {
        if (!word.strip().equals("")) {
            try {
                JSONObject search_request = new JSONObject();
                // construct the add request
                search_request.put(REQUEST_HEADER, SEARCH_METHOD);
                search_request.put(WORD_KEY, word.toLowerCase());// lower casing the word, for better match

                // send request to the server
                reconnectToWrite(search_request);
                JSONObject reply = reconnectToRead();
                return reply;
                // handle failure cases on the client side
            } catch (UnknownHostException e) {
                // construct the failure reply
                JSONObject reply = new JSONObject();
                reply.put(RESPONSE_CODE_KEY, FAILURE_CODE);
                reply.put(RESPONSE_MESSAGE_KEY, "Failed to connect to the server: " + e.getMessage() + " is unknown.");
                return reply;
            } catch (ConnectException e) {
                // construct the failure reply
                JSONObject reply = new JSONObject();
                reply.put(RESPONSE_CODE_KEY, FAILURE_CODE);
                reply.put(RESPONSE_MESSAGE_KEY, "Failed to connect to the server: " + e.getMessage());
                return reply;
            } catch (IOException e) {
                // construct the failure reply
                JSONObject reply = new JSONObject();
                reply.put(RESPONSE_CODE_KEY, FAILURE_CODE);
                reply.put(RESPONSE_MESSAGE_KEY, "Failed to search a word from the server due to client side IO exception");
                e.printStackTrace();
                return reply;
            } catch (ClassNotFoundException e) {
                // construct the failure reply
                JSONObject reply = new JSONObject();
                reply.put(RESPONSE_CODE_KEY, FAILURE_CODE);
                reply.put(RESPONSE_MESSAGE_KEY, "ClassException error.");
                e.printStackTrace();
                return reply;

            }
        }
        // construct the failure reply
        JSONObject reply = new JSONObject();
        reply.put(RESPONSE_CODE_KEY, FAILURE_CODE);
        reply.put(RESPONSE_MESSAGE_KEY, "Please enter non-empty word");
        return reply;

    }


    /**
     * @param args The commandline args
     *             This is the main method of the Dictionary client, controls the execution flow of the client.
     */
    public static void main(String[] args) {
        DictionaryClient client = new DictionaryClient("localhost", 5000);
        ClientController clientController = new ClientController(client);
        ClientGUI clientGUI = new ClientGUI("Multi-Threading Dictionary Client", clientController);
        clientGUI.setVisible(true);
        clientController.setClientGUI(clientGUI);
    }
}
