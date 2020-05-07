package net.zdsoft.desktop.enu;

import java.util.HashMap;
import java.util.Map;

/**
 * @author shenke
 * @since 2017.10.20
 */
public enum SystemNotifyType {

    FIVE_MINUTES(1, 5),
    FIFTEEN_MINUTES(3, 15),
    HALF_HOUR(4, 30),
    ONE_HOUR(5, 60),
    ONE_DAY(6, 1440),
    NONE(99, 0);

    private int type;
    private int minutes;

    SystemNotifyType(int type, int minutes) {
        this.type = type;
        this.minutes = minutes;
    }

    public int getType() {
        return type;
    }

    public int getMinutes() {
        return minutes;
    }

    private static Map<Integer, Integer> typeValueMap = new HashMap<>();
    private static Map<Integer, SystemNotifyType> typeMap = new HashMap<>();

    static {
        typeValueMap.put(1, 5);
        typeValueMap.put(3, 15);
        typeValueMap.put(4, 30);
        typeValueMap.put(5, 60);
        typeValueMap.put(6, 1440);
        typeMap.put(1, FIVE_MINUTES);
        typeMap.put(3, FIFTEEN_MINUTES);
        typeMap.put(4, HALF_HOUR);
        typeMap.put(5, ONE_HOUR);
        typeMap.put(6, ONE_DAY);
        typeMap.put(99, NONE);
    }

    public static int getMinutes(int type){
        return typeValueMap.get(type);
    }

    public static SystemNotifyType valueOf(int type) {
        SystemNotifyType notifyType = typeMap.get(type);
        return notifyType == null ? NONE : notifyType;
    }
}
