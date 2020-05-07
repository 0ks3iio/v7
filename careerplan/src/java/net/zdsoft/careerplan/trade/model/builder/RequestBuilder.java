package net.zdsoft.careerplan.trade.model.builder;

import net.zdsoft.careerplan.trade.utils.GsonFactory;

/**
 * 
 */
public abstract class RequestBuilder {
    private String appAuthToken;
    private String notifyUrl;

    // 验证请求对象
    public abstract boolean validate();

    // 获取bizContent对象，用于下一步转换为json字符串
    public abstract Object getBizContent();

    // 将bizContent对象转换为json字符串
    public String toJsonString() {
        // 使用gson将对象转换为json字符串
        return GsonFactory.getGson().toJson(this.getBizContent());
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("RequestBuilder{");
        sb.append("appAuthToken='").append(appAuthToken).append('\'');
        sb.append(", notifyUrl='").append(notifyUrl).append('\'');
        sb.append('}');
        return sb.toString();
    }

    public String getAppAuthToken() {
        return appAuthToken;
    }

    public RequestBuilder setAppAuthToken(String appAuthToken) {
        this.appAuthToken = appAuthToken;
        return this;
    }

    public String getNotifyUrl() {
        return notifyUrl;
    }

    public RequestBuilder setNotifyUrl(String notifyUrl) {
        this.notifyUrl = notifyUrl;
        return this;
    }
}
