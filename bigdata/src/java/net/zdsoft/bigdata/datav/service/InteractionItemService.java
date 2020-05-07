package net.zdsoft.bigdata.datav.service;

import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.bigdata.datav.entity.InteractionItem;

import java.util.List;

/**
 * @author shenke
 * @since 2018/10/26 上午10:36
 */
public interface InteractionItemService extends BaseService<InteractionItem, String> {


    List<InteractionItem> getInteractionItemsByDiagramType(Integer diagramType);
}
