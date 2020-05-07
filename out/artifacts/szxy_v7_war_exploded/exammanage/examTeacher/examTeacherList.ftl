<#import "/fw/macro/htmlcomponent.ftl" as htmlcom>
<#if examInfoList?exists&& (examInfoList?size > 0)>
<table class="table table-bordered table-striped table-hover" id="showtable${viewType!}">
	<thead>
		<tr>
			<th >序号</th>
			<th >考试名称</th>
			<th >年级</th>
			<th >巡考老师设置</th>
			<th >监考老师设置</th>
			<th >校外老师设置</th>
		</tr>
	</thead>
	<tbody>
	    <#list examInfoList as item>
		<tr>
			<td >${item_index+1}</td>
			<td >${item.examName!}</td>
			<td >${item.gradeCodeName!}</td>
			
			<td ><a href="javascript:" onclick="doSetExamItem('${item.id}','4')" class="color-blue">去设置</a></td>
			<td ><a href="javascript:" onclick="doSetExamItem('${item.id}','5')" class="color-blue">去设置</a></td>
			<td ><a href="javascript:" onclick="doSetExamItem('${item.id}','6')" class="color-blue">去设置</a></td>
		</tr>
		</#list>
	</tbody>
</table>
<#else >
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
	var url =  '${request.contextPath}/exammanage/examTeacher/examItemIndex/page?examId='+examId+"&type="+type;
	$("#examTeacherDiv").load(url);	
}
</script>
