<script src="${request.contextPath}/static/js/jquery.form.js"></script>
<form id="editForm" name="editForm" method="post" >
<div class="box-body groupArrangeList">
	<#assign noData = true />
	<#assign hasExData = false />
	<#if (gkConditionsList?exists && gkConditionsList?size gt 0) || (gkConditionsBList?exists && gkConditionsBList?size gt 0)>
	<#assign noData = false />
	<div class="row">
	<#if needStudy?default(true)>
	<div class="col-sm-6">
	</#if>
	<table class="table table-striped table-hover">
		<thead>
			<tr>
				<th width="">[选考]科目&nbsp;&nbsp;<input type="text" style="width:20%" id="batchCountA" nullable="false" vtype="int" name="batchCountA" <#if rounds.openTwo=="1"> value="1" readOnly="readOnly"<#else> max="10" min="3" value="${rounds.batchCountA?default(3)}" </#if>>批次</th>
				<th width="15%">选课人数</th>
				<th width="15%">班级数（参考值）</th>
				<th width="20%">班级人数（人数上下限）
				</th>
			</tr>
		</thead>
		<tbody>
			<#assign asize = 0 />
			<#if gkConditionsList?exists && gkConditionsList?size gt 0>
			<#assign asize = gkConditionsList?size />
			<#list gkConditionsList as gkc>
			<tr>
				<#if gkc.id?default('') != ''>
					<#assign hasExData = true />
				</#if>
				<#if canEdit>
				<input type="hidden" name="dtos[${gkc_index}].id" value="${gkc.id!}">
				<input type="hidden" name="dtos[${gkc_index}].name" value="${gkc.subjectNames!}">
				<input type="hidden" name="dtos[${gkc_index}].roundsId" value="${gkc.roundsId?default('${roundsId!}')}">
				<input type="hidden" name="dtos[${gkc_index}].type" value="${gkc.type!'1'}">
				<input type="hidden" name="dtos[${gkc_index}].gkType" value="${gkc.gkType!'A'}">
				<input type="hidden" name="dtos[${gkc_index}].subjectIdStr" value="${gkc.subjectIdStr!}">
				<input type="hidden" name="dtos[${gkc_index}].sumNum" value="${gkc.sumNum?default(0)}">
				</#if>
				<td>${gkc.subjectNames!}</td>
				<td>${gkc.sumNum?default(0)}</td>
				<td>
					<#if canEdit>
					<input type="hidden" id="${gkc_index}_hiddenSumNum" value="${gkc.sumNum?default(0)}">
					<input type="text" class="inputClaNumClass" nullable="true" vtype="int" style="width:80%" id="${gkc_index}_inputClaNum" name="dtos[${gkc_index}].claNum" class="form-control pull-left num-input" value="<#if gkc.claNum?default(0) !=0 >${gkc.claNum?default(0)}</#if>" onchange="doChangeClassNum(this,'${gkc_index}')">
					<#else>
						<#if gkc.claNum?default(0) !=0 >${gkc.claNum?default(0)}</#if>
					</#if>
				</td>
				<td>
					<#if canEdit>
					<div class="filter-content">
					<input type="text" style="width:40%" class="form-control pull-left num-input" id="${gkc_index}_num" nullable="false" vtype="int" max="9999" min="1" groupIndex="${gkc_index}" stuMax="${gkc.sumNum?default(0)}" clsMax="${gkc.clsMax}" name="dtos[${gkc_index}].num" value="<#if !(gkc.maxNum??)>${rounds.minNum?default(0)}<#else>${gkc.num?default(0)}</#if>">
					<span class="pull-left">~</span><input type="text" style="width:40%" class="form-control pull-left" id="${gkc_index}_maxNum" nullable="false" vtype="int" max="9999" min="1" groupIndex="${gkc_index}" stuMax="${gkc.sumNum?default(0)}" clsMax="${gkc.clsMax}" name="dtos[${gkc_index}].maxNum" value="<#if gkc.maxNum??>${gkc.maxNum}<#else>${rounds.maxNum?default(0)}</#if>">
					</div>
					<#--
					<span id="${gkc_index}_showStuNumSpan"><#if gkc.claNum?default(0)!=0>${gkc.num?default(0)}~${gkc.maxNum?default(0)}<#else>请输入班级数</#if></span>
					<input type="hidden" id="${gkc_index}_num" name="dtos[${gkc_index}].num" value="${gkc.num?default(0)}">
					<input type="hidden" id="${gkc_index}_maxNum" name="dtos[${gkc_index}].maxNum" value="${gkc.maxNum?default(0)}">
					-->
					<#else>
						${gkc.num?default(0)}~${gkc.maxNum?default(0)}
					</#if>
				</td>
			</tr>
			</#list>
			</#if>
		</tbody>
	</table>
	<#if needStudy?default(true)>
	</div>
	<div class="col-sm-6">
	<table class="table table-striped table-hover">
		<thead>
			<tr>
				<th width="">[学考]科目&nbsp;&nbsp;<input type="text" style="width:20%" id="batchCountB" nullable="false" vtype="int" max="10" min="3" name="batchCountB" value="${rounds.batchCountB?default(4)}">批次</th>
				<th width="15%">选课人数</th>
				<th width="15%">班级数（参考值）</th>
				<th width="20%">班级人数（人数上下限）</th>
			</tr>
		</thead>
		<tbody>
			<#if gkConditionsBList?exists && gkConditionsBList?size gt 0>
			<#list gkConditionsBList as gkc>
			<tr>
				<#if gkc.id?default('') != ''>
					<#assign hasExData = true />
				</#if>
				<#assign bindex = gkc_index+asize />
				<#if canEdit>
				<input type="hidden" name="dtos[${bindex}].id" value="${gkc.id!}">
				<input type="hidden" name="dtos[${bindex}].name" value="${gkc.subjectNames!}">
				<input type="hidden" name="dtos[${bindex}].roundsId" value="${gkc.roundsId?default('${roundsId!}')}">
				<input type="hidden" name="dtos[${bindex}].type" value="${gkc.type!'1'}">
				<input type="hidden" name="dtos[${bindex}].gkType" value="${gkc.gkType!'B'}">
				<input type="hidden" name="dtos[${bindex}].subjectIdStr" value="${gkc.subjectIdStr!}">
				<input type="hidden" name="dtos[${bindex}].sumNum" value="${gkc.sumNum?default(0)}">
				</#if>
				<td>${gkc.subjectNames!}</td>
				<td>${gkc.sumNum?default(0)}</td>
				<td>
					<#if canEdit>
					<input type="hidden" id="${bindex}_hiddenSumNum" value="${gkc.sumNum?default(0)}">
					<input type="text" class="inputClaNumClass" nullable="true" vtype="int" style="width:80%" id="${bindex}_inputClaNum" name="dtos[${bindex}].claNum" class="form-control pull-left num-input" value="<#if gkc.claNum?default(0) !=0 >${gkc.claNum?default(0)}</#if>" onchange="doChangeClassNum(this,'${bindex}')">
					<#else>
						<#if gkc.claNum?default(0) !=0 >${gkc.claNum?default(0)}</#if>
					</#if>
				</td>
				<td>
					<#if canEdit>
					<div class="filter-content">
						<input type="text" style="width:40%" class="form-control pull-left num-input" id="${bindex}_num" nullable="false" vtype="int" max="9999" min="1" groupIndex="${bindex}" stuMax="${gkc.sumNum?default(0)}" clsMax="${gkc.clsMax}" name="dtos[${bindex}].num" value="<#if !(gkc.maxNum??)>${rounds.minNum?default(0)}<#else>${gkc.num?default(0)}</#if>">
						<span class="pull-left">~</span><input type="text" style="width:40%" class="form-control pull-left" id="${bindex}_maxNum" nullable="false" vtype="int" max="9999" min="1" groupIndex="${bindex}" stuMax="${gkc.sumNum?default(0)}" clsMax="${gkc.clsMax}" name="dtos[${bindex}].maxNum" value="<#if gkc.maxNum??>${gkc.maxNum}<#else>${rounds.maxNum?default(0)}</#if>">
					</div>
					<#--
					<span id="${bindex}_showStuNumSpan"><#if gkc.claNum?default(0)!=0>${gkc.num?default(0)}~${gkc.maxNum?default(0)}<#else>请输入班级数</#if></span>
					<input type="hidden" id="${bindex}_num" name="dtos[${bindex}].num" value="${gkc.num?default(0)}">
					<input type="hidden" id="${bindex}_maxNum" name="dtos[${bindex}].maxNum" value="${gkc.maxNum?default(0)}">
					-->
					<#else>
						${gkc.num?default(0)}~${gkc.maxNum?default(0)}
					</#if>
				</td>
			</tr>
			</#list>
			<#else>
			</#if>
		</tbody>
	</table>
	</div>
	</#if>
	</div>
	<#else>
	<div class="no-data-container">
		<div class="no-data">
			<span class="no-data-img">
				<img src="${request.contextPath}/gkelective/images/noOpenClass.png" alt="">
			</span>
			<div class="no-data-body">
				<p class="no-data-txt">当前没有可开班的选课结果</p>
			</div>
		</div>
	</div>
	</#if>
	
	
	<div class="text-right">
		<span id="mymess">
			<#if hasnowpaike>
				正在走班排班中，请稍后...
			</#if>
		</span>
		<a class="btn btn-blue singleCutBtnClass" <#if !hasnowpaike>style="display:none"</#if> href="javascript:" onclick="cutPaiban()">取消</a>
		<#if rounds.openClassType?default('') == '1'>
		<a class="btn btn-white preStep-btn" href="javascript:">上一步</a>
		</#if>
		<#if (rounds.step>4) && isCanRestart>
		<a class="btn btn-blue refresh-btn singleBtnClass" <#if hasnowpaike>style="display:none"</#if> href="javascript:" data-toggle="tooltip" data-placement="top" title="" data-original-title="若被方案引用，则无法重新安排">重新安排</a>
		</#if>
		<a class="btn btn-blue nextStep-btn singleBtnClass" <#if hasnowpaike>style="display:none"</#if> href="javascript:"><#if !canEdit || noData>下一步<#else>进行走班排班</#if></a>
	
	</div>
</div>
</form>
<!-- page specific plugin scripts -->
<script type="text/javascript">
$(function(){
	<#if hasnowpaike>
	toPaiban();
	</#if>
	$('[data-toggle="tooltip"]').tooltip({
		container: 'body',
		trigger: 'hover'
	});
});
$('.nextStep-btn').on("click",function(){
	<#if !canEdit && !noData>
		toNextStep();
	<#else>
		saveGroup();
	</#if>
});

$('.preStep-btn').on("click",function(){
	toUnArrange();
});
function doChangeClassNum(obj,index){
	var claNum = $(obj).val($(obj).val().trim()).val();
	var sumNum = $(obj).parents("tr").find("#"+index+"_hiddenSumNum").val();
	var showStuNumObj = $(obj).parents("tr").find("#"+index+"_showStuNumSpan");
	var minNumObj = $(obj).parents("tr").find("#"+index+"_num");
	var maxNumObj = $(obj).parents("tr").find("#"+index+"_maxNum");
	if (!/^\d+$/.test(claNum) || claNum <= 0) {
		layer.tips("请输入正整数", "#"+index+"_inputClaNum", {
			tipsMore: true,
			tips:3		
		});
		showStuNumObj.val("");
		minNumObj.val("");
		maxNumObj.val("");
		return;
	}
	var mstuNum = parseInt(sumNum/claNum);
	var mstuNumRe = sumNum%claNum;
	var minNum = 0;
	var maxNum = 0;
	if(mstuNumRe == 0){
		minNum = mstuNum-3;
		maxNum = mstuNum+3;
	}else{
		minNum = mstuNum-3;
		maxNum = mstuNum+3;
	}
	if(minNum<=0){
		layer.tips("班级数不合理请重新设置", "#"+index+"_inputClaNum", {
			tipsMore: true,
			tips:3		
		});
		showStuNumObj.val("");
		minNumObj.val("");
		maxNumObj.val("");
		return;
	}
	showStuNumObj.html(minNum+"~"+maxNum);
	minNumObj.val(minNum);
	maxNumObj.val(maxNum);
}
<#if (rounds.step>4) && isCanRestart>
$('.refresh-btn').on("click",function(){
	var options = {btn: ['确定','取消'],title:'确认信息', icon: 1,closeBtn:0};
	showConfirm("重新安排将清除走班开班条件数据（包括走班开班结果数据），确定要重新安排开班吗？",options,refreshOpen,function(){});
});

// 清除重新安排
function refreshOpen(){
	var ii = layer.load();
	$.ajax({
		url:'${request.contextPath}/gkelective/${roundsId!}/openClassArrange/delete',
		data:{"type":'${type!}'},
		dataType : 'json',
		type:'post',
		success:function(data) {
			var jsonO = data;
	 		if(jsonO.success){
	 			layer.closeAll();
				layerTipMsg(jsonO.success,"成功",jsonO.msg);
			  	dealClickType(['.all-result-step'],false);
			  	toSingle();
	 		}
	 		else{
	 			layer.closeAll();
	 			layerTipMsg(jsonO.success,"失败",jsonO.msg);
			}
			layer.close(ii);
		},
 		error:function(XMLHttpRequest, textStatus, errorThrown){}
	});
}
</#if>
var isSubmit=false;
var saveBtn = '.nextStep-btn';
function saveGroup(){
	if(isSubmit){
		return;
	}
	isSubmit=true;
	$(saveBtn).addClass("disabled");
	
	var check = checkValue('.groupArrangeList');
	if(!check){
	 	$(saveBtn).removeClass("disabled");
	 	isSubmit=false;
	 	return;
	}
	
	var flag = true;
	$('.num-input').each(function(){
		var order = $(this).attr('groupIndex');
		var num = $("#"+order+"_num").val();
		var maxNum = $("#"+order+"_maxNum").val();
		if(parseFloat(num) > parseFloat(maxNum)){
			flag = false;
			layer.tips('人数下限值不能超过上限值!', $("#"+order+"_num"), {
				tipsMore: true,
				tips: 4
			});
		}
	});
	if(!flag){
		$(saveBtn).removeClass("disabled");
		isSubmit=false;
		return;
	}
	
	// 提交数据
	//var queryString = $('#editForm').formSerialize()
	var options = {
	    url:'${request.contextPath}/gkelective/${roundsId!}/openClassArrange/save?type=0',
	    dataType : 'json',
	    type:'post',  
	    cache:false, 
	    success:function(data) {
	    	var jsonO = data;//JSON.parse(data);
	    	if(jsonO.success){
	 			layer.closeAll();
			  	toPaiban();
			  	//toNextStep();
	 		}
	 		else{
	 			layerTipMsg(jsonO.success,"失败",jsonO.msg);
	 			$(saveBtn).removeClass("disabled");
	 			isSubmit=false;
			}
	     },
	     error:function(XMLHttpRequest, textStatus, errorThrown){}//请求出错 
	};
 	$('#editForm').ajaxSubmit(options);
}

// 触发排班
function toPaiban(){
	$("#mymess").html("正在准备排班中，请稍后...");
	<#if isCom>
	var url='${request.contextPath}/gkelective/${roundsId!}/openClassArrange/singleResult/save';
	<#else>
	var url='${request.contextPath}/gkelective2/${roundsId!}/openClassArrange/singleResult/save';
	</#if>
	$.ajax({
		url:url,
		dataType : 'json',
		type:'post',
		success:function(data) {
			var jsonO = data;
	 		if(jsonO.success){
	 			if(jsonO.msg=="now"){
	 				$(".singleBtnClass").hide();
	 				$(".singleCutBtnClass").show();
	 				$("#mymess").html("正在走班排班中，请稍后...");
	 				toPaiban2();
	 			}else if(jsonO.msg=="end"){
	 				dealClickType(['.all-result-step'],true);
	 				toNextStep();
	 			}
	 		}
	 		else{
	 			$("#mymess").html(jsonO.msg);
	 			$(".singleBtnClass").show();
	 			$(".singleCutBtnClass").hide();
	 			$(saveBtn).removeClass("disabled");
	 			isSubmit=false;
			}
			
		},
 		error:function(XMLHttpRequest, textStatus, errorThrown){}
	});
	
	
}


function toPaiban2(){
	<#if isCom>
		var url='${request.contextPath}/gkelective/${roundsId!}/openClassArrange/singleResult/check';
	<#else>
		var url='${request.contextPath}/gkelective2/${roundsId!}/openClassArrange/singleResult/check';
	</#if>
	$.ajax({
		url:url,
		dataType : 'json',
		type:'post',
		success:function(data) {
			var jsonO = data;
	 		if(jsonO.success){
	 			if(jsonO.msg=="now"){
	 				$(".singleBtnClass").hide();
	 				$(".singleCutBtnClass").show();
	 				if($("#mymess").length > 0){
		 				$("#mymess").html("正在走班排班中，请稍后...");
		 				window.setTimeout("toPaiban2()",800);
	 				}
	 			}else if(jsonO.msg=="end"){
	 				dealClickType(['.all-result-step'],true);
	 				toNextStep();
	 			}else{
	 				//排班结束 但走班没有
	 				toSingle();
	 			}
	 		}
	 		else{
	 			$("#mymess").html(jsonO.msg);
	 			$(".singleBtnClass").show();
	 			$(".singleCutBtnClass").hide();
	 			$(saveBtn).removeClass("disabled");
	 			isSubmit=false;
			}
			
		},
 		error:function(XMLHttpRequest, textStatus, errorThrown){}
	});
	
	
}

function cutPaiban(){
	<#if isCom>
		var url='${request.contextPath}/gkelective/${roundsId!}/openClassArrange/singleResult/cut';
	<#else>
		var url='${request.contextPath}/gkelective2/${roundsId!}/openClassArrange/singleResult/cut';
	</#if>
	$.ajax({
		url:url,
		dataType : 'json',
		type:'post',
		success:function(data) {
			var jsonO = data;
	 		if(jsonO.success){
	 			if(jsonO.msg=="no"){
	 				layerTipMsg(jsonO.success,"成功","无需取消，排班已结束");
	 				dealClickType(['.all-result-step'],true);
	 			}
	 			toSingle();
	 		}
	 		else{
	 			layerTipMsg(jsonO.success,"失败",jsonO.msg);
	 			toSingle();
	 			isSubmit=false;
			}
			
		},
 		error:function(XMLHttpRequest, textStatus, errorThrown){}
	});
}

function toNextStep(){
	toAllResult();
}	
</script>