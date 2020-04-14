package DictionaryClient;

import org.json.simple.JSONObject;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.ConnectException;
import java.net.UnknownHostException;

public class ReadingThread extends Thread {
    private ObjectInputStream reader;

    private boolean connected = false;

    public ReadingThread(ObjectInputStream reader) {
        this.reader = reader;
    }

    @Override
    public void run() {
        try {
            this.connected = true;
            while (true) {
                JSONObject reply = (JSONObject) reader.readObject();
                ClientController.getClientController().updateUI(reply);
            }
        } catch (UnknownHostException e) {
            Utility.printClientMsg("Connection", "Failed to connect to the server: " + e.getMessage() + " is unknown.");
            // construct the failure reply
            JSONObject reply = new JSONObject();
            reply.put(DictionaryClient.RESPONSE_CODE_KEY, DictionaryClient.FAILURE_CODE);
            reply.put(DictionaryClient.RESPONSE_MESSAGE_KEY, e.getMessage() + " is unknown.");
//            e.printStackTrace();
        } catch (ConnectException e) {
            Utility.printClientMsg("Connection", "Failed to connect to the server: " + e.getMessage());
            // construct the failure reply
            JSONObject reply = new JSONObject();
            reply.put(DictionaryClient.RESPONSE_CODE_KEY, DictionaryClient.FAILURE_CODE);
            reply.put(DictionaryClient.RESPONSE_MESSAGE_KEY, "Failed to connect to the server: " + e.getMessage());
//            e.printStackTrace();
            ClientController.getClientController().setGUIConnectivity("Disconnected.");
        } catch (IOException e) {
            Utility.printClientMsg("Connection", "Failed to connect to the server, connection closed.");
            // construct the failure reply
            JSONObject reply = new JSONObject();
            reply.put(DictionaryClient.RESPONSE_CODE_KEY, DictionaryClient.FAILURE_CODE);
            reply.put(DictionaryClient.RESPONSE_MESSAGE_KEY, "Failed to add a word to the server");
            ClientController.getClientController().setGUIConnectivity("Disconnected.");
//            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            Utility.printClientMsg("Response msg", "The server side response message is not recognisable. ");
//            e.printStackTrace();
        }

        this.connected = false;
    }

    public synchronized boolean isConnected() {
        return connected;
    }

}

