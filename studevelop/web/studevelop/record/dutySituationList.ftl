<title>任职情况登记List</title>
<#import "/fw/macro/htmlcomponent.ftl" as htmlcom>
<div class="table-wrapper">
	<table class="table table-bordered table-striped table-hover no-margin">
		<thead>
				<tr>
					<th style="width:10%">学生姓名</th>
					<th style="width:15%">开始时间</th>
					<th style="width:15%">结束时间</th>
					<th style="width:20%">所任职位</th>
					<th style="width:20%">工作内容</th>
					<th style="width:20%">操作</th>
				</tr>
		</thead>
		<tbody>
			<#if dutySituationList?exists && (dutySituationList?size > 0)>
				<#list dutySituationList as list>
					<tr>
						<td>${list.studentName!}</td>
						<td>${(list.openTime?string('yyyy-MM-dd'))?if_exists}</td>
						<td>${(list.endTime?string('yyyy-MM-dd'))?if_exists}</td>
						<td title="${list.dutyName!}"><p style="margin:0 10px;width:180px;white-space:nowrap;overflow:hidden;text-overflow:ellipsis;">${list.dutyName!}</p></td>
						<td title="${list.dutyContent!}"><p style="margin:0 10px;width:180px;white-space:nowrap;overflow:hidden;text-overflow:ellipsis;">${list.dutyContent!}</p></td>
						<td><a href="javascript:updateExam('${list.id!}');" class="table-btn color-red">编辑</a>&nbsp;&nbsp;&nbsp;<a href="javascript:doDelete('${list.id!}');" class="table-btn color-red">删除</a></td>
					</tr>
				</#list>
		</#if>
		</tbody>
	</table>		
</div>
<script>
	function doDelete(id){
	 	var stuId=$("#studentId").val();
	 	showConfirmMsg('确认删除任职情况信息？','提示',function(){
		$.ajax({
			url:'${request.contextPath}/studevelop/dutySituation/del',
			data: {id:id},
			type:'post',
			success:function(data) {
				var jsonO = JSON.parse(data);
		 		if(jsonO.success){
					layer.closeAll();
					layerTipMsg(jsonO.success,"删除成功",jsonO.msg);
					if(stuId==""){
						DutyList();
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
		var stuId=$("#studentId").val();
		var acadyear=$("#acadyear").val();
		var semester=$("#semester").val();
		var ass = '?acadyear='+acadyear+'&semester='+semester+'&stuId='+stuId+'&id='+id;
		var url='${request.contextPath}/studevelop/dutySituation/edit'+ass;
		indexDiv = layerDivUrl(url,{title: "编辑",width:750,height:350});
	}
</script>
