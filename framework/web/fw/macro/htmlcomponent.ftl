<#macro moduleDiv titleName="">
	<#-- 是否显示框架，即是否要再次加载js，css等文件 -->
	<#if showFrame?default(0) == 1>
	<!doctype html>
	<html>
	<head>
	<meta charset="utf-8">
	<title>${titleName!}</title>
	<link rel="stylesheet" type="text/css" href="${request.contextPath}/static/fw/css/public.css"/>
	<link rel="stylesheet" type="text/css" href="${request.contextPath}/static/fw/css/layout.css"/>
	<link rel="stylesheet" type="text/css" href="${request.contextPath}/static/fw/css/default.css"/>
	
	<script>
	_contextPath = "${request.contextPath}";
	</script>
	<script type="text/javascript" src="${request.contextPath}/static/fw/js/jquery-1.7.2.min.js"></script>
	<script type="text/javascript" src="${request.contextPath}/static/fw/js/jquery-ui.min.js"></script>
	<script type="text/javascript" src="${request.contextPath}/static/fw/js/jquery.form.js"></script>
	<script type="text/javascript" src="${request.contextPath}/static/fw/js/jscroll.js"></script>
	<script type="text/javascript" src="${request.contextPath}/static/fw/js/myscript-chkRadio.js"></script>
	<script type="text/javascript" src="${request.contextPath}/static/fw/js/jwindow.js"></script>
	<script type="text/javascript" src="${request.contextPath}/static/fw/js/handlefielderror.js"></script>
	<script type="text/javascript" src="${request.contextPath}/static/remote/openapi/js/jquery.Layer.js"></script>
	<!--校验脚本-->
	<script type="text/javascript" src="${request.contextPath}/static/fw/js/validate.js"></script>
	<!--日期控制脚本-->
	<#-- script type="text/javascript" src="${request.contextPath}/static/fw/js/LodopFuncs.js"></script-->
	<script type="text/javascript" src="${request.contextPath}/static/fw/js/calendar/WdatePicker.js"></script>
	<script type="text/javascript" src="${request.contextPath}/static/fw/js/jquery.ba-resize.min.js"></script>
	<#-- 加载弹出框 -->
	</head>
	<body >
	<div id="container">
	</#if>
	
	<#--插入内容-->
	<#nested />
	
	<#if showFrame?default(0) == 1>
	</div>
	<!--个人设置的div-->
	<div class="ui-layer" style="display:none;width:800px;">
		<p class="tt"><a href="javascript:;" class="close">关闭</a><span class="title">数据字典</span></p>
		<div id="ui-layer-div" class="wrap pa-10" style="max-height:500px;">
		<img src="${request.contextPath}/static/fw/css/zTreeStyle/img/loading.gif">
	    </div>
	</div>
	
	<script>
	function openWindow(title, width, height, divData, url, endHandler){
		$(".ui-layer .title").text(title);
		$(".ui-layer").css({"width":width});
		$("#ui-layer-div").css({"max-height":height});
		if(divData != ""){
			$("#ui-layer-div").html(divData);
			var layer = $(".ui-layer");
			layer.css({"width":width});
	    	var layerW=parseInt(layer.width());
			var layerH=parseInt(layer.height());
			var layerL=(winW-layerW)/2;
			var layerT=(winH-layerH)/2;
			var reTop=layerT;
	    	var docH=parseInt($(document).height());
			var winTop=parseInt($(window).scrollTop());
			
			layerT=reTop+winTop;
			(docH>winH) ? maskH=docH : maskH=winH;
			$(layer).show().css({top:layerT,left:layerL});
			if($(document).find('.ui-layer-mask').length==0){
				$('body').append('<div class="ui-layer-mask" style="height:'+maskH+'px"></div>');
			};
			$(window).scroll(function(){
				var sTop=parseInt($(window).scrollTop());
				layerT=reTop+sTop;
				layer.css({top:layerT});
			});
			
			layer.find(".close").click(function(e){
				e.preventDefault();
				layer.hide();
				$('.ui-layer-mask').remove();
				$("#ui-layer-div").empty();
			});
		}
		if(url != ""){
			//先居中显示弹出框
			var layer = $(".ui-layer");
	    	var layerW=parseInt(layer.width());
			var layerH=parseInt(layer.height());
			var layerL=(winW-layerW)/2;
			var layerT=(winH-layerH)/2;
			var reTop=layerT;
	    	var docH=parseInt($(document).height());
			var winTop=parseInt($(window).scrollTop());
			layerT=reTop+winTop;
			(docH>winH) ? maskH=docH : maskH=winH;
			$(layer).show().css({top:layerT,left:layerL});
				
			$("#ui-layer-div").load(url, function(){
				var layer = $(".ui-layer");
		    	var layerW=parseInt(layer.width());
				var layerH=parseInt(layer.height());
				var layerL=(winW-layerW)/2;
				var layerT=(winH-layerH)/2;
				var reTop=layerT;
		    	var docH=parseInt($(document).height());
				var winTop=parseInt($(window).scrollTop());
				
				layerT=reTop+winTop;
				(docH>winH) ? maskH=docH : maskH=winH;
				$(layer).show().css({top:layerT,left:layerL});
				if($(document).find('.ui-layer-mask').length==0){
					$('body').append('<div class="ui-layer-mask" style="height:'+maskH+'px"></div>');
				};
				$(window).scroll(function(){
					var sTop=parseInt($(window).scrollTop());
					layerT=reTop+sTop;
					layer.css({top:layerT});
				});
				
				layer.find(".close").click(function(e){
					e.preventDefault();
					layer.hide();
					$('.ui-layer-mask').remove();
					$("#ui-layer-div").empty();
				});
				
				if (endHandler && endHandler != "") {
					if (endHandler instanceof Function) {
						eval(endHandler)();
					} else {
						eval(endHandler);
					}
				}
			});
		}
		
	}
	</script>
	</body>
	</html>
	</#if>
</#macro>

<#macro select options=[] style="width:100px;" myfunchange="" txtName="" txtId="" valName="" valId="" curVal="" showOverFlow=true notNull="false" msgName="" optionDivName="" className="" title="">
	<select>
        	<#nested />
    </select>
</#macro>

<#macro selectSimple options=[] style="width:100px;" myfunchange="" txtName="" txtId="" valName="" valId="" curVal="" showOverFlow=true notNull="false" msgName="" optionDivName="" className="">
	<select style="height:25px;${style}" value="${curVal}" class="select-input" id="${valId}" name="${valName}" <#if myfunchange != "">myfunchange="${myfunchange}"</#if>>
		<#nested />
          	<#list options as x>
            <option value="${x.id}" style="color:blue;" <#if curVal=x.id>selected</#if>>${x.name}22</option>
            </#list>
	</select> 
</#macro>

<#--
分页工具条
container 必须 例如：.class  #id
-->
<#macro pageToolBar container="" class="">
	<script>
		 var reloadDataContainer ="${container}"
	</script>
	<div class="nav-page clearfix ${class!}">
		${htmlOfPaginationLoad}
	</div>
</#macro>

<#--
打印工具按钮
uniqueId 如果页面中存在多个确定唯一性用
container 需要打印的（不需要打印的请在class中添加noprint）， 必须。例如：.class  #id
btn1 btn2 btn3 分别代表三个按钮  除图片外其他 默认都显示   false:代表不显示
printDirection false 横向
btnClass 按钮样式
printUp 上
printLeft 左
printRight 右
printBottom 下
-->
<#macro printToolBar uniqueId="" container=".print" btnClass="" btn1="true" btn2="true" btn3="false" printDirection="false" printUp=15 printLeft=20  printRight=15 printBottom=15>
	
	<#if btn1=='false'>
	<#else>
		<a href="javascript:" class="<#if btnClass?default('')!=''>${btnClass}<#else>btn btn-blue</#if>" onclick="${uniqueId}doPrintResult()" data-toggle="tooltip" data-original-title="打印页面中查询出的结果">打印</a>
	</#if>
	<#if btn2=='false'>
	<#else>
		<a href="javascript:" class="<#if btnClass?default('')!=''>${btnClass}<#else>btn btn-blue</#if>" onclick="${uniqueId}doExcelResult(1)" data-toggle="tooltip" data-original-title="导出页面中查询出的结果">导出Excel</a>
	</#if>
	<#if btn3=='false'>
	<#else>
		<a href="javascript:" class="<#if btnClass?default('')!=''>${btnClass}<#else>btn btn-blue</#if>" onclick="${uniqueId}doExcelResult(2)" data-toggle="tooltip" data-original-title="导出页面中查询出的结果">导出图片</a>
  	</#if>
  	<#--<script src="${request.contextPath}/static/js/LodopFuncs.js" /-->
	<script>
	if (!window.lodopFuncs) {
	            var script = document.createElement("script");
				script.type = "text/javascript";
				script.src = "${request.contextPath}/static/js/LodopFuncs.js";
				document.body.appendChild(script);
				window.lodopFuncs = true;
        } else {
        }
        
  		$(function(){
			$('[data-toggle="tooltip"]').tooltip({
				container: 'body',
				trigger: 'hover'
			});
		});
		function ${uniqueId}doPrintResult(){
			var LODOP=getLodop('${request.contextPath}');  //初始化打印控件，如果发现没有安装的话，会出现一个安装对话框进行下载安装
			if (LODOP==undefined || LODOP==null) {
				return;
			}
			var v=$("${container}").html();
			if(!v){
				alert("暂时没有需要打印的数据");
				return;
			}
			//设置打印内容，getPrintContent函数是LodopFuncs中增加的一个函数，参数传入jQuery对象，第二个参数表示要忽略的样式名，可以不写（主要是有些样式与打印冲突，如prt-report-wrap，这个价了后，后面的内容就不会显示）
			LODOP.ADD_PRINT_HTM("${printUp}mm","${printLeft}mm","RightMargin:${printRight}mm","BottomMargin:${printBottom}mm",getPrintContent($("${container}")));
			<#if printDirection=='true'>
				LODOP.SET_PRINT_PAGESIZE(1,0,0,"");//纵向打印
			<#else>
				LODOP.SET_PRINT_PAGESIZE(2,0,0,"");//横向打印
			</#if>
			LODOP.SET_SHOW_MODE("LANDSCAPE_DEFROTATED",1);//横向时的正向显示
			LODOP.PREVIEW();//打印预览
		}
		function ${uniqueId}doExcelResult(doType){
			var LODOP=getLodop('${request.contextPath}');  //初始化打印控件，如果发现没有安装的话，会出现一个安装对话框进行下载安装
			if (LODOP==undefined || LODOP==null) {
				return;
			 }
			
			//设置打印内容，getPrintContent函数是LodopFuncs中增加的一个函数，参数传入jQuery对象，第二个参数表示要忽略的样式名，可以不写（主要是有些样式与打印冲突，如prt-report-wrap，这个价了后，后面的内容就不会显示）
			if(doType == 1){
				LODOP.ADD_PRINT_TABLE("20mm","15mm","RightMargin:15mm","BottomMargin:15mm",getPrintContent($("${container}")));
				LODOP.SAVE_TO_FILE(getNowFormatDate()+".xls");
			}else{
				LODOP.ADD_PRINT_HTM("20mm","15mm","RightMargin:15mm","BottomMargin:15mm",getPrintContent($("${container}")));
				LODOP.SET_PRINT_PAGESIZE(2,0,0,"");//横向打印
				LODOP.SET_SHOW_MODE("LANDSCAPE_DEFROTATED",1);//横向时的正向显示
				LODOP.SAVE_TO_FILE(getNowFormatDate()+".jpg");
			}
		}
		function getNowFormatDate() {
		    var date = new Date();
		    var seperator1 = "-";
		    var seperator2 = "-";
		    var month = date.getMonth() + 1;
		    var strDate = date.getDate();
		    if (month >= 1 && month <= 9) {
		        month = "0" + month;
		    }
		    if (strDate >= 0 && strDate <= 9) {
		        strDate = "0" + strDate;
		    }
		    var currentdate = date.getFullYear() + seperator1 + month + seperator1 + strDate
		            + "," + date.getHours() + seperator2 + date.getMinutes()
		            + seperator2 + date.getSeconds();
		    return currentdate;
		}
  	</script>
</#macro>

<#--3
*  宏名：datepicker
*  功能：日期选取控件
-->
<#macro datepicker name="" id="" msgName="" value="" class="input-txt input-date fn-left input-sm form-control" readonly="" style="width:140px;" size="22" notNull="false" dateFmt="yyyy-MM-dd" onpicked="clickNoMethod" maxlength="10" placeholder="" oncleared="clickNoMethod" minDate="1900-01-01 00:00:00" maxDate="2099-12-31 23:59:59">
<#if name != "" && id == "">
<#local id=name />
<#elseif id != "" && name == "">
<#local name = id /> 
</#if>
<input name="${name}" msgName="${msgName}" dataType="date" id="${id}" <#if placeholder != ''>placeholder="${placeholder}"</#if> <#if style != "">style="${style}"</#if> type="text" class="${class}  input-readonly" readonly="readonly" size="${size}" maxlength="${maxlength}" nullable="<#if notNull?default('false')='true'>false<#else>true</#if>" value="${value}" <#if readonly?default("") == "" || readonly?default("") == "false">onclick="WdatePicker({minDate:'${minDate}',maxDate:'${maxDate}',dateFmt:'${dateFmt}',onpicked:${onpicked},oncleared:${oncleared}});"</#if>>
<#if readonly?default("") == "">
<a href="javascript:void(0)" onClick="$('#${id}').click();" hidefocus>
<img name="popcal${name}" style="display:none;" id="popcal${id}" class="img-date hidden-${id}" align="absmiddle" alt="选择日期"></a>
</#if>
<script>
function clickNoMethod(){
	return;
}
</script>
</#macro>

<#macro cutOff str='' length=0 omit='…'>
<#if str?exists>
	<#if (str?length>length)>
${str?substring(0,length)}${omit}
	<#else>
${str}
	</#if>
<#else>
</#if>
</#macro>


<#-- 专门用于列表显示字段的长度截取 -->
<#macro cutOff4List str='' length=0>
<#if str?exists>
	<span title='${str}'>
		<#if (str?length>length)>
${str?substring(0,length)}…
		<#else>
${str}
		</#if>
	</span>
<#else>		
</#if>
</#macro>

<#macro cutOff4tableList str='' length=0>
<#if str?exists>
	<span title='${str}'>
		<#if (str?length>length)>
${str?substring(0,length-2)}…
		<#else>
${str}
		</#if>
	</span>
<#else>		
</#if>
</#macro>

<#macro showMsg>
<#--一般提示-->
<div class="popUp-layer popUp-layer-tips" id="panelWindow_success" style="display:none;">
	<p class="tt"><a href="javascript:void(0);" class="close">关闭</a><span id="panelWindow_success_title">提示</span></p>
    <div class="wrap">
        <p class="content"><span class="success" id="panelWindow_success_msg">保存成功！</span></p>
        <p class="t-center pb-20">
            <a class="abtn-blue submit" href="javascript:void(0);">确定</a>
        </p>
    </div>
</div>

<#--错误提示-->
<div class="popUp-layer popUp-layer-tips" id="panelWindow_error" style="display:none;">
	<p class="tt"><a href="javascript:void(0)" class="close">关闭</a><span id="panelWindow_error_title">提示</span></p>
    <div class="wrap">
        <p class="content"><span class="error" id="panelWindow_error_msg">错误</span></p>
        <p class="t-center pb-20">
            <a class="abtn-blue submit" href="javascript:void(0);">确定</a>
        </p>
    </div>
</div>

<#--警告提示-->
<div class="popUp-layer popUp-layer-tips" id="panelWindow_warning" style="display:none;">
	<p class="tt"><a href="javascript:void(0)" class="close">关闭</a><span id="panelWindow_warning_title">提示</span></p>
    <div class="wrap">
        <p class="content"><span class="warn" id="panelWindow_warning_msg">警告</span></p>
        <p class="t-center pb-20">
            <a class="abtn-blue submit" href="javascript:void(0);">确定</a>
        </p>
    </div>
</div>

<div class="popUp-layer popUp-layer-tips" id="panelWindow_tip" style="display:none;">
	<p class="tt"><span id="panelWindow_tip_title">提示</span></p>
    <div class="wrap">
        <p class="content"><span class="tip" id="panelWindow_tip_msg">加载中</span></p>
    </div>
    <p class="t-center pb-20">
    </p>
</div>
<div class="popUp-layer" id="_panel-pulic-window" style="display:none;"></div>
</#macro>

<!--最外层的DIV必须含有class样式promt-div 并且没有DIV 必须要要有class样式fn-rel-->
<#macro showPrompt>
<div class="prompt" id="prompt" style="display:none;"></div>
<script>
function showPrompt(objectId,msg){
	if($('#'+objectId).parent('div').hasClass('promt-div')){
		$("#prompt").css("left",$('#'+objectId).position().left);
	}else{
		var width=$('#'+objectId).position().left;
		var parentDiv=$('#'+objectId).parent('div');
		while(!parentDiv.hasClass('promt-div')){
			width+=parentDiv.position().left;
			parentDiv=parentDiv.parent('div');
		}
		$("#prompt").css("left",width);
	}
	$("#prompt").html(msg+"<a href='javascript:void(0);' onclick=\"$('#prompt').html('');$('#prompt').hide();\">&times;</a><span></span>");
	$("#prompt").show();
	setTimeout('$("#prompt").hide()',5000); //5秒钟后自动关闭
}

$(document).click(function(event){
	var eo=$(event.target);
	if($("#prompt").is(":visible") && !eo.hasClass('promptClick')){
		$('#prompt').html('');
		$('#prompt').hide(); 
	} 
});
</script>
</#macro>

<#--
*  列表形式样式表格
-->
<#macro tableList name="" id="" class="public-table table-list" addClass="" style="">
	<#if name != "" && id == "">
	<#local id=name />
	<#elseif id != "" && name == "">
	<#local name = id />
	</#if>
	<table id="${id}" name="${name}" border="0" cellspacing="0" cellpadding="0" <#if addClass != "">class="${class} ${addClass}"<#else>class="${class}"</#if> style="${style}">
	<#nested>
	</table>
</#macro>

<#--详细信息样式表格-->
<#macro tableDetail id="tabledetail" name="" class="table-form" divClass="" divId="" needScroll="false" isIframe="false">
	<#if name != "" && id == "">
	<#local id=name />
	<#elseif id != "" && name == "">
	<#local name = id />
	</#if>
	<table id="${id}" name="${name}" width="100%" border="0" cellspacing="0" cellpadding="0" class="${class}">
	<#nested>
	</table>
</#macro>

<#--详细信息样式表格-->
<#macro treeTableDetail id="treeTableDetail" name="" class="table3" divClass="" divId="" needScroll="false">
	<#if name != "" && id == "">
	<#local id=name />
	<#elseif id != "" && name == "">
	<#local name = id />
	</#if>
	<div <#if needScroll=="true">style="height:37px;"</#if> class="<#if divClass!=""> ${divClass}</#if>" <#if divId != "" >id="${divId}"</#if>>
		<table id="${id}" name="${name}" width="100%" border="0" cellspacing="0" cellpadding="0" class="${class}">
		<#nested>
		</table>
	</div>
</#macro>

<#macro tableDetailTitle content="" divClass="">
<@tableDetail divClass="${divClass}" needScroll="false">
<tr class="first"><th class="tt">${content}</th></tr>
</@tableDetail>
</#macro>

<#--table内 底部的按钮，保存和取消-->
<#macro tableBottom saveScript="doSave" closeScript="doClose" saveName="保存" closeName="取消" colspan=1>
	<tr>
        	<td colspan="${colspan?default(1)}" class="td-opt" align="center">
            	<a id="saveBtn" href="javascript:void(0);" onclick="javascript:${saveScript}();" class="abtn-blue">${saveName}</a>
                <a id="closeBtn" href="javascript:void(0);" onclick="javascript:${closeScript}();" class="abtn-blue ml-10">${closeName}</a>
            </td>
    </tr>
</#macro>
<#macro tableBottom2>
	<div class="table1-bt t-center">
	 <!--[if lte IE 6]>
    <div style="position:absolute;z-index:-1;width:100%;height:100%;">  
        <iframe style="width:100%;height:100%;border:0;filter:alpha(opacity=0);-moz-opacity:0"></iframe>  
    </div> 
    <![endif]-->
    <#nested>
	</div>
</#macro>

<#--
onChange(val,txt);
-->
<#macro tableDetailSelect msgName name="" id="" class="ui-select-box fn-left" notNull="false" colspan="" width="" style="width:150px;" onChange="" readonly="" value="">
<select>
	        <#nested>
	</select>
	<#if notNull?default("")?lower_case == "yes" || notNull?default("")?lower_case == "true">
		<span class="fn-left c-orange mt-5 ml-10">*</span>
	</#if>
	</td>
</#macro>
<#--别名-->
<#macro tds msgName name="" id="" class="ui-select-box fn-left" notNull="false" width="" style="width:150px;" onChange="" readonly="" value="" colspan="">
	<#if name != "" && id == "">
	<#local id=name />
	<#elseif id != "" && name == "">
	<#local name = id />
	</#if>	
	<@tableDetailSelect msgName="${msgName}" name="${name}" id="${id}" class="${class}" colspan="${colspan}" notNull="${notNull}" value="${value}" readonly="${readonly}" width="${width}" style="${style}" onChange="${onChange}" >
	<#nested>
	</@tableDetailSelect>
</#macro>

<#macro tableDetailTextarea msgName name="" id="" class="text-area" onChange="" notNull="false" maxLength="0" width="" readonly="false" colspan="" style="width:140px;" value="">
	<#if name != "" && id == "">
	<#local id=name />
	<#elseif id != "" && name == "">
	<#local name = id />
	</#if>
	<#local wf = "">
	<#local ws = "">
	<#list width?split(",") as w>
		<#if w_index == 0>
			<#local wf = w>
		<#else>
			<#local ws = w>
		</#if>
	</#list>
	<#if wf != "">
		<th width="${wf}">
	<#else>
		<th>
	</#if>
	${msgName}：</th>
	<#if ws != "">
		<td width="${ws}" <#if colspan != "">colspan="${colspan}"</#if> >
	<#else>
		<td <#if colspan != "">colspan="${colspan}"</#if> >
	</#if>
	<textarea type="text" onChange="${onChange}" class="${class}<#if readonly=="true"> input-readonly</#if>  mt-5 mb-5" <#if style != "">style="${style}"</#if> id="${id}" name="${name}" <#if readonly=="true">readonly="true"</#if> msgName="${msgName}" maxLength="${maxLength}" notNull="${notNull}">${value}</textarea>
	<#if notNull?default("")?lower_case == "yes" || notNull?default("")?lower_case == "true">
		<span class="fn-left c-orange mt-5 ml-10">*</span>
	</#if>
	</td>
</#macro>
<#--别名-->
<#macro tdt msgName name="" id="" class="text-area" notNull="false" onChange="" maxLength="0" width="" readonly="false" colspan="" style="width:140px;" value="">
	<#if name != "" && id == "">
	<#local id=name />
	<#elseif id != "" && name == "">
	<#local name = id />
	</#if>
	<@tableDetailTextarea msgName="${msgName}" name="${name}" id="${id}" onChange="${onChange}" class="${class}" notNull="${notNull}" 
		maxLength="${maxLength}" width="${width}" readonly="${readonly}" colspan="${colspan}" style="${style}" value="${value}" />
</#macro>

<#macro tableDetailDate msgName width="" name="" id="" notNull="false" value="" readonly="" style="width:140px;">
	<#if name != "" && id == "">
	<#local id=name />
	<#elseif id != "" && name == "">
	<#local name = id />
	</#if>
	<#local wf = "">
	<#local ws = "">
	<#list width?split(",") as w>
		<#if w_index == 0>
			<#local wf = w>
		<#else>
			<#local ws = w>
		</#if>
	</#list>
	<#if wf != "">
		<th width="${wf}">
	<#else>
		<th>
	</#if>
	${msgName}：</th>
	<#if ws != "">
		<td width="${ws}">
	<#else>
		<td>
	</#if>
	<@datepicker msgName="${msgName}" notNull="${notNull}" name="${name}" id="${id}" value="${value}" readonly="${readonly}" style="${style}" />
	<#if notNull?default("")?lower_case == "yes" || notNull?default("")?lower_case == "true">
		<span class="fn-left c-orange mt-5 ml-10">*</span>
	</#if>
	</td>
</#macro>
<#--别名-->
<#macro tdd msgName width="" name="" id="" notNull="false" value="" readonly="" style="width:140px;">
	<#if name != "" && id == "">
	<#local id=name />
	<#elseif id != "" && name == "">
	<#local name = id />
	</#if>
	<@tableDetailDate msgName="${msgName}" width="${width}" name="${name}" id="${id}" style="${style}" notNull="${notNull}" value="${value}" readonly="${readonly}" />
</#macro>

<#macro tableDetailInput msgName name="" id="" dataType="" minValue="" maxValue="" decimalLength="" class="input-txt fn-left" notNull="false" maxLength="0" width="" readonly="false" colspan="" style="width:140px;" value="" regex="" regexMsg="" onClick="" onBlur="">
	<#if name != "" && id == "">
	<#local id=name />
	<#elseif id != "" && name == "">
	<#local name = id />
	</#if>
	<#local wf = "">
	<#local ws = "">
	<#list width?split(",") as w>
		<#if w_index == 0>
			<#local wf = w>
		<#else>
			<#local ws = w>
		</#if>
	</#list>
	<#if wf != "">
		<th width="${wf}">
	<#else>
		<th>
	</#if>
	${msgName}：</th>
	<#if ws != "">
		<td width="${ws}" <#if colspan != "">colspan="${colspan}"</#if>>
	<#else>
		<td <#if colspan != "">colspan="${colspan}"</#if> >
	</#if>
	<input type="text" class="${class}<#if readonly=="true"> input-readonly</#if>" <#if style != "">style="${style}"</#if> id="${id}" 
		dataType="${dataType}" minValue="${minValue}" maxValue="${maxValue}" <#if decimalLength != "">decimalLength="${decimalLength}"</#if> 
		name="${name}" msgName="${msgName}" <#if readonly=="true">readonly="true"</#if> <#if maxLength != "0"> maxLength="${maxLength}" </#if> 
		notNull="${notNull}" value="${value}" 
		regex="${regex}" regexMsg="${regexMsg}" onclick="${onClick}" onblur="${onBlur}"/>
	<#if notNull?default("")?lower_case == "yes" || notNull?default("")?lower_case == "true">
		<span class="fn-left c-orange mt-5 ml-10">*</span>
	</#if>
	</td>
</#macro>
<#--别名-->
<#macro tdi msgName name="" id="" class="input-txt fn-left" decimalLength="" onClick="" onBlur="" dataType="" minValue="" maxValue="" notNull="false" maxLength="0" width="" readonly="false" colspan="" style="width:140px;" value="" regex="" regexMsg="">
	<#if name != "" && id == "">
	<#local id=name />
	<#elseif id != "" && name == "">
	<#local name = id />
	</#if>
	<@tableDetailInput msgName="${msgName}" decimalLength="${decimalLength}" id="${id}" name="${name}" dataType="${dataType}" minValue="${minValue}" maxValue="${maxValue}" class="${class}" notNull="${notNull}" maxLength="${maxLength}" 
	width="${width}" readonly="${readonly}" colspan="${colspan}" style="${style}" value="${value}" regex="${regex}" regexMsg="${regexMsg}" onClick="${onClick}" onBlur="${onBlur}"/>
</#macro>

<#--计算高度的宏
totalHeight：总的高度
minusHeight： 需要减去的高度
trimming：微调的高度
-->
<#macro scrollHeight scrollName=".table-content" totalHeight="jQuery('.mainFrame', window.parent.document).height()" minusHeight="jQuery('.head-tt').height() + jQuery('.table1-bt').height() + jQuery('.tab-bg').height()" trimming="0">
jQuery(document).ready(function(){
	$t_c_width=jQuery("${scrollName}").width();
	$t_c_width=$t_c_width-16;
	jQuery("${scrollName}").height(${totalHeight} - (${minusHeight}) - (${trimming}))
	jQuery(".table-header").width($t_c_width);
})
</#macro>

<#--分页导航条
pageInfo 分页的各个参数,也就是分页的类PageInfo对象
function 方法名称，且至少需要两个参数 例如search(pageIndex,pageSize):pageIndex:查询的页数，pageSize：每页条数
allNum 总共数据条数
-->
<#macro pageToolBar2 pageInfo="" function="" allNum="">
	<nav class="nav-page clearfix">
		<ul class="pagination  pull-right" id="pagebar">
			<li <#if ((pageInfo.pageIndex)?default(1) > 1)> onclick="changePageIndex('${(pageInfo.pageIndex)?default(1) -1}')" <#else> class="disabled" </#if>><a href="javascript:void(0)" >&lt;</a></li>
			<#list pageInfo.showCount as countItem >
				<li <#if (pageInfo.pageIndex)?default(1)==countItem>class="active"</#if>><a href="javascript:void(0)" onclick="changePageIndex('${countItem}')">${countItem}</a></li>
			</#list>
			
			<li <#if ((pageInfo.pageIndex)?default(1) < pageInfo.pageCount)> onclick="changePageIndex('${(pageInfo.pageIndex)?default(1) +1}')"<#else> class="disabled"  </#if>><a href="javascript:void(0)" >
				&gt;</a>
			</li>
				
			&nbsp;&nbsp;<span>共${pageInfo.pageCount?default(1)}页</span>
			 <li class="pagination-other">
		    	跳到：
		    	<input type="text" class="form-control" id="pageNum">
		    	<a href="javascript:void(0)" class="btn btn-white" onclick="changePageNum()">确定</a>
		    </li>
		    <li class="pagination-other">
		    	共${allNum}条
		    </li>
		    <#if pageInfo.pageList?exists && pageInfo.pageList?size gt 0>
		    	<li class="pagination-other">
		    	每页
		    	<select name="" id="pageSize" class="form-control" onchange="changePageIndex()">
					<#list pageInfo.pageList as num>
						<option value="${num!}" <#if num == pageInfo.pageSize>selected</#if>>${num!}</option>
					</#list>
				</select>
		    	 条
		   	 </li> 
		   	<#else>
		   		<input type="hidden" class="form-control" id="pageSize" value="${pageInfo.pageSize!}">
		    </#if>
		    
		</ul>
	</nav>
	<script>
		function changePageNum(){
			var pageNum = $('#pageNum').val();
			if(pageNum && pageNum != ''){
				if(isNaN(pageNum) || pageNum < 1 || pageNum.indexOf('.') != -1){
					layer.tips('只能输入正整数!', $('#pageNum'), {
						tipsMore: true,
						tips: 3
					});
					return;
				}
				if(pageNum>${pageInfo.pageCount?default(1)}){
					layer.tips('不能输入超过最大页数：${pageInfo.pageCount?default(1)}!', $('#pageNum'), {
						tipsMore: true,
						tips: 3
					});
					return;
				}
				
			}
			changePageIndex(pageNum)
		}
		function changePageIndex(pageIndex){
			var pageSize = $('#pageSize').val(); 
			if(!pageIndex){
				pageIndex = $('#pagebar li.active a').text();
			}
			if(!pageSize){
				pageSize = ${pageInfo.pageSize!};
			}
			if(!pageSize){
				pageSize=1;
			}
			${function}(pageIndex,pageSize);
		}
		
	</script>
</#macro>