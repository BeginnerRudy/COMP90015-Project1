package DictionaryServer.Services;

import DictionaryServer.Dictionary;
import DictionaryServer.ServiceFactory;

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
            super.dos.writeUTF("Successfully add: " + word + " - " + meaning);
            Dictionary.getDictionary().getHashmap().put(word, meaning);
            System.out.println(Dictionary.getDictionary().getHashmap());
            System.out.println("Successfully add: " + word + " - " + meaning);
            super.closeOutput();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
