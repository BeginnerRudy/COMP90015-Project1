package DictionaryClient;

import java.io.*;
import java.net.*;

/**
 * This class is responsible for all the functionality that a dictionary client should handle.
 */
public class DictionaryClient {
    // Below are message exchange protocol's constant, which is agreed with the server side.
    public static final String SEPARATOR = "$#";
    public static final String ADD_METHOD = "A";
    public static final String DELETE_METHOD = "D";
    public static final String SEARCH_METHOD = "S";
    public static final String SUCCESS_CODE = "1";
    public static final String FAILURE_CODE = "0";

    int port;
    String address;
    Socket socket;
    InputStream inputStream;
    DataInputStream dis;
    OutputStream outputStream;
    DataOutputStream dos;

    public DictionaryClient(String address, int port) {
        this.address = address;
        this.port = port;
    }

    /**
     * @throws IOException The IO exception
     * This method aims to connect to a server by the address and port.
     */
    public void connectToServer() throws IOException {
        this.socket = new Socket(this.address, this.port);
        this.outputStream = socket.getOutputStream();
        this.dos = new DataOutputStream(this.outputStream);
        this.inputStream = socket.getInputStream();
        this.dis = new DataInputStream(this.inputStream);
    }

    /**
     * This method aims to close all the IO stuff's safely.
     */
    public void tearDown() {
        try {
            this.inputStream.close();
            this.dis.close();
            this.outputStream.close();
            this.dos.close();
            this.socket.close();
        } catch (IOException e) {

        }
    }

    /**
     * @param word The word to be added.
     * @param meaning The meaning of the word to be added.
     * @return The response message.
     *
     * This method handles the add function both the success and failure cases.
     */
    public String add(String word, String meaning) {
        System.out.println("word: " + word);
        System.out.println("meaning: " + meaning);

        if (word.strip() != "" && meaning.strip() != "") {

            try {
                this.connectToServer();
                // send request to the server
                this.dos.writeUTF(ADD_METHOD + word + this.SEPARATOR + meaning);
                String reply = this.dis.readUTF();
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
                System.out.println(FAILURE_CODE + "Failed to add a word to the server");
                return FAILURE_CODE + "Failed to add a word to the server";
            }

        }
        return FAILURE_CODE + "Please enter both word and meaning.";
    }


    /**
     * @param word The word to be deleted.
     * @return The response message.
     *
     * This method handles the delete function both the success and failure cases.
     */
    public String delete(String word) {
        if (word.strip() != "") {
            try {
                this.connectToServer();
                // send request to the server
                this.dos.writeUTF(DELETE_METHOD + word);
                String reply = this.dis.readUTF();
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
     *
     * This method handles the search function both the success and failure cases.
     */
    public String search(String word) {
        if (word.strip() != "") {
            try {
                this.connectToServer();
                // send request to the server
                this.dos.writeUTF(SEARCH_METHOD + word);
                String reply = this.dis.readUTF();
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
     * This is the main method of the Dictionary client, controls the execution flow of the client.
     */
    public static void main(String[] args) {
        DictionaryClient client = new DictionaryClient("localhost", 5000);
        ClientController clientController = new ClientController(client);
        ClientGUI clientGUI = new ClientGUI("Multi-Threading Dictionary Client", clientController);
        clientGUI.setVisible(true);
        clientController.setClientGUI(clientGUI);
    }
}
