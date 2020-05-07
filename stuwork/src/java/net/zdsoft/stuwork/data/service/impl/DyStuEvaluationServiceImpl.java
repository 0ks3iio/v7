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

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;

import net.zdsoft.basedata.entity.Clazz;
import net.zdsoft.basedata.entity.Student;
import net.zdsoft.basedata.remote.service.ClassRemoteService;
import net.zdsoft.basedata.remote.service.StudentRemoteService;
import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.framework.entity.Json;
import net.zdsoft.framework.entity.TR;
import net.zdsoft.framework.utils.EntityUtils;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.stuwork.data.constants.StuworkConstants;
import net.zdsoft.stuwork.data.dao.DyStuEvaluationDao;
import net.zdsoft.stuwork.data.dto.DyBusinessOptionDto;
import net.zdsoft.stuwork.data.dto.DyStuEvaluationDto;
import net.zdsoft.stuwork.data.entity.DyBusinessOption;
import net.zdsoft.stuwork.data.entity.DyStuEvaluation;
import net.zdsoft.stuwork.data.service.DyBusinessOptionService;
import net.zdsoft.stuwork.data.service.DyStuEvaluationService;

@Service("dyStuEvaluationService")
public class DyStuEvaluationServiceImpl extends BaseServiceImpl<DyStuEvaluation, String> implements DyStuEvaluationService{
	@Autowired
	private DyStuEvaluationDao dyStuEvaluationDao;
	@Autowired
	private StudentRemoteService studentRemoteService;
	@Autowired
	private ClassRemoteService classRemoteService;
	@Autowired
	private DyBusinessOptionService dyBusinessOptionService;

	@Override
	public DyStuEvaluation findOneByUidAndDto(String unitId,DyBusinessOptionDto dto){
		return dyStuEvaluationDao.findOneByUidAndDto(unitId, dto.getAcadyear(), dto.getSemester(), dto.getStudentId());
	}
	@Override
	public List<DyStuEvaluation> findListByUidAndDto(String unitId,DyBusinessOptionDto dto){
		List<DyStuEvaluation> evaList=new ArrayList<DyStuEvaluation>();
		//获取该班级的所有学生
		List<Student> stuList=SUtils.dt(studentRemoteService.findByClassIds(new String[]{dto.getClassId()}),new TR<List<Student>>(){});
		if(CollectionUtils.isNotEmpty(stuList)){
			Set<String> stuIds=new HashSet<String>();//学生ids
			for(Student stu:stuList){
				stuIds.add(stu.getId());
			}
			List<DyStuEvaluation> stuEvaluationList=dyStuEvaluationDao.findListByUidAndDto(unitId, dto.getAcadyear(), dto.getSemester(), stuIds.toArray(new String[0]));
			Map<String,DyStuEvaluation> evaMap=EntityUtils.getMap(stuEvaluationList,"studentId");
			for(Student stu:stuList){
				DyStuEvaluation eva=evaMap.get(stu.getId());
				if(eva==null){
					eva=new DyStuEvaluation();
					eva.setStudentId(stu.getId());
				}
				eva.setStudentName(stu.getStudentName());
				evaList.add(eva);
			}
		}
		return evaList;
	}
	@Override
	public List<DyStuEvaluationDto> findListByUidAndCid(String unitId,Clazz clazz){
		List<DyStuEvaluationDto> evaList=new ArrayList<DyStuEvaluationDto>();
		
		String classAcadyear=clazz.getAcadyear();
		int schoolingLength=clazz.getSchoolingLength();
		List<Student> stuList=SUtils.dt(studentRemoteService.findByClassIds(new String[]{clazz.getId()}),new TR<List<Student>>(){});
		if(CollectionUtils.isNotEmpty(stuList)){
			Set<String> stuIds=new HashSet<String>();//学生ids
			for(Student stu:stuList){
				stuIds.add(stu.getId());
			}
			List<DyStuEvaluation> stuEvaluationList=dyStuEvaluationDao.findListByUidAndStuids(unitId, stuIds.toArray(new String[0]));
			Map<String,String> evaMap=new HashMap<String, String>();
			if(CollectionUtils.isNotEmpty(stuEvaluationList)){
				for(DyStuEvaluation eva:stuEvaluationList){
					evaMap.put(eva.getStudentId()+eva.getAcadyear()+eva.getSemester(),eva.getGrade());
				}
			}
			String[] semesters=new String[]{"1","2"};
			int year1=0;
			int year2=0;
			if(StringUtils.isNotBlank(classAcadyear)){
				String[] years=classAcadyear.split("-");
				year1=Integer.parseInt(years[0]);
				year2=Integer.parseInt(years[1]);
			}
			for(Student stu:stuList){
				DyStuEvaluationDto eva=new DyStuEvaluationDto();
				eva.setStudentId(stu.getId());
				eva.setStudentName(stu.getStudentName());
				List<DyBusinessOptionDto> dtoList=new ArrayList<>();
				for(int i=0;i<schoolingLength;i++){
					for(String semester:semesters){
						DyBusinessOptionDto dto=new DyBusinessOptionDto();
						String optionName=evaMap.get(stu.getId()+(year1+i)+"-"+(year2+i)+semester);
						if(optionName==null) optionName="";
						dto.setOptionName(optionName);
						dto.setAcadyear((year1+i)+"-"+(year2+i));
						dto.setSemester(semester);
						dtoList.add(dto);
					}
				}
				eva.setDtoList(dtoList);
				evaList.add(eva);
			}
		}
		return evaList;
	}
	@Override
	public String doImport(String unitId, DyBusinessOptionDto dto, List<String[]> datas){
				//  值周班导入数据处理
				Json importResultJson=new Json();
				List<String[]> errorDataList=new ArrayList<String[]>();
				int successCount  =0;
				String[] errorData=null;
				
				String classId=dto.getClassId();
				
				Clazz clazz=SUtils.dc(classRemoteService.findOneById(classId),Clazz.class);
				String className=clazz==null?"":clazz.getClassNameDynamic();
				//获取学生list
				List<Student> stuList=SUtils.dt(studentRemoteService.findByClassIds(new String[]{dto.getClassId()}),new TR<List<Student>>(){});
				Map<String,Student> stuNameMap=EntityUtils.getMap(stuList, "studentName");
				//获取等第list
				List<DyBusinessOption> boptionList=dyBusinessOptionService.findListByUnitIdAndType(unitId, StuworkConstants.BUSINESS_TYPE_2);
				Map<String,String> boptionMap=EntityUtils.getMap(boptionList, "optionName","id");
				
				Set<String> stuIds=new HashSet<String>();//学生ids
				for(Student stu:stuList){
					stuIds.add(stu.getId());
				}
				//获取该学年学期班级下的各个学生评语情况
				List<DyStuEvaluation> stuEvaluationList=dyStuEvaluationDao.findListByUidAndDto(unitId, dto.getAcadyear(), dto.getSemester(), stuIds.toArray(new String[0]));
				Map<String,DyStuEvaluation> evaMap=EntityUtils.getMap(stuEvaluationList, "studentId");
				
				List<DyStuEvaluation> insertList=new ArrayList<DyStuEvaluation>();
				Set<DyStuEvaluation> deleteIds=new HashSet<DyStuEvaluation>();
				DyStuEvaluation eva=null;
				Map<String,String> stuTestMap=new HashMap<String, String>();//判断是否有学生重复 已学号
				for(int i =0;i< datas.size();i++){
					String[] dataArr = datas.get(i);
					int length=dataArr.length;
					String stuName=length>0?dataArr[0]:"";
					if(StringUtils.isNotBlank(stuName)){
						stuName=stuName.trim();
					}else{
						stuName="";
					}
					Student stu=stuNameMap.get(stuName);
					if(stu==null){
						errorData = new String[4];
						errorData[0]=errorDataList.size()+1+"";
						errorData[1]="姓名";
						errorData[2]=stuName;
						errorData[3]="学生姓名有误,或不在"+className+"下";
						errorDataList.add(errorData);
						continue;
					}
					String stuCode=length>1?dataArr[1]:"";
					if(StringUtils.isNotBlank(stuCode)){
						stuCode=stuCode.trim();
					}else{
						stuCode="";
					}
					if(stu.getStudentCode()==null){
						stu.setStudentCode("");
					}
					if(!stu.getStudentCode().equals(stuCode)){
						errorData = new String[4];
						errorData[0]=errorDataList.size()+1+"";
						errorData[1]="学号";
						errorData[2]=stuCode;
						errorData[3]="学号有误，或与该学生姓名不匹配";
						errorDataList.add(errorData);
						continue;
					}
					if(StringUtils.isNotBlank(stuTestMap.get(stuCode))){
						errorData = new String[4];
						errorData[0]=errorDataList.size()+1+"";
						errorData[1]="学号";
						errorData[2]=stuCode;
						errorData[3]="学号重复";
						errorDataList.add(errorData);
						continue;
					}
					stuTestMap.put(stuCode, "one");
					
					String optionName=length>2?dataArr[2]:"";
					if(StringUtils.isNotBlank(optionName)){
						optionName=optionName.trim();
					}else{
						optionName="";
					}
					String oId=boptionMap.get(optionName);
					if(StringUtils.isBlank(oId)){
						errorData = new String[4];
						errorData[0]=errorDataList.size() +1 +"";
						errorData[1]="操作等第";
						errorData[2]=optionName;
						errorData[3]="操作等第有误";
						errorDataList.add(errorData);
						continue;
					}
					String remark=length>3?remark=dataArr[3]:"";
					if(StringUtils.isNotBlank(remark)){
						if(remark.length()>1000){
							errorData = new String[4];
							errorData[0]=errorDataList.size()+1+"";
							errorData[1]="期末评语";
							errorData[2]=remark;
							errorData[3]="期末评语不能超过1000个字符";
							errorDataList.add(errorData);
							continue;
						}
					}
					String association=length>4?dataArr[4]:"";
					if(StringUtils.isNotBlank(association)){
						if(association.length()>1000){
							errorData = new String[4];
							errorData[0]=errorDataList.size()+1+"";
							errorData[1]="社团工作";
							errorData[2]=association;
							errorData[3]="社团工作不能超过1000个字符";
							errorDataList.add(errorData);
							continue;
						}
					}
					eva=new DyStuEvaluation();
					eva.setUnitId(unitId);
					eva.setAcadyear(dto.getAcadyear());
					eva.setSemester(dto.getSemester());
					eva.setStudentId(stu.getId());
					eva.setGradeId(oId);
					eva.setGrade(optionName);
					eva.setRemark(remark);
					eva.setAssociation(association);
					insertList.add(eva);
					
					DyStuEvaluation deleteEva=evaMap.get(stu.getId());
					if(deleteEva!=null){
						deleteIds.add(deleteEva);
					}
					successCount++;
				}
				if(CollectionUtils.isNotEmpty(deleteIds)){
					deleteAll(deleteIds.toArray(new DyStuEvaluation[0]));
				}
				if(CollectionUtils.isNotEmpty(insertList)){
					DyStuEvaluation[] inserts = insertList.toArray(new DyStuEvaluation[0]);
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
	protected BaseJpaRepositoryDao<DyStuEvaluation, String> getJpaDao() {
		return dyStuEvaluationDao;
	}

	@Override
	protected Class<DyStuEvaluation> getEntityClass() {
		return DyStuEvaluation.class;
	}
	@Override
	public List<DyStuEvaluation> findByStudentId(String studentId) {
		return dyStuEvaluationDao.findByStudentId(studentId);
	}
	
	@Override
	public List<DyStuEvaluation> findByStudentIdIn(final String[] studentIds) {
		Specification<DyStuEvaluation> specification = new Specification<DyStuEvaluation>() {
			@Override
			public Predicate toPredicate(Root<DyStuEvaluation> root,
					CriteriaQuery<?> cq, CriteriaBuilder cb) {
				List<Predicate> ps = Lists.newArrayList();
                if(null!=studentIds && studentIds.length>0){
                	queryIn("studentId", studentIds, root, ps, null);  				
                }
                return cb.or(ps.toArray(new Predicate[0]));
            }			
		};
        return dyStuEvaluationDao.findAll(specification);
	}
	@Override
	public List<DyStuEvaluation> findByStudentIdIn(String[] studentIds,String unitId,String acadyear,String semester){
		if(studentIds!=null && studentIds.length>0){
			return dyStuEvaluationDao.findListByUidAndDto(unitId, acadyear, semester, studentIds);
		}
		return dyStuEvaluationDao.findListByUidAndAcad(unitId, acadyear, semester);
	}
	@Override
	public List<DyStuEvaluation> findByUnitIdAndGradeIds(String unitId, String[] gradeIds) {
		if(StringUtils.isBlank(unitId) || ArrayUtils.isEmpty(gradeIds)){
			return new ArrayList<DyStuEvaluation>();
		}
		return dyStuEvaluationDao.findByUnitIdAndGradeIdIn(unitId, gradeIds);
	}

}
