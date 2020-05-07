package net.zdsoft.exammanage.data.service;

import com.alibaba.dubbo.config.annotation.Service;
import net.zdsoft.exammanage.data.entity.EmExamInfo;

import java.util.List;

/**
 * @ProjectName: eis
 * @Package: net.zdsoft.exammanage.data.service
 * @ClassName: EmStatRankingService
 * @Description: java类作用描述
 * @Author: Sweet
 * @CreateDate: 2018/8/20 13:45
 * @UpdateUser: 更新者
 * @UpdateDate: 2018/8/20 13:45
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
@Service
public interface EmStatRankingService {
    /* *
     *
     * @author Sweet
     * @date 2018/8/20 14:14
     * @param [examId]
     * @return java.util.List<net.zdsoft.exammanage.data.entity.EmExamInfo>
     * @des 根据examid查找EmstatRank
     */
    public List<EmExamInfo> getEmExamInfoByExamId(String examId);
}
