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
public class GanSuConstant implements EnvironmentAware, InitializingBean {

    private Logger logger = LoggerFactory.getLogger(GanSuConstant.class);

    private Environment environment;

    @Override
    public void afterPropertiesSet() throws Exception {
        GanSuConstant.GS_START_URL = environment.getProperty("gansu_sync_data_url");
        GanSuConstant.GS_APPID = environment.getProperty("gansu_app_id");
        GanSuConstant.GS_MSID = environment.getProperty("gansu_ms_id");
        GanSuConstant.GS_CLIENT_SECRET = environment.getProperty("gansu_client_secret");
        GanSuConstant.GS_LOGIN_SAML_URL = environment.getProperty("gansu_login_saml_url");
        GanSuConstant.GS_SSO_TYPE = environment.getProperty("gansu_sso_type");

        logger.warn("GanSuConstant load dynamic property from environment GS_START_URL [{}]", GS_START_URL);
        logger.warn("GanSuConstant load dynamic property from environment GS_APPID [{}]", GS_APPID);
        logger.warn("GanSuConstant load dynamic property from environment GS_MSID [{}]", GS_MSID);
        logger.warn("GanSuConstant load dynamic property from environment GS_CLIENT_SECRET [{}]", GS_CLIENT_SECRET);
        logger.warn("GanSuConstant load dynamic property from environment GS_LOGIN_SAML_URL [{}]", GS_LOGIN_SAML_URL);
        logger.warn("GanSuConstant load dynamic property from environment GS_SSO_TYPE [{}]", GS_SSO_TYPE);
    }

    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }

    /**
     * 甘肃账号的前缀
     */
    public static final String GS_BEFORE_USERNAME_VALUE = "gc_";
    public static String GS_START_URL;// = Evn.getString("gansu_sync_data_url");
    public static final String GS_TOKEN_URL = GS_START_URL + "/auth/oauth/token";
    public static final String GS_CALL_URL = GS_START_URL + "/auth/api/pushUserBasic/1.0";
    public static final String GS_PUSH_REMOVE_URL = GS_START_URL + "/auth/api/pushRemoveUser/1.0";
    public static String GS_APPID;// = Evn.getString("gansu_app_id");
    public static String GS_MSID;// = Evn.getString("gansu_ms_id");
    public static String GS_CLIENT_SECRET;// = Evn.getString("gansu_client_secret");

    public static String GS_LOGIN_SAML_URL;// = Evn.getString("gansu_login_saml_url");
    //	#SSO类型:samlsso/lamsso
    public static String GS_SSO_TYPE;// = Evn.getString("gansu_sso_type");

    public static final String GS_SYS_DOMAIN_URL = "http://192.168.0.144";
    //	SAMLResponse
    public static final String GS_SAML_RESPONSE = "SAMLResponse";

    public static final String GS_EDU_ADMIN_TEACHER = "1"; //教育局管理者
    public static final String GS_SCH_ADMIN_TEACHER = "2"; //学校管理者
    public static final String GS_SCH_TEACHER = "21"; //学校普通教师
    public static final String GS_SCH_STUDENT = "22"; //学校普通学生

    public static final String GS_DELETE_USER_LASTTIME_KEY = "syncdata.gansu.delete.user.lasttime";
}
