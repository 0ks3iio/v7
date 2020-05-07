package net.zdsoft.bigdata.datasource;

/**
 * @author shenke
 * @since 2018/11/27 下午1:02
 */
public interface DataType {

    String getType();

    /**
     * 适用于查询数据用于兼容APi的DataType
     * @return
     */
    static DataType api() {
        return Static.api;
    }

    /**
     * 适用于查询数据用于兼容查询静态数据的形式
     * @return
     */
    static DataType json() {
        return Static.json;
    }

    enum Static implements DataType {
        /**
         * rest
         */
        api {
            @Override
            public String getType() {
                return "99";
            }
        },
        json {
            @Override
            public String getType() {
                return "98";
            }
        };
    }
}
