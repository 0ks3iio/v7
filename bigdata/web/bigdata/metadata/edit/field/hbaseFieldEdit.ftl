<form id="metadataForm">
    <div class="form-horizontal form-made">
        <input type="hidden" name="id" value="${field.id!}">
        <input type="hidden" name="metadataId" value="${metadataId!}">
        <input type="hidden" name="columnType" value="string">

        <div class="form-group">
            <label class="col-sm-3 control-label no-padding-right"><font style="color:red;">*</font>名称：</label>
            <div class="col-sm-7">
                <input type="text" name="name" id="name" class="form-control" nullable="false" maxlength="50"
                       value="${field.name!}">
            </div>
        </div>

        <div class="form-group">
            <label class="col-sm-3 control-label no-padding-right"><font style="color:red;">*</font>字段名：</label>
            <div class="col-sm-7">
                <input type="text" name="columnName" id="columnName" class="form-control" nullable="false"
                       maxlength="50" value="${field.columnName!}" placeholder="<#if isPhoenix! ==1>family:column(大写)<#else>family(大写) </#if>">
            </div>
        </div>

        <#if isPhoenix! ==1>
            <div class="form-group">
                <label class="col-sm-3 control-label no-padding-right">说明：</label>
                <div class="col-sm-6">
                    <div class="mt-7" style="color:red">系统默认ID为主键（ROWKEY)，无需维护</div>
                </div>
            </div>
        <#else>
            <div class="form-group">
                <label class="col-sm-3 control-label no-padding-right">说明：</label>
                <div class="col-sm-6">
                    <div class="mt-7" style="color:red">字段名只需维护列族（family)</div>
                </div>
            </div>
        </#if>

        <div class="form-group">
            <label class="col-sm-3 control-label"><font style="color:red;">*</font>排序号：</label>
            <div class="col-sm-7">
                <input type="text" name="orderId" id="orderId" class="form-control" nullable="true"
                       onkeyup="if(this.value.length==1){this.value=this.value.replace(/[^1-9]/g,'')}else{this.value=this.value.replace(/\D/g,'')}"
                       oninput="if(this.value.length==1){this.value=this.value.replace(/[^1-9]/g,'')}else{this.value=this.value.replace(/\D/g,'')}"
                       onafterpaste="if(this.value.length==1){this.value=this.value.replace(/[^1-9]/g,'')}else{this.value=this.value.replace(/\D/g,'')}"
                       maxlength="3" value="${field.orderId!}">
            </div>
        </div>

        <div class="form-group">
            <label class="col-sm-3 control-label no-padding-right">用途：</label>
            <div class="col-sm-7">
                <select multiple name="statType" id="statTypeSelect"
                        class="form-control chosen-select"
                        data-placeholder="选择用途">
                    <option <#if field.statType!?contains('index')?string("yes", "no") == "yes"> selected="selected"</#if> value="index">指标</option>
                    <option <#if field.statType!?contains('dimension')?string("yes", "no") == "yes"> selected="selected"</#if> value="dimension">维度</option>
                    <option <#if field.statType!?contains('showColumn')?string("yes", "no") == "yes"> selected="selected"</#if> value="showColumn">展示列</option>
                </select>
            </div>
        </div>

        <div class="form-group">
            <label class="col-sm-3 control-label no-padding-right">备注：</label>
            <div class="col-sm-7">
                <textarea name="remark" id="remark" type="text/plain" nullable="true" maxlength="200"
                          style="width:100%;height:50px;resize: none;">${field.remark!}</textarea>
            </div>
        </div>
    </div>
</form>
<script>
    function changeDatabaseType(){
        var val = $('#databaseTypeSelect').val();
        if (val == 'time' || val == 'date' || val == 'timestamp' || val == 'datetime' || val == 'string') {
            $('#columnLengthDiv').hide();
        } else {
            $('#columnLengthDiv').show();
        }

        if (val == 'float' || val == 'double') {
            $('#decimalLengthDiv').show();
        }else{
            $('#decimalLengthDiv').hide();
        }

    }

    $(function () {
        $('#databaseTypeSelect').change(function () {
            changeDatabaseType();
        });
        changeDatabaseType();
        initSelection();
    });

    //初始化标签查询下拉框
    function initSelection() {
        $('#statTypeSelect').chosen({
            allow_single_deselect: true,
            disable_search_threshold: 10,
            no_results_text: '没有找到字段',
            max_selected_options: 3
        });

        $(window).off('resize.chosen')
            .on('resize.chosen', function () {
                $('.chosen-select').each(function () {
                    $(this).next().css({'width': $('#name').width() + 20});
                    $(this).next().find('.chosen-results').css('height','100px').addClass('scrollbar-made');
                    $(this).next().find('.chosen-results').css('z-index','9999999');
                })
            }).trigger('resize.chosen');
        $('.chosen-choices').css('min-height', '32px');
    }

    function changeIsPrimaryKey(value) {
        if (value == '0') {
            $('#primaryTypeDiv').addClass('hide');
            $('#databaseTypeSelect').parent().parent().removeClass('hide');
            $('#columnLengthDiv').removeClass('hide');
        } else {
            $('#primaryTypeDiv').removeClass('hide');
            changePrimaryType($('input[name="primaryType"]:checked').val());
        }
    }
    
    function changePrimaryType(type) {
        if (type == '1') {
            $('#databaseTypeSelect').val('int');
            $('#databaseTypeSelect').parent().parent().addClass('hide');
            $('#columnLengthDiv').addClass('hide');
        } else {
            $('#databaseTypeSelect').parent().parent().removeClass('hide');
            $('#columnLengthDiv').removeClass('hide');
        }
    }
</script>