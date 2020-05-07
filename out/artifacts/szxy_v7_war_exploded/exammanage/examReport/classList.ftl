<#if (stuDtoList?exists && stuDtoList?size>0)>
<div class="print">
<table class="table table-bordered table-striped table-hover no-margin">
    <thead>
    <tr>
        <th class="text-center">学号</th>
        <th class="text-center">姓名</th>
        <th class="text-center">班级</th>
        <th class="text-center">考号</th>
        <th class="text-center">考场编号</th>
        <th class="text-center">考试场地</th>
        <th class="text-center">座位号</th>
    </tr>
    </thead>
    <tbody>
		<#if (stuDtoList?exists && stuDtoList?size>0)>
			<#list stuDtoList as item>
            <tr>
                <td class="text-center">${item.student.studentCode!}</td>
                <td class="text-center">${item.student.studentName!}</td>
                <td class="text-center">${className!}</td>
                <td class="text-center">${item.examNumber!}</td>
                <td class="text-center">${item.placeNumber!}</td>
                <td class="text-center">${item.placeName!}</td>
                <td class="text-center">${item.seatNum!}</td>
            </tr>
			</#list>
		</#if>
    </tbody>
</table>
</div>
<#else>
<div class="no-data-container">
    <div class="no-data">
					<span class="no-data-img">
						<img src="${request.contextPath}/static/images/classCard/no-tutor-project.png" alt="">
					</span>
        <h3 class="no-data-body">
            暂无相关数据
        </h3>
    </div>
</div>
<#--<h3 class="lighter smaller">-->
<#--考试班级不存在-->
<#--</h3>-->
</#if>