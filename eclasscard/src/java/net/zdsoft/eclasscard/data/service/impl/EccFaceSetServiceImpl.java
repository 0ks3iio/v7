package net.zdsoft.eclasscard.data.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.zdsoft.basedata.entity.Clazz;
import net.zdsoft.basedata.entity.Student;
import net.zdsoft.basedata.remote.service.ClassRemoteService;
import net.zdsoft.basedata.remote.service.StudentRemoteService;
import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.eclasscard.data.constant.EccConstants;
import net.zdsoft.eclasscard.data.dao.EccFaceSetDao;
import net.zdsoft.eclasscard.data.dto.EccFaceSetDto;
import net.zdsoft.eclasscard.data.entity.EccFaceSet;
import net.zdsoft.eclasscard.data.entity.EccUserFace;
import net.zdsoft.eclasscard.data.service.EccFaceSetService;
import net.zdsoft.eclasscard.data.service.EccUserFaceService;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.framework.entity.TR;
import net.zdsoft.framework.utils.EntityUtils;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.framework.utils.UuidUtils;

@Service("eccFaceSetService")
public class EccFaceSetServiceImpl extends BaseServiceImpl<EccFaceSet, String> implements EccFaceSetService{

	@Autowired
	private EccFaceSetDao eccFaceSetDao;
	@Autowired
	private ClassRemoteService classRemoteService;
	@Autowired
	private EccUserFaceService eccUserFaceService;
	@Autowired
	private StudentRemoteService studentRemoteService;
	@Override
	protected BaseJpaRepositoryDao<EccFaceSet, String> getJpaDao() {
		return eccFaceSetDao;
	}

	@Override
	protected Class<EccFaceSet> getEntityClass() {
		return EccFaceSet.class;
	}

	@Override
	public void saveEccFaceSetList(EccFaceSetDto faceSetDto) {
		eccFaceSetDao.deleteByInfoId(faceSetDto.getInfoId());
		if(StringUtils.isNotBlank(faceSetDto.getClassIds())) {
			String[] classIds = faceSetDto.getClassIds().split(",");
			List<EccFaceSet> setList=new ArrayList<>();
			EccFaceSet set=null;
			for(String s:classIds) {
				set=new EccFaceSet();
				set.setId(UuidUtils.generateUuid());
				set.setInfoId(faceSetDto.getInfoId());
				set.setSetId(s);
				//暂时先默认只有班级
				set.setSetType(EccConstants.ECC_FACE_SET_TYPE_1);
				set.setUnitId(faceSetDto.getUnitId());
				set.setCreationTime(new Date());
				setList.add(set);
			}
			eccFaceSetDao.saveAll(setList);
		}
	}

	@Override
	public List<EccFaceSet> findEccFaceSetListByUnitId(String unitId) {
		return eccFaceSetDao.findByUnitId(unitId);
	}
	
	private Map<String,Integer> findFaceNumberByClassId(String[] classIds){
		List<Student> stulist = SUtils.dt(studentRemoteService.findByClassIds(classIds), new TR<List<Student>>() {});
		Map<String,Integer> stuNumByClassMap=new HashMap<>();
		if(CollectionUtils.isNotEmpty(stulist)) {
			List<String> stuIds = EntityUtils.getList(stulist, e->e.getId());
			List<EccUserFace> faceList=eccUserFaceService.findByOwnerIds(stuIds.toArray(new String[0]));
			List<String> facestuIds = EntityUtils.getList(faceList, e->e.getOwnerId());
			if(CollectionUtils.isNotEmpty(facestuIds)) {
				List<Student> needList=stulist.stream().filter(e->{
					return facestuIds.contains(e.getId());
				}).collect(Collectors.toList());
				if(CollectionUtils.isNotEmpty(needList)) {
					Map<String, List<String>> map11=EntityUtils.getListMap(needList,e->e.getClassId(), e->e.getId());
					for(Entry<String, List<String>> item:map11.entrySet()) {
						stuNumByClassMap.put(item.getKey(), item.getValue().size());
					}
				}
				
			}
		}
		return stuNumByClassMap;
	}

	@Override
	public Map<String, String[]> findEccFaceSetListByInfoIds(String unitId, String[] infoIds) {
		List<EccFaceSet> setList = eccFaceSetDao.findByUnitIdAndInfoIdIn(unitId,infoIds);
		if(CollectionUtils.isNotEmpty(setList)) {
			
			Map<String, String[]> returnMap = new HashMap<String, String[]>();
			Set<String> classIds = EntityUtils.getSet(setList, e->e.getSetId());
			List<Clazz> clazzList = SUtils.dt(classRemoteService.findByIdsSort(classIds.toArray(new String[0])), new TR<List<Clazz>>() {});
			Map<String,Integer> stuNumByClassMap=findFaceNumberByClassId(classIds.toArray(new String[0]));
			if(CollectionUtils.isNotEmpty(clazzList)) {
				Map<String, List<String>> listMap = EntityUtils.getListMap(setList, e->e.getInfoId(), e->e.getSetId());
				for(Entry<String, List<String>> item:listMap.entrySet()) {
					String key = item.getKey();
					List<String> values=item.getValue();
					//List<String> 排序显示
					List<Clazz> otherList = clazzList.stream().filter(e->{
						return values.contains(e.getId());
					}).collect(Collectors.toList());
					if(CollectionUtils.isNotEmpty(otherList)) {
						//班级总人数
						int all=0;
						for(Clazz c:otherList) {
							if(stuNumByClassMap.containsKey(c.getId())) {
								all=all+stuNumByClassMap.get(c.getId());
							}
						}
						String[] arr=new String[]{otherList.stream().map(e->e.getId()).collect(Collectors.joining(",")),
						                        otherList.stream().map(e->e.getClassNameDynamic()).collect(Collectors.joining(",")),
						                        all+""};
						returnMap.put(key, arr);
						
					}
				}
			}
			return returnMap;
		}
		return new HashMap<String, String[]>();
	}

	@Override
	public List<EccFaceSet> findEccFaceSetListByInfoId(String unitId, String infoId) {
		return eccFaceSetDao.findByUnitIdAndInfoId(unitId, infoId);
	}

}
