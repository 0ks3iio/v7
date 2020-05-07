<#import "pageMacro.ftl" as page />
<#import "appStatusMacro.ftl" as appStatus />
<div id="appList">
    <div class="box box-default scroll-height">
        <div class="">
            <div class="form-group clearfix">
                <button class="btn btn-lightblue" id="addApp">新增应用</button>
            </div>
            <div class="filter-item">
                <span class="filter-name">应用用途：</span>
                <div class="filter-content">
                    <select id="applyType" class="form-control" onChange="showAppList()" style="width:150px">
                       <option value="-1" >全部</option>
                       <option value="1" <#if applyType?exists && applyType == '1'>selected</#if>>单点对接</option>
                       <option value="2" <#if applyType?exists && applyType == '2'>selected</#if>>接口申请</option>  
                    </select>
                </div>
            </div>
        </div>
        <div class="table-container">
            <#if appList?? && appList.content?? && appList.content?size gt 0>
                <div class="table-container-body">
                    <table class="tables">
                        <thead>
                        <tr>
                            <th>应用</th>
                            <th>开发者</th>
                            <th>适用单位</th>
                            <th>适用对象</th>
                            <th>状态</th>
                            <th>操作</th>
                        </tr>
                        </thead>
                        <tbody>
                        <#list appList.content as app>
                        <tr>
                            <td>${app.name}</td>
                            <td>${app.developerName!}</td>
                            <td>${app.unitClasses!}</td>
                            <td>${app.userTypes!}</td>
                            <td>
                                <#if app.status??>
                                <@appStatus.appStatus status=app.status.getValue() />
                                </#if>
                            </td>
                            <td>
                                <#if app.status?? && app.status.getValue()!=5>
                                    <a href="javascript:void(0);" data-id="${app.id!}" class="app-edit color-blue">编辑</a>
                                </#if>
                                <#if app.status?? && app.status.getValue()==5>
                                    <a href="javascript:void(0);" data-id="${app.id!}" class="app-audit color-orange">审核</a>
                                </#if>
                            <#--<a href="javascript:void(0);" id="${app.id!}" class="audit">审核</a>-->
                                    <#if app.status?? && app.status.getValue()==1>
                                    <a href="javascript:void(0);" data-id="${app.id!}" class="app-stop color-red">下线</a>
                                    <#elseif app.status?? && app.status.getValue()==2>
                                    <a href="javascript:void(0);" data-id="${app.id!}" class="app-enable color-green">上线</a>
                                    </#if>
                                <#if app.status?? && app.status.getValue()!=3>
                                    <a href="javascript:void(0);" data-id="${app.id!}" class="app-delete color-red">删除</a>
                                </#if>
                            </td>
                        </tr>
                        </#list>
                        </tbody>
                    </table>
                    <#if appList?? && appList.content?? && appList.content?size gt 0>
                        <@page.paginataion pages=appList containerId='appList' pageCallFunction="pageLoadApp" />
                    </#if>
                </div>
            <#else >
                <div class="wrap-1of1 centered no-data-state">
                    <div class="text-center">
                        <img src="${request.contextPath}/bigdata/v3/static/images/public/no-data-common-50.png">
                        <p>暂无数据</p>
                    </div>
                </div>
            </#if>

        </div>
    </div>
</div>
<script>
    $(function () {
        $('#addApp').click(function () {
            router.go({
                path: '/bigdata/app/add',
                type: 'item',
                name: '新增应用',
                length: 3,

            }, function () {
                $('.page-content').load('${request.contextPath}/bigdata/app');
            })
        });
        $('.app-edit').click(function () {
            var id = $(this).data('id');
            router.go({
                path: '/bigdata/app/add?appId=' + id,
                type: 'item',
                name: '编辑应用',
                length: 3,
            }, function () {
                $('.page-content').load('${request.contextPath}/bigdata/app?appId=' + id);
            })
        });
        $('.app-delete').click(function () {
            var id = $(this).data('id');
            showConfirmTips('warn', '提示', '删除操作不可恢复，您确认删除该数据吗？', function () {
                $.post('${request.contextPath}/bigdata/app/delete/' + id, function (res) {
                    showLayerTips('success', '删除成功', 't', goAppHome())
                })
            })
        });
        $('.app-enable').click(function () {
            var id = $(this).data('id');
            $.post('${request.contextPath}/bigdata/app/modify/status/' + id, {"appStatus": "1"}, function (res) {
                showLayerTips('success', '应用已上线', 't', goAppHome())
            })
        })
        $('.app-stop').click(function () {
            var id = $(this).data('id');
            $.post('${request.contextPath}/bigdata/app/modify/status/' + id , {"appStatus": "2"}, function (res) {
                showLayerTips('success', '应用已下线', 't', goAppHome())
            })
        });
        $('.app-audit').click(function () {
            var id = $(this).data('id');
            router.go({
                path: '/bigdata/app/audit?appId=' + id,
                type: 'item',
                name: '审核应用',
                length: 3,
            }, function () {
                $('.page-content').load('${request.contextPath}/bigdata/app/audit?appId=' + id);
            })
        });
    });

    function pageLoadApp(params) {
        router.go({
            path: '/bigdata/app/index?' + params,
            // type: 'item',
            name: '应用管理',
            level: 1,

        }, function () {
            $('.page-content').load('${request.contextPath}/bigdata/app/index?' + params);
        })
    }

    function goAppHome() {
        router.reload({
            path: '',
            name: '应用管理',
            // type: 'item',
            level: 1
        });
    }
    function showAppList(){
   	  var applyType = $('#applyType').val();
   	  router.go({
       path: '/bigdata/app/index?applyType=' + applyType,
       type: 'item',
       name: '查看应用',
       level: 1,
      }, function () {
            $('.page-content').load('${request.contextPath}/bigdata/app/index?applyType=' + applyType);
      })
   	}
</script>