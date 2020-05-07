<#import "/fw/macro/webmacro.ftl" as webmacro>
<@webmacro.commonWeb title="同步状态">
<script src="${request.contextPath}/static/components/jquery/dist/jquery.min.js"></script>
<body>
<#-- 
	<input type="button" value="单位" id="unit" /><p>
	<input type="button" value="学校" id="school" /><p>
	<input type="button" value="校区" id="subschool" /><p>
	<input type="button" value="年级" id="grade" /><p>
	<input type="button" value="老师" id="teacher" /><p>
	<input type="button" value="学年" id="semester" /><p>
	<input type="button" value="班级" id="clazz" /><p>
	<input type="button" value="学生" id="student" /><p>
	<input type="button" value="部门" id="dept" /><p>
    <input type="button" value="部门岗位人员" id="postPersonnel" /><p>
    <input type="button" value="导出" id="userpass" /><p>
 

    <input type="button" value="教育局老师" id="eduTeacher" /><p>
    <input type="button" value="学校" id="school" /><p>
    <input type="button" value="老师" id="teacher" /><p>
    <input type="button" value="部门岗位人员" id="postPersonnel" /><p>
	<input type="button" value="学生" id="student" /><p>
<input type="button" value="家长" id="family" /><p>

-->
<input type="button" value="拉取单位数据" id="doUnit" /><p>


<input type="button" value="注册ap测试" id="registerAp" /><p>

<input type="button" value="课程" id="course" /><p>
<input type="button" value="任课信息" id="classTeach" /><p>

<#--
<input type="button" value="部门" id="dept" /><p>
<input type="button" value="部门岗位人员" id="postPersonnel" /><p>
-->
<script>
<#--  
添加教育局的老师
$("#eduTeacher").on("click", function(){
	$.ajax({url:'${request.contextPath}/desktop/synbase/bj/addEduTeacher',
		    data: null,  
		    type:'get',  
		    cache:false,  
	S	    contentType: "application/json",
		    success:function(data) {
		    	alert(data);
		    }});
});
-->
$("#course").on("click", function(){
	$.ajax({url:'${request.contextPath}/desktop/synbase/bj/addCourse',
		    data: null,  
		    type:'get',  
		    cache:false,  
		    contentType: "application/json",
		    success:function(data) {
		    	alert(data);
		    }});
});

$("#classTeach").on("click", function(){
	$.ajax({url:'${request.contextPath}/desktop/synbase/bj/addClassTeach',
		    data: null,  
		    type:'get',  
		    cache:false,  
		    contentType: "application/json",
		    success:function(data) {
		    	alert(data);
		    }});
});


$("#userpass").on("click", function(){
  document.location.href = "${request.contextPath}/desktop/synbase/bj/userpass/export";
});


$("#registerAp").on("click", function(){
	$.ajax({url:'${request.contextPath}/desktop/synbase/bj/accenter/app/registert/add',
		    data: null,  
		    type:'get',  
		    cache:false,  
		    contentType: "application/json",
		    success:function(data) {
		    	alert(data);
		    }});
});





$("#doUnit").on("click", function(){
	$.ajax({url:'${request.contextPath}/desktop/synbase/bj/saveUnit',
		    data: null,  
		    type:'get',  
		    cache:false,  
		    contentType: "application/json",
		    success:function(data) {
		    	alert(data);
		    }});
});

$("#family").on("click", function(){
	$.ajax({url:'${request.contextPath}/desktop/synbase/bj/addFamily',
		    data: null,  
		    type:'get',  
		    cache:false,  
		    contentType: "application/json",
		    success:function(data) {
		    	alert(data);
		    }});
});

$("#postPersonnel").on("click", function(){
	$.ajax({url:'${request.contextPath}/desktop/synbase/bj/addPostPersonnel',
		    data: null,  
		    type:'get',  
		    cache:false,  
		    contentType: "application/json",
		    success:function(data) {
		    	alert(data);
		    }});
});

$("#dept").on("click", function(){
	$.ajax({url:'${request.contextPath}/desktop/synbase/bj/addDept',
		    data: null,  
		    type:'get',  
		    cache:false,  
		    contentType: "application/json",
		    success:function(data) {
		    	alert(data);
		    }});
});

$("#unit").on("click", function(){
	$.ajax({url:'${request.contextPath}/desktop/synbase/bj/addUnit',
		    data: null,  
		    type:'get',  
		    cache:false,  
		    contentType: "application/json",
		    success:function(data) {
		    	alert(data);
		    }});
});

$("#school").on("click", function(){
	$.ajax({url:'${request.contextPath}/desktop/synbase/bj/addSchool',
		    data: null,  
		    type:'get',  
		    cache:false,  
		    contentType: "application/json",
		    success:function(data) {
		    	alert(data);
		    }});
});

$("#subschool").on("click", function(){
	$.ajax({url:'${request.contextPath}/desktop/synbase/bj/addSubschool',
		    data: null,  
		    type:'get',  
		    cache:false,  
		    contentType: "application/json",
		    success:function(data) {
		    	alert(data);
		    }});
});

$("#grade").on("click", function(){
	$.ajax({url:'${request.contextPath}/desktop/synbase/bj/addGrade',
		    data: null,  
		    type:'get',  
		    cache:false,  
		    contentType: "application/json",
		    success:function(data) {
		    	alert(data);
		    }});
});

$("#teacher").on("click", function(){
	$.ajax({url:'${request.contextPath}/desktop/synbase/bj/addTeacher',
		    data: null,  
		    type:'get',  
		    cache:false,  
		    contentType: "application/json",
		    success:function(data) {
		    	alert(data);
		    }});
});

$("#semester").on("click", function(){
	$.ajax({url:'${request.contextPath}/desktop/synbase/bj/addSemester',
		    data: null,  
		    type:'get',  
		    cache:false,  
		    contentType: "application/json",
		    success:function(data) {
		    	alert(data);
		    }});
});


$("#clazz").on("click", function(){
	$.ajax({url:'${request.contextPath}/desktop/synbase/bj/addClass',
		    data: null,  
		    type:'get',  
		    cache:false,  
		    contentType: "application/json",
		    success:function(data) {
		    	alert(data);
		    }});
});


$("#student").on("click", function(){
	$.ajax({url:'${request.contextPath}/desktop/synbase/bj/addStudent',
		    data: null,  
		    type:'get',  
		    cache:false,  
		    contentType: "application/json",
		    success:function(data) {
		    	alert(data);
		    }});
});



</script>
</@webmacro.commonWeb>