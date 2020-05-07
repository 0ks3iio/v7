/**
 * FileName: INumber.java
 * Author:   shenke
 * Date:     2018/5/29 下午4:31
 * Descriptor:
 */
package net.zdsoft.bigdata.data.echarts;

import com.alibaba.fastjson.JSONObject;

/**
 * @author shenke
 * @since 2018/5/29 下午4:31
 */
public class INumber {

    private String title;
    private int value;

    private Ration ration;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public Ration getRation() {
        return ration;
    }

    public void setRation(Ration ration) {
        this.ration = ration;
    }

    public static class Ration {
        private String title;
        private String leftName;
        private String leftValue;
        private String rightName;
        private String rightValue;

        public String getLeftName() {
            return leftName;
        }

        public void setLeftName(String leftName) {
            this.leftName = leftName;
        }

        public String getLeftValue() {
            return leftValue;
        }

        public void setLeftValue(String leftValue) {
            this.leftValue = leftValue;
        }

        public String getRightName() {
            return rightName;
        }

        public void setRightName(String rightName) {
            this.rightName = rightName;
        }

        public String getRightValue() {
            return rightValue;
        }

        public void setRightValue(String rightValue) {
            this.rightValue = rightValue;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        @Override
        public String toString() {
            return toJSONString();
        }

        public String toJSONString() {
            return JSONObject.toJSONString(this);
        }
    }

    @Override
    public String toString() {
        return toJSONString();
    }

    public String toJSONString() {
        return JSONObject.toJSONString(this);
    }
}
