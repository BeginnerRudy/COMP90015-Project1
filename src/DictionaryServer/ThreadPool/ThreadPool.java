/*
 * @Author: Renjie Meng
 * @Student ID: 877396
 * */

package DictionaryServer.ThreadPool;


import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * This class aims to provide the dictionary server a thread pool architecture.
 * With this class, the server could construct thread as it turning on. This class
 * mainly uses the BlockingQueue to achieve thread pool's functionality.
 */
public class ThreadPool {
    private BlockingQueue taskQueue;

    private List<PoolThread> threadPool;

    boolean isStop = false;
    /**
     * @param maxThread The maximum size of the thread pool
     * This is the constructor of the thread pool.
     */
    public ThreadPool(int maxThread) {
        taskQueue = new ArrayBlockingQueue(maxThread);
        threadPool = new ArrayList<>();
        this.spawn(maxThread);
    }

    /**
     * @param amount The number of threads.
     *
     * This method aims to create and start given amount of thread in the thread pool.
     */
    private void spawn(int amount) {
        for (int i = 0; i < amount; ++i) {
            this.threadPool.add(new PoolThread(this.taskQueue));
        }

        for (PoolThread thread : this.threadPool) {
            thread.start();
        }
    }

    /**
     * @param task The Runnable task to be executed by the thread pool.
     * This method is used to submit new Runnable task to the thread pool/
     */
    public void execute(Runnable task) {
        try {
            // If the thread pool is already stop, tell the server.
            if (this.isStop) throw new IllegalStateException("Thread pool is terminated!");
            this.taskQueue.put(task);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public List<PoolThread> getThreads() {
        return threadPool;
    }

    /**
     * This method is used to terminate all the execution of the thread pool.
     */
    public void stop() {
        this.isStop = true;
        for (PoolThread thread : this.threadPool) {
            thread.stopThread();
        }
    }


}
