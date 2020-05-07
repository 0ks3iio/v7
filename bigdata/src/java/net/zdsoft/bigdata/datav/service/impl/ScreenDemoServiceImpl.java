package net.zdsoft.bigdata.datav.service.impl;

import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.bigdata.datav.dao.ScreenDemoDao;
import net.zdsoft.bigdata.datav.entity.ScreenDemo;
import net.zdsoft.bigdata.datav.service.ScreenDemoService;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author yangkj
 * @since 2019/6/19 17:32
 */
@Service
public class ScreenDemoServiceImpl extends BaseServiceImpl<ScreenDemo,String>
        implements ScreenDemoService {

    @Autowired
    private ScreenDemoDao screenDemoDao;

    @Override
    protected BaseJpaRepositoryDao<ScreenDemo, String> getJpaDao() {
        return screenDemoDao;
    }

    @Override
    protected Class<ScreenDemo> getEntityClass() {
        return ScreenDemo.class;
    }

    @Override
    public List<ScreenDemo> findAllByOrderId() {
        return screenDemoDao.findAllByOrderId();
    }
}
