package net.zdsoft.stutotality.data.service;

import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.stutotality.data.entity.StutotalityOptionDesc;

import java.util.List;

public interface StutotalityOptionDescService extends BaseService<StutotalityOptionDesc,String>{

    public List<StutotalityOptionDesc> findListByOptionIds (String[] optionIds);

}
