package net.zdsoft.framework.webuploader.action;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.zdsoft.framework.config.Evn;
import net.zdsoft.framework.utils.ServletUtils;
import net.zdsoft.system.constant.Constant;
import net.zdsoft.system.remote.service.SysOptionRemoteService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
@Controller
public class FileDownloadController {
	@Autowired
	private SysOptionRemoteService sysOptionRemoteService;
	
	@RequestMapping(value = "/webuploader/dirfiles")
	@ResponseBody
	public void getFileFromDir(String path,ModelMap map,HttpServletResponse response){
		
		String fileSystemPath = Evn.<SysOptionRemoteService> getBean(
				"sysOptionRemoteService").findValue(Constant.FILE_PATH);// 文件系统地址
		String filePath = fileSystemPath + File.separator + "upload" + File.separator + path;
		int sysPathlength = (fileSystemPath + File.separator).length();
		File file = new File(filePath);
		JSONArray array = new JSONArray();
		if (file.exists()) {
			File[] files = file.listFiles();
			if(files!=null){
				for(File f:files){
					JSONObject jsonObject = new JSONObject();
					String fullpath = f.getPath();
					jsonObject.put("fileName",f.getName());
					jsonObject.put("filePath",fullpath.substring(sysPathlength));
					array.add(jsonObject);
				}
			}
		}
		try {
			ServletUtils.print(response, array.toJSONString());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@RequestMapping(value = "/webuploader/removefiles")
	@ResponseBody
	public String removeFile(String path,HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		String fileSystemPath = Evn.<SysOptionRemoteService> getBean(
				"sysOptionRemoteService").findValue(Constant.FILE_PATH);// 文件系统地址
		String filePath = fileSystemPath + File.separator + "upload" + File.separator + path;
		File file=new File(filePath);
		if (file.exists()) {
			File[] files = file.listFiles();
			for (File oneFile : files) {
				oneFile.delete();
			}
			file.delete();
		}
		if(!file.exists()){
			return "success";
		}else{
			return "error";
		}
	}
	
	@RequestMapping(value = "/webuploader/showpicture")
	@ResponseBody
	public void showPictrue(HttpServletRequest request, HttpServletResponse response){
		String filePath=request.getParameter("filePath");
		String fileSystemPath = Evn.<SysOptionRemoteService> getBean(
				"sysOptionRemoteService").findValue(Constant.FILE_PATH);// 文件系统地址
		File file = new File(fileSystemPath+ File.separator+filePath);
		if (file.exists()) {
			response.setContentType("image/jpeg; charset=GBK");
			ServletOutputStream outputStream;
			try {
				outputStream = response.getOutputStream();
				FileInputStream inputStream = new FileInputStream(file);
				byte[] buffer = new byte[1024];
				int i = -1;
				while ((i = inputStream.read(buffer)) != -1) {
					outputStream.write(buffer, 0, i);
				}
				outputStream.flush();
				outputStream.close();
				inputStream.close();
				outputStream = null;
			}
			catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	
	
}
