package me.zero.twotakeonetool.lang;

import me.zero.twotakeonetool.FileLoader;
import me.zero.twotakeonetool.config.FileConfiguration;

public class Language {

	public static String getTranslatedString(LanguageKey key) {		
		FileConfiguration lang = FileLoader.loadLanguageStorage();
		return lang.getSettings().get(key.name()).toString();
	}
	
}
