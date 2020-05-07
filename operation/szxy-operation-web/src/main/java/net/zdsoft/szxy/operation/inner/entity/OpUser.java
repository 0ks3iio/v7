package net.zdsoft.szxy.operation.inner.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;

/**
 * @author shenke
 * @since 2019/1/9 下午3:20
 */
@Data
@Entity
@Table(name = "op_user")
public class OpUser {

    public static final String ADMIN_USER_NAME = "admin";

    @Id
    private String id;
    private String username;
    private String password;
    private String realName;
    private String phone;
    private String email;
    @Temporal(TemporalType.TIMESTAMP)
    private Date creationTime;

    private Integer state;
    private Integer isDeleted;
    private Integer sex;

    /**
     * 该用户单独授权的行政区划
     */
    private String authRegionCode;
}
