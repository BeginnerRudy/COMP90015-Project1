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
                e.printStackTrace();
                // TODO handle the queue has been interrupted.
            }
        }
    }

    public void stopThread() {
        this.isStop = true;
        this.interrupt();
    }

    public boolean isStop() {
        return isStop;
    }
}
