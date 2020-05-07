package net.zdsoft.szxy.operation.unitmanage.service;

import net.zdsoft.szxy.base.entity.Unit;
import net.zdsoft.szxy.base.entity.UnitExtension;
import net.zdsoft.szxy.base.exception.SzxyPassportException;
import net.zdsoft.szxy.base.exception.UnionCodeAlreadyExistsException;
import net.zdsoft.szxy.base.exception.UsernameAlreadyExistsException;
import net.zdsoft.szxy.operation.servermanage.service.NoServerExtensionException;
import net.zdsoft.szxy.operation.unitmanage.dto.UnitAddDto;
import net.zdsoft.szxy.operation.unitmanage.dto.UnitDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Date;
import java.util.List;

/**
 * @author yangkj
 * @since 2019/1/16
 */
public interface OpUnitService {

    /**
     * 停用指定单位
     * @param unitId 单位ID
     */
    void stopUnit(String unitId,String operatorId) throws NoUnitExtensionException;

    /**
     * 将指定单位恢复为正常状态
     * @param unitId 单位ID
     */
    void recoverUnit(String unitId,String operatorId) throws NoUnitExtensionException;

    /**
     * 将指定单位的性质由试用转为正式
     * @param unitId 单位ID
     */
    void turnToOfficial(String unitId,String operatorId) throws NoUnitExtensionException;

    /**
     * 变更单位使用时间为永久
     * @throws NoUnitExtensionException unit not exists
     */
    void updateExpireTimeToPermanent(String unitId,String operatorId) throws NoUnitExtensionException;

    /**
     * 续期单位使用时间，不区分单位使用性质，已停用的单位无法续期
     * @param extendTime 续期终止时间
     * @param unitId 需要续期的单位ID
     * @throws NoUnitExtensionException 无法根据指定的单位ID查询到扩展信息时抛出该异常信息
     * @throws IllegalRenewalTimeException 当续期时间不合法时抛出该异常信息
     */
    void renewal(Date extendTime, String unitId, String operatorId) throws NoUnitExtensionException, IllegalRenewalTimeException;

    /**
     * 续期服务时间（针对永久使用的单位）
     * @throws NoUnitExtensionException 无法根据指定的单位ID查询到扩展信息时抛出该异常信息
     * @throws IllegalRenewalTimeException 当续期时间不合法时抛出该异常信息
     */
    void renewalServiceTime(Date extendTime, String unitId) throws NoUnitExtensionException, IllegalRenewalTimeException;


    /**
     * @param unitId
     */
    UnitExtension findByUnitId(String unitId) throws NoUnitExtensionException;

    /**
     * dto大类实现3个的更新
     * @param unitAddDto
     */
     void updateUnitAndSchoolAndUnitEx(UnitAddDto unitAddDto,String operationId) throws UnionCodeAlreadyExistsException, IllegalRenewalTimeException, NoServerExtensionException;

    /**
     * 分页查询
     * @param parentId 父Id
     * @param unitType  单位类型
     * @param usingNature 单位性质
     * @param pageable 分页属性
     * @return
     */
    Page<UnitDto> findPageByParentId(String parentId, String unitType, String usingNature, Pageable pageable);


    void deleteUnit(String unitId);

    List<Unit> findByUnitClassAndUnitType(Integer unitClass, Integer unitType);

    void saveUnit(UnitAddDto unitAddDto, String operatorId) throws UnionCodeAlreadyExistsException, UsernameAlreadyExistsException, SzxyPassportException;

    void updateUnitStarLevel(String unitId, Integer unitStarLevel);
}
