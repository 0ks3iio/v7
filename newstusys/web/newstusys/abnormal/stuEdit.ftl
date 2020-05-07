<div class="form-horizontal" role="form">
<form name="applyForm" id="applyForm">
	<input type="hidden" name="schid" value="${flow.schid!}">
	<input type="hidden" name="stuid" value="${exStu.id!}">
	<input type="hidden" name="acadyear" value="${flow.acadyear!}">
	<input type="hidden" name="semester" value="${flow.semester!}">
	<input type="hidden" name="flowreason" id="flowreason" value="">
	<div class="form-group mt20">
		<label class="col-sm-2 control-title">查询结果</label>
	</div>
	<div class="form-group">
		<label class="col-sm-2 control-label no-padding-right">学生姓名：</label>
		<div class="col-sm-3 mt7">${exStu.studentName!}<span class="badge badge-green ml10">已转出</span></div>
		<label class="col-sm-2 control-label no-padding-right">转出原因：</label>
		<div class="col-sm-3 mt7">
			<#if exFlowType?default('') != ''>
			${(mcodeSetting.getMcode("DM-YDLB", exFlowType!))?if_exists}(${exFlowDate?string('yyyy-MM-dd')})
			</#if>
		</div>
	</div>
	<div class="form-group">
		<label class="col-sm-2 control-label no-padding-right">性别：</label>
		<div class="col-sm-3 mt7">${(mcodeSetting.getMcode("DM-XB", exStu.sex?default(0)?string))?if_exists}</div>
		<label class="col-sm-2 control-label no-padding-right">身份证号：</label>
		<div class="col-sm-3 mt7">${exStu.identityCard!}</div>
	</div>
	<div class="form-group">
		<label class="col-sm-2 control-label no-padding-right">原学校：</label>
		<div class="col-sm-3 mt7">${exStu.schoolName!}</div>
		<label class="col-sm-2 control-label no-padding-right">原班级：</label>
		<div class="col-sm-3 mt7">${exStu.className!}</div>
	</div>
	<div class="form-group">
		<label class="col-sm-2 control-label no-padding-right">转入学校：</label>
		<div class="col-sm-3 mt7">${flow.toSchName!}</div>
		<label class="col-sm-2 control-label no-padding-right">全国学籍号：</label>
		<div class="col-sm-3 mt7">${exStu.unitiveCode!}</div>
	</div>
	<div class="form-group">
		<label class="col-sm-2 control-label no-padding-right"><span class="color-red">*</span>转入班级：</label>
		<div class="col-sm-3 mt7">
			<select vtype="selectOne" name="currentclassid" id="clsId" nullable="false" class="form-control">
				<#if clsList?exists && clsList?size gt 0>
					<#list clsList as cls>
					<option value="${cls.id!}">${cls.classNameDynamic!}</option>
					</#list>
				</#if>
			</select>
		</div>
		<label class="col-sm-2 control-label no-padding-right">班内编号：</label>
		<div class="col-sm-3"><input type="text" name="stuInnerCode" id="stuInnerCode" maxLength="10" vtype="int" value="${exStu.classInnerCode!}" class="form-control" placeholder=""></div>
	</div>
	<div class="form-group">
		<label class="col-sm-2 control-label no-padding-right"><span class="color-red">*</span>学籍辅号/学号：</label>
		<div class="col-sm-3"><input type="text" name="stuStuCode" id="stuStuCode" nullable="false" maxLength="20" class="form-control" value="${exStu.studentCode!}" placeholder=""></div>
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
				<input type="radio" class="wp flowtype-chk2" name="flowtype" value="${ft.thisId!}">
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
			<a class="btn btn-blue" onclick="saveStu();" href="javascript:;">办理转入</a>
		</div>
	</div>
</form>
</div>
<script>
$(function(){
	initChosenOne("#applyForm");
	
	$('.datepicker').datepicker({
		language: 'zh-CN',
		format: 'yyyy-mm-dd',
		autoclose: true
	}).next().on('click', function(){
		$(this).prev().focus();
	});
	
});

var isSubmit=false;
function saveStu(){
	if(isSubmit){
		return;
	}
	isSubmit=true;
	if(!checkValue('#applyForm')){
		isSubmit=false;
		return;
	}
	
	var ft = '';
	$('.flowtype-chk2').each(function(){
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
	
	showConfirmMsg('确定要转入学生吗？','确定',function(index){
   		var options = {
   			url : "${request.contextPath}/newstusys/abnormal/saveLeaveIn",
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
   		$('#applyForm').ajaxSubmit(options);
   	},function(index){
   		isSubmit=false;
   		layer.close(index);
   	}); 
}
</script>