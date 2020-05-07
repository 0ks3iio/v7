<form id="submitForm">
    <div class="form-horizontal form-made">
        <input type="hidden" name="id" value="${model.id!}">
        <div class="form-group">
            <label class="col-sm-2 control-label no-padding-right">类型：</label>
            <div class="col-sm-8">
                <select id="typeSelect" class="form-control" name="source">
                    <option value="0" <#if model.source?default(0) == 0>selected="selected"</#if>>系统内置
                    </option>
                    <option value="1" <#if model.source?default(0) == 1>selected="selected"</#if>>
                        自定义数据源
                    </option>
                </select>
            </div>
        </div>
        <div class="form-group">
            <label class="col-sm-2 control-label no-padding-right">数据库类型：</label>
            <div class="col-sm-8">
                <select id="dbTypeSelect" class="form-control" name="type">
                    <option value="mysql" <#if model.type?default('mysql') == 'mysql'>selected="selected"</#if>>mysql
                    </option>
                    <option value="impala" <#if model.type?default('mysql') == 'impala'>selected="selected"</#if>>
                        impala
                    </option>
                    <option value="kylin" <#if model.type?default('mysql') == 'kylin'>selected="selected"</#if>>kylin
                    </option>
                </select>
            </div>
        </div>
        <div class="form-group">
            <label class="col-sm-2 control-label no-padding-right"><font style="color:red;">*</font>名称：</label>
            <div class="col-sm-8">
                <input type="text" name="name" id="modelName" class="form-control" nullable="false" maxlength="50"
                       value="${model.name!}">
            </div>
        </div>

        <div class="form-group">
            <label class="col-sm-2 control-label no-padding-right">数据源：</label>
            <div class="col-sm-8">
                <select id="dataSourceSelect" class="form-control" name="dbId">
                </select>
            </div>
        </div>

        <div class="form-group">
            <label class="col-sm-2 control-label no-padding-right"><font style="color:red;">*</font>code：</label>
            <div class="col-sm-8">
                <input type="text" name="code" id="code" class="form-control" nullable="false" maxlength="50"
                       value="${model.code!}" placeholder="系统唯一">
            </div>
        </div>

        <div class="form-group" hidden="hidden">
            <label class="col-sm-2 control-label no-padding-right"><font style="color:red;">*</font>项目名称：</label>
            <div class="col-sm-8">
                <input type="text" name="project" id="project" class="form-control" nullable="false" maxlength="50"
                       value="${model.project!}">
            </div>
        </div>

        <div class="form-group">
            <label class="col-sm-2 control-label no-padding-right"><font style="color:red;">*</font>排序号：</label>
            <div class="col-sm-8">
                <input type="text" name="orderId" id="orderId" class="form-control" nullable="true"
                       onkeyup="if(this.value.length==1){this.value=this.value.replace(/[^1-9]/g,'')}else{this.value=this.value.replace(/\D/g,'')}"
                       onafterpaste="if(this.value.length==1){this.value=this.value.replace(/[^1-9]/g,'')}else{this.value=this.value.replace(/\D/g,'')}"
                       maxlength="5" value="${model.orderId!}">
            </div>
        </div>

        <div class="form-group">
            <label class="col-sm-2 control-label no-padding-right">单位数据集权限：</label>
            <div class="col-sm-8">
                <label class="choice">
                    <input type="radio" <#if model.datasetType?default(1) == 1>checked="checked"</#if> class="all"
                           name="datasetType" value="1" onchange="changeDataSetType(this)">
                    <span class="choice-name"> 全部</span>
                </label>
                <label class="choice">
                    <input type="radio" class="unit" <#if model.datasetType?default(1) == 2>checked="checked"</#if>
                           name="datasetType" value="2" onchange="changeDataSetType(this)">
                    <span class="choice-name"> 按单位</span>
                </label>
                <label class="choice">
                    <input type="radio" class="union" <#if model.datasetType?default(1) == 3>checked="checked"</#if>
                           name="datasetType" value="3" onchange="changeDataSetType(this)">
                    <span class="choice-name"> 按行政区划</span>
                </label>
                <label class="pos-rel" id="remindLabel_unit"
                       <#if model.datasetType?default(1) != 2>hidden="hidden"</#if> >
                    <span class="lbl"><font style="color:red;">注意：创建模型表时必须有unit_id字段</font></span>
                </label>
                <label class="pos-rel" id="remindLabel_union"
                       <#if model.datasetType?default(1) != 3>hidden="hidden"</#if>>
                    <span class="lbl"><font style="color:red;">注意：创建模型表时必须有union_id字段</font></span>
                </label>
            </div>
        </div>

        <div class="form-group">
            <label class="col-sm-2 control-label no-padding-right">用户数据集&nbsp;&nbsp;</label>
            <div class="col-sm-8" style="height: 30px; margin-bottom: 0px;">
                <label class="switch switch-txt mt-5">
                    <input name="switch-field-1" id="timeSwitch" <#if model.userDatasetSwitch?default(0) == 1>
                       checked="checked"</#if> type="checkbox"
                           onchange="changeUserDatasetSwitch(this)">
                    <span class="switch-name">显示</span>
                </label>
                <input type="hidden" id="userDatasetSwitchInput" name="userDatasetSwitch"
                       value="${model.userDatasetSwitch!}">
            </div>
        </div>

        <div class="form-group">
            <label class="col-sm-2 control-label no-padding-right">时间维度&nbsp;&nbsp;</label>
            <div class="col-sm-8" style="height: 30px; margin-bottom: 0px;">
                <label class="switch switch-txt mt-5">
                    <input name="switch-field-1" id="timeSwitch" <#if model.dateDimSwitch?default(0) == 1>
                       checked="checked"</#if> type="checkbox"
                           onchange="changeTimeDateset(this)">
                    <span class="switch-name">显示</span>
                </label>
                <input type="hidden" id="dateDimSwitch" name="dateDimSwitch" value="${model.dateDimSwitch!}">
            </div>
        </div>

        <div class="form-group" <#if model.dateDimSwitch?default(0) == 0> style="display: none" </#if> >
            <label class="col-sm-2 control-label no-padding-right"><font style="color:red;">*</font>时间维度表：</label>
            <div class="col-sm-8">
                <input type="text" name="dateDimTable" id="dateDimTable" class="form-control" nullable="false"
                       value="${model.dateDimTable!}">
            </div>
        </div>

        <div class="form-group"  <#if model.dateDimSwitch?default(0) == 0> style="display: none"</#if> >
            <label class="col-sm-2 control-label no-padding-right"><font style="color:red;">*</font>日期属性：</label>
            <div class="col-sm-8">
                <input type="text" name="dateColumn" id="dateColumn" class="form-control" nullable="false"
                       value="${model.dateColumn!}">
            </div>
        </div>

        <div class="form-group">
            <label class="col-sm-2 control-label no-padding-right">备注：</label>
            <div class="col-sm-8">
                <textarea name="remark" id="remark" type="text/plain" nullable="true" maxlength="200"
                          style="width:100%;height:50px;resize: none;">${model.remark!}</textarea>
            </div>
        </div>
    </div>
</form>
<script>
    $(function () {
        $('#dbTypeSelect').change(function () {
            changeDbType();
        });

        $('#typeSelect').change(function () {
            var value = $('#typeSelect').val();
            if (value == '0') {
                $('#dataSourceSelect').parent().parent().css('display', 'none');
            } else {
                $('#dataSourceSelect').parent().parent().css('display', 'block');
                changeDataType($('#dbTypeSelect').val());
            }
        });

        var value = $('#typeSelect').val();
        if (value == '0') {
            $('#dataSourceSelect').parent().parent().css('display', 'none');
        } else {
            $('#dataSourceSelect').parent().parent().css('display', 'block');
            changeDataType($('#dbTypeSelect').val());
        }

        changeDbType();
    });

    function changeDbType() {
        var value = $('#dbTypeSelect').val();
        if (value == 'kylin') {
            $('#dbName').parent().parent().hide();
            $('#project').parent().parent().show();
            $('#dbName').val('');
        } else {
            $('#dbName').parent().parent().show();
            $('#project').parent().parent().hide();
            $('#project').val('');
        }
        changeDataType(value);
    }
    
    function changeDataType(type) {
        $.ajax({
            url: _contextPath + '/bigdata/datasource/getDatabaseList',
            type: 'GET',
            data: {
                type: type
            },
            success: function (response) {
                $('#dataSourceSelect').empty();
                $.each(response.data, function (i, v) {
                    $('#dataSourceSelect').append('<option value="'+v.id+'">'+v.name+'</option>')
                });
                $('#dataSourceSelect').val('${model.dbId!}');
            }
        });
    }

    function changeDataSetType(e) {

        var val = $(e).val();
        if (val == "1" && $(e).hasClass('all')) {
            $('#remindLabel_unit').hide();
            $('#remindLabel_union').hide();
        } else if (val == "2" && $(e).hasClass('unit')) {
            $('#remindLabel_unit').show();
            $('#remindLabel_union').hide();
        } else {
            $('#remindLabel_union').show();
            $('#remindLabel_unit').hide();
        }

    }

    function changeTimeDateset(e) {

        if ($(e).is(':checked')) {
            $('#dateDimTable').parent().parent().show();
            $('#dateColumn').parent().parent().show();
            $('#dateDimSwitch').val('1');
        } else {
            $('#dateDimTable').parent().parent().hide();
            $('#dateColumn').parent().parent().hide();
            $('#dateDimTable').val('');
            $('#dateColumn').val('');
            $('#dateDimSwitch').val('0');
        }

    }

    function changeUserDatasetSwitch(e) {
        if ($(e).is(':checked')) {
            $('#userDatasetSwitchInput').val('1');
        } else {
            $('#userDatasetSwitchInput').val('0');
        }
    }

</script>