<#import "/fw/macro/htmlcomponent.ftl" as htmlcom>
<form id="mannReForm">
    <#if tabType =='1'>
	<table class="table table-bordered table-striped table-hover no-margin">
		<thead>
				<tr>
					<th>姓名</th>
					<th>奖励名称</th>
					<th>奖励级别</th>
					<th>奖励日期</th>
                    <th>获奖名次</th>
                    <th>颁奖单位</th>
                    <th>操作</th>
				</tr>
		</thead>
		<tbody>
			<#if stuDevelopRewardsList?exists && (stuDevelopRewardsList?size > 0)>
				<#list stuDevelopRewardsList as item>
					<tr>
						<td>${item.stuName!}</td>
						<td>${item.rewardsname!}</td>
						<td>${mcodeSetting.getMcode("DM-JLJB", item.rewardslevel?default("1"))}</td>
						<td>${(item.rewardsdate?string('yyyy-MM-dd'))}</td>
						<td>${item.rewardPosition!}</td>
						<td>${item.rewardsunit!}</td>
						<td>
				           <a href="javascript:doRewardsEdit('${item.id!}');" class="table-btn color-red">编辑</a>
				           <a href="javascript:doRewardsDelete('${item.id!}');" class="table-btn color-red">删除</a>
			            </td>					
					</tr>
				</#list>
			</#if>
		</tbody>
	</table>
	    <#if stuDevelopRewardsList?exists && (stuDevelopRewardsList?size > 0)>
           <@htmlcom.pageToolBar container="#showList"/>
        </#if>
	<#else>
	<table class="table table-bordered table-striped table-hover no-margin">
		<thead>
				<tr>
					<th>姓名</th>
					<th>惩处名称</th>
					<th>惩处原因</th>
					<th>惩处类型</th>
                    <th>惩处日期</th>
                    <th>撤销日期</th>
                    <th>操作</th>
				</tr>
		</thead>
		<tbody>
			<#if stuDevelopPunishmentList?exists && (stuDevelopPunishmentList?size > 0)>
				<#list stuDevelopPunishmentList as item>
					<tr>
						<td>${item.stuName!}</td>
                        <td>${item.punishname!}</td>
                        <td title="${item.punishreason!}"><p style="margin:0 10px;width:200px;white-space:nowrap;overflow:hidden;text-overflow:ellipsis;">${item.punishreason!}</p></td>
						<td>${mcodeSetting.getMcode("DM-CFMC",item.punishType?default('1'))}</td>
						<td>${(item.punishdate?string('yyyy-MM-dd'))!}</td>
						<td>${(item.canceldate?string('yyyy-MM-dd'))!}</td>
						<td>
				           <a href="javascript:doPunishEdit('${item.id!}');" class="table-btn color-red">编辑</a>
				           <a href="javascript:doPunishDelete('${item.id!}');" class="table-btn color-red">删除</a>
			            </td>					
					</tr>
				</#list>
			</#if>
		</tbody>
	</table>
	    <#if stuDevelopPunishmentList?exists && (stuDevelopPunishmentList?size > 0)>
          <@htmlcom.pageToolBar container="#showList"/>
        </#if>
	</#if>
</form>		
<script>
function doRewardsEdit(id){
	var str = "?id="+id;
	var url = "${request.contextPath}/studevelop/rewardsRecord/rewardsEdit"+str;
	indexDiv = layerDivUrl(url,{title: "奖励信息",width:750,height:490});
}

function doPunishEdit(id){
	var str = "?id="+id;
	var url = "${request.contextPath}/studevelop/rewardsRecord/punishEdit"+str;
	indexDiv = layerDivUrl(url,{title: "惩处信息",width:750,height:630});
}

var isSubmit=false;
function doRewardsDelete(id){
     showConfirmMsg('确认删除？','提示',function(){
     var ii = layer.load();
     $.ajax({
			url:'${request.contextPath}/studevelop/rewardsRecord/rewardsDelete',
			data: {'id':id},
			type:'post',
			success:function(data) {
				var jsonO = JSON.parse(data);
		 		if(jsonO.success){
                    layer.closeAll();
					layerTipMsg(jsonO.success,"删除成功",jsonO.msg);
				  	searchList();
		 		}
		 		else{
		 			layerTipMsg(jsonO.success,"删除失败",jsonO.msg);
		 			//$("#arrange-commit").removeClass("disabled");
		 			isSubmit=false;
				}
				layer.close(ii);
			},
	 		error:function(XMLHttpRequest, textStatus, errorThrown){}
		});
		});
}

var isSubmit=false;
function doPunishDelete(id){
     showConfirmMsg('确认删除？','提示',function(){
     var ii = layer.load();
     $.ajax({
			url:'${request.contextPath}/studevelop/rewardsRecord/punishDelete',
			data: {'id':id},
			type:'post',
			success:function(data) {
				var jsonO = JSON.parse(data);
		 		if(jsonO.success){
                    layer.closeAll();
					layerTipMsg(jsonO.success,"删除成功",jsonO.msg);
				  	searchList();
		 		}
		 		else{
		 			layerTipMsg(jsonO.success,"删除失败",jsonO.msg);
		 			//$("#arrange-commit").removeClass("disabled");
		 			isSubmit=false;
				}
				layer.close(ii);
			},
	 		error:function(XMLHttpRequest, textStatus, errorThrown){}
		});
		});
}
</script>