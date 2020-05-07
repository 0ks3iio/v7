<#import "/fw/macro/mobilecommon.ftl" as common />

<@common.moduleDiv titleName="支部动态">
<link href="${request.contextPath}/partybuild7/mobile/css/style.css" rel="stylesheet"/>
<style type="text/css">
.letter-break{
	word-break:break-all;
}
</style>
<body>
<header class="mui-bar mui-bar-nav" style="display:none;">
    <a class="mui-action-back mui-icon mui-icon-left-nav mui-pull-left" id="cancelId"><span>返回</span></a>
    <a class="mui-btn-close">关闭</a>
    <h1 class="mui-title">支部动态</h1>
</header>
<div class="mui-content mui-conference" style="padding-top:0px;">
	<h4>${orgTrend.topic!}</h4>
    <div class="mui-input-row">
        <label>时间：</label>
        <div class="mui-conference-info">${(orgTrend.trendsDate?string('yyyy/MM/dd'))?default('')}</div>
    </div>
    <div class="mui-input-row">
        <label>正文：</label>
        <div class="mui-conference-info letter-break">${orgTrend.trendsContent!}</div>
    </div>
    <div class="mui-input-row">
        <label>备注：</label>
        <div class="mui-conference-info letter-break">${orgTrend.remark!}</div>
    </div>
    <div class="mui-input-row">
        <label>台账分类：</label>
        <div class="mui-conference-info">${orgTrend.runningAccountTypeStr!}</div>
    </div>

    <div class="mui-input-row">
        <label>附件下载：</label>
        <div class="mui-conference-info">
            <#if attachments?exists && (attachments?size gt 0) >
                <#list  attachments as attachment>
                    <a href="#" onclick="downloadFile('${attachment.id!}')" class="letter-break">${attachment.fileName!}</a></br>
                </#list>
            </#if>
        </div>
    </div>
</div>

<script type="text/javascript" >
    function downloadFile(id) {
        location.href="${request.contextPath}/mobile/open/attachment/downloadAttachment?attachmentId="+id+"&isExternalLink=1";
    }

$("#cancelId").click(function(){
	window.history.back(-1);
});
</script>
</@common.moduleDiv>
