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
                   		<span id="${course.id!}" class="choose_subject <#if oneSelect.selectList?exists && oneSelect.selectList?size gt 0>
                   															<#list oneSelect.selectList as select>
                   																<#if select = course.id>
                   																	active
                   																</#if>
                   															</#list>
																		</#if>">${course.subjectName!}</span>
					</#list>
                </#if>
			</div>
		</div>
	</div>
	<#--<div class="filter-item block">
		<span class="filter-name">备注：</span>
		<div class="filter-content">
			<p>${mark!}</p>
		</div>
	</div>-->
	<p class="no-margin text-center" style="height: 21px"><em id="t"></em></p>
</div>

<#-- 确定和取消按钮 -->
<div style="text-align: center;">
    <a class="btn btn-blue" id="result-commit">确定</a>
    <a class="btn btn-white" id="result-close">取消</a>
</div>

<script type="text/javascript">

// 取消按钮操作功能
$("#result-close").on("click", function(){
    doLayerOk("#result-commit", {
    redirect:function(){},
    window:function(){layer.closeAll()}
    });     
 });
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
$("#result-commit").on("click", function(){
    if(isSubmit){
        return;
    }
    $("#t").text("");
    isSubmit=true;
    $(this).addClass("disabled");
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
  	var num = 3;
  	if(length!=num){
  		$("#t").text("请选择" + num + "门课程");
     	$(this).removeClass("disabled");
      	isSubmit=false;
      	return;
  	}
    var acadyear = $("#acadyear option:selected").val();
    var semester = $("#semester option:selected").val();
    var gradeId = $("#gradeId option:selected").val();
    $.ajax({
        url:"${request.contextPath}/basedata/stuselect/edit/save?acadyear="+acadyear+"&semester="+semester+"&gradeId="+gradeId+"&studentId="+studentId+"&subjectIds="+subjectIds,
        cache:false,  
        contentType: "application/json",
        success:function(data) {
            var jsonO = JSON.parse(data);
            if(jsonO.success){
                layer.closeAll();
                layer.msg(jsonO.msg, {offset: 't',time: 2000});
                change2();
            }
            else{
                $("#t").text(jsonO.msg);
                $("#result-commit").removeClass("disabled");
                isSubmit=false;
            }
         }
    });
 });    
   

</script>
