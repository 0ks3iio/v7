<div class="form-group">
<label class="col-sm-2 control-label no-padding-right">科目：</label>
<div class="col-sm-9">
	<#if compreRelationshipList?exists && (compreRelationshipList?size > 0)>
		<#list compreRelationshipList as courseList>
			<label><input id="${courseList.relationshipValue!}" <#if courseIds?index_of(courseList.relationshipValue) != -1>checked</#if> type="checkbox" name="couList" class="wp" value="${courseList.relationshipValue!}"><span class="lbl">${courseList.relationshipName!}</span></label>
		</#list>
	</#if>
</div>
</div>
