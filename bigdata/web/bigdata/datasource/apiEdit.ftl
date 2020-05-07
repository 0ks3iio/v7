<form id="submitForm">
<input type="hidden" name="id" value="${api.id!}">
	<div class="form-horizontal">
		<div class="form-group">
			<label class="col-sm-2 control-label no-padding-right"><font style="color:red;">*</font>API名称：</label>
			<div class="col-sm-6">
			<input type="text" name="name" id="name" class="form-control" nullable="false" maxLength="100" value="${api.name!}">
			</div>
		</div>
		
		<div class="form-group">
			<label class="col-sm-2 control-label no-padding-right"><font style="color:red;">*</font>API链接：</label>
			<div class="col-sm-6">
				<input type="text" name="url" id="url" class="form-control" nullable="false" maxLength="256" value="${api.url!}">
			</div>
		</div>

        <div class="form-group">
            <label class="col-sm-2 control-label no-padding-right"><font style="color:red;">*</font>API参数描述：</label>
            <div class="col-sm-6">
                <textarea name="paramDescription" id="paramDescription" type="text/plain" nullable="false" maxLength="200" style="width:100%;height:160px;">${api.paramDescription!}</textarea>
            </div>
        </div>
		
		<div class="form-group">
			<label class="col-sm-2 control-label no-padding-right"><font style="color:red;">*</font>API接口说明：</label>
			<div class="col-sm-6">
				<textarea name="remark" id="remark" type="text/plain" nullable="false" maxLength="200" style="width:100%;height:160px;">${api.remark!}</textarea>
			</div>
		</div>
		
		<div class="form-group">
            <div class="col-sm-6 col-sm-offset-2" >
                <button type="button" class="btn btn-long btn-blue js-added" id="saveBtn">&nbsp;保存&nbsp;</button>
            </div>
            </div>
        </div>
	</div>
</form>
<script>
var isSubmit=false;

$("#saveBtn").on("click",function(){
	if(isSubmit){
		return;
	}
	isSubmit = true;
	var check = checkValue('#submitForm');
	if(!check){
	 	isSubmit=false;
	 	return;
	}
	var options = {
			url : "${request.contextPath}/bigdata/datasource/api/save",
			dataType : 'json',
			success : function(data){
		 		if(!data.success){
		 			showLayerTips4Confirm('error',data.msg);
		 			isSubmit = false;
		 		}else{
		 			showLayerTips('success',data.msg,'t');
				  	showList('2');
    			}
			},
			clearForm : false,
			resetForm : false,
			type : 'post',
			error:function(XMLHttpRequest, textStatus, errorThrown){}//请求出错 
		};
		$("#submitForm").ajaxSubmit(options);
});
</script>