package net.zdsoft.szxy.base.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

/**
 * @author shenke
 * @since 2019/3/22 上午11:43
 */
@Data
@Entity
@Table(name = "base_server_extension")
public class ServerExtension implements Serializable {

    @Id
    private String id;

    /**
     * 关联单位ID
     */
    private String unitId;
    /**
     * 关联base_server_code
     */
    private String serverCode;
    /**
     * 系统过期时间 （null，跟随单位时间）
     */
    private Date expireTime;
    /**
     * 系统使用性质 （试用、正式）
     */
    private Integer usingNature;
    /**
     * 系统使用状态 （停用、到期停用、正常）
     */
    private Integer usingState;
}
