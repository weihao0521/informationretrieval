package com.IRProject.springmvc.algorithm.PreProcessData;

import com.IRProject.springmvc.algorithm.Classes.Stemmer;

//Convert terms into lower case and 
public class WordStemmer {
	char[] word;
	public String stem(String term){
		word = term.toCharArray();
		Stemmer stem = new Stemmer();
		stem.add(word, word.length);
		stem.stem();
		return stem.toString();
	}

}
