<div class="table-container-body">
    <table class="table table-bordered table-striped table-hover">
        <thead>
        <tr>
            <th>评估项目</th>
            <th>选项</th>
            <th>操作</th>
        </tr>
        </thead>
        <tbody>
        <#assign arrs = ["A","B","C","D","E"] >
        <#if  itemList?exists && (itemList?size gt 0)>
                <#list itemList as item>
                <tr>
                    <td width="200">${item.itemName!}</td>
                    <td>
                        <#if item.codeList?exists && ( item.codeList?size gt 0)>
                            <#list item.codeList as code >
                                ${arrs[code_index]}.${code.codeContent!}&emsp;&emsp;
                            </#list>
                        </#if>
                        </td>
                    <td width="150">
                        <a href="javascript:void(0)" onclick="editLink('${item.id}','true')">复制</a>
                        <a href="javascript:void(0)" onclick="editLink('${item.id}','false')">编辑</a>
                        <a href="javascript:void(0)" onclick="doDelete('${item.id}')">删除</a>
                    </td>
                </tr>
                </#list>
        <#else>

        </#if>

        </tbody>
    </table>
</div>
<script type="text/javascript">
    /**
    function doEdit(id) {
        $("#editDiv").load("${request.contextPath}/studevelop/performanceItem/getPerformItemById?itemId=" + id);
        var gradeCode = $("#gradeUl").find("li[class = 'active']").find("a").attr("val");
        layer.open({
            type: 1,
            offset:"t",
            shade: .5,
            title:'添加评估项目',
            btn :['确定','取消'],
            btn1:function(index ,layero){
                var options={
                    url:"${request.contextPath}/studevelop/performanceItem/save",
                    dataType:"json",
                    type:"post",
                    data:{"itemGrade":gradeCode},
                    success:function(data){
                        var jsonO = data;
                        if(!jsonO.success){
                            layerTipMsg(jsonO.success,"保存失败",jsonO.msg);
                            return;
                        }else{
                            layer.closeAll();
                            layerTipMsg(jsonO.success,"保存成功",jsonO.msg);
                            $("#itemDiv").load("${request.contextPath}/studevelop/performanceItem/list/page?gradeCode=" + gradeCode);
                        }
                    },
                    clearForm:false,
                    resetForm:false,
                    error:function(XMLHttpRequest ,textStatus,errorThrown){}

                };
                $("#performForm").ajaxSubmit(options);
                return false;
            },
            btn2:function(index ,layro){
                layer.closeAll();
            },
            area: '360px',
            content: $('.layer-addScoreItem')
        });
    }
     **/
    function doDelete(id) {
        showConfirmMsg('确认删除？','提示',function(){
            var options={
                url:"${request.contextPath}/studevelop/performanceItem/delete",
                dataType:"json",
                type:"post",
                data:{"itemId":id},
                success:function(data){
                    var jsonO = data;
                    if(!jsonO.success){
                        layerTipMsg(jsonO.success,"删除失败",jsonO.msg);
                        return;
                    }else{
                        layer.closeAll();
                        layerTipMsg(jsonO.success,"删除成功",jsonO.msg);
                        var gradeCode = $("#gradeUl").find("li[class = 'active']").find("a").attr("val");
                        $("#itemDiv").load("${request.contextPath}/studevelop/performanceItem/list/page?gradeCode=" + gradeCode);
                    }
                },
                clearForm:false,
                resetForm:false,
                error:function(XMLHttpRequest ,textStatus,errorThrown){}

            };
            $.ajax(options);
        });


    }
    function doCopy(data){
        editLink(data ,true);
    }
</script>