<#import "/fw/macro/htmlcomponent.ftl" as htmlcom>
<table class="table table-striped table-hover no-margin" style="border-top: 1px solid #ddd;">
    <tbody>
    	<tr>
            <th style="width: 50px;">序号</th>
            <th style="width: 60px;">姓名</th>
            <th style="width: 75px;">班级</th>
            <th>艺术节个人折分</th>
            <th>艺术节团体折分</th>
            <th>体育节个人折分</th>
            <th>体育节团体折分</th>
            <th>外语节个人折分</th>
            <th>外语节团体折分</th>
            <th>科技节个人折分</th>
            <th>科技节团体折分</th>
            <th>文化节个人折分</th>
            <th>文化节团体折分</th>
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
		            <td>${dto.yPerScore?default(0.0)?string("0.#")}</td>
		            <td>${dto.yGroupScore?default(0.0)?string("0.#")}</td>
		            <td>${dto.tPerScore?default(0.0)?string("0.#")}</td>
	            	<td>${dto.tGroupScore?default(0.0)?string("0.#")}</td>
	            	<td>${dto.wPerScore?default(0.0)?string("0.#")}</td>
	            	<td>${dto.wGroupScore?default(0.0)?string("0.#")}</td>
	            	<td>${dto.kPerScore?default(0.0)?string("0.#")}</td>
	            	<td>${dto.kGroupScore?default(0.0)?string("0.#")}</td>
	            	<td>${dto.wenPerScore?default(0.0)?string("0.#")}</td>
	            	<td>${dto.wenGroupScore?default(0.0)?string("0.#")}</td>
            	<#else>
	            	<td></td><td></td>
	            	<td></td><td></td>
	            	<td></td><td></td>
	            	<td></td><td></td>
	            	<td></td><td></td>
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
