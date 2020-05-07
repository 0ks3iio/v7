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
import net.zdsoft.studevelop.data.dao.StuEvaluateRecordDao;
import net.zdsoft.studevelop.data.entity.StuEvaluateRecord;
import net.zdsoft.studevelop.data.service.StuEvaluateRecordService;
import net.zdsoft.system.entity.mcode.McodeDetail;
import net.zdsoft.system.remote.service.McodeRemoteService;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("stuEvaluateRecordService")
public class StuEvaluateRecordServiceImpl extends BaseServiceImpl<StuEvaluateRecord,String> implements StuEvaluateRecordService{

	@Autowired
	private StuEvaluateRecordDao stuEvaluateRecordDao;
	@Autowired
	private StudentRemoteService studentRemoteService;
	@Autowired
    private McodeRemoteService mcodeRemoteService;
	@Override
	public StuEvaluateRecord findById(String stuId, String acadyear,
			String semester) {
		List<StuEvaluateRecord> res = findListByCls(acadyear, semester, new String[] {stuId});
		if(CollectionUtils.isNotEmpty(res)) {
			return res.get(0);
		}
		return null;
	}
	
	@Override
	public List<StuEvaluateRecord> findListByCls(String acadyear,
			String semester, String[] array) {
		return stuEvaluateRecordDao.findListByCls(acadyear,semester,array);
	}
	
	@Override
	protected BaseJpaRepositoryDao<StuEvaluateRecord, String> getJpaDao() {
		return stuEvaluateRecordDao;
	}

	@Override
	protected Class<StuEvaluateRecord> getEntityClass() {
		return StuEvaluateRecord.class;
	}
	@Override
	public Integer deleteByStuIds(String acadyear, String semester,String[] studentIds){
		return stuEvaluateRecordDao.deleteByStuIds(acadyear,semester,studentIds);
	}
	@Override
	public String doImport(String unitId, List<String[]> datas,
			String acadyear, String semester) {
		Json importResultJson=new Json();
		List<String[]> errorDataList=new ArrayList<String[]>();
		int successCount  =0;
		String[] errorData=null;
		Set<String> stuCodeSet = new HashSet<String>();
		List<StuEvaluateRecord> insertList=new ArrayList<StuEvaluateRecord>();
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
        Map<String, String> evaluateLevelMap = new HashMap<String, String>();
        List<McodeDetail> McodeDetailList = SUtils.dt(mcodeRemoteService.findByMcodeIds(new String[]{"DM-PYDJLB"}), new TR<List<McodeDetail>>() {});
        for(McodeDetail mcode : McodeDetailList){
        	evaluateLevelMap.put(mcode.getMcodeContent(), mcode.getThisId());
        }
		for(String[] arr : datas){
			StuEvaluateRecord item = new StuEvaluateRecord();
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
        		if(null == evaluateLevelMap.get(arr[4])){
        			errorData = new String[4];
    				errorData[0]=errorDataList.size()+1+"";
    				errorData[1]="评语等级";
    				errorData[2]=arr[2];
    				errorData[3]="不存在对应的评语等级";
    				errorDataList.add(errorData);
    				continue;
        		}
			}else{
				errorData = new String[4];
				errorData[0]=errorDataList.size()+1+"";
				errorData[1]="评语等级";
				errorData[2]="";
				errorData[3]="评语等级不能为空";
				errorDataList.add(errorData);
				continue;
			}
			if(StringUtils.isBlank(arr[5])){
				errorData = new String[4];
				errorData[0]=errorDataList.size()+1+"";
				errorData[1]="老师寄语";
				errorData[2]="";
				errorData[3]="老师寄语不能为空";
				errorDataList.add(errorData);
				continue;
			}else{
				if(arr[5].length() > 280){
					errorData = new String[4];
					errorData[0]=errorDataList.size()+1+"";
					errorData[1]="老师寄语";
					errorData[2]="";
					errorData[3]="老师寄语字数不能超过280个汉字";
					errorDataList.add(errorData);
					continue;
				}
			}
			if(StringUtils.isBlank(arr[6])){
				errorData = new String[4];
				errorData[0]=errorDataList.size()+1+"";
				errorData[1]="个性特点";
				errorData[2]="";
				errorData[3]="个性特点不能为空";
				errorDataList.add(errorData);
				continue;
			}else{
				if(arr[6].length() > 50){
					errorData = new String[4];
					errorData[0]=errorDataList.size()+1+"";
					errorData[1]="个性特点";
					errorData[2]="";
					errorData[3]="个性特点字数不能超过50个汉字";
					errorDataList.add(errorData);
					continue;
				}
			}
			item.setStudentId(stuCodeIdMap.get(arr[1]));
			item.setEvaluateLevel(evaluateLevelMap.get(arr[4]));
			item.setTeacherEvalContent(arr[5]);
			item.setStrong(arr[6]);
			item.setAcadyear(acadyear);
			item.setSemester(semester);
			item.setId(UuidUtils.generateUuid());
			insertList.add(item);
        	successCount++;
		}		
		if(CollectionUtils.isNotEmpty(insertList)){
			Set<String> stuIdSet = new HashSet<String>();
			for(StuEvaluateRecord item : insertList){
				stuIdSet.add(item.getStudentId());
			}
			if(CollectionUtils.isNotEmpty(stuIdSet)){
				List<StuEvaluateRecord> stuEvaluateRecordList = stuEvaluateRecordDao.findListByCls(acadyear, semester, stuIdSet.toArray(new String[0]));
				StuEvaluateRecord[] deles = stuEvaluateRecordList.toArray(new StuEvaluateRecord[0]);
				deleteAll(deles);
			}
			if(CollectionUtils.isNotEmpty(insertList)){
				StuEvaluateRecord[] inserts = insertList.toArray(new StuEvaluateRecord[0]);
				checkSave(inserts);
				saveAll(inserts);
			}
		}
		importResultJson.put("totalCount", datas.size());
		importResultJson.put("successCount", successCount);
		importResultJson.put("errorCount", errorDataList.size());
		importResultJson.put("errorData", errorDataList);
		return importResultJson.toJSONString();
	}

}
