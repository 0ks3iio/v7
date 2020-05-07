/*
 * @(#)SignRecordInfo.java    Created on 2014-6-11
 * Copyright (c) 2014 ZDSoft Networks, Inc. All rights reserved.
 * $Id$
 */
package net.zdsoft.appstore.entity;

import java.util.Date;

/**
 * 考勤记录实体类
 *
 * @author zhangxm
 * @version $Revision: 1.0 $, $Date: 2014-6-11 下午05:41:36 $
 */
public class AttendanceRecord {

    // 刷卡时间
    private Date recodeTime = null;

    // 设备号
    private String deviceId = null;

    // 卡号
    private String cardNumber = null;

    // 进出标识： 0为进，1为出
    private int ioType = 0;


    public Date getRecodeTime() {
        return recodeTime;
    }

    public void setRecodeTime(Date recodeTime) {
        this.recodeTime = recodeTime;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public int getIoType() {
        return ioType;
    }

    public void setIoType(int ioType) {
        this.ioType = ioType;
    }
}
