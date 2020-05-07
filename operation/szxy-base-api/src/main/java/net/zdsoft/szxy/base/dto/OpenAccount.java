package net.zdsoft.szxy.base.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 开通账号
 * @author shenke
 * @since 2019/3/21 下午1:33
 */
@Data
public class OpenAccount implements Serializable {

    /**
     * family#id 或者 student#id
     */
    private String id;
    /**
     * 用户名应当同时符合passport的数据标准（如果对接passport）
     */
    private String username;
    /**
     * 未加密的密码
     */
    private String password;
    /**
     * 用户所属类型
     */
    private Integer ownerType;

    private String unitId;

    private String realName;

    private String regionCode;

    private String classId;

    private Integer sex;

    private String identityCard;

    private Date birthday;

    private Integer enrollYear;
}
