<script src="${request.contextPath}/static/components/bootstrap-datepicker/dist/js/bootstrap-datepicker.min.js"></script>
<form id="optionForm">
<div class="layer-add">
	<div class="layer-content">
		<div class="form-horizontal">
			<input type="hidden" id="id" name="id" value="${dyClassOtherCheck.id!}">
			<input type="hidden" id="acadyear" name="acadyear" value="${acadyear!}">
			<input type="hidden" id="semester" name="semester" value="${semester!}">
			<div class="form-group">
				<label class="col-sm-3 control-label no-padding-right">选择班级：</label>
				<div class="col-sm-8">
					<select name="classId" id="classId" class="form-control" style="width:120px" onchange="changeList();">
						<#if clazzList?exists && clazzList?size gt 0>
							<#list clazzList as item> 
								<option value="${item.id!}" <#if (item.id!) == (dyClassOtherCheck.classId!) >selected</#if>>${item.classNameDynamic!}</option>
							</#list>
						</#if>
					</select>
				</div>
			</div>
			<div class="form-group">
				<label class="col-sm-3 control-label no-padding-right">考核分：</label>
				<div class="col-sm-8">
					<input type="text" name="score" id="score" style="width:290px;" nullable="false" vtype="number" min="0" max="99" maxLength="4" decimalLength="1" class="form-control" <#if (dyClassOtherCheck.score!) != 0> value="${dyClassOtherCheck.score?string("0.#")}"</#if>>
				</div>
			</div>
			<div class="form-group">
				<label class="col-sm-3 control-label no-padding-right">考核内容：</label>
				<div class="col-sm-8">
					<textarea  id="remark" name="remark" class="form-control" maxLength="500" nullable="false">${dyClassOtherCheck.remark!}</textarea>
				</div>
			</div>
			<div class="form-group" id="dateDiv">
				<label class="col-sm-3 control-label no-padding-right">考核时间：</label>
				<div class="col-sm-8">
					<div class="input-group">
						<input class="form-control date-picker startTime-date date-picker-time" nullable="false" vtype="data" type="text"  name="checkTime" id="checkTime" value="${(dyClassOtherCheck.checkTime?string('yyyy-MM-dd'))!}">	
						<span class="input-group-addon">
							<i class="fa fa-calendar"></i>
						</span>
					</div>
				</div>
			</div>
		</div>
	</div>
</div>
</form>
<div class="layer-footer">
    <a href="javascript:" class="btn btn-lightblue" id="result-commit" onclick="doSaveOption()">确定</a>
    <a href="javascript:" class="btn btn-grey" id="result-close">取消</a>
</div>
<script>
	$(function(){
		//初始化日期控件
		var viewContent={
			'format' : 'yyyy-mm-dd',
			'minView' : '2',
		};
		initCalendarData("#dateDiv",".date-picker",viewContent);
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
			url : "${request.contextPath}/stuwork/otherStart/editSave",
			dataType : 'json',
			success : function(data){
	 			var jsonO = data;
		 		if(!jsonO.success){
		 			layerTipMsg(jsonO.success,"保存失败",jsonO.msg);
		 			isSubmit = false;
		 		}else{
		 			layer.closeAll();
					layerTipMsg(jsonO.success,"保存成功",jsonO.msg);
					changeList();
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
	
	// 取消按钮操作功能
$("#result-close").on("click", function(){
    doLayerOk("#result-commit", {
    redirect:function(){},
    window:function(){layer.closeAll()}
    });     
 });
</script>