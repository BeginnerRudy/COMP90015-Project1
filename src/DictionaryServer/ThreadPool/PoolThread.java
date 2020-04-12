/*
 * @Author: Renjie Meng
 * @Student ID: 877396
 * */

package DictionaryServer.ThreadPool;

import DictionaryServer.DictionaryServer;
import DictionaryServer.ServerController;
import DictionaryServer.Connection;

import java.util.concurrent.BlockingQueue;

/**
 * This class is the thread that in the ThreadPool class.
 */
public class PoolThread extends Thread {
    BlockingQueue taskQueue;

    boolean isStop = false;
    boolean isIdle = true;

    public PoolThread(BlockingQueue taskQueue) {
        this.taskQueue = taskQueue;
    }

    @Override
    public void run() {
        while (!this.isStop) {
            try {
                Connection connection = (Connection) taskQueue.take();
                ServerController.getServerController().changeThreadStateOnGUI(this.getId(), connection.getIP().toString());
                DictionaryServer.printServerMsg("Thread processing new task!");
                connection.run();
                DictionaryServer.printServerMsg("finished");
                ServerController.getServerController().changeThreadStateOnGUI(this.getId(), "Idle");
            } catch (InterruptedException e) {
                DictionaryServer.printServerMsg("The thread in thread pool is interrupted due to server is closed.");
            }
        }
    }

    /**
     * This method would interrupt the current thread.
     */
    public void stopThread() {
        this.isStop = true;
        ServerController.getServerController().changeThreadStateOnGUI(this.getId(), "stopped");
        this.interrupt();
    }

}
