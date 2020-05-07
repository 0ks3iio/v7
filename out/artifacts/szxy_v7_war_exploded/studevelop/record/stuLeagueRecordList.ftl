<title>社团活动登记List</title>
<#import "/fw/macro/htmlcomponent.ftl" as htmlcom>
<div class="table-wrapper">
	<table class="table table-bordered table-striped table-hover no-margin">
		<thead>
				<tr>
					<th>学生姓名</th>
					<th>社团名称</th>
					<th>参加日期</th>
					<th>活动内容</th>
					<th>操作</th>
				</tr>
		</thead>
		<tbody>
			<#if LeagueRecordList?exists && (LeagueRecordList?size > 0)>
				<#list LeagueRecordList as list>
					<tr>
						<td>${list.studentName!}</td>
						<td>${list.leagueName!}</td>
						<td>${(list.joinDate?string('yyyy-MM-dd'))?if_exists}</td>
						<td title="${list.leagueContent!}"><p style="margin:0 10px;width:400px;white-space:nowrap;overflow:hidden;text-overflow:ellipsis;">${list.leagueContent!}</p></td>
						<td><a href="javascript:updateExam('${list.id!}');" class="table-btn color-red">编辑</a>&nbsp;&nbsp;&nbsp;<a href="javascript:doDelete('${list.id!}');" class="table-btn color-red">删除</a></td>
					</tr>
				</#list>
		</#if>
		</tbody>
	</table>		
</div>
<script>
	function doDelete(id){
	  var stuId=$("#stuId").val();
	  showConfirmMsg('确认删除社团活动信息？','提示',function(){
		$.ajax({
			url:'${request.contextPath}/studevelop/leagueRecord/del',
			data: {id:id},
			type:'post',
			success:function(data) {
				var jsonO = JSON.parse(data);
		 		if(jsonO.success){
		 			layer.closeAll();
					layerTipMsg(jsonO.success,"删除成功",jsonO.msg);
					if(stuId==""){
						LeagueList();
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
		var stuId=$("#stuId").val();
		var acadyear=$("#acadyear").val();
		var semester=$("#semester").val();
		var ass = '?acadyear='+acadyear+'&semester='+semester+'&stuId='+stuId+'&id='+id;
		var url='${request.contextPath}/studevelop/leagueRecord/edit'+ass;
		indexDiv = layerDivUrl(url,{title: "编辑",width:750,height:350});
	}
</script>