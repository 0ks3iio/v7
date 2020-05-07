package net.zdsoft.stuwork.data.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import net.zdsoft.basedata.entity.DateInfo;
import net.zdsoft.basedata.entity.Student;
import net.zdsoft.basedata.remote.service.DateInfoRemoteService;
import net.zdsoft.basedata.remote.service.StudentRemoteService;
import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.framework.entity.Json;
import net.zdsoft.framework.entity.TR;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.framework.utils.UuidUtils;
import net.zdsoft.stuwork.data.constants.StuworkConstants;
import net.zdsoft.stuwork.data.dao.DyStuWeekCheckPerformanceDao;
import net.zdsoft.stuwork.data.entity.DyBusinessOption;
import net.zdsoft.stuwork.data.entity.DyStuPunishment;
import net.zdsoft.stuwork.data.entity.DyStuWeekCheckPerformance;
import net.zdsoft.stuwork.data.service.DyBusinessOptionService;
import net.zdsoft.stuwork.data.service.DyStuWeekCheckPerformanceService;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
@Service("dyStuWeekCheckPerformanceService")
public class DyStuWeekCheckPerformanceServiceImpl extends BaseServiceImpl<DyStuWeekCheckPerformance, String> implements DyStuWeekCheckPerformanceService{
    @Autowired
	private DyStuWeekCheckPerformanceDao dyStuWeekCheckPerformanceDao;
    @Autowired
    private DyBusinessOptionService dyBusinessOptionService;
	@Override
	protected BaseJpaRepositoryDao<DyStuWeekCheckPerformance, String> getJpaDao() {
		return dyStuWeekCheckPerformanceDao;
	}
	@Autowired
	private StudentRemoteService studentRemoteService;
	@Autowired
	private DateInfoRemoteService dateInfoRemoteService;

	@Override
	protected Class<DyStuWeekCheckPerformance> getEntityClass() {
		return DyStuWeekCheckPerformance.class;
	}

	@Override
	public List<DyStuWeekCheckPerformance> findByStudentIds(String unitId, String acadyear, String semester, String week, String[] studentIds) {
		if(StringUtils.isBlank(week)){
			List<DyStuWeekCheckPerformance> list=new ArrayList<>();
			if(studentIds!=null && studentIds.length>0) {
				if(studentIds.length<=1000) {
					return dyStuWeekCheckPerformanceDao.findByStudentIds2(unitId, acadyear, semester, studentIds);
				}else {
					int cyc = studentIds.length / 1000 + (studentIds.length % 1000 == 0 ? 0 : 1);
					for (int i = 0; i < cyc; i++) {
						int max = (i + 1) * 1000;
						if (max > studentIds.length)
							max = studentIds.length;
						List<DyStuWeekCheckPerformance> list1 = dyStuWeekCheckPerformanceDao.findByStudentIds2(unitId, acadyear, semester, ArrayUtils.subarray(studentIds, i * 1000, max));
						if(CollectionUtils.isNotEmpty(list1)) {
							list.addAll(list1);
						}
					}
				}
			}
			return list;
			//return dyStuWeekCheckPerformanceDao.findByStudentIds2(unitId, acadyear, semester, studentIds);
		}
		List<DyStuWeekCheckPerformance> list=new ArrayList<>();
		if(studentIds!=null && studentIds.length>0) {
			if(studentIds.length<=1000) {
				return dyStuWeekCheckPerformanceDao.findByStudentIds(unitId, acadyear, semester, Integer.parseInt(week), studentIds);
			}else {
				int cyc = studentIds.length / 1000 + (studentIds.length % 1000 == 0 ? 0 : 1);
				for (int i = 0; i < cyc; i++) {
					int max = (i + 1) * 1000;
					if (max > studentIds.length)
						max = studentIds.length;
					List<DyStuWeekCheckPerformance> list1 = dyStuWeekCheckPerformanceDao.findByStudentIds(unitId, acadyear, semester, Integer.parseInt(week), ArrayUtils.subarray(studentIds, i * 1000, max));
					if(CollectionUtils.isNotEmpty(list1)) {
						list.addAll(list1);
					}
				}
			}
		}
		return list;
		//return dyStuWeekCheckPerformanceDao.findByStudentIds(unitId, acadyear, semester, Integer.parseInt(week), studentIds);
	}

	@Override
	public void saveList(String unitId, String acadyear, String semester,
			String week,
			List<DyStuWeekCheckPerformance> dyStuWeekCheckPerformanceList) {
		List<DyBusinessOption> dyBusinessOptionList = dyBusinessOptionService.findListByUnitIdAndType(unitId, StuworkConstants.BUSINESS_TYPE_4);
		Map<String, String> dyBusinessOptionNameMap = new HashMap<String, String>();
		Map<String, String> dyBusinessOptionScoreMap = new HashMap<String, String>();
		for(DyBusinessOption item : dyBusinessOptionList){
			dyBusinessOptionNameMap.put(item.getId(), item.getOptionName());
			dyBusinessOptionScoreMap.put(item.getId(), String.valueOf(item.getScore()));
		}
		Set<String> studentIdSet = new HashSet<String>();
		for(DyStuWeekCheckPerformance item : dyStuWeekCheckPerformanceList){
			item.setId(UuidUtils.generateUuid());
			item.setUnitId(unitId);
			item.setAcadyear(acadyear);
			item.setSemester(semester);
			//item.setWeek(Integer.parseInt(week));
			if(StringUtils.isNotBlank(item.getGradeId())){
				item.setGrade(dyBusinessOptionNameMap.get(item.getGradeId()));
				item.setScore(Float.parseFloat(dyBusinessOptionScoreMap.get(item.getGradeId())));
			}
			studentIdSet.add(item.getStudentId());
		}
		//dyStuWeekCheckPerformanceDao.deleteByStudentIds(acadyear, semester, Integer.parseInt(week), studentIdSet.toArray(new String[0]));
		dyStuWeekCheckPerformanceDao.saveAll(dyStuWeekCheckPerformanceList);		
	}

	@Override
	public String doImport(String unitId, List<String[]> datas, String acadyear, String semester, String week) {
 
		//week其实没用
		Json importResultJson=new Json();
		List<String[]> errorDataList=new ArrayList<String[]>();
		int successCount  =0;
		String[] errorData=null;
		Set<String> stuCodeSet = new HashSet<String>();
		List<DyStuWeekCheckPerformance> insertList=new ArrayList<DyStuWeekCheckPerformance>();
		for(String[] arr : datas){
			if (StringUtils.isNotBlank(arr[1])) {
				stuCodeSet.add(arr[1].trim());
			}
		}
		List<Student> stuList = SUtils.dt(studentRemoteService.findBySchIdStudentCodes(unitId, stuCodeSet.toArray(new String[0])), new TR<List<Student>>() {});
        List<DyBusinessOption> dyBusinessOptionList = dyBusinessOptionService.findListByUnitIdAndType(unitId, StuworkConstants.BUSINESS_TYPE_4);
        Map<String, DyBusinessOption> optionMap = new HashMap<String, DyBusinessOption>();
		for(DyBusinessOption item : dyBusinessOptionList){
			optionMap.put(item.getOptionName(), item);
		}
        Map<String, Student> stuMap = new HashMap<String, Student>();
		for(Student stu : stuList){
			stuMap.put(stu.getStudentCode(), stu);
        }

		Pattern pattern = Pattern.compile("^[0-9]*[1-9][0-9]*$");
		List<DateInfo> dateInfoList = SUtils.dt(dateInfoRemoteService.findByAcadyearAndSemester(unitId, acadyear, Integer.valueOf(semester)), DateInfo.class);
		Integer maxWeek = dateInfoList.stream().map(e -> e.getWeek()).max(Integer::compareTo).orElse(Integer.valueOf(99));
		DyStuWeekCheckPerformance dscp = null;
		Student stu = null;
		Set<String> stuIdSet = new HashSet<String>();
		Set<String> stuIdWeekSet = new HashSet<String>();
		Set<Integer> weekSet = new HashSet<Integer>();
		for(String[] arr : datas){
			String stuName = arr[0]==null?"":arr[0].trim();
			String stuCode = arr[1]==null?"":arr[1].trim();
			String grade = arr[2]==null?"":arr[2].trim();
			String remark = arr[3]==null?"":arr[3].trim();
			String weekStr = arr[4]==null?"":arr[4].trim();
			if(StringUtils.isBlank(stuName)){
        		errorData = new String[4];
				errorData[0]=errorDataList.size()+1+"";
				errorData[1]="学生姓名";
				errorData[2]="";
				errorData[3]="学生姓名不能为空";
				errorDataList.add(errorData);
				continue;
        	}
			if(StringUtils.isBlank(stuCode)){
        		errorData = new String[4];
				errorData[0]=errorDataList.size()+1+"";
				errorData[1]="学号";
				errorData[2]="";
				errorData[3]="学号不能为空";
				errorDataList.add(errorData);
				continue;
        	}else{
        		stu = stuMap.get(stuCode);
        		if(stu==null){
        			errorData = new String[4];
    				errorData[0]=errorDataList.size()+1+"";
    				errorData[1]="学号";
    				errorData[2]=stuCode;
    				errorData[3]="不存在该学号所属的学生";
    				errorDataList.add(errorData);
    				continue;
        		}else{
        			if(!stuName.equals(stu.getStudentName())){
        				errorData = new String[4];
        				errorData[0]=errorDataList.size()+1+"";
        				errorData[1]="学号";
        				errorData[2]="姓名："+stuName+"&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;学号："+stuCode;
        				errorData[3]="学生姓名与该学号不匹配";
        				errorDataList.add(errorData);
        				continue;
        			}
        		}
        	}
			if(StringUtils.isBlank(grade)){
				errorData = new String[4];
				errorData[0]=errorDataList.size()+1+"";
				errorData[1]="等第";
				errorData[2]="";
				errorData[3]="等第不能为空";
				errorDataList.add(errorData);
				continue;
			}
			DyBusinessOption option = optionMap.get(grade);
    		if(option==null){
    			errorData = new String[4];
				errorData[0]=errorDataList.size()+1+"";
				errorData[1]="等第";
				errorData[2]=grade;
				errorData[3]="不存在对应的等第";
				errorDataList.add(errorData);
				continue;
    		}
			if (StringUtils.isBlank(weekStr)) {
				errorData = new String[4];
				errorData[0]=errorDataList.size()+1+"";
				errorData[1]="周次";
				errorData[2]=weekStr;
				errorData[3]="周次不能为空";
				errorDataList.add(errorData);
				continue;
			}
			if (!pattern.matcher(weekStr).matches()) {
				errorData = new String[4];
				errorData[0]=errorDataList.size()+1+"";
				errorData[1]="周次";
				errorData[2]=weekStr;
				errorData[3]="周次只能为正整数";
				errorDataList.add(errorData);
				continue;
			}
			if (Integer.valueOf(weekStr) > maxWeek) {
				errorData = new String[4];
				errorData[0]=errorDataList.size()+1+"";
				errorData[1]="周次";
				errorData[2]=weekStr;
				errorData[3]="周次上限为" + maxWeek;
				errorDataList.add(errorData);
				continue;
			}
			if(stuIdWeekSet.contains(stu.getId()+"-"+weekStr)){
    			errorData = new String[4];
				errorData[0]=errorDataList.size()+1+"";
				errorData[1]="周次";
				errorData[2]="学号："+stuCode+"&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;周次："+weekStr;
				errorData[3]="学号对应的周次重复";
				errorDataList.add(errorData);
				continue;
    		}
			dscp = new DyStuWeekCheckPerformance();
			dscp.setStudentId(stu.getId());
			dscp.setUnitId(unitId);
			dscp.setGradeId(option.getId());
			dscp.setGrade(option.getOptionName());
			dscp.setScore(option.getScore());
			dscp.setRemark(remark);
			dscp.setAcadyear(acadyear);
			dscp.setSemester(semester);
			dscp.setWeek(Integer.parseInt(weekStr));
			insertList.add(dscp);
			stuIdSet.add(stu.getId());
			weekSet.add(Integer.parseInt(weekStr));
			stuIdWeekSet.add(stu.getId()+"-"+weekStr);
        	successCount++;
		}		
		if(CollectionUtils.isNotEmpty(insertList)){
			List<DyStuWeekCheckPerformance> dscpList = dyStuWeekCheckPerformanceDao.findByStudentIds(unitId, acadyear, semester, weekSet.toArray(new Integer[0]), stuIdSet.toArray(new String[0]));
			dscpList = dscpList.stream().filter(e->stuIdWeekSet.contains(e.getStudentId()+"-"+e.getWeek())).collect(Collectors.toList());
			if(CollectionUtils.isNotEmpty(dscpList)){
				deleteAll(dscpList.toArray(new DyStuWeekCheckPerformance[0]));
			}
			DyStuWeekCheckPerformance[] inserts = insertList.toArray(new DyStuWeekCheckPerformance[0]);
			checkSave(inserts);
			saveAll(inserts);
		}
		importResultJson.put("totalCount", datas.size());
		importResultJson.put("successCount", successCount);
		importResultJson.put("errorCount", errorDataList.size());
		importResultJson.put("errorData", errorDataList);
		return importResultJson.toJSONString();
	}

	@Override
	public List<DyStuWeekCheckPerformance> findByStudentId(String studentId) {
		return dyStuWeekCheckPerformanceDao.findByStudentId(studentId);
	}

	@Override
	public List<DyStuWeekCheckPerformance> findByStudentIdIn(final String[] studentIds) {

		/*Specification<DyStuWeekCheckPerformance> specification = new Specification<DyStuWeekCheckPerformance>() {
			@Override
			public Predicate toPredicate(Root<DyStuWeekCheckPerformance> root,
					CriteriaQuery<?> cq, CriteriaBuilder cb) {
				List<Predicate> ps = Lists.newArrayList();
                if(null!=studentIds && studentIds.length>0){
                	queryIn("studentId", studentIds, root, ps, null);  				
                }
                return cb.or(ps.toArray(new Predicate[0]));
            }			
		};
        return dyStuWeekCheckPerformanceDao.findAll(specification);*/
        
        List<DyStuWeekCheckPerformance> list=new ArrayList<>();
		if(studentIds!=null && studentIds.length>0) {
			if(studentIds.length<=1000) {
				return dyStuWeekCheckPerformanceDao.findByStudentIds(studentIds);
			}else {
				int cyc = studentIds.length / 1000 + (studentIds.length % 1000 == 0 ? 0 : 1);
				for (int i = 0; i < cyc; i++) {
					int max = (i + 1) * 1000;
					if (max > studentIds.length)
						max = studentIds.length;
					List<DyStuWeekCheckPerformance> list1 = dyStuWeekCheckPerformanceDao.findByStudentIds(ArrayUtils.subarray(studentIds, i * 1000, max));
					if(CollectionUtils.isNotEmpty(list1)) {
						list.addAll(list1);
					}
				}
			}
		}
		return list;
	}

	@Override
	public List<DyStuWeekCheckPerformance> findByUnitIdAndGradeIds(String unitId, String[] gradeIds) {
		if(StringUtils.isBlank(unitId) || ArrayUtils.isEmpty(gradeIds)){
			return new ArrayList<DyStuWeekCheckPerformance>();
		}
		return dyStuWeekCheckPerformanceDao.findByUnitIdAndGradeIdIn(unitId, gradeIds);
	}
	@Override
	public List<DyStuWeekCheckPerformance> findByUnitIdAndStuIds(String unitId, String acadyear, String semester,String[] studentIds){
		if(ArrayUtils.isEmpty(studentIds)){
			return dyStuWeekCheckPerformanceDao.findByUnitIdAnd(unitId, acadyear, semester);
		}
		return dyStuWeekCheckPerformanceDao.findByStudentIds(unitId, acadyear, semester, studentIds);
	}
}
