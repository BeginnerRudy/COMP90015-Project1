/*
 * @Author: Renjie Meng
 * @Student ID: 877396
 * */

package DictionaryServer.ThreadPool;

import DictionaryServer.DictionaryServer;

import java.util.concurrent.BlockingQueue;

/**
 * This class is the thread that in the ThreadPool class.
 */
public class PoolThread extends Thread {
    BlockingQueue taskQueue;

    boolean isStop = false;

    public PoolThread(BlockingQueue taskQueue) {
        this.taskQueue = taskQueue;
    }

    @Override
    public void run() {
        while (!this.isStop) {
            try {
                Runnable task = (Runnable) taskQueue.take();
                DictionaryServer.printServerMsg("Thread processing new task!");
                task.run();
                DictionaryServer.printServerMsg("finished");
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
        this.interrupt();
    }

    public boolean isStop() {
        return isStop;
    }
}
