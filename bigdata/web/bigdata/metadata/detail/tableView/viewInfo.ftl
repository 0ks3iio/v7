<div class="table-container">
    <div class="table-container-header clearfix mb-5">
        <button class="pull-right btn btn-lightblue add-data" onclick="editTableView('')">新增视图</button>
    </div>
    <div class="table-container-body">
        <table class="tables">
            <thead>
            <tr>
                <th>名称</th>
                <th>列</th>
                <th>备注</th>
                <th>操作</th>
            </tr>
            </thead>
            <tbody>
                <#if  tableViews?exists &&tableViews?size gt 0>
                <#list tableViews as item>
                <tr>
                    <td>${item.name!}</td>
                    <td title="${item.tableName!?html}"><#if item.tableName! !="" && item.tableName?length gt 60>${item.tableName?substring(0, 60)}......<#else>${item.tableName!}</#if></td>
                    <td title="${item.remark!?html}"><#if item.remark! !="" && item.remark?length gt 10>${item.remark?substring(0, 10)}......<#else>${item.remark!}</#if></td>
                    <td>
                        <a href="javascript:void(0);" onclick="executeTableView('${item.id!}')">执行</a><span class="tables-line">|</span>
                        <a href="javascript:void(0);" onclick="editTableView('${item.id!}')">编辑</a><span class="tables-line">|</span>
                        <a href="javascript:void(0);" onclick="deleteTableView('${item.id!}')">删除</a>
                    </td>
                </tr>
                </#list>
                <#else>
                    <tr >
                        <td  colspan="4" align="center">
                            暂无数据
                        </td>
                    <tr>
                </#if>

            </tbody>
        </table>
    </div>
</div>
<div class="layer layer-tableView">
</div>
<script>
    function editTableView(id) {
        var parentId = $('.directory-tree a.active').attr('id');
        var metadataName =$('.directory-tree a.active').find('span:last-child').text();
        var mdType = $('.filter-item.active').attr('mdType');
        $("#metadataDiv").load('${request.contextPath}/bigdata/metadata/tableViewEdit?id=' + id + '&parentId=' + parentId + "&mdType=" + mdType + "&metadataName=" + metadataName);
        var isSubmit = false;
        layer.open({
            type: 1,
            shade: .6,
            title: id == '' ? '新增视图' : '修改视图',
            btn: ['保存', '取消'],
            yes: function (index, layero) {
                if (isSubmit) {
                    return;
                }
                isSubmit = true;

                if ($('#name').val() == null || $('#name').val() == '') {
                    layer.tips("不能为空", "#name", {
                        tipsMore: true,
                        tips: 3
                    });
                    isSubmit = false;
                    return;
                }

                if(/^[^a-zA-Z]/g.test($('#name').val())){
                    layer.tips("视图名称必须是字母开头", "#name", {
                        tipsMore: true,
                        tips: 3
                    });
                    isSubmit = false;
                    return;
                }

                if (!/^[0-9a-zA-Z_]{1,}$/.test($('#name').val())) {
                    layer.tips("视图名称不能包含特殊字符", "#name", {
                        tipsMore: true,
                        tips: 3
                    });
                    isSubmit = false;
                    return;
                }

                if ($('#columnSelect').val() == null || $('#columnSelect').val() == '') {
                    layer.tips("不能为空", "#columnSelect_chosen", {
                        tipsMore: true,
                        tips: 3
                    });
                    isSubmit = false;
                    return;
                }

                var options = {
                    url: "${request.contextPath}/bigdata/metadata/saveTableView",
                    dataType: 'json',
                    data:{column : $('#columnSelect').val()},
                    success: function (data) {
                        if (!data.success) {
                            showLayerTips4Confirm('error',data.message);
                            isSubmit = false;
                        } else {
                            showLayerTips('success','保存成功!','t');
                            layer.close(index);
                            // 获取id
                            var id = $('.directory-tree a.active').attr('id');
                            var url = "${request.contextPath}/bigdata/metadata/" + id + "/tableView";
                            $("#metadataDetailDiv").load(url);
                        }
                    },
                    clearForm: false,
                    resetForm: false,
                    type: 'post',
                    error: function (XMLHttpRequest, textStatus, errorThrown) {
                    }//请求出错
                };
                $("#metadataForm").ajaxSubmit(options);
            },
            area: ['600px', '320px'],
            content: $('.layer-metadata')
        });
    }

    function deleteTableView(id) {
        showConfirmTips('prompt',"提示","您确定要删除该视图吗？",function(){
            $.ajax({
                url: '${request.contextPath}/bigdata/metadata/deleteTableView',
                type: 'POST',
                data: {
                    id: id,
                },
                dataType: 'json',
                success: function (val) {
                    if (!val.success) {
                        showLayerTips4Confirm('error',val.message);
                    }
                    else {
                        showLayerTips('success','删除成功!','t');
                        // 获取id
                        var id = $('.directory-tree a.active').attr('id');
                        var url = "${request.contextPath}/bigdata/metadata/" + id + "/tableView";
                        $("#metadataDetailDiv").load(url);
                    }
                }
            });
        });
    }
    
    function executeTableView(id) {
        var url =  _contextPath + '/bigdata/metadata/executeTableViewUI?id=' + id;

        $(".layer-tableView").html("<div class='pos-middle-center'><h4><img src='${request.contextPath}/static/ace/img/loading.gif' />加载中......</h4></div>");
        $(".layer-tableView").load(url);
        layer.open({
            type: 1,
            shade: .5,
            title: ['创建视图','font-size:16px'],
            area: ['600px','300px'],
            btn:['执行', '取消'],
            content: $('.layer-tableView'),
            resize:true,
            yes:function (index) {
                $.ajax({
                    url: '${request.contextPath}/bigdata/metadata/executeTableView',
                    type: 'POST',
                    data: {
                        sql: ace.edit("sql").session.getValue(),
                        id:id
                    },
                    dataType: 'json',
                    success: function (val) {
                        if (!val.success) {
                            showLayerTips4Confirm('error',val.message);
                        }
                        else {
                            layer.close(index);
                            showLayerTips('success','执行成功!','t');
                        }
                    }
                });
            },
            cancel:function (index) {
                layer.closeAll();
            }
        });
    }
</script>