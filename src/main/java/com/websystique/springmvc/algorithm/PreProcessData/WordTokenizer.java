package com.websystique.springmvc.algorithm.PreProcessData;
/**
 * 
 * @author millerai
 *
 */
public class WordTokenizer {
	String content;
	String[] words;
	int count;
	public WordTokenizer(String content){
		this.content = content;
		words = this.content.replaceAll("[^a-zA-Z ]", " ").split("\\s+");
	}
	public String nextWord(){
		String word = null;
		
		while (count < words.length && words.length > 0) {
			word = words[count];
			count++;
//			System.out.println(count +"times"+ word);
			return word;
		}
		return null;
	}

}
