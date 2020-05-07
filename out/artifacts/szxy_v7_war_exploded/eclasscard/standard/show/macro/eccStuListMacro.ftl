<!-- S 学生列表 -->
<#macro tableSeat rowNumbers colNumbers spaceNoArr>
<div class="checkin-stu-list">
	<div class="container-parent">
		<div class="gradient"></div>
	    <div class="container-list">
	    	<table width="100%">
                <tbody>
                	<#list rowNumbers..1 as rowItem>
                		<tr class="container-item">
                			<#if rowItem_index==0>
                			 	<td rowspan="${rowItem}" class="col_umber">
                                    <ul>
                                    	<#list rowNumbers..1 as rItem>
                                        <li>${rItem}</li>
                                        </#list>
                                    </ul>
                               	</td>
                             </#if>
	    				<#list 1..colNumbers as colItem>
	    					<td data-row-col="${rowItem}_${colItem}">
	    						<a class="js-openCheckin" href="javascript:;">
	    						
	    						</a>
	    					</td>
	    					<#if spaceNoArr?exists && spaceNoArr?size gt 0 && rowItem_index==0>
			    				<#list spaceNoArr as space>
			    					<#if space==colItem+"">
			    						<td rowspan="${rowItem}" class="space-td">
				                            <p>过</p>
				                            <p>道</p>
				                        </td>
			    						<#break>
			    					</#if>
			    				</#list>
			    			</#if>
	    				</#list>
	    				</tr>
	    			</#list>
                </tbody>
            </table>
	    	
	    </div>
	</div>
</div>
<div class="desktop-footer">
    <div class="desktop-bottom">
        <div class="desktop-rostrum"><span>讲</span><span>台</span></div>
    </div>
</div>
<script>
	//增加数字列
	function addNumberli(rowNumbers){
		var hh='<ul>';
		for(var k=rowNumbers;k>0;k--){
			hh=hh+'<li>'+k+'</li>';
		}
		hh=hh+'</ul>';
		return hh;
	}
	
	function addFirstRow(rowOtherIndexNum){
		var trHtml='<tr class="container-item"><td class="col_umber"></td>';
		<#list 1..colNumbers as colItem>
			trHtml=trHtml+'<td data-row-col="other_'+rowOtherIndexNum+'_${colItem}"><a class="js-openCheckin" href="javascript:;"></a></td>';
			<#if spaceNoArr?exists && spaceNoArr?size gt 0>
				<#list spaceNoArr as space>
					<#if space==colItem+"">
						trHtml=trHtml+'<td class="space-td"></td>';
						<#break>
					</#if>
				</#list>
			</#if>
		</#list>
		trHtml=trHtml+'</tr>';
		return trHtml;
	}
	
	function initCols(rowNumbers){
		//有添加
		$('.container-parent table').find("tr:eq(0)").find(".col_umber").attr('rowspan',rowNumbers);
		$('.container-parent table').find("tr:eq(0)").find(".col_umber").append(addNumberli(rowNumbers));
		$('.container-parent table').find("tr:not(:eq(0))").find(".col_umber").remove();
		$('.container-parent table').find("tr:eq(0)").find(".space-td").attr('rowspan',rowNumbers);
		$('.container-parent table').find("tr:eq(0)").find(".space-td").append('<p>过</p><p>道</p>');
		$('.container-parent table').find("tr:not(:eq(0))").find(".space-td").remove();
	}
</script>
</#macro>