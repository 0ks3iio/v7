<#import  "/fw/macro/htmlcomponent.ftl" as htmlcomponent />
<form id="scoreForm">
<input type="hidden" name="examId" value="${examId!}"/>
<input type="hidden" name="subjectId" value="${subjectId!}"/>
<input type="hidden" name="subjectInfoId" value="${subjectInfoId!}"/>
<input type="hidden" name="classIdSearch" value="${classIdSearch!}"/>
<input type="hidden" name="classType" value="${classType!}"/>
<input type="hidden" name="unitId" value="${unitId!}"/>
<input type="hidden" name="inputType" value="${subjectInfo.inputType!}"/>
<input type="hidden" name="gradeType" value="${subjectInfo.gradeType!}"/>

<#--<div class="table-container-header clearfix">
	<label class="pull-right no-margin">
		<label for="" class="control-label no-padding-right">解锁：</label>
		<input type="checkbox" class="wp wp-switch js-toggleLock" <#if classInfo?exists && ((classInfo.isLock!'0') != '1')>checked="true"</#if>><span class="lbl"></span>
	</label>
</div>-->
<div class="table-wrapper">
	<table class="table table-bordered table-striped table-hover no-margin" id="showGradeTabble">
		<thead>
			<tr>
				<th>姓名</th>
				<th>考号</th>
				<th>学号</th>
				<th>班级</th>
				<th>考试成绩</th>
				<th>状态</th>
				<#if needGeneral?exists && needGeneral>
			        <th>总评成绩</th>
		        </#if>
			</tr>
		</thead>
		<tbody>
			<#if dtoList?exists && (dtoList?size > 0)>
				<#list dtoList as dto>
					<tr>
						<td>
						<input type="hidden" name="dtoList[${dto_index}].id" value="${dto.scoreId!}"/>
						<input type="hidden" name="dtoList[${dto_index}].studentId" value="${dto.stuId!}"/>
						<input type="hidden" name="dtoList[${dto_index}].classId" value="${dto.classId!}"/>
						<input type="hidden" name="dtoList[${dto_index}].teachClassId" value="${dto.teachClassId!}"/>
						${dto.stuName!}
						</td>
						<td>${dto.stuExamNum!}</td>
						<td>${dto.stuCode!}</td>
						<td>${dto.className!}</td>
						<#if subjectInfo.inputType=='S'>
							<td>
							<#if isCanScoreInfo gt -1>
								<input type="text"  class="table-input score_class" name="dtoList[${dto_index}].score" id="score_${dto_index}" <#if classInfo?exists && (classInfo.isLock!'0') == '1'>disabled</#if> vtype = "number" nullable="false"  placeholder="请输入考试成绩" value="${dto.score!}" maxLength="7">
							<#else>
								<input type="text"  readonly="readonly" class="table-input" name="dtoList[${dto_index}].score" id="score_${dto_index}" <#if classInfo?exists && (classInfo.isLock!'0') == '1'>disabled</#if> vtype = "number" nullable="false"  placeholder="请输入考试成绩" value="${dto.score!}">
							</#if>
							</td>
						<#else>
							<td>
								<select <#if classInfo?exists && (classInfo.isLock!'0') == '1'>disabled</#if> name="dtoList[${dto_index}].score" id="score_${dto_index}" <#if (isCanScoreInfo == -1)>  disabled="disabled"</#if> class="form-control score_class">
							 		${mcodeSetting.getMcodeSelect('${subjectInfo.gradeType!}','${dto.score!}',"1")}
								</select>
							</td>
						</#if>
						<td>
							<select <#if classInfo?exists && (classInfo.isLock!'0') == '1'>disabled</#if> name="dtoList[${dto_index}].scoreStatus" id="scoreStatus_${dto_index}" class="form-control status_class" <#if (isCanScoreInfo == -1)>  disabled="disabled"</#if> notnull="false">
						 		${mcodeSetting.getMcodeSelect("DM-CJLX", '${dto.scoreStatus!}', "0")}
							</select>
						</td>
						<#if needGeneral?exists && needGeneral>
					        <td>
					        	<input type="text" class="table-input score_class" name="dtoList[${dto_index}].toScore" id="toScore_${dto_index}" <#if (classInfo?exists && (classInfo.isLock!'0') == '1') || (isCanScoreInfo == -1)>disabled</#if> vtype = "number" nullable="false"  placeholder="请输入总评成绩" value="${dto.toScore!}" maxLength="7">
					        </td>
				        </#if>
					</tr>
				</#list>
		</#if>
		</tbody>
	</table>		
</div>
</form>
<br/>
<div class="row">
	<#if classInfo?exists && (classInfo.isLock!'0') == '1'>
		<div class="col-xs-12 text-center">
			<a href="javascript:" class="btn btn-blue" onclick="doPrintPre()">打印</a>
			<div style="display:none;">
			<@htmlcomponent.printToolBar container="#printContent" btn2="false" btn3="false"  printDirection='true' printUp=0 printLeft=0 printBottom=0 printRight=0/>
			</div>
		</div>
		<script>
			if($("#printBtn").length < 1)
				$("#findDiv").append($('<a id="printBtn" href="javascript:" class="btn btn-blue" onclick="doPrintPre()">打印</a>'));
		</script>
	<#else>
		<script>
			$("#printBtn").remove();
		</script>
		<#if isCanScoreInfo gt -1>
			<div class="col-xs-12 text-right">
					<a href="javascript:void(0);" class="btn btn-blue" onclick="save();">保存</a>
				<a href="javascript:void(0);" class="btn btn-blue" id="mysubmit">提交</a>
			</div>
		</#if>
	</#if>
</div>
<#--打印内容    杭州外国语学校2016学年第1学期-->
<#if classInfo?exists && (classInfo.isLock!'0') == '1'>
<div id="printContent" style="display:none;">
	<#-- 杭外的，临时处理，将单位名称中的数字校园给去掉 -->
	<h3 class="text-center">${unitName?replace("数字校园", "") + "("+acadyear+semesterName + ")"!}<br>${examName!}成绩登记表</h3>
	<div class="col-xs-12">
    	<span class="col-xs-4">班级：${className!}</span>
		<span class="col-xs-4">科目：${subjectName!}</span>
	</div>
	
	<table class="table table-bordered table-condensed no-margin text-center" style="border:1px solid #000">
	    <thead style="border:1px solid #000">
	    	<tr >
		        <th class="text-center" width="50px;" style="border:1px solid #000">序号</th>
		        <th class="text-center" width="200px;"  style="border:1px solid #000">姓名</th>
		        <th class="text-center" width="10%"  style="border:1px solid #000">考试成绩</th>
		        <#if bindingNameList?exists && bindingNameList?size gt 0>
		        <#list bindingNameList as sname>
			        <th width="10%" class="text-center" style="border:1px solid #000">${sname!}</th>
		        </#list>
		        </#if>
		        
		        <#if needGeneral?exists && needGeneral>
			        <th width="10%" class="text-center" style="border:1px solid #000">总评成绩</th>
		        </#if>
		        
				<th width="20px;" style="border:1px solid #000"></th>
		        
		        <th width="50px;" class="text-center" style="border:1px solid #000">序号</th>
		        <th width="200px;" class="text-center" style="border:1px solid #000">姓名</th>
		        <th width="10%" class="text-center" style="border:1px solid #000">考试成绩</th>
		        <#if bindingNameList?exists && bindingNameList?size gt 0>
		        <#list bindingNameList as sname>
			        <th width="10%" class="text-center" style="border:1px solid #000">${sname!}</th>
		        </#list>
		        </#if>
		        <#if needGeneral?exists && needGeneral>
			        <th width="10%" class="text-center" style="border:1px solid #000">总评成绩</th>
		        </#if>
	        </tr>
	    </thead>
	    <tbody>
	     <#if rowCount?default(0) gt 0>
	    <#list 1..rowCount as item>
	    	<tr >
		        <td style="border:1px solid #000">${item_index+1}</td>
		        <td style="border:1px solid #000" class="text-left">${dtoList[item_index].stuName!}</td>
		        <td style="border:1px solid #000">${dtoList[item_index].score!}</td>
		        
		        <#if bindingNameList?exists && bindingNameList?size gt 0>
		        <#list bindingNameList as sname>
			        <td style="border:1px solid #000">${dtoList[item_index].bindingScores[sname_index]!}</td>
		        </#list>
		        </#if>
		        
		        <#if needGeneral?exists && needGeneral>
			        <td style="border:1px solid #000">${dtoList[item_index].toScore!}</td>
		        </#if>
		        
		        <#if item_index == 0>
		      		<td style="border:1px solid #000" rowspan="${rowCount!}"></td>
		      	</#if>
		        
		        <#if !(stuCount lt (item_index+rowCount+1))>
			        <td style="border:1px solid #000">${item_index+rowCount+1}</td>
			        <td style="border:1px solid #000" class="text-left">${dtoList[item_index+rowCount].stuName!}</td>
			        <td style="border:1px solid #000">${dtoList[item_index+rowCount].score!}</td>
			        <#if bindingNameList?exists && bindingNameList?size gt 0>
			        <#list bindingNameList as sname>
				        <td style="border:1px solid #000">${dtoList[item_index+rowCount].bindingScores[sname_index]}</td>
			        </#list>
			        </#if>
			        <#if needGeneral?exists && needGeneral>
				        <td style="border:1px solid #000">${dtoList[item_index+rowCount].toScore!}</td>
			        </#if>

		        </#if>
	        </tr>
	    </#list>
	    </#if>
	    </tbody>
	</table>
	<p />
	<div style="col-xs-12" style="margin-top:20px;padding-top:20px;">
    	<span class="col-xs-5">备课组长（签字）：&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</span>
		<span class="col-xs-5">任课老师（签字）：&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</span>
		<span class="col-xs-2">打印时间：<span id="time"></span></span>
	</div>
</div>
</#if>
<script>
<#--
<#if (isCanScoreInfo == 0)>
$(function(){
	//解锁 上锁
    $('.js-toggleLock').on('change', function(){
		if($(this).prop('checked') === true){
			//解锁
			//alert('js');
			changeLockStatus("0");
		}else{
			//上锁
			//alert('ss');
			changeLockStatus("1");
		}
	});
});
</#if>-->
<#if (isCanScoreInfo == 0) || !(classInfo?exists && (classInfo.isLock!'0') == '1') && (isCanScoreInfo gt -1)>
function changeLockStatus(isLock,index){
	var examClassId = '${classInfo.id!}';
	url = '${request.contextPath}/scoremanage/scoreInfo/lock';
	params = 'examClassId='+examClassId+'&isLock='+isLock;
	
	function submit(index){
		$.ajax({
			type: "POST",
			url: url,
			data: params,
			success: function(msg){
				if(msg.success){
					if(isLock == null){
						layerTipMsg(true,"成功",msg.msg);
					}
					$('#subType [role="presentation"][class="active"] a').click();
				}else{
					layerTipMsg(msg.success,"失败",data.msg);
				}
			},
			dataType: "JSON"
    	});
		
		layer.close(index);
	}
	
	if(isLock == null){
		submit(index);
	}else{
		submit();
	}
}
</#if>
<#if !(classInfo?exists && (classInfo.isLock!'0') == '1') && (isCanScoreInfo gt -1)>
$(function(){
	//$(".score_class").val(62);
    // 回车获取焦点
    //$('input:text:first').focus();
    var $inp = $('input:text');
    $inp.on('keydown', function (e) {
        var key = e.which;
        if (key == 13) {
            e.preventDefault();
            var nxtIdx = $inp.index(this) + 1;
            $(":input:text:eq(" + nxtIdx + ")").focus().select();
        }
    });  
    
    //提交 
    $('#mysubmit').on('click',function(){
    	if(!allCheck()){
			return;
		}
    	
    	var remark = '确定提交吗？'
				+'<br><span class="text-danger">请务必确保提交之前已经保存，'
				+'<br> 提交以后将无法修改成绩信息</span>';
		layer.confirm(remark, {
			btn: ['确定', '取消'],
			yes: function(index){
		    	save(true,index);
			}
		});
    });

	$(".status_class").each(function () {
		if ($(this).val() == "3") {
			$(this).parent().prev().find(".score_class").attr("disabled", "disabled");
			<#if subjectInfo.inputType=='S'>
			$(this).parent().prev().find(".score_class").attr("placeholder", "本次未考");
			</#if>
		}
	});

	<#if subjectInfo.inputType=='G'>
    $(".status_class").on("change", function () {
        if ($(this).val() == "3") {
			$(this).parent().prev().find("option:eq(0)").attr("selected", true);
			$(this).parent().prev().find(".score_class").attr("disabled", "disabled");
        } else {
			$(this).parent().prev().find(".score_class").removeAttr("disabled");
		}
    });
    </#if>

	<#if subjectInfo.inputType=='S'>
	$(".status_class").on("change", function () {
		if ($(this).val() == "3") {
			$(this).parent().prev().find(".score_class").attr("placeholder", "本次未考");
			$(this).parent().prev().find(".score_class").attr("disabled", "disabled");
			$(this).parent().prev().find(".score_class").val("");
		} else {
			$(this).parent().prev().find(".score_class").attr("placeholder", "请输入考试成绩");
			$(this).parent().prev().find(".score_class").removeAttr("disabled");
		}
	});
	</#if>
});

var isSubmit=false;
function save(toSubmit,index){
	if(isSubmit){
		return;
	}
	
	if(!allCheck()){
		return;
	}
	isSubmit=true;
	// 提交数据
	var options = {
		url : '${request.contextPath}/scoremanage/scoreInfo/saveAll',
		dataType : 'json',
		success : function(data){
	 		if(data.success){
	 			if(toSubmit){
	 				changeLockStatus(null,index)
	 			}else{
		 			layerTipMsg(data.success,"成功",data.msg);
					chooseTab('${subjectId!}','${subjectInfoId!}');
	 			}
	 		}
	 		else{
	 			layerTipMsg(data.success,"失败",data.msg);
	 			isSubmit=false;
			}
		},
		clearForm : false,
		resetForm : false,
		type : 'post',
		error:function(XMLHttpRequest, textStatus, errorThrown){}//请求出错 
	};
	$("#scoreForm").ajaxSubmit(options);
} 
function allCheck(){
	//var check = checkValue('#scoreForm');
	//if(!check){
	//	isSubmit=false;
	//	return false;
	//}
	
	<#if subjectInfo.inputType=='S' || (needGeneral?exists && needGeneral)>
	var f=false;
	$(".score_class").each(function(){
		if($(this).val()==''){
			if ($(this).parent().next().find("select").val() == "3") {
				return;
			}
			if ($(this).parent().prev().find("select").val() == "3") {
				return;
			}
			f=true;
			layer.tips('不能为空', $(this), {
				tipsMore: true,
				tips: 3
			});
		}
	});	
	if(f){
		isSubmit=false;
		return false;
	}
	//var reg = /^(([0-9])|([1-9][0-9]{1,2})|([0-9]\.[0-9]{1})|([1-9][0-9]{1,2}\.[0-9]{2}))$/;
	var reg=/^(0|[1-9]\d{0,2})(\.\d{1,2})?$/;
	var max=${subjectInfo.fullScore};
	
	$(".score_class").each(function(){
		if ($(this).parent().next().find("select").val() == "3") {
			return;
		}
		if ($(this).parent().prev().find("select").val() == "3") {
			return;
		}
		if ($(this).hasClass("table-input")) {
			var r = $(this).val().match(reg);
			if (r == null) {
				f = true;
				layer.tips('格式不正确(最多3位整数，2位小数)!', $(this), {
					tipsMore: true,
					tips: 3
				});
				return false;
			}
		}
		var s=parseFloat($(this).val());
		if(s>max){
			f=true;
			layer.tips('考试成绩不能超过'+max+'!', $(this), {
				tipsMore: true,
				tips: 3
			});
			return false;
		}
	});	
	if(f){
		isSubmit=false;
		return false;
	}
	</#if>
	<#if subjectInfo.inputType=='G'>
	var flag = false;
	$(".score_class").each(function() {
		if ($(this).val() == '') {
			if ($(this).parent().next().find("select").val() == "3") {
				return;
			}
			flag = true;
			layer.tips('不能为空', $(this), {
				tipsMore: true,
				tips: 3
			});
		}
	});
	if (flag) {
		isSubmit = false;
		return false;
	}
	</#if>
	//alert();
	return true;
}


<#else>
//打印成绩表
function doPrintPre(){
<#if bindMsg?exists && bindMsg != ''>
	alert("${bindMsg!}");
<#else>
	doPrintResult();
</#if>
}

</#if>
</script>