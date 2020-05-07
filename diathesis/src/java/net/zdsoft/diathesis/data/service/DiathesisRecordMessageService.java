package net.zdsoft.diathesis.data.service;

import net.zdsoft.diathesis.data.entity.DiathesisRecordMessage;

import java.util.List;

/**
 * @Author: panlf
 * @Date: 2019/6/11 9:52
 */
public interface DiathesisRecordMessageService {

    void saveRecordMessage(DiathesisRecordMessage msg);

    DiathesisRecordMessage findById(String id);

    List<DiathesisRecordMessage> findByRecordId(String recordId);

    void deleteByRecordIds(List<String> recordDeleteIds);

    DiathesisRecordMessage findByRecordIdAndLeaveMsgPeopleId(String recordId, String userId);
}
