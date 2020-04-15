/*
 * @Author: Renjie Meng
 * @Student ID: 877396
 * @Date: 2020 April
 * */

package DictionaryClient;

import org.json.simple.JSONObject;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.ConnectException;
import java.net.UnknownHostException;

/**
 * This thread is responsible for continually reading input from the server.
 */
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
            // continually reading from the server
            while (true) {
                // read from the server
                JSONObject reply = (JSONObject) reader.readObject();
                // ask the controller to update UI
                ClientController.getClientController().updateUI(reply);
            }
        } catch (Exception e) {
            handleExceptions(e);
        }

        this.connected = false;
    }

    private void handleExceptions(Exception e) {
        if (e instanceof UnknownHostException) {
            Utility.printClientMsg("Connection", "Failed to connect to the server: " + e.getMessage() + " is unknown.");
            ClientController.getClientController().setGUIConnectivity("Disconnected.");
        } else if (e instanceof ConnectException) {
            Utility.printClientMsg("Connection", "Failed to connect to the server: " + e.getMessage());
            ClientController.getClientController().setGUIConnectivity("Disconnected.");
        } else if (e instanceof IOException) {
            Utility.printClientMsg("Connection", "Failed to connect to the server, connection closed.");
            ClientController.getClientController().setGUIConnectivity("Disconnected.");
        } else if (e instanceof ClassNotFoundException) {
            Utility.printClientMsg("Response msg", "The server side response message is not recognisable. ");
        }
    }

    /**
     * This methods returns whether the reading channel is connected.
     *
     * @return isConnected
     */
    public synchronized boolean isConnected() {
        return connected;
    }

}

