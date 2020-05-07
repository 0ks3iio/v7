/* 
 * @(#)HttpClient.java    Created on 2006-7-17
 * Copyright (c) 2005 ZDSoft.net, Inc. All rights reserved.
 * $Header: /project/signinserver2/src/net/zdsoft/signin2/util/SigninRecordUpload.java,v 1.2 2008/04/28 03:15:27 zhaowj Exp $
 */
package net.zdsoft.appstore.utils;

import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import com.alibaba.fastjson.JSONObject;

/**
 * @author duhc
 * @version $Revision: 1.0 $, $Date: 2018-6-1 $
 * 上传数据工具类
 */
public class SigninRecordUpload {
    
    public static boolean upload(String remoteIpPort,String path,String jsonArray) throws Exception {

        String urlString = remoteIpPort + path;
        
        URL url = new URL(urlString);

        HttpURLConnection huc = (HttpURLConnection) url.openConnection();
        huc.setRequestMethod("POST");
        huc.setDoOutput(true);
        huc.setDoInput(true);
        huc.setUseCaches(false);
        huc.setRequestProperty("Content-Type", "application/json;charset=utf-8");
        huc.connect();
        
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(huc.getOutputStream(), "UTF-8"));
        writer.write(jsonArray);
        writer.close();
        
        StringBuffer sb = new StringBuffer();
        
        int responseCode = huc.getResponseCode();
        if(responseCode == HttpURLConnection.HTTP_OK){
            InputStream inputStream = huc.getInputStream();
            byte[] buffer = new byte[1024];
            while (inputStream.read(buffer) != -1) {
            	sb.append(new String(buffer));
            }
            inputStream.close();
            JSONObject retJson = (JSONObject) JSONObject.parse(sb.toString().trim());
			int code = retJson.containsKey("code")?retJson.getIntValue("code"):-1;
//			String message = retJson.containsKey("message")?retJson.getString("message"):"";
            if (code == 0) {
            	return true;
            } else {
            	return false;
            }
        } else {
        	return false;
        }
    }
}
