<#import "/fw/macro/mobilecommon.ftl" as common>
<#import "/studevelop/mobile/commonStuDevelop.ftl" as stuDevelop>
<#assign title="校外表现">
<#if type?default(1) == 2>
	<#assign title="假期生活">
</#if>
<@common.moduleDiv titleName="${title!}">
<link href="${request.contextPath}/studevelop/mobile/css/style.css" rel="stylesheet"/>
<a href="javascript:void(0);" onclick="doAdd();" class="mui-fixed-edit"></a>
<@stuDevelop.acadyearSemester stuDevelopUrl="${request.contextPath}/mobile/open/studevelop/outside/index?" params="type=${type!}&studentId=${studentId!}" >

</@stuDevelop.acadyearSemester>

<div class="mui-content">
	<#if list?exists && (list?size>0)>
		<div class="mui-album">
			<#list list as item>
				<a href="javascript:void(0);" onclick="doEdit('${item.id!}');" class="mui-album-item">
					<#if item.imagePath?exists && ""!=item.imagePath>
					<span class="mui-album-body">
						<span class="mui-album-num">${item.imageCount!}</span>
						<img src="${item.imagePath!}" class="mui-album-img"/>
					</span>
					</#if>
					<span class="mui-album-footer">${item.title!}</span>
				</a>
			</#list>
		</div>
	<#else>	
		<div class="mui-noRecord">
			<i></i><br />暂无记录
		</div>
	</#if>
</div>

<script type="text/javascript" charset="utf-8">
  	mui.init();
	window.onload=function(){
		var backType = storage.get("backType");
		if("outside_edit" == backType){//如果是编辑页返回
			location.reload();
			storage.remove("backType");
		}
	}
</script>
<script type="text/javascript">
  	function doAdd(){
	  	var str ="?studentId=${studentId!}&acadyear=${acadyear!}&semester=${semester!}&type=${type!}";
  		load("${request.contextPath}/mobile/open/studevelop/outside/add"+str);
  	}
  	
  	function doEdit(id){
  		load("${request.contextPath}/mobile/open/studevelop/outside/add?id="+id);
  	}
</script>
<script type="text/javascript">
<#if !(list?size>0)>
	$(function(){
		var window_h = $(window).height();
		var header_h = $('header').height();
		var content_h = window_h - header_h;
		$('.mui-noRecord').height(content_h);
	});
</#if>
</script>
</@common.moduleDiv>