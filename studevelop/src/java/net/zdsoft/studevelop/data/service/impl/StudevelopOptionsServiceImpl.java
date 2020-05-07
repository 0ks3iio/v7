package net.zdsoft.studevelop.data.service.impl;

import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.studevelop.data.dao.StudevelopOptionsDao;
import net.zdsoft.studevelop.data.entity.StudevelopOptions;
import net.zdsoft.studevelop.data.service.StudevelopOptionsService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by luf on 2018/11/30.
 */
public class StudevelopOptionsServiceImpl extends BaseServiceImpl<StudevelopOptions,String> implements StudevelopOptionsService {

    @Autowired
    private StudevelopOptionsDao studevelopOptionsDao;
    @Override
    protected BaseJpaRepositoryDao<StudevelopOptions, String> getJpaDao() {
        return studevelopOptionsDao;
    }

    @Override
    protected Class<StudevelopOptions> getEntityClass() {
        return StudevelopOptions.class;
    }
}
