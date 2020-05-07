<#import "/fw/macro/mobilecommon.ftl" as common />
<#assign tn = '党员活动记录' />
<#if act.activityLevel?default(1) == 2>
<#assign tn = '领导活动记录' />	
</#if>
<@common.moduleDiv titleName=tn >
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
    <h1 class="mui-title">${tn!}</h1>
</header>
<div class="mui-content mui-conference" style="padding-top:0px;">
    <h4>${act.name!}</h4>
    <div class="mui-input-row">
        <label>开始时间：</label>
        <div class="mui-conference-info">${(act.activityStartDate?string('yyyy/MM/dd'))?default('')}</div>
    </div>
    <div class="mui-input-row">
        <label>结束时间：</label>
        <div class="mui-conference-info">${(act.activityEndDate?string('yyyy/MM/dd'))?default('')}</div>
    </div>
    <div class="mui-input-row">
        <label>活动地点：</label>
        <div class="mui-conference-info letter-break">${act.activityPlace!}</div>
    </div>
    <div class="mui-input-row">
        <label>会议人员：</label>
        <div class="mui-conference-info letter-break">${act.memberNames!}</div>
    </div>
    <div class="mui-input-row">
        <label>活动内容：</label>
        <div class="mui-conference-info letter-break">${act.content!}</div>
    </div>
    <div class="mui-input-row">
        <label>活动备注：</label>
        <div class="mui-conference-info letter-break">${act.remark!}</div>
    </div>
</div>
<script type="text/javascript" >
$("#cancelId").click(function(){
	window.history.back(-1);
});
</script>
</@common.moduleDiv>