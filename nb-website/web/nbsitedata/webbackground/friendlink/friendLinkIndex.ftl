<title>友情链接</title>
<#import "/fw/macro/webmacro.ftl" as w>

<#--友情链接-->
<div class="row">
    <div class="col-xs-12">
        <#--新增，删除等-->
        <div class="filter " >
            <@btnItem btnId="btn-add" btnValue="新增" btnClass="fa-plus"/>
            <@btnItem btnId="btn-remove" btnValue="刪除" btnClass="fa-remove"/>
        </div>

        <#--列表-->
        <div class="row listDiv"></div>
    </div>
</div>

<script>
    $('.page-content-area').ace_ajax('loadScripts', [], function() {
        $('.listDiv').load('${request.contextPath}/sitedata/${type!}/list/page');
        $("#btn-add").on("click",function () {
            var url = "${request.contextPath}/sitedata/${type!}/addoredit/page?articleId=";
            var indexDiv = layerDivUrl(url,{height:270,width:600,title:'新增'});
            resizeLayer(indexDiv, 270, 600);
        });

        $("#btn-remove").on("click",function(){
            var ids = getIds();
            if(ids.length){
                var isDelete = false;
                layer.confirm('确认删除所选数据?',{icon:3,title:'提示'} ,function(index){
                    layer.close(index);
                    $.ajax({
                        url:'${request.contextPath}/sitedata/${type!}/delete?ids='+JSON.stringify(ids),
                        type:'post',
                        cache:false,
                        contentType: "application/json",
                        success:function(data) {
                            var jsonO = JSON.parse(data);
                            if(!jsonO.success){
                                showMsgError(jsonO.msg,"操作失败!",function(index){
                                    layer.close(index);
                                    //$("btn-remove").removeClass("disabled");
                                });
                            }
                            else{
                                // 需要区分移动端和非移动端返回处理不一样
                                showMsgSuccess(jsonO.msg,"操作成功!",function(index){
                                    layer.close(index);
                                    $(".listDiv").load('${request.contextPath}/sitedata/${type!}/list/page');
                                });

                            }
                        }
                    });
                });
            }else{
                alert("请选择数据");
            }
        });
    });

    function getIds(){
        var ids = new Array();
        $(".cbx-td").each(function(){
            if($(this).find("span").attr("chk")=="true"){
                ids.push($(this).find("input").attr("id"));
            }
        });
        return ids;
    }
</script>

<#macro btnItem btnValue divClass="pull-left" btnClass="" btnId="">
<div class="filter-item  ${divClass!}">
    <@w.btn btnId="${btnId!}" btnValue="${btnValue!}" btnClass="${btnClass!}" />
</div>
</#macro>