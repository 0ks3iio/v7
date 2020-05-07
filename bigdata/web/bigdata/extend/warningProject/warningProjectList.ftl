<#import  "/bigdata/v3/templates/commonWebMacro.ftl" as cwm />
<div class="box box-structure">
    <div class="box-header clearfix">
        <div class="form-group search">
            <div class="input-group">
                <input type="text" id="name_search" class="form-control" value="${projectName!}"
                       placeholder="输入项目名称查询">
                <a href="javascript:void(0);" onclick="searchProject()" class="input-group-addon" hidefocus="true"><i class="wpfont icon-search"></i></a>
            </div>
        </div>
        <div class="form-group clearfix">
            <button class="btn btn-lightblue js-add-kanban" onclick="addProject();">新建项目</button>
        </div>
    </div>
    <div class="box-body" id="reportDiv">
        <#if warningProjectList?exists && warningProjectList?size gt 0>
            <table class="tables">
                <thead>
                <tr>
                    <th>项目名称</th>
                    <th>开始时间</th>
                    <th>结束时间</th>
                    <th>定时任务</th>
                    <th>预警次数</th>
                    <th>上一次预警时间</th>
                    <th>操作</th>
                </tr>
                </thead>
                <tbody class="kanban-content">
                        <#list warningProjectList as item>
                        <tr>
                            <td title="${item.projectName!}">
                                    <#if item.projectName!?length gt 15>
                                        ${item.projectName?substring(0, 15)}...
                                    <#else>
                                        ${item.projectName!}
                                    </#if>
                            </td>
                            <td>${item.startTime?string('yyyy-MM-dd HH:mm')}</td>
                            <td>${(item.endTime?string('yyyy-MM-dd HH:mm'))!"永久"}</td>
                            <td>${item.scheduleParam!}</td>
                            <td>${item.warnTimes!}</td>
                            <td>${item.lastWarnDate!}</td>
                            <td><a class="js-edit" href="javascript:void(0);" onclick="editProject('${item.id!}')">
                                编辑</a><span class="tables-line">|</span>
                                <a class="js-delete" href="javascript:void(0);" onclick="deleteProject('${item.id!}')">
                                    删除</a>
                            </td>
                        </tr>
                        </#list>
                </tbody>
            </table>
            <@cwm.pageToolBar container=".page-content" class="text-right"/>
        <#else>
            <div class="no-data-word">
                <img src="${request.contextPath}/bigdata/v3/static/images/kanban-design/img-focus.png"/>&nbsp;&nbsp;暂无记录，请<span
                    class="js-add-kanban color-00cce3 pointer" onclick="addProject()">&nbsp;新建项目</span>
            </div>
        </#if>
    </div>
</div>
<script type="text/javascript">
    function addProject() {
        var href = '/bigdata/warningProject/editProjectUI';
        routerProxy.go({
            path: href,
            level: 2,
            name: '新建项目'
        }, function () {
            $('.page-content').load("${springMacroRequestContext.contextPath}" + href);
        });
    }

    function editProject(id) {
        var href = '/bigdata/warningProject/editProjectUI?id=' + id;
        routerProxy.go({
            path: href,
            level: 2,
            name: '修改项目'
        }, function () {
            $('.page-content').load("${springMacroRequestContext.contextPath}" + href);
        });
    }

    function deleteProject(id) {
        layer.confirm("删除项目会删除对应的预警结果，确认删除？", {
            btn: ['确定', '取消'],
            title: '确认信息',
            icon: 3,
            closeBtn: 0
        }, function (index) {
            layer.close(index);
            $.ajax({
                url: '${request.contextPath}/bigdata/warningProject/deleteWarningProject',
                type: 'POST',
                data: {
                    id: id
                },
                dataType: 'json',
                success: function (val) {
                    if (!val.success) {
                        showLayerTips4Confirm('error', val.message, 't', null);
                    }
                    else {
                        showLayerTips('success', '删除成功', 't');
                        $('.page-content').load("${request.contextPath}/bigdata/warningProject/list");
                    }
                }
            });
        });
    }

    function searchProject() {
        var url = '${request.contextPath}/bigdata/warningProject/list?projectName=' + $('#name_search').val();
        $('.page-content').load(url);
    }
</script>