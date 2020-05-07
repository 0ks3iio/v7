package net.zdsoft.szxy.operation.unitmanage.vo;

import lombok.Getter;
import lombok.Setter;
import net.zdsoft.szxy.operation.unitmanage.entity.OpUnitPrincipal;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author zhanWenze
 * @since 2019年4月11日
 * 单位负责人编辑页面vo
 */

@Getter
@Setter
public class UnitPrincipalEditVo {
    private String id;
    /**
     * 单位名称
     */
    private String unitName;
    /**
     *  单位过期时间
     */
    @Temporal(TemporalType.TIMESTAMP)
    private Date expireTime;
    /**
     *  单位联系人信息 最多3个联系人
     */
    private List<OpUnitPrincipal> unitPrincipals = new ArrayList<>(3);
}
