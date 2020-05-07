 <#if data?exists && data?size gt 0>
    <#list data as homework>
        <tr>
			<td>
			   <#if homework.isSubmit == "NO">
			      <span class="label label-yellow">未完成</span>
			   <#elseif homework.isSubmit == "YES">
			      <span class="label label-green">已完成</span>
			   </#if>
			</td>
			<td>
				<a href="javascript:void(0);" onclick="openHomeWork('${homework.homeWorkUrl!}');">${homework.caseName!}</a>
			</td>
			<td>${homework.showSendTime!}</td>
			<td>${homework.showEndTime!}</td>
	    </tr>
    </#list>
 <#else>
 	 
 </#if>

 <script>
 
   function openHomeWork(homeWorkUrl){
     
      window.open(homeWorkUrl,'','fullscreen,scrollbars,resizable=yes,toolbar=no');
    }
 </script>