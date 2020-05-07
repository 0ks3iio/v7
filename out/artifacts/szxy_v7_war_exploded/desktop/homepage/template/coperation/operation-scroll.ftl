<div class="col-sm-${(data.col)!12}">
    <div class="box box-default">
        <div class="box-header">
            <h4 class="box-title">${(data.title)!}</h4>
        </div>
        <div class="box-body" <#--style="height: ${data.height!}px;"-->>
            <ul class="app-list clearfix">
            	<#if data?? && data.apps?exists>
            	<#list data.apps as app>
                <li class="app">
                    <a href="#" onclick="openModel('${app.id!}','${app.name!}','${app.openType!}','${app.url!}','','','','');">
                        <img width="20" height="20" src="${app.icon}" alt="" class="app-img">
                        <span class="app-name">${app.name}</span>
                    </a>
                </li>
                </#list>
                </#if>
            </ul>
        </div>
        <#--<div class="box-tools dropdown">-->
            <#--<a class="box-tools-btn" href="javascript:void(0);" data-toggle="dropdown"><i class="wpfont icon-ellipsis"></i></a>-->
            <#--<ul class="box-tools-menu dropdown-menu">-->
                <#--<span class="box-tools-menu-angle"></span>-->
                <#--<li><a class="js-box-setting" href="javascript:void(0);" id="commonUserAppSet">设置</a></li>-->
                <#--<li><a class="js-box-remove" href="javascript:void(0);" id="commonUserAppDel">删除</a></li>-->
            <#--</ul>-->
        <#--</div>-->
    </div>
</div>