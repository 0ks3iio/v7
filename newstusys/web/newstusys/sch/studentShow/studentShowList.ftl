<#import "/fw/macro/htmlcomponent.ftl" as htmlcom>
<form id="mannReForm">
	<#if studentList?exists && (studentList?size > 0)>
<div class="table-container">
<div class="table-container-body"  style="max-height:500px;overflow:auto;">
	<table class="table table-bordered table-striped table-hover no-margin">
		<thead>
				<tr>
					<th>学号</th>
					<th>班级</th>
					<th>姓名</th>
                    <th>班内编号</th>
					<th>性别</th>
					<th>出生日期</th>
                    <th>入学年月</th>
                    <th>父亲姓名</th>
					<th>母亲姓名</th>
                    <th>父亲工作单位</th>
					<th>母亲工作单位</th>
					<th>原毕业学校</th>
                    <th>家庭住址</th>
                    <th>操作</th>
				</tr>
		</thead>
		<tbody id="list">
				<#list studentList as student>
					<tr>
						<td style="white-space: nowrap">${student.studentCode!}</td>
						<td style="white-space: nowrap">${student.className!}</td>
						<td style="white-space: nowrap"><a class="table-btn color-red" href="javascript:;" onclick="showStudentDetail('${student.id!}');">${student.studentName!}</a></td>
						<td style="white-space: nowrap">${student.classInnerCode!}</td>
						<td style="white-space: nowrap">${(mcodeSetting.getMcode("DM-XB", student.sex?default(0)?string))?if_exists}</td>
                        <td style="white-space: nowrap">${(student.birthdayDate?string('yyyy-MM-dd'))?default('')}</td>
                        <td style="white-space: nowrap">${(student.toSchDate?string('yyyy-MM'))?default('')}</td>
						<td style="white-space: nowrap">${student.fRealName!}</td>
						<td style="white-space: nowrap">${student.mRealName!}</td>
						<td style="white-space: nowrap">${student.fCompany!}</td>
                        <td style="white-space: nowrap">${student.mCompany!}</td>
                        <td style="white-space: nowrap">${student.oldSchoolName!}</td>
						<td style="white-space: nowrap"><@htmlcom.cutOff4List str='${student.homeAddress!}' length=15 /></td>
						<td style="white-space: nowrap">
				           <a href="javascript:showStudentDetail('${student.id!}');" class="table-btn color-red">查看</a>
			            </td>
					</tr>
				</#list>
		</tbody>
	</table>
	    <#if tabType?default('') != '3'>
           <@htmlcom.pageToolBar container="#showList"/>
        </#if>
    </div>
    </div>    
	<#else>
	<div class="no-data-container">
		<div class="no-data">
			<span class="no-data-img">
				<img src="${request.contextPath}/static/images/public/nodata6.png" alt="">
			</span>
			<div class="no-data-body">
				<p class="no-data-txt">暂无记录</p>
			</div>
		</div>
	</div>
	</#if>
	
</form>		
<script>
	function showStudentDetail(id){
        var classId = '';
        $("#showList").load("${request.contextPath}/newstusys/sch/student/studentDeatilShow.action?studentId="+id+"&classId="+classId);
	}
</script>