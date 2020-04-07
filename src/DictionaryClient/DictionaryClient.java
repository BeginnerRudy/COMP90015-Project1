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
    public static final String SUCCESS_CODE = "1";
    public static final String FAILURE_CODE = "0";
    public static final String REQUEST_HEADER = "request_method";
    public static final String WORD_KEY = "word";
    public static final String MEANING_KEY = "meaning";
    public static final String RESPONSE_CODE_KEY = "response_code";
    public static final String RESPONSE_MESSAGE_KEY = "response_message";

    int port;
    String address;
    Socket socket;
    ObjectOutputStream writer;
    ObjectInputStream reader;

    public DictionaryClient(String address, int port) {
        this.address = address;
        this.port = port;
    }

    /**
     * @throws IOException The IO exception.
     *                     <p>
     *                     This method aims to connect to a server by the address and port.
     */
    public void connectToServer() throws IOException {
        this.socket = new Socket(this.address, this.port);
        this.writer = new ObjectOutputStream(this.socket.getOutputStream());
        this.reader = new ObjectInputStream(this.socket.getInputStream());
        System.out.println("asdasdasd++++++++++++++++++++++++");
    }

    /**
     * This method aims to close all the IO stuff's safely.
     */
    public void tearDown() {
        try {
            this.reader.close();
            this.writer.close();
            this.socket.close();
        } catch (IOException e) {

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
                this.connectToServer();
                JSONObject add_request = new JSONObject();
                // construct the add request
                add_request.put(WORD_KEY, word);
                add_request.put(MEANING_KEY, meaning);
                add_request.put(REQUEST_HEADER, ADD_METHOD);

                // send request to the server
                this.writer.writeObject(add_request);
                this.reader = new ObjectInputStream(this.socket.getInputStream());
                JSONObject reply = (JSONObject) this.reader.readObject();
                this.tearDown();
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
            } catch (ConnectException e) {                // construct the failure reply
                JSONObject reply = new JSONObject();
                reply.put(RESPONSE_CODE_KEY, FAILURE_CODE);
                reply.put(RESPONSE_MESSAGE_KEY, "Failed to connect to the server: " + e.getMessage());
                e.printStackTrace();
                return reply;
            } catch (IOException e) {                // construct the failure reply
                JSONObject reply = new JSONObject();
                reply.put(RESPONSE_CODE_KEY, FAILURE_CODE);
                reply.put(RESPONSE_MESSAGE_KEY, "Failed to add a word to the server");
                e.printStackTrace();
                return reply;
            } catch (ClassNotFoundException e) {                // construct the failure reply
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
    public String delete(String word) {
        if (word.strip() != "") {
            try {
                this.connectToServer();
                // send request to the server
                this.writer.writeUTF(DELETE_METHOD + word);
                String reply = this.reader.readUTF();
                System.out.println(reply);
                this.tearDown();
                return reply;
                // handle failure cases on the client side
            } catch (UnknownHostException e) {
                System.out.println(FAILURE_CODE + "Failed to connect to the server: " + e.getMessage() + " is unknown.");
                return FAILURE_CODE + e.getMessage() + " is unknown.";
            } catch (ConnectException e) {
                System.out.println(FAILURE_CODE + "Failed to connect to the server: " + e.getMessage());
                return FAILURE_CODE + "Failed to connect to the server: " + e.getMessage();
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println(FAILURE_CODE + "Failed to delete a word from the server");
                return FAILURE_CODE + "Failed to delete a word from the server";
            }

        }
        return FAILURE_CODE + "Please enter non-empty word.";
    }

    /**
     * @param word The word to be searched for.
     * @return The response message.
     * <p>
     * This method handles the search function both the success and failure cases.
     */
    public String search(String word) {
        if (word.strip() != "") {
            try {
                this.connectToServer();
                // send request to the server
                this.writer.writeUTF(SEARCH_METHOD + word);
                String reply = this.reader.readUTF();
                System.out.println(reply);
                this.tearDown();
                return reply;
                // handle failure cases on the client side
            } catch (UnknownHostException e) {
                System.out.println(FAILURE_CODE + "Failed to connect to the server: " + e.getMessage() + " is unknown.");
                return FAILURE_CODE + e.getMessage() + " is unknown.";
            } catch (ConnectException e) {
                System.out.println(FAILURE_CODE + "Failed to connect to the server: " + e.getMessage());
                return FAILURE_CODE + "Failed to connect to the server: " + e.getMessage();
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println(FAILURE_CODE + "Failed to search a word from the server");
                return FAILURE_CODE + "Failed to search a word from the server, I/O Exception";
            }
        }
        return FAILURE_CODE + "Please enter non-empty word.";

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
