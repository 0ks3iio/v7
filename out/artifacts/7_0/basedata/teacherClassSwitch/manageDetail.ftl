<#if resultList?exists && resultList?size gt 0>
<table class="table table-striped table-bordered table-hover no-margin">
    <thead>
    <tr>
    	<#if isExport?default("0")=="1">
    	<th width="5%">
			<label>
				<input type="checkbox" class="wp" onclick="swapCheck()">
				<span class="lbl"> 全选</span>
			</label>
		</th>
		</#if>
        <th width="7%" class="text-center">申请人</th>
        <th width="10%" class="text-center">班级</th>
        <th width="23%" class="text-center">需调课程</th>
        <th width="23%" class="text-center">被调课程</th>
        <th class="text-center">备注</th>
        <th width="8%" class="text-center">状态</th>
        <th width="13%" class="text-center">操作</th>
    </tr>
    </thead>
    <tbody>
    <#list resultList as item>
    <tr class="text-center" adjustedId="${item.id!}">
   		<#if isExport?default("0")=="1">
    	<td>
			<label>
				<input type="checkbox" class="wp other" value="${item.id!}">
				<span class="lbl"> </span>
			</label>
		</td>
		</#if>
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
        	<#if isExport?default("0")=="1">
        	<a href="javascript:;" class="table-btn color-green" onclick="exportOne('${item.id!}')">导出调课单</a>
        	<#else>
            <#if item.state == "1">
                <#if item.canDelete!>
                    <a href="javascript:;" class="table-btn color-green" onclick="switchState(this)">撤销</a>
                <#else>
                    <a href="javascript:;" class="table-btn color-green disabled">撤销</a>
                </#if>
            <#elseif item.state == "2">
                <a href="javascript:;" class="table-btn color-green" onclick="switchState(this)">撤销</a>
            <#else>
                <a href="javascript:;" class="table-btn color-green" onclick="switchAgree(this, 1)">通过</a>
                <a href="javascript:;" class="table-btn color-green" onclick="switchAgree(this, 2)">不通过</a>
            </#if>
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
    var switchObj;
    var cancelObj;
    var stateTmp;
    var isCanceling = false;

    function switchAgree(obj, state) {
        if (isCanceling) {
            return;
        }
        isCanceling = true;
        switchObj = obj;
        stateTmp = state;
        var adjustedId = $(obj).parent().parent().attr("adjustedId");
        $.ajax({
            url:"${request.contextPath}/basedata/classswitch/manage/agree",
            data:{"adjustedId":adjustedId,
                "state":state},
            success:function (result) {
                var jsonResult = JSON.parse(result);
                if (jsonResult.success) {
                    $("#switchDetail").load("${request.contextPath}/basedata/classswitch/manage/table?schoolId=${schoolId!}&acadyear=" + $("#acadyear option:selected").val() + "&semester=" + $("#semester option:selected").val() + "&week=" + $("#week option:selected").val());
                    layer.msg(jsonResult.msg, {offset: 't',time: 3000});
                    isCanceling = false;
                } else {
                    layer.msg(jsonResult.msg, {offset: 't',time: 3000});
                    isCanceling = false;
                }
            },
            error:function(XMLHttpRequest, textStatus, errorThrown){
                isCanceling = false;
            }
        });
    }

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
        }, function () {
            isCanceling = false;
        });
    }
    
    var isCheckAll = false;  
	function swapCheck() {
		if (isCheckAll) {  
	        $("input[type='checkbox']").each(function() {  
	            this.checked = false;  
	        });  
	        isCheckAll = false;  
	    } else {  
	        $("input[type='checkbox']").each(function() {  
	            this.checked = true;  
	        });  
	        isCheckAll = true;  
	    }  
	}
    
    function exportOne(adjustedId){
    	layer.prompt({
		   formType: 2,
		    title: '调课原因(非必填)',
		    btnAlign: 'c',
		    maxlength: 200,
		    yes: function(index, layero){
		        // 获取文本框输入的值
		        var reason = layero.find(".layui-layer-input").val();
		        if(reason.length>200){
		        	layer.tips('最多输入200个字数', '.layui-layer-input',{tips: 1});
		        }else{
			        var acadyear = $("#acadyear").val();
			        var semester = $("#semester").val();
		    		var url = '${request.contextPath}/basedata/classswitch/export?acadyear='+acadyear+'&semester='+semester+'&adjustedIds='+adjustedId+'&reason='+reason;
					document.location.href=url;
		        	layer.close(index);
				}
		    }
		});
    }
    
    function exportMore(){
    	var items = $("input.other:checked");
		if(items.length == 0) {
			layer.alert('请勾选至少一条记录',{icon:7});
		}else {
			var adjustedIds = [];
			items.each(function(){
				adjustedIds.push($(this).val());
			})
			exportOne(adjustedIds.join(","));
		}
    }
</script>