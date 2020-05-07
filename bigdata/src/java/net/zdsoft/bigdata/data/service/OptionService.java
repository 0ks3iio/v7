package net.zdsoft.bigdata.data.service;

import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.bigdata.data.dto.OptionDto;
import net.zdsoft.bigdata.data.entity.Option;
import net.zdsoft.bigdata.data.exceptions.BigDataBusinessException;

import java.util.List;

/**
 * Created by wangdongdong on 2018/11/27 16:31.
 */
public interface OptionService extends BaseService<Option, String> {

    Option findByCode(String frameCode) throws BigDataBusinessException;

    void updateOptionStatus(String id, Integer status);
    
    void updateOptionMobility(String id, Integer mobility);

    /**
     * 获取参数
     * @param code
     * @return
     */
    OptionDto getAllOptionParam(String code);

    List<Option> findAllOption();
    
	List<Option> findAllOptionByType(String type);
}
