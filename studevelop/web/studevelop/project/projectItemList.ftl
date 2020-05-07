<#import "/fw/macro/htmlcomponent.ftl" as htmlcom>
<div class="table-container-body">
    <table class="table table-bordered table-striped table-hover">
        <thead>
        <tr>
            <th width="10%" >排序号</th>
            <th width="30%" >项目</th>
            <th width="20%" >显示对象</th>
            <th width="30%" >维护方式</th>
            <th width="10%" >操作</th>
        </tr>
        </thead>
        <tbody>
        <#if  itemList?exists && (itemList?size gt 0)>
            <#list itemList as item>
            <tr>
                <td >${item.sortNumber!}</td>
                <td >${item.itemName!}</td>
                <td class="t-center">
			          <#if item.objectType == '11'>
	             		 仅对学科类别显示
	                  <#else>
			                                仅对学科显示
	                  </#if>
                </td>
                 <td style="word-break: break-all;word-wrap: break-word;" ><#if item.singleOrInput?default("")=="1">输入<#else>单选：${item.optionNames!}</#if></td>
                <td >
                    <a href="javascript:void(0)" onclick="editLink('${item.id}')">编辑</a>
                    <a href="javascript:void(0)" onclick="doDelete('${item.id}')">删除</a>
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
                url:"${request.contextPath}/stuDevelop/commonProject/projectItem/delete",
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
                        searchList();
                    }
                },
                clearForm:false,
                resetForm:false,
                error:function(XMLHttpRequest ,textStatus,errorThrown){}

            };
            $.ajax(options);
        });


    }
    function editLink(id ){
        var url ="${request.contextPath}/studevelop/project/edit?id="+id;

        $("#projectEdit").load(url , function () {
            layer.open({
                type: 1,
                shade: 0.5,
                title: '新增',
                area: '500px',
                btn: ['确定','取消'],
                yes: function(index){
                    saveProject();
                },
                content: $('.layer-add')
            });
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
                url:"${request.contextPath}/stuDevelop/commonProject/projectItemDoClosed",
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