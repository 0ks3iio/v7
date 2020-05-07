<div class="box box-default padding-20">
    <form id="csvForm" enctype='multipart/form-data' method="post">
        <div class="form-horizontal" id="myForm">
            <div class="form-group">
                <h3 class="col-sm-2 control-label bold">基本设置</h3>
            </div>
            <input style="display:none"/>
            <div class="form-group">
                <label class="col-sm-2 control-label">数据源名称</label>
                <div class="col-sm-6">
                    <input type="text" id="excelName" name="name" value="${database.name!}"
                           class="form-control js-file-name width-1of1" maxlength="50" nullable="false" placeholder="请输入数据源名称">
                </div>
            </div>
            <div class="form-group">
                <label class="col-sm-2 control-label">EXCEL地址</label>
                <div class="col-sm-6">
                    <div class="uploader-wrap">
                        <div class="files-wrap scrollbar-made">
                            <div class="empty-file">
                                <img src="${request.contextPath}/static/bigdata/images/excel-icon-grey.png"/>
                            </div>
                        </div>
                        <div class="file-add-wrap">
                            <div class="ellipsis">
                                已添加 <span class="color-blue fileNum">0</span> 张Excel文件
                            </div>
                            <div class="add-file pos-right clearfix">
                                <div class="float-right fileParent">
                                    <label for="file" id="upFile" class="no-margin">添加</label>
                                    <input type="file" nullable="false" class="file"
                                           accept="application/vnd.ms-excel, application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"
                                           id="file" name="file">
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            <div class="form-group">
                <label class="col-sm-2 control-label">表头设置</label>
                <div class="col-sm-10">
                    <div class="">
                        <label class="choice">
                            <input type="radio"  name="headType" value="1" onchange="changeHeadType(1)"
                                   <#if (database.headType!1) == 1>checked="checked"</#if> >
                            <span class="choice-name">把第一行作为表头</span>
                        </label>
                        <label class="choice">
                            <input type="radio"  name="headType" value="2" onchange="changeHeadType(2)"
                                   <#if (database.headType!1) == 2>checked="checked"</#if> >
                            <span class="choice-name">自动生成表头</span>
                        </label>
                    </div>

                    <div id="previewDataList" style="margin-top: 20px;">

                    </div>
                </div>
            </div>
            <div class="form-group">
                <label class="col-sm-2 control-label no-padding-right">备注：</label>
                <div class="col-sm-6">
                    <textarea name="remark" id="remark" type="text/plain" nullable="false" maxLength="200"
                              style="width:100%;height:160px;">${database.remark!}</textarea>
                </div>
            </div>
            <div class="form-group">
                <div class="col-sm-8 col-sm-offset-2">
                    <button class="btn btn-blue js-save" type="button" id="saveBtn">保存</button>
                </div>
            </div>
            <input type="hidden" name="id" id="id" value="${database.id!}">
            <input type="hidden" id="fileName" value="${database.fileName!}">
        </div>
    </form>
</div>
<script type="text/javascript">
    var isSubmit = false;
    $(function () {
        $('.page-content').css('padding-bottom', '20px');

        $('#previewDataList').css({
            'max-height': $(window).height() - $('#previewDataList').offset().top
        });

        initData();

        $('.stu-tea span').on('click', function () {
            $(this).addClass('active').siblings().removeClass('active')
        });

        $(".fileParent").on("change", '.file', function () {
            var urlArr = this.value.split("\\");
            if (this && this.files && this.files[0]) {
                var fileUrl = URL.createObjectURL(this.files[0]);
                var fileName = urlArr[urlArr.length - 1];
                var str = '<div class="every-file" style="height:148;line-height:148px;">\
				        		<img src="${request.contextPath}/static/bigdata/images/excel-icon-grey.png" class="pos-left"/>\
				        		<div class="ellipsis pointer">' + fileName + '</div>\
				        		<img src="${request.contextPath}/static/bigdata/images/remove-grey.png" class="pos-right js-remove"/>\
				        	</div>';
                $('.empty-file').hide();
                $('.files-wrap').append(str);
                $('.fileNum').text($('.files-wrap').find('.every-file').length);
                $('.every-file').css('height', '148px');
                $('#upFile').hide();
                $.ajax({
                    url: '${request.contextPath}/bigdata/nosqlDatasource/previewExcelFile',　　　　　　　　　　//上传地址
                    type: 'POST',
                    cache: false,
                    data: new FormData($('#csvForm')[0]),　　　　　　　　　　　　　//表单数据
                    processData: false,
                    contentType: false,
                    success: function (data) {
                        $('#previewDataList').empty().append(data);
                    }
                });
            }
        });

        $('body').on('mouseenter', '.every-file', function () {
            $(this).addClass('active');
        }).on('mouseleave', '.every-file', function () {
            $(this).removeClass('active');
        });

        $('body').on('click', '.js-remove', function () {
            $(this).parent('.every-file').remove();
            $('.fileNum').text($('.files-wrap').find('.every-file').length);
            if ($('.files-wrap').find('.every-file').length == 0) {
                $('.empty-file').show();
                $('#upFile').show();
                $('#previewDataList').empty();
            }
        });

        $('#saveBtn').on('click', function () {
            if (isSubmit) {
                return;
            }
            isSubmit = true;

            if ($('#excelName').val() == "") {
                $('.page-content').scrollTop(0);
                layer.tips("不能为空", "#excelName", {
                    tipsMore: true,
                    tips: 3
                });
                isSubmit = false;
                return;
            }

            if ($('#excelName').val().replace(/[\u0391-\uFFE5]/g,"aa").length > 50) {
                $('.page-content').scrollTop(0);
                isSubmit = false;
                layer.tips("名称长度不能超过50", "#excelName", {
                    tipsMore: true,
                    tips: 3
                });
                return;
            }

            if ($('.fileNum').text() !='1') {
                layer.tips("文件不能为空", ".files-wrap", {
                    tipsMore: true,
                    tips: 3
                });
                isSubmit = false;
                return;
            }

            var options = {
                url: "${request.contextPath}/bigdata/nosqlDatasource/saveFile?type=excel",
                dataType: 'json',
                success: function (data) {
                    if (!data.success) {
                        layerTipMsg(data.success, data.msg, "");
                        isSubmit = false;
                    } else {
                        showLayerTips('success',data.msg,'t');
                        showList('1');
                    }
                },
                clearForm: false,
                resetForm: false,
                type: 'post',
                error: function (XMLHttpRequest, textStatus, errorThrown) {
                }//请求出错
            };
            $("#csvForm").ajaxSubmit(options);
        });
    });

    function initData() {
        var id = $('#id').val();
        if (id == null || id == '') {
            return;
        }


        var str = '<div class="every-file" style="height:148;line-height:148px;">\
				        		<img src="${request.contextPath}/static/bigdata/images/excel-icon-grey.png" class="pos-left"/>\
				        		<div class="ellipsis pointer">' + $('#fileName').val() + '</div>\
				        		<img src="${request.contextPath}/static/bigdata/images/remove-grey.png" class="pos-right js-remove"/>\
				        	</div>';
        $('.empty-file').hide();
        $('.files-wrap').append(str);
        $('.fileNum').text($('.files-wrap').find('.every-file').length);
        $('.every-file').css('height', '148px');
        $('#upFile').hide();

        $.ajax({
            url: '${request.contextPath}/bigdata/nosqlDatasource/previewExcelFile',　　　　　　　　　　//上传地址
            type: 'POST',
            data: {id: id},
            success: function (data) {
                $('#previewDataList').append(data);
            }
        });
    }

    function changeHeadType(headType) {
        $.ajax({
            url: '${request.contextPath}/bigdata/nosqlDatasource/previewExcelFile',　　　　　　　　　　//上传地址
            type: 'POST',
            cache: false,
            data: new FormData($('#csvForm')[0]),　　　　　　　　　　　　　//表单数据
            processData: false,
            contentType: false,
            success: function (data) {
                $('#previewDataList').empty().append(data);
            }
        });
    }

    function changeExcelTab(obj) {
        var tabKey = $(obj).html();
        $.ajax({
            url: '${request.contextPath}/bigdata/nosqlDatasource/previewExcelFile?tabKey=' + tabKey,　　　　　　　　　　//上传地址
            type: 'POST',
            cache: false,
            data: new FormData($('#csvForm')[0]),　　　　　　　　　　　　　//表单数据
            processData: false,
            contentType: false,
            success: function (data) {
                $('#previewDataList').empty().append(data);
            }
        });
    }
</script>