<#import "../paging/spring-jpa-paging.ftl" as commonMacro />
<#if charts.content?exists && charts.content?size gt 0>
<div class="table-made">
    <table class="tables" id="aa">
        <thead>
        <th></th>
        <th>图表名称</th>
        <th>标签</th>
        <th>操作</th>
        <th></th>
        </thead>
        <tbody>
            <#list charts.content as chart>
                <tr>
                    <td></td>
                    <td>${chart.name!}</td>
                    <td>
                        <#if chart.tagNameList?exists && chart.tagNameList?size gt 0>
                            <#list chart.tagNameList as tag>
                                ${tag!}<#if tag_index < chart.tagNameList?size -1 >,</#if>
                            </#list>
                        </#if>
                    </td>
                    <td>
                        <a href="javascript:viewChart('${chart.id}');">查看</a>
                        <span class="tables-line">|</span>
                        <a href="javascript:void(0)" onclick="doFavorite('${chart.id!}','${chart.name!}');">收藏</a>
                        <span class="tables-line">|</span>
                        <a href="javascript:void(0)" onclick="shareLayer('${chart.id!}','${chart.name!}');">分享</a>
                    </td>
                    <td></td>
                </tr>
            </#list>
        </tbody>
    </table>
    <@commonMacro.pagings pages=charts containerId="businessContentDiv" pageCallFunction="doGetList"/>
</div>
<#else>
    <div class="no-data-common">
        <div class="text-center">
            <img src="${request.contextPath}/bigdata/v3/static/images/bi/no-data-common.png"/>
            <p class="color-999">暂无图表</p>
        </div>
    </div>
</#if>
<div class="layer-wrap layer-share layer-user-select">
</div>
<script>
    function viewChart(folderDetailId) {
        var pForm = document.createElement("form");
        document.body.appendChild(pForm);
        pForm.action = "/bigdata/user/report/preview?businessType=1&businessId=" + folderDetailId;
        pForm.target = "_blank";
        pForm.method = "post";
        pForm.submit();
    }
    
    function doFavorite(chartId,chartName) {
        $.ajax({
            url: '${request.contextPath}/bigdata/favorite/addFavorite',
            data: {
                businessId: chartId,
                businessType: 1,
                businessName: chartName
            },
            type: 'POST',
            dataType: 'json',
            success: function (result) {
                if (!result.success) {
                    showBIErrorTips("提示",result.message,'390px','auto');
                } else {
                    showBiTips('收藏成功',2000);
                }
            }
        });
    }

    function shareLayer(chartId,chartName) {
        $.ajax({
            url: '${request.contextPath}/bigdata/share/getAllUser',
            type: 'GET',
            data: {businessId: chartId},
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
                            doShare(chartId, chartName, teacherArray);
                            layer.closeAll();
                        });
                    }
                });
            }
        });
    }

    function doShare(chartId, chartName, userArray) {
        $.ajax({
            url: '${request.contextPath}/bigdata/share/addShare',
            type: 'POST',
            dataType: 'json',
            data: {
                businessId: chartId,
                businessType: 1,
                businessName: chartName,
                userArray: userArray
            },
            success: function (response) {
                if (response.success) {
                    $("#businessContentDiv").load('${request.contextPath}/bigdata/chartQuery/chartList',function () {
                        showBiTips(response.message,2000);
                    });
                } else {
                    showBIErrorTips("提示",response.message,'390px','auto');
                }
            }
        });
    };

    function doGetList(pageURL) {
        var url = '${request.contextPath}/bigdata/chartQuery/chartList?' + pageURL;
        $('#businessContentDiv').load(url);
    }
</script>