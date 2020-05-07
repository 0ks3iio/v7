<#import "/fw/macro/webUploaderMacro.ftl" as upload />
<script type="text/javascript" src="${request.contextPath}/static/ueditor/third-party/webuploader/webuploader.min.js"></script>
<div class="box box-default">
	<div class="box-header">
		<h4 class="box-title">我的校园</h4>
	</div>
	<div class="box-body">
		<div class="form-horizontal">
		<form name="schoolForm" id="schoolForm" method="post">
			<input type="hidden" name="id" value="${act.id!}" />
			<input type="hidden" name="unitId" value="${act.unitId!}" />
			<input type="hidden" name="acadyear" value="${act.acadyear!}" />
			<input type="hidden" name="semester" value="${act.semester?default(1)}" />
			<input type="hidden" name="rangeId" value="${act.rangeId!}" />
			<input type="hidden" name="rangeType" value="${act.rangeType!}" />
			<input type="hidden" name="actType" value="${act.actType!}" />
			<input type="hidden" name="delAttIds" id="delAttIds" value="">
			<input type="hidden" name="newEn" value="${act.newEn?default(true)?string('true','false')}">
			<input type="hidden" name="hasUpload" id="hasUpload" value="${act.hasUpload?default(false)?string('true','false')}">
			<div class="form-group">
		    	<label class="col-sm-2 control-label">学校简介</label>
		    	<div class="col-sm-6">
		    		<div class="textarea-container">
		    			<textarea name="actRemark" id="actRemark" cols="30" rows="10" nullable="false"   notNull="true" maxLength="400" class="form-control">${act.actRemark!}</textarea>
		    			<span>200</span>
		    		</div>
		    	</div>
			</div>
			<div class="form-group">
				<label class="col-sm-2 control-label">学校风光</label>
				<div class="col-sm-10">
					<div class="upload-img-container clearfix" id="fileList">
						<#--<@upload.picUpload businessKey="${act.id!}" contextPath="${request.contextPath}" resourceUrl="${request.contextPath}/static" size="5" fileNumLimit="2" handler="">
						<div class="filter">
							<div class="filter-item">
								<button class="btn btn-blue js-addPhotos">上传图片</button>
							</div>
						</div>
						</@upload.picUpload>-->
						
						<#assign picSize=2 />
						<#if picList?exists && picList?size gt 0>
						<#assign picSize = 2-picList?size />
						<#list picList as ad>
						<div class="upload-img-item file-item" id="${ad.id!}_div"  style="width:130px;">
							<div class="file-img-wrap">
								<div class="file-panel">
				        			<a class="cancel" href="javascript:;" onclick="deleteFile('${ad.id!}')">删除</a>
				        		</div>
								<img height="130" width="130" layer-src="${request.contextPath}/studevelop/common/attachment/showPic?showOrigin=1&id=${ad.id!}" src="${request.contextPath}/studevelop/common/attachment/showPic?id=${ad.id!}" alt="">
							</div>
						</div>
						</#list>
						</#if>
						<#if picSize gt 0>
						<#list 1..picSize as picIndex>
						<div class="upload-img-item add-img-item">
							<label class="upload-img"><span></span></label>
							<#--
							div class="upload-img-state">
								<a href="javascript:;" class="upload-img-remove"><i class="fa fa-times-circle"></i></a>
								<img src="../images/icons/icon-pdf.png" alt="">
							</div-->
						</div>
						</#list>
						</#if>
					</div>
					<p class="tip tip-grey">支持JPG、GIF、PNG格式，每张图片不超过5M</p>
		    	</div>
		  	</div>
			<div class="form-group">
				<div class="col-sm-offset-2 col-sm-10">
					<a href="javascript:;" class="btn btn-blue btn-long filePicker" id="filePicker">选择照片</a>
					<button type="button" class="btn btn-blue btn-long btn-submit">保存</button>
				</div>
			</div>
		</form>	
		</div>
	</div>
</div>
<script src="${request.contextPath}/studevelop/js/img-adapter.js"></script>
<script>
<#----------------------------pic---------------------------------->
  
  var picBusinessKey = '${act.id!}';
  //缩略图压缩程度
  var ratio = window.devicePixelRatio || 1;
  //缩略图大小
  var thumbnailWidth = 100 * ratio;
  var thumbnailHeight = 100 * ratio;
  //Web Uploader实例
  var picUploader = WebUploader.create({
    //自动上传。
    auto: true,
    swf: '${request.contextPath}/static/webuploader/Uploader.swf',
    // 文件接收服务端。
    server: '${request.contextPath}/webuploader/upload',      
    threads:'8',        //同时运行5个线程传输
    fileNumLimit:'2',  //文件总数量只能选择10个 
	//单个文件最大为10m
    fileSingleSizeLimit: 5*1024*1024,
    // 选择文件的按钮。可选。
    // 内部根据当前运行是创建，可能是input元素，也可能是flash.
    <#if picSize ==1>
    pick: { id:'#filePicker',  //选择文件的按钮
            multiple:false }, 
    <#else>
     pick: { id:'#filePicker',  //选择文件的按钮
            multiple:true }, 
    </#if>        
    formData: {
        key: picBusinessKey
    },
    // 只允许选择图片文件。
    accept: {
        title: 'Images',
        extensions: 'gif,jpg,jpeg,png',
        mimeTypes: 'image/gif,image/jpg,image/jpeg,image/png'
    },
	  compress:{

          // 图片质量，只有type为`image/jpeg`的时候才有效。
          quality: 50,

          // 是否允许放大，如果想要生成小图的时候不失真，此选项应该设置为false.
          allowMagnify: false,

          // 是否允许裁剪。
          crop: false,

          // 是否保留头部meta信息。
          preserveHeaders: true,

          // 如果发现压缩后文件大小比原来还大，则使用原来图片
          // 此属性可能会影响图片自动纠正功能
          noCompressIfLarger: false,

          // 单位字节，如果图片大小小于此值，不会采用压缩。
          compressSize: 150
      }
  });
 picUploader.on('beforeFileQueued', function (file) {
 	var $list = $('#fileList')
  	var items = $list.find('.file-item');
  	if(items.length >= 2){
  		layerTipMsg(false,'提示','最多只能维护2张照片！');
  		return false;
  	}
 });
 
   // 当有文件添加进来的时候
  picUploader.on('fileQueued', function (file) {
	  var $list = $('#fileList')
      var $li = $(
        '<div id="' + file.id + '" class="upload-img-item file-item">' +
        	'<div class="file-img-wrap">\
        	<input type="hidden" id=\''+file.id+'-fullPath\'>\
        		<div class="file-panel">\
        			<a class="cancel" href="javascript:;" onclick="deletePicFile(\''+file.id+'\')">删除</a>\
        		</div>\
        		<img>\
        	</div>' +
        '</div>'
      ),
	  $img = $li.find('img');
	  
	   // $list为容器jQuery实例
      $list.append( $li );

      // 创建缩略图
      // 如果为非图片文件，可以不用调用此方法。
      // thumbnailWidth x thumbnailHeight 为 100 x 100
      thumbnailWidth = 120; 
      thumbnailHeight = 130;

	  picUploader.makeThumb( file, function( error, src ) {
	      	if ( error ) {
	            $img.replaceWith('<span>不能预览</span>');
	            return;
	        }
	      	$img.attr( 'src', src );
      	}, thumbnailWidth, thumbnailHeight);
      	
      	$list.find('.add-img-item:eq(0)').remove();
  });
 
  // 文件上传过程中创建进度条实时显示。
  picUploader.on('uploadProgress', function (file, percentage) {
  	var $li = $( '#'+file.id ),
	$percent = $li.find('.file-upload-progress .progress-bar');
	// 避免重复创建
	if ( !$percent.length ) {
		$percent = $('<div class="file-upload-progress progress"><div class="progress-bar progress-bar-info"><span></span></div></div>').appendTo( $li.find('.file-img-wrap') ).find('.progress-bar');
	}
    $percent.css( 'width', percentage * 100 + '%' ).find('span').text(percentage * 100 + '%');
  });
 
  // 文件上传成功，给info添加文字，标记上传成功。
  picUploader.on('uploadSuccess', function (file,response) {
      var $li = $( '#'+file.id ),
	　　$success = $li.find('div.file-status-success');
	   // 避免重复创建
	   if ( !$success.length ) {
	        $success = $('<div class="file-status-success"></div>').appendTo( $li.find('.file-img-wrap') );
	   }
	   $success.text('上传成功');
	   $('#hasUpload').val('true');
      $("#"+picBusinessKey+"-path").val(response.path);
      $("#"+file.id+"-fullPath").val(response.fullPath);
  });
 
  // 文件上传失败，给info添加文字，标记上传失败。
  picUploader.on('uploadError', function (file) {
	    var $li = $( '#'+file.id ),
		$error = $li.find('div.file-status-error');
	    // 避免重复创建
	    if ( !$error.length ) {
	        $error = $('<div class="file-status-error"></div>').appendTo( $li.find('.file-img-wrap') );
	    }
	    $error.text('上传失败');
  });
 
  // 上传成功或失败后删除进度条。
  picUploader.on('uploadComplete', function (file) {
     $( '#'+file.id ).find('.file-upload-progress').remove();
  });
  
  <#--//绑定提交事件
  $("#picUploadBtn").click(function() {
    console.log("上传...");
    picUploader.upload();   //执行手动提交
    console.log("上传成功");
  });-->
  
  function deletePicFile(fileId){
	showConfirmSuccess('确定要删除该图片吗？','确认',function(){
		if($("#"+fileId+"-fullPath").val() !=""){
			$.ajax({
				url:"${request.contextPath}/webuploader/remove",
				data:{"fullPath":$("#"+fileId+"-fullPath").val()},
				type:'post',
				success:function(data) {
					if(data=="success"){
						picUploader.removeFile(picUploader.getFile(fileId));
						$("#"+fileId).remove();
						$('#fileList').append('<div class="upload-img-item add-img-item" onclick="$(\'#filePicker\').click();"><label class="upload-img"><span></span></label></div>');
					}else{
						//layer.tips('删除文件失败', '#cls',{tipsMore: true});
						layerTipMsg(false,'提示','删除文件失败');
					}
				},
		 		error : function(XMLHttpRequest, textStatus, errorThrown) { 
		 			
				}
			});
		}else{
			picUploader.removeFile(picUploader.getFile(fileId));
			$("#"+fileId).remove();
			$('#fileList').append('<div class="upload-img-item add-img-item" onclick="$(\'#filePicker\').click();"><label class="upload-img"><span></span></label></div>');
		}
		
		layer.closeAll();
	});
 }

<#----------------------------pic end---------------------------------->
layer.photos({
	shade: .6,
	photos:'#fileList',
	anim: 5
});
$('.add-img-item').on('click',function(){
	$('#filePicker').click();
});

function deleteFile(atid){
	showConfirmSuccess('确定要删除该图片吗？','确认',function(){
		var eid = $('#delAttIds').val();
		if(eid!=''){
			eid+=',';
		}
		eid+=atid;
		$('#delAttIds').val(eid);
		$('#'+atid+'_div').remove();
		$('#fileList').append('<div class="upload-img-item add-img-item"><label class="upload-img"><span></span></label></div>');
		
		layer.closeAll();
	});
}

var isSubmit = false;
$('.btn-submit').click(function(){
	if(isSubmit){
		return false;
	}
	isSubmit = true;
	if(!checkValue('.form-horizontal')){
		isSubmit = false;
		return false;
	}
	// 提交数据
	var ii = layer.load();
	var options = {
		url : '${request.contextPath}/studevelop/activity/${actType!}/saveSchoolInfo',
		dataType : 'json',
		success : function(data){
	 		if(data.success){
	 			layer.closeAll();
				layerTipMsg(data.success,"成功",data.msg);
			  	refreshPage();
	 		}
	 		else{
	 			layerTipMsg(data.success,"失败",data.msg);
	 			isSubmit=false;
			}
			layer.close(ii);
		},
		clearForm : false,
		resetForm : false,
		type : 'post',
		error:function(XMLHttpRequest, textStatus, errorThrown){
			
			layer.close(ii);
			isSubmit=false;
		} 
	};
	$("#schoolForm").ajaxSubmit(options);
});

function refreshPage(){
	var url = "${request.contextPath}/studevelop/activity/${actType!}/index/page";
	$('.model-div').load(url);
}
</script>