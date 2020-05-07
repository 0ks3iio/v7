package net.zdsoft.cache.admin.core;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

/**
 * @author shenke
 * Created by shenke on 2017-7-11.
 */
public enum QueryType {
    HEAD("head","head^"),
    MIDDLE("middle","like*"),
    END("end","tail$"),
    UNKOWN("unkown","unkown");

    private String queryKey;
    private String queryKeyCn;

    private static final Map<String,QueryType> queryKeyLook = new HashMap<String,QueryType>(3);

    static {
        for (QueryType queryType : EnumSet.allOf(QueryType.class)) {
            queryKeyLook.put(queryType.getQueryKey(),queryType);
        }
    }

    QueryType(String queryKey, String queryKeyCn) {
        this.queryKey = queryKey;
        this.queryKeyCn = queryKeyCn;
    }

    public String getQueryKey() {
        return queryKey;
    }

    public void setQueryKey(String queryKey) {
        this.queryKey = queryKey;
    }

    public String getQueryKeyCn() {
        return queryKeyCn;
    }

    public void setQueryKeyCn(String queryKeyCn) {
        this.queryKeyCn = queryKeyCn;
    }

    public static QueryType fromType(String t) {
        QueryType type = queryKeyLook.get(t);
        return type == null ? UNKOWN : type;
    }
}
