package com.uc.core.appender.concurrent;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import com.uc.core.PropertyFileInfo;
/**
 * Thread manager implementation for starting the thread which invoke all the worker thread.
 * @author Subrata Saha (ssaha2)
 *
 */
public class PFAThreadManager implements IAppenderThreadManager {

	private ThreadPoolExecutor threadPoolExecuter = null;
	private BlockingQueue<Runnable> workQueue = null;
	private final static long THREAD_KEEP_ALIVE_TIME = 60L;
	
	@Override
	public void start() {
		workQueue = new LinkedBlockingQueue<>();
		threadPoolExecuter = new ThreadPoolExecutor(5, 5, THREAD_KEEP_ALIVE_TIME, TimeUnit.SECONDS, workQueue);
	}
	
	@Override
	public void addTask(PropertyFileInfo item,CountDownLatch latch) {
		PFATask task = new PFATask();
		task.appendKeyValue(item,latch);
		threadPoolExecuter.execute(task);
	}
	
	@Override
	public void stop() {
		threadPoolExecuter.shutdownNow();
        threadPoolExecuter = null;
        workQueue.clear();
        workQueue = null;
	}
}
