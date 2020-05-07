<form id="subForm2" method="post">
<div class="layer layer-setting" id="myDiv">
    <input type="hidden" name="examId" value="${examId!}">
    <input type="hidden" name="subjectId" value="${subjectId!}">
    <input type="hidden" name="infoId" value="${gradeId!}">
    <input type="hidden" name="unitId" value="${unitId!}">
    <input type="hidden" name="id" value="${id!}">
	<div class="layer-content">
		<div class="filter">
			<div class="filter-item">
				<span class="filter-name">A值：</span>
				<div class="filter-content">
					<input type="text" class="form-control" id="val" name="val" value="${val!}" vtype="number" min="0" max="999" maxLength="5" decimalLength="1" nullable="false">
				</div>
			</div>
		</div>
		<div class="explain">
			<h4>说明</h4>
			<p>折算公式：Y = (X - B) / (100 - B) * 20 + A</p>
			<p>Y：折算后的分数</p>
			<p>X：折算前（实际）的分数</p>
			<p>B：折算前（实际）的平均分</p>
			<p>A：按每门自定义的分数</p>
			</div>
		</div>
	</div>
</div><!-- E 拆分设置弹框 -->
</form>
<script>
function saveZfs(){	
	var check = checkValue('#myDiv');
    if(!check){
        $(this).removeClass("disabled");
        isSubmit=false;
        return;
    }
	var options = {
		url:"${request.contextPath}/comprehensive/subjects/score/compreSetupSave",
		dataType : 'json',
		success : function(data){
	 		var jsonO = data;
		 	if(!jsonO.success){
		 		layerTipMsg(jsonO.success,"保存失败",jsonO.msg);
		 		//$("#arrange-commit").removeClass("disabled");
		 		return;
		 	}else{
		 		layer.closeAll();
				//layerTipMsg(jsonO.success,"保存成功",jsonO.msg);
				layer.msg("保存成功", {
						offset: 't',
						time: 2000
					});
				reLoad();
    		}
		},
		clearForm : false,
		resetForm : false,
		type : 'post',
		error:function(XMLHttpRequest, textStatus, errorThrown){}//请求出错 
	};
	$("#subForm2").ajaxSubmit(options);
}
</script>