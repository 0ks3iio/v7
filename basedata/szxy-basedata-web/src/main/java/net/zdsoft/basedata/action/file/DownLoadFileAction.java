package net.zdsoft.basedata.action.file;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.zdsoft.framework.config.Evn;
import net.zdsoft.framework.utils.StringUtils;
import net.zdsoft.system.utils.PathUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class DownLoadFileAction {
	private static Logger logger = LoggerFactory
			.getLogger(DownLoadFileAction.class);

	@RequestMapping("/common/download/file")
	public void download(String filePath,
			HttpServletResponse response) throws Exception {
		InputStream fis = null;
		OutputStream toClient = null;
		try {
			// path是指欲下载的文件的路径。
			File file = new File(filePath);
			// 取得文件名。
			String filename = file.getName();
			// 以流的形式下载文件。
			fis = new BufferedInputStream(new FileInputStream(filePath));
			byte[] buffer = new byte[fis.available()];
			fis.read(buffer);
			fis.close();
			// 清空response
			response.reset();
			// 设置response的Header
			response.addHeader("Content-Disposition", "attachment;filename="
					+ URLEncoder.encode(filename, "utf-8"));
			response.addHeader("Content-Length", "" + file.length());
			toClient = new BufferedOutputStream(response.getOutputStream());
			response.setContentType("application/octet-stream");
			toClient.write(buffer);
			toClient.flush();
			toClient.close();


		} catch (Exception x) {
			logger.error(x.getMessage());
		} finally {
			if (toClient != null) {
				toClient.close();
			}
			if (fis != null) {
				fis.close();
			}
		}
	}

	@SuppressWarnings("deprecation")
	@RequestMapping(value = "/common/showpicture")
	@ResponseBody
	public void showPicture(HttpServletRequest request,
			HttpServletResponse response) {
		String filePath = request.getParameter("filePath");
		String defaultPath = request.getParameter("defaultPath");
		String defaultRealPath = "";
		boolean useDefault = false;
		File picFile = null;
		if (StringUtils.isBlank(filePath)) {
			useDefault = true;
		} else {
			String fileSystemPath = PathUtils.getFilePath();// 文件系统地址
			if (StringUtils.isNotBlank(fileSystemPath)) {
				picFile = new File(fileSystemPath + File.separator + filePath);
			} else {
				useDefault = true;
			}
		}
		if (useDefault || !picFile.exists()) {
			if (StringUtils.isNotBlank(defaultPath)) {
				defaultRealPath = Evn.getRequest().getRealPath(defaultPath);
				picFile = new File(defaultRealPath);
			}
		}
		if (picFile == null || !picFile.exists()) {
			return;
		}
		response.setContentType("image/jpeg; charset=GBK");
		response.addHeader("Cache-Control", "max-age=604800");
		ServletOutputStream outputStream = null;
		FileInputStream inputStream = null;
		try {
			outputStream = response.getOutputStream();
			inputStream = new FileInputStream(picFile);
			byte[] buffer = new byte[1024];
			int i = -1;
			while ((i = inputStream.read(buffer)) != -1) {
				outputStream.write(buffer, 0, i);
			}
			outputStream.flush();
			outputStream.close();
			inputStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (outputStream != null) {
				try {
					outputStream.close();
				} catch (IOException e) {
				}
				outputStream = null;
			}
			if (inputStream != null) {
				try {
					inputStream.close();
				} catch (IOException e) {
				}
				inputStream = null;
			}
		}
	}
}
