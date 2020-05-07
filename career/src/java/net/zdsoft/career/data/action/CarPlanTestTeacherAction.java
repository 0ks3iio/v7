package net.zdsoft.career.data.action;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.zdsoft.basedata.constant.BaseConstants;
import net.zdsoft.basedata.entity.Clazz;
import net.zdsoft.basedata.entity.StorageDir;
import net.zdsoft.basedata.entity.Student;
import net.zdsoft.basedata.remote.service.ClassRemoteService;
import net.zdsoft.basedata.remote.service.StorageDirRemoteService;
import net.zdsoft.basedata.remote.service.StudentRemoteService;
import net.zdsoft.career.data.entity.CarPlanResult;
import net.zdsoft.career.data.service.CarPlanResultService;
import net.zdsoft.framework.action.BaseAction;
import net.zdsoft.framework.annotation.ControllerInfo;
import net.zdsoft.framework.entity.Pagination;
import net.zdsoft.framework.entity.TR;
import net.zdsoft.framework.utils.HtmlToPdf;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.framework.utils.UrlUtils;

import org.apache.commons.collections.CollectionUtils;
import org.apache.curator.shaded.com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/careerplan/teacher")
public class CarPlanTestTeacherAction extends BaseAction{

	@Autowired
	private CarPlanResultService carPlanResultService;
	@Autowired
	private StorageDirRemoteService storageDirRemoteService;
	@Autowired
	private StudentRemoteService studentRemoteService;
	@Autowired
	private ClassRemoteService classRemoteService;
	
	@RequestMapping("/testresult/tab")
	@ControllerInfo("教师端-霍兰德测评结果Tab")
	public String testResultTab(){
		return "/career/teacher/questiontest/carTestTab.ftl";
	}
	
	@RequestMapping("/testresult/head")
	@ControllerInfo("教师端-霍兰德测评结果Head")
	public String testResultListHead(ModelMap map){
		String unitId = getLoginInfo().getUnitId();
		List<Clazz> clazzs = SUtils.dt(classRemoteService.findBySchoolId(unitId),new TR<List<Clazz>>() {});
		clazzs = clazzs.stream().filter(clazz -> clazz.getIsGraduate() == 0).collect(Collectors.toList());
		map.put("classList", clazzs);
		return "/career/teacher/questiontest/carTestListHead.ftl";
	}
	
	@RequestMapping("/testresult/content")
	@ControllerInfo("教师端-霍兰德问卷内容")
	public String testListContent(){
		return "/career/teacher/questiontest/carTestContent.ftl";
	}
	
	@RequestMapping("/testresult/analysis")
	@ControllerInfo("教师端-霍兰德问卷解析")
	public String testAnalysis(){
		return "/career/teacher/questiontest/carTestAnalysis.ftl";
	}
	
	@RequestMapping("/testresult/list")
	@ControllerInfo("教师端-霍兰德测评结果list")
	public String testResultList(String classId,String studentName,ModelMap map, HttpServletRequest request){
		String unitId = getLoginInfo().getUnitId();
		Pagination page = createPagination();
		try {
			studentName=URLDecoder.decode(studentName,"UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		List<CarPlanResult> carPlanTestResults = carPlanResultService.findAllByTimeAndName(unitId,classId,studentName,page);
		map.put("carPlanTestResults", carPlanTestResults);
		sendPagination(request, map, page);
		return "/career/teacher/questiontest/carTestResultList.ftl";
	}
	
	@RequestMapping("/testresult/show")
	@ControllerInfo("教师端-霍兰德测评结果详情")
	public String stuTestResult(String showIntro,String studentId,ModelMap map){
		List<CarPlanResult> testResult = carPlanResultService.findByStudentIds(new String[]{studentId});
		if (CollectionUtils.isNotEmpty(testResult)) {
			map.put("testResult", testResult.get(0));
		} else {
			map.put("testResult", new CarPlanResult());
		}
		map.put("showIntro", showIntro);
		return "/career/teacher/questiontest/carTestResult.ftl";
	}
	
	@ResponseBody
	@RequestMapping("/testresult/delete")
	@ControllerInfo("教师端-霍兰德测评结果删除")
	public String stuTestResultDelete(String studentIds,ModelMap map){
		try {
			String unitId = getLoginInfo().getUnitId();
			carPlanResultService.deleteTest(studentIds,unitId);
		} catch (Exception e) {
			e.printStackTrace();
			return error("删除失败！");
		}
		return success("删除成功");
	}
	
	@RequestMapping("/common/testresult/batchPrintId")
	@ControllerInfo("教师端-批量打印和PDF")
	public String batchPrintIds(String showprint,String studentIds,ModelMap map){
		String[] stuIds = studentIds.split(",");
		List<CarPlanResult> testResults = carPlanResultService.findByStudentIds(stuIds);
		if (CollectionUtils.isNotEmpty(testResults)) {
			map.put("testResults", testResults);
		}
		map.put("showprint", showprint);
		return "/career/teacher/questiontest/carTestBatchResult.ftl";
	}
	
	@ResponseBody
	@RequestMapping("/testresult/exportPDF")
	@ControllerInfo("教师端-导出PDF")
	public String exportPDF(String studentIds,HttpServletRequest request){
		String unitId = getLoginInfo().getUnitId();
		String[] studentId = studentIds.split(",");
		String urlStr = "";
		String uu = "";
		File f = null;
		String basePath = request.getServerName()+ ":" + request.getServerPort() + request.getContextPath();
		StorageDir dir=SUtils.dc(storageDirRemoteService.findOneById(BaseConstants.ZERO_GUID), StorageDir.class);
		Vector<Thread> vectors=new Vector<Thread>();
		for (String str : studentId) {
			urlStr = "/careerplan/teacher/common/testresult/batchPrintId?studentIds="+str;
			uu=dir.getDir()+File.separator+"careerplan"+File.separator+unitId+File.separator+str+".pdf";
			f = new File(uu);
			if (!f.exists()) {
				MyThread myThread=new MyThread();
				myThread.setUrlStr(basePath + urlStr);
				myThread.setUu(uu);
				Thread thread=new Thread(myThread);
				vectors.add(thread);
				thread.start();
			}
		}
		for(Thread thread:vectors){
			try {
				thread.join();//使用join来保证thread的多个线程都执行完后，才执行主线程  
			} catch (InterruptedException e) {
				e.printStackTrace();
				return error("生成PDF失败！");
			}
		}
		return success("生成PDF成功！");
	}
	
	@RequestMapping("/testresult/downPDF")
	@ControllerInfo(value = "教师端-下载PDF")
	public void savepdf(String studentIds,HttpServletRequest request,HttpServletResponse response) {
		String unitId = getLoginInfo().getUnitId();
		String[] stuIds = studentIds.split(",");
		StorageDir dir=SUtils.dc(storageDirRemoteService.findOneById(BaseConstants.ZERO_GUID), StorageDir.class);
		List<String> urls = Lists.newArrayList();
		for (String str : stuIds) {
			urls.add(dir.getDir()+File.separator+"careerplan"+File.separator+unitId+File.separator+str+".pdf");
		}
		List<Student> students = SUtils.dt(studentRemoteService.findListByIds(stuIds), new TR<List<Student>>() {});
		Map<String,String> studentName = students.stream().collect(Collectors.toMap(Student::getId, Student::getStudentName));
		if (CollectionUtils.isNotEmpty(urls)) {
			if (urls.size() > 1) {
				exportZip(response,urls,"生涯兴趣调查报告",studentName);
			} else {
				File file = new File(urls.get(0));
				if(file.exists()){
					try {
						InputStream inStream = new FileInputStream(file);
						response.reset();
						response.setContentType("bin");
						response.addHeader("Content-Disposition", "attachment; filename=\"" + UrlUtils.encode(studentName.get(file.getName().substring(0, file.getName().indexOf("."))), "UTF-8") + ".pdf" + "\"");
						// 循环取出流中的数据
						byte[] b = new byte[1024];
						int len;
						try {
							while ((len = inStream.read(b)) > 0)
								response.getOutputStream().write(b, 0, len);
							inStream.close();
						} catch (IOException e) {
							e.printStackTrace();
						}
					} catch (FileNotFoundException e1) {
						e1.printStackTrace();
					}
				}else{
					return ;
				} 
			}
		} else {
			return;
		}
	}
	
	public void exportZip(HttpServletResponse response,List<String> sourcePath,String fileName,Map<String,String> studentName){
		FileInputStream fis = null;  
        BufferedInputStream bis = null;  
        ZipOutputStream zos = null; 
        try {
        	File[] sourceFiles = new File[sourcePath.size()]; 
        	File file;
        	for (int i = 0;i <sourcePath.size();i++) {
        		file = new File(sourcePath.get(i));
        		if (file.exists()) {
        			sourceFiles[i] = file;
        		}
        	}
        	if (null != sourceFiles && sourceFiles.length>1) {
        		response.setHeader("Content-Disposition", "attachment; filename="+ UrlUtils.encode(fileName, "UTF-8") + ".zip");
        		zos = new ZipOutputStream( new BufferedOutputStream(response.getOutputStream()));  
        		byte[] bufs = new byte[1024*10];  
        		for(int i=0;i<sourceFiles.length;i++){  
        			//创建ZIP实体，并添加进压缩包  
        			ZipEntry zipEntry = new ZipEntry(studentName.get(sourceFiles[i].getName().substring(0, sourceFiles[i].getName().indexOf("."))) + ".pdf");  
        			zos.putNextEntry(zipEntry);  
        			//读取待压缩的文件并写进压缩包里  
        			fis = new FileInputStream(sourceFiles[i]);  
        			bis = new BufferedInputStream(fis, 1024*10);  
        			int read = 0;  
        			while((read=bis.read(bufs, 0, 1024*10)) != -1){  
        				zos.write(bufs,0,read);  
        			}  
        		}  
        	}
        } catch (FileNotFoundException e) {  
        	e.printStackTrace();  
        	throw new RuntimeException(e);  
        } catch (IOException e) {  
        	e.printStackTrace();  
        	throw new RuntimeException(e);  
        }finally{  
        	try {  
        		if(null != bis) bis.close();  
        		if(null != zos) zos.close();  
        	} catch (IOException e) {  
        		e.printStackTrace();  
        		throw new RuntimeException(e);  
        	}  
        } 
	}
	
	class MyThread implements Runnable{

		private String urlStr;
		private String uu;
		
		@Override
		public void run() {
			HtmlToPdf.convert(new String[]{urlStr}, uu,null,"1300",2000);
		}


		public String getUrlStr() {
			return urlStr;
		}

		public void setUrlStr(String urlStr) {
			this.urlStr = urlStr;
		}

		public String getUu() {
			return uu;
		}

		public void setUu(String uu) {
			this.uu = uu;
		}
	}
}
