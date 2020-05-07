<#--
	<div class="page-toolbar">
		<a  href="javascript:void(0)" onclick="toBack()" class="page-back-btn"><i class="fa fa-arrow-left"></i> 返回</a>
		<div class="page-toolbar-btns">
			<a class="btn btn-blue" onclick="achieve('${arrayId}')">完成排班</a>
		</div>
	</div>
-->
<#-- <div class="filter">	
	<div class="filter-item">
		<a class="btn btn-blue" onclick="achieve('${arrayId}')">完成排班</a>
	</div>
</div>  -->
<div class="box box-default">
	<div class="box-body">
		<ul class="nav nav-tabs" role="tablist">
			<#if courseMap?exists>
				<#list courseMap?keys as key>
					<#if key_index == 0>
						<input type="hidden" value="${key}">
						<li class="li active" role="presentation"><a href="javascript:void(0)" onclick="getInfo('${key}')" role="tab" data-toggle="tab">${courseMap[key]}</a></li>
					<#else>
						<li role="presentation"><a href="javascript:void(0)" onclick="getInfo('${key}')" role="tab" data-toggle="tab">${courseMap[key]}</a></li>
					</#if>
				</#list>
			</#if>
		</ul>
		<div class="tab-content" id="showTeacher">
			
		</div>
		
	</div>
</div>
<div class="navbar-fixed-bottom opt-bottom">
	<a class="btn btn-white" href="javascript:void(0)" onclick="toUp()">上一步</a>
	<#-- <a class="btn btn-blue js-add" onclick="doCompleteAdjustTeacher();">保存</a>  -->
	<a class="btn btn-blue" onclick="achieve('${arrayId}')">完成教师安排</a>
</div>


<script>
$(function(){
	showBreadBack(toBack,false,"教师安排");
	
	var id = $(".li.active").prev().val();
	getInfo(id);
})


function getInfo(subjectId) {
	var url =  '${request.contextPath}/newgkelective/${arrayId}/'+subjectId+'/teacher/page';
	$("#showTeacher").load(url);
}

function achieve(arrayId){
	$.ajax({
		url:'${request.contextPath}/newgkelective/${arrayId}/arrayLesson/checkAllTeacher',
		type:'post',
		success:function(data) {
			var jsonO = JSON.parse(data);
	 		if(jsonO.success){
	 			<#if arrangeType?default('01')=='01'>
		 			var url ='${request.contextPath}/newgkelective/${arrayId}/arrayResult/pageIndex';
				<#else>
		 			var url ='${request.contextPath}/newgkelective/xzb/arrayResult/pageIndex?arrayId=${arrayId!}';
				</#if>
				$("#showList").load(url);
	 		}else{
	 			layerTipMsg(data.success,"失败",jsonO.msg);
			}
		},
 		error:function(XMLHttpRequest, textStatus, errorThrown){}
	});
}

function toUp(){
	var url =  '${request.contextPath}/newgkelective/${arrayId}/array/teacher/page';
	$("#showList").load(url);
}
function toBack(){
	<#if arrangeType?default('01')=='02'>
	var url =  '${request.contextPath}/newgkelective/xzb/index/page';
	<#else>
	var url =  '${request.contextPath}/newgkelective/${gradeId}/goArrange/index/page';
	</#if>
	$("#showList").load(url);
}
</script>
