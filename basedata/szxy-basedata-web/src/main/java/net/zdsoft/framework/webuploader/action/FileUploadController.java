package net.zdsoft.framework.webuploader.action;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.zdsoft.framework.annotation.ControllerInfo;
import net.zdsoft.framework.config.Evn;
import net.zdsoft.framework.utils.ResponseUtil;
import net.zdsoft.system.constant.Constant;
import net.zdsoft.system.remote.service.SysOptionRemoteService;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.google.common.collect.ImmutableMap;

/**
 * 
 * @author jiang feng
 *
 */
@Controller
public class FileUploadController {

	@ControllerInfo("照片上传测试页面")
	@RequestMapping("/webuploader/upload/pic")
	public String pic() {
		return "/fw/commonftl/uploadpic.ftl";
	}

	@RequestMapping(value = "/webuploader/upload", method = RequestMethod.POST)
	@ResponseBody
	public void uploadFile(@RequestParam("file") MultipartFile[] files,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		String key = request.getParameter("key");
		String fileSystemPath = Evn.<SysOptionRemoteService> getBean(
				"sysOptionRemoteService").findValue(Constant.FILE_PATH);// 文件系统地址
		//fileSystemPath = "d://test";
		File dir = new File(fileSystemPath);
		if (!dir.exists())// 目录不存在则创建
			dir.mkdirs();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		String ymd = sdf.format(new Date());

		String filePath = File.separator + "upload" + File.separator + ymd
				+ File.separator + key;
		String savePath = fileSystemPath + filePath;
		String fullPath = "";
		try {
			System.out.println("临时保存目录：" + savePath);
			for (MultipartFile file : files) {
				File destFile = new File(savePath);
				if (!destFile.exists()) {
					destFile.mkdirs();
				}
				String tfilePath = destFile.getAbsolutePath() + File.separator
						+ file.getOriginalFilename();
				System.out.println(tfilePath);
				File f = new File(tfilePath);
				file.transferTo(f);
				f.createNewFile();
				fullPath = f.getPath();
			}
			response.setCharacterEncoding("UTF-8");
			ResponseUtil.doResponse(response, ImmutableMap.of("path", savePath,
					"fullPath", fullPath, "relativePath", filePath));
		} catch (Exception e) {
			throw e;
		}
	}

	@RequestMapping(value = "/webuploader/remove")
	@ResponseBody
	public String removeFile(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String fullPath = request.getParameter("fullPath");
		File file = new File(fullPath);
		if (file.delete()) {
			return "success";
		} else {
			return "error";
		}
	}
}
