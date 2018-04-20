package com.uc.core;
/**
 * Some processor API which will used by client file for metadat file processing work.
 * @author Subrata Saha (ssaha2)
 *
 */
public interface IFileProcessor {
	void processEachLine(String line);
	void destruct();
}
