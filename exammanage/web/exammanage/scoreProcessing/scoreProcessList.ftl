<#import "/fw/macro/htmlcomponent.ftl" as htmlcom>
<table class="table table-bordered table-striped table-hover" id="showtable${viewType!}">
	<thead>
		<tr>
			<th class="text-center">序号</th>
			<th class="text-center">考试名称</th>
			<th class="text-center">年级</th>
			<th class="text-center">录分权限</th>
			<th class="text-center">成绩录入</th>
		</tr>
	</thead>
	<tbody>
	<#if examInfoList?exists>
	    <#list examInfoList as item>
		<tr>
			<td class="text-center">${item_index+1}</td>
			<td class="text-center">${item.examName!}</td>
			<td class="text-center">${item.gradeCodeName!}</td>
			
			<td class="text-center"><a href="javascript:" onclick="doSetScoreItem('${item.id}','1')" class="table-btn show-details-btn">去设置</a></td>
			<td class="text-center"><a href="javascript:" onclick="doSetScoreItem('${item.id}','2')" class="table-btn show-details-btn">去录入</a></td>
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
function doSetScoreItem(examId,type){
	var url =  '${request.contextPath}/exammanage/scoreProcessing/scoreItemIndex/page?examId='+examId+"&type="+type;
	$("#scoreProcessDiv").load(url);	
}
</script>
