package net.zdsoft.basedata.service;

import java.io.IOException;
import java.util.List;

import net.zdsoft.basedata.dto.AttFileDto;
import net.zdsoft.basedata.entity.Attachment;

public interface AttachmentService extends BaseService<Attachment, String> {
    /**
     * 查询带dirPath的Attachments
     * @param id
     * @return
     */
	public List<Attachment> findAttachmentDirPathById(String... id);
	/**
	 * 根据objectId查询带dirPath的Attachments
	 * @param objectId
	 * @return
	 */
	public List<Attachment> findAttachmentDirPathByObjId(String... objectId);
	/**
	 * 根据objectId和objectType查询带dirPath的Attachments
	 * @param objectType
	 * @param objectId
	 * @return
	 */
	public List<Attachment> findAttachmentDirPathByObjTypeAndId(String objectType,String... objectId);
		
	/**
     * 从临时目录获取文件，保存附件和附件文件，并删除临时文件fileDtos
     * 
     * @param fileDtos
     * @return
     * @throws IOException
     */
    public List<Attachment> saveAttachment(List<AttFileDto> fileDtos) throws Exception;
    
    /**
     * 从网络URL获取文件，保存附件和附件文件
     * 
     * @param fileDtos
     * @return
     * @throws IOException
     */
    public List<Attachment> saveAttachmentByUrls(List<AttFileDto> fileDtos) throws Exception;
    /**
     * 删除附件，文件
     * @param attachmentIds
     * @param nameSuffixs 同时删除的带后缀文件
     * @throws Exception
     */
    public void deleteAttachments(String[] attachmentIds,String[] nameSuffixs) throws Exception;
    
    public void updateFileNameById(String attachmentId,String filename);

}
