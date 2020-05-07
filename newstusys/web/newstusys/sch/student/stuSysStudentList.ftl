<#import "/fw/macro/htmlcomponent.ftl" as htmlcom>
<form id="mannReForm">
	<table class="table table-bordered table-striped table-hover no-margin">
		<thead>
				<tr>
					<th ><label class="pos-rel">
                        <input type="checkbox" class="wp" id="selectAll">
                        <span class="lbl"></span>
                    </label>选择</th>
					<th>学号</th>
					<th>班级</th>
					<th>姓名</th>
                    <th>班内编号</th>
					<th>性别</th>
					<th>出生日期</th>
                    <th>入学年月</th>
                    <th>操作</th>
				</tr>
		</thead>
		<tbody id="list">
			<#if studentList?exists && (studentList?size > 0)>
				<#list studentList as student>
					<tr>
						<td> <label class="pos-rel">
                            <input name="subjectId" type="checkbox" class="wp" value="${student.id!}">
                            <span class="lbl"></span>
                        </label>
						</td>
						<td>${student.studentCode!}</td>
						<td>${student.className!}</td>
						<td>${student.studentName!}</td>
                        <td>${student.classInnerCode!}</td>
						<td>${(mcodeSetting.getMcode("DM-XB", student.sex?default(0)?string("number")))?if_exists}</td>
                        <td>${(student.birthday?string('yyyy-MM-dd'))?default('')}</td>
                        <td>${(student.toSchoolDate?string('yyyy-MM'))?default('')}</td>
						<td>
				           <a href="javascript:doStudentEdit('${student.id!}');" class="table-btn color-red">编辑</a>
				           <a href="javascript:doStudentDelete('${student.id!}');" class="table-btn color-red">删除</a>
			            </td>					
					</tr>
				</#list>
			</#if>
		</tbody>
	</table>
	    <#if studentList?exists && (studentList?size > 0)>
           <@htmlcom.pageToolBar container="#showList"/>
        </#if>

</form>		
<script>
	
	function  doReload(){
        var url="${request.contextPath}/newstusys/sch/student/studentadmin.action?classId=${classId!}&studentId=${studentId!}";
        $(".model-div").load(url);
    }
	function doStudentEdit(id){
        var classId = $('#classIdSearch').val();
        $("#showList").load("${request.contextPath}/newstusys/sch/student/newStudent.action?studentId="+id+"&classId="+classId);
	}


//全选
$('#selectAll').on('click',function(){
    var total = $('#list :checkbox').length;
    var length = $('#list :checkbox:checked').length;
    if(length != total){
        $('#list :checkbox').prop("checked", "true");
        $(this).prop("checked", "true");
    }else{
        $('#list :checkbox').removeAttr("checked");
        $(this).removeAttr("checked");
    }
});
</script>