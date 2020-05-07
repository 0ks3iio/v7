package net.zdsoft.bigdata.metadata.service;

import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.bigdata.data.exceptions.BigDataBusinessException;
import net.zdsoft.bigdata.metadata.entity.Folder;
import net.zdsoft.bigdata.metadata.entity.FolderDetail;
import net.zdsoft.bigdata.metadata.entity.FolderEx;

import java.util.List;
import java.util.Map;

public interface FolderService extends BaseService<Folder, String> {

    Map<String,List<Folder>> findFolderMapByParentId(String parentId);

    /**
     * 查询目录包含的文件夹
     * @return (key:目录id, value:文件夹)
     */
    Map<String,List<Folder>> findAllFolderForDirectory();

    List<Folder> findAllFolderByParentId(String parentId, Integer folderType);

    void saveFolder(Folder folder) throws BigDataBusinessException;

    List<Folder> findFolderByUsage(Integer usage);

    /**
     * 根据用户权限查询指定文件夹内容
     * @param unitId
     * @param userId
     * @param folderIds (空是查询所有)
     * @return
     */
    Map<String, List<FolderDetail>> findFolderDetailMap(String unitId, String userId, String[] folderIds);

    /**
     * 所有目录树
     * @return
     */
    List<FolderEx> findFolderTree();

    /**
     * 有权限的目录树
     * @return
     */
    List<FolderEx> findAuthFolderTree(String unitId, String userId);

    void updateFolderName(Folder folder) throws BigDataBusinessException;

    /**
     * 删除目录或者文件夹
     * @param folderId
     * @param folderType 类型（1、目录 2、文件夹）
     * @throws BigDataBusinessException
     */
    void deleteFolder(String folderId, Integer folderType) throws BigDataBusinessException;

    Integer getMaxOrderIdByParentId(String parentId);
}
