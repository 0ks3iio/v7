<#import "/fw/macro/htmlcomponent.ftl" as htmlcom>
<#assign OBJECT_STATE_RELATIONED = stack.findValue("@net.zdsoft.familydear.common.FamDearConstant@OBJECT_STATE_RELATIONED") />
<#assign OBJECT_STATE_INITIA = stack.findValue("@net.zdsoft.familydear.common.FamDearConstant@OBJECT_STATE_INITIA") />
<form id="mannReForm">

        <table class="table table-bordered table-striped table-hover no-margin">
            <thead>
            <tr>
                <th><label class="pos-rel">
                        <input type="checkbox" class="wp" id="selectAllSubject">
                        <span class="lbl"></span>
                    </label>选择
                </th>
                <th>结亲对象</th>
                <th>结亲村</th>
                <th>手机号</th>
                <th>冻结时间</th>
                <th>操作</th>
            </tr>
            </thead>
            <tbody id="list">
            <#if objectList?exists && (objectList?size > 0)>
                <#list objectList as item>
                    <tr>
                        <td>

                            <label class="pos-rel">
                                <input name="objectId" type="checkbox" class="wp"  value="${item.id!}">
                                <span class="lbl"></span>
                            </label>
                        </td>

                        <td>${item.name!}</td>
                        <td>${item.village!}</td>
                        <td>${item.mobilePhone!}</td>
                        <td>${(item.modifyTime?string('yyyy-MM-dd'))?default('')}</td>
                        <td>
                            <a href="javascript:objMemberEdit('${item.id!}');" class="table-btn color-red">家庭成员</a>
                            <a href="javascript:historyList('${item.id!}');" class="table-btn color-red">历史结亲</a>
                            <a href="javascript:cancelFlozen('${item.id!}');" class="table-btn color-red">取消冻结</a>
                            <a href="javascript:del('${item.id!}');" class="table-btn color-red">删除</a>
                        </td>
                    </tr>
                </#list>
            </#if>
            </tbody>
        </table>
        <#if objectList?exists && (objectList?size > 0)>
            <@htmlcom.pageToolBar container="#showList"/>
        </#if>
</form>
<script>

    function objMemberEdit(id){
        var currentPageIndex = ${currentPageIndex!};
        var currentPageSize = ${currentPageSize!};
        var url = "${request.contextPath}/familydear/cadresRelation/objMemberLink?objId="+id+"&_pageIndex=" +currentPageIndex+ "&_pageSize="+currentPageSize;
        indexDiv = layerDivUrl(url,{title: "家庭成员",width:750,height:630});
    }
    function cancelFlozen(id) {
        var options={
            url:"${request.contextPath}/familydear/cadresRelation/objFlozen",
            dataType:"json",
            type:"post",
            data:{"objIds":id,"state":'${OBJECT_STATE_INITIA!}'},
            success:function(data){
                var jsonO = data;
                if(!jsonO.success){
                    layerTipMsg(jsonO.success,"冻结失败",jsonO.msg);
                    return;
                }else{
                    layer.closeAll();
                    layer.msg(jsonO.msg, {
                        offset: 't',
                        time: 2000
                    });
                    searchList();
                }
            },
            clearForm:false,
            resetForm:false,
            error:function(XMLHttpRequest ,textStatus,errorThrown){}

        };
        $.ajax(options);

    }
    function del(id){
        showConfirmMsg('确认删除？','提示',function(){
            var ii = layer.load();
            $.ajax({
                url: '${request.contextPath}/familydear/cadresRelation/objFlozenDel?',
                data: {'objIds':id},
                type:'post',
                success:function(data) {
                    layer.closeAll();
                    var jsonO = JSON.parse(data);
                    if(jsonO.success){
                        searchList();
                    }else{
                        layerTipMsg(jsonO.success,"失败",jsonO.msg);
                    }
                    layer.close(ii);
                },
                error:function(XMLHttpRequest, textStatus, errorThrown){}
            });
        });
    }
    function historyList(id){
        var url = "${request.contextPath}/familydear/cadresRelation/historyCadreLink?objId="+id;
        indexDiv = layerDivUrl(url,{title: "历史结亲",width:750,height:630});
    }


    //全选
    $('#selectAllSubject').on('click',function(){
        var total = $('#list [name="objectId"]:checkbox').length;
        var length = $('#list [name="objectId"]:checkbox:checked').length;
        if(length != total){
            $('#list [name="objectId"]:checkbox').prop("checked", "true");
            $(this).prop("checked", "true");
        }else{
            $('#list [name="objectId"]:checkbox').removeAttr("checked");
            $(this).removeAttr("checked");
        }
    });
</script>