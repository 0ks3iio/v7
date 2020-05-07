<!DOCTYPE html>
<html lang="en">
<head>
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1"/>
    <meta charset="UTF-8">
    <title>大数据中心</title>
    <meta name="description" content=""/>
    <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0"/>
    <link rel="stylesheet" type="text/css"
          href="${request.contextPath}/bigdata/v3/static/css/bootstrap.css"/>
    <link rel="stylesheet" href="${request.contextPath}/bigdata/v3/static/font-awsome/css/font-awesome.css"/>
    <link rel="stylesheet" href="${request.contextPath}/bigdata/v3/static/fonts/iconfont.css">
    <link rel="stylesheet" href="${request.contextPath}/bigdata/v3/static/datav/css/style.css">
    <link rel="stylesheet" href="${request.contextPath}/bigdata/v3/static/datav/css/style02.css">
    <link rel="stylesheet" href="${request.contextPath}/bigdata/v3/static/datav/border/css/border.css">
    <link rel="stylesheet" type="text/css" href="${request.contextPath}/bigdata/v3/static/datav/icolor/icolor.css"/>
<#--<link rel="stylesheet" href="${request.contextPath}/static/ztree/zTreeStyle/zTreeStyle.css">-->
    <link rel="stylesheet" type="text/css" href="${request.contextPath}/static/components/slick/slick.css"/>
    <link rel="stylesheet" type="text/css" href="${request.contextPath}/static/components/slick/slick-theme.css"/>
    <style>
        @font-face {
            font-family: Quartz;
            src: url("${request.contextPath}/bigdata/v3/static/datav/fonts/Quartz Regular.ttf");
        }
    </style>
    <script>
        _contextPath = "${request.contextPath}";
        _screenId = "${screenId}";
        _preview = false;
        _fileUrl = "${fileUrl}";
    </script>
    <script src="${request.contextPath}/bigdata/v3/static/datav/datav-ace-tips.js"></script>
</head>
<body>
<!--头部 S-->
<div class="data-center row js-none">
    <div class="logo-name col-md-3">
        <div class="">
            <b>大数据中心</b>
        </div>
    </div>
    <div class="preview col-md-6 text-center">
        <#if diagramTypeGroups?exists && diagramTypeGroups?size gt 0>
            <#list diagramTypeGroups as diagramTypeGroup>
                <#if diagramTypeGroup.group != "其他" && diagramTypeGroup.group != "文字">
                <span class="js-add-other">
                    <img src="${diagramTypeGroup.iconUrl!}" width="20" height="18"/><br/>
                    <span>${diagramTypeGroup.group}</span>
                    <div class="other-data example clearfix no-padding">
                        <div class="temp-logo col-cell no-padding js-none">
                            <#if diagramTypeGroup.categories?exists && diagramTypeGroup.categories?size gt 0>
                                <#list diagramTypeGroup.categories as category>
                                    <div class="centered <#if category_index==0>active</#if>">
                                        <img src="${request.contextPath}${category.iconUrl!}"/>
                                    </div>
                                </#list>
                            </#if>
                        </div>
                        <div class="imgs-choice no-padding scrollbar-made js-add-chart">
                            <#if diagramTypeGroup.categories?exists && diagramTypeGroup.categories?size gt 0>
                                <#list diagramTypeGroup.categories as category>
                                    <div class="litimg clearfix <#if category_index != 0>hide</#if>">
                                        <#if category.views?exists && category.views?size gt 0>
                                            <#list category.views as view>
                                                <div class="pic-wrap" data-diagramtype="${view.diagramType}">
                                                    <span class="pic-border" data-type="bar">
                                                        <img src="${request.contextPath}${view.iconUrl}"/>
                                                    </span>
                                                </div>
                                            </#list>
                                        </#if>
                                    </div>
                                </#list>
                            </#if>
                        </div>
                    </div>
                </span>
                </#if>
            </#list>
        </#if>
        <span class="js-add-text">
            <img src="${request.contextPath}/bigdata/v3/static/images/big-data/text.png" width="20" height="18"/><br/>
            <span>文字</span>
        </span>
        <span class="js-add-table">
            <img src="${request.contextPath}/bigdata/v3/static/images/big-data/table-logo.png" width="20"
                 height="18"/><br/>
            <span>表格</span>
        </span>
        <span class="js-add-other js-add-chart">
				<img src="${request.contextPath}/bigdata/v3/static/images/big-data/adorn.png" width="20"
                     height="18"/><br/>
				<span>装饰</span>
				<div class="other-data clearfix">
                    <div class="pic-wrap" data-diagramtype="202">
						<span class="pic-border js-add-default-border" data-type="">
							<img src="${request.contextPath}/bigdata/v3/static/datav/images/chart-202.png"/>
						</span>
						<p>自定义背景框</p>
					</div>
                    <div class="pic-wrap" data-diagramtype="204">
						<span class="pic-border js-add-default-border" data-type="">
							<img src="${request.contextPath}/bigdata/v3/static/datav/images/chart-204.png"/>
						</span>
						<p>装饰边框</p>
					</div>
                    <div class="pic-wrap" data-diagramtype="205">
						<span class="pic-border js-add-default-border" data-type="">
							<img src="${request.contextPath}/bigdata/v3/static/datav/images/chart-205.png"/>
						</span>
						<p>标题边框</p>
					</div>
				</div>
        </span>
        <span class="js-add-other js-add-chart">
				<img src="${request.contextPath}/bigdata/v3/static/images/big-data/interaction.png" width="20"
                     height="18"/><br/>
				<span>交互</span>
				<div class="other-data clearfix">
					<div class="pic-wrap" data-diagramtype="301">
						<span class="pic-border js-add-default-border" data-type="">
							<img src="${request.contextPath}/bigdata/v3/static/datav/images/chart-301.png"/>
						</span>
						<p>时间轴</p>
					</div>
                    <div class="pic-wrap" data-diagramtype="302">
						<span class="pic-border js-add-default-border" data-type="">
							<img src="${request.contextPath}/bigdata/v3/static/datav/images/chart-302.png"/>
						</span>
						<p>Tab</p>
					</div>
				</div>
        </span>
        <span class="js-add-other">
				<img src="${request.contextPath}/bigdata/v3/static/images/big-data/icon-img.png" width="20"
                     height="18"/><br/>
				<span>图片</span>
				<div class="other-data clearfix js-add-chart">
					<div class="pic-wrap" data-diagramtype="110">
						<span class="pic-border js-add-img" data-type="">
							<img src="${request.contextPath}/bigdata/v3/static/datav/images/chart-110.png" width="130"
                                 height="98"/>
						</span>
						<p>单张图片</p>
					</div>
					<div class="pic-wrap" data-diagramtype="111">
						<span class="pic-border js-add-slide" data-type="">
							<img src="${request.contextPath}/bigdata/v3/static/datav/images/chart-111.png" width="130"
                                 height="98"/>
						</span>
						<p>图片轮播</p>
					</div>
				</div>
			</span>
        <span class="js-add-other js-add-chart">
            <img src="${request.contextPath}/bigdata/v3/static/images/big-data/other-logo.png" width="20"
                 height="18"/><br/>
            <span>其他</span>
            <div class="other-data clearfix ">
                <#if diagramTypeGroups?exists && diagramTypeGroups?size gt 0>
                    <#list diagramTypeGroups as dtg>
                        <#if dtg.group=="其他">
                            <#if dtg.categories?exists && dtg.categories?size gt 0>
                                <#list dtg.categories as category>
                                    <#list category.views as view>
                                        <div class="pic-wrap" data-diagramtype="${view.diagramType}">
                                                    <span class="pic-border" data-type="bar">
                                                        <img src="${request.contextPath}${view.iconUrl}"/>
                                                    </span>
                                        </div>
                                    </#list>
                                </#list>
                            </#if>
                        </#if>
                    </#list>
                </#if>
            </div>
        </span>
        <span class="js-add-other">
				<img src="${request.contextPath}/bigdata/v3/static/images/big-data/storeroom-icon.png" width="20"
                     height="18"/><br/>
				<span>我的收藏</span>
				<div class="other-data clearfix js-add-library">
                    <#if libries?? && libries?size gt 0>
                        <#list libries as diagram>
                        <div class="pic-wrap" data-libraryid="${diagram.id}" data-diagramtype="${diagram.diagramType}">
                            <span class="pic-border js-add-slide" data-type="">
                                <img src="${request.contextPath}/bigdata/v3/static/datav/images/chart-${diagram.diagramType}.png"
                                     width="130"
                                     height="98"/>
                            </span>
                            <p>${diagram.name!}</p>
					    </div>
                        </#list>
                    </#if>
                </div>
			</span>
    </div>
    <div class="preview col-md-3 text-right">
			<span class="js-cut">
				<img src="${request.contextPath}/bigdata/v3/static/images/big-data/cut-img.png"/><br/>
				<span>截取缩略图</span>
			</span>

        <span class="js-preview">
                <img src="${request.contextPath}/bigdata/v3/static/images/big-data/computer.png"/><br/>
                <span>预览</span>
        </span>
    </div>
</div><!--头部 E-->

<!--主体 S-->
<div class="main-container clearfix temp-pick">
    <!-- 左侧 -->
    <div class="left-part scrollbar-made">
        <dl class="coverage-wrap">
            <dt>图层</dt>
            <div id="layer-container">

            </div>
        </dl>
    </div>

    <!--右侧样式设置 S-->
    <!--画布设置-->
    <div class="attr-set float-right win-height js-none">
        <div class="clearfix js-click-btn">
            <button class="fake-btn fake-btn-blue no-radius no-border-side width-1of2">样式设置</button>
            <button class="fake-btn fake-btn-default no-radius no-border-side width-1of2">其他参数</button>
        </div>
        <div class="clearfix">
            <div class="set-tota-wrap">
                <div class="set-op set-op-toggle" data-toggle="collapse" href="#collapseExample01">
                    主题风格
                    <div class="assist">
                        <i class="arrow fa fa-angle-up"></i>
                    </div>
                </div>
                <div id="collapseExample01" class="collapse in" aria-expanded="true">
                    <div class="set-detail clearfix">
						<span class="bg <#if screenStyle?? && screenStyle.backgroundColor?default("#072956")=="#072956">active</#if>"
                              data-bg="#072956" data-color="#00cce3" data-default="rgba(127,219,244,.1)">
							<img src="${request.contextPath}/bigdata/v3/static/images/big-data/blue-bg.png" alt=""/>
							科技蓝
						</span>
                        <span class="bg <#if screenStyle?? && screenStyle.backgroundColor?default("#072956")=="#1a1a1a">active</#if>"
                              data-bg="#1a1a1a" data-color="#fff" data-default="rgba(102,102,102,.1)">
							<img src="${request.contextPath}/bigdata/v3/static/images/big-data/black-bg.png" alt=""/>
							暗黑色
						</span>
                    </div>
                </div>

                <div class="set-op set-op-toggle" data-toggle="collapse" href="#collapseExample02">
                    页面尺寸
                    <div class="assist">
                        <i class="arrow fa fa-angle-up"></i>
                    </div>
                </div>
                <div id="collapseExample02" class="collapse in" aria-expanded="true">
                    <div class="set-detail">
                        <div class="size-pick clearfix">
                            <button class="fake-btn fake-btn-default no-radius-right">标准</button>
                            <button class="fake-btn fake-btn-default no-radius no-border-side">移动端</button>
                            <button class="fake-btn fake-btn-blue no-radius-left">自定义</button>
                        </div>

                        <div class="size-type">
                            <div class="sizes clearfix">
                                <p class="mm">
                                    <span>宽度&nbsp;</span>
                                    <input type="text" name="" disabled="disabled" value="1920 px"/>
                                    <span class="plus-minus js-change clearfix">
												<button type="button" disabled="disabled" class="btn btn-default"><i
                                                        class="arrow fa fa-angle-up"></i></button>
												<button type="button" disabled="disabled" class="btn btn-default"><i
                                                        class="arrow fa fa-angle-down"></i></button>
											</span>
                                </p>
                                <p class="mm">
                                    <span>高度&nbsp;</span>
                                    <input type="text" name="" disabled="disabled" value="1080 px"/>
                                    <span class="plus-minus js-change">
												<button type="button" disabled="disabled" class="btn btn-default"><i
                                                        class="arrow fa fa-angle-up"></i></button>
												<button type="button" disabled="disabled" class="btn btn-default"><i
                                                        class="arrow fa fa-angle-down"></i></button>
											</span>
                                </p>
                            </div>
                            <div class="sizes clearfix">
                                <p class="mm">
                                    <span>宽度&nbsp;</span>
                                    <input type="text" name="" disabled="disabled" value="1080 px"/>
                                    <span class="plus-minus js-change clearfix">
												<button type="button" disabled="disabled" class="btn btn-default"><i
                                                        class="arrow fa fa-angle-up"></i></button>
												<button type="button" disabled="disabled" class="btn btn-default"><i
                                                        class="arrow fa fa-angle-down"></i></button>
											</span>
                                </p>
                                <p class="mm">
                                    <span>高度&nbsp;</span>
                                    <input type="text" name="" disabled="disabled" value="1920 px"/>
                                    <span class="plus-minus js-change">
												<button type="button" disabled="disabled" class="btn btn-default"><i
                                                        class="arrow fa fa-angle-up"></i></button>
												<button type="button" disabled="disabled" class="btn btn-default"><i
                                                        class="arrow fa fa-angle-down"></i></button>
											</span>
                                </p>
                            </div>
                            <div class="sizes clearfix active">
                                <p class="mm plus-minus-w">
                                    <span>宽度&nbsp;</span>
                                    <input type="text" name="" id="w-self"
                                           value="${screenStyle.width?default(1920)} px"/>
                                    <span class="plus-minus js-change clearfix">
											<button type="button" class="btn btn-default"><i
                                                    class="arrow fa fa-angle-up"></i></button>
											<button type="button" class="btn btn-default"><i
                                                    class="arrow fa fa-angle-down"></i></button>
										</span>
                                </p>
                                <p class="mm plus-minus-h">
                                    <span>高度&nbsp;</span>
                                    <input type="text" name="" id="h-self"
                                           value="${screenStyle.height?default(1080)} px"/>
                                    <span class="plus-minus js-change">
											<button type="button" class="btn btn-default"><i
                                                    class="arrow fa fa-angle-up"></i></button>
											<button type="button" class="btn btn-default"><i
                                                    class="arrow fa fa-angle-down"></i></button>
										</span>
                                </p>
                            </div>
                        </div>
                    </div>
                </div>
            <#--<div class="font-detail border-bottom-cfd2d4 line-h-39">-->
            <#--<span class="front-title">时间日期设置：</span>-->
            <#--<select id="dateTimeStyle" name=""-->
            <#--class="form-control time-control chosen-select chosen-width inside-select"-->
            <#--data-placeholder="默认">-->
            <#--<option value="1">居左</option>-->
            <#--<option value="2">居右</option>-->
            <#--<option value="3">隐藏</option>-->
            <#--</select>-->
            <#--</div>-->

                <div class="lay-set set-line">
                    辅助线
                    <div class="pos-right right-10">
                        <label class="pos-rel no-margin">
                            <label class="pos-rel no-margin">
                                <input name="course-checkbox" type="checkbox" class="wp">
                                <span class="lbl no-margin"></span>
                            </label>
                        </label>
                    </div>
                </div>
            </div>
            <div class="set-tota-wrap none">
                <div class="set-op set-op-toggle" data-toggle="collapse" href="#collapseExample03">
                    基础设置
                    <div class="assist">
                        <i class="arrow fa fa-angle-up"></i>
                    </div>
                </div>
                <div id="collapseExample03" class="collapse in" aria-expanded="true">
                    <div class="set-detail clearfix">
                        <div class="filter-item">
                            <span class="front-title">名称：</span>
                            <input id="title" value="${screen.name!}" type="text"
                                   class="form-control inside-select js-title" placeholder="请输入名称"/>
                        </div>

                    </div>
                </div>

                <div class="set-op set-op-toggle" data-toggle="collapse" href="#interaction_element_default_parameter">
                    交互默认参数
                    <div class="assist">
                        <i class="arrow fa fa-angle-up"></i>
                    </div>
                </div>
                <div id="interaction_element_default_parameter" class="collapse in" aria-expanded="true">
                    <div class="set-detail clearfix screen-interaction-element">
                            <#if interactionElements?? && interactionElements?size gt 0>
                            <#list interactionElements as interactionElement>
                                <div class="filter-item">
                                    <span class="front-title">${interactionElement.bindKey}：</span>
                                    <input id="${interactionElement.bindKey}"
                                           value="${interactionElement.defaultValue?default("")}" type="text"
                                           class="form-control inside-select" placeholder="请输入默认值"/>
                                </div>
                            </#list>
                            </#if>
                    </div>
                </div>

            </div>
        </div>
    </div>

    <!--图表设置-->
    <div class="attr-set float-right chart-set win-height hidden js-none diagram-config-manager">

    </div>

    <!--底部设置-->
    <div class="flex-bar clearfix">
    <#--<div class="float-left clearfix">-->
    <#--<span class="box-page active">-->
    <#--页面一-->
    <#--</span>-->
    <#--<span class="box-page color-cfd2d4 js-add-page">-->
    <#--<img src="${request.contextPath}/bigdata/v3/static/images/big-data/plus-grey.png" alt="">-->
    <#--</span>-->
    <#--</div>-->

        <div class="chang-size float-right">
            <div class="icon-wrap icon-wrap-plus text-left"><img
                    src="${request.contextPath}/bigdata/v3/static/datav/images/plus.png"/></div>
            <div class="padding-tb-10">
                <input type="range" min="0" max="100" step="1" value="100">
            </div>
            <div class="icon-wrap icon-wrap-minus text-right no-margin-left"><img
                    src="${request.contextPath}/bigdata/v3/static/datav/images/minus.png"/></div>
            <div class="value text-right no-margin-right">100%</div>
        </div>
    </div>

    <!--中间呈现区域 S-->
    <div class="main-show limit-wrap scrollbar-made no-padding col-cell container-height">
        <div class="temp-wrap-big flex-wrap block">
        <#--<div class="box-resize box-outside box-ud box-bottom js-none"></div>-->
        <#--<div class="box-resize box-outside box-lr box-right js-none"></div>-->

            <!--主内容区 S-->
            <div class="temp-wrap js-unbind"
                 style="background-color: ${screenStyle.backgroundColor?default("#072956")}; width: ${screenStyle.width?default(1920)}px;height: ${screenStyle.height?default(1080)}px;">
            <#--<div class="head-wrap">-->
            <#--<h1 class="page-title height-1of1">-->
            <#--<span class="temp-name">${screen.name!}</span>-->
            <#--<div class="date">-->
            <#--<span class="time"></span>-->
            <#--<div class="right">-->
            <#--<span class="week"></span>-->
            <#--<span class="day"></span>-->
            <#--</div>-->
            <#--</div>-->
            <#--<div class="pos-right">-->
            <#--<button class="btn btn-blue none js-full">全屏</button>-->
            <#--</div>-->
            <#--</h1>-->
            <#--</div>-->
                <div class="chart-part clearfix relative">
                    <div class="bg-show"></div>

                    <div id="box-container">

                    </div>
                </div>
            </div><!--主内容区 E-->
        </div>
    </div><!--中间呈现区域 E-->
</div><!--主体 E-->

<div class="key-box">
    <ul>
        <li class="js-highest">置顶 <img src="${request.contextPath}/bigdata/v3/static/images/big-data/icon-highest.png"
                                       class="pos-left"/></li>
        <li class="js-lowest">置底 <img src="${request.contextPath}/bigdata/v3/static/images/big-data/icon-lowest.png"
                                      class="pos-left" alt=""/></li>
        <li class="js-up">上一层 <img src="${request.contextPath}/bigdata/v3/static/images/big-data/icon-up.png"
                                   class="pos-left" alt=""/></li>
        <li class="js-down">下一层 <img src="${request.contextPath}/bigdata/v3/static/images/big-data/icon-down.png"
                                     class="pos-left"/></li>
        <li class="border"></li>
        <li class="js-lock">锁定 <img src="${request.contextPath}/bigdata/v3/static/images/big-data/icon-lock.png"
                                     class="pos-left"/></li>
        <li class="js-unlock">取消锁定 <img src="${request.contextPath}/bigdata/v3/static/images/big-data/icon-unLock.png"
                                    class="pos-left"/></li>
        <#--<li class="js-gand-up">编组 <img src="${request.contextPath}/bigdata/v3/static/images/big-data/icon-gand-up.png"-->
                                       <#--class="pos-left" alt=""/></li>-->
        <#--<li class="js-gand-up-cancel">取消编组 <img-->
                <#--src="${request.contextPath}/bigdata/v3/static/images/big-data/icon-gand-up-cancel.png"-->
                <#--class="pos-left"/></li>-->
        <li class="border"></li>
        <li class="js-collect-library">收藏<img data-library="js-collect-library"
                                              src="${request.contextPath}/bigdata/v3/static/images/big-data/storeroom.png"
                                              class="pos-left"/></li>
        <li class="js-remove-choose">删除<img data-library="js-remove-choose"
                                            src="${request.contextPath}/bigdata/v3/static/images/big-data/remove-icon.png"
                                            class="pos-left" alt=""/></li>
    </ul>
</div>

<script src="${request.contextPath}/bigdata/v3/static/js/jquery/jquery.min.js"></script>
<script src="${request.contextPath}/bigdata/v3/static/datav/layer/layer.js"></script>
<script src="${request.contextPath}/bigdata/v3/static/js/bootstrap/bootstrap.min.js"></script>
<script src="${request.contextPath}/bigdata/v3/static/js/echarts/echarts.4.1.0.rc2.min.js"></script>
<script src="${request.contextPath}/bigdata/v3/static/js/echarts/echarts-wordcloud.min.js"></script>
<script src="${request.contextPath}/bigdata/v3/static/plugs/html2canvas.js"></script>
<script src="${request.contextPath}/bigdata/v3/static/datav/zresize/jquery.ZResize.js" type="text/javascript"
        charset="utf-8"></script>
<script src="${request.contextPath}/bigdata/v3/static/plugs/editor/ace.js" type="text/javascript"></script>
<script src="${request.contextPath}/bigdata/v3/static/plugs/editor/ext-language_tools.js"></script>
<script src="${request.contextPath}/bigdata/v3/static/plugs/editor/mode-json.js"></script>
<script src="${request.contextPath}/bigdata/v3/static/plugs/editor/mode-sql.js"></script>
<script src="${request.contextPath}/bigdata/v3/static/datav/icolor/icolor.js" type="text/javascript"
        charset="utf-8"></script>
<script src="${request.contextPath}/bigdata/v3/static/js/slick/slick.js"></script>
<script src="${request.contextPath}/static/ztree/js/jquery.ztree.all-3.5.min.js"></script>


<script src="${request.contextPath}/bigdata/v3/static/datav/datav-common.js"></script>
<script src="${request.contextPath}/bigdata/v3/static/datav/datav-ui.js"></script>
<script src="${request.contextPath}/bigdata/v3/static/datav/datav-net.js"></script>
<script src="${request.contextPath}/bigdata/v3/static/datav/datav-task.js"></script>
<script src="${request.contextPath}/bigdata/v3/static/datav/datav-diagram-render-echarts.js"></script>
<script src="${request.contextPath}/bigdata/v3/static/datav/datav-diagram-render.js"></script>
<script src="${request.contextPath}/bigdata/v3/static/datav/datav-diagram-render-other.js"></script>
<script src="${request.contextPath}/bigdata/v3/static/datav/datav-diagram-config.js"></script>
<script src="${request.contextPath}/bigdata/v3/static/datav/datav-dymaic-number.js"></script>
<script src="${request.contextPath}/bigdata/v3/static/datav/datav-tab.js"></script>
<script src="${request.contextPath}/bigdata/v3/static/datav/counter/tweenLite.min.js" defer="defer"
        type="text/javascript" charset="utf-8"></script>
<script src="${request.contextPath}/bigdata/v3/static/datav/counter/lem-counter.js" defer="defer" type="text/javascript"
        charset="utf-8"></script>
<script src="${request.contextPath}/bigdata/v3/static/datav/datav-context-menu.js" type="text/javascript" charset="utf-8"></script>
<script src="${request.contextPath}/bigdata/v3/static/datav/datav-keyboard.js" type="text/javascript" charset="utf-8"></script>
<script>
    <#if diagramVos?? && diagramVos?size gt 0>
        <#list diagramVos as diagramVo>
            dataVUI.addExistsDiagram({
                diagramId: "${diagramVo.diagramId}",
                x: "${diagramVo.x?default("20")}",
                y: "${diagramVo.y?default("20")}",
                diagramType: ${diagramVo.diagramType},
                width: "${diagramVo.width}",
                height: "${diagramVo.height}",
                level: "${diagramVo.level}",
                backgroundColor: "${diagramVo.backgroundColor?default("")}"
            });
            <#if diagramVo.updateInterval?default(-1) gt 0>
                dataVTimer.addTask(function () {
                    dataVRender.doRender({screenId: _screenId, diagramId: '${diagramVo.diagramId}', dispose: false});
                }, '${diagramVo.diagramId}', ${diagramVo.updateInterval} * 1000
            )
            ;
            </#if>
        </#list>
    </#if>
    dataVAce.refreshTips();
</script>
<script type="text/javascript">
    var count = 1;
    var a = 1;
    $(document).ready(function () {

        dataVUI.loadLayer();
        /**
         * 禁止浏览器的右击事件
         * */
        document.oncontextmenu = function(){
            return false;
        }
        $(document).bind("selectstart", function (e) {
            return false;
        });
        $('#w-self').val($('.temp-wrap').width() + 2 + 'px');
        $('#h-self').val($('.temp-wrap').height() + 2 + 'px');
        $(window).resize(function () {
            $('#w-self').val($('.temp-wrap').width() + 2 + 'px');
        });

        $('.imgs-choice').each(function (index, ele) {
            $(this).css({
                height: $(this).prev().children().length * 50,
                overflowY: 'auto'
            })
        });

        //缩略图宽高
        // function imgHeight() {
        //     $('.temp-show img').each(function (index, ele) {
        //         $(this).css({
        //             height: $(this).width() * 3 / 4,
        //         });
        //     })
        // }

        // imgHeight();
        //文本设置，图表设置显现
        $('.chart-part').on('mousedown', '.box-data', function () {
            $(this).addClass('zIndex');
        });

        //1.背景设置
        $('.js-click-btn button').on('click', function () {
            $(this).addClass('fake-btn-blue').removeClass('fake-btn-default').siblings('button').removeClass('fake-btn-blue').addClass('fake-btn-default');
            $('.js-click-btn').next('div').find('.set-tota-wrap').eq($(this).index()).removeClass('none').siblings('.set-tota-wrap').addClass('none');
        });

        //背景颜色设置
        $('.temp-wrap').css('background-color', $('.set-detail').find('.bg.active').data('bg'));
        $('#dateTimeStyle').val('${screenStyle.dateTimeStyle?default(1)}');
        $('.set-detail .bg').click(function () {
            $(this).addClass('active').siblings().removeClass('active');
            // $('.temp-name').css('color',$(this).data('color'));
            $('.temp-wrap').css('background-color', $(this).data('bg'));
            var arrs = ['bg-blue', 'bg-black'];
            for (var i = 0; i < arrs.length; i++) {
                $('.temp-wrap').removeClass(arrs[i]);
            }
            $('.temp-wrap').addClass(arrs[$(this).index()]);
            dataVUI.doUpdateScreenStyle();
        });
        $('.size-pick button').click(function () {
            $(this).removeClass('fake-btn-default').addClass('fake-btn-blue').siblings().addClass('fake-btn-default').removeClass('fake-btn-blue');
            $('.size-type .sizes').eq($(this).index()).addClass('active').siblings().removeClass('active');
            let width = parseInt($('.size-type .sizes').eq($(this).index()).find('input').eq(0).val());
            let height = parseInt($('.size-type .sizes').eq($(this).index()).find('input').eq(1).val());
            $('.temp-wrap').width(width);
            $('.temp-wrap').height(height);
            if ($(this).index() == 2) {
                $('.temp-wrap-big>.box-resize').removeClass('hidden')
            } else {
                $('.temp-wrap-big>.box-resize').addClass('hidden')
            }
            dataVUI.doUpdateScreenStyle(width, height);
        });

        var arr = [10, -10];
        $('.js-change>button').on('click', function () {
            var $val = parseInt($(this).parent().siblings('input').val());
            var $num = $val + arr[$(this).index()];
            if ($num >= 0) {
                $(this).parent().siblings('input').val($num + ' px');
            }
            dataVUI.doUpdateScreenStyle();
        });
        $('.plus-minus-w input[type="text"]').on('blur', function () {
            $('.temp-wrap').width(parseInt($(this).val()));
            dataVUI.doUpdateScreenStyle();
        });
        $('.plus-minus-w button').on('click', function () {
            var $w = $('.temp-wrap').width();
            $('.temp-wrap').width($w + arr[$(this).index()]);
        });
        $('.plus-minus-h input[type="text"]').on('blur', function () {
            $('.temp-wrap').height(parseInt($(this).val()));
            dataVUI.doUpdateScreenStyle();
        });
        $('.plus-minus-h button').on('click', function () {
            var $h = $('.temp-wrap').height();
            $('.temp-wrap').height($h + arr[$(this).index()]);
        });
    <#--//设置时间位置-->
    <#--$('.time-control').on('click', function () {-->
    <#--var $val = $(this).find('option:selected').val();-->
    <#--setDateTimeStyle($val);-->
    <#--dataVUI.doUpdateScreenStyle();-->
    <#--});-->
    <#--setDateTimeStyle(${screenStyle.dateTimeStyle?default(1)});-->

        // function setDateTimeStyle(val) {
        //     if (val == 1) {
        //         $('.date').removeClass('right-20').removeClass('hide')
        //     } else if (val == 2) {
        //         $('.date').addClass('right-20').removeClass('hide')
        //     } else {
        //         $('.date').addClass('hide')
        //     }
        // }

        //网格线
        $('.set-line .pos-rel input').click(function () {
            $('.temp-wrap').toggleClass('active')
        });
        //设置驾驶舱名称
        $('#title').on('blur', function () {
            var $text = $(this).val().replace(/^\s+|\s+$/g, "");
            if ($text.length != 0) {
                $('.temp-name').text($text)
            }
            dataVNet.updateScreenName($text);
        });

        //2.图表设置
        $('.js-choice').on('change', function () {
            $('.js-choice-target>div').eq($(this).val()).removeClass('none').siblings().addClass('none')
        });

        //头部
        //选择项显现
        $('.js-add-other').mouseenter(function () {
            $(this).find('.other-data').show();
        }).mouseleave(function () {
            $(this).find('.other-data').hide()
        });
        //图表类型选择
        $('.temp-logo>div').on('click', function () {
            $(this).addClass('active').siblings().removeClass('active');
            $(this).parent().siblings('div').children('div').eq($(this).index()).removeClass('hide').siblings().addClass('hide');
        });

        //截图
        // $('.js-cut').on('click', function () {
        //     html2canvas($(".temp-wrap")[0]).then(canvas => {
        //         $('.cut-pic').css({
        //             width: 190,
        //             height: 190
        //         });
        //         $('.cut-pic').append(canvas);
        //         $('.cut-pic canvas').width(190).height(190);
        //     });
        // });

        //底部
        //画布伸缩设置
        var $val;
        $('input[type="range"]').bind('input', function () {
            $val = $('input[type="range"]').val();
            scaleBar()
        });
        $('.icon-wrap-plus').on('click', function () {
            $val = $('input[type="range"]').val();
            $val++;
            if ($val >= 100) {
                $val = 100
            }
            scaleBar()
        });
        $('.icon-wrap-minus').on('click', function () {
            $val = $('input[type="range"]').val();
            $val--;
            if ($val <= 0) {
                $val = 0
            }
            scaleBar()
        });

        function scaleBar() {
            $('.value').text($val + '%');
            $('input[type="range"]').val($val);
            $num = $val / 100;
            $x = -(1 - $num) * $('.temp-wrap-big').width() + 'px';
            $y = -(1 - $num) * $('.temp-wrap-big').height() + 'px';
            $('.temp-wrap-big').css({
                transform: 'scale(' + $num + ')',
                'transform-origin': '0% 0%',
                '-webkit-transform-origin': '0% 0%'
            })
        }

        //时间日期
        function getDate() {
            var date = new Date();
            var week = {
                0: '日',
                1: '一',
                2: '二',
                3: '三',
                4: '四',
                5: '五',
                6: '六'
            };
            var hours = date.getHours() < 10 ? "0" + date.getHours() : date.getHours();
            var minutes = date.getMinutes() < 10 ? "0" + date.getMinutes() : date.getMinutes();
            var seconds = date.getSeconds() < 10 ? "0" + date.getSeconds() : date.getSeconds();
            var month = (date.getMonth() + 1) < 10 ? "0" + (date.getMonth() + 1) : date.getMonth() + 1;
            var year = date.getFullYear();
            var day = date.getDate();
            var cur_week = '星期' + week[date.getDay()];
            if ($('.date .time').length > 0) {
                $('.date .time').text(hours + ":" + minutes + ":" + seconds);
            }
            // $('.date .day').text(year + '-' + month + '-' + day);
            // $('.date .week').text(cur_week);
        }

        getDate();
        setInterval(getDate, 1000);

        var context = new DataVContext();


        //图表右键点击
        $('.chart-part').on('contextmenu', '.box-data', function (e) {
            e.preventDefault();
            if (e.which == 3) {
                context.showContext(e);
            } else {
                context.hideContext(e);
            }

        });

        /***
         * 图层鼠标右击事件
         */
        $('body').on('contextmenu', '.left-part dd', function (e) {
            var index = $(this).data('index');
            /*$('.temp-wrap-big.block .box-data.choose').removeClass('choose');
            $('.temp-wrap-big.block .box-data[data-index='+index+']').addClass('choose');*/
            if (e.which == 3) {
                e.preventDefault();
                if (!$(this).hasClass('active')) {
                    $('.left-part .active').removeClass('active');
                    $(this).addClass('active');
                    $('.box-data[data-index=' + index + ']').addClass('choose').siblings().removeClass('choose');
                }
                context.showContext(e);
            }
        });
        /**
         * 图层鼠标左击事件
         */
        $('body').on('mousedown', '.left-part dd', function (e) {
            var index = $(this).data('index');
            if (e.which == 1) {
                if (e.ctrlKey == 1) {               //ctrl键按下
                    $(this).toggleClass('active');
                    $('.box-data[data-index=' + index + ']').toggleClass('choose');

                    //多选，则取消显示参数
                    if ($('.box-data.choose').length > 1) {
                        $('.diagram-config-manager').addClass('hidden').siblings('.attr-set').removeClass('hidden')
                    }
                } else {
                    $('.left-part .active').removeClass('active');
                    $(this).addClass('active');
                    $('.box-data[data-index=' + index + ']').addClass('choose').siblings().removeClass('choose');
                }
            }
        });
    });
</script>
<script>
    $(function () {
        var screenId = "${screenId}";
        $('.js-add-chart').find('.pic-wrap').each(function (index, ele) {
            $(ele).on('click', function () {
                var diagram = {};
                diagram.screenId = screenId;
                diagram.diagramType = $(this).data('diagramtype')
                dataVUI.addDiagram(diagram);
            });
        });
        $('.js-add-text').on('click', function () {
            dataVUI.addDiagram({screenId: _screenId, diagramType: 94});
        });

        $('.js-add-table').on('click', function () {
            dataVUI.addDiagram({screenId: _screenId, diagramType: 99});
        });

        $('.js-add-library').find('.pic-wrap').each(function (index, el) {
            $(el).on('click', function () {
                dataVUI.addDiagram({
                    libraryId: $(this).data('libraryid'),
                    diagramType: $(this).data('diagramtype'),
                    screenId: _screenId
                });
            })
        });

        $('body').on('click', '.js-remove', function () {
            let diagramId = $(this).parent().attr('data-index');
            //$('.temp-wrap-big.block .box-data.choose').remove();
            dataVUI.deleteDiagram(screenId, diagramId);
        });

        /**
         * 大屏图表的鼠标事件
         */
        $('.chart-part').on('mousedown', '.box-data', function (e) {
            var dataIndex = $(this).attr('data-index');
            // if (e.which === 1 && e.ctrlKey) {
            //     //
            //     $(this).toggleClass('choose');
            //     $('.left-part dd[data-index="'+dataIndex+'"]').toggleClass('active')
            //     return;
            // }

            if ($(this).hasClass('choose')) {

                return false;
            }
            $('.left-part dd[data-index="'+dataIndex+'"]').addClass('active').siblings().removeClass('active');
            $(this).addClass('choose').siblings('.box-data').removeClass('choose');
            return false;
        });

        $('.limit-wrap').mousedown(function (e) {
            if (e.target.className !== "box-data") {
                dataVUI.hideConfigPanel();
            }
        });


        $('.js-cut').on('click', function () {
            $('.temp-wrap-big.block').find('.box-data').each(function () {
                if ($(this).children().hasClass('border-self') || $(this).children().hasClass('iframe-self') || $(this).children().hasClass('title-self')) {
                    $(this).find('.box-data-body').addClass('opacity')
                }
            });
            html2canvas($(".temp-wrap")[0]).then(canvas=> {
                $('.cut-pic').css({
                    width: 190,
                    height: 190
                });
                let data = canvas.toDataURL("image/jpeg");
                dataVNet.doUploadShot(data);

                $('.temp-wrap-big.block').find('.box-data').each(function () {
                    if ($(this).children().hasClass('border-self') || $(this).children().hasClass('iframe-self') || $(this).children().hasClass('title-self')) {
                        $(this).find('.box-data-body').removeClass('opacity')
                    }
                });
            });
        });
        //预览
        $('.js-preview').on('click', function () {
            window.open('${request.contextPath}/bigdata/datav/screen/view/${screenId}');
        });
    });

</script>
</body>
</html>