package net.zdsoft.basedata.constant.custom;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.env.Environment;

@Configuration
@Lazy(false)
public class JiaFaConstant implements EnvironmentAware, InitializingBean {

    private Logger logger = LoggerFactory.getLogger(JiaFaConstant.class);

    private Environment environment;

    @Override
    public void afterPropertiesSet() throws Exception {
        JiaFaConstant.JF_START_URL = environment.getProperty("jiafa_hainan_prefix_url");
        JiaFaConstant.JF_APP_ID_VAL = environment.getProperty("jiafa_hainan_app_id");
        JiaFaConstant.JF_APP_KEY_VAL = environment.getProperty("jiafa_hainan_app_key");
        JiaFaConstant.JF_ORG_CODE_VAL = environment.getProperty("jiafa_hainan_orgcode");

        logger.warn("JiaFaConstant load dynamic from environment JF_START_URL [{}]", JF_START_URL);
        logger.warn("JiaFaConstant load dynamic from environment JF_APP_ID_VAL [{}]", JF_APP_ID_VAL);
        logger.warn("JiaFaConstant load dynamic from environment JF_APP_KEY_VAL [{}]", JF_APP_KEY_VAL);
        logger.warn("JiaFaConstant load dynamic from environment JF_ORG_CODE_VAL [{}]", JF_ORG_CODE_VAL);
    }

    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }

    private static String JF_START_URL;// = Evn.getString("jiafa_hainan_prefix_url");
    public static final String JF_GET_ACCESS_TOKEN_URL = JF_START_URL + "/auth/get-accesstoken";
    public static final String JF_GET_USER_INFO_URL = JF_START_URL + "/isbm/p/SSOGetUserInfo";

    public static String JF_APP_ID_VAL;// = Evn.getString("jiafa_hainan_app_id");
    public static String JF_APP_KEY_VAL;// = Evn.getString("jiafa_hainan_app_key");
    public static String JF_ORG_CODE_VAL;// = Evn.getString("jiafa_hainan_orgcode");
    public static final String JF_TOKEN_NAME = "token";

    /**
     * 账号的前缀
     */
    public static final String JF_BEFORE_USERNAME_VALUE = "jf_";

    public static final String JF_REDIRECT_URI = "/homepage/remote/openapi/jiafa/login";
}
