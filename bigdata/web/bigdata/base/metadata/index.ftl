<script src="${request.contextPath}/bigdata/v3/static/plugs/chosen/chosen.jquery.min.js"></script>
<script src="${request.contextPath}/static/bigdata/editor/ace.js"/>
<div class=" overview-set bg-fff metadata metadata clearfix hide">
    <div class="filter-item active" mdType="table">
        <span>表</span>
    </div>
</div>
<div class="box box-default clearfix" style="padding-top: 0px">
    <div class="row height-1of1 no-padding" id="mainDiv">
    </div>
</div>
<div class="layer layer-metadata">
    <div id="metadataDiv">

    </div>
</div>
<script type="text/javascript">
    $(function () {
        function height() {
            $('.js-height').each(function () {
                $(this).css({
                    height: $(window).height() - $(this).offset().top - 20,
                });
            });
            $('.js-chart').each(function () {
                $(this).height('100%').width('100%')
            });
        }

        height();

        // 顶部切换按钮
        $('.metadata .filter-item').on('click', function () {
            $(this).addClass('active').siblings().removeClass('active');
            var mdType = $(this).attr('mdType');
            var url = "${request.contextPath}/bigdata/metadata/system/" + mdType + "/list";
            $("#mainDiv").load(url);
        });

        // 内部切换按钮
        $('#mainDiv').on('click', '.detail-tab', function () {
            $(this).addClass('active').siblings().removeClass('active');
            // 获取id
            var id = $('.directory-tree a.active').attr('id');
            // 获取类型
            var type = $(this).attr('type');
            var mdType = $('.filter-item.active').attr('mdType');
            var url = "${request.contextPath}/bigdata/metadata/" + id + "/" + type + "?mdType=" + mdType + "&isAdmin=true";
            $("#metadataDetailDiv").load(url);
        });

        $('.metadata .filter-item').first().trigger('click');
    });


    function editField(id) {
        var metadataId = $('.directory-tree a.active').attr('id');
        $("#metadataDiv").load('${request.contextPath}/bigdata/metadata/fieldEdit?id=' + id + '&metadataId=' + metadataId);
        var isSubmit = false;
        layer.open({
            type: 1,
            shade: .6,
            title: id == '' ? '新增字段' : '修改字段',
            btn: ['保存', '取消'],
            yes: function (index, layero) {
                if (isSubmit) {
                    return;
                }
                isSubmit = true;

                if ($('#name').val() == "") {
                    layer.tips("不能为空", "#name", {
                        tipsMore: true,
                        tips: 3
                    });
                    isSubmit = false;
                    return;
                }

                if ($('#columnName').val() == "") {
                    layer.tips("不能为空", "#columnName", {
                        tipsMore: true,
                        tips: 3
                    });
                    isSubmit = false;
                    return;
                }
                if ($('#databaseTypeSelect').val() =="char" || $('#databaseTypeSelect').val() =="varchar" ||$('#databaseTypeSelect').val() =="float" || $('#databaseTypeSelect').val() =="double") {
                    if ($('#columnLength').val() == "") {
                        layer.tips("不能为空", "#columnLength", {
                            tipsMore: true,
                            tips: 3
                        });
                        isSubmit = false;
                        return;
                    }
                    if ($('#databaseTypeSelect').val() =="float" || $('#databaseTypeSelect').val() =="double") {
                        if ($('#decimalLength').val() == "") {
                            layer.tips("不能为空", "#decimalLength", {
                                tipsMore: true,
                                tips: 3
                            });
                            isSubmit = false;
                            return;
                        }
                    }
                }

                var options = {
                    url: "${request.contextPath}/bigdata/metadata/saveField",
                    dataType: 'json',
                    success: function (data) {
                        if (!data.success) {
							showLayerTips4Confirm('error',data.message);
                            isSubmit = false;
                        } else {
                        	showLayerTips('success','保存成功!','t');
                            layer.close(index);
                            // 获取id
                            var id = $('.directory-tree a.active').attr('id');
                            var url = "${request.contextPath}/bigdata/metadata/" + id + "/fieldInformation";
                            $("#metadataDetailDiv").load(url);
                        }
                    },
                    clearForm: false,
                    resetForm: false,
                    type: 'post',
                    error: function (XMLHttpRequest, textStatus, errorThrown) {
                    }//请求出错
                };
                $("#metadataForm").ajaxSubmit(options);
            },
            area: ['600px', '450px'],
            content: $('.layer-metadata')
        });
    }

    function deleteField(id) {
		showConfirmTips('prompt',"提示","您确定要删除元数据字段吗？",function(){
            $.ajax({
                url: '${request.contextPath}/bigdata/metadata/deleteField',
                type: 'POST',
                data: {
                    id: id
                },
                dataType: 'json',
                success: function (val) {
                    if (!val.success) {
						showLayerTips4Confirm('error',val.message);
                    }
                    else {
						showLayerTips('success','删除成功!','t');
                        // 获取id
                        var id = $('.directory-tree a.active').attr('id');
                        var url = "${request.contextPath}/bigdata/metadata/" + id + "/fieldInformation";
                        $("#metadataDetailDiv").load(url);
                    }
                }
            });
        });
    }
</script>