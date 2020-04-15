/*
 * @Author: Renjie Meng
 * @Student ID: 877396
 * @Date: 2020 April
 * */

package DictionaryServer;

import DictionaryServer.ThreadPool.PoolThread;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.ArrayList;
import java.util.List;

/**
 * This class is the server side controller, which is responsible for sending UI message
 * to appropriate server component as well as update the GUI with respect to the server.
 */
public class ServerController {
    static ServerController serverController = new ServerController();
    public static final String THREAD = "Thread ";
    ServerGUI serverGUI;


    /**
     * The singleton getter.
     *
     * @return The controller.
     */
    public synchronized static ServerController getServerController() {
        return serverController;
    }

    public void init(ServerGUI serverGUI) {
        this.serverGUI = serverGUI;

    }

    /**
     * This method add thread ids given to the GUI.
     *
     * @param threadIds A list of long which represents thread ids.
     */
    public synchronized void addThreadsOnGUI(ArrayList<Long> threadIds) {
        JTable s = this.serverGUI.getTable1();
        // add row dynamically into the table
        for (Long threadId : threadIds) {
            this.serverGUI.getDtm().addRow(new Object[]{THREAD + threadId, "idle"});
        }

        this.serverGUI.getPoolCount().setText(Integer.toString(DictionaryServer.getServer().getThreadPool().getThreads().size()) + "/" + DictionaryServer.getServer().getMaxPoolSize());
    }


    /**
     * This methods would change the given thread's id status on the GUI.
     *
     * @param threadID The id of thread to change
     * @param status   The status to show.
     */
    public synchronized void changeThreadStateOnGUI(long threadID, String status) {

        DefaultTableModel dtm = serverGUI.getDtm();

        for (int i = 0; i < dtm.getRowCount(); i++) {
            if (dtm.getValueAt(i, 0).equals(THREAD + threadID)) {

                serverGUI.getDtm().setValueAt(status, i, 1);
            }
        }
    }


    /**
     * This method would interrupted the selected thread in the GUI.
     */
    public void killThreads() {
        if (DictionaryServer.getServer().getThreadPool().getRunningThreadCount() <= 1) {
            // only one thread left, not a valid operation.
            // user can just shut down the server.
            JOptionPane.showMessageDialog(null, "Caution! Only one thread working! Not allowed to terminate it.");
        } else {
            // terminate selected threads
            int row = this.serverGUI.getTable1().getSelectedRow();
            String s = (String) this.serverGUI.getTable1().getValueAt(row, 0);
            long id = Integer.parseInt(s.split(" ")[1]);
            DictionaryServer.getServer().getThreadPool().stop(id);
        }
    }

    /**
     * This method would clear the interrupted threads on the GUI.
     */
    public synchronized void cleanDeadThreads() {
        ArrayList<Long> deadIds = DictionaryServer.getServer().getThreadPool().clean();
        DefaultTableModel dtm = serverGUI.getDtm();


        for (int i = dtm.getRowCount() - 1; i >= 0; i--) {
            long currId = Integer.parseInt(dtm.getValueAt(i, 0).toString().split(" ")[1]);
            if (deadIds.contains(currId)) {
                dtm.removeRow(i);
            }
        }

        this.serverGUI.getPoolCount().setText(Integer.toString(DictionaryServer.getServer().getThreadPool().getThreads().size()) + "/" + DictionaryServer.getServer().getMaxPoolSize());

    }

    /**
     * This method is used to shut the server.
     */
    public void shutDownServer() {
        DictionaryServer.getServer().shutDown();

    }

    /**
     * This method would fill the thread pool till containing max number of threads.
     */
    public void fulfill() {
        int n = DictionaryServer.getServer().getMaxPoolSize() - DictionaryServer.getServer().getThreadPool().getThreads().size();
        addThreadsOnGUI(DictionaryServer.getServer().getThreadPool().add(n));
    }
}
