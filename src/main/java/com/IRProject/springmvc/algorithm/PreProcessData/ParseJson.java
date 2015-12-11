package com.IRProject.springmvc.algorithm.PreProcessData;

import org.json.simple.*;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import com.IRProject.springmvc.algorithm.Classes.*;
//import java.util.*;
/*
 * create job.txt or education.txt or skill.txt according to users' needs
 */
public class ParseJson {
	private FileReader fr;
	private BufferedReader br;
	private FileWriter fw;
	private BufferedWriter bw;
	private JSONParser jsonParser;
	private int count;
	private Map<String, String> idPerson;
	private StopWordRemover stopwordRemover;
	private WordStemmer stemmer;
	
	public ParseJson() throws IOException {
		fr = new FileReader(Path.DataDir);
		br = new BufferedReader(fr);
		jsonParser = new JSONParser();
		stopwordRemover = new StopWordRemover();
		stemmer = new WordStemmer();
		count = 0;
		idPerson = new HashMap<String, String>();
	}
	
	//education + summary
	public void getEdu() throws IOException, ParseException {
		fw = new FileWriter(Path.ResultDir + "education.txt");
		bw = new BufferedWriter(fw);
		JSONObject education = null;
		String s = "", summary = "", id = "";
		
		while ((s = br.readLine()) != null) {
			StringBuilder eduStr = new StringBuilder();
			JSONObject jsonObj = (JSONObject)jsonParser.parse(s);
			education = (JSONObject)jsonObj.get("education");
			id = (String)jsonObj.get("_id");
			count++;
			
			//If does not have "education", then skip the person
			if (education == null || id == null) 
				continue;
			
			Iterator<String> iterator = education.keySet().iterator();
				
			//extract all the school names
			while (iterator.hasNext()) {
				String idx = iterator.next();
				JSONObject school = (JSONObject)education.get(idx);
				String eduName = (String)school.get("edu_institution");
				if (eduName != null) {
					eduStr.append(eduName + " ");
				}					
			}
			
			summary = (String)jsonObj.get("summary");
			if (summary != null) 
				eduStr.append(summary);
			
			bw.append(id + "\n");
			
			//Change all the content string to lowercase
			String content = eduStr.toString().toLowerCase();
			
			//Preprocess the string: tokenize + remove stopword + stemming
			WordTokenizer tokenizer = new WordTokenizer(content);
			String word = null;
			while ((word = tokenizer.nextWord()) != null) {
				if (!stopwordRemover.isStopWord(word)) 
					bw.append(stemmer.stem(word) + " ");
			}
			bw.append("\n");

			if (count % 1000 == 0)
				System.out.println("Finished " + count + " documents!");
		}
		close();
	}
	
	//experience(company + job_title) + headline + industry + locality + summary
	public void getJob() throws IOException, ParseException {
		fw = new FileWriter(Path.ResultDir + "job.txt");
		bw = new BufferedWriter(fw);
		JSONObject experience = null;
		String s = "", jobTitle = "", compName = "", summary = "";
		String locale = "", headline = "", industry = "", id = "";
		
		while ((s = br.readLine()) != null) {
			JSONObject jsonObj = (JSONObject)jsonParser.parse(s);
			StringBuilder jobStr = new StringBuilder();		
			experience = (JSONObject)jsonObj.get("experience");
			id = (String)jsonObj.get("_id");
			count++;
			
			//Skip the person if there is no experience
			if (experience == null || id == null) 
				continue;
			
			Iterator<String> iterator = experience.keySet().iterator(); 
			
			//extract company_name + job_title
			while (iterator.hasNext()) {
				String idx = iterator.next();
				JSONObject comInfo = (JSONObject)experience.get(idx);
				JSONArray company = (JSONArray)comInfo.get("company");
				if (company == null || company.size() == 0)
					continue;
				
				//get the company name
				compName = (String)company.get(0);
				jobStr.append(compName + " ");
				
				//get the job title
				jobTitle = (String)comInfo.get("job_title");
				if (jobTitle != null) 	
					jobStr.append(jobTitle + " ");
			}
			
			//extract the summary
			summary = (String)jsonObj.get("summary");
			if (summary != null) {
				jobStr.append(summary + " ");
			}
			
			//extract the locality
			locale = (String)jsonObj.get("locality");
			if (locale != null) {		
				jobStr.append(locale + " ");
			}
			
			//extract the headline
			headline = (String)jsonObj.get("headline");
			if (headline != null) {
				jobStr.append(headline + " ");
			}
			
			//extract the industry
			industry = (String)jsonObj.get("industry");
			if (industry != null) {
				jobStr.append(industry);
			}
			
			bw.append(id + "\n");
			
			//convert the string to lower case
			String content = jobStr.toString().toLowerCase();
			
			//Preprocess the string: tokenize + remove stopword + stemming
			WordTokenizer tokenizer = new WordTokenizer(content);
			String word = null;
			while ((word = tokenizer.nextWord()) != null) {
				if (!stopwordRemover.isStopWord(word)) 
					bw.append(stemmer.stem(word) + " ");
			}
			bw.append("\n");

			if (count % 1000 == 0)
				System.out.println("Finished " + count + " documents!");
		}
		close();
	}
	
	//skills + the current company + locality + summary
	public void getSkill() throws IOException, ParseException {
		String s = "";
		fw = new FileWriter(Path.ResultDir + "skill.txt");
		bw = new BufferedWriter(fw);
		JSONArray skillSet = null;
		String summary = "", id = "", locale = "";

		while ((s = br.readLine()) != null) {
			StringBuilder sklStr = new StringBuilder();
			JSONObject jsonObj = (JSONObject)jsonParser.parse(s);
			skillSet = (JSONArray)jsonObj.get("skills");
			id = (String)jsonObj.get("_id");
			count++;
			
			//Skip the person if the skillset is null
			if (skillSet == null || id == null) {
				continue;
			}
			
			//Skills return an array of skills
			int size = skillSet.size();
			for (int i = 0; i < size; i++) {
				String skill = (String)skillSet.get(i);
				sklStr.append(skill + " ");
			}
			
			//get the current company
			JSONObject experience = (JSONObject)jsonObj.get("experience");
			if (experience != null) {
				JSONObject first = (JSONObject)experience.get("1");
				if (first != null) {
					JSONArray currtComp = (JSONArray)first.get("company");
					if (currtComp != null && currtComp.size() != 0) {
						sklStr.append((String)currtComp.get(0) + " ");
					}
				}
			}
			
			//get the current position
			locale = (String)jsonObj.get("locality");
			if (locale != null) {
				sklStr.append(locale + " ");
			}
			
			//get the summary
			summary = (String)jsonObj.get("summary");
			if (summary != null) {
				sklStr.append(summary);
			}
			
			bw.append(id + "\n");
			
			//convert the string to lower case
			String content = sklStr.toString().toLowerCase();
			
			//Preprocess the string: tokenize + remove stopword + stemming
			WordTokenizer tokenizer = new WordTokenizer(content);
			String word = null;
			while ((word = tokenizer.nextWord()) != null) {
				if (!stopwordRemover.isStopWord(word)) 
					bw.append(stemmer.stem(word) + " ");
			}
			bw.append("\n");
			
			if (count % 1000 == 0)
				System.out.println("Finished " + count + " documents!");
		}
		close();
	}
	
	//education + experience(company + job_title) + headline + industry + locality + summary + skills
	public void getGeneral() throws IOException, ParseException {
		fw = new FileWriter(Path.ResultDir + "general.txt");
		bw = new BufferedWriter(fw);
		JSONObject education = null;
		JSONObject experience = null;
		JSONArray skillSet = null;
		String s = "", jobTitle = "", compName = "", summary = "";
		String locale = "", headline = "", industry = "", id = "";
		
		while ((s = br.readLine()) != null) {
			StringBuilder generalStr = new StringBuilder();
			JSONObject jsonObj = (JSONObject)jsonParser.parse(s);
			education = (JSONObject)jsonObj.get("education");
			experience = (JSONObject)jsonObj.get("experience");
			skillSet = (JSONArray)jsonObj.get("skills");
			id = (String)jsonObj.get("_id");
			count++;
			
			//If does not have "id", or no general information, then skip the person
			if (id == null) 
				continue;
			
			//if "edu" != null, extract all the school names
			if (education != null) {
				Iterator<String> iterator = education.keySet().iterator();		
				while (iterator.hasNext()) {
					String idx = iterator.next();
					JSONObject school = (JSONObject)education.get(idx);
					String eduName = (String)school.get("edu_institution");
					if (eduName != null) {
						generalStr.append(eduName + " ");
					}					
				}
			}
			
			//if "experience" != null, extract company_name + job_title
			if (experience != null) {
				Iterator<String> iterator = experience.keySet().iterator(); 				
				while (iterator.hasNext()) {
					String idx = iterator.next();
					JSONObject comInfo = (JSONObject)experience.get(idx);
					JSONArray company = (JSONArray)comInfo.get("company");
					if (company == null || company.size() == 0)
						continue;
					
					//get the company name
					compName = (String)company.get(0);
					generalStr.append(compName + " ");
					
					//get the job title
					jobTitle = (String)comInfo.get("job_title");
					if (jobTitle != null) 	
						generalStr.append(jobTitle + " ");
				}
			}
			
			//if array "skillSet" != null, extract all the skills
			if (skillSet != null) {
				int size = skillSet.size();
				for (int i = 0; i < size; i++) {
					String skill = (String)skillSet.get(i);
					generalStr.append(skill + " ");
				}
			}
			
			//extract the locality
			locale = (String)jsonObj.get("locality");
			if (locale != null) {		
				generalStr.append(locale + " ");
			}
			
			//extract the headline
			headline = (String)jsonObj.get("headline");
			if (headline != null) {
				generalStr.append(headline + " ");
			}
			
			//extract the industry
			industry = (String)jsonObj.get("industry");
			if (industry != null) {
				generalStr.append(industry);
			}
			
			//extract the summary
			summary = (String)jsonObj.get("summary");
			if (summary != null) 
				generalStr.append(summary);
			
			bw.append(id + "\n");
			
			//Change all the content string to lowercase
			String content = generalStr.toString().toLowerCase();
			
			//Preprocess the string: tokenize + remove stopword + stemming
			WordTokenizer tokenizer = new WordTokenizer(content);
			String word = null;
			while ((word = tokenizer.nextWord()) != null) {
				if (!stopwordRemover.isStopWord(word)) 
					bw.append(stemmer.stem(word) + " ");
			}
			bw.append("\n");

			if (count % 1000 == 0)
				System.out.println("Finished " + count + " documents!");
		}
		close();
	}
	
	//close BufferedReader and BufferedWriter
	private void close() throws IOException {
		br.close();
		bw.close();
	}	
	public static void main(String[] args) throws IOException, ParseException {
		ParseJson parse = new ParseJson();
//		parse.getEdu();
//		parse.getSkill();
//		parse.getJob();
		parse.getGeneral();
	}
}
