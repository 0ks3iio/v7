package net.zdsoft.gkelective.data.service;

import java.util.List;

import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.gkelective.data.dto.GkRoundsDto;
import net.zdsoft.gkelective.data.entity.GkRounds;

public interface GkRoundsService extends BaseService<GkRounds, String>{

	public GkRounds findRoundById(String roundsId);

	/**
	 * 
	 * @param arrangeId
	 * @param openClassType 可为空
	 * @return
	 */
	public List<GkRounds> findBySubjectArrangeId(String arrangeId,String openClassType);
 
	public GkRounds getCurrentGkRounds(String arrangeId);
	/**
	 * 删除数据时要同时更新其它数据的order_id
	 * @param roundsId
	 */
	public void removeByRoundsId(String roundsId);

	public void saveRounds(GkRounds gkRounds);
	/**
	 * 修改步骤
	 * @param step
	 * @param roundsId
	 */
	public void updateStep(int step,String roundsId);

	public List<GkRounds> findBySubjectArrangeIdHasDelete(String arrangeId);

	/**
	 * 保存轮次开班设置
	 * @param dto
	 */
	public void saveDto(GkRoundsDto dto);
}
