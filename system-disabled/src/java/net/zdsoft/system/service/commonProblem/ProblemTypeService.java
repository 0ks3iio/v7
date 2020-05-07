/* 
 * @(#)ProblemTypeService.java    Created on 2017-5-9
 * Copyright (c) 2017 ZDSoft Networks, Inc. All rights reserved.
 * $Id$
 */
package net.zdsoft.system.service.commonProblem;

import java.util.List;

import net.zdsoft.system.entity.commonProblem.ProblemType;

public interface ProblemTypeService {
    /**
     * 根据问题应用查找应用的所有问题类型
     * 
     * @param serverCode
     * @return
     */
    List<ProblemType> findByServerCode(String serverCode);

    /**
     * 新增问题类型
     * 
     * @param type
     * @return
     */
    ProblemType saveOne(ProblemType type);

    /**
     * 根据id修改问题类型名称
     * 
     * @param name
     * @param id
     */
    void updateTypeName(String name, String id);

    /**
     * 根据类型id查找类型
     * 
     * @param id
     * @return
     */
    ProblemType findById(String id);

    /**
     * 根据id软删问题类型
     * 
     * @param id
     */
    void removeProblemTypeById(String id);
}
