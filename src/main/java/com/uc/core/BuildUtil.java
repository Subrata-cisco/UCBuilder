package com.uc.core;

import java.util.Arrays;
import java.util.Optional;

import com.uc.core.tag.EnumTag;
/**
 * Some cool util methods.
 * @author Subrata Saha (ssaha2)
 *
 */
public class BuildUtil {
	
	public static boolean isValidTag(String word) {
		return Arrays.stream(EnumTag.values()).filter(e -> e.getValue().startsWith(word)).findFirst().isPresent();
	}
	
	public static String firstFourChar(String line) {
		return line.substring(0, 4);
	}
	
	public static boolean isLineContainsQuote(String line) {
		return line.indexOf("\"") != -1;
	}
	
	public static EnumTag getTag(String word) {
		EnumTag tags = EnumTag.DEFAULT;
		Optional<EnumTag> foundTag = Arrays.stream(EnumTag.values()).filter(
				e -> e.getValue().startsWith(word)
				)
				.findFirst();
		if (foundTag.isPresent()) {
			tags = foundTag.get();
		}
		return tags;
	}

}
