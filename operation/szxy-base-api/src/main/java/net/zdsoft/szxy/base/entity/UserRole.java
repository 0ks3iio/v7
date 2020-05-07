package net.zdsoft.szxy.base.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author shenke
 * @since 2019/3/20 下午2:42
 */
@Data
@Entity
@Table(name = "sys_user_role")
public class UserRole {
    private static final long serialVersionUID = 1L;
    @Id
    private String id;

    @Column(name = "userid")
    private String userId;
    @Column(name = "roleid")
    private String roleId;


}
