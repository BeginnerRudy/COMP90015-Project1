package DictionaryClient;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

public class DictionaryClient {
    int port;
    String address;

    public DictionaryClient(String address, int port) {
        this.address = address;
        this.port = port;
    }

    public void doSomething() {
        try {
            // connect to the server
            while (true) {
                Socket socket = new Socket(this.address, this.port);
                // get reply
                InputStream reply = socket.getInputStream();
                DataInputStream dis = new DataInputStream(reply);
                String reply_msg = dis.readUTF();
                // show it
                System.out.println(reply_msg);
                // Close connect
                dis.close();
                reply.close();
                socket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args){
        DictionaryClient client = new DictionaryClient("localhost", 5000);
        ClientGUI clientGUI = new ClientGUI("Multi-Threading Dictionary Client");
        clientGUI.setVisible(true);
        client.doSomething();
    }
}
