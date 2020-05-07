<form id="optionForm">
<div class="layer layer-add">
	<div class="layer-content">
		<div class="form-horizontal">
		   <div class="form-group">
				<label class="col-sm-3 control-label no-padding-right"><font style="color:red;">*</font>排序号</label>
				<div class="col-sm-8">
					<input type="text" name="orderId" id="orderId" vtype="int" style="width:290px;" nullable="false" class="form-control" max="99" min="1" id="optionName"  value="${boption.orderId!}">
				</div>
			</div>
			<div class="form-group">
				<label class="col-sm-3 control-label no-padding-right"><font style="color:red;">*</font>选项类型</label>
				<div class="col-sm-8">
						<select name="businessType" id="businessType" style="width:290px;" <#if boption.id?default("")!="">disabled="disabled"</#if> nullable="false" class="form-control" onChange="changeOderId();">
							<option value="1010" <#if "1010"=='${businessType!}'>selected="selected"</#if>>学生违纪</option>
	                        <option value="1020" <#if "1020"=='${businessType!}'>selected="selected"</#if>>学生评语</option>
	                        <option value="1030" <#if "1030"=='${businessType!}'>selected="selected"</#if>>军训等第</option>
	                        <option value="1040" <#if "1040"=='${businessType!}'>selected="selected"</#if>>值周表现等第</option>
	                        <option value="1050" <#if "1050"=='${businessType!}'>selected="selected"</#if>>学农等第</option>
		                </select>
		                <#if boption.id?default("")!="">
		                   <input type="hidden" name="businessType" value="${boption.businessType!}">
		                </#if>
				</div>
			</div>
			<div class="form-group">
				<label class="col-sm-3 control-label no-padding-right"><font style="color:red;">*</font>选项名称</label>
				<div class="col-sm-8">
					<input type="text" name="optionName" style="width:290px;" maxLength="100" nullable="false" class="form-control" id="optionName"  value="${boption.optionName!}">
					<input type="hidden" name="id" id="boptionId" value="${boption.id!}">
				</div>
			</div>
			<div class="form-group">
				<label class="col-sm-3 control-label no-padding-right"><font style="color:red;">*</font>是否需要折分</label>
				<div class="col-sm-8">
					<select name="hasScore" style="width:290px;" id="hasScore" nullable="false" class="form-control"  onChange="changeScore();" <#if '1010' != '${businessType!}'>disabled="disabled"</#if>>
						<option value="1" <#if boption.id?default("")!="" && "1"=='${boption.hasScore!}'>selected="selected"</#if>>是</option>
						<option value="0" <#if boption.id?default("")!="" && "0"=='${boption.hasScore!}'>selected="selected"</#if>>否</option>
		            </select>
				</div>
			</div>
			<div class="form-group" id="isCustomDiv">
				<label class="col-sm-3 control-label no-padding-right"><font style="color:red;">*</font>是否自定义折分</label>
				<div class="col-sm-8">
					<select name="isCustom" style="width:290px;" id="isCustom" nullable="false" class="form-control" onChange="changeCustom();" <#if '1010' != '${businessType!}'>disabled="disabled"</#if>>
					    <option value="0" <#if boption.id?default("")!="" && "0"=='${boption.isCustom!}'>selected="selected"</#if>>否</option>
						<option value="1" <#if boption.id?default("")!="" && "1"=='${boption.isCustom!}'>selected="selected"</#if>>是</option>
		            </select>
				</div>
			</div>
			<div class="form-group" id="scoreDiv">
				<label class="col-sm-3 control-label no-padding-right"><font style="color:red;">*</font>折分</label>
				<div class="col-sm-8">
					<input type="text" name="score" style="width:290px;" nullable="false" vtype="number" decimalLength="1" min="0" max="999999" class="form-control" maxlength="50" id="score"  value="${(boption.score)?string('#.#')!}">
				</div>
			</div>
		</div>
	</div>
</div>
</form>
<div class="layer-footer">
    <a href="javascript:" class="btn btn-lightblue" id="result-commit" onclick="doSaveOption()">确定</a>
    <a href="javascript:" class="btn btn-lightblue" id="result-close">取消</a>
</div>
<script>
	$(function(){
		$(".layer-add").show();
		$("#result-close").on("click", function(){
		    layer.closeAll();
		});
	   var hasScore = $('#hasScore').val();
	   if("0" == hasScore){
	       $('#isCustomDiv').hide();
	       $('#scoreDiv').hide();
	   }
	   var isCustom = $('#isCustom').val();
	   if("1" == isCustom){
	       $('#scoreDiv').hide();
	   }
	});
	var isSubmit=false;
	function doSaveOption(){
		if(isSubmit){
			return;
		}
		isSubmit = true;
		var check = checkValue('#optionForm');
		if(!check){
		 	isSubmit=false;
		 	return;
		}
		var ii = layer.load();
		var options = {
			url : "${request.contextPath}/stuwork/evaluation/score/saveOption",
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
		$("#optionForm").ajaxSubmit(options);
	}
	
	function changeOderId(){
	   var businessType = $('#businessType').val();
	   $.ajax({
		url:"${request.contextPath}/stuwork/evaluation/score/changeOrderId",
		data:{businessType:businessType},
		dataType: "json",
		success: function(data){
		    $("#orderId").val(data.msg); 
		}
	});
	}
	
	function changeScore(){
	   var hasScore = $('#hasScore').val();
	   if("0" == hasScore){
	       $('#isCustomDiv').hide();
	       $('#scoreDiv').hide();
	   }else{
	       $('#isCustomDiv').show();
	       $('#scoreDiv').show();
	   }
	}
	
	function changeCustom(){
	   var isCustom = $('#isCustom').val();
	   if("1" == isCustom){
	       $('#scoreDiv').hide();
	   }else{
	       $('#scoreDiv').show();
	   }
	}
</script>