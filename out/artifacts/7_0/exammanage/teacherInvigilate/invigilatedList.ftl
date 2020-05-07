<#assign countnum=0>
<#if invigilatorList1?exists && (invigilatorList1?size > 0)>
	<#assign countnum=countnum+1>
<table class="table table-bordered table-striped table-hover">
    <thead>
    <tr>
        <#--<th class="text-center" >考场编号</th>-->
        <th class="text-center" >考场编号</th>
        <th class="text-center" >考试场地</th>
        <th class="text-center" >科目</th>
        <th class="text-center" >考试日期</th>
        <th class="text-center" >时段</th>
        <th class="text-center" >监考老师</th>
    </tr>
    </thead>
    <tbody>
		<#list invigilatorList1 as dto>
        <tr>
            <#--<td class="text-center">${dto.examPlaceCode!}</td>-->
            <td class="text-center">${dto.examPlaceCode}</td>
            <td class="text-center">${dto.examPlaceName!}</td>
            <td class="text-center">${dto.subjectName!}</td>
            <td class="text-center">${(dto.startTime?string('yyyy-MM-dd'))?if_exists}</td>
            <td class="text-center">${(dto.startTime?string('HH:mm'))?if_exists}-${(dto.endTime?string('HH:mm'))?if_exists}</td>
            <td class="text-center">${dto.teacherNames!}</td>
        </tr>
		</#list>
    </tbody>
</table>
</#if>
<#--<#if invigilatorList2?exists && (invigilatorList2?size > 0)>-->
	<#--<#assign countnum=countnum+1>-->
<#--<table class="table table-bordered table-striped table-hover">-->
    <#--<thead>-->
    <#--<tr>-->
        <#--<th class="text-center" >序号</th>-->
        <#--<th class="text-center" >考试场地</th>-->
        <#--<th class="text-center" >考试日期</th>-->
        <#--<th class="text-center" >时段</th>-->
        <#--<th class="text-center" >巡考老师</th>-->
    <#--</tr>-->
    <#--</thead>-->
    <#--<tbody>-->
		<#--<#list invigilatorList2 as dto>-->
        <#--<tr>-->
            <#--<td class="text-center">${dto_index+1}</td>-->
            <#--<td class="text-center">${dto.examPlaceName!}</td>-->
            <#--<td class="text-center">${(dto.startTime?string('yyyy-MM-dd'))?if_exists}</td>-->
            <#--<td class="text-center">${(dto.startTime?string('HH:mm'))?if_exists}-${(dto.endTime?string('HH:mm'))?if_exists}</td>-->
            <#--<td class="text-center">${dto.teacherNames!}</td>-->
        <#--</tr>-->
		<#--</#list>-->
    <#--</tbody>-->
<#--</table>-->
<#--</#if>-->
<#if countnum ==0>
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
<#--<table class="table table-bordered ">-->
<#--<tr>-->
<#--<td colspan="4">-->
<#--<p class="alert alert-info center" style="padding:10px;margin:0;">暂时还没有监考信息或者巡考信息</p>-->
<#--</td>-->
<#--<tr>-->
<#--</table>-->
</#if>