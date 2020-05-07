<#if teachClassList?exists && (teachClassList?size>0)>
	<#list teachClassList as item>
		<label class="pos-rel">
		     <input name="teachClassId" type="checkbox" <#if tcIds?default('')?index_of(item.id) gte 0>checked="true"</#if> class="wp" value="${item.id!}">
		     <span class="lbl"> ${item.name!}</span>
		</label>
	</#list>
</#if>