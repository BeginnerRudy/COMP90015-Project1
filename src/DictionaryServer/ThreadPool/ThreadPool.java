package DictionaryServer.ThreadPool;

import DictionaryServer.DictionaryServer;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class ThreadPool {
    BlockingQueue taskQueue;
    List<PoolThread> threadPool;
    boolean isStop = false;

    public ThreadPool(int maxThread) {
        taskQueue = new ArrayBlockingQueue(maxThread);
        threadPool = new ArrayList<>();
        this.spawn(maxThread);
    }

    private void spawn(int amount) {
        for (int i = 0; i < amount; ++i) {
            this.threadPool.add(new PoolThread(this.taskQueue));
        }

        for (PoolThread thread : this.threadPool) {
            thread.start();
        }
    }

    public void execute(Runnable task) {
        try {
            if (!this.isStop) {
                this.taskQueue.put(task);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void stop() {
        this.isStop = true;
        for (PoolThread thread : this.threadPool) {
            thread.stopThread();
        }
    }


}
