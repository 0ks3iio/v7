package net.zdsoft.syncdata.custom.changzhi.constant;

import java.util.HashMap;
import java.util.Map;

import net.zdsoft.framework.entity.Json;
import net.zdsoft.framework.utils.SecurityUtils;

import org.apache.commons.codec.digest.DigestUtils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

/**
 * Designed By luf
 *
 * @author luf
 * @date 2019/6/24 16:29
 */
public class ChangZhiConstant {
	public static final String CZ_REGION = "1404";

	/** 用户名规则 */
	public static final String SYSTEM_NAME_EXPRESSION = "SYSTEM.NAME.EXPRESSION";
	
    public static final String CZ_SYNC_URL = "http://60.220.220.241:48383";

    public static final String CZ_APP_KEY = "ZHBG";
    public static final String CZ_SECRET_KEY = "MeECv2LXDdYcPkYHiW3Bcg==";
    
    public static final String CZ_STATE_NORMARL = "200";
    public static final String CZ_STATE_FAIL = "500";
    public static final String CZ_STATE_NONE = "100";
    
    public static final String DEFAULT_PASSWORD_VALUE = "zdsoft123";
    
    public static final String CZ_GETTEACHER_URL = "/ApiDev/WebAPI/GetAllTeacherByDepId.aspx";
    public static final String CHANG_ZHI_UPDATE_TIME_EDU_KEY = "CHANG_ZHI_UPDATE_TIME_EDU_KEY_update_time";
    public static final String CHANG_ZHI_UPDATE_TIME_SCHOOL_KEY = "CHANG_ZHI_UPDATE_TIME_SCHOOL_KEY_update_time";
    public static final String CHANG_ZHI_UPDATE_TIME_TEACHER_KEY = "CHANG_ZHI_UPDATE_TIME_TEACHER_KEY_update_time";

    public static final String CZ_SEC_YEY = "5";// 学段-幼儿园
    public static final String CZ_SEC_XX = "2";// 学段-幼儿园
    public static final String CZ_SEC_CZ = "1";// 学段-初中
    public static final String CZ_SEC_GZ = "3";// 学段-高中
    public static final String CZ_SEC_ZZ = "4";// 学段-中专

//	key=10	Value=学前教育
//			key=11	Value=小学
//			key=12	Value=小学教学点
//			key=21	Value=完全中学
//			key=22	Value=高级中学
//			key=23	Value=初级中学
//			key=24	Value=九年一贯制学校
//			key=25	Value=十二年一贯制学校
//			key=31	Value=调整后中等职业学校
//			key=32	Value=职业高中学校
//			key=33	Value=中等师范学校
//			key=91	Value=其他机构

    public static final Map<String, String> sectionTransMap = new HashMap<>();
    static{
        sectionTransMap.put("1", "2");
        sectionTransMap.put("2", "1");
        sectionTransMap.put("3", "3");
        sectionTransMap.put("4", "4");
        sectionTransMap.put("5", "0");
    }
    public static final Map<String, String> schTypeTransMap = new HashMap<>();

    static {
        schTypeTransMap.put("10", "111");
        schTypeTransMap.put("11", "211");
        schTypeTransMap.put("12", "218");

        schTypeTransMap.put("21", "341");
        schTypeTransMap.put("22", "342");
        schTypeTransMap.put("23", "311");
        schTypeTransMap.put("24", "312");
        schTypeTransMap.put("25", "345");

        schTypeTransMap.put("31", "361");
        schTypeTransMap.put("32", "365");
        schTypeTransMap.put("33", "363");

        schTypeTransMap.put("91", "419");

    }
    public static final Map<String, String> unitTypeTransMap = new HashMap<>();
    static {
        unitTypeTransMap.put("10", "5");

        unitTypeTransMap.put("11", "3");
        unitTypeTransMap.put("12", "3");

        unitTypeTransMap.put("21", "3");
        unitTypeTransMap.put("22", "3");
        unitTypeTransMap.put("23", "3");
        unitTypeTransMap.put("24", "3");
        unitTypeTransMap.put("25", "3");

        unitTypeTransMap.put("31", "3");
        unitTypeTransMap.put("32", "3");
        unitTypeTransMap.put("33", "4");

        unitTypeTransMap.put("91", "8");
    }
    /**
     * 获取Authorization
     * @param timeSpan 时间戳
     * @return
     */
    public static final String getAuth(String timeSpan) {
        // SHA256(appKey + SecretKey + timeSpan)
        String result = DigestUtils.sha256Hex(CZ_APP_KEY + CZ_SECRET_KEY + timeSpan);
        return result;
    }

    public static void main(String[] args) {
//		String sp = System.currentTimeMillis()+"";
//		System.out.println(sp);
//		System.out.println(getAuth(sp));
        String ss = "{\"State\":\"200\",\"unit_type\":[{\"Key\":\"MTA=\",\"Value\":\"5a2m5YmN5pWZ6IKy\"},{\"Key\":\"MTE=\",\"Value\":\"5bCP5a2m\"},{\"Key\":\"MTI=\",\"Value\":\"5bCP5a2m5pWZ5a2m54K5\"},{\"Key\":\"MjE=\",\"Value\":\"5a6M5YWo5Lit5a2m\"},{\"Key\":\"MjI=\",\"Value\":\"6auY57qn5Lit5a2m\"},{\"Key\":\"MjM=\",\"Value\":\"5Yid57qn5Lit5a2m\"},{\"Key\":\"MjQ=\",\"Value\":\"5Lmd5bm05LiA6LSv5Yi25a2m5qCh\"},{\"Key\":\"MjU=\",\"Value\":\"5Y2B5LqM5bm05LiA6LSv5Yi25a2m5qCh\"},{\"Key\":\"MzE=\",\"Value\":\"6LCD5pW05ZCO5Lit562J6IGM5Lia5a2m5qCh\"},{\"Key\":\"MzI=\",\"Value\":\"6IGM5Lia6auY5Lit5a2m5qCh\"},{\"Key\":\"MzM=\",\"Value\":\"5Lit562J5biI6IyD5a2m5qCh\"},{\"Key\":\"OTE=\",\"Value\":\"5YW25LuW5py65p6E\"}],\"PageSize\":\"200\",\"PageCount\":\"1\",\"RecordCount\":\"12\",\"CurrentPage\":\"1\"}";
        JSONObject js = Json.parseObject(ss);
        JSONArray ar = (JSONArray) js.get("unit_type");
        for (int i = 0; i < ar.size(); i++) {
            JSONObject po = (JSONObject) ar.get(i);
            String pid = po.getString("Key");
            String pn = po.getString("Value");
            System.out.print("key="+new String(SecurityUtils.decodeBase64(pid.getBytes()))+"\t");
            System.out.println("Value="+new String(SecurityUtils.decodeBase64(pn.getBytes())));
        }
    }
}
