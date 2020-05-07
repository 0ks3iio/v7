package net.zdsoft.career.data.action;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.zdsoft.basedata.constant.BaseConstants;
import net.zdsoft.basedata.entity.StorageDir;
import net.zdsoft.basedata.entity.Student;
import net.zdsoft.basedata.remote.service.StorageDirRemoteService;
import net.zdsoft.basedata.remote.service.StudentRemoteService;
import net.zdsoft.career.data.entity.CarPlanResult;
import net.zdsoft.career.data.service.CarPlanResultService;
import net.zdsoft.career.data.service.CarTypeResultService;
import net.zdsoft.framework.action.BaseAction;
import net.zdsoft.framework.annotation.ControllerInfo;
import net.zdsoft.framework.utils.HtmlToPdf;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.framework.utils.UrlUtils;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/careerplan/student")
public class CarPlanTestStuAction extends BaseAction{
	
	@Autowired
	private CarPlanResultService carPlanResultService;
	@Autowired
	private CarTypeResultService carTypeResultService;
	@Autowired
	private StorageDirRemoteService storageDirRemoteService;
	@Autowired
	private StudentRemoteService studentRemoteService;
	
	@RequestMapping("/question/page")
	@ControllerInfo("学生端-空白页面")
	public String showPage(){
		return "/career/student/questiontest/carEmptyPage.ftl";
	}
	
	@RequestMapping("/question/list")
	@ControllerInfo("学生端-跳转到问卷调查页")
	public String showQuestionList(ModelMap map){
		return "/career/student/questiontest/carTestList.ftl";
	}
	
	@RequestMapping("/resultorlist")
	@ControllerInfo("学生端-霍兰德测评页")
	public String showResultOrList(ModelMap map){
		String studentId = getLoginInfo().getOwnerId();
		List<CarPlanResult> testResult = carPlanResultService.findByStudentIds(new String[]{studentId});
		if (CollectionUtils.isNotEmpty(testResult)) {
			map.put("testResult", testResult.get(0));
			return "/career/student/questiontest/carTestResult.ftl";
		} else {
			return "/career/student/questiontest/carTestList.ftl";
		}
	}
	
	@ResponseBody
	@RequestMapping("/testresult/save")
	@ControllerInfo("学生端-分析和保存霍兰德测评结果")
	public String testResultSave(CarPlanResult testResult){
		String unitId = getLoginInfo().getUnitId();
		String studentId = getLoginInfo().getOwnerId();
		try {
			carPlanResultService.saveTestResult(testResult,unitId,studentId);
		} catch (Exception e) {
			e.printStackTrace();
			return error("保存失败！");
		}
		return success("保存成功");
	}
	
	@RequestMapping("/common/pdfResult")
	@ControllerInfo("学生端-导出PDF霍兰德测评页")
	public String showPDFResult(String studentId,ModelMap map){
		List<CarPlanResult> testResult = carPlanResultService.findByStudentIds(new String[]{studentId});
		map.put("testResult", testResult.get(0)); 
		return "/career/student/questiontest/carTestResultPDF.ftl";
	}
	
	@ResponseBody
	@RequestMapping("/testresult/printPDF")
	@ControllerInfo("学生端-生成PDF")
	public String exportPDF(HttpServletRequest request){
		String studentId = getLoginInfo().getOwnerId();
		String unitId = getLoginInfo().getUnitId();
		String urlStr = "/careerplan/student/common/pdfResult?studentId="+studentId;
		StorageDir dir=SUtils.dc(storageDirRemoteService.findOneById(BaseConstants.ZERO_GUID), StorageDir.class);
		String basePath = request.getServerName()+ ":" + request.getServerPort() + request.getContextPath();
		String uu=dir.getDir()+File.separator+"careerplan"+File.separator+unitId+File.separator+studentId+".pdf";
		File f = new File(uu);
		if(f.exists()){
			return f.getAbsolutePath();
		}else{
			HtmlToPdf.convert(new String[]{basePath + urlStr}, uu,null,"1300",10000);
		}	
		f = new File(uu);
		if(f.exists()){
			return f.getAbsolutePath();
		}else{
			return "";
		}
	}
	
	@RequestMapping("/testresult/downPDF")
	@ControllerInfo("学生端-下载PDF")
	public void savepdf(String downPath,HttpServletRequest request,HttpServletResponse response) {
		File file = new File(downPath);
		if(file.exists()){
			String studentId = downPath.substring(downPath.lastIndexOf(File.separator)+1,downPath.lastIndexOf("."));
			Student student = SUtils.dc(studentRemoteService.findOneById(studentId), Student.class);
			try {
				InputStream inStream = new FileInputStream(file);
				response.reset();
		        response.setContentType("bin");
		        response.addHeader("Content-Disposition", "attachment; filename=\"" + UrlUtils.encode(student.getStudentName(), "UTF-8") + ".pdf" + "\"");
		        // 循环取出流中的数据
		        byte[] b = new byte[100];
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
			return;
		} 
	}
}
