<#import "/fw/macro/webmacro.ftl" as w>
<link rel="stylesheet" href="${request.contextPath}/static/ace/css/daterangepicker.css" />

<div class="row classDetail" style="margin-top:10px;">
	<#-- 数据内容 -->
	<div class="clearfix">
		<input type="hidden" name="id" id="id" value="${dto.clazz.id!}">
		<input type="hidden" name="schoolId" id="schoolId" value="${dto.clazz.schoolId!}">
		<input type="hidden" name="acadyear" id="acadyear" value="${dto.clazz.acadyear!}">
		<div class="form-horizontal col-lg-6 col-sm-6 col-xs-12 col-md-6" role="form">
			<div class="form-group" id="form-group-section" >
				<label class="ace-icon red fa fa-circle col-md-3 control-label no-padding-right" for="section"> 所属学段 </label>
				<div class="col-md-9">
					<select <#if dto.clazz.id?default('')!=''>disabled</#if> name="section" id="section" oid="section" nullable="false" data-placeholder="请选择" class="multiselect form-control col-md-10 col-sm-10 col-xs-10 " onchange="dochangeSec()">
						<#if dto.clazz.id?default('')!=''>
							${mcodeSetting.getMcodeSelect("DM-RKXD", (dto.clazz.section?default(0))?string, "0")}
						<#else>
							<#list xdMap?keys as key>
								<option value="${key}" <#if (dto.clazz.section?default(0))?string == key>selected</#if>>${xdMap[key]}</option>
							</#list>
						</#if>
					</select>
				</div>
			</div>
			<div class="form-group" id="form-group-gradeId" >
				<label class="ace-icon red fa fa-circle col-md-3 control-label no-padding-right" for="gradeId"> 年级 </label>
				<div class="col-md-9">
					<select <#if dto.clazz.id?default('')!=''>disabled</#if> name="gradeId" id="gradeId" oid="gradeId" nullable="false" data-placeholder="请选择" class="multiselect form-control col-md-10 col-sm-10 col-xs-10 " onchange="dochangeGra()">		
					</select>
				</div>
			</div>
			<div class="form-group" id="form-group-classCode" >
				<label class="ace-icon red fa fa-circle col-md-3 control-label no-padding-right" for="classCode"> 班级代码 </label>
				<div class="col-xs-12 col-sm-12 col-md-9">
					<span class="block input-icon input-icon-right">
					<input readonly maxLength="10" type="text" name="classCode" id="classCode" oid="classCode" class="form-control col-xs-10 col-sm-10 col-md-10 " placeholder="系统自动生成" value="${dto.clazz.classCode!}" />
					</span>
				</div>
			</div>
			<input type="hidden" name="schoolingLength" id="schoolingLength" value="${dto.clazz.schoolingLength!}" >
			<div class="form-group" id="form-group-schoolingLengthName" >
				<label class="ace-icon red fa fa-circle col-md-3 control-label no-padding-right" for="schoolingLengthName"> 学制 </label>
				<div class="col-xs-12 col-sm-12 col-md-9">
					<span class="block input-icon input-icon-right">
					<input readonly maxLength="10" type="text" id="schoolingLengthName" oid="schoolingLengthName" class="form-control col-xs-10 col-sm-10 col-md-10 " placeholder="根据年级自动生成" value="${dto.clazz.schoolingLength!}" />
					</span>
				</div>
			</div>
			<div class="form-group" id="form-group-className" >
				<label class="ace-icon red fa fa-circle col-md-3 control-label no-padding-right" for="className"> 班级名称 </label>
				<div class="col-xs-12 col-sm-12 col-md-9">
					<span class="block input-icon input-icon-right">
					<span style="width:20%;float:left; text-align:center;padding-top:2px;" id="leftClassName"></span>
					<input nullable="false" style="width:80%" maxLength="30" type="text" name="className" id="className" oid="className" class="form-control col-xs-10 col-sm-10 col-md-10 " value="${dto.clazz.className!}" />
					</span>
				</div>
			</div>
			<div class="form-group" id="form-group-buildDate">
				<label class="ace-icon red fa fa-circle col-md-3 control-label no-padding-right" for="buildDate"> 建班年月 </label>
				<div class="col-xs-12 col-sm-12 col-md-9">
					<span class="block input-icon input-icon-right">
					<input stype="calendar" vtype="date" nullable="false" type="text" name="buildDate" id="buildDate" oid="buildDate" placeholder="建班年月" class="form-control col-xs-10 col-sm-10 col-md-10 " value="${(dto.clazz.buildDate?string('yyyy-MM-dd'))!}" />
					<i class='ace-icon fa fa-calendar'></i>
					</span>
				</div>
			</div>
			
		</div>
		
		<div class="form-horizontal col-lg-6 col-sm-6 col-xs-12 col-md-6" role="form">
			<div class="form-group" id="form-group-artScienceType" >
				<label class="ace-icon red fa fa-circle col-md-3 control-label no-padding-right" for="artScienceType"> 文理类型 </label>
				<div class="col-md-9">
					<select name="artScienceType" id="artScienceType" oid="artScienceType" nullable="false" data-placeholder="请选择" class="multiselect form-control col-md-10 col-sm-10 col-xs-10 " >		
						${mcodeSetting.getMcodeSelect("DM-BJWLLX", (dto.clazz.artScienceType?default(-1))?string, "1")}
					</select>
				</div>
			</div>
			<#if teachAreaList?? && (teachAreaList?size>0)>
			<div class="form-group" id="form-group-teachAreaId" >
				<label class="col-md-3 control-label no-padding-right" for="teachAreaId"> 校区 </label>
				<div class="col-md-9">
					<select class="multiselect form-control col-md-10 col-sm-10 col-xs-10" name="teachAreaId" id="teachAreaId" data-placeholder="--- 请选择 ---" >
						<option value="">--- 请选择 ---</option>
						<#list teachAreaList as item>
							<option value="${item.id}" <#if item.id==dto.clazz.teachAreaId?default('')>selected</#if>>${item.areaName!}</option>
						</#list>
					</select>
				</div>
			</div>
			</#if>
			<div class="form-group" id="form-group-teacherId" >
				<label class="col-md-3 control-label no-padding-right" for="teacherId"> 班主任 </label>
				<div class="col-md-9">
					<select class="" name="teacherId" id="teacherId" data-placeholder="--- 请选择 ---" >
						<#if teacherList?? && (teacherList?size>0)>
							<option value=""></option>
							<#list teacherList as item>
								<option value="${item.id}" <#if item.id==dto.clazz.teacherId?default('')>selected</#if>>${item.teacherName!}</option>
							</#list>
						<#else>
							<option value="">暂无数据</option>
						</#if>
					</select>
				</div>
			</div>
			<div class="form-group" id="form-group-viceTeacherId" >
				<label class="col-md-3 control-label no-padding-right" for="viceTeacherId"> 副班主任 </label>
				<div class="col-md-9">
					<select class="" name="viceTeacherId" id="viceTeacherId" data-placeholder="--- 请选择 ---" >
						<#if teacherList?? && (teacherList?size>0)>
							<option value=""></option>
							<#list teacherList as item>
								<option value="${item.id}" <#if item.id==dto.clazz.viceTeacherId?default('')>selected</#if>>${item.teacherName!}</option>
							</#list>
						<#else>
							<option value="">暂无数据</option>
						</#if>
					</select>
				</div>
			</div>
			<#if dto.clazz.id?default('')!=''>
			<div class="form-group" id="form-group-studentId" >
				<label class="col-md-3 control-label no-padding-right" for="studentId"> 班长 </label>
				<div class="col-md-9">
					<select class="" name="studentId" id="studentId" data-placeholder="--- 请选择 ---" >
						<#if studentList?? && (studentList?size>0)>
							<option value=""></option>
							<#list studentList as item>
								<option value="${item.id}" <#if item.id==dto.clazz.studentId?default('')>selected</#if>>${item.studentName!}</option>
							</#list>
						<#else>
							<option value="">暂无数据</option>
						</#if>
					</select>
				</div>
			</div>
			</#if>
		</div>
	</div>
</div>
<#-- 确定和取消按钮 -->
<div class="row" style="margin-top:10px;">
	<div class="clearfix form-actions center">
		<@w.btn btnId="class-commit" btnClass="fa-check" btnValue="确定" />
		<@w.btn btnId="class-close" btnClass="fa-times" btnValue="取消" />
	</div>
</div>
<!-- /.row -->



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
	initOperationCheck('.classDetail');
	//初始化日期控件
	initCalendar(".classDetail");
	
	$('#teacherId').chosen({
		width:'100%',
		disable_search:false, //是否隐藏搜索框
		no_results_text:"未找到",//无搜索结果时显示的文本
		allow_single_deselect:true,//是否允许取消选择
		search_contains:true//模糊匹配，false是默认从第一个匹配
	}); 
	$('#viceTeacherId').chosen({
		width:'100%',
		disable_search:false, //是否隐藏搜索框
		no_results_text:"未找到",//无搜索结果时显示的文本
		allow_single_deselect:true,//是否允许取消选择
		search_contains:true//模糊匹配，false是默认从第一个匹配
	}); 
	$('#studentId').chosen({
		width:'100%',
		disable_search:false, //是否隐藏搜索框
		no_results_text:"未找到",//无搜索结果时显示的文本
		allow_single_deselect:true,//是否允许取消选择
		search_contains:true//模糊匹配，false是默认从第一个匹配
	}); 
	
	// 取消按钮操作功能
	$("#class-close").on("click", function(){
		doLayerOk("#class-commit", {
		redirect:function(){},
		window:function(){layer.closeAll()}
		});		
	 });
	// 确定按钮操作功能
	var isSubmit=false;
	$("#class-commit").on("click", function(){
		if(isSubmit){
			return;
		}
		isSubmit=true;
		$(this).addClass("disabled");
		var check = checkValue('.classDetail');
		if(!check){
		 	$(this).removeClass("disabled");
		 	isSubmit=false;
		 	return;
		}
		var obj = new Object();
		// 获取此控件下所有的可输入内容，并组织成json格式
		// obj.clazz，是因为url所对应的接收对象是个dto，数据是存在dto.clazz
		obj.clazz = JSON.parse(dealValue('.classDetail'));
		// 提交数据
	 	$.ajax({
		    url:'${request.contextPath}/basedata/class/save',
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
    					$("#class-commit").removeClass("disabled");
    					isSubmit=false;
    				});
		 		}
		 		else{
		 			// 显示成功信息
		 			layer.tips(jsonO.msg, "#class-commit", {tips: [4, '#228B22']});
		 			// 调用封装好的函数，此函数内支持移动端和非移动端，也可以自己写，
		 			// 需要区分移动端和非移动端返回处理不一样
	 				doLayerOk("#class-commit", {
						redirect:function(){},
						window:function(){
							setTimeout(function(){layer.closeAll();}, 2000);
							$("#classList").trigger("reloadGrid");
							$("#gradeList").trigger("reloadGrid");
			 			}
		 			});
    			}
		     }
		});
	 });
	 dochangeSec('${dto.clazz.gradeId!}');
});	
var gradeMap={};
var sectionMap={};
function dochangeSec(gradeIdOld){
	if(!gradeIdOld){
		gradeIdOld='';
	}
	var section = $("#section").val();
	$("#gradeId option").remove();
	if(sectionMap[section]){
		var jsonO=sectionMap[section];
		$.each(jsonO,function(index){
    		var htmlOption="<option ";
    		if(gradeIdOld==jsonO[index].grade.id){
    			htmlOption+=" selected ";
    		}
    		htmlOption+=" value='"+jsonO[index].grade.id+"'>"+jsonO[index].grade.gradeName+"</option>";
    		$("#gradeId").append(htmlOption);
    	});
    	dochangeGra();
    	return;
	}
	$.ajax({
	    url:'${request.contextPath}/basedata/grade/unit/${unitId!}/list',
	    data: {'searchSection':section},  
	    type:'post',  
	    success:function(data) {
	    	var jsonO = JSON.parse(data);
	    	sectionMap[section]=jsonO;
	    	$.each(jsonO,function(index){
	    		var htmlOption="<option ";
	    		if(gradeIdOld==jsonO[index].grade.id){
	    			htmlOption+=" selected ";
	    		}
	    		htmlOption+=" value='"+jsonO[index].grade.id+"'>"+jsonO[index].grade.gradeName+"</option>";
	    		$("#gradeId").append(htmlOption);
	    		gradeMap[jsonO[index].grade.id]=jsonO[index];
	    	});
	    	dochangeGra();
	    }
	});
}
function dochangeGra(){
	var gradeId = $("#gradeId").val();
	$("#acadyear").val(gradeMap[gradeId].grade.openAcadyear);
	$("#leftClassName").html(gradeMap[gradeId].grade.gradeName);
	$("#schoolingLength").val(gradeMap[gradeId].grade.schoolingLength);
	<#if dto.clazz.id?default('')==''>
	$.ajax({
	    url:'${request.contextPath}/basedata/class/maxName/find',
	    data: {'schoolId':gradeMap[gradeId].grade.schoolId,'section':gradeMap[gradeId].grade.section,'acadyear':gradeMap[gradeId].grade.openAcadyear,'schoolingLength':gradeMap[gradeId].grade.schoolingLength},  
	    type:'post',  
	    success:function(data) {
	    	$("#className").val(data);
	    }
	});
	<#else>
	$("#schoolingLengthName").val(gradeMap[gradeId].grade.schoolingLength);
	</#if>
}
</script>
