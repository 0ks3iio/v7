package net.zdsoft.newgkelective.data.service.impl;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.TypeReference;

import net.zdsoft.basedata.remote.service.CourseRemoteService;
import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.framework.utils.EntityUtils;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.newgkelective.data.dao.NewGkReferScoreDao;
import net.zdsoft.newgkelective.data.entity.NewGkReferScore;
import net.zdsoft.newgkelective.data.entity.NewGkScoreResult;
import net.zdsoft.newgkelective.data.service.NewGkReferScoreService;
import net.zdsoft.newgkelective.data.service.NewGkScoreResultService;

@Service("newGkReferScoreService")
public class NewGkReferScoreServiceImpl extends BaseServiceImpl<NewGkReferScore, String> implements NewGkReferScoreService{

	@Autowired
	private NewGkReferScoreDao newGkReferScoreDao;
	@Autowired
	private NewGkScoreResultService newGkScoreResultService;
	@Autowired
	private CourseRemoteService courseRemoteService;
	
	@Override
	protected BaseJpaRepositoryDao<NewGkReferScore, String> getJpaDao() {
		return newGkReferScoreDao;
	}

	@Override
	protected Class<NewGkReferScore> getEntityClass() {
		return NewGkReferScore.class;
	}

	@Override
	public List<NewGkReferScore> findByNames(String unitId, String gradeId, String[] names) {
		return newGkReferScoreDao.findByNames(unitId, gradeId, names);
	}

	@Override
	public List<NewGkReferScore> findListByGradeId(String unitId, String gradeId,boolean isMakeData,boolean isAll) {
		List<NewGkReferScore> list = newGkReferScoreDao.findListByGradeId(unitId,gradeId);
		if(isMakeData){
			list=toMakeData(unitId,list,isAll);
		}
		return list;
	}
	
	@Override
	public List<NewGkReferScore> findListByGradeIdWithMaster(String unitId,String gradeId,boolean isMakeData,boolean isAll){
		return findListByGradeId(unitId, gradeId, isMakeData, isAll);
	}

	@Override
	public List<NewGkReferScore> findByUnitId(String unitId) {
		return newGkReferScoreDao.findByUnitId(unitId);
	}

	@Override
	public void deleteById(String id) {
		//记录软删，对应学生成绩硬删
		newGkScoreResultService.deleteByReferScoreId(id);
		newGkReferScoreDao.deleteById(new Date(), id);
	}
	
	/**
	 * 
	 * @param list
	 * @param isAll 是:包括没有学生成绩的
	 * @return
	 */
	private List<NewGkReferScore> toMakeData(String unitId,List<NewGkReferScore> list,boolean isAll){
		if(CollectionUtils.isEmpty(list)){
			return list;
		}
		List<NewGkReferScore> returnList=new ArrayList<NewGkReferScore>();
		Set<String> idSet = EntityUtils.getSet(list, "id");
		Map<String, Integer> stuNumMap = newGkScoreResultService.findCountByReferId(unitId, idSet.toArray(new String[0]));
		Map<String, Map<String, Float>> avgNum = newGkScoreResultService.findCountSubjectByReferId(unitId,idSet.toArray(new String[0]));
		
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
		for(NewGkReferScore ref :list){
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
		    }else{
		    	if(!isAll){
		    		continue;
		    	}
		    }
		    
		    subNumArr[1] = String.valueOf(subSet.size());
		    dataList.add(subNumArr);
		    if(CollectionUtils.isNotEmpty(dataList2)){
		    	dataList.addAll(dataList2);
		    }  
		    ref.setDataList(dataList);
		    returnList.add(ref);
		}


		
		return returnList;
	}
	
	
	private List<String[]> getData(List<NewGkScoreResult> newGkScoreResultList, Map<String, String> courseMap){
		List<String[]> dataList = new ArrayList<String[]>();
		Set<String> subjectIdSet = new HashSet<String>();
		Set<String> studentIdSet = new HashSet<String>();
		Map<String,List<NewGkScoreResult>> map=new HashMap<String,List<NewGkScoreResult>>();
		for(NewGkScoreResult res : newGkScoreResultList){
			subjectIdSet.add(res.getSubjectId());
			studentIdSet.add(res.getStudentId());
			if(!map.containsKey(res.getSubjectId())){
				map.put(res.getSubjectId(), new ArrayList<NewGkScoreResult>());
			}
			map.get(res.getSubjectId()).add(res);
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
        	String[] subArr = new String[2];
        	if(!map.containsKey(subId)){
        		subArr[0] = courseMap.get(subId);
            	subArr[1] = "0";
            	dataList.add(subArr);
            	continue;
        	}
        	float countScore = 0;
        	for(NewGkScoreResult res : map.get(subId)){
        		countScore = countScore + res.getScore();
        	}
        	float average=0f;
        	if(map.get(subId).size()>0){
        		average = countScore/map.get(subId).size();
        	}
        	DecimalFormat df = new DecimalFormat("0.00");
        	subArr[0] = courseMap.get(subId);
        	subArr[1] = String.valueOf(df.format(average));
        	dataList.add(subArr);
        }
		return dataList;
	}

	@Override
	public NewGkReferScore findById(String referScoreId, boolean isMakeData, String unitId) {
		NewGkReferScore item = newGkReferScoreDao.findById(referScoreId).orElse(null);
		if(item==null){
			return null;
		}
		if(isMakeData){
			List<NewGkScoreResult> resultList = newGkScoreResultService.findByReferScoreIds(unitId, new String[]{item.getId()});
			if(CollectionUtils.isEmpty(resultList)){
				return item;
			}
			Set<String> subjectIds=new HashSet<String>();
			if(CollectionUtils.isNotEmpty(resultList)){
				subjectIds=EntityUtils.getSet(resultList, "subjectId");
			}
			Map<String, String> courseMap = SUtils.dt(courseRemoteService.findPartCouByIds(subjectIds.toArray(new String[0])),new TypeReference<Map<String, String>>(){});
			List<String[]> dataList=getData(resultList, courseMap);;
			item.setDataList(dataList);
		}
		return item;
	}

	@Override
	public String findDefaultIdByGradeId(String unitId, String gradeId) {
		List<NewGkReferScore> list = newGkReferScoreDao.findListByGradeId(unitId, gradeId);
		for (NewGkReferScore newGkReferScore : list) {
			if (Integer.valueOf(1).equals(newGkReferScore.getIsDefault())){
				return newGkReferScore.getId();
			}
		}
		return null;
	}

	@Override
	public String findDefaultIdByGradeId(String gradeId) {
		List<NewGkReferScore> list = newGkReferScoreDao.findListByGradeId(gradeId);
		for (NewGkReferScore newGkReferScore : list) {
			if (Integer.valueOf(1).equals(newGkReferScore.getIsDefault())){
				return newGkReferScore.getId();
			}
		}
		return null;
	}

    @Override
    public void deleteByGradeIds(String... gradeIds) {
        newGkReferScoreDao.deleteByGradeIds(new Date(), gradeIds);
    }

}
