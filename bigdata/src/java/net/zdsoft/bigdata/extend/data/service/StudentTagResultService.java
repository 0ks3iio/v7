package net.zdsoft.bigdata.extend.data.service;

import java.util.List;

import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.bigdata.extend.data.entity.StudentTagResult;
import net.zdsoft.framework.entity.Pagination;

/**
 * Created by wangdongdong on 2018/7/9 13:41.
 */
public interface StudentTagResultService extends BaseService<StudentTagResult, String> {

    List<StudentTagResult> getByTagIds(String[] tagIds, Pagination page);
}
