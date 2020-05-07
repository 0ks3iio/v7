package net.zdsoft.szxy.operation.inner.permission.service.impl;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

/**
 * @author shenke
 * @since 2019/4/3 上午11:27
 */
@EqualsAndHashCode
class ModuleOperateMapper {

    @Getter
    @Setter
    private String moduleId;
    @Getter
    @Setter
    private String operateId;

    String createKey() {
        return moduleId + "_" + operateId;
    }
}
