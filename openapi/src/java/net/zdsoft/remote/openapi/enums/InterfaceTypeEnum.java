/* 
 * @(#)InterfaceTypeEnum.java    Created on 2017-2-23
 * Copyright (c) 2017 ZDSoft Networks, Inc. All rights reserved.
 * $Id$
 */
package net.zdsoft.remote.openapi.enums;

/**
 * @author Chicb
 * @version $Revision: 1.0 $, $Date: 2017-2-23 上午10:51:21 $ 从单位开始 学校 部门 教师 学科 年级 班级 学生 家长 用户
 */
public enum InterfaceTypeEnum {
	SUBJECT {
      @Override
      public String getName() {
          return "subject";
      }
    },
    TEACHCLASSSTUDENT {
        @Override
        public String getName() {
            return "teachClassStudent";
      }
    };
    public abstract String getName();

}
