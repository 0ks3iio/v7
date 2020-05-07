<script type="text/javascript" charset="utf-8" src="${request.contextPath}/static/ueditor/ueditor.config.js"></script>
<script type="text/javascript" charset="utf-8" src="${request.contextPath}/static/ueditor/ueditor.all.min.js"> </script>
<script type="text/javascript" charset="utf-8" src="${request.contextPath}/static/ueditor/lang/zh-cn/zh-cn.js"></script>
<script type="text/javascript" src="${request.contextPath}/static/components/layer/layer.js"></script>
<script type="text/javascript" src="${request.contextPath}/static/components/jquery-slimscroll/jquery.slimscroll.min.js"></script>
<script type="text/javascript" src="${request.contextPath}/static/components/moment/min/moment-with-locales.min.js"></script>
<script type="text/javascript" src="${request.contextPath}/static/components/bootstrap-datetimepicker-4.17/js/bootstrap-datetimepicker.js"></script>
<link rel="stylesheet" type="text/css" href="${request.contextPath}/static/ueditor/themes/default/css/ueditor.css"/>
<link rel="stylesheet" type="text/css" href="${request.contextPath}/static/ueditor/themes/iframe.css"/>
<link rel="stylesheet" type="text/css" href="${request.contextPath}/static/components/bootstrap-datetimepicker-4.17/css/bootstrap-datetimepicker.min.css">
<style>
.datetimepicker {
	margin-top:auto;
}
.add-exam-item{
	height:280px;
}
</style>
<form id="subjectClassForm">
<div class="box box-default">
  	<div class="box-header">
	 	<h4 class="box-title">${examInfo.examName!}</h4>
		<a href="javascript:" class="btn btn-blue pull-right js-saveSubjectInfo" id="btn-saveSubjectInfo" onclick="saveInfo()">保存</a>
  	</div>
	<input type="hidden" name="examId" id="examId" value="${examInfo.id!}">
   <#if !isEdu && isJoin>
	<input type="hidden" name="unitId" value="${unitId!}">
   	<#--<div class="box-header">
		<h4 class="box-title">班级设置</h4>
		<a href="javascript:" class="btn btn-blue pull-right js-copyBeforeClazz" id="btn-copyBeforeClazz" onclick="copyBeforeExamId(2)">+复用</a>
	</div>
    <div class="box-body">
		<div class="filter-item block">
			<div class="row">
				<div class="col-xs-12">
					<div class="box-boder">
						<div class="multiselect" id="classIds">
							<#if clazzList?exists && (clazzList?size>0)>
							<#list clazzList as item>
								<#assign ff=false>
								<#if classInfoList?exists && (classInfoList?size>0)>
									<#list classInfoList as item1>
										<#if item.id==item1.classId>
											<#assign ff=true>
											<#break>
										</#if>
									</#list>
								<#else>
									<#assign ff=false>
								</#if>
								<span class="label-select-item <#if ff>active</#if>" data-value="${item.id!}">${item.classNameDynamic!}</span>
							</#list>
							</#if>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>-->
	</#if>
	
  	 <div class="box-header">
		 <h4 class="box-title">科目设置</h4>
		 <div class="pull-right">
		 	 <span class="tip tip-grey">（复用之后，请维护考试时间后再次保存）</span>
			 <#if isEditSubject ><a href="javascript:" class="btn btn-blue  js-copyBeforeClazz" id="btn-copyBeforeSubject" onclick="copyBeforeExamId(1)">+复用</a></#if>
	 	</div>
	 </div>
	 <div class="box-body">
		<div class="">
			<div class="add-exam add-exam-tbale clearfix courseInfoDivAll">
				<#if subjectInfoList?exists && (subjectInfoList?size>0)>
				<#list subjectInfoList as item>
				<div class="widget-box add-exam-item courseInfoDiv" id="item_${item_index}">
					<input type="hidden" value="${item.id!}" id="id_${item_index}" class="idClass" name="emSubjectInfoList[${item_index}].id">
					<input type="hidden" value="${item.unitId!}" id="unitId_${item_index}" class="unitIdClass" name="emSubjectInfoList[${item_index}].unitId">
					<input type="hidden" value="${item.examId!}" id="examId_${item_index}" class="examIdClass" name="emSubjectInfoList[${item_index}].examId">
					<div class="widget-body" style="height:100%">
						<div class="widget-main padding-12">
							<#if isEditSubject ><a href="javascript:" class="add-exam-close delDiv" id="delDiv_${item_index}"><i class="fa fa-times-circle"></i></a></#if>
							<div class="filter">
								<div class="filter-item">
									<select <#if !isEditSubject >disabled</#if> class="form-control subjectIdClass" id="subjectId_${item_index}" name="emSubjectInfoList[${item_index}].subjectId" title="考试科目">
										<option value="">请选择考试科目</option>
										<#if courseList?exists && (courseList?size>0)>
										<#list courseList as citem>
											<option value="${citem.id!}" <#if citem.id==item.subjectId?default('')> selected="selected"</#if>>${citem.subjectName!}</option>
										</#list>
										</#if>
									</select>
								</div>
								<div class="filter-item">
									<select <#if !isEditSubject >disabled</#if> class="form-control inputTypeClass" id="inputType_${item_index}" name="emSubjectInfoList[${item_index}].inputType" title="成绩录入方式">
										${mcodeSetting.getMcodeSelect("DM-CJLRXSFS", "${item.inputType!}", "0")}
									</select>
								</div>
								<div class="filter-item">
									<div class="filter-content">
									    <div class="input-group">
											<input class="form-control datetimepicker startDateClass" autocomplete="off" vtype="data"  type="text" nullable="false" name="emSubjectInfoList[${item_index}].strStartDate" id="strStartDate_${item_index}" placeholder="考试开始时间(到分钟)" value="${(item.startDate?string('yyyy-MM-dd HH:mm'))!}">
											<span class="input-group-addon">
												<i class="fa fa-calendar bigger-110"></i>
											</span>
									    </div>
									 </div>
								</div>
								<div class="filter-item">
									<div class="filter-content">
									    <div class="input-group">
											<input class="form-control datetimepicker endDateClass" autocomplete="off" vtype="data"  type="text" nullable="false" name="emSubjectInfoList[${item_index}].StrEndDate" id="StrEndDate_${item_index}" placeholder="考试结束时间(到分钟)" value="${(item.endDate?string('yyyy-MM-dd HH:mm'))!}">
											<span class="input-group-addon">
												<i class="fa fa-calendar bigger-110"></i>
											</span>
									    </div>
									 </div>
								</div>
								
								<div class="filter-item inputDivClass" <#if item.inputType?default('')=='G'>style="display:none"</#if>>
									<input <#if !isEditSubject >readonly</#if> type="text" class="form-control fullScoreClass" placeholder="请输入满分值(不能为空)" title="满分值"
									id="fullScore_${item_index}" name="emSubjectInfoList[${item_index}].fullScore"
									 value="<#if item.fullScore??>#{item.fullScore?default(0);M2}</#if>">
								</div>
								<div class="filter-item gradeTypeDivClass" <#if item.id?default('')=='' || item.inputType?default('')=='S'>style="display:none"</#if>>
									<select <#if !isEditSubject >disabled</#if> id="gradeType_${item_index}" name="emSubjectInfoList[${item_index}].gradeType" class="form-control gradeTypeClass" data-placeholder="选择等第显示分等" title="等第显示分等">
										<option value="">选择等第显示分等(不能为空)</option>
										${mcodeSetting.getMcodeSelect("DM-DDMC", item.gradeType?default(''), "0")}
									</select>
								</div>
								
								<div class="filter-item">
									<select <#if !isEditSubject >disabled</#if> class="form-control isLockClass" id="isLock_${item_index}" name="emSubjectInfoList[${item_index}].isLock" title="是否锁定成绩(默认否)">
										<option value="0" >是否锁定成绩(默认否)</option>
										<option value="1" <#if (item.isLock?default(0))?string=='1'>selected</#if>>是</option>
									</select>
								</div>
							</div>
						</div>
					</div>
				</div>
				</#list>
				</#if>
				<#if isEditSubject >
				<div class="widget-box add-exam-item box-bluebg">
					<a class="add-exam-btn" href="javascript:">
						<i class="fa fa-plus fa-3x"></i>
						<p>新增科目及分数</p>
					</a>
				</div>
				</#if>
			</div>
		</div>
  	</div>
</div>
</form>
<div id="addhideDiv" class="hide">
	<div class="widget-box add-exam-item">
		<input type="hidden" value="" id="id" class="idClass" name="id">
		<input type="hidden" value="${examInfo.unitId!}" id="unitId" class="unitIdClass" name="unitId">
		<input type="hidden" value="${examInfo.id!}" id="examId" class="examIdClass" name="examId">
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
						<div class="filter-content">
						    <div class="input-group">
								<input class="form-control datetimepicker startDateClass" autocomplete="off" vtype="data"  type="text" nullable="false" name="strStartDate" id="strStartDate" placeholder="考试开始时间(到分钟)" value="">
								<span class="input-group-addon">
									<i class="fa fa-calendar bigger-110"></i>
								</span>
						    </div>
						 </div>
					</div>
					<div class="filter-item">
						<div class="filter-content">
						    <div class="input-group">
								<input class="form-control datetimepicker endDateClass" autocomplete="off" vtype="data" type="text" nullable="false" name="StrEndDate" id="StrEndDate" placeholder="考试结束时间(到分钟)" value="">
								<span class="input-group-addon">
									<i class="fa fa-calendar bigger-110"></i>
								</span>
						    </div>
						 </div>
					</div>
					<div class="filter-item inputDivClass">
						<input type="text" class="form-control fullScoreClass" placeholder="请输入满分值(不能为空)" title="满分值"
						id="fullScore" name="fullScore"
						 value="">
					</div>
					<div class="filter-item gradeTypeDivClass" style="display:none">
						<select id="gradeType" name="gradeType" class="form-control gradeTypeClass" data-placeholder="选择等第显示分等" title="等第显示分等">
							<option value="">选择等第显示分等(不能为空)</option>
							${mcodeSetting.getMcodeSelect("DM-DDMC", "1", "0")}
						</select>
					</div>
					<div class="filter-item">
						<select class="form-control isLockClass" id="isLock" name="isLock" title="是否锁定成绩(默认否)">
							<option value="0">是否锁定成绩(默认否)</option>
							<option value="1">是</option>
						</select>
					</div>
				</div>
			</div>
		</div>
	</div>
</div>

<!-- page specific plugin scripts -->
<script type="text/javascript">
var index=-1;
<#if subjectInfoList?exists && (subjectInfoList?size>0)>
	index=${subjectInfoList?size-1};
</#if>
var isSubmit = false;
//初始化日期控件参数
$(function(){
	<#if subjectInfoList?exists && (subjectInfoList?size>0)>
	  <#list subjectInfoList as item>
	 	choosens(${item_index});
	  	init(${item_index},'${item.id!}');
	  </#list>
	</#if>
	
	showBreadBack(gobackIndex,true,"返回");
	
	$("#classIds").on("click",".label-select-item",function(){
		if($(this).hasClass("active")){
			$(this).removeClass("active");
		}else{
			$(this).addClass("active");
		}
		
	});
});

function init(ii,subjectInfoId){
	if(subjectInfoId!=''){
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
}

function doDeleteById(id,obj){
	var examId=$("#examId").val();
	layer.load();
	$.ajax({
		url:'${request.contextPath}/exammanage/subjectInfo/delete',
		data: {'id':id,'examId':examId},
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

$(".add-exam-btn").on("click",function(){
	if(isSubmit){
		return;
	}
	isSubmit = true;
	if(index > -1){
		var hasIndex = -1;
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
			var n0="emSubjectInfoList["+index+"].id";
			var i0="id_"+index;
			
			var n1="emSubjectInfoList["+index+"].unitId";
			var i1="unitId_"+index;
			
			var n2="emSubjectInfoList["+index+"].examId";
			var i2="examId_"+index;

			var del0="delDiv_"+index;
			
			var n3="emSubjectInfoList["+index+"].subjectId";
			var i3="subjectId_"+index;
			
			var n4="emSubjectInfoList["+index+"].inputType";
			var i4="inputType_"+index;
			
			var n5="emSubjectInfoList["+index+"].strStartDate";
			var i5="strStartDate_"+index;
			
			var n6="emSubjectInfoList["+index+"].StrEndDate";
			var i6="StrEndDate_"+index;
			
			var n7="emSubjectInfoList["+index+"].fullScore"
			var i7="fullScore_"+index;
			
			var n8="emSubjectInfoList["+index+"].gradeType";
			var i8="gradeType_"+index;
			
			var n9="emSubjectInfoList["+index+"].isLock";
			var i9="isLock_"+index;
			
			$("#item_"+index).find(".idClass").attr("name",n0).attr("id",i0);
			$("#item_"+index).find(".idClass").val('');
			
			$("#item_"+index).find(".unitIdClass").attr("name",n1).attr("id",i1);
			
			$("#item_"+index).find(".examIdClass").attr("name",n2).attr("id",i2);
			
			$("#item_"+index).find(".delDiv").attr("id",del0);
			
			$("#item_"+index).find(".subjectIdClass").attr("name",n3).attr("id",i3);
			$("#item_"+index).find(".subjectIdClass").val("");
			
			$("#item_"+index).find(".inputTypeClass").attr("name",n4).attr("id",i4);
			$("#item_"+index).find(".inputTypeClass").val($("#item_"+hasIndex).find(".inputTypeClass").val());
			
			$("#item_"+index).find(".startDateClass").attr("name",n5).attr("id",i5);
			$("#item_"+index).find(".startDateClass").val("");
			$("#item_"+index).find(".endDateClass").attr("name",n6).attr("id",i6);
			$("#item_"+index).find(".endDateClass").val("");
			//去除readonly
			$("#item_"+index).find(".startDateClass").removeAttr("readonly");
			$("#item_"+index).find(".endDateClass").removeAttr("readonly");
			$("#item_"+index).find(".fullScoreClass").attr("name",n7).attr("id",i7);
			$("#item_"+index).find(".fullScoreClass").val($("#item_"+hasIndex).find(".fullScoreClass").val());
			
			$("#item_"+index).find(".gradeTypeClass").attr("name",n8).attr("id",i8);
			$("#item_"+index).find(".gradeTypeClass").val($("#item_"+hasIndex).find(".gradeTypeClass").val());
			
			
			$("#item_"+index).find(".isLockClass").attr("name",n9).attr("id",i9);
			$("#item_"+index).find(".isLockClass").val($("#item_"+hasIndex).find(".isLockClass").val());
			choosens(index);
			$('#delDiv_'+index).on('click',function(){
				$(this).parent().parent().parent().remove();
			});
			isSubmit = false;
			return;
		}
	}
	index++;
	$("#addhideDiv").find(".widget-box").attr("id","item_"+index);
	$("#addhideDiv").find(".widget-box").addClass("courseInfoDiv");
	var n0="subjectInfoList["+index+"].id";
	var i0="id_"+index;
	
	var n1="emSubjectInfoList["+index+"].unitId";
	var i1="unitId_"+index;
	
	var n2="emSubjectInfoList["+index+"].examId";
	var i2="examId_"+index;

	var del0="delDiv_"+index;
	
	var n3="emSubjectInfoList["+index+"].subjectId";
	var i3="subjectId_"+index;
	
	var n4="emSubjectInfoList["+index+"].inputType";
	var i4="inputType_"+index;
	
	var n5="emSubjectInfoList["+index+"].strStartDate";
	var i5="strStartDate_"+index;
	
	var n6="emSubjectInfoList["+index+"].StrEndDate";
	var i6="StrEndDate_"+index;
	
	var n7="emSubjectInfoList["+index+"].fullScore"
	var i7="fullScore_"+index;
	
	var n8="emSubjectInfoList["+index+"].gradeType";
	var i8="gradeType_"+index;
	
	var n9="emSubjectInfoList["+index+"].isLock";
	var i9="isLock_"+index;
	
	
	$("#addhideDiv").find("input[name='id']").attr("name",n0).attr("id",i0);
	$("#addhideDiv").find("input[name='unitId']").attr("name",n1).attr("id",i1);
	$("#addhideDiv").find("input[name='examId']").attr("name",n2).attr("id",i2);
	
	$("#addhideDiv").find(".delDiv").attr("id",del0);
	
	$("#addhideDiv").find("select[name='subjectId']").attr("name",n3).attr("id",i3);
	
	$("#addhideDiv").find("select[name='inputType']").attr("name",n4).attr("id",i4);
	$("#addhideDiv").find("input[name='strStartDate']").attr("name",n5).attr("id",i5);
	$("#addhideDiv").find("input[name='StrEndDate']").attr("name",n6).attr("id",i6);
	
	$("#addhideDiv").find("input[name='fullScore']").attr("name",n7).attr("id",i7);
	$("#addhideDiv").find("select[name='gradeType']").attr("name",n8).attr("id",i8);
	
	$("#addhideDiv").find("select[name='isLock']").attr("name",n9).attr("id",i9);
	
	var $exam_item=$("#addhideDiv").html();
	$(this).parent().before($exam_item);
	
	$("#addhideDiv").find(".widget-box").attr("id","");
	$("#addhideDiv").find("input[name='"+n0+"']").attr("name","id").attr("id","id");
	$("#addhideDiv").find(".widget-box").removeClass("courseInfoDiv");
	$("#addhideDiv").find("input[name='"+n1+"']").attr("name","unitId").attr("id","unitId");
	$("#addhideDiv").find("input[name='"+n2+"']").attr("name","examId").attr("id","examId");
	
	$("#addhideDiv").find(".delDiv").attr("id","");
	$("#addhideDiv").find("select[name='"+n3+"']").attr("name","subjectId").attr("id","subjectId");
	$("#addhideDiv").find("select[name='"+n4+"']").attr("name","inputType").attr("id","inputType");
	$("#addhideDiv").find("input[name='"+n5+"']").attr("name","strStartDate").attr("id","strStartDate");
	$("#addhideDiv").find("input[name='"+n6+"']").attr("name","StrEndDate").attr("id","StrEndDate");
	$("#addhideDiv").find("input[name='"+n7+"']").attr("name","fullScore").attr("id","fullScore");
	$("#addhideDiv").find("select[name='"+n8+"']").attr("name","gradeType").attr("id","gradeType");
	$("#addhideDiv").find("select[name='"+n9+"']").attr("name","isLock").attr("id","isLock");
	choosens(index);
	$('#delDiv_'+index).on('click',function(){
		$(this).parent().parent().parent().remove();
	});
	isSubmit = false;
});

function choosens(ii){

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
    $('.form-control.datetimepicker').datetimepicker4({
        format: 'YYYY-MM-DD HH:mm',
        sideBySide: true,
        locale: moment.locale('zh-cn'),
        dayViewHeaderFormat: 'YYYY MMMM',
        useCurrent: false,
        minDate:'${(examInfo.examStartDate?string('yyyy-MM-dd HH:mm'))!}',
        maxDate:'${(examInfo.examEndDate?string('yyyy-MM-dd HH:mm'))!}'
    }).next().on('click', function(){
        $(this).prev().focus();
    })
//	initCalendarData("#item_"+ii,".date-picker",viewContentTime);
}


var isSaveSubmit=false;
function saveInfo(){
	if(isSaveSubmit){
		return;
	}
	isSaveSubmit = true;
	$("#btn-saveSubjectInfo").addClass("disabled");
	var b=false;
	var couSelMap={};
	var i=0;
	var startTimes=[];
	var endTimes=[];
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
		
		//开始时间<结束时间  且要在同一天
		var strStartDate = $(this).find(".startDateClass").val();
		if(strStartDate==''){
			layer.tips('不能为空!', $(this).find(".startDateClass"), {
				tipsMore: true,
				tips: 3
			});
			if(!b){
				$(this).find(".startDateClass").focus();
				b=true;
			}
		}
		var StrEndDate = $(this).find(".endDateClass").val();
		if(StrEndDate==''){
			layer.tips('不能为空!', $(this).find(".endDateClass"), {
				tipsMore: true,
				tips: 3
			});
			if(!b){
				$(this).find(".endDateClass").focus();
				b=true;
			}
		}
		if(strStartDate>StrEndDate){
			layer.tips('考试结束时间不能小于考试结束时间!', $(this).find(".endDateClass"), {
					tipsMore: true,
					tips: 3
				});
			if(!b){
				$(this).find(".endDateClass").focus();
				b=true;
			}
		}else{
			//同一天
			var s=strStartDate.substring(0,10);
			var e=StrEndDate.substring(0,10);
			if(s!=e){
				layer.tips('考试结束时间与考试结束时间需要在同一天!', $(this).find(".endDateClass"), {
					tipsMore: true,
					tips: 3
				});
				if(!b){
					$(this).find(".endDateClass").focus();
					b=true;
				}
			}else{
				//时间不能交叉
				if(i>0){
					var c=false;
					for(j=0;j<i;j++){
						if(StrEndDate<=startTimes[j] || strStartDate>=endTimes[j]){
							
						}else{
							c=true;
							j=i+1;//当做直接跳出
						}
					}
					if(c){
						layer.tips('考试时间不能交叉!', $(this).find(".startDateClass"), {
							tipsMore: true,
							tips: 3
						});
						if(!b){
							$(this).find(".startDateClass").focus();
							b=true;
						}
					}else{
						startTimes[i]=strStartDate;
						endTimes[i]=StrEndDate;
						i++;
					}	
				}else{
					startTimes[i]=strStartDate;
					endTimes[i]=StrEndDate;
					i++;
				}
				
			}
		}
	});
	if(b){
		isSaveSubmit = false;
		$("#btn-saveSubjectInfo").removeClass("disabled");
		return;
	}
	var classIdStr="";
	<#if !isEdu>
		$("#classIds .active").each(function(){
			var clazz=$(this).attr("data-value");
			classIdStr=classIdStr+","+clazz;
		})
		if(classIdStr!=""){
			classIdStr=classIdStr.substring(1);
		}
	</#if>
	var layerIndex = layer.load();
	var options = {
		url : "${request.contextPath}/exammanage/subjectClassInfo/list/save",
		data : {"classIdStr":classIdStr},
		dataType : 'json',
		success : function(data){
			$("#btn-saveSubjectInfo").removeClass("disabled");
			isSaveSubmit = false;
 			if(!data.success){
 				layerTipMsg(data.success,"失败",data.msg);
 			}else{
 				layer.msg(data.msg, {
					offset: 't',
					time: 2000
				});
 				
 			}
 			layer.close(layerIndex);
		},
		clearForm : false,
		resetForm : false,
		type : 'post',
		error:function(XMLHttpRequest, textStatus, errorThrown){} 
	};
	$("#subjectClassForm").ajaxSubmit(options);
}


function copyBeforeExamId(type){
	 var examId=$("#examId").val();
	 var url = "${request.contextPath}/exammanage/subjectInfo/sameExamList/page?examId="+examId+"&type="+type;
     var title="请选择考试";
     if(type=="1"){
     	title=title+"(复用科目设置)";
     }else if(type=="2"){
     	title=title+"(复用班级设置)";
     }
	 indexDiv = layerDivUrl(url,{title: title,width:450,height:500});
}

</script>