<title>区域直通图片设置</title>
<#import "/fw/macro/webmacro.ftl" as w>

<#--友情链接-->
<div class="row">
    <div class="col-xs-12">
        <#--新增，删除等-->
        <div class="filter " >
            <#if isInsert?default(true)>
                <@btnItem btnId="btn-add" btnValue="新增" btnClass="fa-plus"/>
            </#if>
            <#--<@btnItem btnId="btn-remove" btnValue="刪除" btnClass="fa-remove"/>-->
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
    });
</script>

<#macro btnItem btnValue divClass="pull-left" btnClass="" btnId="">
<div class="filter-item  ${divClass!}">
    <@w.btn btnId="${btnId!}" btnValue="${btnValue!}" btnClass="${btnClass!}" />
</div>
</#macro>