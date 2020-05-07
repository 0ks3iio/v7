/*
 * Project: v7
 * Author : shenke
 * @(#) StorageFileUtils.java Created on 2016-8-17
 * @Copyright (c) 2016 ZDSoft Inc. All rights reserved
 */
package net.zdsoft.framework.utils;

import org.apache.commons.collections.MapUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import javax.servlet.http.HttpServletRequest;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * @description: 处理上传文件工具类
 * @author: shenke
 * @version: 1.0
 * @date: 2016-8-17下午1:58:00
 */
public class StorageFileUtils {

	public static String DEFAULT_ENCODE = "utf-8";

	/**
	 * 获取单个上传文件，即使有多个只获取第一个
	 * @param req
	 * @return
	 */
	public static MultipartFile getFile(HttpServletRequest req) {
		try {

			CommonsMultipartResolver resolver = newMultipartResolver(req);
			if (resolver.isMultipart(req)) {
				MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest)req;
				Map<String,MultipartFile> fileMap = multiRequest.getFileMap();
				if(MapUtils.isNotEmpty(fileMap)){
					return fileMap.entrySet().iterator().next().getValue();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 获取多个上传文件， 没有文件则返回null
	 * @param request
	 * @return
	 */
	public static List<MultipartFile> getFiles(HttpServletRequest request){
		try {
			// 创建一个通用的多部分解析器
			CommonsMultipartResolver resolver = newMultipartResolver(request);
			// 判断是否有文件上传
			if (resolver.isMultipart(request)) { 
				List<MultipartFile> list = new ArrayList<MultipartFile>();
				//将request变成多部分request  
	            MultipartHttpServletRequest multiRequest=(MultipartHttpServletRequest)request;
	            //获取multiRequest 中所有的文件名  
	            Map<String,MultipartFile> fileMap = multiRequest.getFileMap();
	            list.addAll(fileMap.values());
	            return list;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

//	/**
//	 * 根据指定的字段名获取文件
//	 * @param req	request
//	 * @param filedName  <b>字段名</b>
//	 * @return
//	 */
//	public static MultipartFile getFile(HttpServletRequest req, String filedName){
//		Assert.notNull(filedName);
//		try {
//			CommonsMultipartResolver resolver = newMultipartResolver(req);
//			if(resolver.isMultipart(req)){
//				MultipartHttpServletRequest multipartHttpServletRequest = (MultipartHttpServletRequest)req;
//				Map<String,MultipartFile> fileMap = multipartHttpServletRequest.getFileMap();
//				if(MapUtils.isNotEmpty(fileMap)){
//					for (String s : fileMap.keySet()) {
//						if(fileMap.get(s) != null && filedName.equals(fileMap.get(s).getName())){
//							return fileMap.get(s);
//						}
//					}
//				}
//			}
//		}catch (Exception e){
//
//		}
//		return null;
//	}

	public static String getFileExtension(String fileName) {
		if (Validators.isEmpty(fileName)) {
			return null;
		}

		int pointIndex = fileName.lastIndexOf(".");
		return pointIndex > 0 && pointIndex < fileName.length() ? fileName
				.substring(pointIndex + 1).toLowerCase() : null;
	}

	private static CommonsMultipartResolver newMultipartResolver(HttpServletRequest req){
		CommonsMultipartResolver resolver = new CommonsMultipartResolver(req.getSession().getServletContext());
		resolver.setDefaultEncoding(DEFAULT_ENCODE);
		return resolver;
	}
}
