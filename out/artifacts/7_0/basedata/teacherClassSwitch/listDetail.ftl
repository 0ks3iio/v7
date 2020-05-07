<#if resultList?exists && resultList?size gt 0>
<table class="table table-striped table-bordered table-hover no-margin">
    <thead>
    <tr>
        <th width="7%" class="text-center">申请人</th>
        <th width="10%" class="text-center">班级</th>
        <th width="23%" class="text-center">需调课程</th>
        <th width="23%" class="text-center">被调课程</th>
        <th width="16%" class="text-center">备注</th>
        <th width="8%" class="text-center">状态</th>
        <th width="13%" class="text-center">操作</th>
    </tr>
    </thead>
    <tbody>
    <#list resultList as item>
    <tr class="text-center" adjustedId="${item.id!}">
        <td>${item.operatorName!}</td>
        <td>${item.className!}</td>
        <td adjustingName="${item.adjustingId!}">
            ${item.adjustingName!}
        </td>
        <td beenAdjustedId="${item.beenAdjustedId!}">
            ${item.beenAdjustedName!}
        </td>
        <td style="overflow: hidden;white-space: nowrap;text-overflow: ellipsis;" title="${item.remark?default("无")}">
            ${item.remark?default("无")}
        </td>
        <td>
            <#if item.state == "0">
                <i class="fa fa-circle font-12 color-blue"></i> 待审核
            <#elseif item.state == "3">
                <i class="fa fa-circle font-12 color-blue"></i> 代课同意
            <#elseif item.state == "1">
                <i class="fa fa-circle font-12 color-green"></i> 通过
            <#elseif item.state == "2">
                <i class="fa fa-circle font-12 color-red"></i> 未通过
            </#if>
        </td>
        <td>
            <#if item.canDelete!>
                <a href="javascript:;" class="table-btn color-green" onclick="switchState(this)">撤销</a>
            <#else>
                <a href="javascript:;" class="table-btn color-green disabled">撤销</a>
            </#if>
        </td>
    </tr>
    </#list>
    </tbody>
</table>
<#else>
<div class="no-data-container">
<div class="no-data">
	<span class="no-data-img">
		<img src="${request.contextPath}/static/images/public/nodata6.png" alt="">
	</span>
	<div class="no-data-body">
		<p class="no-data-txt">暂无相关数据</p>
	</div>
</div>
</div>
</#if>

<script>
    var cancelObj;
    var isCanceling = false;
    function switchState(obj) {
        if (isCanceling) {
            return;
        }
        isCanceling = true;
        cancelObj = obj;
        var adjustedId = $(obj).parent().parent().attr("adjustedId");
        layer.confirm('确定撤销吗？', function(index) {
            $.ajax({
                url: "${request.contextPath}/basedata/classswitch/list/cancel",
                data: {"adjustedId": adjustedId},
                success: function (result) {
                    var jsonResult = JSON.parse(result);
                    if (jsonResult.success) {
                        $(cancelObj).parents("tr").remove();
                        layer.msg("已撤销", {offset: 't', time: 2000});
                        isCanceling = false;
                    } else {
                        layer.msg(jsonResult.msg, {offset: 't', time: 2000});
                        isCanceling = false;
                    }
                },
                error: function (XMLHttpRequest, textStatus, errorThrown) {
                    isCanceling = false;
                }
            });
        },function () {
            isCanceling = false;
        });
    }
</script>