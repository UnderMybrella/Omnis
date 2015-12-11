package org.abimon.omnis.lang;

import java.util.HashMap;

@SuppressWarnings("unused")
public class Language {

	private String sentanceStructure = "subject tense verb object";

	private HashMap<String, HashMap<String, String>> otherToThis = new HashMap<String, HashMap<String, String>>();

	public String reorder(String sentance){
		return sentance;
	}

	public static String difference(String findingString, String baseString){
		System.out.println("Difference between " + findingString + " and " + baseString);
		int startIndex = -1;
		for(int i = 0; i < Math.min(findingString.length(), baseString.length()); i++)
			if(findingString.toCharArray()[i] != baseString.toCharArray()[i])
				if(startIndex == -1)
					startIndex = i;
				else if(findingString.length() != baseString.length() && baseString.endsWith(findingString.substring(i)))
					return findingString.substring(startIndex, i);
				else;
			else
				if(startIndex != -1)
					return findingString.substring(startIndex, i);
		return findingString.substring(startIndex);
	}

	public void learn(String otherLanguage, String otherSplitter, String splitter, String[] languageToLearn, String[] existing){
		int len = Math.min(languageToLearn.length, existing.length);
		int differingPos = 0;
		String samePart = "";
		for(int i = 0; i < len; i++)
		{
			if(i == len - 1)
			{
				if(splitter.equals("")){
					String differ = difference(languageToLearn[i], languageToLearn[i - 1]);
					String other = "";
					if(otherSplitter.equals(""));
					else{
						int splitLen = Math.min(existing[i].split(otherSplitter).length, existing[i - 1].split(otherSplitter).length);
						for(int j = 0; j < splitLen; j++)
						{
							if(!existing[i].split(otherSplitter)[j].toLowerCase().equals(existing[i-1].split(otherSplitter)[j].toLowerCase())){
								other = existing[i].split(otherSplitter)[j].toLowerCase();
								System.out.println(existing[i].split(otherSplitter)[j] + " vs " + existing[i-1].split(otherSplitter)[j]);
								break;
							}
						}
					}

					addWord(otherLanguage, other, differ);
				}
				else
					for(int j = 0; j < languageToLearn[i].split(splitter).length; j++)
					{

					}
			}
			else{
				if(splitter.equals("")){
					String differ = difference(languageToLearn[i], languageToLearn[i + 1]);
					String other = "";
					if(otherSplitter.equals(""));
					else{
						int splitLen = Math.min(existing[i].split(otherSplitter).length, existing[i + 1].split(otherSplitter).length);
						for(int j = 0; j < splitLen; j++)
						{
							if(!existing[i].split(otherSplitter)[j].toLowerCase().equals(existing[i+1].split(otherSplitter)[j].toLowerCase())){
								other = existing[i].split(otherSplitter)[j].toLowerCase();
								System.out.println(existing[i].split(otherSplitter)[j] + " vs " + existing[i+1].split(otherSplitter)[j]);
								break;
							}
						}
					}

					addWord(otherLanguage, other, differ);
				}
				else
					for(int j = 0; j < languageToLearn[i].split(splitter).length; j++)
					{

					}
			}
		}
	}

	public String translate(String language, String phrase, String languageSplitter, String joiner){
		String s = "";
		if(languageSplitter.equals("")){}
		else
			for(String word : phrase.toLowerCase().split(languageSplitter))
			{	
				s += translate(language, word) + joiner;
				System.out.println("Working so far: " + s + ". Just translated " + word);
			}
		return s;
	}

	public String translate(String language, String word){
		HashMap<String, String> lang = otherToThis.containsKey(language) ? otherToThis.get(language) : new HashMap<String, String>();
		otherToThis.put(language, lang);
		return lang.containsKey(word) ? lang.get(word) : "";
	}

	private void addWord(String language, String wordOther, String translation){
		HashMap<String, String> lang = otherToThis.containsKey(language) ? otherToThis.get(language) : new HashMap<String, String>();
		lang.put(wordOther, translation);
		otherToThis.put(language, lang);
		System.out.println(language + ": " + wordOther + " now translates to " + translation);
	}

}
