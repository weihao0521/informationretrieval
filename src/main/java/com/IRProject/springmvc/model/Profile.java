/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.IRProject.springmvc.model;

/**
 *
 * @author william
 */
public class Profile {
    private String name;
    private String[] universities;
    private String[] eduTimes;
    private String[] eduTitles;
    private String[] skills;
    private String[] companies; 
    private String[] jobTitles;
    private String locality;
    private String industry;
    private String url;
    private String summary;
    
    public void setEduTimes (String[] eduTimes){
    	this.eduTimes = eduTimes;
    }
    
    public String[] getEduTimes () {
    	return eduTimes;
    }
    
    public void setEduTitles (String[] eduTitles){
    	this.eduTitles = eduTitles;
    }
    
    public String[] getEduTitles () {
    	return eduTitles;
    }
    
    public void setUrl(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }
    
    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getSummary() {
        return summary;
    }
    
    public void setUniversities(String[] universities) {
        this.universities = universities;
    }

    public void setCompanies(String[] companies) {
        this.companies = companies;
    }

    public String[] getUniversities() {
        return universities;
    }

    public String[] getCompanies() {
        return companies;
    }
    public void setIndustry(String industry) {
        this.industry = industry;
    }

    public String getIndustry() {
        return industry;
    }

    public void setLocality(String locality) {
        this.locality = locality;
    }

    public String getLocality() {
        return locality;
    }

    public void setJobTitle(String[] jobTitles) {
        this.jobTitles = jobTitles;
    }

    public String[] getJobTitle() {
        return jobTitles;
    }

    public void setSkills(String[] skills) {
        this.skills = skills;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String[] getSkills() {
        return skills;
    }

    public String getName() {
        return name;
    }
}
