<div class="box box-structure">
<#if favoriteList?exists && favoriteList?size gt 0>
		<table class="tables">
		    <thead>
		        <tr>
		            <th>名称</th>
		            <th>类型</th>
		            <th>操作</th>
		        </tr>
		    </thead>
		    <tbody class="kanban-content">
		    	<#list favoriteList as favorite>
		    	<tr>
			        <td>${favorite.businessName!}</td>
			         <td>
							<#if favorite.businessType! =="1">数据图表<#elseif favorite.businessType! =="3">数据报表<#elseif favorite.businessType! =="5">多维报表<#elseif favorite.businessType! =="6">数据看板<#elseif favorite.businessType! =="7">数据报告<#else>未知</#if></td>
			         <td>
			               <a href="javascript:void(0)" onclick="showReportFromFavorite('${favorite.businessId!}','${favorite.businessType!}','${favorite.businessName!}')"  class="look-over">查看</a><span class="tables-line">|</span>
			                <a href="javascript:void(0)" class="remove" onclick="deleteFavorite('${favorite.id!}','${favorite.businessName!}');">取消收藏</a>
			        </td>
			  	</tr>
			  	</#list>
		    </tbody>
		</table>
<#else>
	<div class="no-data-common">
		<div class="text-center">
			<img src="${request.contextPath}/bigdata/v3/static/images/public/no-data-common-100.png"/>
			<p class="color-999">暂无收藏记录</p>
		</div>
	</div>
</#if>
</div>
<script>
   function showReportFromFavorite(id,type,name) {
		var url = '${request.contextPath}/bigdata/user/report/preview?businessType='+type+'&businessId='+ id+"&businessName="+name;
	 	window.open(url,id)
    }

	function deleteFavorite(id,name){
		showConfirmTips('prompt',"提示","您确定要取消收藏"+name+"吗？",function(){
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
				 			showLayerTips4Confirm('error',data.message);
				 		}else{
				 		    showLayerTips('success',data.message,'t');
         					$('.page-content').load('${request.contextPath}/bigdata/favorite/index'); 
		    			}
		          },
		          error:function(XMLHttpRequest, textStatus, errorThrown){}
		    });
		});
	}
</script>