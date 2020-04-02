package DictionaryServer.Services;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

public abstract class Service implements Runnable {
    Socket socket;
    OutputStream outputStream;
    DataOutputStream dos;
    String body;

    public Service(Socket socket, String body) {
        this.socket = socket;
        this.getOutput();
        this.body = body;
    }


    private void getOutput() {
        try {
            this.outputStream = this.socket.getOutputStream();
            this.dos = new DataOutputStream(this.outputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

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
