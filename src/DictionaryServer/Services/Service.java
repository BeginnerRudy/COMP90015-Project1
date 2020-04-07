package DictionaryServer.Services;

import org.json.simple.JSONObject;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * This class is the abstract class of the service that this dictionary server would
 * provide. The purpose of this class is to reduce the duplication of classes.
 */
public abstract class Service implements Runnable {
    Socket socket; // The socket to do I/O
    ObjectOutputStream writer;
    JSONObject body;

    public Service(Socket socket, JSONObject body) {
        this.socket = socket;
        this.getOutput();
        this.body = body;
    }


    /**
     * This private method aims to read from the socket given.
     */
    private void getOutput() {
        try {
            this.writer = new ObjectOutputStream(this.socket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * This method aims to close the all the I/O stuffs safely.
     */
    protected void closeOutput() {
        try {
            this.writer.close();
            this.socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
