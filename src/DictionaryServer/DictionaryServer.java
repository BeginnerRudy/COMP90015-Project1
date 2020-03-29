package DictionaryServer;

import com.sun.source.tree.WhileLoopTree;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.*;

public class DictionaryServer {
    int port;

    public DictionaryServer(int port) {
        this.port = port;
    }

    public void execute() {
        try {
            ServerSocket serverSocket = new ServerSocket(this.port);
            while (true) {
                Socket socket = serverSocket.accept();
                OutputStream outputStream = socket.getOutputStream();
                DataOutputStream dos = new DataOutputStream(outputStream);
                dos.writeUTF("Hi there.");
                dos.close();
                outputStream.close();
                socket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        // parse the command line
        int port = Integer.parseInt(args[0]);
        // create a server
        DictionaryServer server = new DictionaryServer(port);
        // execute the server
        server.execute();
    }
}
