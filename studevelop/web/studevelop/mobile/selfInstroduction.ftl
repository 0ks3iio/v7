<#import "/fw/macro/mobilecommon.ftl" as common>
<@common.moduleDiv titleName="自我介绍">
<link href="${request.contextPath}/studevelop/mobile/css/style.css" rel="stylesheet"/>
<style type="text/css">
	.mui-input-row{
		background: #fff;
		border-top: 1px solid #dfdfdf;
		border-bottom: 1px solid #dfdfdf;
		margin-top: 10px;
	}
	.mui-input-row textarea{
		border: none !important;
		margin-bottom: 0 !important;
		padding: 10px 10px 0;
	}
	.mui-input-user{
		display: block;
		height: 64px;
		line-height: 64px;
		font-size: 15px;
		color: #333;
	}
</style>
<div class="mui-content">
	<form id ="form" action="">
		<input type="hidden" name="id" value="${item.id!}" />
		<input type="hidden" name="studentId" value="${item.studentId!}" />
		<input type="hidden" name="acadyear" value="${item.acadyear!}" />
		<input type="hidden" name="semester" value="${item.semester!}" />
		<input type="hidden" name="hasRelease" value="${item.hasRelease?default(0)}" />
		
		<div style="display:none;"><input type="file" id="file" name="file" accept="image/*" ></div>
		<div class="mui-input-row pa-10 mui-clearfix">
			<span class="mui-pull-left mui-input-user">${stuName?default("")}</span>
			<#if item.imgPath?exists>
				<img id="img" src="${fileUrl!}${item.filePath!}" class="mui-input-avatar mui-pull-right"/>
			<#else>
				<img id="img" src="${request.contextPath}/studevelop/mobile/images/icon/img_default_photo.png" class="mui-input-avatar mui-pull-right"/>
			</#if>
		</div>
	    <div class="mui-input-row">
			<textarea id="speciality" name="speciality" class="mui-input-clear" rows="6" maxlength="1000" placeholder="请输入特长爱好...">${item.speciality!}</textarea>
		</div>
		<div class="mui-input-row">
			<textarea id="content" name="content" class="mui-input-clear" rows="6" maxlength="1000" placeholder="请输入自我介绍...">${item.content!}</textarea>
		</div>
		<div class="mui-input-opt">
			<button id="save" type="button" class="mui-btn mui-btn-block mui-btn-green" onclick="doSave();">保存</button>
		</div>
	</form>
</div>
<script type="text/javascript" charset="utf-8">
  	mui.init();
  	
  	document.getElementById("img").addEventListener("tap",function(){
			$("#file").click();
	});
	
	$("#file").on('change',function(){
		var file = $(this).get(0).files[0];
		previewFile(file, "#img", function(){
			
		});
	});
</script>
<script>
//保存	
	function doSave(){
		
		if(!isActionable("#save")){
			return;
		}
		
		var speciality = $("#speciality").val();
		var content = $("#content").val();
		if(isNotBlank(speciality)){
			var len = getLength(speciality);
			if(len > 1000){
				alertMsg("特长爱好不能超过1000个字符");
				return;
			}
		}else{
			alertMsg("特长爱好不能为空");
			return;
		}
		
		if(isNotBlank(content)){
			var len = getLength(content);
			if(len > 1000){
				alertMsg("自我介绍不能超过1000个字符");
				return;
			}
		}else{
			alertMsg("自我介绍不能为空");
			return;
		}
		
		setDisabled("#save");//设置按钮不可用
		mui.toast('正在保存',{
			duration: 3500,
			offset: 'center'
		});
		ajaxSubmit("${request.contextPath}/mobile/open/studevelop/instroduction/save", "#form", function(data){
			setDefault("#save");
			toastMsg(data.msg);
			back();
		});
	}
	
	function back(){
		storage.set("type","1");
		history.go(-1);
		//window.history.back(-1);
	}
</script>
</@common.moduleDiv>