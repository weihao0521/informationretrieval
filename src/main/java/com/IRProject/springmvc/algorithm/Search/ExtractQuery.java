package com.IRProject.springmvc.algorithm.Search;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.IRProject.springmvc.algorithm.Classes.*;

public class ExtractQuery {
	List<Query> queries;
	FileReader fr;
	BufferedReader br;
	
	public ExtractQuery() throws FileNotFoundException{
		queries = new ArrayList<Query>();
		fr = new FileReader(Path.QueryDir);
		br = new BufferedReader(fr);
	}

	//Get all the querys' # & content, store them in a list
	public List<Query> GetQueries() throws Exception {

		String queryID = null, s = null;
		String title = null;
		
		if((s = br.readLine()) == null)
			return null;
				
		while(s != null){
			Query q = new Query(); 
			
			//Extract the query #
			if(s.contains("<num>"))
				queryID = s.substring(13).trim();
			
			//Extract the query title as the query content
			else if(s.contains("<title>")){
				title = s.substring(7).trim();
				title = preProcessQuery(title);
				q.SetTopicId(queryID);
				q.SetQueryContent(title);
				queries.add(q);				
			}			
			s = br.readLine();
		}
		br.close();
		return queries;
	}
	
	//preProcessQuery includes:1)to lower case 2) tokenized 3) remove stop words 4)stemming
	private String preProcessQuery(String str) throws IOException{
		StringBuilder sb = new StringBuilder();
		String content = null;
		
		content = str.toLowerCase();
		
		WordTokenizer tokenizer = new WordTokenizer(content);
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
		ExtractQuery queries = new ExtractQuery();
		queries.GetQueries();
		for(Query q : queries.queries)
			System.out.println("queryID: " + q.GetTopicId() + "queryContent: " + q.GetQueryContent());
		
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
