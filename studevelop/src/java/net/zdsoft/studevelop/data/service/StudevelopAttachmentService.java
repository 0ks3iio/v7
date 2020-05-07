package net.zdsoft.studevelop.data.service;

import java.io.File;
import java.util.List;
import java.util.Map;

import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.studevelop.data.entity.StudevelopAttachment;

import org.springframework.web.multipart.MultipartFile;

/**
 * studevelop_attachment
 * @author 
 * 
 */
public interface StudevelopAttachmentService extends BaseService<StudevelopAttachment, String>{

	/**
	 * 保存附件
	 * @param attachment
	 * @param files
	 */
	public void saveAttachment(StudevelopAttachment attachment, MultipartFile file);
	
	/**
	 * 处理ios拍照旋转问题
	 * @param attachment
	 * @param multfile
	 * @param file 修正之后的file
	 */
	public void saveAttachment(StudevelopAttachment attachment, MultipartFile multfile, File file);
	
	/**
	 * 保存附件，
	 * @param attachment
	 * @param oriFile 原图
	 */
	public void saveAttachment(StudevelopAttachment attachment, File oriFile);
	
	/**
	 * 根据ids数组删除studevelop_attachment数据
	 * @param ids
	 * @return
	 */
	public Integer delete(String[] ids);
	
	public void deleteByObjIds(String[] objIds);

	/**
	 * 根据objId数组获取list
	 * @param objId
	 * @return
	 */
	List<StudevelopAttachment> findListByObjIds(String... objId);
	/**
	 * 根据objId数组获取list
	 * @param objId
	 * @return
	 */
	List<StudevelopAttachment> findListByObjIds(String acadyear,String semester,String... objId);
	/**
	 * 根据objId数组删除list
	 * @param objId
	 * @return
	 */
	Integer deleteByObjIds(String acadyear,String semester,String... objId);
	
	/**
	 * 根据objId数组获取map  key:objId, value:list
	 * @param objId
	 * @return
	 */
	Map<String, List<StudevelopAttachment>> findMapByObjIds(String...objId);

	public List<StudevelopAttachment> getAttachmentByObjId(String objId, String objType);
	
	public int findAttachmentNumByObjId(String objId, String objType);
	
	public void saveFile(String objId,String objType,String insertId);
}