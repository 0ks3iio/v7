<#import "/fw/macro/htmlcomponent.ftl" as htmlcom>
<#if carResourceList?exists&&carResourceList?size gt 0>
<div class="tab-pane active">
	<div class="resourceList">
        <#list carResourceList as list>
        <div class="media" onClick="<#if list.linkUrl?exists>showlinkVideo('${list.linkUrl!}')<#else>showResource('${list.id!}')</#if>">
            <div class="media-left">
                <a href="#"><img width="178" height="108" src="${request.contextPath}${list.pictureUrl!}" class="media-object"></a>
            </div>
            <div class="media-body">
                <h4 class="media-heading"><a href="#"><b>${list.title!}</b></a></h4>
                <div class="infor"><a href="#" title="${list.description!}">${list.description!}</a></div>
                <div class="clearfix">
                	<div class="pull-left">
                	<#if list.type == 1>
                		<span class="variety">视频</span>
                	<#elseif list.type == 2>
                		<span class="variety">资讯</span>
                	<#else>
                		<span class="variety">视频</span>
                		<span class="variety">资讯</span>
                	</#if>
                	<span class="color-999">${(list.creationTime?string('yyyy-MM-dd'))!}</span></div>
                	<div class="pull-right color-blue">
                	<#if list.resourceType == 1>	
                		志愿填报指导
                	<#elseif list.resourceType == 2>
                		选科指导
                	<#else>
                		自主招生指导
                	</#if>
                	</div>
                </div>
            </div>
        </div>
        </#list>
    </div>
</div>
<@htmlcom.pageToolBar container="#showResourceListDiv" class="noprint"/>
<#else>
<div class="no-data-container" style="padding-top:10%">
	<div class="no-data">
		<span class="no-data-img">
			<img src="${request.contextPath}/static/images/growth-manual/no-img.png" alt="">
		</span>
		<div class="no-data-body">
			<h3>暂无内容</h3>
		</div>
	</div>
</div>
</#if>
<script>
	function showResource(id) {
		var url = "${request.contextPath}/career/resourcecenter/indexpage/resourceindex?resourceId="+id;
		$("#showResourceDiv").load(url);
		$("#showResourceTabDiv").attr("style","display:none");
		$("#showResourceDiv").attr("style","display:block");
	}
	
	function showlinkVideo(linkUrl) {
		window.open(linkUrl);
	}
</script>