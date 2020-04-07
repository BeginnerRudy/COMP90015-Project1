package DictionaryClient;

import DictionaryClient.DictionaryClient;

import java.awt.*;

/**
 * This class is the client side controller, which is responsible for sending UI message
 * to appropriate server component as well as update the GUI with respect to the server.
 */
public class ClientController {
    DictionaryClient dictionaryClient;

    ClientGUI clientGUI;


    public ClientController(DictionaryClient dictionaryClient) {
        this.dictionaryClient = dictionaryClient;
    }

    public void setClientGUI(ClientGUI clientGUI) {
        this.clientGUI = clientGUI;
    }

    /**
     * @param word The word to add
     * @param meaning The word's meaning to be added.
     *
     * This method is responsible for handing the add button message from the GUI
     */
    public void add(String word, String meaning) {
        String reply = this.dictionaryClient.add(word, meaning);
        updateViewForAddAndDelete(reply);

    }

    /**
     * @param word The word to delete
     *
     * This method is responsible for handing the delete button message from the GUI
     */
    public void delete(String word) {
        String reply = this.dictionaryClient.delete(word);
        updateViewForAddAndDelete(reply);
    }

    /**
     * @param reply The reply from the dictionary client
     *
     * This method is used to update the ADD and DELETE button's view
     */
    private void updateViewForAddAndDelete(String reply) {
        // parse the reply message
        String responseCode = Character.toString(reply.charAt(0));
        String message = reply.substring(1);

        // duplex the message depends on the response code,
        // message with different response code would have different text color.
        if (responseCode.equals(DictionaryClient.SUCCESS_CODE)) {
            clientGUI.getServerResponse().setForeground(new Color(0, 125, 0));
            clientGUI.getServerResponse().setText(message);

        } else if (responseCode.equals(DictionaryClient.FAILURE_CODE)) {
            clientGUI.getServerResponse().setForeground(new Color(255, 0, 0));
            clientGUI.getServerResponse().setText(message);
        }
    }

    /**
     * @param word The word to search
     *
     * This method is responsible for handing the search button message from the GUI
     */
    public void search(String word) {
        String reply = this.dictionaryClient.search(word);
        String responseCode = Character.toString(reply.charAt(0));
        String message = reply.substring(1);
        if (responseCode.equals(DictionaryClient.SUCCESS_CODE)) {
            clientGUI.getClientOutputTextArea().setText(message);

        } else if (responseCode.equals(DictionaryClient.FAILURE_CODE)) {

            clientGUI.getServerResponse().setForeground(new Color(255, 0, 0));
            clientGUI.getServerResponse().setText(message);

        } else {

        }

    }
}
