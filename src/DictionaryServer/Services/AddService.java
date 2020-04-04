package DictionaryServer.Services;

import DictionaryServer.Dictionary;

import java.io.IOException;
import java.net.Socket;

public class AddService extends Service {
    String word;
    String meaning;


    public AddService(Socket socket, String body) {
        super(socket, body);
    }

    private void parse(String body) {
        String lis[] = body.split(ServiceFactory.SEPARATOR);

        this.word = lis[0];
        this.meaning = lis[1];
    }

    @Override
    public synchronized void run() {
        try {
            parse(body);

            // if the word is duplicate
            if (Dictionary.getDictionary().getHashmap().containsKey(this.word)) {
                super.dos.writeUTF("Failed to add, because the word is already in dictionary. ");
                System.out.println("Failed to add, because the word is already in dictionary. ");
            }
            // if the meaning is null
            else if (this.meaning == null) {
                super.dos.writeUTF("Fail to add, because the meaning is empty");
                System.out.println("Fail to add, because the meaning is empty");
            // if the word is not duplicate and the meaning is not null.
            } else {

                super.dos.writeUTF("Successfully add: " + word + " - " + meaning);
                Dictionary.getDictionary().getHashmap().put(word, meaning);
                System.out.println(Dictionary.getDictionary().getHashmap());
                System.out.println("Successfully add: " + word + " - " + meaning);
            }
            super.closeOutput();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
