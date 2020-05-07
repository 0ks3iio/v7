/* 
 * @(#)ImportObject.java    Created on 2007-3-23
 * Copyright (c) 2006 ZDSoft Networks, Inc. All rights reserved.
 * $Header: f:/44CVSROOT/exam/src/net/zdsoft/exam/dataimport/ImportObject.java,v 1.1 2007/03/26 13:05:01 linqz Exp $
 */
package net.zdsoft.newstusys.businessimport.core;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.dom4j.Attribute;
import org.dom4j.Element;

import net.zdsoft.framework.config.Evn;
import net.zdsoft.framework.entity.TR;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.newstusys.businessimport.exception.ConfigurationException;
import net.zdsoft.newstusys.businessimport.param.DataImportParam;
import net.zdsoft.system.entity.mcode.McodeDetail;
import net.zdsoft.system.remote.service.McodeRemoteService;

public class ImportObject {
	
	private McodeRemoteService mcodeRemoteService;
    // 配置对象
    private DataImportParam param;
    private String define;// 对象名称(中文)
    private String name;// 对象名称

    // 保存下拉列的map(包括微代码和非微代码下拉) key=define value=Map<key=content, value=thisId>
    private Map<String, Map<String, String>> selectMap = new HashMap<String, Map<String, String>>();
    // 生成模块下拉框 key=define;value=mcodetail.content或其他非微代码值
    private Map<String, String[]> defineSelectMap = new HashMap<String, String[]>();
    
    private List<ImportObjectNode> nodeList = new ArrayList<ImportObjectNode>();
    private List<String> requiredDefineList = new ArrayList<String>(); // 必填字段
    private List<ImportObjectNode> defaultValueNodeList = new ArrayList<ImportObjectNode>();// 默认值
    private Map<String, ImportObjectNode> defineNodeMap = new HashMap<String, ImportObjectNode>();
    private Map<String, ImportObjectNode> nameNodeMap = new HashMap<String, ImportObjectNode>();

    List<ImportObjectNode> fileNodeList = new ArrayList<ImportObjectNode>();// xml文件节点

    public ImportObject(ImportObject object, DataImportParam param, String configFilePath)
            throws ConfigurationException {
        // xml配置中静态的成员，从缓存中取出
        this.define = object.define;
        this.name = object.name;
        this.fileNodeList = object.fileNodeList;
    }

    /**
     * 解析对象内容
     * 
     * @param element
     * @param param
     * @throws ConfigurationException
     */    
    public ImportObject(Element element, Element commonElement, DataImportParam param,
            String configFilePath) throws ConfigurationException {
        try {
            define = element.attributeValue("define");
            name = element.attributeValue("name");

            // 取节点信息
            List fields = element.elements("field");
            parseFields(fields);

            // 公共节点
            if (null != commonElement) {
                fields = commonElement.elements("field");
                parseFields(fields);
            }
            
         // 动态约束
            dynamicRestrict(param, configFilePath);
        } catch (ConfigurationException e) {
            throw e;
        } catch (Exception e) {
            throw new ConfigurationException(e);
        }
    }
    
    /**
     * 施加约束
     * 
     * @param properties
     * @param configFilePath
     */
    private void dynamicRestrict(DataImportParam param, String configFilePath)
            throws ConfigurationException {
        this.param = param;
        if (MapUtils.isNotEmpty(param.getConstraintFields())) {
			this.selectMap.putAll(param.getConstraintFields());
		}
		// 根据导入字段进行过滤，主键字段不过滤
        Set<String> filterFieldSet = param.getFilterFields();

        boolean isUpdate = param.isOnlyUpdate();

        Map<String, ImportObjectNode> fieldCustomMap = null;

        wrappedFields(filterFieldSet, isUpdate, fieldCustomMap);

        // -----------动态字段--------------
        List<ImportObjectNode> dynamicNodeList = param.getDynamicFields();
        if (null != dynamicNodeList && dynamicNodeList.size() > 0) {
            for (ImportObjectNode node : dynamicNodeList) {
                parse(node);
            }
        }
        
        // 根据业务需求重新自定义结点信息
        redefineFields();
    }
    
    private void parse(ImportObjectNode node) throws ConfigurationException {
        nameNodeMap.put(node.getName(), node);
    }


    /**
     * 重新定义字段
     * @throws ConfigurationException
     */
    private void redefineFields() throws ConfigurationException {
        // 处理结点
        for (ImportObjectNode node : nameNodeMap.values()) {
            nodeList.add(node);

            if ("Y".equalsIgnoreCase(node.getRequired())) {
                requiredDefineList.add(node.getDefine());
            }

            // 默认值
            if (null != node.getDefaultValue()) {
                defaultValueNodeList.add(node);
            }

            checkNode(node);

            defineNodeMap.put(node.getDefine(), node);
        }
    }
    
    /**
     * 字段的檢查
     * 
     * @param node
     * @throws ConfigurationException
     */
    private void checkNode(ImportObjectNode node) throws ConfigurationException {
        String value = node.getName();
        if (value == null || value.equals("")) {
            throw new ConfigurationException("-->配置文件中的节点【" + node.getDefine() + "】name属性配置不对。");
        }
        value = node.getMcode();
        if (StringUtils.isNotBlank(value) && !("Region".equalsIgnoreCase(value))) {
            Map<String, String> map = selectMap.get(value);
            if (map == null) {
            	if(mcodeRemoteService == null) {
            		mcodeRemoteService = Evn.getBean("mcodeRemoteService");
            	}
            	map = new LinkedHashMap<String, String>();
            	List<McodeDetail> details = SUtils.dt(mcodeRemoteService.findByMcodeIds(value), new TR<List<McodeDetail>>() {});
                if (CollectionUtils.isEmpty(details)) {
                	throw new ConfigurationException("-->配置文件中的节点【" + node.getDefine()
                    + "】mcode属性配置不对。");
				}
                for (McodeDetail detail : details) {
					map.put(detail.getMcodeContent(), detail.getThisId());
				}
            } 
            selectMap.put(node.getDefine(), map);
        }
    }
    
    /**
     * 封裝字段
     * 
     * @param filterFieldSet
     * @param isUpdate
     * @param fieldCustomMap
     * @param constraintFieldMap
     * @throws ConfigurationException
     */
    private void wrappedFields(Set<String> filterFieldSet, boolean isUpdate,
            Map<String, ImportObjectNode> fieldCustomMap) throws ConfigurationException {

        for (int i = 0; i < fileNodeList.size(); i++) {
            ImportObjectNode node = fileNodeList.get(i);

            String filedDefine = node.getDefine();
            if (filterFieldSet.contains(filedDefine)) {
                continue;
            }

            ImportObjectNode dynamicNode = node.wrappedNode(isUpdate, i, fieldCustomMap, getDefineSelectMap());
            
            parse(dynamicNode);
        }
    }

    /**
     * 解析字段
     * 
     * @param fields
     * @throws ConfigurationException
     */
    @SuppressWarnings("unchecked")
    private void parseFields(List fields) throws ConfigurationException {
        for (int i = 0; i < fields.size(); i++) {
            Element field = (Element) fields.get(i);

            List<Attribute> listOfAttribute = field.attributes();
            Map<String, String> mapOfAttribute = new HashMap<String, String>();
            for (Attribute attribute : listOfAttribute) {
                mapOfAttribute.put(attribute.getName(), attribute.getValue());
            }

            ImportObjectNode node = new ImportObjectNode(field);
            fileNodeList.add(node);
        }
    }

    public String getDefine() {
        return define;
    }

    public List<ImportObjectNode> getListOfNode() {
        Collections.sort(nodeList, new Comparator<ImportObjectNode>() {
            public int compare(ImportObjectNode o1, ImportObjectNode o2) {
                return o1.getDisplayOrder() - o2.getDisplayOrder();
            }
        });
        return nodeList;
    }

    public String getName() {
        return name;
    }

    /**
     * @return Returns the param.
     */
    public DataImportParam getParam() {
        return param;
    }

    public Map<String, ImportObjectNode> getMapOfDefineObjectNode() {
        return getDefineNodeMap();
    }

    public Map<String, Map<String, String>> getSelectMap() {
        return selectMap;
    }

    public Map<String, String[]> getDefineSelectMap() {
    	if(MapUtils.isNotEmpty(selectMap)){
    		for(Entry<String, Map<String, String>> entry : selectMap.entrySet()){
    			defineSelectMap.put(entry.getKey(), entry.getValue().keySet().toArray(new String[0]));
    		}
    	}
    	return defineSelectMap;
    }

    public List<String> getRequiredDefineList() {
        Collections.sort(requiredDefineList, new Comparator<String>() {
            public int compare(String field1, String field2) {
                ImportObjectNode o1 = defineNodeMap.get(field1);
                ImportObjectNode o2 = defineNodeMap.get(field2);
                return o1.getDisplayOrder() - o2.getDisplayOrder();
            }
        });
        return requiredDefineList;
    }

    public Map<String, ImportObjectNode> getDefineNodeMap() {
        return defineNodeMap;
    }

    public Map<String, ImportObjectNode> getNameNodeMap() {
        return nameNodeMap;
    }

    public List<ImportObjectNode> getDefaultValueNodeList() {
        return defaultValueNodeList;
    }

}
