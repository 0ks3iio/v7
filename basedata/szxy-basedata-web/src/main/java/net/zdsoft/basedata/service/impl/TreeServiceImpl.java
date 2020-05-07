package net.zdsoft.basedata.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import net.zdsoft.basedata.dto.TeachGroupDto;
import net.zdsoft.basedata.dto.TeacherDto;
import net.zdsoft.basedata.dto.TreeNodeDto;
import net.zdsoft.basedata.entity.Clazz;
import net.zdsoft.basedata.entity.Dept;
import net.zdsoft.basedata.entity.Grade;
import net.zdsoft.basedata.entity.Student;
import net.zdsoft.basedata.entity.TeachBuilding;
import net.zdsoft.basedata.entity.TeachPlace;
import net.zdsoft.basedata.entity.Teacher;
import net.zdsoft.basedata.entity.Unit;
import net.zdsoft.basedata.entity.User;
import net.zdsoft.basedata.remote.service.ClassRemoteService;
import net.zdsoft.basedata.remote.service.DeptRemoteService;
import net.zdsoft.basedata.remote.service.GradeRemoteService;
import net.zdsoft.basedata.remote.service.StudentRemoteService;
import net.zdsoft.basedata.remote.service.TeachBuildingRemoteService;
import net.zdsoft.basedata.remote.service.TeachPlaceRemoteService;
import net.zdsoft.basedata.remote.service.TeacherRemoteService;
import net.zdsoft.basedata.remote.service.UnitRemoteService;
import net.zdsoft.basedata.remote.service.UserRemoteService;
import net.zdsoft.basedata.service.TeachGroupService;
import net.zdsoft.basedata.service.TreeService;
import net.zdsoft.framework.entity.Constant;
import net.zdsoft.framework.entity.TR;
import net.zdsoft.framework.utils.EntityUtils;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.power.constant.PowerConstant;
import net.zdsoft.power.entity.SysApRole;
import net.zdsoft.power.remote.service.SysApRoleRemoteService;
import net.zdsoft.system.entity.server.Server;
import net.zdsoft.system.entity.user.Role;
import net.zdsoft.system.remote.service.RoleRemoteService;
import net.zdsoft.system.remote.service.ServerRemoteService;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.TypeReference;

@Service("treeService")
public class TreeServiceImpl implements TreeService {

    @Autowired
    private UnitRemoteService unitService;
    @Autowired
    private GradeRemoteService gradeService;
    @Autowired
    private ClassRemoteService classService;
    @Autowired
    private StudentRemoteService studentService;
    @Autowired
    private TeacherRemoteService teacherService;
    @Autowired
    private DeptRemoteService deptService;
    @Autowired
    private TeachPlaceRemoteService teachPlaceRemoteService;
    @Autowired
    private TeachBuildingRemoteService teachBuildingRemoteService;
    @Autowired
    private UserRemoteService userService;
    @Autowired
	private ServerRemoteService serverRemoteService;
    @Autowired
    private RoleRemoteService roleRemoteService;
    @Autowired
    private SysApRoleRemoteService sysApRoleService;
    @Autowired
    private TeachGroupService teachGroupService;

    @Override
    public JSONArray gradeForSchoolInsetZTree(String... unitIds) {
        JSONArray jsonArr = new JSONArray();
        if (unitIds != null && unitIds.length > 0) {
            List<Unit> unitList = SUtils.dt(unitService.findListByIds(unitIds), new TypeReference<List<Unit>>(){});
            findUnitZTreeJson(null, unitList.size() == 1 ? true : false, jsonArr, unitList);
            Map<String, List<Grade>> gradeMap = SUtils.dt(gradeService.findBySchoolIdMap(unitIds), new TypeReference<Map<String, List<Grade>>>(){});;
            for (Map.Entry<String, List<Grade>> entry : gradeMap.entrySet()) {
                String key = entry.getKey();
                List<Grade> value = entry.getValue();
                findGradeZTreeJson(key, false, jsonArr, value, false);
            }
        }
        return jsonArr;
    }

    @Override
    public void findGradeZTreeJson(String pId, boolean isOpen, JSONArray jsonArr, List<Grade> list, boolean isParent) {
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
    public void findUnitZTreeJson(String pId, boolean isOpen, JSONArray jsonArr, List<Unit> list) {
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
            }
            else {
                treeNodeDto.setType("edu");
            }
            jsonArr.add(JSON.toJSON(treeNodeDto));
        }
    }

    @Override
    public JSONArray gradeForUnitInsetZTree(String... unitIds) {
        JSONArray jsonArr = new JSONArray();
        if (unitIds != null && unitIds.length > 0) {
            List<Unit> unitList = SUtils.dt(unitService.findListByIds(unitIds), new TypeReference<List<Unit>>(){});
            findUnitZTreeJson(null, unitList.size() == 1 ? true : false, jsonArr, unitList);
            Map<String, List<Unit>> unitMap = SUtils.dt(unitService.findDirectUnitsByParentIds(Unit.UNIT_CLASS_SCHOOL, unitIds), new TypeReference<Map<String, List<Unit>>>(){});
            Set<String> unitIdsSet = new HashSet<String>();
            for (Map.Entry<String, List<Unit>> entry : unitMap.entrySet()) {
                String key = entry.getKey();
                List<Unit> value = entry.getValue();
                findUnitZTreeJson(key, false, jsonArr, value);
                for (Unit unit : value) {
                    unitIdsSet.add(unit.getId());
                }
            }
            Map<String, List<Grade>> gradeMap = SUtils.dt(gradeService.findBySchoolIdMap(unitIdsSet.toArray(new String[0])), new TypeReference<Map<String, List<Grade>>>(){});
            for (Map.Entry<String, List<Grade>> entry : gradeMap.entrySet()) {
                String key = entry.getKey();
                List<Grade> value = entry.getValue();
                findGradeZTreeJson(key, false, jsonArr, value, false);
            }
        }
        return jsonArr;
    }
    @Override
    public JSONArray gradeClassForSchoolInsetZTree(String sections,
    		String gradeCodes, String... unitIds) {
    	JSONArray jsonArr = new JSONArray();
        if (unitIds != null && unitIds.length > 0) {
            List<Unit> unitList = SUtils.dt(unitService.findListByIds(unitIds), new TypeReference<List<Unit>>(){});
            findUnitZTreeJson(null, unitList.size() == 1 ? true : false, jsonArr, unitList);
            Map<String, List<Grade>> gradeMap = SUtils.dt(gradeService.findBySchoolIdMap(unitIds), new TypeReference<Map<String, List<Grade>>>(){});
            for (Map.Entry<String, List<Grade>> entry : gradeMap.entrySet()) {
                List<Grade> value = entry.getValue();
                for (int i =0; i<value.size(); i++) {
                	Grade grade = value.get(i);
                    if(StringUtils.isNotBlank(sections) && sections.indexOf(grade.getSection()+"") < 0){
                    	value.remove(i);
                    	i--;
                    	continue;
                    }
                    if(StringUtils.isNotBlank(gradeCodes) && gradeCodes.indexOf(grade.getGradeCode()+"") < 0){
                    	value.remove(i);
                    	i--;
                    	continue;
                    }
                }
            }
            Set<String> gradeIds = new HashSet<String>();
            for (Map.Entry<String, List<Grade>> entry : gradeMap.entrySet()) {
                String key = entry.getKey();
                List<Grade> value = entry.getValue();
                findGradeZTreeJson(key, false, jsonArr, value, true);
                for (Grade grade : value) {
                    gradeIds.add(grade.getId());
                }
            }
            Map<String, List<Clazz>> classMap = SUtils.dt(classService.findMapByGradeIdIn(gradeIds.toArray(new String[0])), new TypeReference<Map<String, List<Clazz>>>(){});
            for (Map.Entry<String, List<Clazz>> entry : classMap.entrySet()) {
                String key = entry.getKey();
                List<Clazz> value = entry.getValue();
                findClassZTreeJson(key, false, jsonArr, value, false);
            }
        }
        return jsonArr;
    }
    @Override
    public JSONArray gradeClassForSchoolInsetZTree(String... unitIds) {
        JSONArray jsonArr = new JSONArray();
        if (unitIds != null && unitIds.length > 0) {
            List<Unit> unitList = SUtils.dt(unitService.findListByIds(unitIds), new TypeReference<List<Unit>>(){});
            findUnitZTreeJson(null, unitList.size() == 1 ? true : false, jsonArr, unitList);
            Map<String, List<Grade>> gradeMap = SUtils.dt(gradeService.findBySchoolIdMap(unitIds), new TypeReference<Map<String, List<Grade>>>(){});
            Set<String> gradeIds = new HashSet<String>();
            for (Map.Entry<String, List<Grade>> entry : gradeMap.entrySet()) {
                String key = entry.getKey();
                List<Grade> value = entry.getValue();
                findGradeZTreeJson(key, false, jsonArr, value, true);
                for (Grade grade : value) {
                    gradeIds.add(grade.getId());
                }
            }
            Map<String, List<Clazz>> classMap = SUtils.dt(classService.findMapByGradeIdIn(gradeIds.toArray(new String[0])), new TypeReference<Map<String, List<Clazz>>>(){});
            for (Map.Entry<String, List<Clazz>> entry : classMap.entrySet()) {
                String key = entry.getKey();
                List<Clazz> value = entry.getValue();
                findClassZTreeJson(key, false, jsonArr, value, false);
            }
        }
        return jsonArr;
    }

    @Override
    public JSONArray gradeClassStudentForSchoolInsetZTree(String... unitIds) {
        JSONArray jsonArr = new JSONArray();
        if (unitIds != null && unitIds.length > 0) {
            List<Unit> unitList = SUtils.dt(unitService.findListByIds(unitIds), new TypeReference<List<Unit>>(){});
            findUnitZTreeJson(null, unitList.size() == 1 ? true : false, jsonArr, unitList);
            Map<String, List<Grade>> gradeMap = SUtils.dt(gradeService.findBySchoolIdMap(unitIds), new TypeReference<Map<String, List<Grade>>>(){});
            Set<String> gradeIds = new HashSet<String>();
            Set<String> classIds = new HashSet<String>();
            for (Map.Entry<String, List<Grade>> entry : gradeMap.entrySet()) {
                String key = entry.getKey();
                List<Grade> value = entry.getValue();
                findGradeZTreeJson(key, false, jsonArr, value, true);
                for (Grade grade : value) {
                    gradeIds.add(grade.getId());
                }
            }
            Map<String, List<Clazz>> classMap = SUtils.dt(classService.findMapByGradeIdIn(gradeIds.toArray(new String[0])), new TypeReference<Map<String, List<Clazz>>>(){});
            for (Map.Entry<String, List<Clazz>> entry : classMap.entrySet()) {
                String key = entry.getKey();
                List<Clazz> value = entry.getValue();
                findClassZTreeJson(key, false, jsonArr, value, true);
                for (Clazz clazz : value) {
                    classIds.add(clazz.getId());
                }
            }
            Map<String, List<Student>> studentMap = SUtils.dt(studentService.findMapByClassIdIn(classIds.toArray(new String[0])), new TypeReference<Map<String, List<Student>>>(){});
            for (Map.Entry<String, List<Student>> entry : studentMap.entrySet()) {
                String key = entry.getKey();
                List<Student> value = entry.getValue();
                findStudentZTreeJson(key, false, jsonArr, value);
            }
        }
        return jsonArr;
    }

    @Override
    public void findClassZTreeJson(String pId, boolean isOpen, JSONArray jsonArr, List<Clazz> list, boolean isParent) {
        if (StringUtils.isBlank(pId)) {
            pId = "";
        }
        TreeNodeDto treeNodeDto;
        for (Clazz grade : list) {
            treeNodeDto = new TreeNodeDto();
            treeNodeDto.setpId(pId);
            treeNodeDto.setId(grade.getId());
            treeNodeDto.setName(grade.getClassName());
            treeNodeDto.setTitle(grade.getClassName());
            treeNodeDto.setOpen(isOpen);
            treeNodeDto.setType("class");
            if (isParent) {
                treeNodeDto.setIsParent(isParent);
            }
            jsonArr.add(JSON.toJSON(treeNodeDto));
        }
    }

    @Override
    public JSONArray gradeClassForUnitInsetZTree(String... unitIds) {
        JSONArray jsonArr = new JSONArray();
        if (unitIds != null && unitIds.length > 0) {
            List<Unit> unitList = SUtils.dt(unitService.findListByIds(unitIds), new TypeReference<List<Unit>>(){});
            findUnitZTreeJson(null, unitList.size() == 1 ? true : false, jsonArr, unitList);
            Map<String, List<Unit>> unitMap = SUtils.dt(unitService.findDirectUnitsByParentIds(Unit.UNIT_CLASS_SCHOOL, unitIds), new TypeReference<Map<String, List<Unit>>>(){});
            Set<String> unitIdsSet = new HashSet<String>();
            for (Map.Entry<String, List<Unit>> entry : unitMap.entrySet()) {
                String key = entry.getKey();
                List<Unit> value = entry.getValue();
                findUnitZTreeJson(key, false, jsonArr, value);
                for (Unit unit : value) {
                    unitIdsSet.add(unit.getId());
                }
            }
            Map<String, List<Grade>> gradeMap = SUtils.dt(gradeService.findBySchoolIdMap(unitIdsSet.toArray(new String[0])), new TypeReference<Map<String, List<Grade>>>(){});
            Set<String> gradeIds = new HashSet<String>();
            for (Map.Entry<String, List<Grade>> entry : gradeMap.entrySet()) {
                String key = entry.getKey();
                List<Grade> value = entry.getValue();
                findGradeZTreeJson(key, false, jsonArr, value, true);
                for (Grade grade : value) {
                    gradeIds.add(grade.getId());
                }
            }
            Map<String, List<Clazz>> classMap = SUtils.dt(classService.findMapByGradeIdIn(gradeIds.toArray(new String[0])), new TypeReference<Map<String, List<Clazz>>>(){});
            for (Map.Entry<String, List<Clazz>> entry : classMap.entrySet()) {
                String key = entry.getKey();
                List<Clazz> value = entry.getValue();
                findClassZTreeJson(key, false, jsonArr, value, false);
            }
        }
        return jsonArr;
    }

    @Override
    public JSONArray unitForDirectInsetZTree(boolean isSchool, String... unitIds) {
        JSONArray jsonArr = new JSONArray();
        if (unitIds != null && unitIds.length > 0) {
            List<Unit> unitList = SUtils.dt(unitService.findListByIds(unitIds), new TypeReference<List<Unit>>(){});
            findUnitZTreeJson(null, unitList.size() == 1 ? true : false, jsonArr, unitList);
            Map<String, List<Unit>> unitMap = SUtils.dt(unitService.findDirectUnitsByParentIds(isSchool ? Unit.UNIT_CLASS_SCHOOL : null, unitIds), new TypeReference<Map<String, List<Unit>>>(){});
            for (Map.Entry<String, List<Unit>> entry : unitMap.entrySet()) {
                String key = entry.getKey();
                List<Unit> value = entry.getValue();
                findUnitZTreeJson(key, false, jsonArr, value);
            }
        }
        return jsonArr;
    }

    @Override
    public JSONArray deptForUnitInsetZTree(String... unitIds) {
        JSONArray jsonArr = new JSONArray();
        if (unitIds != null && unitIds.length > 0) {
            List<Unit> unitList = SUtils.dt(unitService.findListByIds(unitIds), new TypeReference<List<Unit>>(){});
            findUnitZTreeJson(null, unitList.size() == 1 ? true : false, jsonArr, unitList);
            Map<String, List<Dept>> deptMap = SUtils.dt(deptService.findByUnitIdMap(unitIds), new TypeReference<Map<String, List<Dept>>>(){});
            for (Map.Entry<String, List<Dept>> entry : deptMap.entrySet()) {
                String key = entry.getKey();
                List<Dept> value = entry.getValue();
                findDeptZTreeJson(key, false, jsonArr, value, false);
            }
        }
        return jsonArr;
    }

    @Override
    public JSONArray deptTeacherForUnitInsetZTree(String... unitIds) {
        JSONArray jsonArr = new JSONArray();
        if (unitIds != null && unitIds.length > 0) {
            List<Unit> unitList = SUtils.dt(unitService.findListByIds(unitIds), new TypeReference<List<Unit>>(){});
            findUnitZTreeJson(null, unitList.size() == 1 ? true : false, jsonArr, unitList);
            Map<String, List<Dept>> deptMap = SUtils.dt(deptService.findByUnitIdMap(unitIds), new TypeReference<Map<String, List<Dept>>>(){});
            List<String> deptIds = new ArrayList<String>();
            for (Map.Entry<String, List<Dept>> entry : deptMap.entrySet()) {
                String key = entry.getKey();
                List<Dept> value = entry.getValue();
                findDeptZTreeJson(key, false, jsonArr, value, true);
                for (Dept dept : value) {
                    deptIds.add(dept.getId());
                }
            }
            Map<String, List<Teacher>> teacherMap = SUtils.dt(teacherService.findMapByDeptIdIn(deptIds.toArray(new String[0])), new TypeReference<Map<String, List<Teacher>>>(){});
            for (Map.Entry<String, List<Teacher>> entry : teacherMap.entrySet()) {
                String key = entry.getKey();
                List<Teacher> value = entry.getValue();
                findTeacherZTreeJson(key, false, jsonArr, value);
            }

        }
        return jsonArr;
    }

    @Override
    public JSONArray deptForDirectUnitInsetZTree(String... unitIds) {
        JSONArray jsonArr = new JSONArray();
        if (unitIds != null && unitIds.length > 0) {
            List<Unit> unitList = SUtils.dt(unitService.findListByIds(unitIds), new TypeReference<List<Unit>>(){});
            findUnitZTreeJson(null, unitList.size() == 1 ? true : false, jsonArr, unitList);
            Map<String, List<Unit>> unitMap = SUtils.dt(unitService.findDirectUnitsByParentIds(null, unitIds), new TypeReference<Map<String, List<Unit>>>(){});
            Set<String> unitIdsSet = new HashSet<String>();
            for (Map.Entry<String, List<Unit>> entry : unitMap.entrySet()) {
                String key = entry.getKey();
                List<Unit> value = entry.getValue();
                findUnitZTreeJson(key, false, jsonArr, value);
                for (Unit unit : value) {
                    unitIdsSet.add(unit.getId());
                }
            }
            Map<String, List<Dept>> deptMap = SUtils.dt(deptService.findByUnitIdMap(unitIdsSet.toArray(new String[0])), new TypeReference<Map<String, List<Dept>>>(){});
            for (Map.Entry<String, List<Dept>> entry : deptMap.entrySet()) {
                String key = entry.getKey();
                List<Dept> value = entry.getValue();
                findDeptZTreeJson(key, false, jsonArr, value, false);
            }
        }
        return jsonArr;
    }

    @Override
    public void findDeptZTreeJson(String pId, boolean isOpen, JSONArray jsonArr, List<Dept> list, boolean isParent) {
        if (StringUtils.isBlank(pId)) {
            pId = "";
        }
        TreeNodeDto treeNodeDto;
        for (Dept dept : list) {
            treeNodeDto = new TreeNodeDto();
            treeNodeDto.setpId(StringUtils.equals(Constant.GUID_ZERO, dept.getParentId()) ? pId : dept.getParentId());
            treeNodeDto.setId(dept.getId());
            treeNodeDto.setName(dept.getDeptName());
            treeNodeDto.setTitle(dept.getDeptName());
            treeNodeDto.setOpen(isOpen);
            treeNodeDto.setType("dept");
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

    @Override
    public void findTeacherZTreeJson(String pId, boolean isOpen, JSONArray jsonArr, List<Teacher> list) {
        if (StringUtils.isBlank(pId)) {
            pId = "";
        }
        TreeNodeDto treeNodeDto;
        for (Teacher teacher : list) {
            treeNodeDto = new TreeNodeDto();
            treeNodeDto.setpId(pId);
            treeNodeDto.setId(teacher.getId());
            treeNodeDto.setName(teacher.getTeacherName());
            treeNodeDto.setTitle(teacher.getTeacherName());
            treeNodeDto.setOpen(isOpen);
            treeNodeDto.setType("teacher");
            jsonArr.add(JSON.toJSON(treeNodeDto));
        }
    }

	@Override
	public JSONArray placeForBuildingTree(String unitId) {
		JSONArray jsonArr = new JSONArray();
		
		List<TeachBuilding> buildlist = SUtils.dt(teachBuildingRemoteService.findTeachBuildListByUnitId(unitId),new TR<List<TeachBuilding>>(){});
		if(CollectionUtils.isEmpty(buildlist)){
			buildlist =new ArrayList<TeachBuilding>();
		}
		Set<String> buildIds=EntityUtils.getSet(buildlist, "id");
		Map<String,List<TeachPlace>> placeByBuild=new HashMap<String,List<TeachPlace>>();
		List<TeachPlace> noBuildPlace=new ArrayList<TeachPlace>();
		List<TeachPlace> placeList = SUtils.dt(teachPlaceRemoteService.findTeachPlaceListByType(unitId, null),new TR<List<TeachPlace>>(){});
		if(CollectionUtils.isNotEmpty(placeList)){
			for(TeachPlace place:placeList){
				if(StringUtils.isNotBlank(place.getTeachBuildingId())){
					if(buildIds.contains(place.getTeachBuildingId())){
						//有楼层
						if(!placeByBuild.containsKey(place.getTeachBuildingId())){
							placeByBuild.put(place.getTeachBuildingId(), new ArrayList<TeachPlace>());
						}
						placeByBuild.get(place.getTeachBuildingId()).add(place);
						continue;
					}
				}
				noBuildPlace.add(place);
			}
		}
		findBulidZTreeJson(null, buildlist.size() == 1 ? true : false, jsonArr, buildlist);
		if(CollectionUtils.isNotEmpty(noBuildPlace)){
			findPlaceZTreeJson(null, noBuildPlace.size() == 1 ? true : false, jsonArr, noBuildPlace);
		}
		for (Map.Entry<String, List<TeachPlace>> entry : placeByBuild.entrySet()) {
            String key = entry.getKey();
            List<TeachPlace> value = entry.getValue();
            findPlaceZTreeJson(key, false, jsonArr, value);
        }
		return jsonArr;
	}
	
	public void findBulidZTreeJson(String pId, boolean isOpen, JSONArray jsonArr, List<TeachBuilding> list) {
        if (StringUtils.isBlank(pId)) {
            pId = "";
        }
        TreeNodeDto treeNodeDto = null;
        for (TeachBuilding bulid : list) {
            treeNodeDto = new TreeNodeDto();
            treeNodeDto.setpId(pId);
            treeNodeDto.setId(bulid.getId());
            treeNodeDto.setName(bulid.getBuildingName());
            treeNodeDto.setTitle(bulid.getBuildingName());
            treeNodeDto.setOpen(isOpen);
            treeNodeDto.setType("build");
            jsonArr.add(JSON.toJSON(treeNodeDto));
        }
    }
	
	public void findPlaceZTreeJson(String pId, boolean isOpen, JSONArray jsonArr, List<TeachPlace> list) {
        if (StringUtils.isBlank(pId)) {
            pId = "";
        }
        TreeNodeDto treeNodeDto = null;
        for (TeachPlace place : list) {
            treeNodeDto = new TreeNodeDto();
            treeNodeDto.setpId(pId);
            treeNodeDto.setId(place.getId());
            treeNodeDto.setName(place.getPlaceName());
            treeNodeDto.setTitle(place.getPlaceName());
            treeNodeDto.setOpen(isOpen);
            treeNodeDto.setType("place");
            jsonArr.add(JSON.toJSON(treeNodeDto));
        }
    }
	
	
	@Override
	public JSONArray typeServerRoleForUserInsetTree(String unitId,Integer ownerType,Integer unitClass) {
		JSONArray jsonArr = new JSONArray();
		List<Integer> typeList = new ArrayList<>();
		typeList.add(PowerConstant.DEFAULT_SOURCE_VALUE);typeList.add(PowerConstant.ROLE_TYPE_OPER);
		TreeNodeDto treeNodeDto;
        for (Integer type : typeList) {
            treeNodeDto = new TreeNodeDto();
            treeNodeDto.setpId("");
            treeNodeDto.setId(type+"");
            if(PowerConstant.DEFAULT_SOURCE_VALUE == type) {
            	treeNodeDto.setTitle("默认");
            }
            if(PowerConstant.ROLE_TYPE_OPER == type) {
            	treeNodeDto.setTitle("其它");
            }
            treeNodeDto.setName(treeNodeDto.getTitle());
            treeNodeDto.setOpen(false);
            treeNodeDto.setType("source");
            jsonArr.add(JSON.toJSON(treeNodeDto));
        }
        //其它ap
        Map<String, List<Server>> serverMap = new HashMap<>();
        List<Server> serverList = SUtils.dt(serverRemoteService.findByOwnerTypeAndUnitIdAndUnitClass(ownerType,unitId,unitClass), Server.class);
        serverMap.put(PowerConstant.ROLE_TYPE_OPER+"", serverList);
        Set<Integer> serverIds = EntityUtils.getSet(serverList, "id");
        for (Map.Entry<String, List<Server>> entry : serverMap.entrySet()) {
        	String key =entry.getKey();
        	List<Server> value = entry.getValue();
        	findServerZTreeJson(key, false, jsonArr, value);
        }
        
        List<SysApRole> sysApRoleList = SUtils.dt(sysApRoleService.findByServerIdIn(serverIds.toArray(new Integer[serverIds.size()])),SysApRole.class);
        sysApRoleList = sysApRoleList.stream().filter(r ->r.getUnitId().equals(unitId)).collect(Collectors.toList());
        Set<String> roleIds = EntityUtils.getSet(sysApRoleList, "roleId");
		List<Role> roleList1 = SUtils.dt(roleRemoteService.findListByIds(roleIds.toArray(new String[roleIds.size()])), Role.class);
		//过滤掉没有激活的角色
		Map<String,Role> roleMap = new HashMap<>();
		roleList1.forEach(role->{
			if(role.getIsActive().equals("1")) {
				roleMap.put(role.getId(), role);
			}
		});
//		Map<String,Role> roleMap = EntityUtils.getMap(roleList1, "id");
		Map<Integer, List<SysApRole>> apListMap = EntityUtils.getListMap(sysApRoleList, "serverId", StringUtils.EMPTY);
		for (Map.Entry<Integer, List<SysApRole>> entry : apListMap.entrySet()) {
        	String key = entry.getKey()+"";
        	List<SysApRole> value = entry.getValue();
        	List<Role> vRoles = new ArrayList<>();
        	for (SysApRole sysApRole : value) {
        		if(roleMap ==null || roleMap.get(sysApRole.getRoleId()) == null) {
        			continue;
        		}
        		vRoles.add(roleMap.get(sysApRole.getRoleId()));
			}
        	findRoleZTreeJson(key, false, jsonArr, vRoles);
        }
        //默认
        Map<String, List<Role>> roleMap1 = new HashMap<>();
        List<Role> roleList = SUtils.dt(roleRemoteService.findByUnitIdAndRoleType(unitId,
 				PowerConstant.ROLE_TYPE_OPER),Role.class);
        roleList = roleList.stream().filter(r ->r.getIsActive().equals("1")).collect(Collectors.toList());
        roleMap1.put(PowerConstant.DEFAULT_SOURCE_VALUE+"", roleList);
        for (Map.Entry<String, List<Role>> entry : roleMap1.entrySet()) {
        	String key =entry.getKey();
        	List<Role> value = entry.getValue();
        	findRoleZTreeJson(key, false, jsonArr, value);
        }
	    return jsonArr;
	}
	
	private void findRoleZTreeJson(String pId, boolean isOpen, JSONArray jsonArr, List<Role> list) {
		if (StringUtils.isBlank(pId)) {
            pId = "";
        }
        TreeNodeDto treeNodeDto;
        for (Role role : list) {
            treeNodeDto = new TreeNodeDto();
            treeNodeDto.setpId(pId);
            treeNodeDto.setId(role.getId());
            treeNodeDto.setName(role.getName());
            treeNodeDto.setTitle(role.getName());
            treeNodeDto.setOpen(isOpen);
            treeNodeDto.setType("role");
            jsonArr.add(JSON.toJSON(treeNodeDto));
        }
	}

	private void findServerZTreeJson(String pId, boolean isOpen, JSONArray jsonArr, List<Server> list) {
		if (StringUtils.isBlank(pId)) {
            pId = "";
        }
        TreeNodeDto treeNodeDto;
        for (Server server : list) {
            treeNodeDto = new TreeNodeDto();
            treeNodeDto.setpId(pId);
            treeNodeDto.setId(server.getId()+"");
            treeNodeDto.setName(server.getName());
            treeNodeDto.setTitle(server.getName());
            treeNodeDto.setOpen(isOpen);
            treeNodeDto.setType("server");
            jsonArr.add(JSON.toJSON(treeNodeDto));
        }
	}

	@Override
	public JSONArray ownerTypeUserForUnitInsetTree(String... unitIds) {
		JSONArray jsonArr = new JSONArray();
        if (unitIds != null && unitIds.length > 0) {
            List<Unit> unitList = SUtils.dt(unitService.findListByIds(unitIds), new TypeReference<List<Unit>>(){});
            findUnitZTreeJson(null, unitList.size() == 1 ? true : false, jsonArr, unitList);
            List<Integer> ownerType = new ArrayList<>();
            ownerType.add(User.OWNER_TYPE_STUDENT);ownerType.add(User.OWNER_TYPE_TEACHER);ownerType.add(User.OWNER_TYPE_FAMILY);
            Map<String,List<Integer>> ownerTypeMap = new HashMap<>();
            for (Unit unit : unitList) {
            	ownerTypeMap.put(unit.getId(), ownerType);
			}
            for (Map.Entry<String, List<Integer>> entry : ownerTypeMap.entrySet()) {
                String key =entry.getKey();
                List<Integer> value = entry.getValue();
                findOwerTypeZTreeJson(key, false, jsonArr, value);
            }
            List<User> uList = SUtils.dt(userService.findByUnitIds(unitIds), new TypeReference<List<User>>(){});
            Map<String, List<User>> uidMap =EntityUtils.getListMap(uList, User::getUnitId, Function.identity());
            for (Map.Entry<String, List<User>> entry : uidMap.entrySet()) {
                String key =entry.getKey();
                List<User> value = entry.getValue();
                Map<Integer, List<User>> typeMap = EntityUtils.getListMap(value, User::getOwnerType, Function.identity());
                for (Map.Entry<Integer, List<User>> entry1 : typeMap.entrySet()) {
                	String key1 = key + entry1.getKey();
                    List<User> value1 = entry1.getValue();
                    findUserZTreeJson(key1, false, jsonArr, value1);
                }
            }
        }
        return jsonArr;
	}
	/**
	 * @param key
	 * @param b
	 * @param jsonArr
	 * @param value
	 */
	private void findOwerTypeZTreeJson(String pId, boolean isOpen, JSONArray jsonArr, List<Integer> list) {
		if (StringUtils.isBlank(pId)) {
            pId = "";
        }
        TreeNodeDto treeNodeDto;
        for (Integer ownerType : list) {
            treeNodeDto = new TreeNodeDto();
            treeNodeDto.setpId(pId);
            treeNodeDto.setId(pId+ownerType);
            if(User.OWNER_TYPE_FAMILY == ownerType) {
            	treeNodeDto.setTitle("家长");
            }
            if(User.OWNER_TYPE_STUDENT == ownerType) {
            	treeNodeDto.setTitle("学生");
            }
            if(User.OWNER_TYPE_TEACHER == ownerType) {
            	treeNodeDto.setTitle("教师");
            }
            treeNodeDto.setName(treeNodeDto.getTitle());
            treeNodeDto.setOpen(isOpen);
            treeNodeDto.setType("ownerType");
            jsonArr.add(JSON.toJSON(treeNodeDto));
        }
	}

	private void findUserZTreeJson(String pId, boolean isOpen, JSONArray jsonArr, List<User> list) {
		if (StringUtils.isBlank(pId)) {
            pId = "";
        }
        TreeNodeDto treeNodeDto;
        for (User user : list) {
        	Integer userType = user.getUserType();
        	if(userType != null && userType == User.USER_TYPE_COMMON_USER) {
        		treeNodeDto = new TreeNodeDto();
        		treeNodeDto.setpId(pId);
        		treeNodeDto.setId(user.getId());
        		treeNodeDto.setName(user.getUsername());
        		treeNodeDto.setTitle(user.getRealName());
        		treeNodeDto.setOpen(isOpen);
        		treeNodeDto.setType("user");
        		jsonArr.add(JSON.toJSON(treeNodeDto));
        	}
        }
	}

	@Override
	public JSONArray teacherForGroupTree(String unitId, String teacherId) {
		//获取全校老师
		List<TeachGroupDto> groupDtoList=teachGroupService.findAllTeacherGroup(unitId, true);
		if(StringUtils.isNotBlank(teacherId)){
			groupDtoList = groupDtoList.stream().filter(e->e.getMainTeacherList().stream().map(TeacherDto::getTeacherId).collect(Collectors.toList()).contains(teacherId))
					.collect(Collectors.toList());
			groupDtoList.forEach(a->{
				a.setMainTeacherList(a.getMainTeacherList().stream().filter(b->b.getTeacherId().equals(teacherId)).collect(Collectors.toList()));
			});
		}
		JSONArray jsonArr = new JSONArray();
		TreeNodeDto treeNodeDto=null;
		Unit unit = SUtils.dc(unitService.findOneById(unitId), Unit.class);
		treeNodeDto = new TreeNodeDto();
        treeNodeDto.setpId("");
        treeNodeDto.setId(unitId);
        treeNodeDto.setName(unit.getUnitName());
        treeNodeDto.setTitle(unit.getUnitName());
        treeNodeDto.setOpen(false);
        treeNodeDto.setType("school");
        jsonArr.add(JSON.toJSON(treeNodeDto));
		if(CollectionUtils.isNotEmpty(groupDtoList)) {
			for(TeachGroupDto item:groupDtoList) {
				treeNodeDto = new TreeNodeDto();
	            treeNodeDto.setpId(unitId);
	            treeNodeDto.setId(item.getTeachGroupId());
	            treeNodeDto.setName(item.getTeachGroupName());
	            treeNodeDto.setTitle(item.getTeachGroupName());
	            treeNodeDto.setOpen(false);
	            treeNodeDto.setType("group");
	            jsonArr.add(JSON.toJSON(treeNodeDto));
	            if(CollectionUtils.isNotEmpty(item.getMainTeacherList())) {
	            	for(TeacherDto item1:item.getMainTeacherList()) {
	            		treeNodeDto = new TreeNodeDto();
	            		treeNodeDto.setpId(item.getTeachGroupId());
	            		treeNodeDto.setId(item.getTeachGroupId()+"_"+item1.getTeacherId());
	            		treeNodeDto.setName(item1.getTeacherName());
	            		treeNodeDto.setTitle(item1.getTeacherName());
	            		treeNodeDto.setOpen(false);
		                treeNodeDto.setType("teacher");
		                jsonArr.add(JSON.toJSON(treeNodeDto));
	            	}

	            }
			}
		}
		
		return jsonArr;
	}

	@Override
	public JSONArray gradeClassStudentForClazzInsetZTree(String[] classIds) {
		JSONArray jsonArr = new JSONArray();
        if (classIds != null && classIds.length > 0) {
        	List<Clazz> classList = SUtils.dt(classService.findListByIds(classIds),Clazz.class);
        	Collections.sort(classList, new Comparator<Clazz>(){

				@Override
				public int compare(Clazz o1, Clazz o2) {
					if(StringUtils.isBlank(o1.getClassCode())||StringUtils.isBlank(o2.getClassCode())){
						return 0;
					}
					return o1.getClassCode().compareTo(o2.getClassCode());
				}
        		
        	});
        	Map<String, List<Clazz>> classMap = EntityUtils.getListMap(classList, Clazz::getGradeId, Function.identity());
        	List<Grade> gradeList = SUtils.dt(gradeService.findListByIds(classMap.keySet().toArray(new String[0])),Grade.class);
        	Collections.sort(gradeList, new Comparator<Grade>(){

				@Override
				public int compare(Grade o1, Grade o2) {
					if(StringUtils.isBlank(o1.getGradeCode())||StringUtils.isBlank(o2.getGradeCode())){
						return 0;
					}
					return o1.getGradeCode().compareTo(o2.getGradeCode());
				}
        		
        	});
        	Map<String, List<Grade>> gradeMap = EntityUtils.getListMap(gradeList, Grade::getSchoolId, Function.identity());
            List<Unit> unitList = SUtils.dt(unitService.findListByIds(gradeMap.keySet().toArray(new String[0])), new TypeReference<List<Unit>>(){});
            findUnitZTreeJson(null, unitList.size() == 1 ? true : false, jsonArr, unitList);
            for (Map.Entry<String, List<Grade>> entry : gradeMap.entrySet()) {
                String key = entry.getKey();
                List<Grade> value = entry.getValue();
                findGradeZTreeJson(key, false, jsonArr, value, true);
            }
            for (Map.Entry<String, List<Clazz>> entry : classMap.entrySet()) {
                String key = entry.getKey();
                List<Clazz> value = entry.getValue();
                findClassZTreeJson(key, false, jsonArr, value, true);
            }
            Map<String, List<Student>> studentMap = SUtils.dt(studentService.findMapByClassIdIn(classIds), new TypeReference<Map<String, List<Student>>>(){});
            for (Map.Entry<String, List<Student>> entry : studentMap.entrySet()) {
                String key = entry.getKey();
                List<Student> value = entry.getValue();
                findStudentZTreeJson(key, false, jsonArr, value);
            }
        }
        return jsonArr;
	}
}
