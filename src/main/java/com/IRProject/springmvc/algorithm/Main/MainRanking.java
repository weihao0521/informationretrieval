package com.IRProject.springmvc.algorithm.Main;

import java.util.List;

import com.IRProject.springmvc.algorithm.Classes.Document;
import com.IRProject.springmvc.algorithm.Classes.Query;
import com.IRProject.springmvc.algorithm.IndexingLucene.MyIndexReader;
import com.IRProject.springmvc.algorithm.PseudoRFSearch.PseudoRFRetrievalModel;
import com.IRProject.springmvc.algorithm.Search.ExtractQuery;

public class MainRanking {
	public static void main(String[] args) throws Exception {
		// Open index, initialize the pseudo relevance feedback retrieval model, and extract queries
		final int lenSkill = 14773;
		final int lenJob = 16559;
		final int lenEdu = 17166;
		final int lenGen = 20088;
		MyIndexReader ixreader = new MyIndexReader("job");
		PseudoRFRetrievalModel PRFSearchModel=new PseudoRFRetrievalModel(ixreader, lenJob);
		List<Query> QList = new ExtractQuery().GetQueries();
		// begin search
		long startTime = System.currentTimeMillis(); 
		if (QList != null) {
			for (Query aQuery : QList) {
				List<Document> results = PRFSearchModel.RetrieveQuery(aQuery, 20, 100, 0.4);
				if (results != null) {
					int rank = 1;
					for (Document result : results) {
						System.out.println(aQuery.GetTopicId() + " Q0 " + result.docno() + " " + rank + " " + result.score() + " MYRUN");
						rank++;
					}
				}
			}
		}
		long endTime = System.currentTimeMillis(); // end time of running code
		System.out.println("\n\n4 queries search time: " + (endTime - startTime) / 60000.0 + " min");
		ixreader.close();
	}
}
