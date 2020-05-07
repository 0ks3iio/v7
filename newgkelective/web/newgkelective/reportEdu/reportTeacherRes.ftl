<#import  "/fw/macro/htmlcomponent.ftl" as htmlcomponent />
<div class="picker-table" id="selDiv">
	<table class="table">
		<tbody>			
			<tr id="clazzSearch">
				<th width="150">科目：</th>
				<td>
					<div class="outter">
						<#if xgkCourseList?exists && xgkCourseList?size gt 0>
						<#list xgkCourseList as c>
						<a <#if subjectId == c.id!>class="selected"</#if> href="javascript:" data-value="${c.id!}" onclick="getRes('2','${c.id!}');" id="">${c.subjectName!}</a>
						</#list>
						</#if>
					</div>
				</td>
				<#-- <td width="75" style="vertical-align: top;">
					<div class="outter">
						<a class="picker-more" href="javascript:"><span>展开</span><i class="fa fa-angle-down"></i></a>
					</div>
				</td>  -->
			</tr>
			
		</tbody>
	</table>
</div>
<div class="table-container">
	<div class="table-container-body">
		<table class="table table-bordered table-striped table-hover">
			<thead>
				<tr>
					<th>序号</th>
					<th>学校名称</th>
					<th>所属教育局</th>
					<th>教师数</th>
					<th>学生数</th>
					<th>教学班数</th>
					<th>师生比</th>
					<th>师班比</th>
				</tr>
			</thead>
			<tbody>
			<#if dtoList?exists && dtoList?size gt 0>
			<#list dtoList as dto>
				<tr>
					<td>${dto_index+1}</td>
					<td>${dto.unitName!}</td>
					<td>${dto.parentName!}</td>
					
					<td>${dto.teacherNum!}</td>
					<td>${dto.stuNum???then(dto.stuNum,'--')}</td>
					<td>${dto.jxbNum???then(dto.jxbNum,'--')}</td>
					<td>${(dto.stuNum?? && dto.stuNum gt 0)?then((dto.teacherNum/dto.stuNum)?string("0.##"),'--')}</td>
					<td>${(dto.jxbNum?? && dto.jxbNum gt 0)?then((dto.teacherNum/dto.jxbNum)?string("0.##"),'--')}</td>
				</tr>
			</#list>
			</#if>
			</tbody>
		</table>
	</div>
	<#if pageInfo.itemsNum gt 0>
	<@htmlcomponent.pageToolBar2 pageInfo=pageInfo function="queryRes" allNum=pageInfo.itemsNum/>
	</#if>
</div>

<script>
function queryRes(pageIndex,pageSize){
	var gradeCode = $("#gradeCode").val();
	var url = "${request.contextPath}/newgkelective/edu/baseItem/placeRes?unitId=${unitId!}&gradeYear="+gradeCode;
	var url = "${request.contextPath}/newgkelective/edu/baseItem/teacherRes?unitId=${unitId!}&gradeYear="+gradeCode+"&subjectId=${subjectId!}";
	if(pageIndex){
		url=url+'&pageIndex='+pageIndex;
	}
	if(pageSize && pageSize!=""){
		url=url+'&pageSize='+pageSize;
	}
	$("#aa").load(url);
}
</script>