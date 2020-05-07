package net.zdsoft.szxy.operation.unitmanage.entity;

import lombok.Data;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import javax.persistence.*;
import java.io.Serializable;

/**
 *  单位负责人实体类
 * @author zhanWenze
 * @since 2019年4月2日
 */

@Data
@Entity
@Table(name="op_unit_principal")
public class OpUnitPrincipal implements Serializable {

    @Id
    private String id;
    /**
     * 单位联系人类型
     * @see net.zdsoft.szxy.base.enu.UnitPrincipalType
     */
    @Column(length = 1)
    private Integer type;
    /**
     * 真实姓名
     */
    private String realName;
    /**
     * 联系电话
     */
    private String phone;
    /**
     * 备注
     */
    private String remark;
    /**
     * 绑定的单位id
     */
    private String unitId;

}
