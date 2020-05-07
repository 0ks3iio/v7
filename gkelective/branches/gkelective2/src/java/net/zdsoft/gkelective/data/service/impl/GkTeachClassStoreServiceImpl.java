package net.zdsoft.gkelective.data.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.gkelective.data.dao.GkTeachClassStoreDao;
import net.zdsoft.gkelective.data.dao.GkTeachClassStuStoreDao;
import net.zdsoft.gkelective.data.entity.GkTeachClassStore;
import net.zdsoft.gkelective.data.entity.GkTeachClassStuStore;
import net.zdsoft.gkelective.data.service.GkTeachClassStoreService;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
@Service("gkTeachClassStoreService")
public class GkTeachClassStoreServiceImpl extends BaseServiceImpl<GkTeachClassStore, String> implements GkTeachClassStoreService{
	@Autowired
	private GkTeachClassStuStoreDao gkTeachClassStuStoreDao;
	@Autowired
	private GkTeachClassStoreDao gkTeachClassStoreDao;
	@Override
	protected BaseJpaRepositoryDao<GkTeachClassStore, String> getJpaDao() {
		return gkTeachClassStoreDao;
	}

	@Override
	protected Class<GkTeachClassStore> getEntityClass() {
		return GkTeachClassStore.class;
	}

	@Override
	public void deleteByIds(String[] ids) {
		if(ids!=null && ids.length>0){
			gkTeachClassStoreDao.deleteByids(ids);
			gkTeachClassStuStoreDao.deleteByClassIds(ids);
		}
		
	}

	@Override
	public List<GkTeachClassStore> findByRoundsId(String roundsId) {
		List<GkTeachClassStore> list = gkTeachClassStoreDao.findByRoundsId(roundsId);
		if(CollectionUtils.isNotEmpty(list)){
			 Set<String> gId=new HashSet<String>();
			 for(GkTeachClassStore g:list){
				 gId.add(g.getId());
			 }
			 List<GkTeachClassStuStore> stulist = gkTeachClassStuStoreDao.findByClassIdIn(gId.toArray(new String[0]));
			 if(CollectionUtils.isNotEmpty(stulist)){
				 Map<String, List<String>> map = new HashMap<String, List<String>>();
				 for(GkTeachClassStuStore stu:stulist){
					 if(!map.containsKey(stu.getGkClassId())){
						 map.put(stu.getGkClassId(), new ArrayList<String>());
					 }
					 map.get(stu.getGkClassId()).add(stu.getStudentId());
				 }
				 for(GkTeachClassStore g:list){
					 if(map.containsKey(g.getId())){
						g.setStuList(map.get(g.getId())); 
					 }
				 }
			 }			
			 
		 }
		return list;
	}

	@Override
	public Map<String, List<String>> findMapWithStuIdByClassIds(String[] classIds) {
		Map<String, List<String>> map = new HashMap<String, List<String>>();
    	if(ArrayUtils.isEmpty(classIds)){
    		return map;
    	}
        List<GkTeachClassStuStore> list = gkTeachClassStuStoreDao.findByClassIdIn(classIds);
        if (CollectionUtils.isNotEmpty(list)) {
            for (GkTeachClassStuStore tstu : list) {
                if (!map.containsKey(tstu.getStudentId())) {
                    map.put(tstu.getStudentId(), new ArrayList<String>());
                }
                map.get(tstu.getStudentId()).add(tstu.getGkClassId());
            }
        }
        return map;
	}

	@Override
	public List<GkTeachClassStuStore> findByClassIds(String[] classIds) {
		if(ArrayUtils.isEmpty(classIds)){
			return new ArrayList<GkTeachClassStuStore>();
		}
		return gkTeachClassStuStoreDao.findByClassIdIn(classIds);
	}

	@Override
	public List<GkTeachClassStuStore> findByStuIds(String roundId, String[] stuIds) {
		if(StringUtils.isBlank(roundId) || ArrayUtils.isEmpty(stuIds)){
			return new ArrayList<GkTeachClassStuStore>();
		}
		return gkTeachClassStuStoreDao.findByStuIds(roundId,stuIds);
	}

	@Override
	public void saveAllStu(List<GkTeachClassStuStore> saveList) {
		gkTeachClassStuStoreDao.saveAll(saveList);
	}

	@Override
	public void delete(String[] classIds, String[] stuIds) {
		gkTeachClassStuStoreDao.delete(classIds,stuIds);
	}

}
