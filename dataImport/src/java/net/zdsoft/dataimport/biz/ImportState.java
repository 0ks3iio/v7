package net.zdsoft.dataimport.biz;

import java.util.HashMap;
import java.util.Map;

/**
 * @author shenke
 * @@since 17-8-10 下午11:51
 */
public enum ImportState {

    WAIT(1,"等待中"),
    @Deprecated
    ING(2,"正在执行"),
    DONE(3,"解析完成"),
    CANCEL(4, "已取消"),
    ERROR(-1,"解析出错");

    private String state;
    private int code;

    ImportState(int code, String state) {
        this.code = code;
        this.state = state;
    }

    private static final Map<Integer, ImportState> codeMap = new HashMap<Integer, ImportState>(){
        {
            put(WAIT.getCode(), WAIT);
            put(ING.getCode(), ING);
            put(DONE.getCode(), DONE);
            put(CANCEL.getCode(), CANCEL);
            put(ERROR.getCode(), ERROR);
        }
    };
    static {

    }

    public String getState() {
        return state;
    }

    public int getCode() {
        return code;
    }

    public static ImportState fromCode(int code) {
        return codeMap.get(code);
    }
}
