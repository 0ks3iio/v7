package net.zdsoft.teaeaxam.service.impl;

import java.util.Date;
import java.util.List;

import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.framework.utils.UuidUtils;
import net.zdsoft.teaeaxam.dao.TeaexamSubjectLineDao;
import net.zdsoft.teaeaxam.entity.TeaexamSubject;
import net.zdsoft.teaeaxam.entity.TeaexamSubjectLine;
import net.zdsoft.teaeaxam.service.TeaexamSubjectLineService;
import net.zdsoft.teaeaxam.service.TeaexamSubjectService;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
@Service("teaexamSubjectLineService")
public class TeaexamSubjectLineServiceImpl extends BaseServiceImpl<TeaexamSubjectLine,String> implements TeaexamSubjectLineService{
    @Autowired
	private TeaexamSubjectLineDao teaexamSubjectLineDao;
    @Autowired
    private TeaexamSubjectService teaexamSubjectService;
	@Override
	protected BaseJpaRepositoryDao<TeaexamSubjectLine, String> getJpaDao() {
		return teaexamSubjectLineDao;
	}

	@Override
	protected Class<TeaexamSubjectLine> getEntityClass() {
		return TeaexamSubjectLine.class;
	}

	@Override
	public List<TeaexamSubjectLine> findBySubjectId(String subjectId) {
		return teaexamSubjectLineDao.findBySubjectId(subjectId);
	}

	@Override
	public void saveLine(List<TeaexamSubjectLine> lineList, Float fullScore, String subjectId) {
		teaexamSubjectLineDao.deleteBySubjectId(subjectId);
		if(CollectionUtils.isNotEmpty(lineList)){			
			for(TeaexamSubjectLine line : lineList){
				line.setId(UuidUtils.generateUuid());
				line.setCreationTime(new Date());
				line.setModifyTime(new Date());
				line.setSubjectInfoId(subjectId);
			}
			teaexamSubjectLineDao.saveAll(lineList);
		}
		TeaexamSubject subject = teaexamSubjectService.findOne(subjectId);
		subject.setFullScore(fullScore);
		teaexamSubjectService.save(subject);
	}

	@Override
	public List<TeaexamSubjectLine> findBySubjectIds(String[] subjectIds) {
		return teaexamSubjectLineDao.findBySubjectIds(subjectIds);
	}

}
