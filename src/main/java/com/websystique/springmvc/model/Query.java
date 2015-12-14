/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.websystique.springmvc.model;

/**
 *
 * @author william
 */
public class Query {
    private String job;
    private String university;
    private String skill;
    private String query;

    public Query() {
    }

    public Query(String job, String university, String skill, String query) {
        this.job = job;
        this.university = university;
        this.skill = skill;
        this.query = query;
    }

    public String getQuery() {
        return query;
    }

    public String getJob() {
        return job;
    }

    public String getSkill() {
        return skill;
    }

    public String getUniversity() {
        return university;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public void setJob(String job) {
        this.job = job;
    }

    public void setSkill(String skill) {
        this.skill = skill;
    }

    public void setUniversity(String university) {
        this.university = university;
    }
    
}
