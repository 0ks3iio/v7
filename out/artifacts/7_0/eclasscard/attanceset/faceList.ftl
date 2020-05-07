<#import "/fw/macro/htmlcomponent.ftl" as htmlcom>
	<div class="table-container" id="stuHonorListTable">
		<div class="table-container-body">
			<table class="table table-bordered table-striped">
				<thead>
					<tr>
						<th width="5%">序号</th>
						<th width="10%">姓名</th>
						<th width="15%">学号</th>
						<th width="10%">性别</th>
						<th width="15%">班级</th>
						<th >照片</th>
						<th width="8%">备注</th>
						<th width="8%">操作</th>
					</tr>
				</thead>
				<tbody>
				<#if returnDtos?exists&&returnDtos?size gt 0>
					<#list returnDtos as stuInfo>
					<tr>
						<td>${stuInfo_index+1}</td>
						<td>${stuInfo.studentName!}</td>
						<td>${stuInfo.studentCode!}</td>
						<td>
						<#if stuInfo.sex?exists>
							<#if stuInfo.sex==1>男
							<#else>女
							</#if>
						<#else>未设置
						</#if>
						</td>
						<td>${stuInfo.className!}</td>
						<td>
						<#if stuInfo.showPictrueUrl?exists>
						<img width="90" height="100" 
						src="${request.contextPath}${stuInfo.showPictrueUrl!}" 
						alt=""></td>
						</#if>
						<td><#if stuInfo.failTimes gt 2>客户端注册失败，建议重传</#if></td>
						<td>
							<a href="javascript:void(0);" onclick="uploadParamPage('2','${stuInfo.id!}')"><#if stuInfo.showPictrueUrl?exists>重传<#else>上传</#if></a>
							<#if stuInfo.showPictrueUrl?exists>
								<a href="javascript:void(0);" onclick="deletePicture('${stuInfo.id!}',this)">删除</a>
							</#if>
						</td>
					</tr>
					</#list>
				<#else>
				<tr>
					<td colspan="7" align="center">暂无数据</td>
				</tr>
				</#if>
				</tbody>
			</table>
		</div>
	</div>
<#if returnDtos?exists&&returnDtos?size gt 0>
	<@htmlcom.pageToolBar container="#showStuListDiv" class="noprint"/>
</#if>
<div id="uploadDiv-show">

</div>
<script>
$(function(){
	showNotUpload();
})

function showNotUpload() {
	var htmlstr = '<span class="filter-name">${wschName!}</span>';
	htmlstr+= '<div class="filter-content color-red">${wschNum!}</div>'
	$("#showNotUpload").html(htmlstr);
}
function editFace(id) {
	url =  '${request.contextPath}/eclasscard/face/edit?studentId='+id;
	$("#showStuListDiv").load(url);
}
</script>