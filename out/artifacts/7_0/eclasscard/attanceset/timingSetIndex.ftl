<div class="explain">
    <p>设置每天按开机时间段，并且选择勾选一周哪几天受开机时间段控制，不勾选则不受控制；非开机时间段内，班牌自动进入待机状态</p>
</div>
<p class="">
    请设置班牌每天开机时间段：
</p>
<form id="timingSetForm">
    <input type="hidden" id="unitId" name="unitId" value="${unitId!}" >
<div class="clearfix">
    <div class="filter-item block">
        <div class="filter-content">
            <div class="input-group">
                <input type="text" class="form-control timepicker" id="openTime" name="openTime" value="${openTime!}">
                <span class="input-group-addon">
                    <i class="fa fa-minus"></i>
                </span>
                <input type="text" class="form-control timepicker" id="closeTime" name="closeTime" value="${closeTime!}">
            </div>
        </div>
    </div>
    <div class="filter-item block" id="weekChooseDiv">
        <div class="filter-content">
            <label>
                <input id="MON" name="weekChoose" type="checkbox" class="wp" value="MON">
                <span class="lbl"> 星期一</span>
            </label>
            <label>
                <input id="TUES"name="weekChoose" type="checkbox" class="wp" value="TUES">
                <span class="lbl"> 星期二</span>
            </label>
            <label>
                <input id="WED" name="weekChoose" type="checkbox" class="wp" value="WED">
                <span class="lbl"> 星期三</span>
            </label>
            <label>
                <input id="THUR" name="weekChoose" type="checkbox" class="wp" value="THUR">
                <span class="lbl"> 星期四</span>
            </label>
            <label>
                <input id="FRI" name="weekChoose" type="checkbox" class="wp" value="FRI">
                <span class="lbl"> 星期五</span>
            </label>
            <label>
                <input id="SAT" name="weekChoose" type="checkbox" class="wp" value="SAT">
                <span class="lbl"> 星期六</span>
            </label>
            <label>
                <input id="SUN" name="weekChoose" type="checkbox" class="wp" value="SUN">
                <span class="lbl"> 星期日</span>
            </label>
        </div>
    </div>
    <div class="filter-item block">
        <div class="filter-content">
            <button type="button" class="btn btn-blue" onclick="saveTimingSet()">保存</button>
        </div>
    </div>
</div>
</form>
<script>
	$(function(){
		if(date_timepicker &&date_timepicker!=null){
			date_timepicker.remove();
		}
	   
		setTimeout(function(){
			date_timepicker = $('.timepicker').timepicker({
    			defaultTime: '',
    			showMeridian: false
    		})
		},100);
        
        <#if timingSets?exists&&timingSets?size gt 0>
            <#list timingSets as set>
                $("#${set.code!}").attr("checked","true");
            </#list>
        </#if>
	});
	
	var isSubmit=false;
	function saveTimingSet() {
		if (isSubmit) {
			return;
		}
		var openTime = $("#openTime").val();
		var closeTime = $("#closeTime").val();
		if (openTime == "" || closeTime == "") {
            layer.msg("开机时间和关机时间不得为空！");
            return;
        }
		if (compareTimes($("#openTime"),$("#closeTime")) >= 0) {
            layer.msg("关机时间应大于开机时间！");
            return;
        }
        var checkWeek = false;
        $("#weekChooseDiv").find("input").each(function () {
            if ($(this).prop('checked')===true) {
                checkWeek = true;
                return false;
            }
        })
		if (!checkWeek) {
            layer.msg("请选择星期！");
			return;
		}
		var rellayer = layer.confirm('确定保存吗？', function(index){
			layer.close(rellayer);
			isSubmit = true;
			var options = {
				url : "${request.contextPath}/eclasscard/attence/timing/save",
				dataType : 'json',
				success : function(data){
 					isSubmit = false;
 					if(data.success){
	 					layer.msg("保存成功");
	 					$("#tabList").load("${request.contextPath}/eclasscard/attence/timing/set");
 					}else{
 						layerTipMsg(data.success,"失败",data.msg);
					}
				},
				clearForm : false,
				resetForm : false,
				type : 'post',
				error:function(XMLHttpRequest, textStatus, errorThrown){}//请求出错 
			};
			$("#timingSetForm").ajaxSubmit(options);
		});
	}
</script>