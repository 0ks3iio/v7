package net.zdsoft.bigdata.data.dao;

import net.zdsoft.bigdata.data.entity.OptionParam;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;

import java.util.List;

public interface OptionParamDao extends BaseJpaRepositoryDao<OptionParam, String> {

    List<OptionParam> findAllByOptionCodeOrderByOrderId(String optionCode);

}
