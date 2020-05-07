package net.zdsoft.basedata.service;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.domain.Specification;

import net.zdsoft.framework.entity.BaseEntity;
import net.zdsoft.framework.entity.Pagination;
import redis.clients.jedis.Jedis;

public interface BaseService<T extends BaseEntity, K extends Serializable> {

	/**
	 * @param s
	 * @return
	 */
	public List<T> findAll(Specification<T> s);
	
	public K convertKey(Object key);

	public List<T> findAll();

	/**
	 * @deprecated 后续开发中，不要再使用次接口，接口太松散，不利于业务控制及性能优化，需要明确定义业务接口参数
	 * @param s
	 * @return
	 */
	public T findOne(Specification<T> s);

	/**
	 * @param s
	 * @return
	 */
	public long count(Specification<T> s);

	public List<T> findListByIdIn(K[] ids);
	/** @deprecated 兼容美师优课老接口，数字校园不用 */
	public List<T> findByIdIn(K[] ids);

	/**
	 * 根据ids找记录，其实和findByIdIn是一样的
	 * 
	 * @param ids
	 * @return
	 */
	public List<T> findListByIds(K[] ids);
	/** @deprecated 兼容美师优课老接口，数字校园不用 */
	public List<T> findByIds(K[] ids);
	
	public List<T> findListByIdsWithMaster(K[] ids);
	/** @deprecated 兼容美师优课老接口，数字校园不用 */
	public List<T> findByIdsWithMaster(K[] ids);

	public Map<K, T> findMapByIdIn(K[] ids);
	/** @deprecated 兼容美师优课老接口，数字校园不用 */
	public Map<K, T> findByIdInMap(K[] ids);
	
	public Map<K, T> findMapByIdsWithMaster(K[] ids);
	/** @deprecated 兼容美师优课老接口，数字校园不用 */
	public Map<K, T> findByIdInMapWithMaster(K[] ids);

	/**
	 * 获取map，key=ids值， value为对象
	 * @param name
	 * @param ids
	 * @return
	 */
	public Map<K, T> findMapByIn(String name, K[] ids);
	/** @deprecated 兼容美师优课老接口，数字校园不用 */
	public Map<K, T> findByInMap(String name, K[] ids);
	
	public Map<K, T> findMapByInWithMaster(String name, K[] ids);
	/** @deprecated 兼容美师优课老接口，数字校园不用 */
	public Map<K, T> findByInMapWithMaster(String name, K[] ids);

	public <M> List<T> findListByIn(String name, M[] params);
	/** @deprecated 兼容美师优课老接口，数字校园不用 */
	public <M> List<T> findByIn(String name, M[] params);
	
	public <M> long countByIn(String name, M[] params);
	public <M> long countByInWithMaster(String name, M[] params);
	
	public <M> List<T> findListByInWithMaster(String name, M[] params);
	/** @deprecated 兼容美师优课老接口，数字校园不用 */
	public <M> List<T> findByInWithMaster(String name, M[] params);

	public <M> T findOneBy(String name, M param);
	/** @deprecated 兼容美师优课老接口，数字校园不用 */
	public <M> T findBy(String name, M param);
	
	public <M> long countBy(String name, M param);
	public <M> long countByWithMaster(String name, M param);
	
	public <M> T findOneByWithMaster(String name, M param);
	/** @deprecated 兼容美师优课老接口，数字校园不用 */
	public <M> T findByWithMaster(String name, M param);

	<M> List<T> findListBy(String name, M param);
	
	<M> List<T> findListByWithMaster(String name, M param);

	<M> List<T> findListBy(String[] names, M[] params);
	
	<M> List<T> findListByWithMaster(String[] names, M[] params);

	public <M> T findOneBy(final String[] names, final M[] params);
	/** @deprecated 兼容美师优课老接口，数字校园不用 */
	public <M> T findBy(final String[] names, final M[] params);
	
	public <M> T findOneByWithMaster(final String[] names, final M[] params);
	/** @deprecated 兼容美师优课老接口，数字校园不用 */
	public <M> T findByWithMaster(final String[] names, final M[] params);

	/**
	 * @deprecated 后续开发中，不要再使用次接口，接口太松散，不利于业务控制及性能优化，需要明确定义业务接口参数
	 * @param s
	 * @param page
	 * @return
	 */
	public List<T> findAll(Specification<T> s, Pagination page);

	public T findOne(K id);
	
	/**
	 * 强制从数据库中读取
	 * @param id
	 * @return
	 */
	public T findOneFromDB(K id);
	
	
	public T findOneWithMaster(K id);

	/**
	 * 根据id，从缓存中读取内容，如果为空，则读取后，写入缓存
	 * @deprecated 用findOne(id, cacheTime, clazz)代替
	 * @param id
	 * @param cacheTime
	 *            缓存时间
	 * @return
	 */
	public T findOne(K id, int cacheTime);
	
	public T findOne(K id, int cacheTime, Class<T> clazz);
	
	public T findOne(Jedis jedis, K id, int cacheTime, Class<T> clazz);

	public T findOne(Jedis jedis, K id, int cacheTime, Class<T> clazz, T defaultT);
	
	public T findOneWithMaster(K id, int cacheTime);

	/**
	 * 根据id，从缓存中读取内容，如果为空，则读取后，写入缓存
	 * 
	 * @param id
	 * @param shortCacheTime
	 *            是否采用短时间的缓存，一般是10s
	 * @return
	 */
	public T findOne(K id, boolean shortCacheTime);
	
	public T findOneWithMaster(K id, boolean shortCacheTime);

	public List<T> findAll(Pagination page);
	public List<T> findAllWithMaster(Pagination page);
	/** @deprecated 兼容美师优课老接口，数字校园不用 */
	public List<T> findAllByPage(Pagination page);
	/** @deprecated 兼容美师优课老接口，数字校园不用 */
	public List<T> findAllByPageWithMaster(Pagination page);

	/**
	 * @deprecated 除非非常特殊情况，否则不用！
	 * @param sql
	 * @param params
	 * @return
	 */
	public List<Object[]> findBySql(String sql, Object[] params);
	/**
	 * @deprecated 除非非常特殊情况，否则不用！
	 * @param sql
	 * @param params
	 * @return
	 */
	public List<Object[]> findListBySqlWithMaster(String sql, Object[] params);

	public void save(T t);

	public void delete(K id);

	public void delete(T t);

	public void saveAll(T[] ts);

	public void deleteAll(T[] ts);

	void update(T t, K id, String[] properties);

	public T findOne(String id, String[] properties);
	public List<T> findListBy(Class<T> clazz, String[] names, Object[] params, String[] properties);
	public List<T> findListBy(Class<T> clazz, String[] names, Object[] params,String inName, Object[] inArgs, String[] properties);
	public T findOneWithMaster(String id, String[] properties);

	/**
	 * update T set valeXPath=V where whereXPath=W
	 * 
	 * @param whereAndVal
	 * @param whereXpath
	 * @param valueXpath
	 * @param <W>
	 * @param <V>
	 */
	<W, V> void update(Map<W, V> whereAndVal, String whereXpath, String valueXpath);

	T findOneTop();
	/** @deprecated 兼容美师优课老接口，数字校园不用 */
	T findFirst();

	List<T> findListTop(int count);
	/** @deprecated 兼容美师优课老接口，数字校园不用 */
	List<T> findTop(int count);

	List<T> findTopWithMaster(int count);

}
