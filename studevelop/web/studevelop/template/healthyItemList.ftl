<#import "/fw/macro/htmlcomponent.ftl" as htmlcom>
<div class="table-container-body">

    <table class="table table-bordered table-striped table-hover">
        <thead>
        <tr>
            <th width="20%">
                <input id="allCheck" type="checkbox" class="wp" value=""/>
                <span class="lbl"></span>
                选择
            </th>
            <th width="40%" >项目</th>
            <#if project?exists && project.projectType =='2'>
                <th width="10%">计量单位</th>
            </#if>

            <th width="10%" >操作</th>
        </tr>
        </thead>
        <tbody>
        <#if  itemList?exists && (itemList?size gt 0)>
            <#list itemList as item>
            <tr>
                <td >
                    <label class="pos-rel js-select">
                        <input name="stu-checkbox" type="checkbox" class="wp" value="${item.id!}"/>
                        <span class="lbl"></span>
                    </label>
                </td>
                <td >${item.projectName!}</td>
                <#if project?exists && project.projectType =='2'>
                    <td >${item.projectUnit!}</td>
                </#if>
                <td >

                    <a href="javascript:void(0)" onclick="editLink('${item.id}')">编辑</a>
                    <a href="javascript:void(0)" onclick="doDelete('${item.id}')">删除</a>
                    <#if item.isClosed == '0'>
                        <a href="javascript:void(0)" onclick="doSet('${item.id}','1')">关闭数据</a>
                    <#else>
                        <a href="javascript:void(0)" onclick="doSet('${item.id}','0')">打开数据</a>
                    </#if>

                </td>
            </tr>
            </#list>
        <#else>

        </#if>

        </tbody>
    </table>

    <#--<#if  itemList?exists && (itemList?size gt 0)>-->
        <#--<@htmlcom.pageToolBar container="#itemDiv"/>-->
    <#--</#if>-->

</div>
<script type="text/javascript">
    function doDelete(id) {
        showConfirmMsg('确认删除？','提示',function(){
            var options={
                url:"${request.contextPath}/stuDevelop/healthyHeart/projectItem/delete",
                dataType:"json",
                type:"post",
                data:{"id":id},
                success:function(data){
                    var jsonO = data;
                    if(!jsonO.success){
                        layerTipMsg(jsonO.success,"删除失败",jsonO.msg);
                        return;
                    }else{
                        layer.closeAll();
                        layer.msg(jsonO.msg, {
							offset: 't',
							time: 2000
						});
                        changeOptions();
                    }
                },
                clearForm:false,
                resetForm:false,
                error:function(XMLHttpRequest ,textStatus,errorThrown){}

            };
            $.ajax(options);
        });


    }
    function doSet(itemId , state){
        var msg="";
        if(state == '1'){
            msg="关闭数据";
        }else{
            msg="打开数据";
        }
            var options={
                url:"${request.contextPath}/stuDevelop/healthyHeart/projectItemDoClosed",
                dataType:"json",
                type:"post",
                data:{"id":itemId,"state":state},
                success:function(data){
                    var jsonO = data;
                    if(!jsonO.success){
                        layerTipMsg(jsonO.success,msg+"失败",jsonO.msg);
                        return;
                    }else{
                        layer.closeAll();
                        layerTipMsg(jsonO.success,msg+"成功",jsonO.msg);
                        changeOptions();
                    }
                },
                clearForm:false,
                resetForm:false,
                error:function(XMLHttpRequest ,textStatus,errorThrown){}

            };
            $.ajax(options);
    }
    $(function(){
        $("#allCheck").click(function(){
            if($("#allCheck").is(':checked')){
                $("#allCheck").prop("checked",true);
                $('.wp').prop("checked",true);
            }else{
                $("#allCheck").removeAttr("checked");
                $(".wp").removeAttr("checked");
            }
        });
    });
</script>