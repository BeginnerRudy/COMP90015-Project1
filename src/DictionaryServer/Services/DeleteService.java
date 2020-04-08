/*
 * @Author: Renjie Meng
 * @Student ID: 877396
 * */

package DictionaryServer.Services;

import DictionaryServer.Dictionary;
import org.json.simple.JSONObject;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * This class extends the service class which is responsible for handling deleting a word
 * from dictionary.
 */
public class DeleteService extends Service {
    String word;

    public DeleteService(Socket socket, JSONObject body, ObjectOutputStream writer) {
        super(socket, body, writer);
    }

    @Override
    /**
     * This method defines the runnable interface method for deleting
     */
    public synchronized void run() {
        try {
            this.word = (String) body.get(ServiceFactory.WORD_KEY);

            // The word is in the dictionary, then delete it
            if (Dictionary.getDictionary().getHashmap().containsKey(this.word)) {
                // construct object
                JSONObject reply = new JSONObject();
                reply.put(ServiceFactory.RESPONSE_CODE_KEY, ServiceFactory.SUCCESS_DELETE);
                String message = "Successfully delete: " + word;
                reply.put(ServiceFactory.RESPONSE_MESSAGE_KEY, message);
                super.writer.writeObject(reply);
                Dictionary.getDictionary().getHashmap().remove(this.word);
                System.out.println(Dictionary.getDictionary().getHashmap());
                System.out.println(ServiceFactory.SUCCESS_DELETE + "Successfully delete: " + word);
                // if the word is not found
            } else {
                // construct object
                JSONObject reply = new JSONObject();
                reply.put(ServiceFactory.RESPONSE_CODE_KEY, ServiceFactory.FAILURE_CODE);
                reply.put(ServiceFactory.RESPONSE_MESSAGE_KEY, "Delete failed, because no such word in the dictionary");

                super.writer.writeObject(reply);
                System.out.println(ServiceFactory.FAILURE_CODE + "Delete failed, because no such word in the dictionary");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        super.closeOutput();
    }
}
