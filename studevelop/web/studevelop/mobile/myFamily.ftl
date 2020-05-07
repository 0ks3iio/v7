<#import "/fw/macro/mobilecommon.ftl" as common>
<#import "/studevelop/mobile/commonStuDevelop.ftl" as stuDevelop>
<@common.moduleDiv titleName="幸福的一家">
<link href="${request.contextPath}/studevelop/mobile/css/style.css" rel="stylesheet"/>
<style type="text/css">
	.mui-input-row{
		background: #fff;
		border-bottom: 1px solid #dfdfdf;
	}
	.mui-input-row textarea{
		border: none !important;
		margin-bottom: 0 !important;
		padding: 10px 10px 0;
	}
	.mui-btn-up{
		color: #0ec587;
	}
</style>
<@stuDevelop.acadyearSemester stuDevelopUrl="${request.contextPath}/mobile/open/studevelop/family/index?" params="studentId=${studentId!}" >

</@stuDevelop.acadyearSemester>
<div class="mui-content">
	<form id ="form" action="">
		<input type="hidden" name="id" value="${item.id!}" />
		<input type="hidden" name="studentId" value="${item.studentId!}" />
		<input type="hidden" name="acadyear" value="${item.acadyear!}" />
		<input type="hidden" name="semester" value="${item.semester!}" />
		
		<div style="display:none;"><input type="file" id="file" name="file" accept="image/*" ></div>
		
		<#if item.existsImgPath?default(false) && item.imgPath?exists>
			<div id="imgDiv" class="mui-camera-show">
				<a href="#" class="my-file-up mui-camera-up">重新上传</a>
				<img id="img" src="${request.contextPath}/studevelop/common/attachment/showPic?id=${item.imgAttId!}" class="mui-camera-img" />
			</div>
		<#else>
			<div id="imgDiv" class="mui-camera-show" style="display:none;">
				<a href="#" class="my-file-up mui-camera-up">重新上传</a>
				<img id="img" src="${request.contextPath}/studevelop/common/attachment/showPic?id=${item.imgAttId!}" class="mui-camera-img" />
			</div>
			<div id="addImg" class="mui-camera-area">
				<i class="mui-camera-i"></i><br />
				<span class="mui-camera-info">上传全家福，分享你们的幸福~</span><br /><br />
				<button type="button" class="my-file-up mui-btn mui-btn-up">去上传</button>
			</div>
		</#if>
			
	    <div class="mui-input-row mt-10">
			<textarea class="mui-input-clear" id="parentContent" name="parentContent" rows="5" placeholder="hi，对孩子说点什么吧...">${item.parentContent!}</textarea>
		</div>
		<div class="mui-input-row mt-10" >
			<textarea class="mui-input-clear" rows="5" id="childContent" name="childContent" placeholder="hi，对爸妈说点什么吧...">${item.childContent!}</textarea>
		</div>
		<div class="mui-input-opt">
			<button type="button" id="save" class="mui-btn mui-btn-block mui-btn-green" onclick="doSave();">保存</button>
		</div>
	</form>
</div>
<script type="text/javascript" charset="utf-8">
  	mui.init();
  	
  	$(".my-file-up").on('click',function(){
  		$("#file").click();
	});
	
	$("#file").on('change',function(){
		var file = $(this).get(0).files[0];
		previewFile(file, "#img", function(){
			//处理完成
			$("#addImg").hide();
			$("#imgDiv").show();
		});
	});
</script>
<script type="text/javascript">
//保存	
	function doSave(){
		
		if(!isActionable("#save")){
			return;
		}
		
		var parentContent = $("#parentContent").val();
		var childContent = $("#childContent").val();
		if(isNotBlank(parentContent)){
			var len = getLength(parentContent);
			if(len > 1000){
				alertMsg("对孩子说的话不能超过1000个字符(一个汉字两个字符)");
				return;
			}
		}else{
			//alertMsg("对孩子说的话不能为空");
			//return;
		}
		
		if(isNotBlank(childContent)){
			var len = getLength(childContent);
			if(len > 1000){
				alertMsg("对爸妈说的话不能超过1000个字符(一个汉字两个字符)");
				return;
			}
		}else{
			//alertMsg("对爸妈说的话不能为空");
			//return;
		}
		
		setDisabled("#save");//设置按钮不可用
		mui.toast('正在保存',{
			duration: 3500,
			offset: 'center'
		});
		ajaxSubmit("${request.contextPath}/mobile/open/studevelop/family/save", "#form", function(data){
			setDefault("#save");
			toastMsg(data.msg);
			back();
		});
	}
	
	function back(){
		window.history.back(-1);
		//load("${request.contextPath}/mobile/open/studevelop/homepage?studentId=${item.studentId!}");
	}
	
</script>
</@common.moduleDiv>