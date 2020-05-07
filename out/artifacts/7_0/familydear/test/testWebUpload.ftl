<#import "/fw/macro/htmlcomponent.ftl" as htmlcom>
<script type="text/javascript" src="../familydear/test/js/jquery-1.7.2.min.js"></script>
<script type="text/javascript" src="../familydear/test/js/img-adapter.js"></script>
<script type="text/javascript" src="${request.contextPath}/static/remote/openapi/js/jquery.Layer.js"></script>
<link rel="stylesheet" type="text/css" href="../familydear/test/css/style1.css">
<link rel="stylesheet" type="text/css" href="../familydear/test/css/webuploader.css">

<#import "/fw/macro/webUploaderMacro.ftl" as upload />

<!--引入JS-->
<script type="text/javascript" src="../familydear/test/js/webuploader.js"></script>
	<#--<script type="text/javascript" src="${request.contextPath}/static/fw/js/jquery-ui.min.js"></script>-->
	<#--<script type="text/javascript" src="${request.contextPath}/static/fw/js/jquery.form.js"></script>-->
<div id="uploader" class="wu-example">
    <!--用来存放文件信息-->
    <div id="thelist" class="uploader-list">
        <ul class="filelist" id="filelist">
            <#if actDetails?exists&& (actDetails?size > 0)>
                        <#list actDetails as ad>
                            <li>
                                <p class="imgWrap">
                                    <img   data-img-action="adapte" layer-src="${request.contextPath}/test/showPic?id=${ad.id!}&showOrigin=1" src="${request.contextPath}/test/showPic?id=${ad.id!}&showOrigin=0" alt="">
                                </p>
                                <div class="file-panel" >
                                    <input id="picId" type="hidden" value=${ad.id!}>
                                    <span class="cancel">删除</span>
                                </div>
                            </li>
                            <#--<div class="card-item">-->
                                <#--<div class="card-content">-->
                                    <#--<label class="card-checkbox">-->
                                        <#--<input type="checkbox"  class="wp detail-box" value="${ad.id!}">-->
                                        <#--<span class="lbl"></span>-->
                                    <#--</label>-->
                                    <#--<div class="card-tools">-->
                                        <#--<a href="" data-toggle="dropdown"><i class="fa fa-angle-down"></i></a>-->
                                        <#--<div class="dropdown-menu card-tools-menu">-->
                                            <#--<a class="detail-del" href="javascript:;" detailid="${ad.id!}">删除</a>-->
                                        <#--</div>-->
                                    <#--</div>-->
                                    <#--<a href="javascript:void(0);">-->
                                        <#--<div class="card-img ">-->
                                            <#--<img   data-img-action="adapte" layer-src="${request.contextPath}/test/showPic?id=${ad.id!}&showOrigin=1" src="${request.contextPath}/test/showPic?id=${ad.id!}&showOrigin=0" alt="">-->
                                        <#--</div>-->
                                    <#--</a>-->
                                <#--</div>-->

                            <#--</div>-->
                        </#list>
            </#if>
        </ul>
    </div>
    <div class="btns">
        <div id="picker">选择文件</div>
        <button id="ctlBtn" class="btn btn-default">开始上传</button>
    </div>
</div>

<#--<@upload.picUpload businessKey="${photoDirId!}" extensions="jpg,jpeg,png" contextPath="${request.contextPath}" resourceUrl="${resourceUrl}" size="1" fileNumLimit="1" handler="faceImagChange">-->
						<#--<a href="javascript:;" class="btn btn-blue js-addPhotos"><#if hasPic?default(false)>重新上传<#else>选择照片</#if></a>-->
						<#--<!--这里的id就是存放附件的文件夹地址 必须维护&ndash;&gt;-->
						<#--<input type="hidden" id="${photoDirId!}-path" value="">-->
<#--</@upload.picUpload>-->

<!-- page specific plugin scripts -->
<script type="text/javascript">
    $(function(){
        var $wrap = $('#uploader');
        // 图片容器
        // var $queue = $( '<ul class="filelist"></ul>' )
        var $queue = $("#filelist");
        /*init webuploader*/
        var $list=$("#thelist");  //这几个初始化全局的百度文档上没说明，好蛋疼。
        var $btn =$("#ctlBtn");   //开始上传
        var thumbnailWidth = 100;   //缩略图高度和宽度 （单位是像素），当宽高度是0~1的时候，是按照百分比计算，具体可以看api文档
        var thumbnailHeight = 100;

        var uploader = WebUploader.create({
            // 选完文件后，是否自动上传。
            auto: true,

            // swf文件路径
            swf: '${request.contextPath}/static/webuploader/Uploader.swf',
            // 文件接收服务端。
            server: '${request.contextPath}/test/save',

            formData: {
                'objId':'${id!}',
                'objType':'1'
            },

            // 选择文件的按钮。可选。
            // 内部根据当前运行是创建，可能是input元素，也可能是flash.
            pick: '#picker',
            dnd: '#uploader .uploader-list',
            paste: '#uploader',

            // 只允许选择图片文件。
            accept: {
                title: 'Images',
                extensions: 'gif,jpg,jpeg,bmp,png',
                mimeTypes: 'image/*'
            },
            method:'POST',
        });
        $("#filelist").find("li").each(function () {
            var $this = $(this);
            debugger;
            $this.on( 'mouseenter', function() {
                $this.find(".file-panel").stop().animate({height: 30});
            });
            $this.on( 'mouseleave', function() {
                $this.find(".file-panel").stop().animate({height: 0});
            });
        });
        $("#filelist").find("li").find(".file-panel").on( 'click', function() {
            var index = $(this).index(),
                    deg;
            var id = $(this).find("#picId").val();

            switch ( index ) {
                case 0:
                    removeFile( id);
                    return;

                case 1:
                    removeFile( id);
                    break;

                case 2:
                    removeFile( id);
                    break;
            }

            if ( supportTransition ) {
                deg = 'rotate(' + file.rotation + 'deg)';
                $wrap.css({
                    '-webkit-transform': deg,
                    '-mos-transform': deg,
                    '-o-transform': deg,
                    'transform': deg
                });
            } else {
                $wrap.css( 'filter', 'progid:DXImageTransform.Microsoft.BasicImage(rotation='+ (~~((file.rotation/90)%4 + 4)%4) +')');
                // use jquery animate to rotation
                // $({
                //     rotation: rotation
                // }).animate({
                //     rotation: file.rotation
                // }, {
                //     easing: 'linear',
                //     step: function( now ) {
                //         now = now * Math.PI / 180;

                //         var cos = Math.cos( now ),
                //             sin = Math.sin( now );

                //         $wrap.css( 'filter', "progid:DXImageTransform.Microsoft.Matrix(M11=" + cos + ",M12=" + (-sin) + ",M21=" + sin + ",M22=" + cos + ",SizingMethod='auto expand')");
                //     }
                // });
            }


        });
        // 当有文件添加进来的时候
        uploader.on( 'fileQueued', function( file ) {  // webuploader事件.当选择文件后，文件被加载到文件队列中，触发该事件。等效于 uploader.onFileueued = function(file){...} ，类似js的事件定义。
            // var $li = $(
            //         '<div id="' + file.id + '" class="file-item thumbnail">' +
            //         '<img>' +
            //         '<div class="info">' + file.name + '</div>' +
            //         '</div>'
            //         ),
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

            $error.text('上传失败');
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
    });

    // 负责view的销毁
    function removeFile( id) {
        debugger;
        alert(id);
        deletePic(id)
        <#--var url = "${request.contextPath}/test/showPic?id=${ad.id!}";-->
        // var $li = $('#'+file.id);
        //
        // delete percentages[ file.id ];
        // updateTotalProgress();
        // $li.off().find('.file-panel').off().end().remove();
    }

    <#--function refreshPic(){-->
        <#--var id = $("#testId").val();-->
        <#--$("#images").load("${request.contextPath}/test/edit?id=" + id);-->
    <#--}-->

    function deletePic(id){
        $.ajax({
            url:'${request.contextPath}/test/delete',
            data: {"ids":id},
            type:'post',
            success:function(data) {
                var jsonO = JSON.parse(data);
                if(jsonO.success){
                    // layer.closeAll();
                    // layerTipMsg(jsonO.success,"删除成功",jsonO.msg);
                    refreshPic();
                }
                else{
                    // layer.closeAll();
                    // layerTipMsg(jsonO.success,"删除失败",jsonO.msg);
                }
            },
            error:function(XMLHttpRequest, textStatus, errorThrown){}
        });
    }

</script>


