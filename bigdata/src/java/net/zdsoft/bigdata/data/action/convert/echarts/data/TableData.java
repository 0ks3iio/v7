/**
 * FileName: TableData
 * Author:   shenke
 * Date:     2018/4/27 下午1:27
 * Descriptor:
 */
package net.zdsoft.bigdata.data.action.convert.echarts.data;

import java.util.ArrayList;
import java.util.List;

import net.zdsoft.framework.utils.Objects;

/**
 * @author shenke
 * @since 2018/4/27 下午1:27
 */
public class TableData {

    private List<KV> kv;

    public List<KV> getKv() {
        return kv;
    }

    public List<KV> kv() {
        if (kv == null) {
            this.kv = new ArrayList<>();
        }
        return this.kv;
    }

    public Object getV(Object k) {
        if (kv == null) {
            return "";
        }
        for (KV e : kv) {
            if (Objects.equals(e.getK(), k)) {
                return e.getV();
            }
        }
        return "";
    }

    public TableData kv(List<KV> kv) {
        this.kv = kv;
        return this;
    }

    public static class KV {
        private Object k;
        private Object v;

        public Object getK() {
            return k;
        }

        public KV setK(Object k) {
            this.k = k;
            return this;
        }

        public Object getV() {
            return v;
        }

        public KV setV(Object v) {
            this.v = v;
            return this;
        }
    }
}
