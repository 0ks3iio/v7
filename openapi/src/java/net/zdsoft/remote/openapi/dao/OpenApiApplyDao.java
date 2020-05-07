package net.zdsoft.remote.openapi.dao;

import java.util.Date;
import java.util.List;

import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.remote.openapi.entity.OpenApiApply;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface OpenApiApplyDao extends BaseJpaRepositoryDao<OpenApiApply, String> {

    @Query("select type from OpenApiApply where status = ?1 and developerId = ?2")
    public List<String> findInterfaceTypes(int status, String developerId);

    /**
     * 根据开发者查找所有申请的接口类型
     * 
     * @author chicb
     * @param developerId
     * @return
     */
    public List<OpenApiApply> findByDeveloperId(String developerId);

    /**
     * 根据申请状态和开发者编号s查询申请接口信息
     * 
     * @author chicb
     * @param status
     * @param developerIds
     */
    public List<OpenApiApply> findByStatusAndDeveloperIdIn(int status, String[] developerIds);

    /**
     * 根据开发者ids批量查询
     * 
     * @author chicb
     * @param developerIds
     * @return
     */
    public List<OpenApiApply> findByDeveloperIdIn(String[] developerIds);

    /**
     * 删除申请表的接口
     * 
     * @author chicb
     * @param type
     * @param developerId
     */
    @Modifying
    @Query("delete from OpenApiApply where type in ?1 and developerId =?2")
    public void deleteApply(String[] type, String developerId);

    /**
     * 批量修改申请状态，根据 id
     * 
     * @author chicb
     * @param modifyApplys
     */
    @Modifying
    @Query("update OpenApiApply set status=?1,modifyTime=?2 where id in ?3")
    public void batchUpdateStatus(int status, Date modifyTime, String[] ids);

    /**
     * 批量修改申请状态，根据 types 和 developerId
     * 
     * @author chicb
     * @param status
     * @param date
     * @param developerId
     * @param types
     */
    @Modifying
    @Query("update OpenApiApply set status=?1,modifyTime=?2 where developerId=?3 and type in ?4")
    public void updateStatus(int status, Date date, String developerId, String[] types);

    @Query("select type from OpenApiApply where developerId = ?1 and status in ?2")
	public List<String> findByDeveloperIdAndStatusIn(String developerId, int[] status);

    @Query("from OpenApiApply where type = ?1 and status in ?2")
	public List<OpenApiApply> findByTypeAndStatusIn(String type, int[] status);

    @Query("from OpenApiApply where developerId = ?1 and type in ?2")
	public List<OpenApiApply> findByDeveloperIdAndTypeIn(String developerId,
			String[] types);

}
