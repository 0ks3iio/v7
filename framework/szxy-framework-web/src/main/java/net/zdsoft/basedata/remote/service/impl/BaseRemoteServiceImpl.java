package net.zdsoft.basedata.remote.service.impl;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

import net.zdsoft.basedata.remote.service.BaseRemoteService;
import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.framework.entity.BaseEntity;
import net.zdsoft.framework.utils.EntityUtils;
import net.zdsoft.framework.utils.RedisUtils;
import net.zdsoft.framework.utils.SUtils;

public abstract class BaseRemoteServiceImpl<T extends BaseEntity<K>, K extends Serializable> implements BaseRemoteService<T,K> {

	protected abstract BaseService<T, K> getBaseService();

	@Override
	public T findOneObjectById(K id) {
		return getBaseService().findOne(id);
	}
	
	public T findOneObjectById(K id, int cacheTime){
		Type type = getClass().getGenericSuperclass();
		T t = (T)EntityUtils.getGeneric(type);
		String entityName = t.getClass().getSimpleName();
		t = (T) RedisUtils.getObject("entity." + entityName + "." + id, t.getClass());
		if (t == null) {
			t = getBaseService().findOne(id);
			if (t != null) {
				RedisUtils.setObject("entity." + entityName + "." + id, t, cacheTime);
			}
		}
		return t;
	}

	public List<T> findAllObject() {
		return getBaseService().findAll();
	}

	@Override
	public List<T> findListObjectByIds(K... ids) {
		return getBaseService().findListByIdIn(ids);
	}

	@Override
	public List<T> findListObjectBy(Class<T> clazz, String[] names, Object[] params, String[] properties) {
		return getBaseService().findListBy(clazz, names, params, properties);
	}
	
	@Override
	public List<T> findListObjectBy(Class<T> clazz, String[] names, Object[] params, String inName, Object[] inArgs,
			String[] properties) {
		return getBaseService().findListBy(clazz, names, params, inName, inArgs, properties);
	}

	@Override
	public <M> long countByIn(String name, M[] params) {
		return getBaseService().countByIn(name, params);
	}
	@Override
	public <M> List<T> findListObjectByIn(String name, M[] params) {
		return getBaseService().<M>findListByIn(name,params);
	}

	@Override
	public <M> T findOneObjectBy(String name, M param) {
		return getBaseService().findOneBy(name,param);
	}
	
	public <M> long countBy(String name, M param) {
		return getBaseService().countBy(name, param);
	}

	@Override
	public <M> T findOneObjectBy(String[] names, M[] params) {
		return getBaseService().findOneBy(names,params);
	}
	@Override
	public <M> List<T> findListObjectBy(String[] names, M[] params) {
		return getBaseService().findListBy(names,params);
	}

	@Override
	public void update(T t, K id, String[] properties) {
		getBaseService().update(t,id,properties);
	}
	
	@Override
	public void save(T t) {
		getBaseService().save(t);
	}

	@Override
	public void saveAll(T[] ts) {
		getBaseService().saveAll(ts);
	}

	@Override
	public T findOneObjectById(K id, boolean shortCacheTime) {
		if(shortCacheTime)
			return findOneObjectById(id, 10);
		else
			return findOneObjectById(id);
	}

	@Override
	public T findOneObjectById(String id, String[] properties) {
		return getBaseService().findOne(id, properties);
	}

	public <W, V> void update(Map<W, V> whereAndVal, String whereXpath, String valueXpath) {
		getBaseService().update(whereAndVal, whereXpath,valueXpath);
	}

	@Override
	public void saveAll(String os) {
		Type type = getClass().getGenericSuperclass();
		T t = (T)EntityUtils.getGeneric(type);
		List<T> ts = SUtils.dt(os, (Class<T>) t.getClass());
		getBaseService().saveAll(EntityUtils.toArray(ts, (Class<T>) t.getClass()));
	}

	@Override
	public String findOneById(K id) {
		return SUtils.s(findOneObjectById(id));
	}

	@Override
	public String findOneById(K id, boolean shortCacheTime) {
		return SUtils.s(findOneObjectById(id, shortCacheTime));
	}

	@Override
	public String findOneById(K id, int cacheTime) {
		return SUtils.s(findOneObjectById(id, cacheTime));
	}

	@Override
	public String findListByIds(K... ids) {
		return SUtils.s(findListObjectByIds(ids));
	}

	@Override
	public String findAll() {
		return SUtils.s(findAllObject());
	}

	@Override
	public <M> String findListByIn(String name, M[] params) {
		return SUtils.s(findListObjectByIn(name, params));
	}

	@Override
	public <M> String findOneBy(String name, M param) {
		return SUtils.s(findOneObjectBy(name, param));
	}

	@Override
	public <M> String findOneBy(String[] names, M[] params) {
		return SUtils.s(findOneObjectBy(names, params));
	}

	@Override
	public <M> String findListBy(String[] names, M[] params) {
		return SUtils.s(findListObjectBy(names, params));
	}

	@Override
	public String findOneById(String id, String[] properties) {
		return SUtils.s(findOneObjectById(id, properties));
	}

	@Override
	public String findById(K id) {
		return findOneById(id);
	}

	@Override
	public String findById(K id, boolean shortCacheTime) {
		return findOneById(id, shortCacheTime);
	}

	@Override
	public String findById(K id, int cacheTime) {
		return findOneById(id, cacheTime);
	}

	@Override
	public String findByIds(K... ids) {
		return findListByIds(ids);
	}

	@Override
	public <M> String findByIn(String name, M[] params) {
		return findListByIn(name, params);
	}

	@Override
	public <M> String findBy(String name, M param) {
		return findOneBy(name, param);
	}

	@Override
	public <M> String findBy(String[] names, M[] params) {
		return findOneBy(names, params);
	}

	@Override
	public String findById(String id, String[] properties) {
		return findOneById(id, properties);
	}
	
	
}
