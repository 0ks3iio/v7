package net.zdsoft.szxy.operation.inner.controller.vo;

import lombok.Getter;
import lombok.Setter;

/**
 * @author shenke
 * @since 2019/4/9 下午12:45
 */
@Setter
@Getter
public final class EditUserGroupVo {


    private String id;
    private String name;
    /**
     * 数据集范围（行政区划名称）
     */
    private String regionName;

    private boolean hasAuth;
}
