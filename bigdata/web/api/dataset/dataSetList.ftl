<#import  "/bigdata/v3/templates/commonWebMacro.ftl" as cwm />
<div class="box box-structure">
    <div class="box-header clearfix">
        <div class="form-group search">
            <div class="input-group">
                <input type="text" id="name_search" class="form-control" value="${dataSetName!}"
                       placeholder="输入数据集名称查询">
                <a href="javascript:void(0);" onclick="searchDataSet()" class="input-group-addon" hidefocus="true"><i class="wpfont icon-search"></i></a>
            </div>
        </div>
        <div class="form-group clearfix">
            <button class="btn btn-lightblue js-add-kanban" onclick="addDataset();">新建数据集</button>
        </div>
    </div>
    <div class="box-body" id="reportDiv">
        <#if dataSetDtoList?exists && dataSetDtoList?size gt 0>
            <table class="tables">
                <thead>
                <tr>
                    <th>名称</th>
                    <th>创建时间</th>
                    <th>表名</th>
                    <th>元数据名称</th>
                    <th>数据集描述</th>
                    <th>操作</th>
                </tr>
                </thead>
                <tbody class="kanban-content">
                        <#list dataSetDtoList as item>
                        <tr>
                            <td title="${item.name!}">
                                    <#if item.name!?length gt 15>
                                        ${item.name?substring(0, 15)}...
                                    <#else>
                                        ${item.name!}
                                    </#if>
                            </td>
                            <td>${item.creationTime?string('yyyy-MM-dd HH:mm')}</td>
                            <td>${item.tableName!}</td>
                            <td>${item.metadataName!}</td>
                            <td title="${item.remark!}">
                                    <#if item.remark!?length gt 20>
                                        ${item.remark?substring(0, 20)}...
                                    <#else>
                                        ${item.remark!}
                                    </#if>
                            </td>
                            <td><a class="js-edit" href="javascript:void(0);" onclick="editDataSet('${item.id!}')">编辑</a>
                                <span class="tables-line">|</span>
                                <a class="js-delete" href="javascript:void(0);" onclick="deleteDataSet('${item.id!}')"> 删除</a>
                            </td>
                        </tr>
                        </#list>
                </tbody>
            </table>
            <@cwm.pageToolBar container=".page-content" class="text-right"/>
        <#else>
            <div class="no-data-word">
                <img src="${request.contextPath}/bigdata/v3/static/images/kanban-design/img-focus.png"/>&nbsp;&nbsp;暂无记录，请<span
                    class="js-add-kanban color-00cce3 pointer" onclick="addDataset()">&nbsp;新建数据集</span>
            </div>                                                      
        </#if>
    </div>
</div>
<script type="text/javascript">
    function addDataset() {
        var href = '/bigdata/dataset/edit';
        routerProxy.go({
            path: href,
            level: 2,
            name: '新建数据集'
        }, function () {
            $('.page-content').load("${springMacroRequestContext.contextPath}" + href);
        });
    }

    function editDataSet(id) {
        var href = '/bigdata/dataset/edit?id=' + id;
        routerProxy.go({
            path: href,
            level: 2,
            name: '修改数据集'
        }, function () {
            $('.page-content').load("${springMacroRequestContext.contextPath}" + href);
        });
    }

    function deleteDataSet(id) {
        layer.confirm("删除数据集，确认删除？", {
            btn: ['确定', '取消'],
            title: '确认信息',
            icon: 3,
            closeBtn: 0
        }, function (index) {
            layer.close(index);
            $.ajax({
                url: '${request.contextPath}/bigdata/dataset/delete',
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
                        $('.page-content').load("${request.contextPath}/bigdata/dataset/index");
                    }
                }
            });
        });
    }

    function searchDataSet() {
        var url = '${request.contextPath}/bigdata/dataset/index?dataSetName=' + $('#name_search').val();
        $('.page-content').load(url);
    }
</script>