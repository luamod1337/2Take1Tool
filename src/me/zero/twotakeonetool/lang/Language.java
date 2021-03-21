package me.zero.twotakeonetool.lang;

import me.zero.twotakeonetool.FileLoader;
import me.zero.twotakeonetool.config.FileConfiguration;

public class Language {

	public static String getTranslatedString(LanguageKey key) {		
		FileConfiguration lang = FileLoader.loadLanguageStorage();
		String translated = lang.getSettings().get(key.name()).toString();
		if(translated == null) {
			//error, config old!
			System.out.println("error, config outdated");
		}
		return translated;
	}
	
}
