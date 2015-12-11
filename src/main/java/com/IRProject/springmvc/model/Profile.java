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
    private String[] skills;
    private String[] companies; 
    private String summary; 
    private String jobTitle;
    private String locality;
    private String industry;

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

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public void setSkills(String[] skills) {
        this.skills = skills;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSummary() {
        return summary;
    }

    public String[] getSkills() {
        return skills;
    }

    public String getName() {
        return name;
    }
}
