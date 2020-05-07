<form id="submitForm">
<input type="hidden" name="id" value="${gatePeriod.id!}">
<input type="hidden" name="classify" value="${classify?default(1)}">
<!-- S 离校时间 -->
<div class="layer-content">
	<div class="form-horizontal">
		<div class="form-group">
			<label class="col-sm-3 control-label no-padding-right">类型：</label>
			<div class="col-sm-8">
			<#if classify?default(1)==1>
				<#if gatePeriod.type?exists>
					<#if gatePeriod.type==1>
						<input type="hidden" name="type" value="1" >
						<label style="font-weight: normal;"><span class="lbl">放假日离校</span></label>
					<#elseif gatePeriod.type==2>
						<input type="hidden" name="type" value="2">
						<label style="font-weight: normal;"><span class="lbl">休假日离校</span></label>
					<#elseif gatePeriod.type==3>
						<input type="hidden" name="type" value="3">
						<label style="font-weight: normal;"><span class="lbl">临时离校</span></label>
					<#elseif gatePeriod.type==4>
						<input type="hidden" name="type" value="4">
						<label style="font-weight: normal;"><span class="lbl">通校生离校</span></label>
					</#if>
				<#else>
					<select name="type" id="gateType" class="form-control js-changeType" onChange="changeTimepicker()">
						<option value="1">放假日离校</option>
						<option value="2">休假日离校</option>
						<option value="3">临时离校</option>
						<option value="4">通校生离校</option>
					</select>
				</#if>
			<#else>
				<#if gatePeriod.type?exists>
					<#if gatePeriod.type==1>
						<input type="hidden" name="type" value="1" >
						<label style="font-weight: normal;"><span class="lbl">上学</span></label>
					<#else>
						<input type="hidden" name="type" value="2">
						<label style="font-weight: normal;"><span class="lbl">放学</span></label>
					</#if>
				<#else>
					<select name="type" id="gateType" class="form-control js-changeType" onChange="changeTimepicker()">
						<option value="1">上学</option>
						<option value="2">放学</option>
					</select>
				</#if>
			</#if>
			</div>
		</div>
		<div id="tempDateDiv" class="form-group" style="display:none">
			<label class="col-sm-3 control-label no-padding-right">日期：</label>
			<div class="col-sm-8">
				<input name="tempDate" id="tempDate" type="text" data-format="yyyy-MM-dd" class="form-control timepicker1" value="${(gatePeriod.tempDate?default(.now))?string('yyyy-MM-dd')}">
			</div>
		</div>
		<div class="form-group checked-nullable">
			<label class="col-sm-3 control-label no-padding-right">时段：</label>
			<div class="col-sm-8">
				<div class="input-group">
					<input name="beginTime" id="beginTime" type="text" nullable="false" class="form-control timepicker" 
						<#if classify?default(1)==1>value="${gatePeriod.beginTime?default("17:00")}"<#else>value="${gatePeriod.beginTime!}"</#if>>
					<span class="input-group-addon">
						<i class="fa fa-minus"></i>
					</span>
					<input name="endTime" id="endTime" type="text" nullable="false"  class="form-control timepicker"
						<#if classify?default(1)==1>value="${gatePeriod.endTime?default("23:59")}"<#else>value="${gatePeriod.endTime!}"</#if>>
				</div>
			</div>
		</div>
		<div class="form-group">
			<label class="col-sm-3 control-label no-padding-right">年级：</label>
			<div class="col-sm-8" id="checkGradeDiv">
				<#if eccGradeDtos?exists&&eccGradeDtos?size gt 0>
                  	<#list eccGradeDtos as item>
                  	<label><input name="gradeCodes" type="checkbox" <#if item.select>checked="true"</#if> class="wp" value="${item.gradeCode!}"><span class="lbl">${item.gradeName!}</span></label>
              	    </#list>
                </#if>
			</div>
		</div>
	</div>
</div><!-- E 离校时间 -->
</form>
<script type="text/javascript">
$(function(){
	<#if classify?default(1)==1 && gatePeriod.type?exists && gatePeriod.type==3>
		$("#tempDateDiv").show();
		showDateSelect();
	</#if>
	
   $('.timepicker').timepicker({
    	defaultTime: '',
    	showMeridian: false
    })

});  

function changeTimepicker(){
	if($("#gateType").val()==3){
		$("#tempDateDiv").show();
		showDateSelect();
	}else{
		$("#tempDateDiv").hide();
	}
}

function showDateSelect(){
	var minDate = "${((.now)?string('yyyy-MM-dd'))?if_exists}";
	$('.timepicker1').datetimepicker({
		startDate:minDate,
    	language: 'zh-CN',
    	minView: 'month', //选择日期后，不会再跳转去选择时分秒 
　　		format: 'yyyy-mm-dd',
    	autoclose: true
    })
}
var isSubmit = false;
function saveGatePeriod(index){
	if(isSubmit){
        return;
    }
	var check = checkValue('.checked-nullable');
	if(!check){
	 	return;
	}
	if(compareTimes($("#beginTime"),$("#endTime"))>=0){
		layerTipMsgWarn("时段","结束时间应大于开始时间");
		return;
	}
	var checkgrade = false;
	$("#checkGradeDiv").find("input").each(function () {
        if ($(this).prop('checked')===true) {
            checkgrade = true;
            return false;
        }
    })
	if(!checkgrade){
		layerTipMsgWarn("请选择年级","");
		return;
	}
	isSubmit = true;
	var options = {
			url : "${request.contextPath}/eclasscard/attence/gate/period/save",
			dataType : 'json',
			success : function(data){
		 		if(!data.success){
		 			layerTipMsg(data.success,"保存失败",data.msg);
		 			isSubmit = false;
		 		}else{
		 			layer.closeAll();
		 			<#if classify?default(1)==1>
				  	loadGatePeriod();
				  	<#else>
				  	loadInOutPeriod();
				  	</#if>
				  	layer.msg("保存成功");
    			}
			},
			clearForm : false,
			resetForm : false,
			type : 'post',
			error:function(XMLHttpRequest, textStatus, errorThrown){}//请求出错 
		};
		$("#submitForm").ajaxSubmit(options);
}
</script>