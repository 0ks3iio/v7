<div class="filter" style="height:294px">
	<div class="filter-item block">
		<input type="hidden" name="studentId" id="studentId" value="${student.id!}">
		<span class="filter-name">学号：</span>
		<div class="filter-content">
			<p>${student.studentCode!}</p>
		</div>
	</div>
	<div class="filter-item block">
		<span class="filter-name">姓名：</span>
		<div class="filter-content">
			<p>${student.studentName!}</p>
		</div>
	</div>
	<div class="filter-item block">
		<span class="filter-name">已选学科：</span>
		<div class="filter-content">
			<div class="publish-course publish-course-sm">
				<#if coursesList?? && (coursesList?size>0)>
                   <#list coursesList as course>  
                   		<span id="${course.id!}" class="choose_subject <#if chosenSubject?exists && chosenSubject[course.id?default("")]?exists>active</#if>">${course.subjectName!}</span>
                   </#list>
                </#if>
			</div>
		</div>
	</div>
	<div class="filter-item block">
		<span class="filter-name">备注：</span>
		<div class="filter-content">
			<p>${mark!}</p>
		</div>
	</div>
	<p class="no-margin text-center" style="height: 21px"><em id="t"></em></p>
</div>

<script type="text/javascript">

 $('.publish-course span').on('click', function(e){
	e.preventDefault();
	if($(this).hasClass('disabled')) return;

	if($(this).hasClass('active')){
		$(this).removeClass('active');
	}else{
		$(this).addClass('active');
	}
})
//确定按钮操作功能
var isSubmit=false;
function doSaveCourse(){
    if(isSubmit){
        return;
    }
    $("#t").text("");
    isSubmit=true;
  	var studentId=$("#studentId").val();
  	var length=0;
  	var subjectIds="";
  	$(".choose_subject").each(function(){
  		if($(this).hasClass("active")){
  			var subjectId=$(this).attr("id");
  			if(length==0){
  				subjectIds=subjectId
  			}else{
  				subjectIds=subjectIds+","+subjectId
  			}
  			length=length+1;
  		}
  	});
  	var num = '${chooseNum}';
  	if(length!=num){
  		$("#t").text("请选择" + num + "门课程");
      	isSubmit=false;
      	return;
  	} 
    $.ajax({
        url:'${request.contextPath}/newgkelective/${choiceId}/chosenSubject/save?studentId='+studentId+"&subjectId="+subjectIds,
        cache:false,  
        contentType: "application/json",
        success:function(data) {
            var jsonO = JSON.parse(data);
            if(jsonO.success){
                layer.closeAll();
                layer.msg(jsonO.msg, {offset: 't',time: 2000});
                findByCondition(1);
            }
            else{
                $("#t").text(jsonO.msg);
                isSubmit=false;
            }
         }
    });
}    
   

</script>
