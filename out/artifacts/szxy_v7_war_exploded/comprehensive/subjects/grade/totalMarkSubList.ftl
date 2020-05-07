<#if courseList?exists && (courseList?size > 0)>
	<#list courseList as subList>
		<label><input id="${subList.id!}" type="checkbox" name="subList" <#if haveData?index_of(subList.id) != -1>disabled</#if> <#if subIds?index_of(subList.id) != -1>checked</#if> class="wp" value="${subList.id!}"><span class="lbl">${subList.subjectName!}</span></label>
	</#list>
</#if>
