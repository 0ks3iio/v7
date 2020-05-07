<div class="detail-div">
	<div class="filter">
		<div class="filter-item">
			<span class="filter-name">班级：</span>
			<div class="filter-content">
				<p>${className!}</p>
			</div>
		</div>
		<a href="javascript:" class="btn btn-blue pull-right back-btn detaileBack">返回</a>
	</div>
	<table class="table table-striped table-hover">
		<thead>
			<tr>
				<th>学号</th>
				<th>姓名</th>
				<th>性别</th>
			</tr>
		</thead>
		<tbody>
		<#if stuList?exists && stuList?size gt 0>
		<#list stuList as stu>
			<tr>
				<td>${stu.studentCode!}</td>
				<td>${stu.studentName!}</td>
				<td>${mcodeSetting.getMcode("DM-XB","${stu.sex!}")}</td>
			</tr>
		</#list>
		</#if>
		</tbody>
	</table>
</div>
<script>
<#if !stuList?exists || stuList?size == 0>
	layerTipMsgWarn("提示","该班级下已经没有学生了");
	findByCondition();
</#if>
$(function(){
	$('.detaileBack').on("click",function(){
		findByCondition();
	});
});


</script>