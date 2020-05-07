<#macro moduleDiv titleName="">
	<#-- 是否显示框架，即是否要再次加载js，css等文件 -->
	<#if showFrame?default(0) == 1>
	<!doctype html>
	<html>
	<head>
	<meta charset="utf-8">
	<title>${titleName!}</title>
	<link rel="stylesheet" type="text/css" href="${resourceUrl}/fw/css/public.css"/>
	<link rel="stylesheet" type="text/css" href="${resourceUrl}/fw/css/layout.css"/>
	<link rel="stylesheet" type="text/css" href="${resourceUrl}/fw/css/default.css"/>
	
	<script>
	_contextPath = "${request.contextPath}";
	</script>
	<script type="text/javascript" src="${resourceUrl}/fw/js/jquery-1.7.2.min.js"></script>
	<script type="text/javascript" src="${resourceUrl}/fw/js/jquery-ui.min.js"></script>
	<script type="text/javascript" src="${resourceUrl}/fw/js/jquery.form.js"></script>
	<script type="text/javascript" src="${resourceUrl}/fw/js/jscroll.js"></script>
	<script type="text/javascript" src="${resourceUrl}/fw/js/myscript.js"></script>
	<script type="text/javascript" src="${resourceUrl}/fw/js/myscript-chkRadio.js"></script>
	<script type="text/javascript" src="${resourceUrl}/fw/js/jwindow.js"></script>
	<script type="text/javascript" src="${resourceUrl}/fw/js/handlefielderror.js"></script>
	<!--校验脚本-->
	<script type="text/javascript" src="${resourceUrl}/fw/js/validate.js"></script>
	<!--日期控制脚本-->
	<script type="text/javascript" src="${resourceUrl}/fw/js/LodopFuncs.js"></script>
	<script type="text/javascript" src="${resourceUrl}/fw/js/calendar/WdatePicker.js"></script>
	<script type="text/javascript" src="${resourceUrl}/fw/js/jquery.ba-resize.min.js"></script>
	<#-- 加载弹出框 -->
	<@showMsg />
	</head>
	<body >
	<div id="container">
	</#if>
	
	<#--插入内容-->
	<#nested />
	
	<#if showFrame?default(0) == 1>
	</div>
	<!--个人设置的div-->
	<div class="popUp-layer specialDiv" id="setLayer" style="display:none;top:100px;left:300px;"></div>
	</body>
	</html>
	</#if>
</#macro>

<#macro select options=[] style="width:100px;" myfunchange="" txtName="" txtId="" valName="" valId="" curVal="" showOverFlow=true notNull="false" msgName="" optionDivName="" className="" title="">
	<div class="ui-select-box fn-left ${className}" style="${style}">
        <input type="text" class="ui-select-txt" readonly value="" id="${txtId}" name="${txtName}" msgName="${msgName!}" notNull="${notNull}" title="${title!}"/>
        <input type="hidden" value="${curVal}" class="ui-select-value" id="${valId}" name="${valName}"/>
        <a class="ui-select-close"></a>
        <div id="${optionDivName}" class="ui-option" <#if myfunchange != "">myfunchange="${myfunchange}"</#if>>
        	<div style="position:absolute;z-index:-1;width:101%;height:101%;margin-left:-0.5%;">  
				<iframe style="width:100%;height:100%;border:0;filter:alpha(opacity=0);-moz-opacity:0"></iframe>  
			</div>
        	<div class="a-wrap">
        	<#nested />
          	<#list options as x>
            <a val="${x.id}" <#if curVal=x.id>class="selected"</#if>><span>${x.name}</span></a>
            </#list>
            </div>
        </div>
    </div>
</#macro>

<#macro selectSimple options=[] style="width:100px;" myfunchange="" txtName="" txtId="" valName="" valId="" curVal="" showOverFlow=true notNull="false" msgName="" optionDivName="" className="">
	<select style="height:25px;${style}" value="${curVal}" class="select-input" id="${valId}" name="${valName}" <#if myfunchange != "">myfunchange="${myfunchange}"</#if>>
		<#nested />
          	<#list options as x>
            <option value="${x.id}" style="color:blue;" <#if curVal=x.id>selected</#if>>${x.name}22</option>
            </#list>
	</select> 
</#macro>

<#--extremecomponents页面table显示标签控件的通用宏定义文件，每增加一个宏，请说明功能及参数意义
1、ECtoolbar 功能：extremecomponents的分页工具条
2、ECtoolbarWithDelete 功能：extremecomponents标签组件的分页工具条，带“全选”和“删除”功能
3、datepicker 功能：日期选取控件
-->

<#--1
*  宏名：ECtoolbar
*  功能：生成extremecomponents标签组件的分页工具条，不带“全选”和“删除”功能
*  参数：data：	extremecomponents标签组件显示信息的来源列表  
*       action: extremecomponents标签组件触发的action
*       form: extremecomponents标签组件所在的form的名称，不写时extremecomponents会产生一个名称为ec的默认from
-->
<#--
*  宏名：doCheckbox
*  功能：扩展ECtoolbarWithDelete宏的"全选"功能 
*  checkboxname：extremecomponents标签组件中第一（选择）列的checkbox的name
-->
<#macro doCheckbox checkboxname>
	var name = "${checkboxname?default("")}";
	var nameArray = name.split(",");
	var chk = document.getElementById("allSelect").checked;
	for(var j=0;j<nameArray.length;j++){
		var checkbox = document.getElementsByName(nameArray[j]);
		
	    if(chk){
			for(var i=0;i<checkbox.length;i++){	
				if(checkbox[i].disabled) continue;			 
				checkbox[i].checked=true;			
			}
		}else{
			for(var i=0;i<checkbox.length;i++){	
				  checkbox[i].checked=false;			
			}
		}
	}
</#macro>

<#--
*  宏名：doDelete
*  功能：扩展ECtoolbarWithDelete宏的"删除"功能 
*  form: extremecomponents标签组件所在的form的name，不写时extremecomponents会产生一个名称为ec的默认from
*  checkboxname：extremecomponents标签组件中第一（选择）列的checkbox的name
*  deleteaction：extremecomponents标签组件中删除时触发的Action的name
*  confirmMsg: 删除时的提示信息
-->
<#macro doDelete form checkboxname deleteaction confirmMsg>
	var flag= false;
	var length = ${form}.elements.length;
	var name = "${checkboxname?default("")}";
	var nameArray = name.split(",");
	var confirmMsg = "${confirmMsg?default("")}";
	
	for(var j=0;j<nameArray.length;j++){
		var checkbox = document.getElementsByName(nameArray[j]);
	    for(var i=0;i<checkbox.length;i++){				 
			if(checkbox[i].checked==true){
				flag = true;
				break;
			}
			if(flag){
				break;
			}	
		}
	}

	if(!flag){
		showMsgWarn("没有选要删除的行，请先选择！");
		return;
	}	
	var _tempDeleteMessage = "您确认要删除信息吗？";
	if (confirmMsg != ""){
		_tempDeleteMessage = confirmMsg;
	}
	if(showConfirm(_tempDeleteMessage)){		
		${form}.action = "${deleteaction}";
		${form}.submit();
	} else {
		return;
	}
	
</#macro> 
 
<#macro ToolbarWithDelete checkboxname deleteaction="" deleteScript="" container="" form="ec" confirmMsg="" target="" callback="" tableDivClass="base-operation">
	<div class="${tableDivClass}">
	  <p class="opt">
        	<span class="ui-checkbox ui-checkbox-all" data-all="no"><input type="checkbox" class="chk" onclick='javascript:<@doCheckbox checkboxname="${checkboxname}"/>'>全选</span>
            <a href="javascript:void(0)" onclick='javascript:<@doDeleteProcessBar form="${form}" checkboxname="${checkboxname}" deleteScript="${deleteScript}" deleteaction="${deleteaction}" confirmMsg="${confirmMsg}" target="${target}" callback="${callback}"/>'>删除</a>
        <#nested>
        </p>
	  
	  <#--div class="f-left pl-20">
	    	<input type="checkbox" id="allSelect" onclick='javascript:<@doCheckbox checkboxname="${checkboxname}"/>' ><label for="allSelect">&nbsp;全选</label>
	    	<span class="input-btn2" onclick='javascript:<@doDeleteProcessBar form="${form}" checkboxname="${checkboxname}" deleteScript="${deleteScript}" deleteaction="${deleteaction}" confirmMsg="${confirmMsg}" target="${target}" callback="${callback}"/>'><button type="button">删除</button></span>
    	<#nested>				
   	 </div-->
   	 
    <#if container?default("") != "">
    	 <script>
		 var reloadDataContainer ="${container}"
		 </script>
		 ${htmlOfPaginationLoad}
   	 <#else>
     	${htmlOfPaginationDispatch}
     </#if>
	<script>vselect();</script>
	</div>
</#macro>

<#macro ToolbarWithDeleteNoPage checkboxname deleteaction="" deleteScript="" form="ec" confirmMsg="" target="" callback="" tableDivClass="base-operation">
	  <div class="${tableDivClass}">
	  <p class="opt">
            <span class="ui-checkbox ui-checkbox-all" data-all="no"><input type="checkbox" class="chk" onclick='javascript:<@doCheckbox checkboxname="${checkboxname}"/>'>全选</span>
           <a href="javascript:<@doDeleteProcessBar form="${form}" checkboxname="${checkboxname}" deleteScript="${deleteScript}" deleteaction="${deleteaction}" confirmMsg="${confirmMsg}" target="${target}" callback="${callback}"/>;">删除</a>
      <#nested>
      </p>
      </div>
      <script>vselect();</script>
	  <#--div class="f-left pl-20">
    	<input type="checkbox" id="allSelect" onclick='javascript:<@doCheckbox checkboxname="${checkboxname}"/>' ><label for="allSelect">&nbsp;全选&nbsp;&nbsp;</label>
    	<span class="input-btn2" onclick='javascript:<@doDeleteProcessBar form="${form}" checkboxname="${checkboxname}" deleteScript="${deleteScript}" deleteaction="${deleteaction}" confirmMsg="${confirmMsg}" target="${target}" callback="${callback}"/>'><button type="button">删除</button></span> 
    					
   	 </div>
    <div class="clr"></div>
    </div-->
</#macro>

<#macro Toolbar container="" tableDivClass="base-operation">
	<div class="${tableDivClass}">
	  <p class="opt">
    	<#nested>
      </p>	
   	 <#if container?default("") != "">
    	 <script>
		 var reloadDataContainer ="${container}"
		 </script>
		 ${htmlOfPaginationLoad}
   	 <#else>
     	${htmlOfPaginationDispatch}
     </#if>
     <script>vselect();</script>
	</div>
</#macro>

<#macro ToolbarBlank class="f-left pl-20" tableDivClass="base-operation">
	  <div class="${tableDivClass}">
	  <p class="opt">
    	<#nested>				
   	 </p>
	</div>
</#macro>

<#--
*  宏名：doDelete
*  功能：扩展ECtoolbarWithDelete宏的"删除"功能 
*  form: extremecomponents标签组件所在的form的name，不写时extremecomponents会产生一个名称为ec的默认from
*  checkboxname：extremecomponents标签组件中第一（选择）列的checkbox的name
*  deleteaction：extremecomponents标签组件中删除时触发的Action的name
*  confirmMsg: 删除时的提示信息
-->
<#macro doDeleteProcessBar form checkboxname confirmMsg deleteScript="" deleteaction="" target="" callback="">
	var flag= false;
	var name = '${checkboxname?default('')}';
	var nameArray = name.split(',');
	var confirmMsg = '${confirmMsg?default('')}';
	
	for(var j=0;j<nameArray.length;j++){
		var checkbox = document.getElementsByName(nameArray[j]);
	    for(var i=0;i<checkbox.length;i++){				 
			if(checkbox[i].checked==true){
				flag = true;
				break;
			}
			if(flag){
				break;
			}	
		}
	}

	if(!flag){
		showMsgWarn('没有选要删除的行，请先选择！');
		return;
	}	
	var _tempDeleteMessage = '您确认要删除信息吗？';
	if (confirmMsg != ''){
		_tempDeleteMessage = confirmMsg;
	}
	if(showConfirm(_tempDeleteMessage)){		
		<#if deleteScript?default('') != ''>
			eval('${deleteScript}()');
		<#else>
			<#if target?default('') != ''>
				${form}.target = '${target}';
			</#if>
			${form}.action = '${deleteaction}';
			${form}.submit();
			<#if callback?default("") != "">
				eval('${callback}')();
			</#if>
		</#if>
	} else {
		return;
	}
	
</#macro>


<#--3
*  宏名：datepicker
*  功能：日期选取控件
-->
<#macro datepicker name="" id="" msgName="" value="" class="input-txt input-date fn-left" readonly="" style="width:140px;" size="22" notNull="false" dateFmt="yyyy-MM-dd" onpicked="clickNoMethod" maxlength="10" placeholder="" oncleared="clickNoMethod" minDate="1900-01-01 00:00:00" maxDate="2099-12-31 23:59:59">
<#if name != "" && id == "">
<#local id=name />
<#elseif id != "" && name == "">
<#local name = id /> 
</#if>
<input name="${name}" msgName="${msgName}" dataType="date" id="${id}" <#if placeholder != ''>placeholder="${placeholder}"</#if> <#if style != "">style="${style}"</#if> type="text" class="${class}  input-readonly" readonly="readonly" size="${size}" maxlength="${maxlength}" notNull="${notNull}" value="${value}" <#if readonly?default("") == "" || readonly?default("") == "false">onclick="WdatePicker({minDate:'${minDate}',maxDate:'${maxDate}',dateFmt:'${dateFmt}',onpicked:${onpicked},oncleared:${oncleared}});"</#if>>
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
		<td <#if colspan != "">colspan="${colspan}"</#if>>
	</#if>
	<#if readonly == 'true'>
	<div class="${class} ui-select-box-disable" style="${style}">
	<#else>
	<div class="${class}" style="${style}">
	</#if>
		<input type="text" id="${id}Name" class="ui-select-txt" msgName="${msgName}" notNull="${notNull}" value="" readonly/>
	    <input type="hidden" id="${id}" name="${name}" value="${value}" class="ui-select-value" />
	    <a class="ui-select-close"></a>
	    <div class="ui-option" <#if onChange!=''>myfunchange="${onChange}"</#if>>
	    	<div class="a-wrap">
	        <#nested>
	        </div>
	    </div>
	</div>
	
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