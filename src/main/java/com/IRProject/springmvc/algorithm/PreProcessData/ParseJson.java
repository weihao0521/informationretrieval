package com.IRProject.springmvc.algorithm.PreProcessData;

import org.json.simple.*;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import com.IRProject.springmvc.algorithm.Classes.*;
import com.IRProject.springmvc.model.Profile;
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
	//<docNo, profile>
	private HashMap<String, Profile> profiles;
	private Profile profile;
	
	public ParseJson() throws IOException {
		fr = new FileReader(Path.DataDir);
		br = new BufferedReader(fr);
		jsonParser = new JSONParser();
		stopwordRemover = new StopWordRemover();
		stemmer = new WordStemmer();
		count = 0;
		idPerson = new HashMap<String, String>();
	}
	
	public HashMap<String, Profile> getProfiles() {
		return profiles;
	}
	
	//education + summary
	public void getEdu() throws IOException, ParseException {
		fw = new FileWriter(Path.ResultDir + "education.txt");
		bw = new BufferedWriter(fw);
		profiles = new HashMap<String, Profile>();
		JSONObject education = null;
		String s = "", summary = "", id = "";
		String[] eduNames = null;
		
		while ((s = br.readLine()) != null) {
			StringBuilder eduStr = new StringBuilder();
			JSONObject jsonObj = (JSONObject)jsonParser.parse(s);
			education = (JSONObject)jsonObj.get("education");
			id = (String)jsonObj.get("_id");
			count++;
			
			//If does not have "education", then skip the person
			if (education == null || id == null) 
				continue;
			
			profile = new Profile();
			Iterator<String> iterator = education.keySet().iterator();
				
			//extract all the school names
			int size = education.keySet().size();
			eduNames = new String[size];
			int i = 0;
			while (iterator.hasNext()) {
				String idx = iterator.next();
				JSONObject school = (JSONObject)education.get(idx);
				String eduName = (String)school.get("edu_institution");
				if (eduName != null && eduName.length() > 4) {
					eduStr.append(eduName + " ");
					eduNames[i] = eduName;
					i++;
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

			profile.setUniversities(eduNames);
			profile.setName((String)jsonObj.get("name"));
			profile.setLocality((String)jsonObj.get("locality"));
			profile.setUrl((String)jsonObj.get("url"));
			profiles.put(id, profile);
			if (count % 1000 == 0)
				System.out.println("Finished " + count + " documents!");
		}
		close();
	}
	
	//experience(company + job_title) + headline + industry + locality + summary
	public void getJob() throws IOException, ParseException {
		fw = new FileWriter(Path.ResultDir + "job.txt");
		bw = new BufferedWriter(fw);
		profiles = new HashMap<String, Profile>();
		JSONObject experience = null;
		String s = "", jobTitle = "", compName = "", summary = "";
		String locale = "", headline = "", industry = "", id = "";
		String[] companies = null, titles = null;
		
		while ((s = br.readLine()) != null) {
			JSONObject jsonObj = (JSONObject)jsonParser.parse(s);
			StringBuilder jobStr = new StringBuilder();		
			experience = (JSONObject)jsonObj.get("experience");
			id = (String)jsonObj.get("_id");
			count++;
			
			//Skip the person if there is no experience
			if (experience == null || id == null) 
				continue;
			
			profile = new Profile();
			Iterator<String> iterator = experience.keySet().iterator(); 
			
			//extract company_name + job_title
			int sizeComp = experience.keySet().size();
			companies = new String[sizeComp];;
			titles = new String[sizeComp];;
			int i = 0;
			while (iterator.hasNext()) {
				String idx = iterator.next();
				JSONObject comInfo = (JSONObject)experience.get(idx);
				JSONArray company = (JSONArray)comInfo.get("company");
				if (company == null || company.size() == 0)
					continue;
				
				//get the company name
				compName = (String)company.get(0);
				jobStr.append(compName + " ");
				companies[i] = compName;
				
				//get the job title
				jobTitle = (String)comInfo.get("job_title");
				if (jobTitle != null) {	
					jobStr.append(jobTitle + " ");
					titles[i] = jobTitle;
				}
				i++;
			} // end while		
			
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
			
			profile.setCompanies(companies);
			profile.setJobTitle(titles);
			profile.setLocality(locale);
			profile.setIndustry(industry);
			profile.setName((String)jsonObj.get("name"));
			profile.setUrl((String)jsonObj.get("url"));
			profiles.put(id, profile);

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
		profiles = new HashMap<String, Profile>();
		JSONArray skillSet = null;
		String summary = "", id = "", locale = "";
		String[] skills = null, company = null, title = null;

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
			
			profile = new Profile();
			
			//Skills return an array of skills
			int size = skillSet.size();
			skills = new String[size];
			for (int i = 0; i < size; i++) {
				String skill = (String)skillSet.get(i);
				sklStr.append(skill + " ");
				skills[i] = skill;
			}
			
			//get the current company
			JSONObject experience = (JSONObject)jsonObj.get("experience");
			if (experience != null) {
				JSONObject first = (JSONObject)experience.get("1");
				if (first != null) {
					JSONArray currtComp = (JSONArray)first.get("company");
					if (currtComp != null && currtComp.size() != 0) {
						company = new String[1];
						String nameComp = (String)currtComp.get(0);
						sklStr.append(nameComp + " ");
						company[0] = nameComp;
					}
					//get the job title
					String jobTitle = (String)first.get("job_title");
					if (jobTitle != null) {	
						title = new String[1];
						sklStr.append(jobTitle + " ");
						title[0] = jobTitle;
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
			
			profile.setSkills(skills);
			profile.setJobTitle(title);
			profile.setName((String)jsonObj.get("name"));
			profile.setLocality(locale);
			profile.setUrl((String)jsonObj.get("url"));
			profiles.put(id, profile);
			
			if (count % 1000 == 0)
				System.out.println("Finished " + count + " documents!");
		}
		close();
	}
	
	//education + experience(company + job_title) + headline + industry + locality + summary + skills
	public void getGeneral() throws IOException, ParseException {
		fw = new FileWriter(Path.ResultDir + "general.txt");
		bw = new BufferedWriter(fw);
		profiles = new HashMap<String, Profile>();
		JSONObject education = null;
		JSONObject experience = null;
		JSONArray skillSet = null;
		String s = "", jobTitle = "", compName = "", summary = "";
		String locale = "", headline = "", industry = "", id = "";
		String[] eduNames = null, companies = null, titles = null, skills = null;
		
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
			
			profile = new Profile();
			
			//if "edu" != null, extract all the school names
			int i = 0;
			if (education != null) {
				Iterator<String> iterator = education.keySet().iterator();	
				int size = education.keySet().size();
				eduNames = new String[size];
				while (iterator.hasNext()) {
					String idx = iterator.next();
					JSONObject school = (JSONObject)education.get(idx);
					String eduName = (String)school.get("edu_institution");
					if (eduName != null && eduName.length() > 5) {
						generalStr.append(eduName + " ");
						eduNames[i] = eduName;
						i++;
					}					
				}
			}
			
			//if "experience" != null, extract company_name + job_title
			if (experience != null) {
				Iterator<String> iterator = experience.keySet().iterator(); 
				int size = experience.keySet().size();
				companies = new String[size];
				titles = new String[size];
				int j = 0;
				while (iterator.hasNext()) {
					String idx = iterator.next();
					JSONObject comInfo = (JSONObject)experience.get(idx);
					JSONArray company = (JSONArray)comInfo.get("company");
					if (company == null || company.size() == 0)
						continue;
					
					//get the company name
					compName = (String)company.get(0);
					generalStr.append(compName + " ");
					companies[j] = compName;
					
					//get the job title
					jobTitle = (String)comInfo.get("job_title");
					if (jobTitle != null) {	
						generalStr.append(jobTitle + " ");
						titles[j] = jobTitle;
					}
					j++;
				} // end while 
			} // end if "experience"
			
			//if array "skillSet" != null, extract all the skills
			if (skillSet != null) {
				int size = skillSet.size();
				skills = new String[size];
				for (int k = 0; k < size; k++) {
					String skill = (String)skillSet.get(k);
					generalStr.append(skill + " ");
					skills[k] = skill;
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

			profile.setSkills(skills);
			profile.setUniversities(eduNames);
			profile.setJobTitle(titles);
			profile.setCompanies(companies);
			profile.setLocality(locale);
			profile.setName((String)jsonObj.get("name"));
			profile.setUrl((String)jsonObj.get("url"));
			profiles.put(id, profile);
			
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
//		ArrayList<Profile> p = parse.getProfiles();
//		for (Profile pro : p) {
//			System.out.println("profiles:" + pro.getName());
//		}
		System.out.println("profiles len:" + parse.profiles.size());
	}
}
