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
    </div>
    <#if warningProjectList?exists && warningProjectList?size gt 0>
    <div class="box-body" id="reportDiv">
            <table class="tables">
                <thead>
                <tr>
                    <th>项目名称</th>
                    <th>开始时间</th>
                    <th>结束时间</th>
                    <th>预警次数</th>
                    <th>上一次预警时间</th>
                    <th>操作</th>
                </tr>
                </thead>
                <tbody class="kanban-content">
                        <#list warningProjectList as item>
                        <tr>
                            <td>${item.projectName!}</td>
                            <td>${item.startTime?string('yyyy-MM-dd HH:mm')}</td>
                            <td>${(item.endTime?string('yyyy-MM-dd HH:mm'))!"永久"}</td>
                            <td>${item.warnTimes!}</td>
                            <td>${item.lastWarnDate!}</td>
                            <td><a class="js-edit" href="javascript:void(0);" onclick="viewResult('${item.id!}')">
                                查看</a><span class="tables-line">|</span>
                                <a class="js-delete" href="javascript:void(0);" onclick="clearResult('${item.id!}')">
                                    清理结果</a>
                            </td>
                        </tr>
                        </#list>
                </tbody>
            </table>
            <@cwm.pageToolBar container=".page-content" class="text-right"/>
    </div>
      <#else>
        <div class="no-data-common">
			<div class="text-center">
				<img src="${request.contextPath}/bigdata/v3/static/images/public/no-data-common-100.png"/>
				<p class="color-999">暂无记录</p>
			</div>
		</div>
    </#if>
</div>
<script type="text/javascript">

    function viewResult(id) {
        var href = '${request.contextPath}/bigdata/warningResult/viewWarningResult?id=' + id;
        routerProxy.go({
            path: href,
            level: 2,
            name: '预警结果'
        }, function () {
            $('.page-content').load("${request.contextPath}" + href);
        });
    }

    function clearResult(id) {
        layer.confirm("确定清理本项目所有的预警结果？", {btn: ['确定', '取消'], title: '确认信息', icon: 3, closeBtn: 0}, function (index){
            layer.close(index);
            $.ajax({
                url: '${request.contextPath}/bigdata/warningResult/clearWarningResult',
                type: 'POST',
                data : {
                    id : id
                },
                dataType: 'json',
                success: function (val) {
                    if (!val.success) {
                        showLayerTips4Confirm('error', val.message, 't', null);
                    }
                    else {
                        layer.msg('清理成功', {icon: 1, offset:'t', time: 1000});
                        $('.page-content').load('${request.contextPath}/bigdata/warningResult/list');
                    }
                }
            });
        });
    }

    function searchProject() {
        var url = '${request.contextPath}/bigdata/warningResult/list?projectName=' + $('#name_search').val();
        $('.page-content').load(url);
    }
</script>