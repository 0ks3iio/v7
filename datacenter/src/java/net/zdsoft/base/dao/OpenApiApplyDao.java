package net.zdsoft.base.dao;

import java.util.Date;
import java.util.List;

import net.zdsoft.base.entity.eis.OpenApiApply;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface OpenApiApplyDao extends BaseJpaRepositoryDao<OpenApiApply, String> {

    @Query("select type from OpenApiApply where status = ?1 and ticketKey = ?2")
    public List<String> fingByStatusAndTicketKey(int status, String ticketKey);

    /**
     * 根据开发者查找所有申请的接口类型
     * 
     * @author chicb
     * @param developerId
     * @return
     */
    @Query("from OpenApiApply where ticketKey = ?1")
    public List<OpenApiApply> findByTicketKey(String ticketKey);

    /**
     * 根据申请状态和开发者编号s查询申请接口信息
     * 
     * @author chicb
     * @param status
     * @param developerIds
     */
    @Query("from OpenApiApply where status = ?1 and ticketKey in ?1")
    public List<OpenApiApply> findByStatusAndTicketKeyIn(int status, String[] ticketKeys);

    /**
     * 根据开发者ids批量查询
     * 
     * @author chicb
     * @param developerIds
     * @return
     */
    @Query("from OpenApiApply where ticketKey in (?1)")
    public List<OpenApiApply> findByTicketKeyIn(String[] ticketKeys);

    /**
     * 删除申请表的接口
     * 
     * @author chicb
     * @param type
     * @param developerId
     */
    @Modifying
    @Query("delete from OpenApiApply where type in ?1 and ticketKey =?2")
    public void deleteApply(String[] type, String ticketKey);

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
    @Query("update OpenApiApply set status=?1,modifyTime=?2 where ticketKey=?3 and type in ?4")
    public void updateStatus(int status, Date date, String ticketKey, String[] types);

    @Query("select type from OpenApiApply where ticketKey = ?1 and status in ?2")
	public List<String> findByTicketKeyAndStatusIn(String ticketKey, int[] status);

    @Query("from OpenApiApply where type = ?1 and status in ?2")
	public List<OpenApiApply> findByTypeAndStatusIn(String type, int[] status);

    @Query("from OpenApiApply where ticketKey = ?1 and type in ?2")
	public List<OpenApiApply> findByTicketKeyAndTypeIn(String ticketKey,
			String[] types);

    @Query("from OpenApiApply where ticketKey = ?1 and (type = ?2 or interfaceId = ?3)")
	public List<OpenApiApply> findByTicketKeyAndTypeOrInterfaceId(String ticketKey, String type, String interfaceId);

    @Query("from OpenApiApply where ticketKey = ?1 and status = ?2")
	public List<OpenApiApply> findByTicketKeyAndStatus(String ticketKey,int status);

    @Modifying
    @Query("delete from OpenApiApply where ticketKey = ?1 and type in ?2")
	public void deleteByTicketAndTypeIn(String ticketKey, String[] type);

}
