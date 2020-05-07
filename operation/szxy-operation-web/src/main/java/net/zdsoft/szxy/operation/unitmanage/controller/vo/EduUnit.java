package net.zdsoft.szxy.operation.unitmanage.controller.vo;

import lombok.Getter;
import lombok.Setter;

/**
 * 用于新增单位时，显示上级单位
 * @author shenke
 * @since 2019/3/6 下午4:05
 */
@Getter
@Setter
public final class EduUnit {

    private String id;
    private String unitName;
    private String regionCode;
    private String regionName;
}
