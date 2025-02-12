/*
 * @Author: Renjie Meng
 * @Student ID: 877396
 * @Date: 2020 April
 * */

package DictionaryClient;

import org.json.simple.JSONObject;

import javax.swing.*;
import java.awt.*;


/**
 * This class is the client side controller, which is responsible for sending UI message
 * to appropriate server component as well as update the GUI with respect to the server.
 */
public class ClientController {
    private static ClientController clientController = new ClientController();

    public static ClientController getClientController() {
        return clientController;
    }

    ClientGUI clientGUI;


    /**
     * This method initiates ClientController
     *
     * @param clientGUI The ClientGUI the controller needs to control.
     */
    public void init(ClientGUI clientGUI) {
        this.clientGUI = clientGUI;
    }


    /**
     * This method is responsible for udpating the GUI according the given reply.
     *
     * @param reply The reply from client
     */
    public void updateUI(JSONObject reply) {
        // Get the response code of the reply
        String responseCode = (String) reply.get(DictionaryClient.RESPONSE_CODE_KEY);

        // change the GUI for different response code
        if (responseCode.equals(DictionaryClient.SUCCESS_SEARCH)) {
            String meaning = (String) reply.get(DictionaryClient.MEANING_KEY);
            clientGUI.getClientOutputTextArea().setText(meaning);
            clientGUI.getServerResponse().setForeground(new Color(0, 125, 0));
            clientGUI.getServerResponse().setText("Successfully searched, meaning is as shown above.");

        } else if (responseCode.equals(DictionaryClient.SUCCESS_ADD) || responseCode.equals(DictionaryClient.SUCCESS_DELETE)) {
            String message = (String) reply.get(DictionaryClient.RESPONSE_MESSAGE_KEY);
            clientGUI.getServerResponse().setForeground(new Color(0, 125, 0));
            clientGUI.getServerResponse().setText(message);

        } else if (responseCode.equals(DictionaryClient.FAILURE_CODE)) {
            String message = (String) reply.get(DictionaryClient.RESPONSE_MESSAGE_KEY);
            clientGUI.getServerResponse().setForeground(new Color(255, 0, 0));
            clientGUI.getServerResponse().setText(message);

        } else {
            clientGUI.getConnectivity().setText((String) reply.get(DictionaryClient.RESPONSE_CODE_KEY));
        }
    }


    /**
     * @param word    The word to add
     * @param meaning The word's meaning to be added.
     *                <p>
     *                This method is responsible for handing the add button message from the GUI
     */
    public void add(String word, String meaning) {
        if (DictionaryClient.getClient().getConnection().isConnecting()) {
            JOptionPane.showMessageDialog(null, "The client is connecting, try later");
        } else {
            if (word.strip() != "" && meaning.strip() != "") {
                if (!DictionaryClient.getClient().getConnection().isConnected()) {
                    this.setGUIConnectivity("Reconnecting ... ");
                }
                DictionaryClient.getClient().add(word, meaning);
            } else {
                clientGUI.getServerResponse().setForeground(new Color(255, 0, 0));
                this.clientGUI.getServerResponse().setText("Please enter both word and meaning.");
            }
        }

    }

    /**
     * @param word The word to delete
     *             <p>
     *             This method is responsible for handing the delete button message from the GUI
     */
    public void delete(String word) {
        if (DictionaryClient.getClient().getConnection().isConnecting()) {
            JOptionPane.showMessageDialog(null, "The client is connecting, try later");
        } else {

            if (word.strip() != "") {
                if (!DictionaryClient.getClient().getConnection().isConnected()) {
                    ClientController.getClientController().setGUIConnectivity("Reconnecting ... ");
                }
                DictionaryClient.getClient().delete(word);
            } else {
                clientGUI.getServerResponse().setForeground(new Color(255, 0, 0));
                this.clientGUI.getServerResponse().setText("Please enter non-empty word");
            }
        }
    }

    /**
     * @param word The word to search
     *             <p>
     *             This method is responsible for handing the search button message from the GUI
     */
    public void search(String word) {
        if (DictionaryClient.getClient().getConnection().isConnecting()) {
            JOptionPane.showMessageDialog(null, "The client is connecting, try later");
        } else {

            if (word.strip() != "") {
                DictionaryClient.getClient().search(word);
            } else {
                clientGUI.getServerResponse().setForeground(new Color(255, 0, 0));
                this.clientGUI.getServerResponse().setText("Please enter non-empty word");
            }
        }
    }

    /**
     * This method is responsible for disconnect the client and the server
     */
    public void disconnect() {

        if (DictionaryClient.getClient().getConnection().isConnecting()) {
            JOptionPane.showMessageDialog(null, "The client is connecting, try later");
        } else {
            DictionaryClient.getClient().disconnect();
        }
    }


    /**
     * This method is responsible for change the connectivity Jlable in the GUI.
     *
     * @param msg The message to be displayed
     */
    public void setGUIConnectivity(String msg) {
        this.clientGUI.getConnectivity().setText(msg);
        this.clientGUI.getConnectivity().updateUI();
    }
}
