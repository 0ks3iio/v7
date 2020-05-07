<#import "/fw/macro/htmlcomponent.ftl" as htmlcom>
<table class="table table-bordered table-striped table-hover" id="showtable${viewType!}">
	<thead>
		<tr>
			<th class="text-center">序号</th>
			<th class="text-center">考试名称</th>
			<th class="text-center">考试类型</th>
			<th class="text-center">年级</th>
			<th class="text-center">考区设置</th>
			<th class="text-center">参考人数分配</th>
			<th class="text-center">座位编排</th>
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
			
			<td class="text-center"><a href="javascript:" onclick="doSetExamItem('${item.id}','1')" class="table-btn show-details-btn">去设置</a></td>
			<td class="text-center"><a href="javascript:" onclick="doSetExamItem('${item.id}','2')" class="table-btn show-details-btn">去设置</a></td>
			<td class="text-center"><a href="javascript:" onclick="doSetExamItem('${item.id}','3')" class="table-btn show-details-btn">去设置</a></td>
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
function doSetExamItem(examId,type){
	var url =  '${request.contextPath}/exammanage/edu/examArrange/examItemIndex/page?examId='+examId+"&type="+type;
	$("#examArrangeDiv").load(url);	
}
</script>
