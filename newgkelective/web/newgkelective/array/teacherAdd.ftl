<#--
	<div class="page-toolbar">
		<a href="javascript:goBack();" class="page-back-btn""><i class="fa fa-arrow-left"></i> 返回</a>
		<div class="page-toolbar-btns">
			<span class="tip tip-grey">共有 <#if dtoMap?exists>${dtoMap?size!0}<#else>0</#if>条结果</span>
			<a class="btn btn-blue" href="javascript:void(0);" onClick="doAdd();">新增教师资源方案</a>
		</div>
	</div>
-->
<div class="filter">	
	<div class="filter-item">
		<a class="btn btn-blue" href="javascript:void(0);" onClick="doAdd();">新增教师资源方案</a>
	</div>
	<div class="filter-item filter-item-right">
		<span class="color-999"><#if dtoMap?exists>${dtoMap?size!0}<#else>0</#if>条结果</span>
	</div>
</div>
<#if dtoMap?exists && dtoMap?size gt 0>
<#list dtoMap?keys as key>
<div class="box box-default">
	<div class="box-header">
		<h3 class="box-title">${dtoMap[key].itemName!}</h3>
		<div class="box-header-tools">
			<div class="btn-group" role="group">
				<div class="btn-group" role="group">
				<a type="button" class="btn btn-sm btn-white" href="javascript:" onClick="doUpdate('${key!}');">查看教师资源设置</a>
					<button type="button" class="btn btn-sm btn-white dropdown-toggle" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
						更多<span class="caret"></span>
					</button>
					<ul class="dropdown-menu dropdown-menu-right">
						<li><a href="#" class="js-del" onClick="doDelete('${key!}')">删除</a></li>
					</ul>
				</div>
			</div>
		</div>
	</div>
	<div class="box-body">
		<p class="explain">创建时间：${dtoMap[key].creationTime?string("yyyy-MM-dd HH:mm")!}</p>
		<div class="number-container">
			<a href="javascript:void(0);" onClick="doUpdate('${key!}');">
				<ul class="number-list">
					<#if dtoMap[key].subjectList?exists && dtoMap[key].subjectList?size gt 0>
					<#list dtoMap[key].subjectList as item>
						<li><em>${item.exTeacherIdList?size}</em><span>${item.subjectName!}</span></li>
						</#list>
					</#if>
				</ul>
			</a>
		</div>
	</div>
</div>
</#list>
</#if>
<script>
	showBreadBack(goBack,false,"教师资源方案列表");
	function goBack(){
		<#if arrayId?default('')==''>
		var url =  '${request.contextPath}/newgkelective/${gradeId!}/goArrange/addArray/page?divideId=${divideId!}';
		$("#showList").load(url);
		<#else>
			var url =  '${request.contextPath}/newgkelective/${gradeId!}/goArrange/editArray/page?arrayId=${arrayId!}';
			$("#showList").load(url);
		</#if>
	}
	function doDelete(itemId){
		var ii = layer.load();
		layer.confirm('确定删除吗？', function(index){
			$.ajax({
				url : '${request.contextPath}/newgkelective/'+itemId+'/teacherArrange/delete',
				dataType : 'json',
				type : 'post',
				success : function(data){
			 		if(data.success){
			 			layer.closeAll();
						layer.msg(data.msg, {offset: 't',time: 2000});
						
						url='${request.contextPath}/newgkelective/${divideId!}/teacherArrange/index/page?gradeId=${gradeId!}&arrayId=${arrayId!}';
						$("#showList").load(url);
			 		}
			 		else{
			 			layerTipMsg(data.success,"失败",data.msg);
					}
					layer.close(ii);
				},
				error:function(XMLHttpRequest, textStatus, errorThrown){}//请求出错 
			});
		})
	}
	function doUpdate(itemId){
		var url='${request.contextPath}/newgkelective/${divideId!}/teacherArrange/update?gradeId=${gradeId!}&itemId='+itemId+'&arrayId=${arrayId!}';
		$("#showList").load(url);
	}
	function doAdd(){
		var url='${request.contextPath}/newgkelective/${divideId!}/teacherArrange/add?gradeId=${gradeId!}&arrayId=${arrayId!}';
		$("#showList").load(url);
	}
</script>