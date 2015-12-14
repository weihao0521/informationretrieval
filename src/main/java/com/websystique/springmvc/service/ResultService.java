/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.websystique.springmvc.service;

import java.util.ArrayList;
import com.websystique.springmvc.model.Query;
import com.websystique.springmvc.model.Profile;
import com.websystique.springmvc.algorithm.Main.MainRanking;
import java.io.IOException;
import org.springframework.stereotype.Service;
/**
 *
 * @author william
 */
@Service("resultService")
public class ResultService {
    public ArrayList<Profile> getProfiles(Query query,int number) throws IOException, Exception{
        MainRanking rank = new MainRanking();
        return rank.rankingResult(query, number);
    }
}
