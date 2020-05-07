/* 
 * @(#)Problems.java    Created on 2017-5-8
 * Copyright (c) 2017 ZDSoft Networks, Inc. All rights reserved.
 * $Id$
 */
package net.zdsoft.system.entity.commonProblem;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Table;

import net.zdsoft.framework.entity.BaseEntity;

@Entity
@Table(name = "sys_problem")
public class Problem extends BaseEntity<String> {
    private static final long serialVersionUID = 1L;
    private String serverCode;
    private String typeId;
    private String question;
    private String answer;
    private long viewNum;
    private Date creationTime;
    private Date modifyTime;
    private int isDeleted;

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

    @Override
    public String fetchCacheEntitName() {
        return "problem";
    }

    public int getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(int isDeleted) {
        this.isDeleted = isDeleted;
    }

    public long getViewNum() {
        return viewNum;
    }

    public void setViewNum(long viewNum) {
        this.viewNum = viewNum;
    }

}
