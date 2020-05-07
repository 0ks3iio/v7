package net.zdsoft.stuwork.data.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import net.zdsoft.basedata.entity.Student;
import net.zdsoft.basedata.remote.service.SemesterRemoteService;
import net.zdsoft.basedata.remote.service.StudentRemoteService;
import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.framework.entity.Json;
import net.zdsoft.framework.entity.TR;
import net.zdsoft.framework.utils.EntityUtils;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.framework.utils.UuidUtils;
import net.zdsoft.stuwork.data.constants.StuworkConstants;
import net.zdsoft.stuwork.data.dao.DyStuStudyingFarmingDao;
import net.zdsoft.stuwork.data.entity.DyBusinessOption;
import net.zdsoft.stuwork.data.entity.DyStuStudyingFarming;
import net.zdsoft.stuwork.data.service.DyBusinessOptionService;
import net.zdsoft.stuwork.data.service.DyStuStudyingFarmingService;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;
@Service("dyStuStudyingFarmingService")
public class DyStuStudyingFarmingServiceImpl extends BaseServiceImpl<DyStuStudyingFarming, String> implements DyStuStudyingFarmingService{
	
    @Autowired
	private DyStuStudyingFarmingDao dyStuStudyingFarmingDao;
    @Autowired
    private DyBusinessOptionService dyBusinessOptionService;
    @Autowired
    private StudentRemoteService studentRemoteService;
    @Autowired
    private SemesterRemoteService semesterRemoteService;
	
	@Override
	protected BaseJpaRepositoryDao<DyStuStudyingFarming, String> getJpaDao() {
		return dyStuStudyingFarmingDao;
	}

	@Override
	protected Class<DyStuStudyingFarming> getEntityClass() {
		return DyStuStudyingFarming.class;
	}

	@Override
	public List<DyStuStudyingFarming> findByUnitIdAndGradeIds(String unitId, String[] gradeIds) {
		if(StringUtils.isBlank(unitId) || ArrayUtils.isEmpty(gradeIds)){
			return new ArrayList<DyStuStudyingFarming>();
		}
		return dyStuStudyingFarmingDao.findByUnitIdAndGradeIdIn(unitId, gradeIds);
	}
	public List<DyStuStudyingFarming>  findByUnitIdAndStuIds(String unitId, String acadyear, String semester,String[] studentIds){
		if(ArrayUtils.isEmpty(studentIds)){
			return dyStuStudyingFarmingDao.findByUnitIdAnd(unitId, acadyear, semester);
		}
		return dyStuStudyingFarmingDao.findByUnitIdAndStuIdIn(unitId, acadyear, semester, studentIds);
	}
	@Override
	public List<DyStuStudyingFarming> findByStudentIds(String[] studentIds){
		return dyStuStudyingFarmingDao.findByStudentIdIn(studentIds);
	}
	@Override
	public List<DyStuStudyingFarming> findByUnitIdAndStudentIds(String unitId, String[] studentIds) {
		if(StringUtils.isBlank(unitId) || ArrayUtils.isEmpty(studentIds)){
			return new ArrayList<DyStuStudyingFarming>();
		}
		Specification<DyStuStudyingFarming> specification = new Specification<DyStuStudyingFarming>() {
			private static final long serialVersionUID = 1L;

			@Override
			public Predicate toPredicate(Root<DyStuStudyingFarming> root,
					CriteriaQuery<?> cq, CriteriaBuilder cb) {
				List<Predicate> ps = Lists.newArrayList();
            	queryIn("studentId", studentIds, root, ps, null);  				
                return cb.or(ps.toArray(new Predicate[0]));
            }			
		};
		return dyStuStudyingFarmingDao.findAll(specification);
	}

	@Override
	public void saveList(String unitId, String classId, List<DyStuStudyingFarming> dyStuStudyingFarmingList) {
		List<DyBusinessOption> dyBusinessOptionList = dyBusinessOptionService.findListByUnitIdAndType(unitId, StuworkConstants.BUSINESS_TYPE_5);
		Map<String, DyBusinessOption> dyBusinessOptionMap = EntityUtils.getMap(dyBusinessOptionList, DyBusinessOption::getId);
		Set<String> studentIdSet = new HashSet<String>();
		for(DyStuStudyingFarming item : dyStuStudyingFarmingList){
			item.setId(UuidUtils.generateUuid());
			item.setUnitId(unitId);
			if(StringUtils.isNotBlank(item.getGradeId())){
				DyBusinessOption option = dyBusinessOptionMap.get(item.getGradeId());
				if(option!=null){
					item.setGrade(option.getOptionName());
					item.setScore(option.getScore());
				}
			}
			studentIdSet.add(item.getStudentId());
		}
		dyStuStudyingFarmingDao.deleteByUnitIdAndStudentIdIn(unitId, studentIdSet.toArray(new String[0]));
		dyStuStudyingFarmingDao.saveAll(dyStuStudyingFarmingList);	
	}

	@Override
	public String saveImport(String unitId, List<String[]> datas) {
		
		int successCount  =0;
		String[] errorData = null;
		List<String[]> errorDataList=new ArrayList<String[]>();
		
		if(CollectionUtils.isEmpty(datas)){
			errorData=new String[4];
			errorData[0]="1";
			errorData[1]="";
			errorData[2]="";
			errorData[3]="没有导入数据";
			errorDataList.add(errorData);
			return result(0,0,0,errorDataList);
		}
		int totalCount = datas.size();
		
		List<DyBusinessOption> dyBusinessOptionList = dyBusinessOptionService.findListByUnitIdAndType(unitId, StuworkConstants.BUSINESS_TYPE_5);
		if(CollectionUtils.isEmpty(dyBusinessOptionList)){
			errorData=new String[4];
            errorData[0]="1";
            errorData[1]="";
            errorData[2]="";
            errorData[3]="没有设置等第";
            errorDataList.add(errorData);
            return result(totalCount,0,totalCount,errorDataList);
		}
		Map<String, DyBusinessOption> optionMap = new HashMap<String, DyBusinessOption>();
		for(DyBusinessOption item : dyBusinessOptionList){
			optionMap.put(item.getOptionName(), item);
		}
		
		Set<String> stuCodeSet = new HashSet<String>();
		List<DyStuStudyingFarming> insertList=new ArrayList<DyStuStudyingFarming>();
		for(String[] arr : datas){
			if(StringUtils.isNotBlank(arr[1])){
				stuCodeSet.add(arr[1].trim());
			}
        }
		List<Student> stuList = SUtils.dt(studentRemoteService.findBySchIdStudentCodes(unitId, stuCodeSet.toArray(new String[0])), new TR<List<Student>>() {});
		Map<String, Student> stuMap = new HashMap<String, Student>();
		for (Student stu : stuList) {
			stuMap.put(stu.getStudentCode(), stu);
		}

		Student stu = null;
		DyStuStudyingFarming studyFarming = null;
		Set<String> stuIdSet = new HashSet<String>();
		for(String[] arr : datas){
			String stuName = arr[0]==null?"":arr[0].trim();
			String stuCode = arr[1]==null?"":arr[1].trim();
			String grade = arr[2]==null?"":arr[2].trim();
			String acadyear = arr[3]==null?"":arr[3].trim();
			String semester = arr[4]==null?"":arr[4].trim();
			String remark = arr[5]==null?"":arr[5].trim();
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
        		if(stuIdSet.contains(stu.getId())){
        			errorData = new String[4];
    				errorData[0]=errorDataList.size()+1+"";
    				errorData[1]="学号";
    				errorData[2]=stuCode;
    				errorData[3]="学号重复";
    				errorDataList.add(errorData);
    				continue;
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
			if(StringUtils.isNotBlank(acadyear)){
				List<String> acadyearList = SUtils.dt(semesterRemoteService.findAcadeyearList(), String.class);
				if(!acadyearList.contains(acadyear)){
					errorData = new String[4];
					errorData[0]=errorDataList.size()+1+"";
					errorData[1]="学年";
					errorData[2]=acadyear;
					errorData[3]="请填写正确的学年";
					errorDataList.add(errorData);
					continue;
				}
				if(StringUtils.isBlank(semester)){
					errorData = new String[4];
					errorData[0]=errorDataList.size()+1+"";
					errorData[1]="学期";
					errorData[2]=semester;
					errorData[3]="学年学期需同时填写";
					errorDataList.add(errorData);
					continue;
				}
			}
			
			if(StringUtils.isNotBlank(semester)){
				if("第一学期".equals(semester)){
					semester="1";
				}else if("第二学期".equals(semester)){
					semester="2";
				}else{
					errorData = new String[4];
					errorData[0]=errorDataList.size()+1+"";
					errorData[1]="学期";
					errorData[2]=semester;
					errorData[3]="请填写正确的学期";
					errorDataList.add(errorData);
					continue;
				}
				if(StringUtils.isBlank(acadyear)){
					errorData = new String[4];
					errorData[0]=errorDataList.size()+1+"";
					errorData[1]="学年";
					errorData[2]=acadyear;
					errorData[3]="学年学期需同时填写";
					errorDataList.add(errorData);
					continue;
				}
			}
			
			studyFarming = new DyStuStudyingFarming();
			studyFarming.setId(UuidUtils.generateUuid());
			studyFarming.setStudentId(stu.getId());
			studyFarming.setUnitId(unitId);
			studyFarming.setGradeId(option.getId());
			studyFarming.setGrade(option.getOptionName());
			studyFarming.setScore(option.getScore());
			studyFarming.setAcadyear(acadyear);
			studyFarming.setSemester(semester);
			studyFarming.setRemark(remark);
			insertList.add(studyFarming);
			stuIdSet.add(stu.getId());
        	successCount++;
		}		
		if(CollectionUtils.isNotEmpty(insertList)){
			dyStuStudyingFarmingDao.deleteByUnitIdAndStudentIdIn(unitId, stuIdSet.toArray(new String[stuIdSet.size()]));
			dyStuStudyingFarmingDao.saveAll(insertList);
		}
		return result(totalCount, successCount, errorDataList.size(), errorDataList);
	}
	
	private String result(int totalCount ,int successCount , int errorCount ,List<String[]> errorDataList){
        Json importResultJson=new Json();
        importResultJson.put("totalCount", totalCount);
        importResultJson.put("successCount", successCount);
        importResultJson.put("errorCount", errorCount);
        importResultJson.put("errorData", errorDataList);
        return importResultJson.toJSONString();
	}
	
	

}
