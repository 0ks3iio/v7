/* 
 * @(#)Server.java    Created on 2017年2月27日
 * Copyright (c) 2017 ZDSoft Networks, Inc. All rights reserved.
 * $Id$
 */
package net.zdsoft.system.dao.server;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.system.entity.server.Server;

/**
 * @author cuimq
 * @version $Revision: 1.0 $, $Date: 2017年2月27日 上午11:16:11 $
 */
public interface ServerDao extends BaseJpaRepositoryDao<Server, Integer> {

    @Query(value = "from Server where code in (?1) and isDeleted=0")
    List<Server> getServersByCodes(String[] codes);

    @Query("From Server where orderType in (?1) and status = ?2 order by orderType")
    public List<Server> findByOrderType(Integer[] orderTypes, int status);

    /**
     * @param serverCode
     * @return
     */
    @Query("From Server where isDeleted = 0 and serverCode = ?1")
    Server findByServerCode(String serverCode);

    @Query("From Server where id in ?1 and status=?2 and isDeleted=0")
    List<Server> findServerList(Integer[] ids, int status);

    @Query("From Server where orderType in ?1 and status=?2 and isDeleted=0 and isVisible=1 order by name")
    List<Server> findByOrderTypeAndStatus(Integer[] orderTypes, int status);

    @Query("From Server where devId in ?1 and isDeleted = 0")
    List<Server> findAppsByDevIds(String[] devIds);

    @Query("From Server where devId = ?1 and isDeleted = 0")
    List<Server> findServersByDevId(String devId);

    @Modifying
    @Query("update Server set isDeleted = 1 where systemId = ?1")
    void deleteApp(String systemId);

    @Modifying
    @Query("update Server set isDeleted = 1 where id = ?1")
    void deleteAppByAppId(int id);

    @Query("From Server where systemId = ?1")
    Server findAppById(String systemId);

    @Modifying
    @Query("update Server set orderType = ?1 where id = ?2")
    void updateOrderType(int orderType, int id);

    @Modifying
    @Query("update Server set status = ?1 where systemId = ?2")
    void updateAppStatus(int status, String systemId);

    @Modifying
    @Query("update Server set status = ?1,modifyTime = ?2,applyTime=?3 where systemId = ?4")
    void updateAppStatusAndModifyTimeAndApplyTime(int status, Date modifyTime, Date applyTime, String systemId);

    @Modifying
    @Query("update Server set status = ?1 where id = ?2")
    void updateAppStatus(int status, int id);

    @Modifying
    @Query("update Server set status = ?1,modifyTime = ?2,applyTime=?3  where id = ?4")
    void updateAppStatusAndModifyTimeAndApplyTime(int status, Date modifyTime, Date applyTime, int id);

    @Modifying
    @Query("update Server set status = ?1,onlineTime = ?2  where id = ?3")
    void updateAppStatusAndOnlineTime(int status, Date onlineTime, int id);

    @Modifying
    @Query("update Server set status = ?1,auditTime = ?2  where id = ?3")
    void updateAppStatusAndAuditTime(int status, Date auditTime, int id);

    @Modifying
    @Query("update Server set status = ?1,modifyTime = ?2  where id = ?3")
    void updateAppStatusAndModifyTime(int status, Date modifyTime, int id);

    @Query("select count(s) from Server as s where name=?1 and isDeleted = 0")
    int countAppByName(String appName);

    /**
     * 仅用于外部AP新增
     * @return
     */
    @Query("select case when max(serverTypeId) >= 900 then  max(serverTypeId) else 900 end from Server")
    int getMaxServerTypeId();

    @Query("select max(orderId) from Server ")
    int getMaxOrderId();

    @Modifying
    @Query("update Server set name = ?1,description = ?2,icon=?3,iconUrl =?4,indexUrl =?5,verifyUrl=?6,invalidateUrl=?7,unitType=?8,userType=?9,sections=?10,modifyTime=?11,protocol=?12,domain=?13,port=?14,context=?15 where systemId=?16")
    void updateAppInfo(String name, String description, String icon, String iconUrl, String indexUrl, String verifyUrl,
                       String invalidateUrl, String unitType, String userType, String sections, Date modifyTime, String protocol,
                       String domain, int port, String context, String systemId);

    @Modifying
    @Query("update Server set name = ?1,description = ?2,icon=?3,iconUrl =?4,indexUrl =?5,verifyUrl=?6,invalidateUrl=?7,unitType=?8,userType=?9,sections=?10,modifyTime=?11,protocol=?12,domain=?13,port=?14,context=?15,status=?16,openType=?17 where id=?18")
    void updateAppInfo(String name, String description, String icon, String iconUrl, String indexUrl, String verifyUrl,
                       String invalidateUrl, String unitType, String userType, String sections, Date modifyTime, String protocol,
                       String domain, int port, String context, int status, int openType, int id);

    @Modifying
    @Query("update Server set name = ?1,description = ?2,icon=?3,iconUrl =?4,indexUrl =?5,verifyUrl=?6,invalidateUrl=?7,unitType=?8,userType=?9,sections=?10,modifyTime=?11,orderType=?12,protocol=?13,domain=?14,port=?15,context=?16,status=?17, openType=?18 where id=?19")
    void updateAppInfo(String name, String description, String icon, String iconUrl, String indexUrl, String verifyUrl,
                       String invalidateUrl, String unitType, String userType, String sections, Date modifyTime,
                       Integer orderType, String protocol, String domain, int port, String context, int status, int openType,
                       int id);

    @Modifying
    @Query("update Server set status = 0 where status=1 and serverClass=1")
    void updateAllInnerServerStop();

    @Modifying
    @Query("update Server set status = 1 where code in (?1)")
    void updateInnerServerActiveByCodes(String[] codes);

    @Modifying
    @Query("update Server set serverClass = 1 where code in (?1) ")
    void updateServerClassToInner(String[] codes);

    @Modifying
    @Query("update Server set orderType=?2 where code in (?1) ")
    void updateOrderType(String[] codes, int orderType);

    /**
     * 查找没有模块的子系统
     * @return
     */
    @Query(nativeQuery=true, value="select a.* from base_server a where not exists( select 1 from sys_model b where a.sub_Id = b.subsystem) and a.status = 1")
    List<Server> findNonModelsServer();

    @Query("From Server where subId = ?1 and isDeleted = 0")
	public Server findBySubId(Integer subId);

    @Query("From Server where indexUrl = ?1 and isDeleted = 0 and status = 1")
	public Server findByIndexUrl(String url);

    /**
     * 正常状态的子系统
     * @param subIds
     * @return
     */
    @Query(
            value = "from Server where subId in (?1) and isDeleted=0 and status=1"
    )
    List<Server> findBySubIds(Integer[] subIds);
}
