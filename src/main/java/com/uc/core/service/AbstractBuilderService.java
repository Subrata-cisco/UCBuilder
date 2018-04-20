package com.uc.core.service;

import java.io.File;

import com.uc.core.BuilderConstants;
import com.uc.exception.BuilderException;

/**
 * Template class for initiating the sequence of the service and doing home work.
 * 
 * @author ssaha2
 *
 */

public abstract class AbstractBuilderService implements IBuilderService {
	
	private final String defaultFilePath = BuilderConstants.INPUT_FILE_NAME;
	protected String filePath = null;
	
	// No support to stop building if it is taking time ..
	public void startService() {
		boolean fileFound = false;
		if(filePath == null) {
			filePath = defaultFilePath;
		}
		
		File file = new File(filePath);
		fileFound = file.exists();
		
		if(!fileFound) {
			throw new BuilderException("Not changing the configuration file as File not found !! [ "+filePath+" ]");
		}
		
		startProcessingBuildFile();
		startAppendingRequestedFile();
		finish();
	}
	
	protected abstract void startProcessingBuildFile();
	protected abstract void startAppendingRequestedFile();
	protected abstract void finish();
}
