<meta http-equiv="cache-control" content="no-cache">
<script type="text/javascript" src="${request.contextPath}/static/ueditor/third-party/webuploader/webuploader.min.js" />
<div class="box box-default">
	<div class="box-header">
		<h4 class="box-title">我的校园</h4>
	</div>
	<div class="box-body">
		<div class="form-horizontal">
			<div class="form-group">
				<label class="col-sm-2 control-label">校长照片</label>
				<div class="col-sm-10">
					<div class="upload-img-container clearfix">
						<div class="upload-img-item upload-img-new <#if !hasPic?default(false)>open<#else>hidden</#if>">
							<label class="upload-img" for="filePicker2"><span></span></label>
						</div>
						<div class="upload-img-item uploader-list <#if hasPic?default(false)>open<#else>hidden</#if>" style="width:130px;height:130px;">
							<div id="file_id_0" class="file-item" style="width:130px;">
								<div class="file-img-wrap">
									<div class="file-panel">
					        			<a class="cancel" href="javascript:;" onclick="deletePicFile('file_id_0')">删除</a>
					        		</div>
								<#if hasPic?default(false)><img layer-src="${request.contextPath}/studevelop/words/masterpic/show?showOrigin=1&_=${randomNum!}" src="${request.contextPath}/studevelop/words/masterpic/show?${randomNum!}" alt="" layer-index="0"></#if>
								</div>
							</div>
						</div>
					</div>
					<a id="filePicker2" class="btn btn-blue <#if !hasPic?default(false)>open<#else>hidden</#if>">选择图片</a>
					<a id="filePicker3" class="btn btn-blue <#if hasPic?default(false)>open<#else>hidden</#if>">重新上传</a>
					<p class="tip tip-grey">支持JPG、GIF、PNG格式，每张图片不超过5M</p>
		    	</div>
		  	</div>
		</div>
		<form name="wform" id="wform" method="post">
		<input type="hidden" name="id" value="${words.id!}" />
		<input type="hidden" name="unitId" value="${words.unitId!}" />
		<input type="hidden" id="sec" value="${nowSection?default('')}" />
		<#if msg?default('') == ''>
		<ul class="nav nav-tabs" role="tablist">
			<#list sections as sec >
			<li role="presentation" <#if sec_index==0>class="active"</#if> sec="${sec.thisId!}">
				<a href="#aa${sec.thisId!}" id="sec-${sec.thisId!}" role="tab" data-toggle="tab" aria-expanded="true">${sec.mcodeContent!}寄语</a>
			</li>
			</#list>
		</ul>
		<div class="tab-content">
			<#list sections as sec>
			<#assign grades = gradesMap[sec.thisId]?if_exists />
			<#if grades?exists && grades?size gt 0>
			<div class="tab-pane <#if sec_index==0>active</#if>" role="tabpanel" id="aa${sec.thisId!}">
				<div class="form-horizontal">
					<#list grades as gr>
					<div class="form-group">
				    	<label class="col-sm-2 control-label">${gr[1]!}寄语</label>
				    	<div class="col-sm-6">
				    		<div class="textarea-container">
				    			<textarea name="words${gr[0]}" id="words${gr[0]}" cols="30" rows="10" class="form-control" msgName="${gr[1]!}" maxLength="400">${(words.getWordsVal('words'+gr[0]))!}</textarea>
				    			<span>200</span>
				    		</div>
				    	</div>
					</div>
					</#list>
					<div class="form-group">
						<div class="col-sm-offset-2 col-sm-10">
							<button type="button" class="btn btn-blue btn-long btn-submit">保存</button><#if sections?exists && sections?size gt 1><span class="tip color-blue">&nbsp;&nbsp;可以在所有学段的年级寄语都维护之后再保存</span></#if>
						</div>
					</div>
				</div>
			</div>
			</#if>
			</#list>
		</div>
		</#if>
		</form>
	</div>
</div>
<script>
<#if nowSection?default('') != ''>
$('#sec-${nowSection!}').click();
</#if>
<#if msg?default('') != ''>
layerTipMsg(false,"提示","${msg!}");
</#if>

<#-- ==========================pic start====================================================-->
var uploader = WebUploader.create({

    // 选完文件后，是否自动上传。
    auto: true,
    // swf文件路径
    swf: '${request.contextPath}/static/webuploader/Uploader.swf',
    // 文件接收服务端。
    server: '${request.contextPath}/studevelop/words/masterpic/save',

	// 文件数量
	fileNumLimit : 1,
	// 单个文件大小 5M
	fileSingleSizeLimit : 5*1024*1024,
	
    // 选择文件的按钮。可选。
    // 内部根据当前运行是创建，可能是input元素，也可能是flash.
    pick: {
	    id:'#filePicker2',
	    innerHTML : '选择图片',
	    multiple:false
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
    },
    // 缩略图
    thumb : {
    	width: 120,
	    height: 120,
	    // 图片质量，只有type为`image/jpeg`的时候才有效。
	    quality: 70,
	
	    // 是否允许放大，如果想要生成小图的时候不失真，此选项应该设置为false.
	    allowMagnify: false,
	
	    // 是否允许裁剪。
	    crop: true
    }
});

<#if hasPic?default(false)>
uploader.addButton({
    id: '#filePicker3',
    innerHTML : '重新上传'
});
</#if>

// 当有文件添加进来的时候
uploader.on( 'fileQueued', function( file ) {
    var $li = $(
        '<div id="' + file.id + '" class="file-item">' +
        	'<div class="file-img-wrap">\
        		<div class="file-panel">\
        			<a class="cancel" href="javascript:;" onclick="deletePicFile(\''+file.id+'\')">删除</a>\
        		</div>\
        		<img>\
        	</div>' +
        '</div>'
      ),
    $img = $li.find('img');

    // $list为容器jQuery实例
    var $list = $('.uploader-list');
    var fileList = $list.find('.file-item');
	if(fileList && fileList.length>0){
		fileList.each(function(){
			$(this).remove();
		});
	}
    
    $list.addClass('open');
    $list.append( $li );
    $list.removeClass('hidden').addClass('open');
    $('.upload-img-new').removeClass('open').addClass('hidden');
	$('#filePicker2').removeClass('open').addClass('hidden');
	$('#filePicker3').removeClass('hidden').addClass('open');
	<#if !hasPic?default(false)>
	// 添加“添加文件”的按钮，
	uploader.addButton({
	    id: '#filePicker3',
	    innerHTML : '重新上传'
	});
	</#if>

    // 创建缩略图
    // 如果为非图片文件，可以不用调用此方法。
    // thumbnailWidth x thumbnailHeight 为 100 x 100
    uploader.makeThumb( file, function( error, src ) {
        if ( error ) {
            $img.replaceWith('<span>不能预览</span>');
            return;
        }

        $img.attr( 'src', src );
    }, 120, 120 );
});

// 文件上传过程中创建进度条实时显示。
uploader.on( 'uploadProgress', function( file, percentage ) {
    var $li = $( '#'+file.id ),
        $percent = $li.find('.progress span');

    // 避免重复创建
    if ( !$percent.length ) {
        $percent = $('<p class="progress"><span></span></p>')
                .appendTo( $li )
                .find('span');
    }

    $percent.css( 'width', percentage * 100 + '%' );
});

// 文件上传成功，给item添加成功class, 用样式标记上传成功。
uploader.on( 'uploadSuccess', function( file ) {
    $( '#'+file.id ).addClass('upload-state-done');
});

// 文件上传失败，显示上传出错。
uploader.on( 'uploadError', function( file ) {
    var $li = $( '#'+file.id ),
        $error = $li.find('div.error');

    // 避免重复创建
    if ( !$error.length ) {
        $error = $('<div class="error"></div>').appendTo( $li );
    }

    $error.text('上传失败');
});

// 完成上传完了，成功或者失败，先删除进度条。
uploader.on( 'uploadComplete', function( file ) {
    $( '#'+file.id ).find('.progress').remove();
    uploader.reset();
});

function deletePicFile(fileId){
	showConfirmSuccess('确定要删除该图片吗？','确认',function(){
		$.ajax({
		url:"${request.contextPath}/studevelop/words/masterpic/del",
		data:{},
		type:'post',
		success:function(data) {
			layer.closeAll();
			var jsonO = JSON.parse(data);
	 		if(jsonO.success){
				$("#"+fileId).remove();
				$('.upload-img-new').removeClass('hidden').addClass('open');
			}else{
				layerTipMsg(false,'提示','删除失败');
			}
		},
 		error : function(XMLHttpRequest, textStatus, errorThrown) { 
 			//layerTipMsg(XMLHttpRequest.status);
 			layer.closeAll();
 			layerTipMsg(false,'提示','删除失败');
		}
	});
	});
 }

<#-- ==========================pic end====================================================-->
layer.photos({
	shade: .6,
	photos:'.uploader-list',
	anim: 5
});

$('.nav-tabs li').on('click',function(){
	var ss = $(this).attr('sec');
	$('#sec').val(ss);
});

var isSubmit=false;
$('.btn-submit').click(function(){
	if(isSubmit){
		return false;
	}
	isSubmit = true;
	var msg = '';
	$('#wform textarea').each(function(){
		var txt = $(this).val();
		if(getLength(txt) > 400){
			if(msg!=''){
				msg+='、';
			}
			msg+=$(this).attr('msgName');
		}
	});
	if(msg != ''){
		layerTipMsg(false,"提示","保存失败："+msg+"的寄语内容不能超过400个字节（200个汉字）");
		isSubmit = false;
		return false;
	}
	
	if(!checkValue('#wform')){
		isSubmit = false;
		return false;
	}
	var ii = layer.load();
	var options = {
		url : "${request.contextPath}/studevelop/words/save",
		dataType : 'json',
		clearForm : false,
		resetForm : false,
		type : 'post',
		success : function(data){
 			var jsonO = data;
	 		if(!jsonO.success){
	 			layerTipMsg(jsonO.success,"保存失败",jsonO.msg);
	 			$("#arrange-commit").removeClass("disabled");
	 			isSubmit = false;
	 			return;
	 		}else{
	 			layer.closeAll();
				layerTipMsg(jsonO.success,"保存成功",jsonO.msg);
				isSubmit = false;
			  	reloadWords();
			}
			layer.close(ii);
		},
		error:function(XMLHttpRequest, textStatus, errorThrown){}//请求出错 
	};
	$("#wform").ajaxSubmit(options);
});

function reloadWords(){
	var ns = $('#sec').val();
	var url = '${request.contextPath}/studevelop/words/index/page?nowSection='+ns;
	$(".model-div").load(url);
}
</script>