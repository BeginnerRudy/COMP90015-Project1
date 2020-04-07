package DictionaryServer.Services;

import DictionaryServer.Dictionary;

import java.io.IOException;
import java.net.Socket;

/**
 * This class extends the service class which is responsible for handling adding a word
 * to dictionary.
 */
public class AddService extends Service {
    String word;
    String meaning;


    public AddService(Socket socket, String body) {
        super(socket, body);
    }


    /**
     * @param body The request body sent by the client.
     *
     * This method aims to separate the word and meaning in the body.
     */
    private void parse(String body) {
        String lis[] = body.split(ServiceFactory.SEPARATOR);

        this.word = lis[0];
        this.meaning = lis[1];
    }

    /**
     * This method defines the runnable interface method for adding
     */
    @Override
    public synchronized void run() {
        try {
            parse(body);

            // if the word is duplicate
            if (Dictionary.getDictionary().getHashmap().containsKey(this.word)) {
                super.dos.writeUTF(ServiceFactory.FAILURE_CODE + "Failed to add, because the word is already in dictionary. ");
                System.out.println(ServiceFactory.FAILURE_CODE + "Failed to add, because the word is already in dictionary. ");
            }
            // if the meaning is null
            else if (this.meaning == null) {
                super.dos.writeUTF(ServiceFactory.FAILURE_CODE + "Fail to add, because the meaning is empty");
                System.out.println(ServiceFactory.FAILURE_CODE + "Fail to add, because the meaning is empty");
                // if the word is not duplicate and the meaning is not null.
            } else {

                super.dos.writeUTF(ServiceFactory.SUCCESS_CODE + "Successfully add: " + word + " - " + meaning);
                Dictionary.getDictionary().getHashmap().put(word, meaning);
                System.out.println(Dictionary.getDictionary().getHashmap());
                System.out.println(ServiceFactory.SUCCESS_CODE + "Successfully add: " + word + " - " + meaning);
            }
            super.closeOutput();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
