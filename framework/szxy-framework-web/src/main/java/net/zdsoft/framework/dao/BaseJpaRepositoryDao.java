package net.zdsoft.framework.dao;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public abstract interface BaseJpaRepositoryDao<T, K extends Serializable>
		extends JpaRepository<T, K>, JpaSpecificationExecutor<T> {

	void update(T t, K id, String[] properties);
	
	/**
	 * 更新指定属性接口
	 * @param t
	 * @param properties
	 */
	public void update(T t, String[] properties);

	public T findOne(Class<T> clazz, String id, String[] properties);
	
	public List<T> findListBy(Class<T> clazz, String[] names, Object[] params, String[] properties);
	public List<T> findListBy(Class<T> clazz, String[] names, Object[] params, String inName, Object[] inArgs, String[] properties);

	public int maxIntId(Class<T> clazz);

	public int maxIntId(String claName);

	/**
	 * update T set valueXpath=V where whereXpath=W; </br>
	 * 批量更新操作 whereAndVal 的Key值为whereXpath d下的条件，value为set的值
	 * @param whereAndVal
	 * @param whereXpath
	 * @param <W>
	 * @param <V>
	 */
	<W,V> void update(Map<W,V> whereAndVal, String whereXpath, String valueXpath);
	
	public String findData(String tableName, String... ids);
	
	public String findData(String entityName, Date modifyTime);
	
	public String findData(String entityName, Date modifyTime, String modifyTimeName, int fetchSize);
	
}
