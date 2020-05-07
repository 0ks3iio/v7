package net.zdsoft.diathesis.data.service.impl;

import net.zdsoft.diathesis.data.dao.DiathesisRecordMessageDao;
import net.zdsoft.diathesis.data.entity.DiathesisRecordMessage;
import net.zdsoft.diathesis.data.service.DiathesisRecordMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author: panlf
 * @Date: 2019/6/11 9:52
 */
@Service("diathesisRecordMessageService")
public class DiathesisRecordMessageServiceImpl implements DiathesisRecordMessageService {

    @Autowired
    private DiathesisRecordMessageDao diathesisRecordMessageDao;

    @Override
    public void saveRecordMessage(DiathesisRecordMessage msg) {
        diathesisRecordMessageDao.save(msg);
    }

    @Override
    public DiathesisRecordMessage findById(String id) {
        return diathesisRecordMessageDao.findById(id).get();
    }

    @Override
    public List<DiathesisRecordMessage> findByRecordId(String recordId) {

        return diathesisRecordMessageDao.findByRecordId(recordId);
    }

    @Override
    public void deleteByRecordIds(List<String> recordDeleteIds) {
        diathesisRecordMessageDao.deleteByRecordIds(recordDeleteIds);
    }

    @Override
    public DiathesisRecordMessage findByRecordIdAndLeaveMsgPeopleId(String recordId, String userId) {
        return diathesisRecordMessageDao.findByRecordIdAndLeaveMsgPeopleId(recordId,userId);
    }
}
