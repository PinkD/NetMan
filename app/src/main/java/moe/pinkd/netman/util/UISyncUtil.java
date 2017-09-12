package moe.pinkd.netman.util;


import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import java.util.HashMap;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * Created by PinkD on 2017/9/12.
 * to run code in background thread
 */

public class UISyncUtil {
    private static final String TAG = "UISyncUtil";
    private final static Handler handler = new Handler(Looper.getMainLooper());
    private static ExecutorService threadPool;
    private static Queue<Runnable> taskQueue = new LinkedBlockingQueue<>();
    private static Thread queueThread;
    private static Map<Runnable, Callback> callMap = new HashMap<>();
    private static boolean terminated;
    private static final Object LOCK = new Object();

    private static void notifyPool() {
        if (threadPool == null) {
            initPool();
        }
        synchronized (LOCK) {
            LOCK.notify();
        }
    }

    private static void initPool() {
        threadPool = Executors.newSingleThreadExecutor();
        queueThread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    if (taskQueue.isEmpty()) {
                        synchronized (LOCK) {
                            try {
                                LOCK.wait();
                            } catch (InterruptedException ignored) {
                                if (terminated) {
                                    Log.d(TAG, "queueThread: stopped");
                                    break;
                                }
                            }
                        }
                    } else {
                        Runnable task = taskQueue.poll();
                        Callback callback = callMap.get(task);
                        Future<Callback> future = threadPool.submit(task, callback);
                        try {
                            handler.post(future.get(callback.time, TimeUnit.MILLISECONDS).success);
                        } catch (InterruptedException | ExecutionException | TimeoutException e) {
                            handler.post(callMap.get(task).fail);
                            Log.d(TAG, "run task: " + e.toString());
                        } finally {
                            callMap.remove(task);
                        }
                    }
                }
            }
        });
        queueThread.start();
    }

    public static void runInBackground(Runnable task, Callback callback) {
        taskQueue.add(task);
        callMap.put(task, callback);
        notifyPool();
    }

    public static void terminate() {
        terminated = true;
        queueThread.interrupt();
    }

    public static class Callback {
        private Runnable success;
        private Runnable fail;
        private int time = 5000;

        public Callback(Runnable success, Runnable fail) {
            this.success = success;
            this.fail = fail;
        }

        public Callback(Runnable success, Runnable fail, int time) {
            this.time = time;
            this.success = success;
            this.fail = fail;
        }
    }

}
