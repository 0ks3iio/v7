<#import "/fw/macro/htmlcomponent.ftl" as htmlcom>
<table class="table table-bordered table-striped table-hover" id="showtable${viewType!}">
	<tbody>
	<#if examInfoList?exists&& (examInfoList?size > 0)>
        <thead>
			<tr>
				<th class="">序号</th>
				<th class="">考试名称</th>
				<th class="">年级</th>
				<th class="">不排考设置</th>
				<th class="">考号设置</th>
				<th class="">考试场地设置</th>
				<th class="">考场分配设置</th>
				<th class="">考场考生设置</th>
			</tr>
        </thead>
	    <#list examInfoList as item>
		<tr>
			<td class="">${item_index+1}</td>
			<td class="">${item.examName!}</td>
			<td class="">${item.gradeCodeName!}</td>
			
			<td class=""><a href="javascript:" onclick="doSetExamItem('${item.id}','1')" class="color-blue">去设置</a></td>
			<td class=""><a href="javascript:" onclick="doSetExamItem('${item.id}','7')" class="color-blue">去设置</a></td>
			<td class=""><a href="javascript:" onclick="doSetExamItem('${item.id}','2')" class="color-blue">去设置</a></td>
			<td class="">
				<#if item.isgkExamType?default("0")=="1">
					<a href="javascript:" onclick="doSetExamItem('${item.id}','8')" class="color-blue">去设置</a>
				<#else>--</#if>
			</td>
			<td class=""><a href="javascript:" onclick="doSetExamItem('${item.id}','3')" class="color-blue">去设置</a></td>
		</tr>
		</#list>
	<#else><!--text-center-->
        <div class="no-data-container">
            <div class="no-data">
				<span class="no-data-img">
					<img src="${request.contextPath}/static/images/public/nodata6.png" alt="">
				</span>
                <div class="no-data-body">
                    <p class="no-data-txt">暂无相关数据</p>
                </div>
            </div>
        </div>
	</#if>												
	</tbody>
</table>	
<script type="text/javascript">
<#if viewType=="1"> 
 $(function(){
//   	  var table = $('#showtable1').DataTable( {
//	        scrollY: "148px",
//			info: false,
//			searching: false,
//			autoWidth: false,
//			sort: false,
//	        scrollCollapse: true,
//	        paging: false,
//	        language:{
//	        		emptyTable: '没有数据',
//	        		loadingRecords: '加载中...',
//	        	}
//	    } );
   });
</#if>
function doSetExamItem(examId,type){
	var url =  '${request.contextPath}/exammanage/examArrange/examItemIndex/page?examId='+examId+"&type="+type;
	$("#examArrangeDiv").load(url);	
}
</script>
