<table class="table">
<thead>
	<tr>
		<th width="10%">考核类目</th>
		<th width="20%">考核项目</th>
		<th style="word-wrap:break-word;" width="10%">周日</th>
		<th style="word-wrap:break-word;" width="10%">周一</th>
		<th style="word-wrap:break-word;" width="10%">周二</th>
		<th style="word-wrap:break-word;" width="10%">周三</th>
		<th style="word-wrap:break-word;" width="10%">周四</th>
		<th style="word-wrap:break-word;" width="10%">周五</th>
		<th style="word-wrap:break-word;" width="10%">周六</th>
	</tr>
</thead>
<tbody>
<#assign days = [7,1,2,3,4,5,6]>
<#if resultDto1?exists && resultDto1?size gt 0>
    <#list resultDto1 as dto>
    <tr>
		<#if dto_index==0><td rowspan="${resultDto1?size}">卫生</td></#if>
		<td>${dto.itemName!}</td>
		<#list days as day>
		   <td>${(scoreMap[dto.itemId+day]?default(""))}${(remarkMap[dto.itemId+day]?default(""))?default("")}</td>
		</#list>
	</tr>
    </#list>
    <tr>
		<td>总得分</td>
		<td colspan="8">总分：${healthScore!}  排名：${healthRank!} <#if isHealthExcellen>获得小红旗</#if></td>
	</tr>
</#if>
<#if resultDto3?exists && resultDto3?size gt 0>
    <#list resultDto3 as dto>
    <tr>
		<#if dto_index==0><td rowspan="${resultDto3?size+resultDto2?size}">纪律</td></#if>
		<td>${dto.itemName!}</td>
		<#list days as day>
		   <td>
		       <#if dto.itemName=='上课日志'>
		           ${(scoreMap["skrz"+day]?default(""))}
		           ${(remarkMap["skrz"+day]?default(""))?default("")}
		       <#else>
		           ${(scoreMap["wzxrz"+day]?default(""))}
		           ${(remarkMap["wzxrz"+day]?default(""))?default("")}
		       </#if>
		   </td>
		</#list>
	</tr>
    </#list>
</#if>
<#if resultDto2?exists && resultDto2?size gt 0>
    <#list resultDto2 as dto>
    <tr>
		<td>${dto.itemName!}</td>
		<#list days as day>
		   <td>${(scoreMap[dto.itemId+day]?default(""))}${(remarkMap[dto.itemId+day]?default(""))?default("")}</td>
		</#list>
	</tr>
    </#list>
    <tr>
		<td>总得分</td>
		<td colspan="8">总分：${disciplineScore!}  排名：${disciplineRank!} <#if isDisciplineExcellen>获得小红旗</#if></td>
	</tr>
</#if>
<#if dtoList?exists && dtoList?size gt 0>
    <tr>
		<td rowspan="4">寝室考核</td>
		<td>表扬</td>
		<#list days as day>
		   <td>${(scoreMap2['1'+day]?default(''))}</td>
		</#list>
	</tr>
	<tr>
		<td>批评</td>
		<#list days as day>
		   <td>${scoreMap2['2'+day]?default('')}</td>
		</#list>
	</tr>
	<tr>
		<td>其他情况</td>
		<#list days as day>
		   <td>${scoreMap2['3'+day]?default('')}</td>
		</#list>
	</tr>
<#else>
    <tr>
		<td rowspan="4">寝室考核</td>
		<td>表扬</td>
		<#list days as day>
		   <td></td>
		</#list>
	</tr>
	<tr>
		<td>批评</td>
		<#list days as day>
		   <td></td>
		</#list>
	</tr>
	<tr>
		<td>其他情况</td>
		<#list days as day>
		   <td></td>
		</#list>
	</tr>
</#if>
</tbody>
</table>

<br><br>
<table class="table">
<thead>
	<tr>
		<th width="7%" rowspan="2">寝室号</th>
		<th width="5%" rowspan="2">得分</th>
		<th width="9%" rowspan="2">文明寝室</th>
		<th width="9%" rowspan="2">奖励得分</th>
		<th colspan="3" style="word-wrap:break-word;" width="10%">周日</th>
		<th colspan="3" style="word-wrap:break-word;" width="10%">周一</th>
		<th colspan="3" style="word-wrap:break-word;" width="10%">周二</th>
		<th colspan="3" style="word-wrap:break-word;" width="10%">周三</th>
		<th colspan="3" style="word-wrap:break-word;" width="10%">周四</th>
		<th colspan="3" style="word-wrap:break-word;" width="10%">周五</th>
		<th colspan="3" style="word-wrap:break-word;" width="10%">周六</th>
	</tr>
	
	<tr>
        <#list days as day>
		   <th>卫生</th>
		   <th>内务</th>
		   <th>纪律</th>
		</#list>
	</tr>
</thead>
<tbody>
<#if roomList?exists && roomList?size gt 0>
      <#list roomList as room>
      <tr>
		<td>${room.roomName!}</td>
		<td>${(statResultMap[room.id].scoreALL)?default(0)?string('0.#')!}</td>
		<td><#if statResultMap[room.id]?exists && statResultMap[room.id].isExcellent?default(false)><font style="color:red">★</font></#if></td>
		<td>${(statResultMap[room.id].excellentScore)?default(0)?string('0.#')!}</td>
		<#list days as day>
		   <td>${(checkResultMap[room.id+day].wsScore)?default(0)?string('0.#')!}</td>
		   <td>${(checkResultMap[room.id+day].nwScore)?default(0)?string('0.#')!}</td>
		   <td>${(checkResultMap[room.id+day].jlScore)?default(0)?string('0.#')!}</td>
		</#list>
	</tr>
    </#list>
</#if>
<tr>
		<td>平均得分</td>
		<td>${allScoreAvg?default(0)?string('0.#')!}</td>
		<td><font style="color:red">/</font></td>
		<td>${excellentScoreAvg?default(0)?string('0.#')!}</td>
		<#list days as day>
		   <td></td>
		   <td></td>
		   <td></td>
		</#list>
	</tr>
</tbody>
</table>
