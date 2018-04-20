package com.uc.core;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;

import com.uc.exception.BuilderException;

/**
 * Read a file and process each tag of the file..
 * @author ssaha2
 *
 */
public class FileReader {
	
	private UCBuildFileProcessor fileProcessor = null;
	
	public FileReader() {
		fileProcessor = new UCBuildFileProcessor();
	}
	
	public void startFileProcessing(String path){
		File file = new File(path);
		if(!file.exists()){
			throw new BuilderException("File not found ::"+BuilderConstants.INPUT_FILE_NAME+" , Please keep the file in the same folder of the jar..");
		}
		
		try(FileInputStream fis = new FileInputStream(file);
			BufferedReader br = new BufferedReader(new InputStreamReader(fis))) {
			
			String line = null;
			while ((line = br.readLine()) != null) {
				if(!line.trim().isEmpty()) {
					fileProcessor.processEachLine(line);
				}
			}
		 
		}catch(Exception e) {
			throw new BuilderException("Error ::"+e.getMessage()+" , terminating ....");
		} 
	}
	
	public void close() {
		fileProcessor.destruct();
		fileProcessor = null;
	}

}
