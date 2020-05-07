package net.zdsoft.bigdata.base.service.impl;

import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.bigdata.base.dao.BgDwRankDao;
import net.zdsoft.bigdata.base.entity.DwRank;
import net.zdsoft.bigdata.base.service.BgDwRankService;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * @author yangkj
 * @since 2019/5/20 15:41
 */
@Service("bgDwRankService")
public class BgDwRankServiceImpl extends BaseServiceImpl<DwRank,String> implements BgDwRankService {

    @Autowired
    private BgDwRankDao bgDwRankDao;

    @Override
    protected BaseJpaRepositoryDao<DwRank, String> getJpaDao() {
        return bgDwRankDao;
    }

    @Override
    protected Class<DwRank> getEntityClass() {
        return DwRank.class;
    }

    @Override
    public DwRank findByCode(String code) {
        return bgDwRankDao.findByCode(code);
    }
}
