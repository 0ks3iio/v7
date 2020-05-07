package net.zdsoft.dataimport.exception;

import org.apache.commons.lang3.StringUtils;

/**
 * @author shenke
 * @since 2017.08.09
 */
public class ImportParseException extends Exception {

    public ImportParseException() {
        super();
    }

    private String dataSheet;
    private int dataRow = -1;
    private int dataCell = -1;

    public ImportParseException(String dataSheet, int dataRow, int dataCell, String message) {
        this(message);
        this.dataSheet = dataSheet;
        this.dataRow = dataRow;
        this.dataCell = dataCell;
    }

    public ImportParseException(int dataRow, int dataCell, String message) {
        this(null, dataRow, dataCell, message);
    }

    public ImportParseException(int dataCell, String message) {
        this(null, -1, dataCell, message);
    }

    public ImportParseException(String message) {
        super(message);
    }

    public ImportParseException(String message, Throwable cause) {
        super(message, cause);
    }

    public ImportParseException(Throwable cause) {
        super(cause);
    }

    public String getDataSheet() {
        return dataSheet;
    }

    public int getDataCell() {
        return dataCell;
    }

    public int getDataRow() {
        return dataRow;
    }

    public String getParseErrorMessage() {
        StringBuffer error = new StringBuffer();
        if ( StringUtils.isNotBlank(dataSheet) ) {
            error.append("工作表")
                    .append(dataSheet);
        }
        if ( dataRow != -1) {
            error.append("第").append(dataRow).append("行");
        }
        if ( dataCell != -1) {
            error.append("第").append(dataCell).append("条数据不符合模板规范");
        }
        String msg = error.toString();
        return StringUtils.isBlank(msg) ? getMessage() : msg;
    }
}
