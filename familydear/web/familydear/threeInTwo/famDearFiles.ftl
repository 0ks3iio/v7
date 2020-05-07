<#if actDetails?exists&& (actDetails?size > 0)>
    <#list actDetails as ad>
    	<div><span>${ad.filename!}</span><a class="color-blue ml10" href="${request.contextPath}/familydear/threeInTwo/downFile?id=${ad.id!}&showOrigin=1">下载</a><span class="color-blue ml10">|</span><a class="color-blue ml10" onclick="delPic1('${ad.id}')">删除</a></div>
    </#list>
</#if>
<script>
    
    function delPic1(id){
        var picIds;
        picIds = $("#picIds").val()+","+id;
        $("#picIds").val(picIds)
        $.ajax({
            url:'${request.contextPath}/familydear/threeInTwo/delPic',
            data: {"id":id},
            type:'post',
            success:function(data) {
                var jsonO = JSON.parse(data);
                if(jsonO.success){
                    refreshFile();
                }
                else{
                    
                }
            },
            error:function(XMLHttpRequest, textStatus, errorThrown){}
        });
    }
</script>