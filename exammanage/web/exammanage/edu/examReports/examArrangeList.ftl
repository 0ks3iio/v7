<#import "/fw/macro/htmlcomponent.ftl" as htmlcom>
<table class="table table-bordered table-striped table-hover" id="showtable${viewType!}">
	<thead>
		<tr>
			<th class="text-center">序号</th>
			<th class="text-center">考试名称</th>
			<th class="text-center">考试类型</th>
			<th class="text-center">年级</th>
			<th class="text-center">考场门贴</th>
			<th class="text-center">考场对照单</th>
			<th class="text-center">考生桌贴</th>
			<th class="text-center">考生准考证</th>
		</tr>
	</thead>
	<tbody>
	<#if examInfoList?exists>
	    <#list examInfoList as item>
		<tr>
			<td class="text-center">${item_index+1}</td>
			<td class="text-center">${item.examName!}</td>
			<td class="text-center">${mcodeSetting.getMcode("DM-KSLB", item.examType?default(1)?string)}</td>
			<td class="text-center">${item.gradeCodeName!}</td>
			<td class="text-center"><a href="javascript:" onclick="doExamReports('${item.id}','1')" class="table-btn show-details-btn">去查看</a></td>
			<td class="text-center"><a href="javascript:" onclick="doExamReports('${item.id}','2')" class="table-btn show-details-btn">去查看</a></td>
			<td class="text-center"><a href="javascript:" onclick="doExamReports('${item.id}','3')" class="table-btn show-details-btn">去查看</a></td>
			<td class="text-center"><a href="javascript:" onclick="doExamReports('${item.id}','4')" class="table-btn show-details-btn">去查看</a></td>
		</tr>
		</#list>
	</#if>												
	</tbody>
</table>	
<script type="text/javascript">
<#if viewType=="1"> 
 $(function(){
   	  var table = $('#showtable1').DataTable( {
	        scrollY: "148px",
			info: false,
			searching: false,
			autoWidth: false,
			sort: false,
	        scrollCollapse: true,
	        paging: false,
	        language:{
	        		emptyTable: '没有数据',
	        		loadingRecords: '加载中...', 
	        	}
	    } );
   });
</#if>
function doExamReports(examId,type){
	var url =  '${request.contextPath}/exammanage/edu/examReports/examItemIndex/page?examId='+examId+"&type="+type;
	$("#examArrangeDiv").load(url);	
}
</script>
