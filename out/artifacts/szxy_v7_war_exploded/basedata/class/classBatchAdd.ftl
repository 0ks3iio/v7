<#import "/fw/macro/webmacro.ftl" as w>
<div class="row classDetail" style="margin-top:10px;">
	<#-- 数据内容 -->
	<div class="clearfix">
		<input type="hidden" name="schoolId" id="schoolId" value="${dto.clazz.schoolId!}">
		<input type="hidden" name="acadyear" id="acadyear" value="${dto.clazz.acadyear!}">
		<input type="hidden" name="schoolingLength" id="schoolingLength" value="<#if dto.clazz.schoolingLength??>${dto.clazz.schoolingLength}</#if>">
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
					<div>
						<select <#if dto.clazz.id?default('')!=''>disabled</#if> name="gradeId" id="gradeId" oid="gradeId" nullable="false" data-placeholder="请选择" class="multiselect form-control col-md-10 col-sm-10 col-xs-10 " onchange="dochangeGra()">		
						</select>
					</div>
				</div>
			</div>
			<div class="form-group" id="form-group-schoolingLength" >
				<label class="ace-icon red fa fa-circle col-md-3 control-label no-padding-right" for="schoolingLength"> 学制 </label>
				<div class="col-xs-12 col-sm-12 col-md-9">
					<span class="block input-icon input-icon-right">
					<input readonly maxLength="10" type="text" oid="schoolingLength" class="form-control col-xs-10 col-sm-10 col-md-10 " placeholder="根据年级自动生成" value="${dto.clazz.schoolingLength!}" />
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
					<div>
						<select name="artScienceType" id="artScienceType" oid="artScienceType" nullable="false" data-placeholder="请选择" class="multiselect form-control col-md-10 col-sm-10 col-xs-10 ">		
							${mcodeSetting.getMcodeSelect("DM-BJWLLX", (dto.clazz.artScienceType?default(-1))?string, "1")}
						</select>
					</div>
				</div>
			</div>
			<div class="form-group " id="form-group-addClassCount">
				<label class="ace-icon red fa fa-circle col-md-3 control-label no-padding-right" for="addClassCount"> 新增班级数量 </label>
				<div class="col-xs-12 col-sm-12 col-md-9">
					<span class="block input-icon input-icon-right">
					<input min="1" max="20" minlength="0" length="0" vtype="int" maxlength="2" nullable="false" regex="" regextip="" type="text" id="addClassCount" oid="addClassCount" placeholder="新增班级数量" class="form-control col-xs-10 col-sm-10 col-md-10 " value="">
					</span>
				</div>
			</div>
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

<script type="text/javascript">
// 需要用到的js脚本，延迟加载
var scripts = [];
$('.page-content-area').ace_ajax('loadScripts', scripts, function() {
	// 初始化交互控件
	initOperationCheck('.classDetail');
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
		obj.addClassCount = $("#addClassCount").val();
		// 提交数据
	 	$.ajax({
		    url:'${request.contextPath}/basedata/class/batchSave',
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
	if(sectionMap[section]){
		var jsonO=sectionMap[section];
		$("#gradeId option").remove();
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
	    	$("#gradeId option").remove();
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
	$("#schoolingLength").val(gradeMap[gradeId].grade.schoolingLength);
}
</script>