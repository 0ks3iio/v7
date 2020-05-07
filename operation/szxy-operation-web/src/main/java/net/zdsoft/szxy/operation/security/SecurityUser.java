package net.zdsoft.szxy.operation.security;

import java.util.Collections;
import java.util.Set;

/**
 * @author shenke
 * @since 2019/1/11 下午1:43
 */
public interface SecurityUser  {

    /**
     * 登录用户自动添加的角色
     */
    String LOGINED = "ROLE_LOGIN";

    /**
     * 手机号码
     * @return 可能为空，取决于OpUser的值
     */
    String getPhone();

    /**
     * 真实姓名
     * @return 可能为空，具体取决于OpUser的数据
     */
    String getRealName();

    /**
     * 邮箱
     */
    String getEmail();

    /**
     * 用户名
     */
    String getUsername();

    /**
     * ID
     */
    String getId();

    /**
     * 性别
     * @return
     */
    Integer getSex();

    /**
     * 获取有权限的行政区划集合
     * @return may Collections.emptySet();
     */
    default Set<String> getAuthRegions() {
        return Collections.emptySet();
    }
}
