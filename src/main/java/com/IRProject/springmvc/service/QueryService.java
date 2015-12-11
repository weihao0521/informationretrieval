/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.IRProject.springmvc.service;

import com.IRProject.springmvc.model.Profile;
import com.IRProject.springmvc.model.Query;
import java.util.ArrayList;
/**
 *
 * @author william
 */
public interface QueryService {
    ArrayList<Profile> getProfiles(Query query); 
}
