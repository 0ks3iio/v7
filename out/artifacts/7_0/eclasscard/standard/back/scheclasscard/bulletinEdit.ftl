<meta http-equiv="Content-Type" content="text/html;charset=utf-8"/>
<#import "/eclasscard/common/back/groupSelection.ftl" as group />

<div class="box box-default">
	<div class="box-body">
		<div class="tab-content">
			<div id="aa" class="tab-pane active" role="tabpanel">
				<div class="form-horizontal">
					<form id="bulletinForm">
					<input type="hidden" id="" name="id" value="${eccBulletin.id!}" >
					<input type="hidden" name="bulletinLevel" value="1" >
					<div class="form-group">
						<label class="col-sm-2 control-title no-padding-right">发布公告&nbsp;</label>
					</div>
					<div class="form-group">
						<label class="col-sm-2 control-label no-padding-right">发布对象：</label>
						<div class="col-sm-8">
						<#if isEdit >
						<@group.group_div dataUrl="${request.contextPath}/eclasscard/group/eccinfo/groupData" id="eccInfoIds">
							<input type="hidden" id="eccInfoIds" name="eccInfoIds"  class="form-control" value="${eccInfoIds!}">
						</@group.group_div>
						<#else>
							<#if showNames?exists&&showNames?size gt 0>
					        	<div class="class-card-labels">
						        <#list showNames as item>
						        	<span class="class-card-label"><span>${item[1]!}</span><br>${item[0]!}</span>
						        </#list>
								</div>
							<#else>
							<label style="font-weight: normal;"><span class="lbl">发布对象已不存在</span></label>
							</#if>
						</#if>
						</div>
					</div>
					<div class="form-group" id="bulletinType">
						<label class="col-sm-2 control-label no-padding-right">公告类型：</label>
						<div class="col-sm-8">
							<#if isEdit>
								<#if !eccBulletin.type?exists>
									<label><input type="radio" name="type" value="1" class="wp bar" checked><span class="lbl"> 普通公告</span></label>
									<label><input type="radio" name="type" value="2" class="wp topBar" ><span class="lbl"> 顶栏公告</span></label>
									<label><input type="radio" name="type" value="3" class="wp customAll" ><span class="lbl"> 全屏公告</span></label>
								<#elseif eccBulletin.type==1>
									<input type="hidden" name="type" value="1">
									<label style="font-weight: normal;"><span class="lbl">普通公告</span></label>
								<#elseif eccBulletin.type==2>
									<input type="hidden" name="type" value="2">
									<label style="font-weight: normal;"><span class="lbl">顶栏公告</span></label>
								<#else>
									<input type="hidden" name="type" value="3">
									<label style="font-weight: normal;"><span class="lbl">全屏公告</span></label>
								</#if>
							<#else>
							<label style="font-weight: normal;"><span class="lbl">
							<#if eccBulletin.type?exists && eccBulletin.type == 1>普通公告
							<#elseif eccBulletin.type?exists && eccBulletin.type == 2>顶栏公告
							<#else>全屏公告</#if>
							</span></label>
							</#if>
						</div>
					</div>
					<#if !isEdit && eccBulletin.type == 3>
					<div class="form-group">
						<label class="col-sm-2 control-label no-padding-right">选择模板：</label>
						<div class="col-sm-8">
						<label style="font-weight: normal;"><span class="lbl">
						<#if eccBulletin.templetType == 1>
						标准公告
						<#elseif eccBulletin.templetType == 2>
						喜报
						<#elseif eccBulletin.templetType == 3>
						欢迎致辞
						<#else>
						自定义
						</#if>
						</span></label>
						</div>
					</div>
					</#if>
					<div class="form-group <#if !isEdit || (!eccBulletin.type?exists) || (eccBulletin.type?exists && eccBulletin.type != 3)>hidden</#if>">
						<label class="col-sm-2 control-label no-padding-right">选择模板：</label>
						<div class="col-sm-8">
							<input type="hidden" name="templetType" id="templetType" value="${eccBulletin.templetType!}" >
							<ul class="post-list clearfix">
								<li <#if !eccBulletin.templetType?exists || (eccBulletin.templetType?exists && eccBulletin.templetType==1)>class="selected"</#if> name="announcement" id="announcement"  data-action="select">
									<div>
										<a href="javascript:void(0);"><img width="150" height="125" src="${request.contextPath}/static/eclasscard/standard/show/images/post-default.png" alt=""></a>
									</div>
									<p>标准公告</p>
								</li>
								<li <#if eccBulletin.templetType?exists && eccBulletin.templetType==2>class="selected"</#if> data-action="select" name="prosperity">
									<div>
										<a href="javascript:void(0);"><img width="150" height="125" src="${request.contextPath}/static/eclasscard/standard/show/images/post-happy.png" alt=""></a>
									</div>
									<p>喜报</p>
								</li>
								<li <#if eccBulletin.templetType?exists && eccBulletin.templetType==3>class="selected"</#if> data-action="select" name="speech">
									<div>
										<a href="javascript:void(0);"><img width="150" height="125" src="${request.contextPath}/static/eclasscard/standard/show/images/post-welcome.png" alt=""></a>
									</div>
									<p>欢迎致辞</p>
								</li>
								<li id="userDefined" <#if eccBulletin.templetType?exists && eccBulletin.templetType==9>class="selected"</#if> data-action="select" name="userDefined">
									<div>
										<a href="javascript:void(0);"><div class="border-1-e8e8e8 js-color-show" <#if eccBulletin.grounding?exists>style="background-color:${eccBulletin.grounding!}"</#if>></div></a>
									</div>
									<p>自定义</p>
								</li>
							</ul>
						</div>
					</div>
					<#if !isEdit && eccBulletin.type == 3 && eccBulletin.templetType == 9>
						<div class="form-group">
							<label class="col-sm-2 control-label no-padding-right">背景颜色：</label>
							<div class="col-sm-8">
							<div  style="height: 36px; width:100px;border:1px solid #898989;<#if eccBulletin.grounding?exists>background-color:${eccBulletin.grounding!}</#if>"></div>
							</div>
						</div>
					</#if>
					<#if isEdit>
					<div id="custom-color-div" class="form-group <#if eccBulletin.templetType?exists && eccBulletin.templetType==9><#else>hidden</#if>">
						<label class="col-sm-2 control-label no-padding-right">选择背景颜色：</label>
						<div class="col-sm-4 position-relative">
							<input id="custom-color" name="grounding" class="form-control js-color" type="text"  <#if eccBulletin.grounding?exists>style="background-color:${eccBulletin.grounding!}" value="${eccBulletin.grounding!}"</#if>>
						</div>
					</div>
					</#if>
					<div class="form-group <#if (!eccBulletin.type?exists) || (eccBulletin.type?exists && eccBulletin.type != 3)>hidden</#if>">
						<label class="col-sm-2 control-label no-padding-right">是否锁定：</label>
						<div class="col-sm-9">
							<label>
								<#if isEdit>
								<input id="lockScreen" name="lockScreen" class="wp wp-switch js-openNotice" type="checkbox">
								<span class="lbl"></span>
								<#else> 
									<p><#if eccBulletin.lockScreen>是<#else> 否</#if></p>
								</#if>
							</label>
							<#if isEdit><span class="hint-text">锁定开启后，班牌界面展示时将全屏锁定，班牌端无法进行其它操作</span></#if>
						</div>
					</div>
					<div class="form-group">
						<label class="col-sm-2 control-label no-padding-right">班牌展示时间：</label>
						<div class="col-sm-4">
							<div class="input-group">
								<#if isEdit>
								<input type="text" class="form-control datetimepicker" autocomplete="off" placeholder="开始时间" name="beginTime" id="beginTimeVal" value="${eccBulletin.beginTime!}">
								<span class="input-group-addon">
									<i class="fa fa-minus"></i>
								</span>
								<input type="text" class="form-control datetimepicker" autocomplete="off" placeholder="结束时间" name="endTime" id="endTimeVal" value="${eccBulletin.endTime!}">
								<#else> 
								<label style="font-weight: normal;"><span class="lbl">${eccBulletin.beginTime!}至${eccBulletin.endTime!}</span></label>
								</#if>
							</div>
						</div>
					</div>
					
					<div class="form-group <#if eccBulletin.type?exists && (eccBulletin.type==2 || (eccBulletin.type==3 && (eccBulletin.templetType!=1)))>hidden</#if>" id="announcementTitle">
						<label class="col-sm-2 control-label no-padding-right">公告标题：</label>
						<div class="col-sm-4">
							<div class="input-group">
								<#if isEdit>
								<input type="text" id="titleVal" name="title" autocomplete="off" class="form-control js-limit-word" maxlength="20" value="${eccBulletin.title!}">
								<span class="input-group-addon">0/20</span>
								<#else> 
								<label style="font-weight: normal;"><span class="lbl">${eccBulletin.title!}</span></label>
								</#if>
							</div>
						</div>
					</div>
					<div class="form-group">
						<label class="col-sm-2 control-label no-padding-right">公告正文：</label>
						<div class="col-sm-8">
							<#if isEdit>
								<div id="contentdiv" class="hidden">
									<textarea id="content"  type="text/plain" style="width:100%;height:360px;">${eccBulletin.content!}</textarea>
								</div>
								<textarea id="content1" class="hidden" type="text/plain" style="width:100%;height:360px;">${eccBulletin.content!}</textarea>
							<#else> 
							<label style="font-weight: normal;"><span class="lbl">${eccBulletin.content!}</span></label>
							</#if>
						</div>
					</div>
					</form>
					<div class="form-group">
						<#if isEdit>
						<div class="col-sm-8 col-sm-offset-2">
							<button class="btn btn-long btn-blue" onclick="saveBulletin()">发布</button>
							<button class="btn btn-long btn-white" onclick="gobacktoList()">取消</button>
						</div>
						<#else> 
						<div class="col-sm-2 control-label no-padding-right">
							<a class="btn btn-long btn-blue" onclick="gobacktoList()" >返回</a>
						</div>
						</#if>
					</div>
				</div>
			</div>
		</div>
	</div>
</div>
<script>
<#if isEdit>
//实例化编辑器
//建议使用工厂方法getEditor创建和引用编辑器实例，如果在某个闭包下引用该编辑器，直接调用UE.getEditor('editor')就能拿到相关的实例

UE.delEditor('content'); 
ue = UE.getEditor('content',{
    //focus时自动清空初始化时的内容
    autoClearinitialContent:false,
    //关闭字数统计
    wordCount:false,
    //关闭elementPath
    elementPathEnabled:false,
    //默认的编辑区域高度
    toolbars:[[
         'fullscreen', 'source', '|', 'undo', 'redo', '|',
         'bold', 'italic', 'underline', 'fontborder', 'strikethrough', 'superscript', 'subscript', 'removeformat', 'formatmatch', 'autotypeset', 'blockquote', 'pasteplain', '|', 'forecolor', 'backcolor', 'insertorderedlist', 'insertunorderedlist', 'selectall', 'cleardoc', '|',
         'rowspacingtop', 'rowspacingbottom', 'lineheight', '|',
         'customstyle', 'paragraph', 'fontfamily', 'fontsize', '|',
         'directionalityltr', 'directionalityrtl', 'indent', '|',
         'justifyleft', 'justifycenter', 'justifyright', 'justifyjustify', '|', 'touppercase', 'tolowercase', '|',
         'simpleupload','imagenone', 'imageleft', 'imageright', 'imagecenter', '|',
         'horizontal', 'date', 'time', '|',
         'inserttable', 'deletetable', 'insertparagraphbeforetable', 'insertrow', 'deleterow', 'insertcol', 'deletecol', 'mergecells', 'mergeright', 'mergedown', 'splittocells', 'splittorows', 'splittocols', 'charts'
     	]],
    initialFrameHeight:300
    //更多其他参数，请参考ueditor.config.js中的配置项
});
<#if eccBulletin.type?exists && (eccBulletin.type==2 || (eccBulletin.type==3 && (eccBulletin.templetType!=9)))>
	$("#content1").removeClass('hidden');	
<#else> 
	$("#contentdiv").removeClass('hidden');	
</#if>
</#if>
function gobacktoList(){
	backFolderIndex('1');
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
		<#--返回-->
		showBreadBack(gobacktoList,true,"公告列表");
	},100);
}
$(function(){
	<#if eccBulletin.lockScreen>
		$('#lockScreen').click();
	</#if>
	dateInit();
	//颜色
	$('.js-color-show').each(function(){
		$(this).css({
			width: $(this).parents('li').prev('li').find('img').width(),
			height: $(this).parents('li').prev('li').find('img').height()
		})
	});
	$('#custom-color').iColor();
	$('body').on('click','#iColorPicker',function(){
		$('.js-color-show').css('background-color',$('#custom-color').attr('hx'))
		$('#custom-color').val($('#custom-color').attr('hx'));
	});
				
    $('#bulletinType input[type=radio]').on('change', function(){
    	if ($('.bar').prop('checked') === true) {
    		//UE.getEditor('content').setShow();
    		$("#contentdiv").removeClass('hidden');	
			$("#content1").addClass('hidden');		
    	} else {
    		//UE.getEditor('content').setHide();
    		$("#contentdiv").addClass('hidden');	
			$("#content1").removeClass('hidden');
			$("#content1").val("");
    	}
		if($('.customAll').prop('checked') === true){
			$(this).closest('.form-group').next().removeClass('hidden').next().next().removeClass('hidden');
			if($("#userDefined").hasClass('selected')){
				$("#custom-color-div").removeClass('hidden');
				//UE.getEditor('content').setShow();
				$("#contentdiv").removeClass('hidden');	
				$("#content1").addClass('hidden');	
			}
		}else{
			$(this).closest('.form-group').next().addClass('hidden').next().next().addClass('hidden');
			$("#custom-color-div").addClass('hidden');
		}
		if ($('.topBar').prop('checked') === true) {
			$("#announcementTitle").addClass('hidden');	
			$("#titleVal").val('');
			$('#titleVal').next().text(0 +'/'+20);
		} else if($('.customAll').prop('checked') === true && !$("#announcement").hasClass('selected')){
			$("#announcementTitle").addClass('hidden');
		}else{
			$("#announcementTitle").removeClass('hidden');
		}
	});
    
    
	$('[data-action=select]').on('click',function(){
		if($(this).hasClass('disabled')){
			return false;
		}else{
			$(this).addClass('selected').siblings().removeClass('selected');
			$("#contentdiv").addClass('hidden');
			$("#content1").removeClass('hidden');
			$("#content1").val("");
			if ($(this).attr('name') === 'announcement') {
				$("#announcementTitle").removeClass('hidden');
				$("#templetType").val(1);
				$("#custom-color-div").addClass('hidden');
			}else if ($(this).attr('name') === 'prosperity') {
				$("#announcementTitle").addClass('hidden');
				$("#titleVal").val('');
				$('#titleVal').next().text(0 +'/'+20);
				$("#templetType").val(2);
				$("#custom-color-div").addClass('hidden');
			}else if ($(this).attr('name') === 'speech') {
				$("#announcementTitle").addClass('hidden');
				$("#titleVal").val('');
				$('#titleVal').next().text(0 +'/'+20);
				$("#templetType").val(3);
				$("#custom-color-div").addClass('hidden');
			} else {
				$("#announcementTitle").addClass('hidden');
				$("#titleVal").val('');
				$("#templetType").val(9);
				$("#contentdiv").removeClass('hidden');	
				$("#content1").addClass('hidden');	
				$("#custom-color-div").removeClass('hidden');	
			}
		}
	});
	
	<#if isEdit>
	var titleLength = $('#titleVal').val().length;
	$('#titleVal').next().text(titleLength +'/'+20);
	$('.js-limit-word').on('keyup',function(){
		var max = $(this).attr('maxlength');
		
		if(this.value.length+1 > max){
			layer.tips('最好在'+max+'个字数内哦', this, {
				tips: [1, '#000'],
				time: 2000
			})
		}
		$(this).next().text(this.value.length +'/'+max);
	})
	</#if>
});

var isSubmit=false;
function saveBulletin(){
	if(isSubmit){
        return;
    }
    var eccInfoIds = $("#eccInfoIds").val();
    if(!eccInfoIds||eccInfoIds==''){
		layerTipMsgWarn("","请选择发布对象");
		return;
	}
    var beginTime = $("#beginTimeVal").val();
    var endTime = $("#endTimeVal").val();
    if(!beginTime||beginTime==''||!endTime||endTime==''){
    	layerTipMsgWarn("展示时间","请选择完整的展示时间");
		return;
    }else{
	    if(beginTime>=endTime){
    	layerTipMsgWarn("展示时间","结束时间应大于开始时间");
		return;
	    }
    }
    
    var title = $("#titleVal").val();
    if (!$('#announcementTitle').hasClass('hidden')) {
    	if(!title||title==''){
    		layerTipMsgWarn("公告标题","不可为空");
			return;
    	}else{
    		if(title.length>20){
    			layerTipMsgWarn("公告标题","长度不可超过20字");
				return;
    		}
			var specialKey = "[`~!#$^&*()=|{}':;',\\[\\].<>/?~！#￥……&*（）——|{}【】‘；：”“'。，、？]‘’\"";
			for (var i = 0; i < title.length; i++) {
				if (specialKey.indexOf(title.charAt(i)) >= 0) {
					layerTipMsgWarn("公告标题","不能存在特殊字符！");
					return;
				}
			}
		}
    }
    
    var notice = "";
    <#if !eccBulletin.type?exists>
    	if ($('.bar').prop('checked') === true) {
    		notice = UE.getEditor('content').getContent();
    		if(!notice||notice==''){
    			layerTipMsgWarn("公告正文","不可为空");
				return;
    		}
    	} else if ($('.topBar').prop('checked') === true) {
    		notice = $("#content1").val(); 
    		if(!notice||notice==''){
    			layerTipMsgWarn("公告正文","不可为空");
				return;
    		}
    		if (notice.length>100) {
    			layerTipMsgWarn("公告正文","内容不能超过100个字");
				return;
    		}
    	} else {
    		if($("#templetType").val() == 9) {
	    		notice = UE.getEditor('content').getContent();
	    		if(!notice||notice==''){
	    			layerTipMsgWarn("公告正文","不可为空");
					return;
	    		}
    		}else{
	    		notice = $("#content1").val(); 
	    		if(!notice||notice==''){
	    			layerTipMsgWarn("公告正文","不可为空");
					return;
	    		}
	    		if ($("#templetType").val() == 1) {
	    			if (notice.length>100) {
	    				layerTipMsgWarn("公告正文","内容不能超过100个字");
						return;
	    			}
				
	    		} else {
	    			if (notice.length>30) {
	    				layerTipMsgWarn("公告正文","内容不能超过30个字");
						return;
	    			}
	    		}
    		}
    	}
    <#elseif eccBulletin.type == 1>
    	notice = UE.getEditor('content').getContent();
    	if(!notice||notice==''){
    		layerTipMsgWarn("公告正文","不可为空");
			return;
    	} 
    <#elseif eccBulletin.type == 2>
    	notice = $("#content").val(); 
    	if(!notice||notice==''){
    		layerTipMsgWarn("公告正文","不可为空");
			return;
    	}
    	if (notice.length>100) {
    		layerTipMsgWarn("公告正文","内容不能超过100个字");
			return;
    	}
    <#elseif eccBulletin.type == 3>
    	if($("#templetType").val() == 9) {
    		notice = UE.getEditor('content').getContent();
    		if(!notice||notice==''){
    			layerTipMsgWarn("公告正文","不可为空");
				return;
    		}
		}else{
	    	notice = $("#content").val(); 
	    	if(!notice||notice==''){
	    		layerTipMsgWarn("公告正文","不可为空");
				return;
	    	}
	    	if ($("#templetType").val() == 1) {
	    		if (notice.length>100) {
	    			layerTipMsgWarn("公告正文","内容不能超过100个字");
					return;
	    		}
	    	} else {
	    		if (notice.length>30) {
	    			layerTipMsgWarn("公告正文","内容不能超过30个字");
					return;
	    		}
	    	}
		}
    </#if>
    
    if($('.customAll').prop('checked') === false){
    	$("#templetType").val("");
    }
    
    
	isSubmit = true;
	var options = {
			url : "${request.contextPath}/eclasscard/standard/bulletin/save",
			data:{"notice":notice},
			dataType : 'json',
			success : function(data){
		 		if(!data.success){
		 			layerTipMsg(data.success,"保存失败",data.msg);
		 			isSubmit = false;
		 		}else{
					setTimeout(function(){
    					gobacktoList();
    				},500);
    			}
			},
			clearForm : false,
			resetForm : false,
			type : 'post',
			error:function(XMLHttpRequest, textStatus, errorThrown){}//请求出错 
		};
		$("#bulletinForm").ajaxSubmit(options);
}
</script>