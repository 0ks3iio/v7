<#import "/fw/macro/popupMacro.ftl" as popup />
<script src="${request.contextPath}/static/components/bootstrap-datepicker/dist/js/bootstrap-datepicker.js"></script>
<script src="${request.contextPath}/static/components/bootstrap-datepicker/dist/js/bootstrap-datepicker.min.js"></script>
<script src="${request.contextPath}/static/components/bootstrap-datepicker/dist/locales/bootstrap-datepicker.zh-CN.min.js"></script>
<form id="subForm" method="post">
			<div class="layer-content" id="myDiv">
				<div class="form-horizontal">
					<div class="form-group">
						<label class="col-sm-2 control-label no-padding-right">学年</label>
						<div class="col-sm-9">
							<select id="acadyearId" name="acadyear" class="form-control">
								<#if acadyearList?exists && acadyearList?size gt 0>
									<#list acadyearList as acadyear>
										<option value="${acadyear!}"
												<#if acadyear == nowAcadyear>selected</#if>>${acadyear!}</option>
									</#list>
								</#if>
							</select>
						</div>
					</div>
					<div class="form-group">
						<label class="col-sm-2 control-label no-padding-right">学期</label>
						<div class="col-sm-9">
							<select id="semesterId" name="semester" class="form-control">
								<option value="1" <#if 1==nowSemester>selected</#if>>第一学期</option>
								<option value="2" <#if 2==nowSemester>selected</#if>>第二学期</option>
							</select>
						</div>
					</div>
					<div class="form-group">
					    <input type="hidden" name="id" value="${dyStuPunishment.id!}">
						<label class="col-sm-2 control-label no-padding-right">姓名</label>
						<div class="col-sm-9">
							<@popup.selectOneStudent clickId="studentName" columnName="学生(单选)" dataUrl="${request.contextPath}/common/div/student/popupData" id="studentId" name="studentName" dataLevel="4" type="danxuan" recentDataUrl="${request.contextPath}/common/div/student/recentData" resourceUrl="${resourceUrl}" handler="getStuCode();">
                                <input type="text" id="studentName"  class="form-control" nullable="false" value="${dyStuPunishment.studentName!}" readOnly/>
                                <input type="hidden" id="studentId" name="studentId" value="${dyStuPunishment.studentId!}"/>
                            </@popup.selectOneStudent>
						</div>
					</div>
					<div class="form-group">
						<label class="col-sm-2 control-label no-padding-right">学号</label>
						<div class="col-sm-9">
							<input type="text" class="form-control" id="studentCode" value="${dyStuPunishment.studentCode!}" readonly>
						</div>
					</div>
					<div class="form-group">
						<label class="col-sm-2 control-label no-padding-right">违纪类型</label>
						<div class="col-sm-9">
							<select name="punishTypeId" id="punishTypeId" class="form-control" nullable="false" onChange="changeScore();">
							<#if dyBusinessOptionList?exists && dyBusinessOptionList?size gt 0>
							    <option value="">--请选择--</option>
							    <#list dyBusinessOptionList as item>
							        <option value="${item.id!}-${item.isCustom!}-${item.hasScore!}" <#if '${item.id!}' == '${dyStuPunishment.punishTypeId!}'>selected</#if>>${item.optionName!}</option>
							    </#list>
							</#if>						
							</select>
						</div>
					</div>
					<div class="form-group">
						<label class="col-sm-2 control-label no-padding-right">违纪扣分</label>
						<div class="col-sm-9">
							<input type="text" name="score" id="score" vtype="number" decimalLength="1" min="0" max="999999" value="${(dyStuPunishment.score)?string('#.#')!}" class="form-control" readonly>
						</div>
					</div>

					<div class="form-group">
						<label class="col-sm-2 control-label no-padding-right">扣分原因</label>
						<div class="col-sm-9">
							<textarea name="punishContent" id="punishContent" maxLength="2000" class="form-control" nullable="false">${dyStuPunishment.punishContent!}</textarea>
						</div>
					</div>
					<div class="form-group">
						<label class="col-sm-2 control-label no-padding-right">违纪时间</label>
						<div class="col-sm-9">
							<div class="input-group">
								<input type="text" class="form-control datepicker" name="punishDate" id="punishDate" nullable="false" value="${(dyStuPunishment.punishDate?string("yyyy-MM-dd"))!}" readOnly>
								<span class="input-group-addon">
									<i class="fa fa-calendar"></i>
								</span>
							</div>
						</div>
					</div>
				</div>				
			</div>
</form>
<script>
$(function(){
	$('.datepicker').datepicker({
		language: 'zh-CN',
		format: 'yyyy-mm-dd',
		autoclose: true
	}).next().on('click', function(){
		$(this).prev().focus();
	});
	var typeStr = $('#punishTypeId').val();
	if(null != typeStr){
	   var type = typeStr.split("-")[2];
       var punishType = typeStr.split("-")[1];
       if(type == '1' && punishType == '1'){
          $('#score').removeAttr("readonly");
          $('#score').attr('nullable','false');
          $('#score').attr('name','score');
       }
	}
});

function changeScore(){
    var typeStr = $('#punishTypeId').val();
    if(null != typeStr){
       var punishType = typeStr.split("-")[1];
       var type = typeStr.split("-")[2];
       var punishTypeId = typeStr.split("-")[0];
       if(type == '1' && punishType == '1'){
           $('#score').removeAttr("readonly");
           $('#score').attr('nullable','false');
           $('#score').attr('name','score');
       }else{
           $('#score').attr('readonly','');
           $('#score').attr('nullable','ture');
           $('#score').attr('name','');
       }
    }
    $.ajax({
		url:"${request.contextPath}/stuwork/studentManage/getScore",
		data:{punishTypeId:punishTypeId},
		dataType: "json",
		success: function(data){
		    $("#score").val(data.msg); 
		}
	});
}

function getStuCode(){
    var studentId = $('#studentId').val();
    $.ajax({
		url:"${request.contextPath}/stuwork/studentManage/getStuCode",
		data:{studentId:studentId},
		dataType: "json",
		success: function(data){
		    $("#studentCode").val(data.msg); 
		}
	});
}

var isSubmit=false;
function savePunish(){
    if(isSubmit){
    	return;
    }
    isSubmit=true;
    var check = checkValue('#myDiv');
    if(!check){
        $(this).removeClass("disabled");
        isSubmit=false;
        return;
    }
	var options = {
		url : "${request.contextPath}/stuwork/studentManage/punishScoreInfoSave",
		dataType : 'json',
		success : function(data){
	 		var jsonO = data;
	 		isSubmit=false;
		 	if(!jsonO.success){
		 		layerTipMsg(jsonO.success,"保存失败",jsonO.msg);
		 		//$("#arrange-commit").removeClass("disabled");
		 		return;
		 	}else{
		 		layer.closeAll();
				layerTipMsg(jsonO.success,"保存成功",jsonO.msg);
				searchList();
    		}
		},
		clearForm : false,
		resetForm : false,
		type : 'post',
		error:function(XMLHttpRequest, textStatus, errorThrown){}//请求出错 
	};
	$("#subForm").ajaxSubmit(options);
}
</script>
