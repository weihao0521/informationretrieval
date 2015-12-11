/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.IRProject.springmvc.service;

import com.IRProject.springmvc.model.Profile;
import com.IRProject.springmvc.model.Query;
import com.IRProject.springmvc.service.QueryService;
import java.util.ArrayList;
import org.springframework.stereotype.Service;
/**
 *
 * @author william
 */
@Service("queryService")
public class QuerySerivceImpl implements QueryService {
    public ArrayList<Profile> getProfiles(Query query){
        ArrayList<Profile> result = new ArrayList<Profile>();
        return result;
    }
}
