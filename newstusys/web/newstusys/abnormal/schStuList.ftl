<#if stus?exists && stus?size gt 0>
<table class="table table-bordered table-striped table-hover no-margin">
	<thead>
		<tr>
			<th>序号</th>
			<th>学生姓名 </th>
			<th>性别</th>
			<th>班级</th>
			<th>班内编号</th>
			<th>学号/学籍铺号</th>
			<th>身份证号</th>
			<th>操作</th>
		</tr>
	</thead>
	<tbody>
		<#list stus as stu>
		<tr>
			<td>${stu_index+1}</td>
			<td>${stu.studentName!}</td>
			<td>${(mcodeSetting.getMcode("DM-XB", stu.sex?default(0)?string))?if_exists}</td>
			<td>${stu.className!}</td>
			<td>${stu.classInnerCode!}</td>
			<td>${stu.studentCode!}</td>
			<td>${stu.identityCard!}</td>
			<td><a class="color-blue table-btn js-out" href="javascript:;" onclick="toOutPage('${stu.id!}');"><#if deploy?default('')=='hangwai'>学籍减少<#else>转出</#if></a></td>
		</tr>
		</#list>
	</tbody>
</table>
<#else>
<div class="no-data-container">
	<div class="no-data">
		<span class="no-data-img">
			<img src="${request.contextPath}/static/images/public/nodata6.png" alt="">
		</span>
		<div class="no-data-body">
			<p class="no-data-txt">没有相关数据</p>
		</div>
	</div>
</div>
</#if>
<script>
function toOutPage(stuId){
	var url='${request.contextPath}/newstusys/abnormal/out/page?studentId='+stuId;
	indexDiv = layerDivUrl(url,{title: "<#if deploy?default('')=='hangwai'>学籍减少<#else>转出</#if>",width:600,height:500});
}
</script>