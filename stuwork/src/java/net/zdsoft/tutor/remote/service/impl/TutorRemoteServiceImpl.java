package net.zdsoft.tutor.remote.service.impl;

import java.util.List;
import java.util.Map;
import java.util.Set;

import net.zdsoft.framework.utils.EntityUtils;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.tutor.data.entity.TutorResult;
import net.zdsoft.tutor.data.service.TutorResultService;
import net.zdsoft.tutor.remote.service.TutorRemoteService;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
@Service("tutorRemoteService")
public class TutorRemoteServiceImpl implements TutorRemoteService {

	@Autowired
	private TutorResultService tutorResultService;
	@Override
	public String getTutorTeaStuMapByUnitId(String unitId) {
		Map<String,Set<String>> teaStuMap = Maps.newHashMap();
		List<TutorResult> results = tutorResultService.findByUnitId(unitId);
		for(TutorResult result:results){
			if(teaStuMap.containsKey(result.getTeacherId())){
				Set<String> stuIds = teaStuMap.get(result.getTeacherId());
				stuIds.add(result.getStudentId());
				teaStuMap.put(result.getTeacherId(), stuIds);
			}else{
				Set<String> stuIds = Sets.newHashSet();
				stuIds.add(result.getStudentId());
				teaStuMap.put(result.getTeacherId(), stuIds);
			}
		}
		return SUtils.s(teaStuMap);
	}
	
	public String getTutorStuByTeacherId(String teacherId) {
		List<TutorResult> results = tutorResultService.findByTeacherId(teacherId);
		if(CollectionUtils.isNotEmpty(results)) {
			return SUtils.s(EntityUtils.getSet(results, TutorResult::getStudentId));
		}
		return SUtils.s(Sets.newHashSet());
	}

}
