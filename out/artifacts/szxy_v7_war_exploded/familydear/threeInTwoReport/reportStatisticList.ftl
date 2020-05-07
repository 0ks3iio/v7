<#import "/fw/macro/htmlcomponent.ftl" as htmlcom>
<#if famDearThreeInTwoReports?exists&& (famDearThreeInTwoReports?size > 0)>
<table class="table table-bordered table-striped table-hover">
	<thead>
		<tr>
			<th width="10%">部门</th>
			<th width="10%">干部姓名</th>
			<th width="17%">活动时间</th>
			<th width="8%">活动主题</th>
			<th width="15%">活动类型</th>
			<th width="15%">对象学生</th>
			<th width="25%">活动内容</th>
		</tr>
	</thead>
	<tbody>
	    <#list famDearThreeInTwoReports as item>
		<tr>
			<td >${item.deptName!}</td>
			<td >${item.teaName!}</td>
			<td >${((item.startTime)?string('yyyy-MM-dd'))?if_exists}至${((item.endTime)?string('yyyy-MM-dd'))?if_exists}</td>
			<td >${item.titleStr!}</td>
			<td >${item.typeStr!}</td>
			<td >${item.stuNames!}</td>
			<td title="${item.content?default('')}"><@htmlcom.cutOff str="${item.content?default('')}" length=50/></td>
		</tr>
		</#list>
	</tbody>
</table>
	<@htmlcom.pageToolBar container="#showList1" class="noprint"/>
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
