package com.IRProject.springmvc.algorithm.PreProcessData;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.IRProject.springmvc.algorithm.Classes.Path;

public class ParseJson2 {
	private FileReader fr;
	private BufferedReader br;
	private FileWriter fw;
	private BufferedWriter bw;
	private JSONParser jsonParser;
	private int count;
	private Map<String, String> idPerson;
	private StopWordRemover stopwordRemover;
	private WordStemmer stemmer;
	
	public ParseJson2() throws IOException {
		fr = new FileReader(Path.TestDir);
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
//			System.out.println("s1: " + s1);
			StringBuilder eduStr = new StringBuilder();
			JSONObject jsonObj = (JSONObject)jsonParser.parse(s);
			education = (JSONObject)jsonObj.get("education");
			id = (String)jsonObj.get("_id");
			count++;
//			System.out.println("id: " + id);
			
			//If does not have "education", then skip the person
			if (education == null || id == null) {
				continue;
			}
			
			bw.append(id + "\n");
			
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
			if (summary != null) {
				eduStr.append(summary);
			}
			
			//Change all the content string to lowercase
			String content = eduStr.toString().toLowerCase();
			
			// initiate the WordTokenizer class
			WordTokenizer tokenizer = new WordTokenizer(content);
			String word = null;
			while ((word = tokenizer.nextWord()) != null) {
				if (!stopwordRemover.isStopWord(word)) {
					bw.append(stemmer.stem(word) + " ");
				}
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
			count++;		
			experience = (JSONObject)jsonObj.get("experience");
			id = (String)jsonObj.get("_id");
//			System.out.println("The " + count + " Id: " + id);
			
			//Skip the person if there is no experience
			if (experience == null || id == null) {
//				System.out.println("jsonObj.get(\"experience\")" + experience);
				continue;
			}
			
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
//				System.out.print("compName: " + compName);
				
				//get the job title
				jobTitle = (String)comInfo.get("job_title");
				if (jobTitle != null) {		
					jobStr.append(jobTitle + " ");
//					System.out.println(" jobTitle: " + jobTitle);
				}
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
//				System.out.println("locale: " + locale);
			}
			
			//extract the headline
			headline = (String)jsonObj.get("headline");
			if (headline != null) {
				jobStr.append(headline + " ");
//				System.out.println("headline: " + headline);
			}
			
			//extract the locality
			industry = (String)jsonObj.get("industry");
			if (industry != null) {
				jobStr.append(industry);
//				System.out.println("industry: " + industry);
			}
			
			fw.write(id + "\n");
			fw.write(jobStr.toString() + "\n");
//			count++;
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
		String summary = "";
		String id = "";

		while ((s = br.readLine()) != null) {
			StringBuilder sklStr = new StringBuilder();
			JSONObject jsonObj = (JSONObject)jsonParser.parse(s);
			skillSet = (JSONArray)jsonObj.get("skills");
			id = (String)jsonObj.get("_id");
			count++;
//			System.out.println("The " + count + " Id: " + id);
			
			if (skillSet == null || id == null) {
//				System.out.println("jsonObj.get(\"skills\")" + jsonObj.get("skills"));
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
//				System.out.println("currtCompu: " + (String)currtComp.get(0) + " ");
			}
			
			//get the current position
			String position = (String)jsonObj.get("locality");
			if (position != null) {
				sklStr.append(position + " ");
//				System.out.println("pos: " + position);
			}
			
			//get the summary
			summary = (String)jsonObj.get("summary");
			if (summary != null) {
				sklStr.append(summary);
			}
			
			fw.write(id + "\n");
			fw.write(sklStr.toString() + "\n");
			if (count % 1000 == 0)
				System.out.println("Finished " + count + " documents!");
		}
		close();
	}
	
	private void close() throws IOException {
		br.close();
		bw.close();
	}	
	public static void main(String[] args) throws IOException, ParseException {
		ParseJson parse = new ParseJson();
		parse.getEdu();
//		parse.getSkill();
//		parse.getJob();
	}
}
