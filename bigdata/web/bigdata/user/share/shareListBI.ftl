<#import "/bigdata/v3/templates/bi/macro/biCommonWebMacro.ftl" as commonMacro />
<#if shareList?exists && shareList?size gt 0>
    <ul class="tabs tabs-basics bi-tabs" tabs-group="g1">
        <li class="tab <#if tabType?default(1)==1>active</#if>"><a href="javascript:void(0);" id="myShare">我分享的资源</a></li>
        <li class="tab <#if tabType?default(1)==2>active</#if>"><a href="javascript:void(0);" id="beShared">分享给我的资源</a></li>
    </ul>
    <div class="table-made">
        <table class="tables">
            <thead>
            <tr>
                <th></th>
                <th>名称</th>
                <th>类型</th>
                <#if type! ==1>
                    <th>被分享人</th>
                <#else>
                    <th>分享人</th>
                </#if>
                <th>操作</th>
                <th></th>
            </tr>
            </thead>
            <tbody>
            <#list shareList as share>
                <tr>
                    <td></td>
                    <td>${share.businessName!}</td>
                    <td><#if share.businessType! =="1">数据图表<#elseif share.businessType! =="3">数据报表<#elseif share.businessType! =="5">多维报表<#elseif share.businessType! =="6">数据看板<#elseif share.businessType! =="7">数据报告<#else>未知</#if></td>
                    <td title="${share.userNames!}">
                        <#if share.userNames! !="" && share.userNames?length gt 50>${share.userNames?substring(0, 50)}......<#else>${share.userNames!}</#if>
                    </td>
                    <td>
                        <a href="javascript:void(0)"  onclick="showReportFromShare('${share.businessId!}','${share.businessType!}','${share.businessName!}')" >查看</a>
                        <#if type! ==1>
                            <span class="tables-line">|</span><a href="javascript:void(0)"  onclick="deleteShare('${share.businessId!}','${share.businessType!}','${share.businessName!}');">分享</a>
                        </#if>
                    </td>
                    <td></td>
                </tr>
            </#list>
            </tbody>
        </table>
        <@commonMacro.pageToolBar container="#businessContentDiv" class="bi-pagination"/>
    </div>
<#else>
    <div class="no-data-common">
        <div class="text-center">
            <img src="${request.contextPath}/bigdata/v3/static/images/bi/no-data-common.png"/>
            <p class="color-999"><#if type! ==1>暂无分享记录<#else>暂无被分享记录</#if></p>
        </div>
    </div>
</#if>
<div class="layer-wrap layer-share layer-user-select"></div>
<@commonMacro.biConfirmTip></@commonMacro.biConfirmTip>
<script>

    $('#myShare').click(function () {
        var url =  "${request.contextPath}/bigdata/share/common/bi/index?type=1&tabType=1";
        $("#businessContentDiv").load(url);
    })
    $('#beShared').click(function () {
        var url =  "${request.contextPath}/bigdata/share/common/bi/index?type=2&tabType=2";
        $("#businessContentDiv").load(url);
    })

    function showReportFromShare(id,type,name) {
        var url = '${request.contextPath}/bigdata/user/report/preview?businessType='+type+'&businessId='+ id+"&businessName="+name;
        window.open(url,id)
    }


    function deleteShare(id, type, name){
        shareLayer(id, type, name);
    }

    function shareLayer(businessId, businessType, businessName) {
        $.ajax({
            url: '${request.contextPath}/bigdata/share/getAllUser',
            type: 'GET',
            data: {businessId: businessId},
            success: function (response) {
                $.ajax({
                    url: '${request.contextPath}/bigdata/common/tree/shareUserTreeIndex4Bi',
                    type: 'POST',
                    data: {users:response.data},
                    dataType: 'html',
                    beforeSend: function(){
                        $('.layer-user-select').html("<div class='pos-middle-center'><h4><img src='${request.contextPath}/static/ace/img/loading.gif' />玩命的加载中......</h4></div>");
                    },
                    success: function (response) {
                        $('.layer-user-select').html(response);
                        openUserSelectDiv('请选择分享用户','700px','500px',function(){
                            var teacherArray = [];
                            zTreeSelectedUserIdMap.forEach(function (value, key, map) {
                                teacherArray.push(key);
                            });
                            doShare(businessId, businessType, businessName, teacherArray);
                            layer.closeAll();
                        });
                    }
                });
            }
        });

    }
    function doShare(businessId, businessType, businessName, userArray) {
        $.ajax({
            url: '${request.contextPath}/bigdata/share/addShare',
            type: 'POST',
            dataType: 'json',
            data: {
                businessId: businessId,
                businessType: businessType,
                businessName: businessName,
                userArray: userArray
            },
            success: function (response) {
                if (response.success) {
                    //showLayerTips('success', response.message, 't');
                    $("#businessContentDiv").load('${request.contextPath}/bigdata/share/common/bi/index?type=1',function () {
                        if(userArray.length==0){
                            $("#businessType-92-num").html($("#businessType-92-num").text()-1)
                        }
                        showBiTips(response.message,2000);
                    });
                } else {
                    showBIErrorTips("提示",response.message,'390px','auto');
                }
            }
        });
    };
</script>