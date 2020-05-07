package net.zdsoft.api.openapi.remote.openapi.vo;

import net.zdsoft.api.base.entity.eis.OpenApiApp;

/**
 * @author shenke
 * @since 2019/5/22 上午11:17
 */
public final class OpenApiAppDeveloperView {

    private String name;
    private String iconUrl;
    private String indexUrl;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

    public String getIndexUrl() {
        return indexUrl;
    }

    public void setIndexUrl(String indexUrl) {
        this.indexUrl = indexUrl;
    }

    public static OpenApiAppDeveloperView convert(OpenApiApp openApiApp, String fileUrl) {
        OpenApiAppDeveloperView view = new OpenApiAppDeveloperView();
        view.setIndexUrl(openApiApp.getIndexUrl());
        view.setName(openApiApp.getName());
        view.setIconUrl(fileUrl + "/" + openApiApp.getIconUrl());
        //view.setIconUrl("http://file.msyk.cn/upload/openapi/image/icon/2017/3/25/21291064120793586111739699150107.png");
        return view;
    }
}
