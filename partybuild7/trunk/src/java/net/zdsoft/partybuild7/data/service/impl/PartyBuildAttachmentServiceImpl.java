package net.zdsoft.partybuild7.data.service.impl;

import java.util.List;

import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.partybuild7.data.dao.PartyBuildAttachmentDao;
import net.zdsoft.partybuild7.data.entity.PartyBuildAttachment;
import net.zdsoft.partybuild7.data.service.PartyBuildAttachmentService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("PartyBuildAttachmentService")
public class PartyBuildAttachmentServiceImpl extends BaseServiceImpl<PartyBuildAttachment , String> implements PartyBuildAttachmentService {

    @Autowired
    private PartyBuildAttachmentDao partyBuildAttachmentDao;
    @Override
    public List<PartyBuildAttachment> getAttachmentsByObjectId(String objectId) {
        return partyBuildAttachmentDao.getAttachmentsByObjectId(objectId);
    }

    @Override
    public PartyBuildAttachment getAttachmentById(String id) {
        return partyBuildAttachmentDao.getAttachmentById(id);
    }

    @Override
    protected BaseJpaRepositoryDao<PartyBuildAttachment, String> getJpaDao() {
        return partyBuildAttachmentDao;
    }

    @Override
    protected Class<PartyBuildAttachment> getEntityClass() {
        return PartyBuildAttachment.class;
    }
}
