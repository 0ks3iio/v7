<#if contrastTimeInterval?exists && contrastTimeInterval?size gt 0 >
    <div class="mb-20">
        <div class="btn-group js-table-change">
            <button class="btn btn-blue switchTime" type="button" onclick="selectInterval(this, '${timeInterval!}')">${timeInterval!}</button>
            <#list contrastTimeInterval as item>
                <button class="btn btn-default switchTime" type="button" onclick="selectInterval(this, '${item!}')">${item!}</button>
            </#list>
        </div>
    </div>
</#if>
<input type="hidden" id="pageSize" value="${pageSize!}">
<script>
    getReport($('.days-num').text(), false);
    <#if contrastTimeInterval?exists && contrastTimeInterval?size gt 0 >
    <#list contrastTimeInterval as item>
        getReport('${item!}', true);
    </#list>
    </#if>

    <#if isShowExport?exists && isShowExport>
        $('#exportSpan').empty().append("当前分组值过多，完整数据请 <a style=\"cursor: pointer\" href=\"javascript:void(0);\" onclick=\"exportToExcel();\">点击下载</a>");
    <#else>
        $('#exportSpan').empty().append("&nbsp;");
    </#if>

    function getReport(timeInterval, isHide) {
        // 事件id
        var eventId = $('#eventSelect1').val();
        // 指标id
        var indexId = $('#eventIndexSelect1').val();
        // 事件属性集合
        var eventPropertyIds = [];
        // 图表类型
        var chartType = $('#chartTypeSelect').val();
        // 时间单位
        var timeUnit = $('#timeUnitSelect').val();
        // 条件集合
        var conditionList = [];
        // 条件关系 and or
        var conditionRelation = $('.js-and-or-all').text().replace(/\s+/g, "") == '或' ? 'or' : 'and';

        $('.eventPropertySelect').each(function () {
            if ($(this).val() != '') {
                eventPropertyIds.push($(this).val());
            }
        });

        $('.condition-body .condition-body').each(function () {
            var condition = [];
            $(this).find('.conditionPropertySelect').each(function () {
                if ($(this).val() != null && $(this).val() != '') {
                    con = new Object();
                    con.eventPropertyId = $(this).val();
                    con.ruleSymbol = $(this).parent().parent().next().find('.symbolSelect').val();
                    con.conditionValue = $(this).parent().parent().next().next().find('.conditionValue').val();
                    condition.push(con);
                }
            });
            conditionList.push(condition);
        });
        // 筛选条件
        $.ajax({
            url: '${request.contextPath}/bigdata/event/getReportData',
            type: 'POST',
            data: {
                eventId: eventId,
                eventPropertyIds: JSON.stringify(eventPropertyIds),
                chartType: chartType,
                timeUnit: timeUnit,
                timeInterval: timeInterval,
                conditionList: JSON.stringify(conditionList),
                conditionRelation: conditionRelation,
                indexId: indexId,
                isHide: isHide,
                pageSize: $('#pageSize').val()
            },
            dataType: 'html',
            success: function (response) {
                $('#reportDiv').append(response);
                $('.toggle-table').find('span').text('收起报表');
                $('.toggle-table').find('i').css('transform', 'rotateZ(0deg)')
                $('.table-wrap').removeClass('hide');
            }
        });
    }

    function selectInterval(e, interval) {
        $('.switchTime').removeClass('btn-blue').addClass('btn-default');
        $(e).removeClass('btn-default').addClass('btn-blue');
        $('.report-table').addClass('hide');
        $('.' + interval.replace('~','')).first().removeClass('hide');
    }
</script>