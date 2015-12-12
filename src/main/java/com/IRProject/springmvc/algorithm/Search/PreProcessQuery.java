package com.IRProject.springmvc.algorithm.Search;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import com.IRProject.springmvc.algorithm.Classes.Path;

//import com.IRProject.springmvc.algorithm.Classes.*;
import com.IRProject.springmvc.model.Query;

public class PreProcessQuery {
	private Query query;
	private String indicator;
	private String content;
	
	public PreProcessQuery(Query q) {
		query = q;
		generateIndicateContent();
		
	}

	//Get all the querys' # & content, store them in a list
	private void generateIndicateContent() {
		StringBuilder sb = new StringBuilder();
		if (query.getQuery() != null) {
			indicator = "general";
			sb.append(query.getQuery());
			if (query.getJob() != null) {
				sb.append(query.getJob());
			}
			if (query.getSkill() != null) {
				sb.append(query.getSkill());
			}
			if (query.getUniversity() != null) {
				sb.append(query.getUniversity());
			}
		}
		else if (query.getJob() != null && query.getSkill() == null && query.getUniversity() == null) {
			indicator = "job";
			sb.append(query.getJob());
		}
		else if (query.getSkill() != null && query.getJob() == null && query.getUniversity() == null) {
			indicator = "skill";
			sb.append(query.getSkill());
		}
		else if (query.getUniversity() != null && query.getSkill() == null && query.getJob() == null) {
			indicator = "education";
			sb.append(query.getUniversity());
		} // more than two != null
		else {
			indicator = "general";
			if (query.getJob() != null) {
				sb.append(query.getJob());
			}
			if (query.getSkill() != null) {
				sb.append(query.getSkill());
			}
			if (query.getUniversity() != null) {
				sb.append(query.getUniversity());
			}
		}
		content = sb.toString();
	}
	
	//indicate the kind of the query: job? skill? edu? general?
	public String getIndicator() {	
		return indicator;
	}
	
	//preProcessQuery includes:1)to lower case 2) tokenized 3) remove stop words 4)stemming
	public String preProcessQuery() throws IOException {
		StringBuilder sb = new StringBuilder();
		String str = null;
		
		str = content.toLowerCase();
		
		WordTokenizer tokenizer = new WordTokenizer(str);
		WordStemmer stemmer = new WordStemmer();
		StopWordRemover stopRemover = new StopWordRemover();
		String word = null;
		while((word = tokenizer.nextWord()) != null){
			if(!stopRemover.isStopWord(word))
				sb.append(stemmer.stem(word) + " ");
		}
		return sb.toString();		
	}
	
	//Test case
	public static void main(String[] args) throws Exception{
//		Query q = new Query("computer science", null, null, null);
//		Query q = new Query(null, "Carnegie Mellon", null, null);
//		Query q = new Query(null, null, "data analysis", null);
		Query q = new Query(null, null, null, "Carnegie Mellon data analysis");
		PreProcessQuery prePro = new PreProcessQuery(q);
		String str = prePro.preProcessQuery();
		System.out.println("queryPrePro: " + str);
		
	}
}
/*
	Query q = new Query();
	Class Query should not be created outside the while loop, otherwise the List<Query> list's 4 elements 
	are all the 4th title and ID "progress dysphagia  904"
	The reason is that if "Query q = new Query();" is outside the while loop, all the 4 elements in the list 
	point to the same reference of the object q, which ID & title value is the finally updated 4th element.
	2)Another case with same wrong result would be setting Query's value to be static. Then the value would be 
	shared among objects, and will determined by the final updated value assignment.
 */
