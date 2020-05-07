package net.zdsoft.gkelective.data.service;

import java.util.List;

import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.gkelective.data.dto.GkSubjectArrangeDto;
import net.zdsoft.gkelective.data.entity.GkSubjectArrange;

public interface GkSubjectArrangeService extends BaseService<GkSubjectArrange, String> {

    public void saveDto(GkSubjectArrangeDto arrangeDto) throws Exception;
    /**
     * 查询年级下的选课安排信息
     * @param gradeId(不能为空)
     * @return
     */
    public GkSubjectArrangeDto findByGradeId(String gradeId);
    /**
     * 查询选课安排信息
     * @param unitId
     * @param isUsing
     * 默认可以为空 学校查下已经发布传入参数
     * @return
     */
    public List<GkSubjectArrangeDto> findByUnitIdIsUsing(String unitId, Integer isUsing);
    /**
     * 根据id删除
     *
     * @param id
     */
    public void deletedById(String id);
    
	public GkSubjectArrange findArrangeById(String id);
	
	public GkSubjectArrange findEntByGradeId(String gradeId);
	
}
