package DictionaryServer;

/**
 * This class is the server side controller, which is responsible for sending UI message
 * to appropriate server component as well as update the GUI with respect to the server.
 */
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

    /**
     * This method is used to shut the server.
     */
    public void shutDownServer(){
        this.server.shutDown();
    }
}
