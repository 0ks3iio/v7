<h3 class="gk-copy-header">
	<label> 考点</span></label>
</h3>
<div class="tab-content">
	<div id="aaa" class="tab-pane active" role="tabpanel">
		<ul class="gk-copy-list">
		<#if emOptionList?exists && emOptionList?size gt 0>
		<#list emOptionList as item>
			<li><label><input type="radio" name="myOption" class="wp" value="${item.id!}"><span class="lbl">${item.optionName!}</span></label></li>
		</#list>
		</#if>
		</ul>
	</div>
	<div id="bbb" class="tab-pane" role="tabpanel"></div>
</div>
