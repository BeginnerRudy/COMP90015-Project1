package DictionaryClient;

import DictionaryClient.DictionaryClient;

import java.awt.*;

public class ClientController {
    DictionaryClient dictionaryClient;

    ClientGUI clientGUI;


    public ClientController(DictionaryClient dictionaryClient) {
        this.dictionaryClient = dictionaryClient;
    }

    public void setClientGUI(ClientGUI clientGUI) {
        this.clientGUI = clientGUI;
    }

    public void add(String word, String meaning) {
        String reply = this.dictionaryClient.add(word, meaning);
        updateViewForAddAndDelete(reply);

    }

    public void delete(String word) {
        String reply = this.dictionaryClient.delete(word);
        updateViewForAddAndDelete(reply);
    }

    private void updateViewForAddAndDelete(String reply) {
        String responseCode = Character.toString(reply.charAt(0));
        String message = reply.substring(1);

        if (responseCode.equals(DictionaryClient.SUCCESS_CODE)) {
            clientGUI.getServerResponse().setForeground(new Color(0, 125, 0));
            clientGUI.getServerResponse().setText(message);

        } else if (responseCode.equals(DictionaryClient.FAILURE_CODE)) {
            clientGUI.getServerResponse().setForeground(new Color(255, 0, 0));
            clientGUI.getServerResponse().setText(message);
        }
    }

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
