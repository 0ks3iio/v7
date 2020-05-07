package net.zdsoft.szxy.operation.inner.controller.vo;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * @author shenke
 * @since 2019/4/10 下午2:44
 */
@Getter
@Setter
public final class GroupVo {

    private String id;
    private String name;
    private String regionName;
    private String regionCode;

    private Integer userCount;
    private Date creationTime;
}
