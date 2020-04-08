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
 * This class extends the service class which is responsible for handling adding a word
 * to dictionary.
 */
public class AddService extends Service {
    String word;
    String meaning;


    public AddService(Socket socket, JSONObject body, ObjectOutputStream writer) {
        super(socket, body, writer);
    }


    /**
     * @param body The request body sent by the client.
     *             <p>
     *             This method aims to separate the word and meaning in the body.
     */
    private void parse(JSONObject body) {

        this.word = (String) body.get(ServiceFactory.WORD_KEY);
        this.meaning = (String) body.get(ServiceFactory.MEANING_KEY);
    }

    /**
     * This method defines the runnable interface method for adding
     */
    @Override
    public synchronized void run() {
        try {
            parse(body);

            JSONObject reply = new JSONObject();

            // if the word is duplicate
            if (Dictionary.getDictionary().getHashmap().containsKey(this.word)) {
                // construct reply JSON object
                reply.put(ServiceFactory.RESPONSE_CODE_KEY, ServiceFactory.FAILURE_CODE);
                reply.put(ServiceFactory.RESPONSE_MESSAGE_KEY, "Failed to add, because the word is already in dictionary. ");

                // send reply to client
                super.writer.writeObject(reply);
                System.out.println(ServiceFactory.FAILURE_CODE + "Failed to add, because the word is already in dictionary. ");
            }
            // if the meaning is null
            else if (this.meaning == null) {
                // construct reply JSON object
                reply.put(ServiceFactory.RESPONSE_CODE_KEY, ServiceFactory.FAILURE_CODE);
                reply.put(ServiceFactory.RESPONSE_MESSAGE_KEY, "Fail to add, because the meaning is empty");
                super.writer.writeObject(reply);
                System.out.println(ServiceFactory.FAILURE_CODE + "Fail to add, because the meaning is empty");
                // if the word is not duplicate and the meaning is not null.
            } else {
                reply.put(ServiceFactory.RESPONSE_CODE_KEY, ServiceFactory.SUCCESS_ADD);
                String response_msg = "Successfully add: " + word + " - " + meaning;
                reply.put(ServiceFactory.RESPONSE_MESSAGE_KEY, response_msg);
                Dictionary.getDictionary().getHashmap().put(word, meaning);
                super.writer.writeObject(reply);
                System.out.println(Dictionary.getDictionary().getHashmap());
                System.out.println(ServiceFactory.SUCCESS_ADD + "Successfully add: " + word + " - " + meaning);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        super.closeOutput();
    }
}
