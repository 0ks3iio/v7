/**
 * FileName: JexlContextHolder.java
 * Author:   shenke
 * Date:     2018/5/24 上午11:52
 * Descriptor:
 */
package net.zdsoft.bigdata.data.utils;

/**
 * @author shenke
 * @since 2018/5/24 上午11:52
 */
public class JexlContextHolder {

    static ThreadLocal<Object> contextHolder = new InheritableThreadLocal<>();

    public static Object getJexlContext() {
        return contextHolder.get();
    }

    public static void clearJexlContext() {
        contextHolder.remove();
    }

    public static void setJexlContext(Object context) {
        contextHolder.set(context);
    }

    public static class JexlPlaceHolderContext {

        private String userId;
        private String unitId;
        private Integer unitClass;
        private Integer userType;

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getUnitId() {
            return unitId;
        }

        public void setUnitId(String unitId) {
            this.unitId = unitId;
        }

        public Integer getUnitClass() {
            return unitClass;
        }

        public void setUnitClass(Integer unitClass) {
            this.unitClass = unitClass;
        }

        public Integer getUserType() {
            return userType;
        }

        public void setUserType(Integer userType) {
            this.userType = userType;
        }
    }
}
