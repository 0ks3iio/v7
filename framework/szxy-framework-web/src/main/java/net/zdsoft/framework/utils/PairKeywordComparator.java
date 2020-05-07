/* 
 * @(#)PairKeywordIndexComparator.java    Created on 2007-9-26
 * Copyright (c) 2005 ZDSoft.net, Inc. All rights reserved.
 * $Header: /project/keel/src/net/zdsoft/keel/util/helper/PairKeywordComparator.java,v 1.1 2007/09/27 09:22:13 liangxiao Exp $
 */
package net.zdsoft.framework.utils;

import java.util.Comparator;

public class PairKeywordComparator implements Comparator<PairKeyword> {

    public int compare(PairKeyword obj0, PairKeyword obj1) {
        PairKeyword keyword0 = obj0;
        PairKeyword keyword1 = obj1;
        return keyword0.getIndex() > keyword1.getIndex() ? 1 : -1;
    }

}
