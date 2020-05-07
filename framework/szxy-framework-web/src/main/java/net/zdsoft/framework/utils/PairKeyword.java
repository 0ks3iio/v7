/* 
 * @(#)PairKeyword.java    Created on 2007-9-26
 * Copyright (c) 2005 ZDSoft.net, Inc. All rights reserved.
 * $Header: /project/keel/src/net/zdsoft/keel/util/helper/PairKeyword.java,v 1.1 2007/09/27 09:22:13 liangxiao Exp $
 */
package net.zdsoft.framework.utils;

public class PairKeyword {

    private String name;
    private int index;

    public PairKeyword(String name, int index) {
        this.name = name;
        this.index = index;
    }

    public int getIndex() {
        return index;
    }

    public String getName() {
        return name;
    }

}
