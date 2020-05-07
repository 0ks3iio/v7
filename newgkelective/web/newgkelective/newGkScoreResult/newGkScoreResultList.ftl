<div class="table-container">
	<div class="table-container-header clearfix" id="fbt">
		<span>共${count!}份结果</span>
		<#if used?exists && used>
			<button class="btn btn-sm btn-blue pull-right" data-f="0" onClick="saveRef2(event)">取消参考分</button>
		<#else>
			<button class="btn btn-sm btn-blue pull-right" data-f="1" onClick="saveRef2(event)">设为参考分</button>
		</#if>
	</div>
	<div class="table-container-body">
		<table class="table table-bordered table-striped table-hover">
			<thead>
				<thead>
				<tr>
					<th>序号</th>
					<th>姓名</th>
					<th>学号</th>
					<th>性别</th>
					<th>行政班</th>
					<th>总分</th>
					<#if courseList?exists && courseList?size gt 0>
					   <#list courseList as item>
					       <th>${item.subjectName!}</th>
					   </#list>
					</#if>
				</tr>
			</thead>
			<tbody>
			<#if dataList?exists && dataList?size gt 0>
				<#list dataList as item>
				<tr>
					<#list 0..num as t>
					   <td>${item[t]!}</td>
				    </#list>
				</tr>
				</#list>
			</#if>																
			</tbody>
		</table>
	</div>
</div>




<script>
function saveRef2(event){
	//debugger;
   state = $(event.target).attr("data-f");
  // alert(state);
   var gradeId = '${gradeId!}';
   var refId = '${refId!}';
   var classId = '${classId!}';
   $.ajax({
		url:"${request.contextPath}/newgkelective/newGkScoreResult/saveRef",
		data:{gradeId:gradeId,refId:refId,state:state},
		dataType : 'json',
		success : function(data){
		   if(!data.success){
		 	  //layerTipMsg(data.success,"删除失败",data.msg);
		 	  //return;
		 	  layerTipMsg(data.success,"保存失败",data.msg);
		   }else{
		 	  //layer.closeAll();
			  //layerTipMsg(data.success,"删除成功",data.msg);
			  //searchList(classId);
			  layer.msg("操作成功", {
						offset: 't',
						time: 2000
					});
			  switchDefault(state);
    	   }
	    },
		error:function(XMLHttpRequest, textStatus, errorThrown){alert(errorThrown);} 
	});
}
function switchDefault(state){
	if(state == 1){
		$("#fbt button").attr("data-f",0).text("取消参考分");
	}else if(state == 0){
		$("#fbt button").attr("data-f",1).text("设为参考分");
	}else{
		alert("未知状态");
	}
}
</script>