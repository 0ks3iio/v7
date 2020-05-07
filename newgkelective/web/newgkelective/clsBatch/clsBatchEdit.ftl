<form name="clsForm" id="clsForm">
<div class="form-horizontal" role="form" style="height:364px">
<div class="form-group">
	<label class="col-sm-2 control-label no-padding-right">选课科目</label>
	<div class="col-sm-10">
		<div class="publish-course courselist-div">
			<#if courseList?exists && courseList?size gt 0>
			<#list courseList as item>
				<span id="span${item.id}" class="subject <#if cids?default('')?index_of(item.id) != -1> active<#else> disabled</#if>">${item.subjectName!}(${item.fullMark?default(0)})</span>
			</#list>
			</#if>
		</div>
	</div>
</div>
<#list batchs as ba>
<div class="form-group">
	<label class="col-sm-2 control-label no-padding-right">时间点${ba[0]}</label>
	<input type="hidden" name="batch" value="${ba[0]}">
	<input type="hidden" name="id" value="${ba[2]}">
	<input type="hidden" name="className" id="clsName${ba[0]}" value="${ba[3]}">
	<div class="col-sm-10">
		<div class="publish-course">
			<select nullable="false" class="form-control" batch="${ba[0]}" name="subjectIds" style="width:150px;" id="batch${ba[0]}" onchange="changeSub('${ba[0]}');">
			<#if courseList?exists && courseList?size gt 0>
			<#list courseList as item>
				<option value="${item.id!}" id="opt${item.id}${ba[0]}" <#if ba[1]==item.id || (ba[1]=='' && ba_index==item_index)>selected</#if>>${item.subjectName!}</option>
			</#list>
			</#if>
			</select>
		</div>
	</div>
</div>
</#list>
<div class="form-group">
<label class="col-sm-12 control-label">注意：修改科目时间点之后，请重新维护保存走班科目时间点数据。</label>
</div>
</div>
</form>
<div class="layui-layer-btn">
	<a class="layui-layer-btn0" onclick="saveClsBatch();">确定</a>
	<a class="layui-layer-btn1" onclick="cacelSave();">取消</a>
</div>
<script>
function clearAct(){
	$('.courselist-div span').removeClass('active').addClass('disabled');
	var subList = document.getElementsByName('subjectIds');
	for(var i=0;i<subList.length;i++){
		var sb = subList[i];
		var sbid = sb.id;
		var ba = $('#'+sbid).attr('batch');
		$('#clsName'+ba).val($('#opt'+sb.value+ba).text());
		$('#span'+sb.value).addClass('active').removeClass('disabled');;
	}
}

$(function(){
	<#if cids?default('') == ''>
		clearAct();
	</#if>
})




var isSubmit=false;
function saveClsBatch(){
	if(isSubmit){
		return;
	}
	isSubmit=true;
	
	if(!checkValue('#clsForm')){
		isSubmit=false;
	 	return;
	}
	var subs = document.getElementsByName('subjectIds');
	var sids='';
	for(var i=0;i<subs.length;i++){
		var sb = subs[i];
		var sbid = sb.id;
		if(sids.indexOf(sb.value)!=-1){
			layer.tips('各个时间点的科目不可以重复',$("#"+sbid), {
					tipsMore: true,
					tips: 3
				});	
			isSubmit=false;	
			return;	
		}
		var ba = $('#'+sbid).attr('batch');
		$('#clsName'+ba).val($('#opt'+sb.value+ba).text());
		sids=sids+","+sb.value;
	}
	var options = {
		url : "${request.contextPath}/newgkelective/clsBatch/${divideId!}/clsSave?divideClsId=${divideClsId!}",
		dataType : 'json',
		success : function(data){
 			var jsonO = data;
	 		if(!jsonO.success){
	 			layerTipMsg(jsonO.success,"保存失败",jsonO.msg);
	 			isSubmit=false;
	 			return;
	 		}else{
	 			layer.msg("保存成功", {
						offset: 't',
						time: 2000
					});
				layer.closeAll();
				clsBatch();	
			}
		},
		clearForm : false,
		resetForm : false,
		type : 'post',
		error:function(XMLHttpRequest, textStatus, errorThrown){}//请求出错 
	};
	$("#clsForm").ajaxSubmit(options);
}

function changeSub(ba){
	var obid='batch'+ba;
	var sval = $('#'+obid).val();
	var subs = document.getElementsByName('subjectIds');
	for(var i=0;i<subs.length;i++){
		var sb = subs[i];
		var sbid = sb.id;
		if(sbid != obid && sb.value==sval){
			layer.tips('各个时间点的科目不可以重复',$("#"+obid), {
					tipsMore: true,
					tips: 3
				});	
			return;	
		}
	}
	clearAct();
	$('#clsName'+ba).val($('#opt'+sval+ba).text());
}

function cacelSave(){
	layer.closeAll();
}
</script>