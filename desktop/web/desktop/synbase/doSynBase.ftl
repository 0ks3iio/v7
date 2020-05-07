<#import "/fw/macro/webmacro.ftl" as webmacro>
<@webmacro.commonWeb title="同步状态">
<script src="${request.contextPath}/static/components/jquery/dist/jquery.min.js"></script>
<body>
    
    <input type="button" value="文轩--学校" id="wxSchool" onClick = "doWXsave(this.id)" /><p>
	<input type="button" value="文轩--班级" id="wxClazz" onClick = "doWXsave(this.id)" /><p>
	<input type="button" value="文轩--教师" id="wxTeacher" onClick = "doWXsave(this.id)" /><p>
	<input type="button" value="文轩--学校管理员" id="wxAdmin" onClick = "doWXsave(this.id)"/><p>
	<input type="button" value="文轩--学生" id="wxStudent" onClick = "doWXsave(this.id)"/><p>
	<input type="button" value="文轩--家长" id="wxFamily" onClick = "doWXsave(this.id)"/><p>
	<input type="button" value="文轩--校区" id="wxTeachArea" onClick = "doWXsave(this.id)"/><p>
	<input type="button" value="文轩--场地" id="wxTeachPlace" onClick = "doWXsave(this.id)"/><p>
    <input type="button" value="文轩--拉取全部数据" id="wxData" onClick = "doWXsave(this.id)"/><p>


	<input type="button" value="教育局" id="eduUnit" /><p>
	<input type="button" value="小初高学校" id="xcgschool" /><p>
	<input type="button" value="学前学校" id="xqschool" /><p>
	<input type="button" value="学前校区" id="xqsubschool" /><p>
	<input type="button" value="小初高校区" id="subschool" /><p>
	<input type="button" value="学年学期" id="semester" /><p>
	<input type="button" value="小初高年级 " id="grade" /><p>
	<input type="button" value="部门组" id="dept" /><p>
	<input type="button" value="小初高教师" id="xcgteacher" /><p>
	<input type="button" value="教育局教师" id="jyjteacher" /><p>
	<input type="button" value="学前教师" id="xqteacher" /><p>
	<input type="button" value="更新教师多单位和部门" id="moreUnitAndDeptTeacher" /><p>
	<input type="button" value="班级" id="clazz" /><p>
	<input type="button" value="学前班级" id="xqclazz" /><p>
	<input type="button" value="小初高学生" id="student" /><p>
	<input type="button" value="学前学生" id="xqstudent" /><p>
	<input type="button" value="科目信息" id="course" /><p>
    <input type="button" value="任课信息" id="classTeach" /><p>
	<input type="button" value="学生异动" id="ydstudent" /><p>
	<input type="button" value="教师异动" id="ydteacher" /><p>
	<input type="button" value="家长" id="family" /><p>
	
	
	<input type="button" value="拉取全部" id="allData" /><p>
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

var beforeUrl = "${request.contextPath}/examinfo/stu/desktop/synbase/bj/saveUnit";

function doWXsave(type){
   $.ajax({url:beforeUrl+'?param='+type,
		    data: null,  
		    type:'get',  
		    cache:false,  
		    contentType: "application/json",
		    success:function(data) {
		    	alert(data);
		    }});
}





$("#eduUnit").on("click", function(){
	$.ajax({url:beforeUrl+'?param=eduUnit',
		    data: null,  
		    type:'get',  
		    cache:false,  
		    contentType: "application/json",
		    success:function(data) {
		    	alert(data);
		    }});
});

$("#xcgschool").on("click", function(){
	$.ajax({url:beforeUrl+'?param=xcgschool',
		    data: null,  
		    type:'get',  
		    cache:false,  
		    contentType: "application/json",
		    success:function(data) {
		    	alert(data);
		    }});
});

$("#xqschool").on("click", function(){
	$.ajax({url:beforeUrl+'?param=xqschool',
		    data: null,  
		    type:'get',  
		    cache:false,  
		    contentType: "application/json",
		    success:function(data) {
		    	alert(data);
		    }});
});

$("#xqsubschool").on("click", function(){
	$.ajax({url:beforeUrl+'?param=xqsubschool',
		    data: null,  
		    type:'get',  
		    cache:false,  
		    contentType: "application/json",
		    success:function(data) {
		    	alert(data);
		    }});
});


$("#subschool").on("click", function(){
	$.ajax({url:beforeUrl+'?param=subschool',
		    data: null,  
		    type:'get',  
		    cache:false,  
		    contentType: "application/json",
		    success:function(data) {
		    	alert(data);
		    }});
});

$("#semester").on("click", function(){
	$.ajax({url:beforeUrl+'?param=semester',
		    data: null,  
		    type:'get',  
		    cache:false,  
		    contentType: "application/json",
		    success:function(data) {
		    	alert(data);
		    }});
});

$("#grade").on("click", function(){
	$.ajax({url:beforeUrl+'?param=grade',
		    data: null,  
		    type:'get',  
		    cache:false,  
		    contentType: "application/json",
		    success:function(data) {
		    	alert(data);
		    }});
});

$("#dept").on("click", function(){
	$.ajax({url:beforeUrl+'?param=dept',
		    data: null,  
		    type:'get',  
		    cache:false,  
		    contentType: "application/json",
		    success:function(data) {
		    	alert(data);
		    }});
});

$("#xcgteacher").on("click", function(){
	$.ajax({url:beforeUrl+'?param=xcgteacher',
		    data: null,  
		    type:'get',  
		    cache:false,  
		    contentType: "application/json",
		    success:function(data) {
		    	alert(data);
		    }});
});

$("#jyjteacher").on("click", function(){
	$.ajax({url:beforeUrl+'?param=jyjteacher',
		    data: null,  
		    type:'get',  
		    cache:false,  
		    contentType: "application/json",
		    success:function(data) {
		    	alert(data);
		    }});
});

$("#xqteacher").on("click", function(){
	$.ajax({url:beforeUrl+'?param=xqteacher',
		    data: null,  
		    type:'get',  
		    cache:false,  
		    contentType: "application/json",
		    success:function(data) {
		    	alert(data);
		    }});
});

$("#moreUnitAndDeptTeacher").on("click", function(){
	$.ajax({url:beforeUrl+'?param=moreUnitAndDeptTeacher',
		    data: null,  
		    type:'get',  
		    cache:false,  
		    contentType: "application/json",
		    success:function(data) {
		    	alert(data);
		    }});
});



$("#clazz").on("click", function(){
	$.ajax({url:beforeUrl+'?param=clazz',
		    data: null,  
		    type:'get',  
		    cache:false,  
		    contentType: "application/json",
		    success:function(data) {
		    	alert(data);
		    }});
});

$("#xqclazz").on("click", function(){
	$.ajax({url:beforeUrl+'?param=xqclazz',
		    data: null,  
		    type:'get',  
		    cache:false,  
		    contentType: "application/json",
		    success:function(data) {
		    	alert(data);
		    }});
});

$("#student").on("click", function(){
	$.ajax({url:beforeUrl+'?param=student',
		    data: null,  
		    type:'get',  
		    cache:false,  
		    contentType: "application/json",
		    success:function(data) {
		    	alert(data);
		    }});
});

$("#xqstudent").on("click", function(){
	$.ajax({url:beforeUrl+'?param=xqstudent',
		    data: null,  
		    type:'get',  
		    cache:false,  
		    contentType: "application/json",
		    success:function(data) {
		    	alert(data);
		    }});
});


$("#course").on("click", function(){
	$.ajax({url:beforeUrl+'?param=course',
		    data: null,  
		    type:'get',  
		    cache:false,  
		    contentType: "application/json",
		    success:function(data) {
		    	alert(data);
		    }});
});

$("#classTeach").on("click", function(){
	$.ajax({url:beforeUrl+'?param=classTeach',
		    data: null,  
		    type:'get',  
		    cache:false,  
		    contentType: "application/json",
		    success:function(data) {
		    	alert(data);
		    }});
});

$("#ydstudent").on("click", function(){
	$.ajax({url:beforeUrl+'?param=ydstudent',
		    data: null,  
		    type:'get',  
		    cache:false,  
		    contentType: "application/json",
		    success:function(data) {
		    	alert(data);
		    }});
});

$("#ydteacher").on("click", function(){
	$.ajax({url:beforeUrl+'?param=ydteacher',
		    data: null,  
		    type:'get',  
		    cache:false,  
		    contentType: "application/json",
		    success:function(data) {
		    	alert(data);
		    }});
});

$("#family").on("click", function(){
	$.ajax({url:beforeUrl+'?param=family',
		    data: null,  
		    type:'get',  
		    cache:false,  
		    contentType: "application/json",
		    success:function(data) {
		    	alert(data);
		    }});
});

$("#allData").on("click", function(){
	$.ajax({url:beforeUrl+'?param=allData',
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