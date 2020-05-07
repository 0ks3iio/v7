<#import "/fw/macro/htmlcomponent.ftl" as htmlcom>
<div class="table-container">
	<div class="table-container-header">共${countList!}份结果</div>
	<div class="table-container-body print">
		<table class="table table-bordered table-striped table-hover">
			<thead>
				<tr>
					<th>序号</th>
					<th>学生</th>
					<th>学号</th>
					<th>原行政班</th>
					<th width="30%">现所属班级</th>
					<#list 0..(weekDays-1) as day>
					<th>${dayOfWeekMap2[day+""]!}</th>
					</#list>
					<th class="noprint">操作</th>
				</tr>
			</thead>
			<tbody>
			<#if studentClassDtoList?exists && studentClassDtoList?size gt 0>
			    <#list studentClassDtoList as item>
			         <tr>
					     <td>${item_index+1!}</td>
					     <td>${item.studentName!}</td>
					     <td>${item.studentCode!}</td>
					     <td>${item.yClassName!}</td>
					     <td>${item.newClassName!}</td>
					     <#list 0..(weekDays-1) as day>
						     <td>${item.arr[day]!}</td>
					     </#list>
					     
					     <td class="noprint"><a href="javascript:showDetail('${item.studentId!}','${item.studentName!}')">查看详情</a></td>
				     </tr>
			    </#list>
			</#if>											
			</tbody>
		</table>
		<#if studentClassDtoList?exists && studentClassDtoList?size gt 0>
  	        <@htmlcom.pageToolBar container="#tableList2" class="noprint"/>
         </#if>
	</div>
</div>
<script>
function showDetail(studentId,studentName){
    var url = '${request.contextPath}/newgkelective/${arrayId!}/arrayResult/studentClassResultDetailLeft?studentId='+studentId+'&studentName='+studentName;
	$("#tableList").load(encodeURI(url));
}
</script>