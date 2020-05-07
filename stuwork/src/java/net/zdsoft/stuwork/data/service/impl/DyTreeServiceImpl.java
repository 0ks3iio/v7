package net.zdsoft.stuwork.data.service.impl;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.TypeReference;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import net.zdsoft.basedata.dto.TreeNodeDto;
import net.zdsoft.basedata.entity.Clazz;
import net.zdsoft.basedata.entity.Grade;
import net.zdsoft.basedata.entity.Student;
import net.zdsoft.basedata.entity.Unit;
import net.zdsoft.basedata.remote.service.ClassRemoteService;
import net.zdsoft.basedata.remote.service.GradeRemoteService;
import net.zdsoft.basedata.remote.service.StudentRemoteService;
import net.zdsoft.basedata.remote.service.UnitRemoteService;
import net.zdsoft.framework.entity.TR;
import net.zdsoft.framework.utils.EntityUtils;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.stuwork.data.service.DyPermissionService;
import net.zdsoft.stuwork.data.service.DyTreeService;

@Service("dyTreeService")
public class DyTreeServiceImpl implements DyTreeService{

	@Autowired
	private UnitRemoteService unitRemoteService;
	@Autowired
	private GradeRemoteService gradeRemoteService;
	@Autowired
	private ClassRemoteService classRemoteService;
	@Autowired
	private StudentRemoteService studentRemoteService;
	@Autowired
	private DyPermissionService dyPermissionService;
	
	
	@Override
	public JSONArray gradeClassForSchoolInsetZTree(String unitId,String userId) {
		JSONArray jsonArr = new JSONArray();
		if (StringUtils.isNotBlank(unitId) && StringUtils.isNotBlank(userId) ) {
			//权限班级
			Set<String> classPermission = dyPermissionService.findClassSetByUserId(userId);
			if (CollectionUtils.isEmpty(classPermission)) {
				return jsonArr;
			}
			List<Clazz> clazzs = SUtils.dt(classRemoteService.findListByIds(classPermission.toArray(new String[0])),new TR<List<Clazz>>() {});  
			Set<String> gradeIds = EntityUtils.getSet(clazzs, "gradeId");
			Map<String, List<Clazz>> classMap = Maps.newHashMap();
			List<Clazz> clazzs2 = null;
			for (Clazz clazz : clazzs) {
				if (classMap.containsKey(clazz.getGradeId())) {
					clazzs2 = classMap.get(clazz.getGradeId());
					clazzs2.add(clazz);
					classMap.put(clazz.getGradeId(),clazzs2);
				} else {
					clazzs2 = Lists.newArrayList();
					clazzs2.add(clazz);
					classMap.put(clazz.getGradeId(), clazzs2);
				}
			}
			List<Unit> unitList = SUtils.dt(unitRemoteService.findListByIds(unitId),new TypeReference<List<Unit>>() {});
			findUnitZTreeJson(null, unitList.size() == 1 ? true : false,jsonArr, unitList);
			List<Grade> gradeList = SUtils.dt(gradeRemoteService.findListByIds(gradeIds.toArray(new String[0])),new TypeReference<List<Grade>>() {});
			Collections.sort(gradeList, new Comparator<Grade>() {
				@Override
				public int compare(Grade arg0, Grade arg1) {
					return arg0.getGradeCode().compareTo(arg1.getGradeCode());
				}});
			findGradeZTreeJson(unitId, false, jsonArr, gradeList, true);
			for (Map.Entry<String, List<Clazz>> entry : classMap.entrySet()) {
				String key = entry.getKey();
				List<Clazz> value = entry.getValue();
				findClassZTreeJson(key, false, jsonArr, value, false);
			}
		}
		return jsonArr;
	}

	@Override
	public JSONArray gradeClassStudentForSchoolInsetZTree(String unitId,String userId) {
		JSONArray jsonArr = new JSONArray();
        if (StringUtils.isNotBlank(unitId) && StringUtils.isNotBlank(userId) ) {
        	//权限班级
			Set<String> classPermission = dyPermissionService.findClassSetByUserId(userId);
			if (CollectionUtils.isEmpty(classPermission)) {
				return jsonArr;
			}
			List<Clazz> clazzs = SUtils.dt(classRemoteService.findListByIds(classPermission.toArray(new String[0])),new TR<List<Clazz>>() {});  
			Set<String> gradeIds = EntityUtils.getSet(clazzs, "gradeId");
			Map<String, List<Clazz>> classMap = Maps.newHashMap();
			List<Clazz> clazzs2 = null;
			for (Clazz clazz : clazzs) {
				if (classMap.containsKey(clazz.getGradeId())) {
					clazzs2 = classMap.get(clazz.getGradeId());
					clazzs2.add(clazz);
					classMap.put(clazz.getGradeId(),clazzs2);
				} else {
					clazzs2 = Lists.newArrayList();
					clazzs2.add(clazz);
					classMap.put(clazz.getGradeId(), clazzs2);
				}
			}
			List<Unit> unitList = SUtils.dt(unitRemoteService.findListByIds(unitId),new TypeReference<List<Unit>>() {});
			findUnitZTreeJson(null, unitList.size() == 1 ? true : false,jsonArr, unitList);
			List<Grade> gradeList = SUtils.dt(gradeRemoteService.findListByIds(gradeIds.toArray(new String[0])),new TypeReference<List<Grade>>() {});
			Collections.sort(gradeList, new Comparator<Grade>() {
				@Override
				public int compare(Grade o1, Grade o2) {
					return o1.getGradeCode().compareTo(o2.getGradeCode());
				}
			});
			findGradeZTreeJson(unitId, false, jsonArr, gradeList, true);
			for (Map.Entry<String, List<Clazz>> entry : classMap.entrySet()) {
				String key = entry.getKey();
				List<Clazz> value = entry.getValue();
				findClassZTreeJson(key, false, jsonArr, value, false);
			}
            Map<String, List<Student>> studentMap = SUtils.dt(studentRemoteService.findMapByClassIdIn(classPermission.toArray(new String[0])), new TypeReference<Map<String, List<Student>>>(){});
            for (Map.Entry<String, List<Student>> entry : studentMap.entrySet()) {
                String key = entry.getKey();
                List<Student> value = entry.getValue();
                findStudentZTreeJson(key, false, jsonArr, value);
            }
        }
        return jsonArr;
	}
	
	
	@Override
	public void findUnitZTreeJson(String pId, boolean isOpen,JSONArray jsonArr, List<Unit> list) {
		if (StringUtils.isBlank(pId)) {
			pId = "";
		}
		TreeNodeDto treeNodeDto = null;
		for (Unit unit : list) {
			treeNodeDto = new TreeNodeDto();
			treeNodeDto.setpId(pId);
			treeNodeDto.setId(unit.getId());
			treeNodeDto.setName(unit.getUnitName());
			treeNodeDto.setTitle(unit.getUnitName());
			treeNodeDto.setOpen(isOpen);
			if (Unit.UNIT_CLASS_SCHOOL == unit.getUnitClass()) {
				treeNodeDto.setType("school");
			} else {
				treeNodeDto.setType("edu");
			}
			jsonArr.add(JSON.toJSON(treeNodeDto));
		}
	}
	 
	@Override
	public void findGradeZTreeJson(String pId, boolean isOpen,JSONArray jsonArr, List<Grade> list, boolean isParent) {
		if (StringUtils.isBlank(pId)) {
			pId = "";
		}
		TreeNodeDto treeNodeDto;
		for (Grade grade : list) {
			treeNodeDto = new TreeNodeDto();
			treeNodeDto.setpId(pId);
			treeNodeDto.setId(grade.getId());
			treeNodeDto.setName(grade.getGradeName());
			treeNodeDto.setTitle(grade.getGradeName());
			treeNodeDto.setOpen(isOpen);
			treeNodeDto.setType("grade");
			if (isParent) {
				treeNodeDto.setIsParent(isParent);
			}
			jsonArr.add(JSON.toJSON(treeNodeDto));
		}
	}
	
	@Override
    public void findClassZTreeJson(String pId, boolean isOpen, JSONArray jsonArr, List<Clazz> list, boolean isParent) {
        if (StringUtils.isBlank(pId)) {
            pId = "";
        }
        TreeNodeDto treeNodeDto;
        for (Clazz clazz : list) {
            treeNodeDto = new TreeNodeDto();
            treeNodeDto.setpId(pId);
            treeNodeDto.setId(clazz.getId());
            treeNodeDto.setName(clazz.getClassName());
            treeNodeDto.setTitle(clazz.getClassName());
            treeNodeDto.setOpen(isOpen);
            treeNodeDto.setType("class");
            if (isParent) {
                treeNodeDto.setIsParent(isParent);
            }
            jsonArr.add(JSON.toJSON(treeNodeDto));
        }
    }
	
	@Override
    public void findStudentZTreeJson(String pId, boolean isOpen, JSONArray jsonArr, List<Student> list) {
        if (StringUtils.isBlank(pId)) {
            pId = "";
        }
        TreeNodeDto treeNodeDto;
        for (Student student : list) {
            treeNodeDto = new TreeNodeDto();
            treeNodeDto.setpId(pId);
            treeNodeDto.setId(student.getId());
            treeNodeDto.setName(student.getStudentName());
            treeNodeDto.setTitle(student.getStudentName());
            treeNodeDto.setOpen(isOpen);
            treeNodeDto.setType("student");
            jsonArr.add(JSON.toJSON(treeNodeDto));
        }
    }
	
}
