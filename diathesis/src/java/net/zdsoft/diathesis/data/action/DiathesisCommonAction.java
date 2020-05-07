package net.zdsoft.diathesis.data.action;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.ImmutableMap;
import net.zdsoft.basedata.remote.service.FilePathRemoteService;
import net.zdsoft.framework.action.BaseAction;
import net.zdsoft.framework.annotation.ControllerInfo;
import net.zdsoft.framework.config.Evn;
import net.zdsoft.framework.utils.ExportUtils;
import net.zdsoft.framework.utils.ResponseUtil;
import net.zdsoft.framework.utils.UuidUtils;
import net.zdsoft.system.constant.Constant;
import net.zdsoft.system.remote.service.SysOptionRemoteService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Controller
@RequestMapping("/diathesis/common")
public class DiathesisCommonAction extends BaseAction {

	@Autowired
	private FilePathRemoteService filePathRemoteService;
	
	@ResponseBody
    @RequestMapping("/execute")
    public String execute() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("businessKey", UuidUtils.generateUuid());
        return jsonObject.toJSONString();
    }

    @RequestMapping("/exportErrorExcel")
    @ControllerInfo("导出错误信息")
    public void exportError(HttpServletRequest request, HttpServletResponse response) {
        String errorFilePath = request.getParameter("errorPath");
        try {
            ExportUtils.outputFile(errorFilePath, "错误信息.xls", response);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    @RequestMapping("/downloadFile")
    @ControllerInfo("显示照片/下载文件")
    public String downloadFile(String filePath, HttpServletResponse response) {
        try {
            File file;
            if (StringUtils.isNotEmpty(filePath)) {
                String fileSystemPath = filePathRemoteService.getFilePath();// 文件系统地址
                file = new File(fileSystemPath + File.separator + filePath);
                if (file != null && file.exists()) {
                    ExportUtils.outputFile(file, file.getName(), response);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @RequestMapping(value = "/webuploader/upload", method = RequestMethod.POST)
    @ResponseBody
    public void uploadFile(@RequestParam("file") MultipartFile[] files,
                           HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        String key = request.getParameter("key");
        if(StringUtils.isEmpty(key)){
        	throw new Exception("参数缺失！");
        }
        String fileSystemPath = Evn.<SysOptionRemoteService> getBean(
                "sysOptionRemoteService").findValue(Constant.FILE_PATH);
        File dir = new File(fileSystemPath);
        if (!dir.exists())
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

}
