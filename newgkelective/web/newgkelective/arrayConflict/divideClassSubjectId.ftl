<div class="filter">
	<div class="filter-item">
		<span class="filter-name">科目：</span>
		<div class="filter-content">
			<select name="" id="subjectId" class="form-control" onChange="refreshThis();">
			<#if courseList?exists && courseList?size gt 0>
			    <#list courseList as item>
				    <option value="${item.id!}" <#if '${item.id!}'== '${subjectId!}'>selected</#if>>${item.subjectName!}</option>
				</#list>
			</#if>
			</select>
		</div>
	</div>
	<div class="filter-item">
		<span class="filter-name">左列表：</span>
		<div class="filter-content">
			<select name="" id="classId" class="form-control" onChange="showRight();" style="width:200px">
			<#if allClassList?exists && allClassList?size gt 0>
			    <#list allClassList as item>
				    <option value="${item.id!}" <#if '${item.id!}'== '${classId!}'>selected</#if>>${item.className!}</option>
				</#list>
			</#if>
			</select>
		</div>
	</div>
	<div class="filter-item">
		<span class="filter-name">右列表：</span>
		<div class="filter-content">
			<select name="" id="newClassId" class="form-control" onChange="doChange();" style="width:200px">
			<#if newClassList?exists && newClassList?size gt 0>
			    <#list newClassList as item>
				    <option value="${item.id!}" <#if '${item.id!}'== '${newClassId!}'>selected</#if>>${item.className!}</option>
				</#list>
			</#if>
			</select>
		</div>
	</div>
	<div class="filter-item">
		<span class="filter-name">输入：</span>
		<div class="filter-content">
			<input type="text" id="inputValue" name="inputValue" value="1" width="160px">
		</div>
	</div>
	<div class="filter-item">
		<a class="btn btn-blue" onclick="showRight();">加载右列表</a>
		<a class="btn btn-blue" onclick="balanceClass();">移动</a>
	</div>
</div>
<script>
$(function(){
	
})
function refreshThis(){
	var subjectId=$("#subjectId").val();
	var url =  '${request.contextPath}/newgkelective/${arrayId!}/arrayConflict/subjectBalanceIndex?subjectId='+subjectId;
	$("#balanceClassListid").load(url);
}

function showRight(){
	var subjectId=$("#subjectId").val();
	var classId = $("#classId").val();
	var url =  '${request.contextPath}/newgkelective/${arrayId!}/arrayConflict/subjectBalanceList?classId='+classId+'&subjectId='+subjectId;
	$("#balanceClassListid").load(url);
}


var isBalanceClass=false;
function balanceClass(){
	if(isBalanceClass){
		return;
	}
	isBalanceClass=true;
	var fromClassId=$("#classId").val();
	var toClassId=$("#newClassId").val();
	var moveNum=$("#inputValue").val();
	if(fromClassId=="" || toClassId=="" || moveNum==""){
		isBalanceClass=false;
		return;
	}
	var url='${request.contextPath}/newgkelective/${arrayId!}/arrayStudent/balanceClass';
	var ii = layer.load();
	$.ajax({
	    url:url,
		data: {'fromClassId':fromClassId,'toClassId':toClassId,'moveNum':parseInt(moveNum)},
		dataType : 'json',
	    success:function(data) {
	    	var jsonO = data;
	 		if(!jsonO.success){
	 			layerTipMsg(jsonO.success,"操作失败",jsonO.msg);
	 			isBalanceClass = false;
	 		}else{
	 			layer.closeAll();
				layer.msg(jsonO.msg, {offset: 't',time: 2000});
				var url =  '${request.contextPath}/newgkelective/${arrayId!}/arrayStudent/conflictIndex/page';
				$("#showList").load(url);
			}
	    	layer.close(ii);
	    }
	});
}
</script>