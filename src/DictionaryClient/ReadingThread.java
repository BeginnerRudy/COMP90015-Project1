package DictionaryClient;

import org.json.simple.JSONObject;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.ConnectException;
import java.net.UnknownHostException;

public class ReadingThread extends Thread {
    private ObjectInputStream reader;

    public ReadingThread(ObjectInputStream reader) {
        this.reader = reader;
    }

    @Override
    public void run() {
        try {
            while (true) {
                JSONObject reply = (JSONObject) reader.readObject();
                ClientController.getClientController().updateUI(reply);
            }
        } catch (UnknownHostException e) {
            System.out.println(DictionaryClient.FAILURE_CODE + "Failed to connect to the server: " + e.getMessage() + " is unknown.");
            // construct the failure reply
            JSONObject reply = new JSONObject();
            reply.put(DictionaryClient.RESPONSE_CODE_KEY, DictionaryClient.FAILURE_CODE);
            reply.put(DictionaryClient.RESPONSE_MESSAGE_KEY, e.getMessage() + " is unknown.");
            e.printStackTrace();
        } catch (ConnectException e) {
            // construct the failure reply
            JSONObject reply = new JSONObject();
            reply.put(DictionaryClient.RESPONSE_CODE_KEY, DictionaryClient.FAILURE_CODE);
            reply.put(DictionaryClient.RESPONSE_MESSAGE_KEY, "Failed to connect to the server: " + e.getMessage());
            e.printStackTrace();
            ClientController.getClientController().setGUIConnectivity("Disconnected.");
        } catch (IOException e) {
            // construct the failure reply
            JSONObject reply = new JSONObject();
            reply.put(DictionaryClient.RESPONSE_CODE_KEY, DictionaryClient.FAILURE_CODE);
            reply.put(DictionaryClient.RESPONSE_MESSAGE_KEY, "Failed to add a word to the server");
            ClientController.getClientController().setGUIConnectivity("Disconnected.");
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

}

