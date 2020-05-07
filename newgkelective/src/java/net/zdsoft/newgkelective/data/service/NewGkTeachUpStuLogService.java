package net.zdsoft.newgkelective.data.service;

import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.newgkelective.data.entity.NewGkTeachUpStuLog;

import java.util.List;

public interface NewGkTeachUpStuLogService extends BaseService<NewGkTeachUpStuLog, String> {

    /**
     * @param choiceId
     * @param studentIds 可以为null
     * @return
     */
    List<NewGkTeachUpStuLog> findByChoiceId(String choiceId, List<String> studentIds);

}
