<!-- 3.0大数据首页 -->
<!DOCTYPE html>
<html lang="en">
<head>
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1"/>
    <meta charset="UTF-8">
    <title>${platformName}</title>
    <meta name="description" content=""/>
    <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0"/>
    <link rel="shortcut icon"  href="${springMacroRequestContext.contextPath}/bigdata/v3/static/images/public/wanshu-icon16.png" >
    <link rel="stylesheet" type="text/css"
          href="${springMacroRequestContext.contextPath}/bigdata/v3/static/css/bootstrap.css"/>
    <link rel="stylesheet"
          href="${springMacroRequestContext.contextPath}/bigdata/v3/static/font-awsome/css/font-awesome.css"/>
    <link rel="stylesheet" type="text/css"
          href="${springMacroRequestContext.contextPath}/bigdata/v3/static/css/iconfont.css"/>
    <link rel="stylesheet" type="text/css"
          href="${springMacroRequestContext.contextPath}/bigdata/v3/static/fonts/iconfont.css"/>
    <link rel="stylesheet" type="text/css"
          href="${springMacroRequestContext.contextPath}/bigdata/v3/static/css/page.css"/>
    <link rel="stylesheet" type="text/css"
          href="${springMacroRequestContext.contextPath}/bigdata/v3/static/css/all.css"/>
    <link rel="stylesheet" type="text/css"
          href="${springMacroRequestContext.contextPath}/bigdata/v3/static/css/style.css"/>
    <script>
        _contextPath = "${springMacroRequestContext.contextPath}";
    </script>
</head>
<body>
<#import  "/bigdata/v3/templates/commonWebMacro.ftl" as cwm />
<!--头部 S-->
<div class="big-data-head clearfix">
    <div class="logo-box clearfix">
        <img src="${logoUrl}"/>
        <span>${platformName}</span>
    </div>
    <div class="user-box">
    	<#if userType! =="0">
    	<span class="mr-10" style="cursor:pointer;" onclick="loadHelpFromDesktop();">帮助与支持</span>
    	</#if>
        <div role="presentation" class="dropdown">
            <a href="javascript:;" class="user-img dropdown-toggle" data-toggle="dropdown" aria-expanded="false">
                <img src="${springMacroRequestContext.contextPath}/bigdata/v3/static/images/public/user.png"/>
            </a>
            <ul class="dropdown-menu">
                <li>
                     <a  ref="javascript:;" role="tab" data-toggle="tab"> 
                        <img src="${springMacroRequestContext.contextPath}/bigdata/v3/static/images/public/user.png" class="mr-15"/>
                        <span>${username!}</span>
                    </a> 
                </li>
                <li><a href="javascript:;" onclick="logout();" role="tab" data-toggle="tab" class="text-center">退出</a></li>
            </ul>
        </div>
    </div>
</div><!--头部 E-->

<!--主体 S-->
<div class="main-container">
    <!--侧边栏 S-->
    <div class="sidebar clearfix" id="sidebar">
        <div class="static-sidebar">
            <ul>
                <#if homeUrl??>
                <li id="home" data-href="/home" data-type="item" class="module-box cut-line-tb" data-toggle="tooltip" data-placement="right" data-name="首页">
                    <a href="javascript:void(0)">
                    	<i class="iconfont icon-home-fill"></i>
                    </a>
                    <span>首页</span>
                </li>
                </#if>
                <#if rootList?? && rootList?size gt 0>
                    <#list rootList as module>
                        <li id="${module.id}" data-model="${module.id}" data-id="ul_${module.pinyinName!}" data-href="<#if module.url??>${module.url}<#else>/${module.pinyinName}</#if>"
                            data-type="${module.type}" class="module-box" data-toggle="tooltip" data-name="${module.name!}"
                            data-placement="right" >
							 <a href="javascript:void(0)">
                           		 <i class="iconfont ${module.icon!}"></i>
                           	</a>
                            <span>${module.name!}</span>
                        </li>
                    </#list>
                </#if>
            </ul>
        </div>
		<!--
        <div class="fold-box">
				<span class="fold-icon">
					<i class="iconfont icon-packup-fill"></i>
				</span>
        </div>-->

        <div class="dynamic-sidebar" id="other-system">
            <#if rootList?? && rootList?size gt 0>
                <#list rootList as rootModule>
                    <ul id="ul_${rootModule.pinyinName!}">
                        <#if rootModule.type == 'dir'>
                            <#if rootModule.children?? && rootModule.children?size gt 0>
								<li><span>${rootModule.name!}</span></li>
                                <#list rootModule.children as child>
                                    <li id="${child.id!}">
                                        <a href="#${child.url!}">
                                            <span>${child.name!}</span>
                                        </a>
                                    </li>
                                </#list>
                            <#else>
                                <li></li>
                            </#if>
                        </#if>
                    </ul>
                </#list>
            </#if>
        </div>
    </div><!--侧边栏 E-->

    <!--内容 S-->
    <div class="main-content">
        <div class="main-content-inner">
            <div class="breadcrumb clearfix" id="navigation">
                <div class="breadcrumb-name clearfix">
                    <span class="title" id="current_target"></span>
                </div>
                <ol class="breadcrumb-list clearfix">
                </ol>
            </div>
            <div class="page-content">
                <div class="row height-1of1 no-padding">
                    <div class="col-24 col-cell height-1of2">
                        <div class="box box-default wrap-full">

                        </div>
                    </div>
                    <div class="col-12 col-cell height-1of2 no-margin">
                        <div class="box box-default wrap-full">

                        </div>
                    </div>
                    <div class="col-12 col-cell height-1of2 no-margin">
                        <div class="box box-default wrap-full">

                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div><!--内容 E-->
</div><!--主体 E-->
<@cwm.common></@cwm.common>
<script src="${springMacroRequestContext.contextPath}/bigdata/v3/static/js/jquery/jquery.min.js" type="text/javascript"
        charset="utf-8"></script>
<script src="${springMacroRequestContext.contextPath}/bigdata/v3/static/js/jquery/jquery.form.js" type="text/javascript"
        charset="utf-8"></script>        
<script src="${springMacroRequestContext.contextPath}/bigdata/v3/static/js/bootstrap/bootstrap.min.js" type="text/javascript" 
		charset="utf-8"></script>
<script src="${springMacroRequestContext.contextPath}/bigdata/v3/static/fonts/iconfont.js" type="text/javascript" 
		charset="utf-8"></script>
<script src="${springMacroRequestContext.contextPath}/bigdata/v3/static/js/layer/layer.js"  type="text/javascript" 
		charset="utf-8"></script>  
<script src="${springMacroRequestContext.contextPath}/bigdata/v3/static/js/echarts/echarts.4.1.0.rc2.min.js" type="text/javascript" 
		charset="utf-8"></script>
<!-- 
<script src="${springMacroRequestContext.contextPath}/bigdata/v3/static/js/echarts/echarts.min.js" type="text/javascript" 
		charset="utf-8"></script>
		--> 
<script src="${springMacroRequestContext.contextPath}/bigdata/v3/static/js/echarts/echarts-wordcloud.min.js" type="text/javascript" 
		charset="utf-8"></script>
<script src="${springMacroRequestContext.contextPath}/bigdata/v3/static/js/myscript.js" type="text/javascript"
        charset="utf-8"></script>
<script src="${springMacroRequestContext.contextPath}/bigdata/v3/static/js/router.js" type="text/javascript"
        charset="utf-8"></script>
<script src="${springMacroRequestContext.contextPath}/bigdata/v3/static/js/tool.js" type="text/javascript" 
		charset="utf-8"></script>
<script src="${request.contextPath}/bigdata/v3/static/js/laydate/laydate.min.js" type="text/javascript"
        charset="utf-8"></script>
<script src="${request.contextPath}/bigdata/v3/static/js/relation/go.js" type="text/javascript"
        charset="utf-8"></script>
<script src="${request.contextPath}/bigdata/v3/static/js/relation/Figures.js" type="text/javascript"
        charset="utf-8"></script>
<script>
    //模块选择
    let indexUrl = window.location.href;
    if (indexUrl.indexOf('#') < 0 && indexUrl.endsWith('/')) {
        indexUrl = window.location.href + '#/';
    } else if (indexUrl.indexOf('#') < 0 && !indexUrl.endsWith('/')) {
        indexUrl = window.location.href + '/#/';
    }
    let RootRouter = new window.router({
        //TODO 这里写的有问题
        "/": function () {
            if (window.location.href == indexUrl) {

            } else {
                window.location.href = indexUrl;
            }
        }
    });
    RootRouter.init();
    let routerProxy = function (rootRouter) {
        var proxyUtils = {
            reloadNavigation: function (settings, callBack) {
                settings.callBack = callBack;
                let level = settings.level;
                if (level === 0) {
                    //根结点
                    this.tree = [];
                    this.tree.push(settings);
                } else {
                    if (this.tree.length - 1 < level) {
                        this.tree.push(settings);
                    }
                    else if (this.tree.length - 1 == level) {
                        this.tree[level] = settings;
                    }
                    else {
                        this.tree = this.tree.slice(0, level);
                        this.tree.push(settings);
                    }
                }
                if (settings.type === 'item' || this.tree.length > 1) {
                    $('#current_target').html(settings.name);
                }
                this.createNavigation(settings);
            },
            createNavigation: function (settings) {
                if (this.tree.length === 1 && settings.type !== 'item') {
                    return ;
                }
                let navigations = '';
                for (var i = 0; i < this.tree.length; i++) {
                    if (this.tree.length - 1 > i) {
                        navigations = navigations + '<li><a href="#' + this.tree[i].path + '">' + this.tree[i].name + '</a></li>'
                    } else {
                        navigations = navigations + '<li>' + this.tree[i].name + '</li>'
                    }
                    $('.breadcrumb-list ').html(navigations);
                }
            },
            tree: []
        };
        var proxy = {};
        proxy.utils = proxyUtils;


        proxy.origin = rootRouter;

        proxy.add = function (settings, callBack) {
            this.origin.add(settings.path, function () {
                callBack();
                if (typeof settings.modelId !== 'undefined') {
                    proxy.accessLog(settings.name, settings.path, settings.modelId)
                }
                proxyUtils.reloadNavigation(settings, callBack);
            });
        };

        proxy.go = function (settings, callBack) {
            this.origin.go(settings.path, function () {
                callBack();
                if (typeof settings.modelId !== 'undefined') {
                    proxy.accessLog(settings.name, settings.path, settings.modelId)
                }
                proxyUtils.reloadNavigation(settings, callBack);
            });

        };

        proxy.reload = function (settings) {
            this.origin.reload();
            proxyUtils.reloadNavigation(settings);
        };

        proxy.accessLog = function (name, url, modelId) {
            $.ajax({
                url: _contextPath + '/bigdata/accessLog',
                type: 'POST',
                data: {
                    type: '-desktop-operation-mark',
                    length: 6,
                    value: JSON.stringify({
                        "modelId": modelId,
                        "name": name,
                        "url": url
                    })
                },
                success: function () {

                }
            })
        };

        return proxy;
    }(RootRouter);


    window.router = routerProxy;

    routerProxy.add({
        path: '/home',
        name: '首页',
        type: 'item',
        level: 0
    }, function () {
        $('.page-content').load('${springMacroRequestContext.contextPath}${homeUrl!}')
    });
    function logout() {
        window.location.href = '${springMacroRequestContext.contextPath}/fpf/logout/page?call=${v3Index}';
    }
</script>

<script>
    <#if rootList?? && rootList?size gt 0>
        <#list rootList as rootModule>
            <#if rootModule.children?? && rootModule.children?size gt 0>
                <#list rootModule.children as child>
                    router.add({
                        path: '${child.url!}',
                        name: '${child.name}',
                        type: 'item',
                        modelId: '${child.id!}',
                        level: 1
                    }, function () {
                        $('.page-content').load('${springMacroRequestContext.contextPath}${child.url!}')
                    });
                </#list>
            </#if>
        </#list>
    </#if>
</script>
<script>
	//侧边栏扩展
	$('.static-sidebar').mouseenter(function(e){
	    $(this).addClass('active');
	    $('.main-content').css('margin-left','220px');
	}).mouseleave(function(){
	    $(this).removeClass('active');
	    $('.main-content').css('margin-left','50px');
	});

    $('.static-sidebar li').each(function (index, ele) {
        $(this).click(function () {
            if (!$(this).hasClass('active')) {
                $('#folder-no-data').remove();
            }
            $(this).addClass('active').siblings('li').removeClass('active');
            let type = $(this).data('type');
            let href = $(this).data('href');
            let name = $(this).data('name');
            let pinyinName = $(this).data('id');
            let modelId = $(this).data('model');
            if ('item' === type) {
                //do nothing
                $('.dynamic-sidebar').removeClass('active');
                $('.main-content').removeClass('active');
                routerProxy.go({
                    path: href,
                    level: 0,
                    type: 'item',
                    modelId: modelId,
                    name: name
                }, function () {
                    $('.page-content').load("${springMacroRequestContext.contextPath}" + href);
                });
                $('.fold-box').children('span').addClass('hide')
            }
            else if ('dir' === type) {
                $('.dynamic-sidebar').addClass('active');
                $('.main-content').addClass('active');
               // $('.fold-icon').find('i').removeClass('icon-packup-fill').addClass('icon-unfold-fill');
                $('.dynamic-sidebar').children('ul').eq(index - 1).addClass('active').siblings('ul').removeClass('active');
                routerProxy.go({
                    path: href,
                    level: 0,
                    type: 'dir',
                    name: name
                }, function () {
                    //do nothing
                })
                $('.fold-box').children('span').removeClass('hide')
            }else {
                $('.dynamic-sidebar').addClass('active');
                $('.main-content').addClass('active');
                //$('.fold-icon').find('i').removeClass('icon-packup-fill').addClass('icon-unfold-fill');
                $('.dynamic-sidebar').children('ul').eq(index - 1).addClass('active').siblings('ul').removeClass('active');
                $('.fold-box').removeClass('hide');
                $('.fold-box').children('span').removeClass('hide');
                routerProxy.go({
                    path: href,
                    level: 0,
                    type: 'dir',
                    name: name
                }, function () {
                    $('#' + pinyinName).html("<div class='pos-middle-center'><h4><img src='${request.contextPath}/static/ace/img/loading.gif' /><font color='#9EACBD'>加载中......</font></h4></div>");
                    $('#' + pinyinName).load("${springMacroRequestContext.contextPath}" + href);
                    if (pinyinName == 'ul_keshihuamulu' || pinyinName == 'ul_keshihuabaobiao') {
                        $('.page-content').load("${springMacroRequestContext.contextPath}/bigdata/user/folderDetail/search/index?identity=user");
                        $('#current_target').text('可视化报表');
                        $('.breadcrumb-list').empty().append('<li>可视化报表</li>');
                    }

                    if (pinyinName == 'ul_baobiaocangku') {
                        $('.page-content').load("${springMacroRequestContext.contextPath}/bigdata/user/folderDetail/search/index?identity=admin");
                        $('#current_target').text('报表仓库');
                        $('.breadcrumb-list').empty().append('<li>报表仓库</li>');
                    }
                });
            }
			  $('.static-sidebar').removeClass('active');
			  $('.main-content').css('margin-left','50px');
        })
    });
    //TODO 这里不应该这样写
    $('#home').trigger('click');
    $('.dropdown-toggle').not(':disabled').click(function(e){
        e.preventDefault();
        $('.dropdown-toggle').each(function(){
            var $g = $(this).parent('.btn-group');
            if ($g.hasClass('open')) {
                $g.removeClass('open');
            }
        });
        $(this).parent('.btn-group').toggleClass('open');
    });
	
	
	function loadHelpFromDesktop() {
		router.go({
	        path: 'bigdata/help/tree?showAll=no',
	        name:'帮助与支持',
	        level: 1
	    }, function () {
	   		$('.page-content').load('${request.contextPath}/bigdata/help/tree?showAll=no'); 
	    });
    }

    $(function () {
        $('.page-content').removeClass('deveman-content')
    })

    //调度监控定时器任务
    var jobLogTimer = null;
</script>
</body>
</html>