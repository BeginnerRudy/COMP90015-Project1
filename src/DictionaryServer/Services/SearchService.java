package DictionaryServer.Services;

import DictionaryServer.Dictionary;

import java.io.IOException;
import java.net.Socket;

public class SearchService extends Service {
    String word;

    public SearchService(Socket socket, String body) {
        super(socket, body);
    }

    @Override
    public synchronized void run() {
        try {
            this.word = body;
            String meaning = Dictionary.getDictionary().getHashmap().get(word);
            if (meaning != null){
                super.dos.writeUTF(meaning);
                System.out.println("Successfully searched");
            }else{
                super.dos.writeUTF("The word not found.");
                System.out.println("Word not found");

            }
            super.closeOutput();
        } catch (IOException e) {
            e.printStackTrace();
        }
        super.closeOutput();
    }
}
