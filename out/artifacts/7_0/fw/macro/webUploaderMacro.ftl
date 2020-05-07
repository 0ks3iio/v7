<#--
文件上传
businessKey 业务id(唯一)
contextPath 
sourceUrl
size 大小M
extensions 
mimeTypes
tips　页面提示信息，红色文字
fileNumLimit　文件数量限制
handler　　回调函数
-->
<#macro picUpload  businessKey="" contextPath="" resourceUrl="" size="2" extensions="gif,jpg,jpeg,bmp,png" mimeTypes="image/gif,image/jpeg,image/jpg,image/png" fileNumLimit="1" tips="" handler="defaultHandler">
	<#nested>
	<style>  
    #filePicker div:nth-child(2){top:0!important;left:0!important;width:100%!important;height:100%!important;}  
    #filePicker div:nth-child(2) label{margin: 0!important}  
	</style> 
	<div class="layer layer-addPhotos">
		<div><span class="color-red">${tips}</span><span><#if fileNumLimit?number gt 0>最多支持上传${fileNumLimit}个文件　</#if>单个文件大小不能大于${size}M</span></div><br>
		<div class="file-default-box">
			<div class="layer-content">
				<div class="file-list" id="fileList"></div>
			</div>
			<div class="layer-footer text-right">
				<a id="filePicker" class="btn btn-white filePicker">选择文件</a>
				<a id="picUploadBtn" class="btn btn-blue">开始上传</a>
			</div>
		</div>
	</div>
	<script type="text/javascript" src="${request.contextPath}/static/ueditor/third-party/webuploader/webuploader.min.js"></script>  
	<script>
		  var hasUploadSuc=false;// 是否有成功上传图片
		  var $ = jQuery,
		  picBusinessKey='${businessKey}',
		  //缩略图压缩程度
		  ratio = window.devicePixelRatio || 1,
		  //缩略图大小
		  thumbnailWidth = 100 * ratio,
		  thumbnailHeight = 100 * ratio,
		  //Web Uploader实例
		  picUploader = WebUploader.create({
		    //自动上传。
		    auto: false,
		    swf: '${request.contextPath}/static/webuploader/Uploader.swf',
		    // 文件接收服务端。
		    server: '${request.contextPath}/webuploader/upload',      
	        threads:'8',        //同时运行5个线程传输
	        fileNumLimit:'${fileNumLimit}',  //文件总数量只能选择10个 
	 		//单个文件最大为10m
		    fileSingleSizeLimit: ${size?number}*1024*1024,
	        // 选择文件的按钮。可选。
	        // 内部根据当前运行是创建，可能是input元素，也可能是flash.
	        <#if fileNumLimit =="1">
	        pick: { id:'#filePicker', 
	                multiple : false }, 
	        <#else>
	         pick: { id:'#filePicker', 
	                multiple:true }, 
	        </#if>        
	        formData: {
		        key: picBusinessKey
		    },
		    // 只允许选择图片文件。
		    accept: {
		        title: 'Images',
		        extensions: '${extensions}',
		        mimeTypes: '${mimeTypes}'
		    }
		  });
		  
		  picUploader.on("error", function (type) {
			    if (type == "Q_TYPE_DENIED") {
			        layer.msg("请上传${extensions}格式文件");
			    } else if (type == "Q_EXCEED_SIZE_LIMIT") {
			        layer.msg("超出空间文件大小");
			    } else if (type == "F_EXCEED_SIZE") {
			        layer.msg("文件大小不能超过${size?number}M");
			    }else if(type == "Q_EXCEED_NUM_LIMIT"){
			    	 layer.msg("文件数量不能超过${fileNumLimit}个");
			    }else if(type == "F_DUPLICATE"){
			    	 layer.msg("请勿重复上传文件");
				}else{
			        layer.msg("上传出错！请检查后重新上传！错误代码"+type);
			    }
		  });
		 
		   // 当有文件添加进来的时候
		  picUploader.on('fileQueued', function (file) {
			  var $list = $('#fileList')
		      var $li = $(
	            '<div id="' + file.id + '" class="file-item">' +
	            	'<div class="file-img-wrap">\
	            	<input type="hidden" id=\''+file.id+'-fullPath\'>\
	            		<div class="file-panel">\
	            			<a class="cancel" href="javascript:;" onclick="deletePicFile(\''+file.id+'\')">删除</a></a>\
	            		</div>\
	            		<img>\
	            	</div>' +
	                '<div class="file-name" title="'+file.name+'">' +file.name + '</div>' +
	            '</div>'
	          ),
			  $img = $li.find('img');
			  
			   // $list为容器jQuery实例
		      $list.append( $li );
	
		      // 创建缩略图
		      // 如果为非图片文件，可以不用调用此方法。
		      // thumbnailWidth x thumbnailHeight 为 100 x 100
		      thumbnailWidth = thumbnailHeight = 120
	
			  picUploader.makeThumb( file, function( error, src ) {
			      	if ( error ) {
			            $img.replaceWith('<span>不能预览</span>');
			            return;
			        }
			      	$img.attr( 'src', src );
		      	}, thumbnailWidth, thumbnailHeight);
		      <#if fileNumLimit =="1">
		  	  	$("#filePicker").hide();
			  </#if>
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
		  	  <#if fileNumLimit =="1">
		  	  	$("#filePicker").hide();
		  	  </#if>  
		      var $li = $( '#'+file.id ),
			　　$success = $li.find('div.file-status-success');
			   // 避免重复创建
			   if ( !$success.length ) {
			        $success = $('<div class="file-status-success"></div>').appendTo( $li.find('.file-img-wrap') );
			   }
			   $success.text('上传成功');
		      $("#"+picBusinessKey+"-path").val(response.path);
              $("#"+picBusinessKey+"FileName").val(file.name);
	          $("#"+file.id+"-fullPath").val(response.fullPath);
	          
	          hasUploadSuc=true;
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
		  
		  //绑定提交事件
	      $("#picUploadBtn").click(function() {
	        picUploader.upload();   //执行手动提交
	      });
		  var layerheight = $(window).height()*0.618;           
		  var layerwidth = $(window).width()*0.618;                
		  $("#fileList").css("max-height",layerheight-160+"px");
		  $('.js-addPhotos').on('click', function(){
				layer.open({
					type: 1,
					shade: false,
					maxmin: true,
					title:'上传图片',
					btn :false,
					area: [layerwidth+'px', layerheight+'px'],
					content: $('.layer-addPhotos'),
					end: function () {
						for (var i = 0; i < picUploader.getFiles().length; i++) {
			            	// 将图片从上传序列移除
			            	picUploader.removeFile(picUploader.getFiles()[i],true);
			            	$("#"+picUploader.getFiles()[i].id).remove();
			        	}
			        	picUploader.getFiles('inited');
						$("#filePicker").show();
					    if (${handler} instanceof Function) {
							eval(${handler})();
						} else {
							eval(${handler});
						}
		            },
		            success : function(layer){
		            	 picUploader.refresh();
		            	 setTimeout(function(){
						    picUploader.addButton({
				                id: '#filePicker',
				                <#if fileNumLimit =="1">
					                multiple : false,
						        <#else>
					                multiple : true,
						        </#if>
				                innerHTML: '选择文件'
				            });
						 }, 1000);
		            }
				});
				
		  });
		  function deletePicFile(fileId){
			if($("#"+fileId+"-fullPath").val() !=""){
				$.ajax({
					url:"${request.contextPath}/webuploader/remove",
					data:{"fullPath":$("#"+fileId+"-fullPath").val()},
					type:'post',
					success:function(data) {
						if(data=="success"){
							picUploader.removeFile(picUploader.getFile(fileId));
							$("#"+fileId).remove();
						}else{
							layer.msg('删除文件失败');
						}
					},
			 		error : function(XMLHttpRequest, textStatus, errorThrown) {  
			 			layer.msg(XMLHttpRequest.status);
					}
				});
			}else{
				picUploader.removeFile(picUploader.getFile(fileId));
				$("#"+fileId).remove();
			}
			$("#filePicker").show();
			$("#filePicker div").each(function (i) {
				$(this).css({'width':'65px', 'height':'23px'});
            });
		 }
		 
		 function defaultHandler(){
		 
		 }
		 
	</script>
</#macro>
<#--
导入文件上传
id 业务id(唯一)
contextPath 
resourceUrl
size M
extensions 
mimeTypes
fileNumLimit
handler 回调函数
-->
<#macro importFileUpload  businessKey="" contextPath="" resourceUrl="" size="10" extensions="xls,xlsx" mimeTypes="application/xls,application/xlsx" fileNumLimit="1" handler="" validateUrl="" validRowStartNo="">
	<#nested>
	<style>  
    #filePicker3 div:nth-child(2){top:0!important;left:0!important;width:100%!important;height:100%!important;}  
    #filePicker3 div:nth-child(2) label{margin: 0!important}  
	</style> 
	<div class="layer layer-addImportFiles">
			<div class="clearfix">
                <span class="pull-left mt6">只支持.xls .xlsx结尾的文件 </span>
                <a id="filePicker3" class="btn btn-blue filePicker3 pull-right font-14">选择文件</a>
            </div><br>
			<div class="file-default-box">
				<div class="layer-content">
					<table id="fileList3" class="table table-striped table-hover">
						<thead>
							<tr>
								<th>文件名称</th>
								<th>大小</th>
								<th>上传进度</th>
								<th>操作</th>
							</tr>
						</thead>
						<tbody>
						</tbody>
					</table>
				</div>
				<div class="layer-footer text-right">
					<!--<button id="importFileUploadBtn" class="btn btn-blue">开始上传</button>-->
				</div>
			</div>
		</div>
	<script type="text/javascript" src="${request.contextPath}/static/ueditor/third-party/webuploader/webuploader.min.js"></script>  
	<script>
	    var $ = jQuery,
	    excelBusinessKey='${businessKey}',
	    // 优化retina, 在retina下这个值是2
	    ratio = window.devicePixelRatio || 1,
	    // Web Uploader实例
	    importFileUploader = WebUploader.create({
	        // 选完文件后，是否自动上传。
	        auto: true,
	        swf: '${request.contextPath}/static/webuploader/Uploader.swf',
		    // 文件接收服务端。
		    server: '${request.contextPath}/webuploader/upload',      
	        threads:'8',        //同时运行5个线程传输
	        fileNumLimit:'${fileNumLimit}',  //文件总数量只能选择10个 
	        //单个文件最大为10m
		    fileSingleSizeLimit: ${size?number}*1024*1024,
	        // 选择文件的按钮。可选。
	        // 内部根据当前运行是创建，可能是input元素，也可能是flash.
	        <#if fileNumLimit == "1">
	        pick: { id:'#filePicker3',  //选择文件的按钮
	                multiple:false }, 
	        <#else>
	         pick: { id:'#filePicker3',  //选择文件的按钮
	                multiple:true }, 
	        </#if>
	        <#if extensions != "">
		    accept: {
		        title: 'Applications',
		        extensions: '${extensions}',
		        mimeTypes: '${mimeTypes}'
		    },
		    </#if>
		    formData: {
		        key: excelBusinessKey
		    }
	    });
	    
	    // 当有文件添加进来的时候，
	     //监听fileQueued事件，通过uploader.makeThumb来创建图片预览图。
	    //PS: 这里得到的是Data URL数据，IE6、IE7不支持直接预览。可以借助FLASH或者服务端来完成预览。
	    importFileUploader.on('fileQueued', function( file ) {
		    var $list = $('#fileList3')
		    var $li = $(
		            '<tr id='+file.id+'>\
		            	<td>'+ file.name +'</td>\
		            	<td>'+ WebUploader.formatSize(file.size)+'</td>\
		            	<td><span class="label label-green file-status-succ">等待上传</span></td>\
		            	<td><a href="javascript:;" class="color-red" onclick="deleteImportUploadFile(\''+file.id+'\')">删除</a></td>\
		            	<input type="hidden" id=\''+file.id+'-fullPath\'>\
		            </tr>'
		            );
		    // $list为容器jQuery实例
		    $list.find('tbody').append( $li );
	    });
	
	    // 然后剩下的就是上传状态提示了，当文件 上传过程中, 上传成功，上传失败，上传完成
	    // 都分别对应uploadProgress, uploadSuccess, uploadError, uploadComplete事件。
	    // 文件上传过程中创建进度条实时显示。
	    importFileUploader.on('uploadProgress', function( file, percentage ) {
	        var $li = $( '#'+file.id ),
		    $percent = $li.find('.file-upload-progress .progress-bar');
		    // 避免重复创建
		    if ( !$percent.length ) {
		        $percent = ( $li.find('td').eq(2) ).html('<div class="file-upload-progress progress"><div class="progress-bar progress-bar-info"><span></span></div></div>').find('.progress-bar');
		    }
		    $percent.css('width', percentage * 100 + '%' ).find('span').text(percentage * 100 + '%');
	    });
	 
	    // 文件上传成功，给item添加成功class, 用样式标记上传成功。
	    importFileUploader.on('uploadSuccess', function( file,response) {
	         $li = $( '#'+file.id );
	         ($li.find('td').eq(3)).html($('<a href="javascript:;" class="color-red" onclick="deleteImportUploadFile(\''+file.id+'\',\'success\')">删除</a>'))
	         $success = $li.find('span.file-status-succ');
		    
		     $success = $('<span class="label label-green file-status-succ">上传成功</span>').appendTo( $li.find('td').eq(2) );
			 if("${validateUrl!}" !=""){
			 	var url='${validateUrl!}';
			 	if('${request.contextPath}'!=''){
			 		url='${request.contextPath}${validateUrl!}';
			 	}
				$.ajax({
					url:url,
					data: {'filePath':response.fullPath,'validRowStartNo':'${validRowStartNo!}'},  
					type:'post',
					success:function(data) {
						if(data !=""){
							deleteImportUploadFile(file.id,'error');
							layerTipMsgWarn("提示",data);
						}else{
							//设置值
					         $('.import-filelist').html('<li><a href="javascript:void(0);">'+file.name+'</a></li>');
					         $("#"+excelBusinessKey+"-path").val(response.fullPath);
					         $("#"+file.id+"-fullPath").val(response.fullPath);
					         deleteImportUploadFile(file.id,"importFile");
					         layer.closeAll();
						}
					},
			 		error : function(XMLHttpRequest, textStatus, errorThrown) {  
			 			
					}
				});
			}else{
				//设置值
		         $('.import-filelist').html('<li><a href="javascript:void(0);">'+file.name+'</a></li>');
		         $("#"+excelBusinessKey+"-path").val(response.fullPath);
		         $("#"+file.id+"-fullPath").val(response.fullPath);
		          deleteImportUploadFile(file.id,"importFile");
		         layer.closeAll();
			}
	        
	    });
	    
	    importFileUploader.on("error", function (type) {
		    if (type == "Q_TYPE_DENIED") {
		        layer.msg("请上传XLS,XLSX格式文件");
		    } else if (type == "Q_EXCEED_SIZE_LIMIT") {
		        layer.msg("文件大小不能超过${size?number}M");
		    }else if(type == "Q_EXCEED_NUM_LIMIT"){
			    	 layer.msg("文件数量不能超过${fileNumLimit}个");
		    }else if(type == "F_DUPLICATE"){
			    	 layer.msg("重复上传图片");
			}else {
		        layer.msg("上传出错！请检查后重新上传！错误代码"+type);
		    }
		});
	    
	    // 文件上传失败，现实上传出错。
	    importFileUploader.on('uploadError', function( file ) {
	        var $li = $( '#'+file.id ),
		    $error = $li.find('span.file-status-error');

		    // 避免重复创建
		    if ( !$error.length ) {
		        $error = $('<span class="label label-green file-status-error">上传失败</span>').appendTo( $li.find('td').eq(2) );
		    }
	 		$('<a href="javascript:;" class="color-red" onclick="deleteImportUploadFile(\''+file.id+'\')">删除</a>').appendTo( $li.find('td').eq(3) );
	    });
	 
	    // 完成上传完了，成功或者失败，先删除进度条。
	    importFileUploader.on( 'uploadComplete', function( file ) {
	        $( '#'+file.id ).find('.progress').remove();
	    });
	      
	   $('.js-addImportFiles').on('click', function(){
			layer.open({
				type: 1,
				shade: false,
				maxmin: true,
				title:'上传导入文件',
				btn :false,
				area: ['600px','610px'],
				content: $('.layer-addImportFiles'),
				end: function () {
				    if (${handler} instanceof Function) {
						eval(${handler})();
					} else {
						eval(${handler});
					}
	            },
	            success : function(layer){
					 importFileUploader.refresh();
					 setTimeout(function(){ 
						  importFileUploader.addButton({
			                id: '#filePicker3',
			                innerHTML: '选择文件'
			              });
					 }, 1000);
	            }
			});
			
	 	});
	
	function deleteImportUploadFile(fileId,status){
		if(status && status== "success"){
			$.ajax({
				url:"${request.contextPath}/webuploader/remove",
				data:{"fullPath":$("#"+fileId+"-fullPath").val()},
				type:'post',
				success:function(data) {
					if(data=="success"){
						importFileUploader.removeFile(importFileUploader.getFile(fileId));
						$("#"+fileId).remove();
						$('.import-filelist').html('');
					}else{
						 layer.msg("删除文件失败");
					}
				},
		 		error : function(XMLHttpRequest, textStatus, errorThrown) {  
		 			layer.msg(XMLHttpRequest.status);
				}
			});
		}else if(status && status== "importFile"){
			importFileUploader.removeFile(importFileUploader.getFile(fileId));
			$("#"+fileId).remove();
		}else{
			importFileUploader.removeFile(importFileUploader.getFile(fileId));
			$("#"+fileId).remove();
			$('.import-filelist').html('');
		}
    }
	</script>
</#macro>

<#--
文件上传
id 业务id(唯一)
contextPath 
resourceUrl
size M
extensions 
mimeTypes
fileNumLimit
handler 回调函数
-->
<#macro fileUpload  businessKey="" contextPath="" resourceUrl="" size="2" extensions="" mimeTypes="" fileNumLimit="1" handler="defaultHandler">
	<#nested>
		<style>  
    #filePicker2 div:nth-child(2){top:0!important;left:0!important;width:100%!important;height:100%!important;}  
    #filePicker2 div:nth-child(2) label{margin: 0!important}  
	</style> 
	<div class="layer layer-addFiles height-1of1 over-y"">
			<div><span>最多支持上传${fileNumLimit}个文件　单个文件大小不能大于${size}M<#if mimeTypes != "">　只支持${mimeTypes}结尾的文件</#if></span></div><br>
			<div class="file-default-box">
				<div class="layer-content">
					<table id="fileList2" class="table table-striped table-hover">
						<thead>
							<tr>
								<th>文件名称</th>
								<th>大小</th>
								<th>上传进度</th>
								<th>操作</th>
							</tr>
						</thead>
						<tbody>
						</tbody>
					</table>
				</div>
				<div class="layer-footer text-right">
					<a id="filePicker2" class="btn btn-white filePicker2">选择文件</a>
					<button id="fileUploadBtn1" class="btn btn-blue" type="button">开始上传</button>
					<a id="fileUploadCloseBtn" class="btn btn-blue">关闭</a>
				</div>
			</div>
		</div>
	<script type="text/javascript" src="${request.contextPath}/static/ueditor/third-party/webuploader/webuploader.min.js"></script>  
	<script>
		var fileNums = parseInt("${fileNumLimit}");
  		var hasUploadSuc=false;// 是否有成功上传图片
  		var fileName="";
	    var $ = jQuery,
	    fileBusinessKey='${businessKey}',
	    // 优化retina, 在retina下这个值是2
	    ratio = window.devicePixelRatio || 1,
	    // Web Uploader实例
	    fileUploader = WebUploader.create({
	        // 选完文件后，是否自动上传。
	        auto: false,
	        swf: '${request.contextPath}/static/webuploader/Uploader.swf',
		    // 文件接收服务端。
		    server: '${request.contextPath}/webuploader/upload',      
	        threads:'8',        //同时运行5个线程传输
	        fileNumLimit:'${fileNumLimit}',  //文件总数量只能选择10个 
	        //单个文件最大为10m
		    fileSingleSizeLimit: ${size?number}*1024*1024,
	        // 选择文件的按钮。可选。
	        // 内部根据当前运行是创建，可能是input元素，也可能是flash.
	        <#if fileNumLimit == "1">
	        pick: { id:'#filePicker2',  //选择文件的按钮
	                multiple:false }, 
	        <#else>
	         pick: { id:'#filePicker2',  //选择文件的按钮
	                multiple:true }, 
	        </#if>
	        <#if extensions != "">
		    accept: {
		        title: 'fileType',
		        extensions: '${extensions}',
		        mimeTypes: '${mimeTypes}'
		    },
		    </#if>
		    formData: {
		        key: fileBusinessKey
		    }
	    });
	    
	     fileUploader.on("error", function (type) {
			    if (type == "Q_TYPE_DENIED") {
			    	<#if extensions != "">
			    		layer.msg("请上传${extensions}格式文件");
			    	<#else>
			    		layer.msg("请上传文件");
			    	</#if>
			    } else if (type == "Q_EXCEED_SIZE_LIMIT") {
			        layer.msg("文件大小不能超过${size?number}M");
			    }else if(type == "Q_EXCEED_NUM_LIMIT"){
			    	layer.msg("文件数量不能超过${fileNumLimit}个");
		    	}else if(type == "F_EXCEED_SIZE"){
		        	layer.msg("单个文件大小不能超过${size?number}M");
				}else if(type == "F_DUPLICATE") {
					layer.msg("请勿重复上传文件");
				}else {
			        layer.msg("上传出错！请检查后重新上传！错误代码"+type);
			    }
		  });
	    
	    // 当有文件添加进来的时候，
	     //监听fileQueued事件，通过uploader.makeThumb来创建图片预览图。
	    //PS: 这里得到的是Data URL数据，IE6、IE7不支持直接预览。可以借助FLASH或者服务端来完成预览。
	    fileUploader.on('fileQueued', function( file ) {
		    var $list = $('#fileList2')
		    if ($list.find('tbody').find('tr').length >= fileNums) {
		    	fileUploader.removeFile(fileUploader.getFile(file.id),true);
		    } else {
		    	var $li = $(
		            '<tr id='+file.id+'>\
		            	<td style="white-space:normal; word-break:break-all;">'+ file.name +'</td>\
		            	<td>'+ WebUploader.formatSize(file.size)+'</td>\
		            	<td><span class="label label-green file-status-succ">等待上传</span></td>\
		            	<td><a href="javascript:;" class="color-red" onclick="deleteUploadFile(\''+file.id+'\')">删除</a></td>\
		            	<input type="hidden" id=\''+file.id+'-fullPath\'>\
		            </tr>'
		            );
		    	// $list为容器jQuery实例
		    	$list.find('tbody').append( $li );
		    }
		  	if ($list.find('tbody').find('tr').length >= fileNums) {
		  		$("#filePicker2").hide();
		  	}
	    });
	
	    // 然后剩下的就是上传状态提示了，当文件 上传过程中, 上传成功，上传失败，上传完成
	    // 都分别对应uploadProgress, uploadSuccess, uploadError, uploadComplete事件。
	    // 文件上传过程中创建进度条实时显示。
	    fileUploader.on('uploadProgress', function( file, percentage ) {
	        var $li = $( '#'+file.id ),
		    $percent = $li.find('.file-upload-progress .progress-bar');
		    // 避免重复创建
		    if ( !$percent.length ) {
		        $percent = ( $li.find('td').eq(2) ).html('<div class="file-upload-progress progress"><div class="progress-bar progress-bar-info"><span></span></div></div>').find('.progress-bar');
		    }
		    $percent.css('width', percentage * 100 + '%' ).find('span').text(Math.floor(percentage * 100) + '%');
	    });
	 
	    // 文件上传成功，给item添加成功class, 用样式标记上传成功。
	    fileUploader.on('uploadSuccess', function( file,response) {
	    	 <#if fileNumLimit =="1">
		  	  	$("#filePicker2").hide();
		  	 </#if>  
	         $li = $( '#'+file.id );
	         ($li.find('td').eq(3)).html($('<a href="javascript:;" class="color-red" onclick="deleteUploadFile(\''+file.id+'\',\'success\')">删除</a>'))
	         $success = $li.find('span.file-status-succ');
		    
		     $success = $('<span class="label label-green file-status-succ">上传成功</span>').appendTo( $li.find('td').eq(2) );

	        //设置值
	         $("#"+fileBusinessKey+"-path").val(response.path);
	         $("#"+file.id+"-fullPath").val(response.fullPath);
	         <#if fileNumLimit =="1">
	         	fileName=file.name;
	         <#else>
	         	 if(fileName ==""){
	        	  	fileName+=file.name;
	        	 }else{
	        	 	fileName+=","+file.name;
	        	 }
	         </#if>
	         hasUploadSuc=true;
	    });
	 
	    // 文件上传失败，现实上传出错。
	    fileUploader.on('uploadError', function( file ) {
	        var $li = $( '#'+file.id ),
		    $error = $li.find('span.file-status-error');

		    // 避免重复创建
		    if ( !$error.length ) {
		        $error = $('<span class="label label-green file-status-error">上传失败</span>').appendTo( $li.find('td').eq(2) );
		    }
	 		$('<a href="javascript:;" class="color-red" onclick="deleteUploadFile(\''+file.id+'\')">删除</a>').appendTo( $li.find('td').eq(3) );
	    });
	 
	    // 完成上传完了，成功或者失败，先删除进度条。
	    fileUploader.on( 'uploadComplete', function( file ) {
	        $( '#'+file.id ).find('.progress').remove();
	    });
	
	
	    //绑定提交事件
	    $("#fileUploadBtn1").click(function() {
	        //console.log("上传...");
	        fileUploader.upload();   //执行手动提交
	        //console.log("上传成功");
	      });
	      
	    //绑定关闭事件
      	$("#fileUploadCloseBtn").click(function() {
	        layer.closeAll();
      	});
      	var layerheight = $(window).height()*0.618;           
		var layerwidth = $(window).width()*(3/10);  
	   $('.js-addFiles').on('click', function(){
			layer.open({
				type: 1,
				shade: false,
				maxmin: true,
				title:'上传文件',
				btn :false,
				area: [layerwidth+'px', layerheight+'px'],
				content: $('.layer-addFiles'),
				end: function () {
					for (var i = 0; i < fileUploader.getFiles().length; i++) {
			            // 将图片从上传序列移除
			            fileUploader.removeFile(fileUploader.getFiles()[i]);
			            $("#"+fileUploader.getFiles()[i].id).remove();
			        }
			        fileUploader.getFiles('inited');
			        $("#filePicker2").show();
				    if (${handler} instanceof Function) {
						eval(${handler})();
					} else {
						eval(${handler});
					}
	            },
	            success : function(layer){
	            	fileUploader.refresh();
	            	setTimeout(function(){ 
					   fileUploader.addButton({
			                id: '#filePicker2',
			                <#if fileNumLimit =="1">
				                multiple : false, 
					        <#else>
				                multiple : true, 
					        </#if>  
			                innerHTML: '选择文件'
			            });
					 }, 1000); 
	            	
	            }
			});
	 	});
	
	function deleteUploadFile(fileId,status){
		if(status && status== "success"){
			$.ajax({
				url:"${request.contextPath}/webuploader/remove",
				data:{"fullPath":$("#"+fileId+"-fullPath").val()},
				type:'post',
				success:function(data) {
					if(data=="success"){
						fileUploader.removeFile(fileUploader.getFile(fileId),true);
						$("#"+fileId).remove();
					}else{
						layer.msg('删除文件失败');
					}
				},
		 		error : function(XMLHttpRequest, textStatus, errorThrown) {  
		 			layer.msg(XMLHttpRequest.status);
				}
			});
		}else{
			fileUploader.removeFile(fileUploader.getFile(fileId),true);
			$("#"+fileId).remove();
		}
		$("#filePicker2").show();
    }
    
    function defaultHandler(){
    
    }
	</script>
</#macro>