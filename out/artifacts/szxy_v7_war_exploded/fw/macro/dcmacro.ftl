
<#macro analyzeReport dataId="" reportCode="">

<script>
function replaceParameter(url){
	<#if _keys?exists>
	<#list _keys as key>
		url = url.replaceAll("{${key}}", "${Request["_map"][key]!}");
	</#list>
	</#if>
	return url;
}

function analyzeList(){
	$("TR[listOn]").each(function(){
		var text = $(this).attr("listOn");
		if(text.indexOf("<$") >= 0 && text.indexOf("$>") >= 0){
			var text1 = text.substring(text.indexOf("<$") + 2, text.indexOf("$>"));
			var lastIndex = text1.length;
			if(text1.indexOf("@") >= 0){
				lastIndex = text1.indexOf("@");
			}
			var text2 = text1.substring(0, lastIndex);
			//获取对象属性
			var text3 = text1.substring(text1.indexOf("@") + 1, text1.length);
			text2 = text2.replaceAll("\\.", "/");
			text2 = replaceParameter(text2);
			var url = "${request.contextPath}/" + text2;
			var dataf = _dcUrlMap[url];
			if(!dataf || dataf == ""){
				var tr = $(this);
				$.getJSON({
					async:false,
					cache:true,
					url:url,
					success:function(data){
						for(var i = data.length - 1; i > 0; i --){
							var trc = tr.clone();
							trc.removeAttr("listOn");
							var html = trc.html();							
							trc.html(dealHtml(data, html, i));
							tr.after(trc);
						}
						tr.html(dealHtml(data, tr.html(), 0));
					}
				});
			}
		}
	});
	return true;
}

function dealHtml(data, html, index){
	var start = html.indexOf("=_");
	if(start > 0){
		var index1 = html.indexOf(",", start);
		var index2 = html.indexOf("]", start);
		var end = (index1 >= 0 && index1 < index2)? index1 : index2; 
		var attr = html.substring(start + 2, end);
		var value = $(data[index]).attr(attr);
		html = html.replaceAll("_" + attr + ",",value + ","); 
		html = html.replaceAll("_" + attr + "\\]",value + "]"); 
	}
	html = html.replaceAll("_index\\]", index + "]");
	return html;
}

function analyzeUrl(p, text, valType){
	if(text.indexOf("<$") >= 0 && text.indexOf("$>") >= 0){
		var text1 = text.substring(text.indexOf("<$") + 2, text.indexOf("$>"));
		var text2 = text1.substring(0, text1.indexOf("@"));
		//获取对象属性
		var text3 = text1.substring(text1.indexOf("@") + 1, text1.length);
		text2 = text2.replaceAll("\\.", "/");
		text2 = replaceParameter(text2);
		var url = "${request.contextPath}/" + text2;
		v = _dcUrlMap[url];
		if(!v || v == ""){
			$.getJSON({
				async:false,
				url:url,
				cache:true,
				success:function(data){
					var objs = data;
					var obj;
					_dcUrlMap[url] = JSON.stringify(data);
					if(text3.indexOf("[") == 0){
						if(objs.length <= 0){
							obj = "";
						}
						else{
							var index = text3.substring(1, text3.indexOf("]"));
							if(index == +index){
								obj = objs[index];
							}
							else{
								ps2 = index.split("=");
								if(ps2[0] && ps2[1]){
									var success = false;
									for(objsIndex = 0; objsIndex < objs.length; objsIndex ++){
										if($(objs[objsIndex]).attr(ps2[0]) == ps2[1]){
											obj = objs[objsIndex];
											success = true;
											break;
										}
									}
									if(!obj || success == false){
										obj = "";
									}
								}
							}
						}
						text3 = text3.substring(text3.indexOf("]") + 1, text3.length);
					}
					else{
						obj = objs;
					}
					
					if(obj == ""){
						if(valType == "1")
							$(p).val("");
						else if(valType == "2")
							$(p).text("");
						else{
							$(p).attr(valType, "");
						}
					}
					else{
						var vt = $(obj).attr(text3);
						if(valType == "1")
							$(p).val(vt);
						else if(valType == "2")
							$(p).text(vt);
						else{
							$(p).attr(valType, vt);
						}
					}
				}
			});
		}
		else{
			var obj;
			var objs = JSON.parse(v);
			if(text3.indexOf("[") == 0){
				if(objs.length <= 0){
					obj = "";
				}
				else{
					var index = text3.substring(1, text3.indexOf("]"));
					if(index == +index){
						obj = objs[index];
					}
					else{
						ps2 = index.split("=");
						if(ps2[0] && ps2[1]){
							var success = false;
							for(objsIndex = 0; objsIndex < objs.length; objsIndex ++){
								if($(objs[objsIndex]).attr(ps2[0]) == ps2[1]){
									obj = objs[objsIndex];
									success = true;
									break;
								}
							}
							if(!obj || success == false){
								obj = "";
							}
						}
					}
				}
				text3 = text3.substring(text3.indexOf("]") + 1, text3.length);
			}
			else{
				obj = objs;
			}
			if(obj == ""){
				if(valType == "1")
					$(p).val("");
				else if(valType == "2")
					$(p).text("");
				else{
					$(p).attr(valType, "");
				}
			}
			else{
				
				var vt = $(obj).attr(text3);
				if(valType == "1")
					$(p).val(vt);
				else if(valType == "2")
					$(p).text(vt);
				else{
					$(p).attr(valType, vt);
				}
			}
		}
		
		$(p).show();
		
		if(valType == "1"){
			if($(p).val() && $(p).val().indexOf("<$") >= 0 && $(p).val().indexOf("$>") >= 0){
				$(p).val("");
			}
		}
		else if(valType == "2"){
			if($(p).text().indexOf("<$") >= 0 && $(p).text().indexOf("$>") >= 0){
				$(p).text("");
			}
		}
		else{
			if($(p).attr(valType).indexOf("<$") >= 0 && $(p).attr(valType).indexOf("$>") >= 0){
				$(p).attr(valType, "");
			}
		}
	}
}

function analyzeText(){
	var pns = ["TEXTAREA", "SPAN"];
	var objectMap = {};
	for(var index = 0; index < pns.length; index ++){
		$(pns[index]).each(function(){
			var text = $(this).text();
			if(text.indexOf("<$") >= 0 && text.indexOf("$>") >= 0){
				var text1 = text.substring(text.indexOf("<$") + 2, text.indexOf("$>"));
				var text2 = text1.substring(0, text1.indexOf("@"));
				var objectName = text2.substring(3, text2.indexOf(".", 3));
				var ids = text2.substring(text2.indexOf("object.") + "object.".length, text2.length);
				var ids_ = objectMap[objectName];
				if(!ids_ || ids_ == null){
					ids_ = ids;
				}
				else{
					if(ids_.indexOf(ids) < 0){
						ids_ = ids_ + "," + ids;
					}
				}
				objectMap[objectName] = ids_;
			}
		});
	}
	
	for(var oname in objectMap){
		urls = "${request.contextPath}/dc/" + oname + "/objectsWithIds";
		$.ajax({
				async:false,
				type:"post",
				dataType:"json",
				url: urls,
				data:{ids:objectMap[oname]},
				success:function(data){
					for(var d in data){
						url = "${request.contextPath}/dc/" + oname + "/object/" + data[d].id;
						_dcUrlMap[url] = JSON.stringify(data[d]);
					}
				}
		});
	}	
	for(var index = 0; index < pns.length; index ++){
		$(pns[index]).each(function(){
			var text = $(this).text();
			var p = $(this);
			analyzeUrl(p, text, "2");
		});
	}
	return true;
}

function analyzeVal(){
	var pns = ["INPUT", "SELECT"];	
	var objectMap = {};
	for(var index = 0; index < pns.length; index ++){
		$(pns[index]).each(function(){
			var text = $(this).val();
			var text2 = $(this).attr("preVal");
			if(text2 != undefined){
				text = text2;
			}
			if(text.indexOf("<$") >= 0 && text.indexOf("$>") >= 0){
				var text1 = text.substring(text.indexOf("<$") + 2, text.indexOf("$>"));
				var text2 = text1.substring(0, text1.indexOf("@"));
				//获取对象属性
				var text3 = text1.substring(text1.indexOf("@") + 1, text1.length);
				text2 = text2.replaceAll("\\.", "/");
				text2 = replaceParameter(text2);
				var objectName = text2.substring(3, text2.indexOf(".", 3));
				var ids = text2.substring(text2.indexOf("object.") + "object.".length, text2.length);
				var ids_ = objectMap[objectName];
				if(!ids_ || ids_ == null){
					ids_ = ids;
				}
				else{
					if(ids_.indexOf(ids) < 0){
						ids_ = ids_ + "," + ids;
					}
				}
				objectMap[objectName] = ids_;
			}
		});
	}
	
	for(var oname in objectMap){
		urls = "${request.contextPath}/dc/" + oname + "/objects/" + objectMap[oname];
		$.getJSON({
				async:false,
				url: urls ,
				cache:true,
				success:function(data){
					for(var d in data){
						url = "${request.contextPath}/dc/" + oname + "/object/" + data[d].id;
						_dcUrlMap[url] = JSON.stringify(data[d]);
					}
				}
		});
	}	
	
	for(var index = 0; index < pns.length; index ++){
		$(pns[index]).each(function(){
			var text = $(this).val();
			var text2 = $(this).attr("preVal");
			if(text2 != undefined){
				text = text2;
			}
			var p = $(this);
			analyzeUrl(p, text, "1");
		});
	}
	return true;
}

$(document).ready(function(){
layer.load(2);
_dcUrlMap = [];
_dcUrlMap["00000000000000000000000000000000"]= "系统";
if(analyzeList()){
	if(analyzeVal()){
		if(analyzeText()){
			if(onDealVsqlSelect()){
				if(onDealcSelect()){
					if(onDivLoader()){
						layer.closeAll("loading");
						try{
							doLastStep();
						}
						catch(e){
						}
					};
				}
			}
		}
	}
}
});

</script>
</#macro>

<#macro rowOne rowId="" onChange="" class="form-control" ignoreSave="false" readonly=false minValue="" maxValue="" dataType="" value="" vsql="" isCondition=false operation="=" cselect="" rowHeight="30" nullable=true label="" mcodeId="" labelWidth="80" valueWidth="200" type="input" columnId="" initValue=true colspan=1>
<tr height="${rowHeight}" id="${rowId!}">
	<@columnOne onChange=onChange ignoreSave=ignoreSave class=class readonly=readonly minValue=minValue maxValue=maxValue dataType=dataType value=value isCondition=isCondition operation=operation cselect=cselect vsql=vsql rowHeight=rowHeight nullable=nullable label=label mcodeId=mcodeId labelWidth=labelWidth valueWidth=valueWidth type=type columnId=columnId initValue=initValue colspan=colspan />
</tr>
</#macro>

<#macro columnOne preHtml="" expendHtml="" ignoreSave="false" class="form-control" readonly=false vsql="" minValue="" maxValue="" dataType="" isCondition=false operation="=" cselect="" value="" rowHeight="30" mcodeId="" nullable=true label="" labelWidth="80" valueWidth="200" type="input" columnId="" initValue=true colspan=1 onChange="">
	<#if type != "hidden">
	<th class="thForm" width="${labelWidth}" style="padding:5px"><font <#if !nullable>color='red'</#if>>${label!}</font></th>
	<td height="${rowHeight}" width="${valueWidth}" style="padding:5px" colspan="${colspan}">
	</#if>
	<div id="preHtml_${columnId!}">${preHtml!}</div>
	<#if type=="input" || type=="hidden">
		<#if initValue && value=="" && _dataId?default("") != "">
		<input commitData="true" class="${class}" ignoreSave="${ignoreSave}" <#if onChange != "">onchange="${onChange}"</#if> <#if readonly>disabled</#if> msgName="${label}" minValue="${minValue}" maxValue="${maxValue}"  dataType="${dataType}" isCondition=${isCondition?default(false)?string} operation="${operation!}" notNull="${(!nullable)?string}" msgName=${label} value="<$dc.report.data.${_reportCode!}.${_dataId!}@${columnId!}$>" id="${columnId}" name="${columnId}" type="${type}" size="${valueWidth?number/8}"/>
		<#else>
		<input commitData="true" class="${class}" ignoreSave="${ignoreSave}" <#if onChange != "">onchange="${onChange}"</#if> <#if readonly>disabled</#if> msgName="${label}" minValue="${minValue}" maxValue="${maxValue}"  dataType="${dataType}" isCondition=${isCondition?default(false)?string} operation="${operation!}" notNull="${(!nullable)?string}" id="${columnId}" value="${value}" name="${columnId}" type="${type}" size="${valueWidth?number/8}"/>
		</#if>
	<#elseif type="textarea">
		<#if initValue && value=="" && _dataId?default("") != "">
		<textarea commitData="true" notNull="${(!nullable)?string}" ignoreSave="${ignoreSave}" <#if onChange != "">onchange="${onChange}"</#if> <#if readonly>disabled</#if> msgName="${label}" dataType="${dataType}" id="${columnId}" name="${columnId}" class="comments"><$dc.report.data.${_reportCode!}.${_dataId!}@${columnId!}$></textarea></td>
		<#else>
		<textarea commitData="true" notNull="${(!nullable)?string}" ignoreSave="${ignoreSave}" <#if onChange != "">onchange="${onChange}"</#if> <#if readonly>disabled</#if> msgName="${label}" dataType="${dataType}" id="${columnId}" name="${columnId}" class="comments">${value}</textarea>
		</#if>
	<#elseif type == "radio" && mcodeId != "">
		<@radioDiv2 id=columnId  value="" displayName=label mcodeId=mcodeId />
	<#elseif type == "select" && mcodeId != "">
		<#if value?string == "" && _dataId?default("") != "">
		<#local value = "<$dc.report.data." + _reportCode?default("") + "." + _dataId?default("") + "@" + columnId?default("") + "$>" />
		</#if>
		<select commitData="true" notNull="${(!nullable)?string}" ignoreSave="${ignoreSave}" <#if onChange != "">onchange="${onChange}"</#if> msgName="${label}" <#if readonly>disabled</#if> id="${columnId!}" isCondition=${isCondition?default(false)?string} operation="${operation!}" preVal="${value!}" name="${columnId!}" data-placeholder="请选择" class="multiselect form-control col-md-10 col-sm-10 col-xs-10 "  >		
			${mcodeSetting.getMcodeSelect(mcodeId, value?string, "1")}
		</select>
	<#elseif type == "select" && cselect != "">
		<#if value?string == "" && _dataId?default("") != "">
		<#local value = "<$dc.report.data." + _reportCode?default("") + "." + _dataId?default("") + "@" + columnId?default("") + "$>" />
		</#if>
		<select commitData="true" notNull="${(!nullable)?string}" ignoreSave="${ignoreSave}" <#if onChange != "">onchange="${onChange}"</#if> msgName="${label}" <#if readonly>disabled</#if> cselect="1" id="${columnId!}" isCondition=${isCondition?default(false)?string} operation="${operation!}" preVal="${value!}" name="${columnId!}" data-placeholder="请选择" class="multiselect form-control col-md-10 col-sm-10 col-xs-10 "  >		
			<#list cselect?split(";") as _objs>
			<#local _obj = _objs?split(",") />
			<option value="${_obj[0]!}">${_obj[1]!}</option>
			</#list>
		</select>
	<#elseif type == "select" && vsql != "">
		<select commitData="true" notNull="${(!nullable)?string}" ignoreSave="${ignoreSave}" <#if onChange != "">onchange="${onChange}"</#if> msgName="${label}" <#if readonly>disabled</#if> vsql="${vsql!}" id="${columnId!}" isCondition=${isCondition?default(false)?string} operation="${operation!}" preVal="${value!}" name="${columnId!}" data-placeholder="请选择" class="multiselect form-control col-md-10 col-sm-10 col-xs-10 "  >		
		</select>
	<#elseif type == "radio" && cselect != "">
		<#if value?string == "" && _dataId?default("") != "">
		<#local value = "<$dc.report.data." + _reportCode?default("") + "." + _dataId?default("") + "@" + columnId?default("") + "$>" />
		</#if>
		<#list cselect?split(";") as _objs>
			<#if _objs_index gt 0><br /></#if>
			<#local _obj = _objs?split(",") />
			<input commitData="true" type="radio" name="${columnId!}" id="${columnId!}_${_objs_index}" value="${_obj[0]!}"><label for="${columnId!}_${_objs_index}">${_obj[1]!}</label>
		</#list>
	</#if>
	<div>${expendHtml!}</div>
	<#if type != "hidden" >
	</td>
	</#if>
</#macro>
<#macro rowMulti multiNum=9 rowHeight="30" label="" labelWidth="80" valueWidth="200" type="input" columnId="" colspan=1>
<tr height="${rowHeight}">
	<td rowspan="${multiNum}" width="${labelWidth}" style="padding:5px">
		${label!}
	</td>
	<#if type=="input">
		<td width="${valueWidth}" style="padding:5px"  colspan="${colspan}">
		<input value="<$dc.report.data.${_reportCode!}.${_dataId!}@${columnId!}0$>" id="${columnId}0" name="${columnId}0" type="text" size="${valueWidth?number/8}"/></td>
	<#elseif type="textarea">
		<td width="${valueWidth}" style="padding:5px"  colspan="${colspan}">
		<textarea commitData="true" class="comments" id="${columnId}0" name="${columnId}0"><$dc.report.data.${_reportCode!}.${_dataId!}@${columnId!}0$></textarea></td>
	</#if>
</tr>
<#list 1..multiNum-1 as index>
	<tr height="${rowHeight}">
		<#if type=="input">
		<td width="${valueWidth}" style="padding:5px"  colspan="${colspan}">
		<input commitData="true" value="<$dc.report.data.${_reportCode!}.${_dataId!}@${columnId!}${index}$>" id="${columnId}${index}" name="${columnId}${index}" type="text" size="${valueWidth?number/8}"/></td>
	<#elseif type="textarea">
		<td width="${valueWidth}" style="padding:5px" colspan="${colspan}">
		<textarea commitData="true" class="comments" id="${columnId}${index}" name="${columnId}${index}"><$dc.report.data.${_reportCode!}.${_dataId!}@${columnId!}${index}$></textarea></td>
	</#if>
	</tr>
</#list>
</#macro>


<#macro rowMulti2 multiNum=9 rowHeight="30" labels="" labelWidth="80" type="input" columnIds="">
<tr height="${rowHeight}">
	<#list labels?split(",") as label>
	<td width="${labelWidth}" style="padding:5px">
		${label!}
	</td>
	</#list>
</tr>
<#list 1..multiNum as index>
	<tr height="${rowHeight}">
		<#list columnIds?split(",") as columnId>
		<#if type=="input">
		<td width="${labelWidth}" style="padding:5px">
		<input commitData="true" value="<$dc.report.datas.${_reportCode!}.${_dataId!}@[${index - 1}]${columnId!}$>" id="${columnId}@${index}" id="${columnId}@${index}" name="${columnId}@${index}" type="text" size="${labelWidth?number/8}"/></td>
		<#elseif type="textarea">
		<td width="${labelWidth}" style="padding:5px">
		<textarea commitData="true" class="comments" id="${columnId}@${index}" name="${columnId}@${index}"><$dc.report.datas.${_reportCode!}.${_dataId!}@[${index - 1}]${columnId!}$></textarea></td>
		</#if>
		</#list>
	</tr>
</#list>
</#macro>

<#macro dcEditTemplate reportId templateType value="编辑模板" dataId="">
<@dcRdButton value=value url="/dc/report/templateEdit?_templateType=" + templateType + "&_reportId=" + reportId + "&_dataId=" + dataId  />
</#macro>

<#macro dcRemoveButton reportId class="btn btn-blue"  value="删除" dataId="" doAfter="" checkReturn="doCheckRemove()">
<@dcAjaxButton value=value class=class url="/dc/report/removeByDataId/" + reportId + "/" + dataId  checkReturn=checkReturn doAfter=doAfter />
</#macro>

<#macro dcRemoteButton reportId url="" class="btn btn-blue"  value="操作" doAfter="" dataId="" checkReturn="">
<@dcAjaxButton value=value class=class dataId=dataId url=url  checkReturn=checkReturn doAfter=doAfter />
</#macro>

<#macro dcAjaxButton isConditionButton=false dataId="" reportId="" value="返回" class="btn btn-blue" url="" templatePath="" rtnContainer="" checkReturn="" doAfter="">
<#local absPath = "" />
<#if templatePath != "" && templatePath?index_of("/") != 0>
<#local templatePath = "/datacollection/report/" + templatePath />
</#if>
<input type="button" dataId="${dataId}" value="${value}" class="${class}" onclick="javascript:_onAjaxOperation(${isConditionButton?string}, '${url}', '${reportId}', '${templatePath}', '${rtnContainer}', '${checkReturn}', '${doAfter}');"/>
</#macro>

<#macro dcRdButton isConditionButton=false reportId="" value="返回" class="btn btn-blue" url="" templatePath="" rtnContainer="" checkReturn="">
<#local absPath = "" />
<#if templatePath != "" && templatePath?index_of("/") != 0>
<#local templatePath = "/datacollection/report/" + templatePath />
</#if>
<input type="button" value="${value}" class="${class}" onclick="javascript:_onOperation(${isConditionButton?string}, '${url}', '${reportId}', '${templatePath}', '${rtnContainer}', '${checkReturn}');"/>
</#macro>

<#macro saveButton value="保存" updatable=true preSave="" class="btn btn-orange" reportId="">
<#if updatable>
<input type="button" class="${class}" value=${value} onclick="javascript:onSaveReportData('${preSave}', '${reportId}');" <#if !updatable>disabled</#if> />
</#if>

</#macro>

<#macro reportData reportCode="" dataId="">
<script type="text/javascript" src="${request.contextPath}/static/js/validate.js"></script>
<script src="${request.contextPath}/static/cookie/jquery.cookie.js"></script>
<script>
function onDivLoader(){
	$("DIV._divLoader").each(function(){
		var url = $(this).attr("url");
		if(url != ""){
			$(this).load(url);
		}
	});
	return true;
}

function onDealVsqlSelect(){
	$("DIV.__reportData SELECT[vsql != '']").each(function(){
		var sql = $(this).attr("vsql");
		var se = $(this);
		if(sql != undefined){
			se.append("<option value=''>--- 请选择 ---</option>");
			$.getJSON({
				async:false,
				url:"${request.contextPath}/dc/report/vsql",
			 	data: encodeURIComponent($(this).attr("vsql")),  
			    type:'post',  
			    cache:true,
			    contentType: "application/json",
			    success:function(data){
			    	for(var i = 0; i < data.length; i ++){
			    		var d = data[i];
			    		se.append("<option value='" + d[0] + "'>" + d[1] + "</option>");
			    	}
			    	se.val(se.attr("preVal"));
			    }
			});		
		}
	});
	return true;
}

function onDealcSelect(){
	$("DIV.__reportData SELECT[cselect]").each(function(){
		$(this).val($(this).attr("preVal"));
	});
	return true;
}

function onSaveReportData(preSave, reportId){
	if(preSave != ""){
		if(!eval(preSave)){
			return false;
		};
	}
	layer.load(2);
	var types = ["INPUT", "SELECT", "TEXTAREA"];
	var obj = new Object();
	for(var i = 0; i < types.length; i ++){
		$("DIV.__reportData " + types[i]).each(function(){
			var commitData = $(this).attr("commitData");
			var ignoreSave = $(this).attr("ignoreSave");
			if(commitData != undefined && commitData == "true" && ignoreSave != "true"){
				var id = $(this).attr("id");
				if(id && id != ""){
					obj[id] = $(this).val();
				}
			}
		});
	}
	if(!checkAllValidate(".__reportData")){
		layer.closeAll('loading');
		return;
	}
	
	$.getJSON({
		url:"${request.contextPath}/dc/report/saveReportData",
		async:false,
	 	data: JSON.stringify(obj),  
	    type:'post',  
	    cache:false,  
	    contentType: "application/json",
	    success:function(data){
	    	layer.closeAll('loading');
	    	layer.msg(data.msg);
	    	
	    	if(reportId != ""){
		    	_onOperation(false, "", reportId, "", "", "");
		    }
	    }
	});
}
function doCheckRemove(){
	if(!confirm("是否要删除？")){
		return false;
	}
	return true;
}


function _onAjaxOperation(isConditionButton, url, reportId, templatePath, rtnContainer, checkReturn, doAfter){
	
	if(eval(checkReturn) == false){
		return false;
	}
	layer.load(2);
	$.getJSON({
		async:false,
		url:url,
		cache:true,
		success:function(data){
			if(doAfter != ""){
				eval(doAfter);
			}
//			$("#_tr_" + data.dataId).remove();
			layer.closeAll('loading');
		}
	});
}

function _onOperation(isConditionButton, url, reportId, templatePath, rtnContainer, checkReturn){
	if(eval(checkReturn) == false){
		return false;
	}
	layer.load(2);
	var condition = "";
	if(isConditionButton){
		var types = ["INPUT", "SELECT"];
		var arrays = new Array();
		for(var i = 0; i < types.length; i ++){
			$(types[i] + "[isCondition=true]").each(function(){
				var obj = new Object();
				var id = $(this).attr("id");
				obj["name"] = id;
				obj["value"] = $(this).val();
				obj["dataType"] = $(this).attr("dataType");
				obj["alias"] = $(this).attr("alias");
				obj["operation"] = $(this).attr("operation");
				arrays.push(obj);
			});
		}
		condition = encodeURIComponent(JSON.stringify(arrays));
	}
	
	if(url == ""){
		url = "${request.contextPath}/dc/report/listReportData/" + reportId + "?_viewPath=" + templatePath + "&_condition=${(_condition?url)!}";
	}
	else{
		if(url.toLowerCase().indexOf("http") != 0){
			url = "${request.contextPath}" + url
		} 
		if(url.indexOf("?")>=0){
			url = url + "&_viewPath=" + templatePath;
		}
		else{
			url = url + "?_viewPath=" + templatePath;
		}
		if(url.indexOf("_condition") < 0){
			if(isConditionButton){
				url = url + "&_condition=" + condition;
			}
			else{
				url = url + "&_condition=${(_condition?url)!}";
			}
		}
	}
	var container;
    if(!rtnContainer || rtnContainer == ""){
    	container =  $("#deskTopContainer").find(".model-div-show");
   	}
    else{
    	container = $(rtnContainer);
	}
    container.load(url, function(){
            layer.closeAll('loading');
    });
}
</script>
<style type="text/css">
.thForm{  text-align:right; padding-right:3px;}
  
.comments {  
 width:100%;/*自动适应父布局宽度*/  
 height:180px;
 overflow:auto;  
 word-break:break-all;  
/*在ie中解决断行问题(防止自动变为在一行显示，主要解决ie兼容问题，ie8中当设宽度为100%时，文本域类容超过一行时，  
当我们双击文本内容就会自动变为一行显示，所以只能用ie的专有断行属性“word-break或word-wrap”控制其断行)*/  
}  
</style>
<div class="__reportData col-sm-12">
<input commitData="true" type="hidden" id="_reportCode" name="_reportCode" value="${reportCode!}" />
<input commitData="true" type="hidden" id="_dataId" name="_dataId" value="${dataId!}" />
<input commitData="true" type="hidden" id="_isNew" name="_isNew" value="${_isNew!}" />
<#nested />

<@analyzeReport reportCode=reportCode dataId=dataId/>

</div>
</#macro>

<#macro dcDataVal reportCode="" dataId="" columnId="">
<span style="display:none;"><$dc.report.data.${reportCode}.${dataId}@${columnId}$></span>
</#macro>

<#macro dcObjVal type="" id="" columnId="" ignoreSpan=false>
<#if id != "">
<#if !ignoreSpan><span style="display:none;"></#if><$dc.${type}.object.${id}@${columnId}$><#if !ignoreSpan></span></#if>
<#else>
<#if !ignoreSpan>
<span></span>
</#if>
</#if>
</#macro>

<#macro dcCSelectVal cselect="" value="" >
<#list cselect?split(";") as cs>
	<#local cs2 = cs?split(",") />
	<#if cs2[0]?string == value?string>
		${cs2[1]!}
		<#break />			
	</#if>
</#list>
</#macro>

<#macro divLoader url class="_divLoader" id="_divLoader">
<div id="${id!}" class="${class}" url="${url!}"></div>
</#macro>

<#macro dealScoreScript>
function doChangeType(){
	var woDate = $("#wo_date").val();
	if(woDate.length == 0){
		var d = new Date();
		var mo = d.getMonth() + 1;
		var da = d.getDate();
		if(mo < 10)
			mo = "0" + mo;
		if(da < 10)
			da = "0" + da;
		$("#wo_date").val(d.getFullYear() + "/" + mo + "/" + da);
	}
	var scoreType = $("#score_type").val();
	if(!scoreType || isNaN(scoreType)){
		scoreType = 6;
		 $("#score_type").val(scoreType);
	}
	if(scoreType == 6 || scoreType == 27){
		$("#workOver0").show();
		$("#workOver1").show();
		$("#workOver2").show();
		$("#workOver3").show();
		$("#wo_date").attr("notnull", "true");
		$("#wo_start_time").attr("notnull", "true");
		$("#wo_end_time").attr("notnull", "true");
		$("#wo_effection").attr("notnull", "true");
		if(scoreIsReadonly && scoreIsReadonly == true){
			$("#score").attr("readonly", "true");
		}
		else{
			$("#score").removeAttr("readonly");
		}
		doChangeTime(scoreType);
	}
	else{
		$("#workOver0").hide();
		$("#workOver1").hide();
		$("#workOver2").hide();
		$("#workOver3").hide();
		$("#wo_date").attr("notnull", "false");
		$("#wo_start_time").attr("notnull", "false");
		$("#wo_end_time").attr("notnull", "false");
		$("#wo_effection").attr("notnull", "false");
		$("#score").removeAttr("readonly");
		$("#preHtml_description2").html("");
	}
}

function doChangeTime(scoreType){
	var startTime = $("#wo_start_time").val();
	var endTime = $("#wo_end_time").val();
	var effection = $("#wo_effection").val();
	var woDate = $("#wo_date").val();
	woDate = woDate.replaceAll("-", "/");
	woDate = woDate.replaceAll("\\.", "/");
	$("#wo_date").val(woDate);
	startTime = startTime.replace("：",":");
	endTime = endTime.replace("：",":");
	
	if(endTime.length > 0){
		if(endTime.indexOf(".") > 0){
			endTime = endTime.split(".")[0] + ":" + endTime.split(".")[1];
		}
		if(endTime.indexOf(":") < 0){
			endTime = endTime + ":00";
		}
		var ets = endTime.split(":");
		if(ets[0] > 23){
			ets[0] = 23;
			ets[1] = 59;
		}
		if(ets[1] > 59){
			ets[1] = 59;
		}
		if(ets[1].length == 1)
			ets[1] = ets[1] + "0";
		$("#wo_end_time").val(ets[0] + ":" + ets[1]);
	}
	
	if(startTime.length > 0){
		if(startTime.indexOf(".") > 0){
			startTime = startTime.split(".")[0] + ":" + startTime.split(".")[1];
		}
		if(startTime.indexOf(":") < 0){
			startTime = startTime + ":00";
		}
		var sts = startTime.split(":");
		
		if(sts[0] > 23){
			sts[0] = 23;
			ets[1] = 59;
		}
		
		if(sts[1] > 59){
			sts[1] = 59;
		}
		if(sts[1].length == 1)
			sts[1] = sts[1] + "0";
		$("#wo_start_time").val(sts[0] + ":" + sts[1]);
	}
	
	if(startTime.length == 0 || endTime.length == 0){
		return false;
	}
	

    var overTimeScale5 = 0;
    var overTimeScale10 = 0;
	if(ets[0] * 1 < sts[0] * 1){		
        overTimeScale5 = 24 - sts[0] * 1 - sts[1]/60;
		overTimeScale5 = overTimeScale5.toFixed(1);
        overTimeScale10 = ets[0]  *1 + ets[1]/60;        
		overTimeScale10 = overTimeScale10.toFixed(1);
	}
    else{
        overTimeScale5 = ets[0] * 1 - sts[0] * 1 + ets[1]/60 - sts[1]/60;
		overTimeScale5 = overTimeScale5.toFixed(1);
    }
    
	var score = 0;
    	

	var d = new Date();
	var woDate = $("#wo_date").val();
	if(woDate.length == 0){
		var d = new Date();
		var mo = d.getMonth() + 1;
		var da = d.getDate();
		if(mo < 10)
			mo = "0" + mo;
		if(da < 10)
			da = "0" + da;
		woDate = d.getFullYear() + "/" + mo + "/" + da;
	}
	else{
		var ws;
		if(woDate.indexOf("/") > 0){
			ws = woDate.split("/");
		}
		else if(woDate.indexOf("-") > 0){
			ws = woDate.split("-");
		}
		
		if(ws.length == 3){
			d = new Date(ws[0], ws[1] - 1, ws[2]);
		}
	}
	var scoreType = $("#score_type").val();
  	if(overTimeScale5 >= 1){		
        if(d.getDay() == 0 || d.getDay()  == 6){
        	if(scoreType == 6){
    			score = overTimeScale5 * 10;
    		}
    		else if(scoreType == 27){
    			score = overTimeScale5 * 6;
    		}
    		else{
	            score = overTimeScale5 * 5;
    		}
    	}
        else{
            score = overTimeScale5 * 5;
        }
	}
       
 	if(overTimeScale10 > 0){		
		score = score + overTimeScale10 * 10;
	}   
	var isWeekend = "";
	if(d.getDay() == 0){
		isWeekend = "周日";
	}
	else if(d.getDay() == 6){
		isWeekend = "周六";
	}
	
	score = score.toFixed(0);
   	$("#score").val(score);
	
	var con = $("#description2").val();
	var con2 = "";
	if(con.indexOf("【")  >= 0 && con.lastIndexOf("】") >= 0){
		con2 = con.substring(con.indexOf("【") + 1, con.lastIndexOf("】"));		
	}
	else{
		con2 = con;
	}
	
	if(startTime != "" && endTime != "" && effection != ""){
		var effectionDescription = "";
		var effectionDescription0 = "";
		if(ets[0] * 1 >=24 ){
			effectionDescription0 = "通宵";
		}
		if(effection == 1){
			effectionDescription += "取得了重要成果，";
		}
		$("#preHtml_description2").html(woDate + "，" + isWeekend + effectionDescription0 + "加班/值班" + (overTimeScale10 * 1 + overTimeScale5 * 1) + "个小时，" + effectionDescription + "内容如下：");
		$("#description2").val(con2);
	}
}
var scoreChange = 0;
function doScoreChange(){
		scoreChange = 0;
}
function doLastStep(){
doChangeType();
doScoreChange();
}
</#macro>
