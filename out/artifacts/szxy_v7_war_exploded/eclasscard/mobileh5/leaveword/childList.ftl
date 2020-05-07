<!DOCTYPE html>
<html>
	<head>
	    <meta charset="utf-8">
	    <meta name="viewport" content="width=device-width,initial-scale=1,minimum-scale=1,maximum-scale=1,user-scalable=no" />
	    <title>孩子留言</title>
	    <link href="${request.contextPath}/static/mui/css/mui.min.css" rel="stylesheet"/>
	    <link href="${request.contextPath}/static/eclasscard/mobileh5/leaveword/css/style.css" rel="stylesheet"/> 
	</head>
	<body>
	<#--form需要用div隐藏 否则在submit时  追加的input元素会短暂的显示在页面上-->
	<div style="display:none;">
		<form id="_macro_form_id" method="post">
		</form>
		<a class="mui-action-back mui-icon mui-icon-left-nav mui-pull-left"></a>
	</div>
		<div class="mui-content">
		    <ul class="mui-table-view no-margin">
		    <#if familyDto?exists&&familyDto?size gt 0>
		    <#list familyDto as family>
			    <li class="mui-table-view-cell mui-media">
			        <a href="javascript:;" class="grey" onclick="showDetail('${family.id!}')">
			        	<div class="mui-media-object mui-pull-left friend-pic">
			        		<img class="full" src="${family.picUrl!}">
			        		<#if family.unReadNum gt 0><span class="mui-badge mui-badge-danger message-num">${family.unReadNum!}</span></#if>
			        	</div>
			        	
			            <div class="mui-media-body">
			               	<span class="friend-name">${family.name!}</span>
			                <p class='mui-ellipsis last-message'>${family.lastWord!}</p>
			            </div>
			        </a>
			    </li>
		    </#list>
		    </#if>
			</ul>
		</div>
<script src="${request.contextPath}/static/mui/js/jquery-1.9.1.min.js"></script>
<script src="${request.contextPath}/static/mui/js/mui.min.js"></script>
<script src="${request.contextPath}/static/mui/js/weike.js"></script>
<script src="${request.contextPath}/static/mui/js/myWeike.js" async="async" defer="defer"></script>
<script src="${request.contextPath}/static/mui/js/common.js" async="async" defer="defer"></script>
<script type="text/javascript">
$(function () {
	mui.init();
	if (weikeJsBridge) {
		weikeJsBridge.windowClose(".mui-action-back");//绑定关闭事件
	}
});

function showDetail(studentId){
 	load("${request.contextPath}/mobile/open/eclasscard/leavae/word/detail?familyId=${senderId!}&studentId="+studentId);
}
//微课返回方法
function wkGoBack(){
	$(".mui-action-back").click();
}
</script>
	</body>
</html>