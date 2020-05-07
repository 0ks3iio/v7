<#import  "/fw/macro/webmacro.ftl" as w />

<#macro top topAdmin superAdmin>
<#-- 顶级管理员增加超管权限 -->
    <#if topAdmin?default(false)>
                    <li>
                        <a id="developerManage" href="javascript:;" data-show="superId" class="dropdown-toggle system-content">
                            <img class="menu-icon" src="${resourceUrl}/images/icons/app-icon/icon01-sm.png" alt="">
                            <span class="menu-text">应用系统</span>
                            <b class="arrow fa fa-angle-down"></b>
                        </a>
                        <ul class="submenu">
                            <li>
                                <a id="super1" href="javascript:;"
                                   onclick="openModel('super1','开发者管理','1','${request.contextPath}/system/developer/index','应用系统','','superId','');">开发者管理</a>
                            </li>
                            <li>
                                <a id="super1" href="javascript:;"
                                   onclick="openModel('super2','应用管理','1','${request.contextPath}/system/server/index','应用系统','','superId','');">应用管理</a>
                            </li>
                            <li>
                                <a id="super1" href="javascript:;"
                                   onclick="openModel('super2','应用授权','1','${request.contextPath}/system/serverAuthorize/index','应用系统','','superId','');">应用授权</a>
                            </li>
                            <li>
                                <a id="super1" href="javascript:;"
                                   onclick="openModel('super2','常见问题','1','${request.contextPath}/system/problem/index','应用系统','','superId','');">常见问题</a>
                            </li>
                        <#--<li>-->
                        <#--<a id="super1" href="javascript:;"-->
                        <#--onclick="openModel('super2','缓存管理','1','${request.contextPath}/redis/index','缓存管理','','superId','');">缓存管理</a>-->
                        <#--</li>-->
                            <li>
                                <a id="super1" href="javascript:;"
                                   onclick="openModel('super2','接口维护','1','${request.contextPath}/system/interface/index','应用系统','','superId','');">接口维护</a>
                            </li>
                            <li>
                                <a id="super1" href="javascript:;"
                                   onclick="openModel('super2','接口次数','1','${request.contextPath}/developer/interface/count/index','应用系统','','superId','');">接口次数</a>
                            </li>
                        </ul>
                    </li>
    </#if>
    <#if superAdmin?default(false)>
                    <li>
                        <a id="deployManage" href="javascript:;" data-show="superId" class="dropdown-toggle system-content">
                            <img class="menu-icon" src="${resourceUrl}/images/icons/app-icon/icon01-sm.png" alt="">
                            <span class="menu-text">部署工具</span>
                            <b class="arrow fa fa-angle-down"></b>
                        </a>
                        <ul class="submenu">
                            <li>
                                <a id="super1" href="javascript:;"
                                   onclick="openModel('super2','密码重置','1','${request.contextPath}/system/ops/resetPassword?fromDesktop=true','','','superId','');">密码重置</a>
                            </li>
                            <li>
                                <a id="super1" href="javascript:;"
                                   onclick="openModel('super2','系统参数设置','1','${request.contextPath}/system/ops/sysOption/tab?fromDesktop=true','','','superId','');">系统参数设置</a>
                            </li>
                            <li>
                                <a id="super1" href="javascript:;"
                                   onclick="openModel('super2','序列号激活','1','${request.contextPath}/system/ops/init?fromDesktop=true','','','superId','');">序列号激活</a>
                            </li>
                            <li>
                                <a id="super1" href="javascript:;"
                                   onclick="openModel('super2','应用列表','1','${request.contextPath}/system/ops/serverList?fromDesktop=true','','','superId','');">应用列表</a>
                            </li>
                            <li>
                                <a id="super1" href="javascript:;"
                                   onclick="openModel('super2','多域名设置','1','${request.contextPath}/system/ops/serverRegion/index?fromDesktop=true','','','superId','');">多域名设置</a>
                            </li>
                            <li>
                                <a id="super1" href="javascript:;"
                                   onclick="openModel('super2','登录页设置','1','${request.contextPath}/ops/loginSet/index?fromDesktop=true','','','superId','');">登录页设置</a>
                            </li>
                        </ul>
                    </li>
    </#if>
</#macro>

<#if isWenXuan?default(false) >
	<script src="http://smart.winshareyun.cn/winshare-web-portal/static/ws-app-link/ws_app.js"></script>
</#if>
<@w.commonWeb title="${title!}" showFramework=true desktopIndex=true fullLoad=true>
<div id="navbar" class="navbar navbar-default navbar-fixed-top">

    <script type="text/javascript">
        try {
            ace.settings.check('navbar', 'fixed')
        } catch (e) {
        }
    </script>
    <div class="navbar-container clearfix" id="navbar-container">
        <div class="navbar-header">
            <a href="${logoUrl!}" class="navbar-brand">
        	<#if logoPic?default("") != "">
            <img class="logo" src="${request.contextPath}/${logoPic!}" alt="">
            </#if>
                <span>${logoName!}</span>
            </a>
        </div>
        <ul class="nav navbar-nav navbar-right">
        <#if appList?exists && appList?size gt 0>
            <#list appList as appName>
                <li id="app-${appName!}">
                    <#if appName! == 'calendar'>
                        <a class="" href="javascript:;"
                           onclick="openModel('66','日程表','1','${request.contextPath}/desktop/index/userCalendar','','','','');"
                           data-toggle="dropdown" title="日程表">
                            <i class="wpfont icon-calendar"></i>
                        </a>
                    <#elseif appName! == 'setting'>
                        <#if !isWenXuan?default(false)>
                        <a class="" href="#" data-toggle="dropdown"
                           onclick="openModel('99','设置','1','${request.contextPath}/desktop/index/userSetting','','','','');"
                           title="设置">
                            <i class="wpfont icon-cog"></i>
                        </a>
                        </#if>
                    <#else>
                        <script>
                            $(document).ready(function () {
                                $("#app-${appName!}").load("${request.contextPath}/desktop/index/header/${appName!}/page");
                            });
                        </script>
                    </#if>
                </li>
            </#list>

            <li id="app-${appName!}">
                <a class="js-addModule" href="#" title="添加模块">
                    <i class="wpfont icon-module"></i>
                </a>
            </li>

            <li id="app-info" class="navbar-item-user">
                <a href="javascript:;" class="" data-toggle="dropdown">
                    <img class="nav-user-photo" src="${avatarUrl!}" style="width: 39px;height: 39px;"/>
                    <span class="user-info">${realName!}</span>

                    <i class="wpfont icon-caret-down"></i>
                </a>
			 <#if !(isWenXuan?default(false))>
				<div class="dropdown-menu dropdown-menu-user">
                    <ul class="user-list user-list-hover">
						<#if multiIdentity?exists && multiIdentity?size gt 0>
							<#list multiIdentity as u>
				                <li id="li-${u.userName!}">
                                    <img src="${u.avatarUrl!}" alt="" class="user-avatar"
                                         style="width: 39px;height: 39px;">
                                    <h5 class="user-name">${u.realName!}</h5>
                                    <span class="label <#if u.role?default(-1) == 3>label-blue<#elseif u.role?default(-1)==2>label-green<#elseif u.role?default(-1)==9>label-orange<#else>label-yellow</#if>">${u.roleName!}</span>
                                    <div class="user-content">${u.unitName!}</div>
                                    <a href="javascript:;" class="user-switch"><#if u.id == loginInfoUserId!>当前账号<#else>
                                        切换</#if></a>
                                </li>
                            </#list>
                        </#if>
                    </ul>
				    <#if !(isHide?default(false))>
				      <a href="${request.contextPath}/homepage/logout/page?psId=${passportSessionId!}"
                         class="quit">退出账号</a>
                    </#if>
                </div>
             </#if>
            </li>
        </#if>
        </ul>

    </div>

</div>

<div class="main-container" id="main-container">
    <script type="text/javascript">
        try {
            ace.settings.check('main-container', 'fixed')
        } catch (e) {
        }
    </script>
    <#if hasClassifies?default(false)>
    <ul class="superNav <#if deployRegion?default('')=='xingyun'>nebulaul</#if>">
        <#if classifies?exists && classifies?size gt 0>
        <#list classifies as classify>
        <li data-classify="${classify.unitServerClassify.id}" class="classify">
            <a class="<#if classify_index==0>active</#if> classify" href="javascript:void(0);">
                <img class="superNav-icon" src="${request.contextPath}${classify.unitServerClassify.iconPath!}" alt="">
                <span class="superNav-text">${classify.unitServerClassify.name}</span>
            </a>
        </li>
        </#list>
        </#if>

    </ul>
    </#if>

    <div id="sidebar" class="sidebar responsive sidebar-fixed">
        <script type="text/javascript">
            try {
                ace.settings.check('sidebar', 'fixed')
            } catch (e) {
            }
        </script>
        <div class="sidebar-shortcuts">
        <#--<button>
            <i class="wpfont icon-home-fill"></i>
        </button>-->
            <a href="javascript:" class="btn btn-tohome js-siderbar-collapse">
                <i class="wpfont icon-dedent"></i>
            </a>
        </div>
        <#if hasClassifies?default(false)>
            <div class="nav-wrap js-nav-wrap " >
            <#if classifies?exists && classifies?size gt 0>
                <#list classifies as classify>
                    <ul class="nav nav-list <#if classify_index!=0>hide</#if>" id="${classify.unitServerClassify.id}">
                    <#--子系统列表-->
                    <#if classify.serverDtos?exists && classify.serverDtos?size gt 0>
                        <#list classify.serverDtos as serverDto>
                            <li>
                                <a href="javascript:;" data-show="${serverDto.server.id!}" class="dropdown-toggle system-content"
                                   serverId="${serverDto.server.id!}" serverName="${serverDto.server.name!}"
                                   serverOpenType="${serverDto.openType!}"
                                   serverUrlIndex="<#if secondUrl>${serverDto.urlIndexSecond!}<#else>${serverDto.urlIndex!}</#if><#if (serverDto.openType)!=1 && serverDto.urlIndex?default("") != "" >&uid=${userName!}</#if>">
                                    <img class="menu-icon" src="${serverDto.imageUrl!}" alt="" width="20" height="20">
                                    <span class="menu-text">${serverDto.server.name!}</span>
                                    <b class="arrow fa fa-angle-down"></b>
                                </a>
                                <ul class="submenu">
                                    <#if serverDto.modelDtos?exists && serverDto.modelDtos?size gt 0>
                                        <#if serverDto.dir?default(true)>
                                            <#list  serverDto.modelDtos as modelDto>
                                            <li>
                                                <a href="javascript:;" class="dropdown-toggle" id="${modelDto.model.id!}">
                                                    ${modelDto.model.name!}
                                                    <b class="arrow fa fa-angle-down"></b>
                                                </a>
                                                <#if modelDto.subModelDtos?exists && modelDto.subModelDtos?size gt 0>
                                                    <ul class="submenu">
                                                        <#list modelDto.subModelDtos as subModel>
                                                            <li>
                                                                <a id="${subModel.model.id!}" href="javascript:;"
                                                                   onclick="openModel('${subModel.model.id!}','${subModel.model.name!}','${subModel.model.openType!}','${subModel.fullUrl!}','${serverDto.server.name!}','${modelDto.model.name!}','${serverDto.server.id}','${modelDto.model.id!}');">${subModel.model.name!}</a>
                                                            </li>
                                                        </#list>
                                                    </ul>
                                                </#if>
                                            </li>
                                            </#list>
                                        <#else>
                                            <#list  serverDto.noDirModelDtos as modelDto>
                                                <li>
                                                    <a id="${modelDto.model.id!}" href="javascript:;"
                                                       onclick="openModel('${modelDto.model.id!}','${modelDto.model.name!}','${modelDto.model.openType!}','${modelDto.fullUrl!}','${serverDto.server.name!}','','${serverDto.server.id}','');">${modelDto.model.name!}</a>
                                                </li>
                                            </#list>
                                        </#if>
                                    </#if>
                                </ul>
                            </li>
                        </#list>
                    </#if>
                    <#if classify.system?default(false)>
                        <@top topAdmin=topAdmin superAdmin=superAdmin />
                    </#if>
                    </ul>
                </#list>
            <#else>
                <ul class="nav nav-list" >
                    <@top topAdmin=topAdmin superAdmin=superAdmin />
                </ul>
            </#if>
            </div>
        <#else >

    <#--常用操作-->
        <div class="nav-wrap js-nav-wrap">
    <#if isDesktopShow ?default(true)>
	    <div class="common-module">
            <ul class="nav nav-list">
            <#if appId == -1>
            <li>
                <a id="commonModel-73101" href="javascript:;" onclick="goHome();">
                    <img class="menu-icon" src="${request.contextPath}/desktop/images/icon-desktop-default.png" alt="">
                    <span class="menu-text">桌面</span>
                </a>
            </li>
            </#if>
            
    		<#if commonModelDtos?exists && commonModelDtos?size gt 0>
                <#list commonModelDtos as commonModelDto>
				<li class="">
                    <a id="commonModel-${commonModelDto.model.id!}" href="javascript:;"
                       onclick="openModel('${commonModelDto.model.id!}','${commonModelDto.model.name!}','${commonModelDto.model.openType!}','${commonModelDto.fullUrl!}','','','','');">
                        <img class="menu-icon" src="${commonModelDto.imageUrl!}" alt="">
                        <span class="menu-text">${commonModelDto.model.name!}</span>
                    </a>
                </li>
                </#list>
            </#if>
            </ul>
        </div>
    </#if>
            <ul class="nav nav-list">
            <#--子系统列表-->
    <#if serverDtos?exists && serverDtos?size gt 0>
        <#list serverDtos as serverDto>
            <li>
                <a href="javascript:;" data-show="${serverDto.server.id!}" class="dropdown-toggle system-content"
                   serverId="${serverDto.server.id!}" serverName="${serverDto.server.name!}"
                   serverOpenType="${serverDto.openType!}"
                   serverUrlIndex="<#if secondUrl>${serverDto.urlIndexSecond!}<#else>${serverDto.urlIndex!}</#if><#if (serverDto.openType)!=1 && serverDto.urlIndex?default("") != "" >&uid=${userName!}</#if>">
                    <img class="menu-icon" src="${serverDto.imageUrl!}" alt="" width="20" height="20">
                    <span class="menu-text">${serverDto.server.name!}</span>
                    <b class="arrow fa fa-angle-down"></b>
                </a>
                <ul class="submenu">
                    <#if serverDto.modelDtos?exists && serverDto.modelDtos?size gt 0>
                        <#if serverDto.dir?default(true)>
                            <#list  serverDto.modelDtos as modelDto>
                            <li>
                                <a href="javascript:;" class="dropdown-toggle" id="${modelDto.model.id!}">
                                    ${modelDto.model.name!}
                                    <b class="arrow fa fa-angle-down"></b>
                                </a>
                                <#if modelDto.subModelDtos?exists && modelDto.subModelDtos?size gt 0>
                                    <ul class="submenu">
                                        <#list modelDto.subModelDtos as subModel>
                                            <li>
                                                <a id="${subModel.model.id!}" href="javascript:;"
                                                   onclick="openModel('${subModel.model.id!}','${subModel.model.name!}','${subModel.model.openType!}','${subModel.fullUrl!}','${serverDto.server.name!}','${modelDto.model.name!}','${serverDto.server.id}','${modelDto.model.id!}');">${subModel.model.name!}</a>
                                            </li>
                                        </#list>
                                    </ul>
                                </#if>
                            </li>
                            </#list>
                        <#else>
                            <#list  serverDto.noDirModelDtos as modelDto>
                                <li>
                                    <a id="${modelDto.model.id!}" href="javascript:;"
                                       onclick="openModel('${modelDto.model.id!}','${modelDto.model.name!}','${modelDto.model.openType!}','${modelDto.fullUrl!}','${serverDto.server.name!}','','${serverDto.server.id}','');">${modelDto.model.name!}</a>
                                </li>
                            </#list>
                        </#if>
                    </#if>
                </ul>
            </li>
        </#list>
    </#if>
            <#-- 顶级管理员增加超管权限 -->
    <#if topAdmin?default(false)>
        <li>
            <a id="developerManage" href="javascript:;" data-show="superId" class="dropdown-toggle system-content">
                <img class="menu-icon" src="${resourceUrl}/images/icons/app-icon/icon01-sm.png" alt="">
                <span class="menu-text">应用系统</span>
                <b class="arrow fa fa-angle-down"></b>
            </a>
            <ul class="submenu">
                <li>
                    <a id="super1" href="javascript:;"
                       onclick="openModel('super1','开发者管理','1','${request.contextPath}/system/developer/index','应用系统','','superId','');">开发者管理</a>
                </li>
                <li>
                    <a id="super1" href="javascript:;"
                       onclick="openModel('super2','应用管理','1','${request.contextPath}/system/server/index','应用系统','','superId','');">应用管理</a>
                </li>
                <li>
                    <a id="super1" href="javascript:;"
                       onclick="openModel('super2','应用授权','1','${request.contextPath}/system/serverAuthorize/index','应用系统','','superId','');">应用授权</a>
                </li>
                <li>
                    <a id="super1" href="javascript:;"
                       onclick="openModel('super2','常见问题','1','${request.contextPath}/system/problem/index','应用系统','','superId','');">常见问题</a>
                </li>
            <#--<li>-->
            <#--<a id="super1" href="javascript:;"-->
            <#--onclick="openModel('super2','缓存管理','1','${request.contextPath}/redis/index','缓存管理','','superId','');">缓存管理</a>-->
            <#--</li>-->
                <li>
                    <a id="super1" href="javascript:;"
                       onclick="openModel('super2','接口维护','1','${request.contextPath}/system/interface/index','应用系统','','superId','');">接口维护</a>
                </li>
                <li>
                    <a id="super1" href="javascript:;"
                       onclick="openModel('super2','接口次数','1','${request.contextPath}/developer/interface/count/index','应用系统','','superId','');">接口次数</a>
                </li>
            </ul>
        </li>
    </#if>
    <#if superAdmin?default(false)>
        <li>
            <a id="deployManage" href="javascript:;" data-show="superId" class="dropdown-toggle system-content">
                <img class="menu-icon" src="${resourceUrl}/images/icons/app-icon/icon01-sm.png" alt="">
                <span class="menu-text">部署工具</span>
                <b class="arrow fa fa-angle-down"></b>
            </a>
            <ul class="submenu">
                <li>
                    <a id="super1" href="javascript:;"
                       onclick="openModel('super2','密码重置','1','${request.contextPath}/system/ops/resetPassword?fromDesktop=true','','','superId','');">密码重置</a>
                </li>
                <li>
                    <a id="super1" href="javascript:;"
                       onclick="openModel('super2','系统参数设置','1','${request.contextPath}/system/ops/sysOption/tab?fromDesktop=true','','','superId','');">系统参数设置</a>
                </li>
                <li>
                    <a id="super1" href="javascript:;"
                       onclick="openModel('super2','序列号激活','1','${request.contextPath}/system/ops/init?fromDesktop=true','','','superId','');">序列号激活</a>
                </li>
                <li>
                    <a id="super1" href="javascript:;"
                       onclick="openModel('super2','应用列表','1','${request.contextPath}/system/ops/serverList?fromDesktop=true','','','superId','');">应用列表</a>
                </li>
                <li>
                    <a id="super1" href="javascript:;"
                       onclick="openModel('super2','多域名设置','1','${request.contextPath}/system/ops/serverRegion/index?fromDesktop=true','','','superId','');">多域名设置</a>
                </li>
                <li>
                    <a id="super1" href="javascript:;"
                       onclick="openModel('super2','登录页设置','1','${request.contextPath}/ops/loginSet/index?fromDesktop=true','','','superId','');">登录页设置</a>
                </li>
            </ul>
        </li>
    </#if>
            </ul>
        </div>
        <div class="subNav-modal js-subNav-modal">
        <#--子模块-->
            <div class="subNav-modal-container">
    <#if serverDtos?exists && serverDtos?size gt 0>
        <#list serverDtos as serverDto>
            <div class="subNav" data-id="${serverDto.server.id!}">
                <h3 class="subNav-name">${serverDto.server.name!}</h3>
                <div class="subNav-container">
                    <div class="subNav-item">
                        <#if serverDto.modelDtos?exists && serverDto.modelDtos?size gt 0>
                            <#list  serverDto.modelDtos as modelDto>
                                <h5 class="subNav-item-name">${modelDto.model.name!}</h5>
                                <ul class="subNav-list clearfix">
                                    <#if modelDto.subModelDtos?exists && modelDto.subModelDtos?size gt 0>
                                        <#list modelDto.subModelDtos as subModel>
                                            <li><a href="javascript:;"
                                                   onclick="openModel('${subModel.model.id!}','${subModel.model.name!}','${subModel.model.openType!}','${subModel.fullUrl!}','${serverDto.server.name!}','${modelDto.model.name!}','${serverDto.server.id}','${modelDto.model.id!}');">${subModel.model.name!}</a>
                                            </li>
                                        </#list>
                                    </#if>
                                </ul>
                            </#list>
                        </#if>
                    </div>
                </div>
            </div>
        </#list>
    </#if>
            </div>
        </div>
        </#if>
    <#--<div class="sidebar-toggle sidebar-collapse js-siderbar-collapse" id="sidebar-collapse">-->
    <#--<i class="wpfont icon-angle-double-left"></i>-->
    <#--</div>-->
        <script type="text/javascript">
            try {
                ace.settings.check('sidebar', 'collapsed')
            } catch (e) {
            }
            $(document).ready(function () {
        <#if appId != -1>
        var openFirst = true;
        <#else>
        var openFirst = false;
        </#if>
                var index = 0;
                $(".system-content").each(function () {
                    var serverUrlIndex = $(this).attr("serverUrlIndex");
                    if (!serverUrlIndex || serverUrlIndex == '') {
                        return;
                    }
                    var serverId = $(this).attr("serverId");
                    var serverName = $(this).attr("serverName");
                    var serverOpenType = $(this).attr("serverOpenType");
                    var $that = $(this);
                    $(this).bind("click", function () {
                        var open = (!_preModelId || _preModelId != serverId) && !$that.parent("li").hasClass("open")
                        if (open) {
                            openModel(serverId, serverName, serverOpenType, serverUrlIndex);
                        }
                    });
                    if (index === 0 && openFirst) {
                        $(this).click();
                    }
                    index++;
                });
	                <#if isShowTips?default(false) && serverIsNull?default(false) && !topAdmin?default(false) && !superAdmin?default(false) >
			     	       layer.open({
			     	           type: 1,
			     	           shade: 0.5,
			     	           closeBtn: 0,
			     	           title: '没有可以访问的子系统，请进行授权！',
			     	           area: '500px',
			     	           btn: ['确定'],
			     	       })
	                </#if>
            });
        </script>
        <script src="${request.contextPath}/static/components/jquery-slimscroll/jquery.slimscroll.min.js"></script>


    </div>
    <div class="main-content">
        <div class="page-tab-wrap clearfix ">
            <div class="btn-tab-scroll left js-pageTabLeft" href="#">
                <a href="#">
                    <i class="wpfont icon-arrow-left"></i>
                </a>
            </div>
            <div class="page-tabs js-pageTabs">
                <div class="page-tabs-container js-pageTabsContainer">
                </div>
            </div>
            <div class="btn-tab-scroll right js-pageTabRight" href="#">
                <a href="#">
                    <i class="wpfont icon-arrow-right"></i>
                </a>
            </div>
        </div>
        <div class="main-content-inner">
            <div class="breadcrumb show" style="display: none;" id="breadDiv">
                <div class="float-left _back_div" style="display: none;">
                    <a href="javascript:void(0);" class="back"></a>
                    <span class="title title-name">页面标题</span>
                </div>
                <ol class="float-right">
                    <li><a href="javascript:void(0);" id="bread-home" onclick="goHome();">桌面</a></li>
                    <li><a href="javascript:void(0);" id="bread-system"></a></li>
                    <li id="bread-parent"><a href="javascript:void(0);"></a></li>
                    <li class="active" id="bread-child"><a href="javascript:void(0);"></a></li>
                </ol>
            </div>
            <!-- div load 页面 -->
            <div class="page-content">
                <div class="row">
                    <div class="col-xs-12" id="deskTopContainer">
                        <div id="index-home" class="row">
                            <!-- 桌面首页 -->
<#assign USER_LAYOUT_TWO2ONE =stack.findValue("net.zdsoft.desktop.entity.UserSet@LAYOUT_TWO2ONE")>
                        <#-- 布局方式1 2:1 -->
<#if layout?default('') == USER_LAYOUT_TWO2ONE>
<div class="col-sm-8 no-padding">
	<@loopFunctionAreaUser functionAreaUsers=twoFunctionAreaUsers callBack="bindFunction" />
</div>
<div class="col-sm-4 no-padding">
    <@loopFunctionAreaUser functionAreaUsers=oneFunctionAreaUsers callBack="bindFunction" />
</div>
<#else>
    <@loopFunctionAreaUser functionAreaUsers=functionAreas callBack="bindFunction" />
</#if>
                            <script>
                                var functionAreaSetDOM = $("#box-tool-template");

                                function bindFunction(functionAreaUserId, functionAreaIndex) {
        <#if functionSet?default(false)>
        $("#" + functionAreaUserId + " .box-body").after(functionAreaSetDOM.html());
        $("#" + functionAreaUserId + " .box-tool-template").removeClass("box-tool-template");
        $("#" + functionAreaUserId + " .js-box-remove").unbind("click").bind("click", function () {
            var that = $(this);
            layer.confirm('确定删除该功能区吗？', {
                title: ['提示', 'font-size:16px;'],
                btn: ['确定', '取消'],
                yes: function (index) {
                    $.ajax({
                        url: "${request.contextPath}/desktop/app/delete/functionAreaUserSet?functionAreaUserId=" + functionAreaUserId + "&functionAreaIndex=" + functionAreaIndex,
                        data: {},
                        dataType: 'json',
                        contentType: 'application/json',
                        type: 'GET',
                        success: function (data) {
                            if (data.success) {
                                that.closest('.box').remove();
                                // showSuccessMsg(data.msg);
                            } else {
                                showErrorMsg(data.msg);
                            }
                        }
                    });
                    layer.close(index);
                }
            })
        });
        $("#" + functionAreaUserId + " .js-box-setting").unbind("click").bind("click", function () {
            $(".layer-boxSetting").load("${request.contextPath}/desktop/app/showFunctionAreaUserSet/?layout=${layout!}&functionAreaUserId=" + functionAreaUserId + "&functionAreaIndex=" + functionAreaIndex + "&" + new Date().getTime(), function () {
                layer.open({
                    type: 1,
                    shade: .5,
                    title: ['设置', 'font-size:16px'],
                    area: ['940px', '720px'],
                    maxmin: true,
                    btn: ['确定', '取消'],
                    content: $('.layer-boxSetting'),
                    resize: true,
                    yes: function (index) {
                        if (!deskTopCheckVal(".layer-boxSetting")) {
                            return;
                        }
                        $.ajax({
                            url: "${request.contextPath}/desktop/app/save/functionAreaUserSet",
                            data: dealDValue(".layer-boxSetting"),
                            dataType: 'json',
                            contentType: "application/json",
                            type: 'post',
                            success: function (data) {
                                if (data.success) {
                                    showErrorMsgWithCall(data.msg, goHome);
                                } else {
                                    showErrorMsg(data.msg);
                                }
                            }
                        })
                    },
                    btn2: function (index) {
                        layer.closeAll();
                    }
                });
                $(".layer-boxSetting").parent().css('overflow', 'auto');
            })
        });
        </#if>
                                }
                            </script>


                        <#-- 遍历功能区模板 -->
<#macro loopFunctionAreaUser functionAreaUsers=[] callBack="" >
    <#if functionAreaUsers?exists && functionAreaUsers?size gt 0>
        <#list functionAreaUsers as fa>
            <div id="${fa.id}"></div>
        </#list>
    <script>
        <#--$(document).ready(function () {-->
        <#--<#list functionAreaUsers as fa>-->
        <#--$("#${fa.id}").load("${request.contextPath}/desktop/app/showFunctionArea?functionAreaUserId=${fa.id}",function(){loadCallBack('${fa.id!}','${fa_index+1}')});-->
        <#--</#list>-->
        <#--});-->
        function loadCallBack(functionAreaUserId, index) {
            <#if callBack?default('') != ''>
                ${callBack}(functionAreaUserId, index);
            </#if>
        }
    </script>
    </#if>
</#macro>
                        <#--  -->
                            <div id="box-tool-template" style="display: none;">
                                <div class="box-tools dropdown">
                                    <a class="box-tools-btn" href="javascript:void(0);" data-toggle="dropdown"><i
                                            class="wpfont icon-ellipsis"></i></a>
                                    <ul class="box-tools-menu dropdown-menu">
                                        <span class="box-tools-menu-angle"></span>
                                        <li><a class="js-box-setting" href="javascript:void(0);">设置</a></li>
                                        <li><a class="js-box-remove" href="javascript:void(0);">删除</a></li>
                                    </ul>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            <!-- S 面包屑导航 -->
            <div class="breadcrumb show" style="display: none;" id="breadIFrame">
                <div class="float-left _back_iframe" style="display: none;">
                    <a href="javascript:void(0);" class="back"></a>
                    <span class="title title-name"></span>
                </div>
                <ol class="float-right">
                    <li><a href="javascript:void(0);" id="bread-home" onclick="goHome();">桌面</a></li>
                    <li><a href="javascript:void(0);" id="bread-system"></a></li>
                    <li id="bread-parent"><a href="javascript:void(0);"></a></li>
                    <li class="active" id="bread-child"><a href="javascript:void(0);"></a></li>
                </ol>
            </div>
            <!-- E iframe插入页面 -->
            <div class="embed-responsive iframe-page js-iframe-page">

            </div>
            <iframe style="display: none;" src="" id="eis5"></iframe>
            <iframe style="display: none;" src="" id="eis6"></iframe>
            <img src="" style="display: none;" id="passportVerifyImage">
        </div>
    </div>
</div>

<div class="user-setting"></div>
<!-- 功能区设置弹出框 -->
<div class="layer layer-boxSetting"></div>
<!-- S 添加模块弹出框 -->
<div class="layer layer-addModule"></div>
<!-- common Layer  通用弹出 -->
<div id="common-Layer"></div>
<div class="layer layer-password">
    <div class="layer-content" id="weak_password_layer">
        <div class="form-horizontal">
            <div class="form-group">
                <label class="col-sm-2 control-label no-padding-right" style="width:90px;">原密码</label>
                <div class="col-sm-9">
                    <input type="password" name="c1" class="form-control">
                </div>
            </div>
            <div class="form-group">
                <label class="col-sm-2 control-label no-padding-right" style="width:90px;">新密码</label>
                <div class="col-sm-9">
                    <input type="password" name="c3" class="form-control">
                </div>
            </div>
            <div class="form-group">
                <label class="col-sm-2 control-label no-padding-right" style="width:90px;">确认新密码</label>
                <div class="col-sm-9">
                    <input type="password" name="c2" class="form-control">
                </div>
            </div>
            <div class="form-group">
                <label class="col-sm-2 control-label no-padding-right" style="width:90px;font-size: 4px;"></label>
                <div class="col-sm-9" style="font-size: 2px;">
                    ${passwordWarn!}
                </div>
            </div>
        </div>
    </div>
</div>
<script>
    $(document).ready(function () {
    <#-- $("#navbar").load("${request.contextPath}/desktop/index/header",headerInit);
    $("#sidebar").load("${request.contextPath}/desktop/index/nav",function () {
    });-->
    	$(document).click(function(e){
			if(!$(e.target).parents().hasClass("js-popover-filter") && !$(e.target).parents().hasClass("popover")){
				$('.js-popover-filter').popover('hide');
			}
		});
        sideBarInit();
        newNavListInit();
        pageTabInit();
        goHome();
        <#--先判断是否过了密码有效期-->
        <#if pwdValidityPeriod>
        	<#--过了密码有效期则需要修改密码-->
        	layer.open({
                type: 1,
                shade: 0.5,
                closeBtn: 0,
                title: '系统检测到您的密码已经有${pwdValidityPeriodDay}天没有修改，请修改密码！',
                area: '500px',
                btn: ['确定'],
                yes: function (index, layero) {
                    $.ajax({
                        url: "${request.contextPath}/desktop/user/pwd/reset",
                        data: dealDValue(".layer-password"),
                        clearForm: false,
                        resetForm: false,
                        dataType: 'json',
                        contentType: "application/json",
                        type: 'post',
                        success: function (data) {
                            if (data.success) {
                                showSuccessMsg("密码修改成功");
                                $('#weak_password_layer').html("");
                            } else {
                                showErrorMsg(data.msg);
                            }
                        }
                    })
                },
                content: $('.layer-password')
            })
        <#else>
        	<#if expireAheadNotify?default(false)>
	            $(".layerTipOpenWarn").show();
	            $(".openTisTitle").html('过期提醒');
	            $(".openTisMsg").html('${expireAheadNotifyMessage?default("")}');
	            layer.open({
	                type: 1,
	                shade: .5,
	                title: [''],
	                area: '355px',
	                content: $('.layerTipOpen'),
	            });
	        <#else>
	            //是否弹出修改密码
	            <#if isPop?default(false) >
			    layer.open({
	                type: 1,
	                shade: 0.5,
	                closeBtn: 0,
	                title: '系统检测到您的密码强度过低，请修改密码！',
	                area: '500px',
	                btn: ['确定'],
	                yes: function (index, layero) {
	                    $.ajax({
	                        url: "${request.contextPath}/desktop/user/pwd/reset",
	                        data: dealDValue(".layer-password"),
	                        clearForm: false,
	                        resetForm: false,
	                        dataType: 'json',
	                        contentType: "application/json",
	                        type: 'post',
	                        success: function (data) {
	                            if (data.success) {
	                                showSuccessMsg("密码修改成功");
	                                $('#weak_password_layer').html("");
	                            } else {
	                                showErrorMsg(data.msg);
	                            }
	                        }
	                    })
	                },
	                content: $('.layer-password')
	            })
	            <#else>
				$('#weak_password_layer').html("");
	            </#if>
	        </#if>
        </#if>
        
        autoRefresh();

        //是否加载文轩智慧平台的应用图标
        <#if isWenXuan?default(false) >
          var option = {
              wxLoginName: '${wxUserName!}',
              witaccount:'${wxWitaccount!}',
              iconLeft: '10px', //	非必需，传入像素值20px 或者百分比 10%
              iconTop: '60%',	//	非必需，传入像素值20px 或者百分比 10%
              hostUrl: 'http://smart.winshareyun.cn/winshare-web-portal',
              animationPosition: 'right' //非必须，可选值left,right，如果是left表示动画自右向左滑动，默认right
          }
			var applink = new WsAppLink(option);
			applink.init();
        </#if>
    });

    function autoRefresh() {
        var isAuto = false;
        try {
	            <#if eis5_domain?default('') != '' >
                    isAuto = true;
	                $("#eis5").attr("src", "${eis5_domain}/fpf/homepage/checkValidate.action");
                </#if>
	            <#if eis6_domain?default('') != '' >
                    isAuto = true;
	                $("#eis6").attr("src", "${eis6_domain}/fpf/homepage/checkValidate.action");
                </#if>
        } catch (e) {

        }
        if (isAuto) {
            setTimeout(autoRefresh, 1000 * 60 * 20); //20m
        }
    }

    $(document).ready(function () {
        $('.nav-bar-user').on('click', function () {
            $(this).next().toggle().end()
                    .closest('li').addClass('open')
                    .siblings().removeClass('open').find('.dropbox').hide();
        });
        $(".nav-user-photo").each(function () {
            var iconUrl = $(this).attr("src");
            $(this).attr("src", iconUrl + "?" + new Date().getTime());
        });
        <#if multiIdentity?exists && multiIdentity?size gt 0>
            <#list multiIdentity as u>
                $("#li-${u.userName!}").click(function () {
                    $.ajax({
                        type: "get",
                        url: "${request.contextPath}/homepage/logout/page?userName=${u.userName!}&psId=${passportSessionId!}",
                        dataType: "json",
                        data: {},
                        success: function (data) {
                            if (data.success) {
                                location.href = data.businessValue;
                            } else {
                                showWarnMsg(data.msg);
                                $("#li-${u.userName!}").remove();
                            }
                        }
                    });
                });
            </#list>
        </#if>

        $('.dropdown-menu-user').parent().on('shown.bs.dropdown', function () {
            var container = $('.dropdown-menu-user .user-list');
            var max = $(window).height() - container.offset().top - 55;

            if (container.outerHeight() > max) {
                container.slimscroll({
                    height: max
                })
            }
        });
        $('.classify').on('click', function() {
            var id = $(this).data('classify');
            $(this).find('a').addClass('active');
            $(this).siblings('li').each(function() {
                $(this).find('a').removeClass('active');
            });

            $('#' + id).removeClass('hide').siblings('ul').addClass('hide');
        })
    });

    //passport 切换失败时会调用
    function processError(e) {
        showWarnMsg(e);
    }

    try {
        ace.settings.check('navbar', 'fixed')
    } catch (e) {
    }


    // 添加模块
    $('.js-addModule').on('click', function () {
        $('.layer-addModule').load("${request.contextPath}/desktop/index/addModule", function () {
            layer.open({
                type: 1,
                shade: .5,
                title: ['添加模块', 'font-size:16px'],
                area: ['940px', '600px'],
                btn: ['确定', '取消'],
                yes: function (index, layero) {
                    addFunctionArea("${request.contextPath}");
                },
                content: $('.layer-addModule')
            })
        });
    });

    function addFunctionArea(contextPath) {
        var $active;
        var functions = new Array();
        var n = 0;
        $(".module-style-list").find("li").each(function () {
            $this = $(this);
            var class1 = null;
            if (!$this.hasClass("selected")) {
                $active = $this;
                return;
            }
            class1 = $(this).attr("class");
            class1 = class1.replace("selected", "");
            class1 = class1.replace("applied", "");
            var obj = dealEValue("." + class1);
            functions[n] = obj;
            n++;
        });
        var options;
        options = {
            url: contextPath + "/desktop/app/addFunctionAreaUser",
            data: JSON.stringify(functions),
            clearForm: false,
            resetForm: false,
            dataType: "json",
            type: 'post',
            contentType: "application/json",
            success: function (data) {
                if (data.success) {
                    showSuccessMsgWithCall(data.msg, goHome);
                    //    showSuccessMsg(data.msg);
                } else {
                    showErrorMsg(data.msg);
                }
            }
        }
        $.ajax(options);
    }

    function dealEValue(container) {
        var tags = ["input"];
        var os;
        var obj = new Object();
        for (var i = 0; i < tags.length; i++) {
            if (typeof(container) == "string") {
                os = jQuery(container + " " + tags[i]);
            }
            else {
                return;
            }
            os.each(function () {
                $this = $(this);
                var value = $this.val();
                var name = $this.attr("name");
                //          name = name || $this.attr("id");
                obj[name] = value;
            });
        }
        return obj;
    }

    $.ajaxSetup({
        statusCode: {
            499: function (data) {
                window.location.href = data.responseText;
            }
        }
    });
    window.sessionId = '${sessionId}';
</script>
</@w.commonWeb>
