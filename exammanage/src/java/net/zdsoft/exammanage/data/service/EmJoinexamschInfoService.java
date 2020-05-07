package net.zdsoft.exammanage.data.service;

import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.exammanage.data.entity.EmJoinexamschInfo;
import net.zdsoft.framework.entity.Pagination;

import java.util.List;

public interface EmJoinexamschInfoService extends BaseService<EmJoinexamschInfo, String> {

    public void deleteByExamId(String examId);

    public List<EmJoinexamschInfo> saveAllEntitys(EmJoinexamschInfo... joinexamschInfo);

    public List<EmJoinexamschInfo> findByExamId(String examId);

    public List<EmJoinexamschInfo> findByExamIdAndPage(String examId, Pagination page);

}
