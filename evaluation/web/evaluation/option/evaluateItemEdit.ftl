<div class="layer layer-addItem" style="display:block;">
	<form id="itemForm">
	<div class="layer-content">
		<input type="hidden"  name="id" value="${item.id!}">
		<input type="hidden" name="evaluateType" value="${dto.evaluateType!}">
		<input type="hidden" name="showType" value="${dto.showType!}">
		<input type="hidden" name="itemType" id="itemType" value="${dto.itemType!}">
		<h4>排序号：</h4>
		<input type="text" class="form-control" style="width:50px;" value="${item.itemNo!}" id="itemNo" name="itemNo" vtype="int" min="1" max="99" nullable="false">
		<h4>名称：</h4>
		<input type="text" class="form-control" value="${item.itemName!}" maxlength="400" id="itemName" name="itemName" nullable="false">
		<#if dto.itemType?default("")!="12">
			<h4>新增选项：</h4>
			<div style="max-height:180px;overflow:auto;">
			<table class="table table-nobordered no-margin">
				<tbody id="selectOption">
					<#if optionList?exists && optionList?size gt 0>
						<#list optionList as option>
							<tr id="optiontr${option_index}">
								<input type="hidden" name="optionList[${option_index}].id" value="${option.id!}">
								<td class="text-center">选项</td>
								<#if dto.itemType?default("")=="11">
									<td width="80%"><input type="text" maxlength="100" name="optionList[${option_index}].optionName" id="optionName${option_index}" value="${option.optionName!}" nullable="false" class="form-control" placeholder="请输入选项名称"></td>
								<#else>
									<td width="48%"><input type="text" maxlength="100" name="optionList[${option_index}].optionName" id="optionName${option_index}" value="${option.optionName!}" nullable="false" class="form-control" placeholder="请输入选项名称"></td>
									<td width="6%" class="text-center color-grey">一</td>
									<td width="25%"><input type="text" name="optionList[${option_index}].score" id="score${option_index}" value="${option.score?string("0.#")!}" nullable="false" class="form-control"  regextip="只能一位小数点"  regex="/^(\d+\.\d{1,1}|\d+)$/" vtype="number" min="0" max="99" placeholder="请输入分值"></td>
								</#if>
								<td><a href="javascript:void(0);" onclick="deletetr(${option_index})" class="color-red">删除</a></td>
							</tr>
						</#list>
					<#else>
						<tr id="optiontr0">
							<td class="text-center">选项</td>
							<#if dto.itemType?default("")=="11">
								<td width="80%"><input type="text" maxlength="100" name="optionList[0].optionName" id="optionName0" class="form-control" nullable="false" placeholder="请输入选项名称"></td>
							<#else>
								<td width="48%"><input type="text" maxlength="100" name="optionList[0].optionName" id="optionName0" class="form-control" nullable="false" placeholder="请输入选项名称"></td>
								<td width="6%" class="text-center color-grey">一</td>
								<td width="25%"><input type="text" name="optionList[0].score" id="score0" class="form-control" regextip="只能一位小数点"  regex="/^(\d+\.\d{1,1}|\d+)$/" vtype="number" min="0" max="99"  nullable="false" placeholder="请输入分值"></td>
							</#if>
							<td><a href="javascript:void(0);" onclick="deletetr(0)" class="color-red">删除</a></td>
						</tr>
					</#if>
				</tbody>
			</table>
			</div>
			<input type="hidden" id="number" value="${number!}">
			<a class="js-addOption" href="javascript:void(0);">新增选项</a>
		</#if>
	</div>
	</form>
</div>
<div class="layer-footer">
	<a href="javascript:" class="btn btn-lightblue" id="result-commit" onclick="saveItem();return false;">确定</a>
	<a href="javascript:" class="btn btn-lightblue" id="result-close">取消</a>
</div>
<script>
	$(function(){
		$("#result-close").on("click", function(){
		    layer.closeAll();
		 });
		// 添加选项
		$('.js-addOption').on('click', function(e){
			var itemType=$("#itemType").val();
			var number=$("#number").val();
			if(number==undefined || number==""){
				layerTipMsg(false,"出错","新增选项出错");
				return ;
			}
			$("#number").val(Number(number)+1);
			e.preventDefault();
			var item ='';
			if(itemType=="11"){
				item = '<tr id=optiontr'+number+'>\
							<td class="text-center">选项</td>\
							<td width="80%"><input type="text" maxlength="100" name="optionList['+number+'].optionName" id="optionName'+number+'" nullable="false" class="form-control" placeholder="请输入选项名称"></td>\
							<td><a href="javascript:void(0);" onclick="deletetr('+number+')" class="color-red">删除</a></td>\
						</tr>';
			}else{
				item = '<tr id=optiontr'+number+'>\
							<td class="text-center">选项</td>\
							<td width="48%"><input type="text" maxlength="100" name="optionList['+number+'].optionName" id="optionName'+number+'" nullable="false" class="form-control" placeholder="请输入选项名称"></td>\
							<td width="6%" class="text-center color-grey">一</td>\
							<td width="25%"><input type="text" name="optionList['+number+'].score" id="score'+number+'" nullable="false" class="form-control" regextip="只能一位小数点"  regex="/^[0-9]+([.]{1}[0-9]{1})?$/" vtype="number" min="0" max="99" placeholder="请输入分值"></td>\
							<td><a href="javascript:void(0);" onclick="deletetr('+number+')" class="color-red">删除</a></td>\
						</tr>';
			}
			$("#selectOption").append(item);
		})
	});
	
	function deletetr(number){
		if($('#selectOption tr').length==1){
			layerTipMsg(false,"删除失败","不能全部删除选项");
			return;
		}
		$("#optiontr"+number).remove();
	}
	
	var isSubmit=false;
	function saveItem(){
		if(isSubmit){
			return;
		}
		isSubmit = true;
		var check = checkValue('#itemForm');
		if(!check){
		 	isSubmit=false;
		 	return;
		}
		var ii = layer.load();
		var options = {
			url : "${request.contextPath}/evaluate/option/saveItem",
			dataType : 'json',
			success : function(data){
	 			var jsonO = data;
		 		if(!jsonO.success){
		 			layerTipMsg(jsonO.success,"保存失败",jsonO.msg);
		 			isSubmit = false;
		 		}else{
		 			layer.closeAll();
					layerTipMsg(jsonO.success,"保存成功",jsonO.msg);
				  	doSearch();
    			}
    			layer.close(ii);
			},
			clearForm : false,
			resetForm : false,
			type : 'post',
			error:function(XMLHttpRequest, textStatus, errorThrown){}//请求出错 
		};
		$("#itemForm").ajaxSubmit(options);
	}
	
</script>