<link rel="stylesheet" type="text/css" href="${request.contextPath}/static/bigdata/icolor/icolor.css"/>
<link rel="stylesheet" href="${request.contextPath}/static/ztree/zTreeStyle/zTreeStyle.css"/>
<style>
    .box-self {
        position: relative;
        margin-bottom: 20px;
        padding: 10px 0px 20px;
    }

    .box-header-self {
        position: relative;
    }

    .box-title-self {
        padding: 20px 0;
        font-size: 20px;
        font-weight: normal;
        line-height: 20px;
        text-align: center;
        font-weight: bolder;
        color: #535353;
    }

    .box-body-self {
        height: 200px; /*设置高才有滚动*/
        padding: 0 20px;
        /*overflow: hidden;*/
        /*overflow-x: hidden;*/
        overflow-y: auto;
        /*display:flex;
        justify-content:center;
        align-items:center;*/
    }

    /*滚动条隐藏，效果仍然在*/
    .box-body-self::-webkit-scrollbar {
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
    .teacher-number {
        width: 100%;
        margin: 0 -1.7%;
        overflow: hidden;
        font-size: 0;
        text-align: center;
    }

    .teacher-number li {
        display: inline-block;
        padding: 12px 2.6%;
        border: 1px solid #666;
        border-radius: 5px;
        margin: 0 1.6%;
        font-size: 24px;
        line-height: 1;
        color: #535353;
        font-weight: bolder;
    }

    /*比例*/
    .teacher-sex {
        width: 100%;
        border-radius: 15px;
        overflow: hidden;
        height: 28px;
        line-height: 28px;
    }

    .teacher-sex div {
        float: left;
        height: 100%;
        font-size: 14px;
        text-align: right;
        color: #fff;
    }

    .teacher-sex-male {
        background-color: #1f83f5;
    }

    .teacher-sex-female {
        background-color: #d142a4;
    }

    /*报表*/
    table {
        border-spacing: 0;
        border-collapse: collapse;
    }

    td,
    th {
        padding: 0;
    }

    .table-self {
        width: 100%;
        max-width: 100%;
    }

    .table-self th,
    .table-self td {
        padding: 10px 12px;
        text-align: left;
    }

    .table-self thead, .table-self tbody {
        border-bottom: 3px solid #999
    }

    .table-self thead {
        border-top: 3px solid #999;
    }

    .table-self th {
        color: #323232;
    }

    .table-self td {
        border-bottom: 1px solid #999;
        color: #343434;
    }

    /*字体数值*/
    @font-face {
        font-family: Quartz;
        src: url("${request.contextPath}/static/bigdata/css/fonts/Quartz Regular.ttf");
    }

    .number-self {
        /*width: 100%;*/
        font-family: Quartz, sans-serif;
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

    /*数据升降*/
    .data-change {
        font-size: 50px;
        font-weight: bolder;
        text-align: center;
    }

    .data-change-up {
        color: #1f83f5;
    }

    .data-change-down {
        color: #d042a4;
    }
</style>
<div class="index">
<!-- 图表配置 -->
<div class="facing" id="chart_container">
    <div class="col-xs-6 no-padding height-1of1 <#--scroll-height-->">
        <div class="box box-default height-1of1 no-margin">
            <div class="box-header step-head">
                <ol class=" steps-made">
                    <li class="first active">
                        <a href="javascript:void(0);" step="template" style="text-decoration:none;" class="p-step">
                            <span class="step-made"></span>
                            <span class="step-made step-big look">1</span>
                            <p>选择模板</p>
                        </a>
                    </li>
                    <li class="active">
                        <a href="javascript:void(0);" step="datasource" style="text-decoration:none;" class="p-step">
                            <span class="step-made"></span>
                            <span class="step-made step-big">2</span>
                            <p>数据源</p>
                        </a>
                    </li>
                    <li class="active">
                        <a href="javascript:void(0);" step="echarts_parameters" style="text-decoration:none;" class="p-step">
                            <span class="step-made"></span>
                            <span class="step-made step-big">3</span>
                            <p>自定义参数</p>
                        </a>
                    </li>
                    <li class="last active">
                        <a href="javascript:void(0);" step="tag_authorization" style="text-decoration:none;" class="p-step">
                            <span class="step-made"></span>
                            <span class="step-made step-big">4</span>
                            <p>标签和授权</p>
                        </a>
                    </li>
                </ol>
            </div>
            <div class="box-body no-padding">
                <@body_chart_type chartTypes=chartTypes />
                <@body_datasource datasourceTypes=dataSourceTypes databases=databases apis=apis database=database api=api regions=regions chart=chart/>
                <div id="body_echarts_parameters" class="_step" style="display: none;">
                </div>
                <@body_self_parameters chart=chart tags=tags orderTypes=orderTypes />
            </div>
            <div class="text-right run-data">
                <button type="button" class="btn btn-blue next-button" id="next_datasource">下一步：数据源</button>
                <button type="button" class="btn btn-blue next-button" id="next_echarts_parameters" style="display: none;">下一步：自定义参数</button>
                <button type="button" class="btn btn-blue next-button" id="next_tag_authorization" style="display: none;">下一步：标签和授权</button>
                <button type="button" class="btn btn-blue next-button" id="next_save" style="display: none;">保存</button>
                <button type="button" class="btn btn-blue" id="screenshot">截取缩略图</button>
            </div>

        </div>
    </div>

    <div class="col-xs-6 no-padding height-1of1 <#--scroll-height vetically-center-->  show-part">
        <div class="canvas-wrap" id="main">
        </div>
    </div>
</div>
</div>
<script>
    var ec_instance = null;
    var _chart_json = ${chart.toJSONString()};
</script>
<script src="${request.contextPath}/static/bigdata/js/echarts/myCharts.js"></script>
<script src="${request.contextPath}/static/bigdata/js/html2canvas.js"/>
<script src="${request.contextPath}/static/bigdata/icolor/icolor.js"/>
<script src="${request.contextPath}/static/bigdata/editor/ace.js"/>
<script src="${request.contextPath}/static/bigdata/editor/ext-language_tools.js"/>
<script src="${request.contextPath}/static/bigdata/editor/mode-json.js"/>
<script src="${request.contextPath}/static/bigdata/editor/mode-sql.js"/>
<script src="${request.contextPath}/static/bigdata/editor/theme-xcode.js"/>
<script src="${request.contextPath}/static/ztree/js/jquery.ztree.all-3.5.min.js"></script>
<script src="${request.contextPath}/bigdata/v3/static/js/chartEditConfig.js"></script>
<script src="${request.contextPath}/bigdata/v3/static/js/chartEdit.basicEdit.js"></script>
<input type="hidden" value="1" id="tag_type" />

<!-- 选择模版的body部分 -->
<#macro body_chart_type chartTypes >
    <div id="body_template" class="_step">
        <div class="left-part left-part-two scroll-height-inner">
        <#if chartTypes?? && chartTypes?size gt 0>
            <#list chartTypes as ct>
                <p index="${ct_index}">${ct.name!}</p>
            </#list>
        </#if>
        </div>

        <div class="content-choice scroll-height-inner">
            <div class="imgs-choice  no-padding">
            <#if chartTypes?? && chartTypes?size gt 0>
                <#list chartTypes as ct>
                    <div class="litimg none" index="${ct_index}">
                        <#list ct.chartCategoryClassifications as cct>
                            <img type="${cct.chartType}" src="${cct.thumbnail}"/>
                        </#list>
                    </div>
                </#list>
            </#if>
            </div>
        </div>
    </div>
</#macro>

<#macro body_datasource datasourceTypes databases apis regions database=true api=false chart={}>
    <div id="body_datasource" class="_step">
        <div class="tab-content no-padding scroll-height-inner">
            <div class="tab-pane active" id="aa">
                <div class="box-body">
                    <div class="form">
                        <div class="form-group js-one">
                            <div class="filter filter-made clearfix">
                                <div class="filter-item">
                                    <span class="filter-name">数据类型：</span>
                                    <div class="filter-content">
                                        <select name="" id="datasourceType" class="form-control" data-placeholder="未选择">
                                            <#if datasourceTypes?? && datasourceTypes?size gt 0>
                                                <#list datasourceTypes as dt>
                                                    <option value="${dt.getValue()!}"
                                                            <#if chart.dataSourceType?default(0) == dt.getValue()>selected</#if>>${dt.getName()!}</option>
                                                </#list>
                                            </#if>
                                        </select>
                                    </div>
                                </div>
                                <div class="filter-item" <#if !(api||database)> style="display: none;"</#if>>
                                    <span class="filter-name">数据源：</span>
                                    <div class="filter-content">
                                        <select <#if !database>style="display: none;"</#if> name="" id="database"
                                                class="form-control" data-placeholder="未选择">
                                            <#if databases?? && databases?size gt 0>
                                                <#list databases as ds>
                                                    <option value="${ds.id!}" <#if chart.databaseId?default("")==ds.id>selected</#if>>${ds.name!}</option>
                                                </#list>
                                            </#if>
                                        </select>
                                        <select <#if !api>style="display: none;"</#if> name="" id="api"
                                                class="form-control" data-placeholder="未选择">
                                            <#if apis?? && apis?size gt 0>
                                                <#list apis as ds>
                                                    <option value="${ds.id!}" <#if chart.apiId?default("")==ds.id>selected</#if>>${ds.name!}</option>
                                                </#list>
                                            </#if>
                                        </select>
                                    </div>
                                </div>
                                <div class="filter-item">
                                    <span class="filter-name">地图：</span>
                                    <div class="filter-content select-height-36">
                                        <select name="" id="map" class="form-control" data-placeholder="未选择">
                                            <option value="00" <#if chart.map! =="00">selected</#if>>中国</option>
                                            <#if regions?? && regions?size gt 0>
                                                <#list regions as r>
                                                    <option value="${r.regionCode!}"
                                                            <#if province! ==r.regionCode>selected</#if>>${r.regionName!}</option>
                                                </#list>
                                            </#if>
                                        </select>
                                    </div>
                                </div>
                                <div class="filter-item" id="city_region" regionCode="${chart.map!}">
                                    <div class="filter-content select-height-36">

                                    </div>
                                </div>
                            </div>
                        </div>
                        <div class="form-group">
                            <p><b>编辑数据</b></p>
                            <div class="text scroll-height-area">
                                <pre id="editor" class="scroll-height-area">

                                </pre>
                            </div>
                        </div>
                        <div class="form-group no-margin" >
                            <p><b>自动更新</b></p>
                            <div class="js-height-93">
                                <p class="color-999 col-sm-3">自动更新请求每</p>
                                <input id="updateInterval" value="${chart.updateInterval!}" type="number" min="0" class="col-sm-1" />
                                <p class="color-999 col-sm-2">秒一次</p>
                            </div>
                        </div>
                        <div class="form-group self-set no-margin" id="data-demo" style="display: none;">
                            <p><b>示例数据</b></p>
                            <div class="js-height-93">
                                <p class="color-999">>demo数据1</p>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</#macro>




<#macro body_self_parameters chart tags orderTypes  >
    <div class="form-horizontal _step overflow-hidden scroll-height-inner" style="display: none;" id="body_tag_authorization">
        <div class="form-group">
            <h4 class="col-sm-2 control-label no-padding-right bold">基础设置&nbsp;</h4>　
        </div>
        <div class="form-group">
            <label class="col-sm-2 control-label no-padding-right">图表名称&nbsp;&nbsp;</label>
            <div class="col-sm-7">
                <input type="text" id="chart_name" value="${chart.name!}" class="form-control js-file-name" placeholder="请输入图表名称">
            </div>
        </div>
        <div class="form-group">
            <label class="col-sm-2 control-label no-padding-right">设置标签&nbsp;&nbsp;</label>
            <div class="col-sm-9">
                <p class="parallel">
                    标签设置不可超过4个字<span class="control-label">标签管理</span>
                </p>
                <div class="tag-set">
                    <#if tags?exists && tags?size gt 0>
                        <#list tags as tag>
                        <span>
                            <span tag_id="${tag.id!}" class="label-name <#if tag.selected>selected</#if> ">${tag.name!}</span>
                            <i class="fa fa-minus"></i>
                        </span>
                        </#list>
                    </#if>
                    <span>
                        <span class="plus-label">+</span>
                    </span>
                </div>
            </div>
        </div>
        <#--<div class="form-group">-->
            <#--<label class="col-sm-2 control-label no-padding-right">大屏专用&nbsp;&nbsp;</label>-->
            <#--<div class="col-sm-2 switch-button">-->
                <#--<input id="isForCockpit" name="switch-field-1" <#if chart.isForCockpit?? ><#if chart.isForCockpit == 1 >checked="checked"</#if></#if> class="wp wp-switch" type="checkbox">-->
                <#--<span class="lbl"></span>-->
            <#--</div>-->
        <#--</div>-->
        <div class="form-group">
            <h4 class="col-sm-2 control-label no-padding-right bold">文件夹设置&nbsp;</h4>　
        </div>
        <div class="form-group">
            <label class="col-sm-2 control-label no-padding-right">文件夹&nbsp;&nbsp;</label>
            <div class="col-sm-6">
                <div class="filter">
                    <div class="filter-item block">
                        <select name="folderId" id="folderId" onchange="changeFolder()"
                                class="form-control chosen-select chosen-width"
                                data-placeholder="<#if folderTree?exists && folderTree?size gt 0>请选择文件夹<#else>请先维护文件夹</#if>">
                                <#if folderTree?exists && folderTree?size gt 0>
                                    <#list folderTree as dir>
                                        <optgroup label="${dir.folderName!}">
                                            <#if folderMap[dir.id]?exists>
                                                <#list folderMap[dir.id] as item>
                                                     <option <#if folderDetail.folderId! == item.id!>selected</#if> value="${item.id!}">&nbsp;&nbsp;&nbsp;${item.folderName!}</option>
                                                </#list>
                                            </#if>
                                            <#list dir.childFolder as secondDir>
                                                <optgroup label="&nbsp;&nbsp;&nbsp;${secondDir.folderName!}">
                                                    <#if folderMap[secondDir.id]?exists>
                                                        <#list folderMap[secondDir.id] as secondDirItem>
                                                               <option <#if folderDetail.folderId! == secondDirItem.id!>selected</#if> value="${secondDirItem.id!}">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;${secondDirItem.folderName!}</option>
                                                        </#list>
                                                    </#if>
                                                    <#list secondDir.childFolder as thirdDir>
                                                        <optgroup label="&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;${thirdDir.folderName!}">
                                                            <#if folderMap[thirdDir.id]?exists>
                                                                  <#list folderMap[thirdDir.id] as thirdDirItem>
                                                                       <option <#if folderDetail.folderId! == thirdDirItem.id!>selected</#if> value="${thirdDirItem.id!}">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;${thirdDirItem.folderName!}</option>
                                                                  </#list>
                                                            </#if>
                                                        </optgroup>
                                                    </#list>
                                                </optgroup>
                                            </#list>
                                        </optgroup>
                                    </#list>
                                </#if>
                        </select>
                    </div>
                </div>
            </div>
        </div>
        <div class="form-group">
            <label class="col-sm-2 control-label no-padding-right">排序号&nbsp;&nbsp;</label>
            <div class="col-sm-6">
                <input type="text" name="orderId" id="orderId" class="form-control" nullable="true" onkeyup="if(this.value.length==1){this.value=this.value.replace(/[^1-9]/g,'')}else{this.value=this.value.replace(/\D/g,'')}" onafterpaste="if(this.value.length==1){this.value=this.value.replace(/[^1-9]/g,'')}else{this.value=this.value.replace(/\D/g,'')}" maxlength="3" value="${folderDetail.orderId!}">
            </div>
        </div>
        <div class="form-group">
            <h4 class="col-sm-2 control-label no-padding-right bold">授权设置&nbsp;</h4>　
        </div>
        <div class="form-group">
            <label class="col-sm-2 control-label no-padding-right">类型&nbsp;&nbsp;</label>
            <div class="col-sm-9 order-type">
                <#if orderTypes?exists && orderTypes?size gt 0>
                    <#list orderTypes as orderType>
                    <label class="choice">
                        <input <#if orderType.orderType==chart.orderType>checked</#if> type="radio" class="wp" value="${orderType.orderType!}" name="class-radio">
                        <span class="choice-name">${orderType.orderName!}</span>
                    </label>
                    </#list>
                </#if>
            </div>
        </div>
        <div class="order-main-div form-group chart-tree">

        </div>
    </div>
<script>
    
    $(function () {
        if ($('#orderId').val() == '') {
            changeFolder();
        }
    });
    
    function changeFolder() {
        $.ajax({
            url: '${request.contextPath}/bigdata/folder/getMaxOrderId',
            data:{folderId : $('#folderId').val()},
            dataType: 'json',
            success: function (val) {
                if (val.success) {
                    $('#orderId').val(val.data);
                } else {
                    layer.msg(val.message, {icon: 2, time: 1000});
                }
            }
        });
    }
</script>
</#macro>
