<meta http-equiv="Content-Type" content="text/html;charset=utf-8"/>
<script type="text/javascript" charset="utf-8" src="${request.contextPath}/static/ueditor/ueditor.config.js"></script>
<script type="text/javascript" charset="utf-8" src="${request.contextPath}/static/ueditor/ueditor.all.min.js"> </script>
<script type="text/javascript" charset="utf-8" src="${request.contextPath}/static/ueditor/lang/zh-cn/zh-cn.js"></script>
<link rel="stylesheet" type="text/css" href="${request.contextPath}/static/ueditor/themes/default/css/ueditor.css"/>
<link rel="stylesheet" type="text/css" href="${request.contextPath}/static/ueditor/themes/iframe.css"/>
<script>
<#if !show>
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
    var isSubmit=false;
    function saveNotice(){
    	var notice = UE.getEditor('content').getContent();
    	if(isSubmit){
			return;
		}
		isSubmit=true;
		 var ii = layer.load();
	    $.ajax({
	        url:'${request.contextPath}/gkelective/${arrangeId}/goClass/saveNotice/save',
	        data: {'arrangeId':'${arrangeId}',"notice":notice},
	        type:'post',  
	        success:function(data) {
	            var jsonO = JSON.parse(data);
	            if(jsonO.success){
	                layer.closeAll();
					layerTipMsg(jsonO.success,"保存成功",jsonO.msg);
	                editNotice('1');
	            }
	            else{
					layerTipMsg(jsonO.success,"保存失败",jsonO.msg);
	                isSubmit=false;
	            }
	            layer.close(ii);
	         }
	    });
    }
    function editNotice(show){
    	if(!show){
    		show = '0';
    	}
    	var url =  '${request.contextPath}/gkelective/${arrangeId}/doGoElective/edit/page?show='+show;
		$("#itemShowDivId").load(url);
    }
</script>
<#if show>
<div class="box box-default">
	<div class="box-header">
		<h4 class="box-title">设置通告</h4>
	</div>
	<div class="box-body">
		<div>
			<p>${arrangeDto.gkSubjectArrange.notice!}</p>
		</div>
		<div class="text-right" style="margin-top:10px">
			<a href="javascript:void(0);" class="btn btn-blue" onclick="editNotice('0');">修改</a>
			<a href="javascript:void(0);" class="btn btn-blue" onclick="itemShowList();">返回</a>
		</div>
	</div>
</div>
<#else>
<div class="box box-default">
	<div class="box-header">
		<h4 class="box-title">设置通告</h4>
	</div>
	<div class="box-body">
		<div>
			<textarea id="content" name="content" type="text/plain" style="width:100%;height:500px;">${arrangeDto.gkSubjectArrange.notice!}</textarea>
		</div>
		<div class="text-right" style="margin-top:10px">
			<a href="javascript:void(0);" class="btn btn-blue" onclick="saveNotice();">保存</a>
			<a href="javascript:void(0);" class="btn btn-blue" onclick="<#if arrangeDto.gkSubjectArrange.notice?default('') == ''>itemShowList();<#else>editNotice('1');</#if>">返回</a>
		</div>
	</div>
</div>
<#--
<div class="sendBulletin-form">
<div class="row">
	<div class="col-sm-12">
		<div class="choose-explain">
			<h5>选课说明</h5>
				<textarea id="content" name="content" type="text/plain" style="width:1024px;height:500px;">${arrangeDto.gkSubjectArrange.notice!}</textarea>
		</div>
	</div>
</div>
</div>
<a href="javascript:void(0);" class="btn btn-blue" onclick="saveNotice();">保存</a>
<a href="javascript:void(0);" class="btn btn-blue" onclick="editNotice('1');">返回</a>
-->
</#if>
