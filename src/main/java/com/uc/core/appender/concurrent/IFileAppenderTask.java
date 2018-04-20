package com.uc.core.appender.concurrent;

import java.util.concurrent.CountDownLatch;

import com.uc.core.PropertyFileInfo;
/**
 * Worker thread API which will actually update the configuration file.
 * @author Subrata Saha (ssaha2)
 *
 */
public interface IFileAppenderTask extends Runnable {
   void appendKeyValue(PropertyFileInfo item,CountDownLatch latch);
   void stopTask();
}
