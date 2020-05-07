<#import "/fw/macro/webmacro.ftl" as w>
<div class="row gradeDetail" style="margin-top:10px;">
	<#-- 数据内容 -->
	<div class="clearfix">
		<input type="hidden" name="id" id="id" value="${dto.grade.id!}">
		<input type="hidden" name="schoolId" id="schoolId" value="${dto.grade.schoolId!}">
		<div class="form-horizontal col-lg-6 col-sm-6 col-xs-12 col-md-6" role="form">
			<div class="form-group" id="form-group-section" >
				<label class="ace-icon red fa fa-circle col-md-3 control-label no-padding-right" for="section"> 所属学段 </label>
				<div class="col-md-9">
					<select <#if dto.grade.id?default('')!=''>disabled</#if> name="section" id="section" oid="section" nullable="false" data-placeholder="请选择" class="multiselect form-control col-md-10 col-sm-10 col-xs-10 " >		
						<#if dto.grade.id?default('')!=''>
							${mcodeSetting.getMcodeSelect("DM-RKXD", (dto.grade.section?default(0))?string, "0")}
						<#else>
							<#list xdMap?keys as key>
								<option value="${key}" <#if (dto.grade.section?default(0))?string == key>selected</#if>>${xdMap[key]}</option>
							</#list>
						</#if>
					</select>
				</div>
			</div>
			<div class="form-group" id="form-group-gradeName" >
				<label class="ace-icon red fa fa-circle col-md-3 control-label no-padding-right" for="gradeName"> 年级名称 </label>
				<div class="col-xs-12 col-sm-12 col-md-9">
					<span class="block input-icon input-icon-right">
					<input readonly maxLength="10" type="text" name="gradeName" id="gradeName" oid="gradeName" placeholder="系统自动生成" class="form-control col-xs-10 col-sm-10 col-md-10 " value="${dto.grade.gradeName!}" />
					</span>
				</div>
			</div>
			<div class="form-group" id="form-group-gradeCode" >
				<label class="ace-icon red fa fa-circle col-md-3 control-label no-padding-right" for="gradeCode"> 年级代码 </label>
				<div class="col-xs-12 col-sm-12 col-md-9">
					<span class="block input-icon input-icon-right">
					<input readonly maxLength="10" type="text" name="gradeCode" id="gradeCode" oid="gradeCode" placeholder="系统自动生成" class="form-control col-xs-10 col-sm-10 col-md-10 " value="${dto.grade.gradeCode!}" />
					</span>
				</div>
			</div>
			<div class="form-group" id="form-group-openAcadyear" >
				<label class="ace-icon red fa fa-circle col-md-3 control-label no-padding-right" for="openAcadyear"> 入学学年 </label>
				<div class="col-xs-12 col-sm-12 col-md-9">
					<#if dto.grade.id?default('')!=''>
						<span class="block input-icon input-icon-right">
						<input readonly nullable="false" maxLength="10" type="text" name="openAcadyear" id="openAcadyear" oid="openAcadyear" class="form-control col-xs-10 col-sm-10 col-md-10 " value="${dto.grade.openAcadyear!}" />
						</span>
					<#else>
						<select name="openAcadyear" id="openAcadyear" oid="openAcadyear" nullable="false" class="multiselect form-control col-md-10 col-sm-10 col-xs-10 ">		
							<#if acadyearList?exists && (acadyearList?size>0)>
								<#list acadyearList as item>
									<option value="${item!}">${item!}</option>
								</#list>
							<#else>
								<option value="">未设置</option>
							</#if>
						</select>
					</#if>
				</div>
			</div>
			<div class="form-group" id="form-group-schoolingLength" >
				<label class="ace-icon red fa fa-circle col-md-3 control-label no-padding-right" for="schoolingLength"> 学制 </label>
				<div class="col-xs-12 col-sm-12 col-md-9">
					<span class="block input-icon input-icon-right">
					<#if dto.grade.id?default('')!=''>
						<input maxLength="10" type="text" name="schoolingLength" id="schoolingLength" oid="schoolingLength" placeholder="根据学校信息自动生成" class="form-control col-xs-10 col-sm-10 col-md-10 " value="${dto.grade.schoolingLength!}" />
					<#else>
						<input readonly maxLength="10" type="text" name="schoolingLength" id="schoolingLength" oid="schoolingLength" placeholder="根据学校信息自动生成" class="form-control col-xs-10 col-sm-10 col-md-10 " value="${dto.grade.schoolingLength!}" />
					</#if>
					</span>
				</div>
			</div>
			
		</div>
		
		<div class="form-horizontal col-lg-6 col-sm-6 col-xs-12 col-md-6" role="form">
			<div class="form-group " id="form-group-amLessonCount">
				<label class="ace-icon red fa fa-circle col-md-3 control-label no-padding-right" for="amLessonCount"> 上午课时数 </label>
				<div class="col-xs-12 col-sm-12 col-md-9">
					<span class="block input-icon input-icon-right">
					<input min="0" max="" minlength="0" length="0" vtype="int" maxlength="1" nullable="false" regex="" regextip="" type="text" id="amLessonCount" oid="amLessonCount" placeholder="上午课时数" class="form-control col-xs-10 col-sm-10 col-md-10 " value="${dto.grade.amLessonCount?default(0)}">
					</span>
				</div>
			</div>
			<div class="form-group " id="form-group-pmLessonCount">
				<label class="ace-icon red fa fa-circle col-md-3 control-label no-padding-right" for="pmLessonCount"> 下午课时数 </label>
				<div class="col-xs-12 col-sm-12 col-md-9">
					<span class="block input-icon input-icon-right">
					<input min="0" max="" minlength="0" length="0" vtype="int" maxlength="1" nullable="false" regex="" regextip="" type="text" id="pmLessonCount" oid="pmLessonCount" placeholder="下午课时数" class="form-control col-xs-10 col-sm-10 col-md-10 " value="${dto.grade.pmLessonCount?default(0)}">
					</span>
				</div>
			</div>
			<div class="form-group " id="form-group-nightLessonCount">
				<label class="ace-icon red fa fa-circle col-md-3 control-label no-padding-right" for="nightLessonCount"> 晚上课时数 </label>
				<div class="col-xs-12 col-sm-12 col-md-9">
					<span class="block input-icon input-icon-right">
					<input min="0" max="" minlength="0" length="0" vtype="int" maxlength="1" nullable="false" regex="" regextip="" type="text" id="nightLessonCount" oid="nightLessonCount" placeholder="晚上课时数" class="form-control col-xs-10 col-sm-10 col-md-10 " value="${dto.grade.nightLessonCount?default(0)}">
					</span>
				</div>
			</div>
			<div class="form-group" id="form-group-teacherId" >
				<label class="col-md-3 control-label no-padding-right" for="teacherId"> 年级组长 </label>
				<div class="col-md-9">
					<select class="" name="teacherId" id="teacherId" data-placeholder="--- 请选择 ---" >
						<#if teacherList?? && (teacherList?size>0)>
							<option value=""></option>
							<#list teacherList as item>
								<option value="${item.id}" <#if item.id==dto.grade.teacherId?default('')>selected</#if>>${item.teacherName!}</option>
							</#list>
						<#else>
							<option value="">暂无数据</option>
						</#if>
					</select>
				</div>
			</div>
			<div class="form-group " id="form-group-displayOrder">
				<label class="col-md-3 control-label no-padding-right" for="displayOrder"> 排序号 </label>
				<div class="col-xs-12 col-sm-12 col-md-9">
					<span class="block input-icon input-icon-right">
					<input min="0" max="" minlength="0" length="0" vtype="int" maxlength="10" nullable="true" regex="" regextip="" type="text" id="displayOrder" oid="displayOrder" placeholder="排序号" class="form-control col-xs-10 col-sm-10 col-md-10 " value="<#if dto.grade.displayOrder??>${dto.grade.displayOrder?default(0)}</#if>">
					</span>
				</div>
			</div>
		</div>
	</div>
</div>
<#-- 确定和取消按钮 -->
<div class="row" style="margin-top:10px;">
	<div class="clearfix form-actions center">
		<@w.btn btnId="grade-commit" btnClass="fa-check" btnValue="确定" />
		<@w.btn btnId="grade-close" btnClass="fa-times" btnValue="取消" />
	</div>
</div>



<!-- page specific plugin scripts -->

<!--[if lte IE 8]>
  <script src="${request.contextPath}/static/ace/js/excanvas.js"></script>
<![endif]-->

<script type="text/javascript">
// 需要用到的js脚本，延迟加载
var scripts = [null,
"${request.contextPath}/static/ace/components/moment/moment.js",
"${request.contextPath}/static/ace/components/bootstrap-daterangepicker/daterangepicker.js",
"${request.contextPath}/static/ace/components/chosen/chosen.jquery.min.js",
"${request.contextPath}/static/js/validate.js",
null];
$('.page-content-area').ace_ajax('loadScripts', scripts, function() {
	// 初始化交互控件
	initOperationCheck('.gradeDetail');
	$('#teacherId').chosen({
		width:'100%',
		disable_search:false, //是否隐藏搜索框
		no_results_text:"未找到",//无搜索结果时显示的文本
		allow_single_deselect:true,//是否允许取消选择
		search_contains:true//模糊匹配，false是默认从第一个匹配
	}); 
	// 取消按钮操作功能
	$("#grade-close").on("click", function(){
		doLayerOk("#grade-commit", {
		redirect:function(){},
		window:function(){layer.closeAll()}
		});		
	 });
	// 确定按钮操作功能
	var isSubmit=false;
	$("#grade-commit").on("click", function(){
		if(isSubmit){
			return;
		}
		isSubmit=true;
		$(this).addClass("disabled");
		var check = checkValue('.gradeDetail');
		if(!check){
		 	$(this).removeClass("disabled");
		 	isSubmit=false;
		 	return;
		}
		var obj = new Object();
		// 获取此控件下所有的可输入内容，并组织成json格式
		// obj.grade，是因为url所对应的接收对象是个dto，数据是存在dto.grade
		obj.grade = JSON.parse(dealValue('.gradeDetail'));
		// 提交数据
	 	$.ajax({
		    url:'${request.contextPath}/basedata/grade/save',
		    data: JSON.stringify(obj),  
		    type:'post',  
		    cache:false,  
		    contentType: "application/json",
		    success:function(data) {
		    	var jsonO = JSON.parse(data);
		 		if(!jsonO.success){
		 			swal({
			 			title: "操作失败!",
		    			text: jsonO.msg,
		    			type: "error",
		    			showConfirmButton: true,
		    			confirmButtonText: "确定"
		    		}, function(){
    					$("#grade-commit").removeClass("disabled");
    					isSubmit=false;
    				});
		 		}
		 		else{
		 			// 显示成功信息
		 			layer.tips(jsonO.msg, "#grade-commit", {tips: [4, '#228B22']});
		 			// 调用封装好的函数，此函数内支持移动端和非移动端，也可以自己写，
		 			// 需要区分移动端和非移动端返回处理不一样
	 				doLayerOk("#grade-commit", {
						redirect:function(){},
						window:function(){
							setTimeout(function(){layer.closeAll();}, 2000);
							$("#gradeList").trigger("reloadGrid");
			 			}
		 			});
    			}
		     }
		});
	 });
});	
</script>
