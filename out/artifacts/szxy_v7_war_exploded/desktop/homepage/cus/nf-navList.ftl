<script type="text/javascript">
    try{ace.settings.check('sidebar' , 'fixed')}catch(e){}
</script>
<div class="sidebar-shortcuts">
    <#--<button>
        <i class="wpfont icon-home-fill"></i>
    </button>-->
    <a href="javascript:" class="btn btn-tohome js-siderbar-collapse">
        <i class="wpfont icon-dedent"></i>
    </a>
</div>
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
            <#--开发测试用-->
            <#if frameworkEnv.isDevModel()>
            <li>
                <a id="commonModel-73101" href="javascript:;" onclick="openModel('super1','数据填报','1','${request.contextPath}/dc/report/listReports','数据填报','','superId','');">
                    <img class="menu-icon" src="${request.contextPath}/desktop/images/icon-desktop-default.png" alt="">
                    <span class="menu-text">数据填报</span>
                </a>
            </li>
            </#if>
    		<#if commonModelDtos?exists && commonModelDtos?size gt 0>
	    		<#list commonModelDtos as commonModelDto>
				<li class="">
					<a id="commonModel-${commonModelDto.model.id!}" href="javascript:;" onclick="openModel('${commonModelDto.model.id!}','${commonModelDto.model.name!}','${commonModelDto.model.openType!}','${commonModelDto.fullUrl!}','','','','');">
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
                <a href="#" data-show="${serverDto.server.id!}" class="dropdown-toggle system-content" serverId="${serverDto.server.id!}" serverName="${serverDto.server.name!}" serverOpenType="${serverDto.openType!}" serverUrlIndex="<#if secondUrl>${serverDto.urlIndexSecond!}<#else>${serverDto.urlIndex!}</#if><#if (serverDto.openType)!=1 && serverDto.urlIndex?default("") != "" >&uid=${userName!}</#if>" >
                    <img class="menu-icon" src="${serverDto.imageUrl!}" alt="" width="20" height="20" >
                    <span class="menu-text">${serverDto.server.name!}</span>
                    <b class="arrow fa fa-angle-down"></b>
                </a>
                <ul class="submenu">
                    <#if serverDto.modelDtos?exists && serverDto.modelDtos?size gt 0>
                        <#if serverDto.dir?default(true)>
                        <#list  serverDto.modelDtos as modelDto>
                            <li>
                                <a href="#" class="dropdown-toggle" id="${modelDto.model.id!}">
                                    ${modelDto.model.name!}
                                    <b class="arrow fa fa-angle-down"></b>
                                </a>
                                <#if modelDto.subModelDtos?exists && modelDto.subModelDtos?size gt 0>
                                    <ul class="submenu">
                                        <#list modelDto.subModelDtos as subModel>
                                            <li>
                                                <a id="${subModel.model.id!}" href="#" onclick="openModel('${subModel.model.id!}','${subModel.model.name!}','${subModel.model.openType!}','${subModel.fullUrl!} <#if subModel.model.openType!=1 > ${sessionId!} </#if> ','${serverDto.server.name!}','${modelDto.model.name!}','${serverDto.server.id}','${modelDto.model.id!}');">${subModel.model.name!}</a>
                                            </li>
                                        </#list>
                                    </ul>
                                </#if>
                            </li>
                        </#list>
                        <#else>
                            <#list  serverDto.noDirModelDtos as modelDto>
                                <li>
                                    <a id="${modelDto.model.id!}" href="#" onclick="openModel('${modelDto.model.id!}','${modelDto.model.name!}','${modelDto.model.openType!}','${modelDto.fullUrl!}<#if modelDto.model.openType != 1>${sessionId!}</#if>','${serverDto.server.name!}','','${serverDto.server.id}','');">${modelDto.model.name!}</a>
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
            <a id="developerManage" href="#" data-show="superId" class="dropdown-toggle system-content">
                <img class="menu-icon" src="${resourceUrl}/images/icons/app-icon/icon01-sm.png" alt="">
                <span class="menu-text">应用系统</span>
                <b class="arrow fa fa-angle-down"></b>
            </a>
            <ul class="submenu">
                <li>
                    <a id="super1" href="#" onclick="openModel('super1','开发者管理','1','${request.contextPath}/system/developer/index','超级管理系统','','superId','');">开发者管理</a>
                </li>
                <li>
                    <a id="super1" href="#" onclick="openModel('super2','应用管理','1','${request.contextPath}/system/server/index','超级管理系统','','superId','');">应用管理</a>
                </li>
                <li>
                    <a id="super1" href="#" onclick="openModel('super2','应用授权','1','${request.contextPath}/system/serverAuthorize/index','超级管理系统','','superId','');">应用授权</a>
                </li>
                <li>
                    <a id="super1" href="#" onclick="openModel('super2','常见问题','1','${request.contextPath}/system/problem/index','超级管理系统','','superId','');">常见问题</a>
                </li>
                <li>
                    <a id="super1" href="#" onclick="openModel('super2','缓存管理','1','${request.contextPath}/redis/index','缓存管理','','superId','');">缓存管理</a>
                </li>
            </ul>
        </li>
    </#if>
    <#if superAdmin?default(false)>
        <li>
            <a id="deployManage" href="#" data-show="superId" class="dropdown-toggle system-content">
                <img class="menu-icon" src="${resourceUrl}/images/icons/app-icon/icon01-sm.png" alt="">
                <span class="menu-text">部署工具</span>
                <b class="arrow fa fa-angle-down"></b>
            </a>
            <ul class="submenu">
                <li>
                    <a id="super1" href="#" onclick="openModel('super2','密码重置','1','${request.contextPath}/system/ops/resetPassword?fromDesktop=true','','','superId','');">密码重置</a>
                </li>
                <li>
                    <a id="super1" href="#" onclick="openModel('super2','系统参数设置','1','${request.contextPath}/system/ops/sysOption/tab?fromDesktop=true','','','superId','');">系统参数设置</a>
                </li>
                <li>
                    <a id="super1" href="#" onclick="openModel('super2','序列号激活','1','${request.contextPath}/system/ops/init?fromDesktop=true','','','superId','');">序列号激活</a>
                </li>
                <li>
                    <a id="super1" href="#" onclick="openModel('super2','应用列表','1','${request.contextPath}/system/ops/serverList?fromDesktop=true','','','superId','');">应用列表</a>
                </li>
                <li>
                    <a id="super1" href="#" onclick="openModel('super2','多域名设置','1','${request.contextPath}/system/ops/serverRegion/index?fromDesktop=true','','','superId','');">多域名设置</a>
                </li>
                <li>
                    <a id="super1" href="#" onclick="openModel('super2','登录页设置','1','${request.contextPath}/ops/loginSet/index?fromDesktop=true','','','superId','');">登录页设置</a>
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
                                            <li><a href="javascript:;" onclick="openModel('${subModel.model.id!}','${subModel.model.name!}','${subModel.model.openType!}','${subModel.fullUrl!}<#if subModel.model.openType!=1>${sessionId!}</#if>','${serverDto.server.name!}','${modelDto.model.name!}','${serverDto.server.id}','${modelDto.model.id!}');">${subModel.model.name!}</a></li>
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

<#--<div class="sidebar-toggle sidebar-collapse js-siderbar-collapse" id="sidebar-collapse">-->
    <#--<i class="wpfont icon-angle-double-left"></i>-->
<#--</div>-->
<script type="text/javascript">
    try{ace.settings.check('sidebar' , 'collapsed')}catch(e){}
    $(document).ready(function () {
    	<#if moduleId != -1>
    	   $('#'+${moduleId!}).parents('ul').find('.submenu').css('display', 'block');
    	   $('#'+${moduleId!}).parent('li').addClass('active');
           $('#'+${moduleId!}).click();
        </#if>
        <#if appId != -1>
        var openFirst = true;
        <#else>
        var openFirst = false;
        </#if>
        var index = 0;
       $(".system-content").each(function () {
           var serverUrlIndex = $(this).attr("serverUrlIndex");
           if (!serverUrlIndex || serverUrlIndex == '' ) {
               return ;
           }
           var serverId = $(this).attr("serverId");
           var serverName   = $(this).attr("serverName");
           var serverOpenType  = $(this).attr("serverOpenType");
           var $that = $(this);
           $(this).bind("click",function () {
               var open =  (!_preModelId || _preModelId!=serverId) && !$that.parent("li").hasClass("open")
               if (open){
                   openModel(serverId,serverName,serverOpenType,serverUrlIndex);
               }
           });
           if (index === 0 && openFirst) {
               $(this).click();
           }
           index ++;
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
