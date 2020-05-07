/* 
 * @(#)ProblemRemoteDto.java    Created on 2017-5-15
 * Copyright (c) 2017 ZDSoft Networks, Inc. All rights reserved.
 * $Id$
 */
package net.zdsoft.system.remote.dto;

import java.util.Date;

/**
 * @author gzjsd
 * @version $Revision: 1.0 $, $Date: 2017-5-15 下午07:48:17 $
 */
public class ProblemRemoteDto {

    private static final long serialVersionUID = -4500263587720867107L;
    private String id;
    private String serverCode;
    private String typeId;
    private String question;
    private String answer;
    private Date creationTime;
    private Date modifyTime;
    private int isDeleted;
    private String typeName;// 类型名称
    private String serverName;// 应用名称

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getServerCode() {
        return serverCode;
    }

    public String getTypeId() {
        return typeId;
    }

    public String getQuestion() {
        return question;
    }

    public String getAnswer() {
        return answer;
    }

    public Date getCreationTime() {
        return creationTime;
    }

    public Date getModifyTime() {
        return modifyTime;
    }

    public int getIsDeleted() {
        return isDeleted;
    }

    public String getTypeName() {
        return typeName;
    }

    public String getServerName() {
        return serverName;
    }

    public void setServerCode(String serverCode) {
        this.serverCode = serverCode;
    }

    public void setTypeId(String typeId) {
        this.typeId = typeId;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public void setCreationTime(Date creationTime) {
        this.creationTime = creationTime;
    }

    public void setModifyTime(Date modifyTime) {
        this.modifyTime = modifyTime;
    }

    public void setIsDeleted(int isDeleted) {
        this.isDeleted = isDeleted;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public void setServerName(String serverName) {
        this.serverName = serverName;
    }

}
