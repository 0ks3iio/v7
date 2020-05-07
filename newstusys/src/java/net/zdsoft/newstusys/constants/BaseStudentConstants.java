package net.zdsoft.newstusys.constants;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.ArrayUtils;

import net.zdsoft.system.entity.mcode.McodeDetail;

/**
 * Created by Administrator on 2018/3/3.
 */
public class BaseStudentConstants {
    public static final String SYSTEM_DEPLOY_SCHOOL="SYSTEM.DEPLOY.SCHOOL";
    
    /**
     * 学生家长端编辑
     */
    public static final String STUSYS_COLS_TYPE_STUDENTEDIT = "studentedit";
    
    /**
     * 浙江诸暨
     */
    public static final String SYSDEPLOY_SCHOOL_ZJZJ = "zjzj";
	
    public static final String SOURCE_SN = "1";// 辖区内
    public static final String SOURCE_SNSW = "2";// 省内市外
    public static final String SOURCE_SW = "3";// 省外
    public static final String SOURCE_NONE = "4";// 无户口
    
    public static final String REGION_ZJZJ = "330681";
    
    public static final String COUNTRY_CHINA = "156";
    
	/**
     * 母亲
     */
    public static final String RELATION_MOTHER="52";
    /**
     * 父亲
     */
    public static final String RELATION_FATHER="51";
    /**
     * 是监护人
     */
    public static final int RELATION_IS_GUARDIAN =1;

    /**
     * 附件文件夹名称
     */
    public final static String PICTURE_FILEPATH = "photo";

    public static final String PERMISSION_TYPE_STUWORK = "1"; //德育
    public static final String PERMISSION_TYPE_STUDENT  = "2"; //学籍
    
    public static final String NOWSTATE_DJ = "40"; 
	
	/**
	 * 证件类型-身份证
	 */
	public static final String IDCARDTYPE_ID = "1";
	
	/**
	 * 家长关系-母亲
	 */
	public static final String GX_QT = "80";
	
	public static final int SEX_MALE = 1;
	public static final int SEX_FEMALE = 2;
	
	
	/**
	 * 学生异动类型
	 */
	public static final String YD_RJ="07";//入境1111
	public static final String YD_XQNZR="08";//县区内转入1111
	public static final String YD_XX="11";//休学
	public static final String YD_FX="12";//复学
	public static final String YD_ZC = "21";// 转出
	public static final String YD_ZR = "22";// 转入
	public static final String YD_SNZR="26";//省内转入1111
	public static final String YD_SWZR="27";//省外转入1111
	public static final String YD_HKXB="28";//户口新报1111
	public static final String YD_YNJBB="29";//一年级补报1111
	public static final String YD_CJ="35";//出境1111
	public static final String YD_ZWXQN="36";//转往县区内1111
	public static final String YD_ZWSN="37";//转往省内1111
	public static final String YD_ZWSW="38";//转往省外1111
	public static final String YD_DJ = "40";//登记
	public static final String YD_SW="51";//死亡
	public static final String YD_QTZJ="89";//其他增加
	public static final String YD_QTJS="99";//其他(减少)
	
	/**
	 * 异动类型分类
	 */
	public static final String FLOWTYPE_ENTER = "enter"; // 入校操作
	public static final String FLOWTYPE_LEAVE = "leave"; // 离校操作
	
	public static final int BUSINESSTYPE_ABNORMAL4 = 4;
	
	public static Map<String, String[]> belongMap = new HashMap<String, String[]>();
	
	/**
	 * 根据异动类型取所属类别
	 * @param flowtype 异动类型
	 */
	public static String[] getFlowTypeBelong(String flowtype) {
		if (belongMap.isEmpty()) {
			String[] enterType = { YD_FX, YD_ZR, YD_RJ, YD_XQNZR, YD_SNZR, YD_SWZR, YD_HKXB, YD_YNJBB, YD_QTZJ };
			String[] leaveType = { YD_XX, YD_ZC, YD_CJ, YD_ZWXQN, YD_ZWSN, YD_ZWSW, YD_SW, YD_QTJS };
			belongMap.put(FLOWTYPE_ENTER, enterType);
			belongMap.put(FLOWTYPE_LEAVE, leaveType);
		}
		return belongMap.get(flowtype);
	}
	
	/**
	 * 筛选范围内的值
	 * @param flowTypes
	 * @param withIns thisId范围
	 * @return
	 */
	public static List<McodeDetail> findInUsingMcode(List<McodeDetail> flowTypes, String[] withIns){
		if(flowTypes == null) {
			flowTypes = new ArrayList<>();
			return flowTypes;
		}
		if(ArrayUtils.isEmpty(withIns)) {
			flowTypes.clear();
		} else {
			Iterator<McodeDetail> fit = flowTypes.iterator();
			while(fit.hasNext()) {
				McodeDetail md = fit.next();
				if(!ArrayUtils.contains(withIns, md.getThisId())) {
					fit.remove();
				}
			}
		}
		return flowTypes;
	}
	
}
