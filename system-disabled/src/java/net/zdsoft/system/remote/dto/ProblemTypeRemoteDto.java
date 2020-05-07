/* 
 * @(#)ProblemTypeRemoteDto.java    Created on 2017-6-15
 * Copyright (c) 2017 ZDSoft Networks, Inc. All rights reserved.
 * $Id$
 */
package net.zdsoft.system.remote.dto;

import java.io.Serializable;

/**
 * @author gzjsd
 * @version $Revision: 1.0 $, $Date: 2017-6-15 上午11:10:42 $
 */
public class ProblemTypeRemoteDto implements Serializable {

    private static final long serialVersionUID = -257086891354332946L;
    private String id;
    private String name;

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

}
