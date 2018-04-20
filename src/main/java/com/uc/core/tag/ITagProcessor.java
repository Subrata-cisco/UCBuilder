package com.uc.core.tag;

import com.uc.core.PropertyFileInfo;
/**
 * Interface file for tag processing work of the metadata.
 * @author Subrata Saha (ssaha2)
 *
 */
public interface ITagProcessor {
	public void processTag(String line);
	public boolean inProgress();
	public PropertyFileInfo getPropertyFileInfo();
}
