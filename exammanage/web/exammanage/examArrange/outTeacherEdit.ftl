<form id="teachAddForm">
<div class="layer-body">
	<div class="filter clearfix"> 
		<div class="filter-item">
			<label for="" class="filter-name">老师姓名：</label>
			<div class="filter-content">
				<input type="text" class="form-control" name="outTeacherName" id="outTeacherName" value=""/>
			</div>
		</div>
		<div class="filter-item">
			<label for="" class="filter-name">电话号码：</label>
			<div class="filter-content">
				<input type="text" class="form-control" name="outTeacherTel" id="outTeacherTel"  maxlength="11" value=""/>
			</div>
		</div>
	</div>  
</div>
</form>
<div class="layer-footer">
	<a href="javascript:" class="btn btn-lightblue" id="arrange-commit">确定</a>
	<a href="javascript:" class="btn btn-grey" id="arrange-close">取消</a>
</div>
<script>
var isSubmit=false;
$("#arrange-commit").on("click",function(){
	if(isSubmit){
		return;
	}
	var outTeacherName = $('#outTeacherName').val();
	var outTeacherTel = $('#outTeacherTel').val();
	if (outTeacherName == "") {
		layerTipMsg(false,"提示","老师姓名不能为空！");
		isSubmit=false;
		return;
	}
	if (outTeacherTel == "") {
		layerTipMsg(false,"提示","电话号码不能为空！");
		isSubmit=false;
		return;
	}
	if(outTeacherName.replace(/[^\x00-\xff]/g, "01").length > 20){
		layerTipMsg(false,"提示","老师姓名不能超过20个字符！");
		isSubmit=false;
		return;
	}
	var myreg = /^1\d{10}$/;
	if(!myreg.test(outTeacherTel)) { 
		layerTipMsg(false,"提示","请输入有效的电话号码！");
		isSubmit=false;
		return;
    } 
	isSubmit=true;
	$("#arrange-commit").addClass("disabled");
	var options = {
		url : '${request.contextPath}/exammanage/examArrange/outTeacherSave?examId=${examId!}',
		dataType : 'json',
		success : function(data){
	 		if(data.success){
	 			layer.closeAll();
				layerTipMsg(data.success,"成功",data.msg);
				var url =  '${request.contextPath}/exammanage/examArrange/outTeacherIndex/page?examId=${examId!}';
				$("#showTabDiv").load(url);
	 		}
	 		else{
	 			layerTipMsg(data.success,"失败",data.msg);
	 			$("#arrange-commit").removeClass("disabled");
	 			isSubmit=false;
			}
		},
		clearForm : false,
		resetForm : false,
		type : 'post',
		error:function(XMLHttpRequest, textStatus, errorThrown){}//请求出错 
	};
	$("#teachAddForm").ajaxSubmit(options);
});

// 取消按钮操作功能
$("#arrange-close").on("click", function(){
    doLayerOk("#arrange-commit", {
    redirect:function(){},
    window:function(){layer.closeAll()}
    });     
 });
 </script>