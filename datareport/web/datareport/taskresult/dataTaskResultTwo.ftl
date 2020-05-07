<form id="dataResultForm">
<h3>${titleName!}</h3>
<p class="color-red text-right">*为必填项</p>
<div class="table-container no-margin" style="overflow-x:auto;">
	<div class="table-container-body">
		<input type="hidden" id="taskId" name="taskId">
		<input type="hidden" id="reportId" name="reportId">
		<input type="hidden" id="tableType" name="tableType" value="2">
		<table class="table table-bordered table-striped table-hover">
			<tbody id="taskResultsTbody">
				<#if columns?exists&&columns?size gt 0>
					<#list columns as rank>
						<tr>
							<th width="180px" style="white-space: nowrap;min-width: 180px;"><#if rank.isNotnull == 1><span class="color-red">*</span>&nbsp;</#if>${rank.columnName!}</th>
							<td id="sumTd${rank_index}" style="white-space: nowrap;"></td>
						</tr>
					</#list>
				</#if>
				<tr id="rankDelTr"><th width="180px" style="white-space: nowrap;min-width: 180px;">操作</th><th id="sumTh" style="white-space: nowrap;">统计</th></tr>
			</tbody>
		</table>
		<br/>
	</div>
</div>
</form>
</br>
<#if remark?exists>
	<div>
		<textarea readonly type="text/plain" style="width:100%;height:50px;">${remark!}</textarea>
	</div>
</#if>
<script>
	var resultIndex = ${resultsList?size};

	$(function(){
		<#if resultsList?exists && resultsList?size gt 0>
		haveData = true;
			<#list resultsList as results>
				<#list results as result>
					$("#sumTd${result_index}").before('<td class="rankTd${results_index}"><input style="width:150px" class="form-control <#if columns[result_index].isNotnull == 1>isNotnull</#if> <#if columns[result_index].columnType == 2>isNumber</#if> <#if columns[result_index].columnType == 3>No</#if>" type="text" id="dataReportResults[${results_index}].dataArray[${result_index}]" name="dataReportResults[${results_index}].dataArray[${result_index}]" autocomplete="off" value="<#if result!="null">${result!}</#if>" <#if columns[result_index].columnType == 2>maxlength="13" onkeyup="limitWords(this)"</#if>></td>');
				</#list>
				$("#sumTh").before('<td class="rankTd${results_index}"><span style="cursor:pointer" class="color-blue" href="#" onClick="deleteOneResult(\''+ "rankTd${results_index}" +'\')">删除</span><input class="rowIndex" type="hidden" id="dataReportResults[${results_index}].rowIndex" name="dataReportResults[${results_index}].rowIndex"></td>');
			</#list>
		</#if>
		<#if sumResult?exists && sumResult?size gt 0>
			<#list sumResult as sum>
				<#if sum!="null">
					$("#sumTd${sum_index}").append('<#if columns[sum_index].methodType==1>取平均：</#if><#if columns[sum_index].methodType==2>求和：</#if>${sum!}');
				</#if>
			</#list>
		</#if>
	})
	
	<#-- 新增列 -->
	function addNewResult() {
		<#if columns?exists&&columns?size gt 0>
			<#list columns as rank>
				$("#sumTd${rank_index}").before('<td class="rankTd'+ resultIndex +'"><input style="width:150px" class="form-control <#if rank.isNotnull == 1>isNotnull</#if> <#if rank.columnType == 2>isNumber</#if> <#if rank.columnType == 3>No</#if>" type="text" id="dataReportResults['+ resultIndex +'].dataArray[${rank_index}]" name="dataReportResults['+ resultIndex +'].dataArray[${rank_index}]" autocomplete="off" <#if rank.columnType == 2>maxlength="13" onkeyup="limitWords(this)"</#if>></td>');
			</#list>
		</#if>
		$("#sumTh").before('<td class="rankTd'+ resultIndex +'"><span style="cursor:pointer" class="color-blue" href="#" onClick="deleteOneResult(\''+ ("rankTd"+ resultIndex) +'\')">删除</span><input class="rowIndex" type="hidden" id="dataReportResults['+ resultIndex +'].rowIndex" name="dataReportResults['+ resultIndex +'].rowIndex"></td>');
		resultIndex++;
	}
	
	<#-- 删除列 -->
	function deleteOneResult(index) {
		$("."+index).remove();
	}
	
	<#-- 保存数据 -->
	var subResults = false;
	function saveTaskResults(taskId,reportId) {
		if (subResults) {
			return;
		}
		var haveError = false;
		if ($('#rankDelTr td').length==0) {
			layerMsg("数据不能为空！")
			return;
		}
		$(".form-control").each(function(){
			$(this).parent().removeClass("has-error");
			$(this).parent().removeAttr('title');
			if (($(this).val().length==0 || $(this).val()==null || $(this).val()=="") && $(this).hasClass('isNotnull')) {
				$(this).parent().addClass("has-error");
				$(this).parent().attr('title','内容不能为空！');
				haveError = true;
			}
			if ($(this).val().length>0 && $(this).hasClass('isNumber') && !isNumber($(this).val())) {
				$(this).parent().addClass("has-error");
				$(this).parent().attr('title','请填写数字！');
				haveError = true;
			}
		});
		if (haveError) {
			layerMsg("数据有误请修改！")
			return;
		}
		var rowIndex = 1;
		$(".rowIndex").each(function(){
			$(this).val(rowIndex);
			rowIndex++;
		});
		rowIndex = 1;
		$("#taskResultsTbody tr").each(function(){
			$(this).find('.No').each(function(){
				$(this).val(rowIndex);
				rowIndex++;
			});
			rowIndex = 1;
		});
		$("#taskId").val(taskId);
		$("#reportId").val(reportId);
		var rellayer = layer.confirm('确定要保存吗？', function(index){
			layer.close(rellayer);
			subResults = true;
			layerTime();
			var options = {
				url : "${request.contextPath}/datareport/taskresult/savetaskresults",
				dataType : 'json',
				success : function(data){
 					subResults = false;
 					layerClose();
 					if(!data.success){
 						layerTipMsg(data.success,"保存失败",data.msg);
 					}else{
 						layerMsg("保存成功！");
 						showDataResult(taskId,reportId);
					}
				},
				clearForm : false,
				resetForm : false,
				type : 'post',
				error:function(XMLHttpRequest, textStatus, errorThrown){}//请求出错 
			};
			$("#dataResultForm").ajaxSubmit(options);
		});
	}
	
	<#-- 判断是否是数字  -->
	function isNumber(val){
	    var regPos = /^\d+(\.\d+)?$/; //非负浮点数
	    var regNeg = /^(-(([0-9]+\.[0-9]*[1-9][0-9]*)|([0-9]*[1-9][0-9]*\.[0-9]+)|([0-9]*[1-9][0-9]*)))$/; //负浮点数
	    if(regPos.test(val) || regNeg.test(val)){
	        return true;
	    }else{
	        return false;
	    }
	}
</script>