package net.zdsoft.basedata.remote.service;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import net.zdsoft.framework.entity.BaseEntity;

public interface BaseRemoteService<T extends BaseEntity<K>, K extends Serializable> {

    /**
     * 根据对象ID获取对象的JSON信息，具体对象由子类来决定
     *
     * @param id 对象的String类型主键，与Entity配置一致
     * @return 返回Json格式
     */
    String findOneById(K id);

    /**
     * @deprecated 兼容美师优课接口，数字校园不用
     */
    String findById(K id);

    T findOneObjectById(K id);


    /**
     * 根据id，从缓存中读取内容，如果为空，则读取后，写入缓存
     *
     * @param id
     * @param shortCacheTime 是否采用短时间的缓存，一般是10s
     * @return
     */
    String findOneById(K id, boolean shortCacheTime);

    /**
     * @deprecated 兼容美师优课接口，数字校园不用
     */
    String findById(K id, boolean shortCacheTime);

    T findOneObjectById(K id, boolean shortCacheTime);

    /**
     * 根据id，从缓存中读取内容，如果为空，则读取后，写入缓存
     *
     * @param id
     * @param cacheTime 缓存时间
     * @return
     */
    String findOneById(K id, int cacheTime);

    /**
     * @deprecated 兼容美师优课接口，数字校园不用
     */
    String findById(K id, int cacheTime);

    T findOneObjectById(K id, int cacheTime);

    /**
     * 根据对象ID数组，获取对象的JSONArray信息，具体对象由子类来决定
     *
     * @param ids 对象的主键数组
     * @return 返回JsonArray格式
     */
    String findListByIds(K... ids);

    /**
     * @deprecated 兼容美师优课接口，数字校园不用
     */
    String findByIds(K... ids);

    List<T> findListObjectByIds(K... ids);

    /**
     * 获取数据库中所有对象，包括软删除，具体对象由子类来决定
     *
     * @return 返回JsonArray格式
     */
    String findAll();

    List<T> findAllObject();

    /**
     * 类似于findByIdIn()</br>不过这里利用泛型参数，使得参数类型不必拘泥于K
     *
     * @param name
     * @param params
     * @param <M>
     * @return
     */
    <M> String findListByIn(String name, M[] params);

    /**
     * @deprecated 兼容美师优课接口，数字校园不用
     */
    <M> String findByIn(String name, M[] params);

    <M> List<T> findListObjectByIn(String name, M[] params);

    <M> long countByIn(String name, M[] params);

    /**
     * @param name
     * @param param
     * @return
     */
    <M> String findOneBy(String name, M param);

    /**
     * @deprecated 兼容美师优课接口，数字校园不用
     */
    <M> String findBy(String name, M param);

    <M> T findOneObjectBy(String name, M param);

    <M> long countBy(String name, M param);

    /**
     * @param names
     * @param params
     * @return
     */
    <M> String findOneBy(final String[] names, final M[] params);

    /**
     * @deprecated 兼容美师优课接口，数字校园不用
     */
    <M> String findBy(final String[] names, final M[] params);

    <M> T findOneObjectBy(final String[] names, final M[] params);

    <M> String findListBy(final String[] names, final M[] params);

    <M> List<T> findListObjectBy(final String[] names, final M[] params);


    void update(T t, K id, String[] properties);

    void save(T t);

    void saveAll(T[] ts);


    /**
     * update T set valeXPath=V where whereXPath=W
     *
     * @param whereAndVal
     * @param whereXpath
     * @param valeXpath
     * @param <W>
     * @param <V>
     */
    <W, V> void update(Map<W, V> whereAndVal, String whereXpath, String valeXpath);

    String findOneById(String id, String[] properties);

    List<T> findListObjectBy(Class<T> clazz, String[] names, Object[] params, String[] properties);

    List<T> findListObjectBy(Class<T> clazz, String[] names, Object[] params, String inName, Object[] inArgs, String[] properties);

    /**
     * @deprecated 兼容美师优课接口，数字校园不用
     */
    String findById(String id, String[] properties);

    T findOneObjectById(String id, String[] properties);

    void saveAll(String os);
}
