package com.websystique.springmvc.algorithm.PreProcessData;

import com.websystique.springmvc.algorithm.Classes.Path;
import org.json.simple.*;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import com.websystique.springmvc.model.Profile;
/**
 * 
 * @author millerai
 * 
 *	create job.txt or education.txt or skill.txt according to users' needs
 */

public class ParseJson {
	private FileReader fr;
	private BufferedReader br;
	private FileWriter fw;
	private BufferedWriter bw;
	private JSONParser jsonParser;
	private int count, coLength;
	private StopWordRemover stopwordRemover;
	private WordStemmer stemmer;
	//<docNo, profile>
	private HashMap<String, Profile> profiles;
	private Profile profile;
	private HashMap<String, Long> collectionLens;
	
	public ParseJson() throws IOException {
		fr = new FileReader(Path.DataDir);
		br = new BufferedReader(fr);
		jsonParser = new JSONParser();
		stopwordRemover = new StopWordRemover();
		stemmer = new WordStemmer();
		collectionLens = new HashMap<String, Long>();
		count = 0;
	}
	
	public HashMap<String, Profile> getProfiles() {
		return profiles;
	}
	
	public HashMap<String, Long> getCoLength() {
		return collectionLens;
	}
	
	//education + summary
	public void getEdu() throws IOException, ParseException {
		fw = new FileWriter(Path.ResultDir + "education.txt");
		bw = new BufferedWriter(fw);
		profiles = new HashMap<String, Profile>();
		JSONObject education = null;
		JSONObject experience = null;
		JSONArray skillSet = null;
		String s = "", summary = "", id = "", jobTitle = "", compName = "";
		ArrayList<String> eduNames = null, eduTimes = null, eduTitles = null;
		String[] skills = null, companies = null, titles = null;
		long coLength = 0;
		
		while ((s = br.readLine()) != null) {
			StringBuilder eduStr = new StringBuilder();
			JSONObject jsonObj = (JSONObject)jsonParser.parse(s);
			education = (JSONObject)jsonObj.get("education");
			StringBuilder jobStr = new StringBuilder();		
			experience = (JSONObject)jsonObj.get("experience");	
			id = (String)jsonObj.get("_id");
			count++;
			
			//If does not have "education", then skip the person
			if (education == null || id == null) 
				continue;
			
			profile = new Profile();
			Iterator<String> iterator = education.keySet().iterator();
				
			//extract all the school names
			eduNames = new ArrayList<String>();
			eduTimes = new ArrayList<String>();
			eduTitles = new ArrayList<String>();
			while (iterator.hasNext()) {
				String idx = iterator.next();
				JSONObject school = (JSONObject)education.get(idx);
				String eduName = (String)school.get("edu_institution");
				if (eduName != null && eduName.length() > 2) {
					eduStr.append(eduName + " ");
					eduNames.add(eduName);
					String eduTime = (String)school.get("edu_time");
					if (eduTime != null) {
						eduTimes.add(eduTime.replaceAll("\\?", "-"));
					}
					else {
						eduTimes.add(eduTime);
					}
					JSONArray eduTitle = (JSONArray)school.get("edu_title");
					if (eduTitle != null && eduTitle.size() != 0) {
						eduTitles.add((String)eduTitle.get(0));
					}
					else {
						eduTitles.add(null);
					}
				}					
			}
			if (eduNames.size() == 0) {
				eduNames = null;
				continue;
			}
			
			//if "experience" != null, extract company_name + job_title
			if (experience != null) {
				Iterator<String> iteratorJob = experience.keySet().iterator(); 
				int size = experience.keySet().size();
				companies = new String[size];
				titles = new String[size];
				int j = 0;
				while (iteratorJob.hasNext()) {
					String idx = iteratorJob.next();
					JSONObject comInfo = (JSONObject)experience.get(idx);
					JSONArray company = (JSONArray)comInfo.get("company");
					if (company == null || company.size() == 0)
						continue;
					
					//get the company name
					compName = (String)company.get(0);
					eduStr.append(compName + " ");
					companies[j] = compName;
					
					//get the job title
					jobTitle = (String)comInfo.get("job_title");
					if (jobTitle != null) {	
						titles[j] = jobTitle;
					}
					j++;
				} // end while 
			} // end if "experience"
			
			//if array "skillSet" != null, extract all the skills
			skillSet = (JSONArray)jsonObj.get("skills");
			if (skillSet != null) {
				int size = skillSet.size();
				skills = new String[size];
				for (int k = 0; k < size; k++) {
					String skill = (String)skillSet.get(k);
					eduStr.append(skill + " ");
					skills[k] = skill;
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
				if (!stopwordRemover.isStopWord(word)) {
					bw.append(stemmer.stem(word) + " ");
					coLength++;
				}
				
			}
			bw.append("\n");

			if(eduNames != null) {
				int sizeEdu = eduNames.size();
				profile.setUniversities(eduNames.toArray(new String[sizeEdu]));
				profile.setEduTimes(eduTimes.toArray(new String[sizeEdu]));
				profile.setEduTitles(eduTitles.toArray(new String[sizeEdu]));
			}
			profile.setName((String)jsonObj.get("name"));
			profile.setSummary(summary);
			profile.setSkills(skills);
			profile.setCompanies(companies);
			profile.setJobTitle(titles);
			profile.setIndustry((String)jsonObj.get("industry"));
			profile.setLocality((String)jsonObj.get("locality"));
			profile.setUrl((String)jsonObj.get("url"));
			profiles.put(id, profile);
			
			eduNames = null; skills = null; companies = null; titles = null; eduTimes = null; eduTitles = null;
			
			if (count % 1000 == 0)
				System.out.println("Finished " + count + " documents!");
		} // end while
		close();
		collectionLens.put("education", coLength);	
	}
	
	//experience(company + job_title) + headline + industry + locality + summary
	public void getJob() throws IOException, ParseException {
		fw = new FileWriter(Path.ResultDir + "job.txt");
		bw = new BufferedWriter(fw);
		profiles = new HashMap<String, Profile>();
		JSONObject experience = null;
		JSONArray skillSet = null;
		JSONObject education = null;
		ArrayList<String> eduNames = null; 
		String s = "", jobTitle = "", compName = "", summary = "";
		String locale = "", headline = "", industry = "", id = "";
		String[] companies = null, titles = null, skills = null;
		long coLength = 0;
		
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
			
			//if "edu" != null, extract all the school names
			education = (JSONObject)jsonObj.get("education");
			if (education != null) {
				Iterator<String> iteratorEdu = education.keySet().iterator();
				eduNames = new ArrayList<String>();
				while (iteratorEdu.hasNext()) {
					String idx = iteratorEdu.next();
					JSONObject school = (JSONObject)education.get(idx);
					String eduName = (String)school.get("edu_institution");
					if (eduName != null && eduName.length() > 2) {
						eduNames.add(eduName);
					}					
				}
				if (eduNames.size() == 0)
					eduNames = null;
			}//end "edu"
			
			//if array "skillSet" != null, extract all the skills
			skillSet = (JSONArray)jsonObj.get("skills");
			if (skillSet != null) {
				int size = skillSet.size();
				skills = new String[size];
				for (int k = 0; k < size; k++) {
					String skill = (String)skillSet.get(k);
					skills[k] = skill;
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
				if (!stopwordRemover.isStopWord(word)) {
					bw.append(stemmer.stem(word) + " ");
					coLength++;
				}
			}
			bw.append("\n");
			
			if (eduNames != null) {
				profile.setUniversities(eduNames.toArray(new String[eduNames.size()]));
			}
			profile.setCompanies(companies);
			profile.setSummary(summary);
			profile.setJobTitle(titles);
			profile.setLocality(locale);
			profile.setIndustry(industry);
			profile.setSkills(skills);
			profile.setName((String)jsonObj.get("name"));
			profile.setUrl((String)jsonObj.get("url"));
			profiles.put(id, profile);
			
			companies = null; titles = null; skills = null; eduNames = null;

			if (count % 1000 == 0)
				System.out.println("Finished " + count + " documents!");
		}//end while
		close();
		collectionLens.put("job", coLength);
	}
	
	//skills + the current company + locality + summary
	public void getSkill() throws IOException, ParseException {
		String s = "";
		fw = new FileWriter(Path.ResultDir + "skill.txt");
		bw = new BufferedWriter(fw);
		profiles = new HashMap<String, Profile>();
		JSONArray skillSet = null;
		JSONObject education = null;
		ArrayList<String> eduNames = null; 
		String summary = "", id = "", locale = "";
		String[] skills = null, company = null, title = null;
		long coLength = 0;

		while ((s = br.readLine()) != null) {
			StringBuilder sklStr = new StringBuilder();
			JSONObject jsonObj = (JSONObject)jsonParser.parse(s);
			skillSet = (JSONArray)jsonObj.get("skills");
			education = (JSONObject)jsonObj.get("education");
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
			
			//if "edu" != null, extract all the school names
			if (education != null) {
				Iterator<String> iterator = education.keySet().iterator();
				eduNames = new ArrayList<String>();
				while (iterator.hasNext()) {
					String idx = iterator.next();
					JSONObject school = (JSONObject)education.get(idx);
					String eduName = (String)school.get("edu_institution");
					if (eduName != null && eduName.length() > 2) {
						eduNames.add(eduName);
					}					
				}
				if (eduNames.size() == 0)
					eduNames = null;
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
				if (!stopwordRemover.isStopWord(word)) {
					bw.append(stemmer.stem(word) + " ");
					coLength++;
				}
			}
			bw.append("\n");
			
			if (eduNames != null) {
				profile.setUniversities(eduNames.toArray(new String[eduNames.size()]));
			}
			profile.setSkills(skills);
			profile.setSummary(summary);
			profile.setCompanies(company);
			profile.setJobTitle(title);
			profile.setIndustry((String)jsonObj.get("industry"));
			profile.setName((String)jsonObj.get("name"));
			profile.setLocality(locale);
			profile.setUrl((String)jsonObj.get("url"));
			profiles.put(id, profile);
			
			eduNames = null; skills = null; company = null; title = null;
			
			if (count % 1000 == 0)
				System.out.println("Finished " + count + " documents!");
		}
		close();
		collectionLens.put("skill", coLength);
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
		ArrayList<String> eduNames = null; 
		String[] companies = null, titles = null, skills = null;
		long coLength = 0;
		
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
			if (education != null) {
				Iterator<String> iterator = education.keySet().iterator();
				eduNames = new ArrayList<String>();
				while (iterator.hasNext()) {
					String idx = iterator.next();
					JSONObject school = (JSONObject)education.get(idx);
					String eduName = (String)school.get("edu_institution");
					if (eduName != null && eduName.length() > 2) {
						generalStr.append(eduName + " ");
						eduNames.add(eduName);
					}					
				}
				if (eduNames.size() == 0)
					eduNames = null;
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
				if (!stopwordRemover.isStopWord(word)) {
					bw.append(stemmer.stem(word) + " ");
					coLength++;	
				}
			}
			bw.append("\n");


			if (eduNames != null) {
				profile.setUniversities(eduNames.toArray(new String[eduNames.size()]));
			}
			profile.setSkills(skills);
			profile.setSummary(summary);
			profile.setCompanies(companies);
			profile.setJobTitle(titles);
			profile.setLocality(locale);
			profile.setIndustry(industry);
			profile.setName((String)jsonObj.get("name"));
			profile.setUrl((String)jsonObj.get("url"));
			profiles.put(id, profile);
			
			eduNames = null; companies = null; titles = null; skills = null;
			
			if (count % 1000 == 0)
				System.out.println("Finished " + count + " documents!");
		}//end "general"
		close();
		collectionLens.put("general", coLength);
	}
	
	//close BufferedReader and BufferedWriter
	private void close() throws IOException {
		br.close();
		bw.close();
	}	
	public static void main(String[] args) throws IOException, ParseException {
		ParseJson parse = new ParseJson();
		parse.getEdu();  	//edu len:235217
//		parse.getSkill(); 	//skill len: 623166	
//		parse.getJob(); 	//job len: 616581
//		parse.getGeneral();  	//general len: 1128252
		HashMap<String, Profile> p = parse.getProfiles();
//		for (Profile pro : p) {
//			System.out.println("profiles:" + pro.getName());
//		}
		System.out.println("general len: " + p.size());
	}
}
