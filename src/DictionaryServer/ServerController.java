/*
 * @Author: Renjie Meng
 * @Student ID: 877396
 * */

package DictionaryServer;

import DictionaryServer.ThreadPool.PoolThread;

import java.util.List;

/**
 * This class is the server side controller, which is responsible for sending UI message
 * to appropriate server component as well as update the GUI with respect to the server.
 */
public class ServerController {
    static ServerController serverController = new ServerController();
    public static final String CONNECTION = "Connection ";
    DictionaryServer server;

    ServerGUI serverGUI;


    public synchronized static ServerController getServerController() {
        return serverController;
    }

    public void init(DictionaryServer dictionaryServer, ServerGUI serverGUI) {
        this.server = dictionaryServer;
        this.serverGUI = serverGUI;
        ServerController.getServerController().initThreadsOnGUI(server.getThreadPool().getThreads());

    }

    public synchronized void initThreadsOnGUI(List<PoolThread> threads){
        // add row dynamically into the table
        for (PoolThread thread : threads) {
            this.serverGUI.getDtm().addRow(new Object[]{"Thread " + thread.getId(), thread.isStop()});
        }
    }

    public synchronized void removeConnectionFromGUI(Connection connection){
    }

    public synchronized void killConnection(){
    }

    /**
     * This method is used to shut the server.
     */
    public void shutDownServer() {
        this.server.shutDown();

    }
}
