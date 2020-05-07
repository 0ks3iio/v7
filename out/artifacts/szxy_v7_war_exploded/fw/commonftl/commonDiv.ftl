<#import "/fw/macro/popupMacro.ftl" as popup />
<script>
function popupCallBack(){
	var index=$("#key").val();
	$("#studentName-"+index).val($("#studentName1000").val());
	$("#studentId-"+index).val($("#studentId1000").val());
	alert("回调函数");
}


function selectStudent(index){
	$("#studentName1000").val($("#studentName-"+index).val());
	$("#studentId1000").val($("#studentId-"+index).val());
	$("#key").val(index);
	$("#stuSelect").click();
}
</script>
<#list 1..100 as t>
学生(多选): <input type="text" id="studentName-${t}" value=""/><button onclick="selectStudent('${t}')" class="btn btn-blue">选择学生</button>
<input type="hidden" id="studentId-${t}" value=""/>
<br>
<br>
</#list>
<@popup.selectMoreStudent clickId="stuSelect" id="studentId1000" name="studentName1000" handler="popupCallBack()">
 	<input type="hidden" id="studentName1000" value=""/>
    <input type="hidden" id="studentId1000" value=""/>
    <input type="hidden" id="key" value=""/>
    <a id="stuSelect"></a>
</@popup.selectMoreStudent>
<br>
<br>
<!--
<@popup.selectMoreStudent clickId="studentName" id="studentId" name="studentName" handler="popupCallBack()">
学生(多选): <input type="text" id="studentName" value=""/>
         <input type="hidden" id="studentId" value=""/>
</@popup.selectMoreStudent>
<br>
<br>
<@popup.selectOneStudent clickId="studentName1" id="studentId1" name="studentName1" handler="popupCallBack()">
学生(单选): <input type="text" id="studentName1" value=""/>
         <input type="hidden" id="studentId1" value=""/>
</@popup.selectOneStudent>
<br>
<br>
<@popup.selectMoreTeacher clickId="teacherName" id="teacherId" name="teacherName" handler="popupCallBack()">
教师(多选): <input type="text" id="teacherName" value=""/>
         <input type="hidden" id="teacherId" value=""/>
</@popup.selectMoreTeacher>
<br>
<br>
<@popup.selectOneTeacher clickId="teacherName1" id="teacherId1" name="teacherName1" handler="popupCallBack()">
教师(单选): <input type="text" id="teacherName1" value=""/>
         <input type="hidden" id="teacherId1" value=""/>
</@popup.selectOneTeacher>
<br>
<br>
<@popup.selectMoreClass clickId="className" id="classId" name="className" handler="popupCallBack()">
班级(多选):<input type="text" id="className" value=""/>
         <input type="hidden" id="classId" value=""/>
</@popup.selectMoreClass>
<br>
<br>
<@popup.selectOneClass clickId="className1" id="classId1" name="className1" handler="popupCallBack()">
班级(单选):<input type="text" id="className1" value=""/>
         <input type="hidden" id="classId1" value=""/>
</@popup.selectOneClass>
<br>
<br>
<@popup.selectMoreUser clickId="userName100" id="userId100" name="userName100" handler="popupCallBack()">
用户(多选):<input type="text" id="userName100" value=""/>
         <input type="hidden" id="userId100" value=""/>
</@popup.selectMoreUser>
<br>
<br>
<@popup.selectOneUser clickId="userName101" id="userId101" name="userName101" handler="popupCallBack()">
用户(单选):<input type="text" id="userName101" value=""/>
         <input type="hidden" id="userId101" value=""/>
</@popup.selectOneUser>
<br>
<br>
<@popup.selectMoreStuByClass clickId="studentName100" id="studentId100" name="studentName100" handler="popupCallBack()">
按班级选择学生(多选): <input type="text" id="studentName100" value=""/>
         <input type="hidden" id="studentId100" value=""/>
</@popup.selectMoreStuByClass>
<br>
<br>
<@popup.selectOneStuByClass clickId="studentName101" id="studentId101" name="studentName101" handler="popupCallBack()">
按班级选择学生(多选): <input type="text" id="studentName101" value=""/>
         <input type="hidden" id="studentId101" value=""/>
</@popup.selectOneStuByClass>

-->