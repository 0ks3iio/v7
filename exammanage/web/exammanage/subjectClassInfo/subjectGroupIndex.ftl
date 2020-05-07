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
				<div class="widget-box add-exam-item courseInfoDiv" id="item_${item_index}" style="height: 420px;width: 312px">
					<input type="hidden" value="${item.id!}" id="id_${item_index}" class="idClass" name="emSubjectInfoList[${item_index}].id">
					<input type="hidden" value="${item.unitId!}" id="unitId_${item_index}" class="unitIdClass" name="emSubjectInfoList[${item_index}].unitId">
					<input type="hidden" value="${item.examId!}" id="examId_${item_index}" class="examIdClass" name="emSubjectInfoList[${item_index}].examId">
					<div class="widget-body" style="height:100%">
						<div class="widget-main padding-12">
							<#if isEditSubject ><a href="javascript:" class="add-exam-close delDiv" id="delDiv_${item_index}"><i class="fa fa-times-circle"></i></a></#if>
							<table width="100%">
								<tr>
									<td class="text-right" width="70">科目：</td>
									<td>
										<select <#if !isEditSubject >disabled</#if> class="form-control subjectIdClass" id="subjectId_${item_index}" name="emSubjectInfoList[${item_index}].subjectId" title="考试科目">
											<option value="">请选择考试科目</option>
											<#if courseList?exists && (courseList?size>0)>
											<#list courseList as citem>
												<option value="${citem.id!}" <#if citem.id==item.subjectId?default('')> selected="selected"</#if>>${citem.subjectName!}</option>
											</#list>
											</#if>
										</select>
									</td>
								</tr>
								<tr class="gkDivClass" <#if item.ysy?default(false)>style="display:none"</#if>>
									<td class="text-right" style="width:100px;">考生范围：</td>
									<td>
										<select <#if !isEditSubject >disabled</#if> class="form-control gkSubTypeClass" id="gkSubType_${item_index}" name="emSubjectInfoList[${item_index}].gkSubType" title="成绩录入方式">
											<option value="0" <#if item.gkSubType?default("")=="0">selected="selected"</#if>>所有学生</option>
											<option value="1" <#if item.gkSubType?default("")=="1">selected="selected"</#if>>仅选考学生</option>
											<option value="2" <#if item.gkSubType?default("")=="2">selected="selected"</#if>>仅学考学生</option>
										</select>
									</td>
								</tr>

								<tr class="xueDivClass" <#if item.ysy?default(false) || item.gkSubType?default("")=="1">style="display:none"</#if>>
									<td class="text-right" >学考开始时间：</td>
									<td>
										<div class="input-group">
											<input class="form-control datetimepicker gkStartDateClass" autocomplete="off" vtype="data"  type="text" nullable="false" name="emSubjectInfoList[${item_index}].strGkStartDate" id="strGkStartDate_${item_index}" value="${(item.gkStartDate?string('yyyy-MM-dd HH:mm'))!}">
											<span class="input-group-addon">
												<i class="fa fa-calendar bigger-110"></i>
											</span>
										</div>
									</td>
								</tr>
								<tr class="xueDivClass" <#if item.ysy?default(false) || item.gkSubType?default("")=="1">style="display:none"</#if>>
									<td class="text-right" >学考结束时间：</td>
									<td>
										<div class="input-group">
											<input class="form-control datetimepicker gkEndDateClass" autocomplete="off" vtype="data"  type="text" nullable="false" name="emSubjectInfoList[${item_index}].strGkEndDate" id="strGkEndDate_${item_index}"  value="${(item.gkEndDate?string('yyyy-MM-dd HH:mm'))!}">
											<span class="input-group-addon">
												<i class="fa fa-calendar bigger-110"></i>
											</span>
										</div>
									</td>
								</tr>
								<tr class="xuanDivClass" <#if item.gkSubType?default("")=="2">style="display:none"</#if>>
									<td class="text-right" ><span class="startSpanClass"><#if !item.ysy?default(false)>选考</#if>开始时间：</span></td>
									<td>
										<div class="input-group">
											<input class="form-control datetimepicker startDateClass" autocomplete="off" vtype="data"  type="text" nullable="false" name="emSubjectInfoList[${item_index}].strStartDate" id="strStartDate_${item_index}" value="${(item.startDate?string('yyyy-MM-dd HH:mm'))!}">
											<span class="input-group-addon">
												<i class="fa fa-calendar bigger-110"></i>
											</span>
										</div>
									</td>
								</tr>
								<tr class="xuanDivClass" <#if item.gkSubType?default("")=="2">style="display:none"</#if>>
									<td class="text-right" style="width:100px;"><span class="endSpanClass"><#if !item.ysy?default(false)>选考</#if>结束时间：</span></td>
									<td>
										<div class="input-group">
											<input class="form-control datetimepicker endDateClass" autocomplete="off" vtype="data"  type="text" nullable="false" name="emSubjectInfoList[${item_index}].strEndDate" id="strEndDate${item_index}"  value="${(item.endDate?string('yyyy-MM-dd HH:mm'))!}">
											<span class="input-group-addon">
												<i class="fa fa-calendar bigger-110"></i>
											</span>
										</div>
									</td>
								</tr>
								<tr>
									<td class="text-right">分制：</td>
									<td>
										<select <#if !isEditSubject >disabled</#if> class="form-control inputTypeClass" id="inputType_${item_index}" name="emSubjectInfoList[${item_index}].inputType" title="成绩录入方式">
											${mcodeSetting.getMcodeSelect("DM-CJLRXSFS", "${item.inputType!}", "0")}
										</select>
									</td>
								</tr>
								<tr class="inputDivClass" <#if item.inputType?default('')=='G'>style="display:none"</#if>>
									<td class="text-right">满分：</td>
									<td>
										<input <#if !isEditSubject >readonly</#if> type="text" class="form-control fullScoreClass" placeholder="请输入满分值(不能为空)" title="满分值"
											id="fullScore_${item_index}" name="emSubjectInfoList[${item_index}].fullScore"
											value="<#if item.fullScore??>#{item.fullScore?default(0);M2}</#if>">
									</td>
								</tr>
								<tr class="gradeTypeDivClass" <#if item.id?default('')=='' || item.inputType?default('')=='S'>style="display:none"</#if>>
									<td class="text-right">满分：</td>
									<td>
										<select <#if !isEditSubject >disabled</#if> id="gradeType_${item_index}" name="emSubjectInfoList[${item_index}].gradeType" class="form-control gradeTypeClass" data-placeholder="选择等第显示分等" title="等第显示分等">
											<option value="">选择等第显示分等(不能为空)</option>
											${mcodeSetting.getMcodeSelect("DM-DDMC", item.gradeType?default(''), "0")}
										</select>
									</td>
								</tr>
								<tr>
									<td class="text-right">锁定成绩：</td>
									<td>
										<select <#if !isEditSubject >disabled</#if> class="form-control isLockClass" id="isLock_${item_index}" name="emSubjectInfoList[${item_index}].isLock" title="是否锁定成绩(默认否)">
											<option value="0" >是否锁定成绩(默认否)</option>
											<option value="1" <#if (item.isLock?default(0))?string=='1'>selected</#if>>是</option>
										</select>
									</td>
								</tr>
							</table>
						</div>
					</div>
				</div>
				</#list>
				</#if>
				<#if isEditSubject >
				<div class="widget-box add-exam-item box-bluebg" style="height: 420px;">
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
	<div class="widget-box add-exam-item" style="height: 420px;width: 312px">
		<input type="hidden" value="" id="id" class="idClass" name="id">
		<input type="hidden" value="${examInfo.unitId!}" id="unitId" class="unitIdClass" name="unitId">
		<input type="hidden" value="${examInfo.id!}" id="examId" class="examIdClass" name="examId">
		<div class="widget-body" style="height:100%">
			<div class="widget-main padding-12">
				<a href="javascript:" class="add-exam-close delDiv"><i class="fa fa-times-circle"></i></a>
				<table width="100%">
					<tr>
						<td class="text-right" width="70">科目：</td>
						<td>
							<select class="form-control subjectIdClass" id="subjectId" name="subjectId" title="考试科目">
								<option value="">请选择考试科目</option>
								<#if courseList?exists && (courseList?size>0)>
								<#list courseList as citem>
									<option value="${citem.id!}">${citem.subjectName!}</option>
								</#list>
								</#if>
							</select>
						</td>
					</tr>
					<tr class="gkDivClass" style="display:none">
						<td class="text-right" style="width:100px;">考生范围：</td>
						<td>
							<select class="form-control gkSubTypeClass" id="gkSubType" name="gkSubType">
								<option value="0">所有学生</option>
								<option value="1">仅选考学生</option>
								<option value="2">仅学考学生</option>
							</select>
						</td>
					</tr>

					<tr class="xueDivClass" style="display:none">
						<td class="text-right">学考开始时间：</td>
						<td>
							<div class="input-group">
								<input class="form-control datetimepicker gkStartDateClass" autocomplete="off" vtype="data"  type="text" nullable="false" name="strGkStartDate" id="strGkStartDate"  value="">
								<span class="input-group-addon">
									<i class="fa fa-calendar bigger-110"></i>
								</span>
							</div>
						</td>
					</tr>
					<tr  class="xueDivClass" style="display:none">
						<td class="text-right" >学考结束时间：</td>
						<td>
							<div class="input-group">
								<input class="form-control datetimepicker gkEndDateClass" autocomplete="off" vtype="data" type="text" nullable="false" name="strGkEndDate" id="strGkEndDate"  value="">
								<span class="input-group-addon">
									<i class="fa fa-calendar bigger-110"></i>
								</span>
							</div>
						</td>
					</tr>
					<tr class="xuanDivClass">
						<td class="text-right"  ><span class="startSpanClass">开始时间：</span></td>
						<td>
							<div class="input-group">
								<input class="form-control datetimepicker startDateClass" autocomplete="off" vtype="data"  type="text" nullable="false" name="strStartDate" id="strStartDate"  value="">
								<span class="input-group-addon">
									<i class="fa fa-calendar bigger-110"></i>
								</span>
							</div>
						</td>
					</tr>
					<tr class="xuanDivClass">
						<td class="text-right" style="width:100px;"><span class="endSpanClass">结束时间：</span></td>
						<td>
							<div class="input-group">
								<input class="form-control datetimepicker endDateClass" autocomplete="off" vtype="data"  type="text" nullable="false" name="strEndDate" id="strEndDate"  value="">
								<span class="input-group-addon">
									<i class="fa fa-calendar bigger-110"></i>
								</span>
							</div>
						</td>
					</tr>
					<tr>
						<td class="text-right">分制：</td>
						<td>
							<select class="form-control inputTypeClass" id="inputType" name="inputType" title="成绩录入方式">
								${mcodeSetting.getMcodeSelect("DM-CJLRXSFS", "S", "0")}
							</select>
						</td>
					</tr>
					<tr  class="inputDivClass">
						<td class="text-right">满分：</td>
						<td>
							<input type="text" class="form-control fullScoreClass" placeholder="请输入满分值(不能为空)" title="满分值"
								id="fullScore" name="fullScore" value="">
						</td>
					</tr>
					<tr  class="gradeTypeDivClass" style="display:none">
						<td class="text-right">等第：</td>
						<td>
							<select id="gradeType" name="gradeType" class="form-control gradeTypeClass" data-placeholder="选择等第显示分等" title="等第显示分等">
								<option value="">选择等第显示分等(不能为空)</option>
								${mcodeSetting.getMcodeSelect("DM-DDMC", "1", "0")}
							</select>
						</td>
					</tr>
					<tr>
						<td class="text-right">锁定成绩：</td>
						<td>
							<select class="form-control isLockClass" id="isLock" name="isLock" title="是否锁定成绩(默认否)">
								<option value="0">是否锁定成绩(默认否)</option>
								<option value="1">是</option>
							</select>
						</td>
					</tr>
				</table>
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
var course73Map={};
<#if courseList73?exists && courseList73?size gt 0>
<#list courseList73 as item>
	course73Map['${item.id!}']='${item.subjectCode!}';
</#list>
</#if>

var isSubmit = false;
//初始化日期控件参数
<#--var viewContentTime={-->
	<#--'format' : 'yyyy-mm-dd hh:ii',-->
	<#--'minView' : '0',-->
	<#--'startDate':'${(examInfo.examStartDate?string('yyyy-MM-dd HH:mm'))!}',-->
	<#--'endDate':'${(examInfo.examEndDate?string('yyyy-MM-dd HH:mm'))!}'-->
<#--};-->
$(function(){
	showBreadBack(gobackIndex,true,"返回");
	<#if subjectInfoList?exists && (subjectInfoList?size>0)>
	  <#list subjectInfoList as item>
	 	choosens(${item_index});
	  	init(${item_index},'${item.id!}');
	  </#list>
	</#if>


	$("#classIds").on("click",".label-select-item",function(){
		if($(this).hasClass("active")){
			$(this).removeClass("active");
		}else{
			$(this).addClass("active");
		}

	});
});

function init(ii,subjectInfoId){
	$('#delDiv_'+ii).on('click',function(){
		$(this).parent().parent().parent().remove();
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

			var n11="emSubjectInfoList["+index+"].gkSubType";
			var i11="gkSubType_"+index;

			var n4="emSubjectInfoList["+index+"].inputType";
			var i4="inputType_"+index;

			var n12="emSubjectInfoList["+index+"].strGkStartDate";
			var i12="strGkStartDate_"+index;

			var n5="emSubjectInfoList["+index+"].strStartDate";
			var i5="strStartDate_"+index;

			var n6="emSubjectInfoList["+index+"].strEndDate";
			var i6="strEndDate_"+index;

			var n10="emSubjectInfoList["+index+"].strGkEndDate";
			var i10="strGkEndDate_"+index;

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

			$("#item_"+index).find(".gkSubTypeClass").attr("name",n11).attr("id",i11);

			$("#item_"+index).find(".inputTypeClass").attr("name",n4).attr("id",i4);
			$("#item_"+index).find(".inputTypeClass").val($("#item_"+hasIndex).find(".inputTypeClass").val());

			$("#item_"+index).find(".gkStartDateClass").attr("name",n12).attr("id",i12);
			$("#item_"+index).find(".gkStartDateClass").val("");
			$("#item_"+index).find(".startDateClass").attr("name",n5).attr("id",i5);
			$("#item_"+index).find(".startDateClass").val("");
			$("#item_"+index).find(".endDateClass").attr("name",n6).attr("id",i6);
			$("#item_"+index).find(".endDateClass").val("");
			$("#item_"+index).find(".gkEndDateClass").attr("name",n10).attr("id",i10);
			$("#item_"+index).find(".gkEndDateClass").val("");
			//去除readonly
			$("#item_"+index).find(".startDateClass").removeAttr("readonly");
			$("#item_"+index).find(".endDateClass").removeAttr("readonly");
			$("#item_"+index).find(".gkEndDateClass").removeAttr("readonly");
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

	var n11="emSubjectInfoList["+index+"].gkSubType";
	var i11="gkSubType_"+index;

	var n4="emSubjectInfoList["+index+"].inputType";
	var i4="inputType_"+index;

	var n5="emSubjectInfoList["+index+"].strStartDate";
	var i5="strStartDate_"+index;

	var n12="emSubjectInfoList["+index+"].strGkStartDate";
	var i12="strGkStartDate_"+index;

	var n6="emSubjectInfoList["+index+"].strEndDate";
	var i6="strEndDate_"+index;
	var n10="emSubjectInfoList["+index+"].strGkEndDate";
	var i10="strGkEndDate_"+index;

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
	$("#addhideDiv").find("select[name='gkSubType']").attr("name",n11).attr("id",i11);

	$("#addhideDiv").find("select[name='inputType']").attr("name",n4).attr("id",i4);
	$("#addhideDiv").find("input[name='strStartDate']").attr("name",n5).attr("id",i5);
	$("#addhideDiv").find("input[name='strEndDate']").attr("name",n6).attr("id",i6);
	$("#addhideDiv").find("input[name='strGkStartDate']").attr("name",n12).attr("id",i12);
	$("#addhideDiv").find("input[name='strGkEndDate']").attr("name",n10).attr("id",i10);

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
	$("#addhideDiv").find("select[name='"+n11+"']").attr("name","gkSubType").attr("id","gkSubType");
	$("#addhideDiv").find("select[name='"+n4+"']").attr("name","inputType").attr("id","inputType");
	$("#addhideDiv").find("input[name='"+n5+"']").attr("name","strStartDate").attr("id","strStartDate");
	$("#addhideDiv").find("input[name='"+n12+"']").attr("name","strGkStartDate").attr("id","strGkStartDate");
	$("#addhideDiv").find("input[name='"+n6+"']").attr("name","strEndDate").attr("id","strEndDate");
	$("#addhideDiv").find("input[name='"+n10+"']").attr("name","strGkEndDate").attr("id","strGkEndDate");
	$("#addhideDiv").find("input[name='"+n7+"']").attr("name","fullScore").attr("id","fullScore");
	$("#addhideDiv").find("select[name='"+n8+"']").attr("name","gradeType").attr("id","gradeType");
	$("#addhideDiv").find("select[name='"+n9+"']").attr("name","isLock").attr("id","isLock");
	choosens(index);
	$('#delDiv_'+index).on('click',function(){
		$(this).parent().parent().parent().remove();
	});
	isSubmit = false;
});

var ysy1='${ysy1!}';
var ysy2='${ysy2!}';
var ysy3='${ysy3!}';
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
	$("#subjectId_"+ii).on("change",function(){
		var subjectId=$(this).val();
		var subjectCode=course73Map[subjectId];//subjectCode存在即是7选3科目
		if(!subjectCode){
			$(this).parent().parent().parent().find(".gkDivClass").hide();
			$(this).parent().parent().parent().find(".xueDivClass").hide();
			$(this).parent().parent().parent().find(".xuanDivClass").show();
			$(this).parent().parent().parent().find(".startSpanClass").html("开始时间：");
			$(this).parent().parent().parent().find(".endSpanClass").html("结束时间：");
			$(this).parent().parent().parent().find(".gkSubTypeClass").val(0);
		}else{
			$(this).parent().parent().parent().find(".gkDivClass").show();
			$(this).parent().parent().parent().find(".startSpanClass").html("选考开始时间：");
			$(this).parent().parent().parent().find(".endSpanClass").html("选考结束时间：");
			var gkSubType=$(this).parent().parent().parent().find(".gkSubTypeClass").val();
			if(gkSubType && gkSubType=="1"){//仅选考
				$(this).parent().parent().parent().find(".xuanDivClass").show();
				$(this).parent().parent().parent().find(".xueDivClass").hide();
			}else if(gkSubType && gkSubType=="2"){//仅学考
				$(this).parent().parent().parent().find(".xuanDivClass").hide();
				$(this).parent().parent().parent().find(".xueDivClass").show();
			}else{
				$(this).parent().parent().parent().find(".xuanDivClass").show();
				$(this).parent().parent().parent().find(".xueDivClass").show();
			}
		}
	});
	$("#gkSubType_"+ii).on("change",function(){
		var gkSubType=$(this).val();
		if(gkSubType && gkSubType=="1"){//仅选考
			$(this).parent().parent().parent().find(".xuanDivClass").show();
			$(this).parent().parent().parent().find(".xueDivClass").hide();
		}else if(gkSubType && gkSubType=="2"){//仅学考
			$(this).parent().parent().parent().find(".xuanDivClass").hide();
			$(this).parent().parent().parent().find(".xueDivClass").show();
		}else{
			$(this).parent().parent().parent().find(".xuanDivClass").show();
			$(this).parent().parent().parent().find(".xueDivClass").show();
		}
	});
    // 时间
//    $('.datetimepicker').datetimepicker({
//        format: 'YYYY-MM-DD HH:mm',
//        sideBySide: true,
//        language: 'zh-CN',
//        locale: moment.locale('zh-cn'),
//        useCurrent: false,
//    }).next().on('click', function(){
//        $(this).prev().focus();
//    })
    // 时间
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
    });
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
	var gkendTimes=[];
	$(".courseInfoDiv").each(function(){
		//各种校验
		var subjectId = $(this).find(".subjectIdClass").val();
		var subjectCode=course73Map[subjectId];
		var isYsy=false;
		var checkDate=false;
		var gkSubType = $(this).find(".gkSubTypeClass").val();
		if(!subjectCode){
			isYsy=true;
			checkDate=true;
		}
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
		var strGkEndDate = $(this).find(".gkEndDateClass").val();
		var isXue=false;
		if(gkSubType && gkSubType=="1"){//仅选考，学考时间不用判断
			isYsy=true;
		}else if(gkSubType && gkSubType=="2"){//仅学考，选考时间不用判断
			isXue=true;
		}
		//开始时间<结束时间  且要在同一天
		var strStartDate = $(this).find(".startDateClass").val();
		if(!isXue && strStartDate==''){
			layer.tips('不能为空!', $(this).find(".startDateClass"), {
				tipsMore: true,
				tips: 3
			});
			if(!b){
				$(this).find(".startDateClass").focus();
				b=true;
			}
		}

		var strEndDate = $(this).find(".endDateClass").val();
		if(!isXue && strEndDate==''){
			layer.tips('不能为空!', $(this).find(".endDateClass"), {
				tipsMore: true,
				tips: 3
			});
			if(!b){
				$(this).find(".endDateClass").focus();
				b=true;
			}
		}
		var strGkStartDate = $(this).find(".gkStartDateClass").val();
		if(!isYsy && strGkStartDate==''){
			layer.tips('不能为空!', $(this).find(".gkStartDateClass"), {
				tipsMore: true,
				tips: 3
			});
			if(!b){
				$(this).find(".gkStartDateClass").focus();
				b=true;
			}
		}
		if(!isYsy && strGkEndDate==''){
			layer.tips('不能为空!', $(this).find(".gkEndDateClass"), {
				tipsMore: true,
				tips: 3
			});
			if(!b){
				$(this).find(".gkEndDateClass").focus();
				b=true;
			}
		}

		if(!isYsy && strGkStartDate>strGkEndDate){
			layer.tips('考试结束时间不能小于考试开始时间!', $(this).find(".gkEndDateClass"), {
					tipsMore: true,
					tips: 3
				});
			if(!b){
				$(this).find(".gkEndDateClass").focus();
				b=true;
			}
		}
		if(!isXue && strStartDate>strEndDate){
			layer.tips('考试结束时间不能小于考试开始时间!', $(this).find(".endDateClass"), {
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
			var e=strEndDate.substring(0,10);
			var se=strGkStartDate.substring(0,10);
			var ge=strGkEndDate.substring(0,10);
			if(!isYsy && se!=ge){
				layer.tips('考试开始时间与考试结束时间需要在同一天!', $(this).find(".gkEndDateClass"), {
					tipsMore: true,
					tips: 3
				});
				if(!b){
					$(this).find(".gkEndDateClass").focus();
					b=true;
				}
			}
			if(!isXue && s!=e){
				layer.tips('考试开始时间与考试结束时间需要在同一天!', $(this).find(".endDateClass"), {
					tipsMore: true,
					tips: 3
				});
				if(!b){
					$(this).find(".endDateClass").focus();
					b=true;
				}
			}else{
				//非7选3的科目 时间不能交叉
				if(checkDate){
                    if(i>0){
                        var c=false;
                        for(j=0;j<i;j++){
                            if((!isXue && strEndDate<=startTimes[j]) || strStartDate>=endTimes[j] || (!isYsy && strStartDate>=gkendTimes[j])){

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
                            endTimes[i]=strEndDate;
                            gkendTimes[i]=strGkEndDate;
                            i++;
                        }
                    }else{
                        startTimes[i]=strStartDate;
                        endTimes[i]=strEndDate;
                        gkendTimes[i]=strGkEndDate;
                        i++;
                    }
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
		error:function(XMLHttpRequest, textStatus, errorThrown){ } 
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

