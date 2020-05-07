package net.zdsoft.exammanage.data.dao;

import net.zdsoft.exammanage.data.entity.EmPlace;
import net.zdsoft.framework.entity.Pagination;

import java.util.List;
import java.util.Set;

public interface EmPlaceJdbcDao {

    public List<EmPlace> getEmPlaceOrder(String examId, String[] optionIds, Pagination page);

    public Set<String> getOptionId(String examId);
}
