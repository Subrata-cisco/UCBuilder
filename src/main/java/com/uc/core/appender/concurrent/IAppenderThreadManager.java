package com.uc.core.appender.concurrent;

import java.util.concurrent.CountDownLatch;

import com.uc.core.PropertyFileInfo;
/**
 * Thread manager API for starting the thread which will update the individual file.
 * @author Subrata Saha (ssaha2)
 *
 */
public interface IAppenderThreadManager {
	void start();
    void addTask(PropertyFileInfo item,CountDownLatch latch);
    void stop();
}
