<#if rooms?exists && rooms?size gt 0>
<div class="table-container">
	<div class="table-container-body">
		<table class="table table-bordered table-striped table-hover">
			<thead>
				<tr>
					<th>序号</th>
					<th>考场编号</th>
					<th>所属考点</th>
					<th>容纳考生数</th>
					<th>操作</th>
				</tr>
			</thead>
			<tbody>
				<#list rooms as rm>
				<tr>
					<td>${rm_index+1}</td>
					<td>${rm.roomNo!}</td>
					<td>${rm.schoolName!}</td>
					<td>${rm.perNum?default(0)}</td>
				    <td><a class="color-blue mr10" href="javascript:;" onclick="teaList('${rm.roomNo!}');">查看考生</a><a class="color-blue" href="javascript:;" onclick="combine('${rm.roomNo!}');">合并考场</a></td>
				</tr>
				</#list>
			</tbody>
		</table>
	</div>
</div>
<#else>
<div class="no-data-container">
	<div class="no-data">
		<span class="no-data-img">
			<img src="${request.contextPath}/static/images/public/nodata6.png" alt="">
		</span>
		<div class="no-data-body">
			<p class="no-data-txt">该科目下没有相关数据</p>
		</div>
	</div>
</div>
</#if>
<script>
function combine(rno){
	var sid = $('#subInfoId').val();
	var url = '${request.contextPath}/teaexam/siteSet/setIndex/${examId!}/changeRmList?subInfoId='+sid+'&type=1&oldRoomNo='+rno;
	indexDiv = layerDivUrl(url,{title: "合并考场",width:750,height:650});
}
</script>