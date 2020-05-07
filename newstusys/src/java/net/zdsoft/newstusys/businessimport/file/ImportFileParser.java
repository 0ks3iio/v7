/* 
 * @(#)ImportFileParser.java    Created on Apr 28, 2010
 * Copyright (c) 2010 ZDSoft Networks, Inc. All rights reserved.
 * $Id$
 */
package net.zdsoft.newstusys.businessimport.file;

/**
 * @author zhaosf
 * @version $Revision: 1.0 $, $Date: Apr 28, 2010 8:00:28 PM $
 */
public interface ImportFileParser {

    public ImportFileParserParam.OutParam parseFile(ImportFileParserParam.InParam inParam)
            throws Exception;
}
