package net.zdsoft.bigdata.extend.data.service;

import java.util.List;

import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.bigdata.extend.data.entity.TeacherTagResult;
import net.zdsoft.framework.entity.Pagination;

/**
 * Created by wangdongdong on 2018/7/9 13:41.
 */
public interface TeacherTagResultService extends BaseService<TeacherTagResult, String> {

    List<TeacherTagResult> getByTagIds(String[] tagIds, Pagination page);
}
