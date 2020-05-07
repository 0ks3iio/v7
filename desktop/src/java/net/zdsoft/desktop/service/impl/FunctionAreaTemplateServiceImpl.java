package net.zdsoft.desktop.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.desktop.dao.FunctionAreaTemplateDao;
import net.zdsoft.desktop.entity.FunctionAreaTemplate;
import net.zdsoft.desktop.service.FunctionAreaTemplateService;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;

/**
 * @author shenke
 * @since 2017/2/6 9:53
 */
@Service
public class FunctionAreaTemplateServiceImpl extends BaseServiceImpl<FunctionAreaTemplate,String> implements FunctionAreaTemplateService{

    @Autowired
    private FunctionAreaTemplateDao customTemplateDao;

    @Override
    protected BaseJpaRepositoryDao<FunctionAreaTemplate, String> getJpaDao() {

        return customTemplateDao;
    }

    @Override
    protected Class<FunctionAreaTemplate> getEntityClass() {

        return FunctionAreaTemplate.class;
    }

	@Override
	public void save(FunctionAreaTemplate template) {
		customTemplateDao.save(template);
	}
}
