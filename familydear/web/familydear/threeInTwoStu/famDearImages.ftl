<#if actDetails?exists&& (actDetails?size > 0)>
    <#list actDetails as ad>
        <span class="position-relative float-left mr10 mb10">
		<a class="pull-left">
             <img id ="" style="width: 200px;height: 94px"  data-img-action="adapte" layer-src="${request.contextPath}/familydear/threeInTwoStu/showPic?id=${ad.id!}&showOrigin=1" src="${request.contextPath}/familydear/threeInTwoStu/showPic?id=${ad.id!}&showOrigin=0" alt="">
         </a>
        <a class="pos-abs" style="top: -10px;right: -6px;" onclick="delPic1('${ad.id}')">
            <i class="fa fa-times-circle color-red"></i>
        </a>
        </span>
    </#list>
</#if>
<script>
    $(function () {
        layer.photos({
            shade: .6,
            photos:'.js-layer-photos',
            shift: 5
        });
    });
    function delPic1(id){
        var picIds;
        picIds = $("#picIds").val()+","+id;
        $("#picIds").val(picIds)
        $.ajax({
            url:'${request.contextPath}/familydear/threeInTwoStu/delPic',
            data: {"id":id},
            type:'post',
            success:function(data) {
                var jsonO = JSON.parse(data);
                if(jsonO.success){
                    refreshPic(index);
                }
                else{
                    
                }
            },
            error:function(XMLHttpRequest, textStatus, errorThrown){}
        });
    }
</script>