package DictionaryClient;

import DictionaryClient.DictionaryClient;
import DictionaryServer.Dictionary;
import org.json.simple.JSONObject;

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
     * @param word    The word to add
     * @param meaning The word's meaning to be added.
     *                <p>
     *                This method is responsible for handing the add button message from the GUI
     */
    public void add(String word, String meaning) {
        JSONObject reply = this.dictionaryClient.add(word, meaning);
        updateViewForAddAndDelete(reply, DictionaryClient.SUCCESS_ADD);

    }

    /**
     * @param word The word to delete
     *             <p>
     *             This method is responsible for handing the delete button message from the GUI
     */
    public void delete(String word) {
        JSONObject reply = this.dictionaryClient.delete(word);

        updateViewForAddAndDelete(reply, DictionaryClient.SUCCESS_DELETE);
    }

    /**
     * @param reply The reply from the dictionary client
     *              <p>
     *              This method is used to update the ADD and DELETE button's view
     */
    private void updateViewForAddAndDelete(JSONObject reply, String success_code) {
        // parse the reply message
        String responseCode = (String) reply.get(DictionaryClient.RESPONSE_CODE_KEY);
        // duplex the message depends on the response code,
        // message with different response code would have different text color.
        if (responseCode.equals(success_code)) {
            String message = (String) reply.get(DictionaryClient.RESPONSE_MESSAGE_KEY);
            clientGUI.getServerResponse().setForeground(new Color(0, 125, 0));
            clientGUI.getServerResponse().setText(message);
            System.out.println(message);

        } else if (responseCode.equals(DictionaryClient.FAILURE_CODE)) {
            String message = (String) reply.get(DictionaryClient.RESPONSE_MESSAGE_KEY);
            clientGUI.getServerResponse().setForeground(new Color(255, 0, 0));
            clientGUI.getServerResponse().setText(message);
        }
    }

    /**
     * @param word The word to search
     *             <p>
     *             This method is responsible for handing the search button message from the GUI
     */
    public void search(String word) {
        JSONObject reply = this.dictionaryClient.search(word);
        String responseCode = (String) reply.get(DictionaryClient.RESPONSE_CODE_KEY);
        if (responseCode.equals(DictionaryClient.SUCCESS_SEARCH)) {
            String meaning = (String) reply.get(DictionaryClient.MEANING_KEY);
            clientGUI.getClientOutputTextArea().setText(meaning);
            clientGUI.getServerResponse().setForeground(new Color(0, 125, 0));
            clientGUI.getServerResponse().setText("Successfully searched, meaning is as shown above.");

        } else if (responseCode.equals(DictionaryClient.FAILURE_CODE)) {
            String message = (String) reply.get(DictionaryClient.RESPONSE_MESSAGE_KEY);
            clientGUI.getServerResponse().setForeground(new Color(255, 0, 0));
            clientGUI.getServerResponse().setText(message);

        }

    }
}
