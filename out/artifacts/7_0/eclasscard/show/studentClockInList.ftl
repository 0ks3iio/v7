<#import "/fw/macro/htmlcomponent.ftl" as htmlcom>
<div class="table-container">
	<div class="table-container-body">
		<table class="table table-striped">
			<thead>
				<tr>
					<th>序号</th>
					<th>刷卡时间</th>
					<th>刷卡班牌场地</th>
					<th>班牌用途</th>
					<th>姓名</th>
					<th>学号</th>
					<th>行政班班级</th>
					<th>寝室号</th>
				</tr>
			</thead>
			<tbody>
			<#if eccClockInList?exists && eccClockInList?size gt 0>
			    <#list eccClockInList as item>
				<tr>
					<td>${item.orderNumber!}</td>
					<td>${item.clockInTime?string("yyyy-MM-dd HH:mm:ss")!}</td>
					<td>${item.placeName!}</td>
					<td>${usedForMap[item.type!]!}</td>
					<td>${item.studentName!}</td>
					<td>${item.studentCode!}</td>
					<td>${item.className!}</td>
					<td>${item.roomName!}</td>
				</tr>
				</#list>
			</#if>													
			</tbody>
	  </table>
	  <#if eccClockInList?exists && eccClockInList?size gt 0>
  	      <@htmlcom.pageToolBar container="#showList" class="noprint"/>
      </#if>	
	</div>
</div>	

<script>
<#if eccClockInList?exists && eccClockInList?size gt 0>
$(function(){
	$('.pagination').attr('class','pagination pull-right pagination-sm');
});
</#if>
</script>									