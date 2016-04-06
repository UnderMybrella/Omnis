package org.abimon.omnis.util;

import java.util.HashMap;

import org.abimon.omnis.net.Website;

public class Translate {

	public static HashMap<String, String> languageMap = new HashMap<String, String>();

	static{
		languageMap.put("detect", "auto");

		languageMap.put("afrikaans", "af");
		languageMap.put("albanian", "sq");
		languageMap.put("amharic", "am");
		languageMap.put("arabic", "ar");
		languageMap.put("armenian", "hy");
		languageMap.put("azerbaijani", "az");

		languageMap.put("basque", "eu");
		languageMap.put("belarusian", "be");
		languageMap.put("bengali", "bn");
		languageMap.put("bosnian", "bs");
		languageMap.put("bulgarian", "bg");

		languageMap.put("catalan", "ca");
		languageMap.put("cebuano", "ceb");
		languageMap.put("chickewa", "ny");
		languageMap.put("chinese", "zh-CN");

		languageMap.put("english", "en");
	}

	public static String translate(String languageFrom, String languageTo, String phrase){

		languageFrom = languageMap.containsKey(languageFrom) ? languageMap.get(languageFrom) : languageFrom;
		languageTo = languageMap.containsKey(languageTo) ? languageMap.get(languageTo) : languageTo;

		Website website = new Website("https://translate.google.com/#" + languageFrom + "/" + languageTo + "/" + phrase);

		String newPhrase = "";

		return newPhrase;
	}
}
