package net.zdsoft.exammanage.xj.service;

import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.exammanage.xj.entity.XjexamType;

import java.util.List;

/**
 * @author yangsj  2017年10月12日下午4:41:51
 */
public interface XjExamTypeService extends BaseService<XjexamType, String> {

    /**
     * @param array
     * @return
     */
    List<XjexamType> findByTypeKeys(String... typeKeys);


}
