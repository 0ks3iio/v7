<#import "/fw/macro/popupMacro.ftl" as popup />
<div class="box box-default" id="showList">
	<div class="box-body">
		<table class="table table-striped">
			<thead>
				<tr>
					<th>寝室楼</th>
					<th>人员</th>
					<th>操作</th>
				</tr>
			</thead>
			<tbody>
				<#if buildingList?exists && buildingList?size gt 0>
	      	  	  <#list buildingList as building>
	      	  	  	 <tr>
	      	  	  	 	<td>${building.name!}</td>
	      	  	  	 	<td> 
						<span id="userNames${building_index}">${building.userNames!}</span>
	      	  	  	 	</td>
	      	  	  	 	<td>
	      	  	  	 	<@popup.selectMoreUser  clickId="userName1${building_index}" handler='clickUserName("${building_index}")' id="userId${building_index}" name="userName${building_index}" recentDataUrl="${request.contextPath}/stuwork/dorm/checkRole/save?searchBuildId=${building.id!}&acadyear=${acadyear!}&semesterStr=${semesterStr!}">
						<a href="javascript:" id="userName1${building_index}" class="color-lightblue">修改</a>
						<input type="hidden" id="userId${building_index}"  value="${building.userIds!}"/>
						<input type="hidden" id="userName${building_index}"  value="${building.userNames!}"/>
						</@popup.selectMoreUser>
	      	  	  	 	</td>
	      	  	  	 </tr>
	      	  	  </#list>
	      	  <#else>
		          <tr >
		          	<td colspan="3" align="center">
		          		暂无寝室楼
		          	</td>
		          <tr>
	          </#if>
			</tbody>
		</table>
	</div>
</div>
<script>
	function clickUserName(index){
		layerTipMsg("success","保存成功","操作成功");
		$("#userNames"+index).html($("#userName"+index).val());
	}
</script>
