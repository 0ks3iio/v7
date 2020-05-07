package net.zdsoft.teaeaxam.action;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.zdsoft.basedata.entity.School;
import net.zdsoft.basedata.entity.Teacher;
import net.zdsoft.basedata.remote.service.SchoolRemoteService;
import net.zdsoft.basedata.remote.service.TeacherRemoteService;
import net.zdsoft.framework.annotation.ControllerInfo;
import net.zdsoft.framework.dataimport.DataImportAction;
import net.zdsoft.framework.entity.Json;
import net.zdsoft.framework.entity.TR;
import net.zdsoft.framework.utils.ExcelUtils;
import net.zdsoft.framework.utils.ExportUtils;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.framework.utils.UuidUtils;
import net.zdsoft.teaeaxam.entity.TeaexamInfo;
import net.zdsoft.teaeaxam.entity.TeaexamRegisterInfo;
import net.zdsoft.teaeaxam.entity.TeaexamSite;
import net.zdsoft.teaeaxam.entity.TeaexamSubject;
import net.zdsoft.teaeaxam.service.TeaexamInfoService;
import net.zdsoft.teaeaxam.service.TeaexamRegisterInfoService;
import net.zdsoft.teaeaxam.service.TeaexamSiteService;
import net.zdsoft.teaeaxam.service.TeaexamSubjectService;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
@Controller
@RequestMapping("/teaexam/scoreInfo/scoreInfoImport")
public class TeaexamScoreInfoImportAction extends DataImportAction{
	private Logger logger = Logger.getLogger(TeaexamScoreInfoImportAction.class);
	@Autowired
	private TeaexamInfoService teaexamInfoService;
	@Autowired
	private TeaexamSubjectService teaexamSubjectService;
	@Autowired
	private TeacherRemoteService teacherRemoteService;
	@Autowired
	private TeaexamRegisterInfoService teaexamRegisterInfoService;
	@Autowired
	private TeaexamSiteService teaexamSiteService;
	@Autowired
	private SchoolRemoteService schoolRemoteService;
	@ControllerInfo("进入导入首页")
	@RequestMapping("/main")
	public String execute(ModelMap map, String examId, String subjectId, HttpServletRequest req) {
		// 业务名称
		map.put("businessName", "成绩导入");

		// 导入URL 
		map.put("businessUrl", "/teaexam/scoreInfo/scoreInfoImport/import");
		// 导入模板
		map.put("templateDownloadUrl", "/teaexam/scoreInfo/scoreInfoImport/template?examId="+examId+"&subjectId="+subjectId);
		// 导入对象
		map.put("objectName", getObjectName());
		// 导入说明
		map.put("description", getDescription());
		
		map.put("businessKey", UuidUtils.generateUuid());
		TeaexamInfo examInfo = teaexamInfoService.findOne(examId);
		TeaexamSubject subject = teaexamSubjectService.findOne(subjectId);
		int year = NumberUtils.toInt(req.getParameter("year")); 
		int type = NumberUtils.toInt(req.getParameter("type"));;
		Calendar now = Calendar.getInstance();
		int nowy = now.get(Calendar.YEAR); 
		if(year == 0) {
			year = nowy;
		}
		map.put("type", type);
		map.put("year", year);
		map.put("examId", examId);
		map.put("subjectId", subjectId);
		map.put("examName", examInfo.getExamName());
		int section = subject.getSection();
		String sectionName = "";
		if(section == 1){
			sectionName = "小学";
		} else if (0 == section) {
			sectionName = "学前";
		}else if(section == 2){
			sectionName = "初中";
		}else if(section == 3){
			sectionName = "高中";
		}
		map.put("subjectName", subject.getSubjectName()+"("+sectionName+")");
		return "/teaexam/scoreInfo/scoreInfoImport.ftl";
	}
	
	@Override
	public String getObjectName() {
		return "scoreInfoImport";
	}

	@Override
	public String getDescription() {
		String str =  "<h4 class='box-graybg-title'>说明</h4>"
				+ "<p>1、导入文件中请确认数据是否正确</p>"
		        + "<p>2、需要有该科目的导入权限</p>"
		        + "<p>3、已报名的教师如果未安排考场，则不能导入成绩";
		return str;
	}

	@Override
	public List<String> getRowTitleList() {
		List<String> tis = new ArrayList<String>();
		tis.add("考生姓名");
		tis.add("准考证号");
		tis.add("所在单位");
		tis.add("考场编号");
		tis.add("座位号");
		tis.add("考试科目");
		tis.add("考试成绩");
		return tis;
	}

	@Override
	@RequestMapping("/import")
	@ResponseBody
	public String dataImport(String filePath, String params) {
		//logger.info("学生奖励业务数据处理中......");
		JSONObject json = JSONObject.parseObject(params);
		String examId=json.get("examId").toString();
		String subjectId=json.get("subjectId").toString();
		List<String[]> datas = ExcelUtils.readExcel(filePath,
				getRowTitleList().size());
		if(datas.size() == 0){
			Json importResultJson=new Json();
			importResultJson.put("totalCount", 0);
			importResultJson.put("successCount", 0);
			importResultJson.put("errorCount", 0);
			return importResultJson.toJSONString();
		}
		String jsonMsg = teaexamRegisterInfoService.doImport(datas, subjectId, examId);
		logger.info("导入结束......");

		
		return jsonMsg;
	}

	@Override
	@RequestMapping("/template")
	public void downloadTemplate(HttpServletRequest request,
			HttpServletResponse response) {
		String examId=request.getParameter("examId");
		String subjectId=request.getParameter("subjectId");
		List<TeaexamRegisterInfo> regList = scoreList(examId, subjectId);
		List<String> tis = getRowTitleList();
		Map<String, List<Map<String, String>>> sheetName2RecordListMap = new HashMap<String, List<Map<String, String>>>();
		List<Map<String,String>> recordList = new ArrayList<Map<String, String>>();
		for(TeaexamRegisterInfo reg : regList){
			Map<String,String> map = new HashMap<String, String>();
			map.put("考生姓名",reg.getTeacherName());
			map.put("准考证号",reg.getCardNo());
			map.put("所在单位",reg.getSchName());
			map.put("考场编号",reg.getRoomNo());
			map.put("座位号",reg.getSeatNo());
			map.put("考试科目",reg.getSubName());
			recordList.add(map);
		}
		sheetName2RecordListMap.put(getObjectName(),recordList);
		Map<String, List<String>> titleMap = new HashMap<String, List<String>>();
		titleMap.put(getObjectName(), tis);
		ExportUtils ex = ExportUtils.newInstance();
		ex.exportXLSFile("成绩导入", titleMap, sheetName2RecordListMap, response);
	
	}
	
	
	public List<TeaexamRegisterInfo> scoreList(String examId, String subId){
		List<TeaexamRegisterInfo> regList = teaexamRegisterInfoService.findByStatusAndExamIdIn(2,new String[]{examId});//已审核通过的
		Set<String> subIdSet = new HashSet<String>();
		Set<String> roomNoSet = new HashSet<String>();
		Map<String, String> roomNoLocationIdMap = new HashMap<String, String>(); 
		Set<String> locationIdSet = new HashSet<String>();
		Map<String, String> locationIdSchIdMap = new HashMap<String, String>();
		Set<String> schIdSet = new HashSet<String>();
		for(TeaexamRegisterInfo reg : regList){
			locationIdSet.add(reg.getLocationId());
			schIdSet.add(reg.getLocationId());
		}
		List<TeaexamSite> siteList = new ArrayList<TeaexamSite>();
		if(CollectionUtils.isNotEmpty(locationIdSet)){
			siteList = teaexamSiteService.findListByIds(locationIdSet.toArray(new String[0]));
		}
		for(TeaexamSite site : siteList){
			schIdSet.add(site.getSchoolId());
			locationIdSchIdMap.put(site.getId(), site.getSchoolId());
		}
		List<School> schList = new ArrayList<School>();
		if(CollectionUtils.isNotEmpty(schIdSet)){
			schList = SUtils.dt(schoolRemoteService.findListByIds(schIdSet.toArray(new String[0])), new TR<List<School>>() {});
		}
		Map<String, String> schNameMap = new HashMap<String, String>();
		for(School sch : schList){
			schNameMap.put(sch.getId(), sch.getSchoolName());
		}
		for(TeaexamRegisterInfo reg : regList){
			subIdSet.add(reg.getSubjectInfoId());
			if (StringUtils.isNotEmpty(reg.getRoomNo())) {
				roomNoSet.add(reg.getRoomNo());
				roomNoLocationIdMap.put(reg.getRoomNo(), schNameMap.get(reg.getLocationId()));
			}
		}
		List<String> roomNoList = new ArrayList<String>();
		roomNoList.addAll(roomNoSet);
		Collections.sort(roomNoList, new Comparator<String>() {
            public int compare(String o1, String o2) {
                return o2.compareToIgnoreCase(o2);
            }
        });
		List<TeaexamSubject> subList = new ArrayList<TeaexamSubject>();
		if(CollectionUtils.isNotEmpty(subIdSet)){
			subList = teaexamSubjectService.findListByIds(subIdSet.toArray(new String[0]));			
		}
		/*if(CollectionUtils.isNotEmpty(subList)){
			subId = subList.get(0).getId();		
		}	*/
		List<TeaexamRegisterInfo> regList2 = new ArrayList<TeaexamRegisterInfo>();
		List<TeaexamRegisterInfo> regListTemp = teaexamRegisterInfoService.findByStatusAndRoomNo("2", null, subId, examId);
		for(TeaexamRegisterInfo reg : regListTemp){
			if(StringUtils.isNotBlank(reg.getRoomNo())){
				regList2.add(reg);
			}
		}

		Set<String> teaIdSet = new HashSet<String>();
		Set<String> schIdSet2 = new HashSet<String>();
		for(TeaexamRegisterInfo reg : regList2){
			teaIdSet.add(reg.getTeacherId());
			schIdSet2.add(reg.getSchoolId());
		}
		List<Teacher> teacherList = new ArrayList<Teacher>();
		if(CollectionUtils.isNotEmpty(teaIdSet)){
			teacherList = SUtils.dt(teacherRemoteService.findListByIds(teaIdSet.toArray(new String[0])), new TR<List<Teacher>>() {}); 
		}
        Map<String, String> teacherNameMap = new HashMap<String, String>();
		for(Teacher teacher : teacherList){
			teacherNameMap.put(teacher.getId(), teacher.getTeacherName());
        }
		Map<String, String> subNameMap = new HashMap<String, String>();
        for(TeaexamSubject sub : subList){
        	String subctionName = "";
			if(sub.getSection()==1){
				subctionName = "小学";
			} else if (0 == sub.getSection()) {
				subctionName = "学前";
			}else if(sub.getSection()==2){
				subctionName = "初中";
			}else if(sub.getSection()==3){
				subctionName = "高中";
			}
			subNameMap.put(sub.getId(), sub.getSubjectName()+"（"+subctionName+"）");
        }
        List<School> schList2 = new ArrayList<School>();
        if(CollectionUtils.isNotEmpty(schIdSet2)){
        	schList2 = SUtils.dt(schoolRemoteService.findListByIds(schIdSet2.toArray(new String[0])), new TR<List<School>>() {});
		}
        Map<String, String> schNameMap2 = new HashMap<String, String>();
        for(School sch : schList2){
        	schNameMap2.put(sch.getId(), sch.getSchoolName());
        }
        for(TeaexamRegisterInfo reg : regList2){
        	reg.setTeacherName(teacherNameMap.get(reg.getTeacherId()));
        	reg.setSubName(subNameMap.get(reg.getSubjectInfoId()));
        	reg.setSchName(schNameMap2.get(reg.getSchoolId()));
		}  
        return regList2;
	}

}
