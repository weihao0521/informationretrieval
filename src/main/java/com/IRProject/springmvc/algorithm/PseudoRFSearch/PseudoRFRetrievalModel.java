package com.IRProject.springmvc.algorithm.PseudoRFSearch;

import java.io.IOException;
import java.util.*;
import java.util.Map.Entry;

import com.IRProject.springmvc.algorithm.Classes.Document;
import com.IRProject.springmvc.algorithm.IndexingLucene.MyIndexReader;
import com.IRProject.springmvc.model.Profile;

public class PseudoRFRetrievalModel {

	MyIndexReader ixreader;
	private final int LENGTH_Col;
	//<docID, Doc>
	Map<Integer, Doc> unionPostList;
	//<docID, <token, p(w|D)>>
	Map<Integer, HashMap<String, Double>> DocTokenScore;
	//<dodcNo, Profile object>, corresponding to the specific indexing documents, for "job", for "skill"
	Map<String, Profile> docNoProfiles;
	//one big pseudo relevant doc's length
	long RefLength;
	
	public PseudoRFRetrievalModel(MyIndexReader ixreader, int lenCollection, HashMap<String, Profile> docFiles)
	{
		this.ixreader=ixreader;
		LENGTH_Col = lenCollection;
		unionPostList = new HashMap<Integer, Doc>();
		DocTokenScore = new HashMap<Integer, HashMap<String, Double>>();
		docNoProfiles = docFiles;
		RefLength = 0;
	}
	
	
	/**
	 * Search for the topic with pseudo relevance feedback. 
	 * The returned results (retrieved documents) should be ranked by the score (from the most relevant to the least).
	 * 
	 * @param aQuery The query to be searched for.
	 * @param TopN The maximum number of returned document
	 * @param TopK The count of feedback documents
	 * @param alpha parameter of relevance feedback model
	 * @return TopN most relevant document, in List structure
	 */
	public List<Document> RetrieveQuery( String query, int TopN, int TopK, double alpha) throws Exception {	

		//sort all retrieved documents from most relevant to least, and return TopN
		List<Document> results = new ArrayList<Document>();
		
		//get <token, P(token|feedback documents)>
		HashMap<String,Double> TokenRFScore = GetTokenRFScore(query,TopK);
		String[] tokens = query.split("\\s+");
		
		for(Entry<Integer, Doc> e : unionPostList.entrySet()){
			int docID = e.getKey();
			String docNO = ixreader.getDocno(docID);
			double docRankScore = 1;
			
			for(int i = 0; i < tokens.length; i++){				
				//P(w|D') = alpha * P(w|D) + (1-alpha) * P(w|REF)s
				double TokenDocScoreBefore = DocTokenScore.get(docID).get(tokens[i]);
				double TokenDocScoreAfter = alpha * TokenDocScoreBefore + (1-alpha) * TokenRFScore.get(tokens[i]);
				
				//P(Q|D) = P(w1|D')*P(w2|D')*,...,*P(wn|D')
				docRankScore = docRankScore * TokenDocScoreAfter;
			}
			//Profile p = docNoProfiles.get(docNO)
			Document doc = new Document(String.valueOf(docID), docNO, Math.log(docRankScore), docNoProfiles.get(docNO));
			results.add(doc);
		}
		
		//refresh some public fields for a new query
		refresh();
		Collections.sort(results, new sortRankingList());
		return results.subList(0, TopN);
	}
	
	//return HashMap<{token[i], RFProb}, {...}>
	public HashMap<String,Double> GetTokenRFScore(String query,  int TopK) throws Exception
	{
		if(query == null || TopK <= 0)
			return null;
		
		//<token, P(w|REF)>
		HashMap<String,Double> TokenRFScore=new HashMap<String,Double>();
		
		//Store topK relevant documents
		List<Document> rankList  = new ArrayList<Document>();
		
		//<token, P(w|Co)>
		Map<String, Double> TermCoProb = new HashMap<String, Double>();
		int mu = 1000;
		
		String[] tokens = query.split("\\s+");
		for(int i = 0; i<tokens.length; i++){
			int[][] postingList = ixreader.getPostingList(tokens[i]);
			long termCoFreq = 0;
			
			//All seen token's union posting lists and collection freq
			if(postingList != null){
				termCoFreq = ixreader.CollectionFreq(tokens[i]);
				//sum the term frequency of each token in the query that a document contains
				unionPosting(tokens[i], postingList);
			}
			//For tokens never existed in Collection, assume its collection freq = 1
			else			
				termCoFreq = 1;
		
			//term's collection probability in the query
			TermCoProb.put(tokens[i], 1.0 * termCoFreq / LENGTH_Col);
		}
		
		//Entry<DocID, Doc>; Doc: length, <token, tf>
		for(Entry<Integer, Doc> e : unionPostList.entrySet()){
			Doc docInfo = e.getValue();
			int docID = e.getKey();
			int lenD = docInfo.length;
			double docRankScore = 1;
			
			//<<w1, P(w1|D1)>, <w2, P(w2|D1)>,..., <wn, P(wn|D1)>>
			HashMap<String, Double> tokenScore = new HashMap<String, Double>();
			for(int i = 0; i < tokens.length; i++){
				double termProb_doc = 0;
				double termProb_C = TermCoProb.get(tokens[i]);
				
				//P(w|D) = [c(w,D) + mu*P(w|C)]/(|D| + mu)
				if(docInfo.word_tf.containsKey(tokens[i])){
					int termFreq = docInfo.word_tf.get(tokens[i]);					
					termProb_doc = (termFreq + mu * termProb_C) / (lenD + mu);
				}				
				//P(w|D) = mu*P(w|C)/(|D| + mu)
				else
					termProb_doc = mu * termProb_C / (lenD + mu);
				
				//<<w1,p(w1|D1)>, <w2,p(w2|D1)>, <w3,p(w3|D1)>, ...>			
				tokenScore.put(tokens[i], termProb_doc);
				
				//<(docID, <<w1,p(w1|D)>, <w2,p(w2|D)>, <w3,p(w3|D)>, ...>),...>
				DocTokenScore.put(docID, tokenScore);
				
				//P(Q|D) = p(w1|D)*p(w2|D)*..*p(w|D)
				docRankScore = docRankScore * termProb_doc;			
			}
			//no need to store Profiles, so set them null;
			Document doc = new Document(String.valueOf(docID), "",docRankScore, null);
			rankList.add(doc);
		}
		
		//Sort relevant documents with descending order
		Collections.sort(rankList, new sortRankingList());
		List<Document> RefDocs = rankList.subList(0, TopK);

		//<word, c(w, REF)>
		Map<String, Integer> TokenRefCount = RefTermf(RefDocs, unionPostList, tokens);
		
		//Get all words' RefProb: P(w|REF) = [c(w,REF) + mu*P(w|C)]/(|REF| + mu)
		for(int i = 0; i < tokens.length; i++){
			double rfScore = 1.0 * (TokenRefCount.get(tokens[i]) + mu*TermCoProb.get(tokens[i])) / (RefLength + mu);
			TokenRFScore.put(tokens[i], rfScore);
		}		
		return TokenRFScore;
	}
	
	//get each query term's frequency for the one big pseudo relevant document
	private HashMap<String, Integer> RefTermf(List<Document> REFList, Map<Integer, Doc> unionPosting, String[] tokens){
		
		//<token, c(w, REF)>
		HashMap<String, Integer> RefWordCount = new HashMap<String, Integer>();
		
		//For each word, iterate all the REF documents to get a wordCount of REF
		for(int i = 0; i < tokens.length; i++){
			int tokenCount = 0;
			for(Document doc: REFList){
				Doc docInfo = unionPosting.get(Integer.valueOf(doc.docid()));
				
				RefLength = RefLength + docInfo.length;
				if(docInfo.word_tf.containsKey(tokens[i]))
					tokenCount = tokenCount + docInfo.word_tf.get(tokens[i]);				
			}
			RefWordCount.put(tokens[i], tokenCount);
		}
		
		//Pseudo relevant doc's length repeat tokens' time, so divide by tokens' length
		RefLength = RefLength / tokens.length;	
		return RefWordCount;
	}
	
	//HashMap<{Doc_id1, Doc1(<token[1], tf1>, <token[2], tf2>, <...>, doc_len)}>
	private void unionPosting(String token, int[][] posting) throws IOException{
		for (int i = 0; i < posting.length; i++){
			int docID = posting[i][0];
			int termFreq = posting[i][1];
			int len = ixreader.docLength(docID);
			if(!unionPostList.containsKey(docID))
				unionPostList.put(docID, new Doc(token, termFreq, len));				
			else
				unionPostList.get(docID).word_tf.put(token, termFreq);			
		}					
	}	
	
	//Doc class includes each doc's length, seen tokens and their frequency
	private class Doc{
		Map<String, Integer> word_tf;
		int length;
		
		public Doc(String token,int freq, int length){
			word_tf = new HashMap<String, Integer>();
			word_tf.put(token, freq);
			this.length = length;	
		}	
	}
	
	//Sort documents by their ranking score with descending order
	private class sortRankingList implements Comparator<Document>{
		@Override
		public int compare(Document o1, Document o2) {
			
			//Sort documents with descending order
			if(o1.score() < o2.score())
				return 1;
			else if (o1.score() > o2.score())
				return -1;
			
			//If two documents' ranking score are the same, sort by their ID with increasing order
			else{
				if(Integer.valueOf(o1.docid()) > Integer.valueOf(o2.docid()))
					return 1;
				else 
					return -1;
			}
		}
	}	
	
	//Refresh the public fields for a new Query
	private void refresh(){
		unionPostList = new HashMap<Integer, Doc>();
		DocTokenScore = new HashMap<Integer, HashMap<String, Double>>();
		RefLength = 0;
	}
	/*
	 * Problem: 
	 * some public field should be refreshed for every new query 
	 * 
	 * tokenScore must be put outside the tokens' for loop, otherwise it will only store the last token
	 */
}