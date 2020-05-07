/**
 * FileName: Table.java
 * Author:   shenke
 * Date:     2018/5/28 下午4:14
 * Descriptor:
 */
package net.zdsoft.bigdata.data.echarts;

import java.util.List;

import com.alibaba.fastjson.JSONObject;

/**
 * @author shenke
 * @since 2018/5/28 下午4:14
 */
public class Table {

    private List<Object> thead;
    //private List<Tr>
    private List<Tr> trs;

    public List<Object> getThead() {
        return thead;
    }

    public void setThead(List<Object> thead) {
        this.thead = thead;
    }

    public List<Tr> getTrs() {
        return trs;
    }

    public void setTrs(List<Tr> trs) {
        this.trs = trs;
    }

    public static class Tr {
        private List<Object> tds;

        public List<Object> getTds() {
            return tds;
        }

        public void setTds(List<Object> tds) {
            this.tds = tds;
        }

        @Override
        public String toString() {
            return "Tr{" +
                    "tds=" + tds +
                    '}';
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
