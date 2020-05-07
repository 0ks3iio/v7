<!DOCTYPE html>
<html>
<head>
	<meta charset="utf-8">
	<meta name="viewport" content="width=device-width,initial-scale=1,minimum-scale=1,maximum-scale=1,user-scalable=no" />
	<title>新高考选课</title>
	<link href="${request.contextPath}/static/mui/css/mui.min.css" rel="stylesheet"/>
	<link href="${request.contextPath}/static/mui/css/mui.icons-extra.css" rel="stylesheet">
	<link href="${request.contextPath}/static/mui/css/iconfont.css" rel="stylesheet">
	<link href="${request.contextPath}/static/mui/css/pages.css" rel="stylesheet"/>
</head>
<body>
	<#--
	<header class="mui-bar mui-bar-nav" style="height:0px;display:none;">
	    <a class="mui-btn-close">关闭</a>
	    <h1 class="mui-title">新高考选课</h1>
	</header>
	-->
	<a class="mui-btn-close mui-icon mui-icon-left-nav mui-pull-left" style="display:none;"></a>
	<div style="display:none;">
		<form id="_macro_form_id" method="post">
		</form>
	</div>
	<div class="mui-content" style="padding-top:0px;">
         <#list chDtos as dto>
         <div class="mui-style01 mui-card">
				<ul class="mui-table-view">
					<li class="mui-table-view-cell mui-media">
						<#if dto.timeState==0>
						<span class="mui-style01-tip bgColor-gray">未开始
						<#elseif dto.timeState==1>
						<span class="mui-style01-tip bgColor-orange">进行中
						<#elseif dto.timeState==2>
						<span class="mui-style01-tip bgColor-gray">已结束
						<#else>
						<span class="mui-style01-tip bgColor-green">已采用
						</#if>
						</span>
						<a href="javascript:;" class="" onclick="toChoice('${dto.choiceId!}',${dto.warning?string('true','false')})">
							<div class="mui-media-header">${dto.choiceName!}</div>
							<div class="mui-media-body">
								<p class="mui-choosectime">选课时间：</p>
						        <p class="mui-choosectime">${(dto.startTime?string('yyyy-MM-dd HH:mm'))?if_exists}至 ${(dto.endTime?string('yyyy-MM-dd HH:mm'))?if_exists} </p>
							</div>
						</a>
					</li>
				</ul>
		 </div>
		 </#list>
	</div>
	<script src="${request.contextPath}/static/mui/js/jquery-1.9.1.min.js"></script>
    <script src="${request.contextPath}/static/mui/js/mui.min.js"></script>
	<script src="${request.contextPath}/static/mui/js/weike.js"></script>
	<script src="${request.contextPath}/static/mui/js/myWeike.js" async="async" defer="defer"></script>
	<script src="${request.contextPath}/static/mui/js/common.js" async="async" defer="defer"></script>
</body>
<script type="text/javascript" charset="utf-8">
	mui.init();
	
function toChoice(chid, warn){
	if(warn){
		toastMsg('您被设置为不参与选课！如需进行选课，请联系教务管理员进行修改');
		return;
	}
	var url = '${request.contextPath}/mobile/open/newgkelective/${studentId!}/choice/page?choiceId='+chid;
	loadByHref(url);
}
</script>
</html>