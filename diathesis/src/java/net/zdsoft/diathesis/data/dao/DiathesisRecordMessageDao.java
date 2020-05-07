package net.zdsoft.diathesis.data.dao;

import net.zdsoft.diathesis.data.entity.DiathesisRecordMessage;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * @Author: panlf
 * @Date: 2019/10/30 9:51
 */
public interface DiathesisRecordMessageDao extends BaseJpaRepositoryDao<DiathesisRecordMessage,String> {

    @Query("FROM DiathesisRecordMessage where recordId=?1 order by modifyTime desc ")
    List<DiathesisRecordMessage> findByRecordId(String recordId);

    @Modifying
    @Query("delete from DiathesisRecordMessage where recordId in ?1")
    void deleteByRecordIds(List<String> recordIds);

    @Query("from DiathesisRecordMessage where recordId = ?1 and leaveMsgPeopleId=?2")
    DiathesisRecordMessage findByRecordIdAndLeaveMsgPeopleId(String recordId, String userId);
}
