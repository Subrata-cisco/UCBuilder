package com.uc.core.service;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CountDownLatch;
import java.util.function.Consumer;

import com.uc.core.BuilderMetaDataStore;
import com.uc.core.FileReader;
import com.uc.core.PropertyFileInfo;
import com.uc.core.appender.concurrent.IAppenderThreadManager;
import com.uc.core.appender.concurrent.PFAThreadManager;
/**
 * A Service which knows how to start the UC configuration changes to the different files.
 * @author Subrata Saha (ssaha2)
 *
 */
public class UCBuildService extends AbstractBuilderService {
	
	private FileReader fileReader = null;
	private IAppenderThreadManager appenderThreadMgr = null;
	
	public UCBuildService() {
		fileReader = new FileReader(); 
		appenderThreadMgr = new PFAThreadManager();
	}
	
	public UCBuildService(String inputFile) {
		this();
		filePath = inputFile;
	}

	@Override
	protected void startProcessingBuildFile() {
		fileReader.startFileProcessing(filePath);
	}

	@Override
	protected void startAppendingRequestedFile() {
		BlockingQueue<PropertyFileInfo> taskQueue =  BuilderMetaDataStore.getInstance().getPropertyFileInfoQueue();
		appenderThreadMgr.start();
		CountDownLatch latch = new CountDownLatch(taskQueue.size());
		
		Consumer<PropertyFileInfo> consumer = item -> appenderThreadMgr.addTask(item,latch);
		taskQueue.stream().forEach(item -> consumer.accept(item));
		
		try {
			latch.await();
		} catch (InterruptedException e) {
			System.out.println("File modification task interrupted !!");
		}
	}

	@Override
	protected void finish() {
		if(fileReader != null) {
			fileReader.close();
			fileReader = null;
		}
		
		if(appenderThreadMgr != null) {
			appenderThreadMgr.stop();
			appenderThreadMgr = null;
		}
	}
}
