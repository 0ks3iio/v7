<div class="box-standard">
    <div class="box-standard-part box-catalog">
        <div class="box-part-title">
            <span>目录管理</span>
            <div class="pos-icon show js-add-directorys">
                <i class="iconfont icon-adddirectory-b"></i>&nbsp;&nbsp;添加
            </div>
        </div>
        <div class="box-part-content">
        <div class="directory-wrap">
            <ul class="directory-tree">
            <#if folderTree?size gt 0>
                        <#list folderTree as first>
                            <li class="" folder_id="${first.id!}">
                                <a href="javascript:void(0);">
                                    <#if first.childFolder?size gt 0>
                                        <span class="multilevel"><i class="iconfont icon-caret-down"></i></span>
                                    </#if>
                                    <span title="${first.folderName!}">${first.folderName!}</span>
                                    <div class="pos-icon" folder_id="${first.id!}">
                                        <i class="iconfont icon-adddirectory-b js-add-directory"></i>
                                        <i class="iconfont icon-editor-fill js-edit-directory"></i>
                                        <i class="iconfont icon-delete-bell js-remove-directory"></i>
                                    </div>
                                </a>
                                <#if first.childFolder?size gt 0>
                                    <ul class="collapse" id="${first.id!}">
                                        <#list first.childFolder as second>
                                            <li class="" folder_id="${second.id!}">
                                                <a href="javascript:void(0);">
                                                    <#if second.childFolder?size gt 0>
                                                        <span class="multilevel"><i
                                                                class="iconfont icon-caret-down"></i></span>
                                                    </#if>
                                                    <span title="${second.folderName!}">${second.folderName!}</span>
                                                    <div class="pos-icon" folder_id="${second.id!}">
                                                        <i class="iconfont icon-adddirectory-b js-add-directory"></i>
                                                        <i class="iconfont icon-editor-fill js-edit-directory"></i>
                                                        <i class="iconfont icon-delete-bell js-remove-directory"></i>
                                                    </div>
                                                </a>
                                                <#if second.childFolder?size gt 0>
                                                    <ul class="collapse" id="${second.id!}">
                                                        <#list second.childFolder as third>
                                                            <li class="" folder_id="${third.id!}">
                                                                <a href="javascript:void(0);">
                                                                    <span title="${third.folderName!}">${third.folderName!}</span>
                                                                    <div class="pos-icon" folder_id="${third.id!}">
                                                                        <i class="iconfont icon-editor-fill js-edit-directory"></i>
                                                                        <i class="iconfont icon-delete-bell js-remove-directory"></i>
                                                                    </div>
                                                                </a>
                                                            </li>
                                                        </#list>
                                                    </ul>
                                                </#if>
                                            </li>
                                        </#list>
                                    </ul>
                                </#if>

                            </li>
                        </#list>

            <#else>
                <div class="no-data">
                    <img src="${request.contextPath}/bigdata/v3/static/images/folder-settings/img-directory.png"/>
                    <div>暂无目录，请添加</div>
                </div>
            </#if>
            </ul>
            </div>
        </div>
    </div>

    <div class="box-standard-part box-catalog-part">
        <div class="box-part-title">
            <span>文件夹管理</span>
            <div class="pos-icon js-add-foldfile" id="addFolderBtn">
                <i class="iconfont icon-addfiles-fill"></i>&nbsp;&nbsp;添加
            </div>
        </div>
        <div class="box-part-content pa-5" id="folderDiv">
            <div class="no-data">
                <img src="${request.contextPath}/bigdata/v3/static/images/folder-settings/img-folder.png"/>
                <div>暂无文件夹</div>
            </div>

            <div class="folder-box-wrap">

            </div>
        </div>
    </div>
</div>
<!--右键box-->
<div class="key-box" data-index="">
    <ul>
        <li class="js-remove"><i class="iconfont icon-delete-bell"></i>删除</li>
        <li class="js-edit"><i class="iconfont icon-editor-fill"></i>修改</li>
    </ul>
</div>
<!--弹窗-->
<div class="layer add-folder" id="addFolderDiv">
    <form id="folderSubmitForm">
        <input type="hidden" id="parentId" name="parentId">
        <input type="hidden" id="folderType" name="folderType">
        <div class="form-horizontal form-made">
            <div class="form-group">
                <label class="col-sm-2 control-label"><font style="color:red;">*</font>名称：</label>
                <div class="col-sm-10">
                    <input type="text" name="folderName" id="folderName" maxlength="50" nullable="false" class="form-control" value="">
                </div>
            </div>
    </form>
</div>
<script type="text/javascript">
    $(function () {
        //目录
        $('.directory-tree').on('click', 'a', function (e) {
            e.stopPropagation();
            $('#addFolderBtn').addClass('show');
            if ($(this).find('i:first').hasClass('icon-caret-down')) {
                $(this).find('i:first').removeClass('icon-caret-down').addClass('icon-caret-up');
            } else {
                $(this).find('i:first').removeClass('icon-caret-up').addClass('icon-caret-down');
            }
            $('.directory-tree').find('a').removeClass('active');
            $(this).addClass('active');
            var folderId = $(this).parent().attr('folder_id');
            $('#' + folderId).slideToggle();
            $.ajax({
                url: '${request.contextPath}/bigdata/folder/getChildFolder',
                data: {
                    folderId: folderId
                },
                type: 'POST',
                dataType: 'json',
                success: function (val) {
                    if (!val.success) {
                        showLayerTips4Confirm('error', val.message, 't', null);
                    } else {
                        $('#folderDiv').find('.folder-box-wrap').empty();
                        if (val.data.length > 0) {
                            $('#folderDiv').find('.no-data').hide();
                            $.each(val.data, function (i, v) {
                                var str = '<div class="folder-box" id="' + v.id + '">\
								<div class="folder-facade">\
									<svg class="icon" aria-hidden="true">\
									  <use xlink:href="#icon-folder-fill"></use>\
									</svg>\
									<div class="folder-name" title="'+html2Escape(v.folderName)+'">' + html2Escape(v.folderName) + '</div>\
								</div>\
							</div>';
                                $('#folderDiv').find('.folder-box-wrap').append(str);
                            });
                        } else {
                            $('#folderDiv').find('.no-data').show();
                        }
                    }
                }
            });
        });

        //添加下级目录
        $('.directory-tree').on('click', '.js-add-directory', function (e) {
            e.stopPropagation();

            var parentFolderId = $(this).parent().attr('folder_id');
            $('#parentId').val(parentFolderId);
            $('#folderType').val('1');
            var $self = $(this);
            var isSubmit = false;
            layer.open({
                type: 1,
                title: '添加目录',
                area: ['520px', 'auto'],
                btn: ['确定', '取消'],
                content: $('.add-folder'),
                yes: function (index, layero) {
                    if (isSubmit) {
                        return;
                    }
                    isSubmit = true;

                    var options = {
                        url: '${request.contextPath}/bigdata/folder/saveFolder',
                        dataType: 'json',
                        success: function (val) {
                            if (!val.success) {
                                showLayerTips4Confirm('error', val.message, 't', null);
                            } else {
                                layer.close(index);
                                if ($self.closest('a').next().is('ul')) {
                                    var str = '<li folder_id="'+val.data+'"><a href="javascript:void(0);">\
                                        <span title="'+$('#folderName').val()+'">' + $('#folderName').val() + '</span>\
                                        <div class="pos-icon" folder_id="' + val.data + '">';
                                    if (!$self.closest('ul').hasClass('collapse')) {
                                        str = str + '<i class="iconfont icon-adddirectory-b js-add-directory"></i>';
                                    }
                                    str = str + '<i class="iconfont icon-editor-fill js-edit-directory"></i>\
                                            <i class="iconfont icon-delete-bell js-remove-directory"></i>\
                                        </div></a></li>';

                                    $self.closest('a').next('ul').append(str);
                                } else {
                                    var str = '<ul class="collapse" id="' + parentFolderId + '">\
                                    <li folder_id="'+val.data+'">\
                                        <a href="javascript:void(0);">\
                                            <span title="'+$('#folderName').val()+'">' + $('#folderName').val() + '</span>\
                                            <div class="pos-icon" folder_id="' + val.data + '">';
                                    if (!$self.closest('ul').hasClass('collapse')) {
                                        str = str + '<i class="iconfont icon-adddirectory-b js-add-directory"></i>';
                                    }
                                    str = str + '<i class="iconfont icon-editor-fill js-edit-directory"></i>\
                                            <i class="iconfont icon-delete-bell js-remove-directory"></i>\
                                        </div></a></li></ul>';

                                    $self.closest('a').attr('href', 'javascript:void(0);').prepend('<span class="multilevel"><i class="iconfont icon-caret-down"></i></span>').after(str);
                                }
                                showLayerTips('success', '添加成功', 't');
                                $('#folderName').val('');
                            }
                            isSubmit = false;
                        },
                        clearForm: false,
                        resetForm: false,
                        type: 'post',
                        error: function (XMLHttpRequest, textStatus, errorThrown) {
                            isSubmit = false;
                        }//请求出错
                    };
                    $("#folderSubmitForm").ajaxSubmit(options);
                },
                end: function () {
                    $('#folderName').val('');
                }
            });
        });

        //添加一级目录
        $('.js-add-directorys').on('click', function () {
            event.stopPropagation();

            $('#parentId').val('00000000000000000000000000000000');
            $('#folderType').val('1');
            var isSubmit = false;
            layer.open({
                type: 1,
                title: '添加目录',
                area: ['520px', 'auto'],
                btn: ['确定', '取消'],
                content: $('.add-folder'),
                yes: function (index, layero) {
                    if (isSubmit) {
                        return;
                    }
                    isSubmit = true;

                    if ($('#folderName').val() == '') {
                        showLayerTips4Confirm('error', '名称不能为空!', 't', null);
                        isSubmit = false;
                        return;
                    }

                    var options = {
                        url: '${request.contextPath}/bigdata/folder/saveFolder',
                        dataType: 'json',
                        success: function (val) {
                            if (!val.success) {
                                showLayerTips4Confirm('error', val.message, 't', null);
                            } else {
                                layer.close(index);
                                var str = '<li folder_id="'+val.data+'">\
                                    <a href="javascript:void(0);" data-toggle="collapse">\
                                        <span title="'+$('#folderName').val()+'">' + $('#folderName').val() + '</span>\
                                        <div class="pos-icon" folder_id=' + val.data + '>\
                                            <i class="iconfont icon-adddirectory-b js-add-directory"></i>\
                                            <i class="iconfont icon-editor-fill js-edit-directory"></i>\
                                            <i class="iconfont icon-delete-bell js-remove-directory"></i>\
                                        </div>\
                                    </a>\
                                </li>';
                                $('.directory-tree').find('.no-data').hide();
                                $('.directory-tree').append(str);
                                showLayerTips('success', '添加成功', 't');
                                $('#folderName').val('');
                            }
                            isSubmit = false;
                        },
                        clearForm: false,
                        resetForm: false,
                        type: 'post',
                        error: function (XMLHttpRequest, textStatus, errorThrown) {
                            isSubmit = false;
                        }//请求出错
                    };
                    $("#folderSubmitForm").ajaxSubmit(options);
                },
                end: function () {
                    $('#folderName').val('');
                }
            });
        });
        //修改目录名
        $('.directory-tree').on('click', '.js-edit-directory', function () {
            var folderId = $(this).parent().attr('folder_id');
            var target = $(this).parent().prev();
            var text = target.html();
            target.empty().append('<input type="text" maxlength="50" name="" id="" value="' + text + '" />');
            target.find('input').select().on('blur', function () {
                var val = target.find('input').val().trim() == '' ? '暂无' : target.find('input').val();
                if (val != text) {
                    $.ajax({
                        url: '${request.contextPath}/bigdata/folder/modifyFolderName',
                        data: {
                            id: folderId,
                            folderName: val
                        },
                        type: 'POST',
                        dataType: 'json',
                        success: function (result) {
                            if (!result.success) {
                                showLayerTips4Confirm('error', result.message, 't', null);
                                target.empty().text(text);
                            } else {
                                target.empty().text(val);
                                target.attr('title', val);
                            }
                        }
                    });
                } else {
                    target.empty().text(val);
                }
            });
            return false;
        });
        //删除目录
        $('.directory-tree').on('click', '.js-remove-directory', function (e) {
            e.stopPropagation();
            var folderId = $(this).parent().attr('folder_id');
            var $self = $(this);
            showConfirmTips('prompt', '删除目录', '确定要删除该目录?', function (index) {
                $.ajax({
                    url: '${request.contextPath}/bigdata/folder/deleteDirectory',
                    data: {
                        folderId: folderId
                    },
                    type: 'POST',
                    dataType: 'json',
                    success: function (val) {
                        layer.close(index);
                        if (!val.success) {
                            showLayerTips4Confirm('error', val.message, 't', null);
                        } else {
                            showLayerTips('success', '删除成功', 't');
                            var $ul = $('.directory-tree').find('li[folder_id="'+folderId+'"]').parent();
                            $self.closest('li').remove();
                            if ($ul.find('li').length < 1) {
                                $ul.prev().find('span.multilevel').remove();
                                if ($ul.hasClass('directory-tree')) {

                                } else {
                                    $ul.remove();
                                }
                            }
                            if ($('.directory-tree').find('li').length == 0) {
                                $('.directory-tree').find('.no-data').show();
                            }
                            $('#addFolderBtn').removeClass('show');
                        }
                    }
                });
            });
        });

        //添加文件夹
        $('.js-add-foldfile').on('click', function () {
            var directoryId = $('.directory-tree').find('a.active').parent().attr('folder_id');

            $('#parentId').val(directoryId);
            $('#folderType').val('2');
            var isSubmit = false;
            layer.open({
                type: 1,
                title: '添加文件夹',
                area: ['520px', 'auto'],
                btn: ['确定', '取消'],
                content: $('.add-folder'),
                yes: function (index, layero) {
                    if (isSubmit) {
                        return;
                    }
                    isSubmit = true;

                    var options = {
                        url: '${request.contextPath}/bigdata/folder/saveFolder',
                        dataType: 'json',
                        success: function (val) {
                            if (!val.success) {
                                showLayerTips4Confirm('error', val.message, 't', null);
                            } else {
                                layer.close(index);
                                var str = '<div class="folder-box" id="'+val.data+'">\
                                    <div class="folder-facade">\
                                        <svg class="icon" aria-hidden="true">\
                                          <use xlink:href="#icon-folder-fill"></use>\
                                        </svg>\
                                        <div class="folder-name" title="'+html2Escape($('#folderName').val())+'">'+html2Escape($('#folderName').val())+'</div>\
                                    </div>\
                                </div>';
                                $('#folderDiv').find('.no-data').hide();
                                $('.folder-box-wrap').append(str);
                                showLayerTips('success', '添加成功', 't');
                            }
                            isSubmit = false;
                        },
                        clearForm: false,
                        resetForm: false,
                        type: 'post',
                        error: function (XMLHttpRequest, textStatus, errorThrown) {
                            isSubmit = false;
                        }//请求出错
                    };
                    $("#folderSubmitForm").ajaxSubmit(options);
                },
                end: function () {
                    $('#folderName').val('');
                }
            });
        });
        //屏幕点击
        $('body').on('click', function (e) {
            if ($('.key-box').hasClass('active')) {
                if (e.target.className !== "key-box") {
                    $('.key-box').removeClass('active');
                }
            }
        });
        //文件夹右键
        $('.folder-box-wrap').on('contextmenu', '.folder-box', function (e) {
            e.preventDefault();
            $('.key-box').addClass('active').data('index', $(this).index()).css({
                top: e.pageY + 2,
                left: e.pageX
            });
        });
        //删除
        $('.key-box').on('click', '.js-remove', function (e) {
            var folderId = $('.folder-box').eq($('.key-box').data('index')).attr('id');

            showConfirmTips('prompt', '删除文件夹', '确定要删除该文件夹?', function (index) {
                layer.close(index);
                $.ajax({
                    url: '${request.contextPath}/bigdata/folder/deleteFolder',
                    data: {
                        folderId: folderId
                    },
                    type: 'POST',
                    dataType: 'json',
                    success: function (val) {
                        if (!val.success) {
                            showLayerTips4Confirm('error', val.message, 't', null);
                        } else {
                            showLayerTips('success', '删除成功', 't');
                            $('.folder-box').eq($('.key-box').data('index')).remove();
                            if ($('.folder-box').length == 0) {
                                $('.folder-box-wrap').siblings('.no-data').show();
                            }
                        }
                    }
                });
            });
        });
        //编辑
        $('.key-box').on('click', '.js-edit', function (e) {
            var folderId = $('.folder-box').eq($('.key-box').data('index')).attr('id');
            var target = $('.folder-box').eq($('.key-box').data('index')).find('.folder-name');
            var text = html2Escape(target.text());
            target.empty().append('<input type="text" maxlength="50" name="" id="" value="' + text + '" />');
            target.find('input').select().on('blur', function () {
                var val = target.find('input').val().trim() == '' ? '暂无' : target.find('input').val();
                if (val != text) {
                    $.ajax({
                        url: '${request.contextPath}/bigdata/folder/modifyFolderName',
                        data: {
                            id: folderId,
                            folderName: val
                        },
                        type: 'POST',
                        dataType: 'json',
                        success: function (result) {
                            if (!result.success) {
                                showLayerTips4Confirm('error', result.message, 't', null);
                                target.empty().text(text);
                            } else {
                                target.empty().text(val);
                                target.attr('title', val);
                            }
                        }
                    });
                } else {
                    target.empty().text(val);
                }
            });
        });

        /**
         * 主要用于处理""，防止用户创建带有""的文件夹名字后光标显示不全以及
         * 点击修改按钮后""后面的内容直接消失
         * @param sHtml
         * @returns {*}
         */
        function html2Escape(sHtml) {
            return sHtml.replace(/[<>&"]/g,function(c){return {'<':'&lt;','>':'&gt;','&':'&amp;','"':'&quot;'}[c];});
        }

    })
</script>