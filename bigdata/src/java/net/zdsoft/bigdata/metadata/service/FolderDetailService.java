package net.zdsoft.bigdata.metadata.service;

import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.bigdata.metadata.entity.FolderDetail;

import java.util.List;
import java.util.stream.Stream;

public interface FolderDetailService extends BaseService<FolderDetail, String> {


    /**
     * 根据用户权限查询文件夹内容
     * @param unitId
     * @param userId
     * @param folderIds (为空查询所有)
     * @return
     */
    List<FolderDetail> findAllAuthorityFolderDetail(String unitId, String userId, String[] folderIds);

    /**
     * 根据用户权限查询文件夹内容
     * @param unitId
     * @param userId
     * @param businessName
     * @return
     */
    List<FolderDetail> findAllAuthorityFolderDetailByBusinessName(String unitId, String userId, String businessName);

    void deleteByBusinessId(String businessId);

    void deleteByBusinessIdIn(String [] businessIds);

    Integer getMaxOrderIdByFolderId(String folderId);

    /**
     * 查询指定文件夹内容
     * @param folderIds
     * @return
     */
    List<FolderDetail> findAllFolderDetailByFolderId(String[] folderIds);

    /**
     * 查询用户有权限查看的数量
     * @param unitId
     * @param userId
     * @param businessType
     * @return
     */
    long countAllAuthorityFolderDetail(String unitId, String userId, Integer businessType);

    /**
     * 查询文件夹内容
     * @param businessName
     * @return
     */
    List<FolderDetail> findAllFolderDetailByBusinessName(String businessName);

    /**
     * 查询有权限的最新20条
     * @param unitId
     * @param userId
     * @return
     */
    List<FolderDetail> findRecentAuthorityFolderDetail(String unitId, String userId);

    /**
     * 查询所有的最新20条
     * @return
     */
    List<FolderDetail> findRecentFolderDetail();

    List<String> getCharts(String unitId, String userId);

}
