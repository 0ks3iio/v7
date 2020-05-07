<!doctype html>
<html lang="en">
<head>
    <meta charset="UTF-8" />
    <title>调代管理</title>
    <meta name="viewport" content="width=device-width,initial-scale=1,minimum-scale=1,maximum-scale=1,user-scalable=no" />
    <link href="${request.contextPath}/static/mui/css/mui.min.css" rel="stylesheet"/>
    <link href="${request.contextPath}/static/mui/css/pages.css" rel="stylesheet"/>
	<script src="${request.contextPath}/static/mui/js/jquery-1.9.1.min.js"></script>
	<script src="${request.contextPath}/static/mui/js/mui.min.js" type="text/javascript" charset="utf-8"></script>
	<script src="${request.contextPath}/static/mui/js/weike.js"></script>
	<script src="${request.contextPath}/static/mui/js/myWeike.js"></script>
	<script src="${request.contextPath}/static/mui/js/common.js"></script>
	
</head>
<body>
	<#--
	    <header class="mui-bar mui-bar-nav"  style="display:none;">
	        <a class="mui-btn-close mui-icon mui-icon-left-nav mui-pull-left"></a>
	        <h1 class="mui-title">调代管理</h1>
	    </header>
	-->
    <a class="mui-btn-close mui-icon mui-icon-left-nav mui-pull-left" style="display:none;"></a>
    <div class="mui-content">
    	<div class="mui-card manage-list bgColor-blue model_3">
        	<div class="mui-card-header">
        		<span class="f-16">调课管理</span>
        		<#if showMap['3'][2]?default('')=='1'>
        			<span class="prompt">最近一条审核通过</span>
        		<#elseif showMap['3'][2]?default('')=='1'>
        			<span class="prompt">最近一条审核通过</span>
        		</#if>
        	</div>
        	<div class="mui-card-footer">
        		<span>
        			<span class="mr-20"><span class="f-20 mr-5">${showMap['3'][0]}</span><#if adminType?default('')=='1'>待审核<#else>审核中</#if></span>
        			<span class="mr-20"><span class="f-20 mr-5">${showMap['3'][1]}</span><#if adminType?default('')=='1'>本月</#if>已审核</span>
        		</span>
        		<span class="mui-icon mui-icon-arrowright"></span>
        	</div>
        </div>
    	<div class="mui-card manage-list bgColor-green model_1">
        	<div class="mui-card-header">
        		<span class="f-16">代课管理</span>
        		<#if showMap['1'][2]?default('')=='1'>
        			<span class="prompt">最近一条审核通过</span>
        		<#elseif showMap['1'][2]?default('')=='1'>
        			<span class="prompt">最近一条审核通过</span>
        		</#if>
        	</div>
        	<div class="mui-card-footer">
        		<span>
        			<span class="mr-20"><span class="f-20 mr-5">${showMap['1'][0]}</span><#if adminType?default('')=='1'>待审核<#else>审核中</#if></span>
        			<span class="mr-20"><span class="f-20 mr-5">${showMap['1'][1]}</span><#if adminType?default('')=='1'>本月</#if>已审核</span>
        		</span>
        		<span class="mui-icon mui-icon-arrowright"></span>
        	</div>
        </div>
    	<div class="mui-card manage-list bgColor-orange model_2">
        	<div class="mui-card-header">
        		<span class="f-16">管课管理</span>
        		<#if showMap['2'][2]?default('')=='1'>
        			<span class="prompt">最近一条审核通过</span>
        		<#elseif showMap['2'][2]?default('')=='1'>
        			<span class="prompt">最近一条审核通过</span>
        		</#if>
        	</div>
        	<div class="mui-card-footer">
        		<span>
        			<span class="mr-20"><span class="f-20 mr-5">${showMap['2'][0]}</span><#if adminType?default('')=='1'>待审核<#else>审核中</#if></span>
        			<span class="mr-20"><span class="f-20 mr-5">${showMap['2'][1]}</span><#if adminType?default('')=='1'>本月</#if>已审核</span>
        		</span>
        		<span class="mui-icon mui-icon-arrowright"></span>
        	</div>
        </div>
    </div>
</body>
<script type="text/javascript" charset="utf-8">
	 $(function() {
		$(".model_3").on("click",function(){
			findByType("3");
		});
		$(".model_1").on("click",function(){
			findByType("1");
		});
		$(".model_2").on("click",function(){
			findByType("2");
		});
	})
	function findByType(type){
		var url = '${request.contextPath}/mobile/open/adjusttipsay/showList/page?unitId=${unitId!}&userId=${userId!}&teacherId=${teacherId!}&adminType=${adminType!}&type='+type;
		loadByHref(url);
	}
</script>
</html>
