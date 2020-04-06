package DictionaryServer.ThreadPool;

import DictionaryServer.DictionaryServer;

import java.util.concurrent.BlockingQueue;

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
            } catch (InterruptedException e) {
                DictionaryServer.printServerMsg("The thread in thread pool is interrupted due to server is closed.");
            }
        }
    }

    public void stopThread() {
        this.isStop = true;
        this.interrupt();
    }
}
