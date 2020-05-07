package net.zdsoft.dataimport.core;

/**
 * @author shenke
 * @since 2017.07.31
 */
public class DataCell {
     private Object data;
     private Class type;
     private String fieldName;
     private String header;


    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public Class getType() {
        return type;
    }

    public void setType(Class type) {
        this.type = type;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }
}
