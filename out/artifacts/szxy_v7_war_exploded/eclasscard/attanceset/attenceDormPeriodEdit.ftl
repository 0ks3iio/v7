<form id="submitForm">
<input type="hidden" name="id" value="${eccAttenceDormPeriod.id!}">
<div class="layer-content">
	<div class="form-horizontal">
		<div class="form-group">
			<label class="col-sm-3 control-label no-padding-right">类型：</label>
			<div class="col-sm-8">
			<#if eccAttenceDormPeriod.type?exists>
				<#if eccAttenceDormPeriod.type==1>
					<input type="hidden" name="type" value="1" >
					<label style="font-weight: normal;"><span class="lbl">上学日考勤</span></label>
				<#elseif eccAttenceDormPeriod.type==2>
					<input type="hidden" name="type" value="2" class="wp">
					<label style="font-weight: normal;"><span class="lbl">收假日考勤</span></label>
				</#if>
			<#else>
				<select name="type" id="attencetype" class="form-control">
					<option value="1">上学日考勤</option>
					<option value="2">收假日考勤</option>
				</select>
			</#if>
			</div>
		</div>
		<div class="form-group checked-nullable">
			<label class="col-sm-3 control-label no-padding-right">时段：</label>
			<div class="col-sm-8">
				<div class="input-group">
					<input name="beginTime" id="beginTime" type="text" nullable="false" class="form-control timepicker" value="${eccAttenceDormPeriod.beginTime!}">
					<span class="input-group-addon">
						<i class="fa fa-minus"></i>
					</span>
					<input name="endTime" id="endTime" type="text" nullable="false" class="form-control timepicker" value="${eccAttenceDormPeriod.endTime!}">
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
		<div class="form-group">
			<label class="col-sm-3 control-label no-padding-right">条件：</label>
			<div class="col-sm-8">
				<label><input name="nextDayAttence" type="checkbox" <#if eccAttenceDormPeriod.nextDayAttence>checked="true"</#if> class="wp"><span class="lbl"> 次日为休息日时该时段不考勤</span></label>
			</div>
		</div>
	</div>
</div>
</form>
<script type="text/javascript">
$(function(){
   $('.timepicker').timepicker({
    	defaultTime: '',
    	showMeridian: false
    })
});  
var isSubmit = false;
function saveDormPeriod(index){
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
			url : "${request.contextPath}/eclasscard/attence/dorm/period/save",
			dataType : 'json',
			success : function(data){
		 		if(!data.success){
		 			layerTipMsg(data.success,"保存失败",data.msg);
		 			isSubmit = false;
		 		}else{
		 			layer.closeAll();
				  	loadDormPeriod();
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