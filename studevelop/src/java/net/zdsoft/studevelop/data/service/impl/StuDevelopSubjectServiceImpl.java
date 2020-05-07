package net.zdsoft.studevelop.data.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.framework.utils.StringUtils;
import net.zdsoft.framework.utils.UuidUtils;
import net.zdsoft.studevelop.data.dao.StuDevelopCateGoryDao;
import net.zdsoft.studevelop.data.dao.StuDevelopSubjectDao;
import net.zdsoft.studevelop.data.dto.StuDevelopSubjectDto;
import net.zdsoft.studevelop.data.entity.StuDevelopCateGory;
import net.zdsoft.studevelop.data.entity.StuDevelopSubject;
import net.zdsoft.studevelop.data.service.StuDevelopProjectService;
import net.zdsoft.studevelop.data.service.StuDevelopSubjectService;
import net.zdsoft.studevelop.data.service.StuHealthyProjectService;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("stuDevelopSubjectService")
public class StuDevelopSubjectServiceImpl extends BaseServiceImpl<StuDevelopSubject, String> implements StuDevelopSubjectService{
    @Autowired
	private StuDevelopSubjectDao stuDevelopSubjectDao;
    @Autowired
    private StuDevelopCateGoryDao stuDevelopCateGoryDao;
    @Autowired
    private StuDevelopProjectService stuDevelopProjectService;
	@Autowired
	private StuHealthyProjectService stuHealthyProjectService;
	@Override
	protected BaseJpaRepositoryDao<StuDevelopSubject, String> getJpaDao() {
		return stuDevelopSubjectDao;
	}

	@Override
	protected Class<StuDevelopSubject> getEntityClass() {
		return StuDevelopSubject.class;
	}

	@Override
	public void saveStuDevelopSubject(StuDevelopSubjectDto stuDevelopSubjectDto) {
		StuDevelopSubject stuDevelopSubject = stuDevelopSubjectDto.getStuDevelopSubject();
		List<StuDevelopCateGory> stuDevelopCateGoryList = stuDevelopSubjectDto.getStuDevelopCateGoryList();
		if(null == stuDevelopCateGoryList){
			stuDevelopCateGoryList = new ArrayList<StuDevelopCateGory>();
		}
		List<StuDevelopCateGory> goryListTemp = new ArrayList<StuDevelopCateGory>();
		for(StuDevelopCateGory gory : stuDevelopCateGoryList){
			if(null != gory){
				goryListTemp.add(gory);
			}
		}
		stuDevelopCateGoryList = goryListTemp;
		if(StringUtils.isBlank(stuDevelopSubject.getId())){
			String id = UuidUtils.generateUuid();
			stuDevelopSubject.setId(id);
			stuDevelopSubject.setCreationTime(new Date());
			for(StuDevelopCateGory gory : stuDevelopCateGoryList){
				gory.setSubjectId(id);
				gory.setCreationTime(new Date());
				gory.setId(UuidUtils.generateUuid());
				if(StringUtils.isBlank(gory.getState())){
					gory.setState("0");
				}
			}
		}else{
			for(StuDevelopCateGory gory : stuDevelopCateGoryList){
				gory.setSubjectId(stuDevelopSubject.getId());
				if(StringUtils.isBlank(gory.getState())){
					gory.setState("0");
				}
				if(StringUtils.isBlank(gory.getId())){
					gory.setId(UuidUtils.generateUuid());
				}
			}
			stuDevelopCateGoryDao.deleteBySubjectIds(new String[]{stuDevelopSubject.getId()});
		}
		stuDevelopSubjectDao.save(stuDevelopSubject);
		stuDevelopCateGoryDao.saveAll(stuDevelopCateGoryList);
	}

	@Override
	public List<StuDevelopSubject> stuDevelopSubjectList(String unitId,
			String acadyear, String semester, String gradeId) {
		List<StuDevelopSubject> stuDevelopSubjectList = stuDevelopSubjectDao.stuDevelopSubjectList(unitId, acadyear, semester, gradeId);
		Set<String> subjectIdSet = new HashSet<String>();
		for(StuDevelopSubject item : stuDevelopSubjectList){
			subjectIdSet.add(item.getId());
		}
		//进行改造
		Map<String , List<StuDevelopCateGory>> categoryMap = new HashMap<>();
		if(org.apache.commons.collections.CollectionUtils.isNotEmpty(subjectIdSet)){
			List<StuDevelopCateGory> stuDevelopCateGoryList = stuDevelopCateGoryDao.findListBySubjectIdIn(subjectIdSet.toArray(new String[0]));
			categoryMap = stuDevelopCateGoryList.stream().collect(Collectors.groupingBy(c->c.getSubjectId()));
		}
		for(StuDevelopSubject sub : stuDevelopSubjectList){
			if(categoryMap.containsKey(sub.getId())){
				String goryNames = "";
				List<StuDevelopCateGory> inList=categoryMap.get(sub.getId());
				for(StuDevelopCateGory gory : inList){
					if(sub.getId().equals(gory.getSubjectId())){
						goryNames = goryNames + gory.getCategoryName() + "、";
					}
				}
				if(goryNames.length()>0){
					sub.setCategoryNames(goryNames.substring(0, goryNames.length()-1));
				}
				sub.setCateGoryList(inList);
			}
		}
		return stuDevelopSubjectList;
	}

	@Override
	public void deleteByIds(String[] ids) {
		stuDevelopSubjectDao.deleteByIds(ids);
		stuDevelopCateGoryDao.deleteBySubjectIds(ids);
	}

	@Override
	public void copySubject(String oldAcadyear, String oldSemester, String[] gradeIds, 
			String acadyear, String semester, String unitId) {
		List<StuDevelopSubject> stuDevelopSubjectList = stuDevelopSubjectDao.stuDevelopSubjectListByGradeIds(unitId, oldAcadyear, oldSemester, gradeIds);
		Map<String, String> subjectIdMap = new HashMap<String, String>();
		Set<String> subjectIdSet = new HashSet<String>();
		for(StuDevelopSubject item : stuDevelopSubjectList){
			subjectIdSet.add(item.getId());
			String newId = UuidUtils.generateUuid();
			subjectIdMap.put(item.getId(), newId);
		}
		List<StuDevelopCateGory> stuDevelopCateGoryList = new ArrayList<StuDevelopCateGory>();
		Map<String, String> gorySubIdMap = new HashMap<String, String>();
		if(CollectionUtils.isNotEmpty(subjectIdSet)){
			stuDevelopCateGoryList = stuDevelopCateGoryDao.findListBySubjectIdIn(subjectIdSet.toArray(new String[0]));
			for(StuDevelopCateGory item : stuDevelopCateGoryList){
				gorySubIdMap.put(item.getId(), subjectIdMap.get(item.getSubjectId()));
			}
		}
		//先删除原有的
		List<StuDevelopSubject> yStuDevelopSubjectList = stuDevelopSubjectDao.stuDevelopSubjectListByGradeIds(unitId, acadyear, semester, gradeIds);
		Set<String> ySubjectIdSet = new HashSet<String>();
		for(StuDevelopSubject item : yStuDevelopSubjectList){
			ySubjectIdSet.add(item.getId());
		}
		if(CollectionUtils.isNotEmpty(ySubjectIdSet)){
			stuDevelopCateGoryDao.deleteBySubjectIds(ySubjectIdSet.toArray(new String[0]));
		}
		if(CollectionUtils.isNotEmpty(yStuDevelopSubjectList)){
			stuDevelopSubjectDao.deleteAll(yStuDevelopSubjectList);
		}		
		//将组装好的保存
		if(CollectionUtils.isNotEmpty(stuDevelopSubjectList)){
			List<StuDevelopSubject> stuDevelopSubjectList2 = new ArrayList<StuDevelopSubject>();
			for(StuDevelopSubject item : stuDevelopSubjectList){
				StuDevelopSubject sub = new StuDevelopSubject();
				sub.setAcadyear(acadyear);
				sub.setSemester(semester);
				sub.setId(subjectIdMap.get(item.getId()));
				sub.setUnitId(item.getUnitId());
				sub.setGradeId(item.getGradeId());
				sub.setName(item.getName());
				sub.setCreationTime(item.getCreationTime());
				stuDevelopSubjectList2.add(sub);
			}
			stuDevelopSubjectDao.saveAll(stuDevelopSubjectList2);
		}
		if(CollectionUtils.isNotEmpty(stuDevelopCateGoryList)){
			List<StuDevelopCateGory> stuDevelopCateGoryList2 = new ArrayList<StuDevelopCateGory>();
			for(StuDevelopCateGory item : stuDevelopCateGoryList){
				StuDevelopCateGory gory = new StuDevelopCateGory();
				gory.setId(UuidUtils.generateUuid());
				gory.setCategoryName(item.getCategoryName());
				gory.setState(item.getState());
				gory.setSubjectId(gorySubIdMap.get(item.getId()));
				gory.setCreationTime(item.getCreationTime());
				stuDevelopCateGoryList2.add(gory);
			}
			stuDevelopCateGoryDao.saveAll(stuDevelopCateGoryList2);
		}
	}

	@Override
	@Transactional
	public void doCopy(String oldAcadyear, String oldSemester, String[] gradeIds,
			String acadyear, String semester, String unitId) {
		//处理学科设置
		copySubject(oldAcadyear, oldSemester, gradeIds, acadyear, semester, unitId);
		//处理项目设置
		stuDevelopProjectService.copyProject(oldAcadyear, oldSemester, gradeIds, acadyear, semester, unitId);
		stuHealthyProjectService.doCopy(oldAcadyear,oldSemester,acadyear,semester,unitId);
	}

}
