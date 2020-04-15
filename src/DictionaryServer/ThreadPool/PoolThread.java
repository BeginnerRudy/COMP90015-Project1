/*
 * @Author: Renjie Meng
 * @Student ID: 877396
 * @Date: 2020 April
 * */

package DictionaryServer.ThreadPool;

import DictionaryServer.ServerController;
import DictionaryServer.Connection;
import DictionaryServer.Utility;

import java.io.IOException;
import java.util.concurrent.BlockingQueue;

/**
 * This class is the thread that in the ThreadPool class.
 */
public class PoolThread extends Thread {
    BlockingQueue taskQueue;

    boolean isStop = false;
    Connection connection;

    public PoolThread(BlockingQueue taskQueue) {
        this.taskQueue = taskQueue;
    }

    @Override
    public void run() {
        while (!this.isStop) {
            try {
                this.connection = (Connection) taskQueue.take();
                ServerController.getServerController().changeThreadStateOnGUI(this.getId(), connection.getIP().toString());
                Utility.printServerMsg("Thread", "Thread processing new task!");
                connection.run();
                Utility.printServerMsg("Thread", "Finish current serving.");
                if (!this.isStop) {
                    ServerController.getServerController().changeThreadStateOnGUI(this.getId(), "Idle");
                }
            } catch (InterruptedException e) {
                this.interrupt();
                Utility.printServerMsg("Thread", "The thread is interrupted.");
            }
        }
    }

    /**
     * This method would interrupt the current thread.
     */
    public void stopThread() {
        this.isStop = true;
        ServerController.getServerController().changeThreadStateOnGUI(this.getId(), "interrupted");
        this.interrupt();


        if (connection != null) {
            try {
                this.connection.getSocket().close();

            } catch (IOException e) {
//            e.printStackTrace();
                Utility.printServerMsg("Connection", "The socket is already closed.");

            }
        }
    }
}
