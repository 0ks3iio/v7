<#import "/fw/macro/webUploaderMacro.ftl" as upload />
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
		
		<div class="form-group" style="display:none">
			<label class="col-sm-2 control-label no-padding-right"><font style="color:red;">*</font>类型：</label>
			<div class="col-sm-6">
                <input type="hidden" name="type" id="type" value="${database.type!}">
			</div>
		</div>

        <div class="form-group hive-db">
            <label class="col-sm-2 control-label no-padding-right"><font style="color:red;">*</font>连接模式：</label>
            <div class="col-sm-6">
                <select name="connectMode" id="connectMode" class="form-control" onchange="changeConnectModel()">
					<option value="hiveserver2" <#if (database.connectMode!) == 'hiveserver2'>selected="selected"</#if>>hiveserver2</option>
					<option value="zookeeper" <#if (database.connectMode!) == 'zookeeper'>selected="selected"</#if>>zookeeper</option>
                </select>
            </div>
        </div>
		
		<div class="form-group">
			<label class="col-sm-2 control-label no-padding-right"><font style="color:red;">*</font>域名：</label>
			<div class="col-sm-6">
				<input type="text" name="domain" id="domain" class="form-control" nullable="false" maxLength="50" value="${database.domain!}">
			</div>
		</div>
		
		<div class="form-group hiveserver2">
			<label class="col-sm-2 control-label no-padding-right"><font style="color:red;">*</font>端口：</label>
			<div class="col-sm-6">
				<input type="text" name="port" id="port" class="form-control" nullable="false" maxLength="6" vtype="digits" value="${database.port!}">
			</div>
		</div>

        <div class="form-group zookeeper">
            <label class="col-sm-2 control-label no-padding-right"><font style="color:red;">*</font>命名空间：</label>
            <div class="col-sm-6">
                <input type="text" name="nameSpace" id="nameSpace" class="form-control" nullable="false" maxLength="50" value="${database.nameSpace!}">
            </div>
        </div>
		
		<div class="form-group hive-db db-name ">
			<label class="col-sm-2 control-label no-padding-right"><font style="color:red;">*</font>数据库名称：</label>
			<div class="col-sm-6">
				<input type="text" name="dbName" id="dbName" class="form-control" nullable="false" maxLength="50" value="${database.dbName!}">
			</div>
		</div>

        <div class="form-group hive-db auth-way">
            <label class="col-sm-2 control-label no-padding-right"><font style="color:red;">*</font>认证方式：</label>
            <div class="col-sm-6">
                <select name="authWay" id="authWay" class="form-control" onchange="changeAuthWay()">
                    <option value="">无</option>
                    <option value="kerberos" <#if (database.authWay!) == 'kerberos'>selected="selected"</#if> >kerberos</option>
                    <option value="username" <#if (database.authWay!) == 'username'>selected="selected"</#if>>用户名</option>
                    <option value="usernameAndPassword" <#if (database.authWay!) == 'usernameAndPassword'>selected="selected"</#if>>用户名和密码</option>
                </select>
            </div>
        </div>

        <div class="form-group hive-db kerberos">
            <label class="col-sm-2 control-label no-padding-right"><font style="color:red;">*</font>服务器principal：</label>
            <div class="col-sm-6">
                <input type="text" name="serverPrincipal" id="serverPrincipal" class="form-control" nullable="false" maxLength="50" value="${database.serverPrincipal!}">
            </div>
        </div>

        <div class="form-group hive-db kerberos">
            <label class="col-sm-2 control-label no-padding-right"><font style="color:red;">*</font>客户端principal：</label>
            <div class="col-sm-6">
                <input type="text" name="clientPrincipal" id="clientPrincipal" class="form-control" nullable="false" maxLength="50" value="${database.clientPrincipal!}">
            </div>
        </div>

        <div class="form-group hive-db kerberos">
                    <@upload.fileUpload businessKey="keytab" contextPath="${request.contextPath}" resourceUrl="${resourceUrl!}" extensions="keytab" size="1" fileNumLimit="1" handler="loadFile">
                        <label class="col-sm-2 control-label no-padding-right"><font style="color:red;">*</font>keytab文件：</label>
                        <div class="col-sm-4">
                            <a class="file">
                                <input type="button"
                                       class="form-control datetimepicker js-addFiles"
                                       id="file">上传文件
                            </a>
                            <p class="js-file-content" class="form-control">${database.keytabFile!}</p>
                        </div>
                        <!--这里的id就是存放附件的文件夹地址 必须维护-->
                        <input type="hidden" id="keytabFileName" name="keytabFileName" value="">
                        <input type="hidden" id="keytab-path" name="keytabFile" value="${database.keytabFile!}">
					</@upload.fileUpload>
        </div>

		<div class="form-group username usernameAndPassword">
			<label class="col-sm-2 control-label no-padding-right"><font style="color:red;">*</font>用户名：</label>
			<div class="col-sm-6">
				<input type="text" name="userName" id="userName" class="form-control" nullable="false" maxLength="50" value="${database.userName!}">
			</div>
		</div>
		
		<div class="form-group usernameAndPassword">
			<label class="col-sm-2 control-label no-padding-right">密码：</label>
			<div class="col-sm-6">
				<input type="password" name="password" id="password" class="form-control" nullable="true" maxLength="50" value="${database.password!}">
			</div>
		</div>
		<div class="form-group hive-db">
			<label class="col-sm-2 control-label no-padding-right">初始化SQL：</label>
			<div class="col-sm-6">
				<textarea name="initSql" id="initSql" type="text/plain" nullable="true" maxLength="200" style="width:100%;height:160px;">${database.remark!}</textarea>
			</div>
		</div>

        <div class="form-group kylin-db">
            <label class="col-sm-2 control-label no-padding-right"><font style="color:red;">*</font>项目：</label>
            <div class="col-sm-6">
                <input type="text" name="projectName" id="projectName" class="form-control" nullable="false" maxLength="50" value="${database.dbName!}">
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

$(document).ready(function(){
    changeType();
    changeConnectModel();
});

function changeType() {
	var type = $('#type').val();
    if (type == '06' || type == "08" ) {
        $('.kylin-db').hide();
		$('.hive-db').show();
        changeConnectModel();
        changeAuthWay();
	} else if (type == '07') {
        $('.kylin-db').show();
        $('.hive-db').hide();
        $('.usernameAndPassword').show();
        $('.zookeeper').hide();
        $('.hiveserver2').show();
	}else if (type == '12') {
        $('.kylin-db').hide();
        $('.hive-db').hide();
        $('.usernameAndPassword').hide();
        $('.zookeeper').hide();
	}else if (type == '09') {
      	$('.kylin-db').hide();
		$('.hive-db').hide();
		 $('.zookeeper').hide();
		$('.db-name').show();
		$('.auth-way').show();
        changeAuthWay();
	}
}

function changeConnectModel() {
    var type = $('#type').val();
    if (type == '06' || type == "08" ) {
        var cm = $('#connectMode').val();
        if (cm == "hiveserver2") {
            $('.zookeeper').hide();
			$('.hiveserver2').show();
		} else {
            $('.hiveserver2').hide();
            $('.zookeeper').show();
		}
    }
}

function changeAuthWay() {
    var type = $('#type').val();
    if (type == '06'  || type == "08" || type == "09") {
		var cm = $('#authWay').val();
		if (cm == "") {
		    $('.kerberos').hide();
		    $('.username').hide();
		    $('.usernameAndPassword').hide();
		} else if (cm == "kerberos") {
            $('.kerberos').show();
            $('.username').hide();
            $('.usernameAndPassword').hide();
		} else if (cm == "username") {
            $('.kerberos').hide();
            $('.usernameAndPassword').hide();
            $('.username').show();
        } else if (cm == "usernameAndPassword") {
            $('.kerberos').hide();
            $('.username').hide();
            $('.usernameAndPassword').show();
		}
	}
}

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
    $("input[name='file']").remove();
	var options = {
			url : "${request.contextPath}/bigdata/nosqlDatasource/test",
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
	$("input[name='file']").remove();
    var options = {
        url : "${request.contextPath}/bigdata/nosqlDatasource/save",
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

function loadFile() {
    if (hasUploadSuc) {
        $("#keytabFileName").val(fileName);
        var file = $("#keytab-path").val() + fileName;
        $("#keytab-path").val(file);
        $(".js-file-content").html(fileName);
    }
}
</script>