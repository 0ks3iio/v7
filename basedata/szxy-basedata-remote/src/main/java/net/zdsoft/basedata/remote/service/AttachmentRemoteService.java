package net.zdsoft.basedata.remote.service;

import java.io.IOException;

public interface AttachmentRemoteService {
	 /**
     * 查询带dirPath的Attachments
     * @param id
     * @return
     */
	public String findAttachmentDirPathById(String... id);
	/**
	 * 根据objectId查询带dirPath的Attachments
	 * @param objectId
	 * @return
	 */
	public String findAttachmentDirPathByObjId(String... objectId);
		
	/**
	 * 根据objectType和objectId查询带dirPath的Attachments
	 * @param objectType
	 * @param objectId
	 * @return
	 */
	public String findAttachmentDirPathByObjTypeAndId(String objectType,String... objectId);
	
	/**
     * 从临时目录获取文件，保存附件和附件文件，并删除临时文件fileDtos
     * 
     * @param fileDtos
     * @return
     * @throws IOException
     */
    public String saveAttachment(String fileDtoArr) throws Exception;
    /**
     * 删除附件，文件
     * @param attachmentIds
     * @param nameSuffixs 同时删除的带后缀文件（由裁剪或转换而成的）
     * @throws Exception
     */
    public void deleteAttachments(String[] attachmentIds,String[] nameSuffixs) throws Exception;
    
    public void updateFileNameById(String attachmentId,String filename);
    /**
     * 从网络URL获取文件，保存附件和附件文件
     * 
     * @param fileDtos
     * @return
     * @throws IOException
     */
    public String saveAttachmentByUrls(String fileDtos) throws Exception;
}
