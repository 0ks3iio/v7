/* 
 * @(#)ImportXML.java    Created on 2007-3-23
 * Copyright (c) 2006 ZDSoft Networks, Inc. All rights reserved.
 * $Header: f:/44CVSROOT/exam/src/net/zdsoft/exam/dataimport/ImportXML.java,v 1.1 2007/03/26 13:05:01 linqz Exp $
 */
package net.zdsoft.newstusys.businessimport.core;

import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.dom4j.io.SAXReader;

import net.zdsoft.newstusys.businessimport.exception.ConfigurationException;
import net.zdsoft.newstusys.businessimport.param.DataImportParam;
import net.zdsoft.newstusys.utils.ConfigFileUtils;

public class ImportXML {

    private DataImportParam param;
    private URL configFile;

    // 配置文件中的object对应的对象类名
    private String entityClassName;
    private String objecDefine;// 配置文件中对象名
    private String xlsSheetName;// 读取xls中的sheet的名称，如果不存在，则默认第1个

    private String configFilePath;
    private List<String> listeners;// 监听器
    private ImportObject configObject;// 配置对象

    private static Map<String, ImportXML> cacheMap = new ConcurrentHashMap<String, ImportXML>();// 缓存

    public ImportXML(String filePath, DataImportParam param) throws ConfigurationException {
        this.configFilePath = filePath;
        this.param = param;

//        String objectName = param.getObjectName();
//        String key = configFilePath + "-" + objectName;
//        cacheMap.clear();
//        ImportXML xml = cacheMap.get(key);
//        if (null != xml) {
//            // 缓存xml中内容
//            this.entityClassName = xml.entityClassName;
//            this.objecDefine = xml.objecDefine;
//            this.xlsSheetName = xml.xlsSheetName;
//            this.listeners = xml.listeners;
//
//            // 动态内容
//            this.configObject = new ImportObject(xml.configObject, param, configFilePath);
//        } else {
            this.configFile = ConfigFileUtils.getConfigFileUrl(filePath);
            if (configFile == null || configFile.equals(""))
                throw new ConfigurationException("找不到配置文件！");
            
            parse();
//          cacheMap.put(key, this);
//        }
    }

    /**
     * 解析文件
     * 
     * @throws ConfigurationException
     */
    private void parse() throws ConfigurationException {
        String objectName = param.getObjectName();
        try {
            SAXReader saxReader = new SAXReader();
            org.dom4j.Document document = saxReader.read(configFile);
            
            // 取得xml对象内容
            configObject = parserConfigObject(objectName,document);
            if (configObject == null) {
                throw new ConfigurationException("配置文件中找不到类型为[" + objectName + "]配置");
            }

            // 监听器
            listeners = parserListeners(objectName, document);
            if (listeners == null) {
                throw new ConfigurationException("配置文件中找不到类型为[" + objectName + "]配置");
            }
        } catch (ConfigurationException e) {
            throw e;
        } catch (Exception ex) {
            throw new ConfigurationException("" + ex.getMessage());
        }
    }

    /**
     * 解析xml配置的对象内容
     * 
     * @author linqzh 2007-3-23
     * @param objectName
     * @return
     * @throws ConfigurationException
     */
    private ImportObject parserConfigObject(String objectName, org.dom4j.Document document)
            throws ConfigurationException {
        ImportObject configObject = null;

        try {
            String xpath = ("/root/object[@name='" + objectName + "']");//.toLowerCase();
            org.dom4j.Element element = (org.dom4j.Element) document.selectSingleNode(xpath);
            if (element != null) {
                entityClassName = element.attributeValue("entityclassname");
                objecDefine = element.attributeValue("define");
                xlsSheetName = element.attributeValue("xlsSheetName");

                xpath = ("/root/object[@name='commonobject']");//.toLowerCase();
                org.dom4j.Element commonElement = (org.dom4j.Element) document
                        .selectSingleNode(xpath);

                configObject = new ImportObject(element, commonElement, param, configFilePath);
            }
        } catch (ConfigurationException e) {
            throw e;
        } catch (Exception e) {
            throw new ConfigurationException("-->解析配置文件出错，请确认配置文件中是否存在name属性为" + objectName
                    + "的节点。" + e.getMessage());
        }

        return configObject;
    }

    /**
     * 解析xml中的监听器
     * 
     * @author linqzh 2007-3-23
     * @param objectName
     * @return
     * @throws ConfigurationException
     */
    private List<String> parserListeners(String objectName, org.dom4j.Document document)
            throws ConfigurationException {
        List<String> listOfListener = new ArrayList<String>();
        try {
            String xpath = ("/root/object[@name='" + objectName + "']/listeners/listener");
                    //.toLowerCase();

            @SuppressWarnings("unchecked")
            Iterator iterator = document.selectNodes(xpath).iterator();
            while (iterator.hasNext()) {
                org.dom4j.Element element = (org.dom4j.Element) iterator.next();
                if (!element.getTextTrim().equals("")) {
                    listOfListener.add(element.getTextTrim());
                }
            }
        } catch (Exception e) {
            throw new ConfigurationException("配置文件解析出错,请确认是否配置了Listener节点。" + e.getMessage());
        }
        
        //如果没有配置，则默认dataImportListener
        if(listOfListener.size() == 0){
            listOfListener.add("dataImportListener");
        }
        return listOfListener;
    }

    /**
     * 取得xml配置的对象内容
     */
    public ImportObject getConfigObject() {
        return configObject;
    }

    /**
     * 取得xml文件中配置的监听器
     * 
     * @return
     * @throws ConfigurationException
     */
    public List<String> getListeners() {
        return listeners;
    }

    public String getEntityClassName() {
        return entityClassName;
    }

    public String getObjecDefine() {
        return objecDefine;
    }

    public String getXlsSheetName() {
        return xlsSheetName;
    }

}
