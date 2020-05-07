<#import "/fw/macro/htmlcomponent.ftl" as htmlcom>
<table class="table table-striped table-hover no-margin" style="border-top: 1px solid #ddd;">
    <tbody>
    	<tr>
            <th>序号</th>
            <th>姓名</th>
            <th>班级</th>
            <th>品德表现等级</th>
            <th>品德表现折分</th>
            <th>学生干部折分</th>
            <th>社团折分</th>
        	<th>社会实践折分</th>
        	<th>其它奖励折分</th>
            <th>处罚折分</th>
        </tr>
        <#if stuList?exists && stuList?size gt 0>
        <#list stuList as item>
        	<#assign dto=dtoMap[item.id]?default("")>
	        <tr>
	            <td>${item_index+1}</td>
	            <td>${item.studentName!}</td>
	            <#assign className=classNameMap[item.classId]?default("")>
	            <td>${className!}</td>
	            <#if dto?exists && dto!="">
		            <td>${dto.evatr!}</td>
		            <td>${dto.evaScore?default(0.0)?string("0.#")}</td>
	            	<td>${dto.stuPerScore?default(0.0)?string("0.#")}</td>
	            	<td>${dto.groupScore?default(0.0)?string("0.#")}</td>
	            	<td>${dto.tryScore?default(0.0)?string("0.#")}</td>
	            	<td>${dto.otherScore?default(0.0)?string("0.#")}</td>
	            	<td>${dto.punishScore?default(0.0)?string("0.#")}</td>
            	<#else>
	            	<td></td><td></td>
	            	<td></td><td></td>
	            	<td></td><td></td>
	            	<td></td>
            	</#if>
	            
	        </tr>
	    </#list>
	    <#else>
			<tr>
				<td  colspan="88" align="center">
				暂无数据
				</td>
			<tr>
        </#if>
    </tbody>
</table>
<#if stuList?exists&&stuList?size gt 0>
	<@htmlcom.pageToolBar container="#showListDiv" class="noprint"/>
</#if>
<script>
</script>
