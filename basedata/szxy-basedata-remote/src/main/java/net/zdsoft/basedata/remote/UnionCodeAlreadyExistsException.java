package net.zdsoft.basedata.remote;

/**
 * @author shenke
 * @since 2019/2/28 下午2:49
 */
public class UnionCodeAlreadyExistsException extends Exception {

    private String unionCode;

    public UnionCodeAlreadyExistsException(String message, String unionCode) {
        super(message);
        this.unionCode = unionCode;
    }

    public String getUnionCode() {
        return unionCode;
    }
}
