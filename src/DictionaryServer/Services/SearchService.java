package DictionaryServer.Services;

import DictionaryServer.Dictionary;
import org.json.simple.JSONObject;

import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.TimeUnit;

/**
 * This class extends the service class which is responsible for handling searching a word
 * from dictionary.
 */
public class SearchService extends Service {
    String word;

    public SearchService(Socket socket, JSONObject body) {
        super(socket, body);
    }

    @Override
    /**
     * This method defines the runnable interface method for searching
     */
    public synchronized void run() {
        try {
            TimeUnit.SECONDS.sleep(3);
//            this.word = body;
            String meaning = Dictionary.getDictionary().getHashmap().get(word);
            // The word is in the dictionary, then delete it
            if (meaning != null){
                super.writer.writeUTF(ServiceFactory.SUCCESS_CODE + meaning);
                System.out.println(ServiceFactory.SUCCESS_CODE + "Successfully searched");
            }else{
                // The word is not the dictionary, return fail to search
                super.writer.writeUTF(ServiceFactory.FAILURE_CODE + "The word not found.");
                System.out.println(ServiceFactory.FAILURE_CODE + "Word not found");

            }
            super.closeOutput();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e){
            // TODO remove it later, only for demo purpose
        }
        super.closeOutput();
    }
}
