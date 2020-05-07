package net.zdsoft.teaeaxam.action;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
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

import net.zdsoft.basedata.entity.School;
import net.zdsoft.basedata.entity.Teacher;
import net.zdsoft.basedata.remote.service.SchoolRemoteService;
import net.zdsoft.basedata.remote.service.TeacherRemoteService;
import net.zdsoft.framework.action.BaseAction;
import net.zdsoft.framework.entity.LoginInfo;
import net.zdsoft.framework.entity.TR;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.teaeaxam.constant.TeaexamConstant;
import net.zdsoft.teaeaxam.dto.TeaexamRegisterInfoDto;
import net.zdsoft.teaeaxam.entity.TeaexamInfo;
import net.zdsoft.teaeaxam.entity.TeaexamRegisterInfo;
import net.zdsoft.teaeaxam.entity.TeaexamSite;
import net.zdsoft.teaeaxam.entity.TeaexamSubject;
import net.zdsoft.teaeaxam.entity.TeaexamSubjectLimit;
import net.zdsoft.teaeaxam.entity.TeaexamSubjectLine;
import net.zdsoft.teaeaxam.service.TeaexamInfoService;
import net.zdsoft.teaeaxam.service.TeaexamRegisterInfoService;
import net.zdsoft.teaeaxam.service.TeaexamSiteService;
import net.zdsoft.teaeaxam.service.TeaexamSubjectLimitService;
import net.zdsoft.teaeaxam.service.TeaexamSubjectLineService;
import net.zdsoft.teaeaxam.service.TeaexamSubjectService;
@Controller
@RequestMapping("/teaexam/scoreInfo")
public class TeaexamScoreInfoAction extends BaseAction{
	@Autowired
	private TeaexamInfoService teaexamInfoService;
	@Autowired
	private SchoolRemoteService schoolRemoteService;
	@Autowired
	private TeaexamRegisterInfoService teaexamRegisterInfoService;
	@Autowired
	private TeaexamSiteService teaexamSiteService;
	@Autowired
	private TeaexamSubjectService teaexamSubjectService;
	@Autowired
	private TeacherRemoteService teacherRemoteService;
	@Autowired
	private TeaexamSubjectLineService teaexamSubjectLineService;
	@Autowired
	private TeaexamSubjectLimitService teaexamSubjectLimitService;
	
	@RequestMapping("/index/page")
	public String pageIndex(String examId,String roomNo, String subId, HttpServletRequest req, ModelMap map){
		LoginInfo info = getLoginInfo();
		String unitId = info.getUnitId();
		int year = NumberUtils.toInt(req.getParameter("year")); 
		int type = NumberUtils.toInt(req.getParameter("type"));;
		Calendar now = Calendar.getInstance();
		int nowy = now.get(Calendar.YEAR); 
		if(year == 0) {
			year = nowy;
		}
		map.put("maxYear", nowy+1);
		map.put("minYear", nowy-5);
		map.put("type", type);
		map.put("year", year);
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		String dateString = formatter.format(new Date());
		List<TeaexamInfo> examList = teaexamInfoService.findByEndTime(year, type, dateString,unitId);
		map.put("examList", examList);
		if(StringUtils.isBlank(examId) && CollectionUtils.isNotEmpty(examList)){
			examId = examList.get(0).getId();
		}
		if(CollectionUtils.isEmpty(examList)){
			examId = "";
		}
		scoreList(examId, roomNo, subId, map);
		return "/teaexam/scoreInfo/soreList.ftl";
	}
	
	
	public void scoreList(String examId, String roomNo, String subId, ModelMap map){
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
                return o1.compareToIgnoreCase(o2);
            }
        });
		if((StringUtils.isBlank(roomNo) || "undefined".equals(roomNo)) && CollectionUtils.isNotEmpty(roomNoList)){
			roomNo = roomNoList.get(0);
		}
		List<TeaexamSubject> subList = new ArrayList<TeaexamSubject>();
		Map<String, Float> maxMap = new HashMap<>();
		if(CollectionUtils.isNotEmpty(subIdSet)){
			List<TeaexamSubject> subListTemp = teaexamSubjectService.findListByIds(subIdSet.toArray(new String[0]));
			subIdSet.add(TeaexamConstant.ALL_LIMIT);
			List<TeaexamSubjectLimit> limitList = teaexamSubjectLimitService.findBySubjectIds(subIdSet.toArray(new String[0]));
			String teacherId = getLoginInfo().getOwnerId();
			TeaexamSubjectLimit li = teaexamSubjectLimitService.findByExamIdAndSubId(TeaexamConstant.ALL_LIMIT, TeaexamConstant.ALL_LIMIT);
			if(null!=li && StringUtils.isNotBlank(li.getTeacherIds()) && li.getTeacherIds().contains(teacherId)){
				subList = subListTemp;
			}else{
				for(TeaexamSubject sub : subListTemp){
					for(TeaexamSubjectLimit limit : limitList){
						if(sub.getId().equals(limit.getSubjectInfoId()) && StringUtils.isNotBlank(limit.getTeacherIds()) && limit.getTeacherIds().contains(teacherId)){
							subList.add(sub);
						}
					}
				}
			}
		}
		if(CollectionUtils.isNotEmpty(subList) && StringUtils.isBlank(subId)){
			subId = subList.get(0).getId();		
		}	
		List<TeaexamRegisterInfo> regList2 = new ArrayList<TeaexamRegisterInfo>();
		if(StringUtils.isNotBlank(roomNo) && StringUtils.isNotBlank(subId)){
			regList2 = teaexamRegisterInfoService.findByStatusAndRoomNo("2", roomNo, subId, examId);
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
			subNameMap.put(sub.getId(), sub.getSubjectName()+"("+subctionName+")");
			if(sub.getFullScore() != null) {
				maxMap.put(sub.getId(), sub.getFullScore());
			}
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
        map.put("maxMap", maxMap);
        map.put("lineState", -1);
        if(StringUtils.isNotBlank(subId)){
        	List<TeaexamSubjectLine> lineList = teaexamSubjectLineService.findBySubjectId(subId);
        	if(CollectionUtils.isNotEmpty(lineList)){
        		map.put("lineState", 1);
        	}else{
        		map.put("lineState", 0);
        	}
        }
		map.put("examId", examId);
		map.put("subList", subList);
		map.put("roomNoList", roomNoList);
		map.put("subId", subId);
		map.put("roomNoLocationIdMap", roomNoLocationIdMap);
		map.put("regList2", regList2);
		map.put("roomNo", roomNo);
	}
	
	@ResponseBody
	@RequestMapping("/saveScore")
	public String saveScore(TeaexamRegisterInfoDto regDto, String subjectId){		
		try{
			List<TeaexamSubjectLimit> limitList = teaexamSubjectLimitService.findBySubjectIds(new String[]{TeaexamConstant.ALL_LIMIT,subjectId});
			Set<String> teacherIdSet = new HashSet<String>();
			for(TeaexamSubjectLimit limit : limitList){
				String teacherIds = limit.getTeacherIds();
				if(StringUtils.isNotBlank(teacherIds)){
					String[] arr = teacherIds.split(",");
					for(String teacherId : arr){
						teacherIdSet.add(teacherId);
					}
				}
			}
			if(!teacherIdSet.contains(getLoginInfo().getOwnerId())){
				return error("没有该科目的录分权限！");
			}
			List<TeaexamRegisterInfo> regListTemp = regDto.getRegList();
			if(CollectionUtils.isEmpty(regListTemp)){
				return error("没有需要保存的数据！");
			}
            Set<String> regIdSet = new HashSet<String>();
            Map<String, Float> regMap = new HashMap<String, Float>();
			for(TeaexamRegisterInfo reg : regListTemp){
				regIdSet.add(reg.getId());
				regMap.put(reg.getId(), reg.getScore());
            }
			if(CollectionUtils.isNotEmpty(regIdSet)){
				List<TeaexamRegisterInfo> regList = teaexamRegisterInfoService.findListByIds(regIdSet.toArray(new String[0]));
				for(TeaexamRegisterInfo reg : regList){
					reg.setScore(regMap.get(reg.getId()));
				}
				teaexamRegisterInfoService.saveAll(regList.toArray(new TeaexamRegisterInfo[0]));
			}
		}catch(Exception e){
			e.printStackTrace();
			return error("操作失败！"+e.getMessage());
		}
		return returnSuccess();	
	}
	
	@ResponseBody
	@RequestMapping("/getLimit")
	public String getLimit(String examId, String subjectId){
		try{
			List<TeaexamSubjectLimit> limitList = teaexamSubjectLimitService.findBySubjectIds(new String[]{TeaexamConstant.ALL_LIMIT,subjectId});
			Set<String> teacherIdSet = new HashSet<String>();
			for(TeaexamSubjectLimit limit : limitList){
				String teacherIds = limit.getTeacherIds();
				if(StringUtils.isNotBlank(teacherIds)){
					String[] arr = teacherIds.split(",");
					for(String teacherId : arr){
						teacherIdSet.add(teacherId);
					}
				}
			}
			if(!teacherIdSet.contains(getLoginInfo().getOwnerId())){
				return error("没有该科目的录分权限！");
			}
		}catch(Exception e){
			e.printStackTrace();
			return error("操作失败！"+e.getMessage());
		}
		return returnSuccess();	
	}
}
