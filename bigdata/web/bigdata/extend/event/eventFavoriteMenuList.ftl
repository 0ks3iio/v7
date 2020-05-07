<ul class="dropdown-made">
<#if favoriteList?exists&&favoriteList?size gt 0>
<#list favoriteList as favorite>
<li>
	<a href="javacript:void(0);" onclick="addFavorite2Group('${favorite.id!}','${favorite.windowSize!}')">
    	<label class="pos-rel js-kind" >
			<span class="lbl">${favorite.favoriteName!}</span>
			<p class="color-999">${favorite.timeInfo!}</p>
		</label>
	</a>
</li>
</#list>
<#else>
<li><a href="javacript:void(0);" ><label class="pos-rel js-kind" ><span class="lbl">无可用的组件</span></label></a></li>
 </#if>
</ul>
<script type="text/javascript">
		function addFavorite2Group(favoriteId,windowSize){
			 $.ajax({
			            url:"${request.contextPath}/bigdata/event/dashboard/group/favorite/add",
			            data:{
			              'groupId':'${groupId!}',
			              'favoriteId':favoriteId
			            },
			            type:"post",
			            clearForm : false,
						resetForm : false,
			            dataType: "json",
			            success:function(data){
			            	layer.closeAll();
					 		if(!data.success){
					 			showLayerTips('error',data.msg,'t');
					 		}else{
					 		     showLayerTips('success',data.msg,'t');   
			 					 loadFavoriteData('${groupId!}');
								//$('.no-data-state').hide();
								layer.close(menuDivIndex);
			    			}
			          },
			          error:function(XMLHttpRequest, textStatus, errorThrown){}
			    });
		}
		
		function addFavoriteData(favoriteId,windowSize){
			var chartSizeCss='big-chart-part';
			if(windowSize =='1')
				chartSizeCss='middle-chart-part';
			var str = '<div id="chart-'+favoriteId+'" class="box box-default chart-part '+chartSizeCss+'"></div>';
		    $("#eventChartDiv").append(str);
		    $("#chart-"+favoriteId).load("${request.contextPath}/bigdata/event/dashboard/favorite/detail?groupId=${groupId!}&favoriteId="+favoriteId);		
		}
</script>