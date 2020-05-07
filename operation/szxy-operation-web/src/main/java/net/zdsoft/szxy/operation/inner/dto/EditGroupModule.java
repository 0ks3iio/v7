package net.zdsoft.szxy.operation.inner.dto;

import lombok.Data;

import java.util.List;

/**
 * @author shenke
 * @since 2019/4/10 上午11:19
 */
@Data
public final class EditGroupModule {

    private String moduleId;
    private String moduleName;
    private boolean hasAuth;
    private String regionCode;

    private List<EditUserOperate> operates;
}
