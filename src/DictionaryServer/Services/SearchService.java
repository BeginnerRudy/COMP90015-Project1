package DictionaryServer.Services;

import DictionaryServer.Dictionary;
import org.json.simple.JSONObject;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.concurrent.TimeUnit;

/**
 * This class extends the service class which is responsible for handling searching a word
 * from dictionary.
 */
public class SearchService extends Service {
    String word;

    public SearchService(Socket socket, JSONObject body, ObjectOutputStream writer) {
        super(socket, body, writer);
    }

    @Override
    /**
     * This method defines the runnable interface method for searching
     */
    public synchronized void run() {
        try {
            this.word = (String) body.get(ServiceFactory.WORD_KEY);
            String meaning = Dictionary.getDictionary().getHashmap().get(word);
            // The word is in the dictionary, then delete it
            if (meaning != null){
                // construct reply
                JSONObject reply = new JSONObject();
                reply.put(ServiceFactory.RESPONSE_CODE_KEY, ServiceFactory.SUCCESS_SEARCH);
                reply.put(ServiceFactory.MEANING_KEY, meaning);
                super.writer.writeObject(reply);
                System.out.println(ServiceFactory.SUCCESS_SEARCH + "Successfully searched");
            }else{
                // The word is not the dictionary, return fail to search
                // construct reply
                JSONObject reply = new JSONObject();
                reply.put(ServiceFactory.RESPONSE_CODE_KEY, ServiceFactory.FAILURE_CODE);
                reply.put(ServiceFactory.RESPONSE_MESSAGE_KEY, "The word not found.");
                super.writer.writeObject(reply);
                System.out.println(ServiceFactory.FAILURE_CODE + "Word not found");

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        super.closeOutput();
    }
}
