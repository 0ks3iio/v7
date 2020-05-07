package net.zdsoft.officework.constant;

public class OfficeConstants {
	
	public static final String EIS_SCHEDULER_OFFICE_H3C_HEALTH_START = "eis.scheduler.office.h3c.health.start";
	public static final String OFFICE_H3C_ATTANCE_DEMO = "office.h3c.attance.demo";
	/**aes约定加密key*/
//	public static final String OFFICE_HEALTH_H3C_ENCRYPTKEY = "roS4PbqzOCYrxai3";
	/**请求url*/
	public static final String OFFICE_HEALTH_H3C_REQUEST_URL = "office.health.h3c.request.url";
	/**请求-摘要认证username*/
	public static final String OFFICE_HEALTH_H3C_REQUEST_USERNAME = "office.health.h3c.request.username";
	/**请求-摘要认证password*/
	public static final String OFFICE_HEALTH_H3C_REQUEST_PASSWORD = "office.health.h3c.request.password";
	/**设备序列号*/
	public static final String OFFICE_HEALTH_H3C_REQUEST_UNITID_DEVSN = "office.health.h3c.request.unitid.devSn";
	/**微课进出校推送url*/
	public static final String OFFICE_HEALTH_WEIKE_HEALTH_URL = "office.health.weike.health.url";
	
	/**出校*/
	public static final int OFFICE_HEALTH_OUT = 0;
	/**进校*/
	public static final int OFFICE_HEALTH_IN = 1;
	
	/**统计的是一天的数据*/
	public static final int OFFICE_HEALTH_COUNT_TYPE_1 = 1;
	/**统计的是1小时的数据*/
	public static final int OFFICE_HEALTH_COUNT_TYPE_2 = 2;
	
	//2017-11-13 duhc
	/**
	 * 统计数据类型
	 */
    public static final Integer HEALTH_TYPE_DAY = 1;
    public static final Integer HEALTH_TYPE_HOURS = 2;
    /**
     * 统计的时间范围 日 周 月
     */
    public static final String HEALTH_DATE_DAY= "1";
    public static final String HEALTH_DATE_WEEK = "2";
    public static final String HEALTH_DATE_MONTH = "3";
    
    //数据来源，01华三，02天波（其他第三方推送），03海康
    public static final String HEALTH_DATA_H3C= "01";
    public static final String HEALTH_DATA_TB = "02";
    public static final String HEALTH_DATA_HK = "03";
    /**
     * 健康数据类型
     */
    public static final String IN_OUT = "01";//进出校
    
    //两个字节取值，高位字节表示运动系数，低位字节表示具体的心率值，当心率值为0时表示心率采样超时，为1时表示未佩戴，为2时表示运动，其余值表示心率的值，单位是（次/分钟）
    public static final String SPORT_HEART = "50";
    public static final String SPORT_WALK = "51";//以小时为单位的步数累计
    public static final String SPORT_RUN = "52";
//    public static final String SPORT_NAP = "53";
//    private static final String SPORT_SLEEP = "55";
    //以5分钟为单位的总睡眠时长，数值为两个字节，高位表示有效睡眠时长，低位表示总睡眠时长。比如值是20570，先转成16进制是0x505A,有效睡眠时长为0x50*5=400分钟
    // 总睡眠时长0x5A*5=450分钟。特例：如果收到的数据值为65280(0xFF00)表示未佩戴手环
    public static final String SPORT_NAPNEW = "57";
    //以5分钟为单位的总睡眠时长，数值为两个字节，高位表示有效睡眠时长，低位表示总睡眠时长。比如值是20570，先转成16进制是0x505A,有效睡眠时长为0x50*5=400分钟
    //总睡眠时长0x5A*5=450分钟。特例：如果收到的数据值为65280(0xFF00)表示未佩戴手环
    public static final String SPORT_SLEEPNEW = "58";
    public static final String BATTER_LV = "59";
    public static final String HARDWARE_VERSION = "5B";
    public static final String SOFTWARE_VERSION = "5A";
    public static final String FLOWER = "5C";
    public static final String DEVICERESETFLAG = "5F";
    public static final String REALTIME_WALK = "6F";
    public static final String GROWTH_HEIGHT = "71";
    public static final String GROWTH_WEIGHT = "72";
    public static final String BODY_TEMPERATURE = "73";
//    private static final String BLOODPRESSURE_LOW = "80";
//    private static final String BLOODPRESSURE_HIGH = "81";
//    private static final String BLOODPRESSURE_HEART = "82";
    
    public static final int NOT_WORN_VALUE  = 65280;
    
    
    public static final String DELETE_OLD_DATA_REDIS_MSG = "delete-old-data-redis-msg";
    /**
     * redis锁前缀
     */
    public static final String CLOCK_IN_REDIS_LOCK_PREFIX = "clock-in-redis-prefix-officework";
    public static final String LOG_ID_REDIS_PREFIX = "log-id-prefix";
    
    /**
     * 设备类型，01为话机，02为健康ap,03海康门禁,04h3c设备（场景id-学校id）
     */
    public static final String HEALTH_DEVICE_TYPE_01 = "01";
    public static final String HEALTH_DEVICE_TYPE_02 = "02";
    public static final String HEALTH_DEVICE_TYPE_03 = "03";
    public static final String HEALTH_DEVICE_TYPE_04 = "04";
}
