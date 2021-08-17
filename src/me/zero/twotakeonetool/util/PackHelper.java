package me.zero.twotakeonetool.util;

public class PackHelper {

	public static String createOldFileName(String newName) {
		int index = newName.indexOf('_');
		if(index == -1) return newName;
		return newName.substring(index);
	}
	
}
