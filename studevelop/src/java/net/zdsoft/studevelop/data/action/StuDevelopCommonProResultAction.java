package net.zdsoft.studevelop.data.action;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.servlet.http.HttpSession;

import net.zdsoft.basedata.dto.TreeNodeDto;
import net.zdsoft.basedata.entity.ClassTeaching;
import net.zdsoft.basedata.entity.Clazz;
import net.zdsoft.basedata.entity.Grade;
import net.zdsoft.basedata.entity.Semester;
import net.zdsoft.basedata.entity.Student;
import net.zdsoft.basedata.remote.service.ClassRemoteService;
import net.zdsoft.basedata.remote.service.ClassTeachingRemoteService;
import net.zdsoft.basedata.remote.service.GradeRemoteService;
import net.zdsoft.basedata.remote.service.SemesterRemoteService;
import net.zdsoft.basedata.remote.service.StudentRemoteService;
import net.zdsoft.framework.annotation.ControllerInfo;
import net.zdsoft.framework.entity.Json;
import net.zdsoft.framework.entity.LoginInfo;
import net.zdsoft.framework.entity.TR;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.framework.utils.UuidUtils;
import net.zdsoft.studevelop.data.constant.StuDevelopConstant;
import net.zdsoft.studevelop.data.dto.StudevelopTemResultDto;
import net.zdsoft.studevelop.data.entity.StuDevelopCateGory;
import net.zdsoft.studevelop.data.entity.StuDevelopSubject;
import net.zdsoft.studevelop.data.entity.StudevelopTemplate;
import net.zdsoft.studevelop.data.entity.StudevelopTemplateItem;
import net.zdsoft.studevelop.data.entity.StudevelopTemplateOptions;
import net.zdsoft.studevelop.data.entity.StudevelopTemplateResult;
import net.zdsoft.studevelop.data.service.StuDevelopCateGoryService;
import net.zdsoft.studevelop.data.service.StuDevelopSubjectService;
import net.zdsoft.studevelop.data.service.StudevelopTemplateItemService;
import net.zdsoft.studevelop.data.service.StudevelopTemplateOptionsService;
import net.zdsoft.studevelop.data.service.StudevelopTemplateResultService;
import net.zdsoft.studevelop.data.service.StudevelopTemplateService;
import net.zdsoft.system.entity.mcode.McodeDetail;
import net.zdsoft.system.remote.service.McodeRemoteService;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.TypeReference;

/**
 * Created by luf on 2018/12/19.
 */

@Controller
@RequestMapping("/stuDevelop/proItemResult")
public class StuDevelopCommonProResultAction extends CommonAuthAction {
    @Autowired
    private SemesterRemoteService semesterRemoteService;
    @Autowired
    private ClassRemoteService classRemoteService;
    @Autowired
    private ClassTeachingRemoteService classTeachingService;
    @Autowired
    private StudentRemoteService studentRemoteService;
    @Autowired
    private GradeRemoteService gradeRemoteService;
    @Autowired
    private McodeRemoteService mcodeRemoteService;
    @Autowired
    private StudevelopTemplateService studevelopTemplateService;
    @Autowired
    private StudevelopTemplateOptionsService studevelopTemplateOptionsService;
    @Autowired
    private StudevelopTemplateItemService studevelopTemplateItemService;
    @Autowired
    private StudevelopTemplateResultService studevelopTemplateResultService;
    @Autowired
    private StuDevelopSubjectService stuDevelopSubjectService;
    @Autowired
    private StuDevelopCateGoryService stuDevelopCateGoryService;
    @RequestMapping("/toClassTree/page")
    public String toClassTree(ModelMap map,String acadyear,String semester,String noTeaching){
    	map.put("acadyear", acadyear);
    	map.put("semester", semester);
    	map.put("noTeaching", noTeaching);
        return "/studevelop/commonProjectItem/toClassTreeIndex.ftl";
    }
    /**
     * 本单位年级-班级-学生
     * 
     * @param unitId
     * @return
     */
    @ResponseBody
    @RequestMapping("/studentTree/page")
    @ControllerInfo("")
    public String gradeClassStudentForSchoolInsetTree(HttpSession httpSession,String acadyear,String semester,String noTeaching) {
        LoginInfo loginInfo = getLoginInfo(httpSession);
        JSONArray jsonArray = new JSONArray();
        List<Clazz> clazzList=SUtils.dt(classRemoteService.findByTeacherId(loginInfo.getOwnerId()),new TR<List<Clazz>>(){});
        Set<String> classIds=new HashSet<>();
        List<Clazz> lastClazzList=new ArrayList<>();
        if(!"true".equals(noTeaching)){
        	List<ClassTeaching> classTeachingList= SUtils.dt(classTeachingService.findClassTeachingList(loginInfo.getUnitId(), acadyear, semester, loginInfo.getOwnerId())
        			,new TR<List<ClassTeaching>>(){});
        	if(CollectionUtils.isNotEmpty(classTeachingList)){
        		classIds.addAll(classTeachingList.stream().map(ClassTeaching::getClassId).collect(Collectors.toSet()));
        		List<Clazz> clazzs=SUtils.dt(classRemoteService.findListByIds(classIds.toArray(new String[0])),new TR<List<Clazz>>(){});
        		if(CollectionUtils.isNotEmpty(clazzs)){//任课老师的行政班
        			lastClazzList.addAll(clazzs);
        		}
        	}
        }
        if(CollectionUtils.isNotEmpty(clazzList)){
        	classIds.addAll(clazzList.stream().map(Clazz::getId).collect(Collectors.toSet()));
        	lastClazzList.addAll(clazzList);//班主任的班级
        }
        if(CollectionUtils.isNotEmpty(classIds)){
        	findClazzZTreeJson(null, false, jsonArray, lastClazzList);
        	Map<String, List<Student>> studentMap = SUtils.dt(studentRemoteService.findMapByClassIdIn(classIds.toArray(new String[0])), new TypeReference<Map<String, List<Student>>>(){});
        	 for (Map.Entry<String, List<Student>> entry : studentMap.entrySet()) {
                 String key = entry.getKey();
                 List<Student> value = entry.getValue();
                 findStudentZTreeJson(key, false, jsonArray, value);
             }
        }
        return Json.toJSONString(jsonArray);
    }
   
    @RequestMapping("/index/page")
    public String healthStudentIndex(ModelMap map){
    	LoginInfo login=getLoginInfo();
        List<String> acadyearList = SUtils.dt(semesterRemoteService.findAcadeyearList(), new TR<List<String>>() {});
        if(CollectionUtils.isEmpty(acadyearList)){
            return errorFtl(map,"学年学期不存在");
        }
        map.put("acadyearList", acadyearList);
        Semester semesterObj = SUtils.dc(semesterRemoteService.getCurrentSemester(1,login.getUnitId()), Semester.class);
        if(semesterObj!=null){
            String acadyear=semesterObj.getAcadyear();
            String semester=semesterObj.getSemester()+"";
            map.put("acadyear", acadyear);
            map.put("semester", semester);
        }else{
            map.put("acadyear", "");
            map.put("semester", "");
        }
        boolean isAdmin=isAdmin(StuDevelopConstant.PERMISSION_TYPE_REPORT);
        map.put("isAdmin",isAdmin);
        return "/studevelop/commonProjectItem/stuProItemResultIndex.ftl";
    }
    @ResponseBody
    @RequestMapping("/classIds")
    @ControllerInfo(value = "获得班级列表")
    public List<Clazz> acadyearUpdate(String acadyear, String semester, ModelMap map) {
        List<Clazz> classList = SUtils.dt(classRemoteService.findByIdCurAcadyear(getLoginInfo().getUnitId(),acadyear), new TR<List<Clazz>>() {});
        return classList;
    }

    @ResponseBody
    @RequestMapping("/stuIds")
    @ControllerInfo(value = "获得学生列表")
    public List<Student> classUpdate(String classId, ModelMap map) {
        Map<String, List<Student>> stuMap = SUtils.dt(studentRemoteService.findMapByClassIdIn(new String[]{classId}),new TR<Map<String, List<Student>>>(){});
        List<Student> stuList = new ArrayList<Student>();
        if(!stuMap.isEmpty()){
            stuList = stuMap.get(classId);
        }
        return stuList;
    }
    @RequestMapping("/classList")
    public String stuHealthHeartList(@RequestParam String acadyear, @RequestParam String semester, @RequestParam String classId, String code , ModelMap map){
    	List<Student> stuList=SUtils.dt(studentRemoteService.findByClassIds(classId),new TR<List<Student>>(){});
    	Clazz  cla = SUtils.dc(classRemoteService.findOneById(classId),Clazz.class);
        Grade grade = SUtils.dc(gradeRemoteService.findOneById(cla.getGradeId()),Grade.class);
        int section =grade.getSection();
        String unitId=getLoginInfo().getUnitId();
        List<StudevelopTemplate> templateList = studevelopTemplateService.getTemplateByCode(acadyear,semester,null,String.valueOf(section), code,unitId);
        List<McodeDetail> detailList = SUtils.dt(mcodeRemoteService.findByMcodeIds("DM-SXXMLX") ,McodeDetail.class);
        Map<String,Boolean> completeMap=new HashMap<>();
        if(CollectionUtils.isNotEmpty(templateList)){
    		//获取模板
        	StudevelopTemplate template = templateList.get(0);
        	//获取项目
            List<StudevelopTemplateItem> templateItemList = studevelopTemplateItemService.getTemplateItemListByObjectType(template.getId(),null,StuDevelopConstant.HEALTH_IS_NOT_CLOSED);
            Map<String,List<StudevelopTemplateItem>> itemListMap = templateItemList.stream().collect(Collectors.groupingBy((e->e.getObjectType())));
            Map<String,StudevelopTemplateItem> itemMap = templateItemList.stream().collect(Collectors.toMap(StudevelopTemplateItem::getId, Function.identity()));
            Set<String> templagetItemIds = templateItemList.stream().map(s->s.getId()).collect(Collectors.toSet());
            //班级下学生
    		Set<String> stuIdSet = stuList.stream().map(Student::getId).collect(Collectors.toSet());
    		//整个班级的获取结果
    		List<StudevelopTemplateResult> templateResultList = studevelopTemplateResultService.getTemplateResultByStudentIds
    				(templagetItemIds.toArray(new String[0]), stuIdSet.toArray(new String[0]) ,acadyear ,semester);
    		if(CollectionUtils.isNotEmpty(templateResultList)){
    			if("2".equals(code)){//身心健康
    				StudevelopTemplateItem item=null;
        			//key为 学生id_objectType
        			Map<String,List<StudevelopTemplateResult>> resultListMap=new HashMap<>();
        			for (StudevelopTemplateResult result : templateResultList) {
        				if(StringUtils.isBlank(result.getTemplateOptionId()) && StringUtils.isBlank(result.getResult())){
        					continue;
        				}
        				item=itemMap.get(result.getTemplateItemId());
        				if(item!=null){
        					String key=result.getStudentId()+"_"+item.getObjectType();
        					if(!resultListMap.containsKey(key)){
        						resultListMap.put(key, new ArrayList<>());
        					}
        					resultListMap.get(key).add(result);
        				}
    				}
        			for (Student student : stuList) {
        				for (McodeDetail mcodeDetail : detailList) {
        					boolean flag=true;
        					String key=code+mcodeDetail.getThisId();
        					if(itemListMap.containsKey(key)){
        						if(!resultListMap.containsKey(student.getId()+"_"+key) || (itemListMap.get(key).size()>resultListMap.get(student.getId()+"_"+key).size())){
        							flag=false;
        						}
        					}
        					completeMap.put(student.getId()+"_"+key, flag);
        				}
        			}
    			}else{//思想素质
    				Map<String, List<StudevelopTemplateResult>> resultListMap=new HashMap<>();
    				String key=null;
    				for (StudevelopTemplateResult result : templateResultList) {
    					if(StringUtils.isBlank(result.getTemplateOptionId()) && StringUtils.isBlank(result.getResult())){
        					continue;
        				}
    					key=result.getStudentId()+"_"+result.getTemplateItemId();
						if(!resultListMap.containsKey(key)){
							resultListMap.put(key, new ArrayList<>());
						}
						resultListMap.get(key).add(result);
					}
    				for (Student student : stuList) {
    					for (StudevelopTemplateItem item : templateItemList) {
    						boolean flag=true;
    						if(!resultListMap.containsKey(student.getId()+"_"+item.getId())){
    							flag=false;
    						}
    						completeMap.put(student.getId()+"_"+item.getId(), flag);
        				}
    				}
    			}
    		}
    		map.put("templateItemList", templateItemList);
    	}
        map.put("completeMap",completeMap);
        map.put("code",code);
    	map.put("stuList",stuList);
    	map.put("detailList",detailList);
    	return "/studevelop/commonProjectItem/healthHeartList.ftl";
    }
    @RequestMapping("/edit")
    public String studentHealthHeartEdit(@RequestParam String acadyear, @RequestParam String semester,
    		@RequestParam String studentId, String code , ModelMap map){

        Student stu = SUtils.dc(studentRemoteService.findOneById(studentId) , Student.class);
        Clazz  cla = SUtils.dc(classRemoteService.findOneById(stu.getClassId()),Clazz.class);
        Grade grade = SUtils.dc(gradeRemoteService.findOneById(cla.getGradeId()),Grade.class);
        int section =grade.getSection();
        String unitId=getLoginInfo().getUnitId();
        List<StudevelopTemplate> templateList = studevelopTemplateService.getTemplateByCode(acadyear,semester,null,String.valueOf(section), code,unitId);
        StudevelopTemplate template = null;
        if(CollectionUtils.isNotEmpty(templateList)){
            template = templateList.get(0);
        }else{
            return "/studevelop/commonProjectItem/stuProjectResult.ftl";
        }

        List<StudevelopTemplateItem> templateItemList = studevelopTemplateItemService.getTemplateItemListByObjectType(template.getId(),null,"0");
//        templateItemList = templateItemList.stream().sorted(Comparator.comparing(StudevelopTemplateItem::getCreationTime)).collect(Collectors.toList());
        List<String> templagetItemIds = templateItemList.stream().map(s->s.getId()).collect(Collectors.toList());
        List<StudevelopTemplateOptions> templateOptionsList = studevelopTemplateOptionsService.getOptionsListByTemplateItemId(templagetItemIds.toArray(new String[0]));
        List<StudevelopTemplateResult> templateResultList = studevelopTemplateResultService.getTemplateResultByStudentId(templagetItemIds.toArray(new String[0]), studentId ,acadyear , semester);
        Map<String, StudevelopTemplateResult> templateResultMap = new HashMap<>();
        if (CollectionUtils.isNotEmpty(templateResultList)) {
            templateResultMap = templateResultList.stream().collect(Collectors.toMap(e -> e.getTemplateItemId(), Function.identity()));
        }

        Map<String ,List<StudevelopTemplateOptions> >  optionsMap = new HashMap<>();
        if(CollectionUtils.isNotEmpty(templateOptionsList)){
            optionsMap = templateOptionsList.stream().collect(Collectors.groupingBy(e->e.getTemplateItemId()));
        }
        for (StudevelopTemplateItem item : templateItemList) {
            List<StudevelopTemplateOptions> list = optionsMap.get(item.getId());
            item.setTemplateOptions(list);
            StudevelopTemplateResult result = templateResultMap.get(item.getId());
            if (result == null) {
                result = new StudevelopTemplateResult();
                result.setTemplateId(template.getId());
                result.setTemplateItemId(item.getId());
                result.setStudentId(studentId);
                result.setAcadyear(acadyear);
                result.setSemester(semester);
            }
            item.setTemplateResult(result);
        }

        Map<String,List<StudevelopTemplateItem>> templateItemMap = templateItemList.stream().collect(Collectors.groupingBy(e->e.getObjectType()));

        map.put("templateItemMap", templateItemMap);
        List<McodeDetail> detailList = SUtils.dt(mcodeRemoteService.findByMcodeIds("DM-SXXMLX") ,McodeDetail.class);

        map.put("detailList" ,detailList);
        map.put("code",code);
        return "/studevelop/commonProjectItem/stuProjectItemResultEdit.ftl";
    }

    @ResponseBody
    @RequestMapping("/save")
    public String saveResult(StudevelopTemResultDto dto,String classId,String isAdmin,String code){
        try{
            List<StudevelopTemplateResult> resultList = dto.getResultList();
            if (CollectionUtils.isNotEmpty(resultList)) {
            	Set<String> templateItemIds=new HashSet<>();
            	String studentId=resultList.get(0).getStudentId();
            	if(!"true".equals(isAdmin) && !"1".equals(code)){//不是管理员的情况下 也不是成绩登记的情况
            		List<Clazz> clazzList=SUtils.dt(classRemoteService.findByTeacherId(getLoginInfo().getOwnerId()),new TR<List<Clazz>>(){});
            		boolean canSave=false;
            		if(CollectionUtils.isNotEmpty(clazzList)){
                    	if(clazzList.stream().map(Clazz::getId).collect(Collectors.toSet()).contains(classId)){
                    		canSave=true;
                    	}
            		}
            		if(!canSave)
            			 return returnError("-1","不是班主任没有保存权限!");
            	}
            	List<StudevelopTemplateResult> inserList=new ArrayList<>();
                for (StudevelopTemplateResult result : resultList) {
                	templateItemIds.add(result.getTemplateItemId());
                	if(StringUtils.isBlank(result.getTemplateOptionId()) && StringUtils.isBlank(result.getResult())){
                		continue;//没有保存数据的内容 不进入数据库
                	}
                    if (StringUtils.isNotEmpty(result.getId())) {
                        result.setModifyTime(new Date());
                    }else{
                        result.setId(UuidUtils.generateUuid());
                        result.setCreationTime(new Date());
                    }
                    inserList.add(result);
                }
                studevelopTemplateResultService.deleteByItemIdsStuIds(templateItemIds.toArray(new String[0]), 
                		new String[]{studentId}, dto.getAcadyear(), dto.getSemester());
                studevelopTemplateResultService.saveAll(inserList.toArray(new StudevelopTemplateResult[0]));
            }
        }catch(Exception e){
            e.printStackTrace();
            return returnError();
        }

        return returnSuccess();
    }
    @RequestMapping("/gradeScore/classList")
    public String gradeScoreClaList(String acadyear ,String semester ,String classId ,ModelMap map){
    	String unitId=getLoginInfo().getUnitId();
    	Clazz  cla = SUtils.dc(classRemoteService.findOneById(classId),Clazz.class);
    	String gradeId = cla.getGradeId();
    	//学科
        List<StuDevelopSubject> subjectList = stuDevelopSubjectService.stuDevelopSubjectList(unitId, acadyear, semester, gradeId);
        List<Student> stuList=SUtils.dt(studentRemoteService.findByClassIds(classId),new TR<List<Student>>(){});
        Map<String,Boolean> completeMap=new HashMap<>();
        if(CollectionUtils.isNotEmpty(subjectList)){
        	Grade grade = SUtils.dc(gradeRemoteService.findOneById(gradeId),Grade.class);
            int section =grade.getSection();
            List<StudevelopTemplate> templateList = studevelopTemplateService.getTemplateByCode(acadyear,semester,gradeId,String.valueOf(section),
            											StuDevelopConstant.TEMPLATE_CODE_GRADE,unitId);
            if(CollectionUtils.isNotEmpty(templateList)){
            	//获取模板
            	StudevelopTemplate template = templateList.get(0);
            	//获取项目
                List<StudevelopTemplateItem> templateItemList = studevelopTemplateItemService.getTemplateItemListByObjectType(template.getId(),null,StuDevelopConstant.HEALTH_IS_NOT_CLOSED);
                Map<String,Boolean> typeMap=new HashMap<>();//true 对学科类别。false对学科
                for (StudevelopTemplateItem item : templateItemList) {
                	typeMap.put(item.getId(), "11".equals(item.getObjectType())?true:false);
				}
                Set<String> templagetItemIds = templateItemList.stream().map(s->s.getId()).collect(Collectors.toSet());
                //班级下学生
        		Set<String> stuIdSet = stuList.stream().map(Student::getId).collect(Collectors.toSet());
        		//整个班级的获取结果
        		List<StudevelopTemplateResult> templateResultList = studevelopTemplateResultService.getTemplateResultByStudentIds
        				(templagetItemIds.toArray(new String[0]), stuIdSet.toArray(new String[0]) ,acadyear ,semester);
        		Map<String,List<StudevelopTemplateResult>> resultTypeMap=new HashMap<>();//对学科类别显示的结果
        		Map<String,List<StudevelopTemplateResult>> resultNoTypeMap=new HashMap<>();//仅对学科
        		//结果的map key为 学生id+学科id+项目id
        		if(CollectionUtils.isNotEmpty(templateResultList)){
        			for (StudevelopTemplateResult result : templateResultList) {
        				if(typeMap.containsKey(result.getTemplateItemId())){
        					String key=result.getStudentId()+"_"+result.getSubjectId()+"_"+result.getTemplateItemId();
        					if(typeMap.get(result.getTemplateItemId())){//对学科类别
        						if(!resultTypeMap.containsKey(key)){
        							resultTypeMap.put(key, new ArrayList<StudevelopTemplateResult>());
        						}
        						resultTypeMap.get(key).add(result);
        					}else{
        						if(!resultNoTypeMap.containsKey(key)){
        							resultNoTypeMap.put(key, new ArrayList<StudevelopTemplateResult>());
        						}
        						resultNoTypeMap.get(key).add(result);
        					}
        				}
					}
        		}
        		for(Student stu:stuList){
        			for(StuDevelopSubject subject:subjectList){
        				boolean flag=true;
        				for (StudevelopTemplateItem item : templateItemList) {
        					String key=stu.getId()+"_"+subject.getId()+"_"+item.getId();
        					if("11".equals(item.getObjectType())){//对学科类别  至少一条
        						if(!resultTypeMap.containsKey(key)){
        							flag=false;
	        						break;
        						}else{
        							if(CollectionUtils.isNotEmpty(subject.getCateGoryList())){//学科类别的数据
        								if(subject.getCateGoryList().size()>resultTypeMap.get(key).size()){
        									flag=false;
        	        						break;
        								}
        							}
        						}
        					}else{//12  对学科显示  只有一条
        						if(!resultNoTypeMap.containsKey(key)){
        							//无论是输入还是
        							flag=false;
	        						break;
        						}
        					}
        				}
        				completeMap.put(stu.getId()+"_"+subject.getId(), flag);
        			}
        		}
            }
    	}
    	map.put("completeMap", completeMap);
    	map.put("subjectList", subjectList);
    	map.put("stuList", stuList);
    	return "/studevelop/commonProjectItem/stuProjectClassResult.ftl";
    }
    @RequestMapping("/gradeScore/edit")
    public String gradeScoreEdit(String acadyear ,String semester ,String studentId ,ModelMap map){
    	Student stu = SUtils.dc(studentRemoteService.findOneById(studentId) , Student.class);
    	String classId = stu.getClassId();
    	String studentName = stu.getStudentName();
        Clazz  cla = SUtils.dc(classRemoteService.findOneById(classId),Clazz.class);
        String gradeId = cla.getGradeId();
        Grade grade = SUtils.dc(gradeRemoteService.findOneById(gradeId),Grade.class);
        List<StuDevelopSubject> subjectList = stuDevelopSubjectService.stuDevelopSubjectList(getLoginInfo().getUnitId(), acadyear, semester, gradeId);

        int section =grade.getSection();
        String unitId=getLoginInfo().getUnitId();
        List<StudevelopTemplate> templateList = studevelopTemplateService.getTemplateByCode(acadyear,semester,gradeId,String.valueOf(section), StuDevelopConstant.TEMPLATE_CODE_GRADE,unitId);
        StudevelopTemplate template = null;
        if(CollectionUtils.isNotEmpty(templateList)){
            template = templateList.get(0);
        }else{
            return "/studevelop/commonProjectItem/stuProjectResult.ftl";
        }
        List<StudevelopTemplateItem> templateItemList = studevelopTemplateItemService.getTemplateItemListByObjectType(template.getId(),null,StuDevelopConstant.HEALTH_IS_NOT_CLOSED);

        List<String> templagetItemIds = templateItemList.stream().map(s->s.getId()).collect(Collectors.toList());
//        List<StudevelopTemplateOptions> templateOptionsList = studevelopTemplateOptionsService.findListByIn("templateItemId",templagetItemIds.toArray());
        List<StudevelopTemplateResult> templateResultList = studevelopTemplateResultService.getTemplateResultByStudentId(templagetItemIds.toArray(new String[0]), studentId ,acadyear ,semester);
        Map<String, StudevelopTemplateResult> templateResultMap = new HashMap<>();
        if (CollectionUtils.isNotEmpty(templateResultList)) {
            for (StudevelopTemplateResult result : templateResultList) {
                StringBuffer buffer = new StringBuffer(result.getTemplateItemId() + "_" + result.getSubjectId());
                if(StringUtils.isNotEmpty(result.getCategoryId())){
                    buffer.append("_" + result.getCategoryId());
                }
                templateResultMap.put(buffer.toString(), result);
            }
        }
        for (StudevelopTemplateItem item : templateItemList) {
            for(StuDevelopSubject sub : subjectList){
                List<StuDevelopCateGory> cateGoryList =sub.getCateGoryList();
                if ("11".equals(item.getObjectType()) && CollectionUtils.isNotEmpty(cateGoryList)) {
                    for (StuDevelopCateGory cateGory : cateGoryList) {
                        String key = item.getId()+"_"+sub.getId()+"_" +cateGory.getId();
                        StudevelopTemplateResult result = templateResultMap.get(key);
                        if (result == null) {
                            result = new StudevelopTemplateResult();
                            result.setStudentId(studentId);
                            result.setTemplateItemId(item.getId());
                            result.setTemplateId(item.getTemplateId());
                            result.setSubjectId(sub.getId());
                            result.setCategoryId(cateGory.getId());
                            result.setAcadyear(acadyear);
                            result.setSemester(semester);
                            templateResultMap.put(key , result);
                        }
                    }
                }
                else if ("12".equals(item.getObjectType()) || CollectionUtils.isEmpty(cateGoryList)) {
                    String key = item.getId()+"_"+sub.getId();
                    StudevelopTemplateResult result = templateResultMap.get(key);
                    if (result == null) {
                        result = new StudevelopTemplateResult();
                        result.setStudentId(studentId);
                        result.setTemplateItemId(item.getId());
                        result.setTemplateId(item.getTemplateId());
                        result.setSubjectId(sub.getId());
                        result.setAcadyear(acadyear);
                        result.setSemester(semester);
                        templateResultMap.put(key , result);
                    }
                }
            }
        }
        List<StudevelopTemplateOptions> templateOptionsList = studevelopTemplateOptionsService.getOptionsListByTemplateItemId(templagetItemIds.toArray(new String[0]));
        Map<String, List<StudevelopTemplateOptions>> optionsMap = new HashMap<>();
        for (StudevelopTemplateOptions options : templateOptionsList) {
            List<StudevelopTemplateOptions> list = optionsMap.get(options.getTemplateItemId());
            if (list == null) {
                list = new ArrayList<>();
                optionsMap.put(options.getTemplateItemId(), list);
            }
            list.add(options);
        }

        for (StudevelopTemplateItem item : templateItemList) {
            List<StudevelopTemplateOptions> list = optionsMap.get(item.getId());
            item.setTemplateOptions(list);
        }

        map.put("templateItemList" ,templateItemList);
        map.put("templateResultMap",templateResultMap);
        map.put("studentName", studentName);
        map.put("subjectList", subjectList);
//        map.put("stuDevelopCateGoryList", stuDevelopCateGoryList);
//        map.put("stuDevelopProjectList", stuDevelopProjectList);
        map.put("acadyear", acadyear);
        map.put("semester", semester);
        map.put("studentId", studentId);
        return "/studevelop/commonProjectItem/itemScoreRecordList.ftl";
    }
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
    public void findClazzZTreeJson(String pId, boolean isOpen, JSONArray jsonArr, List<Clazz> list) {
        if (StringUtils.isBlank(pId)) {
            pId = "";
        }
        TreeNodeDto treeNodeDto = null;
        for (Clazz clazz : list) {
            treeNodeDto = new TreeNodeDto();
            treeNodeDto.setpId(pId);
            treeNodeDto.setId(clazz.getId());
            treeNodeDto.setName(clazz.getClassNameDynamic());
            treeNodeDto.setTitle(clazz.getClassNameDynamic());
            treeNodeDto.setOpen(isOpen);
            treeNodeDto.setType("class");
            jsonArr.add(JSON.toJSON(treeNodeDto));
        }
    }
}
