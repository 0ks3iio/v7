<div class="form-horizontal" role="form">
	<div class="form-group mt20">
		<label class="col-sm-2 control-title">查询结果</label>
	</div>
	<div class="form-group">
		<label class="col-sm-2 control-label no-padding-right"></label>
		<div class="col-sm-8">抱歉，未找到该学生，请认真核查姓名和身份证号。<a class="color-blue" href="javascript:none;" onclick="showNew();">新增学生</a></div>
	</div>
</div>
<form id="stuApplyForm" name="stuApplyForm">
<input type="hidden" name="schoolId" value="${schoolId!}">
<input type="hidden" name="isLeaveSchool" value="0">
<input type="hidden" name="eventSource" value="0">
<input type="hidden"  id="hasAddPic" name="hasAddPic" value="false">
<input type="hidden" name="forAbnormal" value="1">
<input type="hidden" name="acadyear" value="${flow.acadyear!}">
<input type="hidden" name="semester" value="${flow.semester!}">
<input type="hidden" name="flowreason" id="flowreason" value="">
<div class="form-horizontal" role="form" id="applyEditDiv" style="display:none;">
	<div class="form-group mt20">
		<label class="col-sm-2 control-title no-padding-right"><span class="form-title">新增学生</span></label>
	</div>
	<div class="form-group">
		<label class="col-sm-2 control-label no-padding-right"><span class="color-red">*</span>学籍辅号：</label>
		<div class="col-sm-3">
			<input type="text" name="studentCode" id="studentCode" maxLength="20" nullable="false" class="form-control" placeholder="">
		</div>
		<label class="col-sm-2 control-label no-padding-right"><span class="color-red">*</span>学生姓名：</label>
		<div class="col-sm-3">
			<input type="text" name="studentName" id="studentName" class="form-control" value="${stuName!}" maxLength="100" nullable="false" placeholder="">
		</div>
	</div>
	<div class="form-group">
		<label class="col-sm-2 control-label no-padding-right">英文姓名：</label>
		<div class="col-sm-3">
			<input type="text" name="englishName" id="englishName" maxLength="30" class="form-control" placeholder="">
		</div>
		<label class="col-sm-2 control-label no-padding-right">曾用名：</label>
		<div class="col-sm-3">
			<input type="text" name="oldName" id="oldName" maxLength="100" class="form-control" placeholder="">
		</div>
	</div>
	<div class="form-group">
		<label class="col-sm-2 control-label no-padding-right">全国学籍号：</label>
		<div class="col-sm-3">
			<input type="text" name="unitiveCode" id="unitiveCode" maxLength="30" class="form-control" placeholder="">
		</div>
		<label class="col-sm-2 control-label no-padding-right">班内编号：</label>
		<div class="col-sm-3">
			<input type="text" name="classInnerCode" id="classInnerCode" maxLength="10" vtype="int" class="form-control" placeholder="">
		</div>
	</div>
	<div class="form-group">
		<label class="col-sm-2 control-label no-padding-right"><span class="color-red">*</span>性别：</label>
		<div class="col-sm-3">
			<select name="sex" id="sex" nullable="false" class="form-control">
				${mcodeSetting.getMcodeSelect("DM-XB", "", "")}
			</select>
		</div>
		<label class="col-sm-2 control-label no-padding-right">出生日期：</label>
		<div class="col-sm-3">
			<div class="input-group">
				<input class="form-control datetimepicker datepicker" nullable="true" type="text" name="birthday" id="birthday" value="">
				<span class="input-group-addon">
					<i class="fa fa-calendar"></i>
				</span>
			</div>
		</div>
	</div>
	<div class="form-group">
		<label class="col-sm-2 control-label no-padding-right"><span class="color-red">*</span>班级：</label>
		<div class="col-sm-3">
			<select vtype="selectOne" name="classId" id="classId" nullable="false" class="form-control">
				<#if clsList?exists && clsList?size gt 0>
					<#list clsList as cls>
					<option value="${cls.id!}">${cls.classNameDynamic!}</option>
					</#list>
				</#if>
			</select>
		</div>
		<label class="col-sm-2 control-label no-padding-right">民族：</label>
		<div class="col-sm-3">
			<select name="nation"  id="nation" class="form-control" >
            ${mcodeSetting.getMcodeSelect("DM-MZ", "", "")}
            </select>
		</div>
	</div>
	<div class="form-group">
		<label class="col-sm-2 control-label no-padding-right"><span class="color-red">*</span>身份证件类型：</label>
		<div class="col-sm-3">
			<select name="identitycardType" nullable="false" id="identitycardType" class="form-control">
            ${mcodeSetting.getMcodeSelect("DM-SFZJLX", "01", "")}
            </select>
		</div>
		<label class="col-sm-2 control-label no-padding-right"><span class="color-red">*</span>身份证件号：</label>
		<div class="col-sm-3">
			<input type="text" name="identityCard" id="identityCard" nullable="false" maxlength="30" value="${stuIdentityCard!}" class="form-control">
		</div>
	</div>
	<div class="form-group">
		<label class="col-sm-2 control-label no-padding-right">国籍地区：</label>
		<div class="col-sm-3">
			<select name="country" id="country" class="form-control" notnull="false" >
            ${mcodeSetting.getMcodeSelect("DM-COUNTRY", "", "")}
            </select>
		</div>
		<label class="col-sm-2 control-label no-padding-right">监护人：</label>
		<div class="col-sm-3">
			<input type="hidden" name="familyTempList[0].id" value="">
	        <input type="hidden" name="familyTempList[0].schoolId" value="${schoolId!}">
	        <input type="hidden" name="familyTempList[0].studentId" value="">
	        <input type="hidden" name="familyTempList[0].isGuardian" value="1">
	        <input type="hidden" name="familyTempList[0].eventSource" value="0">
	        <input type="hidden" name="familyTempList[0].isDeleted" value="0">
	        <input type="hidden" name="familyTempList[0].openUserStatus" value="1">
			<input type="text" msgName="监护人" name="familyTempList[0].realName" id="realName0" maxlength="60" value="" class="form-control">
		</div>
	</div>
	<div class="form-group">
		<label class="col-sm-2 control-label no-padding-right">监护人关系：</label>
		<div class="col-sm-3">
			<select name="familyTempList[0].relation" id="relation0" class="form-control">
            ${mcodeSetting.getMcodeSelect("DM-GX", "", "1")}
            </select>
		</div>
		<label class="col-sm-2 control-label no-padding-right">监护人手机：</label>
		<div class="col-sm-3">
			<input type="text" maxlength="20" id="mobilePhone0" name="familyTempList[0].mobilePhone" value=""  class="form-control">
		</div>
	</div>
	<div class="form-group">
		<label class="col-sm-2 control-label no-padding-right">父亲姓名：</label>
		<div class="col-sm-3">
			<input type="text" name="linkAddress" name="linkAddress" maxLength="60" class="form-control" placeholder="">
		</div>
		<label class="col-sm-2 control-label no-padding-right">母亲姓名：</label>
		<div class="col-sm-3">
			<input type="text" name="nowaddress" name="nowaddress" maxLength="60" class="form-control" placeholder="">
		</div>
	</div>
	<div class="form-group">
		<label class="col-sm-2 control-label no-padding-right">户口省县：</label>
		<div class="col-sm-3">
			<select vtype="selectOne" name="registerPlace" id="registerPlace" class="form-control">
            <#if regionList?exists && (regionList?size>0)>
                <option value="">---请选择---</option>
                <#list regionList as region>
                    <option value="${region.fullCode!}">  ${region.fullName!}</option>
                </#list>
            <#else>
                <option value="">---请选择---</option>
            </#if>
            </select>
		</div>
		<label class="col-sm-2 control-label no-padding-right">户籍乡镇：</label>
		<div class="col-sm-3">
			<input type="text" name="registerAddress" name="registerAddress" maxLength="60" class="form-control" placeholder="">
		</div>
	</div>
	<div class="form-group">
		<label class="col-sm-2 control-label no-padding-right">现居住地址：</label>
		<div class="col-sm-3">
			<input type="text" name="homeAddress" name="homeAddress" maxLength="60" class="form-control" placeholder="">
		</div>
		<label class="col-sm-2 control-label no-padding-right">学生类别：</label>
		<div class="col-sm-3">
			<select name="studentType" id="studentType" class="form-control" notnull="false" >
            ${mcodeSetting.getMcodeSelect("DM-XSLB", "", "")}
            </select>
		</div>
	</div>
	<div class="form-group">
		<label class="col-sm-2 control-label no-padding-right">港澳台侨：</label>
		<div class="col-sm-3">
			<select name="compatriots"  id="compatriots" class="form-control" >
            ${mcodeSetting.getMcodeSelect("DM-GATQ", "", "")}
            </select>
		</div>
		<label class="col-sm-2 control-label no-padding-right">政治面貌：</label>
		<div class="col-sm-3">
			<select name="background"  id="background" class="form-control" >
            ${mcodeSetting.getMcodeSelect("DM-XSZZMM", "", "")}
            </select>
		</div>
	</div>
	<div class="form-group">
		<label class="col-sm-2 control-label no-padding-right">是否留守儿童：</label>
		<div class="col-sm-3">
			<label class="inline">
				<input type="radio" class="wp" name="stayin" value="1">
				<span class="lbl"> 是</span>
			</label>
			<label class="inline">
				<input type="radio" class="wp" name="stayin" value="0" checked="checked">
				<span class="lbl"> 否</span>
			</label>
		</div>
		<label class="col-sm-2 control-label no-padding-right">是否随迁子女：</label>
		<div class="col-sm-3">
			<label class="inline">
				<input type="radio" class="wp" name="isMigration" value="1">
				<span class="lbl"> 是</span>
			</label>
			<label class="inline">
				<input type="radio" class="wp" name="isMigration" value="0" checked="checked">
				<span class="lbl"> 否</span>
			</label>
		</div>
	</div>
	<div class="form-group">
		<label class="col-sm-2 control-label no-padding-right">是否随班就读：</label>
		<div class="col-sm-3">
			<select name="regularClass"  id="regularClass" class="form-control" >
            ${mcodeSetting.getMcodeSelect("DM-SBJD", "", "")}
            </select>
		</div>
		<label class="col-sm-2 control-label no-padding-right">是否住宿生：</label>
		<div class="col-sm-3">
			<label class="inline">
				<input type="radio" class="wp" name="isBoarding" value="1">
				<span class="lbl"> 是</span>
			</label>
			<label class="inline">
				<input type="radio" class="wp" name="isBoarding" value="0" checked="checked">
				<span class="lbl"> 否</span>
			</label>
		</div>
	</div>
	<div class="form-group">
		<label class="col-sm-2 control-label no-padding-right">学生状态：</label>
		<div class="col-sm-3">
			<select name="nowState" id="nowState" class="form-control" notnull="false" >
            ${mcodeSetting.getMcodeSelect("DM-ZJYDLB", "40", "")}
            </select>
		</div>
	</div>
	<div class="form-group">
		<label class="col-sm-2 control-label no-padding-right"><span class="color-red">*</span>转入时间：</label>
		<div class="col-sm-3">
			<div class="input-group">
				<input class="form-control datetimepicker datepicker" nullable="false" type="text" name="flowdate" id="flowdate" value="${(nowDate?string('yyyy-MM-dd'))!}">
				<span class="input-group-addon">
					<i class="fa fa-calendar"></i>
				</span>
			</div>
		</div>
	</div>
	<div class="form-group">
		<label class="col-sm-2 control-label no-padding-right"><span class="color-red">*</span>转入原因：</label>
		<div class="col-sm-8">
			<#list flowTypes as ft>
			<label class="inline">
				<input type="radio" class="wp flowtype-chk3" name="flowtype" value="${ft.thisId!}">
				<span class="lbl" id="text-${ft.thisId!}"> ${ft.mcodeContent!}</span>
			</label>
			</#list>
		</div>
	</div>
	<div class="form-group">
		<label class="col-sm-2 control-label no-padding-right"><span class="color-red">*</span>备注：</label>
		<div class="col-sm-8">
			<textarea name="remark" id="remark" maxLength="100" nullable="false" cols="30" rows="5" class="form-control"></textarea>
		</div>
	</div>
	<div class="form-group">
		<label class="col-sm-2 control-label no-padding-right"></label>
		<div class="col-sm-8">
			<a href="javascript:;" class="btn btn-blue" onclick="saveApply();">新增并转入</a>
		</div>
	</div>
</div>
</form>
<script type="text/javascript" src="${request.contextPath}/newstusys/sch/student/studentValidate.js"/>
<script>
$(function(){
	initChosenOne("#stuApplyForm");
	
	$('.datepicker').datepicker({
		language: 'zh-CN',
		format: 'yyyy-mm-dd',
		autoclose: true
	}).next().on('click', function(){
		$(this).prev().focus();
	});
	
});

function showNew(){
	$('#applyEditDiv').show();
}

var isSubmit=false;
function saveApply(){
	if(isSubmit){
		return;
	}
	isSubmit=true;
	if(!checkValue('#stuApplyForm')){
		isSubmit=false;
		return;
	}
	//电话号码校验
    if(document.getElementById("mobilePhone0").value != ''){
        if (!checkMobilePhone(document.getElementById("mobilePhone0"))){
            isSubmit=false;
            return false;
        }
    }
	
	var ft = '';
	$('.flowtype-chk3').each(function(){
		if($(this).prop('checked')==true || $(this).prop('checked')=='checked'){
			ft = $(this).val();
			return;
		}
	});
	if(ft == ''){
		showMsgError('请选择转入原因！');
		isSubmit=false;
		return;
	}
	$('#flowreason').val($.trim($('#text-'+ft).text()));
	showConfirmMsg('确定要新增并转入学生吗？','确定',function(index){
   		var options = {
   			url : "${request.contextPath}/newstusys/sch/student/saveStudent",
            dataType : 'json',
            success : function(data){
                var jsonO = data;
                if(!jsonO.success){
                    layer.close(index);
                    layerTipMsg(jsonO.success,"",jsonO.msg);
                    isSubmit=false;
                    return;
                }else{
                    layer.msg("保存成功");
                    backToSch();
                }
                isSubmit=false;
            },
            clearForm : false,
            resetForm : false,
            type : 'post',
            error:function(XMLHttpRequest, textStatus, errorThrown){}
   		};
   		$('#stuApplyForm').ajaxSubmit(options);
   	},function(index){
   		isSubmit=false;
   		layer.close(index);
   	}); 
}
</script>