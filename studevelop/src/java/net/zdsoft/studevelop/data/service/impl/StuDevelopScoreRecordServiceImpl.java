package net.zdsoft.studevelop.data.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.zdsoft.basedata.entity.Student;
import net.zdsoft.basedata.remote.service.StudentRemoteService;
import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.framework.entity.Json;
import net.zdsoft.framework.entity.TR;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.framework.utils.UuidUtils;
import net.zdsoft.studevelop.data.dao.StuDevelopScoreRecordDao;
import net.zdsoft.studevelop.data.entity.StuDevelopCateGory;
import net.zdsoft.studevelop.data.entity.StuDevelopProject;
import net.zdsoft.studevelop.data.entity.StuDevelopScoreRecord;
import net.zdsoft.studevelop.data.entity.StuDevelopSubject;
import net.zdsoft.studevelop.data.service.StuDevelopCateGoryService;
import net.zdsoft.studevelop.data.service.StuDevelopProjectService;
import net.zdsoft.studevelop.data.service.StuDevelopScoreRecordService;
import net.zdsoft.studevelop.data.service.StuDevelopSubjectService;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
@Service("stuDevelopScoreRecordService")
public class StuDevelopScoreRecordServiceImpl extends BaseServiceImpl<StuDevelopScoreRecord,String> implements StuDevelopScoreRecordService{
    @Autowired
	private StuDevelopScoreRecordDao stuDevelopScoreRecordDao;
    @Autowired
    private StudentRemoteService studentRemoteService;
    @Autowired
    private StuDevelopSubjectService stuDevelopSubjectService;
    @Autowired
    private StuDevelopCateGoryService stuDevelopCateGoryService;
    @Autowired
    private StuDevelopProjectService stuDevelopProjectService;
	@Override
	protected BaseJpaRepositoryDao<StuDevelopScoreRecord, String> getJpaDao() {
		return stuDevelopScoreRecordDao;
	}

	@Override
	protected Class<StuDevelopScoreRecord> getEntityClass() {
		return StuDevelopScoreRecord.class;
	}

	@Override
	public void saveScore(List<StuDevelopScoreRecord> stuDevelopScoreRecordList) {
		stuDevelopScoreRecordDao.saveAll(stuDevelopScoreRecordList);
	}

	@Override
	public List<StuDevelopScoreRecord> stuDevelopScoreRecordList(
			String acadyear, String semester, String studentId) {
		return stuDevelopScoreRecordDao.stuDevelopScoreRecordList(acadyear,semester,studentId);
	}

	@Override
	@Transactional
	public String doImport(String unitId, List<String[]> datas,
			String acadyear, String semester, String gradeId) {
		Json importResultJson=new Json();
		List<String[]> errorDataList=new ArrayList<String[]>();
		int successCount  =0;
		String[] errorData=null;
		Set<String> stuCodeSet = new HashSet<String>();
		List<StuDevelopScoreRecord> insertList=new ArrayList<StuDevelopScoreRecord>();
		for(String[] arr : datas){
			stuCodeSet.add(arr[1]);
        }
		List<Student> stuList = SUtils.dt(studentRemoteService.findBySchIdStudentCodes(unitId, stuCodeSet.toArray(new String[0])), new TR<List<Student>>() {});
        Map<String, String> stuCodeNameMap = new HashMap<String, String>();
        Map<String, String> stuCodeIdMap = new HashMap<String, String>();
		for(Student stu : stuList){
			stuCodeNameMap.put(stu.getStudentCode(), stu.getStudentName());
			stuCodeIdMap.put(stu.getStudentCode(), stu.getId());
        }
		List<StuDevelopSubject> stuDevelopSubjectList = stuDevelopSubjectService.stuDevelopSubjectList(unitId, acadyear, semester, gradeId);
		Set<String> subNameSet = new HashSet<String>();
		Set<String> subIdSet = new HashSet<String>();
		Map<String, String> subMap = new HashMap<String, String>(); 
		for(StuDevelopSubject sub : stuDevelopSubjectList){
			subNameSet.add(sub.getName());
			subIdSet.add(sub.getId());
			subMap.put(sub.getName(), sub.getId());
		}
		List<StuDevelopCateGory> stuDevelopCateGoryList = new ArrayList<StuDevelopCateGory>();
		if(CollectionUtils.isNotEmpty(subIdSet)){
			stuDevelopCateGoryList = stuDevelopCateGoryService.findListBySubjectIdIn(subIdSet.toArray(new String[0]));
		}
		Map<String, Set<String>> subGoryIdMap = new HashMap<String, Set<String>>();
		Map<String, Map<String, String>> subGoryMap = new HashMap<String, Map<String, String>>();
		for(StuDevelopSubject sub : stuDevelopSubjectList){
			Set<String> groyNameSet = new HashSet<String>();
			Map<String, String> goryIdMap = new HashMap<String, String>();
			for(StuDevelopCateGory gory : stuDevelopCateGoryList){
				if(sub.getId().equals(gory.getSubjectId())){
					groyNameSet.add(gory.getCategoryName());
					goryIdMap.put(gory.getCategoryName(), gory.getId());
				}
			}
			subGoryIdMap.put(sub.getName(), groyNameSet);
			subGoryMap.put(sub.getName(), goryIdMap);
		}
		
		List<StuDevelopProject> stuDevelopProjectList = stuDevelopProjectService.stuDevelopProjectList(unitId, acadyear, semester, gradeId);
		Map<Integer ,String> proMap = new HashMap<Integer, String>();
		Map<String ,String> proTypeMap = new HashMap<String, String>();
		Map<Integer,StuDevelopProject> projectMap = new HashMap<>();
		int t = 6;
		for(StuDevelopProject pro : stuDevelopProjectList){
			proMap.put(t, pro.getId());
			projectMap.put(t,pro);
			proTypeMap.put(pro.getId(), pro.getState());
			t++;
		}
		Map<String ,String> scoreMap = new HashMap<String, String>();
		for(String[] arr : datas){
			if(StringUtils.isBlank(arr[0])){
        		errorData = new String[4];
				errorData[0]=errorDataList.size()+1+"";
				errorData[1]="学生姓名";
				errorData[2]="";
				errorData[3]="学生姓名不能为空";
				errorDataList.add(errorData);
				continue;
        	}
			if(StringUtils.isBlank(arr[1])){
        		errorData = new String[4];
				errorData[0]=errorDataList.size()+1+"";
				errorData[1]="学号";
				errorData[2]="";
				errorData[3]="学号不能为空";
				errorDataList.add(errorData);
				continue;
        	}else{
        		if(StringUtils.isBlank(stuCodeNameMap.get(arr[1]))){
        			errorData = new String[4];
    				errorData[0]=errorDataList.size()+1+"";
    				errorData[1]="学号";
    				errorData[2]=arr[1];
    				errorData[3]="不存在该学号所属的学生";
    				errorDataList.add(errorData);
    				continue;
        		}else{
        			if(StringUtils.isNotBlank(arr[0]) && !arr[0].equals(stuCodeNameMap.get(arr[1]))){
        				errorData = new String[4];
        				errorData[0]=errorDataList.size()+1+"";
        				errorData[1]="学号";
        				errorData[2]="姓名："+arr[0]+"&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;学号："+arr[1];
        				errorData[3]="学生姓名与该学号不匹配";
        				errorDataList.add(errorData);
        				continue;
        			}
        		}
        	}
			if(StringUtils.isNotBlank(arr[4])){
				if(!subNameSet.contains(arr[4])){
					errorData = new String[4];
					errorData[0]=errorDataList.size()+1+"";
					errorData[1]="学科";
					errorData[2]="";
					errorData[3]="该学年学期年级下不存在该学科";
					errorDataList.add(errorData);
					continue;
				}
			}else{
				errorData = new String[4];
				errorData[0]=errorDataList.size()+1+"";
				errorData[1]="学科";
				errorData[2]="";
				errorData[3]="学科不能为空";
				errorDataList.add(errorData);
				continue;
			}
			String goryId = "";
			if(StringUtils.isNotBlank(arr[5])){
				if(!subGoryIdMap.get(arr[4]).contains(arr[5])){
					errorData = new String[4];
					errorData[0]=errorDataList.size()+1+"";
					errorData[1]="学科类别";
					errorData[2]="";
					errorData[3]="该科目下不存在该学科类别";
					errorDataList.add(errorData);
					continue;
				}
				goryId = subGoryMap.get(arr[4]).get(arr[5]);
			}else{
				if(subGoryIdMap.get(arr[4]).size()>0){
					errorData = new String[4];
					errorData[0]=errorDataList.size()+1+"";
					errorData[1]="学科类别";
					errorData[2]="";
					errorData[3]="该科目下存在学科类别，学科类别不能为空";
					errorDataList.add(errorData);
					continue;
				}
			}
            for(int i=6;i<stuDevelopProjectList.size()+6;i++){
            	StuDevelopScoreRecord item = new StuDevelopScoreRecord();
            	item.setProjectId(proMap.get(i));
				if(StringUtils.isNotEmpty(arr[i]) && net.zdsoft.framework.utils.StringUtils.getRealLength(arr[i]) > 12){
					StuDevelopProject developProject = projectMap.get(i);
					String proName="";
					if(developProject != null){
						proName = developProject.getProjectName();
					}

					errorData = new String[4];
					errorData[0]=errorDataList.size()+1+"";
					errorData[1]="分数";
					errorData[2]="";
					errorData[3]="该"  + arr[4]+"科目下"+ arr[5] + "学科类别"+ proName+"项目成绩字符长度超过12个字符";
					errorDataList.add(errorData);
					continue;
				}
            	item.setScore(arr[i]);
            	item.setCategoryId(goryId);
            	item.setStudentId(stuCodeIdMap.get(arr[1]));
    			item.setSubjectId(subMap.get(arr[4]));
    			item.setAcadyear(acadyear);
    			item.setSemester(semester);
    			item.setId(UuidUtils.generateUuid());
    			if(StringUtils.isNotBlank(goryId) && "2".equals(proTypeMap.get(proMap.get(i)))){
    				scoreMap.put(stuCodeIdMap.get(arr[1])+"-"+subMap.get(arr[4])+"-"+proMap.get(i), arr[i]);
    			}else{
    				insertList.add(item);
    			}
			}
        	successCount++;
		}		
		for (String key : scoreMap.keySet()) { 
			StuDevelopScoreRecord item = new StuDevelopScoreRecord();
			item.setProjectId(key.split("-")[2]);
        	item.setScore(scoreMap.get(key));
        	item.setStudentId(key.split("-")[0]);
			item.setSubjectId(key.split("-")[1]);
			item.setAcadyear(acadyear);
			item.setSemester(semester);
			item.setId(UuidUtils.generateUuid());
			insertList.add(item);
		} 
		//导入超过1000的情况
		if (CollectionUtils.isNotEmpty(insertList)) {
			Set<String> stuIdSet = new HashSet<String>();
			for(StuDevelopScoreRecord item : insertList){
				stuIdSet.add(item.getStudentId());
			}
			stuDevelopScoreRecordDao.deleteByStudentIds(acadyear, semester, stuIdSet.toArray(new String[0]));
		    if ( insertList.size() > 1000 ) {
		        int loopNumber = insertList.size()/1000;
		        for (int i=0; i<loopNumber; i++ ) {
		            List<StuDevelopScoreRecord> list1 = insertList.subList(i * 1000, (i+1)*1000);
		            if ( list1 != null ) {
		            	StuDevelopScoreRecord[] inserts = list1.toArray(new StuDevelopScoreRecord[0]);
		            	saveAll(inserts);
		            }
		            if ( i+1 == loopNumber &&  insertList.size() -(1000 * loopNumber) > 0) {
		                List<StuDevelopScoreRecord> list2 = insertList.subList((i+1) * 1000, insertList.size());
		                if ( list2 != null ) {
		                	StuDevelopScoreRecord[] inserts = list2.toArray(new StuDevelopScoreRecord[0]);
		                	saveAll(inserts);
		                }
		           }
		       }
		     } else {
		    	  StuDevelopScoreRecord[] inserts = insertList.toArray(new StuDevelopScoreRecord[0]);
		    	  saveAll(inserts);
		    }
		}
		importResultJson.put("totalCount", datas.size());
		importResultJson.put("successCount", successCount);
		importResultJson.put("errorCount", errorDataList.size());
		importResultJson.put("errorData", errorDataList);
		return importResultJson.toJSONString();
	}

	@Override
	public List<StuDevelopScoreRecord> findByProjectId(String acadyear,
			String semester, String projectId) {
		return stuDevelopScoreRecordDao.findByProjectId(acadyear, semester, projectId);
	}

	@Override
	public void deleteByStudentIds(String acadyear, String semester,
			String[] studentIds) {
		stuDevelopScoreRecordDao.deleteByStudentIds(acadyear, semester,studentIds);
	}

}
