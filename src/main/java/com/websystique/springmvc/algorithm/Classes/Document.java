package com.websystique.springmvc.algorithm.Classes;
/**
 * @author millerai
 */

import com.websystique.springmvc.model.Profile;

public class Document {
	
	protected String docid;
	protected String docno;
	protected double score;
	protected Profile profile;
	
	public Document( String docid, String docno, double score, Profile profile ) {
		this.docid = docid;
		this.docno = docno;
		this.score = score;
		this.profile = profile;
	}
	
	public String docid() {
		return docid;
	}
	
	public String docno() {
		return docno;
	}
	
	public double score() {
		return score;
	}
	
	public Profile profile() {
		return profile;
	}
	
	public void setProfile(Profile profile) {
		this.profile = profile;
	}
	
	public void setDocid( String docid ) {
		this.docid = docid;
	}
	
	public void setDocno( String docno ) {
		this.docno = docno;
	}
	
	public void setScore( double score ) {
		this.score = score;
	}	
}
