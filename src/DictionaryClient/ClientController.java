package DictionaryClient;

import DictionaryClient.DictionaryClient;

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
        clientGUI.getServerResponse().setText(reply);
    }

    public void delete(String word) {
        String reply = this.dictionaryClient.delete(word);
        clientGUI.getServerResponse().setText(reply);
    }

    public void search(String word) {
        String reply = this.dictionaryClient.search(word);
        clientGUI.getClientOutputTextArea().setText(reply);
    }
}
