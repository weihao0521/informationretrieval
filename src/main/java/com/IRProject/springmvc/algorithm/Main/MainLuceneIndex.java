package com.IRProject.springmvc.algorithm.Main;

import java.util.Map;
import com.IRProject.springmvc.algorithm.IndexingLucene.MyIndexReader;
import com.IRProject.springmvc.algorithm.IndexingLucene.MyIndexWriter;
import com.IRProject.springmvc.algorithm.IndexingLucene.PreProcessedCorpusReader;

public class MainLuceneIndex {
	public static void main(String[] args) throws Exception {
		// main entrance
		MainLuceneIndex hm2 = new MainLuceneIndex();
		
		long startTime=System.currentTimeMillis();
		hm2.WriteIndex("skill");
		long endTime=System.currentTimeMillis();
		System.out.println("index skill corpus running time: "+(endTime-startTime)/60000.0+" min"); 

		startTime=System.currentTimeMillis();
		hm2.WriteIndex("job");
		endTime=System.currentTimeMillis();
		System.out.println("index job corpus running time: "+(endTime-startTime)/60000.0+" min");
		
		startTime=System.currentTimeMillis();
		hm2.WriteIndex("education");
		endTime=System.currentTimeMillis();
		System.out.println("index education corpus running time: "+(endTime-startTime)/60000.0+" min");
		
		startTime=System.currentTimeMillis();
		hm2.WriteIndex("general");
		endTime=System.currentTimeMillis();
		System.out.println("index education corpus running time: "+(endTime-startTime)/60000.0+" min");
	}

	public void WriteIndex(String dataType) throws Exception {
		// Initiate pre-processed collection file reader
		PreProcessedCorpusReader corpus=new PreProcessedCorpusReader(dataType);
		
		// initiate the output object
		MyIndexWriter output=new MyIndexWriter(dataType);
		
		// initiate a doc object, which can hold document number and document content of a document
		Map<String, String> doc = null;

		int count=0;
		// build index of corpus document by document
		while ((doc = corpus.nextDocument()) != null) {
			// load document number and content of the document
			String docno = doc.keySet().iterator().next();
			String content = doc.get(docno);
			// index this document
			output.index(docno, content); 
			
			count++;
			if(count%1000==0)
				System.out.println("finish "+count+" docs");
		}
		System.out.println("Totaly document count:  "+count);
		output.close();
	}
	
	public void ReadIndex(String dataType, String token) throws Exception {
		// Initiate the index file reader
		MyIndexReader ixreader=new MyIndexReader(dataType);
		
		// do retrieval
		int df = ixreader.DocFreq(token);
		long ctf = ixreader.CollectionFreq(token);
		System.out.println(" >> the token \""+token+"\" appeared in "+df+" documents and "+ctf+" times in total");
		if(df>0){
			int[][] posting = ixreader.getPostingList(token);
			for(int ix=0;ix<posting.length;ix++){
				int docid = posting[ix][0];
				int freq = posting[ix][1];
				String docno = ixreader.getDocno(docid);
//				System.out.printf("    %20s    %6d    %6d\n", docno, docid, freq);
			}
		}
		ixreader.close();
	}
}
