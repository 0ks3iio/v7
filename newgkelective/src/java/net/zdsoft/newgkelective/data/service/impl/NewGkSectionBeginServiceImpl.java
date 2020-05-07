package net.zdsoft.newgkelective.data.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.newgkelective.data.dao.NewGkSectionBeginDao;
import net.zdsoft.newgkelective.data.entity.NewGkSectionBegin;
import net.zdsoft.newgkelective.data.service.NewGkSectionBeginService;

@Service("newGkSectionBeginService")
public class NewGkSectionBeginServiceImpl extends BaseServiceImpl<NewGkSectionBegin, String>
		implements NewGkSectionBeginService {

	@Autowired
	private NewGkSectionBeginDao newGkSectionBeginDao;
	@Override
	protected BaseJpaRepositoryDao<NewGkSectionBegin, String> getJpaDao() {
		return newGkSectionBeginDao;
	}

	@Override
	protected Class<NewGkSectionBegin> getEntityClass() {
		return NewGkSectionBegin.class;
	}

	@Override
	public void updateJoinBySectionId(String[] noJoinId, String sectionId) {
		List<NewGkSectionBegin> beginList = findListBy("sectionId", sectionId);
		List<String> noJoinIdList=new ArrayList<String>();
		if(noJoinId!=null && noJoinId.length>0) {
			noJoinIdList = Arrays.asList(noJoinId);
		}
		for(NewGkSectionBegin vv:beginList) {
			if(noJoinIdList.contains(vv.getId())) {
				vv.setNoJoin(0);
			}else {
				vv.setNoJoin(1);
			}
			vv.setModifyTime(new Date());
		}
		saveAll(beginList.toArray(new NewGkSectionBegin[] {}));
		
	}

	@Override
	public void deleteBySectionId(String sectionId) {
		newGkSectionBeginDao.deleteBySectionId(sectionId);
	}
}
