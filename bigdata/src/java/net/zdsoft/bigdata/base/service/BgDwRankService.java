package net.zdsoft.bigdata.base.service;

import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.bigdata.base.entity.DwRank;


/**
 * @author yangkj
 * @since 2019/5/20 15:40
 */
public interface BgDwRankService extends BaseService<DwRank,String> {

    DwRank findByCode(String code);
}
