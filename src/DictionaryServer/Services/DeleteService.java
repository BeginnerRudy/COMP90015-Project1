package DictionaryServer.Services;

import DictionaryServer.Dictionary;

import java.io.IOException;
import java.net.Socket;

public class DeleteService extends Service {
    String word;

    public DeleteService(Socket socket, String body) {
        super(socket, body);
    }

    @Override
    public synchronized void run() {
        try {
            this.word = body;
            super.dos.writeUTF("Successfully delete: " + word);
            Dictionary.getDictionary().getHashmap().remove(body);
            System.out.println(Dictionary.getDictionary().getHashmap());
            System.out.println("Successfully delete: " + word);
            super.closeOutput();
        } catch (IOException e) {
            e.printStackTrace();
        }
        super.closeOutput();
    }
}
