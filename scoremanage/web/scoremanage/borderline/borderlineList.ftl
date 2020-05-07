<#import "/fw/macro/taskJobWeb.ftl" as taskWeb>
<form id="myform">
<div class="win-box gradeset">
	<div class="box-header">
		<h4 class="box-title showExamNameClass" style="display:none">
		</h4>
		<h4 class="box-title">
			分数线设定
		</h4><br>
		<span style="color:red">
		统计方式说明：1、百分比：统计时取该科目分数的百分比。2、名次：统计时取该名次的分数。3、分值：统计时取该分数
		</span>
	</div>
	<#assign borIndex=-1>
	<div class="box-body">
		<#if borderlineMap?? && (borderlineMap?size>0)>
			<#list borderlineMap?keys as key>
				<div class="gradeset-body">
					<h5 class="gradeset-name">${key?split(',')[1]}分数线</h5>
					<div class="gradeset-content">
						<input type="hidden" class="hidExamId" value="${examId}">
						<input type="hidden" class="hidsubjectId" value="${key?split(',')[0]}">
						<input type="hidden" class="hidStatType" value="10">
						<input type="hidden" class="hidGradeCode" value="${gradeCode}">
						<#if (borderlineMap[key]?size>0)>
						<#list borderlineMap[key] as item>
							<#assign borIndex=borIndex+1>
							<div class="gradeset-item statType1Div">
								<a href="javascript:void(0);" class="red gradeset-close existClass"><i class="fa fa-trash"></i></a>
								<input type="hidden" class="idClass" name="borderlineList[${borIndex}].id" value="${item.id}">
								<input type="hidden" class="examIdClass" name="borderlineList[${borIndex}].examId" value="${item.examId}">
								<input type="hidden" class="subjectIdClass" name="borderlineList[${borIndex}].subjectId" value="${item.subjectId}">
								<input type="hidden" class="statTypeClass" name="borderlineList[${borIndex}].statType" value="${item.statType}">
								<input type="hidden" class="gradeCodeClass" name="borderlineList[${borIndex}].gradeCode" value="${item.gradeCode}">
								
								<input class="form-control nameOrUpClass" name="borderlineList[${borIndex}].nameOrUp" type="text" placeholder="请输入分数线名称" value="${item.nameOrUp!}">
								<select class="form-control statMethodDoClass" name="borderlineList[${borIndex}].statMethodDo">
									<option value="">请选择统计方式</option>
									<#if statType1?? && (statType1?size>0)>
										<#list statType1?keys as key>
											<option value="${key}" <#if key?default('a')==item.statMethodDo?default('b')>selected</#if> >${statType1[key]}</option>
										</#list>
									</#if>
								</select>
								<input class="form-control ratioValueClass" name="borderlineList[${borIndex}].ratioValue" type="text" placeholder="请输入相关值" value="${item.ratioValue!}">
								<span></span>
							</div>
						</#list>
						</#if>
					</div>
					<a href="javascript:" class="btn btn-lightblue js-gradeset-add">+增加分数线</a>
				</div>
			</#list>
		</#if>
	</div>
</div><!-- 分数线设定部分结束 -->
<!-- 分段成绩设定部分开始 -->
<#--
<div class="win-box gradeset">
	<div class="box-header">
		<h4 class="box-title">
			分段成绩设定
		</h4>
	</div>
	<div class="box-body">
		<#if gardeSectionMap?? && (gardeSectionMap?size>0)>
			<#list gardeSectionMap?keys as key>
				<div class="gradeset-body">
					<h5 class="gradeset-name">${key?split(',')[1]}成绩</h5>
					<#assign statType21="">
					<#assign statType22="">
					<#assign statType23="">
					<#if (gardeSectionMap[key]?size>0)>
						<#list gardeSectionMap[key] as item>
							<#if item.statType?default('')=='21'>
							<#assign statType21=item>
							<#elseif item.statType?default('')=='22'>
							<#assign statType22=item>
							<#elseif item.statType?default('')=='23'>
							<#assign statType23=item>
							</#if>
						</#list>
					</#if>
					<div class="gradeset-content">
						<div class="gradeset-item statType2Div">
							<label>
								<input name="gardeSectionList[${key_index*3}].isUsing" type="checkbox" class="wp isUsingClass" value="1" <#if statType21!="" && statType21.isUsing==1>checked</#if>>
								<span class="lbl"> 百分比</span>
							</label>
							<#if statType21!="">
								<input type="hidden" class="idClass" name="gardeSectionList[${key_index*3}].id" value="${statType21.id}">
								<input type="hidden" class="examIdClass" name="gardeSectionList[${key_index*3}].examId" value="${statType21.examId}">
								<input type="hidden" class="subjectIdClass" name="gardeSectionList[${key_index*3}].subjectId" value="${statType21.subjectId}">
								<input type="hidden" class="statTypeClass" name="gardeSectionList[${key_index*3}].statType" value="${statType21.statType}">
								<input type="text" class="form-control statMethodDoClass" name="gardeSectionList[${key_index*3}].statMethodDo" class="input-sm" placeholder="请输入下限值" value="${statType21.statMethodDo!}"> %
								<input type="text" class="form-control nameOrUpClass" name="gardeSectionList[${key_index*3}].nameOrUp" class="input-sm" placeholder="请输入上限值" value="${statType21.nameOrUp!}"> %
								<input type="text" class="form-control ratioValueClass" name="gardeSectionList[${key_index*3}].ratioValue" class="input-sm" placeholder="请输入间隔值" value="${statType21.ratioValue!}"> %
							<#else>
								<input type="hidden" class="idClass" name="gardeSectionList[${key_index*3}].id" value="">
								<input type="hidden" class="examIdClass" name="gardeSectionList[${key_index*3}].examId" value="${examId}">
								<input type="hidden" class="subjectIdClass" name="gardeSectionList[${key_index*3}].subjectId" value="${key?split(',')[0]}">
								<input type="hidden" class="statTypeClass" name="gardeSectionList[${key_index*3}].statType" value="21">
								<input type="text" name="form-control gardeSectionList[${key_index*3}].statMethodDo" class="input-sm statMethodDoClass" placeholder="请输入下限值" value=""> %
								<input type="text" name="form-control gardeSectionList[${key_index*3}].nameOrUp" class="input-sm nameOrUpClass" placeholder="请输入上限值" value=""> %
								<input type="text" name="form-control gardeSectionList[${key_index*3}].ratioValue" class="input-sm ratioValueClass" placeholder="请输入间隔值" value=""> %
							</#if>
						</div>
						<div class="gradeset-item statType2Div">
							<label>
								<input name="gardeSectionList[${key_index*3+1}].isUsing" type="checkbox" class="wp isUsingClass" value="1" <#if statType22!="" && statType22.isUsing==1>checked</#if>>
								<span class="lbl"> 名&emsp;次</span>
							</label>
							<#if statType22!="">
								<input type="hidden" class="idClass" name="gardeSectionList[${key_index*3+1}].id" value="${statType22.id}">
								<input type="hidden" class="examIdClass" name="gardeSectionList[${key_index*3+1}].examId" value="${statType22.examId}">
								<input type="hidden" class="subjectIdClass" name="gardeSectionList[${key_index*3+1}].subjectId" value="${statType22.subjectId}">
								<input type="hidden" class="statTypeClass" name="gardeSectionList[${key_index*3+1}].statType" value="${statType22.statType}">
								<input type="text" name="form-control gardeSectionList[${key_index*3+1}].statMethodDo" class="input-sm statMethodDoClass" placeholder="请输入下限值" value="${statType22.statMethodDo!}">
								<input type="text" name="form-control gardeSectionList[${key_index*3+1}].nameOrUp" class="input-sm nameOrUpClass" placeholder="请输入上限值" value="${statType22.nameOrUp!}">
								<input type="text" name="form-control gardeSectionList[${key_index*3+1}].ratioValue" class="input-sm ratioValueClass" placeholder="请输入间隔值" value="${statType22.ratioValue!}">
							<#else>
								<input type="hidden" class="idClass" name="gardeSectionList[${key_index*3+1}].id" value="">
								<input type="hidden" class="examIdClass" name="gardeSectionList[${key_index*3+1}].examId" value="${examId}">
								<input type="hidden" class="subjectIdClass" name="gardeSectionList[${key_index*3+1}].subjectId" value="${key?split(',')[0]}">
								<input type="hidden" class="statTypeClass" name="gardeSectionList[${key_index*3+1}].statType" value="22">
								<input type="text" name="form-control gardeSectionList[${key_index*3+1}].statMethodDo" class="input-sm statMethodDoClass" placeholder="请输入下限值" value="">
								<input type="text" name="form-control gardeSectionList[${key_index*3+1}].nameOrUp" class="input-sm nameOrUpClass" placeholder="请输入上限值" value="">
								<input type="text" name="form-control gardeSectionList[${key_index*3+1}].ratioValue" class="input-sm ratioValueClass" placeholder="请输入间隔值" value="">
							</#if>
						</div>
						<div class="gradeset-item statType2Div">
							<label>
								<input name="gardeSectionList[${key_index*3+2}].isUsing" type="checkbox" class="wp isUsingClass" value="1" <#if statType23!="" && statType23.isUsing==1>checked</#if>>
								<span class="lbl"> 分&emsp;值</span>
							</label>
							<#if statType23!="">
								<input type="hidden" class="idClass" name="gardeSectionList[${key_index*3+2}].id" value="${statType23.id}">
								<input type="hidden" class="examIdClass" name="gardeSectionList[${key_index*3+2}].examId" value="${statType23.examId}">
								<input type="hidden" class="subjectIdClass" name="gardeSectionList[${key_index*3+2}].subjectId" value="${statType23.subjectId}">
								<input type="hidden" class="statTypeClass" name="gardeSectionList[${key_index*3+2}].statType" value="${statType23.statType}">
								<input type="text" name="form-control gardeSectionList[${key_index*3+2}].statMethodDo" class="input-sm statMethodDoClass" placeholder="请输入下限值" value="${statType23.statMethodDo!}">
								<input type="text" name="form-control gardeSectionList[${key_index*3+2}].nameOrUp" class="input-sm nameOrUpClass" placeholder="请输入上限值" value="${statType23.nameOrUp!}">
								<input type="text" name="form-control gardeSectionList[${key_index*3+2}].ratioValue" class="input-sm ratioValueClass" placeholder="请输入间隔值" value="${statType23.ratioValue!}">
							<#else>
								<input type="hidden" class="idClass" name="gardeSectionList[${key_index*3+2}].id" value="">
								<input type="hidden" class="examIdClass" name="gardeSectionList[${key_index*3+2}].examId" value="${examId}">
								<input type="hidden" class="subjectIdClass" name="gardeSectionList[${key_index*3+2}].subjectId" value="${key?split(',')[0]}">
								<input type="hidden" class="statTypeClass" name="gardeSectionList[${key_index*3+2}].statType" value="23">
								<input type="text" name="form-control gardeSectionList[${key_index*3+2}].statMethodDo" class="input-sm statMethodDoClass" placeholder="请输入下限值" value="">
								<input type="text" name="form-control gardeSectionList[${key_index*3+2}].nameOrUp" class="input-sm nameOrUpClass" placeholder="请输入上限值" value="">
								<input type="text" name="form-control gardeSectionList[${key_index*3+2}].ratioValue" class="input-sm ratioValueClass" placeholder="请输入间隔值" value="">
							</#if>
						</div>
					</div>
				</div>
			</#list>
		</#if>
	</div>
</div>
-->
</form>
<@taskWeb.commonWeb taskUrl="/scoremanage/borderline/statistic" taskUrlDatas="examId" 
	taskUrlDataIds="examIdSearch" taskBtnId = "taskBtnId" isShowTask = false showTaskBtnId = "showTaskBtnId" 
	taskBusinessType=businessType>
	<div class="page-btns" id="savaBtenDiv">
		<button class="btn btn-blue saveBtn" onClick="saveInfo()">保存</button>
		<button class="btn btn-blue " id="taskBtnId" >统计</button>
	</div>
</@taskWeb.commonWeb>
<script type="text/javascript">
$("#showDiv").show();
$(".showExamNameClass").prepend(oldExamName);
var index=-1;
<#if borderlineMap?? && borderlineMap?size==0>
	layer.tips('未设置考试科目!', $("#examIdSearch"), {
		tipsMore: true,
		tips: 2
	});
	$("#showDiv").hide();
<#else>
	$("#showDiv").show();
	index=${borIndex};
</#if>
$('.js-gradeset-add').click(function(){
	index++;
	var examId=$(this).parent().find('.hidExamId').val();
	var subjectId=$(this).parent().find('.hidsubjectId').val();
	var statType=$(this).parent().find('.hidStatType').val();
	var gradeCode=$(this).parent().find('.hidGradeCode').val();
	var htmlStr='<div class="gradeset-item statType1Div">';
	htmlStr+='<a href="javascript:" class="red gradeset-close"><i class="fa fa-trash"></i></a>';
	htmlStr+='<input type="hidden" class="idClass" name="borderlineList['+index+'].id" value="">';
	htmlStr+='<input type="hidden" class="examIdClass" name="borderlineList['+index+'].examId" value="'+examId+'">';
	htmlStr+='<input type="hidden" class="subjectIdClass" name="borderlineList['+index+'].subjectId" value="'+subjectId+'">';
	htmlStr+='<input type="hidden" class="statTypeClass" name="borderlineList['+index+'].statType" value="'+statType+'">';
	htmlStr+='<input type="hidden" class="gradeCodeClass" name="borderlineList['+index+'].gradeCode" value="'+gradeCode+'">';
	
	htmlStr+='<input class="form-control nameOrUpClass" name="borderlineList['+index+'].nameOrUp" type="text" placeholder="请输入分数线名称">';
	htmlStr+='<select class="form-control statMethodDoClass" name="borderlineList['+index+'].statMethodDo" id="">';
	htmlStr+='<option value="">请选择统计方式</option>';
	<#if statType1?? && (statType1?size>0)>
		<#list statType1?keys as key>
			htmlStr+='<option value="${key}" >${statType1[key]}</option>'
		</#list>
	</#if>
	htmlStr+='</select>'
	htmlStr+='<input class="form-control ratioValueClass" name="borderlineList['+index+'].ratioValue" type="text" placeholder="请输入相关值">&nbsp;<span></span></div>';
	//htmlStr='<div class="gradeset-item"><a href="javascript:void(0);" class="red gradeset-close"><i class="fa fa-trash"></i></a><input class="input-sm" type="text" placeholder="请输入分数线名称">&nbsp;<select name="" id=""><option value="0">请输入统计方式</option><option value="1">请输入百分比</option><option value="2">请输入名次</option><option value="3">请输入分值</option></select>&nbsp;<input class="input-sm" type="text" placeholder="请输入百分比">&nbsp;<span></span></div>';
	$(this).parent().find('.gradeset-content').append(htmlStr);
	bindingAction();
});
function doDeleteById(ids,thisObj){
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
	var ii = layer.load();
	$.ajax({
		url:'${request.contextPath}/scoremanage/borderline/delete',
		data: {'id':sendIds},
		type:'post',
		success:function(data) {
			var jsonO = JSON.parse(data);
	 		if(jsonO.success){
				layer.closeAll();
				thisObj.parent().remove();
	 		}
	 		else{
				layerTipMsg(jsonO.success,"失败",jsonO.msg);
			}
			layer.close(ii);
		},
 		error : function(XMLHttpRequest, textStatus, errorThrown) {}
	});
}
function bindingAction(){
	$('.gradeset-close').on('click',function(){
		var thisObj = $(this);
		if($(this).hasClass('existClass')){
			var id = $(this).parent().find('.idClass').val();
			showConfirmMsg('确认删除已保存的记录？','提示',function(){
				doDeleteById(id,thisObj);
			});
		}else{
			$(this).parent().remove();
		}
	});
}
bindingAction();
function saveInfo(){
	if(isSubmit){
		return;
	}
	isSubmit = true;
	var b=false;
	$(".statType1Div").each(function(){
		//各种校验
		var nameOrUp = $(this).find(".nameOrUpClass").val($.trim($(this).find(".nameOrUpClass").val())).val();
		var statMethodDo = $(this).find(".statMethodDoClass").val();
		var ratioValue = $(this).find(".ratioValueClass").val($.trim($(this).find(".ratioValueClass").val())).val();
		var reg = /^(([0-9])|([1-9][0-9]{1,2})|([0-9]\.[0-9]{1})|([1-9][0-9]{1,2}\.[0-9]{1}))$/;
		var r;
		if(nameOrUp==''){
			layer.tips('不能为空!', $(this).find(".nameOrUpClass"), {
				tipsMore: true,
				tips: 3
			});
			if(!b){
				$(this).find(".nameOrUpClass").focus();
				b=true;
			}
		}else{
			if(nameOrUp.length>30){
				layer.tips('长度不能大于30', $(this).find(".nameOrUpClass"), {
					tipsMore: true,
					tips: 3
				});
				if(!b){
					$(this).find(".nameOrUpClass").focus();
					b=true;
				}
			}
		}
		if(statMethodDo==''){
			layer.tips('不能为空!', $(this).find(".statMethodDoClass"), {
				tipsMore: true,
				tips: 3
			});
			if(!b){
				$(this).find(".statMethodDoClass").focus();
				b=true;
			}
		}
		if(ratioValue==''){
			layer.tips('不能为空!', $(this).find(".ratioValueClass"), {
				tipsMore: true,
				tips: 3
			});
			if(!b){
				$(this).find(".ratioValueClass").focus();
				b=true;
			}
		}else{
			if(statMethodDo!=''){
				if(statMethodDo=='11'){
					r = ratioValue.match(reg);
					if(r==null){
						layer.tips('格式不正确(最多3位整数，1位小数)!', $(this).find(".ratioValueClass"), {
							tipsMore: true,
							tips: 3
						});
						if(!b){
							$(this).find(".ratioValueClass").focus();
							b=true;
						}
					}else if(!(parseFloat(ratioValue)>0 && parseFloat(ratioValue)<=100)){
						layer.tips('必须(0,100]之间', $(this).find(".ratioValueClass"), {
							tipsMore: true,
							tips: 3
						});
						if(!b){
							$(this).find(".ratioValueClass").focus();
							b=true;
						}
					}
				}else if(statMethodDo=='12'){
					r = ratioValue.match(/^(([1-9])|([1-9][0-9]{1,4}))$/);
					if(r==null){
						layer.tips('格式不正确(最多5位正整数)!', $(this).find(".ratioValueClass"), {
							tipsMore: true,
							tips: 3
						});
						if(!b){
							$(this).find(".ratioValueClass").focus();
							b=true;
						}
					}
				}else if(statMethodDo=='13'){
					r = ratioValue.match(reg);
					if(r==null){
						layer.tips('格式不正确(最多3位整数，1位小数)!', $(this).find(".ratioValueClass"), {
							tipsMore: true,
							tips: 3
						});
						if(!b){
							$(this).find(".ratioValueClass").focus();
							b=true;
						}
					}else if(!(parseFloat(ratioValue)>0 && parseFloat(ratioValue)<=999)){
						layer.tips('必须(0,999]之间', $(this).find(".ratioValueClass"), {
							tipsMore: true,
							tips: 3
						});
						if(!b){
							$(this).find(".ratioValueClass").focus();
							b=true;
						}
					}
				}
			}
		}
	});
	
	$(".statType2Div").each(function(){
		//各种校验
		var statType = $(this).find(".statTypeClass").val();
		var statMethodDo = $(this).find(".statMethodDoClass").val($.trim($(this).find(".statMethodDoClass").val())).val();
		var nameOrUp = $(this).find(".nameOrUpClass").val($.trim($(this).find(".nameOrUpClass").val())).val();
		var ratioValue = $(this).find(".ratioValueClass").val($.trim($(this).find(".ratioValueClass").val())).val();
		var reg = /^(([0-9])|([1-9][0-9]{1,2})|([0-9]\.[0-9]{1})|([1-9][0-9]{1,2}\.[0-9]{1}))$/;
		var r;
		if(statMethodDo == '' && $(this).find('.isUsingClass').is(':checked')){
			layer.tips('不能为空!', $(this).find(".statMethodDoClass"), {
				tipsMore: true,
				tips: 3
			});
			if(!b){
				$(this).find(".statMethodDoClass").focus();
				b=true;
			}
		}else if(statMethodDo != ''){
			if(statType == '22'){
				r = statMethodDo.match(/^(([0-9])|([1-9][0-9]{1,4}))$/);
				if(r==null){
					layer.tips('格式不正确(最多5位整数)!', $(this).find(".statMethodDoClass"), {
						tipsMore: true,
						tips: 3
					});
					if(!b){
						$(this).find(".statMethodDoClass").focus();
						b=true;
					}
				}else if(statType == '22' && !(parseFloat(statMethodDo)>0 && parseFloat(statMethodDo)<=99999)){
					layer.tips('必须(0,99999]之间', $(this).find(".statMethodDoClass"), {
						tipsMore: true,
						tips: 3
					});
					if(!b){
						$(this).find(".statMethodDoClass").focus();
						b=true;
					}
				}
			}else{
				r = statMethodDo.match(reg);
				if(r==null){
					layer.tips('格式不正确(最多3位整数，1位小数)!', $(this).find(".statMethodDoClass"), {
						tipsMore: true,
						tips: 3
					});
					if(!b){
						$(this).find(".statMethodDoClass").focus();
						b=true;
					}
				}else{
					if(statType == '21' && !(parseFloat(statMethodDo)>0 && parseFloat(statMethodDo)<=100)){
						layer.tips('必须(0,100]之间', $(this).find(".statMethodDoClass"), {
							tipsMore: true,
							tips: 3
						});
						if(!b){
							$(this).find(".statMethodDoClass").focus();
							b=true;
						}
					}else if(statType == '23' && !(parseFloat(statMethodDo)>0 && parseFloat(statMethodDo)<=999)){
						layer.tips('必须(0,999]之间', $(this).find(".statMethodDoClass"), {
							tipsMore: true,
							tips: 3
						});
						if(!b){
							$(this).find(".statMethodDoClass").focus();
							b=true;
						}
					}
				}
			}
		}
		if(nameOrUp == '' && $(this).find('.isUsingClass').is(':checked')){
			layer.tips('不能为空!', $(this).find(".nameOrUpClass"), {
				tipsMore: true,
				tips: 3
			});
			if(!b){
				$(this).find(".nameOrUpClass").focus();
				b=true;
			}
		}else if(nameOrUp != ''){
			if(statType == '22'){
				r = nameOrUp.match(/^(([0-9])|([1-9][0-9]{1,4}))$/);
				if(r==null){
					layer.tips('格式不正确(最多5位整数)!', $(this).find(".nameOrUpClass"), {
						tipsMore: true,
						tips: 3
					});
					if(!b){
						$(this).find(".nameOrUpClass").focus();
						b=true;
					}
				}else if(statType == '22' && !(parseFloat(nameOrUp)>0 && parseFloat(nameOrUp)<=99999)){
					layer.tips('必须(0,99999]之间', $(this).find(".nameOrUpClass"), {
						tipsMore: true,
						tips: 3
					});
					if(!b){
						$(this).find(".nameOrUpClass").focus();
						b=true;
					}
				}
			}else{
				r = nameOrUp.match(reg);
				if(r==null){
					layer.tips('格式不正确(最多3位整数，1位小数)!', $(this).find(".nameOrUpClass"), {
						tipsMore: true,
						tips: 3
					});
					if(!b){
						$(this).find(".nameOrUpClass").focus();
						b=true;
					}
				}else{
					if(statType == '21' && !(parseFloat(nameOrUp)>0 && parseFloat(nameOrUp)<=100)){
						layer.tips('必须(0,100]之间', $(this).find(".nameOrUpClass"), {
							tipsMore: true,
							tips: 3
						});
						if(!b){
							$(this).find(".nameOrUpClass").focus();
							b=true;
						}
					}else if(statType == '23' && !(parseFloat(nameOrUp)>0 && parseFloat(nameOrUp)<=999)){
						layer.tips('必须(0,999]之间', $(this).find(".nameOrUpClass"), {
							tipsMore: true,
							tips: 3
						});
						if(!b){
							$(this).find(".nameOrUpClass").focus();
							b=true;
						}
					}
				}
			}
		}
		if(ratioValue == '' && $(this).find('.isUsingClass').is(':checked')){
			layer.tips('不能为空!', $(this).find(".ratioValueClass"), {
				tipsMore: true,
				tips: 3
			});
			if(!b){
				$(this).find(".ratioValueClass").focus();
				b=true;
			}
		}else if(ratioValue != ''){
			if(statType == '22'){
				r = ratioValue.match(/^(([0-9])|([1-9][0-9]{1,4}))$/);
				if(r==null){
					layer.tips('格式不正确(最多5位整数)!', $(this).find(".ratioValueClass"), {
						tipsMore: true,
						tips: 3
					});
					if(!b){
						$(this).find(".ratioValueClass").focus();
						b=true;
					}
				}
			}else{
				r = ratioValue.match(reg);
				if(r==null){
					layer.tips('格式不正确(最多3位整数，1位小数)!', $(this).find(".ratioValueClass"), {
						tipsMore: true,
						tips: 3
					});
					if(!b){
						$(this).find(".ratioValueClass").focus();
						b=true;
					}
				}
			}
		}
		if(!b && statMethodDo!='' && nameOrUp!=''){
			if(parseFloat(statMethodDo) >= parseFloat(nameOrUp)){
				layer.tips('上限值必须大于下限值!', $(this).find(".nameOrUpClass"), {
					tipsMore: true,
					tips: 3
				});
				$(this).find(".nameOrUpClass").focus();
				b=true;
			}
		}
		if(!b && statMethodDo!='' && nameOrUp!='' && ratioValue!=''){
			if(parseFloat(ratioValue)>(parseFloat(nameOrUp)-parseFloat(statMethodDo))){
				layer.tips('不能大于前两项差值!', $(this).find(".ratioValueClass"), {
					tipsMore: true,
					tips: 3
				});
				$(this).find(".ratioValueClass").focus();
				b=true;
			}
		}
	});
	if(b){
		isSubmit = false;
		return;
	}
	$(".saveBtn").addClass("disabled");
	var ii = layer.load();
	var options = {
		url : "${request.contextPath}/scoremanage/borderline/list/save",
		dataType : 'json',
		success : function(data){
 			$(".saveBtn").removeClass("disabled");
 			isSubmit = false;
 			if(!data.success){
	 			layerTipMsg(data.success,"失败",data.msg);
 			}else{
 				layerTipMsg(data.success,"成功",data.msg);
				oldQuery();
 			}
 			layer.close(ii);
		},
		clearForm : false,
		resetForm : false,
		type : 'post',
		error:function(XMLHttpRequest, textStatus, errorThrown){}//请求出错 
	};
	$("#myform").ajaxSubmit(options);
}
</script>