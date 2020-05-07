<#if clsList?exists && clsList?size gt 0>
<div class="table-container">
<div class="table-container-body"  style="max-height:500px;overflow:auto;">
<table class="table table-bordered table-striped">
	<thead>
		<tr>
			<th colspan="3">考核项目</th>
			<#list clsList as cls>
			<th>${cls.classNameDynamic!}</th>
			</#list>
		</tr>
	</thead>
	<tbody>
	<#assign weeks = [[7,'周日'],[1,'周一'],[2,'周二'],[3,'周三'],[4,'周四'],[5,'周五'],[6,'周六'],[8,'']] />
		<#list [1,2] as statType>
			<#assign rp= wsCount?default(1) />
			<#if statType == 2>
			<#assign rp= jlCount?default(1) />
			</#if>
		<tr>
		<td rowspan="${rp?default(1)}"><#if statType==1>卫生情况<#else>纪律情况</#if></td>
			<#list weeks as wk>
				<#assign wkrp = 1 />
				<#assign wkDtos = typeMap[statType+'-'+wk[0]] />
				<#if wkDtos?exists && wkDtos?size gt 0>
				<#assign wkrp = wkDtos?size />
				<#assign hasItem = true />
				</#if>
				<td style="white-space: nowrap" rowspan="${wkrp?default(1)}">${wk[1]}</td><#--周次-->
				<#assign hasVal = false />
			<#if hasItem?default(false)>
				<#list wkDtos as wd>
					<#assign hasVal = true />
					<td style="white-space: nowrap">${wd.itemName!}</td><#--项目名称-->
					<#list clsList as cls>
					<td <#if wd.itemName?default('')=='红旗'>class="color-red"</#if>>${(scoreMap[wd.itemId+cls.id+wk[0]]?default(""))}${(remarkMap[wd.itemId+cls.id+wk[0]]?default(""))?default("")}</td>
					</#list>
					<#if wd_has_next>
					</tr>
					<tr>
					</#if>
				</#list>
			</#if>
			<#if !hasItem?default(false) || !hasVal?default(false)>
				<td></td>
				<#list clsList as cls>
				<td></td>
				</#list>
			</#if>
			<#if wk_has_next>
			</tr>
			<tr>
			</#if>
			</#list>
		</tr>	
		</#list>
	</tbody>
</table>
</div>
</div>
<#else>
<div class="no-data-container">
	<div class="no-data">
		<span class="no-data-img">
			<img src="${request.contextPath}/static/images/public/nodata6.png" alt="">
		</span>
		<div class="no-data-body">
			<p class="no-data-txt">没有班级信息</p>
		</div>
	</div>
</div>
</#if>