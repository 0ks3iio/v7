package net.zdsoft.stutotality.remote.service.impl;

import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.stutotality.data.service.StutotalityResultRemoteService;
import net.zdsoft.stutotality.remote.service.StutotalityRemoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by XuWang on 2019/11/12.
 */
@Service("stutotalityRemoteService")
public class StutotalityRemoteServiceImpl implements StutotalityRemoteService {

    @Autowired
    private StutotalityResultRemoteService stutotalityResultRemoteService;

    @Override
    public String getStutotalitytypeList(String studentId) {
        return SUtils.s(stutotalityResultRemoteService.getStutotalitytypeList(studentId));
    }

    @Override
    public String getStutotalityItemResult(String studentId,String type) {
        return SUtils.s(stutotalityResultRemoteService.getStutotalityItemResult(studentId,type));
    }


    /*@Override
    public String checkCode(String studentId,String codeId){
        return SUtils.s(stutotalityResultRemoteService.checkCode(studentId,codeId));
    }*/
    @Override
    public String saveCode(String studentId,String codeId){
        return SUtils.s(stutotalityResultRemoteService.saveCode(studentId,codeId));
    }
}
