package net.zdsoft.szxy.operation.unitmanage.service;

import net.zdsoft.szxy.operation.unitmanage.entity.OpUnitPrincipal;
import net.zdsoft.szxy.operation.unitmanage.vo.UnitPrincipalEditVo;
import net.zdsoft.szxy.operation.unitmanage.vo.UnitPrincipalVo;
import net.zdsoft.szxy.operation.unitmanage.dto.OpUnitPrincipalQueryDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Set;

/**
 * 单位负责人管理业务层
 * @author  zhanWenze
 * @since 2019年4月2日
 */
public interface OpUnitPrincipalService {
    /**
     *  根据单位查询单位负责人
     * @param unitId
     */
    List<OpUnitPrincipal> findUnitPrincipalsByUnitId(String unitId);

    /**
     *  根据星级、联系人或手机号查询查询单位负责人
     */

    Page<UnitPrincipalVo> findPageByParentId(Set<String> regionSet, String parentId, OpUnitPrincipalQueryDto opUnitPrincipalQueryDto, Pageable pageable);

    /**
     *  增加单位负责人(最多三个)
     * @return 添加单位联系人是否成功
     */

    void addUnitPrincipalsByUnit(List<OpUnitPrincipal> opUnitPrincipal);

    /**
     * 更新单位负责人
     * @param editVo
     */
    void updateUnitPrincipals(UnitPrincipalEditVo editVo);

    /**
     * 删除单位下的所有联系人
     */
    void delUnitPrincipalsByUnitId(String unitId);

}
