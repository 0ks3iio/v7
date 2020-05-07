<form id="metadataForm">
    <div class="form-horizontal">
        <input type="hidden" id="tableViewId" name="id" value="${tableView.id!}">
        <input type="hidden" name="parentId" value="${parentId!}">

        <div class="form-group">
            <label class="col-sm-2 control-label no-padding-right"><font style="color:red;">*</font>名称：</label>
            <div class="col-sm-8">
                <input type="text" name="name" id="name" class="form-control" nullable="false" maxlength="30"
                       value="${tableView.name!}">
            </div>
        </div>

        <div class="form-group">
            <label class="col-sm-2 control-label no-padding-right"><font style="color:red;">*</font>列：</label>
            <div class="col-sm-8">
                    <select multiple name="" id="columnSelect"
                            class="chosen-select"
                            data-placeholder="选择字段">
                            <#if columns?exists && columns?size gt 0>
                                <#list columns as column>
                                    <option value="${column.columnName!}" <#if tableView.tableName!?contains(column.columnName!)?string("yes", "no") == "yes"> selected="selected"</#if>>${column.name!}</option>
                                </#list>
                            </#if>
                    </select>
            </div>
        </div>

        <div class="form-group">
            <label class="col-sm-2 control-label no-padding-right">备注：</label>
            <div class="col-sm-8">
                <textarea name="remark" id="remark" type="text/plain" nullable="true" maxlength="200"
                          style="width:100%;height:80px;resize: none;">${tableView.remark!}</textarea>
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
                    $(this).next().find('.chosen-results').css('height','125px').addClass('scrollbar-made');
                    $(this).next().find('.chosen-search-input').css('width','77px');
                })
            }).trigger('resize.chosen');
        $('.chosen-choices').css('min-height', '32px');
        $('.chosen-choices').css('z-index', '9999999');
    }
</script>