<div class="box box-default padding-70" id="chart_basic_edit_container">
    <div class="form-horizontal" id="myForm">
        <div class="form-group">
            <h4 class="col-sm-2 control-label no-padding-right bold">基础设置&nbsp;</h4>　
        </div>
        <div class="form-group">
            <label class="col-sm-2 control-label no-padding-right">报表名称&nbsp;&nbsp;</label>
            <div class="col-sm-4">
                <input type="text" id="chart_name" value="${chart.name!}" maxlength="100" class="form-control js-file-name" placeholder="请输入报表名称">
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

        <div class="form-group">
            <h4 class="col-sm-2 control-label no-padding-right bold">文件夹设置&nbsp;</h4>　
        </div>
        <div class="form-group">
            <label class="col-sm-2 control-label no-padding-right">文件夹&nbsp;&nbsp;</label>
            <div class="col-sm-4">
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
            <div class="col-sm-4">
                <input type="text" name="orderId" id="orderId" class="form-control" nullable="true" onkeyup="if(this.value.length==1){this.value=this.value.replace(/[^1-9]/g,'')}else{this.value=this.value.replace(/\D/g,'')}" onafterpaste="if(this.value.length==1){this.value=this.value.replace(/[^1-9]/g,'')}else{this.value=this.value.replace(/\D/g,'')}" maxlength="3" value="${folderDetail.orderId!}">
            </div>
        </div>

        <div class="form-group">
            <h4 class="col-sm-2 control-label no-padding-right bold">授权设置&nbsp;</h4>　
        </div>
        <div class="form-group">
            <label class="col-sm-2 control-label no-padding-right">类型&nbsp;&nbsp;</label>
            <div class="col-sm-10 order-type">
                <#if orderTypes?exists && orderTypes?size gt 0>
                    <#list orderTypes as orderType>
                    <label class="choice">
                        <input <#if orderType.orderType==chart.orderType>checked</#if> type="radio" class="wp" value="${orderType.orderType!}" name="class-radio">
                        <span class="choice-name"> ${orderType.orderName!}</span>
                    </label>
                    </#list>
                </#if>
            </div>
        </div>

        <div class="form-group order-tree">
            <label class="col-sm-2 control-label no-padding-right">报表名称&nbsp;&nbsp;</label>
            <div class="col-sm-10 order-main-div">

            </div>
        </div>

        <div class="form-group">
            <div class="col-sm-8 col-sm-offset-2">
                <button type="button" class="btn btn-long btn-blue js-added report-basic-save">保存报表</button>
            </div>
        </div>
    </div>

</div>
<input type="hidden" value="3" id="tag_type" />
<input type="hidden" value="report" id="type" />
<link rel="stylesheet" href="${request.contextPath}/static/ztree/zTreeStyle/zTreeStyle.css"/>
<script src="${request.contextPath}/static/ztree/js/jquery.ztree.all-3.5.min.js"></script>
<script>
    var _chart_json = ${chart.toJSONString()};
    _chart_json.datasourceType = _chart_json.dataSourceType;
    _chart_json.dataSourceId = _chart_json.databaseId;
    _chart_json.termMap = '${termMap!}';
    _chart_json.templateId = '${chart.templateId!}';
    _chart_json.templateName = '${template.templateName!}';
    _chart_json.templatePath = '${template.templatePath!}';
    _chart_json.templateFileName = '${template.templateFileName!}';
    _chart_json.thumbnailPath = '${template.thumbnailPath!}';
    _chart_json.thumbnailFileName = '${template.thumbnailFileName!}';
    $('.report-basic-save').on('click', function () {
        _chart_json.name = $('#chart_name').val();
        if (_chart_json.name.length == 0) {
            tips('报表名称不能为空', '#chart_name');
            return;
        }
        if (_chart_json.name.replace(/[\u0391-\uFFE5]/g,"aa").length > 100) {
            tips('报表名称长度不能超过100', '#chart_name');
            return;
        }
        var data = _chart_json ? _chart_json : {};
        var tags = [];
        $('span.selected').each(function () {
            tags.push($(this).attr('tag_id'));
        });
        if (tags.length > 3) {
            console.log("tags more");
        }
        data.tags = tags;
        var orderType = $('input:radio:checked').val();
        data.orderType = orderType;
        //授权单位 和 人
        var unitArray = [];
        if (orderType == '4') {
            zTreeSelectedUnitIdMap.forEach(function (value, key, map) {
                unitArray.push(key);
            });
        }

        var teacherArray = [];
        if (orderType == '6') {
            zTreeSelectedUserIdMap.forEach(function (value, key, map) {
                teacherArray.push(key);
            });
        }

        data.orderUnit = unitArray;
        data.orderTeacher = teacherArray;

        if ($('#folderId').val() == null) {
            tips('请选择文件夹', '#folderId');
            return;
        }

        data.folderId = $('#folderId').val();
        data.orderId = $('#orderId').val();

        //ajax调用保存
        $.ajax({
            url: '${request.contextPath}/bigdata/report/template/save',
            data: data,
            type: 'POST',
            dataType: 'json',
            success: function (val) {
                if (!val.success) {
                    showLayerTips4Confirm('error', val.message, 't', null);
                }
                else {
                    // createReportScreen();
                    layer.msg('保存成功', {icon: 1, offset:'t', time: 1000}, function () {
                        var href = '/bigdata/report/template/index';
                        routerProxy.go({
                            path: href,
                            level: 2,
                            name: '报表设置'
                        }, function () {
                            $('.page-content').load("${springMacroRequestContext.contextPath}" + href);
                        });
                    });
                }
            },
            error: function () {
                $('#tagsList').load('${request.contextPath}/bigdata/chart/layer/tags?chartId=' + $('#chart_id').val());
            }
        });

    });

    function createReportScreen() {
        //延时执行，让页面渲染完成
        $.ajax({
            type: "POST",
            url: '${request.contextPath}/bigdata/report/template/screenshot',
            data: {
                data: '',
                chartId: $('#chart_id').val()
            },
            timeout: 60000,
            success: function (data) {

            }
        });
    }

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
<script src="${request.contextPath}/bigdata/v3/static/js/chartEdit.basicEdit.js"/>
