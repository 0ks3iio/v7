package net.zdsoft.exammanage.xj.service;


import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.exammanage.xj.entity.XjexamInfo;

/**
 * @author yangsj  2017年10月12日下午4:38:57
 */
public interface XjExamInfoService extends BaseService<XjexamInfo, String> {

    /**
     * @param studentName
     * @param admission
     * @param examStuSeatType
     * @return
     */
    XjexamInfo findStuSeatInfo(String studentName, String admission, String type);

    /**
     * @param studentName
     * @param admission
     * @return
     */
    XjexamInfo findStuInfo(String studentName, String admission);


}
