package DictionaryClient;

import java.io.*;
import java.net.*;

public class DictionaryClient {
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

    public void connectToServer() throws IOException {
        this.socket = new Socket(this.address, this.port);
        this.outputStream = socket.getOutputStream();
        this.dos = new DataOutputStream(this.outputStream);
        this.inputStream = socket.getInputStream();
        this.dis = new DataInputStream(this.inputStream);
    }

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


    public static void main(String[] args) {
        DictionaryClient client = new DictionaryClient("localhost", 5000);
        ClientController clientController = new ClientController(client);
        ClientGUI clientGUI = new ClientGUI("Multi-Threading Dictionary Client", clientController);
        clientGUI.setVisible(true);
        clientController.setClientGUI(clientGUI);
    }
}
