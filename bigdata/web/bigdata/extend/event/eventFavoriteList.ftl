<div class="box overview-content mt-20" >
	<div class="btn-group filter fn-left dropdown-btn-button">
	  	<button type="button" class="btn btn-blue dropdown-toggle" onclick="showDropdownMeun();" >+添加 </button>
	  	<ul class="dropdown-menu" >
	  	   	<li class="next-btn pointer js-next" ><a href="javascript:void(0);" onclick="loadFavoriteMenuList();">从事件库添加<span class="wpfont icon-angle-single-right"></span></a></li>
		    <li role="separator" class="divider"></li>
		    <li><a href="javascript:void(0);" onclick="go2EventPage();">新建事件组件</a></li>
	  	</ul>
	</div>
</div>
<div class="box chart-content clearfix" id="eventChartDiv">
<#if favoriteList?exists&&favoriteList?size gt 0>
	<#list favoriteList as favorite>
			<div  id="chart-${favorite.id!}" class="box box-default chart-part <#if favorite.windowSize! == 1>middle-chart-part<#else>big-chart-part</#if>"></div>
	</#list>
<#else>
<div class="wrap-1of1 centered no-data-state js-height" >
	<div class="text-center">
		<img src="${request.contextPath}/static/bigdata/images/card-no-data.png"/>
		<p class="no-data-explain color-999">当前组还未添加事件组件，你可以点击左上角<a class="bold js-btn" href="javascript:void(0)" onclick="showDropdownMeun();">"+添加"</a>添加事件</p>
	</div>
</div>
 </#if>
</div>
<div class="layer layer-choose-goal wrap-1of1 no-padding over-auto" id="favoriteMenuListId"></div>
<script type="text/javascript">
	var menuDivIndex;
	function go2EventPage(favoriteId){
		if(!favoriteId)
			favoriteId="";
		router.go({
	         path: '/bigdata/event/query?favoriteId='+favoriteId,
	        name:'事件分析',
	        level: 1
	    }, function () {
	   		 $('#20000000000000000000000000000084 a').addClass('active').parent().siblings('li').find('a').removeClass('active');
		 	$('.page-content').load('${request.contextPath}/bigdata/event/query?favoriteId='+favoriteId);
	    });
	}
	
	function deleteGroupFavoriteId(favoriteId,name){
		 showConfirmTips('prompt',"提示","您确定要删除"+name+"吗？",function(){
			 $.ajax({
		            url:"${request.contextPath}/bigdata/event/dashboard/group/favorite/delete",
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
		    			}
		          },
		          error:function(XMLHttpRequest, textStatus, errorThrown){}
		    });
		},function(){
	
		});
	}
	
	function loadFavoriteMenuList(){
		 menuDivIndex =layer.open({
    		type: 1,
            shade: .6,
            title: '选择事件',
            btn: false,
            area: ['370px','400px'],
            content: $('.layer-choose-goal')
    	});
		$('.dropdown-btn-button').removeClass('open');
		$("#favoriteMenuListId").load("${request.contextPath}/bigdata/event/dashboard/favorite/menuList?groupId=${groupId!}");
		return false;
	}
	function showDropdownMeun(){
		$('.dropdown-btn-button').addClass('open');
		$('dropdown-btn-button button').attr('aria-expanded',true);
		return false;
	}
	
	document.addEventListener('click',function( e ){
	  	var dropdownMenuDiv = $('.dropdown-btn-button');
		if( e.target !== dropdownMenuDiv ){
		    $('.dropdown-btn-button').removeClass('open');
		}
	},true)
	
	$(document).ready(function(){
		<#if favoriteList?exists&&favoriteList?size gt 0>
			<#list favoriteList as favorite>
					  $("#chart-${favorite.id!}").load("${request.contextPath}/bigdata/event/dashboard/favorite/detail?favoriteId=${favorite.id!}");		
			</#list>
		<#else>
			function height(){
	            $('.js-height').each(function(){
	                $(this).css({
	                    height: $(window).height() - $(this).offset().top - 200,
	                });
	            });
	        }
	        height();
		</#if>
	});
</script>