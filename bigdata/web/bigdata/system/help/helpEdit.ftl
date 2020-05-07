<script type="text/javascript" src="${request.contextPath}/bigdata/v3/static/plugs/wangEditor/wangEditor.min.js"></script>
<style>
.w-e-text-container{
    height:400px !important;/*!important是重点，因为原div是行内样式设置的高度300px*/
}
</style>
<form id="helpSubmitForm">
<input type="hidden" name="id" id="id" value="${help.id!}">
<input type="hidden" name="content" id="content">
<input type="hidden" name="moduleName" id="moduleName" value="${help.moduleName!}">
<div class="">
	<div class="form-horizontal form-made form-made-horizontal">
		<div class="form-group">
			<label class="col-sm-2 control-label"><font style="color:red;">*</font>所属模块：</label>
			<div class="col-sm-8">
				<select name="moduleId" id="moduleId" class="form-control" nullable="false" onChange="moduleChange(this.value);">
					<option value="">请选择模块名称</option>
					 <#list moduleList as module>
						<#if module.type! !="dir">
                		<option value="${module.id!}" <#if help.moduleId! == module.id!>selected="selected"</#if> userType="${module.userType!}">${module.name!}</option>
                		</#if>
                	</#list>
				</select>
			</div>
		</div>
			<div class="form-group">
			<label class="col-sm-2 control-label"><font style="color:red;">*</font>帮助名称：</label>
			<div class="col-sm-8">
				<input type="text" class="form-control js-file-name" placeholder="请输入帮助名称"  name="name" id="name" maxLength="50" nullable="false" value="${help.name!}">
			</div>
		</div>
		<div class="form-group">
			<label class="col-sm-2 control-label"><font style="color:red;">*</font>是否核心模块：</label>
			<div class="col-sm-8">
				<select name="core" id="core" class="form-control">
	                  <option value="0"  <#if help.core! == 0>selected="selected"</#if>>否</option>
		              <option value="1" <#if help.core! == 1>selected="selected"</#if>>是</option>
				</select>
			</div>
		</div>
		<div class="form-group">
			<label class="col-sm-2 control-label"><font style="color:red;">*</font>排序号：</label>
			<div class="col-sm-8">
				<input type="text" name="orderId" id="orderId" class="form-control" nullable="false"
	                   onkeyup="if(this.value.length==1){this.value=this.value.replace(/[^1-9]/g,'')}else{this.value=this.value.replace(/\D/g,'')}"
	                   onafterpaste="if(this.value.length==1){this.value=this.value.replace(/[^1-9]/g,'')}else{this.value=this.value.replace(/\D/g,'')}"
	                   maxlength="3" value="${help.orderId?default(1)}">
			</div>
		</div>
		<div class="form-group">
			<label class="col-sm-2 control-label">描述：</label>
			<div class="col-sm-8">
				<textarea rows="2" class="form-control" name="description" id="description" maxlength="100" nullable="true">${help.description!}</textarea>
			</div>
		</div>
		<div class="form-group">
			<label class="col-sm-2 control-label">内容：</label>
			<div class="col-sm-8">
				<div class="editor" id="help-richTextContentDiv"></div>
			</div>
		</div>
		<div class="form-group">
	        <label class="col-sm-2 control-label no-padding" for="form-field-1"></label>
	        <div class="col-sm-8">
				<button type="button" class="btn btn-long btn-blue" id="saveTempBtn" onclick="saveTempHelp();">&nbsp;保存&nbsp;</button>
	            <button type="button" class="btn btn-long btn-blue" id="saveBtn" onclick="saveHelp();">&nbsp;保存&返回&nbsp;</button>
	        </div>
	    </div>
	</div>
</div>	
<div id="hiddenContentDiv" style="display:none">${help.content!}</div>	
</form>
<script type="text/javascript">
	var isSubmit=false;
 	var E = window.wangEditor;
	var  richTextEditor;
	
	$(document).ready(function(){
	    richTextEditor = new E('#help-richTextContentDiv') ;
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
 		
		var userType = $("#moduleId").find("option:selected").attr("userType");
		if(userType && userType.indexOf("1")<0){
			$('#core').attr("disabled","disabled")
		}
 		
	});

	function moduleChange(value){
		$("#moduleName").val($("#moduleId").find("option:selected").text());
		if($("#name").val() =="")
			$("#name").val($("#moduleId").find("option:selected").text());
		var userType = $("#moduleId").find("option:selected").attr("userType");
		if(userType && userType.indexOf("1")<0){
			$("#core").val("0");
			$('#core').attr("disabled","disabled")
		}else{
			$('#core').removeAttr("disabled")
		}
	}
	
	function saveHelp(){
		if(isSubmit){
			return;
		}
		isSubmit = true;
		var check = checkValue('#helpSubmitForm');
		if(!check){
		 	isSubmit=false;
		 	return;
		}
		$('#content').val(richTextEditor.txt.html());
	 	var options = {
			url:"${request.contextPath}/bigdata/help/save",
			dataType : 'json',
			success : function(data){
				layer.closeAll();
	 			if(!data.success){
	 				showLayerTips4Confirm('error',data.message);
	 				isSubmit = false;
	 			}else{
	 				showLayerTips('success',data.message,'t');
	 				router.go({
				        path: '/bigdata/help/index'
				    });
				}
			},
			clearForm : false,
			resetForm : false,
			type : 'post',
			error:function(XMLHttpRequest, textStatus, errorThrown){}//请求出错 
		};
		$("#helpSubmitForm").ajaxSubmit(options);   
	}

	function saveTempHelp(){
		if(isSubmit){
			return;
		}
		isSubmit = true;
		var check = checkValue('#helpSubmitForm');
		if(!check){
		 	isSubmit=false;
		 	return;
		}
		$('#content').val(richTextEditor.txt.html());
	 	var options = {
			url:"${request.contextPath}/bigdata/help/saveWithNoFresh",
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
		$("#helpSubmitForm").ajaxSubmit(options);   
	}

</script>