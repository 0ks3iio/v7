<#macro data_common>

</#macro>

<!-- names not empty -->
<#macro data_dynamic elements datasource_types datasources>
    <div class="bs-example" data-example-id="nav-tabs-with-dropdown" id="myTabs">
        <ul class="nav nav-tabs">
            <#list elements as element>
                <#if element_index <2>
                <li role="presentation" <#if element_index==0>class="active"</#if>><a href="#n_${element_index}">${element.name!}</a></li>
                </#if>
            </#list>
            <#if elements?size gt 3>
            <li role="presentation" class="dropdown">
                <a class="dropdown-toggle" data-toggle="dropdown" href="#" role="button" aria-haspopup="true"
                   aria-expanded="true" id="more">
                    更多<span class="caret"></span>
                </a>
                <ul class="dropdown-menu min-width-1of1 js-more" role="menu">
                    <li><a href="#">折线图</a></li>
                    <li><a href="#">雷达图</a></li>
                    <li><a href="#">标签云</a></li>
                    <li><a href="#">地图</a></li>
                </ul>
            </li>
            </#if>
        </ul>
    </div>
	<div class="tab-content">
        <#list elements as element>
            <div class="tab-pane <#if element_index==0>active</#if>" id="n_${element_index}">
                <div class="bs-docs-section" id="drop-down">
                    <div class="filter">
                        <div class="filter-item block">
                            <span class="filter-name">数据类型&nbsp;&nbsp;</span>
                            <select name="" id="" class="form-control chosen-select chosen-width" data-placeholder="未选择">
                                <#if datasource_types?exists && datasource_types?size gt 0>
                                    <#list datasource_types as dt>
                                        <option <#if element.dataSource==dt.getValue()>selected</#if> value="${dt.getValue()}">${dt.getName()!}</option>
                                    </#list>
                                </#if>
                            </select>
                        </div>
                        <div class="filter-item block">
                            <span class="filter-name">数据源	&nbsp;&nbsp;</span>
                            <select  name="" id="" class="form-control chosen-select chosen-width" data-placeholder="未选择">
                                <#if datasources?exists && datasources?size gt 0>
                                    <#list datasources as ds>
                                        <option <#if ds.id==element.apiId?default("") || ds.id==element.databaseId?default("")></#if> value="${ds.id}">${ds.name}</option>
                                    </#list>
                                </#if>
                            </select>
                        </div>
                    </div>
                </div>
                <div class="text">
                    <textarea name="" rows="" cols="" style="width: 100%;" placeholder="<请在此插入数据>">${element.dataSet!}</textarea>
                </div>
            </div>
        </#list>
    </div>
</#macro>