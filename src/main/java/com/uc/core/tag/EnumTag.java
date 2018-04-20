package com.uc.core.tag;
/**
 * Few constant which defined in the metadata files.
 * @author Subrata Saha (ssaha2)
 *
 */
public enum EnumTag {

	START("$start"), PFILE("$pfile"), END("$end"), DEFAULT("");

	private String val;

	EnumTag(String val) {
		this.val = val;
	}

	public String getValue() {
		return val;
	}
	
}
