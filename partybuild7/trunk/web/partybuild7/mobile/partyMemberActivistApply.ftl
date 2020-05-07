<#import "/fw/macro/mobilecommon.ftl" as common />

<@common.moduleDiv titleName="申请积极分子">
<link href="${request.contextPath}/partybuild7/mobile/css/style.css" rel="stylesheet"/>
<header class="mui-bar mui-bar-nav mui-bar-grey" style="display:none;">
    <a class="mui-action-back mui-icon mui-icon-left-nav mui-pull-left" id="cancelId" ><span>返回</span></a>
    <a class="mui-btn-close">关闭</a>
    <a class="mui-icon mui-icon-more mui-pull-right"></a>
    <h1 class="mui-title">申请积极分子</h1>
</header>
<div class="mui-content" id="divContent" style="padding-top:0px;" >
    <form class="mui-input-group mui-activist-list" id="orgForm">
        <div class="mui-input-row">
            <p class="mui-activist-organization mui-label-show"><!--没有选择党组织的时候去掉mui-label-show,选择了党组织了后加上mui-label-show-->
                <label>我要申请：</label>
                <input type="hidden" id="orgId" name="orgId" value="${partyMemberApply.orgId!}" >
                <input type="hidden" id="id" name="id" value="${partyMemberApply.id!}" >
                <input type="hidden" id="" name="partyMemberId" value="${partyMemberApply.partyMemberId!}" >
                <#if partyOrgs?exists && (partyOrgs?size <= 10) >
                    <span id='showOrganizationPicker' class="showOrganizationPicker">

                    <input type="text" <#if isReadOnly >readonly="true"</#if>  id="orgName" class="mui-input-clear"  data-input-clear="1" placeholder="你要申请哪个党组织" value="${partyMemberApply.orgName!}">
                    </span>
                <#else>
                    <input type="text"   onclick="getOrgnames();"  readOnly =  "readOnly"   id="orgName" class="mui-input-clear" data-input-clear="1" placeholder="你要申请哪个党组织" value="${partyMemberApply.orgName!}">
                </#if>
            </p>
        </div>
        <div class="mui-input-row mui-bg-row-grey">
            <span class="activist-tit">申请内容：</span>
        </div>
        <div class="mui-input-row mui-activist-info">
		    	<textarea class="activist-introduce"  <#if isReadOnly >readonly="true"</#if> id="content" name="applicationContent" class="mui-input-clear" placeholder="请输入申请内容...">${partyMemberApply.applicationContent!}</textarea>
        </div>
    </form>
    <#if !isReadOnly >
        <div class="mui-activist-btn"><a href="#" onclick="save();">提交</a></div>
    </#if>
</div>
<div id="orgNames" class="mui-content" style="display: none">
    <form class="mui-input-group mui-activist-seach">
        <div class="mui-input-row">
            <div class="mui-col-xs-10 mui-pull-left">
                <input type="text" id="orgName2" class="mui-input-clear f15" placeholder="请输入名称" oninput="doSelect();" data-input-clear="1" value="">
            </div>
            <div class="mui-col-xs-2 mui-pull-right">
                <button class="mui-btn-link"  id="doCancel">取消</button>
            </div>
        </div>
        <ul id="ulOrgNames" class="mui-table-view">
            <#if partyOrgs?exists && (partyOrgs?size gt 0 ) >
                <#list partyOrgs as party>
                    <li  class="mui-table-view-cell"  val="${party.id!}">${party.name!}</li>
                </#list>

            </#if>
        </ul>

    </form>
</div>
<script type="text/javascript">
    var isSubmit=false;
    function save() {
        if(isSubmit){
            reutrn;
        }
        isSubmit = true;
        var orgName = $("#orgName").val();

        var content = $("#content").val();
        if(!isNotBlank(orgName)){
            alertMsg("党组织不能为空");
            isSubmit = false;
            return;
        }
        if(!isNotBlank(content)){
            alertMsg("申请内容不能为空");
            isSubmit = false;
            return;
        }
        if(getLength(content) >2000){
            toastMsg('申请内容 超出最大长度限制1800个字符！');
            isSubmit = false;
            return;
        }
        var options={
            url:"${request.contextPath}/mobile/open/partybuild7/partyMemberApply/save",
            dataType:"json",
            type:"post",
            success:function(data){
                var jsonO = data;
                if(!jsonO.success){
                    toastMsg("保存失败");
                    return;
                }else{
                    toastMsg("保存成功");
                }
                isSubmit = false;
                load("${request.contextPath}/mobile/open/partybuild7/partyMemberApply/partyMemberApplyLink?teacherId=${teacherId!}");
            },
            clearForm : false,
            resetForm : false,
            error:function(XMLHttpRequest ,textStatus,errorThrown){}
        }
        $("#orgForm").ajaxSubmit(options);
    }

    <#if partyOrgs?exists && (partyOrgs?size <= 10) &&  !isReadOnly >
    var orgs = ${orgs!};
    (function($, doc) {
        $.init();
        $.ready(function() {
            //普通示例
            var organizationPicker = new $.PopPicker();
            organizationPicker.setData(orgs);
            var showUserPickerButton = doc.getElementById('showOrganizationPicker');
//            var organizationResult = doc.getElementById('organizationResult');
            var orgId = doc.getElementById("orgId");
            var orgName = doc.getElementById("orgName");
            showOrganizationPicker.addEventListener('tap', function(event) {
                organizationPicker.show(function(items) {

//                    var name = items[0]['text'];
                    orgId.value=items[0]['value'];
                    orgName.value = items[0]['text'];
                    //返回 false 可以阻止选择框的关闭
                    //return false;
                });
            }, false);
        });
    })(mui, document);
    </#if>
    function getOrgnames(e) {

        $("#divContent").hide();
        $("#orgNames").show();
        e.stopPropagation();
        e.preventDefault();
    }
    function doSelect() {
        var orgName = $("#orgName2").val();
        $("#ulOrgNames li").each(function () {
            if($(this).text().indexOf(orgName) >= 0){
                $(this).show();
            }else{
                $(this).hide();
            }
        })

    }
    $("#ulOrgNames li").bind("click",function () {
        var name= $(this).text();
        var value = $(this).attr("val");
        $("#orgId").val(value);
        $("#orgName").val(name);
        $("#divContent").show();
        $("#orgNames").hide();

    })
    $("#doCancel").click(function (e) {
        e.stopPropagation();
        e.preventDefault();
        //load("${request.contextPath}/mobile/open/partybuild7/partyMemberApply/partyMemberApplyLink?teacherId=${teacherId!}");
        $("#divContent").show();
        $("#orgNames").hide();
        return false;
    })
    $("#cancelId").click(function(){
//        window.history.back(-1);
        <#if isReApply == '1' >
            load("${request.contextPath}/mobile/open/partybuild7/partyMemberApply/partyMemberApplyLink?teacherId=${teacherId!}");
        <#else >
            load("${request.contextPath}/mobile/open/partybuild7/homepage?teacherId=${teacherId!}");
        </#if>

    });
</script>
</@common.moduleDiv>