package net.zdsoft.bigdata.data.manager.datasource;

import java.util.List;

/**
 * @author ke_shen@126.com
 * @since 2018/4/8 下午2:27
 */
public class IColumn {

    private String name;
    private int type;
    private Object value;

    public IColumn(String name, int type) {
        this.name = name;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public int getType() {
        return type;
    }

    public Object getValue() {
        return value;
    }

    public IColumn setValue(Object value) {
        this.value = value;
        return this;
    }

    /**
     * same as toJSONString only user name and value
     */
    @Override
    public String toString() {
        return "\"" + this.name + "\":\"" + this.value + "\"";
    }

    public String toJSONString() {
        return this.toString();
    }

    /**
     * IColumn#name is key and IColumn#value is value
     */
    public static String toJSONString(IColumn... iColumns) {
        StringBuilder builder = new StringBuilder();
        builder.append("{");
        for (IColumn iColumn : iColumns) {
            builder.append(iColumn.toString());
            builder.append(",");
        }
        builder.delete(builder.length() - 1, builder.length());
        builder.append("}");
        return builder.toString();
    }

    public static String toJSONArrayString(List<IColumn[]> iColumns) {
        StringBuilder builder = new StringBuilder();
        builder.append("[");
        for (IColumn[] iColumnArray : iColumns) {
            builder.append("{");
            for (IColumn iColumn : iColumnArray) {
                builder.append(iColumn.toString());
                builder.append(",");
            }
            builder.delete(builder.length() - 1, builder.length());
            builder.append("},");
        }
        builder.delete(builder.length() - 1, builder.length());
        builder.append("]");
        return builder.toString();
    }
}
