package net.zdsoft.partybuild7.data.dao;

import java.util.List;

import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.partybuild7.data.entity.PartyBuildAttachment;

import org.springframework.data.jpa.repository.Query;

public interface PartyBuildAttachmentDao extends BaseJpaRepositoryDao<PartyBuildAttachment,String> {

    @Query(nativeQuery = true,value = "select * from pb_attachment where object_id = ?1  order by upload_date")
    public List<PartyBuildAttachment> getAttachmentsByObjectId(String objectId);

    @Query(nativeQuery = true,value = "select * from pb_attachment where id = ?1 ")
    public PartyBuildAttachment getAttachmentById(String id);
}
