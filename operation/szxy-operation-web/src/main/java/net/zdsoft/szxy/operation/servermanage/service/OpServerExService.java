package net.zdsoft.szxy.operation.servermanage.service;

import net.zdsoft.szxy.base.entity.ServerExtension;
import net.zdsoft.szxy.operation.servermanage.dto.ServerExtensionDto;
import net.zdsoft.szxy.operation.unitmanage.dto.EnableServerDto;
import net.zdsoft.szxy.operation.unitmanage.service.IllegalRenewalTimeException;

import java.util.Date;
import java.util.List;

/**
 * @author 张帆远
 * @since 2019/1/17  上午11:16
 */
public interface OpServerExService {

    /**
     * 停用指定子系统
     * @param id 子系统扩展id
     */
    void stopServer(String id,String operatorId) throws NoServerExtensionException;

    /**
     * 将指定子系统恢复为正常状态
     * @param id 子系统扩展id
     */
    void recoverServer(String id ,String operatorId) throws NoServerExtensionException;

    /**
     * 将指定子系统的性质由试用转为正式
     * @param id 子系统扩展id
     */
    void turnToOfficial(String id ,String operatorId) throws NoServerExtensionException;

    /**
     * 续期单位使用时间，不区分单位使用性质，已停用的单位无法续期
     * @param extendTime 续期终止时间
     * @param id 子系统扩展id
     * @throws NoServerExtensionException 无法根据子系统扩展id查询到扩展信息时抛出该异常信息
     * @throws IllegalRenewalTimeException 当续期时间不合法时抛出该异常信息
     */
    void renewal(Date extendTime, String id ,String operatorId) throws NoServerExtensionException, IllegalRenewalTimeException;

    /**
     * 获取所有可以授权的系统
     * @return
     */
    List<EnableServerDto> getAllEnableServers();

    /**
     * 根据单位ID查询系统名称，系统使用性质，系统过期时间
     * @param unitId   单位ID
     * @return   返回查询结果
     * @author  zhangfy
     */
    List<ServerExtensionDto> findSystemByUnitId(String unitId);



    /**
     * 查找某个单位未授权的系统
     * @param unitId
     * @return
     * @author panlf
     */
    List<EnableServerDto> unAuthorizeSystem(String unitId);

    /**
     * 对单位指定的系统授权
     * @param
     * @return
     * @author panlf
     */
    String saveAuthoringSystem(List<ServerExtension> opServerExtensions, String operatorId);

    /**
     * 批量更新系统
     * @param opServerExtensions
     * @param operatorId
     */
    void updateSystem(List<ServerExtension> opServerExtensions,String operatorId) throws NoServerExtensionException,IllegalRenewalTimeException ;
}
