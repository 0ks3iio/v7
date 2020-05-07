<div class="filter">
	<div class="filter-item">
		<span class="filter-name">年级：</span>
		<div class="filter-content">
			<select name="" id="grade" class="form-control" onChange="changeGrade('${type!}')">
				<#if type!='1'><option value="">---请选择---</option></#if>
				<#if grades?exists&&grades?size gt 0>
                  	<#list grades as item>
					<option <#if type!='1'>value="${item.gradeCode!}<#else>value="${item.id!}</#if>">${item.gradeName!}</option>
              	    </#list>
                </#if>
			</select>
		</div>
	</div>
	<div class="filter-item" id="justShowSunDay" style="display:none">
		<label ><input type="checkbox" id="sunDayInput" class="wp" onclick="justSunDay()"><span class="lbl"> 仅显示星期天</span></label>
	</div>
	<div class="filter-item filter-item-right">
		<button class="btn btn-blue" onclick="saveInfoDate()" <#if type!='1'>style="display:none"</#if>>保存</button>
		<button class="btn btn-blue" onclick="periodEdit('')" <#if type=='1'>style="display:none"</#if>>新增</button>
		<button class="btn btn-white" onclick="syncInoutTime()" <#if type!='7'>style="display:none"</#if>>同步</button>
	</div>
</div>

<div id="infoShowDiv">
</div>
<script type="text/javascript">
$(function(){
<#if type=='1'>
	loadDateInfo();
<#elseif type=='3'>
	loadDormPeriod();
<#elseif type=='4'>
	loadGatePeriod();
<#elseif type=='7'>
	loadInOutPeriod();
</#if>
});
function loadDateInfo(justSunDay){
	var grade =$("#grade").val();
	var url =  '${request.contextPath}/eclasscard/attence/date/info/list?grade='+grade+'&justSunDay='+justSunDay;
	$("#infoShowDiv").load(url);
}
function loadDormPeriod(){
	var grade =$("#grade").val();
	var url =  '${request.contextPath}/eclasscard/attence/dorm/period/list?gradeCode='+grade;
	$("#infoShowDiv").load(url);
}
function loadGatePeriod(){
	var grade =$("#grade").val();
	var url =  '${request.contextPath}/eclasscard/attence/gate/period/list?classify=1&gradeCode='+grade;
	$("#infoShowDiv").load(url);
}
function loadInOutPeriod(){
	var grade =$("#grade").val();
	var url =  '${request.contextPath}/eclasscard/attence/gate/period/list?classify=2&gradeCode='+grade;
	$("#infoShowDiv").load(url);
}

function syncInoutTime(){
	var msgIndex = layer.msg("正在同步...");
	$.ajax({
		url:'${request.contextPath}/eccShow/eclasscard/sync/inout',
		data: {},
		type:'post',
		success:function(data) {
			var jsonO = JSON.parse(data);
			layer.close(msgIndex);
 			if(jsonO.success){
 				layer.msg("同步成功");
 			}else{
 				layerTipMsg(jsonO.success,"失败",jsonO.msg);
			}
		},
		error:function(XMLHttpRequest, textStatus, errorThrown){}
	});
}

function changeGrade(type){
	if(type=='1'){
		justSunDay();
	}else if(type=='3'){
		loadDormPeriod();
	}else if(type=='4'){
		loadGatePeriod();
	}else if(type=='7'){
		loadInOutPeriod();
	}
}

function justSunDay(){
	var justSunDay ='';
	if($("#sunDayInput").prop('checked')===true){
		justSunDay ='1';
	}
	$("#justShowSunDay").hide();
	loadDateInfo(justSunDay);
}
</script>