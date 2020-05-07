
<#macro formGroup id formGroupName="" length="0" maxLength="1000" nullable="true" regexStr="" regexTip="" type="text" labelText="" lableColor="" circle=false value="">
	<div class="form-group col-lg-6 col-sm-6 col-xs-12 col-md-6" id="form-group-${id!}">
	<label class="ace-icon ${lableColor!} <#if circle>fa fa-circle </#if> col-md-3 control-label no-padding-right" for="${id!}">${labelText!}</label>
		<div class="col-xs-12 col-sm-12 col-md-9">
			<#if type=="select">
				<div >
					<div>
						<select id="${id!}" nullable="${nullable!}" data-placeholder="请选择" class="form-control col-md-12 col-sm-12 col-xs-12 ">
						<option value="">--- 请选择 ---</option>
						<#nested />
						</select>
					</div>
				</div>
			<#else>
			<span class="block input-icon input-icon-right">
				<input min="" max="" minlength="0" length="${length!}" vtype="" maxlength="${maxLength!}" nullable="${nullable!}" regex="${regexStr!}" regextip="${regexTip!}" type="${type!}" id="${id!}" oid="${id!}" placeholder="${labelText!}" class="form-control col-xs-10 col-sm-10 col-md-10 " value="${value!}">
			</span>
			</#if>
		</div>
</div>
</#macro>

<#macro btnList value="" class="" title="修改" type="edit" color="green">
	<a href="javascript:;" othertext="11" value="${value!}" id="btn_${type}_${value!}" class="${color!} bigger-140 btn-${type!} ${class!}" title="${title!}" type="${type!}">
		<i class="ace-icon fa fa-<#if type=="edit">pencil<#elseif type=="delete">trash-o</#if>"></i>
	</a>
</#macro>

<#macro searchItem label="" type="text" id="" nullable="true" name="" class="form-control" itemClass="">
	<div class="filter-item ${itemClass!}">
		<label for="" class="filter-name">${label!}：</label>
		<div class="filter-content">
			<#if type=="text">
				<input type="${type!}" id="${id}"  nullable="${nullable!}" name="${name!}" class="${class!}">
			<#elseif type="select">
				
			<#elseif type="nested">
				<#nested />
			</#if>
		</div>
	</div>
</#macro>

<#macro select options=[] class="" id="" onchange="" val="">
	<select class="form-control ${class!}" id="${id!}" onChange="${onchange!}">
		<#nested />
		<#list options as x>
			<option value="${x.id}" <#if val=x.id>selected</#if>><span>${x.name!}</span></a>
		</#list>sd
	</select>
</#macro>
