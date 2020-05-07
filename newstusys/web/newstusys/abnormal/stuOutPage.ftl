<div class="layer-addTerm layer-change" style="display:block;" id="myDiv">
<form id="subForm" name="subForm">
	<input type="hidden" name="stuid" value="${stu.id!}">
	<input type="hidden" name="schid" value="${schoolId!}">
	<input type="hidden" name="flowreason" id="flowreason" value="">
	<div class="layer-body">
		<#--div class="explain explain-no-icon">
			提示：学生转出后，学生及其家长、账号信息都将被注销
		</div-->
		<div class="form-horizontal" role="form">
			<div class="form-group">
				<label class="col-sm-2 control-label no-padding-right">学生姓名：</label>
				<div class="col-sm-10 mt7">${stu.studentName!}</div>
			</div>
			<div class="form-group">
				<label class="col-sm-2 control-label no-padding-right">身份证号：</label>
				<div class="col-sm-10 mt7">${stu.identityCard!}</div>
			</div>
			<div class="form-group">
				<label class="col-sm-2 control-label no-padding-right">当前班级：</label>
				<div class="col-sm-10 mt7">${stu.className!}</div>
			</div>
			<div class="form-group">
				<label class="col-sm-2 control-label no-padding-right"><span class="color-red">*</span>转出时间：</label>
				<div class="col-sm-5">
					<div class="input-group">
						<input class="form-control datetimepicker datepicker" type="text" name="flowdate" id="flowdate" value="${(nowDate?string('yyyy-MM-dd'))!}">
						<span class="input-group-addon">
							<i class="fa fa-calendar"></i>
						</span>
					</div>
				</div>
			</div>
			<div class="form-group">
				<label class="col-sm-2 control-label no-padding-right"><span class="color-red">*</span>转出原因：</label>
				<div class="col-sm-10">
					<#list flowTypes as ft>
					<label class="inline">
						<input type="radio" class="wp flowtype-chk" name="flowtype" value="${ft.thisId!}">
						<span class="lbl" id="text-${ft.thisId!}"> ${ft.mcodeContent!}</span>
					</label>
					</#list>
				</div>
			</div>
			<div class="form-group">
				<label class="col-sm-2 control-label no-padding-right"><span class="color-red">*</span>备注：</label>
				<div class="col-sm-10">
					<textarea name="remark" id="remark" maxLength="100" nullable="false" cols="30" rows="3" class="form-control"></textarea>
				</div>
			</div>
		</div>
	</div>
</form>
<div class="layer-footer">
   <a class="btn btn-lightblue" id="arrange-commit">确定</a>
   <a class="btn btn-grey" id="arrange-close">取消</a>
</div>
</div>
<script>
$(function(){
	$('.datepicker').datepicker({
		language: 'zh-CN',
		format: 'yyyy-mm-dd',
		autoclose: true
	}).next().on('click', function(){
		$(this).prev().focus();
	});
});

// 取消按钮操作功能
$("#arrange-close").on("click", function(){
    doLayerOk("#arrange-commit", {
    redirect:function(){},
    window:function(){layer.closeAll()}
    });     
});

var isSubmit=false;
$("#arrange-commit").on("click", function(){
   	if(isSubmit){
		return;
	}
	isSubmit=true;
	if(!checkValue('#subForm')){
		isSubmit=false;
		return;
	}
	
	var ft = '';
	$('.flowtype-chk').each(function(){
		if($(this).prop('checked')==true || $(this).prop('checked')=='checked'){
			ft = $(this).val();
			return;
		}
	});
	if(ft == ''){
		showMsgError('请选择转出原因！');
		isSubmit=false;
		return;
	}
	$('#flowreason').val($.trim($('#text-'+ft).text()));
   	
   	showConfirmMsg('确定要转出学生吗？','确定',function(index){
   		var options = {
   			url : "${request.contextPath}/newstusys/abnormal/saveLeave",
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
                    $("#arrange-close").click();
                    toStuList();
                }
                isSubmit=false;
            },
            clearForm : false,
            resetForm : false,
            type : 'post',
            error:function(XMLHttpRequest, textStatus, errorThrown){}
   		};
   		$('#subForm').ajaxSubmit(options);
   	},function(index){
   		isSubmit=false;
   		layer.close(index);
   	});  
});

</script>