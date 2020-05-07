<div class="box box-default chart-part border normal-height">
	<#if preview! !="yes">
	<div class="box-set">
		<div class="pos-icon show">
			<i class="wpfont icon-paper bold js-save" onclick="saveRichText('${component.id!}');"></i>
			<i class="wpfont icon-close bold js-box-remove" onclick="deleteComponent('${component.id!}','${component.name!}');"></i>
		</div>
	</div>
	</#if>
	<section>
      	<div class="editor editor-made" id="richTextContentDiv-${component.id!}"></div>
  	</section>
</div>
<div id="hiddenContentDiv-${component.id!}" style="display:none">${component.content!}</div>	
<script type="text/javascript">
	
	$(document).ready(function(){
		var richTextEditor = new E('#richTextContentDiv-${component.id!}') ;
	    editorMap.set('${component.id!}',richTextEditor);
	    richTextEditor.customConfig.zIndex = ${zIndex?default(10000)};
	    <#if preview! !="yes">
		    richTextEditor.customConfig.menus =
		    [ 
			    'head',  // 标题
			    'bold',  // 粗体
			    'fontSize',  // 字号
			    'fontName',  // 字体
			    'italic',  // 斜体
			    'underline',  // 下划线
			    'strikeThrough',  // 删除线
			    'foreColor',  // 文字颜色
			    'backColor',  // 背景颜色
			   	'link',  // 插入链接
			    'list',  // 列表
			    'justify',  // 对齐方式
			    'quote',  // 引用
			    'emoticon',  // 表情
			    'code',  // 插入代码
			    'undo',  // 撤销
			    'redo'  // 重复
			];
		<#else>
 			richTextEditor.customConfig.menus =
		    [ ];
		</#if>
	    richTextEditor.customConfig.uploadImgShowBase64 = true;
		richTextEditor.customConfig.uploadImgMaxSize = 100 * 1024 * 1024;
		richTextEditor.create();
 		richTextEditor.txt.html($('#hiddenContentDiv-${component.id!}').html());
 		<#if preview! =="yes">
			$('.w-e-text').attr('contenteditable',false);
		</#if>
	});
		
	function saveRichText(componentId){
		var content =editorMap.get(componentId).txt.html();
		$.ajax({
	            url:"${request.contextPath}/bigdata/data/analyse/component/richText/update",
	            data:{
	              'componentId':componentId,
	              'content':content
	            },
	            type:"post",
	            clearForm : false,
				resetForm : false,
	            dataType: "json",
	            success:function(data){
	            	layer.closeAll();
			 		if(!data.success){
			 			showLayerTips4Confirm('error',data.message);
			 		}else{
			 		   showLayerTips('success',data.message,'t');
	    			}
	          },
	          error:function(XMLHttpRequest, textStatus, errorThrown){}
	    });
	}
</script>