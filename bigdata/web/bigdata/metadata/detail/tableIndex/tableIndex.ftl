<input type="hidden" id="dbType" value="${dbType!}">
<div class="table-container">
    <div class="table-container-header clearfix mb-5">
        <button class="pull-right btn btn-lightblue add-data" onclick="editTableIndex('')">新增索引</button>
    </div>
    <div class="table-container-body">
        <table class="tables">
            <thead>
            <tr>
                <th>名称</th>
                <th>类型</th>
                <th>列</th>
                <th>操作</th>
            </tr>
            </thead>
            <tbody>
                <#if  tableIndexs?exists &&tableIndexs?size gt 0>
                <#list tableIndexs as item>
                <tr>
                    <td>${item.name!}</td>
                    <td>
                        <#if item.type?default('1') == '1'>普通索引</#if>
                        <#if item.type?default('1') == '2'>唯一索引</#if>
                        <#if item.type?default('1') == '3'>主键索引</#if>
                    </td>
                    <td title="${item.columns!}"><#if item.columns! !="" && item.columns?length gt 60>${item.columns?substring(0, 60)}......<#else>${item.columns!}</#if></td>
                    <td>
                        <a href="javascript:void(0);" onclick="executeTableIndex('${item.id!}')">执行</a><span class="tables-line">|</span>
                        <a href="javascript:void(0);" onclick="editTableIndex('${item.id!}')">编辑</a><span class="tables-line">|</span>
                        <a href="javascript:void(0);" onclick="deleteTableIndex('${item.id!}')">删除</a>
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
<div class="layer layer-tableIndex">
</div>
<script>
    function editTableIndex(id) {
        var metadataId = $('.directory-tree a.active').attr('id');
        var metadataName =$('.directory-tree a.active').find('span:last-child').text();
        var mdType = $('.filter-item.active').attr('mdType');
        var dbType = $('#dbType').val();
        $("#metadataDiv").load('${request.contextPath}/bigdata/metadata/tableIndexEdit?id=' + id + "&metadataId=" + metadataId + "&dbType=" + dbType);
        var isSubmit = false;
        layer.open({
            type: 1,
            shade: .6,
            title: id == '' ? '新增索引' : '修改索引',
            btn: ['保存', '取消'],
            yes: function (index, layero) {
                if (isSubmit) {
                    return;
                }

                if ($('#name').val() == null || $('#name').val() == '') {
                    layer.tips("不能为空", "#name", {
                        tipsMore: true,
                        tips: 3
                    });
                    isSubmit = false;
                    return;
                }

                if(/^[^a-zA-Z]/g.test($('#name').val())){
                    layer.tips("索引名称必须是字母开头", "#name", {
                        tipsMore: true,
                        tips: 3
                    });
                    isSubmit = false;
                    return;
                }

                if (!/^[0-9a-zA-Z_]{1,}$/.test($('#name').val())) {
                    layer.tips("索引名称不能包含特殊字符", "#name", {
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

                isSubmit = true;

                var options = {
                    url: "${request.contextPath}/bigdata/metadata/saveTableIndex",
                    dataType: 'json',
                    data:{column : $('#columnSelect').val(), metadataId:metadataId},
                    success: function (data) {
                        if (!data.success) {
                            showLayerTips4Confirm('error',data.message);
                            isSubmit = false;
                        } else {
                            showLayerTips('success','保存成功!','t');
                            layer.close(index);
                            // 获取id
                            var id = $('.directory-tree a.active').attr('id');
                            var url = "${request.contextPath}/bigdata/metadata/" + id + "/tableIndex";
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

    function deleteTableIndex(id) {
        showConfirmTips('prompt',"提示","您确定要删除该索引吗？",function(){
            $.ajax({
                url: '${request.contextPath}/bigdata/metadata/deleteTableIndex',
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
                        var url = "${request.contextPath}/bigdata/metadata/" + id + "/tableIndex";
                        $("#metadataDetailDiv").load(url);
                    }
                }
            });
        });
    }
    
    function executeTableIndex(id) {

        var dbType = $('#dbType').val();
        if (dbType == 'hbase') {
            $.ajax({
                url: '${request.contextPath}/bigdata/metadata/executeTableIndex',
                type: 'POST',
                data: {
                    dbType:dbType,
                    id:id
                },
                dataType: 'json',
                success: function (val) {
                    if (!val.success) {
                        showLayerTips4Confirm('error',val.message);
                    }
                    else {
                        showLayerTips('success','执行成功!','t');
                    }
                }
            });
            return;
        }

        var url =  _contextPath + '/bigdata/metadata/executeTableIndexUI?id=' + id;

        $(".layer-tableIndex").html("<div class='pos-middle-center'><h4><img src='${request.contextPath}/static/ace/img/loading.gif' />加载中......</h4></div>");
        $(".layer-tableIndex").load(url);
        layer.open({
            type: 1,
            shade: .5,
            title: ['创建索引','font-size:16px'],
            area: ['600px','300px'],
            btn:['执行', '取消'],
            content: $('.layer-tableIndex'),
            resize:true,
            yes:function (index) {
                $.ajax({
                    url: '${request.contextPath}/bigdata/metadata/executeTableIndex',
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