<meta http-equiv="Content-Type" content="text/html;charset=utf-8"/>
<#import "/fw/macro/popupMacro.ftl" as popup />
<div class="box box-default">
	<div class="box-body">
		<div class="tab-content">
			<div id="aa" class="tab-pane active" role="tabpanel">
				<div class="form-horizontal">
					<form id="bulletinForm">
					<input type="hidden" id="" name="id" value="${eccBulletin.id!}" >
					<input type="hidden" name="bulletinLevel" value="2" >
					<div class="form-group">
						<label class="col-sm-2 control-title no-padding-right">发布公告&nbsp;</label>
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
							<input type="hidden" id="eccInfoIds" name="eccInfoIds" value="${eccInfo.id!}" >
						</div>
					</div>
					<div class="form-group" id="bulletinType">
						<label class="col-sm-2 control-label no-padding-right">公告类型：</label>
						<div class="col-sm-8">
							<#if isEdit>
								<#if !eccBulletin.type?exists>
								<label><input type="radio" name="type" value="1" class="wp" checked><span class="lbl">普通公告</span></label>
								<label><input type="radio" name="type" value="2" class="wp topBar"><span class="lbl">顶栏公告</span></label>
								<#elseif eccBulletin.type==1>
								<input type="hidden" name="type" value="1">
								<label style="font-weight: normal;"><span class="lbl"> 普通公告</span></label>
								<#else>
								<input type="hidden" name="type" value="2">
								<label style="font-weight: normal;"><span class="lbl"> 顶栏公告</span></label>
								</#if>
							<#else>
							<label style="font-weight: normal;"><span class="lbl">
							<#if eccBulletin.type?exists && eccBulletin.type == 1>普通公告<#else>顶栏公告</#if>
							</span></label>
							</#if>
						</div>
					</div>
					<div class="form-group">
						<label class="col-sm-2 control-label no-padding-right">班牌展示时间：</label>
						<div class="col-sm-4">
							<div class="input-group">
								<#if isEdit>
								<input type="text" name="beginTime" id="beginTimeVal" autocomplete="off" class="form-control datetimepicker" placeholder="开始时间" value="${eccBulletin.beginTime!}">
								<span class="input-group-addon">
									<i class="fa fa-minus"></i>
								</span>
								<input type="text" name="endTime" id="endTimeVal" autocomplete="off" class="form-control datetimepicker" placeholder="结束时间" value="${eccBulletin.endTime!}">
								<#else> 
								<label style="font-weight: normal;"><span class="lbl">${eccBulletin.beginTime!}至${eccBulletin.endTime!}</span></label>
								</#if>
							</div>
						</div>
					</div>
					<div class="form-group <#if eccBulletin.type?exists && eccBulletin.type==2>hidden</#if>" id="announcementTitle">
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
							
							<#if !eccBulletin.type?exists>
								<textarea id="content" type="text/plain" style="width:100%;height:360px;">${eccBulletin.content!}</textarea>
								<textarea id="content1" class="hidden" type="text/plain" style="width:100%;height:360px;">${eccBulletin.content!}</textarea>
							<#else>
								<textarea id="content" type="text/plain" style="width:100%;height:360px;">${eccBulletin.content!}</textarea>
							</#if>
							
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
<#if !eccBulletin.type?exists || (eccBulletin.type?exists && eccBulletin.type == 1)>
//实例化编辑器
//建议使用工厂方法getEditor创建和引用编辑器实例，如果在某个闭包下引用该编辑器，直接调用UE.getEditor('editor')就能拿到相关的实例
UE.delEditor('content'); 
var ue = UE.getEditor('content',{
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
</#if>
</#if>
function gobacktoList(){
	backFolderIndex('3');
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
	dateInit();
    
    $('#bulletinType input[type=radio]').on('change', function(){
    	$('#titleVal').val("");
    	$('#titleVal').next().text(0 +'/'+20);
		if ($('.topBar').prop('checked') === true) {
			$("#announcementTitle").addClass('hidden');
			UE.getEditor('content').setHide();
			$("#content1").removeClass('hidden');
			$("#content1").val("");
		} else {
			$("#announcementTitle").removeClass('hidden');
			UE.getEditor('content').setShow();
			$("#content1").addClass('hidden');
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
    	if ($('.topBar').prop('checked') === true) {
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
    		notice = UE.getEditor('content').getContent();
    		if(!notice||notice==''){
    			layerTipMsgWarn("公告正文","不可为空");
				return;
    		}
    	}
    <#elseif eccBulletin.type == 1>
    	notice = UE.getEditor('content').getContent();
    	if(!notice||notice==''){
    		layerTipMsgWarn("公告正文","不可为空");
			return;
    	}
    <#else>
    	notice = $("#content").val();
    	if(!notice||notice==''){
    		layerTipMsgWarn("公告正文","不可为空");
			return;
    	}
    	if (notice.length>100) {
    		layerTipMsgWarn("公告正文","内容不能超过100个字");
			return;
    	}
    	
    </#if>
    
    
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
					gobacktoList();
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