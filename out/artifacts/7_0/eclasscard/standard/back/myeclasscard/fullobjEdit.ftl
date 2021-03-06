<#import "/eclasscard/common/back/mediaSelection.ftl" as media />
<div class="box box-default">
	<div class="box-body">
		<div class="tab-content">
			<div id="aa" class="tab-pane active" role="tabpanel">
				<div class="form-horizontal">
					<form id="eccFullObjForm">
					<input type="hidden" id="" name="id" value="${fullObj.id!}" >
					<div class="form-group">
						<label class="col-sm-2 control-title no-padding-right">发布全屏信息&nbsp;</label>
					</div>
					<div class="form-group">
						<label class="col-sm-2 control-label no-padding-right">发布对象：</label>
						<div class="col-sm-8">
							<div class="class-card-labels">
							<#if eccInfo.type=='10'>
								<span class="class-card-label"><span>${eccInfo.name!}</span><br>${eccInfo.className!}</span>
							<#else> 
								<span class="class-card-label"><span>${eccInfo.name!}</span><br>${eccInfo.placeName!}</span>
							</#if> 
							</div>
							<input type="hidden" name="sendType" value="0" >
							<input type="hidden" id="eccInfoId" name="eccInfoId" value="${eccInfo.id!}" >
						</div>
					</div>
					<div class="form-group">
						<label class="col-sm-2 control-label no-padding-right">选择媒体&nbsp;</label>
						<div class="col-sm-3">
							<a class="btn btn-default js-choose-media">选择媒体文件</a>
							<@media.media_div dataUrl="${request.contextPath}/eclasscard/group/media/popupData" id="objectId" typeId="type" toObjectId="toObjectId">
								<input type="hidden" id="objectId" name="objectId"  class="form-control" value="${fullObj.objectId!}">
								<input type="hidden" id="type" name="type" class="form-control" value="${fullObj.type!}">
								<input type="hidden" id="toObjectId"  class="form-control" value="${toObjectId!}">
							</@media.media_div>
							<div class="media-place">
								<#if fullObj.objectId?exists>
								<div class="card-content">
									<a href="javascript:;">
										<div class="card-img ratio-16of9">
										<#if object.type == 1>
											<img src="${request.contextPath}/static/images/growth-manual/photos.png" alt="">
										<#elseif object.type == 2>
											<img src="${request.contextPath}/static/images/growth-manual/videos.png" alt="">
										<#else>
											<img src="${request.contextPath}${object.coverUrl!}" alt="">
										</#if>
										</div>
									</a>
									<h4 class="card-name card-name-edit">
										<p>${object.title!}</p>
										<img src="${request.contextPath}/static/images/growth-manual/jpg-icon.png" alt="">
									</h4>
								</div>
								</#if>
							</div>
						</div>
					</div>
					<div class="form-group">
						<label class="col-sm-2 control-label no-padding-right">生效时间&nbsp;</label>
						<div class="col-sm-7">
							<div class="input-group">
								<input type="text" class="form-control datetimepicker" autocomplete="off" placeholder="开始时间" name="beginTime" id="beginTimeVal" value="${fullObj.beginTime!}">
								<span class="input-group-addon">
									<i class="fa fa-minus"></i>
								</span>
								<input type="text" class="form-control datetimepicker" autocomplete="off" placeholder="结束时间" name="endTime" id="endTimeVal" value="${fullObj.endTime!}">
							</div>
						</div>
					</div>
					<div class="form-group">
						<label class="col-sm-2 control-label no-padding-right">是否锁定&nbsp;</label>
						<div class="col-sm-6">
							<label>
								<input id="lockScreen" name="lockScreen" class="wp wp-switch js-openNotice" type="checkbox">
								<span class="lbl"></span>
							</label>
							<span class="hint-text">锁定开启后，班牌界面展示时将全屏锁定，班牌端无法进行其它操作</span>
						</div>
					</div>
					
					</form>
					<div class="form-group">
						<div class="col-sm-8 col-sm-offset-2">
							<button class="btn btn-long btn-blue" onclick="saveEccFullObj()">发布</button>
							<button class="btn btn-long btn-white" onclick="gobacktoList()">取消</button>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
</div>
<script>
function gobacktoList(){
	backFolderIndex('${tabType!}');
}

function gotoMedieSet(){
	layer.closeAll();
	backFolderIndex('4');
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
	    	pickerPosition:'top-right',
	    	autoclose: true
	    })
		<#--返回-->
		showBreadBack(gobacktoList,true,"全屏信息列表");
	},100);
}
$(function(){
	<#if fullObj.lockScreen>
		$('#lockScreen').click();
	</#if>
	dateInit();
});

var isSubmit=false;
function saveEccFullObj(){
	if(isSubmit){
        return;
    }
    var objectId = $("#objectId").val();
    if(!objectId||objectId==''){
		layerTipMsgWarn("","请选择展示媒体");
		return;
	}
    var beginTime = $("#beginTimeVal").val();
    var endTime = $("#endTimeVal").val();
    if(!beginTime||beginTime==''||!endTime||endTime==''){
    	layerTipMsgWarn("生效时间","请选择完整的生效时间");
		return;
    }else{
	    if(beginTime>=endTime){
    	layerTipMsgWarn("生效时间","结束时间应大于开始时间");
		return;
	    }
    }
    
	isSubmit = true;
	var options = {
			url : "${request.contextPath}/eclasscard/standard/fullscreen/save",
			data:{},
			dataType : 'json',
			success : function(data){
		 		if(!data.success){
		 			layerTipMsg(data.success,"保存失败",data.msg);
		 			isSubmit = false;
		 		}else{
					gobacktoList();
    			}
			},
			clearForm : false,
			resetForm : false,
			type : 'post',
			error:function(XMLHttpRequest, textStatus, errorThrown){}//请求出错 
		};
		$("#eccFullObjForm").ajaxSubmit(options);
}
</script>