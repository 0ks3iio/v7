/*
 * Project: v7
 * Author : shenke
 * @(#) ClassFlowService.java Created on 2016-9-27
 * @Copyright (c) 2016 ZDSoft Inc. All rights reserved
 */
package net.zdsoft.basedata.service;

import org.springframework.web.multipart.MultipartFile;

import net.zdsoft.basedata.entity.ClassFlow;
import net.zdsoft.basedata.entity.ImportEntity;
import net.zdsoft.basedata.entity.Student;

/**
 * @description:
 * @author: shenke
 * @version: 1.0
 * @date: 2016-9-27下午4:27:05
 */
public interface ClassFlowService extends BaseService<ClassFlow, String> {

    /**
     * 转班
     * 
     * @param studentId
     * @param newClassId
     * @param newGradeId
     */
    void flowClass(Student student, String newClassId, String operateUserId);

    /**
     * 批量导入，保存
     * 
     * @param uploadFile
     * @param userId
     */
    void saveImport(MultipartFile uploadFile, String unitId, String userId) throws Exception;

    void batchImport(ImportEntity importEntity);

    public void saveAllEntitys(ClassFlow... classFlow);
}
