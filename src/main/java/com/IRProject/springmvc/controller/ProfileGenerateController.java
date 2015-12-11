/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.IRProject.springmvc.controller;

import com.IRProject.springmvc.model.Profile;
import com.IRProject.springmvc.model.Query;
import com.IRProject.springmvc.service.QueryService;
import java.util.ArrayList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author william
 */
@RestController
public class ProfileGenerateController {
    @Autowired
    QueryService queryService;
    
    @RequestMapping(value = "/search/", method = RequestMethod.GET)
    public ResponseEntity<ArrayList<Profile>> getUsersProfiles(
            @RequestParam(value = "university",required = false) String university, 
            @RequestParam(value = "job",required = false) String job, 
            @RequestParam(value = "skill",required = false) String skill, 
            @RequestParam(value = "query",required = false) String query){
        Query userQuery = new Query(job, university, skill, query);
        ArrayList<Profile> userProfiles = queryService.getProfiles(userQuery);
                System.out.println(university);
        System.out.println(job);
        System.out.println(skill);
        System.out.println(query);
        return new ResponseEntity<ArrayList<Profile>>(userProfiles, HttpStatus.OK);
    }
}
