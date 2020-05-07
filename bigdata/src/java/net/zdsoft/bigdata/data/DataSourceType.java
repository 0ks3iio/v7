package net.zdsoft.bigdata.data;

/**
 * 数据源类型
 *
 * @author ke_shen@126.com
 * @since 2018/4/8 下午4:40
 */
public enum DataSourceType {

    DB(1) {
        @Override
        public String getName() {
            return "数据库";
        }
    },
    API(2) {
        @Override
        public String getName() {
            return this.toString();
        }
    },
    /**
     * 静态的
     */
    STATIC(3) {
        @Override
        public String getName() {
            return "静态数据";
        }
    };

    private int value;

    DataSourceType(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public abstract String getName();

    public boolean match(int type) {
        return this.getValue() == type;
    }

    public static DataSourceType parse(int type) {
        switch (type) {
            case 1:
                return DB;
            case 2:
                return API;
            case 3:
                return STATIC;
            default:
                return null;
        }
    }
}
