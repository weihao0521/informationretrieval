package com.IRProject.springmvc.algorithm.Main;

import java.io.IOException;
import java.util.HashMap;

import org.json.simple.parser.ParseException;

import com.IRProject.springmvc.algorithm.IndexingLucene.MyIndexReader;
import com.IRProject.springmvc.algorithm.PreProcessData.ParseJson;
import com.IRProject.springmvc.model.Profile;

public class CollectionLength {
	public static void main(String[] args) throws IOException, ParseException {
		HashMap<String, Profile> jobProfiles, skillProfiles, eduProfiles, generProfiles;
		int coLen = 0;
		MyIndexReader ixreader = new MyIndexReader("job");
		ParseJson parse = new ParseJson();
		parse.getJob();
		jobProfiles = parse.getProfiles();
		System.out.println("jobProfiles length: " + jobProfiles.size());

		int id = ixreader.getDocid("in-1pengw");
//		for (String docNo : jobProfiles.keySet()) {
//			coLen += ixreader.docLength(ixreader.getDocid(docNo));
//
//		}
		System.out.println("id: " + id);
	}
}
