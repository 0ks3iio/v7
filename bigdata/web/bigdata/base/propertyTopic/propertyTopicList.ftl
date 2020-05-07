<div class="filter-made mb-10">
    <div class="filter-item filter-item-right clearfix">
        <button class="btn btn-lightblue" onclick="addPropertyTopic();">新增资产主题</button>
    </div>
</div>
<#if propertyTopics?exists && propertyTopics?size gt 0>
    <table class="tables">
        <thead>
        <tr>
            <th>名称</th>
            <th>排序号</th>
            <th>描述</th>
            <th>操作</th>
        </tr>
        </thead>
        <tbody class="kanban-content">
        <#list propertyTopics as propertyTopic>
            <tr>
                <td title="${propertyTopic.name!?html}" style="width: 250px;">
                    <div style="width: 150px;" class="ellipsis">
                        ${propertyTopic.name!?html}
                    </div>
                </td>
                <td>${propertyTopic.orderId!}</td>
                <td title="${propertyTopic.remark!?html}">
                    <div style="width: 150px;" class="ellipsis">
                        ${propertyTopic.remark!?html}
                    </div>
                </td>
                <td>
                    <#if propertyTopic.isCustom! == 1>
                    <a href="javascript:void(0)" onclick="editPropertyTopic('${propertyTopic.id!}','${propertyTopic.name!?html?js_string}','${propertyTopic.remark!?html?js_string}','${propertyTopic.orderId!}')"  class="look-over">编辑</a><span class="tables-line">|</span>
                    <a href="javascript:void(0)" onclick="deletePropertyTopic('${propertyTopic.id!}','${propertyTopic.name!?html?js_string}');">删除</a>
                    </#if>
                </td>
            </tr>
        </#list>
        </tbody>
    </table>
<#else>
    <div class="no-data-common">
        <div class="text-center">
            <img src="${request.contextPath}/bigdata/v3/static/images/public/no-data-common-100.png"/>
            <p class="color-999">
                暂无资产主题信息
            </p>
        </div>
    </div>
</#if>
<div class="layer layer-editParam layui-layer-wrap" style="display: none;">
    <div class="layer-content">
        <div class="form-horizontal">
            <div class="form-group">
                <label class="col-sm-3 control-label no-padding-right"><font style="color:red;">*</font>名称：</label>
                <div class="col-sm-8">
                    <input type="text" id="propertyTopicName" class="form-control" maxlength="25">
                </div>
            </div>
            <div class="form-group">
                <label class="col-sm-3 control-label no-padding-right"><font style="color:red;">*</font>排序号：</label>
                <div class="col-sm-8">
                    <input type="text" id="propertyTopicOrderId" class="form-control" maxlength="3" oninput = "value=value.replace(/[^\d]/g,'')">
                </div>
            </div>
            <div class="form-group">
                <label class="col-sm-3 control-label no-padding-right">描述：</label>
                <div class="col-sm-8">
                    <textarea id="remark" maxlength="250" class="form-control" style="height:100px"></textarea>
                </div>
            </div>
        </div>
    </div>
</div>
<script>
    function addPropertyTopic() {
        $.ajax({
            url: '${request.contextPath}/bigdata/property/topic/getLargestOrderId',
            type: 'GET',
            success: function (result) {
                if (!result.success) {
                    showLayerTips4Confirm('error','获取当前排序号失败');
                } else {
                    $("#propertyTopicOrderId").val(result.data)
                }
            }
        });
        $("#propertyTopicName").val('');
        $("#remark").val('');
        layer.open({
            type: 1,
            shade: 0.5,
            title: '新增资产主题',
            area: '500px',
            btn: ['确定', '取消'],
            yes:function(index, layero){

                if ($('#propertyTopicName').val() == "") {
                    layer.tips("不能为空", "#propertyTopicName", {
                        tipsMore: true,
                        tips: 3
                    });
                    return;
                }
                if ($('#propertyTopicOrderId').val() == "") {
                    layer.tips("不能为空", "#propertyTopicOrderId", {
                        tipsMore: true,
                        tips: 3
                    });
                    return;
                }

                var params = {
                    name:$("#propertyTopicName").val(),
                    remark:$("#remark").val(),
                    orderId:$("#propertyTopicOrderId").val()
                };

                $.ajax({
                    url: '${request.contextPath}/bigdata/property/topic/addPropertyTopic',
                    type: 'POST',
                    data: params,
                    success: function (result) {
                        if (!result.success) {
                            showLayerTips4Confirm('error',result.message);
                        } else {
                            showLayerTips('success','新增成功!','t');
                            layer.close(index);
                            $('.page-content').load('${request.contextPath}/bigdata/property/topic/index');
                        }
                    }
                });
            },
            content: $('.layer-editParam')
        })
    }

    function editPropertyTopic(id,name,remark,orderId) {
        $("#propertyTopicName").val(name);
        $("#remark").val(remark);
        $("#propertyTopicOrderId").val(orderId)
        // var isSubmit = false;
        layer.open({
            type: 1,
            shade: 0.5,
            title: '编辑资产主题',
            area: '500px',
            btn: ['确定', '取消'],
            yes:function(index, layero){

                if ($('#propertyTopicName').val() == "") {
                    layer.tips("不能为空", "#propertyTopicName", {
                        tipsMore: true,
                        tips: 3
                    });
                    return;
                }
                if ($('#propertyTopicOrderId').val() == "") {
                    layer.tips("不能为空", "#propertyTopicOrderId", {
                        tipsMore: true,
                        tips: 3
                    });
                    return;
                }

                var params = {
                    id:id,
                    name:$("#propertyTopicName").val(),
                    remark:$("#remark").val(),
                    orderId:$("#propertyTopicOrderId").val()
                };

                $.ajax({
                    url: '${request.contextPath}/bigdata/property/topic/editPropertyTopic',
                    type: 'POST',
                    data: params,
                    success: function (result) {
                        if (!result.success) {
                            showLayerTips4Confirm('error',result.message);
                        } else {
                            showLayerTips('success','编辑成功!','t');
                            layer.close(index);
                            $('.page-content').load('${request.contextPath}/bigdata/property/topic/index');
                        }
                    }
                });
            },
            content: $('.layer-editParam')
        })
    }

    function deletePropertyTopic(id,name){
        showConfirmTips('prompt',"提示","您确定要删除"+name+"吗？",function(){
            $.ajax({
                url:"${request.contextPath}/bigdata/property/topic/deletePropertyTopic",
                data:{
                    'id':id
                },
                type:"post",
                clearForm : false,
                resetForm : false,
                dataType: "json",
                success:function(result){
                    layer.closeAll();
                    if(!result.success){
                        showLayerTips4Confirm('error',result.message);
                    }else{
                        showLayerTips('success',result.message,'t');
                        $('.page-content').load('${request.contextPath}/bigdata/property/topic/index');
                    }
                },
                error:function(XMLHttpRequest, textStatus, errorThrown){}
            });
        });
    }
</script>