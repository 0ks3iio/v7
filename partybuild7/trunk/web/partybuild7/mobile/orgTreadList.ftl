
<#import "/fw/macro/mobilecommon.ftl" as common />

<@common.moduleDiv titleName="支部动态">
<link href="${request.contextPath}/partybuild7/mobile/css/style.css" rel="stylesheet"/>
<header class="mui-bar mui-bar-nav" style="display:none;">
    <a class="mui-action-back mui-icon mui-icon-left-nav mui-pull-left" id="cancelId"><span>返回</span></a>
    <a class="mui-btn-close">关闭</a>
    <h1 class="mui-title">支部动态</h1>
</header>
<div class="mui-content" style="padding-top:0px;">
    <ul class="mui-table-view mui-table-view-chevron">
        <#if orgTrendList?exists && (orgTrendList?size gt 0) >
            <#list orgTrendList as orgTrend>
                <li class="mui-table-view-cell mui-media" onclick="doShow('${orgTrend.id!}')">
                    <a class="mui-navigate-right">
                        <div class="mui-media-body">
                            <h5 class="mui-ellipsis">${orgTrend.topic!}</h5>
                            <p class="mui-ellipsis"><span class="mui-icon"><i class="mui-icon-clock"></i></span>${((orgTrend.trendsDate)?string('yyyy/MM/dd'))?default('')}</p>
                        </div>
                    </a>
                </li>
            </#list>
        </#if>



    </ul>
</div>
<script type="text/javascript" >
    function doShow(id) {
        load("${request.contextPath}/mobile/open/partybuild7/orgTrend/showInfo?id="+id);
    }

$("#cancelId").click(function(){
	load("${request.contextPath}/mobile/open/partybuild7/homepage?teacherId=${teacherId!}");
});    
</script>
</@common.moduleDiv>