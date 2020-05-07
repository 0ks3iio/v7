<title>荣誉评选List</title>
<#import "/fw/macro/htmlcomponent.ftl" as htmlcom>
<div class="table-wrapper">
	<table class="table table-bordered table-striped table-hover no-margin">
		<thead>
				<tr>
					<th>姓名</th>
					<th>荣誉类型</th>
					<th>荣誉名称</th>
					<th>获得日期</th>
					<th style="width:200px">备注</th>
					<th>操作</th>
				</tr>
		</thead>
		<tbody>
			<#if honorRecordsList?exists && (honorRecordsList?size > 0)>
				<#list honorRecordsList as list>
					<tr>
						<td>${list.studentName!}</td>
						<td>${list.honorTypeStr!}</td>
						<#if list.honorType == "1">
							<td>${mcodeSetting.getMcode('DM-XJRW',(list.honorLevel!))}</td>
						<#else>
							<td>${mcodeSetting.getMcode('DM-QCYGK',(list.honorLevel!))}</td>
						</#if>
						<td>${(list.giveDate?string('yyyy-MM-dd'))?if_exists}</td>
						<td title="${list.remark!}"><p style="margin:0 10px;width:200px;white-space:nowrap;overflow:hidden;text-overflow:ellipsis;">${list.remark!}</p></td>
						<td><#if (list.honorLevel!) != "08"><a href="javascript:updateExam('${list.id!}');" class="table-btn color-red">编辑</a>&nbsp;&nbsp;&nbsp;<a href="javascript:doDelete('${list.id!}');" class="table-btn color-red">删除</a></#if></td>
					</tr>
				</#list>
		</#if>
		</tbody>
	</table>		
</div>
<script>
	function doDelete(id){
	 	var stuId=$("#stuId").val();
	 	showConfirmMsg('确认删除荣誉信息？','提示',function(){
		$.ajax({
			url:'${request.contextPath}/studevelop/honorRecord/del',
			data: {id:id},
			type:'post',
			success:function(data) {
				var jsonO = JSON.parse(data);
		 		if(jsonO.success){
					layer.closeAll();
					layerTipMsg(jsonO.success,"删除成功",jsonO.msg);
					if(stuId==""){
						honorList();
					}else{
						changeStuId();
					}
		 		}
		 		else{
		 			layerTipMsg(jsonO.success,"删除失败",jsonO.msg);
				}
			},
	 		error:function(XMLHttpRequest, textStatus, errorThrown){}
		});
	  });
	}

	function updateExam(id){
		var ass = '?id='+id;
		var url='${request.contextPath}/studevelop/honorRecord/edit'+ass;
		indexDiv = layerDivUrl(url,{title: "编辑",width:750,height:500});
	} 	
</script>
