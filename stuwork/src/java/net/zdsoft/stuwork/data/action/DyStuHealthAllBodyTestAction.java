package net.zdsoft.stuwork.data.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.google.common.collect.Maps;

import net.zdsoft.basedata.entity.Student;
import net.zdsoft.basedata.remote.service.StudentRemoteService;
import net.zdsoft.framework.action.BaseAction;
import net.zdsoft.framework.annotation.ControllerInfo;
import net.zdsoft.framework.utils.EntityUtils;
import net.zdsoft.framework.utils.ExportUtils;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.stuwork.data.entity.DyStuHealthResult;
import net.zdsoft.stuwork.data.service.DyStuHealthResultService;
import net.zdsoft.system.entity.mcode.McodeDetail;
import net.zdsoft.system.remote.service.McodeRemoteService;

@Controller
@RequestMapping("/stuwork")
public class DyStuHealthAllBodyTestAction extends BaseAction {

	@Autowired
	private DyStuHealthResultService dyStuHealthResultService;
	@Autowired
	private StudentRemoteService studentRemoteService;
	@Autowired
	private McodeRemoteService mcodeRemoteService;
	
	
	@RequestMapping("/health/stat/index")
	@ControllerInfo("页面的第一次跳转")
	public String showIndex(){
		
		return "/stuwork/health/allBodyTest/healthIndex.ftl";
	}
	
	
	@RequestMapping("/health/result/list")
	public String allTestResultList(String studentId,ModelMap map){
		Map<String, List<DyStuHealthResult>> dshrMap = Maps.newHashMap();
		 //记录类型
        List<McodeDetail> semesterType = SUtils.dt(mcodeRemoteService.findByMcodeIds("DM-XQ"), McodeDetail.class);
        Map<String, String> semesterMap = EntityUtils.getMap(semesterType, "thisId", "mcodeContent");
		if(StringUtils.isNotBlank(studentId)){
			List<DyStuHealthResult> listDSHR = dyStuHealthResultService.findByUnitIdAndStuId(getLoginInfo().getUnitId(),studentId);
			if(CollectionUtils.isNotEmpty(listDSHR)){
				Student student =  SUtils.dc(studentRemoteService.findOneById(studentId), Student.class);
				for (DyStuHealthResult dyStuHealthResult : listDSHR) {
					dyStuHealthResult.setStudentName(student.getStudentName());
					dyStuHealthResult.setSchoolYearTeam(dyStuHealthResult.getAcadyear()+semesterMap.get(dyStuHealthResult.getSemester()));
				}
			}
			dshrMap = EntityUtils.getListMap(listDSHR, "schoolYearTeam", null);
		}
		map.put("dshrMap", dshrMap);
		return "/stuwork/health/allBodyTest/healthResultList.ftl";
	}
	
	@RequestMapping("/health/export")
	public void resultHealthExport(String studentId,ModelMap map){
		List<DyStuHealthResult> listDSHR = dyStuHealthResultService.findByUnitIdAndStuId(getLoginInfo().getUnitId(),studentId);
		if(CollectionUtils.isNotEmpty(listDSHR)){
			Student student =  SUtils.dc(studentRemoteService.findOneById(studentId), Student.class);
			for (DyStuHealthResult dyStuHealthResult : listDSHR) {
				dyStuHealthResult.setStudentName(student.getStudentName());
			}
		}
		doExportTea(listDSHR, getResponse());
	}
	
	private void doExportTea(List<DyStuHealthResult> listDSHR,HttpServletResponse response){
		List<McodeDetail> sexType = SUtils.dt(mcodeRemoteService.findByMcodeIds("DM-XB"), McodeDetail.class);
		Map<String, String> sexNameMap = EntityUtils.getMap(sexType, "thisId", "mcodeContent");
		Map<String, List<Map<String, String>>> sheetName2RecordListMap = new HashMap<String, List<Map<String, String>>>();
		List<Map<String,String>> recordList = new ArrayList<Map<String, String>>();
		for(DyStuHealthResult dshr : listDSHR){
			Map<String,String> sMap = new HashMap<String,String>();
			sMap.put("学生姓名", dshr.getStudentName());
			sMap.put("学生性别", sexNameMap.get(dshr.getSex()));
			sMap.put("学生班级", dshr.getClassName());
			sMap.put("学年", dshr.getAcadyear());
			sMap.put("学期", dshr.getSemester());
			sMap.put("体检项", dshr.getItemName());
			sMap.put("体检单位", dshr.getItemUnit());
			sMap.put("体检结果", dshr.getItemResult());
			recordList.add(sMap);
		}
		sheetName2RecordListMap.put("学生体检",recordList);
		Map<String, List<String>> titleMap = new HashMap<String, List<String>>();
		List<String> tis = getRowTitleList();
		titleMap.put("学生体检", tis);
		ExportUtils ex = ExportUtils.newInstance();
		ex.exportXLSFile( "学生体检信息", titleMap, sheetName2RecordListMap, response);	
	}
	
	private List<String> getRowTitleList() {
		List<String> tis = new ArrayList<String>();
		tis.add("学生姓名");
		tis.add("学生性别");
		tis.add("学生班级");
		tis.add("学年");
		tis.add("学期");
		tis.add("体检项");
		tis.add("体检单位");
		tis.add("体检结果");
		return tis;
	}
}
