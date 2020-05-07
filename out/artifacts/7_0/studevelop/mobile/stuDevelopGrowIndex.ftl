<!DOCTYPE html>
<html>
<head>
	<meta charset="utf-8">
	<meta name="viewport" content="width=device-width,initial-scale=1,minimum-scale=1,maximum-scale=1,user-scalable=no" />
	<meta name="apple-mobile-web-app-capable" content="yes">
	<meta name="apple-touch-fullscreen" content="yes">
	<meta name="format-detection" content="telephone=no,email=no">
	<meta name="ML-Config" content="fullscreen=yes,preventMove=no">
	<title>手册列表</title>
	<link href="${request.contextPath}/static/mui/css/mui.css" rel="stylesheet"/>
	<link href="${request.contextPath}/static/mui/css/mui.icons-extra.css" rel="stylesheet"/>
	<link href="${request.contextPath}/studevelop/mobile/css/style.css" rel="stylesheet"/>
</head>

<body>
	<div style="display:none;">
		<form id="_macro_form_id" method="post">
		</form>
	</div>
	<div class="mui-content">
		<div class="manual-list-wrap">
			<ul class="manual-list">
				<#if numberList?exists && numberList?size gt 0>
					<#list numberList as item>
						<li <#if item_index==0>class="current"</#if>>
							<h4 class="manual-step"><span><em>${item!}年级</em></span>遇见最好的自己</h4>
							<#assign stuIntrList=stuIntroMap[item]>
							<#if stuIntrList?exists &&  stuIntrList?size gt 0>
							<#list  stuIntrList as stuIntr>
								<#if item_index==0 && stuIntr_index==0 >
									<#if  stuIntr.semester=="2">
									<a class="manual-object manual-object-blue" href="javascript:;" onclick="toShow('${stuIntr.acadyear!}','${stuIntr.semester!}');"><span>${sectionName!}${item!}年级<small>第二学期</small></span></a>
									<#else>
									<a class="manual-object manual-object-latest" href="javascript:;"><img src="${request.contextPath}/studevelop/mobile/images/temp/avatar.png" alt=""></a>
									<a class="manual-object manual-object-blue" href="javascript:;" onclick="toShow('${stuIntr.acadyear!}','${stuIntr.semester!}');"><span>${sectionName!}${item!}年级<small>第一学期</small></span></a>
									</#if>
								<#else>
								<a class="manual-object manual-object-<#if stuIntr.semester=='2'>blue<#else>yellow</#if>" href="javascript:;" onclick="toShow('${stuIntr.acadyear!}','${stuIntr.semester!}');"><span>${sectionName!}${item!}年级<small>第<#if stuIntr.semester=="2">二<#else>一</#if>学期</small></span></a>
								</#if>
							</#list>
							</#if>
						</li>
					</#list>
				</#if>
			</ul>
		</div>
	</div>
	
	<script src="${request.contextPath}/static/mui/js/jquery-1.9.1.min.js"></script>
	<script src="${request.contextPath}/static/mui/js/jquery.form.js"></script>
	<script src="${request.contextPath}/static/mui/js/mui.min.js"></script>
	<script src="${request.contextPath}/static/mui/js/mui.zoom.js"></script>
	<script src="${request.contextPath}/static/mui/js/mui.previewimage.js"></script>
	<script src="${request.contextPath}/static/mui/js/img.ratio.js"></script>
	<script src="${request.contextPath}/static/mui/js/mui.picker.js"></script>
	<script src="${request.contextPath}/static/mui/js/mui.poppicker.js"></script>

	<script src="${request.contextPath}/static/mui/js/storage.js"></script>
	<script src="${request.contextPath}/static/mui/js/common.js"></script>
	<script src="${request.contextPath}/static/mui/js/weike.js"></script>
	<script src="${request.contextPath}/static/mui/js/myWeike.js"></script>
	<script type="text/javascript" charset="utf-8">
	  	mui.init({
	  		gestureConfig:{
  				doubletap: true,
  				longtap: true
  			}
	  	});
	  	mui.previewImage();
	  	mui.toast('成长手册图片较多，请尽量wifi情况下浏览');
	  	function toShow(acadyear,semester){
	  		load("${request.contextPath}/mobile/open/studevelop/handbook/show?studentId=${studentId!}&acadyear="+acadyear+"&semester="+semester);
	  	}
	</script>
</body>
</html>