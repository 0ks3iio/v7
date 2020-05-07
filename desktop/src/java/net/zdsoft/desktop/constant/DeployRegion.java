package net.zdsoft.desktop.constant;

import java.util.HashMap;
import java.util.Map;

import net.zdsoft.basedata.constant.BaseConstants;

/**
 * @author shenke
 * @since 2017.05.18
 */
public class DeployRegion {

    public static final String SYS_OPTION_REGION = BaseConstants.SYS_OPTION_REGION;

    public static final Map<String, String> DEPLOY_MAP = new HashMap<String, String>(10);

    /**
     * 首页title map key = sys_option SYSTEM.DEPLOY.REGION value is title
     */
    public static final Map<String, String> INDEX_TITLE_MAP = new HashMap<>(10);

    /**
     * body title default
     */
    public static final String DEFAULT_INDEX_TITLE = "智慧校园";

    //部署地区
    public static final String DEPLOY_DAQIONG        = "daqing";
    public static final String DEPLOY_SDPY           = "sdpy";
    public static final String DEPLOY_DONGGUAN       = "DongGuan";
    public static final String DEPLOY_BINJIANG       = "BinJiang";
    public static final String DEPLOY_LASA           = "xizang";
    public static final String DEPLOY_YUlING         = "yulin";
    public static final String DEPLOY_XIGU           = "xigu";
    public static final String DEPLOY_HUNANXINSHAO   = "hnxs";
    
    public static final String DEPLOY_XINGYUN        = "xingyun";
    
    public static final String DEPLOY_LONGYOU       = "longYou";
    
    public static final String DEPLOY_JIAFA       = "jiafa";
    /**
	 * 系统部署地区 海口
	 */
	public static final String DEPLOY__HAIKOU = "HaiKou";

    /**
     * 七台河
     * update SYS_OPTION set nowvalue='qitaihe' where iniid='SYSTEM.DEPLOY.REGION';
     * commit;
     */
    public static final String DEPLOY_QTH            = "qitaihe";

    /**
     * 天长
     */
    public static final String DEPLOY_TIANCHANG     = "tianchang";

    static {

        DEPLOY_MAP.put(DEPLOY_DAQIONG,      "大庆教育云服务平台");
        DEPLOY_MAP.put(DEPLOY_SDPY,         "欢迎登录平邑县教育系统协同办公平台");

        INDEX_TITLE_MAP.put(DEPLOY_DAQIONG, "大庆教育云服务平台");
        INDEX_TITLE_MAP.put(DEPLOY_SDPY,    "平邑县教育系统协同办公平台");
    }

}
