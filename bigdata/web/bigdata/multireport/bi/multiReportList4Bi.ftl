<#import "/bigdata/v3/templates/bi/macro/biCommonWebMacro.ftl" as commonMacro />
<#if resultList?exists && resultList?size gt 0>
<div class="table-made">
    <table class="tables">
        <thead>
        <th></th>
        <th><#if type!  ==6>看板<#else>报告</#if>名称</th>
        <th>标签</th>
        <th>操作</th>
        <th></th>
        </thead>
        <tbody>
        <#list resultList as report>
        <tr>
            <td></td>
            <td>${report.name!}</td>
            <td>
                <#if report.tagNameList?exists>
                     <#list report.tagNameList as tag>
                         ${tag!}&nbsp;
                     </#list>
                 </#if>
            </td>
            <td>
                <a href="javascript:void(0)" onclick="previewMultiReport('${report.id!}','${report.type!}','${report.name!}')" class="look-over">查看</a>
                <span class="tables-line">|</span>
                <a href="javascript:void(0)"  onclick="doFavorite('${report.id!}','${report.type!}','${report.name!}');">收藏</a>
                <span class="tables-line">|</span>
                <a href="javascript:void(0)"  onclick="shareLayer('${report.id!}','${report.type!}','${report.name!}');">分享</a>
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
            <p class="color-999">暂无<#if type! ==6>看板<#else>报告</#if>记录</p>
        </div>
    </div>
</#if>
<div class="layer-wrap layer-share layer-user-select"></div>
<@commonMacro.biConfirmTip></@commonMacro.biConfirmTip>
<script>

    function previewMultiReport(id,type,name) {
        var url = '${request.contextPath}/bigdata/user/report/preview?businessType='+type+'&businessId='+ id+"&businessName="+name;
        window.open(url,id)
    };

    //收藏
    function doFavorite(businessId, businessType, businessName) {
        $.ajax({
            url: '${request.contextPath}/bigdata/favorite/addFavorite',
            data: {
                businessId: businessId,
                businessType: businessType,
                businessName: businessName
            },
            type: 'POST',
            dataType: 'json',
            success: function (val) {
                if (!val.success) {
                    showBIErrorTips("提示",val.message,'390px','auto');
                } else {
                    showBiTips('收藏成功',2000);
                }
            }
        });
    };

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
                layer.closeAll();
                if (response.success) {
                    $("#businessContentDiv").load('${request.contextPath}/bigdata/multireport/bi/list?type='+businessType,function(){
                        showBiTips(response.message,2000);
                    });
                } else {
                    showBIErrorTips("提示",response.message,'390px','auto');
                }
            }
        });
    };
</script>