package com.uc.core;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.stream.Collectors;
/**
 * In memory information of the changes which user has requested in metadata file.
 * 1. Reader will store the information.
 * 2. Thread manager -> Worker thread will use this information to update the configuration.
 * @author Subrata Saha (ssaha2)
 *
 */
public class BuilderMetaDataStore {
	
	private static BuilderMetaDataStore store = null;
	private Map<String, String> metaDataMap;
	private BlockingQueue<PropertyFileInfo> propertyFileInfoQueue;
	private Map<String,PropertyFileInfo> pathToFileInfo = null;
	
	private BuilderMetaDataStore() {
		metaDataMap = new LinkedHashMap<>();
		propertyFileInfoQueue = new LinkedBlockingQueue<>();
		pathToFileInfo = new HashMap<>();
	}
	
	public static BuilderMetaDataStore getInstance() {
		return BuilderMetaDataStoreHolder.getStore();
	}
	
	private static class BuilderMetaDataStoreHolder {
		public static BuilderMetaDataStore getStore() {
			if(store == null) {
				store = new BuilderMetaDataStore();
			}
			return store;
		}
	}

	public void setPropertyInfo(PropertyFileInfo fileInfo) {
		propertyFileInfoQueue.add(fileInfo);
		pathToFileInfo.put(fileInfo.getFilePath(), fileInfo);
	}

	public void setMetaData(String key, String value) {
		metaDataMap.put(key, value);
	}

	public String getMetaData(String key) {
		return metaDataMap.get(key); 
	}

	public BlockingQueue<PropertyFileInfo> getPropertyFileInfoQueue() {
		return propertyFileInfoQueue;
	}
	
	public PropertyFileInfo getPropertyFileInfo(String path) {
		return pathToFileInfo.get(path); 
	}
	
	public Object[] getAllHeaderValues() {
		return metaDataMap.values().stream().collect(Collectors.toList()).toArray();
	}
}
