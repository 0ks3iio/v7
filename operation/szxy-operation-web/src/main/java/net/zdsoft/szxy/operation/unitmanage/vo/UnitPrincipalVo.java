package net.zdsoft.szxy.operation.unitmanage.vo;

import lombok.Getter;
import lombok.Setter;
import net.zdsoft.szxy.base.entity.Unit;
import net.zdsoft.szxy.base.entity.UnitExtension;
import net.zdsoft.szxy.operation.unitmanage.entity.OpUnitPrincipal;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *  用于单位负责人管理，显示多个单位联系人
 * @author zhanWenze
 * @since 2019年4月2日
 */

@Getter
@Setter
public final class UnitPrincipalVo {

    /**
     *  单位相关信息 名称 unitId
     */

    private String id;

    private String unitName;

    /**
     *  单位扩展信息  过期时间  星级
     */
    private Integer starLevel;

    @Temporal(TemporalType.TIMESTAMP)
    private Date expireTime;

    private Long expireDay;

    /**
     *  单位联系人信息 最多3个联系人
     */
    private List<OpUnitPrincipal> unitPrincipals = new ArrayList<>(3);
}
