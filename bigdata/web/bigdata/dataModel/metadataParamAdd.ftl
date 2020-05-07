<form id="modelParamForm">
    <div class="form-horizontal form-made">
        <input type="hidden" name="id" value="${param.id!}">
        <input type="hidden" name="code" value="${param.code!}">
        <input type="hidden" id="paramType" name="type" value="${param.type!}">
        <input type="hidden" id="builtInSource" name="source" value="${model.source}">
            <div class="form-group">
                <label class="col-sm-3 control-label no-padding-right"><font style="color:red;">*</font>表名：</label>
                <div class="col-sm-8">
                    <select id="metadataSelect" name="useTable" class="form-control">
                        <#if metadataList?exists>
                            <#list metadataList as metadata>
                                <option value="${metadata.id!}" <#if metadata.id == param.useTable!>selected="selected"</#if> >${metadata.name!}</option>
                            </#list>
                        </#if>
                    </select>
                </div>
            </div>

        <div class="form-group">
            <label class="col-sm-3 control-label no-padding-right"><font style="color:red;">*</font>名称：</label>
            <div class="col-sm-8">
                <input type="text" name="name" id="modelParamName" class="form-control" nullable="false" maxlength="50"
                       value="${param.name!}">
            </div>
        </div>

        <#if param.type?default('index') == 'index'>
            <div class="form-group">
                <label class="col-sm-3 control-label no-padding-right"><font style="color:red;">*</font>聚合函数类型：</label>
                <div class="col-sm-8">
                    <select id="measureSelect" name="measures" class="form-control">
                        <option value="count" <#if param.measures?default('count') == 'count'>selected="selected"</#if>>
                            count
                        </option>
                        <option value="sum" <#if param.measures?default('count') == 'sum'>selected="selected"</#if>>
                            sum
                        </option>
                        <option value="avg" <#if param.measures?default('count') == 'avg'>selected="selected"</#if>>
                            avg
                        </option>
                        <option value="max" <#if param.measures?default('count') == 'max'>selected="selected"</#if>>
                            max
                        </option>
                        <option value="min" <#if param.measures?default('count') == 'min'>selected="selected"</#if>>
                            min
                        </option>
                        <option value="distinct count"
                                <#if param.measures?default('count') == 'distinct count'>selected="selected"</#if>>
                            distinct count
                        </option>
                    </select>
                </div>
            </div>
        </#if>

        <div class="form-group hide">
            <label class="col-sm-3 control-label no-padding-right"><font style="color:red;">*</font>字段名：</label>
            <div class="col-sm-8">
                <select id="fieldSelect" name="useField" class="form-control">
                    <#if useField??>
                        <option value="${useField.id!}">${useField.name}</option>
                    </#if>
                </select>
            </div>
        </div>

        <#if param.type?default('index') == 'dimension'>
            <div class="form-group" hidden="hidden">
                <label class="col-sm-3 control-label no-padding-right">上级维度：</label>
                <div class="col-sm-8">
                    <select  style="height: auto" name="parentId" id="parentId" class="form-control">
                        <#if parentName??>
                            <option value="${param.parentId!}">${parentName}</option>
                        <#else >
                            <option value="">请选择上级维度</option>
                        </#if>
                    </select>
                </div>
            </div>
        </#if>

        <div class="form-group hide">
            <label class="col-sm-3 control-label no-padding-right">排序字段：</label>
            <div class="col-sm-8">
                <select id="orderFieldSelect" name="orderField" class="form-control">
                    <#if orderField??>
                        <option value="${orderField.id!}">${orderField.name}</option>
                    <#else >
                        <option value="">请选择排序字段</option>
                    </#if>
                </select>
            </div>
        </div>

        <div class="form-group hide">
            <label class="col-sm-3 control-label no-padding-right">自定义排序：</label>
            <div class="col-sm-8">
                <textarea name="orderJson" id="orderJson" type="text/plain" nullable="true" maxlength="1000"
                          placeholder='请确保所有值维护的排序号位数都是3位，否则结果有可能乱掉。自定义排序格式：{"星期一":"001","星期二":"002","星期三":"003","星期四":"004","星期五":"005","星期六":"006","星期日":"007"}'
                          style="width:100%;height:100px;resize: none">${param.orderJson!}</textarea>
            </div>
        </div>

        <div class="form-group hide">
            <label class="col-sm-3 control-label no-padding-right">外键字段：</label>
            <div class="col-sm-8">
                <input type="text" name="dimForeignId" id="dimForeignId" class="form-control" nullable="true"
                       maxlength="50" value="${param.dimForeignId!}">
            </div>
        </div>

        <div class="form-group hide">
            <label class="col-sm-3 control-label no-padding-right">关联表字段：</label>
            <div class="col-sm-8">
                <input type="text" name="factForeignId" id="factForeignId" class="form-control" nullable="true"
                       maxlength="50" value="${param.factForeignId!}">
            </div>
        </div>

        <div class="form-group">
            <label class="col-sm-3 control-label no-padding-right"><font style="color:red;">*</font>排序号：</label>
            <div class="col-sm-8">
                <input type="text" name="orderId" id="orderId" class="form-control" nullable="true"
                       onkeyup="if(this.value.length==1){this.value=this.value.replace(/[^1-9]/g,'')}else{this.value=this.value.replace(/\D/g,'')}"
                       onafterpaste="if(this.value.length==1){this.value=this.value.replace(/[^1-9]/g,'')}else{this.value=this.value.replace(/\D/g,'')}"
                       maxlength="5" value="${param.orderId!}">
            </div>
        </div>

        <div class="form-group">
            <label class="col-sm-3 control-label no-padding-right">是否隐藏&nbsp;&nbsp;</label>
            <div class="col-sm-8" style="height: 30px; margin-bottom: 0px;">
                <label class="switch mt-5">
                    <input name="switch-field-1" id="timeSwitch" <#if param.isFilter?default(0) == 1> checked="checked"</#if>
                        type="checkbox"
                           onchange="changeDisplaySwitch(this)">
                    <span class="switch-name">显示</span>
                </label>
                <input type="hidden" id="isFilter" name="isFilter" value="${param.isFilter!}">
            </div>
        </div>
    </div>
</form>
<script>
    $(function () {
        let useTable;

        $('#measureSelect').change(function () {

            var value = $('#measureSelect').val();
            if (value == 'count') {
                $('#fieldSelect').parent().parent().addClass('hide');
                $('#dimForeignId').parent().parent().addClass('hide');
                $('#factForeignId').parent().parent().addClass('hide');
                $('#orderFieldSelect').parent().parent().addClass('hide');
                $('#orderJson').parent().parent().addClass('hide');
                $('#useField').val('');
            } else {
                $('#fieldSelect').parent().parent().removeClass('hide');
                if ($('#paramType').val() == 'dimension') {
                    $('#dimForeignId').parent().parent().removeClass('hide');
                    $('#factForeignId').parent().parent().removeClass('hide');
                    $('#orderFieldSelect').parent().parent().removeClass('hide');
                    $('#orderJson').parent().parent().removeClass('hide');
                }
            }

        });
        
        $('#metadataSelect').change(function () {
            changeMetadata();
        });

        $('#parentId').on('click', function() {
            if(useTable==null||useTable!=$('#metadataSelect').val()) {
                useTable = $('#metadataSelect').val();
                $.ajax({
                    url: '${request.contextPath}/bigdata/model/showDimensionDataModel',
                    data: {
                        'useTable': $('#metadataSelect').val(),
                        'modelName': $('#modelParamName').val(),
                        'id': $("input[name=id]").val()
                    },
                    type: "post",
                    dataType: "json",
                    success: function (data) {
                        appendDimensionOption(data);
                    }
                })
            }
        });

        $.ajax({
            url: '${request.contextPath}/bigdata/model/showDimensionDataModel',
            data: {
                'useTable': $('#metadataSelect').val(),
                'modelName': $('#modelParamName').val(),
                'id':  $("input[name=id]").val()
            },
            type: "post",
            dataType: "json",
            success: function (data) {
                useTable =$('#metadataSelect').val();
                appendDimensionOption(data);
            }
        });

        var value = $('#measureSelect').val();
        if (value == 'count') {
            $('#fieldSelect').parent().parent().addClass('hide');
            $('#dimForeignId').parent().parent().addClass('hide');
            $('#factForeignId').parent().parent().addClass('hide');
            $('#orderFieldSelect').parent().parent().addClass('hide');
            $('#orderJson').parent().parent().addClass('hide');
            $('#useField').val('');
        } else {
            $('#fieldSelect').parent().parent().removeClass('hide');
        }
        if ($('#paramType').val() == 'dimension') {
            $('#dimForeignId').parent().parent().removeClass('hide');
            $('#factForeignId').parent().parent().removeClass('hide');
            $('#orderFieldSelect').parent().parent().removeClass('hide');
            $('#orderJson').parent().parent().removeClass('hide');
        }

        var metadataId = $('#metadataSelect').val();
        var type = $('#paramType').val();

        $.ajax({
            url: _contextPath + '/bigdata/metadata/getMetadataTableColumns',
            type: 'GET',
            data: {
                metadataId : metadataId,
                type: type
            },
            success: function (response) {
                $('#fieldSelect').find("option").not(":first").remove();
                $('#orderFieldSelect').find("option").not(":first").remove();
                $.each(response.data, function (i, v) {
                    <#if useField??>
                    if(v.id!=='${useField.id}'){
                        $('#fieldSelect').append('<option value="'+v.id+'">'+v.name+'</option>');
                    }
                    <#else>
                        $('#fieldSelect').append('<option value="'+v.id+'">'+v.name+'</option>');
                    </#if>

                    <#if orderField??>
                    if(v.id!=='${orderField.id}'){
                        $('#orderFieldSelect').append('<option value="'+v.id+'">'+v.name+'</option>');
                    }
                    <#else>
                    $('#orderFieldSelect').append('<option value="'+v.id+'">'+v.name+'</option>');
                    </#if>
                });
            }
        });
    });

    function changeDisplaySwitch(e) {
        if ($(e).is(':checked')) {
            $('#isFilter').val('1');
        } else {
            $('#isFilter').val('0');
        }
    }



    function changeMetadata() {
        var metadataId = $('#metadataSelect').val();
        var type = $('#paramType').val();
        $.ajax({
            url: _contextPath + '/bigdata/metadata/getMetadataTableColumns',
            type: 'GET',
            data: {
                metadataId : metadataId,
                type: type
            },
            success: function (response) {
                $('#fieldSelect').empty();
                if($('#orderFieldSelect').val==null){
                    $('#orderFieldSelect').find("option").not(":first").remove();
                }else{
                    $('#orderFieldSelect').empty();
                    $('#orderFieldSelect').append('<option value="">'+"请选择排序字段"+'</option>');
                }

                $.each(response.data, function (i, v) {
                    <#if useField??>
                    if(v.id!=='${useField.id}'){
                        $('#fieldSelect').append('<option value="'+v.id+'">'+v.name+'</option>');
                    }
                    <#else>
                    $('#fieldSelect').append('<option value="'+v.id+'">'+v.name+'</option>');
                    </#if>

                    <#if orderField??>
                    if(v.id!=='${orderField.id}'){
                        $('#orderFieldSelect').append('<option value="'+v.id+'">'+v.name+'</option>');
                    }
                    <#else>
                    $('#orderFieldSelect').append('<option value="'+v.id+'">'+v.name+'</option>');
                    </#if>
                });

            }
        });
    }
    function appendDimensionOption(data) {
        let modelParamId=$("#parentId");
        if (data.data && data.data.length !== 0) {
            modelParamId.find("option").not(":first").remove();
            $.each(data.data, function (i, v) {
                if (modelParamId.val() !== v.id) {
                    modelParamId.append('<option value="'+v.id+'">'+v.name+'</option>');
                }
            })
            if ($("#parentId").val() !== "") {
                modelParamId.append('<option value="">'+"请选择上级维度"+'</option>');
            }
        }else {
            modelParamId.empty();
            modelParamId.append('<option value="">'+"请选择上级维度"+'</option>');
        }
    }



</script>