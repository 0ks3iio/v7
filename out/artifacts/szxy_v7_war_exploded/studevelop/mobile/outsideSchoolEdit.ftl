<#import "/fw/macro/mobilecommon.ftl" as common>
<#import "/studevelop/mobile/commonStuDevelop.ftl" as stuDevelop>
<#assign title="校外表现">
<#if item?exists && item.type?default(1) == 2>
	<#assign title="假期生活">
</#if>
<@common.moduleDiv titleName="${title!}">
<link href="${request.contextPath}/studevelop/mobile/css/style.css" rel="stylesheet"/>
<style type="text/css">
	.mui-input-row{
		background: #fff;
		border-top: 1px solid #dfdfdf;
		border-bottom: 1px solid #dfdfdf;
		margin-top: 10px;
	}
	.mui-input-row input[type='text']{
		border: 0;
		margin-bottom: 0;
	}
	.mui-input-row textarea{
		border: none !important;
		margin-bottom: 0 !important;
		padding: 10px 10px 0;
	}
	.mui-input-user{
		display: block;
		height: 64px;
		line-height: 64px;
		font-size: 15px;
		color: #333;
	}
</style>

<#--<#if id?default('') == ''>-->

	<#--<@stuDevelop.acadyearSemester stuDevelopUrl="${request.contextPath}/mobile/open/studevelop/outside/index?" params="type=${type!}&studentId=${studentId!}" >-->

	<#--</@stuDevelop.acadyearSemester>-->

<#--</#if>-->

<form id="form" method="post" enctype="multipart/form-data" >
	<input type="hidden" name="id" value="${item.id!}" />
	<input type="hidden" name="studentId" value="${item.studentId!}" />
	<input type="hidden" name="acadyear" value="${item.acadyear!}" />
	<input type="hidden" name="semester" value="${item.semester!}" />
	<input type="hidden" name="type" value="${item.type!}" />
	<input type="hidden" id="delImgIds" name="delImgIds" value="" />

	<div class="mui-input-row mui-clearfix">
		<input type="text" class="mui-input-clear" id="title" name="title" value="${item.title!}" placeholder="请输入主题...(必填)" maxlength="50"  />
	</div>
	<div class="mui-input-row">
		<textarea class="mui-input-clear" id="content" name="content" rows="6" placeholder="请输入详情...(必填)" maxlength="2000">${item.content!}</textarea>
	</div>
	<div class="mui-input-row pa-10">
		<p class="f-15 c-333">照片</p>
		<ul class="mui-photo-add mui-clearfix" id="fileDiv">
			<#if item?exists && item.images?exists && (item.images?size>0)>
				<#list item.images as img>
					<li>
						<a href="#">
							<img src="${fileUrl!}${img.filePath!}" id="${img.id!}" data-preview-src="${img.path!}" data-preview-group="1"  />
						</a>
					</li>
				</#list>
			</#if>
		</ul>
	</div>
	<div id="delSheet" class="mui-popover mui-popover-bottom mui-popover-action">
		<input type="hidden" id="delImgId" value=""/>
		<!-- 可选择菜单 -->
		<ul class="mui-table-view">
		  <li class="mui-table-view-cell">
			<a href="#" class="del-img">删除图片</a>
		  </li>
		</ul>
		<!-- 取消菜单 -->
		<ul class="mui-table-view">
		  <li class="mui-table-view-cell">
			<a href="#sheet1"><b>取消</b></a>
		  </li>
		</ul>
	</div>
</form>
<div class="mui-input-opt">
	<button id="save" type="button" class="mui-btn mui-btn-block mui-btn-green" onclick="doSave();">保存</button>
</div>
<script type="text/javascript" charset="utf-8">
  	mui.init({
  		gestureConfig:{
			doubletap: true,
			longtap: true
		}	
  	});
  	mui.previewImage();
  	mui('.mui-photo-add').on('longtap', 'img', function() {
  		//长按弹出删除框
  		var id = $(this).attr("id");
  		mui('#delSheet').popover('toggle');
  		$('#delImgId').val(id);
		return false;
	});
	mui('body').on('tap', '.mui-popover-action li>a', function() {
		if($(this).hasClass('del-img')){
			//移除
			var delImgId = $('#delImgId').val();
			$("#"+delImgId).parents('li').remove();
			<#if item.id?exists && ""!=item.id>
				//修改信息时才需要后台删除id
				var delImgIds = $("#delImgIds").val();
				if(isNotBlank(delImgIds)){
					delImgIds += "," + delImgId;
				}else{
					delImgIds = delImgId;
				}
				$("#delImgIds").val(delImgIds);
			</#if>
		};
		mui('#delSheet').popover('toggle');
		$("#delImgId").val('');
	});
</script>
<script type="text/javascript">
   imgRatio2('.mui-photo-add', 1);
   setInterval(function(){
   		imgRatio2('.mui-photo-add', 1);
   },500);
   setTimeout(function(){
   		imgRatio2('.mui-photo-add', 1);
   },1000);
</script>
<script type="text/javascript">
	var _index = 0;
//追加拍照
	function addFileElem(){
		_index++;
		var html = '<li>';
			html += '<a href="javascript:void(0);" class="add" id="file_a_'+_index+'">';
	    	html +=		'<div style="display:none;"><input type="file" id="file_input_'+_index+'" name="file_name_'+_index+'" accept="image/*" ></div>';
	    	html +=		'<img src="" id="file_img_'+_index+'"  data-preview-group="1" style="display:none;"/>';
	    	html +=		'<span id="file_span_'+_index+'" class="mui-icon mui-icon-plusempty"></span>';
	    	html +=	'</a>';
			html += '</li>';
		$("#fileDiv").append(html);
		
		mui('#fileDiv').on('tap',"#file_a_"+_index, function(){
			$("#file_input_"+_index).click();
		});
		
		$("#file_input_"+_index).on('change',function(){
			var file = $(this).get(0).files[0];
			$("#file_span_"+_index).hide();
			$("#file_img_"+_index).show();
			//预览属性
			$("#file_img_"+_index).attr("data-preview-src","");
			mui('#fileDiv').off('tap',"#file_a_"+_index);
			previewFile(file, "#file_img_"+_index, function(){
				//追加
				addFileElem();
				
				//$("#file_a_"+_index).removeClass("add");
				
				//取消点击事件
				//处理图片
				imgRatio2('.mui-photo-add',1);
				myImgRatio();
			});
		});
		
		
	}	
	//初始化
	addFileElem();

//初始化图片
	function myImgRatio(){
		//$("img").each(function(e){
		//	var height = $(this).parent("a").height();
		//	$(this).height(height);
		//});	
	}
	
	myImgRatio();
		
//保存	
	function doSave(){
		
		if(!isActionable("#save")){
			return;
		}
		
		var title = $("#title").val();
		var content = $("#content").val();
		if(isNotBlank(title)){
			var len = getLength(title);
			if(len > 50){
				alertMsg("主题不能超过50个字符");
				return;
			}
		}else{
			alertMsg("主题不能为空");
			return;
		}
		
		if(isNotBlank(content)){
			var len = getLength(content);
			if(len > 2000){
				alertMsg("详情不能超过2000个字符");
				return;
			}
		}else{
			alertMsg("详情不能为空");
			return;
		}
		
		setDisabled("#save");//设置按钮不可用
		mui.toast('正在保存',{
			duration: 3500,
			offset: 'center'
		});
		ajaxSubmit("${request.contextPath}/mobile/open/studevelop/outside/save", "#form", function(data){
			setDefault("#save");
			if(data.success == true){
				toastMsg(data.msg);
				back();
			}else{
				toastMsg(data.msg);
			}
			
		});
	}
	
	function back(){
		storage.set("backType","outside_edit");
		history.go(-1);
		//var str = "?studentId=${item.studentId!}&acadyear=${item.acadyear!}&semester=${item.semester!}&type=${item.type?default(1)}";
		//load("${request.contextPath}/mobile/open/studevelop/outside/index"+str);
	}
</script>
</@common.moduleDiv>