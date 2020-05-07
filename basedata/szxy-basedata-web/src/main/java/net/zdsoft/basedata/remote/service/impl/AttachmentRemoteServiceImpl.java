package net.zdsoft.basedata.remote.service.impl;

import java.util.List;

import net.zdsoft.basedata.dto.AttFileDto;
import net.zdsoft.basedata.remote.service.AttachmentRemoteService;
import net.zdsoft.basedata.service.AttachmentService;
import net.zdsoft.framework.entity.TR;
import net.zdsoft.framework.utils.SUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
@Service("attachmentRemoteService")
@com.alibaba.dubbo.config.annotation.Service
public class AttachmentRemoteServiceImpl implements AttachmentRemoteService {

	@Autowired
	private AttachmentService attachmentService;
	@Override
	public String findAttachmentDirPathById(String... id) {
		return SUtils.s(attachmentService.findAttachmentDirPathById(id));
	}

	@Override
	public String findAttachmentDirPathByObjId(String... objectId) {
		return SUtils.s(attachmentService.findAttachmentDirPathByObjId(objectId));
	}

	@Override
	public String saveAttachment(String fileDtoArr) throws Exception {
		List<AttFileDto> fileDtos = SUtils.dt(fileDtoArr,new TR<List<AttFileDto>>() {});
		return SUtils.s(attachmentService.saveAttachment(fileDtos));
	}
	
	@Override
	public String saveAttachmentByUrls(String fileDtoArr) throws Exception {
		List<AttFileDto> fileDtos = SUtils.dt(fileDtoArr,new TR<List<AttFileDto>>() {});
		return SUtils.s(attachmentService.saveAttachmentByUrls(fileDtos));
	}

	@Override
	public void deleteAttachments(String[] attachmentIds, String[] nameSuffixs)
			throws Exception {
		attachmentService.deleteAttachments(attachmentIds, nameSuffixs);
	}

	@Override
	public void updateFileNameById(String attachmentId, String filename) {
		attachmentService.updateFileNameById(attachmentId, filename);
	}

	@Override
	public String findAttachmentDirPathByObjTypeAndId(String objectType,String... objectId){
		return SUtils.s(attachmentService.findAttachmentDirPathByObjTypeAndId(objectType,objectId));
	}
}
