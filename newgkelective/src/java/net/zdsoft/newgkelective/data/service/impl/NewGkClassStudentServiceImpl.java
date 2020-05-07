package net.zdsoft.newgkelective.data.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.zdsoft.basedata.entity.Clazz;
import net.zdsoft.basedata.entity.Student;
import net.zdsoft.basedata.remote.service.ClassRemoteService;
import net.zdsoft.basedata.remote.service.StudentRemoteService;
import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.framework.entity.TR;
import net.zdsoft.framework.utils.EntityUtils;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.newgkelective.data.constant.NewGkElectiveConstant;
import net.zdsoft.newgkelective.data.dao.NewGkClassStudentDao;
import net.zdsoft.newgkelective.data.dao.NewGkClassStudentJdbcDao;
import net.zdsoft.newgkelective.data.dto.NewGkConditionDto;
import net.zdsoft.newgkelective.data.entity.NewGkClassStudent;
import net.zdsoft.newgkelective.data.entity.NewGkDivideClass;
import net.zdsoft.newgkelective.data.service.NewGkClassStudentService;
import net.zdsoft.newgkelective.data.service.NewGkDivideClassService;
import net.zdsoft.newgkelective.data.utils.SplitUtils;

@Service("newGkClassStudentService")
public class NewGkClassStudentServiceImpl extends BaseServiceImpl<NewGkClassStudent, String> implements NewGkClassStudentService{

	@Autowired
	private NewGkClassStudentDao newGkClassStudentDao;
	@Autowired
	private NewGkDivideClassService newGkDivideClassService;
	@Autowired
	private StudentRemoteService studentRemoteService;
	@Autowired
	private ClassRemoteService classRemoteService;
	@Autowired
	private NewGkClassStudentJdbcDao newGkClassStudentJdbcDao;
	
	
	@Override
	protected BaseJpaRepositoryDao<NewGkClassStudent, String> getJpaDao() {
		return newGkClassStudentDao;
	}

	@Override
	protected Class<NewGkClassStudent> getEntityClass() {
		return NewGkClassStudent.class;
	}
	@Override
	public List<NewGkConditionDto> findClassDetail(String divideClassId,String divideId){
		 List<NewGkConditionDto> dtoList=new ArrayList<NewGkConditionDto>();
		 //获取当前选择班级的学生信息
		 List<NewGkClassStudent> classStudentList=findListBy("classId", divideClassId);
		 Set<String> studentIds=new HashSet<String>();
		 if(CollectionUtils.isEmpty(classStudentList)){
			 return dtoList;
		 }
		 for(NewGkClassStudent classStudent:classStudentList){
			 studentIds.add(classStudent.getStudentId());
		 }
		 //获取每个学生对应的所有现属班级
		 List<NewGkDivideClass> nowDivideClassList=newGkDivideClassService.findByDivideIdAndSourceType(divideId,NewGkElectiveConstant.CLASS_SOURCE_TYPE1,true);
		 Map<String,String> divideClassNameMap=EntityUtils.getMap(nowDivideClassList, "id","className");
		 
		 //List<String> nowDivideClassIds=EntityUtils.getList(nowDivideClassList,"id");
		 List<NewGkClassStudent> nowClassStudentList=findListBy("divideId", divideId);//findListByClassIds(nowDivideClassIds.toArray(new String[0]));
		 //Key-studentId   value-现属班级
		 Map<String,String> stuIdOfClaNamesMap=new HashMap<String,String>();
		 for(NewGkClassStudent nowClassStudent:nowClassStudentList){
			 String inClassNames=stuIdOfClaNamesMap.get(nowClassStudent.getStudentId());
			 String inClassName=divideClassNameMap.get(nowClassStudent.getClassId());
			 if(StringUtils.isNotBlank(inClassName)){
				 if(StringUtils.isBlank(inClassNames)){
					 inClassNames=inClassName;
				 }else{
					 inClassNames+="、"+inClassName;
				 }
			 }
			 stuIdOfClaNamesMap.put(nowClassStudent.getStudentId(), inClassNames);
		 }
		 //查原班级
		 List<Student> studentList=SUtils.dt(studentRemoteService.findListByIds(studentIds.toArray(new String[0])),new TR<List<Student>>(){});
		 Map<String,Student> studentMap=EntityUtils.getMap(studentList, "id");
		 Set<String> classIds=new HashSet<String>();
		 if(CollectionUtils.isNotEmpty(studentList)){
			 for(Student student:studentList){
				 classIds.add(student.getClassId());
			 }
		 }
		 Map<String,String> classNameMap=EntityUtils.getMap(SUtils.dt(classRemoteService.findListByIds
				 					(classIds.toArray(new String[0])),new TR<List<Clazz>>(){}),"id","classNameDynamic");
		 Student stu=null;
		 for(NewGkClassStudent classStudent:classStudentList){
			 NewGkConditionDto dto=new NewGkConditionDto();
			 String studentId=classStudent.getStudentId(); 
			 dto.setStudentId(studentId);
			 stu=studentMap.get(studentId);
			 dto.setStudentCode(stu.getStudentCode());
			 dto.setStudentName(stu.getStudentName());
			 dto.setSex(stu.getSex());
			 dto.setOldClassName(classNameMap.get(stu.getClassId()));
			 dto.setNowClassName(stuIdOfClaNamesMap.get(studentId));
			 dtoList.add(dto);
		 }
		 return dtoList;
	}
	@Override
	public Map<String, List<String>> findMapByClassIds(String unitId,String divideId, String[] classIds) {
		if(classIds!=null && classIds.length>0){
			List<NewGkClassStudent> list = new ArrayList<NewGkClassStudent>();
			if(classIds.length>1000){
				list = SplitUtils.doSplit(Arrays.asList(classIds), e->newGkClassStudentDao.findByUnitIdAndDivideIdAndClassIdIn(unitId, divideId, e.toArray(new String[0])), 1000);
			}else{
				list = newGkClassStudentDao.findByUnitIdAndDivideIdAndClassIdIn(unitId, divideId, classIds);
			}
			if(CollectionUtils.isNotEmpty(list)){
				Map<String, List<String>> map = new HashMap<String, List<String>>();
				for(NewGkClassStudent ss:list){
					if(!map.containsKey(ss.getClassId())){
						map.put(ss.getClassId(), new ArrayList<String>());
					}
					map.get(ss.getClassId()).add(ss.getStudentId());
				}
				return map;
			}
			
		}
		return new HashMap<String, List<String>>();
	}
	
	
	@Override
	public Set<String> findSetByClassIds(String unitId,String divideId, String[] classIds){
		Set<String> studentIds=new HashSet<String>();
		if(classIds!=null && classIds.length>0){
			List<NewGkClassStudent> classStudentList=newGkClassStudentDao.findByUnitIdAndDivideIdAndClassIdIn(unitId, divideId, classIds);
			if(CollectionUtils.isNotEmpty(classStudentList)){
				for(NewGkClassStudent classStudent:classStudentList){
					studentIds.add(classStudent.getStudentId());
				}
			}
		}
		return studentIds;
	}

	@Override
	public void saveAllList(List<NewGkClassStudent> insertStudentList) {
		if(CollectionUtils.isNotEmpty(insertStudentList)){
			
			newGkClassStudentJdbcDao.insertBatch(insertStudentList);
//			newGkClassStudentDao.saveAll(checkSave(insertStudentList.toArray(new NewGkClassStudent[]{})));
		}
		
	}

	@Override
	public void deleteByClassIdIn(String unitId, String divideId, String[] delClassId) {
		if(delClassId!=null && delClassId.length>0){
			
			if(delClassId.length > 1000){
				String[] delUnitIds = new String[1000];
				int j = 0;
				for(int i=0;i<delClassId.length;i++){
					delUnitIds[j++] = delClassId[i];
					if(j >= 1000 || i >= (delClassId.length-1)){
						newGkClassStudentJdbcDao.deleteBydivideIdOrClassIds(unitId,divideId,delUnitIds, null);
						delUnitIds = new String[1000];
						if(delClassId.length - 1 - i < 1000){
							delUnitIds = new String[delClassId.length - 1 - i];
						}
						j = 0;
					}
				}
				if(j != 0){
					System.out.println("--------删除出错----------");
				}
				
			}else{
				newGkClassStudentJdbcDao.deleteBydivideIdOrClassIds(unitId,divideId,delClassId, null);
			}
		}
	}

	@Override
	public void saveOrSaveList(String unitId,
			String divideId, String[] divideClassIds, List<NewGkClassStudent> classStudentList) {
		if(divideClassIds!=null && divideClassIds.length>0){
			deleteByClassIdIn(unitId, divideId, divideClassIds);
		}
		if(CollectionUtils.isNotEmpty(classStudentList)){
			//newGkClassStudentDao.saveAll(checkSave(classStudentList.toArray(new NewGkClassStudent[0])));
			newGkClassStudentJdbcDao.insertBatch(checkSave(classStudentList.toArray(new NewGkClassStudent[0])));
		}
	}

	@Override
	public List<NewGkClassStudent> findListByStudentId(String unitId,String divideId,String studentId) {
		return newGkClassStudentDao.findListByStudentId(unitId,divideId,studentId);
	}

	@Override
	public List<NewGkClassStudent> findListByStudentIds(String unitId,String divideId,String[] studentIds) {
		if(ArrayUtils.isEmpty(studentIds)){
			return new ArrayList<NewGkClassStudent>();
		}
		if(studentIds.length<=1000){
			return newGkClassStudentDao.findListByStudentIds(unitId,divideId,studentIds);
		}else{
			List<NewGkClassStudent> list =new ArrayList<NewGkClassStudent>();
			int cyc = studentIds.length / 1000 + (studentIds.length % 1000 == 0 ? 0 : 1);
			for (int i = 0; i < cyc; i++) {
				int max = (i + 1) * 1000;
				if (max > studentIds.length)
					max = studentIds.length;
				List<NewGkClassStudent> list1 = newGkClassStudentDao.findListByStudentIds(unitId,divideId,ArrayUtils.subarray(studentIds, i * 1000, max));
				if(CollectionUtils.isNotEmpty(list1)){
					list.addAll(list1);
				}
			}
			
			return list;
		}
		
	}

	@Override
	public List<NewGkClassStudent> findListByDivideStudentId(String divideId, String[] classType,String[] studentId,String scourceType) {
		if(ArrayUtils.isEmpty(studentId)){
			return new ArrayList<NewGkClassStudent>();
		}
		if(studentId.length<=1000){
			return newGkClassStudentDao.findListByDivideStudentId(divideId,classType,studentId,scourceType);
		}else{
			List<NewGkClassStudent> list =new ArrayList<NewGkClassStudent>();
			int cyc = studentId.length / 1000 + (studentId.length % 1000 == 0 ? 0 : 1);
			for (int i = 0; i < cyc; i++) {
				int max = (i + 1) * 1000;
				if (max > studentId.length)
					max = studentId.length;
				List<NewGkClassStudent> list1 = newGkClassStudentDao.findListByDivideStudentId(divideId,classType,ArrayUtils.subarray(studentId, i * 1000, max),scourceType);
				if(CollectionUtils.isNotEmpty(list1)){
					list.addAll(list1);
				}
			}
			
			return list;
		}
		
	}

	@Override
	public void saveAndDel(String[] ids, NewGkClassStudent[] list) {
		if(ids!=null){
			newGkClassStudentJdbcDao.deleteBydivideIdOrClassIds(null,null,null, ids);
		}
		if(list!=null){
			//newGkClassStudentDao.saveAll(checkSave(list));
			newGkClassStudentJdbcDao.insertBatch(checkSave(list));
		}
	}

	@Override
	public void saveChangeClass(String leftClassSelect, String rightClassSelect, String leftAddStu, String rightAddStu) {
		Map<String,String> stuClassMap = new HashMap<String, String>();
		if(StringUtils.isNotBlank(leftAddStu)){
			String[] leftStuArr = leftAddStu.split(",");
			for (String stu : leftStuArr) {
				stuClassMap.put(stu, leftClassSelect);
			}
			
		}
		if(StringUtils.isNotBlank(rightAddStu)){
			String[] rightStuArr = rightAddStu.split(",");
			for (String stu : rightStuArr) {
				stuClassMap.put(stu, rightClassSelect);
			}
		}
		
		List<NewGkClassStudent> studentList = newGkClassStudentDao.findByStudentIdInAndClassIdIn(stuClassMap.keySet().toArray(new String[0]),new String[]{leftClassSelect,rightClassSelect});
		for (NewGkClassStudent cs : studentList) {
			if(stuClassMap.containsKey(cs.getStudentId())){
				cs.setClassId(stuClassMap.get(cs.getStudentId()));
				cs.setModifyTime(new Date());
			}
		}

		// 拆分后班级同步更改
        List<NewGkDivideClass> newGkDivideClassList = newGkDivideClassService.findByParentIds(new String[]{leftClassSelect, rightClassSelect});
        if (CollectionUtils.isNotEmpty(newGkDivideClassList)) {
            Map<String, List<String>> parentIdToSplitClassIdMap = EntityUtils.getListMap(newGkDivideClassList, NewGkDivideClass::getParentId, e -> e.getId());
            List<NewGkClassStudent> splitStudentList = newGkClassStudentDao.findByStudentIdInAndClassIdIn(stuClassMap.keySet().toArray(new String[0]), EntityUtils.getArray(newGkDivideClassList, NewGkDivideClass::getId, String[]::new));
            Map<String, List<NewGkClassStudent>> studentIdTosplitStudentList = EntityUtils.getListMap(splitStudentList, NewGkClassStudent::getStudentId, e -> e);
            for (Map.Entry<String, List<NewGkClassStudent>> one : studentIdTosplitStudentList.entrySet()) {
                List<String> splitClassIdTmp = parentIdToSplitClassIdMap.get(stuClassMap.get(one.getKey()));
                List<NewGkClassStudent> tmp = one.getValue();
                for (int i = 0; i < tmp.size(); i++) {
                    tmp.get(i).setClassId(splitClassIdTmp.get(i));
                    tmp.get(i).setModifyTime(new Date());
                }
            }
        }
    }

	@Override
	public void deleteByClassIdAndStuIdIn(String classId, String[] stuIds) {
		if(stuIds == null || stuIds.length == 0) {
			return;
		}
		newGkClassStudentJdbcDao.deleteByClassIdAndStuids(classId, stuIds);
		
	}

	@Override
	public void deleteByDivideId(String unitId, String divideId) {
		newGkClassStudentJdbcDao.deleteBydivideIdOrClassIds(unitId,divideId,null, null);
	}

    @Override
    public void deleteByStudentIds(String... s) {
        newGkClassStudentDao.deleteByStudentIdIn(s);
    }

    @Override
    public void deleteByClassIds(String... classIds) {
        newGkClassStudentDao.deleteByClassIdIn(classIds);
    }

    @Override
	public List<NewGkClassStudent> findListByClassIds(String unitId, String divideId, String[] classIds) {
		return newGkClassStudentDao.findByUnitIdAndDivideIdAndClassIdIn(unitId, divideId, classIds);
	}

	@Override
	public List<String> findArrangeStudentIdWithMaster(String divideId, String classType) {
		return newGkClassStudentJdbcDao.findArrangeStudentIdWithMaster(divideId,classType);
	}

	@Override
	public void deleteByIds(String[] ids) {
		if(ArrayUtils.isEmpty(ids)){
			return;
		}
		if(ids.length<=1000){
			newGkClassStudentDao.deleteByIdIn(ids);
		}else{
			int cyc = ids.length / 1000 + (ids.length % 1000 == 0 ? 0 : 1);
			for (int i = 0; i < cyc; i++) {
				int max = (i + 1) * 1000;
				if (max > ids.length)
					max = ids.length;
				newGkClassStudentDao.deleteByIdIn(ArrayUtils.subarray(ids, i * 1000, max));
			}
			
		}
		
	}

}
