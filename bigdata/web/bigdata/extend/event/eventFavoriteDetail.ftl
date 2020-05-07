<div class="chart-header">
	<span><a href="javascript:void(0)" style="text-decoration:none;" onclick="go2EventPage('${favorite.id!}');" title="${favorite.favoriteName!}"><#if favorite.favoriteName! !="" && favorite.favoriteName?length gt 20>${favorite.favoriteName?substring(0, 20)}......<#else>${favorite.favoriteName!}</#if></a></span>
	<div class="box-tools dropdown">
        <a class="box-tools-btn" href="javascript:void(0);" data-toggle="dropdown" aria-expanded="false"><i class="wpfont icon-ellipsis"></i></a>
        <ul class="box-tools-menu dropdown-menu">
            <span class="box-tools-menu-angle"></span>
            <li><a class="js-box-middle <#if favorite.windowSize! ==1>active</#if>" href="javascript:void(0);" id="middle${favorite.id!}" onclick="changeWindowSize('${favorite.id!}','middle',1);">中</a></li>
            <li><a class="js-box-big <#if favorite.windowSize! ==2>active</#if>" href="javascript:void(0);" id="big${favorite.id!}" onclick="changeWindowSize('${favorite.id!}','big',2);">大</a></li>
            <li role="separator" class="divider"></li>
            <!--<li><a class="js-box-setting" href="javascript:void(0);" >设置</a></li>-->
            <li><a class="js-box-remove" href="javascript:void(0);"  onclick="deleteGroupFavoriteId('${favorite.id!}','${favorite.favoriteName!}');">删除</a></li>
        </ul>
    </div>
</div>
 <p class="color-999">
     <#if favorite.timeInfo?default('') != ''>
         ${favorite.timeInfo!}
     <#else>
         ${favorite.beginDate!}~${favorite.endDate!}
     </#if>
     <#if compareTimeIntervalList?exists && compareTimeIntervalList?size gt 0>
         <#list compareTimeIntervalList as item>
             &nbsp;对比&nbsp;${item!}
         </#list>
     </#if>
 </p>
<div class="chart-wrap" id="chart-div-${favorite.id!}"></div>
<div class="chart-wrap" id="no-chart-div-${favorite.id!}"></div>
<script type="text/javascript">
	function changeWindowSize(id,type,windowSize){
			$.ajax({
	            url:"${request.contextPath}/bigdata/event/dashboard/favoreite/changeWindowSize",
	            data:{
	              'id':id,
	              windowSize:windowSize
	            },
	            type:"post",
	            clearForm : false,
				resetForm : false,
	            dataType: "json",
	            async:true, 
	            success:function(response){
				 		if(!response.success){
	
				 		}else{
				 			var obj  = $('#'+type+id);
				 			if(type =='middle'){
								obj.addClass('active').parent().siblings('li').find('a').removeClass('active');
								obj.closest('.box').removeClass('big-chart-part').addClass('middle-chart-part');
								window["chart_div_"+id].resize();
							}else{
								
								obj.addClass('active').parent().siblings('li').find('a').removeClass('active');
								obj.closest('.box').removeClass('middle-chart-part').addClass('big-chart-part');
								window["chart_div_"+id].resize();
							}
		    			}
		          },
		          error:function(XMLHttpRequest, textStatus, errorThrown){
	
		          }
	   		 });
    	}
    	
    	$(document).ready(function(){
    		$('#no-chart-div-${favorite.id!}').hide();
            $('#chart-div-${favorite.id!}').show();
    	
    		var chart_div_name ="chart_div_${favorite.id!}"; 
	    	window[chart_div_name]=echarts.init(document.getElementById('chart-div-${favorite.id!}'));
	    	window[chart_div_name].showLoading({
			　   text: '数据正在努力加载...'
			});
    	
			$.ajax({
		            url:"${request.contextPath}/bigdata/event/getFavoriteChartOption",
		            data:{
		              'favoriteId':'${favorite.id!}'
		            },
		            type:"post",
		            clearForm : false,
					resetForm : false,
		            dataType: "json",
		            async:true, 
		            success:function(response){
				 		if(!response.success){
							window[chart_div_name].hideLoading();
							//layer.msg("${favorite.favoriteName!}开小差了,请稍后再试", {offset: 't',time: 2000});
						  $('#no-chart-div-${favorite.id!}').empty().html("<div class=\"wrap-1of1 centered no-data-state\">\n" +
                            "                        <div class=\"text-center\">\n" +
                            "                            <img src=\"${request.contextPath}/bigdata/v3/static/images/public/no-data-common-50.png\"/>\n" +
                            "                            <p>暂无数据</p>\n" +
                            "                        </div>\n" +
                            "                    </div>");
                    	$('#no-chart-div-${favorite.id!}').show();
                    	$('#chart-div-${favorite.id!}').hide();
				 		}else{
				 		    var data = JSON.parse(response.data);
							window[chart_div_name].setOption(data);
							window[chart_div_name].hideLoading();
		    			}
		          },
		          error:function(XMLHttpRequest, textStatus, errorThrown){
						window[chart_div_name].hideLoading();
		          }
		    });
			    
		});
</script>