package com.IRProject.springmvc.algorithm.PreProcessData;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import com.IRProject.springmvc.algorithm.Classes.Path;

public class StopWordRemover {
	Set<String> stopWords;
	FileReader fr;
	BufferedReader br;
	
	public StopWordRemover() throws IOException{
		stopWords = new HashSet<String>();
		fr = new FileReader(Path.StopwordDir);
		br = new BufferedReader(fr);
		String s = null;
		while ((s = br.readLine())!=null){
			stopWords.add(s.trim());
		}
	}
	
	public boolean isStopWord(String word){
		return stopWords.contains(word);
	}
}
