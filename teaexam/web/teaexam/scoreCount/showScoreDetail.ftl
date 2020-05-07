<div class="filter">
    <div class="filter-item filter-item-left">
		<span class="filter-name">考试名称：${examName!}</span>
	</div>
	<div class="filter-item filter-item-left">
		<span class="filter-name">学科：</span>
		<div class="filter-content">
			<select name="" id="subjectId" class="form-control" onChange="searchScoreList();">
			<#if subList?exists && subList?size gt 0>
			       <option value="">--全部--</option>
			    <#list subList as item>
			       <option value="${item.id!}" <#if '${item.id}'=='${subjectId!}'>selected="selected"</#if>>${item.subjectName!}</option>
			    </#list>
			<#else>
		        <option value="">--请选择--</option>
		    </#if>
			</select>
		</div>
	</div>
	<div class="filter-item filter-item-left">
		<span class="filter-name">单位：</span>
		<div class="filter-content">
			<select vtype="selectOne" name="" id="schoolId" class="form-control" onChange="searchScoreList();">
		    <#if schlList?exists && schlList?size gt 0>
		        <option value="">--全部--</option>
			    <#list schlList as item>
			       <#if item.id !='00000000000000000000000000000000'>
			       <option value="${item.id!}" <#if '${item.id}'=='${schoolId!}'>selected="selected"</#if>>${item.centerSchoolName!}</option>
			       </#if>
			    </#list>
			<#else>
		        <option value="">--请选择--</option>
		    </#if>
			</select>
		</div>
	</div>
	<div class="filter-item filter-item-right">
		<a class="btn btn-blue" href="#" onClick="doExport2();">导出</a>
	</div>
</div>
<div class="table-container">
	<div class="table-container-body">
		<table class="table table-bordered table-striped table-hover">
			<thead>
				<tr>
					<th>序号</th>
					<th>学科</th>
					<th>单位</th>
					<th>参与考试人数</th>
					<th>优秀人数</th>
					<th>优秀率</th>
					<th>合格人数</th>
					<th>合格率</th>
					<th>不合格人数</th>
					<th>不合格率</th>
					<th>平均分</th>
				</tr>
			</thead>
			<tbody>
			<#if dtoList?exists && dtoList?size gt 0>
			    <#list dtoList as item>
				<tr>
				    <td style="white-space: nowrap">${item_index+1!}</td>
				    <td style="white-space: nowrap">${item.subjectName!}</td>
				    <td>${item.schoolName!}<#if item.centerSch?default(false)><img class="ml10" src="${request.contextPath}/static/images/base/icons/app-icon03.png" onclick="toDetail('${item.schoolId!}','${item.subjectId!}')"></img></#if></td>
				    <td style="white-space: nowrap">${item.countNum!}</td>
				    <td style="white-space: nowrap">${item.yxNum!}</td>
				    <td style="white-space: nowrap">${item.yxPer!?string("#.##")}%</td>
				    <td style="white-space: nowrap">${item.hgNum!}</td>				  
				    <td style="white-space: nowrap">${item.hgPer!?string("#.##")}%</td>
				    <td style="white-space: nowrap">${item.bhgNum!}</td>				 
				    <td style="white-space: nowrap">${item.bhgPer!?string("#.##")}%</td>
				    <td style="white-space: nowrap">${item.avgScore!?string("#.##")}</td>
				</tr>
				</#list>
			<#else>
				<tr>
					<td colspan="11" align="center">暂无数据</td>
				</tr>
			</#if>
			</tbody>
		</table>
	</div>
</div>
<script>
$(function(){
    var classIdSearch = $('#schoolId');
	$(classIdSearch).chosen({
	width:'350px',
	no_results_text:"未找到",//无搜索结果时显示的文本
	allow_single_deselect:true,//是否允许取消选择
	disable_search:false, //是否有搜索框出现
	search_contains:true,//模糊匹配，false是默认从第一个匹配
	//max_selected_options:1 //当select为多选时，最多选择个数
	});
})

function toDetail(csId, subId){
	var url = "${request.contextPath}/teaexam/scoreCount/centerSchool/list?examId=${examId!}&cenSchId="+csId+"&subjectId="+subId;
    $('#scoreTabDiv').load(url);
}

function searchScoreList(){
    var schoolId = $('#schoolId').val();
    var subjectId = $('#subjectId').val();
    var url = "${request.contextPath}/teaexam/scoreCount/showScoreDetail?examId=${examId!}&schoolId="+schoolId+"&subjectId="+subjectId;
    $('#scoreTabDiv').load(url);
}

function doExport2(){
    var schoolId = $('#schoolId').val();
    var subjectId = $('#subjectId').val();
    document.location.href="${request.contextPath}/teaexam/scoreCount/exportdetail?examId=${examId!}&schoolId="+schoolId+"&subjectId="+subjectId;
}
</script>