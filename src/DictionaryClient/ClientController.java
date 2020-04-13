/*
 * @Author: Renjie Meng
 * @Student ID: 877396
 * */

package DictionaryClient;

import org.json.simple.JSONObject;

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

    DictionaryClient dictionaryClient;

    ClientGUI clientGUI;


    public void setClientGUI(ClientGUI clientGUI) {
        this.clientGUI = clientGUI;
    }

    public void setDictionaryClient(DictionaryClient dictionaryClient) {
        this.dictionaryClient = dictionaryClient;
    }


    public void updateUI(JSONObject reply){
        String responseCode = (String) reply.get(DictionaryClient.RESPONSE_CODE_KEY);
        if (responseCode.equals(DictionaryClient.SUCCESS_SEARCH)) {
            String meaning = (String) reply.get(DictionaryClient.MEANING_KEY);
            clientGUI.getClientOutputTextArea().setText(meaning);
            clientGUI.getServerResponse().setForeground(new Color(0, 125, 0));
            clientGUI.getServerResponse().setText("Successfully searched, meaning is as shown above.");

        } else if (responseCode.equals(DictionaryClient.SUCCESS_ADD) || responseCode.equals(DictionaryClient.SUCCESS_DELETE) ) {
            String message = (String) reply.get(DictionaryClient.RESPONSE_MESSAGE_KEY);
            clientGUI.getServerResponse().setForeground(new Color(0, 125, 0));
            clientGUI.getServerResponse().setText(message);

        } else if (responseCode.equals(DictionaryClient.FAILURE_CODE)) {
            String message = (String) reply.get(DictionaryClient.RESPONSE_MESSAGE_KEY);
            clientGUI.getServerResponse().setForeground(new Color(255, 0, 0));
            clientGUI.getServerResponse().setText(message);

        }
    }


    /**
     * @param word    The word to add
     * @param meaning The word's meaning to be added.
     *                <p>
     *                This method is responsible for handing the add button message from the GUI
     */
    public void add(String word, String meaning) {
        this.dictionaryClient.add(word, meaning);

    }

    /**
     * @param word The word to delete
     *             <p>
     *             This method is responsible for handing the delete button message from the GUI
     */
    public void delete(String word) {
        this.dictionaryClient.delete(word);
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
        this.dictionaryClient.search(word);
    }


    /**
     * This method is responsible for disconnect the client and the server
     */
    public void disconnect() {
        dictionaryClient.disconnect();
    }


    public void setGUISystemMsg(String msg){
        this.clientGUI.getConnectivity().setText(msg);
    }
}
