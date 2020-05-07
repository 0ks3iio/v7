<#import "dynamic.ftl" as dynamic/>
<style type="text/css">
    .box-self{
        position:relative;
        margin-bottom:20px;
        padding: 10px 0px 20px;
    }
    .box-header-self{
        position:relative;
    }
    .box-title-self{
        padding:20px 0;
        font-size:20px;
        font-weight:normal;
        line-height:20px;
        text-align:center;
        font-weight: bolder;
        color:#535353;
    }
    .box-body-self{
        height: 200px;	/*设置高才有滚动*/
        padding:0 20px;
        /*overflow: hidden;*/
        /*overflow-x: hidden;*/
        overflow-y: auto;
        /*display:flex;
        justify-content:center;
        align-items:center;*/
    }
    /*滚动条隐藏，效果仍然在*/
    .box-body-self::-webkit-scrollbar{
        display: none;
    }
    /*定义滚动条的样式*/
    /*.box-body-self::-webkit-scrollbar{
        width: 5px;
        height: 5px;
        background-color: #F5F5F5;
    }
    .box-body-self::-webkit-scrollbar-track{
        -webkit-box-shadow: inset 0 0 6px rgba(0,0,0,0.3);
        border-radius: 10px;
        background-color: #F5F5F5;
    }
    .box-body-self::-webkit-scrollbar-thumb{
        border-radius: 10px;
        -webkit-box-shadow: inset 0 0 6px rgba(0,0,0,.3);
        background-color: #555;
    }*/

    /*自定义数值*/
    .teacher-number{
        width: 100%;
        margin:0 -1.7%;
        overflow:hidden;
        font-size:0;
        text-align:center;
    }
    .teacher-number li{
        display:inline-block;
        padding:12px 2.6%;
        border:1px solid #666;
        border-radius:5px;
        margin:0 1.6%;
        font-size:24px;
        line-height:1;
        color:#535353;
        font-weight: bolder;
    }

    /*比例*/
    .teacher-sex{
        width: 100%;
        border-radius:15px;
        overflow:hidden;
        height:28px;
        line-height:28px;
    }
    .teacher-sex div{
        float:left;
        height:100%;
        font-size:14px;
        text-align:right;
        color:#fff;
    }
    .teacher-sex-male{background-color:#1f83f5;}
    .teacher-sex-female{background-color:#d142a4;}

    /*报表*/
    table {
        border-spacing: 0;
        border-collapse: collapse;
    }
    td,
    th {
        padding: 0;
    }
    .table-self{
        width:100%;
        max-width:100%;
    }
    .table-self th,
    .table-self td{
        padding:10px 12px;
        text-align: left;
    }
    .table-self thead, .table-self tbody{border-bottom: 3px solid #999}
    .table-self thead{border-top: 3px solid #999;}
    .table-self th{color: #323232;}
    .table-self td{border-bottom:1px solid #999;color: #343434;}

    /*字体数值*/
    @font-face {
        font-family: Quartz;
        src: url("${request.contextPath}/static/bigdata/css/fonts/Quartz Regular.ttf");
    }
    .number-self {
        /*width: 100%;*/
        font-family: Quartz , sans-serif;
        font-size: 72px;
        color: #368ff7;
        text-align: center;
        /*overflow: hidden;*/
    }

    /*动画位移*/
    /*.animate{
        position:relative;
        animation:mymove 5s infinite;
        -webkit-animation:mymove 5s infinite;
    }*/

    .vetically-center{
        position: absolute;
        top: 50%;
        left: 50%;
        transform: translate(-50%,-50%);
    }

    /*数据升降*/
    .data-change{
        font-size: 50px;
        font-weight: bolder;
        text-align: center;
    }
    .data-change-up {color: #1f83f5;}
    .data-change-down{color: #d042a4;}

</style>

<div class="facing" id="chart_container">
    <div class="left-part" id="chart_type_list" chart-show="false">
        <#if chartTypes?exists && chartTypes?size gt 0>
        <#list chartTypes as ct>
            <#assign selected=(ct_index==0 && chart.id?default('') == '' && chart.chartType?default(0)==0) />
            <#assign expand=seriesName==ct.seriesName />
        <p <#if expand> class="bg-grey"</#if> data-toggle="collapse" href="#ct_${ct_index}">
            ${ct.name!}
            <i class="arrow fa fa-angle-down"></i>
        </p>
        <div id="ct_${ct_index}" class="collapse <#if expand>in"</#if>
             aria-expanded="<#if expand>true<#else>false</#if>">
            <div class="box-boder no-border text-center">
                <#list ct.chartCategoryClassifications as cct>
                    <img type="${cct.chartType!}"
                         src="${cct.thumbnail!}" <#if (selected && cct_index==0)||cct.chartType==chart.chartType!0>
                         class="border-active"</#if>/>
                </#list>
            </div>
        </div>
        </#list>
        </#if>
    </div>

    <div class="right-part">
        <ul class="nav nav-tabs nav-tabs-1">
            <li class="text-center active col-md-6">
                <a data-toggle="tab" href="#aa" id="datasource_tab">数据源</a>
            </li>
            <li class="text-center col-md-6">
                <a data-toggle="tab" href="#bb" id="my_parameters">自定义参数</a>
            </li>
        </ul>

        <div class="tab-content padding-tab">
            <div id="aa" class="tab-pane active">
                <#if composite?default(false)>
                    <@dynamic.data_dynamic elements=[] datasource_types=dataSourceTypes?default([]) datasources=datasources?default([]) />
                <#else>
                    <div class="bs-docs-section" id="drop-down">
                        <div class="filter">
                            <div class="filter-item block">
                                <span class="filter-name">数据类型&nbsp;&nbsp;</span>
                                <select name="" id="select_datasourceType" class="form-control chosen-select chosen-width"
                                        data-placeholder="未选择">
                                    <#if dataSourceTypes?? && dataSourceTypes?size gt 0>
                                        <#list dataSourceTypes as dataSourceType>
                                            <option
                                                <#if chart.dataSourceType?default(0)==dataSourceType.getValue() >selected</#if>
                                                value="${dataSourceType.getValue()!}">${dataSourceType.getName()!}</option>
                                        </#list>
                                    </#if>
                                </select>
                            </div>
                            <div class="filter-item block"
                                 <#if chart.dataSourceType?default(0) ==3>style="display: none;" </#if>>
                                <span class="filter-name">数据源	&nbsp;&nbsp;</span>
                                <select name="" id="select_datasourceId" class="form-control chosen-select chosen-width"
                                        data-placeholder="未选择">
                                    <#if datasources?? && datasources?size gt 0>
                                        <#list datasources as d>
                                            <option
                                                <#if chart.databaseId?default('')==d.id || chart.apiId?default('')==d.id>selected</#if>
                                                value="${d.id!""}">${d.name!""}</option>
                                        </#list>
                                    </#if>
                                </select>
                            </div>
                        </div>
                    </div>
                    <div class="text" id="aceEditor" style="width: 100%;">
                        <pre id="editor" >

                        </pre>
                        <textarea id="dataSet" name="" rows="" cols="" style="width: 100%;display: none;"
                                  placeholder="<请在此插入SQL>">${chart.dataSet!}</textarea>
                    </div>
                    <div id="text_blank" class="blank" style="display: none;"></div>
                </#if>
            </div>

            <div id="bb" class="tab-pane">
                <div class="filter">
                    <div class="filter-item block update-inteval">
                        <span class="filter-name">频率(秒)&nbsp;</span>
                        <div class="filter-content">
                        <input name="updateInterval" id="chart_updateInterval"
                               value="${chart.updateInterval!}" class="form-control width-191" placeholder="建议30-60秒"/>
                        </div>
                    </div>
                    <div class="filter-item block js-width" style="<#if chart.chartType?default(-1)!=91 && chart.chartType?default(-1)!=93>display:none;</#if>">
                        <span class="filter-name">地图&nbsp;</span>
                        <select name="map" id="map"
                                class=" form-control  chosen-select chosen-width" data-placeholder="未选择">
                            <option value="00" <#if chart.map! =="00">selected</#if>>中国</option>
                            <#if regions?? && regions?size gt 0>
                                <#list regions as r>
                                    <option value="${r.regionCode!}" <#if chart.map! ==r.regionCode>selected</#if>>${r.regionName!}</option>
                                </#list>
                            </#if>
                        </select>
                    </div>
                </div>
                <#--<div class="blank"></div>-->
            </div>

        </div>
        <div class="btn-wrap">
            <#--<div class="col-md-6 col-sm-6 no-padding-left clearfix">-->
                <#--<button type="button" id="run_chart" class="btn btn-default btn-block">运行数据</button>-->
            <#--</div>-->
            <#--<div class="col-md-6 col-sm-6 no-padding-right clearfix">-->
                <#--<button id="chart_save" class="btn btn-blue btn-block">保存图表</button>-->
            <#--</div>-->
            <button type="button" id="run_chart" class="btn btn-default btn-block">运行数据</button>
            <button id="chart_save" class="btn btn-blue btn-block">保存图表</button>
        </div>
    </div>

    <div class="chart-show">
        <div id="main"></div>
    </div>

</div>

<div class="layer layer-save">
    <div class="filter-item block">
        <span class="filter-name">图表名称　</span>
        <div class="filter-content">
            <input type="text" id="chart_name" value="${chart.name!}" class="form-control width-300"
                   placeholder="请输入图表名称">
        </div>
    </div>
    <div id="tagsList">
        <div class="filter-item block">
            <span class="filter-name">设置标签　</span>
            <div class="">
                <select multiple name="" id="tag_selection" class="form-control chosen-select"
                        data-placeholder="<#if tags?? && tags?size gt 0>请选择合适的标签<#else>没有可选标签，去基础设置新建一个吧</#if>"
                        style="width:300px;">
                <#if tags?? && tags?size gt 0>
                    <#list tags as tag>
                        <option value="${tag.id!}" <#if tag.selected!false>selected</#if>>${tag.name!}</option>
                    </#list>
                </#if>
                </select>
            </div>
        </div>
    </div>
    <input type="hidden" id="run_run" value="${run?default(false)?string}" />
</div>
<script src="${request.contextPath}/static/bigdata/js/echarts/myCharts.js"></script>
<#--<script src="${request.contextPath}/static/bigdata/js/html2canvas.0.4.12/html2canvas.min.js"></script>-->
<script src="${request.contextPath}/static/bigdata/js/html2canvas.js"/>
<script src="${request.contextPath}/static/bigdata/editor/ace.js"/>
<script src="${request.contextPath}/static/bigdata/editor/ext-language_tools.js"/>
<script src="${request.contextPath}/static/bigdata/editor/mode-json.js"/>
<script src="${request.contextPath}/static/bigdata/editor/mode-sql.js"/>
<script src="${request.contextPath}/static/bigdata/editor/theme-xcode.js"/>
<script>
    var _chartEdit_json = ${chart.toJSONString()};
</script>
<script src="${request.contextPath}/bigdata/v3/static/js/chartEdit.edit.js"/>