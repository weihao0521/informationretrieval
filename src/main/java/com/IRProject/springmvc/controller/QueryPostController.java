/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.IRProject.springmvc.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.IRProject.springmvc.model.Query;
/**
 *
 * @author william
 */
@Controller
public class QueryPostController {
    @RequestMapping(value = "/query/", method = RequestMethod.POST)
	public @ResponseBody Query PostService(@RequestBody Query query) {
            System.out.println(query.getJob());
		return query;
	}
}
