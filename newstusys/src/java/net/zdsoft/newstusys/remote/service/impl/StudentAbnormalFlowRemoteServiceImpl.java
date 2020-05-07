package net.zdsoft.newstusys.remote.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import net.zdsoft.basedata.entity.Clazz;
import net.zdsoft.basedata.remote.service.ClassRemoteService;
import net.zdsoft.basedata.remote.service.StudentRemoteService;
import net.zdsoft.framework.entity.TR;
import net.zdsoft.framework.utils.EntityUtils;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.newstusys.constants.BaseStudentConstants;
import net.zdsoft.newstusys.dto.FlowStudentDto;
import net.zdsoft.newstusys.entity.StudentAbnormalFlow;
import net.zdsoft.newstusys.remote.service.StudentAbnormalFlowRemoteService;
import net.zdsoft.newstusys.service.StudentAbnormalFlowService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
@Service("studentAbnormalFlowRemoteService")
public class StudentAbnormalFlowRemoteServiceImpl implements StudentAbnormalFlowRemoteService{
	@Autowired
	private StudentAbnormalFlowService studentAbnormalFlowService;
	@Autowired
	private StudentRemoteService studentRemoteService;
	@Autowired
	private ClassRemoteService classRemoteService;
	@Override
	public String findAbnormalFlowStudent(String schoolId, String acadyear,
			String semester, String[] flowTypes) {
		List<StudentAbnormalFlow> flowList = studentAbnormalFlowService.findAbnormalFlowStudent(schoolId, acadyear, semester, flowTypes);
		Set<String> stuidSet = new HashSet<String>();
		Set<String> classIdSet = new HashSet<String>();
		Set<String> resultSet = new HashSet<String>();
		for(StudentAbnormalFlow flow : flowList){
			if(!stuidSet.contains(flow.getStuid())){			
				stuidSet.add(flow.getStuid());
				resultSet.add(flow.getStuid()+","+flow.getCurrentclassid()+","+flow.getFlowsource());
			}
			classIdSet.add(flow.getCurrentclassid());
			classIdSet.add(flow.getFlowsource());
		}
		Map<String, String> clsMap = new HashMap<String, String>();
		if(CollectionUtils.isNotEmpty(classIdSet)){
			List<Clazz> clsList = SUtils.dt(classRemoteService.findListByIds(classIdSet.toArray(new String[0])),new TR<List<Clazz>>(){});
			for(Clazz cls : clsList){
				clsMap.put(cls.getId(), cls.getGradeId());
			}
		}
		List<FlowStudentDto> dtoList = new ArrayList<FlowStudentDto>();
		for(String result : resultSet){
			FlowStudentDto dto = new FlowStudentDto();
			dto.setStudentId(result.split(",")[0]);
			dto.setCurrentGradeId(clsMap.get(result.split(",")[1]));
			dto.setSourceGradeId(clsMap.get(result.split(",")[2]));
			dtoList.add(dto);
		}
		return SUtils.s(dtoList);
	}

	public JSONArray findRecoverStuBySemester(String schoolId, String acadyear, String semester, Integer section){
		JSONArray ar = new JSONArray();
		//TODO
		List<StudentAbnormalFlow> fxList = studentAbnormalFlowService.findAbnormalFlowStudentSec(schoolId, acadyear, semester, section!=null? (section.intValue()+""):null, new String[]{BaseStudentConstants.YD_FX});
		if(CollectionUtils.isEmpty(fxList)){
			return ar;
		}
		Set<String> stuIds = new HashSet<>();
		Iterator<StudentAbnormalFlow> fit = fxList.iterator();
		List<String> clsIds = new ArrayList<>();
		while(fit.hasNext()){
			StudentAbnormalFlow fl = fit.next();
			if(!stuIds.contains(fl.getStuid())){
				if(!clsIds.contains(fl.getFlowto())){// 复学后班级
					clsIds.add(fl.getFlowto());
				}
				stuIds.add(fl.getStuid());
				continue;
			}
			fit.remove();
		}
		// 根据复学学生找休学记录
		List<StudentAbnormalFlow> xxList = studentAbnormalFlowService.findFlowByStuidsType(schoolId, stuIds.toArray(new String[0]), new String[]{BaseStudentConstants.YD_XX});
		if(CollectionUtils.isEmpty(xxList)){
			return ar;
		}
		stuIds.clear();
		fit = xxList.iterator();
		while(fit.hasNext()){
			StudentAbnormalFlow fl = fit.next();
			if(!stuIds.contains(fl.getStuid())){
				if(!clsIds.contains(fl.getFlowsource())){// 休学前班级
					clsIds.add(fl.getFlowsource());
				}
				stuIds.add(fl.getStuid());
				continue;
			}
			fit.remove();
		}
		Map<String, StudentAbnormalFlow> fxMap = EntityUtils.getMap(fxList, StudentAbnormalFlow::getStuid);
		Map<String, String> clsMap = new HashMap<String, String>();
		if(CollectionUtils.isNotEmpty(clsIds)){
			List<Clazz> clsList = SUtils.dt(classRemoteService.findListByIds(clsIds.toArray(new String[0])),new TR<List<Clazz>>(){});
			for(Clazz cls : clsList){
				clsMap.put(cls.getId(), cls.getAcadyear());
			}
		}
		//stuId学生id，oldAcadyear休学学年，oldSemester休学学期，oldClsId原班级id，nowClsId复学后班级id
		for (StudentAbnormalFlow xx : xxList) {
			StudentAbnormalFlow fx = fxMap.get(xx.getStuid());
			String oldYear = clsMap.get(xx.getFlowsource());
			String newYear = clsMap.get(fx.getFlowto());
			if(StringUtils.isEmpty(oldYear) || StringUtils.isEmpty(newYear)
				|| StringUtils.equals(oldYear, newYear)){
				continue;// 过滤：原班级或新班级不存在，或复学后还在同一年级
			}
			JSONObject jo = new JSONObject();
			int year = NumberUtils.toInt(newYear.split("-")[0])-NumberUtils.toInt(oldYear.split("-")[0]);
			jo.put("years", year);
			jo.put("stuId", fx.getStuid());
			jo.put("oldAcadyear", xx.getAcadyear());
			jo.put("oldSemester", xx.getSemester());
			jo.put("oldClsId", xx.getFlowsource());
			jo.put("nowClsId", fx.getFlowto());
			ar.add(jo);
		}
		return ar;
	}

}
