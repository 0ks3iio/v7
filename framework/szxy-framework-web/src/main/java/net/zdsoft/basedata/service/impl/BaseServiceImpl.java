package net.zdsoft.basedata.service.impl;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.reflect.MethodUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import com.alibaba.fastjson.TypeReference;
import com.google.common.collect.Lists;

import net.zdsoft.basedata.dao.BaseJdbcDao;
import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.framework.config.LocalCacheUtils;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.framework.entity.BaseEntity;
import net.zdsoft.framework.entity.Pagination;
import net.zdsoft.framework.entity.Specifications;
import net.zdsoft.framework.utils.EntityUtils;
import net.zdsoft.framework.utils.RedisUtils;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.framework.utils.UuidUtils;
import redis.clients.jedis.Jedis;

public abstract class BaseServiceImpl<T extends BaseEntity<K>, K extends Serializable> implements BaseService<T, K> {
	
	private static final Logger log = Logger.getLogger(BaseServiceImpl.class);

	protected abstract BaseJpaRepositoryDao<T, K> getJpaDao();

	protected abstract Class<T> getEntityClass();
	
	@Override
	public K convertKey(Object key) {
		return (K)key;
	}

	private Class<T> tClass;
	private Class<K> kClass;

	public BaseServiceImpl() {
		initActualType();
	}

	protected Class<T> getTClass() {
		return tClass;
	}

	protected Class<K> getKClass() {
		return kClass;
	}

	private <O> Class<O> classCast(Type type) {
		try {
			return (Class<O>) type;
		} catch (Exception e) {
			return null;
		}
	}

	private void initActualType() {
		Type type = this.getClass().getGenericSuperclass();
		Type[] typeArguments = ((ParameterizedType) type).getActualTypeArguments();
		if (ArrayUtils.isNotEmpty(typeArguments)) {
			for (Type argument : typeArguments) {
				if (tClass == null) {
					tClass = classCast(argument);
				}
				if (kClass == null) {
					kClass = classCast(argument);
				}

			}
		}
	}

	@Autowired
	private BaseJdbcDao baseJdbcDao;

	@Override
	public List<T> findAll(Specification<T> s) {
		return getJpaDao().findAll(s);
	}

	@Override
	public List<T> findAll(final Specification<T> s, final Pagination page) {
		if (page == null) {
			return findAll(s);
		}
		Page<T> pages = getJpaDao().findAll(s, page.toPageable());
		page.setMaxRowCount((int) pages.getTotalElements());
		return pages.getContent();
	}

	@Override
	public List<T> findListByIdIn(final K[] ids) {
		return findListByIn("id", ids);
	}

	@Override
	public <M> List<T> findListByIn(final String name, final M[] params) {
		final Set<M> set = new HashSet<>();
		for (M m : params) {
			if (m == null)
				continue;
			set.add(m);
		}
		if (CollectionUtils.isEmpty(set)) {
			return new ArrayList<>();
		}
		Specification<T> s = new Specification<T>() {
			@Override
			public Predicate toPredicate(Root<T> root, CriteriaQuery<?> cq, CriteriaBuilder cb) {
				List<Predicate> ps = new ArrayList<>();
				queryIn(name, set.toArray(), root, ps, null);
				return cb.or(ps.toArray(new Predicate[0]));
			}

		};
		return getJpaDao().findAll(s);
	}
	
	public <M> long countByInWithMaster(final String name, final M[] params) {
		return countByIn(name, params);
	}
	public <M> long countByIn(final String name, final M[] params) {
		if (ArrayUtils.isEmpty(params)) {
			return 0;
		}
		final Set<M> set = new HashSet<>();
		for (M m : params) {
			if (m == null)
				continue;
			set.add(m);
		}
		if (CollectionUtils.isEmpty(set)) {
			return 0;
		}
		Specification<T> s = new Specification<T>() {
			@Override
			public Predicate toPredicate(Root<T> root, CriteriaQuery<?> cq, CriteriaBuilder cb) {
				List<Predicate> ps = new ArrayList<>();
				queryIn(name, set.toArray(), root, ps, null);
				return cb.or(ps.toArray(new Predicate[0]));
			}

		};
		return getJpaDao().count(s);
	}

	@Override
	public <M> T findOneBy(final String name, final M param) {
		if (StringUtils.isBlank(name)) {
			return null;
		}
		return getJpaDao().findOne(new Specification<T>() {
			@Override
			public Predicate toPredicate(Root<T> root, CriteriaQuery<?> criteriaQuery,
					CriteriaBuilder criteriaBuilder) {
				return criteriaQuery.where(criteriaBuilder.equal(root.<M> get(name), param)).getRestriction();
			}
		}).orElse(null);
	}
	public <M> long countByWithMaster(final String name, final M param) {
		return countBy(name, param);
	}
	public <M> long countBy(final String name, final M param) {
		if (StringUtils.isBlank(name)) {
			return 0;
		}
		return getJpaDao().count(new Specification<T>() {
			@Override
			public Predicate toPredicate(Root<T> root, CriteriaQuery<?> criteriaQuery,
					CriteriaBuilder criteriaBuilder) {
				return criteriaQuery.where(criteriaBuilder.equal(root.<M> get(name), param)).getRestriction();
			}
		});
	}

	@Override
	public <M> T findOneBy(final String[] names, final M[] params) {
		if (ArrayUtils.isEmpty(names)) {
			return null;
		}
		return getJpaDao().findOne(new Specification<T>() {
			@Override
			public Predicate toPredicate(Root<T> root, CriteriaQuery<?> criteriaQuery,
					CriteriaBuilder criteriaBuilder) {
				List<Predicate> ps = new ArrayList<>();
				for (int i = 0, length = names.length; i < length; i++) {
					String name = names[i];
					ps.add(criteriaBuilder.equal(root.<M> get(name), params[i]));
				}
				return criteriaQuery.where(criteriaBuilder.and(ps.toArray(new Predicate[0]))).getRestriction();
			}
		}).orElse(null);
	}

	@Override
	public <M> List<T> findListBy(final String[] names, final M[] params) {
		if (ArrayUtils.isEmpty(names)) {
			return Lists.newArrayList();
		}
		return getJpaDao().findAll(new Specification<T>() {
			@Override
			public Predicate toPredicate(Root<T> root, CriteriaQuery<?> criteriaQuery,
					CriteriaBuilder criteriaBuilder) {
				List<Predicate> ps = new ArrayList<>();
				for (int i = 0, length = names.length; i < length; i++) {
					String name = names[i];
					ps.add(criteriaBuilder.equal(root.<M> get(name), params[i]));
				}
				return criteriaQuery.where(criteriaBuilder.and(ps.toArray(new Predicate[0]))).getRestriction();
			}
		});
	}

	@Override
	public <M> List<T> findListBy(final String name, final M param) {
		if (StringUtils.isBlank(name)) {
			return Lists.newArrayList();
		}
		return getJpaDao().findAll(new Specification<T>() {
			@Override
			public Predicate toPredicate(Root<T> root, CriteriaQuery<?> criteriaQuery,
					CriteriaBuilder criteriaBuilder) {
				return criteriaQuery.where(criteriaBuilder.equal(root.<M> get(name), param)).getRestriction();
			}
		});
	}
	
	

	@Override
	public T findOneFromDB(K id) {
		if(id == null)
			return null;
		return getJpaDao().findById(id).orElse(null);
	}

	@Override
	public T findOne(final K id) {
		if (id == null)
			return null;
		String objectType = EntityUtils.getGenericClassName(this.getClass());
		T t = LocalCacheUtils.getValue(objectType, String.valueOf(id));
		if(t == null) {
			t = getJpaDao().findById(id).orElse(null);	
			if(t != null) {
				LocalCacheUtils.putValue(objectType, String.valueOf(id), t);
			}
		}
		return t;
		
	}

	@Override
	public List<T> findAll(final Pagination page) {
		Page<T> pages = getJpaDao().findAll(page.toPageable());
		page.setMaxRowCount((int) pages.getTotalElements());
		return pages.getContent();
	}

	@Override
	public T findOne(Specification<T> s) {
		List<T> ts = getJpaDao().findAll(s);
		if (CollectionUtils.isNotEmpty(ts)) {
			return ts.get(0);
		} else {
			return null;
		}
	}

	@Override
	public List<T> findAll() {
		return getJpaDao().findAll();
	}

	@Override
	public List<Object[]> findBySql(String sql, Object[] params) {
		String key = SUtils.s(params);
		key = EntityUtils.getCode(sql + key);
		String s = RedisUtils.get("findBySql." + key);
		if (StringUtils.isBlank(s)) {
			List<Object[]> ss = baseJdbcDao.findBySql(sql, params);
			RedisUtils.set("findBySql." + key, SUtils.s(ss), 30);
			return ss;
		}
		return SUtils.dt(s, new TypeReference<List<Object[]>>() {
		});
	}

	@Override
	public long count(Specification<T> s) {
		return getJpaDao().count(s);
	}

	@Override
	public Map<K, T> findMapByIdIn(K[] ids) {
		return findMapByIn("id", ids);
	}
	

	@Override
	public List<T> findListBy(Class<T> clazz, String[] names, Object[] params, String[] properties) {
		return getJpaDao().findListBy(clazz, names, params, properties);
	}
	
	@Override
	public List<T> findListBy(Class<T> clazz, String[] names, Object[] params, String inName, Object[] inArgs, String[] properties) {
		return getJpaDao().findListBy(clazz, names, params, inName, inArgs, properties);
	}

	@Override
	public Map<K, T> findMapByIn(String name, K[] ids) {
		List<T> ts = findListByIn(name, ids);
		Map<K, T> map = new HashMap<>();
		for (T t : ts) {
			map.put((K) t.getId(), t);
		}
		return map;
	}

	/**
	 * 组装in，和K类型有关
	 *  @param name
	 * @param params
	 * @param root
	 * @param ps
	 * @param cb
	 */
	public <M> void queryIn(final String name, final M[] params, Root<T> root, List<Predicate> ps, CriteriaBuilder cb) {
		List<Predicate> inPredicates = Lists.newArrayList();
		if (params.length <= 1000) {
			ps.add(root.<K> get(name).in((Object[]) params));
		} else {
			int cyc = params.length / 1000 + (params.length % 1000 == 0 ? 0 : 1);
			for (int i = 0; i < cyc; i++) {
				int max = (i + 1) * 1000;
				if (max > params.length)
					max = params.length;
				Predicate p = root.<K> get(name).in((Object[]) ArrayUtils.subarray(params, i * 1000, max));
				inPredicates.add(p);
			}
			if ( cb != null ) {
				ps.add(cb.or(inPredicates.toArray(new Predicate[inPredicates.size()])));
			} else {
				ps.addAll(inPredicates);
			}
		}
	}

	/**
	 * 组装in，和K类型有关
	 * 
	 * @param name
	 * @param params
	 * @param root
	 * @param ps
	 */
	public <M> void queryNotIn(final String name, final M[] params, Root<T> root, List<Predicate> ps,
			final CriteriaBuilder cb) {
		if (params.length <= 1000) {
			ps.add(root.<K> get(name).in((Object[]) params).not());
		} else {
			int cyc = params.length / 1000 + (params.length % 1000 == 0 ? 0 : 1);
			for (int i = 0; i < cyc; i++) {
				int max = (i + 1) * 1000;
				if (max > params.length)
					max = params.length;
				Predicate p = root.<K> get(name).in((Object[]) ArrayUtils.subarray(params, i * 1000, max)).not();
				ps.add(p);
			}
		}
	}

	/**
	 * 保存修改时处理特定字段
	 * 
	 * @param
	 */
	@SuppressWarnings("unchecked")
	public List<T> checkSave(T... ts) {
		if (ts == null) {
			return new ArrayList<>();
		}
		Field f = null;
		for (T t : ts) {
			try {
				if (StringUtils.isBlank((String) t.getId()))
					t.setId((K) UuidUtils.generateUuid());
			} catch (Exception e) {
				try {
					if (t.getId() == null) {
						Integer maxId = getJpaDao().maxIntId(t.getClass().getName());
						t.setId((K) ((new Integer(maxId + 1))));
					}
				} catch (Exception e1) {
					log.warn(e1);
				}
			}
			try {
				f = t.getClass().getDeclaredField("creationTime");
				if (f != null) {
					Object o = MethodUtils.invokeMethod(t, "getCreationTime");
					if (o == null)
						MethodUtils.invokeMethod(t, "setCreationTime", new Date());
				}
			} catch (Exception e) {
				log.warn(e);
			}
			try {
				f = t.getClass().getDeclaredField("modifyTime");
				if (f != null) {
					MethodUtils.invokeMethod(t, "setModifyTime", new Date());
				}
			} catch (Exception e) {
				log.warn(e);
			}
			try {
				f = t.getClass().getDeclaredField("eventSource");
				if (f != null) {
					MethodUtils.invokeMethod(t, "setEventSource", 0);
				}
			} catch (Exception e) {
				log.warn(e);
			}
		}
		return Arrays.asList(ts);
	}

	@Override
	public void save(T t) {
		String objectType = EntityUtils.getGenericClassName(this.getClass());
		LocalCacheUtils.remove(objectType, String.valueOf(t.getId()));
		getJpaDao().save(t);
	}

	@Override
	public void delete(K id) {
		getJpaDao().deleteById(id);
	}

	@Override
	public void delete(T t) {
		getJpaDao().delete(t);
	}

	@Override
	public void saveAll(T[] ts) {
		if (ArrayUtils.isEmpty(ts))
			return;
		getJpaDao().saveAll(Arrays.asList(ts));
	}

	@Override
	public void deleteAll(T[] ts) {
		if (ArrayUtils.isEmpty(ts))
			return;
		getJpaDao().deleteAll(Arrays.asList(ts));
	}

	@Override
	public void update(T t, K id, String[] properties) {
		getJpaDao().update(t, id, properties);
	}

	@Override
	public T findOne(K id, int cacheTime) {
		Type type = getClass().getGenericSuperclass();
		T t = (T) EntityUtils.getGeneric(type);
		String entityName = t.getClass().getSimpleName();
		String key = entityName + ".By.ID_" + cacheTime + "@" + id;

		t = (T) RedisUtils.getObject(key, t.getClass());
		if (t == null) {
			t = getJpaDao().findById(id).orElse(null);
			if (t != null) {
				RedisUtils.setObject(key, t, cacheTime);
			}
		}
		return t;
	}
	
	@Override
	public T findOne(Jedis jedis, K id, int cacheTime, Class<T> clazz) {
		String entityName = clazz.getSimpleName();
		String key = entityName + ".By.ID_" + cacheTime + "@" + id;
		T t = RedisUtils.getObject(jedis, key, clazz);
		if (t == null) {
			t = getJpaDao().findById(id).orElse(null);
			if (t != null) {
				RedisUtils.setObject(key, t, cacheTime);
			}
		}
		return t;
	}
	
	@Override
	public T findOne(Jedis jedis, K id, int cacheTime, Class<T> clazz, T defaultT) {
		String entityName = clazz.getSimpleName();
		String key = entityName + ".By.ID_" + cacheTime + "@" + id;
		T t = RedisUtils.getObject(jedis, key, clazz);
		if (t == null) {
			t = getJpaDao().findById(id).orElse(defaultT);
			if (t != null) {
				RedisUtils.setObject(key, t, cacheTime);
			}
		}
		return t;
	}

	@Override
	public T findOne(K id, int cacheTime, Class<T> clazz) {
		String entityName = clazz.getSimpleName();
		String key = entityName + ".By.ID_" + cacheTime + "@" + id;

		T t = RedisUtils.getObject(key, clazz);
		if (t == null) {
			t = getJpaDao().findById(id).orElse(null);
			if (t != null) {
				RedisUtils.setObject(key, t, cacheTime);
			}
		}
		return t;
	}

	@Override
	public T findOne(K id, boolean shortCacheTime) {
		if (shortCacheTime)
			return findOne(id, RedisUtils.TIME_SHORT_CACHE);
		else
			return findOne(id);
	}

	@Override
	// @Cacheable(key = "#root.args[1]")
	public T findOne(String id, String[] properties) {
		return findOne((K)id);
//		Type type = getClass().getGenericSuperclass();
//		T t = (T) EntityUtils.getGeneric(type);
//		return getJpaDao().findOne((Class<T>) t.getClass(), id, properties);
	}

	@Override
	public <W, V> void update(Map<W, V> whereAndVal, String whereXpath, String valueXpath) {
		getJpaDao().update(whereAndVal, whereXpath, valueXpath);
	}

	@Override
	public List<T> findListByIds(K[] ids) {
		return findListByIdIn(ids);
	}

	@Override
	public T findOneTop() {
		List<T> ts = findListTop(1);
		if (CollectionUtils.isEmpty(ts))
			return null;
		else
			return ts.get(0);
	}

	@Override
	public List<T> findListTop(int count) {
		Pageable page = new PageRequest(0, count);
		List<T> ts = getJpaDao().findAll(page).getContent();
		if (CollectionUtils.isEmpty(ts))
			return new ArrayList<>();
		else
			return ts;
	}

	@Override
	public List<T> findListByIdsWithMaster(K[] ids) {
		return findListByIds(ids);
	}

	@Override
	public Map<K, T> findMapByIdsWithMaster(K[] ids) {
		return findMapByIdIn(ids);
	}

	@Override
	public Map<K, T> findMapByInWithMaster(String name, K[] ids) {
		return findMapByIn(name, ids);
	}

	@Override
	public <M> List<T> findListByInWithMaster(String name, M[] params) {
		return findListByIn(name, params);
	}

	@Override
	public <M> T findOneByWithMaster(String name, M param) {
		return findOneBy(name, param);
	}

	@Override
	public <M> List<T> findListByWithMaster(String name, M param) {
		return findListBy(name, param);
	}

	@Override
	public <M> List<T> findListByWithMaster(String[] names, M[] params) {
		return findListBy(names, params);
	}

	@Override
	public <M> T findOneByWithMaster(String[] names, M[] params) {
		return findOneBy(names, params);
	}

	@Override
	public T findOneWithMaster(K id) {
		return findOne(id);
	}

	@Override
	public T findOneWithMaster(K id, int cacheTime) {
		return findOne(id, cacheTime);
	}

	@Override
	public T findOneWithMaster(K id, boolean shortCacheTime) {
		return findOne(id, shortCacheTime);
	}

	@Override
	public List<T> findAllWithMaster(Pagination page) {
		return findAll(page);
	}

	@Override
	public List<Object[]> findListBySqlWithMaster(String sql, Object[] params) {
		return findBySql(sql, params);
	}

	@Override
	public T findOneWithMaster(String id, String[] properties) {
		return findOne(id, properties);
	}

	@Override
	public List<T> findTopWithMaster(int count) {
		return findListTop(count);
	}

	@Override
	public List<T> findByIdIn(K[] ids) {
		return findListByIdIn(ids);
	}

	@Override
	public List<T> findByIds(K[] ids) {
		return findListByIds(ids);
	}

	@Override
	public List<T> findByIdsWithMaster(K[] ids) {
		return findListByIdsWithMaster(ids);
	}

	@Override
	public Map<K, T> findByIdInMap(K[] ids) {
		return findMapByIdIn(ids);
	}

	@Override
	public Map<K, T> findByIdInMapWithMaster(K[] ids) {
		return findMapByIdsWithMaster(ids);
	}

	@Override
	public Map<K, T> findByInMap(String name, K[] ids) {
		return findMapByIn(name, ids);
	}

	@Override
	public Map<K, T> findByInMapWithMaster(String name, K[] ids) {
		return findMapByInWithMaster(name, ids);
	}

	@Override
	public <M> List<T> findByIn(String name, M[] params) {
		return findListByIn(name, params);
	}

	@Override
	public <M> List<T> findByInWithMaster(String name, M[] params) {
		return findListByInWithMaster(name, params);
	}

	@Override
	public <M> T findBy(String name, M param) {
		return findOneBy(name, param);
	}

	@Override
	public <M> T findByWithMaster(String name, M param) {
		return findOneByWithMaster(name, param);
	}

	@Override
	public <M> T findBy(String[] names, M[] params) {
		return findOneBy(names, params);
	}

	@Override
	public <M> T findByWithMaster(String[] names, M[] params) {
		return findOneByWithMaster(names, params);
	}

	@Override
	public List<T> findAllByPage(Pagination page) {
		return findAll(page);
	}

	@Override
	public List<T> findAllByPageWithMaster(Pagination page) {
		return findAllWithMaster(page);
	}

	@Override
	public T findFirst() {
		return findOneTop();
	}

	@Override
	public List<T> findTop(int count) {
		return findListTop(count);
	}
}
