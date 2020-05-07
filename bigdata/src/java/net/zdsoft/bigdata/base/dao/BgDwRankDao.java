package net.zdsoft.bigdata.base.dao;

import net.zdsoft.bigdata.base.entity.DwRank;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;


/**
 * @author yangkj
 * @since 2019/5/20 15:38
 */
public interface BgDwRankDao extends BaseJpaRepositoryDao<DwRank,String> {

    /**
     * 根据code获取数据仓库层级对象的结果集
     * @param code
     * @return DwRank
     */
    DwRank findByCode(String code);
}
