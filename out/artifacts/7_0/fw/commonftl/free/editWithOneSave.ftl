<#import "/fw/macro/htmlcomponent.ftl" as component>
<#import "/fw/macro/commonmacro.ftl" as commonmacro>
<@component.moduleDiv>
<span id="${dataUuid!}">
<@component.tableDetail divClass="table-form">
	<#assign columnSize = fwDataGroup.columnSize />
	<#if columnSize == 0>
	<#assign columnSize =  1/>
	</#if>
	<#list fwDataDetailDatas as data>
		<#assign dataId = data.id?default("") />
		<#assign haveClosed = false />
		<#assign tdCount = 0 />
		<#assign occupyCount = 0 />
		<#assign remainCount = columnSize />
		<#assign remainCountReal = columnSize />
		<#list fwDataDetails as detail>
			<#assign dataValue = data[detail.entityName]?default("") />
			<#if detail.isHiden == 1>
				<#if dataId != "">
				<input type="hidden" value="${dataValue}" dataId="${dataId!}" id="${detail.entityName!}_${dataId}" name="${detail.entityName!}" />
				<#else>
				<input type="hidden" value="${dataValue}" dataId="${dataId!}" id="${detail.entityName!}_${detail_index}" name="${detail.entityName!}" />
				</#if>
			<#else>
				<#assign remainCount = remainCountReal />
				<#assign remainCount = remainCount - detail.columnSize />
				<#if remainCount &lt; 0 && remainCountReal &gt; 0 && remainCountReal != columnSize>
					<td colspan="${remainCountReal* 2}">&nbsp;</td>
				</#if>
				<#--预测剩下的空间小于0，或者-->
				<#if occupyCount % columnSize == 0 || remainCount &lt; 0>
					<#if occupyCount &gt; 0>
						</tr>
						<#assign haveClosed = true />
						<#assign remainCount = columnSize />
						<#assign remainCountReal = columnSize />
					</#if>
					<tr>
					<#assign occupyCount = 0 />
					<#assign tdCount = 0 />
					<#assign haveClosed = false />
				</#if>
				<#assign dataValue = data[detail.entityName]?default("") />
				<th dataId="${dataId!}" id="th_${(detail.entityName + "_" + dataId)!}">${detail.displayName!}：</th>
				<#assign tdCount = tdCount + detail.columnSize />
				<#assign occupyCount = occupyCount + detail.columnSize />
				<#assign colspan = remainCountReal />
				<#if detail.columnSize &lt; colspan>
					<#assign colspan = detail.columnSize />
				</#if>
				<#assign remainCountReal = remainCountReal - colspan />
				<td <#if colspan &gt; 1>colspan="${colspan * 2 -1}"</#if>>
				
			<#-- 开始组装数据 -->	
			
			<#if detail.displayType?default("INPUT") == "INPUT">
				<input dataId="${dataId!}" msgName="${detail.displayName!}" style="<#if detail.columnSize &gt; 1>width:${88*detail.columnSize}px;</#if>" type="input" class="input-txt" name="${detail.entityName!}" id="${(detail.entityName + "_" + dataId)!}" value="${dataValue}">
			<#elseif detail.displayType == "SELECT">
				<#if detail.mcodeId?exists>
					<select class="select-input" dataId="${dataId!}"  name="${detail.entityName!}" id="${detail.entityName!}_${dataId}" style="height:25px;width:160px;">
		  		 	${mcodeSetting.getMcodeSelector(detail.mcodeId, dataValue?string, "1")}
					</select> 
				<#else>
					<input dataId="${dataId!}" msgName="${detail.displayName!}" type="input" class="input-txt" name="${detail.entityName!}" id="${(detail.entityName + "_" + dataId)!}" value="${dataValue}">
				</#if>
			<#elseif detail.displayType == "TEXT">
				<#if detail.mcodeId?exists>
				${mcodeSetting.getMcode(detail.mcodeId, dataValue)}
				<#else>
				${dataValue}
				</#if>
				<input dataId="${dataId!}" msgName="${detail.displayName!}" type="hidden" name="${detail.entityName!}" id="${(detail.entityName + "_" + dataId)!}" value="${dataValue}">
			<#elseif detail.displayType == "RADIO">
				${mcodeSetting.getMcodeRadio(detail.mcodeId, dataValue?string, detail.entityName, dataId)}
			<#elseif detail.displayType == "DATE">
				${(data[detail.entityName]?string(detail.mcodeId))!}
			<#elseif detail.displayType == "CHECKBOX">
				${mcodeSetting.getMcodeCheckbox(detail.mcodeId, dataValue?string, detail.entityName, dataId)}
			<#elseif detail.displayType == "DATEEDIT">
			<#elseif detail.displayType == "TEXTAREA">
				<textarea type="text" class="mt-5 mb-5 text-area" style="width:${detail.columnSize*150}px;" id="${detail.entityName!}" id="${detail.entityName!}" >${dataValue}</textarea>
			</#if>
				</td>
			</#if>
		</#list>
		<#if haveClosed == false && (columnSize - tdCount) &gt; 0>
		<td colspan="${(columnSize - tdCount)*2}">&nbsp;</td>
		</tr>
		</#if>
		
		<#if fwDataDetailDatas?size == data_index + 1>
			<tr>
				<td class="td-opt" colspan="${columnSize * 2}">
					<span id="${dataId}_span_before" dataId="${dataId}"></span>
			        <a dataUuid="${dataUuid!}" markType="saveBtn" dataId="${dataId}" href="javascript:void(0);" class="abtn-blue ml-10">保存</a>
					<span id="${dataId}_span_after" dataId="${dataId}"></span>
					&nbsp;<span id="${dataId}_saveTip" dataId="${dataId}"></span>
			    </td>
		  </tr>
		  <#else>
			<tr id="tr_${dataId}" colspan="${columnSize}" height="10"></tr>
			<#--记录之间的分隔行-->
			<tr colspan="${columnSize}" height="10"></tr>
		</#if>
	</#list>
</@component.tableDetail>
</span>
</@component.moduleDiv>

<script>
function changedMark(id){
	$("#th_" + id).css({'color':'blue','font-weight':'600'});
}

$(document).ready(
function(){
	$("input[type='input'],select").each(
	function(){
		$(this).on("change", "", function(){
			changedMark($(this).attr("id"));
		});
	});
	$("input[type='radio']").each(
	function(){
		$(this).on("click", "", function(){
			changedMark($(this).attr("name"));
		});
	});
});

function dealParamOneSave(dataUuid){
	var ps = new Array();
	$("span[id='" + dataUuid + "'] [name='id']").each(function(){
		var dataId = $(this).val();
		var objs = new Object();
		//获取参数
		$("span[id='" + dataUuid + "'] select[dataId='" + dataId+ "']").each(function(){
			objs[$(this).attr("name")] = $(this).val();
		});
		$("span[id='" + dataUuid + "'] input[dataId='" + dataId+ "']").each(function(){
			if($(this).attr("type") == "radio" ){
				if($(this).attr("checked") == "checked"){
					var name = $(this).attr("name");
					name = name.replace("_" + dataId, "");
					objs[name] = $(this).val();
				}
			}
			else{
				if($(this).attr("type") == "checkbox" ){
					if($(this).attr("checked") == "checked"){
						var name = $(this).attr("name");
						name = name.replace("_" + dataId, "");
						if(objs[name]){
							objs[name] = objs[name] + "," + $(this).val();
						}
						else{
							objs[name] = $(this).val();
						}
					}
				}
				else{
					objs[$(this).attr("name")] = $(this).val();
				}
			}
		});
		ps.push(objs);
	});
	alert(JSON.stringify(ps));
	return JSON.stringify(ps);
}

function doSuccess(dataUuid){
	$("span[id='" + dataUuid + "'] th").css({'color':'black','font-weight':'300'});
}
</script>

