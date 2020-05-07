package net.zdsoft.datareport.data.action;

import net.zdsoft.basedata.constant.BaseConstants;
import net.zdsoft.basedata.dto.AttFileDto;
import net.zdsoft.basedata.entity.Attachment;
import net.zdsoft.basedata.entity.StorageDir;
import net.zdsoft.basedata.remote.service.AttachmentRemoteService;
import net.zdsoft.basedata.remote.service.FilePathRemoteService;
import net.zdsoft.basedata.remote.service.StorageDirRemoteService;
import net.zdsoft.datareport.data.constants.ReportConstants;
import net.zdsoft.datareport.data.entity.DataReportInfo;
import net.zdsoft.datareport.data.entity.DataReportObj;
import net.zdsoft.datareport.data.entity.DataReportTask;
import net.zdsoft.datareport.data.service.DataReportColumnService;
import net.zdsoft.datareport.data.service.DataReportInfoService;
import net.zdsoft.datareport.data.service.DataReportObjService;
import net.zdsoft.datareport.data.service.DataReportResultsService;
import net.zdsoft.datareport.data.service.DataReportTableService;
import net.zdsoft.datareport.data.service.DataReportTaskService;
import net.zdsoft.framework.action.BaseAction;
import net.zdsoft.framework.annotation.ControllerInfo;
import net.zdsoft.framework.entity.TR;
import net.zdsoft.framework.utils.EntityUtils;
import net.zdsoft.framework.utils.ExportUtils;
import net.zdsoft.framework.utils.HtmlToPdf;
import net.zdsoft.framework.utils.RedisUtils;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.framework.utils.UrlUtils;
import net.zdsoft.system.remote.service.SysOptionRemoteService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.curator.shaded.com.google.common.collect.Maps;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Controller
@RequestMapping("/datareport/table")
public class DataReportTableAction extends BaseAction{
	
	@Autowired
	private StorageDirRemoteService storageDirRemoteService;
	@Autowired
	private AttachmentRemoteService attachmentRemoteService;
	@Autowired
	private DataReportTableService dataReportTableService;
	@Autowired
	private DataReportInfoService dataReportInfoService;
	@Autowired
	private DataReportTaskService dataReportTaskService;
	@Autowired
	private DataReportColumnService dataReportColumnService;
	@Autowired
	private DataReportResultsService dataReportResultsService;
	@Autowired
	private DataReportObjService dataReportObjService;
	@Autowired
	private FilePathRemoteService filePathRemoteService;
	@Autowired
	private SysOptionRemoteService sysOptionRemoteService;
	
	@ResponseBody
	@RequestMapping("/attfiles/save")
	@ControllerInfo("附件保存")
	public String attFilesSave(String array,String taskId) {
		try{
			array = StringEscapeUtils.unescapeHtml(array);
			List<AttFileDto> fileDtos = SUtils.dt(array,new TR<List<AttFileDto>>() {});
			String fileNames = dataReportTableService.saveAttFiles(fileDtos,taskId,getLoginInfo().getUnitId());
			if (StringUtils.isNotEmpty(fileNames)) {
				return success(fileNames); 
			} else {
				return error("保存失败没有文件"); 
			}
		}catch (Exception e) {
			e.printStackTrace();
			return error("保存失败！"+e.getMessage());
		}
	}
	
	@ResponseBody
	@RequestMapping("/attfiles/delete")
	@ControllerInfo("附件删除")
	public String attFilesSave(String attId) {
		try {
			attachmentRemoteService.deleteAttachments(new String[]{attId}, null);
		} catch (Exception e) {
			e.printStackTrace();
			return error("删除失败");
		}
		return success("删除成功");
	}
	
	@RequestMapping("/downattfiles")
	@ControllerInfo(value="下载附件文件")
	public void downAttFiles(String infoId, String taskId,HttpServletRequest request,HttpServletResponse response) {
		Map<String,Map<File,String>> fileMap = Maps.newHashMap();
		Map<String,String> objNameMaps = Maps.newHashMap();
		List<Attachment> attachments = null;
		String filePath = getFilePath();
		Map<String, String> objFileNameMap = new HashMap<>();
		if (StringUtils.isNotEmpty(infoId)) {
			List<DataReportTask> dataReportTasks = dataReportTaskService.findByReportId(infoId,null);
			if(CollectionUtils.isNotEmpty(dataReportTasks)){
				dataReportTasks = dataReportTasks.stream().filter(e->e.getState() == ReportConstants.REPORT_TASK_STATE_3).collect(Collectors.toList());
			}
			String[] objIds = EntityUtils.getArray(dataReportTasks, DataReportTask::getObjId, String[]::new);
			String[] taskIds = EntityUtils.getArray(dataReportTasks, DataReportTask::getId, String[]::new);
			if (ArrayUtils.isNotEmpty(objIds)) {
				List<DataReportObj> objList = dataReportObjService.findByObjIds(objIds);
				Map<String, String> objMaps = EntityUtils.getMap(objList, DataReportObj::getId, DataReportObj::getObjectName);
				int i = 0;
				for (DataReportTask task : dataReportTasks) {
					objNameMaps.put(task.getId(), objMaps.get(task.getObjId()));
				}
			}
			if (ArrayUtils.isNotEmpty(taskIds)) {
				attachments = SUtils.dt(attachmentRemoteService.findAttachmentDirPathByObjTypeAndId(ReportConstants.REPORT_TASK_ACCESSORY,taskIds),new TR<List<Attachment>>(){});
			} else {
				attachments = new ArrayList<>();
			}
			Map<File,String> fileNameMap = null;
			File file;
			for (Attachment att : attachments) {
				file = new File(filePath + File.separator + att.getFilePath());
				if (file.exists()) {
					String fns = objFileNameMap.get(att.getObjId());
					if(StringUtils.isNotEmpty(fns)){
						continue;
					}
					fns = att.getFilename();
					objFileNameMap.put(att.getObjId(), fns);
					if (fileMap.get(att.getObjId())!=null) {
						fileNameMap = fileMap.get(att.getObjId());
					} else {
						fileNameMap = Maps.newHashMap();
					}
					fileNameMap.put(file, att.getFilename());
					fileMap.put(att.getObjId(), fileNameMap);
				}
			}
		} else {
			DataReportTask task = dataReportTaskService.findOne(taskId);
			DataReportObj obj = dataReportObjService.findById(task.getObjId());
			objNameMaps.put(task.getId(), obj.getObjectName());
			attachments = SUtils.dt(attachmentRemoteService.findAttachmentDirPathByObjTypeAndId(ReportConstants.REPORT_TASK_ACCESSORY,taskId),new TR<List<Attachment>>(){});
			Map<File,String> fileNameMap = Maps.newHashMap();
			File file;
			for (Attachment att : attachments) {
				file = new File(filePath + File.separator + att.getFilePath());
				if (file.exists()) {
					String fns = objFileNameMap.get(att.getObjId());
					if(StringUtils.isNotEmpty(fns)){
						continue;
					}
					fns = att.getFilename();
					objFileNameMap.put(att.getObjId(), fns);
					fileNameMap.put(file, att.getFilename());
				}
			}
			fileMap.put(taskId, fileNameMap);
		}
		objFileNameMap = null;

		FileInputStream fis = null;  
        BufferedInputStream bis = null;  
        ZipOutputStream zos = null; 
        try {
        	if (fileMap.size()>=1) {
        		response.setHeader("Content-Disposition", "attachment; filename="+ UrlUtils.encode("附件", "UTF-8") + ".zip");
        		zos = new ZipOutputStream( new BufferedOutputStream(response.getOutputStream()));  
        		byte[] bufs = new byte[1024*10];
				for (Map.Entry<String, Map<File, String>> keyEntry : fileMap.entrySet()) {
					String tId = keyEntry.getKey();
					log.error("对象附件开始====" + objNameMaps.get(tId));
					for (Map.Entry<File, String> entry : keyEntry.getValue().entrySet()) {
						log.error("对象附件开始====" + objNameMaps.get(tId) + "=====附件===" + entry.getValue());
						//创建ZIP实体，并添加进压缩包
						ZipEntry zipEntry = new ZipEntry(objNameMaps.get(tId) + File.separator + entry.getValue());
						zos.putNextEntry(zipEntry);
						//读取待压缩的文件并写进压缩包里
						fis = new FileInputStream(entry.getKey());
						bis = new BufferedInputStream(fis, 1024 * 10);
						int read = 0;
						while ((read = bis.read(bufs, 0, 1024 * 10)) != -1) {
							zos.write(bufs, 0, read);
						}
					}
				}
        	}
        } catch (FileNotFoundException e) {  
        	log.error(e.getMessage(), e);
        	throw new RuntimeException(e);  
        } catch (IOException e) {
			log.error(e.getMessage(), e);
        	throw new RuntimeException(e);  
        }finally{  
        	try {  
        		if(null != bis) bis.close();  
        		if(null != zos) zos.close();  
        	} catch (IOException e) {
				log.error(e.getMessage(), e);
        		throw new RuntimeException(e);  
        	}  
        } 
	}
	
	@ResponseBody
	@RequestMapping("/loadexceltable")
	@ControllerInfo("下载excel:模板或上传的文件")
	public void loadtemplate(String objId, String objectType, HttpServletResponse response){
		List<Attachment> attachments = SUtils.dt(attachmentRemoteService.findAttachmentDirPathByObjTypeAndId(objectType, objId),new TR<List<Attachment>>(){});
		if (CollectionUtils.isNotEmpty(attachments)) {
			String fileName = null;
			DataReportInfo dataReportInfo = null;
			if (ReportConstants.REPORT_INFO_TEMPLATE.equals(objectType)) {
				dataReportInfo = dataReportInfoService.findOne(objId);
				fileName = dataReportInfo.getTitle() + ".xls";
			}
			if (ReportConstants.REPORT_TASK_ATT.equals(objectType)) {
				DataReportTask dataReportTask = dataReportTaskService.findOne(objId);
				dataReportInfo = dataReportInfoService.findOne(dataReportTask.getReportId());
				DataReportObj dataReportObj = dataReportObjService.findById(dataReportTask.getObjId());
				fileName = dataReportInfo.getTitle() + "-" + dataReportObj.getObjectName() + ".xls";
			}
			if (ReportConstants.REPORT_INFO_STATS.equals(objectType)) {
				dataReportInfo = dataReportInfoService.findOne(objId);
				fileName = dataReportInfo.getTitle() + "-统计" + ".xls";
			}
			Attachment attachment = attachments.get(0);
			StorageDir dir = SUtils.dc(storageDirRemoteService.findOneById(attachment.getDirId()), StorageDir.class);
			try {
				ExportUtils.outputFile(dir.getDir() + File.separator + attachment.getFilePath(), fileName, response);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	@ResponseBody
	@RequestMapping("/createexcel")
	@ControllerInfo("生成Excel模板文件")
	public String createExcel(String infoId, Integer tableType) {
		String unitId = getLoginInfo().getUnitId();
		try {
			dataReportTableService.createExcel(infoId,unitId,tableType);
		} catch (Exception e) {
			try {
				dataReportInfoService.deleteOrRevokeInfo(infoId, unitId, 1);
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
			return error("失败");
		}
		return success("成功");
	}
	
	@ResponseBody
	@RequestMapping("/printPDF")
	@ControllerInfo("生成PDF")
	public String exportPDF(String infoId, String taskId, Integer type, Integer tableType, HttpServletRequest request){
		String unitId = getLoginInfo().getUnitId();
		String urlStr = "/datareport/infomanage/common/showtaskPDF?infoId="+infoId+"&taskId="+taskId+"&type="+type+"&tableType="+tableType;
		StorageDir dir=SUtils.dc(storageDirRemoteService.findOneById(BaseConstants.ZERO_GUID), StorageDir.class);
		String basePath = request.getServerName()+ ":" + request.getServerPort() + request.getContextPath();
		String uu = "";
		
		if (Objects.equals(type, 1)) {
			uu=dir.getDir()+File.separator+"datareport"+File.separator+unitId+File.separator+taskId+".pdf";			
		} else {
			uu=dir.getDir()+File.separator+"datareport"+File.separator+unitId+File.separator+infoId+".pdf";
		}
		File f = new File(uu);
		if(f.exists()){
			return f.getAbsolutePath();
		}else{
			HtmlToPdf.convert(new String[]{basePath + urlStr}, uu, null,null,2000);
		}	
		f = new File(uu);
		if(f.exists()){
			return f.getAbsolutePath();
		}else{
			return "";
		}
	}
	
	@RequestMapping("/downPDF")
	@ControllerInfo("下载PDF")
	public void savepdf(String infoId,String taskId,String downPath,Integer type,HttpServletRequest request,HttpServletResponse response) {
		File file = new File(downPath);
		if(file.exists()){
			DataReportInfo dataInfo =  dataReportInfoService.findOne(infoId);
			String pdfName = "";
			if (Objects.equals(type, 1)) {
				DataReportTask dataTask = dataReportTaskService.findOne(taskId);
				DataReportObj dataObj = dataReportObjService.findById(dataTask.getObjId());
				pdfName = dataInfo.getTitle() + "-" + dataObj.getObjectName();
			} else {
				pdfName = dataInfo.getTitle() + "-统计";
			}
			try (InputStream inStream = new FileInputStream(file)) {
				response.reset();
		        response.setContentType("bin");
		        response.addHeader("Content-Disposition", "attachment; filename=\"" + UrlUtils.encode(pdfName, "UTF-8") + ".pdf" + "\"");
		        // 循环取出流中的数据
		        byte[] b = new byte[1024];
		        int len = 0;
		        while ((len = inStream.read(b)) > 0) {
		        	response.getOutputStream().write(b, 0, len);
		        }
			} catch (IOException e) {
				e.printStackTrace();
			}
		}else{
			return;
		} 
	}
	
	/**
	 * 生成Excel文件
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/exportexcel")
	@ControllerInfo("生成Excel文件")
	public String exportExcel(String infoId, String taskId, Integer tableType, Integer type) {
		try {
			dataReportTableService.exportExcel(infoId,taskId,tableType,type);
		} catch (Exception e) {
			e.printStackTrace();
			return error("失败");
		}
		return success("成功");
	}
	
	private String getFilePath() {
		String key = "Constant.SystemIni@" + "FILE.PATH";
		String path = RedisUtils.get(key);
		if (StringUtils.isBlank(path)) {
			path = sysOptionRemoteService.findValue("FILE.PATH")+File.separator+"store";
			if (StringUtils.isNotBlank(path)) {
				RedisUtils.set(key, path);
			}
		}
		return StringUtils.isBlank(path) ? "/opt/data/store" : path;
	}
	
	@ResponseBody
	@RequestMapping("/datastructure")
	@ControllerInfo("修改数据结构")
	public String dataStructure(){
		try{
			dataReportTableService.saveNewStructure();
		}catch (Exception e) {
			e.printStackTrace();
			return error("保存失败！"+e.getMessage());
		}
		return success("保存成功");
	}
	
}
