/*
 * @Author: Renjie Meng
 * @Student ID: 877396
 * */

package DictionaryServer;

import javax.swing.*;

/**
 * This class is the server side controller, which is responsible for sending UI message
 * to appropriate server component as well as update the GUI with respect to the server.
 */
public class ServerController {
    static ServerController serverController = new ServerController();
    public static final String Thread = "Thread ";
    DictionaryServer server;

    ServerGUI serverGUI;

    int threadCount = 0;

    public synchronized static ServerController getServerController() {
        return serverController;
    }

    public void init(DictionaryServer dictionaryServer, ServerGUI serverGUI) {
        this.server = dictionaryServer;
        this.serverGUI = serverGUI;
    }

    public synchronized void addNewConnectionToGUI(Connection connection){
        this.serverGUI.getDefaultListModel().addElement(Thread + threadCount);
        threadCount += 1;
    }

    public synchronized void removeConnectionFromGUI(Connection connection){

    }

    public synchronized void killThread(){
        // get the selected value and remove it from list
        int selectedIndex = this.serverGUI.getList1().getSelectedIndex();
        this.serverGUI.getDefaultListModel().remove(selectedIndex);
        this.serverGUI.getList1().updateUI();
    }

    /**
     * This method is used to shut the server.
     */
    public void shutDownServer() {
        this.server.shutDown();

    }
}
