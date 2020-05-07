<#import "/fw/macro/mobilecommon.ftl" as common />
<#assign PARTY_STATE_DENY =stack.findValue("@net.zdsoft.partybuild7.data.constant.PartyBuildConstant@PARTY_STATE_DENY")>
<#assign PARTY_STATE_NOT =stack.findValue("@net.zdsoft.partybuild7.data.constant.PartyBuildConstant@PARTY_STATE_NOT")>
<#assign PARTY_STATE_ACTIVIST =stack.findValue("@net.zdsoft.partybuild7.data.constant.PartyBuildConstant@PARTY_STATE_ACTIVIST")>
<#assign MEMBER_AUDIT_STATE_ING =stack.findValue("@net.zdsoft.partybuild7.data.constant.PartyBuildConstant@MEMBER_AUDIT_STATE_ING")>
<#assign MEMBER_AUDIT_STATE_PASS =stack.findValue("@net.zdsoft.partybuild7.data.constant.PartyBuildConstant@MEMBER_AUDIT_STATE_PASS")>


<#if partyMember.partyApplicationState == PARTY_STATE_NOT && partyMember.isDeny ==  MEMBER_AUDIT_STATE_ING >
    <#assign activistClass = "mui-page-awaiting" >
    <#assign  state = "等待审核" >

<#elseif partyMember.partyApplicationState == -1  && partyMember.isDeny  ==  1 >
    <#assign activistClass = "mui-page-failure" >
    <#assign  state = "审核未通过" >
<#elseif partyMember.partyApplicationState == PARTY_STATE_NOT  && partyMember.isDeny  ==  1>
    <#assign activistClass = "mui-page-awaiting" >
    <#assign  state = "继续审核" >
<#else>
    <#assign activistClass = "mui-page-success" >
    <#assign  state = "审核通过" >
</#if>
<#assign  remark = "${partyMember.activistRemark!}" >
<@common.moduleDiv titleName="申请积极分子">
<link href="${request.contextPath}/partybuild7/mobile/css/style.css" rel="stylesheet"/>

<header class="mui-bar mui-bar-nav mui-bar-grey" style="display:none;" >
    <a class="mui-action-back mui-icon mui-icon-left-nav mui-pull-left" id="cancelId" ><span>返回</span></a>
    <a class="mui-btn-close">关闭</a>
    <a class="mui-icon mui-icon-more mui-pull-right"></a>
    <h1 class="mui-title">申请积极分子</h1>
</header>

<nav class="mui-bar mui-bar-tab mui-bottom-fixed">
    <#if partyMember.partyApplicationState == PARTY_STATE_NOT && partyMember.isDeny ==  MEMBER_AUDIT_STATE_ING>
        <a class="mui-footer-btn mui-color-red" onclick="doDelete('${partyMember.id!}','撤销申请');" href="#" >撤销申请</a>
    <#elseif partyMember.partyApplicationState == PARTY_STATE_NOT  && partyMember.isDeny  ==  1 >
        <a class="mui-footer-btn mui-color-red" onclick="doDelete('${partyMember.id!}','撤销申请');" href="#" >撤销申请</a>
    <#elseif  partyMember.partyApplicationState == -1  && partyMember.isDeny  ==  1  >
        <a class="mui-footer-btn w50" href="#" onclick="doDelete('${partyMember.id!}','取消申请');" >取消申请</a>
        <a class="mui-footer-btn mui-f-btn-blue w50" href="#" onclick="doEdit('${partyMember.id!}');" >重新申请</a>
    </#if>
</nav>


<div class="mui-content" style="padding-top:0px;padding-bottom: 40px;">
    <div class="mui-page-prompt">

        <p class="${activistClass!}"><i></i></p>
        <!-- <p class="mui-page-success"><i></i></p>
         <p class="mui-page-failure"><i></i></p>-->
        <p class="prompt f14">${state!}</p>
    </div>
    <div class="mui-conference">
        <div class="mui-approvalParty-info">
            <div class="mui-input-row">
                <label>申请组织：</label>
                <div class="mui-conference-info">${partyMember.orgName!}</div>
            </div>
            <div class="mui-input-row">
                <label>申请内容：</label>
                <div class="mui-conference-info mui-approvalParty-apply">${partyMemberApply.applicationContent!}</div>
            </div>
            <#if (partyMember.partyApplicationState == PARTY_STATE_NOT || partyMember.partyApplicationState == -1) && partyMember.isDeny  ==  1 >
                <div class="mui-input-row">
                    <label>审核意见：</label>
                    <div class="mui-conference-info mui-approvalParty-apply">${partyMember.activistRemark!}</div>
                </div>
            </#if>

        </div>
    </div>
</div>
<script type="text/javascript">
    function doDelete(id ,str) {
        confirmMsg('确认' + str +"?",'提示',"","",function(){
            var options={
                url:"${request.contextPath}/mobile/open/partybuild7/partyMemberApply/delete",
                dataType:"json",
                type:"post",
                data:{"partyMemberId":id},
                success:function(data){
                    var jsonO = data;
                    if(!jsonO.success){
                        toastMsg(jsonO.msg);
                    }else{
                        toastMsg(jsonO.msg);
                    }
                    load("${request.contextPath}/mobile/open/partybuild7/partyMemberApply/partyMemberApplyLink?teacherId=${partyMember.id!}");
                },
                clearForm:false,
                resetForm:false,
                error:function(XMLHttpRequest ,textStatus,errorThrown){}

            };
            $.ajax(options);
        });
    }

    function doEdit(id) {
        load("${request.contextPath}/mobile/open/partybuild7/partyMemberApply/partyMemberApplyLink?isReApply=true&teacherId="+id);
    }
    $("#cancelId").click(function(){
//        window.history.back(-1);
        load("${request.contextPath}/mobile/open/partybuild7/homepage?teacherId=${teacherId!}");
    });
</script>
</@common.moduleDiv>