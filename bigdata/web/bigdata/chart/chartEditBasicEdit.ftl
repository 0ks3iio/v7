<div class="box box-default padding-70" id="chart_basic_edit_container">
    <div class="form-horizontal" id="myForm">
        <div class="form-group">
            <h4 class="col-sm-2 control-label no-padding-right bold">基础设置&nbsp;</h4>　
        </div>
        <div class="form-group">
            <label class="col-sm-2 control-label no-padding-right">图表名称&nbsp;&nbsp;</label>
            <div class="col-sm-4">
                <input type="text" id="chart_name" value="${chart.name!}" class="form-control js-file-name" placeholder="请输入图表名称">
            </div>
        </div>
        <div class="form-group">
            <label class="col-sm-2 control-label no-padding-right">设置标签&nbsp;&nbsp;</label>
            <div class="col-sm-4">
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
                        <select name="folderId" id="folderId"
                                class="form-control chosen-select chosen-width"
                                data-placeholder="<#if folderList?exists && folderList?size gt 0>请选择文件夹<#else>请先维护文件夹</#if>">
                                <#if folderList?exists && folderList?size gt 0>
                                    <#list folderList as folder>
                                        <option value="${folder.id!}"
                                                <#if folderDetail.folderId! == folder.id!>selected</#if>>${folder.folderName!}</option>
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
            <div class="col-sm-6 order-type">
                <#if orderTypes?exists && orderTypes?size gt 0>
                    <#list orderTypes as orderType>
                    <label class="pos-rel">
                        <input <#if orderType.orderType==chart.orderType>checked</#if> type="radio" class="wp" value="${orderType.orderType!}" name="class-radio">
                        <span class="lbl">${orderType.orderName!}</span>
                    </label>
                    </#list>
                </#if>
            </div>
        </div>

        <div class="form-group order-tree"
            <#if chart.orderType?default(0) != 4 && chart.orderType?default(0) != 5> style="display: none;" </#if>>
            <label class="col-sm-2 control-label no-padding-right">&nbsp;&nbsp;</label>
            <div class="col-sm-5">
                <div class="bs-callout bs-callout-danger">
                    <ul class="nav nav-tabs">
                        <li class="active">
                            <a href="#aa" class="order-tree-unit" data-toggle="tab">下级单位</a>
                        </li>
                        <li class=" order-tree-teacher" <#if chart.orderType?default(0) != 5>style="display: none;"</#if>>
                            <a href="#bb" data-toggle="tab">教师</a>
                        </li>
                    </ul>
                    <div class="tab-content tree-wrap tree-tab width-1of1">
                        <div class="row no-margin">
                            <div class="filter-item col-sm-8 no-margin-right">
                                <div class="pos-rel pull-left width-1of2">
                                    <input id="tree_search_keywords" type="text" class="typeahead scrollable form-control width-1of1" autocomplete="off" data-provide="typeahead" placeholder="请输入关键词">
                                </div>
                                <div class="input-group-btn">
                                    <button id="tree_search" type="button" class="btn btn-default">
                                        <i class="fa fa-search"></i>
                                    </button>
                                </div>
                            </div>
                            <div class="col-sm-2 switch-button text-right">
                                <span>级联:</span>
                            </div>
                            <div class="col-sm-2 switch-button">
                                <input id="tree_cascade" name="switch-field-1" class="wp wp-switch" type="checkbox">
                                <span class="lbl"></span>
                            </div>
                        </div>
                        <div class="tab-pane active" id="aa">
                            <ul id="treeDemo-one" class="ztree"></ul>
                        </div>
                        <div class="tab-pane" id="bb">
                            <ul id="treeDemo-two" class="ztree"></ul>
                        </div>
                    </div>
                </div>
            </div>

            <div class="col-sm-4">
                <div class="bs-callout bs-callout-danger">
                    <p class="choose-num">已选（<span>0</span>）</p>
                    <div class="choose-item">
                        <div class="no-data">
                            <#--<img src="../images/big-data/no-choice.png"/>-->
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <div class="form-group">
            <div class="col-sm-8 col-sm-offset-2">
                <button type="button" class="btn btn-long btn-blue js-added chart-basic-save">保存图表</button>
            </div>
        </div>
    </div>

</div>
<input type="hidden" value="1" id="tag_type" />
<link rel="stylesheet" href="${request.contextPath}/static/ztree/zTreeStyle/zTreeStyle.css"/>
<script src="${request.contextPath}/static/ztree/js/jquery.ztree.all-3.5.min.js"></script>
<script>
    var _chart_json = ${chart.toJSONString()};
    _chart_json.datasourceType = _chart_json.dataSourceType;
</script>
<script src="${request.contextPath}/bigdata/v3/static/js/chartEdit.basicEdit.js"/>
