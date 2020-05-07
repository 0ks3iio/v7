<#import "/fw/macro/popupMacro.ftl" as popup />
<#import "/fw/macro/webUploaderMacro.ftl" as upload />
<meta http-equiv="Content-Type" content="text/html;charset=utf-8"/>
<link rel="stylesheet" type="text/css" href="${request.contextPath}/static/components/cropper/cropper.min.css">
<script type="text/javascript" charset="utf-8" src="${request.contextPath}/static/components/cropper/cropper.min.js"></script>
<div class="box box-default">
	<div class="box-body">
		<div class="tab-content">
			<div id="cc" class="tab-pane active" role="tabpanel">
				<div class="form-horizontal">
					<form id="honorForm">
					<input type="hidden" name="id" value="${eccHonor.id!}" >
					<input type="hidden" name="createTime" value="${eccHonor.createTime!}" >
					<input type="hidden" name="type" value="2" >
					<input type="hidden" name="status" value="1" >
					<div class="form-group">
						<label class="col-sm-2 control-title no-padding-right">颁布荣誉&nbsp;</label>
					</div>
					<div class="form-group">
						<label class="col-sm-2 control-label no-padding-right">荣誉名称：</label>
						<div class="col-sm-4">
							<div class="input-group">
								<input type="text" id='title' name="title" autocomplete="off" class="form-control js-limit-word" maxlength="10" value="${eccHonor.title!}">
								<span class="input-group-addon">0/10</span>
							</div>
						</div>
					</div>
					<div class="form-group">
						<label class="col-sm-2 control-label no-padding-right">班牌展示时间：</label>
						<div class="col-sm-4">
							<div class="input-group">
								<input type="text" id='beginTime' name="beginTime" autocomplete="off" class="form-control datetimepicker" placeholder="开始时间" value="${eccHonor.beginTime!}">
								<span class="input-group-addon">
									<i class="fa fa-minus"></i>
								</span>
								<input type="text" id='endTime' name="endTime" autocomplete="off" class="form-control datetimepicker" placeholder="结束时间" value="${eccHonor.endTime!}">
							</div>
						</div>
					</div>
					<div class="form-group">
						<label class="col-sm-2 control-label no-padding-right">获奖日期：</label>
						<div class="col-sm-4">
							<div class="input-group">
								<input type="text" id='awardTime' name="awardTime" autocomplete="off" class="form-control awardtimepicker" placeholder="获奖日期" <#if eccHonor.awardTime?exists>value="${(eccHonor.awardTime?string("yyyy-MM-dd"))?if_exists}"</#if>>
							</div>
						</div>
					</div>
					<div class="form-group">
						<label class="col-sm-2 control-label no-padding-right">获奖学生：</label>
						<div class="col-sm-4">
							<@popup.selectOneStuByClass id="studentId" name="studentName" clickId="studentName" dataUrl="${request.contextPath}/common/div/student/popupData/class?classId=${classId!}">
								<input type="text" autocomplete="off" class="form-control" id="studentName" name="studentName"  value="${eccHonor.studentName!}" />
								<input type="hidden" id="studentId" name="studentId" value="${eccHonor.studentId!}"/>
							</@popup.selectOneStuByClass>
						</div>
					</div>
					<#if (eccHonor.id!) == ''>
					<div class="form-group">
						<label class="col-sm-2 control-label no-padding-right">上传获奖照片：</label>
						<div class="col-sm-8">
							<div class="filter">
								<div class="filter-item">
									<@upload.picUpload businessKey="${photoDirId!}" extensions="jpg,jpeg,png" contextPath="${request.contextPath}" resourceUrl="${resourceUrl}" size="10" fileNumLimit="1" handler="changeImg">
										<a href="javascript:;" class="btn btn-blue js-addPhotos">选择照片</a>
										<!--这里的id就是存放附件的文件夹地址 必须维护-->
										<input type="hidden" id="${photoDirId!}-path" value="">
									</@upload.picUpload>
									&emsp;图片仅支持jpg、jpeg、png格式
								</div>
							</div>
							<input type="hidden" id="cutImgSize" name="cutImgSize" value="" >
							<input type="hidden" id="filePath" name="filePath" value="" >
							<input type="hidden" id="fileName" name="fileName" value="" >
							<div class="img-cropper row">
								<div class="col-sm-7">
									<div class="img-cropper-content">
									<img id="picPath" src="${request.contextPath}/static/eclasscard/standard/show/images/no-image.png" alt="">
									</div>
								</div>
								<div class="col-sm-5">
									<div class="img-cropper-preview"></div>
								</div>
							</div>
						</div>
					</div>
					<#else>
					<div class="form-group">
						<label class="col-sm-2 control-label no-padding-right">照片：</label>
						<div class="col-sm-8">
							<div class="img-cropper row">
								<div class="col-sm-7">
									<div class="img-cropper-content">
									<img src="${request.contextPath}${imageUrl!}" alt="">
									</div>
								</div>
							</div>
						</div>
					</div>
					</#if>
					</form>
					<div class="form-group">
						<div class="col-sm-8 col-sm-offset-2">
							<button class="btn btn-long btn-blue" onclick="honorSave()">确定</button>
							<button class="btn btn-long btn-white" onclick="gobacktoList()">取消</button>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
</div>
<script>
	<#if (eccHonor.id!) == ''>
	var image = $('.img-cropper img')[0];
	var cropper = new Cropper(image, {
		aspectRatio: 4 / 3,
		viewMode:2,
		preview: $('.img-cropper-preview')[0],
		crop: function(e) {
			$("#cutImgSize").val(JSON.stringify(e.detail));
		}
	});
	</#if>
function gobacktoList(){
	backFolderIndex('2');
}
function dateInit(){
	if(date_timepicker &&date_timepicker!=null){
		date_timepicker.remove();
	}
	setTimeout(function(){
		var minDate = "${((.now)?string('yyyy-MM-dd HH:mm'))?if_exists}";
		date_timepicker = $('.datetimepicker').datetimepicker({
			startDate:minDate,
			language: 'zh-CN',
    		format: 'yyyy-mm-dd hh:ii',
    		autoclose: true
    	})
		date_timepicker = $('.awardtimepicker').datetimepicker({
			language: 'zh-CN',
			minView: 'month',
    		format: 'yyyy-mm-dd',
    		autoclose: true
    	})
		<#--返回-->
		showBreadBack(gobacktoList,true,"荣誉列表");
	},100);
}
$(function(){
	dateInit();
	var titleLength = $('#title').val().length;
	$('#title').next().text(titleLength +'/'+10);
	$('.js-limit-word').on('keyup',function(){
		var max = $(this).attr('maxlength');
		if(this.value.length+1 > max){
			layer.tips('最好在'+max+'个字数内哦', this, {
				tips: [1, '#000'],
				time: 2000
			})
		}
		$(this).next().text(this.value.length +'/'+max);
	});
	
});

function changeImg() {
	$.ajax({
		url:'${request.contextPath}/eclasscard/standard/honor/imageurl',
		data:{"path":"${photoPath!}"},
		type:'post',
		success:function(data) {
			$("#attachmentId").val("");
			if (data == "") {
				$("#filePath").val("");
				$("#fileName").val("");
				$(".img-cropper img").attr("src","${request.contextPath}/static/eclasscard/standard/show/images/no-image.png");
			} else {
				var json1 = JSON.parse(data);
				$("#filePath").val(json1.filePath);
				$("#fileName").val(json1.fileName);
				$("#picPath").attr("src","${request.contextPath}"+json1.imageUrl);
				cropper.replace($(image).attr("src"));
			}
		},
 		error:function(XMLHttpRequest, textStatus, errorThrown){}
	});
}

	var isSubmit=false;
	function honorSave(){
		if(isSubmit){
       		return;
    	}
    	
    	var title = $("#title").val();
    	if(!title||title==''){
    		layerTipMsgWarn("荣誉名称","不可为空");
			return;
    	}else{
    		if(title.length>10){
    			layerTipMsgWarn("荣誉名称","长度不可超过10字");
				return;
    		}
    	}
    	
    	var beginTime = $("#beginTime").val();
    	var endTime = $("#endTime").val();
    	if(!beginTime||beginTime==''||!endTime||endTime==''){
    		layerTipMsgWarn("展示时间","请选择完整的展示时间");
			return;
    	}else{
	    	if(beginTime>=endTime){
    			layerTipMsgWarn("展示时间","结束时间应大于开始时间");
				return;
			}
	   	}
	   	
	   	var awardTime = $("#awardTime").val();
	   	if (!awardTime||awardTime=='') {
	   		layerTipMsgWarn("荣获时间","请选择完整的荣获时间");
			return;
	   	}
	   	
	    var studentId = $("#studentId").val();
	    if(!studentId||studentId==''){
	    	layerTipMsgWarn("选择学生","不可为空");
			return;
	    }
    	
    	<#if (eccHonor.id!) == ''>
    	var filePath = $("#filePath").val();
    	if (!filePath || filePath=='') {
    		layerTipMsgWarn("上传图片","不可为空");
			return;
    	}
    	</#if>
		isSubmit = true;
		var options = {
			url : "${request.contextPath}/eclasscard/standard/honor/save",
			dataType : 'json',
			success : function(data){
		 		if(!data.success){
		 			layerTipMsg(data.success,"保存失败",data.msg);
		 			isSubmit = false;
		 		}else{
					gobacktoList()
    			}
			},
			clearForm : false,
			resetForm : false,
			type : 'post',
			error:function(XMLHttpRequest, textStatus, errorThrown){}//请求出错 
		};
		$("#honorForm").ajaxSubmit(options);
	}
</script>