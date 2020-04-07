package DictionaryServer.Services;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

/**
 * This class is the abstract class of the service that this dictionary server would
 * provide. The purpose of this class is to reduce the duplication of classes.
 */
public abstract class Service implements Runnable {
    Socket socket; // The socket to do I/O
    OutputStream outputStream;
    DataOutputStream dos;
    String body;

    public Service(Socket socket, String body) {
        this.socket = socket;
        this.getOutput();
        this.body = body;
    }


    /**
     * This private method aims to read from the socket given.
     */
    private void getOutput() {
        try {
            this.outputStream = this.socket.getOutputStream();
            this.dos = new DataOutputStream(this.outputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * This method aims to close the all the I/O stuffs safely.
     */
    protected void closeOutput() {
        try {
            this.outputStream.close();
            this.dos.close();
            this.socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
