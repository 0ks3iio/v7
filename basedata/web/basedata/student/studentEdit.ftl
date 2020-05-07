<#import "/fw/macro/webmacro.ftl" as w>
<link rel="stylesheet" href="${request.contextPath}/static/ace/css/daterangepicker.css" />

<div class="row studentDetail" style="margin-top:10px;">
	<#-- 数据内容 -->
	<div class="clearfix">
		<input type="hidden" name="id" id="id" value="${student.id!}">
		<input type="hidden" name="classId" id="classId" value="${student.classId!}">
		<input type="hidden" name="schoolId" id="schoolId" value="${student.schoolId!}">
		
		<div class="form-horizontal col-lg-6 col-sm-6 col-xs-12 col-md-6" role="form">
			<div class="form-group" id="form-group-studentName" >
				<label class="ace-icon red fa fa-circle col-md-3 control-label no-padding-right" for="studentName"> 姓名</label>
				<div class="col-xs-12 col-sm-12 col-md-9">
					<span class="block input-icon input-icon-right">
					<input  maxLength="30" nullable="false" type="text" name="studentName" id="studentName" oid="studentName" placeholder="姓名" class="form-control col-xs-10 col-sm-10 col-md-10 " value="${student.studentName!}" />
					</span>
				</div>
			</div>
			<div class="form-group" id="form-group-studentCode" >
				<label class="ace-icon red fa fa-circle col-md-3 control-label no-padding-right" for="studentCode"> 学号 </label>
				<div class="col-md-9">
					<span class="block input-icon input-icon-right">
					<input  maxLength="20" type="text" name="studentCode" id="studentCode" oid="studentCode" placeholder="学号"  class="form-control col-xs-10 col-sm-10 col-md-10 " value="${student.studentCode!}" />
					</span>
				</div>
			</div>
			<div class="form-group" id="form-group-unitiveCode" >
				<label class="ace-icon red fa fa-circle col-md-3 control-label no-padding-right" for="unitiveCode"> 学籍号 </label>
				<div class="col-xs-12 col-sm-12 col-md-9">
					<span class="block input-icon input-icon-right">
					<input maxLength="20" type="text" name="unitiveCode" id="unitiveCode" oid="unitiveCode" placeholder="学籍号" class="form-control col-xs-10 col-sm-10 col-md-10 " value="${student.unitiveCode!}" />
					</span>
				</div>
			</div>
			<div class="form-group" id="form-group-identityCard" >
				<label class="ace-icon red fa fa-circle col-md-3 control-label no-padding-right" for="identityCard"> 身份证号 </label>
				<div class="col-xs-12 col-sm-12 col-md-9">
					<span class="block input-icon input-icon-right">
					<input maxLength="20" type="text" name="identityCard" id="identityCard" oid="identityCard" placeholder="身份证号" class="form-control col-xs-10 col-sm-10 col-md-10 " value="${student.identityCard!}" />
					</span>
				</div>
			</div>
			
			<div class="form-group" id="form-group-sex" >
				<label class="ace-icon red fa fa-circle col-md-3 control-label no-padding-right" for="sex"> 性别 </label>
				<div class="col-xs-12 col-sm-12 col-md-9">
					<span class="block input-icon input-icon-right">
						<select name="sex" id="sex" oid="sex"   data-placeholder="请选择" class="multiselect form-control col-md-10 col-sm-10 col-xs-10 " >		
							${mcodeSetting.getMcodeSelect("DM-XB", student.sex?default('')?string, "1")}
						</select>
					</span>
				</div>
			</div>
			<div class="form-group" id="form-group-country" >
				<label class="ace-icon red fa fa-circle col-md-3 control-label no-padding-right" for="country"> 国籍/地区 </label>
				<div class="col-xs-12 col-sm-12 col-md-9">
					<span class="block input-icon input-icon-right">
						<select name="country" id="country" oid="country"   data-placeholder="请选择" class="multiselect form-control col-md-10 col-sm-10 col-xs-10 " >		
							${mcodeSetting.getMcodeSelect("DM-COUNTRY", student.country?default(''), "1")}
						</select>
					</span>
				</div>
			</div>
			
		</div>
		
		<div class="form-horizontal col-lg-6 col-sm-6 col-xs-12 col-md-6" role="form">
			<div class="form-group" id="form-group-birthday">
				<label class="ace-icon red fa fa-circle col-md-3 control-label no-padding-right" for="birthday"> 出生日期 </label>
				<div class="col-xs-12 col-sm-12 col-md-9">
					<span class="block input-icon input-icon-right">
					<input stype="calendar" vtype="date" type="text" name="birthday" id="birthday" oid="birthday" placeholder="出生日期" class="form-control col-xs-10 col-sm-10 col-md-10 " value="${(student.birthday?string('yyyy-MM-dd'))!}" />
					<i class='ace-icon fa fa-calendar'></i>
					</span>
				</div>
			</div>
			<div class="form-group" id="form-group-enrollYear">
				<label class="ace-icon red fa fa-circle col-md-3 control-label no-padding-right" for="enrollYear"> 入学学年 </label>
				<div class="col-xs-12 col-sm-12 col-md-9">
					<select  name="enrollYear" id="enrollYear" oid="enrollYear"   data-placeholder="请选择" class="multiselect form-control col-md-10 col-sm-10 col-xs-10 " >		
						<option value="">--- 请选择 ---</option>
						<#if acadyearList?? && (acadyearList?size>0)>
							<#list acadyearList  as item> 
		                   		<option value="${item!}" <#if item==student.enrollYear?default('')>selected</#if>>${item!}学年</option>
			                </#list>
						</#if>
					</select>
				</div>
			</div>
			<div class="form-group" id="form-group-identitycardType">
				<label class="ace-icon red fa fa-circle col-md-3 control-label no-padding-right" for="identitycardType"> 身份证类型 </label>
				<div class="col-xs-12 col-sm-12 col-md-9">
					<span class="block input-icon input-icon-right">
						<select  name="identitycardType" id="identitycardType" oid="identitycardType"   data-placeholder="请选择" class="multiselect form-control col-md-10 col-sm-10 col-xs-10 " >		
							${mcodeSetting.getMcodeSelect("DM-SFZJLX", student.identitycardType?default(''),"1")}
						</select>
					</span>
				</div>
			</div>
			<div class="form-group" id="form-group-pin" >
				<label class="ace-icon red fa fa-circle col-md-3 control-label no-padding-right" for="pin"> 全国学籍号 </label>
				<div class="col-xs-12 col-sm-12 col-md-9">
					<span class="block input-icon input-icon-right">
					<input maxLength="20" type="text" name="pin" id="pin" oid="pin" placeholder="全国学籍号" class="form-control col-xs-10 col-sm-10 col-md-10 " value="${student.pin!}" />
					</span>
				</div>
			</div>
			
			<div class="form-group" id="form-group-sourceType" >
				<label class="ace-icon red fa fa-circle col-md-3 control-label no-padding-right" for="sourceType"> 生源类别 </label>
				<div class="col-xs-12 col-sm-12 col-md-9">
					<span class="block input-icon input-icon-right">
						<select  name="sourceType" id="sourceType" oid="sourceType"   data-placeholder="请选择" class="multiselect form-control col-md-10 col-sm-10 col-xs-10 " >		
							${mcodeSetting.getMcodeSelect("DM-DGXSLY", student.sourceType?default('')?string, "1")}
						</select>
					</span>
				</div>
			</div>
		</div>
		
	</div>
</div>
<#-- 确定和取消按钮 -->
<div class="row" style="margin-top:10px;">
	<div class="clearfix form-actions center">
		<@w.btn btnId="student-commit" btnClass="fa-check" btnValue="确定" />
		<@w.btn btnId="student-close" btnClass="fa-times" btnValue="取消" />
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
	initOperationCheck('.studentDetail');
	//初始化日期控件
	initCalendar(".studentDetail");
	// 取消按钮操作功能
	$("#student-close").on("click", function(){
		doLayerOk("#student-commit", {
		redirect:function(){},
		window:function(){layer.closeAll()}
		});		
	 });
	// 确定按钮操作功能
	var isSubmit=false;
	$("#student-commit").on("click", function(){
		if(isSubmit){
			return;
		}
		isSubmit=true;
		$(this).addClass("disabled");
		var checkVal = checkValue('.studentDetail');
		if(!checkVal){
			isSubmit=false;
			$(this).removeClass("disabled");
			return ;
		}
		var obj = new Object();
		// 获取此控件下所有的可输入内容，并组织成json格式
		obj = JSON.parse(dealValue('.studentDetail'));
		// 提交数据
	 	$.ajax({
		    url:'${request.contextPath}/basedata/student/save',
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
    					$("#student-commit").removeClass("disabled");
    					isSubmit=false;
    				});
		 		}else{
		 			// 显示成功信息
		 			layer.tips(jsonO.msg, "#student-commit", {tips: [4, '#228B22']});
		 			// 调用封装好的函数，此函数内支持移动端和非移动端，也可以自己写，
		 			// 需要区分移动端和非移动端返回处理不一样
	 				doLayerOk("#student-commit", {
						redirect:function(){},
						window:function(){
							setTimeout(function(){layer.closeAll();}, 1000);
							$("#studentList").trigger("reloadGrid");
			 			}
		 			});
    			}
		     }
		});
	 });
});	

</script>
