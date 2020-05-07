package net.zdsoft.szxy.base.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * @author shenke
 * @since 2019/3/20 下午4:45
 */
@Data
@Entity
@Table(name = "sys_role_perm")
public class RolePerm implements Serializable {

    @Id
    private String id;
    /**
     * 角色id
     */
    @Column(name = "roleid")
    private String roleId;
    /**
     * 模块id
     */
    @Column(name = "moduleid")
    private Integer modelId;
    @Column(name = "operids")
    private String operIds;
    private Integer type;
}
