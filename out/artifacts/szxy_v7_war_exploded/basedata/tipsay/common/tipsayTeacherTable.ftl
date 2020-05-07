<#macro scheduleTable  tableClass tableId weekDayList mm=0 am=0 pm=0 nm=0 showBr=false itemClass="">

<table class="${tableClass!}" id="${tableId!}"  >
	<thead>
        <tr>
          <th class="sort" width="2%">&nbsp;</th>
            <th class="sort" width="3%">&nbsp;</th>
            <#if weekDayList?exists && weekDayList?size gt 0>
			<#list weekDayList as weekDay>
				<th>${weekDay[1]!}</th>
			</#list>
			</#if>
        </tr>
    </thead>
    <tbody>
    	<#if mm gt 0>
			<#list 1..mm as ii>
				
				<tr>
					<#if ii_index==0>
					<td rowspan="${mm}" class="sort"><p>早</p><p>自</p><p>习</p></td>
	            	</#if>
	            	<td class="sort sort_1">${ii}</td>
					<#if weekDayList?exists && weekDayList?size gt 0>
					<#list weekDayList as weekDay>
						<td>
						<span class="item ${itemClass!}" data-user="${weekDay[0]!}_1_${ii}">
		                	
	                	</span>
						</td>
					</#list>
					</#if>
				</tr>	
			</#list>
		</#if>
		<#if showBr && mm gt 0 && am gt 0>
		<tr>
        	<td class="line" colspan="${weekDayList?size+2}"></td>
        </tr>
		</#if>
		<#if am gt 0>
			<#list 1..am as ii>
				<tr>
					<#if ii_index==0>
					<td rowspan="${am}" class="sort"><p>上</p><p>&nbsp;</p><p>午</p></td>
					</#if>
					<td class="sort sort_1">${ii}</td>
					<#if weekDayList?exists && weekDayList?size gt 0>
					<#list weekDayList as weekDay>
						<td>
						<span class="item ${itemClass!}" data-user="${weekDay[0]!}_2_${ii}">
		                	
	                	</span>
						</td>
					</#list>
					</#if>
				</tr>	
			</#list>
		</#if>
		<#if showBr && am gt 0 && pm gt 0>
		<tr>
        	<td class="line" colspan="${weekDayList?size+2}"></td>
        </tr>
		</#if>
		<#if pm gt 0>
			<#list 1..pm as ii>
				
				<tr>
					<#if ii_index==0>
					<td rowspan="${pm}" class="sort"><p>下</p><p>&nbsp;</p><p>午</p></td>
					</#if>
					<td class="sort sort_1">${ii}</td>
					<#if weekDayList?exists && weekDayList?size gt 0>
					<#list weekDayList as weekDay>
						<td>
						<span class="item ${itemClass!}" data-user="${weekDay[0]!}_3_${ii}">
		                	
	                	</span>
						</td>
					</#list>
					</#if>
				</tr>	
			</#list>
		</#if>
		<#if showBr && pm gt 0 && nm gt 0>
		<tr>
        	<td class="line" colspan="${weekDayList?size+2}"></td>
        </tr>
		</#if>
		<#if nm gt 0>
			<#list 1..nm as ii>
				<tr>
					<#if ii_index==0>
					<td rowspan="${nm}" class="sort"><p>晚</p><p>&nbsp;</p><p>上</p></td>
					</#if>
					<td class="sort sort_1">${ii}</td>
					<#if weekDayList?exists && weekDayList?size gt 0>
					<#list weekDayList as weekDay>
						<td>
							<span class="item ${itemClass!}" data-user="${weekDay[0]!}_4_${ii}">
			                
		                	</span>
						</td>
					</#list>
					</#if>
				</tr>	
			</#list>
		</#if>

    </tbody>
</table>
</#macro>