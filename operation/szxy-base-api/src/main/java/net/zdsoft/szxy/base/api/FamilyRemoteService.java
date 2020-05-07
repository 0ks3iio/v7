package net.zdsoft.szxy.base.api;

import net.zdsoft.szxy.base.RecordName;
import net.zdsoft.szxy.base.entity.Family;
import net.zdsoft.szxy.base.exception.SzxyPassportException;
import net.zdsoft.szxy.base.query.FamilyQuery;
import net.zdsoft.szxy.monitor.Rpc;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * 基础数据： 家长接口
 * @author shenke
 * @since 2019/3/21 下午3:13
 */
@Rpc(domain = RecordName.name)
public interface FamilyRemoteService {

    /**
     * 根据主键查询， 可能为null
     * @param id 主键
     * @return
     */
    Family getFamilyById(String id);

    /**
     * 根据主键查询家长信息，过滤软删数据
     * @param ids 家长ID
     * @return
     */
    List<Family> getFamiliesById(String[] ids);

    /**
     * 根据学生id查询对应的家长信息，不包括软删数据
     * @param studentIds 学生ID
     * @return
     */
    List<Family> getFamiliesByStudentId(String[] studentIds);

    /**
     * 家长动态查询接口
     * @param familyQuery 动态查询参数
     * @return
     */
    Page<Family> queryFamilies(FamilyQuery familyQuery, Pageable page);

    /**
     * 更新手机号 同步更新用户、passport
     * @param familyId 主键
     * @param mobilePhone 新的手机号
     */
    void updateMobilePhone(String familyId, String mobilePhone) throws SzxyPassportException;
}
