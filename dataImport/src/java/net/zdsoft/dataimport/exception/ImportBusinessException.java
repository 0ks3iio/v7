package net.zdsoft.dataimport.exception;

/**
 * 处理字段异常
 * @author shenke
 * @since 2017.08.01
 */
public class ImportBusinessException extends Exception {

    private String field;
    private int index;

    public ImportBusinessException() {
        super();
    }

    public ImportBusinessException(String message) {
        super(message);
    }

    public ImportBusinessException(String message, String field, int index) {
        this(message);
        this.field = field;
        this.index = index;
    }

    public String getField() {
        return field;
    }

    public int getIndex() {
        return index;
    }
}
