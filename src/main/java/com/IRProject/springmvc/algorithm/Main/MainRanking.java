package com.IRProject.springmvc.algorithm.Main;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.simple.parser.ParseException;

import com.IRProject.springmvc.algorithm.Classes.Document;
import com.IRProject.springmvc.algorithm.IndexingLucene.MyIndexReader;
import com.IRProject.springmvc.algorithm.PreProcessData.ParseJson;
import com.IRProject.springmvc.algorithm.PseudoRFSearch.PseudoRFRetrievalModel;
import com.IRProject.springmvc.algorithm.Search.PreProcessQuery;
import com.IRProject.springmvc.model.*;

public class MainRanking {
	private String contentQ;
	private String indicatorQ;
	private Map<String, Long> lens;
	//<DocNO, profile> hashMap for all indexing files.
	private HashMap<String, Profile> allProfiles;
	private HashMap<String, Profile> jobProfiles, skillProfiles, eduProfiles, generProfiles;
	private ParseJson parse = null;
	
	public MainRanking(Query q) throws IOException {
		PreProcessQuery prePro = new PreProcessQuery(q);
		contentQ = prePro.preProcessQuery();
		indicatorQ = prePro.getIndicator();
		parse = new ParseJson();
		lens = new HashMap<String, Long>();
	}
	
	private void getProfiles() throws IOException, ParseException {

		if (indicatorQ.equals("job")) {
			if (jobProfiles == null) {
				parse.getJob();
				jobProfiles = parse.getProfiles();
				allProfiles = jobProfiles;
			}
			else
				allProfiles = jobProfiles;
			
			if (!lens.containsKey("job")) {
				long lenJob = parse.getCoLength().get("job");
				lens.put("job", lenJob);
			}
		}
		else if (indicatorQ.equals("skill")) {
			if (skillProfiles == null) {
				parse.getSkill();
				skillProfiles = parse.getProfiles();
				allProfiles = skillProfiles;
			}
			else
				allProfiles = skillProfiles;
			
			if (!lens.containsKey("skill")) {
				long lenSkill = parse.getCoLength().get("skill");
				lens.put("skill", lenSkill);
			}
		}
		else if (indicatorQ.equals("education")) {
			if (eduProfiles == null) {
				parse.getEdu();
				eduProfiles = parse.getProfiles();
				allProfiles = eduProfiles;
			}
			else
				allProfiles = eduProfiles;
			
			if (!lens.containsKey("education")) {
				long lenEdu = parse.getCoLength().get("education");
				lens.put("education", lenEdu);
			}
		}
		else {
			if (generProfiles == null) {
				parse.getGeneral();
				generProfiles = parse.getProfiles();
				allProfiles = generProfiles;
			}
			else
				allProfiles = generProfiles;
			
			if(!lens.containsKey("general")) {
				long lenGener = parse.getCoLength().get("general");
				lens.put("general", lenGener);
			}
		}
	}
	
	public ArrayList<Profile> rankingResult() throws Exception {
		//get specific allProfiles
		getProfiles();
		ArrayList<Profile> profiles = new ArrayList<Profile>();
		MyIndexReader ixreader = new MyIndexReader(indicatorQ);
		PseudoRFRetrievalModel PRFSearchModel=new PseudoRFRetrievalModel(ixreader, lens.get(indicatorQ), allProfiles);
		long startTime = System.currentTimeMillis();
		if (contentQ != null) {
			List<Document> results = PRFSearchModel.RetrieveQuery(contentQ, 20, 100, 0.4);
			if (results != null) {
				int rank = 1;
				for (Document doc : results) {
					profiles.add(doc.profile());
					System.out.println( doc.docno() + " " + rank + " " + doc.score());
					rank++;
				}
			}
		}
		long endTime = System.currentTimeMillis(); // end time of running code
		System.out.println("\n\nThe query search time: " + (endTime - startTime) / 60000.0 + " min");
		ixreader.close();
		return profiles;
	}
	
	public static void main(String[] args) throws Exception {
//		Query q = new Query("data analyst Google", null, null, null);
		Query q = new Query(null, "Carnegie Mellon University Zhejiang University machine learning", null, null);
//		Query q = new Query(null, null, "data analysis", null);
//		Query q = new Query(null, null, null, "Carnegie Mellon data analysis");
		MainRanking rank = new MainRanking(q);
		ArrayList<Profile> profiles = rank.rankingResult();
		System.out.println("size: " + profiles.size() + " indicator: " + rank.indicatorQ);
	}
}
