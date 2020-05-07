package net.zdsoft.stuwork.data.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import net.zdsoft.basedata.entity.Clazz;
import net.zdsoft.basedata.entity.Grade;
import net.zdsoft.basedata.entity.Student;
import net.zdsoft.basedata.remote.service.ClassRemoteService;
import net.zdsoft.basedata.remote.service.GradeRemoteService;
import net.zdsoft.basedata.remote.service.StudentRemoteService;
import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.framework.entity.Json;
import net.zdsoft.framework.entity.TR;
import net.zdsoft.framework.utils.EntityUtils;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.framework.utils.UuidUtils;
import net.zdsoft.stuwork.data.dao.DyStuHealthResultDao;
import net.zdsoft.stuwork.data.entity.DyStuHealthProjectItem;
import net.zdsoft.stuwork.data.entity.DyStuHealthResult;
import net.zdsoft.stuwork.data.service.DyStuHealthProjectItemService;
import net.zdsoft.stuwork.data.service.DyStuHealthResultService;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;

@Service("dyStuHealthResultService")
public class DyStuHealthResultServiceImpl extends BaseServiceImpl<DyStuHealthResult, String> implements DyStuHealthResultService{
	@Autowired
	private DyStuHealthResultDao dyStuHealthResultDao;
	@Autowired
    private StudentRemoteService studentRemoteService;
	@Autowired
	private DyStuHealthProjectItemService dyStuHealthProjectItemService;
	@Autowired
	private ClassRemoteService classRemoteService;
	@Autowired
	private GradeRemoteService gradeRemoteService;
	
	@Override
	public List<DyStuHealthResult> findByUidAndSe(String unitId,String acadyear,String semester){
		return dyStuHealthResultDao.findByUidAndSe(unitId, acadyear, semester);
	}
	@Override
	protected BaseJpaRepositoryDao<DyStuHealthResult, String> getJpaDao() {
		return dyStuHealthResultDao;
	}

	@Override
	protected Class<DyStuHealthResult> getEntityClass() {
		return DyStuHealthResult.class;
	}

	@Override
	public List<DyStuHealthResult> findByUnitIdAndStuId(String unitId, String studentId) {
		// TODO Auto-generated method stub
		return dyStuHealthResultDao.findByUnitIdAndStuId(unitId,studentId);
	}
	@Override
	public String doImport(String unitId,String acadyear,String semester, List<String[]> datas) {
		Json importResultJson=new Json();
		List<String[]> errorDataList=new ArrayList<String[]>();
		int successCount  =0;
		String[] errorData=null;
		
		List<Student> studentList=SUtils.dt(studentRemoteService.findBySchoolIdIn(null,new String[]{unitId}), new TR<List<Student>>(){});
		Map<String, String> scMap = EntityUtils.getMap(studentList, "id", "classId");
		//得到当前的班级的id
		List<String> clazzIds = EntityUtils.getList(studentList, "classId");
		
		List<Clazz> clazzList = new ArrayList<Clazz>();
		//超过1000的情况
		if (CollectionUtils.isNotEmpty(clazzIds)) {
            if ( clazzIds.size() > 1000 ) {
                List<String> clazzIds1 = Lists.newArrayList(clazzIds);
                int loopNumber = clazzIds1.size()/1000;
                for (int i=0; i<loopNumber; i++ ) {
                	List<Clazz> clazzs1 = SUtils.dt(classRemoteService.findListByIds(clazzIds1.subList(i * 1000, (i+1)*1000).toArray(new String[0])), Clazz.class);
                    if ( clazzs1 != null ) {
                    	clazzList.addAll(clazzs1);
                    }
                    //
                    if ( i+1 == loopNumber &&  clazzIds1.size() -(1000 * loopNumber) > 0) {
                    	List<Clazz> clazzs2 = SUtils.dt(classRemoteService.findListByIds(clazzIds1.subList((i+1) * 1000, clazzIds.size()).toArray(new String[0])), Clazz.class);
                        if ( clazzs2 != null ) {
                        	clazzList.addAll(clazzs2);
                        }
                    }
                }
            } else {
            	clazzList = SUtils.dt(classRemoteService.findListByIds(clazzIds.toArray(new String[clazzIds.size()])), Clazz.class);
            }
        }
		Map<String, Clazz> cMap = EntityUtils.getMap(clazzList, "id");
		
		//得到所有的gradeids
		List<String> gradeIds = EntityUtils.getList(clazzList, "gradeId");
		
		List<Grade> gradeList = new ArrayList<Grade>();
		//超过1000的情况
		if (CollectionUtils.isNotEmpty(gradeIds)) {
            if ( gradeIds.size() > 1000 ) {
                List<String> gradeIds1 = Lists.newArrayList(gradeIds);
                int loopNumber = gradeIds1.size()/1000;
                for (int i=0; i<loopNumber; i++ ) {
                	List<Grade> grades1 = SUtils.dt(gradeRemoteService.findListByIds(gradeIds1.subList(i * 1000, (i+1)*1000).toArray(new String[0])), Grade.class);
                    if ( grades1 != null ) {
                    	gradeList.addAll(grades1);
                    }
                    //
                    if ( i+1 == loopNumber &&  gradeIds1.size() -(1000 * loopNumber) > 0) {
                    	List<Grade> grades2 = SUtils.dt(gradeRemoteService.findListByIds(gradeIds1.subList((i+1) * 1000, gradeIds1.size()).toArray(new String[0])), Grade.class);
                        if ( grades2 != null ) {
                        	gradeList.addAll(grades2);
                        }
                    }
                }
            } else {
            	gradeList = SUtils.dt(gradeRemoteService.findListByIds(gradeIds.toArray(new String[gradeIds.size()])), Grade.class);
            }
        }
	    Map<String, String> gnMap = EntityUtils.getMap(gradeList, "id", "gradeName");
		
		List<DyStuHealthProjectItem>  listDSHPI = dyStuHealthProjectItemService.findBySemester(unitId, acadyear, semester);
		//key - student
		Map<String,Student> studentMap = EntityUtils.getMap(studentList, "studentCode"); 
		
		//得到学年学期的体检
		List<DyStuHealthResult> listDSHR = findByUidAndSe(unitId,acadyear,semester);
		Map<String,List<DyStuHealthResult>> stuHealthResultMap = EntityUtils.getListMap(listDSHR, "studentId",null);
		List<DyStuHealthResult> listDSHR1 = Lists.newArrayList();
		DyStuHealthResult dyStuHealthResult=null;
		int sequence=1;//初始行
		for(int i =0;i< datas.size();i++){
			sequence++;
			String[] dataArr = datas.get(i);
			int length=dataArr.length;
			String studentCode=length>0?dataArr[0]:"";
			if(StringUtils.isNotBlank(studentCode)){
				studentCode=studentCode.trim();
			}else{
				studentCode="";
			}
			Student	stu=studentMap.get(studentCode);
			if(stu==null){
				errorData = new String[4];
				errorData[0]=String.valueOf(sequence);
				errorData[1]="学号";
				errorData[2]=studentCode;
				errorData[3]="学号有误";
				errorDataList.add(errorData);
				continue;
			}
			String studentName=length>1?dataArr[1]:"";
			if(StringUtils.isNotBlank(studentName)){
				studentName=studentName.trim();
			}else{
				studentName="";
			}
			if(!stu.getStudentName().equals(studentName)){
				errorData = new String[4];
				errorData[0]=String.valueOf(sequence);
				errorData[1]="学生姓名";
				errorData[2]=studentName;
				errorData[3]="学生姓名有误，或该姓名和学号不匹配";
				errorDataList.add(errorData);
				continue;
			}
			for (int j = 0; j < listDSHPI.size(); j++) {
				if(length<(j+2) ||StringUtils.isBlank(dataArr[j+2])){
					errorData = new String[4];
					errorData[0]=String.valueOf(sequence);
					errorData[1]=listDSHPI.get(j).getItemName();
					errorData[2]="";
					errorData[3]=listDSHPI.get(j).getItemName()+"体检结果不能为空";
					errorDataList.add(errorData);
					break;
				}
				if(stuHealthResultMap.containsKey(stu.getId())) {
					List<DyStuHealthResult> list1 = stuHealthResultMap.get(stu.getId());
					for (DyStuHealthResult dyStuHealthResult2 : list1) {
						if(dyStuHealthResult2.getItemId().equals(listDSHPI.get(j).getItemId())) {
							dyStuHealthResult = dyStuHealthResult2;
							break;
						}else {
							dyStuHealthResult = null;
						}
					}
				}else {
					dyStuHealthResult = null;
				}
				if(dyStuHealthResult != null) {
					dyStuHealthResult.setItemResult(dataArr[j+2]);
				}else {
					dyStuHealthResult = new DyStuHealthResult();
					dyStuHealthResult.setId(UuidUtils.generateUuid());
					dyStuHealthResult.setAcadyear(acadyear);
					dyStuHealthResult.setSemester(semester);
					dyStuHealthResult.setUnitId(unitId);
					dyStuHealthResult.setStudentId(stu.getId());
					dyStuHealthResult.setItemId(listDSHPI.get(j).getItemId());
					dyStuHealthResult.setItemName(listDSHPI.get(j).getItemName());
					dyStuHealthResult.setItemUnit(listDSHPI.get(j).getItemUnit());
					dyStuHealthResult.setItemResult(dataArr[j+2]);
					dyStuHealthResult.setSex(String.valueOf(stu.getSex()));
					dyStuHealthResult.setClassName(gnMap.get(cMap.get(scMap.get(stu.getId())).getGradeId())+cMap.get(scMap.get(stu.getId())).getClassName());
					dyStuHealthResult.setOrderId(listDSHPI.get(j).getOrderId());
				}
				listDSHR1.add(dyStuHealthResult);
			}
		}
		
		//导入超过1000的情况
		if (CollectionUtils.isNotEmpty(listDSHR1)) {
            if ( listDSHR1.size() > 1000 ) {
                int loopNumber = listDSHR1.size()/1000;
                for (int i=0; i<loopNumber; i++ ) {
                	List<DyStuHealthResult> list1 = listDSHR1.subList(i * 1000, (i+1)*1000);
                    if ( list1 != null ) {
                    	DyStuHealthResult[] inserts = list1.toArray(new DyStuHealthResult[0]);
            			saveAll(inserts);
                    }
                    if ( i+1 == loopNumber &&  listDSHR1.size() -(1000 * loopNumber) > 0) {
                    	List<DyStuHealthResult> list2 = listDSHR1.subList((i+1) * 1000, listDSHR1.size());
                        if ( list2 != null ) {
                        	DyStuHealthResult[] inserts = list2.toArray(new DyStuHealthResult[0]);
                			saveAll(inserts);
                        }
                    }
                }
            } else {
            	DyStuHealthResult[] inserts = listDSHR1.toArray(new DyStuHealthResult[0]);
    			saveAll(inserts);
            }
        }
		importResultJson.put("totalCount", datas.size());
		importResultJson.put("successCount", datas.size()-errorDataList.size());
		importResultJson.put("errorCount", errorDataList.size());
		importResultJson.put("errorData", errorDataList);
		return importResultJson.toJSONString();
	}
	@Override
	public List<DyStuHealthResult> findOneByStudnetId(String unitId, String acadyear,
			String semester, String studentId) {
		return dyStuHealthResultDao.findOneByStudnetId(unitId, acadyear, semester, studentId);
	}
	@Override
	public List<DyStuHealthResult> findListByStudentIds(String unitId,
			String acadyear, String semester, String[] studentIds) {
		return dyStuHealthResultDao.findListByStudentIds(unitId, acadyear, semester, studentIds);
	}

}
