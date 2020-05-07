<script type="text/javascript" src="${request.contextPath}/bigdata/v3/static/plugs/wangEditor/wangEditor.min.js"></script>
<style>
.w-e-text-container{
    height: 600px !important;/*!important是重点，因为原div是行内样式设置的高度300px*/
}
</style>
<form id="noticeSubmitForm">
<input type="hidden" name="id"  id="id" value="${notice.id!}">
<input type="hidden" name="status" value="${notice.status!}">
<input type="hidden" name="content" id="content">
<div class="">
	<div class="form-horizontal form-made form-made-horizontal">
		<div class="form-group">
			<label class="col-sm-2 control-label"><font style="color:red;">*</font>公告名称：</label>
			<div class="col-sm-8">
				<input type="text" class="form-control js-file-name" placeholder="请输入公告名称"  name="title" id="title" maxLength="50" nullable="false" value="${notice.title!}">
			</div>
		</div>
		<div class="form-group">
			<label class="col-sm-2 control-label"><font style="color:red;">*</font>类型：</label>
			<div class="col-sm-8">
				<select name="noticeType" id="noticeType" class="form-control" >
	                  <option value="1"  <#if notice.noticeType! == 1>selected="selected"</#if>>发布</option>
		              <option value="2" <#if notice.noticeType! == 2>selected="selected"</#if>>升级</option>
		              <option value="3" <#if notice.noticeType! == 3>selected="selected"</#if>>安全</option>
		              <option value="4" <#if notice.noticeType! == 4>selected="selected"</#if>>新功能</option>
		              <option value="9" <#if notice.noticeType! == 9>selected="selected"</#if>>其他</option>
				</select>
			</div>
		</div>
		<div class="form-group">
			<label class="col-sm-2 control-label">内容：</label>
			<div class="col-sm-8">
				<div class="editor " id="notice-richTextContentDiv"></div>
			</div>
		</div>
		<div class="form-group">
	        <label class="col-sm-2 control-label no-padding" for="form-field-1"></label>
	        <div class="col-sm-8">
	            <button type="button" class="btn btn-long btn-blue" id="saveTempBtn" onclick="saveTempNotice();">&nbsp;保存&nbsp;</button>
	            <button type="button" class="btn btn-long btn-blue" id="saveBtn" onclick="saveNotice();">&nbsp;保存&返回&nbsp;</button>
	        </div>
	        </div>
	    </div>
	</div>
</div>	
<div id="hiddenContentDiv" style="display:none">${notice.content!}</div>	
</form>
<script type="text/javascript">
	var isSubmit=false;
 	var E = window.wangEditor;
	var  richTextEditor;
	
	$(document).ready(function(){
	    richTextEditor = new E('#notice-richTextContentDiv') ;
	    // 自定义处理粘贴的文本内容
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
	    richTextEditor.customConfig.uploadImgShowBase64 = true;
		richTextEditor.customConfig.uploadImgMaxSize = 100 * 1024 * 1024;
		richTextEditor.create();
 		richTextEditor.txt.html($('#hiddenContentDiv').html());
	});
	
	function saveNotice(){
		if(isSubmit){
			return;
		}
		isSubmit = true;
		var check = checkValue('#noticeSubmitForm');
		if(!check){
		 	isSubmit=false;
		 	return;
		}
		$('#content').val(richTextEditor.txt.html());
	 	var options = {
			url:"${request.contextPath}/bigdata/notice/save",
			dataType : 'json',
			success : function(data){
				layer.closeAll();
	 			if(!data.success){
	 				showLayerTips4Confirm('error',data.message);
	 				isSubmit = false;
	 			}else{
	 				showLayerTips('success',data.message,'t');
	 				router.go({
				        path: '/bigdata/notice/index'
				    });
				}
			},
			clearForm : false,
			resetForm : false,
			type : 'post',
			error:function(XMLHttpRequest, textStatus, errorThrown){}//请求出错 
		};
		$("#noticeSubmitForm").ajaxSubmit(options);   
	}

	function saveTempNotice(){
		if(isSubmit){
			return;
		}
		isSubmit = true;
		var check = checkValue('#noticeSubmitForm');
		if(!check){
		 	isSubmit=false;
		 	return;
		}
		$('#content').val(richTextEditor.txt.html());
	 	var options = {
			url:"${request.contextPath}/bigdata/notice/saveWithNoFresh",
			dataType : 'json',
			success : function(data){
				layer.closeAll();
	 			if(!data.success){
	 				showLayerTips4Confirm('error',data.message);
	 			}else{
	 				showLayerTips('success','保存成功','t');
	 				$('#id').val(data.message);
				}
				isSubmit = false;
			},
			clearForm : false,
			resetForm : false,
			type : 'post',
			error:function(XMLHttpRequest, textStatus, errorThrown){}//请求出错 
		};
		$("#noticeSubmitForm").ajaxSubmit(options);   
	}
</script>