package com.uc.core;

import com.uc.core.tag.ITagProcessor;
import com.uc.core.tag.UCTagProcessor;
import com.uc.exception.BuilderException;

/**
 * File Processor process each file and accumulate the different info. 
 * @author Subrata Saha (ssaha2)
 *
 */
public class UCBuildFileProcessor implements IFileProcessor {

	private static final String EQUAL_SIGN_CHAR = "=";
	private ITagProcessor tagProcessor;

	public UCBuildFileProcessor() {
		tagProcessor = new UCTagProcessor();
	}

	@Override
	public void processEachLine(String line) {
		if (!line.startsWith("#")) {
			process(line);
		}
	}
	
	@Override
	public void destruct() {
		tagProcessor = null;
	}

	private void process(String line) {
		line = line.trim();
		if (BuildUtil.isValidTag(BuildUtil.firstFourChar(line)) || tagProcessor.inProgress()) {
			tagProcessor.processTag(line);
			if(!tagProcessor.inProgress()) {
				PropertyFileInfo fileInfo = tagProcessor.getPropertyFileInfo();
				BuilderMetaDataStore.getInstance().setPropertyInfo(fileInfo);
			}
		} else {
			processInfo(line);
		}
	}
	
	private void processInfo(String line) {
		if(line!= null && line.contains(EQUAL_SIGN_CHAR)) {
			int index = line.indexOf(EQUAL_SIGN_CHAR);
			String key = line.substring(0, index).trim();
			String value = line.substring(index+1,line.length()).trim();
			
			if(BuildUtil.isLineContainsQuote(value)) {
				throw new BuilderException(" Quote not allowed in :"+line);
			}
			
			BuilderMetaDataStore.getInstance().setMetaData(key,value);
		}
	}
}
