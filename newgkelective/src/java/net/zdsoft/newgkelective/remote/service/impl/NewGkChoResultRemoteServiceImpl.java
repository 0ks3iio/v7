package net.zdsoft.newgkelective.remote.service.impl;

import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.zdsoft.basedata.remote.service.impl.BaseRemoteServiceImpl;
import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.newgkelective.data.entity.NewGkChoResult;
import net.zdsoft.newgkelective.data.entity.NewGkChoice;
import net.zdsoft.newgkelective.data.service.NewGkChoResultService;
import net.zdsoft.newgkelective.data.service.NewGkChoiceService;
import net.zdsoft.newgkelective.remote.service.NewGkChoResultRemoteService;
@Service("newGkChoResultRemoteService")
public class NewGkChoResultRemoteServiceImpl extends BaseRemoteServiceImpl<NewGkChoResult, String>implements NewGkChoResultRemoteService{

	@Autowired
	private NewGkChoResultService newGkChoResultService;
	@Autowired
	private NewGkChoiceService newGkChoiceService;
	@Override
	protected BaseService<NewGkChoResult, String> getBaseService() {
		return newGkChoResultService;
	}
	@Override
	public String findStuIdListBySubjectId(String gradeId,boolean isXuankao,String[] subjectIds) {
		
		NewGkChoice choice = newGkChoiceService.findDefaultByGradeId(gradeId);
		if(choice==null) {
			return null;
		}
		
		Map<String, Set<String>> stuIdMap = newGkChoResultService.findStuIdListByChoiceIdAndSubjectId(choice.getUnitId(),choice.getId(),subjectIds,isXuankao);
		return SUtils.s(stuIdMap);
	}

}
