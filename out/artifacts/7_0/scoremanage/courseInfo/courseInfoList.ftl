<div class="box-header">
	<h4 class="box-title">
		科目及分数设置
		<a href="javascript:" class="btn btn-sm btn-white js-layer-01" id="gradeCodeCopyOpen" style="display:none" onclick="gradeCodeCopyOpen()">复制科目到同学段</a>
	</h4>
	<div class="box-boder" id="gradeCodeCopyToDivId" style="display:none">
		<div class="multiselect" id="gradeCodeCopyTo">
		</div>
		<div class="page-btns" id="">
			<a href="javascript:" class="btn btn-blue" id="doGradeCodeCopyCourse" onClick="doGradeCodeCopyCourse()">确认</a>
			<span style="color:red">提醒：只复制保存后的数据，除了班级信息不会复制，其他都会复制，已设置的科目不会覆盖。</span>
		</div>
	</div>
</div>
<form id="myform">
<input type="hidden" name="examInfoId" value="${examId!}">
<div class="">
	<div class="add-exam add-exam-tbale clearfix courseInfoDivAll">
		<#if subjectInfoList?exists && (subjectInfoList?size>0)>
		<#list subjectInfoList as item>
		<div class="widget-box add-exam-item courseInfoDiv" id="item_${item_index}" <#if isCanEditClass>style="height:400px;"<#else>style="height:250px;"</#if>>
			<input type="hidden" value="${item.id!}" id="id_${item_index}" class="idClass" name="subjectInfoList[${item_index}].id">
			<input type="hidden" value="${item.unitId!}" id="unitId_${item_index}" class="unitIdClass" name="subjectInfoList[${item_index}].unitId">
			<input type="hidden" value="${item.examId!}" id="examId_${item_index}" class="examIdClass" name="subjectInfoList[${item_index}].examId">
			<input type="hidden" value="${item.rangeType!}" id="rangeType_${item_index}" class="rangeTypeClass" name="subjectInfoList[${item_index}].rangeType">
			<div class="widget-body" style="height:100%">
				<div class="widget-main padding-12">
					<#if isCanEditSubject ><a href="javascript:" class="add-exam-close delDiv" id="delDiv_${item_index}"><i class="fa fa-times-circle"></i></a></#if>
					<div class="filter">
						<div class="filter-item">
							<select <#if !isCanEditSubject >disabled</#if> class="form-control subjectIdClass" id="subjectId_${item_index}" name="subjectInfoList[${item_index}].subjectId" title="考试科目">
								<option value="">请选择考试科目</option>
								<#if courseList?exists && (courseList?size>0)>
								<#list courseList as citem>
									<option value="${citem.id!}" <#if citem.id==item.subjectId?default('')> selected="selected"</#if>>${citem.subjectName!}</option>
								</#list>
								</#if>
							</select>
						</div>
						<div class="filter-item">
							<select <#if !isCanEditSubject >disabled</#if> class="form-control inputTypeClass" id="inputType_${item_index}" name="subjectInfoList[${item_index}].inputType" title="成绩录入方式">
								${mcodeSetting.getMcodeSelect("DM-CJLRXSFS", "${item.inputType!}", "0")}
							</select>
						</div>
						<div class="filter-item">
							<select <#if !isCanEditSubject >disabled</#if> class="form-control examModeClass" id="examMode_${item_index}" name="subjectInfoList[${item_index}].examMode" title="考试方式">
								${mcodeSetting.getMcodeSelect("DM-KSFS", "${item.examMode!}", "0")}
							</select>
						</div>
						<#if isCanEditClass>
						<div class="filter-item">
							<div style="display: inline-block; width:85%">
							<select multiple id="classIds_${item_index}" name="subjectInfoList[${item_index}].classIds" class="tag-input-style classIdsClass"  data-placeholder="选择行政班">
							</select>
							</div>
							<a href="javascript:;" class="copyClassIdClass" data-toggle="tooltip" data-placement="top" title="复制第一个设置的行政班"><i class="fa fa-copy"></i></a>
							<a href="javascript:;" class="selectAllClassIdClass" data-toggle="tooltip" data-placement="top" title="全选行政班"><i class="fa fa-check-square-o"></i></a>
						</div>
						<div class="filter-item" >
							<div style="display: inline-block; width:85%">
							<select multiple id="teachClassIds_${item_index}" name="subjectInfoList[${item_index}].teachClassIds" class="tag-input-style teachClassIdsClass"  data-placeholder="选择教学班">
							</select>
							</div>
							<a href="javascript:;" class="copyTeachClassIdClass" data-toggle="tooltip" data-placement="top" title="复制第一个设置的教学班"><i class="fa fa-copy"></i></a>
							<a href="javascript:;" class="selectAllTeachClassIdClass" data-toggle="tooltip" data-placement="top" title="全选教学班"><i class="fa fa-check-square-o"></i></a>
						</div>
						</#if>
						<div class="filter-item gradeTypeDivClass" <#if item.id?default('')=='' || item.inputType?default('')=='S'>style="display:none"</#if>>
							<select <#if !isCanEditSubject >disabled</#if> id="gradeType_${item_index}" name="subjectInfoList[${item_index}].gradeType" class="form-control gradeTypeClass" data-placeholder="选择等第显示分等" title="等第显示分等">
								<option value="">选择等第显示分等(不能为空)</option>
								${mcodeSetting.getMcodeSelect("DM-DDMC", item.gradeType?default(''), "0")}
							</select>
						</div>
						<div class="filter-item inputDivClass" <#if item.inputType?default('')=='G'>style="display:none"</#if>>
							<input <#if !isCanEditSubject >readonly</#if> type="text" class="form-control fullScoreClass" placeholder="请输入满分值(不能为空)" title="满分值"
							id="fullScore_${item_index}" name="subjectInfoList[${item_index}].fullScore"
							 value="<#if item.fullScore??>#{item.fullScore?default(0);M2}</#if>">
						</div>
					</div>
				</div>
			</div>
		</div>
		</#list>
		</#if>
		<#if isCanEditSubject >
		<div class="widget-box add-exam-item box-bluebg" <#if isCanEditClass>style="height:400px;"<#else>style="height:250px;"</#if>>
			<a class="add-exam-btn" href="javascript:">
				<i class="fa fa-plus fa-3x"></i>
				<p>新增科目及分数</p>
			</a>
		</div>
		</#if>
	</div>
</div>
</form>
<div id="addhideDiv" class="hide">
	<div class="widget-box add-exam-item" <#if isCanEditClass>style="height:400px;"<#else>style="height:250px;"</#if>>
		<input type="hidden" value="" id="id" class="idClass" name="id">
		<input type="hidden" value="${unitId!}" id="unitId" class="unitIdClass" name="unitId">
		<input type="hidden" value="${examId!}" id="examId" class="examIdClass" name="examId">
		<input type="hidden" value="${gradeCode!}" id="rangeType" class="rangeTypeClass" name="rangeType">
		<div class="widget-body" style="height:100%">
			<div class="widget-main padding-12">
				<a href="javascript:" class="add-exam-close delDiv"><i class="fa fa-times-circle"></i></a>
				<div class="filter">
					<div class="filter-item">
						<select class="form-control subjectIdClass" id="subjectId" name="subjectId" title="考试科目">
							<option value="">请选择考试科目</option>
							<#if courseList?exists && (courseList?size>0)>
							<#list courseList as citem>
								<option value="${citem.id!}">${citem.subjectName!}</option>
							</#list>
							</#if>
						</select>
					</div>
					<div class="filter-item">
						<select class="form-control inputTypeClass" id="inputType" name="inputType" title="成绩录入方式">
							${mcodeSetting.getMcodeSelect("DM-CJLRXSFS", "S", "0")}
						</select>
					</div>
					<div class="filter-item">
						<select class="form-control examModeClass" id="examMode" name="examMode" title="考试方式">
							${mcodeSetting.getMcodeSelect("DM-KSFS", "1", "0")}
						</select>
					</div>
					<#if isCanEditClass>
					<div class="filter-item">
						<div style="display: inline-block; width:85%">
						<select multiple id="classIds" name="classIds" class="tag-input-style classIdsClass"  data-placeholder="选择行政班">
						</select>
						</div>
						<a href="javascript:;" class="copyClassIdClass" data-toggle="tooltip" data-placement="top" title="复制第一个设置的行政班"><i class="fa fa-copy"></i></a>
						<a href="javascript:;" class="selectAllClassIdClass" data-toggle="tooltip" data-placement="top" title="全选行政班"><i class="fa fa-check-square-o"></i></a>
					</div>
					<div class="filter-item" >
						<div style="display: inline-block; width:85%">
						<select multiple id="teachClassIds" name="teachClassIds" class="tag-input-style teachClassIdsClass"  data-placeholder="选择教学班">
						</select>
						</div>
						<a href="javascript:;" class="copyTeachClassIdClass" data-toggle="tooltip" data-placement="top" title="复制第一个设置的教学班"><i class="fa fa-copy"></i></a>
						<a href="javascript:;" class="selectAllTeachClassIdClass" data-toggle="tooltip" data-placement="top" title="全选行政班"><i class="fa fa-check-square-o"></i></a>
					</div>
					</#if>
					<div class="filter-item gradeTypeDivClass" style="display:none">
						<select id="gradeType" name="gradeType" class="form-control gradeTypeClass" data-placeholder="选择等第显示分等" title="等第显示分等">
							<option value="">选择等第显示分等(不能为空)</option>
							${mcodeSetting.getMcodeSelect("DM-DDMC", "1", "0")}
						</select>
					</div>
					<div class="filter-item inputDivClass">
						<input type="text" class="form-control fullScoreClass" placeholder="请输入满分值(不能为空)" title="满分值"
						id="fullScore" name="fullScore"
						 value="">
					</div>
				</div>
			</div>
		</div>
	</div>
</div>

<script type="text/javascript">
//页面科目设置数量
var index=-1;
<#if subjectInfoList?exists && (subjectInfoList?size>0)>
	$("#showDiv").show();
	index=${subjectInfoList?size-1};
<#elseif isCanEditSubject>
	$("#showDiv").show();
<#else>
	layer.tips('未查询到考试科目!', $("#examIdSearch"), {
		tipsMore: true,
		tips: 2
	});
	$("#showDiv").hide();
</#if>
<#if isCanEditSubject && subjectInfoList?exists && (subjectInfoList?size>0)>
	$("#gradeCodeCopyOpen").show();
</#if>
//selectSubjectMaxScoreMap 科目默认最大值
var selectSubjectMaxScoreMap={};
<#if courseList?exists && (courseList?size>0)>
	<#list courseList as citem>
		selectSubjectMaxScoreMap['${citem.id}']='${citem.fullMark?default(0)}';
	</#list>
</#if>

//所有  key:subjectId value:行政班
var selectClassMap={};
//所有   key:subjectId value:教学班
var selectTeachClassMap={};
//选中     key:subjectId classId value:classId
var subjectIdClassMap={};
var subjectIdTeachClassMap={};
$(function(){
	var section = oldGradeCode.substr(0,1);
	var sameSection=0;
	$("#gradeCodeSearch").find(".label-select-item").each(function(){
		var data_value=$(this).attr("data-value");
		if(section == data_value.substr(0,1) && oldGradeCode != data_value){
			$("#gradeCodeCopyTo").append($(this).clone());
			sameSection=sameSection+1;
		}
	});
	if(sameSection==0){
		//没有同学段年级 同学段复制按钮隐藏
		$("#gradeCodeCopyOpen").hide();
	}
	//同学段复制列表事件
	$("#gradeCodeCopyTo .label-select-item").on('click',function(){
		if($(this).hasClass('active')){
			$(this).removeClass('active');
		}else{
			$(this).addClass('active');
		}
	});
	<#if subjectInfoList?exists && (subjectInfoList?size>0)>
	  <#list subjectInfoList as item>
	  	<#list item.classIdsMap?keys as key>
	  		if(!selectClassMap['${item.subjectId}']){
	  			selectClassMap['${item.subjectId}']={};
	  		}
	  		selectClassMap['${item.subjectId}']['${key}']='${item.classIdsMap[key]}';
	  	</#list>
	  	<#list item.teachClassIdsMap?keys as key>
	  		if(!selectTeachClassMap['${item.subjectId}']){
	  			selectTeachClassMap['${item.subjectId}']={};
	  		}
	  		selectTeachClassMap['${item.subjectId}']['${key}']='${item.teachClassIdsMap[key]}';
	  	</#list>
	  	choosens(${item_index});
	  	init(${item_index},'${item.id!}');
	  </#list>
	</#if>
	
	doBinding();
});
//点击复制到同学段  隐藏部分展现
function gradeCodeCopyOpen(){
	if($("#gradeCodeCopyToDivId").hasClass('gradeCodeIsOpen')){
		$("#gradeCodeCopyToDivId").hide();
		$("#gradeCodeCopyToDivId").removeClass('gradeCodeIsOpen');
	}else{
		$("#gradeCodeCopyToDivId").show();
		$("#gradeCodeCopyToDivId").addClass('gradeCodeIsOpen');
	}
}
//复制到同学段确定按钮
var isCopy=false;
function doGradeCodeCopyCourse(){
	if(isCopy){
		return;
	}
	isCopy = true;
	var toGradeCode = [];
	var ind = 0;
	$("#gradeCodeCopyTo .active").each(function(){
		toGradeCode[ind++] = $(this).attr('data-value');
	});
	if(toGradeCode.length == 0){
		layer.tips('请选择年级后操作!', $("#doGradeCodeCopyCourse"), {
			time:1500,
			tipsMore: true,
			tips: 2
		});
		isCopy = false;
		return;
	}
	$("#doGradeCodeCopyCourse").addClass("disabled");
	var layerIndex = layer.load();
	$.ajax({
		url:'${request.contextPath}/scoremanage/courseInfo/copyGradeCourse',
		data: {'examId':oldExamId,'gradeCode':oldGradeCode,'toGradeCode':toGradeCode},
		type:'post',
		traditional: true,
		success:function(data) {
			var jsonO = JSON.parse(data);
	 		if(jsonO.success){
	 			layer.tips('复制成功!', $("#gradeCodeCopyOpen"), {
					tipsMore: true,
					tips: 2
				});
				$("#gradeCodeCopyToDivId").hide();
	 		}
	 		else{
	 			layerTipMsg(jsonO.success,"失败",jsonO.msg);
			}
			isCopy = false;
			layer.close(layerIndex);
			$("#doGradeCodeCopyCourse").removeClass("disabled");
		},
 		error : function(XMLHttpRequest, textStatus, errorThrown) {}
	});
}

//班级全选 复制事件
function doBinding(){
	//复制事件
	$(".courseInfoDivAll").on('click','.copyClassIdClass',function(){
		var classIds=$(".courseInfoDiv:first").find(".classIdsClass").val();
		var opts=$(this).parent().find(".classIdsClass option");
		if(opts && opts.length>0 && classIds.length>0){
			$.each($(this).parent().find(".classIdsClass option"),function(){
				for(var j=0;j<classIds.length;j++){
					if($(this).val()==classIds[j]){
						$(this).attr("selected","selected");
					}
				}
			});
			$('#'+$(this).parent().find(".classIdsClass").attr('id')).trigger("chosen:updated");
		}
		layer.tips('复制成功!', $(this), {
			tipsMore: true,
			tips: 2
		});
	});
	//复制事件
	$(".courseInfoDivAll").on('click','.copyTeachClassIdClass',function(){
		var teachClassIds=$(".courseInfoDiv:first").find(".teachClassIdsClass").val();
		var opts=$(this).parent().find(".teachClassIdsClass option");
		if(opts && opts.length>0 && teachClassIds!=null && teachClassIds.length>0){
			$.each($(this).parent().find(".teachClassIdsClass option"),function(){
				for(var j=0;j<teachClassIds.length;j++){
					if($(this).val()==teachClassIds[j]){
						$(this).attr("selected","selected");
					}
				}
			});
			$('#'+$(this).parent().find(".teachClassIdsClass").attr('id')).trigger("chosen:updated");
		}
		layer.tips('复制成功!', $(this), {
			tipsMore: true,
			tips: 2
		});
	});
	//全选事件
	$(".courseInfoDivAll").on('click','.selectAllClassIdClass',function(){
		var classIdsSel = $(this).parent(".filter-item").find(".classIdsClass");
		classIdsSel.children("option").each(function(){
			if(!$(this).is(":selected")){
				$(this).attr("selected","selected");
			}
		});
		$('#'+classIdsSel.attr('id')).trigger("chosen:updated");
		layer.tips('全选成功!', $(this), {
			tipsMore: true,
			tips: 2
		});
	});
	//全选事件
	$(".courseInfoDivAll").on('click','.selectAllTeachClassIdClass',function(){
		var teachClassIdsSel = $(this).parent(".filter-item").find(".teachClassIdsClass");
		teachClassIdsSel.children("option").each(function(){
			if(!$(this).is(":selected")){
				$(this).attr("selected","selected");
			}
		});
		$('#'+teachClassIdsSel.attr('id')).trigger("chosen:updated");
		layer.tips('全选成功!', $(this), {
			tipsMore: true,
			tips: 2
		});
	});
}


//数据库删除操作
function doDeleteById(ids,obj){
	var sendIds;
	if(ids instanceof Array){
		sendIds='';
		for(var i=0;i<ids.length;i++){
			sendIds+=","+ids[i];
		}
		sendIds=sendIds.substring(1,sendIds.length);
	}else{
		sendIds=ids;
	}
	layer.load();
	$.ajax({
		url:'${request.contextPath}/scoremanage/courseInfo/delete',
		data: {'id':sendIds,'examId':oldExamId},
		type:'post',
		success:function(data) {
			var jsonO = JSON.parse(data);
 			layer.closeAll();
	 		if(jsonO.success){
				obj.parent().parent().parent().remove();
	 		}
	 		else{
	 			layer.closeAll();
	 			layerTipMsg(jsonO.success,"失败",jsonO.msg);
			}
		},
 		error : function(XMLHttpRequest, textStatus, errorThrown) {}
	});
}

//保存
var isSaveSubmit=false;
function saveInfo(){
	if(isSaveSubmit){
		return;
	}
	isSaveSubmit = true;
	$("#courseInfo-commit").addClass("disabled");
	var b=false;
	var couSelMap={};
	$(".courseInfoDiv").each(function(){
		//各种校验
		var subjectId = $(this).find(".subjectIdClass").val();
		if(subjectId==''){
			layer.tips('不能为空!', $(this).find(".subjectIdClass"), {
				tipsMore: true,
				tips: 3
			});
			if(!b){
				$(this).find(".subjectIdClass").focus();
				b=true;
			}
		}
		if(couSelMap[subjectId]){
			layer.tips('科目不可重复!', $(this).find(".subjectIdClass"), {
				tipsMore: true,
				tips: 3
			});
			if(!b){
				$(this).find(".subjectIdClass").focus();
				b=true;
			}
		}else{
			couSelMap[subjectId]=subjectId;
		}
		var inputType = $(this).find(".inputTypeClass").val();
		if(inputType == 'S'){
			var fullScore = $(this).find(".fullScoreClass").val($.trim($(this).find(".fullScoreClass").val())).val();
			var reg = /^(([0-9])|([1-9][0-9]{1,2})|([0-9]\.[0-9]{1})|([1-9][0-9]{1,2}\.[0-9]{1}))$/;
			var r;
			if(fullScore==''){
				layer.tips('不能为空!', $(this).find(".fullScoreClass"), {
					tipsMore: true,
					tips: 3
				});
				if(!b){
					$(this).find(".fullScoreClass").focus();
					b=true;
				}
			}else{
				r = fullScore.match(reg);
				if(r==null){
					layer.tips('格式不正确(最多3位整数，1位小数)!', $(this).find(".fullScoreClass"), {
						tipsMore: true,
						tips: 3
					});
					if(!b){
						$(this).find(".fullScoreClass").focus();
						b=true;
					}
				}else{
					if(!(fullScore>0 && fullScore<1000)){
						layer.tips('必须大于0小于1000', $(this).find(".fullScoreClass"), {
							tipsMore: true,
							tips: 3
						});
						if(!b){
							$(this).find(".fullScoreClass").focus();
							b=true;
						}
					}
				}
			}
		}else{
			var gradeType = $(this).find(".gradeTypeClass").val();
			if(gradeType==''){
				layer.tips('不能为空!', $(this).find(".gradeTypeClass"), {
					tipsMore: true,
					tips: 3
				});
				if(!b){
					$(this).find(".gradeTypeClass").focus();
					b=true;
				}
			}
		}
		
	});
	if(b){
		isSaveSubmit = false;
		$("#courseInfo-commit").removeClass("disabled");
		return;
	}
	var layerIndex = layer.load();
	var options = {
		url : "${request.contextPath}/scoremanage/courseInfo/list/save",
		dataType : 'json',
		success : function(data){
			$("#courseInfo-commit").removeClass("disabled");
			isSaveSubmit = false;
 			if(!data.success){
 				layerTipMsg(data.success,"失败",data.msg);
 			}else{
// 				layerTipMsg(data.success,"成功",data.msg);
				oldQuery();
				layer.close(layerIndex);
 				layer.msg(data.msg);
 			}
 			layer.close(layerIndex);
		},
		clearForm : false,
		resetForm : false,
		type : 'post',
		error:function(XMLHttpRequest, textStatus, errorThrown){} 
	};
	$("#myform").ajaxSubmit(options);
}



//新增考试科目设置

var isAddSubmit=false;
$(".add-exam-btn").on("click",function(){
	if(isAddSubmit){
		return;
	}
	isAddSubmit = true;
	if(index > -1){
		var hasIndex = -1;
		//取最大值
		for(var i=index;i>-1;i--){
			if($("#item_"+i).html()){
				hasIndex = i;
				break;
			}
		}
		
		if(hasIndex>-1){
			$(this).parent().before($("#item_"+hasIndex).clone());
			index++;
			$(".courseInfoDiv:last").attr("id","item_"+index);
			
			var s0="subjectInfoList["+index+"].id";
			var s00="id_"+index;
			var ss00="subjectInfoList["+index+"].examId";
			var ss0000="examId_"+index;
			var sss00="subjectInfoList["+index+"].rangeType";
			var sss0000="rangeType_"+index;
			var ssss00="subjectInfoList["+index+"].unitId";
			var ssss0000="unitId_"+index;
			
			var del0="delDiv_"+index;
			
			var s1="subjectInfoList["+index+"].subjectId";
			var s11="subjectId_"+index;
			var s2="subjectInfoList["+index+"].fullScore"
			var s22="fullScore_"+index;
			var s6="subjectInfoList["+index+"].classIds";
			var s66="classIds_"+index;
			var s7="subjectInfoList["+index+"].teachClassIds";
			var s77="teachClassIds_"+index;
			var s8="subjectInfoList["+index+"].isLock";
			var s88="isLock_"+index;
			var s9="subjectInfoList["+index+"].gradeType";
			var s99="gradeType_"+index;
			
			var ss11="subjectInfoList["+index+"].examMode";
			var ss1111="examMode_"+index;
			var ss12="subjectInfoList["+index+"].inputType";
			var ss1212="inputType_"+index;
			//var ss13="subjectInfoList["+index+"].examDate";
			//var ss1313="examDate_"+index;
			
			$("#item_"+index).find(".chosen-container").remove();
			
			$("#item_"+index).find(".idClass").attr("name",s0).attr("id",s00);
			$("#item_"+index).find(".idClass").val('');
			$("#item_"+index).find(".examIdClass").attr("name",ss00).attr("id",ss0000);
			$("#item_"+index).find(".rangeTypeClass").attr("name",sss00).attr("id",sss0000);
			$("#item_"+index).find(".unitIdClass").attr("name",ssss00).attr("id",ssss0000);
			
			$("#item_"+index).find(".delDiv").attr("id",del0);
			
			$("#item_"+index).find(".subjectIdClass").attr("name",s1).attr("id",s11);
			$("#item_"+index).find(".subjectIdClass").val("");
			$("#item_"+index).find(".examModeClass").attr("name",ss11).attr("id",ss1111);
			$("#item_"+index).find(".examModeClass").val($("#item_"+hasIndex).find(".examModeClass").val());
			$("#item_"+index).find(".inputTypeClass").attr("name",ss12).attr("id",ss1212);
			$("#item_"+index).find(".inputTypeClass").val($("#item_"+hasIndex).find(".inputTypeClass").val());
			//$("#item_"+index).find(".examDateClass").attr("name",ss13).attr("id",ss1313);
			
			$("#item_"+index).find(".fullScoreClass").attr("name",s2).attr("id",s22);
			$("#item_"+index).find(".classIdsClass").attr("name",s6).attr("id",s66);
			$("#item_"+index).find(".classIdsClass option").remove();

			$("#item_"+index).find(".teachClassIdsClass").attr("name",s7).attr("id",s77);
			$("#item_"+index).find(".teachClassIdsClass option").remove();
			
			
			$("#item_"+index).find(".isLockClass").attr("name",s8).attr("id",s88);
			$("#item_"+index).find(".isLockClass").val($("#item_"+hasIndex).find(".isLockClass").val());
			$("#item_"+index).find(".gradeTypeClass").attr("name",s9).attr("id",s99);
			$("#item_"+index).find(".gradeTypeClass").val($("#item_"+hasIndex).find(".gradeTypeClass").val());
			choosens(index);
			//doBinding();
			$('#delDiv_'+index).on('click',function(){
				$(this).parent().parent().parent().remove();
			});
			isAddSubmit = false;
			return;
		}
	}
	index++;

	$("#addhideDiv").find(".widget-box").attr("id","item_"+index);
	$("#addhideDiv").find(".widget-box").addClass("courseInfoDiv");
	var s0="subjectInfoList["+index+"].id";
	var s00="id_"+index;
	var ss00="subjectInfoList["+index+"].examId";
	var ss0000="examId_"+index;
	var sss00="subjectInfoList["+index+"].rangeType";
	var sss0000="rangeType_"+index;
	var ssss00="subjectInfoList["+index+"].unitId";
	var ssss0000="unitId_"+index;
	
	var del0="delDiv_"+index;
	
	var s1="subjectInfoList["+index+"].subjectId";
	var s11="subjectId_"+index;
	var s2="subjectInfoList["+index+"].fullScore"
	var s22="fullScore_"+index;
	var s6="subjectInfoList["+index+"].classIds";
	var s66="classIds_"+index;
	var s7="subjectInfoList["+index+"].teachClassIds";
	var s77="teachClassIds_"+index;
	var s8="subjectInfoList["+index+"].isLock";
	var s88="isLock_"+index;
	var s9="subjectInfoList["+index+"].gradeType";
	var s99="gradeType_"+index;
	
	var ss11="subjectInfoList["+index+"].examMode";
	var ss1111="examMode_"+index;
	var ss12="subjectInfoList["+index+"].inputType";
	var ss1212="inputType_"+index;
	//var ss13="subjectInfoList["+index+"].examDate";
	//var ss1313="examDate_"+index;
	
	$("#addhideDiv").find("input[name='id']").attr("name",s0).attr("id",s00);
	$("#addhideDiv").find("input[name='examId']").attr("name",ss00).attr("id",ss0000);
	$("#addhideDiv").find("input[name='rangeType']").attr("name",sss00).attr("id",sss0000);
	$("#addhideDiv").find("input[name='unitId']").attr("name",ssss00).attr("id",ssss0000);
	
	$("#addhideDiv").find(".delDiv").attr("id",del0);
	
	$("#addhideDiv").find("select[name='subjectId']").attr("name",s1).attr("id",s11);
	
	$("#addhideDiv").find("select[name='examMode']").attr("name",ss11).attr("id",ss1111);
	$("#addhideDiv").find("select[name='inputType']").attr("name",ss12).attr("id",ss1212);
	//$("#addhideDiv").find("input[name='examDate']").attr("name",ss13).attr("id",ss1313);
	
	$("#addhideDiv").find("input[name='fullScore']").attr("name",s2).attr("id",s22);
	$("#addhideDiv").find("select[name='classIds']").attr("name",s6).attr("id",s66);
	$("#addhideDiv").find("select[name='teachClassIds']").attr("name",s7).attr("id",s77);
	$("#addhideDiv").find("select[name='isLock']").attr("name",s8).attr("id",s88);
	$("#addhideDiv").find("select[name='gradeType']").attr("name",s9).attr("id",s99);
	
	var $exam_item=$("#addhideDiv").html();
	$(this).parent().before($exam_item);
	choosens(index);
	//initCalendar(".listDiv");
	$("#addhideDiv").find(".widget-box").attr("id","");
	$("#addhideDiv").find("input[name='"+s0+"']").attr("name","id").attr("id","id");
	$("#addhideDiv").find(".widget-box").removeClass("courseInfoDiv");
	$("#addhideDiv").find("input[name='"+ss00+"']").attr("name","examId").attr("id","examId");
	$("#addhideDiv").find("input[name='"+sss00+"']").attr("name","rangeType").attr("id","rangeType");
	$("#addhideDiv").find("input[name='"+ssss00+"']").attr("name","unitId").attr("id","unitId");
	
	$("#addhideDiv").find(".delDiv").attr("id","");
	
	$("#addhideDiv").find("select[name='"+s1+"']").attr("name","subjectId").attr("id","subjectId");
	
	$("#addhideDiv").find("select[name='"+ss11+"']").attr("name","examMode").attr("id","examMode");
	$("#addhideDiv").find("select[name='"+ss12+"']").attr("name","inputType").attr("id","inputType");
	//$("#addhideDiv").find("input[name='"+ss13+"']").attr("name","examDate").attr("id","examDate");
	
	$("#addhideDiv").find("input[name='"+s2+"']").attr("name","fullScore").attr("id","fullScore");
	$("#addhideDiv").find("select[name='"+s6+"']").attr("name","classIds").attr("id","classIds");
	$("#addhideDiv").find("select[name='"+s7+"']").attr("name","teachClassIds").attr("id","teachClassIds");
	$("#addhideDiv").find("select[name='"+s8+"']").attr("name","isLock").attr("id","isLock");
	$("#addhideDiv").find("select[name='"+s9+"']").attr("name","gradeType").attr("id","gradeType");
	
	$('#delDiv_'+index).on('click',function(){
		$(this).parent().parent().parent().remove();
	});
	isAddSubmit = false;
});



//初始化一个考试科目设置列表   班级多选框初始化  考试等第分数切换事件  切换科目改变班级列表以及满分值事件
function choosens(ii){
	chosenClass('#classIds_'+ii);
	chosenTeachClass('#teachClassIds_'+ii);
	
	$("#inputType_"+ii).on("change",function(){
		var inputType=$(this).val();
		if(inputType=='S'){
			$(this).parent().parent().parent().find(".inputDivClass").show();
			$(this).parent().parent().parent().find(".gradeTypeDivClass").hide();
		}else if(inputType=='G'){
			$(this).parent().parent().parent().find(".inputDivClass").hide();
			$(this).parent().parent().parent().find(".gradeTypeDivClass").show();
		}
	});
	
	$("#subjectId_"+ii).on("change",function(){
		var subjectId = $(this).val();
		//满分默认值
		if(selectSubjectMaxScoreMap[subjectId]){
			if(selectSubjectMaxScoreMap[subjectId]!='0'){
				$("#fullScore_"+ii).val(selectSubjectMaxScoreMap[subjectId]);
			}
		}
		$("#classIds_"+ii+" option").remove();
		$("#teachClassIds_"+ii+" option").remove();
		if(subjectIdClassMap[subjectId] || subjectIdTeachClassMap[subjectId]){
			if(subjectIdClassMap[subjectId]){
				var jsonO = subjectIdClassMap[subjectId];
				$.each(jsonO,function(index){
		    		var htmlOption="<option ";
		    		htmlOption+=" value='"+jsonO[index].id+"'>"+jsonO[index].name;
		    		htmlOption+="</option>";
		    		$("#classIds_"+ii).append(htmlOption);
		    	});
			}
			if(subjectIdTeachClassMap[subjectId]){
				var jsonO = subjectIdTeachClassMap[subjectId];
				$.each(jsonO,function(index){
		    		var htmlOption="<option ";
		    		htmlOption+=" value='"+jsonO[index].id+"'>"+jsonO[index].name;
		    		htmlOption+="</option>";
		    		$("#teachClassIds_"+ii).append(htmlOption);
		    	});
			}
			$('#classIds_'+ii).trigger("chosen:updated");
			$('#teachClassIds_'+ii).trigger("chosen:updated");
	    	return;
		}
		var layerIndex = layer.load();
		$.ajax({
		    url:'${request.contextPath}/scoremanage/courseInfo/findClass',
		    data: {'acadyear':oldAcadyear,'semester':oldSemester,'subjectId':subjectId,'gradeCode':oldGradeCode,'showAllClass': '${showAllClass}'},
		    type:'post',  
		    success:function(data) {
		    	var jsonArr = JSON.parse(data);
		    	var jsonO=jsonArr[0];
		    	subjectIdClassMap[subjectId]=jsonO;
				$.each(jsonO,function(index){
		    		var htmlOption="<option ";
		    		htmlOption+=" value='"+jsonO[index].id+"'>"+jsonO[index].name;
		    		htmlOption+="</option>";
		    		$("#classIds_"+ii).append(htmlOption);
		    	});
		    	jsonO=jsonArr[1];
		    	subjectIdTeachClassMap[subjectId]=jsonO;
				$.each(jsonO,function(index){
		    		var htmlOption="<option ";
		    		htmlOption+=" value='"+jsonO[index].id+"'>"+jsonO[index].name;
		    		htmlOption+="</option>";
		    		$("#teachClassIds_"+ii).append(htmlOption);
		    	});
		    	$('#classIds_'+ii).trigger("chosen:updated");
				$('#teachClassIds_'+ii).trigger("chosen:updated");
				layer.close(layerIndex);
		    }
		});
	});
	
}

//行政班选择框初始化
function chosenClass(id){
	$(id).chosen({
		width:'100%',
		results_height:'200px',
		multi_container_height:'80px',
		no_results_text:"未找到",//无搜索结果时显示的文本
		allow_single_deselect:true,//是否允许取消选择
		disable_search:true, //是否有搜索框出现
		search_contains:true,//模糊匹配，false是默认从第一个匹配
		//max_selected_options:1 //当select为多选时，最多选择个数
	});
	$('.chosen-choices').each(function(){
		$(this)[0].style.cssText = "height:60px !important;overflow:auto";
	});
}
//教学班选择框初始化 
function chosenTeachClass(id){
	$(id).chosen({
		width:'100%',
		results_height:'200px',
		multi_container_height:'80px',
		no_results_text:"未找到",//无搜索结果时显示的文本
		allow_single_deselect:true,//是否允许取消选择
		disable_search:true, //是否有搜索框出现
		search_contains:true,//模糊匹配，false是默认从第一个匹配
		//max_selected_options:1 //当select为多选时，最多选择个数
	});
	$('.chosen-choices').each(function(){
		$(this)[0].style.cssText = "height:90px !important;overflow:auto";
	});
}


//第一次已有科目设置时初始化  删除事件 给班级置值
function init(ii,id){
	if(id!=''){
		$('#delDiv_'+ii).on('click',function(){
			var id=$(this).parent().parent().parent().find(".idClass").val();
			if(id == ''){
				$(this).parent().parent().parent().remove();
				return;
			}
			var obj=$(this);
			showConfirmMsg('确认删除已保存的记录？','提示',function(){
				doDeleteById(id,obj);
			});
		});
	}else{
		$('#delDiv_'+ii).on('click',function(){
			$(this).parent().parent().parent().remove();
		});
	}
	var subjectId = $("#subjectId_"+ii).val();
	if(subjectIdClassMap[subjectId] || subjectIdTeachClassMap[subjectId]){
		if(subjectIdClassMap[subjectId]){
			var jsonO = subjectIdClassMap[subjectId];
			$.each(jsonO,function(index){
	    		var htmlOption="<option ";
	    		if(selectClassMap[subjectId] && selectClassMap[subjectId][jsonO[index].id]){
	    			htmlOption+="selected";
	    		}
	    		htmlOption+=" value='"+jsonO[index].id+"'>"+jsonO[index].name;
	    		htmlOption+="</option>";
	    		$("#classIds_"+ii).append(htmlOption);
	    	});
		}
		if(subjectIdTeachClassMap[subjectId]){
			var jsonO = subjectIdTeachClassMap[subjectId];
			$.each(jsonO,function(index){
	    		var htmlOption="<option ";
	    		if(selectTeachClassMap[subjectId] && selectTeachClassMap[subjectId][jsonO[index].id]){
	    			htmlOption+="selected";
	    		}
	    		htmlOption+=" value='"+jsonO[index].id+"'>"+jsonO[index].name;
	    		htmlOption+="</option>";
	    		$("#teachClassIds_"+ii).append(htmlOption);
	    	});
		}
		$('#classIds_'+ii).trigger("chosen:updated");
		$('#teachClassIds_'+ii).trigger("chosen:updated");
    	return;
	}
	var layerIndex = layer.load();
	$.ajax({
	    url:'${request.contextPath}/scoremanage/courseInfo/findClass',
	    data: {'acadyear':oldAcadyear,'semester':oldSemester,'subjectId':subjectId,'gradeCode':oldGradeCode,'isgkExamType':'${isgkExamType!}','showAllClass': '${showAllClass}'},  
	    type:'post',  
	    success:function(data) {
	    	var jsonArr = JSON.parse(data);
	    	var jsonO=jsonArr[0];
	    	subjectIdClassMap[subjectId]=jsonO;
			$.each(jsonO,function(index){
	    		var htmlOption="<option ";
	    		if(selectClassMap[subjectId] && selectClassMap[subjectId][jsonO[index].id]){
	    			htmlOption+="selected";
	    		}
	    		htmlOption+=" value='"+jsonO[index].id+"'>"+jsonO[index].name;
	    		htmlOption+="</option>";
	    		$("#classIds_"+ii).append(htmlOption);
	    	});
	    	jsonO=jsonArr[1];
	    	subjectIdTeachClassMap[subjectId]=jsonO;
			$.each(jsonO,function(index){
	    		var htmlOption="<option ";
	    		if(selectTeachClassMap[subjectId] && selectTeachClassMap[subjectId][jsonO[index].id]){
	    			htmlOption+="selected";
	    		}
	    		htmlOption+=" value='"+jsonO[index].id+"'>"+jsonO[index].name;
	    		htmlOption+="</option>";
	    		$("#teachClassIds_"+ii).append(htmlOption);
	    	});
	    	$('#classIds_'+ii).trigger("chosen:updated");
			$('#teachClassIds_'+ii).trigger("chosen:updated");
			layer.close(layerIndex);
	    }
	});
}
</script>