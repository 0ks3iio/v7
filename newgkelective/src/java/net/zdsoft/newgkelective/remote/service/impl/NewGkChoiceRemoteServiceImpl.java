package net.zdsoft.newgkelective.remote.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.zdsoft.basedata.remote.service.impl.BaseRemoteServiceImpl;
import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.newgkelective.data.entity.NewGkChoice;
import net.zdsoft.newgkelective.data.service.NewGkChoiceService;
import net.zdsoft.newgkelective.remote.service.NewGkChoiceRemoteService;

@Service
public class NewGkChoiceRemoteServiceImpl extends BaseRemoteServiceImpl<NewGkChoice, String> implements NewGkChoiceRemoteService {

	@Autowired
	private NewGkChoiceService newGkChoiceService;
	
	@Override
	public List<NewGkChoice> findListByGradeId(String gradeId) {
		return newGkChoiceService.findListByGradeId(gradeId);
	}

	@Override
	protected BaseService<NewGkChoice, String> getBaseService() {
		return newGkChoiceService;
	}
	
}


