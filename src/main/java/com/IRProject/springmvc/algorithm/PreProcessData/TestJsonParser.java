package com.IRProject.springmvc.algorithm.PreProcessData;

import java.util.Iterator;

import org.json.simple.*;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
/**
 * 
 * @author millerai
 *
 */
public class TestJsonParser {
	public static void main(String[] args) throws ParseException {
		String str = "{\"_id\":\"in-1emilyzhao\",\"education\":{\"1\":{\"edu_description\":null,\"edu_institution\":\"Syracuse University - Martin J. Whitman School of Management\",\"edu_time\":\"2012?2015\",\"edu_title\":[\"Bachelor of Science (B.S.)\"]},\"2\":{\"edu_description\":null,\"edu_institution\":\"Syracuse University Abroad - Madrid Center, Spain\",\"edu_time\":\"2015\",\"edu_title\":[]}},\"groups\":{},\"industry\":\"Marketing and Advertising\",\"locality\":\"Greater Minneapolis-St. Paul Area\",\"name\":\"Emily Zhao\",\"url\":\"https://www.linkedin.com/in/1emilyzhao\"}";
		JSONParser jsonParser = new JSONParser();
		JSONObject jsonObj = (JSONObject)jsonParser.parse(str);
		String url = (String)jsonObj.get("url");
		System.out.println("The url is: " + url);
		JSONObject education = (JSONObject)jsonObj.get("education");
		System.out.println("The education object is: " + education);
		//If there is summary, then extract the summary and combine with "education"
		StringBuilder sb = new StringBuilder();
		String summary = "";
		if (jsonObj.get("summary") != null) {
			summary = (String)jsonObj.get("summary");
			
		}
		sb.append(summary);
		Iterator<String> iterator = education.keySet().iterator();
		int count = 0;
		while (iterator.hasNext()) {
			count++;
			String key = (String)iterator.next();
			System.out.println("The " + count + " key is: " + key);
			JSONObject school = (JSONObject)education.get(key);
			System.out.println("The " + count + " school is: " + school.toString());
//			Iterator<String> iter2 = school.keySet().iterator();
			String eduName = (String)school.get("edu_institution");
			sb.append(eduName + " ");
			System.out.println("The " + count + " school name is: " + eduName);
//			while ( iter2.hasNext()) {
//				String keyChild = iter2.next();
//				if (keyChild.equals("edu_institution")){
//					String eduName = (String)school.get(keyChild);
//					sb.append(eduName + " ");
//					System.out.println("The " + count + " school name is: " + eduName);
//				}
//			}			
		}
		System.out.println("child object of education is: " + sb.toString());
//		String str2 = education.toString();
//		System.out.println("The str2 is: " + str2);  
//		String str3 = str2.replaceAll("[^A-Za-z]", " ");
//		System.out.println("The str3 is: " + str3);
//		String[] strArray = str3.split("\\s+");
//		for (String str5 : strArray){
//			System.out.println("The str5 is: " + str5);
//		}
	}
}
