/* 
 * @(#)ProblemRemoteServiceImpl.java    Created on 2017-5-16
 * Copyright (c) 2017 ZDSoft Networks, Inc. All rights reserved.
 * $Id$
 */
package net.zdsoft.system.remote.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.system.entity.commonProblem.Problem;
import net.zdsoft.system.entity.commonProblem.ProblemType;
import net.zdsoft.system.remote.dto.ProblemRemoteDto;
import net.zdsoft.system.remote.dto.ProblemTypeRemoteDto;
import net.zdsoft.system.remote.service.ProblemRemoteService;
import net.zdsoft.system.service.commonProblem.ProblemService;
import net.zdsoft.system.service.commonProblem.ProblemTypeService;

/**
 * @author gzjsd
 * @version $Revision: 1.0 $, $Date: 2017-5-16 上午10:50:51 $
 */
@Service("problemRemoteService")
@com.alibaba.dubbo.config.annotation.Service
public class ProblemRemoteServiceImpl implements ProblemRemoteService {
    @Autowired
    private ProblemService problemService;
    @Autowired
    private ProblemTypeService problemTypeService;

    @Override
    public String findProblemListByServerCode(String serverCode) {
        List<Problem> problemList = problemService.findByServerCode(serverCode);
        List<ProblemRemoteDto> list = getRemoteDtoList(problemList);
        return SUtils.s(list);
    }

    @Override
    public String findProblemTypeListByServerCode(String serverCode) {
        List<ProblemType> typeList = problemTypeService.findByServerCode(serverCode);
        List<ProblemTypeRemoteDto> typeDtoList = getRemoteTypeDtoList(typeList);
        return SUtils.s(typeDtoList);
    }

    private List<ProblemTypeRemoteDto> getRemoteTypeDtoList(List<ProblemType> typeList) {
        List<ProblemTypeRemoteDto> typeDtoList = new ArrayList<ProblemTypeRemoteDto>();
        if (typeList != null && !typeList.isEmpty()) {
            for (ProblemType t : typeList) {
                typeDtoList.add(getRemoteTypeDto(t));
            }
        }
        return typeDtoList;
    }

    private ProblemTypeRemoteDto getRemoteTypeDto(ProblemType t) {
        ProblemTypeRemoteDto typeDto = new ProblemTypeRemoteDto();
        typeDto.setId(t.getId());
        typeDto.setName(t.getName());
        return typeDto;
    }

    private List<ProblemRemoteDto> getRemoteDtoList(List<Problem> problemList) {
        List<ProblemRemoteDto> dtoList = new ArrayList<ProblemRemoteDto>();
        if (problemList != null && !problemList.isEmpty()) {
            for (Problem p : problemList) {
                dtoList.add(getRemoteDto(p));
            }
        }
        return dtoList;
    }

    private ProblemRemoteDto getRemoteDto(Problem p) {
        ProblemRemoteDto dto = new ProblemRemoteDto();
        dto.setId(p.getId());
        dto.setTypeId(p.getTypeId());
        dto.setQuestion(p.getQuestion());
        dto.setAnswer(p.getAnswer());
        dto.setModifyTime(p.getModifyTime());
        return dto;
    }

    @Override
    public String findProblemOrderByModifyTime(String serverCode) {
        List<Problem> pList = problemService.findOrderByModifyTime(serverCode);
        List<ProblemRemoteDto> list = getRemoteDtoList(pList);
        return SUtils.s(list);
    }

    @Override
    public String findProblemOrderByViewNum(String serverCode) {
        List<Problem> pList = problemService.findOrderByViewNum(serverCode);
        List<ProblemRemoteDto> list = getRemoteDtoList(pList);
        return SUtils.s(list);
    }

    @Override
    public String findProblemById(String id) {
        Problem problem = problemService.findById(id);
        ProblemRemoteDto dto = new ProblemRemoteDto();
        if (problem != null) {
            ProblemType type = problemTypeService.findById(problem.getTypeId());
            if (type != null) {
                dto = getRemoteDto(problem);
                dto.setTypeName(type.getName());
            }
        }
        return SUtils.s(dto);
    }

    @Override
    public String findByServerAndQuestion(String serverCode, String question) {
        List<Problem> pList = problemService.findByServerAndQuestion(serverCode, question);
        List<ProblemRemoteDto> list = getRemoteDtoList(pList);
        return SUtils.s(list);
    }

    @Override
    public void updatViewNum(String id) {
        problemService.updatViewNum(id);
    }
}
