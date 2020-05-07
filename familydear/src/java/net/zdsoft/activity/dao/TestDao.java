package net.zdsoft.activity.dao;

import net.zdsoft.familydear.entity.FamDearAttachment;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * @ProjectName: eis
 * @Package: net.zdsoft.activity.dao
 * @ClassName: TestDao
 * @Description: java类作用描述
 * @Author: Sweet
 * @CreateDate: 2019/5/23 15:38
 * @UpdateUser: 更新者
 * @UpdateDate: 2019/5/23 15:38
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
public interface TestDao extends BaseJpaRepositoryDao<FamDearAttachment, String> {
    @Query("from FamDearAttachment where objId=?1 and objecttype=?2 order by creationTime")
    public List<FamDearAttachment> getAttachmentByObjId(String objId, String objType);

    @Query("from FamDearAttachment where objId in (?1)")
    public List<FamDearAttachment> findListByObjIds(String... objId);

    @Query("select count(*) from FamDearAttachment where objId=?1 and objecttype=?2")
    public Integer findAttachmentNumByObjId(String objId, String objType);

    @Query("from FamDearAttachment where objId in (?1)")
    public List<FamDearAttachment> getAttachmentByObjIds(String[] objIds);
}
