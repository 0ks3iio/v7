
<#import "/fw/macro/mobilecommon.ftl" as common />
<@common.moduleDiv>
<link href="${request.contextPath}/partybuild7/mobile/css/style.css" rel="stylesheet"/>
<header class="mui-bar mui-bar-nav mui-bar-grey" style="display:none;" >
    <a class="mui-action-back mui-icon mui-icon-left-nav mui-pull-left"><span>返回</span></a>
    <a class="mui-btn-close">关闭</a>
    <a class="mui-icon mui-icon-more mui-pull-right"></a>
    <h1 class="mui-title">吉林省教育厅党组织...</h1>
</header>
<nav class="mui-bar mui-bar-tab mui-bottom-fixed">
    <div class="mui-input-row mui-checkbox mui-left">
		   <span>
			   <label class="mui-checkbox-all">全选</label>
			   <input name="checkbox" value="Item 1" type="checkbox">
		   </span>
        <button type="button" class="mui-btn mui-btn-primary mui-btn-outlined">同意</button>
        <button type="button" class="mui-btn mui-btn-outlined">继续审核</button>
        <button type="button" class="mui-btn mui-btn-outlined">撤回</button>
    </div>
</nav>
<div class="mui-content" >
    <div class="mui-segmented-control mui-segmented-control-inverted mui-segmented-control-fixed">
        <a class="mui-control-item mui-active" href="#item1">
            <span>未审核</span>
        </a>
        <a class="mui-control-item" href="#item2">
            <span>已通过审核</span>
        </a>
        <a class="mui-control-item" href="#item3">
            <span>未通过审核</span>
        </a>
    </div>
    <div class="mui-content-scroll">
        <div id="item1" class="mui-control-content mui-active">
            <ul class="mui-table-view" >
                <input type="hidden" value="${partyMemberAwaitingList?size}" >
                <#if partyMemberAwaitingList?exists && (partyMemberAwaitingList?size gt 0 )>
                    <#list partyMemberAwaitingList as await>
                        <li class="mui-table-view-cell mui-media mui-approvalParty-list" onclick="getInfo('${await.id}')">
                            <div class="mui-input-row mui-checkbox mui-left">
                                <div class="mui-media-body">
                                    <p class="mui-approvalParty-tit"><span>${await.teacherName!}</span><span></span><span>${await.phone!}</span></p>
                                    <p class="mui-ellipsis">所属单位：${await.unitName!}</p>
                                    <p class="mui-ellipsis">申请党组织：${orgName!}</p>
                                </div>
                                <input name="checkbox" value="Item 1" type="checkbox">
                            </div>

                        </li>
                    </#list>
                </#if>
            </ul>
        </div>
    <#--${appsetting.getMcode("DM-XB").getHtmlTag(await.sex?default(''))}-->
        <#--<div id="item2" class="mui-control-content">-->
            <#--<ul class="mui-table-view" >-->
                <#--<#if partyMemberConfirmList?exists && (partyMemberConfirmList?size gt 0)>-->
                    <#--<#list partyMemberConfirmList as member>-->
                        <#--<li class="mui-table-view-cell mui-media mui-approvalParty-list">-->
                            <#--<div class="mui-input-row mui-checkbox mui-left">-->
                                <#--<div class="mui-media-body">-->
                                    <#--<p class="mui-approvalParty-tit"><span>${member.teacherName!}</span><span>${appsetting.getMcode("DM-XB").getHtmlTag(member.sex?default(''))}</span><span>${member.phone!}</span></p>-->
                                    <#--<p class="mui-ellipsis">所属单位：${member.unitName!}</p>-->
                                    <#--<p class="mui-ellipsis">申请党组织：${orgName!}</p>-->
                                <#--</div>-->
                                <#--<input name="checkbox" value="Item 1" type="checkbox">-->
                            <#--</div>-->
                        <#--</li>-->
                    <#--</#list>-->
                <#--</#if>-->
            <#--</ul>-->
        <#--</div>-->
        <#--<div id="item3" class="mui-control-content">-->
            <#--<ul class="mui-table-view" >-->
            <#--<#if partyMemberNotConfirmList?exists && (partyMemberNotConfirmList?size gt 0)>-->
                <#--<#list partyMemberNotConfirmList as member>-->
                    <#--<li class="mui-table-view-cell mui-media mui-approvalParty-list">-->
                        <#--<div class="mui-input-row mui-checkbox mui-left">-->
                            <#--<div class="mui-media-body">-->
                                <#--<p class="mui-approvalParty-tit"><span>${member.teacherName!}</span><span>${appsetting.getMcode("DM-XB").getHtmlTag(member.sex?default(''))}</span><span>${member.phone!}</span></p>-->
                                <#--<p class="mui-ellipsis">所属单位：${member.unitName!}</p>-->
                                <#--<p class="mui-ellipsis">申请党组织：${orgName!}</p>-->
                            <#--</div>-->
                            <#--<input name="checkbox" value="Item 1" type="checkbox">-->
                        <#--</div>-->

                    <#--</li>-->
                <#--</#list>-->
            <#--</#if>-->

            <#--</ul>-->
        <#--</div>-->
    </div>

</div>
<script type="text/javascript" >
    function getInfo(id) {
        load("${request.contextPath}/mobile/open/partybuild7/partyMemberApply/activistState?teacherId="+id);
    }

</script>
</@common.moduleDiv>

