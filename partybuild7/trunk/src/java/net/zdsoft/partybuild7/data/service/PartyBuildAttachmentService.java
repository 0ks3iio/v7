package net.zdsoft.partybuild7.data.service;

import java.util.List;

import net.zdsoft.partybuild7.data.entity.PartyBuildAttachment;

public interface PartyBuildAttachmentService {

    public List<PartyBuildAttachment> getAttachmentsByObjectId(String objectId);

    public PartyBuildAttachment getAttachmentById(String id);
}
