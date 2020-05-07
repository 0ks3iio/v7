<meta http-equiv="Content-Type" content="text/html;charset=utf-8"/>
<#import "/eclasscard/common/back/groupSelection.ftl" as group />
<link rel="stylesheet" href="${request.contextPath}/static/components/chosen/chosen.min.css">
<link rel="stylesheet" href="${request.contextPath}/static/components/bootstrap-datetimepicker/css/bootstrap-datetimepicker.min.css">
<link rel="stylesheet" href="${request.contextPath}/static/css/components.css">
<script type="text/javascript" charset="utf-8" src="${request.contextPath}/static/ueditor/ueditor.config.js"></script>
<script type="text/javascript" charset="utf-8" src="${request.contextPath}/static/ueditor/ueditor.all.min.js"> </script>
<script type="text/javascript" charset="utf-8" src="${request.contextPath}/static/ueditor/lang/zh-cn/zh-cn.js"></script>
<link rel="stylesheet" type="text/css" href="${request.contextPath}/static/ueditor/themes/default/css/ueditor.css"/>
<link rel="stylesheet" type="text/css" href="${request.contextPath}/static/ueditor/themes/iframe.css"/>
<div class="box-body">
	<div class="form-horizontal">
<form id="bulletinForm">
<input type="hidden" id="" name="id" value="${eccBulletin.id!}" >
	<#if !isAll>
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
				<input type="hidden" id="" name="sendType" value=0 >
				<input type="hidden" id="eccInfoIds" name="eccInfoIds" value="${eccInfo.id!}" >
			</div>
		</div>
	<#else> 
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
				</#if>
			</#if>
			</div>
		</div>
	</#if> 
		<div class="form-group">
			<label class="col-sm-2 control-label no-padding-right">展示时间：</label>
			<div class="col-sm-4">
				<div class="input-group">
					<#if isEdit>
					<input type="text" name="beginTime" autocomplete="off" id="beginTimeVal" class="form-control datetimepicker" value="${eccBulletin.beginTime!}">
					<span class="input-group-addon">
						<i class="fa fa-minus"></i>
					</span>
					<input type="text" name="endTime" autocomplete="off" id="endTimeVal" class="form-control datetimepicker" value="${eccBulletin.endTime!}">
					<#else> 
					<label style="font-weight: normal;"><span class="lbl">${eccBulletin.beginTime!}至${eccBulletin.endTime!}</span></label>
					</#if>
				</div>
			</div>
		</div>
		<div class="form-group">
			<label class="col-sm-2 control-label no-padding-right">公告标题：</label>
			<div class="col-sm-4">
				<div class="input-group">
					<#if isEdit>
					<input type="text" id="titleVal" name="title" class="form-control js-limit-word" maxlength="20" value="${eccBulletin.title!}">
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
				<textarea id="content" name="content" type="text/plain" style="width:100%;height:360px;">${eccBulletin.content!}</textarea>
			<#else> 
					<label style="font-weight: normal;"><span class="lbl">${eccBulletin.content!}</span></label>
			</#if>
			</div>
		</div>
		</form>
		<div class="form-group">
			<#if isEdit>
			<div class="col-sm-8 col-sm-offset-2">
				<a class="btn btn-long btn-blue" onclick="saveBulletin()">发布</a>
				<a class="btn btn-long btn-white" onclick="backList()" >返回</a>
			</div>
			<#else> 
			<div class="col-sm-2 control-label no-padding-right">
				<a class="btn btn-long btn-blue" onclick="backList()" >返回</a>
			</div>
			</#if>
		</div>
	</div>
</div>
</form>
<script>
<#if isEdit>
//实例化编辑器
//建议使用工厂方法getEditor创建和引用编辑器实例，如果在某个闭包下引用该编辑器，直接调用UE.getEditor('editor')就能拿到相关的实例
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
         'imagenone', 'imageleft', 'imageright', 'imagecenter', '|',
         'horizontal', 'date', 'time', '|',
         'inserttable', 'deletetable', 'insertparagraphbeforetable', 'insertrow', 'deleterow', 'insertcol', 'deletecol', 'mergecells', 'mergeright', 'mergedown', 'splittocells', 'splittorows', 'splittocols', 'charts'
     	]],
    initialFrameHeight:300
    //更多其他参数，请参考ueditor.config.js中的配置项
});
</#if>
$(function(){
	<#--返回-->
	showBreadBack(backList,true,"通知公告");
	var minDate = "${((.now)?string('yyyy-MM-dd HH:mm'))?if_exists}";
	$('.datetimepicker').datetimepicker({
		startDate:minDate,
		language: 'zh-CN',
    	format: 'yyyy-mm-dd hh:ii',
    	autoclose: true
    })
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
    	layerTipMsgWarn("自定义对象","不可为空");
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
    if(!title||title==''){
    	layerTipMsgWarn("标题","不可为空");
		return;
    }else{
    	if(title.length>20){
    	layerTipMsgWarn("标题","长度不可超过20字");
		return;
    	}
    }
    var notice = UE.getEditor('content').getContent();
    if(!notice||notice==''){
    	layerTipMsgWarn("公告正文","不可为空");
		return;
    }
	isSubmit = true;
	var options = {
			url : "${request.contextPath}/eclasscard/bulletin/save",
			dataType : 'json',
			success : function(data){
		 		if(!data.success){
		 			layerTipMsg(data.success,"保存失败",data.msg);
		 			isSubmit = false;
		 		}else{
					backList();
    			}
			},
			clearForm : false,
			resetForm : false,
			type : 'post',
			error:function(XMLHttpRequest, textStatus, errorThrown){}//请求出错 
		};
		$("#bulletinForm").ajaxSubmit(options);
}

function backList(){
	hidenBreadBack();
	<#if backAll>
	bulletinList();
	<#else> 
	var url = '${request.contextPath}/eclasscard/bulletin/list?eccInfoId=${eccInfo.id!}';
	$("#tabContent").load(url);
	setTimeout(function(){
		showBreadBack(myEclasscardList,true,"我的班牌");
	},100); 
	</#if>
}
</script>
