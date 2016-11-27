package com.bob.core;

import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import com.bob.model.advertiser;
import com.bob.model.jobBean;

public class LoadJobs {

	private static CloseableHttpClient httpclient = HttpClients.createDefault(); // 

	public static void main(String[] args) throws Exception {

		String strURL = "https://jobsearch-api.cloud.seek.com.au/search?&callback=jQuery182014058358692271755_1480220220217&"
				+ "keywords=&companyProfileStructuredDataId=&hirerId=&hirerGroup=&"
				+ "classification=6281&subclassification=&graduateSearch=false&"
				+ "&nation=3001&area=&isAreaUnspecified=false&worktype=&salaryRange=0-999999&"
				+ "salaryType=annual&dateRange=999&sortMode=ListedDate&engineConfig=&usersessionid=opl0nb4zh1uswqg5r5bbk1vw&"
				+ "eventCaptureSessionId=280051be-cb0d-459d-b748-b940f8cf7449&userqueryid=183443514209146617&"
				+ "include=expanded&siteKey=NZ-Main&seekSelectAllPages=true&hadPremiumListings=true&_=1480220220566&page=1";

		String htmlString = "";
		HttpGet request = new HttpGet(strURL);

		CloseableHttpResponse response = httpclient.execute(request);
		ArrayList<jobBean> jobs = new ArrayList<jobBean>();
		try {

			System.out.println(response.getStatusLine()); // status code

			HttpEntity entity = response.getEntity();
			htmlString = EntityUtils.toString(entity); 
			htmlString = htmlString.replace(
					"typeof jQuery182014058358692271755_1480220220217 === 'function' && jQuery182014058358692271755_1480220220217(",
					"");
			htmlString = htmlString.substring(5);
			htmlString = htmlString.substring(0, htmlString.length() - 2);

			try {
				JSONArray myJsonArray = new JSONArray("[" + htmlString + "]");

				for (int i = 0; i < myJsonArray.length(); i++) {
					// get JsonObject
					JSONObject myjObject = myJsonArray.getJSONObject(i);

					JSONArray data = new JSONArray(myjObject.get("data").toString());
					for (int j = 0; j < data.length(); j++) {
						JSONObject job = data.getJSONObject(j);
						String title = job.getString("title");
						int id = job.getInt("id");
						String teaser = job.getString("teaser");
						String location = job.getString("location");
						String area = job.getString("area");
						JSONObject ad = job.getJSONObject("advertiser");
						String adid = ad.get("id").toString();
						String description = ad.getString("description");
						advertiser adv = new advertiser();
						adv.setId(adid);
						adv.setDescription(description);
						jobBean jb = new jobBean(id, title, location, area, adv, teaser);
						System.out.println(jb.toString());
						jobs.add(jb);

					}

				}

			} catch (Exception e) {
				System.out.println(e);
			}

		} finally {
			response.close();
		}
		if (jobs.size() > 0) {
			System.out.println(jobs.size());
			DataBaseTools.setConn();
			DataBaseTools.Insert(jobs);
			DataBaseTools.closeConn();

		}
	}

}