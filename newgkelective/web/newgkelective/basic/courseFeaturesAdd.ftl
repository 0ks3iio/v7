<style type="text/css">

#openAbcCourse span
{
overflow:hidden;
}
</style>
<form id="subjectForm">

<div id="openAbcCourse">
	<input type="hidden" value="" name="courseA" id="courseA">
	<input type="hidden" value="" name="courseB" id="courseB">
	<input type="hidden" value="" name="courseO" id="courseO">
	<h3 class="text-center">请选择需要开设的科目</h3>
	<div class="form-horizontal mt30">
		<div class="form-group">
			<label class="col-sm-1 control-label no-padding-right">选考科目：</label>
			<div class="col-sm-11">
				<div class="publish-course" id="course_A">
					<#if gkCourseList?exists && gkCourseList?size gt 0>
						<#list gkCourseList as course>
						<span  id="${course.id!}_A" class="<#if jxbCodes?index_of(course.subjectCode) gt -1 > active</#if>" data-value='${course.id!}' title='${course.subjectName!}'>${course.subjectName!}</span>	
						</#list>
					</#if>		
				</div>
			</div>
		</div>
		<div class="form-group">
			<label class="col-sm-1 control-label no-padding-right">学考科目：</label>
			<div class="col-sm-11">
				<div class="publish-course" id="course_B">
					<#if gkCourseList?exists && gkCourseList?size gt 0>
						<#list gkCourseList as course>
						<span  id="${course.id!}_B" class="<#if jxbCodes?index_of(course.subjectCode) gt -1 > active</#if>" data-value='${course.id!}' title='${course.subjectName!}'>${course.subjectName!}</span>	
						</#list>
					</#if>		
				</div>
			</div>
		</div>
		<div class="form-group">
			<label class="col-sm-1 control-label no-padding-right">行政班科目：</label>
			<div class="col-sm-11">
				<div class="publish-course" id="course_O">
					<#if xzbCourseList?exists && xzbCourseList?size gt 0>
						<#list xzbCourseList as xzbCourse>
						<span  id="${xzbCourse.id!}_O" class="<#if jxbCodes?index_of(xzbCourse.subjectCode) gt -1> disabled <#elseif ysyCodes?index_of(xzbCourse.subjectCode) gt -1 > active </#if>" data-value='${xzbCourse.id!}' title='${xzbCourse.subjectName!}' >${xzbCourse.subjectName!}</span>	
						</#list>
					</#if>		
				</div>
			</div>
		</div>
	</div>
</div>
</form>
<div class="navbar-fixed-bottom opt-bottom">
    <a href="javascript:" class="btn btn-blue" id="subject-commit" onclick="saveChooseSubject()">确定</a>
</div>
<script>
	$(function(){
		//绑定点击事件
		$('#openAbcCourse .publish-course span').off('click').on('click', function(e){
			e.preventDefault();
			if($(this).hasClass('disabled')) return;
	
			if($(this).hasClass('active')){
				$(this).removeClass('active');
			}else{
				$(this).addClass('active');
			}
			var chooseId=$(this).attr("id");
			if(chooseId.indexOf('_O') > -1){
				clearXzbSubject();
			}else{
				clearChooseSubject();
			}
			
		});
	
	})
	function clearXzbSubject(){
		$("#course_O span").each(function(){
			var subId=$(this).attr("data-value");
			if($("#"+subId+"_A")){
				if($(this).hasClass("active")){
					if(!$("#"+subId+"_A").hasClass('disabled')){
						if($("#"+subId+"_A").hasClass('active')){
							$("#"+subId+"_A").removeClass("active");
						}
						
						$("#"+subId+"_A").addClass('disabled');
					}
				
				
					if(!$("#"+subId+"_B").hasClass('disabled')){
						if($("#"+subId+"_B").hasClass('active')){
							$("#"+subId+"_B").removeClass("active");
						}
						
						$("#"+subId+"_B").addClass('disabled');
					}
			
				}else{
					
					if($("#"+subId+"_A").hasClass('disabled')){
						$("#"+subId+"_A").removeClass('disabled');
					}
					
					
					if($("#"+subId+"_B").hasClass('disabled')){
						$("#"+subId+"_B").removeClass('disabled');
					}
					
				}
			}
			
		})
	}
	function clearChooseSubject(){
		var nochooseA={};
		$("#course_A span").each(function(){
			var subId=$(this).attr("data-value");
			if($(this).hasClass("active")){
				if(!$("#"+subId+"_O").hasClass('disabled')){
					if($("#"+subId+"_O").hasClass('active')){
						$("#"+subId+"_O").removeClass("active");
					}
					
					$("#"+subId+"_O").addClass('disabled');
				}
			}else{
				nochooseA[subId]=subId;
			}
		})
		$("#course_B span").each(function(){
			var subId=$(this).attr("data-value");
			if($(this).hasClass("active")){
				if(!$("#"+subId+"_O").hasClass('disabled')){
					if($("#"+subId+"_O").hasClass('active')){
						$("#"+subId+"_O").removeClass("active");
					}
					
					$("#"+subId+"_O").addClass('disabled');
				}
			}else{
				if(nochooseA[subId]){
					//AB都没有选中
					if($("#"+subId+"_O").hasClass('disabled')){
						$("#"+subId+"_O").removeClass('disabled');
					}
				}
			}
		})
	}
	
	function checkMyForm(){
		//7选3 上课科目组装
		makeIds("course_A","courseA");
		makeIds("course_B","courseB");
		makeIds("course_O","courseO");
		return true;
	}
	function makeIds(spanParentId,inputId){
		var subjectIds="";
		$("#"+spanParentId+" span").each(function(){
			if($(this).hasClass("active") && (!$(this).hasClass("disabled"))){
				var subId=$(this).attr("data-value");
				subjectIds=subjectIds+","+subId;
			}
		})
		if(subjectIds!=""){
			subjectIds=subjectIds.substring(1);
		}
		$("#"+inputId).val(subjectIds);
	}
	var isSubmit=false;
	function saveChooseSubject(){
		if(isSubmit){
			return;
		}
		isSubmit=true;
		var check = checkMyForm();
	    if(!check){
	        $("#subject-commit").removeClass("disabled");
	        isSubmit=false;
	        return;
	    }
		var options = {
			url : "${request.contextPath}/newgkelective/${gradeId!}/courseFeatures/addSubject",
			dataType : 'json',
			success : function(data){
	 			var jsonO = data;
		 		if(!jsonO.success){
		 			layerTipMsg(jsonO.success,"保存失败",jsonO.msg);
		 			$("#subject-commit").removeClass("disabled");
		 			return;
		 		}else{
		 			layer.closeAll();
	 				layer.msg("保存成功", {
						offset: 't',
						time: 2000
					});
		 			
				  	toRefesh("1");
				}
			},
			clearForm : false,
			resetForm : false,
			type : 'post',
			error:function(XMLHttpRequest, textStatus, errorThrown){}//请求出错 
		};
		$("#subjectForm").ajaxSubmit(options);
	}
	
	function toRefesh(useMaster){
		var url;
		url = '${request.contextPath}/newgkelective/${gradeId!}/courseFeatures/gradeIndex?useMaster='+useMaster;
		$("#gradeTableList").load(url);
	}	
	
</script>
