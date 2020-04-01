package DictionaryClient;

import java.io.*;
import java.net.*;

public class DictionaryClient {
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

    public void connectToServer() {
        try {
            this.socket = new Socket(this.address, this.port);
            this.outputStream = socket.getOutputStream();
            this.dos = new DataOutputStream(this.outputStream);
            this.inputStream = socket.getInputStream();
            this.dis = new DataInputStream(this.inputStream);

        } catch (IOException e) {
            System.out.println("Failed to connect to the server, please try later.");
        }
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

    public String add(String request) {
        try {
            this.connectToServer();
            // send request to the server
            this.dos.writeUTF("A" + request);
            String reply = this.dis.readUTF();
            System.out.println(reply);
            this.tearDown();
            return reply;
        } catch (IOException e) {
            e.printStackTrace();
//            System.out.println("Failed to add a word to the server");
            return "da";
        }

    }

    public void doSomething(String request) {
        try {
            // connect to the server
            while (true) {
                // show it
                dos.writeUTF("The user add a word");

                System.out.println("Add Button Clicked");
                // get reply
                String reply_msg = dis.readUTF();
                System.out.println(reply_msg);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        DictionaryClient client = new DictionaryClient("localhost", 5000);
        ClientController clientController = new ClientController(client);
        ClientGUI clientGUI = new ClientGUI("Multi-Threading Dictionary Client", clientController);
        clientGUI.setVisible(true);
        clientController.setClientGUI(clientGUI);
    }
}
