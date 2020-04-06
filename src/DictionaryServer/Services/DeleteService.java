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
            // if the word is not found
            this.word = body;
            if (Dictionary.getDictionary().getHashmap().containsKey(this.word)) {
                super.dos.writeUTF(ServiceFactory.SUCCESS_CODE + "Successfully delete: " + word);
                Dictionary.getDictionary().getHashmap().remove(body);
                System.out.println(Dictionary.getDictionary().getHashmap());
                System.out.println(ServiceFactory.SUCCESS_CODE + "Successfully delete: " + word);
            } else {
                super.dos.writeUTF(ServiceFactory.FAILURE_CODE + "Delete failed, because no such word in the dictionary");
                System.out.println(ServiceFactory.FAILURE_CODE + "Delete failed, because no such word in the dictionary");
            }

            super.closeOutput();
        } catch (IOException e) {
            e.printStackTrace();
        }
        super.closeOutput();
    }
}
