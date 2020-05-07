package net.zdsoft.teaeaxam.action;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import net.zdsoft.basedata.entity.Unit;
import net.zdsoft.basedata.remote.service.UnitRemoteService;
import net.zdsoft.framework.action.BaseAction;
import net.zdsoft.framework.annotation.ControllerInfo;
import net.zdsoft.framework.entity.TR;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.framework.utils.UuidUtils;
import net.zdsoft.teaeaxam.constant.TeaexamConstant;
import net.zdsoft.teaeaxam.dto.TeaexamSubjectDto;
import net.zdsoft.teaeaxam.entity.TeaexamInfo;
import net.zdsoft.teaeaxam.entity.TeaexamSubject;
import net.zdsoft.teaeaxam.service.TeaexamInfoService;
import net.zdsoft.teaeaxam.service.TeaexamSubjectService;

@Controller
@RequestMapping("/teaexam")
public class TeaexamInfoAction extends BaseAction{
	@Autowired
	private TeaexamInfoService teaexamInfoService;
	@Autowired
	private TeaexamSubjectService teaexamSubjectService;
	@Autowired
	private UnitRemoteService unitRemoteService;
	
	@RequestMapping("/examInfo/index/page")
	public String pageIndex(HttpServletRequest req, ModelMap map){
		int year = NumberUtils.toInt(req.getParameter("year")); 
		int type = NumberUtils.toInt(req.getParameter("type"));;
		Calendar now = Calendar.getInstance();
		int nowy = now.get(Calendar.YEAR); 
		if(year == 0) {
			year = nowy;
		}
		map.put("maxYear", nowy+1);
		map.put("minYear", nowy-5);
		map.put("year", year);
		map.put("type", type);
		return "/teaexam/examInfo/examInfoHead.ftl";
	}
	
	@RequestMapping("/examInfo/examInfoList")
	public String examList(int year, int type, ModelMap map){		
		List<TeaexamInfo> teaexamInfoList = teaexamInfoService.findByInfoYearType(getLoginInfo().getUnitId(), year, type);
		Set<String> examIdSet = new HashSet<String>();
		for(TeaexamInfo exam : teaexamInfoList){
			exam.setSubNames(exam.getTrainItems());
			examIdSet.add(exam.getId());
		}
		if (TeaexamConstant.EXAM_INFOTYPE_0 == type) {
			List<TeaexamSubject> subList = new ArrayList<TeaexamSubject>();
			if (CollectionUtils.isNotEmpty(examIdSet)) {
				subList = teaexamSubjectService.findByExamIds(examIdSet.toArray(new String[0]));
			}
			for (TeaexamInfo exam : teaexamInfoList) {
				exam.setSubNames("");
				String subNames = "";
				for (TeaexamSubject sub : subList) {
					if (exam.getId().equals(sub.getExamId())) {
						String sectionName = "";
						if (1 == sub.getSection()) {
							sectionName = "小学";
						} else if (0 == sub.getSection()) {
							sectionName = "学前";
						} else if (2 == sub.getSection()) {
							sectionName = "初中";
						} else if (3 == sub.getSection()) {
							sectionName = "高中";
						}
						subNames = subNames + sub.getSubjectName() + "(" + sectionName + ")" + ",";
					}
				}
				if (StringUtils.isNotBlank(subNames)) {
					subNames = subNames.substring(0, subNames.length() - 1);
				}
				exam.setSubNames(subNames);
			} 
		}
		map.put("type", type);
		map.put("teaexamInfoList", teaexamInfoList);
		return "/teaexam/examInfo/examInfoList.ftl";
	}
	
	@RequestMapping("/examInfo/examInfoEdit")
	public String examInfoEdit(int year, int type, String examId, int state, ModelMap map){	
		TeaexamInfo teaexamInfo;
		List<TeaexamSubject> subjectList;
		List<String> schIdList = new ArrayList<String>();
		 Map<String, String> schNameMap = new HashMap<String, String>();
		if(StringUtils.isNotBlank(examId)){
			teaexamInfo = teaexamInfoService.findOne(examId);
			subjectList = teaexamSubjectService.findByExamIds(new String[]{examId});
			map.put("teaexamInfo", teaexamInfo);
			map.put("subjectList", subjectList);
			String schIds = teaexamInfo.getSchoolIds();
			String[] schIdArr;
			if(StringUtils.isNotBlank(schIds)){
				schIdArr = schIds.split(",");
				for(String schId : schIdArr){
					schIdList.add(schId);
				}
				List<Unit> schList = SUtils.dt(unitRemoteService.findListByIds(schIdArr), new TR<List<Unit>>(){});
				for(Unit sch : schList){
					schNameMap.put(sch.getId(), sch.getUnitName());
				}
			}
		}else{
			teaexamInfo = new TeaexamInfo();
			teaexamInfo.setInfoYear(year);
			teaexamInfo.setInfoType(type);
			map.put("teaexamInfo", teaexamInfo);
		}
		map.put("schIdList", schIdList);
		map.put("type", type);
		map.put("year", year);
		map.put("unitId", getLoginInfo().getUnitId());
		map.put("schNameMap", schNameMap);
		if(state == 1){
			return "/teaexam/examInfo/examInfoEidt.ftl";
		}else{
			return "/teaexam/examInfo/examInfoDetail.ftl";
		}
	}
	
	@ResponseBody
	@RequestMapping("/examInfo/examInfoSave")
    @ControllerInfo(value = "")
	public String examInfoSave(TeaexamInfo teaexamInfo, TeaexamSubjectDto teaexamSubjectDto, int state, ModelMap map){
		try{
			List<TeaexamInfo> teaexamInfoList = teaexamInfoService.findByInfoYearType(getLoginInfo().getUnitId(), teaexamInfo.getInfoYear(), teaexamInfo.getInfoType());
            for(TeaexamInfo item : teaexamInfoList){
            	if(StringUtils.isNotBlank(teaexamInfo.getId()) ){
            		if(!teaexamInfo.getId().equals(item.getId())){
                		if((teaexamInfo.getRegisterBegin().getTime()>item.getRegisterBegin().getTime() && teaexamInfo.getRegisterBegin().getTime()<item.getRegisterEnd().getTime())
        						|| teaexamInfo.getRegisterEnd().getTime()>item.getRegisterBegin().getTime() && teaexamInfo.getRegisterEnd().getTime()<item.getRegisterEnd().getTime()
        						|| teaexamInfo.getRegisterBegin().getTime()<item.getRegisterBegin().getTime() && teaexamInfo.getRegisterEnd().getTime()>item.getRegisterEnd().getTime()){
        					return error("该时间段已有记录，请重新输入报名时间！");
        				}
                	}
            	}else{
            		if((teaexamInfo.getRegisterBegin().getTime()>item.getRegisterBegin().getTime() && teaexamInfo.getRegisterBegin().getTime()<item.getRegisterEnd().getTime())
    						|| teaexamInfo.getRegisterEnd().getTime()>item.getRegisterBegin().getTime() && teaexamInfo.getRegisterEnd().getTime()<item.getRegisterEnd().getTime()
    						|| teaexamInfo.getRegisterBegin().getTime()<item.getRegisterBegin().getTime() && teaexamInfo.getRegisterEnd().getTime()>item.getRegisterEnd().getTime()){
    					return error("该时间段已有记录，请重新输入报名时间！");
    				}
            	}
            }  
            
			String schoolIds = "";
			List<String> schIdList = teaexamSubjectDto.getSchIdList();
			List<Unit> schList = new ArrayList<Unit>();
			if (null != schIdList && CollectionUtils.isNotEmpty(schIdList)) {
				schList = SUtils.dt(unitRemoteService.findListByIds(schIdList.toArray(new String[0])),
						new TR<List<Unit>>() {
						});
			}
			if (CollectionUtils.isNotEmpty(schList)) {
				for (Unit sch : schList) {
					schoolIds = schoolIds + sch.getId() + ",";
				}
			} else {
				return error("单位不能为空，请选择单位！");
			}
			teaexamInfo.setSchoolIds(schoolIds);
			String examId = "";
			boolean isNew = StringUtils.isBlank(teaexamInfo.getId());
			if (isNew) {
				examId = UuidUtils.generateUuid();
				teaexamInfo.setId(examId);
				teaexamInfo.setCreationTime(new Date());
				teaexamInfo.setModifyTime(new Date());
			} else {
				examId = teaexamInfo.getId();
				teaexamInfo.setModifyTime(new Date());
			}
			List<TeaexamSubject> teaexamSubjectList1 = teaexamSubjectDto.getTeaexamSubjectList();
            if (TeaexamConstant.EXAM_INFOTYPE_0 == teaexamInfo.getInfoType()) {
				if (CollectionUtils.isEmpty(teaexamSubjectList1)) {
					return error("考试科目不能为空，请维护考试科目！");
				} else {
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
					for (TeaexamSubject sub : teaexamSubjectList1) {
						Date startTime = sub.getStartTime();
						Date endTime = sub.getEndTime();
						if (null != startTime && null != endTime) {
							String startTimeStr = sdf.format(startTime);
							String endTimeStr = sdf.format(endTime);
							if (!startTimeStr.equals(endTimeStr)) {
								return error(sub.getSubjectName() + "的考试时间不在同一天，请重新选择考试时间！");
							}
						}
					}

				}	
				
				List<TeaexamSubject> teaexamSubjectList2 = new ArrayList<TeaexamSubject>();
				for (TeaexamSubject sub : teaexamSubjectList1) {
					sub.setExamId(examId);
					sub.setModifyTime(new Date());
					if (StringUtils.isBlank(sub.getId())) {
						sub.setId(UuidUtils.generateUuid());
						sub.setCreationTime(new Date());
					}
					teaexamSubjectList2.add(sub);
				}
				int i = 0;
				for (TeaexamSubject item1 : teaexamSubjectList1) {
					int t = 0;
					for (TeaexamSubject item2 : teaexamSubjectList2) {
						if (item1.getSubjectName().equals(item2.getSubjectName())
								&& item1.getSection() == item2.getSection() && i != t) {
							return error("科目:" + item1.getSubjectName() + "重名！");
						}
//						if (!(item1.getStartTime().getTime() == item2.getStartTime().getTime()
//								&& item1.getEndTime().getTime() == item2.getEndTime().getTime())
//								&& i != t
//								&& ((item1.getStartTime().getTime() > item2.getStartTime().getTime()
//										&& item1.getStartTime().getTime() < item2.getEndTime().getTime())
//										|| (item1.getEndTime().getTime() > item2.getStartTime().getTime()
//												&& item1.getEndTime().getTime() < item2.getEndTime().getTime())
//										|| (item1.getStartTime().getTime() < item2.getStartTime().getTime()
//												&& item1.getEndTime().getTime() > item2.getEndTime().getTime()))) {
//							return error(
//									item1.getSubjectName() + "和" + item2.getSubjectName() + "考试时间交叉，请重新输入科目考试时间！");
//						}
						t++;
					}
					if (item1.getStartTime().getTime() > item1.getEndTime().getTime()) {
						return error("科目:" + item1.getSubjectName() + "考试开始时间不能大于考试结束时间");
					}
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
					String startTimeStr = sdf.format(item1.getStartTime());
					String endTimeStr = sdf.format(item1.getEndTime());
					SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd");
					Date startTime = sdf2.parse(startTimeStr);
					Date endTime = sdf2.parse(endTimeStr);
					if (startTime.getTime() < teaexamInfo.getExamStart().getTime()) {
						return error("科目:" + item1.getSubjectName() + "考试开始时间不能小于考试时间范围的开始时间");
					}
					if (endTime.getTime() > teaexamInfo.getExamEnd().getTime()) {
						return error("科目:" + item1.getSubjectName() + "考试结束时间不能大于考试时间范围的结束时间");
					}
					i++;
				}
			}
			teaexamInfo.setState(state);
            teaexamInfoService.saveTeaexamInfo(teaexamInfo, teaexamSubjectList1);
		}catch(Exception e){
			e.printStackTrace();
			return error("操作失败！"+e.getMessage());
		}
		return returnSuccess();		
	}
	
	@ResponseBody
	@RequestMapping("/examInfo/examInfoDelete")
    @ControllerInfo(value = "")
	public String examInfoDelete(String id){
		try{
			teaexamInfoService.deleteTeaexamInfo(id);
		}catch(Exception e){
			e.printStackTrace();
			return error("操作失败！"+e.getMessage());
		}
		return returnSuccess();	
	}
	
}
