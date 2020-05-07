package net.zdsoft.stutotality.data.service;

import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.stutotality.data.entity.StutotalityHealthOption;

import java.util.List;

public interface StutotalityHealthOptionService extends BaseService<StutotalityHealthOption,String> {



	public List<StutotalityHealthOption> findByHealthId(String healthId);

	public List<StutotalityHealthOption> findByUnitId(String unitId);

	public List<StutotalityHealthOption> findByUnitIdWithMaster(String unitId);

	public List<StutotalityHealthOption> findHealthItemsByIds(String[] healthIds);

	public List<StutotalityHealthOption> findByHealthIdAndUnitId(String healthId,String unitId);

	public List<StutotalityHealthOption> findByUnitIdAndGradeCode(String unitId, String gradeCode);

	public void deleteByhealthId(String healthId);

	public void saveHealthStandard(StutotalityHealthOption stutotalityHealthOption,List<StutotalityHealthOption> healthOption);

}
