/* 
 * @(#)ProblemRemoteService.java    Created on 2017-5-16
 * Copyright (c) 2017 ZDSoft Networks, Inc. All rights reserved.
 * $Id$
 */
package net.zdsoft.system.remote.service;

/**
 * @author xuxiyu
 * @version $Revision: 1.0 $, $Date: 2017-5-16 上午10:49:44 $
 */
public interface ProblemRemoteService {
    /**
     * 根据服务查询问题
     * 
     * @param question
     * @return
     */
    String findProblemListByServerCode(String serverCode);

    /**
     * 根据服务查询问题类型
     * 
     * @param serverCode
     * @return
     */
    String findProblemTypeListByServerCode(String serverCode);

    String findProblemOrderByModifyTime(String serverCode);

    String findProblemOrderByViewNum(String serverCode);

    /**
     * 根据问题id查找问题类型名称、问题详情
     * 
     * @param id
     * @return
     */
    String findProblemById(String id);

    /**
     * 问题标题查找服务问题详情
     * 
     * @param question
     * @return
     */
    String findByServerAndQuestion(String serverCode, String question);

    /**
     * 修改viewNum的值+1
     * 
     * @param id
     */
    void updatViewNum(String id);
}
