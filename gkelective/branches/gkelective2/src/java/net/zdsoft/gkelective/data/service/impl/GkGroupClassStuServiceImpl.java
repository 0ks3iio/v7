package net.zdsoft.gkelective.data.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.framework.utils.UuidUtils;
import net.zdsoft.gkelective.data.dao.GkGroupClassStuDao;
import net.zdsoft.gkelective.data.entity.GkGroupClassStu;
import net.zdsoft.gkelective.data.service.GkGroupClassStuService;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("gkGroupClassStuService")
public class GkGroupClassStuServiceImpl extends BaseServiceImpl<GkGroupClassStu, String> implements GkGroupClassStuService{

	@Autowired
	private GkGroupClassStuDao gkGroupClassStuDao;
	@Override
	public Map<String, List<String>> findByGroupClassIdIn(
			String... groupClassIds) {
		if(groupClassIds==null || groupClassIds.length<=0){
			return new HashMap<String, List<String>>();
		}
		List<GkGroupClassStu> list = gkGroupClassStuDao.findByGroupClassIdIn(groupClassIds);
		if(CollectionUtils.isEmpty(list)){
			return new HashMap<String, List<String>>();
		}
		Map<String, List<String>> returnMap = new HashMap<String, List<String>>();
		for(GkGroupClassStu g:list){
			if(!returnMap.containsKey(g.getGroupClassId())){
				returnMap.put(g.getGroupClassId(), new ArrayList<String>());
			}
			returnMap.get(g.getGroupClassId()).add(g.getStudentId());
		}
		return returnMap;
	}

	@Override
	protected BaseJpaRepositoryDao<GkGroupClassStu, String> getJpaDao() {
		return gkGroupClassStuDao;
	}

	@Override
	protected Class<GkGroupClassStu> getEntityClass() {
		return GkGroupClassStu.class;
	}

	@Override
	public void deleteByGroupClassIdIn(String... groupClassIds) {
		if(groupClassIds!=null && groupClassIds.length>0){
			gkGroupClassStuDao.deleteByGroupClassIdIn(groupClassIds);
		}
	}

	@Override
	public void saveStuList(Set<String> stuIds, String groupClassId) {
		gkGroupClassStuDao.deleteByGroupClassIdIn(new String[]{groupClassId});
		if(CollectionUtils.isNotEmpty(stuIds)){
			GkGroupClassStu s=null;
			List<GkGroupClassStu> insertList=new ArrayList<GkGroupClassStu>();
			for(String ss:stuIds){
				s=new GkGroupClassStu();
				s.setGroupClassId(groupClassId);
				s.setId(UuidUtils.generateUuid());
				s.setStudentId(ss);
				insertList.add(s);
			}
			this.saveAll(insertList.toArray(new GkGroupClassStu[]{}));
		}
	}

	@Override
	public void deleteStu(String[] groupClassIds, String[] stuId) {
		gkGroupClassStuDao.deleteStu(groupClassIds,stuId);
	}

	@Override
	public List<GkGroupClassStu> findGkGroupClassStuList(String roundId, String[] stuId) {
		if(stuId!=null && stuId.length == 0){
			return new ArrayList<GkGroupClassStu>();
		}
		if(stuId == null){
			return gkGroupClassStuDao.findGkGroupClassStuList(roundId);
		}else{
			return gkGroupClassStuDao.findGkGroupClassStuList(roundId,stuId);
		}
	}

}
