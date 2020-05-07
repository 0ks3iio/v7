<#macro commonWeb title="智慧校园" showFramework=false desktopIndex=true fullLoad=false>
<#if showFramework?default(false)>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1" />
    <meta charset="utf-8" />
    <title>${title!"首页"}</title>
	<#if favicon?default(true)>
    <link rel="icon" href="${request.contextPath}/static/images/icons/favicon.ico" type="image/x-icon" />
    <link rel="shortcut icon" href="${request.contextPath}/static/images/icons/favicon.ico" type="image/x-icon" />
	</#if>
    <meta name="description" content="" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0" />

	<!-- bootstrap & fontawesome -->
    <link rel="stylesheet" href="${request.contextPath}/static/components/bootstrap/dist/css/bootstrap.min.css" />
    <link rel="stylesheet" href="${request.contextPath}/static/components/font-awesome/css/font-awesome.css" />
    
    <!-- page specific plugin styles -->
	<link rel="stylesheet" href="${request.contextPath}/static/components/fullcalendar/dist/fullcalendar.min.css">
    
    <link rel="stylesheet" href="${request.contextPath}/static/css/iconfont.css">
    <link rel="stylesheet" href="${request.contextPath}/static/css/components.css">
	<#if fullLoad>
        <link rel="stylesheet" href="${request.contextPath}/static/css/page-desktop.css">
        <link rel="stylesheet" href="${request.contextPath}/static/css/page-desk.css">
	</#if>
	<link rel="stylesheet" href="${request.contextPath}/static/css/pages.css"/>
	<link rel="stylesheet" type="text/css" href="${request.contextPath}/static/components/icolor/icolor.css"/>
    <!--[if lte IE 9]>
	<link rel="stylesheet" href="${request.contextPath}/static/css/pages-ie.css" />
    <![endif]-->
    

    <!-- page specific plugin styles -->
    <link rel="stylesheet" href="${request.contextPath}/static/components/layer/skin/layer.css">
    <link rel="stylesheet" href="${request.contextPath}/static/components/chosen/chosen.min.css">
    <link rel="stylesheet" href="${request.contextPath}/static/components/bootstrap-datepicker/dist/css/bootstrap-datepicker3.css">
	
    <link rel="stylesheet" href="${request.contextPath}/static/components/bootstrap-datetimepicker/css/bootstrap-datetimepicker.min.css">

    <!--[if !IE]> -->
    <script src="${request.contextPath}/static/components/jquery/dist/jquery.min.js"></script>
    <!-- <![endif]-->
    <!--[if IE]>
    <script src="${request.contextPath}/static/components/jquery.1x/dist/jquery.min.js"></script>
    <![endif]-->
    
    <script src="${request.contextPath}/static/components/bootstrap/dist/js/bootstrap.min.js"></script>
    <#if fullLoad>
        <script src="${request.contextPath}/static/components/moment/moment.min.js"></script>
		<script src="${request.contextPath}/static/components/fullcalendar/dist/fullcalendar.js"></script>
    	<script src="${request.contextPath}/static/assets/js/ace-extra.js" async="async" defer="defer"></script>
    	<!-- ace scripts -->
    	<script src="${request.contextPath}/static/assets/js/src/ace.ajax-content.js" async="async" defer="defer"></script>  		<!-- 异步加载js -->
    	<script src="${request.contextPath}/static/components/jquery-slimscroll/jquery.slimscroll.min.js" async="async" defer="defer"></script>
    	<script src="${request.contextPath}/static/js/desktop.js"></script>
    	<script type="text/javascript" src="${request.contextPath}/static/echarts/echarts.min.js" async="async" defer="defer"></script>
		<script src="${request.contextPath}/static/js/md5.js" async="async" defer="defer"></script>
		<script src="${request.contextPath}/static/js/chartsScript.js" async="async" defer="defer"></script>
		<script src="${request.contextPath}/static/components/typeahead.js/dist/typeahead.jquery.min.js" async="async" defer="defer"></script>
        <script src="${request.contextPath}/static/components/dragsort/jquery-list-dragsort.js" async="async" defer="defer"></script>
	    <script src="${request.contextPath}/static/components/bootstrap-datetimepicker/js/bootstrap-datetimepicker.js"></script>
    	<script src="${request.contextPath}/static/components/bootstrap-datetimepicker/js/locales/bootstrap-datetimepicker.zh-CN.js" async="async" defer="defer"></script>
		<script src="${request.contextPath}/static/components/fullcalendar/dist/lang/zh-cn.js"></script>
	</#if>

    <!--日期控件-->

    

    
    <!-- inline styles related to this page -->
    <!-- ace settings handler -->
    
    <!-- HTML5shiv and Respond.js for IE8 to support HTML5 elements and media queries -->
    <!--[if lte IE 8]>
    <script src="${request.contextPath}/static/components/html5shiv/dist/html5shiv.min.js"  async="async" defer="defer"></script>
    <script src="${request.contextPath}/static/components/respond/dest/respond.min.js" async="async" defer="defer"></script>
    <![endif]-->
    <!-- basic scripts -->
    <script type="text/javascript">
        if('ontouchstart' in document.documentElement) document.write("<script src='${request.contextPath}/static/components/_mod/jquery.mobile.custom/jquery.mobile.custom.js'>"+"<"+"/script>");
    </script>
    <!-- page specific plugin scripts -->
    <script src="${request.contextPath}/static/components/layer/layer.js" ></script>
    <script src="${request.contextPath}/static/components/jquery-slimscroll/jquery.slimscroll.min.js" async="async" defer="defer"></script>
    <script src="${request.contextPath}/static/components/chosen/chosen.jquery.min.js" async="async" defer="defer"></script>
    <script src="${request.contextPath}/static/cookie/jquery.cookie.js" async="async" defer="defer"></script>
    <script src="${request.contextPath}/static/js/jquery.form.js" async="async" defer="defer"></script>
    <script src="${request.contextPath}/static/js/tool.js" async="async" defer="defer"></script>
    <script src="${request.contextPath}/static/js/cache.js" async="async" defer="defer"></script>
    <script src="${request.contextPath}/static/bigdata/icolor/icolor.js"></script>
    <#--
	<script src="${request.contextPath}/static/js/LodopFuncs.js"></script>
	-->

    </head>

<body class="no-skin">
	</#if>
	<#if !desktopIndex && showFramework>
		<div class="page-content"><div class="row"><div class="col-xs-12" id="deskTopContainer" >
	</#if>
		<#nested />
	<#if !desktopIndex && showFramework>
		</div></div></div>
	</#if>
		
	<#if  showFramework?default(false)>
	<div class="layer layer-tip layerTipOpen">
		<div class="layer-content">
			<div class="layer-body">
				<i class="layer-tip-icon layer-tip-icon-success layerTipClass layerTipOpenSucc" style="display:none"></i>
				<i class="layer-tip-icon layer-tip-icon-failed layerTipClass layerTipOpenFail" style="display:none"></i>
				<i class="layer-tip-icon layer-tip-icon-warning layerTipClass layerTipOpenWarn" style="display:none"></i>
				<h3 class="layer-title openTisTitle"></h3>
				<p class="openTisMsg"></p>
			</div>
		</div>
	</div>
	<script>
        _contextPath = "${request.contextPath?default("")}";
       self.setInterval(function(){
       	$.post( "${request.contextPath}/checkLive", function( data ) {
		});
       }, 1200000);
	</script>
	</body>
	</html>
	</#if>
</#macro>


<#macro commonWeb2 title="">

<#if showFramework?default(false)>
<!DOCTYPE html>
<html lang="en">
	<head>
		<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1" />
		<meta charset="utf-8" />
		<title>${title!}</title>
		<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0" />
		
		
		<!--[if !IE]> -->
		<script type="text/javascript">
			window.jQuery || document.write("<script src='${request.contextPath}/static/ace/js/jquery.js'>"+"<"+"/script>");
		</script>

		<!-- <![endif]-->

		<!--[if IE]>
		<script type="text/javascript">
		 window.jQuery || document.write("<script src='${request.contextPath}/static/ace/js/jquery1x.js'>"+"<"+"/script>");
		</script>
		<![endif]-->
		<script type="text/javascript">
			if('ontouchstart' in document.documentElement) document.write("<script src='${request.contextPath}/static/ace/js/jquery.mobile.custom.js'>"+"<"+"/script>");
		</script>
		<script src="${request.contextPath}/static/ace/js/bootstrap.js"></script>
		
		<link rel="stylesheet" href="${request.contextPath}/static/ext/layout-fw.css" />

		<!--[if !IE]> -->
		<link rel="stylesheet" href="${request.contextPath}/static/ace/css/pace.css" />
		<script data-pace-options='{ "ajax": true, "document": true, "eventLag": false, "elements": false }' src="${request.contextPath}/static/ace/js/pace.js"></script>
		<!-- <![endif]-->
		<!-- bootstrap & fontawesome -->
		<link rel="stylesheet" href="${request.contextPath}/static/ace/css/bootstrap.css" />
		<link rel="stylesheet" href="${request.contextPath}/static/ace/css/font-awesome.css" />
		<!-- text fonts -->
		<link rel="stylesheet" href="${request.contextPath}/static/ace/css/ace-fonts.css" />
		<!-- ace styles -->
		<link rel="stylesheet" href="${request.contextPath}/static/ace/css/ace.min.css" class="ace-main-stylesheet" id="main-ace-style" />
		<!--[if lte IE 9]>
			<link rel="stylesheet" href="${request.contextPath}/static/ace/css/ace-part2.css" class="ace-main-stylesheet" />
		<![endif]-->
		<!--[if lte IE 9]>
		  <link rel="stylesheet" href="${request.contextPath}/static/ace/css/ace-ie.css" />
		<![endif]-->
		<!-- ace settings handler -->
		<script src="${request.contextPath}/static/ace/js/ace-extra.js"></script>
		<!-- HTML5 shim and Respond.js IE8 support of HTML5 elements and media queries -->
		<!--[if lte IE 8]>
		<script src="${request.contextPath}/static/ace/js/html5shiv.js"></script>
		<script src="${request.contextPath}/static/ace/js/respond.js"></script>
		<![endif]-->
		
		<!--[if IE]>
		<script src="${request.contextPath}/static/ace/components/jquery.1x/dist/jquery.js"></script>
		<![endif]-->
		<script type="text/javascript">
			if('ontouchstart' in document.documentElement) document.write("<script src='${request.contextPath}/static/ace/components/_mod/jquery.mobile.custom/jquery.mobile.custom.js'>"+"<"+"/script>");
		</script>
		<link rel="stylesheet" href="${request.contextPath}/static/ace/components/_mod/jquery-ui.custom/jquery-ui.custom.css" />
		<script src="${request.contextPath}/static/ace/components/_mod/jquery-ui.custom/jquery-ui.custom.js"></script>
		<script src="${request.contextPath}/static/ace/components/jqueryui-touch-punch/jquery.ui.touch-punch.js"></script>
		<script src="${request.contextPath}/static/ace/components/bootstrap/dist/js/bootstrap.js"></script>
		<!-- ace scripts -->
		<script src="${request.contextPath}/static/ace/assets/js/src/elements.scroller.js"></script>
		<script src="${request.contextPath}/static/ace/assets/js/src/ace.js"></script>
		<script src="${request.contextPath}/static/ace/assets/js/src/ace.basics.js"></script>
		<script src="${request.contextPath}/static/ace/assets/js/src/ace.ajax-content.js"></script>
		<script src="${request.contextPath}/static/ace/assets/js/src/ace.sidebar.js"></script>
		<script src="${request.contextPath}/static/ace/assets/js/src/ace.sidebar-scroll-1.js"></script>
		<script src="${request.contextPath}/static/ace/assets/js/src/ace.submenu-hover.js"></script>
		<#--
		这里先加载这个与ace.widget-box.js一起使用是有点问题先注释了
		<script src="${request.contextPath}/static/ace/assets/js/src/ace.widget-on-reload.js"></script>
		 -->
		<script src="${request.contextPath}/static/ace/components/autosize/dist/autosize.js"></script>
		
		<script src="${request.contextPath}/static/layer/layer.js"></script>
		<script src="${request.contextPath}/static/layer/extend/layer.ext.js"></script>
		<link rel="stylesheet" href="${request.contextPath}/static/layer/skin/layer.css" />
		<link rel="stylesheet" href="${request.contextPath}/static/layer/skin/layer.ext.css" />
		
		<script src="${request.contextPath}/static/js/tool.js"></script>
		
	</head>
</#if>
<body>		
		<#nested />
</body>
</html>
</#macro>

<#macro putSubSystem subSystemDto>
<#--子系统-->
<#if subSystemDto.subSystemDtos?size lte 0>
	<#--有模块的子系统-->
	<#if subSystemDto.modelDtos?size gt 0>
	<li class="open">
		<a href="javascript:;" class="dropdown-toggle">
			<i class="menu-icon fa fa-folder"></i>
			<span class="menu-text">
				${subSystemDto.subSystem.name!}
				<#--<span class="badge badge-primary">${subSystemDto.modelDtos?size}</span>-->
			</span>
			<b class="arrow fa fa-angle-down"></b>
		</a>
		<b class="arrow"></b>
		<ul class="submenu nav-show">
			<@putModels subSystemDto />
		</ul>
	</li>
	<#else>
	<#--没有模块的子系统-->
	<li class="">
		<a data-url="${subSystemDto.subSystem.url!}" href="#${subSystemDto.subSystem.url!}">
			<i class="menu-icon fa fa-fire"></i>
			<span class="menu-text"> ${subSystemDto.subSystem.name!} </span>
		</a>
		<b class="arrow"></b>
	</li>
	</#if>
<#else>
<#--子系统目录-->
<li class="">
	<a href="javascript:;" class="dropdown-toggle">
		<i class="menu-icon fa fa-folder-o"></i>
		<span class="menu-text">
			${subSystemDto.subSystem.name!}
			<#--<span class="badge badge-primary">${subSystemDto.subSystemDtos?size}</span>-->
		</span>
		<b class="arrow fa fa-angle-down"></b>
	</a>
	<b class="arrow"></b>
	<ul class="submenu">
		<#list subSystemDto.subSystemDtos as subSystem2>
		<@putSubSystem subSystem2 />
		</#list>
	</ul>
</li>
</#if>
</#macro>

<#macro putModel modelDto>
<li class="">
	<a data-url="${modelDto.model.url!}" href="#${modelDto.model.url!}">
		<i class="menu-icon fa fa-book"></i>
		<span class="menu-text"> ${modelDto.model.name!} </span>
	</a>
	<b class="arrow"></b>
</li>

</#macro>

<#macro putModels subSystem>
<#list subSystem.modelDtos as modelDto>
<#if modelDto.subModelDtos?size gt 0>
<li class="active open">
	<a href="javascript:;" class="dropdown-toggle">
		<i class="menu-icon fa fa-hdd-o"></i>
		<span class="menu-text">
			${modelDto.model.name!}
			<#--<span class="badge badge-primary">${modelDto.subModelDtos?size}</span>-->
		</span>
		<b class="arrow fa fa-angle-down"></b>
	</a>
	<b class="arrow"></b>
	<ul class="submenu">
		<#list modelDto.subModelDtos as model2>
		<@putModel model2 />
		</#list>
	</ul>
</li>
<#else>
<li class="">
	<a data-url="${modelDto.model.url!}" href="#${modelDto.model.url!}">
		<i class="menu-icon fa fa-book"></i>
		<span class="menu-text"> ${modelDto.model.name!} </span>
	</a>
	<b class="arrow"></b>
</li>
</#if>
</#list>
</#macro>

<#macro inputDiv columnInfo value id="" displayName="" vtype="" placeholder="" readonly=false divInner="" inputInner="" divClass="" inputClass="" cols=2 >
	<#if displayName == "">
		<#local displayName2 = (columnInfo[id].displayName)!"" />
		<#if displayName2 == "">
		<#local displayName2 = id />
		</#if>
	<#else>
		<#local displayName2 = displayName>
	</#if>
	<#local format = (columnInfo[id].format)!"" />
	<#local nullable = (columnInfo[id].nullable?string("true", "false"))! />
	<div class="form-group ${divClass!}" id="form-group-${id!}" ${divInner}>
		<label class="<#if nullable?default("true") == "false">ace-icon red fa fa-circle </#if>col-md-3 control-label no-padding-right" for="${id!}"> ${displayName2!} </label>
		<div class="col-xs-12 col-sm-12 col-md-9">
			<span class="block input-icon input-icon-right">
			<#if columnInfo[id]?exists>
				<#local maxLength = columnInfo[id].maxLength!1000 />
				<#local minLength = columnInfo[id].minLength!0 />
				<#local min = columnInfo[id].min!" />
				<#local max = columnInfo[id].max!" />
				<#local length = columnInfo[id].length!0 />
				<#if vtype == "">
					<#local vtype = columnInfo[id].vtype!"string" />
				</#if>
				<#local regex = columnInfo[id].regex!>
				<#local regexTip = columnInfo[id].regexTip!>
			<#else>
				<#local maxLength = 1000 />
				<#local minLength = (columnInfo[id].minLength)!0 />
				<#local vtype = "string" />
				<#local min = "" />
				<#local max = "" />
				<#local regex = "">
				<#local regexTip = "">
			</#if>
			<input min="${min!}" max="${max!}" minLength="${minLength!0}" length="${length!0}" vtype="${vtype!string}" maxLength="${maxLength!1000}" nullable="${nullable!true}" regex="${regex!}" regexTip="${regexTip!}" ${inputInner!} <#if readonly!false == true>disabled</#if> <#if "password"=vtype?default("")>type="password"<#else>type="text" </#if> id="${id!}" oid="${id!}" placeholder="<#if placeholder="">${displayName2}<#else>${placeholder!}</#if>" class="form-control col-xs-10 col-sm-10 col-md-10 ${inputClass!}" value="<#if format?default("") != "" && value?is_date>${(value?string(format))!}<#else>${value!}</#if>" />
			</span>
		</div>
	</div>
</#macro>

<#macro dateDiv columnInfo value id="" displayName=""  placeholder="" vtype="date" readonly=false divInner="" inputInner="" divClass="" inputClass="" cols=2 >
	<#if displayName == "">
		<#local displayName2 = (columnInfo[id].displayName)!"" />
		<#if displayName2 == "">
		<#local displayName2 = id />
		</#if>
	<#else>
		<#local displayName2 = displayName>
	</#if>
	<#local format = (columnInfo[id].format)!"" />
	<#local nullable = (columnInfo[id].nullable?string("true", "false"))! />
	<div class="form-group ${divClass!}" id="form-group-${id!}" ${divInner}>
		<label class="<#if nullable?default("true") == "false">ace-icon red fa fa-circle </#if>col-md-3 control-label no-padding-right" for="${id!}"> ${displayName2!} </label>
		<div class="col-xs-12 col-sm-12 col-md-9">
			<span class="block input-icon input-icon-right">
			<input stype="calendar" vtype="${vtype!}" nullable="${nullable!}" ${inputInner!} <#if readonly!false == true>disabled</#if> type="text" id="${id!}${readonly?string("_disabled", "")}" oid="${id!}" placeholder="<#if placeholder="">${displayName2}<#else>${placeholder!}</#if>" class="form-control col-xs-10 col-sm-10 col-md-10 ${inputClass!}" value="<#if format?default("") != "" && value?is_date>${(value?string(format))!}<#else>${value!}</#if>" />
			<i class='ace-icon fa fa-calendar'></i>
			</span>
		</div>
	</div>
</#macro>

<#macro dateDivWithNoColumn nullable=""  value="" id="" displayName=""  placeholder="" vtype="date" readonly=false divInner="" inputInner="" divClass="" inputClass="" cols=2 >
	<div class="form-group ${divClass!}" id="form-group-${id!}" ${divInner}>
		<div class="">
			<span class="block input-icon input-icon-right">
			<input name="${id!}" nullable="${nullable}" stype="calendar" vtype="${vtype!}" nullable="${nullable!}" ${inputInner!} <#if readonly!false == true>disabled</#if> type="text" id="${id!}${readonly?string("_disabled", "")}" oid="${id!}" placeholder="<#if placeholder="">${displayName}<#else>${placeholder!}</#if>" class="form-control  ${inputClass!}" value="<#if format?default("") != "" && value?is_date>${(value?string(format))!}<#else>${value!}</#if>" />
			<i class='ace-icon fa fa-calendar'></i>
			</span>
		</div>
	</div>
</#macro>


<#macro selectDiv columnInfo id="" displayName="" value=""  readonly=false mcodeId=""  divInner="" selectInner="" divClass="" selectClass="" cols=2 multiSelect=false>
<div class="form-group ${divClass!}" id="form-group-${id!}" ${divInner}>
	<#if displayName == "">
		<#local displayName2 = (columnInfo[id].displayName)!"" />
		<#if displayName2 == "">
		<#local displayName2 = id />
		</#if>
	<#else>
		<#local displayName2 = displayName>
	</#if>
	<#local nullable = (columnInfo[id].nullable?string("true", "false"))! />
	<label class="<#if nullable?default("true") == "false">ace-icon red fa fa-circle </#if>col-md-3 control-label no-padding-right" for="${id!}"> ${displayName2!} </label>
	<div class="col-md-9">
		<div>
			<select <#if multiSelect>multiple=""</#if> id="${id!}" oid="${id!}"  nullable="${nullable!}" data-placeholder="请选择" <#if readonly == true>disabled</#if> class="multiselect form-control col-md-10 col-sm-10 col-xs-10 ${selectClass!}"  ${selectInner!}>		
				<#if mcodeId == "">
				${mcodeSetting.getMcodeSelect((columnInfo[id].mcodeId)!"", value?string, "1")}
				<#else>
				${mcodeSetting.getMcodeSelect(mcodeId, value?string, "1")}
				</#if>
			</select>
		</div>
	</div>
</div>
</#macro>

<#macro selectVselectDiv columnInfo id="" displayName="" value=""  readonly=false vselect=[]  divInner="" selectInner="" divClass="" selectClass="" cols=2 multiSelect=false>
<div class="form-group ${divClass!}" id="form-group-${id!}" ${divInner}>
	<#if displayName == "">
		<#local displayName2 = (columnInfo[id].displayName)!"" />
		<#if displayName2 == "">
		<#local displayName2 = id />
		</#if>
	<#else>
		<#local displayName2 = displayName>
	</#if>
	<#local nullable = (columnInfo[id].nullable?string("true", "false"))! />
	<label class="<#if nullable?default("true") == "false">ace-icon red fa fa-circle </#if>col-md-3 control-label no-padding-right" for="${id!}"> ${displayName2!} </label>
	<div class="col-md-9">
		<div>
			<select <#if multiSelect>multiple=""</#if> id="${id!}" oid="${id!}"  nullable="${nullable!}" data-placeholder="请选择" <#if readonly == true>disabled</#if> class="multiselect form-control col-md-10 col-sm-10 col-xs-10 ${selectClass!}"  ${selectInner!}>		
				<#if vselect?size gt 0>
					<option value="">--- 请选择 ---</option>
					<#list vselect as vs>
						<#local vs2 = vs?split(":")>
						<option value="${vs2[0]!}" <#if value?string?default("") == vs2[0]?string?default("")>selected</#if> >${vs2[1]!}</option>
					</#list>				
				<#else>
					<option value="">--- 请选择 ---</option>
				</#if>
			</select>
		</div>
	</div>
</div>
</#macro>

<#-- obj表示整个对象，因为vsql可能会带有对象的一些参数，所以需要传入 -->
<#macro selectVsqlDiv columnInfo obj id="" displayName="" value=""  readonly=false vsql=""  divInner="" selectInner="" divClass="" selectClass="" cols=2 multiSelect=false>
<div class="form-group ${divClass!}" id="form-group-${id!}" ${divInner}>
	<#if displayName == "">
		<#local displayName2 = (columnInfo[id].displayName)!"" />
		<#if displayName2 == "">
		<#local displayName2 = id />
		</#if>
	<#else>
		<#local displayName2 = displayName>
	</#if>
	<#local nullable = (columnInfo[id].nullable?string("true", "false"))! />
	<label class="<#if nullable?default("true") == "false">ace-icon red fa fa-circle </#if>col-md-3 control-label no-padding-right" for="${id!}"> ${displayName2!} </label>
	<div class="col-md-9">
		<div>
			<select id="${id!}" nullable="${nullable!}"  data-placeholder="请选择" <#if readonly == true>disabled</#if> class="form-control col-md-10 col-sm-10 col-xs-10 ${selectClass!}"  ${selectInner!}>		
				<#if vsql == "">
				${mcodeSetting.getVsqlSelect((columnInfo[id].vsql)!"", value?string, "1", obj)}
				<#else>
				${mcodeSetting.getVsqlSelect(vsql, value?string, "1", obj)}
				</#if>
			</select>
		</div>
	</div>
</div>
</#macro>

<#macro radioDiv columnInfo id="" displayName="" value=""  readonly=false mcodeId=""  divInner="" selectInner="" divClass="" selectClass="" cols=2 >
<#local nullable = (columnInfo[id].nullable?string("true", "false"))! />
<div class="form-group ${divClass!}" id="form-group-${id!}" ${divInner}>
	<label class="<#if nullable?default("true") == "false">ace-icon red fa fa-circle </#if>col-md-3 control-label no-padding-right" for="${id!}"> <#if displayName == "">${(columnInfo[id].displayName)!""}<#else>${displayName!}</#if> </label>
	<div class="col-md-9">
		<div>
			<div class="radio">
				<#if mcodeId == "">
				${mcodeSetting.getMcodeRadio((columnInfo[id].mcodeId)!"", value?string, id)}
				<#else>
				${mcodeSetting.getMcodeRadio(mcodeId, value?string, id)}
				</#if>
			</div>
		</div>
	</div>
</div>
<script>
<#if readonly == true>
$("[name='${id!}']").attr("disabled", "disabled");
</#if>
</script>
</#macro>

<#macro radioDiv2 id="" nullable=false displayName="" value=""  readonly=false mcodeId=""  divInner="" selectInner="" divClass="" selectClass="" colspan=1 >
	${mcodeSetting.getMcodeRadio(mcodeId, value?string, id)}
<script>
<#if readonly == true>
$("[name='${id!}']").attr("disabled", "disabled");
</#if>
</script>
</#macro>

<#macro checkboxDiv columnInfo id="" displayName="" value=""  readonly=false mcodeId=""  divInner="" selectInner="" divClass="" selectClass="" cols=2 >
<#local nullable = (columnInfo[id].nullable?string("true", "false"))! />
<div class="form-group ${divClass!}" id="form-group-${id!}" ${divInner}>
	<label class="<#if nullable?default("true") == "false">ace-icon red fa fa-circle </#if>col-md-3 control-label no-padding-right" for="${id!}"> <#if displayName == "">${(columnInfo[id].displayName)!""}<#else>${displayName!}</#if> </label>
	<div class="col-md-9">
		<div>
		<div class="checkbox">
			<#if mcodeId == "">
			${mcodeSetting.getMcodeCheckbox((columnInfo[id].mcodeId)!"", value?string, id)}
			<#else>
			${mcodeSetting.getMcodeCheckbox(mcodeId, value?string, id)}
			</#if>
		</div>
		</div>
	</div>
</div>
<script>
<#if readonly == true>
$("[name='${id!}']").attr("disabled", "disabled");
</#if>
</script>
</#macro>

<#macro initEntityColumn obj fields columnInfo cols=2 exFields=[]>
	<#local size = 0 />
	<#list fields as field>
		<#if !exFields?seq_contains(field)>
		<#local hide = false>
		<#local disabled = false>
		<#if columnInfo[field]?exists>
			<#local hide = columnInfo[field].hide!false>
			<#local disabled = columnInfo[field].disabled!false>
		</#if>
		<#if hide == false && disabled == false>
			<#local size = size + 1 />
		</#if>
		</#if>
	</#list>
	<#local aveSize = (size / cols)?floor />
	<#local aveMod = size % cols />
	<#local colIndex = 0 />
	<#local count = 0>
	<#local theEnd = false>
	<#list fields as field>
		<#if !exFields?seq_contains(field)>
		<#local vtype = "">
		<#local mcodeId = "">
		<#local vsql = "">
		<#local hide = false>
		<#local disabled = false>
		<#local multiSelect = false>
		<#local readonly = false>
		<#local vselect = [] >
		<#if columnInfo[field]?exists>
			<#local vtype = (columnInfo[field].vtype)!"string" />
			<#local mcodeId = columnInfo[field].mcodeId!"" />
			<#local vsql = columnInfo[field].vsql!"" />
			<#local vselect = columnInfo[field].vselect![] />
			<#local hide = columnInfo[field].hide!false>
			<#local multiSelect = columnInfo[field].multiSelect!false>
			<#local disabled = columnInfo[field].disabled!false>
			<#local readonly = columnInfo[field].readonly!false>
		</#if>
		<#if vtype == "">
			<#local vtype = "string">
		</#if>
		<#if hide == true>
			<input type="hidden" id="${field}" value="${obj[field]!}">	
		<#elseif disabled == false>
			<#if count == 0>
				<div class="form-horizontal col-lg-${12/cols?floor} col-sm-${12/cols?floor} col-xs-12 col-md-${12/cols?floor}" role="form">
			</#if>
			<#if aveMod gt colIndex>
				<#local fix = 1>
			<#else>
				<#local fix = 0>
			</#if>
			<#if vtype == "date">
				<@dateDiv columnInfo=columnInfo id=field  value=obj[field]! readonly=readonly/>
			<#elseif vtype == "select" && vselect?size gt 0>
				<@selectVselectDiv columnInfo=columnInfo id=field  value=obj[field]! vselect=vselect readonly=readonly multiSelect=multiSelect/>
			<#elseif vtype == "select" && mcodeId != "">
				<@selectDiv columnInfo=columnInfo id=field  value=obj[field]! mcodeId=mcodeId readonly=readonly multiSelect=multiSelect/>
			<#elseif vtype == "select" && vsql != "">
				<@selectVsqlDiv columnInfo=columnInfo id=field  value=obj[field]! vsql=vsql obj=obj readonly=readonly multiSelect=multiSelect/>
			<#elseif vtype == "radio" && mcodeId != "">
				<@radioDiv columnInfo=columnInfo id=field  value=obj[field]! mcodeId=mcodeId readonly=readonly/>
			<#elseif vtype == "checkbox" && mcodeId != "">
				<@checkboxDiv columnInfo=columnInfo id=field  value=obj[field]! mcodeId=mcodeId readonly=readonly/>
			<#else>
				<@inputDiv columnInfo=columnInfo id=field  value=obj[field]! readonly=readonly/>
			</#if>
			<#local theEnd = false>
			<#if (count + 1) gte (aveSize + fix)>
				</div>		
				<#local theEnd = true>
				<#local count = 0>
				<#local colIndex = colIndex + 1>
			<#else>
				<#local count = count + 1>
			</#if>
		</#if>
		</#if>
	</#list>
	<#if theEnd == false>
		</div>
	</#if>
</#macro>

<#macro arrowRight>
<i class="fa fa-long-arrow-right blue" aria-hidden="true"></i>
</#macro>

<#macro pageRef url name>
<#if url?index_of("http://") == 0>
<a href="${url!}" target="_blank">
	${name!}
	<i class="fa bigger-110"></i>
</a>
<#else>
<a href="javascript:" onclick="javascript:gotoHash('${url}');">
	${name!}
	<i class="fa bigger-110"></i>
</a>
</#if>

</#macro>

<#macro btn btnValue btnId="" btnClass="" exClass="" title="">
<a href="javascript:" id="${btnId!}" class="btn btn-primary ${exClass!}" title="${title!}">
<i class="ace-icon fa ${btnClass!} align-top bigger-125"></i>
${btnValue!}
</a>
<#--
输入框中回车时有点问题
<button id="${btnId!}" class="btn btn-primary ${exClass!}" title="${title!}">
	<i class="ace-icon fa ${btnClass!} align-top bigger-125"></i>
	${btnValue!}
</button>
-->
</#macro>

<#macro btnEdit value="" id="" otherText="" class="btn-edit" title="修改" permission="">
<#if permissions?exists && permission != "">
	<#if permissions?seq_contains(permission)>
		<a href="javascript:void();" otherText="${otherText!}" value="${value!}" id="btn_edit_${id!}" class="green bigger-140 ${class!}" title="${title!}">
			<i class="ace-icon fa fa-pencil"></i>
		</a>
	</#if>
<#else>
	<a href="javascript:void();" otherText="${otherText!}" value="${value!}" id="btn_edit_${id!}" class="green bigger-140 ${class!}" title="${title!}">
		<i class="ace-icon fa fa-pencil"></i>
	</a>
</#if>
</#macro>

<#macro btnDelete value="" id="" otherText="" class="btn-delete" title="删除" permission="">
<#if permissions?exists && permission != "">
	<#if permissions?seq_contains(permission)>
		<a href="javascript:void();" otherText="${otherText!}" value="${value!}" id="btn_edit_${id!}" class="red bigger-140 ${class!}" title="${title!}">
			<i class="ace-icon fa fa-trash-o"></i>
		</a>
	</#if>
<#else>
	<a href="javascript:void();" otherText="${otherText!}" value="${value!}" id="btn_edit_${id!}" class="red bigger-140 ${class!}" title="${title!}">
		<i class="ace-icon fa fa-trash-o"></i>
	</a>
</#if>
</#macro>

<#macro btnOther value="" id="" class="btn-other" title="删除" permission="">
<#if permissions?exists && permission != "">
	<#if permissions?seq_contains(permission)>
		<a href="javascript:void();" value="${value!}" id="btn_edit_${id!}" class="red bigger-140 ${class!}" title="${title!}">
			<i class="ace-icon fa fa-trash-o"></i>
		</a>
	</#if>
<#else>
	<a href="javascript:void();" value="${value!}" id="btn_edit_${id!}" class="green bigger-140 ${class!}" title="${title!}">
		<i class="ace-icon fa fa-trash-o"></i>
	</a>
</#if>
</#macro>

<#macro cbx id="">
<label class="pos-rel">
	<input type="checkbox" id="${id!}" class="ace" />
	<span class="lbl"></span>
</label>
</#macro>


<#macro pagination container pagination page_index callback="">
<div class="table-footer clearfix">
  	<ul class="pagination">
	    <li <#if pagination.pageIndex == 1>class="disabled"</#if>>
	    	<a href="javascript:;" <#if pagination.pageIndex != 1>onclick="javascript:gotoPage${page_index?default(0)}('${pagination.pageIndex - 1}');"</#if> aria-label="Previous">
	    		<span aria-hidden="true">&laquo;</span>
	    	</a>
	    </li>
	    <#if pagination.maxPageIndex != 0>
	       <#if pagination.maxPageIndex lt 8>
	    
	    	<#list 1..pagination.maxPageIndex as index>
	
		    	<li <#if index == pagination.pageIndex>class="active"</#if>><a href="javascript:;" onclick="javascript:gotoPage${page_index?default(0)}('${index}');">${index}</a></li>
		    </#list>
		   <#else>  
		      <#if  pagination.maxPageIndex == pagination.pageIndex>
		       <li><a href="javascript:;" onclick="javascript:gotoPage${page_index?default(0)}('1');">1</a></li>   
		       <li><a>...</a></li>
		       <li><a href="javascript:;" onclick="javascript:gotoPage${page_index?default(0)}('${pagination.pageIndex}');">${pagination.pageIndex}</a></li>		       
		      <#elseif pagination.maxPageIndex/2 lt pagination.pageIndex>
		       <li ><a href="javascript:;" onclick="javascript:gotoPage${page_index?default(0)}('1');">1</a></li>   
		      <li><a>...</a></li>
		       <li><a href="javascript:;" onclick="javascript:gotoPage${page_index?default(0)}('${pagination.pageIndex}');">${pagination.pageIndex}</a></li>
		       <li><a>...</a></li>
		       <li ><a href="javascript:;" onclick="javascript:gotoPage${page_index?default(0)}('${pagination.maxPageIndex}');">${pagination.maxPageIndex}</a></li>   
		      <#else>
		      <li><a href="javascript:;" onclick="javascript:gotoPage${page_index?default(0)}('${pagination.pageIndex}');">${pagination.pageIndex}</a></li>
		      <li><a>...</a></li>
		      <li ><a href="javascript:;" onclick="javascript:gotoPage${page_index?default(0)}('${pagination.maxPageIndex}');">${pagination.maxPageIndex}</a></li>   
		      </#if>		      
		   </#if>
	    <#else>
	    	<li <#if 1 == pagination.pageIndex>class="active"</#if>><a href="javascript:;" onclick="javascript:gotoPage${page_index?default(0)}('1');">1</a></li>
	    </#if>
	    <li <#if pagination.pageIndex == pagination.maxPageIndex>class="disabled"</#if>>
	    	<a href="javascript:;" <#if pagination.pageIndex != pagination.maxPageIndex>onclick="javascript:gotoPage${page_index?default(0)}('${pagination.pageIndex + 1}');"</#if> aria-label="Next">
	    		<span aria-hidden="true">&raquo;</span>
	    	</a>
	    </li>
  	</ul>
</div>
<script>
var param${page_index?default(0)} = "";
<#list pagination.params?keys as m1>
<#if m1 != "_pageIndex" && m1 != "_pageSize">
if(param${page_index?default(0)} == ""){
	param${page_index?default(0)} = "${m1}=${pagination.params[m1]}";
}
else{
	param${page_index?default(0)} = param${page_index?default(0)} + "&${m1}=${pagination.params[m1]}";
}
</#if>
</#list>
function gotoPage${page_index?default(0)}(index){
	var v = "_pageSize=${pagination.pageSize?default(20)}&_pageIndex=" + index;
	if(param${page_index?default(0)} == ""){
		param${page_index?default(0)} = v 
	}
	else{
		param${page_index?default(0)} = param${page_index?default(0)} + "&" + v;
	}
	<#if callback!=''>
	$("${container!}").load(encodeURI("${pagination.uri}?" + param${page_index?default(0)},"UTF-8"),function(){
		${callback!}();
		console.log("page");
	});
	<#else>
	$("${container!}").load(encodeURI("${pagination.uri}?" + param${page_index?default(0)},"UTF-8"));
	</#if>
}
</script>
</#macro>

<#macro pagination2 container pagination page_index callback="">
<nav class="nav-page clearfix">
    <ul class="pagination pull-right">
        <!-- 前一页 -->
        <li <#if pagination.pageIndex == 1>class="page-prev disabled"<#else>class="page-prev"</#if>>
            <a href="javascript:;" <#if pagination.pageIndex != 1>onclick="javascript:gotoPage${page_index?default(0)}('${pagination.pageIndex - 1}');"</#if>>&lt;</a>
        </li>
        <#if pagination.maxPageIndex != 0>
           <#if pagination.maxPageIndex lt 10>
              <#list 1..pagination.maxPageIndex as index>
                  <li <#if index == pagination.pageIndex>class="page-item active"<#else>class="page-item"</#if>><a href="javascript:;" onclick="javascript:gotoPage${page_index?default(0)}('${index}');">${index}</a></li>
              </#list>
           <#else>  
              <#if pagination.pageIndex lt 5>
                <#list 1..5 as index>
                  <li <#if index == pagination.pageIndex>class="page-item active"<#else>class="page-item"</#if>><a href="javascript:;" onclick="javascript:gotoPage${page_index?default(0)}('${index}');">${index}</a></li>
                </#list>
                <li>...</li>
                <li class="page-item"><a href="javascript:;" onclick="javascript:gotoPage${page_index?default(0)}('${pagination.maxPageIndex}');">${pagination.maxPageIndex}</a></li>
              <#elseif pagination.pageIndex gt (pagination.maxPageIndex-4)>
                <li class="page-item"><a href="javascript:;" onclick="javascript:gotoPage${page_index?default(0)}('1');">1</a></li>
                <li>...</li>     
                <#list pagination.maxPageIndex-4..pagination.maxPageIndex as index>
                    <li <#if index == pagination.pageIndex>class="page-item active"<#else>class="page-item"</#if>><a href="javascript:;" onclick="javascript:gotoPage${page_index?default(0)}('${index}');">${index}</a></li>
                </#list>
              <#else>
                <li class="page-item"><a href="javascript:;" onclick="javascript:gotoPage${page_index?default(0)}('1');">1</a></li>
                <li>...</li>
                    <#list pagination.pageIndex-2..pagination.pageIndex+2 as index>
                        <li <#if index == pagination.pageIndex>class="page-item active"<#else>class="page-item"</#if>><a href="javascript:;" onclick="javascript:gotoPage${page_index?default(0)}('${index}');">${index}</a></li>
                    </#list>
                <li>...</li>
                <li class="page-item"><a href="javascript:;" onclick="javascript:gotoPage${page_index?default(0)}('${pagination.maxPageIndex}');">${pagination.maxPageIndex}</a></li>   
              </#if>
           </#if>
        <#else>
            <li <#if 1 == pagination.pageIndex>class="page-item active"<#else>class="page-item"</#if>><a href="javascript:;" onclick="javascript:gotoPage${page_index?default(0)}('1');">1</a></li>
        </#if>
        
        <!-- 后一页 -->
        <li <#if pagination.pageIndex == pagination.maxPageIndex || pagination.maxPageIndex==0>class="page-next disabled"<#else>class="page-next"</#if>>
            <a href="javascript:;" <#if pagination.pageIndex != pagination.maxPageIndex>onclick="javascript:gotoPage${page_index?default(0)}('${pagination.pageIndex + 1}');"</#if>>
                &gt;
            </a>
        </li>
        <#if pagination.maxPageIndex != 0>
        <li class="pagination-other">跳到：
            <input type="text" class="form-control" id="pageInput">
            <a  class="btn btn-white" onclick="javascript:jumpPage();">确定</a>
        </li>
        <li class="pagination-other">共${pagination.maxRowCount}条</li>
        <li class="pagination-other">
                                         每页
            <input type="text" class="form-control" value="${pagination.pageSize }" id="pageSize" onkeyup="if(event.keyCode==13){pageSizeChange();}">
                                         条
        </li>
        </#if>
    </ul>
</nav>
<script>
var param${page_index?default(0)} = "";
//参数拼接
<#list pagination.params?keys as m1>
    <#if m1 != "_pageIndex" && m1 != "_pageSize">
    if(param${page_index?default(0)} == ""){
        param${page_index?default(0)} = "${m1}=${pagination.params[m1]}";
    }
    else{
        param${page_index?default(0)} = param${page_index?default(0)} + "&${m1}=${pagination.params[m1]}";
    }
    </#if>
</#list>

function gotoPage${page_index?default(0)}(index, size){
    var pageSize="_pageSize=${pagination.pageSize?default(20)}";
    if(size){
      pageSize = "_pageSize="+size;
    }
    var v = pageSize + "&_pageIndex=" + index;
    if(param${page_index?default(0)} == ""){
        param${page_index?default(0)} = v 
    }
    else{
        param${page_index?default(0)} = param${page_index?default(0)} + "&" + v;
    }
    <#if callback!=''>
    $("${container!}").load(encodeURI("${pagination.uri}?" + param${page_index?default(0)},"UTF-8"),function(){
        ${callback!}();
        console.log("page");
    });
    <#else>
    $("${container!}").load(encodeURI("${pagination.uri}?" + param${page_index?default(0)},"UTF-8"));
    </#if>
}

//页面跳转
function jumpPage(){
  var pageNum = $("#pageInput").val();
  if(!$.isNumeric(pageNum)){
    showMsgError("请填写正确数值","错误信息",function(index){
      $("#pageInput").val("");
      layer.close(index);
    });
    return;
  }
  pageNum = parseInt(pageNum);
  if(!(pageNum>=1 && pageNum<=${pagination.maxPageIndex})){
    showMsgError("请填写正确数值","错误信息",function(index){
      $("#pageInput").val("");
      layer.close(index);
    });
    return;
  }
  gotoPage${page_index?default(0)}(pageNum);
}

function pageSizeChange(){
    var size = $("#pageSize").val();
    if(!$.isNumeric(size)){
      showMsgError("请填写正确数值","错误信息",function(index){
        $("#size").val("");
        layer.close(index);
      });
      return;
    }
    size = parseInt(size);
    if(size<1){
      showMsgError("请填写正确数值","错误信息",function(index){
        $("#size").val("");
        layer.close(index);
      });
      return;
    }
    gotoPage${page_index?default(0)}(1, size);
}
</script>
</#macro>




<#macro region regionCode regionName>
<div class="multi-select" style="width:250px;">
    <div class="multi-select-text">
        <input type="text" class="form-control"  value="${regionName! }" id="regionName"/>
        <i class="fa fa-caret-down"></i>
    </div>
</div>
<input type="hidden" id="regionCode" value="${regionCode! }"/>
<script>
$('.multi-select-text').click(function(){
  var $multi=$(this).parent('.multi-select');
  $('.multi-select-layer').remove();
  $multi.append('<div class="multi-select-layer"><ul class="multi-select-tab clearfix"><li class="current">选择省份</li><li>选择城市</li><li>选择区县</li></ul><div id="province" class="content-list"></div><ul id="city" class="content-list clearfix"></ul><ul id="district" class="content-list clearfix"></ul></div>');
  
  //初始化
  $.ajax({
    url:"${request.contextPath}/region/proviceList",
    dataType:'json',
    success:function(data){
      $("#province").append(data.msg);
      $multi.children('.multi-select-layer').height($multi.children('.multi-select-layer').height());
    }
  });
  
  
  //省点击事件
  $('#province').on('click','dl dd span',function(){
    var province_txt=$(this).text();
    var regionCode = $(this).data("region-code");
    var fullCode = $(this).data("full-code")
    $("#regionCode").val(regionCode);
    $('#province dd span').removeClass('current');
    $(this).addClass('current');
    var textVal = province_txt;
    //textVal = textVal.substring(0,16);
    $multi.find('.form-control').val(textVal).attr('data-province',province_txt);
    
    //获取市列表
    $('.multi-select-tab li:eq(1)').addClass('current').siblings('li').removeClass('current');
    $('.multi-select-tab li:eq(0)').addClass('can');
    $('#province').hide().next('#city').show();
    
    $.ajax({
      url:"${request.contextPath}/region/subRegionList",
      data:{
        'fullRegionCode':fullCode
      },
      type:"post",
      dataType:"json",
      success:function(data){
        $('#city,#district').html('');//清空市、区列表
        var subRegions = JSON.parse(data.msg);
        for(var i=0;i<subRegions.length;i++){
          var regionObj = subRegions[i];
         //插入市列表
          $('#city').append(
              '<li data-region-code="'+ regionObj.regionCode +'" data-full-code="'+regionObj.fullCode+'">' + regionObj.regionName + '</li>' 
          );
        }
      }
    });
  });
  
  //市选择获取数据
  $('#city').on('click','li',function(){
      var province_txt=$multi.find('.form-control').attr('data-province');
      var city_txt=$(this).text();
      var regionCode = $(this).data("region-code");
      $("#regionCode").val(regionCode);
      var fullCode = $(this).data("full-code")
      $(this).addClass('current').siblings('li').removeClass('current');
      var textVal = province_txt+'/'+city_txt;
     // textVal = textVal.substring(0,16);
      $multi.find('.form-control').val(textVal).attr('data-city',city_txt);
      
      //获取区列表
      $('.multi-select-tab li:eq(2)').addClass('current').siblings('li').removeClass('current');
      $('.multi-select-tab li:eq(0),.multi-select-tab li:eq(1)').addClass('can');
      $('#province').hide().next('#city').hide().next('#district').show();
      
      $.ajax({
        url:"${request.contextPath}/region/subRegionList",
        data:{
          'fullRegionCode':fullCode
        },
        type:"post",
        dataType:"json",
        success:function(data){
          $('#district').html('');//清空区列表
          var subRegions = JSON.parse(data.msg);
          for(var i=0;i<subRegions.length;i++){
            var regionObj = subRegions[i];
          //插入区列表
            $('#district').append(
                '<li data-region-code="'+ regionObj.regionCode +'" data-full-code="'+regionObj.fullCode+'">' + regionObj.regionName + '</li>' 
            );
          }
        }
      });
      
  });
  
  //区选择获取数据
  $('#district').on('click','li',function(){
      var province_txt=$multi.find('.form-control').attr('data-province');
      var city_txt=$multi.find('.form-control').attr('data-city');
      var district_txt=$(this).text();
      var regionCode = $(this).data("region-code");
      $("#regionCode").val(regionCode);
      $(this).addClass('current').siblings('li').removeClass('current');
      var textVal = province_txt+'/'+city_txt+'/'+district_txt;
      //textVal = textVal.substring(0,16);
      $multi.find('.form-control').val(textVal).attr('data-district',district_txt);
      $multi.children('.multi-select-layer').remove();
  });
  
  //tab切换-回退
  $('.multi-select-tab').on('click','li.can',function(){
      $(this).addClass('current').removeClass('can').siblings('li').removeClass('current');
      $('.multi-select-tab li:gt('+$(this).index()+')').removeClass('can');
      $multi.find('.multi-select-layer .content-list:eq('+$(this).index()+')').show().siblings('.content-list').hide();
  });
  
  //点击其他区域隐藏层
  $(document).click(function(event){
      var eo=$(event.target);
      if($('.multi-select-layer').is(':visible') && !eo.parents('.multi-select').length)
      $('.multi-select-layer').remove();
  });
  
});
</script>
</#macro>


