/*
 * Project: v7
 * Author : shenke
 * @(#) UeditorController.java Created on 2016-10-11
 * @Copyright (c) 2016 ZDSoft Inc. All rights reserved
 */
package net.zdsoft.framework.ueditor.action;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.zdsoft.framework.action.BaseAction;
import net.zdsoft.framework.ueditor.ActionEnter;
import net.zdsoft.system.utils.PathUtils;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @description: 
 *               (這裡的主要目的是為了解決無法訪問/static/ueditor/controller.jsp的問題,待解決這個項目之後再進行考慮好了
 *               ) 同時將原來6.0中的下載圖片等方法進行遷移和重寫
 * @author: shenke
 * @version: 1.0
 * @date: 2016-10-11下午3:09:41
 */
@Controller
@RequestMapping(value = { "/static/ueditor/jsp","/common" })
public class UeditorController extends BaseAction {

	private static final Logger log = LoggerFactory
			.getLogger(UeditorController.class);

	@ResponseBody
	@RequestMapping(value = { "/controller.jsp","/controller"})
	public String execute(String action,HttpServletRequest request) {
		String exec = StringUtils.EMPTY;
		try {

			HttpServletResponse response = getResponse();
			request.setCharacterEncoding("utf-8");
			response.setHeader("Content-Type", "text/html");

			String rootPath = request.getSession().getServletContext()
					.getRealPath("/");

			exec = new ActionEnter(request, rootPath).exec();
			request.getParameterMap();
			// response.getWriter().write( e );
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return exec;
	}

	/**
	 * 下載
	 * 
	 * @param response
	 * @param filename
	 * @param filePath
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/downloadFile.action")
	public String downloadFile(HttpServletResponse response, String filename,
			String filePath) {

		try {
			String path = null;
			if (StringUtils.isBlank(filename)) {
				filename = "";
				path = filePath;
			} else {
				path = PathUtils.getFilePath() + File.separator + filePath
						+ File.separator + filename;
			}

			File storefile = new File(path);

			if (!storefile.exists()) {
				throw new FileNotFoundException(storefile.getPath());
			}

			if (log.isDebugEnabled()) {
				log.debug("读取文件: " + storefile.getAbsolutePath());
			}

			InputStream data = new FileInputStream(storefile);
			int fileSize = (int) storefile.length();
			String mimeType = getRequest().getSession().getServletContext()
					.getMimeType(storefile.getAbsolutePath());

			if ((mimeType == null) || mimeType.trim().equals("")) {
				mimeType = "application/unknown";
			} else if (mimeType.startsWith("text/html")) {
				mimeType = "application/unknown";
			}
			mimeType = "application/x-msdownload";

			response.setHeader("Content-Disposition", "attachment; filename="
					+ URLEncoder.encode(filename, "UTF-8"));

			response.setContentType(mimeType);
			response.setContentLength(fileSize);
			copyStream(response.getOutputStream(), data);
		} catch (Exception e) {
			return error("文件下载出错！\n错误信息[" + e.getMessage() + "]");
		}
		return success("成功");
	}

	@ResponseBody
	@RequestMapping("")
	public String showPicture() {
		return "";
	}

	private void copyStream(OutputStream out, InputStream in) throws Exception {

		try {
			byte[] buffer = new byte[128 * 1024];
			int read_count;

			while ((read_count = in.read(buffer)) != -1) {
				out.write(buffer, 0, read_count);
			}
			out.flush();
		} catch (Exception e) {
			throw new IOException("Error while serving the requested file!");
		} finally {
			if (in != null) {
				in.close();
			}

			if (out != null) {
				out.close();
			}
		}
	}
}
