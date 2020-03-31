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

    public void add(String request) {
        String reply = this.dictionaryClient.add(request);
        clientGUI.getClientOutputTextArea().setText(reply);
    }
}
