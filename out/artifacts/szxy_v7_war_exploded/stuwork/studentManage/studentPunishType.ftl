<form id="subForm" method="post">
<div class="main-content" id="myDiv">				
				<div class="main-content-inner">
					<div class="page-content">
						<div class="box box-default">
							<div class="box-body">
								<div class="table-container">
									<div class="table-container-header text-right">
										<a class="btn btn-blue" onclick="save();">保存</a>
									</div>
									<div class="table-container-body">
										<table class="table table-striped table-hover">
											<thead>
												<tr>
													<th>处分类别</th>
													<th>拆分设置</th>
												</tr>
											</thead>
											<tbody>
												<tr>
													<td>口头警告</td>
													<td>
														扣 <input type="text" id="score0" name="dyBusinessOptionList[0].score" value="${score0!}" vtype="number" nullable="false" min="0" max="999999" maxLength="8" decimalLength="2" class="form-control number inline-block"> 分
														<input type="hidden" name="dyBusinessOptionList[0].optionName" value="口头警告">
														<input type="hidden" name="dyBusinessOptionList[1].optionName" value="书面告诫">
														<input type="hidden" name="dyBusinessOptionList[2].optionName" value="警告处分">
														<input type="hidden" name="dyBusinessOptionList[3].optionName" value="严重警告处分">
														<input type="hidden" name="dyBusinessOptionList[4].optionName" value="记过处分">
														
														<input type="hidden" name="dyBusinessOptionList[0].orderId" value="1">
														<input type="hidden" name="dyBusinessOptionList[1].orderId" value="2">
														<input type="hidden" name="dyBusinessOptionList[2].orderId" value="3">
														<input type="hidden" name="dyBusinessOptionList[3].orderId" value="4">
														<input type="hidden" name="dyBusinessOptionList[4].orderId" value="5">
													</td>
												</tr>
												<tr>
													<td>书面告诫</td>
													<td>
														扣 <input type="text" id="score1" name="dyBusinessOptionList[1].score" value="${score1!}" vtype="number" nullable="false" min="0" max="999999" maxLength="8" decimalLength="2" class="form-control number inline-block"> 分
													</td>
												</tr>
												<tr>
													<td>警告处分</td>
													<td> 
														扣 <input type="text" id="score2" name="dyBusinessOptionList[2].score" value="${score2!}" vtype="number" nullable="false" min="0" max="999999" maxLength="8" decimalLength="2" class="form-control number inline-block"> 分
													</td>
												</tr>
												<tr>
													<td>严重警告处分</td>
													<td>
														扣 <input type="text" id="score3" name="dyBusinessOptionList[3].score" value="${score3!}" vtype="number" nullable="false" min="0" max="999999" maxLength="8" decimalLength="2" class="form-control number inline-block"> 分
													</td>
												</tr>
												<tr>
													<td>记过处分</td>
													<td>
														扣 <input type="text" id="score4" name="dyBusinessOptionList[4].score" value="${score4!}" vtype="number" nullable="false" min="0" max="999999" maxLength="8" decimalLength="2" class="form-control number inline-block"> 分
													</td>
												</tr>
											</tbody>
										</table>
									</div>
								</div>
								
							</div>
						</div>
					</div><!-- /.page-content -->
				</div>
			</div><!-- /.main-content -->
</form>
<script>
var isSubmit=false;
function save(){
   var check = checkValue('#myDiv');
    if(!check){
        $(this).removeClass("disabled");
        isSubmit=false;
        return;
    }
	var options = {
		url : "${request.contextPath}/stuwork/studentManage/punishTypeSave",
		dataType : 'json',
		success : function(data){
	 		var jsonO = data;
		 	if(!jsonO.success){
		 		layerTipMsg(jsonO.success,"保存失败",jsonO.msg);
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