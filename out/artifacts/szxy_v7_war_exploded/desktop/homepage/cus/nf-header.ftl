<script type="text/javascript">
    try{ace.settings.check('navbar' , 'fixed')}catch(e){}
</script>
<div class="navbar-container clearfix" id="navbar-container">
    <div class="navbar-header">
        <a href="${logoUrl!}" class="navbar-brand">
        	<#if loginPic?default("") != "">
            <img class="logo" src="${request.contextPath}/${logoPic!}" alt="">
            </#if>
            <span>${logoName!}</span>
        </a>
    </div>
    <ul class="nav navbar-nav navbar-right">
        <#if appList?exists && appList?size gt 0>
            <#list appList as appName>
                <li id="app-${appName!}" <#if appName! == 'info'>class="navbar-item-user" </#if> >
                    <#if appName! == 'calendar'>
                        <a class="" href="javascript:;" onclick="openModel('66','日程表','1','${request.contextPath}/desktop/index/userCalendar','','','','');"  data-toggle="dropdown"  title="日程表">
                            <i class="wpfont icon-calendar"></i>
                        </a>
                    <#elseif appName! == 'setting'>
                       <#if !(isWenXuan?default(false))>
                        <a  class="" href="#" data-toggle="dropdown" onclick="openModel('99','设置','1','${request.contextPath}/desktop/index/userSetting','','','','');"   title="设置">
                            <i class="wpfont icon-cog"></i>
                        </a>
                       </#if>
                    <#else>
                        <script>
                            $(document).ready(function(){
                                $("#app-${appName!}").load("${request.contextPath}/desktop/index/header/${appName!}/page");
                            });
                        </script>
                    </#if>
                </li>
            </#list>
        </#if>
    </ul>

</div>