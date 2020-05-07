package net.zdsoft.studevelop.data.service.impl;

import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.studevelop.data.dao.StudevelopTemplateOptionsDao;
import net.zdsoft.studevelop.data.entity.StudevelopTemplateOptions;
import net.zdsoft.studevelop.data.service.StudevelopTemplateOptionsService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by luf on 2018/12/17.
 */
@Service("StudevelopTemplateOptionsService")
public class StudevelopTemplateOptionsServiceImpl extends BaseServiceImpl<StudevelopTemplateOptions ,String> implements StudevelopTemplateOptionsService {

    @Autowired
    private StudevelopTemplateOptionsDao studevelopTemplateOptionsDao;
    @Override
    protected BaseJpaRepositoryDao<StudevelopTemplateOptions, String> getJpaDao() {
        return studevelopTemplateOptionsDao;
    }

    @Override
    protected Class<StudevelopTemplateOptions> getEntityClass() {
        return StudevelopTemplateOptions.class;
    }

    @Override
    public List<StudevelopTemplateOptions> getOptionsListByTemplateItemId(String[] itemIds) {
    	if(itemIds==null || itemIds.length==0){
    		return new ArrayList<>();
    	}
        return studevelopTemplateOptionsDao.getOptionsListByTemplateItemId(itemIds);
    }
}
