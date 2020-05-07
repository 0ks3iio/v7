package net.zdsoft.api.openapi.remote.openapi.vo;

import net.zdsoft.api.base.entity.eis.OpenApiApp;
import net.zdsoft.api.base.enums.AppStatusEnum;
import net.zdsoft.api.base.enums.UnitClassEnum;
import net.zdsoft.api.base.enums.UserTypeEnum;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * @author shenke
 * @since 2019/5/21 下午8:00
 */
public final class OpenApiList {

    private String id;
    private String name;
    private String unitClasses;
    private String userTypes;
    private AppStatusEnum status;
    private String developerName;
    private String developerId;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUnitClasses() {
        return unitClasses;
    }

    public void setUnitClasses(String unitClasses) {
        this.unitClasses = unitClasses;
    }

    public String getUserTypes() {
        return userTypes;
    }

    public void setUserTypes(String userTypes) {
        this.userTypes = userTypes;
    }

    public AppStatusEnum getStatus() {
        return status;
    }

    public void setStatus(AppStatusEnum status) {
        this.status = status;
    }

    public static OpenApiList convert(OpenApiApp app) {
        OpenApiList vo = new OpenApiList();
        vo.setId(app.getId());
        vo.setName(app.getName());
        vo.setStatus(AppStatusEnum.from(app.getStatus()));
        String unitClasses = Arrays.stream(StringUtils.split(app.getUnitClasses(), OpenApiApp.SEPARATOR))
                .map(NumberUtils::toInt)
                .map(UnitClassEnum::getName)
                .collect(Collectors.joining(OpenApiApp.SEPARATOR));
        vo.setUnitClasses(unitClasses);
        String userTypes = Arrays.stream(StringUtils.split(app.getUserTypes(), OpenApiApp.SEPARATOR))
                .map(NumberUtils::toInt)
                .map(UserTypeEnum::getName)
                .collect(Collectors.joining(OpenApiApp.SEPARATOR));
        vo.setUserTypes(userTypes);
        vo.developerId = app.getDeveloperId();
        return vo;
    }


    public String getDeveloperId() {
        return developerId;
    }

    public void setDeveloperId(String developerId) {
        this.developerId = developerId;
    }

    public String getDeveloperName() {
        return developerName;
    }

    public OpenApiList setDeveloperName(String developerName) {
        this.developerName = developerName;
        return this;
    }
}
