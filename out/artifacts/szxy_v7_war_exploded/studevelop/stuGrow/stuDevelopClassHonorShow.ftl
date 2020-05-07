<title>活动图片列表</title>
<link rel="stylesheet" type="text/css" href="${request.contextPath}/static/webuploader/webuploader.css"/>
<script type="text/javascript" src="${request.contextPath}/static/webuploader/webuploader.js" />

<#--<#include "activityConstant.ftl" />-->
<#assign hasData = false />

<#if actDetails?exists && actDetails?size gt 0>
    <#assign hasData = true />
</#if>
<div class="filter">
    <div class="state-default">
        <div class="filter-item">
            <button type="button" class="btn btn-blue" id="filePickerButton">上传图片</button>
            <div id="filePicker" style="display:none;">上传图片</div>
        </div>
    <#if hasData>
        <div class="filter-item">
            <button type="button" class="btn btn-blue js-toManage">批量管理</button>
        </div>
    </#if>
    </div>
    <div class="state-inManage hidden">
        <div class="filter-item">
            <label><input type="checkbox" id="allCheck" class="wp"><span class="lbl"> 全选</span></label>
        </div>
        <div class="filter-item"><button type="button"  class="btn btn-danger js-delete">删除</button></div>
        <div class="filter-item"><button type="button"  class="btn btn-blue js-confirm">取消</button></div>
    </div>
</div>
<div class="card-list card-list-sm js-layer-photos clearfix">
<#if hasData>
    <#list actDetails as ad>
        <div class="card-item">
            <div class="card-content">
                <label class="card-checkbox">
                    <input type="checkbox"  class="wp detail-box" value="${ad.id!}">
                    <span class="lbl"></span>
                </label>
                <div class="card-tools">
                    <a href="" data-toggle="dropdown"><i class="fa fa-angle-down"></i></a>
                    <div class="dropdown-menu card-tools-menu">
                        <a class="detail-del" href="javascript:;" detailid="${ad.id!}">删除</a>
                    </div>
                </div>
                <a href="javascript:void(0);">
                    <div class="card-img ">
                        <img   data-img-action="adapte" layer-src="${request.contextPath}/studevelop/common/attachment/showPic?id=${ad.id!}&showOrigin=1" src="${request.contextPath}/studevelop/common/attachment/showPic?id=${ad.id!}&showOrigin=0" alt="">
					</div>
                </a>
            </div>

        </div>
    </#list>
</#if>
</div>
<script src="${request.contextPath}/studevelop/js/img-adapter.js"></script>
<script>
    <#-- ==========================pic start====================================================-->
    var uploader = WebUploader.create({

        // 选完文件后，是否自动上传。
        auto: true,
        // swf文件路径
        swf: '${request.contextPath}/static/webuploader/Uploader.swf',
        // 文件接收服务端。
        server: '${request.contextPath}/studevelop/common/attachment/save',
        // 表单数据
        formData :{
            'objId':'${id!}',
            'objType':'${actType}'
        },
        // 文件数量
//        fileNumLimit : 10,
        // 单个文件大小 5M
        fileSingleSizeLimit : 5*1024*1024,

        // 选择文件的按钮。可选。
        // 内部根据当前运行是创建，可能是input元素，也可能是flash.
        pick: {
            id:'#filePicker',
            innerHTML : '上传图片',
            multiple:true
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
            width: 272,
            height: 204,
            // 图片质量，只有type为`image/jpeg`的时候才有效。
            quality: 70,

            // 是否允许放大，如果想要生成小图的时候不失真，此选项应该设置为false.
            allowMagnify: false,

            // 是否允许裁剪。
            crop: true
        }
    });

    // 当有文件添加进来的时候
    uploader.on( 'fileQueued', function( file ) {
        var $li = $(
                '<div id="' + file.id + '" class="file-item thumbnail">' +
                '<img>' +
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

        // 创建缩略图
        // 如果为非图片文件，可以不用调用此方法。
        // thumbnailWidth x thumbnailHeight 为 100 x 100
        uploader.makeThumb( file, function( error, src ) {
            if ( error ) {
                $img.replaceWith('<span>不能预览</span>');
                return;
            }

            $img.attr( 'src', src );
        }, 130, 130 );
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
        refreshList();
    });
    <#-- ==========================pic end====================================================-->



    var toDelIds = '';
    $(function(){
        $('#filePickerButton').on('click',function(){
            $('#filePicker input:file').click();
        });

        // 批量管理
        $('.js-toManage').on('click',function(){
            $('.state-default').addClass('hidden');
            $('.state-inManage').removeClass('hidden');
            $('.card-list').addClass('in-manage');
        });

        $("#allCheck").on('click',function () {

            if($(this).is(":checked")){
                $('input.detail-box').each(function () {
                    if(!$(this).is(":checked")){

                        $(this).prop("checked",true);
                    }

                })
            }else{
                $('.wp.detail-box').each(function () {
                    $(this).removeAttr("checked");
                })
            }

        })
        // 批量删除
        $('.js-delete').on('click',function(){
            $('.detail-box').each(function(){
                if($(this).is(":checked")){
                    toDelIds+=($(this).val()+',');
                }
            });
            if(toDelIds == ''){
                layerTipMsg(false,"提示","没有选择要删除的图片！");
                return;
            }

            showConfirmSuccess('确定要删除该图片吗？','确认',deletePic);
        });

        $('.js-confirm').on('click',function(){
            $('.state-default').removeClass('hidden');
            $('.state-inManage').addClass('hidden');
            $('.card-list').removeClass('in-manage');
        });

        $('.detail-del').on('click',function(){
            var cid = $(this).attr('detailid');
            toDelIds = cid;
            showConfirmSuccess('确定要删除该图片吗？','确认',deletePic);
        });
       
		// 浏览图片
		layer.photos({
			shade: .6,
			photos:'.js-layer-photos',
			shift: 5
		});
    });
     // 图片显示适配
    setTimeout(function(){
    	imgAdapter($('img[data-img-action=adapte]')); 
    },300);
    function deletePic(){
        $.ajax({
            url:'${request.contextPath}/studevelop/common/attachment/delete',
            data: {"ids":toDelIds},
            type:'post',
            success:function(data) {
                var jsonO = JSON.parse(data);
                if(jsonO.success){
                    layer.closeAll();
                    layerTipMsg(jsonO.success,"删除成功",jsonO.msg);
                    refreshList();
                }
                else{
                    layer.closeAll();
                    layerTipMsg(jsonO.success,"删除失败",jsonO.msg);
                }
            },
            error:function(XMLHttpRequest, textStatus, errorThrown){}
        });
    }

    function cancelDel(){
        toDelIds = '';
        layer.closeAll();
    }

    function refreshList(){
        $("#upLoadDiv").load("${request.contextPath}/studevelop/classHonor/show/page?" + $("#queryClassHornr").serialize());
    }
</script>