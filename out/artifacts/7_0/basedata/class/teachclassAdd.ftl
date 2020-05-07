<#import "/fw/macro/webmacro.ftl" as w>
<#import "/basedata/class/detailmacro.ftl" as d>
<title>教学班信息</title>
<div class="row teachClassDetail" style="margin-top:10px;">
	<div class="clearfix">
	<input type="hidden" name="id" id="id" value="${teachClass.id!}">
		<div class="form-horizontal col-lg-12 col-sm-12 col-xs-12 col-md-12" role="form">
			<@d.formGroup id="acadyear" labelText="学&nbsp;&nbsp;年"  lableColor="red" circle=true type="select" nullable="false">
				<#if acadyearList?exists && (acadyearList?size>0)>
					<#list acadyearList as acadyear>
						<option value="${acadyear!}" <#if acadyear?default('')==teachClass.acadyear?default('')>selected</#if>>${acadyear!}</option>
					</#list>
				</#if>
			</@d.formGroup>
			<@d.formGroup id="semester" labelText="学&nbsp;&nbsp;期" type="select" lableColor="red" circle=true nullable="false">
				${mcodeSetting.getMcodeSelect('DM-XQ',(teachClass.semester?default(0))?string,'0')}
			</@d.formGroup>
			<@d.formGroup id="name" labelText="班级名称" value="${teachClass.name!}" maxLength="25" lableColor="red" circle=true nullable="false"/>
			<@d.formGroup id="classType" labelText="班级属性" type="select" lableColor="red" circle=true nullable="false">
				<option value="1" <#if "1"==teachClass.classType?default('')>selected</#if>>普通班</option>
				<option value="2" <#if "2"==teachClass.classType?default('')>selected</#if>>平行班</option>
			</@d.formGroup>
			
			<div class="form-group col-lg-6 col-sm-6 col-xs-12 col-md-6" id="form-group-teacherId">
			<label class="ace-icon red  fa fa-circle   col-md-3 control-label no-padding-right" for="teacherId">任课教师</label>
				<div class="col-xs-12 col-sm-12 col-md-9">
					<div id="teacherIdDiv">
						<select class="form-control col-md-12 col-sm-12 col-xs-12 " id="teacherId" nullable="false" >
						<option value="">--- 请选择 ---</option>
						<#if teachers?exists && teachers?size gt 0>
							<#list teachers as teacher>
								<option value="${teacher.id!}" <#if teacher.id?default('')==teachClass.teacherId?default('')>selected</#if>>${teacher.teacherName!}</option>
							</#list>
						</#if>
						</select>
					</div>
				</div>
			</div>
			
			<div class="form-group col-lg-6 col-sm-6 col-xs-12 col-md-6" id="form-group-courseId">
			<label class="ace-icon red  fa fa-circle   col-md-3 control-label no-padding-right" for="courseId">科&nbsp;&nbsp;目</label>
				<div class="col-xs-12 col-sm-12 col-md-9">
					<div id="courseIdDiv">
						<select class="form-control col-md-12 col-sm-12 col-xs-12 " id="courseId" nullable="false" >
						<option value="">--- 请选择 ---</option>
						<#if courses?exists && courses?size gt 0>
							<#list courses as course>
								<option value="${course.id!}" <#if course.id?default('')==teachClass.courseId?default('')>selected</#if>>${course.shortName!}</option>
							</#list>
						</#if>
						</select>
					</div>
				</div>
			</div>
			<div class="form-group col-lg-6 col-sm-6 col-xs-12 col-md-6" id="form-group-assistantTeachers">
				<label class="ace-icon red fa col-md-3 control-label no-padding-right" for="assistantTeachers">辅助教师</label>
					<div class="col-xs-12 col-sm-12 col-md-9">
						<select multiple id="ateschids" name="ateschids" class="tag-input-style" data-placeholder=" ">
						<option value="">--- 请选择 ---</option>
							<#if teachers?exists && teachers?size gt 0>
								<#list teachers as teacher>
									<option value="${teacher.id!}" <#if teachClass.lkxzSelectMap?? && teachClass.lkxzSelectMap[teacher.id]??>selected</#if>>${teacher.teacherName!}</option>
								</#list>
							</#if>
						</select>
					</div>
			</div>
			
		</div>
	</div>
	<div class="clearfix form-actions center">
		<@w.btn btnId="teachClass-commit" btnClass="fa-check" btnValue="确定" />
		<@w.btn btnId="teachClass-close" btnClass="fa-times" btnValue="取消" />
	</div>
</div>


<script type="text/javascript">
var scripts = [null,"${request.contextPath}/static/ace/components/chosen/chosen.jquery.min.js"];
$('.page-content-area').ace_ajax('loadScripts', scripts, function() {

	initOperationCheck('.courseDetail');
	
	$('#ateschids').chosen({
		width:'100%',
		results_height:'120px',
		multi_container_height:'100px',
		no_results_text:"未找到",//无搜索结果时显示的文本
		allow_single_deselect:true,//是否允许取消选择
		search_contains:true,//模糊匹配，false是默认从第一个匹配
		//max_selected_options:1 //当select为多选时，最多选择个数
	}); 
	$('#courseId').chosen({
			width:'100%',
			no_results_text:"未找到",//无搜索结果时显示的文本
			allow_single_deselect:true,//是否允许取消选择
			disable_search:false, //是否有搜索框出现
			search_contains:true,//模糊匹配，false是默认从第一个匹配
			//max_selected_options:1 //当select为多选时，最多选择个数
		});
	$('#teacherId').chosen({
			width:'100%',
			no_results_text:"未找到",//无搜索结果时显示的文本
			allow_single_deselect:true,//是否允许取消选择
			disable_search:false, //是否有搜索框出现
			search_contains:true,//模糊匹配，false是默认从第一个匹配
			//max_selected_options:1 //当select为多选时，最多选择个数
		});
		
	// 取消按钮操作功能
	$("#teachClass-close").on("click", function(){
		doLayerOk("#teachClass-commit", {
			redirect:function(){gotoHash("${request.contextPath}/basedata/course/index/page")},
			window:function(){layer.closeAll()}
		});		
	 });
	// 确定按钮操作功能
	$("#teachClass-commit").on("click", function(){
		$(this).addClass("disabled");
		var check = checkValue('.teachClassDetail');
		if(!check){
		 	$(this).removeClass("disabled");
		 	return;
		}
		var teacherId=$("#teacherId").val();
		if(typeof(teacherId)=="undefine" || teacherId==""){
			layer.tips('不能为空!', $("#teacherIdDiv"), {
					tipsMore: true,
					tips: 3
			});
			$(this).removeClass("disabled");
		 	return;
		}	
		var courseId=$("#courseId").val();
		if(typeof(courseId)=="undefine" || courseId==""){
			layer.tips('不能为空!', $("#courseIdDiv"), {
					tipsMore: true,
					tips: 3
			});
			$(this).removeClass("disabled");
		 	return;
		}
		
		var obj = new Object();
		// 获取此控件下所有的可输入内容，并组织成json格式
		// obj.grade，是因为url所对应的接收对象是个dto，数据是存在dto.grade
		obj = JSON.parse(dealValue('.teachClassDetail'));
		// 提交数据
	 	$.ajax({
		    url:'${request.contextPath}/basedata/teachclass/saveorupdate',
		    data: JSON.stringify(obj),  
		    type:'post',  
		    cache:false,  
		    contentType: "application/json",
		    success:function(data) {
		    	var jsonO = JSON.parse(data);
		 		if(!jsonO.success){
		 			swal({title: "操作失败!",
	    			text: jsonO.msg,type: "error",showConfirmButton: true,confirmButtonText: "确定"}, function(){
	    			$("#teachClass-commit").removeClass("disabled");
	    			});
		 		}
		 		else{
		 			// 显示成功信息
		 			layer.tips(jsonO.msg, "#teachClass-commit", {tips: [4, '#228B22']});
		 			// 调用封装好的函数，此函数内支持移动端和非移动端，也可以自己写，
		 			// 需要区分移动端和非移动端返回处理不一样
	 				doLayerOk("#teachClass-commit", {
						redirect:function(){gotoHash("${request.contextPath}/basedata/teachclass/index/page?acadyear=${acadyear!}&semester=${semester!}")},
						window:function(){
							setTimeout(function(){layer.closeAll();}, 300);
							$("#courseList").trigger("reloadGrid");
							gotoHash("${request.contextPath}/basedata/teachclass/index/page?acadyear=${acadyear!}&semester=${semester!}");
			 			}
		 			});				 			
    			}
		     }
		});
	 });
});
	
</script>


