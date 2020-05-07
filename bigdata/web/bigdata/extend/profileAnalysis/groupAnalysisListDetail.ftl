<#list userList as item>
    <tr>
    		<#assign per =100/(cols?size) >
    		<#list cols as col>
	        <td title="${item[col.columnName]!}" >
	        	<div style="width: 90%;" class="ellipsis">
	                        ${item[col.columnName]!}
	                    </div>
	        </td>
            </#list>
    </tr>
</#list>
<#if userList?size ==0>
                                        <tr>
                                            <td colspan="88" align="center">
                                                暂无结果数据
                                            </td>
                                        </tr><tr>
</tr>
</#if>
<input type="hidden" id="pageIndex" value="${pageIndex!}">
<input type="hidden" id="count" value="${count!}">