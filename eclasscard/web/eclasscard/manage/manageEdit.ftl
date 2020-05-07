<#import "/fw/macro/popupMacro.ftl" as popup />
<form id="submitForm">
<input type="hidden" name="id" value="${eccInfo.id!}">
<div class="layer-content">
	<div class="form-horizontal">
		<div class="form-group">
			<label class="col-sm-3 control-label no-padding-right">用途：</label>
			<div class="col-sm-8">
				<select name="type" id="bpyt-val" class="form-control" onchange="changeType()">
					<option value="">---请选择---</option>
					<#if usedForDtos?exists&&usedForDtos?size gt 0>
	                  	<#list usedForDtos as item>
						<option value="${item.thisId!}" <#if eccInfo.type?exists && item.thisId == eccInfo.type>selected</#if>>${item.content!}</option>
	              	    </#list>
                	</#if>
				</select>
			</div>
		</div>
		
		<div class="form-group" id="classChoiseDiv">
			<@popup.selectOneClass clickId="className" id="classId-val" name="className" handler="changeClass()">
				<label class="col-sm-3 control-label no-padding-right">班级：</label>
				<div class="col-sm-8">
					<input type="hidden" id="classId-val" name="classId" value="${eccInfo.classId!}">
					<input type="text" id="className" class="form-control" value="${eccInfo.className!}" data-value="" />
				</div>
			</@popup.selectOneClass>
		</div>
		<div class="form-group" id="plcaeChoiseDiv">
			<div id="classPlaceShow">
				<label class="col-sm-3 control-label no-padding-right">场地：</label>
				<div class="col-sm-8">
					<label  style="font-weight: normal;"><span class="lbl" id="showPlaNameSpan">${eccInfo.placeName!}</span></label>
				</div>
			</div>
			<div id="classPlaceSelect">
			<@popup.selectOnePlaceByType clickId="placeName" id="placeId-val" name="placeName" handler="">
				<label class="col-sm-3 control-label no-padding-right">场地：</label>
				<div class="col-sm-8">
					<input type="hidden" id="placeId-val" name="placeId" value="${eccInfo.placeId!}">
					<input type="text" id="placeName" class="form-control" value="${eccInfo.placeName!}" data-value="" />
				</div>
			</@popup.selectOnePlaceByType>
			</div>
		</div>
		<div class="form-group" id="dormPlcaeChoiseDiv">
			<label class="col-sm-3 control-label no-padding-right">场地：</label>
			<div class="col-sm-8">
				<select id="dormPlaceId-val" class="form-control" onchange="setDormPlaceId()">
					<option value="">---请选择---</option>
					<#if dbArray?exists&&dbArray?size gt 0>
	                  	<#list dbArray as item>
						<option <#if eccInfo.placeId?exists && eccInfo.placeId==item.buildingId>selected</#if>   value="${item.buildingId!}">${item.buildingName!}</option>
	              	    </#list>
                    </#if>
				</select>
			</div>
		</div>
		<div class="form-group">
			<label class="col-sm-3 control-label no-padding-right">备注：</label>
			<div class="col-sm-8">
				<textarea name="remark" id="remarkText" class="form-control" maxLength="50">${eccInfo.remark!}</textarea>
			</div>
		</div>
		<div class="form-group">
			<label class="col-sm-3 control-label no-padding-right">注：</label>
			<div class="col-sm-8">
				<label><span class="lbl"> 更换用途，请调整相应权限和内容</span></label>
			</div>
		</div>
	</div>
</div>
</form>
<script>
$(function(){
	changeType(1);
})
function setDormPlaceId(){
	$("#placeId-val").val($("#dormPlaceId-val").val());
}

function changeClass(){
	if($("#bpyt-val").val()=='10'){
		var classId = $("#classId-val").val();
		$.ajax({
	        url:"${request.contextPath}/eclasscard/get/classPlace/page",
	        data:{'classId':classId},
	        dataType:'json',
	        async: true,
	        type:'POST',
	        success: function(data) {
				var obj = data;
				if(obj.placeId&&obj.placeId!=''){
					$("#placeId-val").val(obj.placeId);
					$("#showPlaNameSpan").html(obj.placeName);
					$("#classPlaceShow").show();
					$("#classPlaceSelect").hide();
				}else{
					$("#placeId-val").val('');
					$("#placeName").val('');
					$("#classPlaceShow").hide();
					$("#classPlaceSelect").show();
				}
	        },
	        error: function(XMLHttpRequest, textStatus, errorThrown) {
			}
	    })
	}
}

function changeType(type){
	if(type!=1){
		$("#placeId-val").val('');
		$("#classId-val").val('');
		$("#className").val('');
		$("#placeName").val('');
	}
	if($("#bpyt-val").val()=='10'){
		var placeval = $("#placeId-val").val();
		if(placeval&&placeval!=''){
			$("#classPlaceShow").show();
			$("#classPlaceSelect").hide();
		}else{
			$("#classPlaceSelect").show();
			$("#classPlaceShow").hide();
		}
		$("#classChoiseDiv").show();
	}else{
		$("#classPlaceShow").hide();
		$("#classPlaceSelect").show();
		$("#classChoiseDiv").hide();
	}
	if($("#bpyt-val").val()=='30'){
		$("#plcaeChoiseDiv").hide();
		$("#dormPlcaeChoiseDiv").show();
	}else{
		$("#plcaeChoiseDiv").show();
		$("#dormPlcaeChoiseDiv").hide();
	}
}

function saveECLassCard(index){
	var type = $("#bpyt-val").val();
	var classId = $("#classId-val").val();
	var placeId = $("#placeId-val").val();
	if(!type || $.trim(type) == ""){
		layerTipMsgWarn("提示","请选择用途");
		return;
	}
	if(type=='10' && (!classId || $.trim(classId) == "")){
		layerTipMsgWarn("提示","请选择班级");
		return;
	}
	if(!placeId || $.trim(placeId) == ""){
		layerTipMsgWarn("提示","请选择场地");
		return;
	}
	var check = checkValue('#remarkText');
	if(!check){
	 	return;
	}
	var remarkText = $("#remarkText").val();
	if(!remarkText||remarkText==''){
    }else{
    	if(remarkText.length>25){
	    	layerTipMsgWarn("备注","长度不可超过25字");
			return;
    	}
    }
	var options = {
			url : "${request.contextPath}/eclasscard/manage/save",
			dataType : 'json',
			success : function(data){
	 			var jsonO = data;
		 		if(!jsonO.success){
		 			layerTipMsg(jsonO.success,"保存失败",jsonO.msg);
		 			isSubmit = false;
		 		}else{
		 			layer.closeAll();
				  	showList();
				  	layer.msg("保存成功");
    			}
    			layer.close(index);
			},
			clearForm : false,
			resetForm : false,
			type : 'post',
			error:function(XMLHttpRequest, textStatus, errorThrown){}//请求出错 
		};
		$("#submitForm").ajaxSubmit(options);
}
</script>