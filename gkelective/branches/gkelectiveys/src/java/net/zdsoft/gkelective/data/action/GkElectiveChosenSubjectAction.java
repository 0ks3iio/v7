package net.zdsoft.gkelective.data.action;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.zdsoft.basedata.entity.Clazz;
import net.zdsoft.basedata.entity.Course;
import net.zdsoft.basedata.entity.Grade;
import net.zdsoft.basedata.entity.Student;
import net.zdsoft.basedata.remote.service.ClassRemoteService;
import net.zdsoft.basedata.remote.service.CourseRemoteService;
import net.zdsoft.basedata.remote.service.GradeRemoteService;
import net.zdsoft.basedata.remote.service.StudentRemoteService;
import net.zdsoft.framework.action.BaseAction;
import net.zdsoft.framework.annotation.ControllerInfo;
import net.zdsoft.framework.entity.Json;
import net.zdsoft.framework.entity.TR;
import net.zdsoft.framework.utils.DateUtils;
import net.zdsoft.framework.utils.EntityUtils;
import net.zdsoft.framework.utils.ExportUtils;
import net.zdsoft.framework.utils.RedisInterface;
import net.zdsoft.framework.utils.RedisUtils;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.gkelective.data.constant.GkElectveConstants;
import net.zdsoft.gkelective.data.dto.ChosenSubjectSearchDto;
import net.zdsoft.gkelective.data.dto.GkConditionDto;
import net.zdsoft.gkelective.data.dto.GkResultDto;
import net.zdsoft.gkelective.data.dto.StudentSubjectDto;
import net.zdsoft.gkelective.data.entity.GkLimitSubject;
import net.zdsoft.gkelective.data.entity.GkRelationship;
import net.zdsoft.gkelective.data.entity.GkResult;
import net.zdsoft.gkelective.data.entity.GkStuRemark;
import net.zdsoft.gkelective.data.entity.GkSubjectArrange;
import net.zdsoft.gkelective.data.service.GkLimitSubjectService;
import net.zdsoft.gkelective.data.service.GkRelationshipService;
import net.zdsoft.gkelective.data.service.GkResultService;
import net.zdsoft.gkelective.data.service.GkStuRemarkService;
import net.zdsoft.gkelective.data.service.GkSubjectArrangeService;
import net.zdsoft.system.entity.mcode.McodeDetail;
import net.zdsoft.system.remote.service.McodeRemoteService;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;

@Controller
@RequestMapping("/gkelective/{arrangeId}")
public class GkElectiveChosenSubjectAction extends BaseAction {
	
	private static final boolean GkLimitSubject = false;

	@InitBinder
	public void initBinder(WebDataBinder wb) throws Exception {
		wb.setAutoGrowCollectionLimit(Integer.MAX_VALUE);
	}
	
    @Autowired
    private ClassRemoteService classService;
    @Autowired
    private CourseRemoteService courseRemoteService;
    @Autowired
    private StudentRemoteService studentService;
    @Autowired
    private GkResultService gkResultService;
    @Autowired
    private GkSubjectArrangeService gkSubjectArrangeService;
    @Autowired
    private GradeRemoteService gradeRemoteService;
    @Autowired
    private GkStuRemarkService gkStuRemarkService;
    @Autowired
    private GkRelationshipService gkRelationshipService;
    @Autowired
    private McodeRemoteService mcodeRemoteService;
    @Autowired
    private GkLimitSubjectService gkLimitSubjectService;
    
    @RequestMapping("/chosenSubject/index/page")
    @ControllerInfo(value = "选课结果index")
    public String showIndex(@PathVariable final String arrangeId, ModelMap map, HttpSession httpSession) {
        map.put("arrangeId", arrangeId);
        final GkSubjectArrange gkArrange = RedisUtils.getObject(GkElectveConstants.GK_ARRANGE_KEY+arrangeId, RedisUtils.TIME_FIVE_MINUTES, new TypeReference<GkSubjectArrange>(){}, new RedisInterface<GkSubjectArrange>(){
			@Override
			public GkSubjectArrange queryData() {
				return gkSubjectArrangeService.findArrangeById(arrangeId);
			}
		});
		map.put("gkSubArr", gkArrange);
		// 查出年级下的所有行政班级--5分钟缓存减少性能消耗
        List<Clazz> clazzList = RedisUtils.getObject(GkElectveConstants.GRADE_CLASS_LIST_KEY+gkArrange.getGradeId(), RedisUtils.TIME_FIVE_MINUTES, new TypeReference<List<Clazz>>(){}, new RedisInterface<List<Clazz>>(){
			@Override
			public List<Clazz> queryData() {
				return SUtils.dt(classService.findBySchoolIdGradeId(gkArrange.getUnitId(),gkArrange.getGradeId()),new TR<List<Clazz>>() {});
			}
        });
        map.put("clazzList", clazzList);
        //新高考科目缓存5分钟
        List<Course> coursesList = RedisUtils.getObject(GkElectveConstants.GK_OPENSUBJECT_KEY+arrangeId, RedisUtils.TIME_ONE_MINUTE, new TypeReference<List<Course>>(){}, new RedisInterface<List<Course>>(){
			@Override
			public List<Course> queryData() {
				List<GkRelationship> findByTypePrimaryIdIn = gkRelationshipService.findByTypePrimaryIdIn(GkElectveConstants.RELATIONSHIP_TYPE_03,arrangeId);
				Set<String> subjectIds = EntityUtils.getSet(findByTypePrimaryIdIn, "relationshipTargetId");
				return SUtils.dt(courseRemoteService.findListByIds(subjectIds.toArray(new String[0])),new TR<List<Course>>() {});
			}
		});
        map.put("coursesList", coursesList);
        return "/gkelectiveys/chosenSubject/chosenSubjectIndex.ftl";
    }
    @ResponseBody
    @RequestMapping("/chosenSubject/saveStatues")
    @ControllerInfo(value = "保存选课审核list")
    public String saveStatues(GkResultDto dto,int index,int isPass){
    	List<GkResult> gkResultList = dto.getGkResults();
            try {
            	if(CollectionUtils.isNotEmpty(gkResultList)){
            		List<GkResult> gkResults = new ArrayList<GkResult>();
            		GkResult g;
            		for(GkResult e : gkResultList){
            			for(int i=0;i<e.getIds().length;i++){
            				g = new GkResult();
            				g.setId(e.getIds()[i]);
            				g.setStatus(index == (i+1)?isPass:e.getStatues()[i]);
            				gkResults.add(g);
            			}
            		}
            		gkResultService.updateStatus(gkResults);
            	}else{
                   return error("没有数据需要保存!");
            	}
            }catch (Exception e) {
                e.printStackTrace();
                return returnError("保存失败！", e.getMessage());
            }
            return success("保存成功！");
    }
    @ResponseBody
    @RequestMapping("/chosenSubject/saveOneStatues")
    @ControllerInfo(value = "保存选课审核list")
    public String saveStatues(String resId,int isPass){
    	try {
    		List<GkResult> gkResults = new ArrayList<GkResult>();
    		GkResult g = new GkResult();
    		g.setId(resId);
    		g.setStatus(isPass);
    		gkResults.add(g);
			gkResultService.updateStatus(gkResults);
    	}catch (Exception e) {
    		e.printStackTrace();
    		return returnError("保存失败！", e.getMessage());
    	}
    	return success("修改成功！");
    }
    @RequestMapping("/chosenSubject/list/page")
    @ControllerInfo(value = "显示已选择的学生list")
    public String showChosenList(@PathVariable String arrangeId,ChosenSubjectSearchDto searchDto, ModelMap map, HttpServletRequest request) {
    	findIsChosenList(arrangeId, searchDto, map);
        return "/gkelectiveys/chosenSubject/isChosenStudentList.ftl";
    }
	public void findIsChosenList(String arrangeId, ChosenSubjectSearchDto searchDto, ModelMap map) {
        GkSubjectArrange gkArrange = gkSubjectArrangeService.findArrangeById(arrangeId);
        Map<String,String> subjectMap = new HashMap<String, String>();
        if(StringUtils.isNotBlank(searchDto.getSearchSubject())){
        	//去空格，中文逗号转换为英文逗号
        	searchDto.setSearchSubject(searchDto.getSearchSubject().replaceAll(" ", ""));
        	searchDto.setSearchSubject(searchDto.getSearchSubject().replaceAll("，", ","));
        	String[] split = searchDto.getSearchSubject().split(",");
        	for (String string : split) {
        		subjectMap.put(string, string);
			}
        }
        if(StringUtils.isNotBlank(searchDto.getSearchCondition())){
        	searchDto.setSearchCondition(searchDto.getSearchCondition().replaceAll(" ", ""));
        	searchDto.setSearchCondition(searchDto.getSearchCondition().replaceAll("，", ","));
        	searchDto.setSearchCondition(searchDto.getSearchCondition().replaceAll("%", ""));
        }
        map.put("subjectMap", subjectMap);
        // 查出带分页的已选课的信息 按班级查找不分页
    	List<GkResult> gkResultList = gkResultService.findGkResult(arrangeId, searchDto, null, null);
    	
//        Set<String> stuIds = EntityUtils.getSet(gkResultList, "studentId");
//        Map<String, Student> studentMap = new HashMap<String, Student>();
//        if(CollectionUtils.isNotEmpty(stuIds)){
//        	List<Student> studentList = SUtils.dt(studentService.findByIds(stuIds.toArray(new String[0])),new TR<List<Student>>() {});
//            studentMap = EntityUtils.getMap(studentList, "id");
//        }
        // 查出年级下的所有行政班级--5分钟缓存减少性能消耗
        final String gid=gkArrange.getGradeId();
        List<Clazz> clazzList = RedisUtils.getObject(GkElectveConstants.GRADE_CLASS_LIST_KEY+gid, RedisUtils.TIME_FIVE_MINUTES, new TypeReference<List<Clazz>>(){}, new RedisInterface<List<Clazz>>(){
			@Override
			public List<Clazz> queryData() {
				return SUtils.dt(classService.findBySchoolIdGradeId(gkArrange.getUnitId(),gid),new TR<List<Clazz>>() {});
			}
        });

//        Set<String> classIds = new HashSet<String>();
//        for (Clazz c : clazzList) {
//            classIds.add(c.getId());
//        }
        Map<String, Clazz> classMap = EntityUtils.getMap(clazzList, "id");
//        Student student = null;
        Clazz clazz = null;
        for (GkResult gkr : gkResultList) {
    		clazz = classMap.get(gkr.getClassId());
    		if(clazz!=null)
    		gkr.setClassName(clazz != null ? clazz.getClassNameDynamic() : "");
        }
        map.put("classId", searchDto.getSearchClassId());
        map.put("gkResultList", gkResultList);
        map.put("arrangeId", arrangeId);
        map.put("isChosenCount", gkResultList.size());
        map.put("gkArrange", gkArrange);
	}
	
	@ResponseBody
    @RequestMapping("/chosenSubject/count")
    @ControllerInfo(value = "弹出层根据学生学号查询学生信息")
    public String showCount(@PathVariable final String arrangeId) {
    	JSONObject json = new JSONObject();
    	final GkSubjectArrange gkArrange = RedisUtils.getObject(GkElectveConstants.GK_ARRANGE_KEY+arrangeId, RedisUtils.TIME_FIVE_MINUTES, new TypeReference<GkSubjectArrange>(){}, new RedisInterface<GkSubjectArrange>(){
			@Override
			public GkSubjectArrange queryData() {
				return gkSubjectArrangeService.findArrangeById(arrangeId);
			}
		});
    	List<Clazz> clazzList = RedisUtils.getObject(GkElectveConstants.GRADE_CLASS_LIST_KEY+gkArrange.getGradeId(), RedisUtils.TIME_FIVE_MINUTES, new TypeReference<List<Clazz>>(){}, new RedisInterface<List<Clazz>>(){
			@Override
			public List<Clazz> queryData() {
				return SUtils.dt(classService.findBySchoolIdGradeId(gkArrange.getUnitId(),gkArrange.getGradeId()),new TR<List<Clazz>>() {});
			}
        });
    	Set<String> classIds = EntityUtils.getSet(clazzList, "id");
    	List<Student> studentList = SUtils.dt(studentService.findByClassIds(classIds.toArray(new String[0])), new TR<List<Student>>() {});
        Integer countIsChosen=0;
        Integer countUnChosen=0;
        if(CollectionUtils.isNotEmpty(studentList)){
	        // 已选课的总数
	        countIsChosen = gkResultService.findisChosenStudentCountByArrangeId(arrangeId, EntityUtils.getSet(studentList, "id").toArray(new String[0]));
	        // 未选课的总数 = 年级中所有学生数量 - 已选课的总数
	        countUnChosen = studentList.size() - countIsChosen;
        }
        json.put("countIsChosen", countIsChosen);
        json.put("countUnChosen", countUnChosen);
        return json.toJSONString();
    }

//    /**
//     * 导航栏已选课，未选课统计总数的显示
//     * 
//     * @param arrangeId
//     * @param classIds
//     * @return map的key分别为countIsChosen 和 countUnChosen
//     */
//    private Map<String, Integer> getCount1(String arrangeId, String[] classIds) {
//        Map<String, Integer> string2Count = new HashMap<String, Integer>();
//        //需要优化查下太慢
//        List<Student> studentList = SUtils.dt(studentService.findByClassIds(classIds), new TR<List<Student>>() {});
//        Integer countIsChosen=0;
//        Integer countUnChosen=0;
//        if(CollectionUtils.isNotEmpty(studentList)){
//	        // 已选课的总数
//	        countIsChosen = gkResultService.findisChosenStudentCountByArrangeId(arrangeId, EntityUtils.getSet(studentList, "id").toArray(new String[0]));
//	        // 未选课的总数 = 年级中所有学生数量 - 已选课的总数
//	        countUnChosen = studentList.size() - countIsChosen;
//        }
//        string2Count.put("countIsChosen", countIsChosen);
//        string2Count.put("countUnChosen", countUnChosen);
//        return string2Count;
//    }

    @RequestMapping("/chosenSubject/unChosenlist/page")
    @ControllerInfo(value = "显示未选择的学生list")
    public String showUnChosenList(@PathVariable final String arrangeId,ChosenSubjectSearchDto searchDto, ModelMap map, HttpServletRequest request, HttpSession httpSession) {
//        Pagination page = createPagination(request);
        List<GkResult> gkResultList = new ArrayList<GkResult>();
        final GkSubjectArrange gkArrange = gkSubjectArrangeService.findArrangeById(arrangeId);
        // 查出年级下的所有行政班级--5分钟缓存减少性能消耗
        List<Clazz> clazzList = RedisUtils.getObject(GkElectveConstants.GRADE_CLASS_LIST_KEY+gkArrange.getGradeId(), RedisUtils.TIME_FIVE_MINUTES, new TypeReference<List<Clazz>>(){}, new RedisInterface<List<Clazz>>(){
			@Override
			public List<Clazz> queryData() {
				return SUtils.dt(classService.findBySchoolIdGradeId(gkArrange.getUnitId(),gkArrange.getGradeId()),new TR<List<Clazz>>() {});
			}
        });
        Set<String> classIds = new HashSet<String>();
        Map<String, String> id2name = new HashMap<String, String>();
        for (Clazz c : clazzList) {
            classIds.add(c.getId());
            id2name.put(c.getId(), c.getClassNameDynamic());
        }
        // 根据条件参数classId判断（classId为空则 传入该年级下所有参数）
        if (StringUtils.isBlank(searchDto.getSearchClassId())) {
            gkResultList = gkResultService.findUnChosenGkResult(arrangeId, searchDto, null, true, classIds.toArray(new String[0]));
        }else {
            Set<String> ids = new HashSet<String>();
            ids.add(searchDto.getSearchClassId());
            gkResultList = gkResultService.findUnChosenGkResult(arrangeId, searchDto, null, false, ids.toArray(new String[0]));
        }
        Set<String> stuIds = EntityUtils.getSet(gkResultList, "studentId");
        Map<String,GkStuRemark> stuRemarkMap = new HashMap<String, GkStuRemark>();
        Map<String, Student> studentMap = new HashMap<String, Student>();
        if(CollectionUtils.isNotEmpty(stuIds)){
        	List<GkStuRemark> stuRemarks = gkStuRemarkService.findByStudentIds(arrangeId, GkStuRemark.TYPE_REMARK, stuIds.toArray(new String[0]));
        	stuRemarkMap = EntityUtils.getMap(stuRemarks, "studentId");
        	List<Student> studentList = SUtils.dt(studentService.findListByIds(stuIds.toArray(new String[0])),new TR<List<Student>>() {});
            studentMap = EntityUtils.getMap(studentList, "id");
        }
        Student student = null;
        for (GkResult gkr : gkResultList) {
        	student = studentMap.get(gkr.getStudentId());
        	if(student!=null){
        		gkr.setClassName(id2name.get(student.getClassId()));
        		if(stuRemarkMap.containsKey(gkr.getStudentId())){
        			gkr.setRemark(stuRemarkMap.get(gkr.getStudentId()).getRemark());
        		}
        	}
        }

        // 所有供选择的学科
        List<Course> coursesList = RedisUtils.getObject(GkElectveConstants.GK_OPENSUBJECT_KEY+arrangeId, RedisUtils.TIME_ONE_MINUTE, new TypeReference<List<Course>>(){}, new RedisInterface<List<Course>>(){
			@Override
			public List<Course> queryData() {
				List<GkRelationship> findByTypePrimaryIdIn = gkRelationshipService.findByTypePrimaryIdIn(GkElectveConstants.RELATIONSHIP_TYPE_03,arrangeId);
				Set<String> subjectIds = EntityUtils.getSet(findByTypePrimaryIdIn, "relationshipTargetId");
				return SUtils.dt(courseRemoteService.findListByIds(subjectIds.toArray(new String[0])),new TR<List<Course>>() {});
			}
		});
        map.put("gkArrange", gkArrange);
        map.put("coursesList", coursesList);
        map.put("gkResultList", gkResultList);
        map.put("classId", searchDto.getSearchClassId());
//        if (StringUtils.isBlank(searchDto.getSearchClassId())) {
//            map.put("unChosenCount", page.getMaxRowCount());
//        }
//        else {
//            map.put("unChosenCount", gkResultList.size());
//        }
        map.put("unChosenCount", gkResultList.size());
//        sendPagination(request, map, page);
        return "/gkelectiveys/chosenSubject/unChosenStudentList.ftl";
    }
    
    @RequestMapping("/chosenSubject/edit/page")
    @ControllerInfo(value = "编辑或新增学生选课")
    public String showEditPage(@PathVariable final String arrangeId, ModelMap map, String stuCode, String studentId) {
        GkSubjectArrange gkArrange = gkSubjectArrangeService.findArrangeById(arrangeId);
        Student s = new Student();
        List<GkResult> gkr = new ArrayList<GkResult>();
        Map<String, GkResult> chosenSubject = new HashMap<String, GkResult>();
        if (gkArrange != null) {
            if (StringUtils.isNotBlank(stuCode)) {
                s = SUtils.dc(studentService.findByGreadIdStuCode(gkArrange.getGradeId(), stuCode), Student.class);
            }
            else {
                s = SUtils.dc(studentService.findOneById(studentId), Student.class);
            }
            if (s != null) {
                // 按id查找学生的选课结果
                gkr = gkResultService.findGkByStuId(new String[]{s.getId()}, arrangeId);
                for (GkResult g : gkr) {
                    chosenSubject.put(g.getSubjectId(), g);
                }
            }
            // 所有供选择的学科
            List<Course> coursesList = RedisUtils.getObject(GkElectveConstants.GK_OPENSUBJECT_KEY+arrangeId, RedisUtils.TIME_ONE_MINUTE, new TypeReference<List<Course>>(){}, new RedisInterface<List<Course>>(){
    			@Override
    			public List<Course> queryData() {
    				List<GkRelationship> findByTypePrimaryIdIn = gkRelationshipService.findByTypePrimaryIdIn(GkElectveConstants.RELATIONSHIP_TYPE_03,arrangeId);
    				Set<String> subjectIds = EntityUtils.getSet(findByTypePrimaryIdIn, "relationshipTargetId");
    				return SUtils.dt(courseRemoteService.findListByIds(subjectIds.toArray(new String[0])),new TR<List<Course>>() {});
    			}
    		});
            map.put("coursesList", coursesList);
        }
        map.put("subjectNum", gkArrange.getSubjectNum());
        if (studentId == null) {
            return "/gkelectiveys/chosenSubject/resultAdd.ftl";
        }
        map.put("student", s);
        map.put("chosenSubject", chosenSubject);
        return "/gkelectiveys/chosenSubject/resultEdit.ftl";
    }

    @ResponseBody
    @RequestMapping("/chosenSubject/findDetail")
    @ControllerInfo(value = "弹出层根据学生学号查询学生信息")
    public String showDetail(String stuCode, ModelMap map, @PathVariable String arrangeId) {
    	if (StringUtils.isBlank(stuCode)) {
            return error("请输入要查询的学号");
        }
        GkSubjectArrange gkArrange = gkSubjectArrangeService.findArrangeById(arrangeId);
        List<GkResult> gkr = new ArrayList<GkResult>();
        GkResult gk = new GkResult();
        Map<String, GkResult> chosenSubject = new HashMap<String, GkResult>();
        if (gkArrange != null) {
        	stuCode = stuCode.trim();
            Student s = SUtils.dc(studentService.findByGreadIdStuCode(gkArrange.getGradeId(), stuCode), Student.class);
            if (s == null) {
                return error("不存在学号为" + stuCode + "的学生");
            }
            Clazz clazz = SUtils.dt(classService.findOneById(s.getClassId()), new TR<Clazz>() {});
            Grade grade = SUtils.dt(gradeRemoteService.findOneById(gkArrange.getGradeId()), new TR<Grade>() {});
            // 学生已选科目
            gkr = gkResultService.findGkByStuId(new String[]{s.getId()}, arrangeId);
            for (GkResult g : gkr) {
                chosenSubject.put(g.getSubjectId(), g);
            }
            gk.setStuName(s.getStudentName());
            gk.setClassName(grade.getGradeName() + clazz.getClassName());
            gk.setSubjectId2Name(chosenSubject);
        }
        return Json.toJSONString(gk);
    }

    @ResponseBody
    @RequestMapping("/chosenSubject/save")
    @ControllerInfo(value = "保存学生新增或修改的选课信息")
    public String doSaveGkResult(String stuCode, String subjectId, String studentId, ModelMap map,
            @PathVariable String arrangeId) {
        List<GkResult> gkr = new ArrayList<GkResult>();
        try {
            GkSubjectArrange gkArrange = gkSubjectArrangeService.findArrangeById(arrangeId);
            Student s = new Student();
            if (gkArrange != null) {
                if (StringUtils.isNotBlank(stuCode)) {
                	stuCode = stuCode.trim();
                    s = SUtils.dc(studentService.findByGreadIdStuCode(gkArrange.getGradeId(), stuCode), Student.class);
                }
                else {
                    s = SUtils.dc(studentService.findOneById(studentId), Student.class);
                }
                if (s == null) {
                    return error("不存在该学号的学生");
                }
                else {
                    // 按id查找学生的选课结果
                    gkr = gkResultService.findGkByStuId(new String[]{s.getId()}, arrangeId);
                }
                List<GkResult> newGkList = new ArrayList<GkResult>();
                // 获取设置的选课数
                String[] courseIds = subjectId.split(",");
                if (courseIds.length != gkArrange.getSubjectNum()) {
                    return error("请选择" + gkArrange.getSubjectNum() + "门课程");
                }
                List<GkLimitSubject> GkLimitSubjectList = gkLimitSubjectService.findBySubjectArrangeId(arrangeId);
                for (GkLimitSubject gkLimitSubject : GkLimitSubjectList) {
                	List<String> subjectIdsList = Arrays.asList(gkLimitSubject.getSubjectVal().split(","));
                	if (subjectIdsList.containsAll(Arrays.asList(courseIds))) {
                		return error("限选组合请调整后，再保存！");
                	}
                }
                for (int i = 0; i < courseIds.length; i++) {
                    GkResult gk = new GkResult();
                    if (CollectionUtils.isNotEmpty(gkr)) {
                        gk.setId(gkr.get(i).getId());
                    }
                    gk.setSubjectArrangeId(arrangeId);
                    gk.setStudentId(s.getId());
                    gk.setSubjectId(courseIds[i]);
                    newGkList.add(gk);
                }
                gkResultService.saveAll(newGkList, null, null);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            return returnError("保存失败！", e.getMessage());
        }

        if (CollectionUtils.isNotEmpty(gkr)) {
            return success("修改成功！");
        }
        return success("新增成功！");
    }

    @ResponseBody
    @RequestMapping("/chosenSubject/saveAll")
    @ControllerInfo(value = "批量保存未选课学生的选课信息")
    public String doSaveAllGkResult(GkResultDto dto, ModelMap map, @PathVariable final String arrangeId) {
        List<GkResult> tempList = new ArrayList<GkResult>();
        List<GkResult> temp1List = new ArrayList<GkResult>();
        List<GkResult> saveList = new ArrayList<GkResult>();
        List<GkStuRemark> saveStuList = new ArrayList<GkStuRemark>();
        Set<String> stuIds = new HashSet<String>();
        Set<String> xxsubids = new HashSet<String>();
        try {
            GkSubjectArrange gkArrange = gkSubjectArrangeService.findArrangeById(arrangeId);
            
            List<GkLimitSubject>  limitsublist = gkLimitSubjectService.findBySubjectArrangeId(arrangeId);
            // 取出所有有选课信息的GkResult
            for (GkResult gkr : dto.getGkResults()) {
                if (gkr.getCourseIds() != null && gkr.getCourseIds().length>0) {
                    tempList.add(gkr);
                }
                if(StringUtils.isNotBlank(gkr.getRemark())){
                	temp1List.add(gkr);
                }
                stuIds.add(gkr.getStudentId());
            }
            // 确认选课数目是否有误，正确的需要进一步处理之后放入List中准备保存
            for (GkResult g : tempList) {
                if (g.getCourseIds().length != gkArrange.getSubjectNum()) {
                    return error("请确认已选课的学生都选择了 " + gkArrange.getSubjectNum() + "门课程");
                }
                boolean isb=false;
                if(CollectionUtils.isNotEmpty(limitsublist)){
					for(GkLimitSubject ent: limitsublist){
						String subs =ent.getSubjectVal();
						if(StringUtils.isNotBlank(subs)){
							if(subs.indexOf(g.getCourseIds()[0])>=0 && subs.indexOf(g.getCourseIds()[1])>=0 && subs.indexOf(g.getCourseIds()[2])>=0){
								isb=true;
								xxsubids.add(StringUtils.join(g.getCourseIds(), ","));
							}
						}
					}
				}
                
                if(isb){
                	continue;
                }
                
                for (int i = 0; i < g.getCourseIds().length; i++) {
                    GkResult gk = new GkResult();
                    gk.setSubjectArrangeId(arrangeId);
                    gk.setStudentId(g.getStudentId());
                    gk.setSubjectId(g.getCourseIds()[i]);
                    saveList.add(gk);
                }
                
            }
            for (GkResult g : temp1List) {
            	GkStuRemark stuRemark = new GkStuRemark();
            	stuRemark.setRemark(g.getRemark());
            	stuRemark.setStudentId(g.getStudentId());
            	stuRemark.setSubjectArrangeId(arrangeId);
            	stuRemark.setType(GkStuRemark.TYPE_REMARK);
            	saveStuList.add(stuRemark);
            }
            gkResultService.saveAll(saveList, saveStuList, stuIds);
            
            String ts = "保存成功！";
            if(CollectionUtils.isNotEmpty(xxsubids)){
            	List<Course> courses = RedisUtils.getObject(GkElectveConstants.GK_OPENSUBJECT_KEY+arrangeId, RedisUtils.TIME_ONE_MINUTE, new TypeReference<List<Course>>(){}, new RedisInterface<List<Course>>(){
        			@Override
        			public List<Course> queryData() {
        				List<GkRelationship> findByTypePrimaryIdIn = gkRelationshipService.findByTypePrimaryIdIn(GkElectveConstants.RELATIONSHIP_TYPE_03,arrangeId);
        				Set<String> subjectIds = EntityUtils.getSet(findByTypePrimaryIdIn, "relationshipTargetId");
        				return SUtils.dt(courseRemoteService.findListByIds(subjectIds.toArray(new String[0])),new TR<List<Course>>() {});
        			}
        		});
            	Map<String,Course> submap = EntityUtils.getMap(courses, "id");
            	String strv= "";
            	for(String key: xxsubids){
            		String[] keys= key.split(",");
            		strv += submap.get(keys[0]).getShortName()+","+submap.get(keys[1]).getShortName()+","+submap.get(keys[2]).getShortName()+":";
            	}
            	if(CollectionUtils.isNotEmpty(saveList)){
            		ts="部分保存成功！限选组合{"+strv.substring(0,strv.length()-1)+"}请调整后，再保存！";
            	}
            	ts="限选组合{"+strv.substring(0,strv.length()-1)+"}请调整后，再保存！";
            }
            
            return success(ts);
        }catch (Exception e) {
            e.printStackTrace();
            return returnError("保存失败！", e.getMessage());
        }
    }

    @ResponseBody
    @RequestMapping("/chosenSubject/delete")
    @ControllerInfo(value = "删除选课信息")
    public String doDeleteGkResult(String studentId, ModelMap map, @PathVariable String arrangeId) {
        try {
            GkSubjectArrange gkArrange = gkSubjectArrangeService.findArrangeById(arrangeId);
            if (gkArrange != null) {
                Student s = SUtils.dc(studentService.findOneById(studentId), Student.class);
                // 按id查找学生的选课结果
                List<String> gkr = gkResultService.findCoursesByStuId(s.getId(), arrangeId);
                if (CollectionUtils.isEmpty(gkr) || s == null) {
                    return error("你要删除的信息不存在,请刷新后重试");
                }
                else {
                    gkResultService.removeByArrangeIdAndStudentId(arrangeId, s.getId());
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            return returnError("删除失败！", e.getMessage());
        }

        return success("删除成功！");
    }
    
    
    @RequestMapping("/doQueryStudent/list/page")
    @ControllerInfo(value = "已选择结果统计学生详细")
    public String doQueryStudentList(@PathVariable String arrangeId,ModelMap map, HttpServletRequest request) {
    	String detailsPagevalue = request.getParameter("detailsPagevalue");
    	String[] dps = detailsPagevalue.split("-");
    	String courseId;
    	String type;
    	String classId;
    	
    	if(dps.length==3){
    		 courseId = dps[0];
        	 type = dps[1];
        	 classId = dps[2];
    	}else{
	    	 courseId = dps[0];
	    	 type = dps[1];
	    	 classId="";
    	}
    	ChosenSubjectSearchDto searchDto = new ChosenSubjectSearchDto();
    	searchDto.setSearchClassId(classId);
    	searchDto.setSearchSubject(courseId);
    	findIsChosenList(arrangeId, searchDto, map);
    	map.put("hasStuCount", true);
        return "/gkelectiveys/chosenSubject/isChosenStudentList.ftl";
    }
    
    @RequestMapping("/countChosenSubject/list/page")
    @ControllerInfo(value = "已选择结果统计")
    public String showCountList(@PathVariable final String arrangeId, ModelMap map,ChosenSubjectSearchDto searchDto,String searchViewType) {
    	if(StringUtils.isBlank(searchViewType)){
    		searchViewType="1";
    	}
    	searchViewType="2";
        List<Course> courses = RedisUtils.getObject(GkElectveConstants.GK_OPENSUBJECT_KEY+arrangeId, RedisUtils.TIME_ONE_MINUTE, new TypeReference<List<Course>>(){}, new RedisInterface<List<Course>>(){
			@Override
			public List<Course> queryData() {
				List<GkRelationship> findByTypePrimaryIdIn = gkRelationshipService.findByTypePrimaryIdIn(GkElectveConstants.RELATIONSHIP_TYPE_03,arrangeId);
				Set<String> subjectIds = EntityUtils.getSet(findByTypePrimaryIdIn, "relationshipTargetId");
				return SUtils.dt(courseRemoteService.findListByIds(subjectIds.toArray(new String[0])),new TR<List<Course>>() {});
			}
		});
        //导航栏使用
        GkSubjectArrange gkArrange = gkSubjectArrangeService.findArrangeById(arrangeId);
        
        List<GkResult> findByArrangeId = gkResultService.findResultByChosenSubjectSearchDto(arrangeId, gkArrange.getGradeId(), searchDto);
        
        List<GkLimitSubject>  limitsublist = gkLimitSubjectService.findBySubjectArrangeId(arrangeId);
        
        if(CollectionUtils.isEmpty(findByArrangeId)){
        	if("2".equals(searchViewType)){
        		//return "/gkelectiveys/chosenSubject/resultCount.ftl";
        	}else{
        		return "/gkelectiveys/chosenSubject/resultCountView.ftl";
        	}
        	
        }
        StudentSubjectDto dto = new StudentSubjectDto();
        Map<String, StudentSubjectDto> dtoMap = new HashMap<String, StudentSubjectDto>();
        if(CollectionUtils.isNotEmpty(findByArrangeId)){
	        for (GkResult result : findByArrangeId) {
	            if (!dtoMap.containsKey(result.getStudentId())) {
	                dto = new StudentSubjectDto();
	                dto.setChooseSubjectIds(new HashSet<String>());
	                dto.setScoreMap(new HashMap<String, Double>());
	                dto.setStuId(result.getStudentId());
	                dtoMap.put(result.getStudentId(), dto);
	            }
	            dtoMap.get(result.getStudentId()).getChooseSubjectIds().add(result.getSubjectId());
	        }
    	}
        Map<String, Integer> courseId2Count = new HashMap<String, Integer>();
        if(CollectionUtils.isNotEmpty(findByArrangeId)){
		    for (GkResult gkResult : findByArrangeId) {
		    	Integer integer = courseId2Count.get(gkResult.getSubjectId());
		    	if(integer == null){
		    		integer = 0;
		    	}
		    	courseId2Count.put(gkResult.getSubjectId(), ++integer);
			}
        }
        Map<String, Integer> courseName2Count = new HashMap<String, Integer>();
        Map<String, String> courseName2Id = new HashMap<String, String>();
        for (Course c : courses) {
        	courseName2Id.put(c.getSubjectName(), c.getId());
            if (courseId2Count.containsKey(c.getId())) {
                courseName2Count.put(c.getSubjectName(), courseId2Count.get(c.getId()));
            }else{
            	courseName2Count.put(c.getSubjectName(), 0);
            }
        }
        //各科目选择结果
        courseName2Count = sortByValue(courseName2Count);
        map.put("courseName2Count", courseName2Count);
        //计算组合
        Integer[] cSize = new Integer[courses.size()];
        for(int i = 0;i < courses.size();i++){
        	cSize[i] = i;
        }
        //三科目选择结果
        CombineAlgorithm combineAlgorithm = new CombineAlgorithm(cSize,3);
        Integer[][] result = combineAlgorithm.getResutl();
        List<GkConditionDto> gk3Condition = new ArrayList<GkConditionDto>();
		findSubRes(courses, dtoMap, result, gk3Condition, 3);
		//新增过滤筛选组合限选课程
		if(CollectionUtils.isNotEmpty(limitsublist)){
			for(GkLimitSubject lsent:limitsublist){
				String sv = lsent.getSubjectVal();
				if(StringUtils.isNotBlank(sv)){
					String[] svs = sv.split(",");
					if (svs.length==3) {
						for(GkConditionDto dto1: gk3Condition){
							Set<String> subs =dto1.getSubjectIds();
							if(subs.contains(svs[0])&& subs.contains(svs[1]) &&subs.contains(svs[2])){
								dto1.setLimitSubject(true);
							}
							
						}
					}
				}
					
				
			}
			
		}
		//end
        map.put("gk3Condition", gk3Condition);
        //二科目选择结果
        combineAlgorithm = new CombineAlgorithm(cSize,2);
        result = combineAlgorithm.getResutl();
        List<GkConditionDto> gk2Condition = new ArrayList<GkConditionDto>();
        findSubRes(courses, dtoMap, result, gk2Condition, 2);
        
        map.put("gk2Condition", gk2Condition);
        map.put("courseName2Id", courseName2Id);
        if("2".equals(searchViewType)){
    		return "/gkelectiveys/chosenSubject/resultCount.ftl";
    	}else{
    		JSONObject json=new JSONObject();
    		json.put("legendData", courseName2Count.keySet().toArray(new String[0]));
    		JSONArray jsonArr=new JSONArray();
    		JSONObject json2=null;
    		for(Map.Entry<String,Integer> entry : courseName2Count.entrySet()){
    			json2=new JSONObject();
    			json2.put("value", entry.getValue());
    			json2.put("name", entry.getKey());
    			jsonArr.add(json2);
    		}
    		json.put("loadingData", jsonArr);
    		String jsonStringData=json.toString();
    		map.put("jsonStringData1", jsonStringData);
    		
    		json=new JSONObject();
    		String[] xAxisData=new String[gk2Condition.size()];
    		Integer[][] loadingData=new Integer[1][gk2Condition.size()];
    		for(int i=0;i<gk2Condition.size();i++){
    			xAxisData[i]=gk2Condition.get(i).getSubShortNames();
    			loadingData[0][i]=gk2Condition.get(i).getSumNum();
    		}
    		json.put("xAxisData", xAxisData);
    		json.put("loadingData", loadingData);
    		json.put("legendData", new String[]{"人数"});
    		jsonStringData=json.toString();
    		map.put("jsonStringData2", jsonStringData);
    		
    		json=new JSONObject();
    		xAxisData=new String[gk3Condition.size()];
    		loadingData=new Integer[1][gk3Condition.size()];
    		for(int i=0;i<gk3Condition.size();i++){
    			xAxisData[i]=gk3Condition.get(i).getSubShortNames();
    			loadingData[0][i]=gk3Condition.get(i).getSumNum();
    		}
    		json.put("xAxisData", xAxisData);
    		json.put("loadingData", loadingData);
    		json.put("legendData", new String[]{"人数"});
    		jsonStringData=json.toString();
    		map.put("jsonStringData3", jsonStringData);
    		return "/gkelectiveys/chosenSubject/resultCountView.ftl";
    	}
    }
	public void findSubRes(List<Course> courses, Map<String, StudentSubjectDto> dtoMap, Integer[][] resutl, List<GkConditionDto> gkCondition, int num) {
		GkConditionDto cDto = null;
        Set<String> linIds = null;
        String[] subNames = null;
        String shortName=null;
//        String shortName1=null;
        for(int i = 0;i < resutl.length;i++){
        	cDto = new GkConditionDto();
        	shortName="";
        	linIds = new HashSet<String>();
        	subNames = new String[resutl[i].length];
        	cDto.setSubjectIds(linIds);
        	cDto.setSubNames(subNames);
        	for(int j = 0;j < resutl[i].length;j++){
        		linIds.add(courses.get(resutl[i][j]).getId());
        		subNames[j] = courses.get(resutl[i][j]).getSubjectName();
        		shortName=shortName+courses.get(resutl[i][j]).getShortName();
        	}
        	cDto.setSubShortNames(shortName);
        	cDto.setSumNum(0);
        	gkCondition.add(cDto);
        }
        for (GkConditionDto item : gkCondition) {
			for(Map.Entry<String, StudentSubjectDto> entry : dtoMap.entrySet()){
				linIds = entry.getValue().getChooseSubjectIds();
				if(num == CollectionUtils.intersection(linIds,item.getSubjectIds()).size()){
					item.setSumNum(item.getSumNum()+1);
				}
			}
		}
        Collections.sort(gkCondition, new Comparator<GkConditionDto>() {
          @Override
          public int compare(GkConditionDto o1, GkConditionDto o2) {
              return o2.getSumNum().compareTo(o1.getSumNum());
          }
      });  
	}

	@ResponseBody
	@RequestMapping("/limitSubject/saveAll")
	@ControllerInfo("保存限选科目")
	public String doLimitSubject(String subjectIds,@PathVariable String arrangeId) {
		try{
			if (StringUtils.isNotBlank(subjectIds)) {
				String[] vals = subjectIds.split(",");
				if(vals!=null && vals.length==3){
					GkLimitSubject sent =null;
					List<GkLimitSubject>  list = gkLimitSubjectService.findBySubjectArrangeId(arrangeId);
					if(CollectionUtils.isNotEmpty(list)){
						for(GkLimitSubject ent: list){
							String subs =ent.getSubjectVal();
							if(StringUtils.isNotBlank(subs)){
								if(subs.indexOf(vals[0])>=0 && subs.indexOf(vals[1])>=0 && subs.indexOf(vals[2])>=0){
									break;
								}else{
									sent =new GkLimitSubject();
									sent.setSubjectArrangeId(arrangeId);
									sent.setSubjectVal(subjectIds);
								}
							}
						}
					}else{
						sent =new GkLimitSubject();
						sent.setSubjectArrangeId(arrangeId);
						sent.setSubjectVal(subjectIds);
					}
					if(sent!=null){
						gkLimitSubjectService.saveLimitSubject(sent);
					}
				}
						
			}
			
			
		}catch(Exception e){
			e.printStackTrace();
			return returnError();
		}
		return returnSuccess();
	}
	
    public static Map<String, Integer> sortByValue(Map<String, Integer> map) {
        ArrayList<Map.Entry<String, Integer>> list = new ArrayList<Map.Entry<String, Integer>>(map.entrySet());
        Collections.sort(list, new Comparator<Map.Entry<String, Integer>>() {
            @Override
            public int compare(Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2) {
                return o2.getValue() - o1.getValue();
            }
        });

        Map<String, Integer> result = new LinkedHashMap<String, Integer>();
        for (Map.Entry<String, Integer> entry : list) {
            result.put(entry.getKey(), entry.getValue());
        }
        return result;
    }
    
    /**
     * @Description:组合算法 从M个数中取出N个数，无顺序
     * 二维Object数组
     */
    class CombineAlgorithm {

        /* src数组的长度 */
        private int m;

        /* 需要获取N个数 */
        private int n;
        
        //临时变量，obj的行数
        private int objLineIndex;
        
        /* 存放结果的二维数组 */
        public Integer[][] obj;
        
		public CombineAlgorithm(Integer[] src, int getNum){
            m = src.length;
            n = getNum;
            
            /*  初始化  */
            objLineIndex = 0;
            obj = new Integer[combination(m,n)][n];
            
            Integer[] tmp = new Integer[n];
            combine(src, 0, 0, n, tmp);
//            System.out.println(src.length+"取"+getNum+"共有："+objLineIndex);
        }

        /**
         * <p>
         * 计算 C(m,n)个数 = (m!)/(n!*(m-n)!)
         * </p>
         * 从M个数中选N个数，函数返回有多少种选法 参数 m 必须大于等于 n m = 0; n = 0; retuan 1;
         * 
         * @param m
         * @param n
         * @return
         */
        public int combination(int m, int n) {
            if (m < n)
                return 0; // 如果总数小于取出的数，直接返回0

            int k = 1;
            int j = 1;
            // 该种算法约掉了分母的(m-n)!,这样分子相乘的个数就是有n个了
            for (int i = n; i >= 1; i--) {
                k = k * m;
                j = j * n;
                m--;
                n--;
            }
            return k / j;
        }
        
        /**
         * <p> 递归算法，把结果写到obj二维数组对象 </p>      
         * @param src
         * @param srcIndex
         * @param i
         * @param n 取几个
         * @param tmp
         * 大致思路
         * 例如 1，2，3，4，5，6，7 数组取3个相当于C（7，3）
         * 123
         * 124
         * 125
         * ...
         * 134
         * 135
         * ...
         * 567
         */
        private void combine(Integer src[], int srcIndex, int i, int n, Integer[] tmp) {
            int j;
            for (j = srcIndex; j < src.length - (n - 1); j++ ) {
                tmp[i] = src[j];
                if (n == 1) {
                    //System.out.println(Arrays.toString(tmp));
                    System.arraycopy(tmp, 0, obj[objLineIndex], 0, tmp.length);
                    objLineIndex ++;
                } else {
                    n--;//3---2---1
                    i++;//0---1---2
                    combine(src, j+1, i, n, tmp);
                    n++;
                    i--;
                }
            }
            
        }

        public Integer[][] getResutl() {
            return obj;
        }
        
    }
    
    @RequestMapping("/chosenSubject/export")
    @ControllerInfo(value = "导出已选结果" )
	public String exportResult(@PathVariable final String arrangeId, HttpServletResponse response){
    	// 数据整理 组装
    	List<GkResult> results = gkResultService.findByArrangeId(arrangeId);
    	if(CollectionUtils.isEmpty(results)){
    		return "";
    	}
    	Map<String, StudentSubjectDto> stuSubs = new HashMap<String, StudentSubjectDto>();
		for(GkResult re : results){
			// 学生选课数据处理
			StudentSubjectDto ssDto = stuSubs.get(re.getStudentId());
			if(ssDto == null){
				ssDto = new StudentSubjectDto();
				ssDto.setStuId(re.getStudentId());
				ssDto.setSubjectIds(new ArrayList<String>());
			}
			List<String> sids = ssDto.getSubjectIds();
			sids.add(re.getSubjectId());
			ssDto.setSubjectIds(sids);
			stuSubs.put(re.getStudentId(), ssDto);
		}
		results.clear();
        // 获得学生
        List<Student> stulist = SUtils.dt(studentService.findListByIds(stuSubs.keySet().toArray(new String[0])), new TR<List<Student>>() {
        });
        if(CollectionUtils.isEmpty(stulist)){
    		return "";
    	}
        Set<String> clsIds = EntityUtils.getSet(stulist, "classId");
        List<Clazz> clsList = SUtils.dt(classService.findListByIds(clsIds.toArray(new String[0])), new TR<List<Clazz>>() {
        });
        Map<String, String> clsMap = EntityUtils.getMap(clsList, "id", "classNameDynamic");
        Map<String, Map<String, McodeDetail>> mcodeDetailMap = SUtils.dt(mcodeRemoteService.findMapByMcodeIds("DM-XB"), new TypeReference<Map<String, Map<String, McodeDetail>>>(){});
        // 待匹配数据
        GkResult re;
        for(Student stu : stulist){
        	re = new GkResult();
        	re.setCourseIds(stuSubs.get(stu.getId()).getSubjectIds().toArray(new String[0]));
        	re.setStuName(stu.getStudentName());
        	re.setClassName(clsMap.get(stu.getClassId()));
        	re.setStucode(stu.getStudentCode());
        	if(mcodeDetailMap.get("DM-XB").get(stu.getSex()+"") != null){
        		re.setStuSex(mcodeDetailMap.get("DM-XB").get(stu.getSex()+"").getMcodeContent());
        	}
        	results.add(re);
        	re = null;
        }
        Collections.sort(results, new Comparator<GkResult>(){

			@Override
			public int compare(GkResult o1, GkResult o2) {
				if(StringUtils.equals(o1.getClassName(), o2.getClassName())){
					StringUtils.trimToEmpty(o1.getStuName()).
					compareTo(StringUtils.trimToEmpty(o2.getStuName()));
				}
				return StringUtils.trimToEmpty(o1.getClassName()).compareTo(StringUtils.trimToEmpty(o2.getClassName()));
			}
        });
        List<String> tis = new ArrayList<String>();
    	tis.add("班级");
    	tis.add("学生");
    	tis.add("学号");
    	tis.add("性别");
    	List<Course> courseList = RedisUtils.getObject(GkElectveConstants.GK_OPENSUBJECT_KEY+arrangeId, RedisUtils.TIME_ONE_MINUTE, new TypeReference<List<Course>>(){}, new RedisInterface<List<Course>>(){
			@Override
			public List<Course> queryData() {
				List<GkRelationship> findByTypePrimaryIdIn = gkRelationshipService.findByTypePrimaryIdIn(GkElectveConstants.RELATIONSHIP_TYPE_03,arrangeId);
				Set<String> subjectIds = EntityUtils.getSet(findByTypePrimaryIdIn, "relationshipTargetId");
				return SUtils.dt(courseRemoteService.findListByIds(subjectIds.toArray(new String[0])),new TR<List<Course>>() {});
			}
		});
    	Map<String, String> subNameMap = new HashMap<String, String>();
    	for(Course cs : courseList){
    		tis.add(cs.getSubjectName());
    		subNameMap.put(cs.getId(), cs.getSubjectName());
    	}
    	List<Map<String, String>> datas = new ArrayList<Map<String,String>>();
		Map<String, String> conMap = null;
    	for(GkResult gr : results){
    		conMap = new HashMap<String, String>();
    		String[] cids = gr.getCourseIds();
    		boolean hasSub = false;
    		for(String cid : cids){
    			if(subNameMap.containsKey(cid)){
    				hasSub = true;
    				conMap.put(subNameMap.get(cid), "√");
    			}
    		}
    		if(!hasSub){
    			continue;
    		}
    		conMap.put("班级", gr.getClassName());
    		conMap.put("学生", gr.getStuName());
    		conMap.put("学号", gr.getStucode());
    		conMap.put("性别", gr.getStuSex());
    		datas.add(conMap);
    	}
    	
    	// export
		Map<String, List<Map<String, String>>> sheetName2RecordListMap = new HashMap<String, List<Map<String,String>>>();
		sheetName2RecordListMap.put("学生已选结果", datas);
		Map<String, List<String>> titleMap = new HashMap<String, List<String>>();
		titleMap.put("学生已选结果", tis);
		GkSubjectArrange ar = gkSubjectArrangeService.findArrangeById(arrangeId);
		Grade grade = SUtils.dt(gradeRemoteService.findOneById(ar.getGradeId()), new TR<Grade>() {});
		String fileName = grade.getOpenAcadyear().substring(0, 4)+"级"+grade.getGradeName()+"已选结果-"+DateUtils.date2String(new Date(), "yyyyMMddHHmm");
		ExportUtils ex = ExportUtils.newInstance();
		ex.exportXLSFile(fileName, titleMap, sheetName2RecordListMap, response);
    	return "";
    }
}
