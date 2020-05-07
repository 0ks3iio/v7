<#import  "/bigdata/v3/templates/bi/macro/biCommonWebMacro.ftl" as commonMacro />
<#if favoriteList?exists && favoriteList?size gt 0>
	<div class="table-made">
		<table class="tables">
			<thead>
			<th></th>
			<th>名称</th>
			<th>类型</th>
			<th>操作</th>
			<th></th>
			</thead>
			<tbody>
			<#list favoriteList as favorite>
				<tr>
					<td></td>
					<td>${favorite.businessName!}</td>
					<td>
						<#if favorite.businessType! =="1">数据图表<#elseif favorite.businessType! =="3">数据报表<#elseif favorite.businessType! =="5">多维报表<#elseif favorite.businessType! =="6">数据看板<#elseif favorite.businessType! =="7">数据报告<#else>未知</#if>
					</td>
					<td>
						<a href="javascript:void(0)" onclick="showReportFromFavorite('${favorite.businessId!}','${favorite.businessType!}','${favorite.businessName!}')"  class="look-over">查看</a><span class="tables-line">|</span>
						<span class="tables-line">|</span>
						<a href="javascript:void(0)" class="remove" onclick="deleteFavorite('${favorite.id!}','${favorite.businessName!}');">取消收藏</a>
					</td>
					<td></td>
				</tr>
			</#list>
			</tbody>
		</table>
		<@commonMacro.pageToolBar container="#businessContentDiv" class="bi-pagination"/>
	</div>
<#else>
	<div class="no-data-common">
		<div class="text-center">
			<img src="${request.contextPath}/bigdata/v3/static/images/bi/no-data-common.png"/>
			<p class="color-999">暂无收藏记录</p>
		</div>
	</div>
</#if>
<@commonMacro.biConfirmTip></@commonMacro.biConfirmTip>
<script>

	function showReportFromFavorite(id,type,name) {
		var url = '${request.contextPath}/bigdata/user/report/preview?businessType='+type+'&businessId='+ id+"&businessName="+name;
		window.open(url,id)
	}

	function deleteFavorite(id,name){
		showBIConfirmTips("提示","您确定要取消收藏"+name+"吗？",'390px','auto',function(){
			$.ajax({
				url:"${request.contextPath}/bigdata/favorite/delete",
				data:{
					'id':id
				},
				type:"post",
				clearForm : false,
				resetForm : false,
				dataType: "json",
				success:function(data){
					layer.closeAll();
					if(!data.success){
						showBIErrorTips("提示",data.message,'390px','auto');
					}else{
						$("#businessContentDiv").load('${springMacroRequestContext.contextPath}/bigdata/favorite/common/bi/index',function(){
							$("#businessType-91-num").html($("#businessType-91-num").text()-1)
							showBiTips(data.message,2000);
						});
					}
				},
				error:function(XMLHttpRequest, textStatus, errorThrown){}
			});
		});
	}
</script>