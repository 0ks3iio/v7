<script src="${request.contextPath}/static/components/jquery/dist/jquery.min.js"></script>
<script src="${request.contextPath}/static/layer/layer.js"></script>
<script src="${request.contextPath}/static/layer/extend/layer.ext.js"></script>
<script src="${request.contextPath}/static/js/desktop.js"></script>
<script src="${request.contextPath}/static/js/tool.js"></script>
<!-- bootstrap & fontawesome -->
<link rel="stylesheet" href="${request.contextPath}/static/ace/css/bootstrap.css" />
<!-- text fonts -->
<!-- ace styles -->
<link rel="stylesheet" href="${request.contextPath}/static/ace/css/ace.min.css" class="ace-main-stylesheet" id="main-ace-style" />
<div class="layer layer-saveUserInfo " >
	<div class="form-horizontal">
	    <div class="form-group layerServerSee" style="display:none">   
	       <input type="text" id="ownerType" class="form-control" 
			  <#if user?exists> value="${user.ownerType!}" </#if> />
		   <input type="text"  id="sex" class="form-control" 
			  <#if user?exists> value="${user.sex!}" </#if> />
		   <input type="text"  id="userName" class="form-control" 
			  <#if user?exists> value="${user.username!}" </#if> />
		   <input type="text"  id="idCardNo" class="form-control" 
			  <#if user?exists> value="${user.identityCard!}" </#if> />
		   <input type="text"  id="phone" class="form-control" 
			  <#if user?exists> value="${user.mobilePhone!}" </#if> />
		   <input type="text"  id="userType" class="form-control" 
			  <#if user?exists> value="${user.userType!}" </#if> />
		</div>
		<div class="form-group">
			<label class="col-sm-3 control-label no-padding-right">性别：</label>
			<div class="col-sm-9" >
			  <select name="sex" id="sex" class="form-control inteEdit">
					<option value="1" >男</option>
					<option value="2" >女</option>
			  </select>
			</div>
		</div>
		<div class="form-group">
			<label class="col-sm-3 control-label no-padding-right">选择单位：</label>
			<div class="col-sm-9" >
			  <select name="unitId" id="unitId" class="form-control">
			     <#if unitList?exists && (unitList?size>0)>
	                <#list unitList as unit>
	                  <option value="${unit.id!}">
	                     ${unit.unitName!}</option>
	                </#list>
                </#if>
			  </select>
			</div>
		</div>
		<div class="form-group">
			<label class="col-sm-3 control-label no-padding-right">姓名：</label>
			<div class="col-sm-9" >
			  <input type="text" nullable="false" maxLength="50" id="name" class="form-control"
			  <#if user?exists> value="${user.realName!}" </#if> />
			</div>
		</div>
		<#if isStudent?default(false)>
			<div class="form-group">
				<label class="col-sm-3 control-label no-padding-right">选择班级：</label>
				<div class="col-sm-9" >
				  <select name="classId" id="classId" class="form-control">
				     <#if allClazz?exists && (allClazz?size>0)>
		                <#list allClazz as clazz>
		                  <option value="${clazz.id!}">
		                     ${clazz.className!}</option>
		                </#list>
	                </#if>
				  </select>
				</div>
			</div>
		<#else>
			<div class="form-group">
				<label class="col-sm-3 control-label no-padding-right">教师编号：</label>
				<div class="col-sm-9" >
				  <input type="text" nullable="false" vtype="digits" min="1" maxlength="8" id="teacherCode" class="form-control" />
				</div>
			</div>
		</#if>
	</div>
</div>
<script>
 $(document).ready(function () {
	 layer.open({
         type: 1,
         shade: 0.5,
         closeBtn: 0,
         title: '请完善用户信息',
         area: '500px',
         btn: ['保存','取消'],
         yes: function (index, layero) {
        	 saveUserInfo("${request.contextPath}/homepage/remote/openapi/xigu/saveUser");
         },
         content: $('.layer-saveUserInfo')
     })
 });
 
//保存用户信息
 var isSubmit=false;
 function saveUserInfo(contextPath){
     if(isSubmit){
 		return;
 	}
 	isSubmit = true;
     var check = checkValue('.layer-saveUserInfo');
 	if(!check){
 	 	$(this).removeClass("disabled");
 	 	isSubmit=false;
 	 	return;
 	}
   $.ajax({
         url:contextPath,
         data:dealDValue(".layer-saveUserInfo"),
         clearForm : false,
         resetForm : false,
         dataType:'json',
         contentType: "application/json",
         type:'post',
         success:function (data) {
             isSubmit = false;
             if(data.success){
                showSuccessMsgWithCall(data.msg,doLogin(data.businessValue));
             }else{
                showErrorMsg(data.msg);
             }
         }
   })
 }
 
 function doLogin(contextPath){
	 location.href=contextPath;
 }
</script>

