/* 
 * @(#)ProblemDto.java    Created on 2017-5-8
 * Copyright (c) 2017 ZDSoft Networks, Inc. All rights reserved.
 * $Id$
 */
package net.zdsoft.system.dto.commonProblem;

import java.io.Serializable;
import java.util.Date;

public class ProblemDto implements Serializable {
    private static final long serialVersionUID = -1286952676010885401L;
    private String id;
    private String serverCode;
    private String typeId;
    private String question;
    private String answer;
    private Date creationTime;
    private Date modifyTime;
    private String typeName;

    public String getId() {
        return id;
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

    public void setId(String id) {
        this.id = id;
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

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }
}
