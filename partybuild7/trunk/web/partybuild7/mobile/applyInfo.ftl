<#import "/fw/macro/mobilecommon.ftl" as common />
<#assign aps = applyState?default(0) />
<#if auditState?default(0) == 1>
<#assign aps = aps-1 />
</#if>
<#assign tn = '积极分子考察' />
<#if aps == 1>
<#assign tn = '入党审批' />	
<#elseif aps == 2>
<#assign tn = '党员转正' />
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
<nav class="mui-bar mui-bar-tab mui-bottom-fixed">
	<#if mem.partyApplicationState?default(0) lt 1><a class="mui-footer-btn mui-color-blue w33" onclick="agree(-1);">退回</a></#if>
	<#if auditState?default(0) != 2><a class="mui-footer-btn mui-color-blue w33" onclick="agree(0);">继续审核</a></#if>
	<#if auditState?default(0) != 1><a class="mui-footer-btn mui-f-btn-blue w33" onclick="agree(1);">同意</a></#if>
</nav>
<div class="mui-content mui-conference" style="padding-top:0px; padding-bottom: 40px;">
    <div class="mui-approvalParty-info">
		<h4 class="mui-approvalParty-tit">${mem.teacherName!}（${mcodeSetting.getMcode("DM-XB",mem.sex?default(0)?string)}）</h4>
        <div class="mui-input-row">
	        <label>联系方式：</label>
	        <div class="mui-conference-info">${mem.phone!}</div>
	    </div>
    	<div class="mui-input-row">
	        <label>申请组织：</label>
	        <div class="mui-conference-info">${mem.orgName!}</div>
	    </div>
	    <div class="mui-input-row">
	        <label>申请内容：</label>
	        <div class="mui-conference-info mui-approvalParty-apply letter-break"><#if apply?exists>${apply.applicationContent!}</#if></div>
	    </div>
        <div class="mui-input-row mui-bg-row-grey">
            <span class="activist-tit">审核意见：</span>
        </div>
        <div class="mui-input-row mui-activist-info">
            <textarea  style="height: 150px;font-size: 15px;" name="remark" id="remark" class="mui-input-clear" maxLength="100" placeholder="请输入审核意见信息...">${mem.showRemark!'同意'}</textarea>
        </div>
	</div>
</div>
<script type="text/javascript" >
$("#cancelId").click(function(){
	//window.history.back(-1);
	var tid=storage.get('current.teacher.id');
	load('${request.contextPath}/mobile/open/partybuild7/memberList/index?teacherId='+tid+'&applyState=${aps}&auditState=${auditState?default(0)}');
});

var isSubmit=false;
function agree(pass){
	if(isSubmit){
		return;
	}
	isSubmit=true;
	
	var remark = $("#remark").val();
	if(pass !='1' && !isNotBlank(remark)){
		toastMsg('审核意见 不能为空！');
		isSubmit=false;
		return;
	}
	if(getLength(remark) >100){
		toastMsg('审核意见 超出最大长度限制100个字符！');
		isSubmit=false;
		return;
	}
	var applyState= new Number('${applyState!}') + pass;
	var isDeny="";
	if(pass == 1){
	    isDeny = "0";
	}else{
	    isDeny = "1"
	    <#if auditState?default(1)==1>
	    	applyState--;
	    </#if>
	}
	
	var options={
	    url:"${request.contextPath}/mobile/open/partybuild7/memberList/audit",
		data:{"applyState":applyState,"isDeny":isDeny,"remark":remark,"ids":'${mem.id!}'},
		type:"post",
		dataType:"json",
            success:function(data){
                var jsonO = data;
                if(!jsonO.success){
                    isSubmit=false;
                    toastMsg(jsonO.msg);
                    return;
                }else{
					$("#cancelId").click();

                }

            },
            clearForm : false,
            resetForm : false,
            error:function(XMLHttpRequest ,textStatus,errorThrown){}
	}
	$.ajax(options);
}
</script>
</@common.moduleDiv>