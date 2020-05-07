package net.zdsoft.newgkelective.data.action;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.TypeReference;

import net.zdsoft.basedata.constant.BaseConstants;
import net.zdsoft.basedata.entity.Clazz;
import net.zdsoft.basedata.entity.Course;
import net.zdsoft.basedata.entity.Student;
import net.zdsoft.basedata.remote.service.ClassRemoteService;
import net.zdsoft.basedata.remote.service.CourseRemoteService;
import net.zdsoft.basedata.remote.service.StudentRemoteService;
import net.zdsoft.framework.action.BaseAction;
import net.zdsoft.framework.entity.TR;
import net.zdsoft.framework.utils.EntityUtils;
import net.zdsoft.framework.utils.Objects;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.newgkelective.data.entity.NewGkReferScore;
import net.zdsoft.newgkelective.data.entity.NewGkScoreResult;
import net.zdsoft.newgkelective.data.service.NewGkReferScoreService;
import net.zdsoft.newgkelective.data.service.NewGkScoreResultService;
@Controller
@RequestMapping("/newgkelective")
public class NewGkScoreResultAction extends BaseAction{
	@Autowired
	private NewGkReferScoreService newGkReferScoreService;
	@Autowired
	private NewGkScoreResultService newGkScoreResultService;
	@Autowired
	private CourseRemoteService courseRemoteService;
	@Autowired
	private StudentRemoteService studentRemoteService;
	@Autowired
	private ClassRemoteService classRemoteService;

	@RequestMapping("/newGkScoreResult/index")
	public String pageIndexList(String gradeId, String useMaster, ModelMap map){
		String unitId = getLoginInfo().getUnitId();
		List<NewGkReferScore> newGkReferScoreList = new ArrayList<NewGkReferScore>();
		if(StringUtils.isNotBlank(gradeId)){
			if(Objects.equals(useMaster, "1"))
				newGkReferScoreList =  newGkReferScoreService.findListByGradeIdWithMaster(getLoginInfo().getUnitId(), gradeId, false, false);
			else
				newGkReferScoreList =  newGkReferScoreService.findListByGradeId(getLoginInfo().getUnitId(), gradeId, false, false);
		}
		Set<String> refIdSet = new HashSet<String>();
		if(CollectionUtils.isNotEmpty(newGkReferScoreList)){
			refIdSet=EntityUtils.getSet(newGkReferScoreList, NewGkReferScore::getId);
			
			Map<String, Integer> stuNumMap = newGkScoreResultService.findCountByReferId(unitId, refIdSet.toArray(new String[0]));
			Map<String, Map<String, Float>> avgNum = newGkScoreResultService.findCountSubjectByReferId(unitId, refIdSet.toArray(new String[0]));
			
			Set<String> subjectIdSet = new HashSet<String>();//获取所有科目
			for (Entry<String, Map<String, Float>> item:avgNum.entrySet()) {
				Set<String> set = item.getValue().keySet();
				subjectIdSet.addAll(set);
			}
			Map<String, String> courseMap = SUtils.dt(courseRemoteService.findPartCouByIds(subjectIdSet.toArray(new String[0])),new TypeReference<Map<String, String>>(){});
			List<String[]> dataList;
			List<String[]> dataList2;
			Set<String> subSet;
			DecimalFormat df = new DecimalFormat("0.00");
			for(NewGkReferScore ref : newGkReferScoreList){
				/*if(StringUtils.isNotBlank(refId) && refId.equals(ref.getId())){
					ref.setUsed(true);
				}*/
				dataList = new ArrayList<String[]>();
				String[] stuNumArr = new String[2];//学生人数
		        stuNumArr[0] = "学生人数";
				if(stuNumMap.containsKey(ref.getId()) && stuNumMap.get(ref.getId())!=null){
			        stuNumArr[1] = String.valueOf(stuNumMap.get(ref.getId()));
				}else{
					 stuNumArr[1] ="0";
				}
				dataList.add(stuNumArr);
				String[] subNumArr = new String[2];//科目
			    subNumArr[0] = "科目";
			    subSet = new HashSet<String>();
			    dataList2 = new ArrayList<String[]>();
			    if(avgNum.containsKey(ref.getId())){
			    	Map<String, Float> avgMap2 = avgNum.get(ref.getId());
			    	for(Entry<String, Float> item2:avgMap2.entrySet()){
			    		if(!courseMap.containsKey(item2.getKey())){
			    			continue;
			    		}
			    		String[] subArr = new String[2];
			        	
			        	subArr[0] = courseMap.get(item2.getKey());
			        	subArr[1] = String.valueOf(df.format(avgMap2.get(item2.getKey())));
			        	dataList2.add(subArr);
			        	subSet.add(item2.getKey());
			    	}
			    }
			    
			    subNumArr[1] = String.valueOf(subSet.size());
			    dataList.add(subNumArr);
			    if(CollectionUtils.isNotEmpty(dataList2)){
			    	dataList.addAll(dataList2);
			    }  
				ref.setDataList(dataList);
			}
		}else{
			return "redirect:/newgkelective/newGkScoreResult/import/main?unitId="+unitId+"&gradeId="+gradeId+"&showReturn=false";
		}
		map.put("unitId", unitId);
		map.put("newGkReferScoreList", newGkReferScoreList);
		map.put("count", newGkReferScoreList.size());
		//map.put("urlType", urlType);
		map.put("gradeId", gradeId);
		return "/newgkelective/newGkScoreResult/newGkReferScoreList.ftl";
	}
	
	public List<String[]> getData(List<NewGkScoreResult> newGkScoreResultList, Map<String, String> courseMap){
		List<String[]> dataList = new ArrayList<String[]>();
		Set<String> subjectIdSet = new HashSet<String>();
		Set<String> studentIdSet = new HashSet<String>();
		for(NewGkScoreResult res : newGkScoreResultList){
			subjectIdSet.add(res.getSubjectId());
			studentIdSet.add(res.getStudentId());
		}
        String[] stuNumArr = new String[2];//学生人数
        stuNumArr[0] = "学生人数";
        stuNumArr[1] = String.valueOf(studentIdSet.size());
        dataList.add(stuNumArr);
        String[] subNumArr = new String[2];//科目
        subNumArr[0] = "科目";
        subNumArr[1] = String.valueOf(subjectIdSet.size());
        dataList.add(subNumArr);
        
        for(String subId : subjectIdSet){
        	float countScore = 0;
        	String[] subArr = new String[2];
        	for(NewGkScoreResult res : newGkScoreResultList){
        		if(subId.equals(res.getSubjectId())){
        			countScore = countScore + res.getScore();
        		}
        	}
        	float average = countScore/(studentIdSet.size());
        	DecimalFormat df = new DecimalFormat("0.00");
        	subArr[0] = courseMap.get(subId);
        	subArr[1] = String.valueOf(df.format(average));
        	dataList.add(subArr);
        }
		return dataList;
	}
	
	@ResponseBody
	@RequestMapping("/newGkScoreResult/delete")
	public String delete(String refId){
		try{
			newGkReferScoreService.deleteById(refId);
		}catch(Exception e){
			e.printStackTrace();
			return error("操作失败！"+e.getMessage());
		}
		return returnSuccess();
	}
	
	@ResponseBody
	@RequestMapping("/newGkScoreResult/saveRef")
	public String saveRef(String gradeId, String refId, String state){
		try{
			/*List<NewGkChoice> newGkChoiceList = newGkChoiceService.findListByIds(new String[]{chioceId});
			NewGkChoice newGkChoice = newGkChoiceList.get(0);*/
			NewGkReferScore newGkReferScore=newGkReferScoreService.findById(refId, false,null);
			if("1".equals(state)){
				//newGkChoice.setReferScoreId(refId);
                String cancelDefaultId=newGkReferScoreService.findDefaultIdByGradeId(newGkReferScore.getUnitId(), newGkReferScore.getGradeId());
                if (StringUtils.isNotBlank(cancelDefaultId)) {
                    NewGkReferScore cancelDefault=newGkReferScoreService.findById(cancelDefaultId, false,null);
                    cancelDefault.setIsDefault(0);
                    newGkReferScoreService.save(cancelDefault);
                }
                newGkReferScore.setIsDefault(1);
			}else{
				//newGkChoice.setReferScoreId("");
                newGkReferScore.setIsDefault(0);
			}
			//newGkChoiceService.save(newGkChoice);
            newGkReferScoreService.save(newGkReferScore);
		}catch(Exception e){
			e.printStackTrace();
			return error("操作失败！"+e.getMessage());
		}
		return returnSuccess();
	}
	
	@RequestMapping("/newGkScoreResult/showScoreHead")
	public String showScoreHead(String refId, String unitId, String gradeId, ModelMap map){
		//头部默认取行政班数据
//		List<NewGkScoreResult> newGkScoreResultList = newGkScoreResultService.findByReferScoreIds(new String[]{refId});
		NewGkReferScore refer = newGkReferScoreService.findListByIds(new String[]{refId}).get(0);
		String refName =  refer.getName();
//		Set<String> studentIdSet = new HashSet<String>();
//		for(NewGkScoreResult item : newGkScoreResultList){
//			studentIdSet.add(item.getStudentId());
//		}
//		Set<String> clsIdSet = new HashSet<String>();
//		if(CollectionUtils.isNotEmpty(studentIdSet)){
//			List<Student> studentList = SUtils.dt(studentRemoteService.findListByIds(studentIdSet.toArray(new String[0])), new TR<List<Student>>(){});
//		    for(Student stu : studentList){
//		    	clsIdSet.add(stu.getClassId());
//		    }		    
//		}

		List<Clazz> clsList2=SUtils.dt(classRemoteService.findBySchoolIdGradeId(unitId,gradeId), new TR<List<Clazz>>(){});
//		List<Clazz> clsList2 = new ArrayList<Clazz>();
//		if(CollectionUtils.isNotEmpty(clsIdSet)){
//			List<Clazz> clsList = SUtils.dt(classRemoteService.findListByIds(clsIdSet.toArray(new String[0])), new TR<List<Clazz>>(){});
//			for(Clazz cls : clsList){
//				if(gradeId.equals(cls.getGradeId())){
//					clsList2.add(cls);
//				}
//			}
//			map.put("clsList", clsList2);
//		}
		
		map.put("clsList", clsList2);
		map.put("refId", refId);
		map.put("refName", refName);
		map.put("gradeId", gradeId);
        map.put("unitId", unitId);
		return "/newgkelective/newGkScoreResult/newGkScoreResultHead.ftl";
	}

	
	@RequestMapping("/newGkScoreResult/showScoreList")
	public String showScoreList(String refId, String classId, String unitId, String gradeId, ModelMap map){
		String chioRefId = newGkReferScoreService.findDefaultIdByGradeId(unitId, gradeId);
		if(StringUtils.isNotBlank(chioRefId) && chioRefId.equals(refId)){
			map.put("used", true);
		}else{
			map.put("used", false);
		}
		//需要展示的学生数据
		Set<String> classStuIds=new HashSet<>();
		List<Student> studentList = new ArrayList<Student>();
		List<NewGkScoreResult> newGkScoreResultList=new ArrayList<>();
		if(StringUtils.isNotBlank(classId)) {
			//某个班级
			studentList = SUtils.dt(studentRemoteService.findPartStudByGradeId(unitId,null,new String[] {classId}, null), new TR<List<Student>>(){});
			if(CollectionUtils.isNotEmpty(studentList)) {
				classStuIds = EntityUtils.getSet(studentList, e->e.getId());
				newGkScoreResultList=newGkScoreResultService.findByReferScoreIdAndStudentIdIn(unitId, refId, classStuIds.toArray(new String[] {}));
			}
			
		}else {
			//全部学生
			studentList = SUtils.dt(studentRemoteService.findPartStudByGradeId(unitId,gradeId,null, null), new TR<List<Student>>(){});
			if(CollectionUtils.isNotEmpty(studentList)) {
				classStuIds = EntityUtils.getSet(studentList, e->e.getId());
			}
			newGkScoreResultList = newGkScoreResultService.findByReferScoreIds(unitId, new String[]{refId});
		}
		
		//暂时从选课结果出发 那么如果某个班级存在的某个科目都没有成绩暂时不显示
		Set<String> studentIdSet = new HashSet<String>();//所获得的学生数据基本是指所要展示的学生数据
		Set<String> courseIdSet = new HashSet<String>();
		Map<String, Float> stuScoreMap = new HashMap<String, Float>();
		for(NewGkScoreResult item : newGkScoreResultList){
			studentIdSet.add(item.getStudentId());
			courseIdSet.add(item.getSubjectId());
			stuScoreMap.put(item.getStudentId()+item.getSubjectId(), item.getScore());
		}
		Map<String, Float> stuScoreCountMap = new HashMap<String, Float>();
		for(String stuId : studentIdSet){
			float countScore = 0;
			for(NewGkScoreResult item : newGkScoreResultList){
				if(stuId.equals(item.getStudentId())){
					countScore = countScore + item.getScore();
				}
			}
			stuScoreCountMap.put(stuId, countScore);
		}
		Set<String> clsIdSet = new HashSet<String>();
		Map<String, String> stuCodeMap = new HashMap<String, String>();//学生学号
		Map<String, String> stuNameMap = new HashMap<String, String>();//学生姓名
		Map<String, Integer> stuSexMap = new HashMap<String, Integer>();//学生性别
		Map<String, String> stuClsMap = new HashMap<String, String>();//学生班级id
		for(Student stu : studentList){
	    	clsIdSet.add(stu.getClassId());
	    	stuCodeMap.put(stu.getId(), stu.getStudentCode());
	    	stuNameMap.put(stu.getId(), stu.getStudentName());
	    	stuClsMap.put(stu.getId(), stu.getClassId());
	    	stuSexMap.put(stu.getId(), stu.getSex());
	    }
//		if(CollectionUtils.isNotEmpty(studentIdSet)){
//			studentList = SUtils.dt(studentRemoteService.findPartStudByGradeId(null,null,studentIdSet.toArray(new String[0])), new TR<List<Student>>(){});
//		    for(Student stu : studentList){
//		    	clsIdSet.add(stu.getClassId());
//		    	stuCodeMap.put(stu.getId(), stu.getStudentCode());
//		    	stuNameMap.put(stu.getId(), stu.getStudentName());
//		    	stuClsMap.put(stu.getId(), stu.getClassId());
//		    	stuSexMap.put(stu.getId(), stu.getSex());
//		    }
//		}
		Map<String, String> clsNameMap = new HashMap<String, String>();//班级名称
		if(CollectionUtils.isNotEmpty(clsIdSet)){
			List<Clazz> clsList = SUtils.dt(classRemoteService.findListByIds(clsIdSet.toArray(new String[0])), new TR<List<Clazz>>(){});
			for(Clazz cls : clsList){
				clsNameMap.put(cls.getId(), cls.getClassNameDynamic());
			}
		}
		List<Course> courseList = new ArrayList<Course>();
		List<String> courseIdList = new ArrayList<String>();
		if(CollectionUtils.isNotEmpty(courseIdSet)){
			courseList = SUtils.dt(courseRemoteService.findListByIds(courseIdSet.toArray(new String[0])), new TR<List<Course>>(){});
		}
		
		// 对科目进行排序
		ArrayList<String> codeList = new ArrayList<>(Arrays.asList(BaseConstants.SUBJECT_TYPES_YSY));
		codeList.addAll(Arrays.asList(BaseConstants.SUBJECT_73));
		Collections.sort(courseList,(x,y)->{
			int xi = codeList.indexOf(x.getSubjectCode());
			if(xi == -1) {
				xi = 20;
			}
			int yi = codeList.indexOf(y.getSubjectCode());
			if(yi == -1) {
				yi = 20;
			}
			return  xi - yi;
		});
		
		for(Course course : courseList){
			courseIdList.add(course.getId());
		}
		List<String[]> dataList = new ArrayList<String[]>();
		int i = 1;
		DecimalFormat df = new DecimalFormat("0.00");
		for(Student stu : studentList){
			String[] arr = new String[6+courseIdSet.size()];
			arr[0] = String.valueOf(i);
			arr[1] = stuNameMap.get(stu.getId());
			arr[2] = stuCodeMap.get(stu.getId());
			arr[3] = stuSexMap.get(stu.getId())==1?"男":"女";
			arr[4] = clsNameMap.get(stu.getClassId());
			if(!stuScoreCountMap.containsKey(stu.getId())) {
				continue;
			}
			
			arr[5] = String.valueOf(df.format(stuScoreCountMap.get(stu.getId())));
			int p = 6;
			for(String courseId : courseIdList){
				if(null!=stuScoreMap.get(stu.getId()+courseId)){
					arr[p] = String.valueOf(stuScoreMap.get(stu.getId()+courseId));
				}else{
					arr[p] = "";
				}
				p++;
			}
			i++;
			dataList.add(arr);

//			if(StringUtils.isNotBlank(classId)){
//				if(classId.equals(stu.getClassId())){
//					String[] arr = new String[6+courseIdSet.size()];
//					arr[0] = String.valueOf(i);
//					arr[1] = stuNameMap.get(stu.getId());
//					arr[2] = stuCodeMap.get(stu.getId());
//					arr[3] = stuSexMap.get(stu.getId())==1?"男":"女";
//					arr[4] = clsNameMap.get(stu.getClassId());
//					arr[5] = String.valueOf(df.format(stuScoreCountMap.get(stu.getId())));
//					int p = 6;
//					for(String courseId : courseIdList){
//						if(null!=stuScoreMap.get(stu.getId()+courseId)){
//							arr[p] = String.valueOf(stuScoreMap.get(stu.getId()+courseId));
//						}else{
//							arr[p] = "";
//						}
//						p++;
//					}
//					i++;
//					dataList.add(arr);
//				}
//			}else{
//				String[] arr = new String[6+courseIdSet.size()];
//				arr[0] = String.valueOf(i);
//				arr[1] = stuNameMap.get(stu.getId());
//				arr[2] = stuCodeMap.get(stu.getId());
//				arr[3] = stuSexMap.get(stu.getId())==1?"男":"女";
//				arr[4] = clsNameMap.get(stu.getClassId());
//				arr[5] = String.valueOf(df.format(stuScoreCountMap.get(stu.getId())));
//				int p = 6;
//				for(String courseId : courseIdList){
//					if(null!=stuScoreMap.get(stu.getId()+courseId)){
//						arr[p] = String.valueOf(stuScoreMap.get(stu.getId()+courseId));
//					}else{
//						arr[p] = "";
//					}
//					p++;
//				}
//				i++;
//				dataList.add(arr);
//			}
		}
	    Collections.sort(dataList,new Comparator<String[]>(){
            public int compare(String[] arg0, String[] arg1) {
            	if(StringUtils.isBlank(arg0[5])){
            		arg0[5]="0";
            	}
            	if(StringUtils.isBlank(arg1[5])){
            		arg1[5]="0";
            	}
            	
            	try {
					return (int)(-Float.parseFloat(arg0[5]) + Float.parseFloat(arg1[5]));
				} catch (NumberFormatException e) {
					return 0;
				}
            }
        });
	    int m = 1;
	    for(String[] arr : dataList){
	    	arr[0] = String.valueOf(m);
	    	m++;
	    }
		map.put("courseList", courseList);
		map.put("dataList", dataList);
		map.put("num", 5+courseIdSet.size());
		map.put("gradeId", gradeId);
		map.put("refId", refId);
		map.put("classId", classId);
		map.put("count", dataList.size());
		return "/newgkelective/newGkScoreResult/newGkScoreResultList.ftl";
	}
}
