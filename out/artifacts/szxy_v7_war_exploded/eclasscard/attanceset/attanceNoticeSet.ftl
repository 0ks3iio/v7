<#import "/fw/macro/popupMacro.ftl" as popup />
<form id="noticeSetForm">
<input type="hidden" name="idClass" value="${noticeSetClass.id!}">
<input type="hidden" name="unitId" value="${noticeSetClass.unitId!}">
<input type="hidden" name="idDorm" value="${noticeSetDorm.id!}">
<input type="hidden" name="idInOut" value="${noticeSetInOut.id!}">
<div class="form-horizontal">
	<#if isStandard>
	<div class="form-group js-one">
		<label class="col-sm-3 control-label">全屏信息发布权限
			<span class="has-tips js-screen">
				<i class="fa fa-question-circle fa-tip" data-toggle="tooltip" data-placement="right" title="开启后，教师可在’我的班牌‘中发布全屏多媒体信息"></i>
			</span>：
		</label>
		<div class="col-sm-9">
			<label>
				<input name="screenRadio" class="wp js-closeScreen" <#if !fullScreen>checked="checked"</#if> type="radio" value=0> 
				<span class="lbl"> 关闭</span>
			</label>
			<label>
				<input name="screenRadio" class="wp js-openScreen" <#if fullScreen>checked="checked"</#if>type="radio" value=1> 
				<span class="lbl"> 开启</span>
			</label>
		</div>
	</div>
	<div class="form-group js-two">
		<label class="col-sm-3 control-label">班牌端视频播放声音：</label>
		<div class="col-sm-9">
			<label>
				<input name="voiceRadio" class="wp js-closeVoice" <#if vox>checked="checked"</#if>  type="radio" value=1> 
				<span class="lbl"> 静音</span>
			</label>
			<label>
				<input name="voiceRadio" class="wp js-openVoice" <#if !vox>checked="checked"</#if>type="radio" value=0> 
				<span class="lbl"> 有声音</span>
			</label>
		</div>
	</div>
	<div class="form-group js-three">
		<label class="col-sm-3 control-label">考场门贴展示
			<span class="has-tips js-door">
			<i class="fa fa-question-circle fa-tip" data-toggle="tooltip" data-placement="right" title="1、展示时系统将默认全屏锁定，无法进行其它操作  . 2、考场门贴在班牌端的全屏展示为最高优先级"></i>
			</span>：
		</label>
		<div class="col-sm-9">
			<label>
				<input name="doorRadio" class="wp js-close" <#if !doorSticker>checked="checked"</#if> type="radio" value=0> 
				<span class="lbl"> 不展示</span>
			</label>
			<label>
				<input name="doorRadio" class="wp js-open" <#if doorSticker>checked="checked"</#if> type="radio" value=1> 
				<span class="lbl"> 全屏展示</span>
			</label>
		</div>
	</div>
	<div class="form-group js-three <#if !doorSticker>hidden</#if>">
		<div class="col-sm-6 col-sm-offset-3 padding-10 bg-f2faff">
			<div class="clearfix">
				<label class="control-label form-left-name">开始时间：</label>
				<div class="form-right-content">
					<div class="form-group-txt">
						开考前
						<select name="doorDelayTime" id="doorDelayTime">
							<option <#if doorDelayTime?default('20')=='30'>selected</#if> value="30">30</option>
							<option <#if doorDelayTime?default('20')=='25'>selected</#if> value="25">25</option>
							<option <#if doorDelayTime?default('20')=='20'>selected</#if> value="20">20</option>
							<option <#if doorDelayTime?default('20')=='15'>selected</#if> value="15">15</option>
						</select>
						分钟
					</div>
				</div>
			</div>
		</div>
	</div>

	<div class="form-group js-four">
        <label class="col-sm-3 control-label">学生空间登录方式：</label>
        <div class="col-sm-9">
            <label>
                <input name="loginRadio" class="wp js-loginTypeOne" <#if loginType==0>checked="checked"</#if> type="radio" value=0> 
                <span class="lbl"> 直接刷卡</span>
            </label>
            <label>
                <input name="loginRadio" class="wp js-loginTypeTwo" <#if loginType==1>checked="checked"</#if> type="radio" value=1> 
                <span class="lbl"> 刷卡+密码</span>
            </label>
        </div>
    </div>

    <div class="form-group js-five">
        <label class="col-sm-3 control-label">PPT、照片播放速度：</label>
        <div class="col-sm-9">
			<div class="form-right-content">
				<div class="form-group-txt">
				每隔
                <select name="speedValue" id="speedValue">
                <option <#if speedValue?default("5")=="3">selected</#if> value="3">3</option>
                <option <#if speedValue?default("5")=="5">selected</#if> value="5">5</option>
                <option <#if speedValue?default("5")=="10">selected</#if> value="10">10</option>
                <option <#if speedValue?default("5")=="15">selected</#if> value="15">15</option>
                <option <#if speedValue?default("5")=="20">selected</#if> value="20">20</option>
                <option <#if speedValue?default("5")=="30">selected</#if> value="30">30</option>
                </select>
				秒，播放下一张
				</div>
			</div>
        </div>
    </div>

	<div class="form-group js-six">
		<label class="col-sm-3 control-label">人脸识别服务：</label>
		<div class="col-sm-9">
			<label>
				<input name="faceService" class="wp js-closeScreen js-face js-close" <#if !showArcFace>checked="checked"</#if> type="radio" value=0>
				<span class="lbl"> 关闭</span>
			</label>
			<label>
				<input name="faceService" class="wp js-openScreen js-face js-open" <#if showArcFace>checked="checked"</#if> type="radio" value=1>
				<span class="lbl"> 开启</span>
			</label>
		</div>
	</div>
	<div class="form-group js-nine <#if !showArcFace>hidden</#if>">
		<div class="col-sm-6 col-sm-offset-3 padding-10 bg-f2faff">
			<div class="clearfix margin-bottom-15">
				<label class="control-label form-left-name">下发照片阈值：</label>
				<div class="form-right-content">
					<div class="form-group-txt">
					当照片间相似度低于<input type="text"  name="pushThreshold" id="pushThreshold" value="${control?default('0.85')}" maxLength="4" style="width:50px;height: 20px;">才可下发
					<br/>（可输入0.8-0.9；值越大越相似，越容易下发）
					</div>
				</div>
			</div>
			<div class="clearfix margin-bottom-15">
				<label class="control-label form-left-name">识别人脸阈值：</label>
				<div class="form-right-content">
					<div class="form-group-txt">
					当摄像头识别人脸时与照片相似度高于<input type="text"  name="checkThreshold" id="checkThreshold" value="${distinguish?default('0.85')}" maxLength="4" style="width:50px;height: 20px;">才可识别成功
					<br/>（可输入0.8-0.9；值越大越相似，识别时摄像头捕捉的图像与照片的匹配要求越高）
					</div>
				</div>
			</div>
		</div>
	</div>

	<div class="form-group js-nine">
		<label class="col-sm-3 control-label">签到提示音：</label>
		<div class="col-sm-9">
			<label>
				<input name="prompt" class="wp js-closeScreen" <#if !openPrompt>checked="checked"</#if> type="radio" value=0>
				<span class="lbl"> 关闭</span>
			</label>
			<label>
				<input name="prompt" class="wp js-openScreen" <#if openPrompt>checked="checked"</#if> type="radio" value=1>
				<span class="lbl"> 开启</span>
			</label>
		</div>
	</div>
	</#if>

	<div class="form-group">
		<label class="col-sm-3 control-label"><h4 class="bold">未签到通知</h4></label>
	</div>
	<div class="form-group js-seven">
		<label class="col-sm-3 control-label">上课未签到通知：</label>
		<div class="col-sm-9">
			<label>
				<input name="sendClass" class="wp js-close" <#if noticeSetClass?exists && !noticeSetClass.send> checked="checked"</#if> value=0 type="radio"> 
				<span class="lbl"> 不通知</span>
			</label>
			<label>
				<input name="sendClass" class="wp js-open" <#if noticeSetClass?exists && noticeSetClass.send> checked="checked"</#if> value=1 type="radio"> 
				<span class="lbl"> 通知</span>
			</label>
		</div>
	</div>
	<div class="form-group js-seven <#if noticeSetClass?exists && !noticeSetClass.send>hidden</#if>">
		<div class="col-sm-6 col-sm-offset-3 padding-10 bg-f2faff">
			<div class="clearfix margin-bottom-15">
				<label class="control-label form-left-name">通知人员：</label>
				<div class="form-right-content">
					<label class="margin-r-20">
						<input type="checkbox" name="sendClassMasterClass" <#if noticeSetClass.sendClassMaster>checked="true"</#if> class="wp">
						<span class="lbl"> 班主任</span>
					</label>
					<label>
						<input type="checkbox" name="sendGradeMasterClass" <#if noticeSetClass.sendGradeMaster>checked="true"</#if> class="wp">
						<span class="lbl"> 年级组长</span>
					</label>
				</div>
			</div>
			
			<div class="clearfix">
				<label class="control-label form-left-name">其他人员：</label>
				<@popup.selectMoreTeacherUser clickId="userNameClass" id="classUserIds" name="userNameClass" handler="">
					<div class="col-sm-6">
						<div class="input-group">
							<input type="hidden" id="classUserIds" name="classUserIds" value="${classUserIds!}">
							<input type="text" id="userNameClass" class="form-control" value="${classUserNames!}">
							<a class="input-group-addon" href="javascript:void(0);"></a>
						</div>
					</div>
				</@popup.selectMoreTeacherUser>
			</div>
			
			<div class="clearfix">
				<label class="control-label form-left-name">通知时间：</label>
				<div class="form-right-content">
					<div class="form-group-txt">
						签到截止
							<select name="delayTime" id="delayTime">
							<option <#if noticeSetClass.delayTime?default(10)==20>selected</#if> value="20">20</option>
							<option <#if noticeSetClass.delayTime?default(10)==10>selected</#if>  value="10">10</option>
							<option <#if noticeSetClass.delayTime?default(10)==5>selected</#if> value="5">5</option>
							<option <#if noticeSetClass.delayTime?default(10)==3>selected</#if> value="3">3</option>
							<option <#if noticeSetClass.delayTime?default(10)==2>selected</#if> value="2">2</option>
							</select>
						分钟后
					</div>
				</div>
			</div>
		</div>
	</div>
	<div class="form-group js-eight">
		<label class="col-sm-3 control-label">寝室未签到通知：</label>
		<div class="col-sm-9">
			<label>
				<input name="sendDorm" class="wp js-close" <#if noticeSetDorm?exists && !noticeSetDorm.send>checked="checked"</#if> value=0 type="radio"> 
				<span class="lbl"> 不通知</span>
			</label>
			<label>
				<input name="sendDorm" class="wp js-open" <#if noticeSetDorm?exists && noticeSetDorm.send>checked="checked"</#if> value=1 type="radio"> 
				<span class="lbl"> 通知</span>
			</label>
		</div>
	</div>
	<div class="form-group js-eight <#if noticeSetDorm?exists && !noticeSetDorm.send>hidden</#if>">
		<div class="col-sm-6 col-sm-offset-3 padding-10 bg-f2faff">
			<div class="clearfix margin-bottom-15">
				<label class="control-label form-left-name">通知人员：</label>
				<div class="form-right-content">
					<label class="margin-r-20">
						<input type="checkbox" name="sendClassMasterDorm" <#if noticeSetDorm.sendClassMaster>checked="true"</#if>  class="wp">
						<span class="lbl"> 班主任</span>
					</label>
					<label>
						<input type="checkbox" name="sendGradeMasterDorm" <#if noticeSetDorm.sendGradeMaster>checked="true"</#if> class="wp">
						<span class="lbl"> 年级组长</span>
					</label>
				</div>
			</div>
			
			<div class="clearfix">
				<label class="control-label form-left-name">其他人员：</label>
					<@popup.selectMoreTeacherUser clickId="userNameDorm" id="dormUserIds" name="userNameDorm" handler="">
					<div class="col-sm-6">
						<div class="input-group">
							<input type="hidden" id="dormUserIds" name="dormUserIds" value="${dormUserIds!}">
							<input type="text" id="userNameDorm" class="form-control" value="${dormUserNames!}">
							<a class="input-group-addon" href="javascript:void(0);"></a>
						</div>
					</div>
					</@popup.selectMoreTeacherUser>
			</div>
			
			<div class="clearfix">
				<label class="control-label form-left-name">通知时间：</label>
				<div class="form-right-content">
					<div class="form-group-txt">
						在每个考勤签到时间段结束后，发送需要考勤年级的未签到名单给相应老师
					</div>
				</div>
			</div>
		</div>
	</div>
	<div class="form-group js-nine">
		<label class="col-sm-3 control-label">上下学签到通知：</label>
		<div class="col-sm-9">
			<label>
				<input name="sendInOut" class="wp js-close" <#if noticeSetInOut?exists && !noticeSetInOut.send>checked="checked"</#if> value=0 type="radio"> 
				<span class="lbl"> 不通知</span>
			</label>
			<label>
				<input name="sendInOut" class="wp js-open" <#if noticeSetInOut?exists && noticeSetInOut.send>checked="checked"</#if> value=1 type="radio"> 
				<span class="lbl"> 通知</span>
			</label>
		</div>
	</div>
	<div class="form-group js-nine <#if noticeSetInOut?exists && !noticeSetInOut.send>hidden</#if>">
		<div class="col-sm-6 col-sm-offset-3 padding-10 bg-f2faff">
			<div class="clearfix margin-bottom-15">
				<label class="control-label form-left-name">通知人员：</label>
				<div class="form-right-content">
					<label class="margin-r-20">
						<input type="checkbox" name="sendParentMasterInOut" <#if noticeSetInOut.sendParentMaster>checked="true"</#if>  class="wp">
						<span class="lbl"> 家长</span>
					</label>
				</div>
			</div>
			
			<div class="clearfix">
				<label class="control-label form-left-name">通知时间：</label>
				<div class="form-right-content">
					<div class="form-group-txt">
						实时发送，学生上下学考勤刷卡签到成功后立即发送给家长
					</div>
				</div>
			</div>
		</div>
	</div>
</div>
</form>
<br>
<div class="col-sm-offset-3"><button class="btn btn-blue btn-long" onclick="saveNoticeSet()">保存</button></div>
<script type="text/javascript">
$(function(){
	//发送通知设置
	$('.js-openNotice').on('click',function(){
		if($(this).prop('checked')===true){
			$(this).closest('.form-group').nextAll().removeClass('hidden');
		}else{
			$(this).closest('.form-group').nextAll().addClass('hidden');
		}
	})
	$('[data-toggle="tooltip"]').tooltip();
	<#if noticeSetClass?exists && noticeSetClass.send>
		$('#noticeSetClass').click();
	</#if>
	<#if noticeSetDorm?exists && noticeSetDorm.send>
		$('#noticeSetDorm').click();
	</#if>
	<#if noticeSetInOut?exists && noticeSetInOut.send>
		$('#noticeSetInOut').click();
	</#if>
	// 发送通知设置
	$('.js-open').each(function(){
		$(this).on('click',function(){
			if($(this).hasClass("js-face")){
				if($("#pushThreshold").val()==''){
					$("#pushThreshold").val("0.8");
				}
				if($("#checkThreshold").val()==''){
					$("#checkThreshold").val("0.8");
				}
			}
			$(this).closest('.form-group').next('.form-group').removeClass('hidden');
		});
	})
	
	$('.js-close').each(function(){
		$(this).on('click',function(){
			$(this).closest('.form-group').next('.form-group').addClass('hidden');
		});
	})
	$('.chosen-container').each(function() {
		$(this).width('100%');
	});
	
	$('.js-openNotice').on('click',function(){
		if($(this).prop('checked')===true){
			$(this).closest('.form-group').nextAll().removeClass('hidden');
		}else{
			$(this).closest('.form-group').nextAll().addClass('hidden');
		}
	})
});
function setUserids(type){
	if(type==1){
		$("#classUserIds").val($("#userIdsClass").val());
	}else{
		$("#dormUserIds").val($("#userIdsDorm").val());
	}
}
function checkFaceType(){
	var faceType=$("input[name='faceService']:checked").val();
  	if(faceType=="1"){
    	var pushThreshold=$.trim($("#pushThreshold").val());
		var checkThreshold=$.trim($("#checkThreshold").val());
    	if(checkParseFloat(pushThreshold,2)){
    		var ss=parseFloat(pushThreshold);
    		if(ss>=0.8 && ss<=0.9){
    			
    		}else{
    			layer.tips('请输入阈值范围在0.8~0.9', $("#pushThreshold"), {
					tipsMore: true,
					tips:3				
				});
    			return false;
    		}
    	}else{
    		layer.tips('请输入阈值范围在0.8~0.9', $("#pushThreshold"), {
				tipsMore: true,
				tips:3				
			});
    		return false;
    	}
    	
    	if(checkParseFloat(checkThreshold,2)){
    		var ss=parseFloat(checkThreshold);
    		if(ss>=0.8 && ss<=0.9){
    			
    		}else{
    			layer.tips('请输入阈值范围在0.8~0.9', $("#checkThreshold"), {
					tipsMore: true,
					tips:3				
				});
    			return false;
    		}
    	}else{
    		layer.tips('请输入阈值范围在0.8~0.9', $("#checkThreshold"), {
				tipsMore: true,
				tips:3				
			});
    		return false;
    	}
    }else{
		$("#pushThreshold").val("");
		$("#checkThreshold").val("");
    }
    return true;
}
var isSubmit=false;
function saveNoticeSet(){
	if(isSubmit){
        return;
    }
   	if(!checkFaceType()){
   		isSubmit = false;
   		return;
   	}
    isSubmit = true;
	var options = {
		url : '${request.contextPath}/eclasscard/attence/notice/save',
		dataType : 'json',
		success : function(data){
	 		if(data.success){
				layer.msg("保存成功");
				showList('2');
	 		}
	 		else{
	 			layerTipMsg(data.success,"保存失败",data.msg);
	 			isSubmit=false;
			}
		},
		clearForm : false,
		resetForm : false,
		type : 'post',
		error:function(XMLHttpRequest, textStatus, errorThrown){} 
	};
	$("#noticeSetForm").ajaxSubmit(options);
}

</script>