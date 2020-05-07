<form id="metadataForm">
    <div class="form-horizontal">
        <input type="hidden" id="tableViewId" name="id" value="${tableIndex.id!}">
        <input type="hidden" name="mdId" value="${tableIndex.mdId!}">

        <div class="form-group">
            <label class="col-sm-2 control-label no-padding-right"><font style="color:red;">*</font>名称：</label>
            <div class="col-sm-8">
                <input type="text" name="name" id="name" class="form-control" nullable="false" maxlength="30"
                       value="${tableIndex.name!}">
            </div>
        </div>


            <div class="form-group <#if dbType?default('mysql') == 'hbase'>hide</#if>" >
                <label class="col-sm-2 control-label no-padding-right"><font style="color:red;">*</font>类型：</label>
                <div class="col-sm-8">
                    <select id="indexTypeSelect" name="type" class="form-control" onchange="changeIndexType()">
                        <option <#if tableIndex.type?default('1') == '1'>selected="selected"</#if> value="1">普通索引</option>
                        <option <#if tableIndex.type?default('1') == '2'>selected="selected"</#if> value="2">唯一索引</option>
                        <option <#if tableIndex.type?default('1') == '3'>selected="selected"</#if> value="3">主键索引</option>
                    </select>
                </div>
            </div>


        <div class="form-group">
            <label class="col-sm-2 control-label no-padding-right"><font style="color:red;">*</font>列：</label>
            <div class="col-sm-8">
                    <select multiple name="" id="columnSelect"
                            class="form-control chosen-select"
                            data-placeholder="选择字段">
                            <#if columns?exists && columns?size gt 0>
                                <#list columns as column>
                                    <option value="${column.id!}" <#if tableIndex.columns!?contains(column.id!)?string("yes", "no") == "yes"> selected="selected"</#if>>${column.name!}</option>
                                </#list>
                            </#if>
                    </select>
            </div>
        </div>
    </div>
</form>
<script>
    $(function () {
        initSelection();
    });

    //初始化标签查询下拉框
    function initSelection() {
        $('#columnSelect').chosen({
            allow_single_deselect: true,
            disable_search_threshold: 10,
            no_results_text: '没有找到字段',
            max_selected_options: 5
        });

        $(window).off('resize.chosen')
            .on('resize.chosen', function () {
                $('.chosen-select').each(function () {
                    $(this).next().css({'width': $('#name').width() + 20});
                    $(this).next().find('.chosen-results').css('height','65px').addClass('scrollbar-made');
                    $(this).next().find('.chosen-results').css('z-index','9999999');
                })
            }).trigger('resize.chosen');
        $('.chosen-choices').css('min-height', '32px');
    }
    
    function changeIndexType() {

    }
</script>