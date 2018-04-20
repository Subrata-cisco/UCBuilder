package com.uc.core.appender.concurrent;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.uc.core.BuilderConstants;
import com.uc.core.PropertyFileInfo;
import com.uc.exception.BuilderException;

/**
 * Property File Appender thread Task.
 *  1. Create the temp file where modification will be done.
 *  2. Get the modified map where all the updates will be available and write back to temp file created.
 *  3. Take the backup of the original file.
 *  4. Rename temp file to original file.
 *  5. notify that all work is done.
 * 
 * @author ssaha2
 *
 */
public class PFATask implements IFileAppenderTask {
	
	private volatile boolean canStartWork = true;
	private PropertyFileInfo item = null;
	private CountDownLatch latch = null;
			
	@Override
	public void run() {
		if(canStartWork) {
			appendProperty();
		}
	}

	@Override
	public void appendKeyValue(PropertyFileInfo item,CountDownLatch latch) {
		this.item = item;
		this.latch = latch;
	}

	@Override
	public void stopTask() {
		canStartWork = false;
		item = null;
	}
	
	private void appendProperty() {
		System.out.println("Working on :"+item.getFilePath()+" by :"+Thread.currentThread().getName());
		// 1. Create the temp file where modification will be done.
		String originalFilePath = item.getFilePath();
		File tempFile = new File(originalFilePath+".tmp");
		createTempFile(tempFile);
		
		// 2. Get the modified map where all the updates will be available and write back to temp file created.
		createTempFileWithModifiedContent(originalFilePath, tempFile);
		
		// 3. Take the backup of the original file.
		renameFile(originalFilePath,originalFilePath+".bak");

		// 4. Rename temp file to original file.
		renameFile(tempFile.getAbsolutePath(),originalFilePath);
		
		// 5. notify that all work is done.
		if(latch != null) {
			latch.countDown();
		}
		System.out.println(originalFilePath+" file is updated !!");
	}

	private void createTempFile(File tempFile) {
		if(!tempFile.exists()) {
			try {
				boolean created = tempFile.createNewFile();
				if(!created) {
					throw new BuilderException("Error in creating temp file .. Please make the source folder writable !!");
				}
			} catch (IOException e) {
				throw new BuilderException("Error in creating temp file(Give folder write permission) :"+tempFile.getAbsolutePath());
			}
		}
	}

	private void createTempFileWithModifiedContent(String originalFilePath, File tempFile) {
		try (Stream<String> inputFile = Files.lines(Paths.get(originalFilePath));
				PrintWriter outputTempFile = new PrintWriter(tempFile, "UTF-8")) {
			
			Map<String, String> existingMap = inputFile
					.filter(line -> !line.startsWith("#"))
					.filter(line -> !line.trim().isEmpty()) 
					.collect(Collectors.toMap(
							(key -> key.substring(0, key.indexOf(BuilderConstants.EQUAL_SIGN_CHAR)).trim()),
							value -> value.substring(value.indexOf(BuilderConstants.EQUAL_SIGN_CHAR) + 1, value.length()).trim()));					
							
			
			//existingMap.forEach((x, y) -> System.out.println("Key: " + x +", value: "+ y));
			
			item.getProperty().forEach((x,y) -> existingMap.merge(x, y, (ov,nv) -> nv)); 
			
			existingMap.forEach((key, value) -> {
		       outputTempFile.write(key + BuilderConstants.EQUAL_SIGN_CHAR + value + System.lineSeparator()); 
		    });
			
		}catch(Exception ex) {
			throw new BuilderException("Error in updating temp file to be replaced... error :"+ex.getMessage());
		}
	}

	private void renameFile(String src,String dest) {
		File srcFile = new File(src);
		File destFile = new File(dest);
		
		if(destFile.exists()) {
			boolean isDeleted = destFile.delete();
			if(!isDeleted) {
				throw new BuilderException("Could not delete the file :"+destFile.getAbsolutePath());
			}
		}
		
		boolean isRenamed = srcFile.renameTo(destFile); 
		if(!isRenamed) {
			throw new BuilderException("Could not rename the file :"+src+" to :"+dest);
		}
	}

}
