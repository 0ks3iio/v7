package net.zdsoft.exammanage.xj.dao;

import net.zdsoft.exammanage.xj.entity.XjexamInfo;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import org.springframework.data.jpa.repository.Query;

/**
 * @author yangsj  2017年10月12日下午4:35:09
 */
public interface XjExamInfoDao extends BaseJpaRepositoryDao<XjexamInfo, String> {

    /**
     * @param studentName
     * @param admission
     * @param type
     * @return
     */
    @Query("From XjexamInfo  Where stuName = ?1 and admission = ?2 and type = ?3")
    XjexamInfo findStuSeatInfo(String studentName, String admission, String type);

    /**
     * @param studentName
     * @param admission
     * @return
     */
    @Query("From XjexamInfo  Where stuName = ?1 and admission = ?2 ")
    XjexamInfo findStuInfo(String studentName, String admission);

}
