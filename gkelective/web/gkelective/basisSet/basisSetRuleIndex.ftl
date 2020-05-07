<div class="box print">
	<form id="submitForm">
	<table class="table table-striped no-margin">
		<thead>
			<tr>
				<th width="20%">序号</th>
				<th width="20%">优先项</th>
				<th width="20%">调整</th>
				<th></th>
			</tr>
		</thead>
		<tbody>
			<#if allocationList?? && (allocationList?size>0)>
      			<#list allocationList as item>
      				<tr>
						<td>${item.sort?default(0)}</td>
						<td>${item.name!}</td>
						<td>
							<input type="hidden" name="allocationList[${item_index}].id" value="${item.id!}">
							<input type="hidden" name="allocationList[${item_index}].isUsing" value="${item.isUsing!}">
							<input type="hidden" name="allocationList[${item_index}].type" value="${item.type!}">
							<input type="hidden" class="sortClass" name="allocationList[${item_index}].sort" value="${item.sort?default(0)}">
							<a href="javascript:;" class="table-btn <#if item_index==0>disabled</#if> js-moveUp">
								<i class="fa fa-arrow-up"></i>
							</a>
							<a href="javascript:;" class="table-btn <#if item_index==(allocationList?size-1)>disabled</#if> js-moveDown">
								<i class="fa fa-arrow-down"></i>
							</a>
						</td>
						<td></td>
					</tr>
      			</#list>
      		<#else>
      		<tr >
	          	<td colspan="88" align="center">
	          		暂无数据
	          	</td>
	          <tr>
      		</#if>
		</tbody>
	</table>
	</form>
	<p class="tip tip-grey">说明：系统开班时，将按照设置的优先顺序进行开班。当顺序1无法完成开班时，将自动执行顺序，依此类推；温馨提示：开班开课流程正在进行中不能修改，需流程走完之后才能再次设置</p>
	<div class="text-right">
		<#if !(gkArrange?exists && gkArrange.isLock==1)><button class="btn btn-blue" onclick="doSaveScore()">保存</button></#if>
	</div>
</div>
<script>
	function resort(that){
		that.closest('tbody').find('>tr').each(function(index){
			$(this).find('>td:first').text(index+1);
			$(this).find('.sortClass').val(index+1);
		});
		switchEnable('.js-moveUp','.js-moveDown')
	}
	
	function moveUp(){
		$(this).closest('tr').insertBefore($(this).closest('tr').prev());
		resort($(this));
	}
	
	function moveDown(){
		$(this).closest('tr').insertAfter($(this).closest('tr').next());
		resort($(this));
	}
	
	function switchEnable(el1,el2){
		$(el1).removeClass('disabled');
		$(el2).removeClass('disabled');

		$(el1).first().addClass('disabled');
		$(el2).last().addClass('disabled');
	}
	
	$('.js-moveUp').on('click',moveUp);
	$('.js-moveDown').on('click',moveDown);
	
	var isSubmit=false;
	function doSaveScore(){
		if(isSubmit){
			return;
		}
		isSubmit = true;
		var ii = layer.load();
		var options = {
			url : "${request.contextPath}/gkelective/${arrangeId}/basisSet/rule/save",
			dataType : 'json',
			success : function(data){
	 			var jsonO = data;
		 		if(!jsonO.success){
		 			layerTipMsg(jsonO.success,"保存失败",jsonO.msg);
		 			isSubmit = false;
		 		}else{
		 			layer.closeAll();
					layerTipMsg(jsonO.success,"保存成功",jsonO.msg);
				  	itemShowList(2);
    			}
    			layer.close(ii);
			},
			clearForm : false,
			resetForm : false,
			type : 'post',
			error:function(XMLHttpRequest, textStatus, errorThrown){}//请求出错 
		};
		$("#submitForm").ajaxSubmit(options);
	}
	
</script>