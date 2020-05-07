
<script type="text/javascript" src="${request.contextPath}/static/webuploader/webuploader.js" />
<div id="myDiv">
    <form id="subForm" >
        <input type="hidden" name="createUserId" value="${famDearThreeInTwo.createUserId!}">
        <input type="hidden" name="id" id="id" value="${famDearThreeInTwo.id!}">
        <input type="hidden" name="unitId" value="${famDearThreeInTwo.unitId!}">
        <input type="hidden" name="state" value="${famDearThreeInTwo.state!}">
        <input type="hidden" name="createTime" value="${famDearThreeInTwo.createTime!}">
        <input type="hidden" name="isDelete" value="${famDearThreeInTwo.isDelete!}">
        
        <div class="main-content">
        <div class="main-content-inner">
            <div class="page-content">
                <div class="box box-default">
                
                    <div class="box-body">
                       <div class="form-horizontal mt20">
		                   <div class="form-group">
		                       <label class="col-sm-2 control-label no-padding-right"><span style="color:red">*</span>年度：</label>
		                       <div class="col-sm-3">
		                           <select class="form-control" id="year" name="year">
							           <#if acadyearList?exists && (acadyearList?size>0)>
							               <#list acadyearList as item>
							                   <option value="${item!}" <#if year?default(nowYear)==item?default('b')>selected</#if>>${item!}</option>
							               </#list>
							           </#if>
							       </select>
	                       		</div>
	                       </div>
	                       <div class="form-group">
	                       	<label class="col-sm-2 control-label no-padding-right"><span style="color:red">*</span>标题：</label>
	                          	<div class="col-sm-3">
	                           	<input type="text" class="form-control" id="title" name="title" value="${famDearThreeInTwo.title!}" maxlength="200" style="width:100%;"/>
	                       	</div>
	                       </div>
	                       <div class="form-group">
	                           <label class="col-sm-2 control-label no-padding-right"><span style="color:red">*</span>内容：</label>
	                           <div class="col-sm-8">
	                           	<textarea class="form-control" cols="30" rows="5" maxlength="2000" id="content" name="content"   style="width:100%;">${famDearThreeInTwo.content!}</textarea>
	                           </div>
	                       </div>
                           <div class="form-group">
                                <label class="col-sm-2 control-label no-padding-right">图片：</label>
                                <div class="col-sm-8">
                                    <div class="clearfix">
                                    	<div id="ticketImages" class="js-layer-photos">
											<#if actDetails?exists&& (actDetails?size > 0)>
											    <#list actDetails as ad>
											        <span class="position-relative float-left mr10 mb10">
													<a class="pull-left">
											             <img id ="" style="width: 94px;height: 94px"  data-img-action="adapte" layer-src="${request.contextPath}/familydear/threeInTwo/showPic?id=${ad.id!}&showOrigin=1" src="${request.contextPath}/familydear/threeInTwo/showPic?id=${ad.id!}&showOrigin=0" alt="">
											         </a>
											        <a class="pos-abs" style="top: -10px;right: -6px;" onclick="delPic1('${ad.id}',1)">
											            <i class="fa fa-times-circle color-red"></i>
											        </a>
											        </span>
											    </#list>
											</#if>
                                    	</div>
										<a href="javascript:void(0);" class="form-file pull-left mb10" id="ticket">
											<i class="fa fa-plus"></i>
										</a>
									</div>
								<div class="color-999"><i class="fa fa-exclamation-circle color-yellow"></i> 尺寸建议：178*108 ，图片大小不得超过10M</div>
                           		</div>
                            </div>
                            <div class="form-group">
                                <label class="col-sm-2 control-label no-padding-right">文件：</label>
                                <div class="col-sm-8">
                                    <p><button type="button" class="btn btn-blue" id="fileUp">上传文件</button></p>
									<div id="desFile">
										<#if fileAtts?exists&& (fileAtts?size > 0)>
											    <#list fileAtts as ad>
											        <div><span>${ad.filename!}</span><a class="color-blue ml10" href="${request.contextPath}/familydear/threeInTwo/downFile?id=${ad.id!}&showOrigin=1">下载</a><span class="color-blue ml10">|</span><a class="color-blue ml10" onclick="delPic1('${ad.id}',2)">删除</a></div>
											    </#list>
											</#if>
  									</div>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-sm-2 control-label no-padding-right"></label>
                                <div class="col-sm-8">
                                    <a class="btn btn-blue" onclick="save();" href="javascript:;">保存</a>
                                    <a class="btn btn-blue" onclick="goBack();" href="javascript:;">返回</a>
                                </div>
                            </div>
                      </div>
				</div>
			</div>
		<div>
	</div>
	</div>
</form>
</div>
<script>

	function goBack(){
		url = "${request.contextPath}/familydear/threeInTwo/edu/index/page";
		$(".model-div-show").load(url);
	}
	
	var isSubmit=false;
    function save() {
    	if(isSubmit){
    		isSubmit = true;
			return;
		}
		var check = checkValue('#subForm');
		if(!check){
			isSubmit=false;
			return;
		}
        var year = $("#year").val();
        if(!year){
            layerTipMsg(false,"提示!","年度不能为空!");
            return;
        }
        var title = $("#title").val();
        if(!title){
            layerTipMsg(false,"提示!","标题不能为空!");
            return;
        }
        var content = $("#content").val();
        if(!content){
            layerTipMsg(false,"提示!","内容不能为空!");
            return;
        }
        
        var options = {
            url : "${request.contextPath}/familydear/threeInTwo/edu/save/page",
            dataType : 'json',
            success : function(data){
                var jsonO = data;
                if(!jsonO.success){
                    layerTipMsg(jsonO.success,"保存失败",jsonO.msg);
                    $("#arrange-commit").removeClass("disabled");
                    isSubmit=false;
                    return;
                }else{
                    layer.closeAll();
                    layerTipMsg(jsonO.success,"保存成功",jsonO.msg);
                    isSubmit=false;
                    goBack();
                }
            },
            clearForm : false,
            resetForm : false,
            type : 'post',
            error:function(XMLHttpRequest, textStatus, errorThrown){}//请求出错
        };
        $("#subForm").ajaxSubmit(options);
    }
    
    
    $(function(){
        var $wrap = $('#uploader');
        var $queue = $("#filelist");
        /*init webuploader*/
        var $btn =$("#ctlBtn");   
        var thumbnailWidth = 100;   //缩略图高度和宽度 （单位是像素），当宽高度是0~1的时候，是按照百分比计算，具体可以看api文档
        var thumbnailHeight = 100;


        var uploader = WebUploader.create({
            // 选完文件后，是否自动上传。
            auto: true,

            // swf文件路径
            swf: '${request.contextPath}/static/webuploader/Uploader.swf',
            // 文件接收服务端。
            server: '${request.contextPath}/familydear/threeInTwo/saveAttachment',

            formData: {
                'objId':'${famDearThreeInTwo.id!}',
                'objType':'famDearThreeInTwoPicture',
            },

            // 选择文件的按钮。可选。
            // 内部根据当前运行是创建，可能是input元素，也可能是flash.
            pick: '#ticket',
            duplicate:true,

            // 只允许选择图片文件。
            accept: {
                title: 'Images',
                extensions: 'gif,jpg,jpeg,bmp,png',
                mimeTypes: 'image/*'
            },
            method:'POST',
        });
        
        // 当有文件添加进来的时候
        uploader.on( 'fileQueued', function( file ) {  // webuploader事件.当选择文件后，文件被加载到文件队列中，触发该事件。等效于 uploader.onFileueued = function(file){...} ，类似js的事件定义。
            var $li = $( '<li id="' + file.id + '">' +
                    '<p class="imgWrap">' +
                    '<img>' +
                    '</p>'+
                    '</li>' ),
                    $img = $li.find('img'),

                    $btns = $('<div class="file-panel">' +
                            '<span class="cancel">删除</span></div>').appendTo( $li );


            // $list为容器jQuery实例
            // $queue.append( $li );
            // refreshPic();
            // 创建缩略图
            // 如果为非图片文件，可以不用调用此方法。
            // thumbnailWidth x thumbnailHeight 为 100 x 100
            uploader.makeThumb( file, function( error, src ) {   //webuploader方法
                if ( error ) {
                    $img.replaceWith('<span>不能预览</span>');
                    return;
                }

                $img.attr( 'src', src );
            }, thumbnailWidth, thumbnailHeight );

            // uploader.upload();
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
            refreshPic();
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

            console.log("上传成功");
        });

        // 完成上传完了，成功或者失败，先删除进度条。
        uploader.on( 'uploadComplete', function( file ) {
            $( '#'+file.id ).find('.progress').remove();
        });
        
        $btn.on( 'click', function() {
            console.log("上传...");
            uploader.upload();
            console.log("上传成功");
        });
        
        
        var uploader1 = WebUploader.create({
            // 选完文件后，是否自动上传。
            auto: true,

            // swf文件路径
            swf: '${request.contextPath}/static/webuploader/Uploader.swf',
            // 文件接收服务端。
            server: '${request.contextPath}/familydear/threeInTwo/saveAttachment',

            formData: {
                'objId':'${famDearThreeInTwo.id!}',
                'objType':'famDearThreeInTwoFile',
            },

            // 选择文件的按钮。可选。
            // 内部根据当前运行是创建，可能是input元素，也可能是flash.
            pick: '#fileUp',
            duplicate:true,
            
            method:'POST',
        });
        
        // 当有文件添加进来的时候
        uploader1.on( 'fileQueued', function( file ) {  // webuploader事件.当选择文件后，文件被加载到文件队列中，触发该事件。等效于 uploader.onFileueued = function(file){...} ，类似js的事件定义。
            var $li = $( '<li id="' + file.id + '">' +
                    '<p class="imgWrap">' +
                    '<img>' +
                    '</p>'+
                    '</li>' ),
                    $img = $li.find('img'),

                    $btns = $('<div class="file-panel">' +
                            '<span class="cancel">删除</span></div>').appendTo( $li );


            // $list为容器jQuery实例
            // $queue.append( $li );
            // refreshPic();
            // 创建缩略图
            // 如果为非图片文件，可以不用调用此方法。
            // thumbnailWidth x thumbnailHeight 为 100 x 100
            uploader1.makeThumb( file, function( error, src ) {   //webuploader方法
                if ( error ) {
                    $img.replaceWith('<span>不能预览</span>');
                    return;
                }

                $img.attr( 'src', src );
            }, thumbnailWidth, thumbnailHeight );

            // uploader.upload();
        });
        // 文件上传过程中创建进度条实时显示。
        uploader1.on( 'uploadProgress', function( file, percentage ) {
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
        uploader1.on( 'uploadSuccess', function( file ) {
            refreshFile();
            $( '#'+file.id ).addClass('upload-state-done');
        });

        // 文件上传失败，显示上传出错。
        uploader1.on( 'uploadError', function( file ) {
            var $li = $( '#'+file.id ),
                    $error = $li.find('div.error');

            // 避免重复创建
            if ( !$error.length ) {
                $error = $('<div class="error"></div>').appendTo( $li );
            }

            console.log("上传成功");
        });

        // 完成上传完了，成功或者失败，先删除进度条。
        uploader1.on( 'uploadComplete', function( file ) {
            $( '#'+file.id ).find('.progress').remove();
        });
        
        $btn.on( 'click', function() {
            console.log("上传...");
            uploader1.upload();
            console.log("上传成功");
        });
    });
    
    function refreshPic(){
    	var id=$("#id").val();
        $("#ticketImages").load("${request.contextPath}/familydear/threeInTwo/showAllpic?id=" + id);

    }
    
    function refreshFile(){
    	var id=$("#id").val();
        $("#desFile").load("${request.contextPath}/familydear/threeInTwo/showAllFile?id=" + id);
    }
    
    $(function () {
        layer.photos({
        	closeBtn:1,
            shade: .6,
            photos:'.js-layer-photos',
            shift: 1
        });
    });
    function delPic1(id,type){
        var picIds;
        picIds = $("#picIds").val()+","+id;
        $("#picIds").val(picIds)
        $.ajax({
            url:'${request.contextPath}/familydear/threeInTwo/delPic',
            data: {"id":id},
            type:'post',
            success:function(data) {
                var jsonO = JSON.parse(data);
                if(jsonO.success){
                	if(type==1){
                    	refreshPic();
                	}else{
                		refreshFile();
                	}
                }
                else{
                    
                }
            },
            error:function(XMLHttpRequest, textStatus, errorThrown){}
        });
    }
    
</script>