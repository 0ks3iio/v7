<#import "/fw/macro/htmlcomponent.ftl" as htmlcom>
<form id="checkForm">
<div class="filter">
	<#--<div class="filter-item" id="dateDiv">
		<span class="filter-name">日期：</span>
		<div class="filter-content">
			<div class="input-group">
				<input class="form-control date-picker startTime-date date-picker-time" value="${attDto.searchDate?string("yyyy-MM-dd")}" vtype="data" style="width: 120px" type="text"  name="searchDate" id="searchDate"  onchange="doSearch();">
				<span class="input-group-addon">
					<i class="fa fa-calendar"></i>
				</span>
			</div>
		</div>
	</div>-->
	<div class="filter-item">
		<span class="filter-name">年级：</span>
		<div class="filter-content">
			<select name="gradeId" id="gradeId" class="form-control" onchange="doSearch()" style="width:120px">
				<option value="">---请选择---</option>
				<#if gradeList?exists && gradeList?size gt 0>.
					<#list gradeList as item>
						<option value="${item.id!}" <#if item.id==attDto.gradeId?default("")>selected="selected"</#if> >${item.gradeName!}</option>
					</#list>
				</#if>
			</select>
		</div>
	</div>
	<div class="filter-item">
		<span class="filter-name">班级：</span>
		<div class="filter-content">
			<select name="classId" id="classId" class="form-control" onchange="doSearch()" style="width:120px">
				<option value="">---请选择---</option>
				<#if clazzList?exists && clazzList?size gt 0>.
					<#list clazzList as item>
						<option value="${item.id!}" <#if item.id==attDto.classId?default("")>selected="selected"</#if> >${item.classNameDynamic!}</option>
					</#list>
				</#if>
			</select>
		</div>
	</div>
	<div class="filter-item">
		<span class="filter-name">状态：</span>
		<div class="filter-content">
			<select name="attStatus" id="attStatus" class="form-control" onchange="doSearch()">
				<option value=0>---请选择---</option>
				<option value=1 <#if 1==attDto.attStatus?default("")>selected="selected"</#if>>在校</option>
				<option value=2 <#if 2==attDto.attStatus?default("")>selected="selected"</#if>>离校</option>
			</select>
		</div>
	</div>
</div>
</form>

<div class="table-container">
	<div class="table-container-body">
	<table class="table table-striped">
		<thead>
			<tr>
				<th>序号</th>
				<th>姓名</th>
				<th>学号</th>
				<th>班级</th>
				<th>班主任</th>
				<th>年级</th>
				<th>刷卡时间</th>
				<th>状态</th>
			</tr>
		</thead>
		<tbody>
			<#if gateList?exists && gateList?size gt 0>
					<#list gateList as item>
						<tr>
							<td>${item_index+1}</td>
							<td>${item.studentName!}</td>
							<td>${item.studentCode!}</td>
							<td>${item.gradeName!}${item.className!}</td>
							<td>${item.teacherName!}</td>
							<td>${item.gradeName!}</td>
							<td>${(item.clockInTime?string("yyyy-MM-dd HH:mm:ss"))!}</td>
							<td><#if 1==item.status?default(0)>在校<#else><span class="color-red">离校</span></#if></td>
						</tr>
					</#list>
				<#else>
					<tr>
						<td colspan="8" align="center">暂无数据</td>
					</tr>
				</#if>
		</tbody>
	</table>
	<@htmlcom.pageToolBar container="#itemShowDivId">
	</@htmlcom.pageToolBar>
	</div>
</div>
<script>
	$(function(){
		<#-- //初始化日期控件
			var viewContent={
				'format' : 'yyyy-mm-dd',
				'minView' : '2',
				'endDate' : '${.now?string('yyyy-MM-dd')}',
				'startDate' : '${beginDate?string('yyyy-MM-dd')}'
			};
			initCalendarData("#dateDiv",".date-picker",viewContent);-->
	});
	function doSearch(){
		$("#itemShowDivId").load("${request.contextPath}/eclasscard/gate/attance/schoolList/page?"+searchUrlValue("#checkForm"));
	}
</script>
