package com.uc.exception;
/**
 * Single exception class to raise concerns !!
 * @author Subrata Saha (ssaha2)
 *
 */
public class BuilderException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public BuilderException(String error){
		super(error);
	}
	
}
