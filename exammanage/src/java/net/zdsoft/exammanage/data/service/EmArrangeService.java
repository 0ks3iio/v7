package net.zdsoft.exammanage.data.service;

import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.exammanage.data.entity.EmArrange;

import java.util.List;

public interface EmArrangeService extends BaseService<EmArrange, String> {

    public EmArrange findByExamId(String examId);

    public void saveArrange(String examId, String type, String[] seatNums);

    public void arrangeResult(String examId);

    public List<EmArrange> findByExamIdIn(String[] array);

}
