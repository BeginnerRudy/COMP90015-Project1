package DictionaryServer;

public class ServerController {
    DictionaryServer server;

    ServerGUI serverGUI;

    public ServerController(DictionaryServer dictionaryServer) {
        this.server = dictionaryServer;
    }


    public void setServerGUI(ServerGUI serverGUI) {
        this.serverGUI = serverGUI;
    }

    public void serverGUI(ServerGUI serverGUI) {
    }

    public void shutDownServer(){
        this.server.shutDown();
    }
}
