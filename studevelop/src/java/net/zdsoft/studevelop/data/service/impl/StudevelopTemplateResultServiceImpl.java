package net.zdsoft.studevelop.data.service.impl;

import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.studevelop.data.dao.StudevelopTemplateResultDao;
import net.zdsoft.studevelop.data.entity.StudevelopTemplateResult;
import net.zdsoft.studevelop.data.service.StudevelopTemplateResultService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by luf on 2018/12/20.
 */
@Service("studevelopTemplateResultService")
public class StudevelopTemplateResultServiceImpl extends BaseServiceImpl<StudevelopTemplateResult,String> implements StudevelopTemplateResultService {
    @Autowired
    private StudevelopTemplateResultDao studevelopTemplateResultDao;
    @Override
    protected BaseJpaRepositoryDao<StudevelopTemplateResult, String> getJpaDao() {
        return studevelopTemplateResultDao;
    }

    @Override
    protected Class<StudevelopTemplateResult> getEntityClass() {
        return StudevelopTemplateResult.class;
    }

    @Override
    public List<StudevelopTemplateResult> getTemplateResultByStudentId(String[] templateItemIds, String studentId,String acadyear ,String semester) {
        return studevelopTemplateResultDao.getTemplateResultByStudentId(templateItemIds,studentId,acadyear,semester);
    }
    public List<StudevelopTemplateResult> getTemplateResultByStudentIds(String[] templateItemIds , String[] studentIds,String acadyear ,String semester){
    	 return studevelopTemplateResultDao.getTemplateResultByStudentIds(templateItemIds,studentIds,acadyear,semester);
    }
    @Override
    public Integer deleteByItemIdsStuIds(String[] templateItemIds , String[] studentIds,String acadyear ,String semester){
    	return studevelopTemplateResultDao.deleteByItemIdsStuIds(templateItemIds,studentIds,acadyear,semester);
    }
    @Override
    public Integer deleteByItemIdsStuIdsSubId(String[] templateItemIds , String[] studentIds,String acadyear ,String semester,String subjectId){
    	return studevelopTemplateResultDao.deleteByItemIdsStuIdsSubId(templateItemIds,studentIds,acadyear,semester,subjectId);
    }
}
