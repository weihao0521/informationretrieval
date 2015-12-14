/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.websystique.springmvc.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.websystique.springmvc.service.ResultService;
import java.util.ArrayList;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import com.websystique.springmvc.model.Query;
import com.websystique.springmvc.model.Profile;
/**
 *
 * @author william
 */
@Controller
public class ResultController {
    @Autowired
    ResultService resultService;
    
    @RequestMapping(value = "/result", method = RequestMethod.GET)
    public ResponseEntity<ArrayList<Profile>> getUsersProfiles(
            @RequestParam(value = "university",required = false) String university, 
            @RequestParam(value = "job",required = false) String job, 
            @RequestParam(value = "skill",required = false) String skill, 
            @RequestParam(value = "query",required = false) String query,
            @RequestParam(value = "number",required = false) String number) throws Exception{
        Query userQuery = new Query(job, university, skill, query);
//        System.out.println(university);
//        System.out.println(job);
//        System.out.println(skill);
        System.out.println(number);
        if(number==null){
            number = 50+"";
        }
        if(Integer.parseInt(number) > 200){
            number = 200+"";
        } 
    	ArrayList<Profile> categories = resultService.getProfiles(userQuery, Integer.parseInt(number));
        try {
            if(categories.isEmpty()){
                return new ResponseEntity<ArrayList<Profile>>(HttpStatus.NO_CONTENT);//You many decide to return HttpStatus.NOT_FOUND
            }   
            return new ResponseEntity<ArrayList<Profile>>(categories, HttpStatus.OK);
        } catch (NullPointerException e) {
            return new ResponseEntity<ArrayList<Profile>>(HttpStatus.NO_CONTENT);
        }
    }
}
