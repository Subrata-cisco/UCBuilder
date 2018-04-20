package com.uc.core.tag;

import java.io.File;
import java.text.MessageFormat;

import com.uc.core.BuildUtil;
import com.uc.core.BuilderConstants;
import com.uc.core.BuilderMetaDataStore;
import com.uc.core.PropertyFileInfo;
import com.uc.exception.BuilderException;
/**
 * Implementation class of UC metadata tag processing work.
 * @author Subrata Saha (ssaha2)
 *
 */
public class UCTagProcessor implements ITagProcessor {

	private boolean sectionStarted = false;
	private PropertyFileInfo fileInfo = null;
	
	@Override
	public void processTag(String line) {
		String startingTag = BuildUtil.firstFourChar(line);
		EnumTag input = BuildUtil.getTag(startingTag);
		handleTag(input,line);
	}
	
	@Override
	public boolean inProgress() {
		return sectionStarted;
	}
	
	@Override
	public PropertyFileInfo getPropertyFileInfo() {
		return fileInfo;
	}

	private void handleTag(EnumTag inputTag,String line) {
		switch (inputTag) {
			case START:
				processStartTag(inputTag);
				break;
			case PFILE:
				processPFileTag(inputTag,line);
				break;
			case END:
				processEndTag(inputTag);
				break;
			default :
				processKeyValue(line);
		}
	}

	private void processKeyValue(String line) {
		if(!sectionStarted) {
			throw new BuilderException("Invalid line found:"+line);
		}
		int index = line.indexOf(BuilderConstants.EQUAL_SIGN_CHAR);
		if(index >= 0) {
			String key = line.substring(0, index).trim();
			String value = line.substring(index+1,line.length()).trim();
			
			value = MessageFormat.format(value, BuilderMetaDataStore.getInstance().getAllHeaderValues());
			
			fileInfo.setProperty(key, value); 
		}else {
			throw new BuilderException("Invalid line[Reason = not found !!] found:"+line);
		}
	}

	private void processPFileTag(EnumTag inputTag,String line) {
		if(!sectionStarted){
			raiseException("Syntax error File "+BuilderConstants.INPUT_FILE_NAME+" for <$pfile> :"+inputTag);
		}
		
		String rootPath = BuilderMetaDataStore.getInstance().getMetaData(BuilderConstants.ROOT_PATH);
		if(rootPath == null) {
			throw new BuilderException(BuilderConstants.ROOT_PATH +" not found in "+BuilderConstants.INPUT_FILE_NAME);
		}
		
		String value = line.substring(EnumTag.START.getValue().length()+1,line.length());
		if(BuildUtil.isLineContainsQuote(value)) {
			throw new BuilderException(" Quote not allowed in :"+line);
		}
		
		File path = new File(rootPath+value);
		if(!path.exists()) {
			throw new BuilderException("Not a valid file :"+path.getAbsolutePath());
		}
		
		fileInfo = new PropertyFileInfo(path.getAbsolutePath());
	}

	private void processEndTag(EnumTag inputTag) {
		if(!sectionStarted){
			raiseException("Syntax error File "+BuilderConstants.INPUT_FILE_NAME+" for <$end> :"+inputTag);
		}
		sectionStarted = false;
	}

	private void processStartTag(EnumTag inputTag) {
		if(sectionStarted){
			raiseException("Syntax error File "+BuilderConstants.INPUT_FILE_NAME+" for <$start>"+inputTag);
		}
		sectionStarted = true;
	}
	
	private void raiseException(String exMesg){
		throw new BuilderException(exMesg);
	}
}
