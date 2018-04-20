package com.uc.core;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
/**
 * VO class for each tag.
 * @author Subrata Saha (ssaha2)
 *
 */
public class PropertyFileInfo {

	private String filePath ;
	
	private Map<String,String> map = new HashMap<>();
	
	public PropertyFileInfo(String path){
		this.filePath = path;
	}
	
	public Map<String,String> getProperty(){
		return map;
	}
	
	public void setProperty(String key,String value){
		map.put(key, value);
	}

	public String getFilePath() {
		return filePath;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		Iterator<String> it = map.keySet().iterator();
		while(it.hasNext()){
			String key = it.next();
			sb.append(key).append("=").append(map.get(key));
		}
		return "PropertyInfo [updating file :" + filePath + ", with values :" + sb.toString() + "]";
	}
	
	
	
}
