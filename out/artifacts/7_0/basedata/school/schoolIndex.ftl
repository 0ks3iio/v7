<title>学校信息设置</title>
<#import "/fw/macro/webmacro.ftl" as w>
<#import "/fw/macro/treemacro.ftl" as t>
<#-- jqGrid报表引入文件 -->
<link rel="stylesheet" href="${request.contextPath}/static/ace/css/ui.jqgrid.css" />

<#-- sweetalert引入文件  -->
<link rel="stylesheet" href="${request.contextPath}/static/sweetalert/sweetalert.css" />
<script src="${request.contextPath}/static/sweetalert/sweetalert.min.js"></script>

<div class="row">
	<div class="col-lg-12 col-md-12" >
		<!-- PAGE CONTENT BEGINS -->
		<div class="well well-sm">
			相关功能：
			<@w.pageRef url="${request.contextPath}/basedata/school/index/page" name="学校信息设置" />
		</div>
	</div>
</div>

<div class="filter" id="searchDiv">
	<div class="filter-item pull-right">
		<@w.btn btnId="btn-showTeachArea" btnValue="查看校区" btnClass="fa-folder-o" title=""/>
	</div>
</div>

<div class="col-xs-12 col-sm-4"style="width:100%;float:none;">
	<div class="widget-box" >
		<div class="widget-header">
			<h4 class="widget-title">学校信息</h4>

			<div class="widget-toolbar">
				<a href="javascript:" data-action="collapse" id="schoolColl">
					<i class="ace-icon fa fa-chevron-up"></i>
				</a>
			</div>
		</div>

		<div class="widget-body">
			<div class="widget-main">
				<div class="clearfix schoolDetail">
				<input type="hidden" name="id" id="id" value="${dto.school.id!}">
				<div class="form-horizontal col-lg-6 col-sm-6 col-xs-12 col-md-6" role="form">
					<div class="form-group" id="form-group-schoolName" >
						<label class="ace-icon red fa fa-circle col-md-3 control-label no-padding-right" for="schoolName"> 学校名称 </label>
						<div class="col-xs-12 col-sm-12 col-md-9">
							<span class="block input-icon input-icon-right">
							<input readonly nullable="false" maxLength="60" type="text" name="schoolName" id="schoolName" oid="schoolName" class="form-control col-xs-10 col-sm-10 col-md-10 " value="${dto.school.schoolName!}" />
							</span>
						</div>
					</div>
					<div class="form-group" id="form-group-schoolCode" >
						<label class="ace-icon red fa fa-circle col-md-3 control-label no-padding-right" for="schoolCode"> 学校代码 </label>
						<div class="col-xs-12 col-sm-12 col-md-9">
							<span class="block input-icon input-icon-right">
							<input readonly nullable="false" maxLength="50" type="text" name="schoolCode" id="schoolCode" oid="schoolCode" class="form-control col-xs-10 col-sm-10 col-md-10 " value="${dto.school.schoolCode!}" />
							</span>
						</div>
					</div>
					<div class="form-group" id="form-group-regionCode" >
						<label class="ace-icon red fa fa-circle col-md-3 control-label no-padding-right" for="regionCode"> 所在地行政区 </label>
						<div class="col-xs-12 col-sm-12 col-md-9">
							<span class="block input-icon input-icon-right">
							<input readonly nullable="false" type="text" class="form-control col-xs-10 col-sm-10 col-md-10 " value="${dto.regionName!}" />
							<input type="hidden" name="regionCode" id="regionCode" oid="regionCode" value="${dto.school.regionCode!}" />
							</span>
						</div>
					</div>
					<div class="form-group" id="form-group-schoolType" >
						<label class="ace-icon red fa fa-circle col-md-3 control-label no-padding-right" for="schoolType"> 学校类别 </label>
						<div class="col-md-9">
							<select <#if (gradeSize?default(0)>0)> disabled </#if> name="schoolType" id="schoolType" oid="schoolType" nullable="false" data-placeholder="请选择" class="multiselect form-control col-md-10 col-sm-10 col-xs-10 " onchange="doChangeSchoolType()">		
								${mcodeSetting.getMcodeSelect("DM-XXLB", dto.school.schoolType?default(''), "1")}
							</select>
						</div>
					</div>
					<div class="form-group" id="form-group-runSchoolType" >
						<label class="ace-icon red fa fa-circle col-md-3 control-label no-padding-right" for="runSchoolType"> 学校办别 </label>
						<div class="col-md-9">
							<select disabled name="runSchoolType" id="runSchoolType" oid="runSchoolType" nullable="false" data-placeholder="请选择" class="multiselect form-control col-md-10 col-sm-10 col-xs-10 " >		
								${mcodeSetting.getMcodeSelect("DM-XXBB", (dto.school.runSchoolType?default(-1))?string, "1")}
							</select>
						</div>
					</div>
					
				</div>
				
				<div class="form-horizontal col-lg-6 col-sm-6 col-xs-12 col-md-6" role="form">
					<div class="form-group " id="form-group-infantYear" style="display:none">
						<label class="col-md-3 control-label no-padding-right" for="infantYear"> 幼儿园规定年制</label>
						<div class="col-xs-12 col-sm-12 col-md-9">
							<span class="block input-icon input-icon-right">
							<input min="0" max="9" minlength="0" length="0" vtype="int" maxlength="1" regex="" regextip="" type="text" id="infantYear" oid="infantYear" placeholder="幼儿园规定年制" class="form-control col-xs-10 col-sm-10 col-md-10 " value="<#if dto.school.infantYear??>${dto.school.infantYear?default(0)}</#if>">
							</span>
						</div>
					</div>
					<div class="form-group " id="form-group-gradeYear" style="display:none">
						<label class="col-md-3 control-label no-padding-right" for="gradeYear"> 小学规定年制</label>
						<div class="col-xs-12 col-sm-12 col-md-9">
							<span class="block input-icon input-icon-right">
							<input min="0" max="9" minlength="0" length="0" vtype="int" maxlength="1" regex="" regextip="" type="text" id="gradeYear" oid="gradeYear" placeholder="小学规定年制" class="form-control col-xs-10 col-sm-10 col-md-10 " value="<#if dto.school.gradeYear??>${dto.school.gradeYear?default(0)}</#if>">
							</span>
						</div>
					</div>
					<div class="form-group " id="form-group-juniorYear" style="display:none">
						<label class="col-md-3 control-label no-padding-right" for="juniorYear"> 初中规定年制</label>
						<div class="col-xs-12 col-sm-12 col-md-9">
							<span class="block input-icon input-icon-right">
							<input min="0" max="9" minlength="0" length="0" vtype="int" maxlength="1" regex="" regextip="" type="text" id="juniorYear" oid="juniorYear" placeholder="初中规定年制" class="form-control col-xs-10 col-sm-10 col-md-10 " value="<#if dto.school.juniorYear??>${dto.school.juniorYear?default(0)}</#if>">
							</span>
						</div>
					</div>
					<div class="form-group " id="form-group-seniorYear" style="display:none">
						<label class="col-md-3 control-label no-padding-right" for="seniorYear"> 高中规定年制</label>
						<div class="col-xs-12 col-sm-12 col-md-9">
							<span class="block input-icon input-icon-right">
							<input min="0" max="9" minlength="0" length="0" vtype="int" maxlength="1" regex="" regextip="" type="text" id="seniorYear" oid="seniorYear" placeholder="高中规定年制" class="form-control col-xs-10 col-sm-10 col-md-10 " value="<#if dto.school.seniorYear??>${dto.school.seniorYear?default(0)}</#if>">
							</span>
						</div>
					</div>
					
				</div>
				</div>
				<#-- 确定和取消按钮 -->
				<div class="" style="margin-top:10px;">
					<div class="clearfix form-actions center">
						<@w.btn btnId="school-commit" btnClass="fa-check" btnValue="保存" />
					</div>
				</div>
			</div>
		</div>
	</div>
</div>
<br>
<div class="filter" id="searchTeachAreaDiv" style="display:none">
	<div class="filter-item pull-right">
		<@w.btn btnId="btn-addTeachArea" btnValue="新增校区" btnClass="fa-plus" title="没有校区可不设置"/>
	</div>
</div>
<div class="row listDiv" id="teaAreaDiv" >	
</div>

<a href="#" id="btn-scroll-up" class="btn-scroll-up btn btn-sm btn-inverse">
	<i class="ace-icon fa fa-angle-double-up icon-only bigger-110"></i>
</a><!-- page specific plugin scripts -->
<script type="text/javascript">
	var schtypeAllMap={};
	<#if schtypeAllMap?? && (schtypeAllMap?size>0)>
		<#list schtypeAllMap?keys as key>
			schtypeAllMap['${key}']='${schtypeAllMap[key]}';
		</#list>
	</#if>
	<#assign sectionlin=dto.school.sections?default("")>
	<#assign sectionMore=sectionlin?split(",")>
	<#list sectionMore as item>
		<#if item=='0'>
			$("#form-group-infantYear").show();
		<#elseif item=='1'>
			$("#form-group-gradeYear").show();
		<#elseif item=='2'>
			$("#form-group-juniorYear").show();
		<#elseif item=='3'>
			$("#form-group-seniorYear").show();
		</#if>
	</#list>
	var indexDiv = 0;
	var scripts = [null, 
		"${request.contextPath}/static/ace/js/jqGrid/jquery.jqGrid.src.js",
		"${request.contextPath}/static/ace/js/jqGrid/i18n/grid.locale-cn.js",
		"${request.contextPath}/static/ace/assets/js/src/ace.scrolltop.js",		"${request.contextPath}/static/ace/assets/js/src/ace.widget-box.js",
		null];
	options_default = {
		width:1000
	};
	$('.page-content-area').ace_ajax('loadScripts', scripts, function() {
		// 初始化交互控件
		initOperationCheck('.schoolDetail');
		// 确定按钮操作功能
		var isSubmit=false;
		$("#school-commit").on("click", function(){
			if(isSubmit){
				return;
			}
			isSubmit=true;
			$(this).addClass("disabled");
			var check = checkValue('.schoolDetail');
			if(!check){
			 	$(this).removeClass("disabled");
			 	isSubmit=false;
			 	return;
			}
			var obj = new Object();
			// 获取此控件下所有的可输入内容，并组织成json格式
			// obj.school，是因为url所对应的接收对象是个dto，数据是存在dto.school
			obj.school = JSON.parse(dealValue('.schoolDetail'));
			// 提交数据
		 	$.ajax({
			    url:'${request.contextPath}/basedata/school/save',
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
	    					
	    				});
			 		}
			 		else{
			 			swal({
				 			title: "操作成功!",
			    			text: jsonO.msg,
			    			type: "success",
			    			showConfirmButton: true,
			    			confirmButtonText: "确定"
			    		}, function(){
	    					
	    				});
	    			}
	    			$("#school-commit").removeClass("disabled");
					isSubmit=false;
			     }
			});
		 });
		
		// 新增操作
		$("#btn-addTeachArea").on("click", function(){
			indexDiv = layerDivUrl("${request.contextPath}/basedata/teachArea/edit/page");
		});
		
		$("#btn-showTeachArea").on("click", function(){
			doQuery();
		});
		
	});
	function doQuery(){
		var url =  '${request.contextPath}/basedata/teachArea/unit/${unitId!}/list/page';
		$("#teaAreaDiv").load(url);
		$("#searchTeachAreaDiv").show();
		if($("#schoolColl .fa").hasClass("fa-chevron-up")){
			$("#schoolColl").click();
		}
	}
	function doChangeSchoolType(){
		var schoolType = $("#schoolType").val();
		var section = schtypeAllMap[schoolType];
		$("#form-group-infantYear").hide();
		$("#form-group-gradeYear").hide();
		$("#form-group-juniorYear").hide();
		$("#form-group-seniorYear").hide();
		if(section){
			var sections = section.split(",");
			for(var i=0;i < sections.length;i++){
				if(sections[i]==0){
					$("#form-group-infantYear").show();
				}else if(sections[i]==1){
					$("#form-group-gradeYear").show();
				}else if(sections[i]==2){
					$("#form-group-juniorYear").show();
				}else if(sections[i]==3){
					$("#form-group-seniorYear").show();
				}
			}
		}
	}
</script>
