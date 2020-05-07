package net.zdsoft.szxy.operation.inner.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @author shenke
 * @since 2019/4/9 下午12:51
 */
@Getter
@Setter
public final class EditUserModule {

    private String moduleId;
    private String moduleName;
    private boolean hasAuth;

    private List<EditUserOperate> operates;
}
