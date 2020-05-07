<form id="submitForm">
<input type="hidden" name="id" value="${database.id!}">
	<div class="form-horizontal">
        <div class="form-group">
            <label class="col-sm-2 control-label no-padding-right">类型：</label>
            <div class="col-sm-6">
				<div class="mt-7">${databaseName?replace(".png", "")}</div>
            </div>
        </div>
		<div class="form-group">
			<label class="col-sm-2 control-label no-padding-right"><font style="color:red;">*</font>数据源名称：</label>
			<div class="col-sm-6">
			<input type="text" name="name" id="name" class="form-control" nullable="false" maxLength="50" value="${database.name!}">
			</div>
		</div>
		
		<div class="form-group" hidden="hidden">
			<label class="col-sm-2 control-label no-padding-right"><font style="color:red;">*</font>类型：</label>
			<div class="col-sm-6">
				<select name="type" id="type" class="form-control" >
					${mcodeSetting.getMcodeSelect("DM-SJKLX","${database.type!}","0")}
				</select>
			</div>
		</div>
		
		<div class="form-group">
			<label class="col-sm-2 control-label no-padding-right"><font style="color:red;">*</font>域名：</label>
			<div class="col-sm-6">
				<input type="text" name="domain" id="domain" class="form-control" nullable="false" maxLength="50" value="${database.domain!}">
			</div>
		</div>
		
		<div class="form-group">
			<label class="col-sm-2 control-label no-padding-right"><font style="color:red;">*</font>端口：</label>
			<div class="col-sm-6">
				<input type="text" name="port" id="port" class="form-control" nullable="false" maxLength="6" vtype="digits" value="${database.port!}">
			</div>
		</div>
		
		<div class="form-group">
			<label class="col-sm-2 control-label no-padding-right"><font style="color:red;">*</font>数据库名称：</label>
			<div class="col-sm-6">
				<input type="text" name="dbName" id="dbName" class="form-control" nullable="false" maxLength="50" value="${database.dbName!}">
			</div>
		</div>
		
		<div class="form-group">
			<label class="col-sm-2 control-label no-padding-right"><font style="color:red;">*</font>用户名：</label>
			<div class="col-sm-6">
				<input type="text" name="username" id="username" class="form-control" nullable="false" maxLength="50" value="${database.username!}">
			</div>
		</div>
		
		<div class="form-group">
			<label class="col-sm-2 control-label no-padding-right">密码：</label>
			<div class="col-sm-6">
				<input type="password" name="password" id="password" class="form-control" nullable="true" maxLength="50" value="${database.password!}">
			</div>
		</div>
		<div class="form-group">
			<label class="col-sm-2 control-label no-padding-right">编码：</label>
			<div class="col-sm-6">
				<input type="text" name="characterEncoding" id="characterEncoding" class="form-control" nullable="true" maxLength="20" value="${database.characterEncoding!}">
			</div>
		</div>
		<div class="form-group">
			<label class="col-sm-2 control-label no-padding-right">备注：</label>
			<div class="col-sm-6">
				<textarea name="remark" id="remark" type="text/plain" nullable="true" maxLength="200" style="width:100%;height:160px;">${database.remark!}</textarea>
			</div>
		</div>
		<div class="form-group">
            <label class="col-sm-2 control-label no-padding" for="form-field-1"></label>
            <div class="col-sm-6">
                <button type="button" class="btn btn-long btn-orange js-added" id="testBtn">&nbsp;测试&nbsp;</button>
                <button type="button" class="btn btn-long btn-blue js-added" id="saveBtn">&nbsp;保存&nbsp;</button>
            </div>
            </div>
        </div>
	</div>
</form>
<script>
var isSubmit=false;

$("#testBtn").on("click",function(){
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
			url : "${request.contextPath}/bigdata/datasource/db/test",
			dataType : 'json',
			success : function(data){
		 		if(!data.success){
		 			showLayerTips4Confirm('error',data.msg);
		 			isSubmit = false;
		 		}else{
					showLayerTips('success',data.msg,'t');
					isSubmit = false;
    			}
			},
			clearForm : false,
			resetForm : false,
			type : 'post',
			error:function(XMLHttpRequest, textStatus, errorThrown){}//请求出错 
		};
		$("#submitForm").ajaxSubmit(options);
});

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
			url : "${request.contextPath}/bigdata/datasource/db/save",
			dataType : 'json',
			success : function(data){
		 		if(!data.success){
		 			showLayerTips4Confirm('error',data.msg);
		 			isSubmit = false;
		 		}else{
		 			showLayerTips('success',data.msg,'t');
				  	showList('1');
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