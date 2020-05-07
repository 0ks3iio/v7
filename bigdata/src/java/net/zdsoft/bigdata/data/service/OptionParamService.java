package net.zdsoft.bigdata.data.service;

import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.bigdata.data.entity.OptionParam;

import java.util.List;

/**
 * Created by wangdongdong on 2018/11/27 16:31.
 */
public interface OptionParamService extends BaseService<OptionParam, String> {

    List<OptionParam> findByOptionCode(String code);

    void saveOptionParam(OptionParam optionParam);
}
