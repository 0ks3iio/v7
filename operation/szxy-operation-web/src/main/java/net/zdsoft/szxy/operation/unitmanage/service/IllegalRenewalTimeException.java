package net.zdsoft.szxy.operation.unitmanage.service;

import lombok.Getter;

import java.util.Date;

/**
 * 非法的续期时间
 * @author shenke
 * @since 2019/1/21 下午3:09
 */
public class IllegalRenewalTimeException extends Exception {

    @Getter
    private Date illegalDate;


    public IllegalRenewalTimeException(String message, Date illegalDate) {
        super(message);
        this.illegalDate = illegalDate;
    }
}
