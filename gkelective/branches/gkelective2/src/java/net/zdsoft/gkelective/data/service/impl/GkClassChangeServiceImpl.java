package net.zdsoft.gkelective.data.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.zdsoft.framework.config.ControllerException;
import net.zdsoft.framework.utils.EntityUtils;
import net.zdsoft.framework.utils.UuidUtils;
import net.zdsoft.gkelective.data.dto.ClassChangeDto;
import net.zdsoft.gkelective.data.entity.GkBatch;
import net.zdsoft.gkelective.data.entity.GkGroupClassStu;
import net.zdsoft.gkelective.data.entity.GkResult;
import net.zdsoft.gkelective.data.entity.GkStuRemark;
import net.zdsoft.gkelective.data.entity.GkTeachClassEx;
import net.zdsoft.gkelective.data.entity.GkTeachClassStore;
import net.zdsoft.gkelective.data.entity.GkTeachClassStuStore;
import net.zdsoft.gkelective.data.service.GkBatchService;
import net.zdsoft.gkelective.data.service.GkClassChangeService;
import net.zdsoft.gkelective.data.service.GkGroupClassStuService;
import net.zdsoft.gkelective.data.service.GkResultService;
import net.zdsoft.gkelective.data.service.GkStuRemarkService;
import net.zdsoft.gkelective.data.service.GkTeachClassExService;
import net.zdsoft.gkelective.data.service.GkTeachClassStoreService;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("gkClassChangeService")
public class GkClassChangeServiceImpl implements GkClassChangeService{

//	@Autowired
//	private TeachClassStuRemoteService teachClassStuRemoteService;
//	@Autowired
//	private GkStuConversionService gkStuConversionService;
	@Autowired
	private GkTeachClassExService gkTeachClassExService;
	@Autowired
	private GkStuRemarkService gkStuRemarkService;
	@Autowired
	private GkBatchService gkBatchService;
//	@Autowired
//	private TeachClassRemoteService teachClassRemoteService;
//	@Autowired
//	private GkGroupClassService gkGroupClassService;
	@Autowired
	private GkGroupClassStuService gkGroupClassStuService;
	@Autowired
	private GkResultService gkResultService;
	@Autowired
	private GkTeachClassStoreService gkTeachClassStoreService;
	
	@Override
	public void saveClassChange(String arrangeId, String roundId, String operator, String leftClassSelect, String rightClassSelect, String leftAddStu, String rightAddStu) {
		//type#classId#subjectIds#batch#classType
		String[] split = leftClassSelect.split("#");
		String[] split2 = rightClassSelect.split("#");
		leftClassSelect = split[1];
		rightClassSelect = split2[1];
		String[] leftAddStus = null;
		Set<String> allStuIds = new HashSet<String>();
		if(StringUtils.isNotBlank(leftAddStu)){
			leftAddStus = leftAddStu.split(",");
		}
		String[] rightAddStus = null;
		if(StringUtils.isNotBlank(rightAddStu)){
			rightAddStus = rightAddStu.split(",");
		}
		if(ClassChangeDto.TYPE_1.equals(split[0])){
			//groupclass--组合班之间调整
			List<GkBatch> batchList = gkBatchService.findBatchListByGroupClassId(roundId, new String[]{leftClassSelect,rightClassSelect});
			Map<String,List<GkBatch>> gkBatchMap = new HashMap<String, List<GkBatch>>();//key groupClassId
			List<GkBatch> btLinList = null;
			for (GkBatch gkBatch : batchList) {
				btLinList = gkBatchMap.get(gkBatch.getGroupClassId());
				if(btLinList == null){
					btLinList = new ArrayList<GkBatch>();
					gkBatchMap.put(gkBatch.getGroupClassId(), btLinList);
				}
				btLinList.add(gkBatch);
			}
			Set<String> leftTeachClassIds = new HashSet<String>();
			Set<String> rightTeachClassIds = new HashSet<String>();
			Set<String> allTeachClassIds = new HashSet<String>();
			for(Map.Entry<String,List<GkBatch>> entry : gkBatchMap.entrySet()){
				btLinList = entry.getValue();
				for (GkBatch item : btLinList) {
					if(leftClassSelect.equals(item.getGroupClassId())){
						leftTeachClassIds.add(item.getTeachClassId());
					}else{
						rightTeachClassIds.add(item.getTeachClassId());
					}
				}
			}
			allTeachClassIds.addAll(leftTeachClassIds);
			allTeachClassIds.addAll(rightTeachClassIds);
			if(leftTeachClassIds.size()!=rightTeachClassIds.size()){
				throw new ControllerException("组合班科目数不同无法调整");
			}
//			Map<String, List<String>> leftStudentTeachClassMap = SUtils.dt(teachClassStuRemoteService.findMapWithStuIdByClassIds(leftTeachClassIds.toArray(new String[0])),new TR<Map<String, List<String>>>(){});
//			Map<String, List<String>> rightStudentTeachClassMap = SUtils.dt(teachClassStuRemoteService.findMapWithStuIdByClassIds(rightTeachClassIds.toArray(new String[0])),new TR<Map<String, List<String>>>(){});
			
//			Map<String, List<String>> leftStudentTeachClassMap=gkTeachClassStoreService.findMapWithStuIdByClassIds(leftTeachClassIds.toArray(new String[0]));
//			Map<String, List<String>> rightStudentTeachClassMap=gkTeachClassStoreService.findMapWithStuIdByClassIds(rightTeachClassIds.toArray(new String[0]));
//			
//			if(leftAddStus!=null){
//				for (String string : leftAddStus) {
//					List<String> linList = rightStudentTeachClassMap.get(string);
//					if(linList == null || linList.size() != rightTeachClassIds.size()){
//						//页面停留时间长，在其他地方调掉了该学生
//						throw new ControllerException("学生不存在，请刷新页面重新操作");
//					}else if(leftStudentTeachClassMap.containsKey(string)){
//						//右边已存在该学生
//						throw new ControllerException("学生已存在，请刷新页面重新操作");
//					}
//					allStuIds.add(string);
//				}
//			}
//			if(rightAddStus!=null){
//				for (String string : rightAddStus) {
//					List<String> linList = leftStudentTeachClassMap.get(string);
//					if(linList == null || linList.size() != leftTeachClassIds.size()){
//						//页面停留时间长，在其他地方调掉了该学生
//						throw new ControllerException("学生不存在，请刷新页面重新操作");
//					}else if(rightStudentTeachClassMap.containsKey(string)){
//						//右边已存在该学生
//						throw new ControllerException("学生已存在，请刷新页面重新操作");
//					}
//					allStuIds.add(string);
//				}
//			}
//			List<TeachClass> teaClslist = SUtils.dt(teachClassRemoteService.findByIds(allTeachClassIds.toArray(new String[0])), new TR<List<TeachClass>>(){});
//			Map<String,TeachClass> allTeaClsMap = EntityUtils.getMap(teaClslist, "id");
//			Map<String,TeachClass> leftTeaClsMap = new HashMap<String,TeachClass>();//subjectId
//			Map<String,TeachClass> rightTeaClsMap = new HashMap<String,TeachClass>();//subjectId
//			Map<String,TeachClass> leftTeaClsSubMap = new HashMap<String,TeachClass>();//subjectId
//			Map<String,TeachClass> rightTeaClsSubMap = new HashMap<String,TeachClass>();//subjectId
//			for (TeachClass teachClass : teaClslist) {
//				if(leftTeachClassIds.contains(teachClass.getId())){
//					leftTeaClsMap.put(teachClass.getId(), teachClass);
//					leftTeaClsSubMap.put(teachClass.getCourseId(), teachClass);
//				}else if(rightTeachClassIds.contains(teachClass.getId())){
//					rightTeaClsMap.put(teachClass.getId(), teachClass);
//					rightTeaClsSubMap.put(teachClass.getCourseId(), teachClass);
//				}
//			}
//			
//			List<TeachClassStu> saveList = new ArrayList<TeachClassStu>();
//			TeachClassStu teachClassStu = null;
//			List<GkStuConversion> conSaveList = new ArrayList<GkStuConversion>();
//			GkStuConversion gkStuConversion = null;
			
			List<GkTeachClassStuStore> AddStuList=new ArrayList<GkTeachClassStuStore>();
			List<GkGroupClassStu> gcsSaveList = new ArrayList<GkGroupClassStu>();
			
			GkGroupClassStu gkGroupClassStu = null;
			if(leftAddStus!=null && leftAddStus.length>0){
				gkTeachClassStoreService.delete(rightTeachClassIds.toArray(new String[0]),leftAddStus);
				for (String string : leftAddStus) {
					gkGroupClassStu = new GkGroupClassStu();
					gkGroupClassStu.setId(UuidUtils.generateUuid());
					gkGroupClassStu.setStudentId(string);
					gkGroupClassStu.setGroupClassId(leftClassSelect);
					gcsSaveList.add(gkGroupClassStu);
					
					for(String clid:leftTeachClassIds){
						GkTeachClassStuStore ent =new GkTeachClassStuStore();
						ent.setId(UuidUtils.generateUuid());
						ent.setGkClassId(clid);
						ent.setStudentId(string);
						AddStuList.add(ent);
					}
					
					allStuIds.add(string);
				}
				
			}
			if(rightAddStus!=null && rightAddStus.length>0){
				gkTeachClassStoreService.delete(leftTeachClassIds.toArray(new String[0]),rightAddStus);
				for (String string : rightAddStus) {
					gkGroupClassStu = new GkGroupClassStu();
					gkGroupClassStu.setId(UuidUtils.generateUuid());
					gkGroupClassStu.setStudentId(string);
					gkGroupClassStu.setGroupClassId(rightClassSelect);
					gcsSaveList.add(gkGroupClassStu);
					
					for(String clid:rightTeachClassIds){
						GkTeachClassStuStore ent =new GkTeachClassStuStore();
						ent.setId(UuidUtils.generateUuid());
						ent.setGkClassId(clid);
						ent.setStudentId(string);
						AddStuList.add(ent);
					}
					
					allStuIds.add(string);
				}
			}
			if(CollectionUtils.isNotEmpty(AddStuList)){
				gkTeachClassStoreService.saveAllStu(AddStuList);
			}
//			if(CollectionUtils.isNotEmpty(conSaveList)){
//				gkStuConversionService.saveAllEntitys(conSaveList.toArray(new GkStuConversion[0]));
//			}
			List<GkGroupClassStu> findGkGroupClassStuList = gkGroupClassStuService.findGkGroupClassStuList(roundId, allStuIds.toArray(new String[0]));
			gkGroupClassStuService.deleteAll(findGkGroupClassStuList.toArray(new GkGroupClassStu[0]));
			gkGroupClassStuService.saveAll(gcsSaveList.toArray(new GkGroupClassStu[0]));
			
			List<GkTeachClassStore> teaClslist = gkTeachClassStoreService.findListByIds(allTeachClassIds.toArray(new String[0]));
			Map<String,GkTeachClassStore> allTeaClsMap = EntityUtils.getMap(teaClslist, "id");
			//重新计算平均分
			refTeachClassScore(arrangeId, roundId, split[2].split(","), allTeachClassIds.toArray(new String[0]), allTeaClsMap);
		}else{
			//teachclass--教学班之间调整
			Map<String, List<String>> studentTeachClassMap = gkTeachClassStoreService.findMapWithStuIdByClassIds(new String[]{leftClassSelect,rightClassSelect});
			List<String> linList = null;
			if(leftAddStus!=null){
				for (String string : leftAddStus) {
					linList = studentTeachClassMap.get(string);
					if(linList == null || !linList.contains(rightClassSelect)){
						//页面停留时间长，在其他地方调掉了该学生
						throw new ControllerException("学生不存在，请刷新页面重新操作");
					}else if(linList.contains(leftClassSelect)){
						//左边已存在该学生
						throw new ControllerException("学生已存在，请刷新页面重新操作");
					}
				}
			}
			if(rightAddStus!=null){
				for (String string : rightAddStus) {
					linList = studentTeachClassMap.get(string);
					if(linList == null || !linList.contains(leftClassSelect)){
						//页面停留时间长，在其他地方调掉了该学生
						throw new ControllerException("学生不存在，请刷新页面重新操作");
					}else if(linList.contains(rightClassSelect)){
						//右边已存在该学生
						throw new ControllerException("学生已存在，请刷新页面重新操作");
					}
				}
			}
			List<GkTeachClassStuStore> saveList = new ArrayList<GkTeachClassStuStore>();
			GkTeachClassStuStore teachClassStu = null;
//			List<GkStuConversion> conSaveList = new ArrayList<GkStuConversion>();
			if(leftAddStus!=null && leftAddStus.length>0){
				gkTeachClassStoreService.delete(new String[]{rightClassSelect},leftAddStus);
				for (String string : leftAddStus) {
					teachClassStu = new GkTeachClassStuStore();
					teachClassStu.setId(UuidUtils.generateUuid());
					teachClassStu.setGkClassId(leftClassSelect);
					teachClassStu.setStudentId(string);
					saveList.add(teachClassStu);
				}
			}
			if(rightAddStus!=null && rightAddStus.length>0){
				gkTeachClassStoreService.delete(new String[]{leftClassSelect},rightAddStus);
				for (String string : rightAddStus) {
					teachClassStu = new GkTeachClassStuStore();
					teachClassStu.setId(UuidUtils.generateUuid());
					teachClassStu.setGkClassId(rightClassSelect);
					teachClassStu.setStudentId(string);
					saveList.add(teachClassStu);
				}
			}
			if(CollectionUtils.isNotEmpty(saveList)){
				gkTeachClassStoreService.saveAllStu(saveList);
			}
//			if(CollectionUtils.isNotEmpty(conSaveList)){
//				gkStuConversionService.saveAllEntitys(conSaveList.toArray(new GkStuConversion[0]));
//			}
			//重新计算平均分
			List<GkTeachClassStore> teaClslist = gkTeachClassStoreService.findListByIds(new String[]{leftClassSelect,rightClassSelect});
			Map<String,GkTeachClassStore> allTeaClsMap = EntityUtils.getMap(teaClslist, "id");
			refTeachClassScore(arrangeId, roundId, split[2].split(","), new String[]{leftClassSelect,rightClassSelect}, allTeaClsMap);
		}
	}

	/**
	 * 重新计算平均分
	 */
	public void refTeachClassScore(String arrangeId, String roundId, String[] subjectIds, String[] allTeachClassIds,
			Map<String, GkTeachClassStore> allTeaClsMap) {
		List<GkTeachClassEx> findGkTeachClassExList = gkTeachClassExService.findGkTeachClassExList(roundId, allTeachClassIds);
		Map<String, GkTeachClassEx> gkTeachClassExMap = EntityUtils.getMap(findGkTeachClassExList, "teachClassId");
		List<GkTeachClassEx> gkTeachClassExSaveList = new ArrayList<GkTeachClassEx>();
		List<GkTeachClassStuStore> teachClassStuList = gkTeachClassStoreService.findByClassIds(allTeachClassIds);
		Map<String,Set<String>> teachClassStuMap = new HashMap<String,Set<String>>();//教学班对应的学生
		Set<String> studentIds = new HashSet<String>();
		Set<String> linSet = null;
		for (GkTeachClassStuStore item : teachClassStuList) {
			studentIds.add(item.getStudentId());
			linSet = teachClassStuMap.get(item.getGkClassId());
			if(linSet == null){
				linSet = new HashSet<String>();
				teachClassStuMap.put(item.getGkClassId(), linSet);
			}
			linSet.add(item.getStudentId());
		}
		List<GkStuRemark> findStuScoreList = gkStuRemarkService.findStuScoreList(arrangeId, subjectIds, studentIds.toArray(new String[0]));
		Map<String,Map<String,GkStuRemark>> stuReMap = new HashMap<String,Map<String,GkStuRemark>>();//key studentId subjectId
		Map<String,GkStuRemark> linMap = null;
		for (GkStuRemark gkStuRemark : findStuScoreList) {
			linMap = stuReMap.get(gkStuRemark.getStudentId());
			if(linMap == null){
				linMap = new HashMap<String, GkStuRemark>();
				stuReMap.put(gkStuRemark.getStudentId(), linMap);
			}
			linMap.put(gkStuRemark.getSubjectId(), gkStuRemark);
		}
		GkStuRemark gkStuRemark = null;
		GkTeachClassEx gkTeachClassEx = null;
		GkTeachClassStore teachClass = null;
		for(Map.Entry<String,Set<String>> entry:teachClassStuMap.entrySet()){
			double dou = 0;
			teachClass = allTeaClsMap.get(entry.getKey());
			for(String stuId:entry.getValue()){
				linMap = stuReMap.get(stuId);
				if(linMap!=null){
					gkStuRemark = linMap.get(teachClass.getSubjectId());
					if(gkStuRemark!=null){
//						System.out.println(teachClass.getName()+":"+gkStuRemark.getScore());
						dou+=gkStuRemark.getScore();
					}
				}
			}
//			System.out.println(teachClass.getName()+"总分:"+dou);
			if(dou!=0){
				if(gkTeachClassExMap.get(entry.getKey()) == null){
					gkTeachClassEx = new GkTeachClassEx();
					gkTeachClassEx.setRoundsId(roundId);
					gkTeachClassEx.setTeachClassId(entry.getKey());
					gkTeachClassEx.setAverageScore(formatDouble(dou/entry.getValue().size()));
					gkTeachClassExSaveList.add(gkTeachClassEx);
				}else{
					gkTeachClassExMap.get(entry.getKey()).setAverageScore(formatDouble(dou/entry.getValue().size()));
					gkTeachClassExSaveList.add(gkTeachClassExMap.get(entry.getKey()));
				}
			}else{
				if(gkTeachClassExMap.get(entry.getKey()) == null){
					gkTeachClassEx = new GkTeachClassEx();
					gkTeachClassEx.setRoundsId(roundId);
					gkTeachClassEx.setTeachClassId(entry.getKey());
					gkTeachClassEx.setAverageScore(dou);
					gkTeachClassExSaveList.add(gkTeachClassEx);
				}else{
					gkTeachClassExMap.get(entry.getKey()).setAverageScore(dou);
					gkTeachClassExSaveList.add(gkTeachClassExMap.get(entry.getKey()));
				}
			}
		}
		if(CollectionUtils.isNotEmpty(gkTeachClassExSaveList)){
			gkTeachClassExService.saveAllEntitys(gkTeachClassExSaveList.toArray(new GkTeachClassEx[0]));
		}
	}
	
	/**
     * 保留两位小数，四舍五入
     * @param d
     * @return
     */
	private double formatDouble(double dou) {
        return (double)Math.round(dou*100)/100;
    }

	@Override
	public void saveStuSubChange(String unitId, String arrangeId, String roundId, String stuId, String searchClassType, String chosenClassIds, String searchSubjectIds) {
		Set<String> stuTeachClassIds = new HashSet<String>();
		Set<String> allClassIds = new HashSet<String>();
		//先将学生从原教学班踢出
		List<GkTeachClassStuStore> findByStuIds = gkTeachClassStoreService.findByStuIds(roundId, new String[]{stuId});
		Set<String> teachClassIds = EntityUtils.getSet(findByStuIds, "gkClassId");
		List<GkTeachClassStore> teaClslist = gkTeachClassStoreService.findListByIdIn(teachClassIds.toArray(new String[0]));
		if(CollectionUtils.isNotEmpty(teaClslist)){
			Set<String> teachCalssIds = EntityUtils.getSet(teaClslist, "id");
			List<GkBatch> findByClassIds = gkBatchService.findByClassIds(roundId, teachCalssIds.toArray(new String[0]));
			if(CollectionUtils.isNotEmpty(findByClassIds)){
				stuTeachClassIds = EntityUtils.getSet(findByClassIds, "teachClassId");
				gkTeachClassStoreService.delete(stuTeachClassIds.toArray(new String[0]),new String[]{stuId});
				allClassIds.addAll(stuTeachClassIds);
			}
		}
		if("1".equals(searchClassType)){
			//组合班形式--将学生调进组合班
			List<GkBatch> findBatchListByGroupClassId = gkBatchService.findBatchListByGroupClassId(roundId, chosenClassIds);
			Set<String> newClassIds=new HashSet<String>();
			//将学生从原有组合班踢出
			List<GkGroupClassStu> gkGroupClassStuList = gkGroupClassStuService.findGkGroupClassStuList(roundId,new String[]{stuId});
			if(CollectionUtils.isNotEmpty(gkGroupClassStuList)){
				gkGroupClassStuService.deleteAll(gkGroupClassStuList.toArray(new GkGroupClassStu[0]));
			}
			for (GkBatch item : findBatchListByGroupClassId) {
				allClassIds.add(item.getTeachClassId());
				newClassIds.add(item.getTeachClassId());
			}
			GkGroupClassStu gkGroupClassStu = new GkGroupClassStu();
			gkGroupClassStu.setId(UuidUtils.generateUuid());
			gkGroupClassStu.setGroupClassId(chosenClassIds);
			gkGroupClassStu.setStudentId(stuId);
			gkGroupClassStuService.saveAll(new GkGroupClassStu[]{gkGroupClassStu});
			//--所有批次同时调整
			
			List<GkTeachClassStuStore> tcsSaveList = new ArrayList<GkTeachClassStuStore>();
			GkTeachClassStuStore teachClassStu = null;
			for (String string : newClassIds) {
				teachClassStu = new GkTeachClassStuStore();
				teachClassStu.setId(UuidUtils.generateUuid());
				teachClassStu.setGkClassId(string);
				teachClassStu.setStudentId(stuId);
				tcsSaveList.add(teachClassStu);
			}
			if(CollectionUtils.isNotEmpty(tcsSaveList)){
				gkTeachClassStoreService.saveAllStu(tcsSaveList);
			}
			teaClslist = gkTeachClassStoreService.findListByIds(allClassIds.toArray(new String[0]));
			Map<String, GkTeachClassStore> map = EntityUtils.getMap(teaClslist, "id");
			Set<String> subjectIds = EntityUtils.getSet(teaClslist, "subjectId");
			//重新计算平均分
			refTeachClassScore(arrangeId, roundId, subjectIds.toArray(new String[0]), allClassIds.toArray(new String[0]), map);
			
		}else if("2".equals(searchClassType)){
			//单科班形式--所有批次同时调整
			//将学生从原有组合班踢出
			List<GkGroupClassStu> gkGroupClassStuList = gkGroupClassStuService.findGkGroupClassStuList(roundId,new String[]{stuId});
			if(CollectionUtils.isNotEmpty(gkGroupClassStuList)){
				gkGroupClassStuService.deleteAll(gkGroupClassStuList.toArray(new GkGroupClassStu[0]));
			}
			List<GkTeachClassStuStore> tcsSaveList = new ArrayList<GkTeachClassStuStore>();
			GkTeachClassStuStore teachClassStu = null;
			if(chosenClassIds.indexOf("@")>0){
				//2+1或者有混合班
				String[] choSplit = chosenClassIds.split("@");
				String groupClassId = choSplit[0];
				GkGroupClassStu gkGroupClassStu = new GkGroupClassStu();
				gkGroupClassStu.setId(UuidUtils.generateUuid());
				gkGroupClassStu.setGroupClassId(groupClassId);
				gkGroupClassStu.setStudentId(stuId);
				gkGroupClassStuService.saveAll(new GkGroupClassStu[]{gkGroupClassStu});
				chosenClassIds = choSplit[1];
			}
			String[] split = chosenClassIds.split(",");
			for (String string : split) {
				teachClassStu = new GkTeachClassStuStore();
				teachClassStu.setId(UuidUtils.generateUuid());
				teachClassStu.setGkClassId(string);
				teachClassStu.setStudentId(stuId);
				tcsSaveList.add(teachClassStu);
				allClassIds.add(string);
			}
			if(CollectionUtils.isNotEmpty(tcsSaveList)){
				gkTeachClassStoreService.saveAllStu(tcsSaveList);
			}
			teaClslist = gkTeachClassStoreService.findListByIds(allClassIds.toArray(new String[0]));
			Map<String, GkTeachClassStore> map = EntityUtils.getMap(teaClslist, "id");
			Set<String> subjectIds = EntityUtils.getSet(teaClslist, "subjectId");
			//重新计算平均分
			refTeachClassScore(arrangeId, roundId, subjectIds.toArray(new String[0]), allClassIds.toArray(new String[0]), map);
		}
		
		
		//重新保存选课结果
		String[] subIds = searchSubjectIds.split(",");
		List<GkResult> gkResultSaveList = new ArrayList<GkResult>();
		GkResult gkResult = null;
		for (String subId : subIds) {
			gkResult = new GkResult();
			gkResult.setId(UuidUtils.generateUuid());
			gkResult.setCreationTime(new Date());
			gkResult.setModifyTime(new Date());
			gkResult.setSubjectArrangeId(arrangeId);
			gkResult.setSubjectId(subId);
			gkResult.setStudentId(stuId);
			gkResult.setStatus(1);//默认锁定状态
			gkResultSaveList.add(gkResult);
		}
		//先移除之前选择的再保存这次选择的结果
		gkResultService.removeByArrangeIdAndStudentId(arrangeId, stuId);
		gkResultService.saveAll(gkResultSaveList.toArray(new GkResult[0]));
	}

}
