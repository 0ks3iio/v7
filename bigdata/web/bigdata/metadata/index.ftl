<script src="${request.contextPath}/bigdata/v3/static/plugs/chosen/chosen.jquery.min.js"></script>
<script src="${request.contextPath}/static/bigdata/editor/ace.js"/>
<div class=" overview-set bg-fff metadata metadata clearfix hide">
    <div class="filter-item active" mdType="table">
        <span>表</span>
    </div>
    <div class="filter-item" mdType="job">
        <span>任务</span>
    </div>
    <div class="filter-item" mdType="model">
        <span>模型</span>
    </div>
    <div class="filter-item" mdType="event">
        <span>事件</span>
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
    var editImg = '${request.contextPath}/static/bigdata/images/edits.png';
    var removeImg = '${request.contextPath}/static/bigdata/images/removes.png';
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
            $("#mainDiv").html("<div class='pos-middle-center'><h4><img src='${request.contextPath}/static/ace/img/loading.gif' />加载中......</h4></div>");
            var mdType = $(this).attr('mdType');
            var url = "${request.contextPath}/bigdata/metadata/" + mdType + "/list";
            $("#mainDiv").load(url);
        });

        // 内部切换按钮
        $('#mainDiv').on('click', '.detail-tab', function () {
            $("#metadataDetailDiv").html("<div class='pos-middle-center'><h4><img src='${request.contextPath}/static/ace/img/loading.gif' />加载中......</h4></div>");
            $(this).addClass('active').siblings().removeClass('active');
            // 获取id
            var id = $('.directory-tree a.active').attr('id');
            // 获取类型
            var type = $(this).attr('type');
            var mdType = $('.filter-item.active').attr('mdType');
            var url = "${request.contextPath}/bigdata/metadata/" + id + "/" + type + "?mdType=" + mdType;
            $("#metadataDetailDiv").load(url);
        });

        $('.metadata .filter-item').first().trigger('click');
    });

    function editMetadata(id) {
        var mdType = $('.filter-item.active').attr('mdType');
        $("#metadataDiv").load('${request.contextPath}/bigdata/metadata/metadataEdit?id=' + id + '&mdType=' + mdType);
        var isSubmit = false;
        layer.open({
            type: 1,
            shade: .6,
            title: id == '' ? '新增元数据' : '修改元数据',
            btn: ['保存', '取消'],
            yes: function (index, layero) {
                if (isSubmit) {
                    return;
                }
                isSubmit = true;

 				if ($('#databaseTypeIdSelect option:selected').val() == "") {
                    layer.tips("不能为空", "#databaseTypeIdSelect", {
                        tipsMore: true,
                        tips: 3
                    });
                    isSubmit = false;
                    return;
                }
                
                if ($('#name').val() == "") {
                    layer.tips("不能为空", "#name", {
                        tipsMore: true,
                        tips: 3
                    });
                    isSubmit = false;
                    return;
                }

                if ($('#tableName').val() == "") {
                    layer.tips("不能为空", "#tableName", {
                        tipsMore: true,
                        tips: 3
                    });
                    isSubmit = false;
                    return;
                }

                if ($('#dwRankSelect').val() == null || $('#dwRankSelect').val() == "") {
                    layer.tips("不能为空", "#dwRankSelect", {
                        tipsMore: true,
                        tips: 3
                    });
                    isSubmit = false;
                    return;
                }

                if ($('#propertyTopicSelect').val() == null || $('#propertyTopicSelect').val() == "") {
                    layer.tips("不能为空", "#propertyTopicSelect", {
                        tipsMore: true,
                        tips: 3
                    });
                    isSubmit = false;
                    return;
                }

                $('#databaseTypeIdSelect').removeAttr('disabled');
                $('#isPhoenixSelect').removeAttr('disabled');

                var tags = [];
                $('.tag-set').find('span.selected').each(function () {
                    tags.push($(this).attr('tag_id'));
                });

                var options = {
                    url: "${request.contextPath}/bigdata/metadata/saveMetadata",
                    dataType: 'json',
                    data:{tags:tags},
                    success: function (data) {
                        if (!data.success) {
                        	showLayerTips4Confirm('error',data.message);
                            $('#databaseTypeIdSelect').attr('disabled', 'disabled');
                            $('#isPhoenixSelect').attr('disabled', 'disabled');
                            isSubmit = false;
                        } else {
                            showLayerTips('success','保存成功!','t');
                            $('#databaseTypeIdSelect').attr('disabled', 'disabled');
                            layer.close(index);
                            var url = "${request.contextPath}/bigdata/metadata/" + mdType + "/list?id=" + data.data;
                            $("#mainDiv").load(url);
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
            area: ['600px', '750px'],
            content: $('.layer-metadata')
        });
    }

    function deleteMetadata(id) {
        showConfirmTips('prompt',"提示","您确定要删除元数据吗？",function(){
            $.ajax({
                url: '${request.contextPath}/bigdata/metadata/deleteMetadata',
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
                        showLayerTips('success','删除成功','t');
                        var mdType = $('.metadata .filter-item').attr('mdType');
                        var url = "${request.contextPath}/bigdata/metadata/" + mdType + "/list";
                        $("#mainDiv").load(url);
                    }
                }
            });
        });
    }

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
                if ($('#databaseTypeSelect').val() =="char" || $('#databaseTypeSelect').val() =="varchar" ||$('#databaseTypeSelect').val() =="float" || $('#databaseTypeSelect').val() =="double"|| $('#databaseTypeSelect').val() =="int" || $('#databaseTypeSelect').val() =="bigint" || $('#databaseTypeSelect').val() =="decimal") {
                    if (!$('#columnLengthDiv').hasClass('hide')) {
                        if ($('#columnLength').val() == "") {
                            layer.tips("不能为空", "#columnLength", {
                                tipsMore: true,
                                tips: 3
                            });
                            isSubmit = false;
                            return;
                        }
                    }

                    if ($('#databaseTypeSelect').val() =="float" || $('#databaseTypeSelect').val() =="double" || $('#databaseTypeSelect').val() =="decimal") {
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
                if ($('#columnFormat')) {
                    if ($('#columnFormat').val() == "") {
                        layer.tips("不能为空", "#columnFormat", {
                            tipsMore: true,
                            tips: 3
                        });
                        isSubmit = false;
                        return;
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
            area: ['600px', '550px'],
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

    function editRelation(id) {
        var metadataId = $('.directory-tree a.active').attr('id');
        var metadataName =$('.directory-tree a.active').find('span:last-child').text();
        var mdType = $('.filter-item.active').attr('mdType');
        $("#metadataDiv").load('${request.contextPath}/bigdata/metadata/relationEdit?id=' + id + '&metadataId=' + metadataId + "&mdType=" + mdType + "&metadataName=" + metadataName);
        var isSubmit = false;
        layer.open({
            type: 1,
            shade: .6,
            title: id == '' ? '新增血缘关系' : '修改血缘关系',
            btn: ['保存', '取消'],
            yes: function (index, layero) {
                if (isSubmit) {
                    return;
                }
                isSubmit = true;

                var options = {
                    url: "${request.contextPath}/bigdata/metadata/saveRelation?targetName=" + $('#targetIdSelect').find('option:selected').text(),
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
                            var url = "${request.contextPath}/bigdata/metadata/" + id + "/bloodRelationship";
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
            area: ['600px', '200px'],
            content: $('.layer-metadata')
        });
    }

    function deleteRelation(id, sourceName, targetName) {
       showConfirmTips('prompt',"提示","您确定要删除血缘关系吗？",function(){
            $.ajax({
                url: '${request.contextPath}/bigdata/metadata/deleteRelation',
                type: 'POST',
                data: {
                    id: id,
                    sourceName: sourceName,
                    targetName: targetName
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
                        var url = "${request.contextPath}/bigdata/metadata/" + id + "/bloodRelationship";
                        $("#metadataDetailDiv").load(url);
                    }
                }
            });
        });
    }

    function editQualityRuleRelation(id) {
        var metadataId = $('.directory-tree a.active').attr('id');
        var mdType = $('.filter-item.active').attr('mdType');
        $("#metadataDiv").load('${request.contextPath}/bigdata/metadata/ruleRelationEdit?id=' + id + '&metadataId=' + metadataId + "&mdType=" + mdType);
        var isSubmit = false;
        layer.open({
            type: 1,
            shade: .6,
            title: id == '' ? '新增数据规则' : '修改数据规则',
            btn: ['保存', '取消'],
            yes: function (index, layero) {
                if (isSubmit) {
                    return;
                }
                isSubmit = true;

                if ($('#ruleName').val() == "") {
                    layer.tips("不能为空", "#ruleName", {
                        tipsMore: true,
                        tips: 3
                    });
                    isSubmit = false;
                    return;
                }

                if ($('#columnSelect').val() == null || $('#columnSelect').val() == "") {
                    layer.tips("不能为空", "#columnSelect", {
                        tipsMore: true,
                        tips: 3
                    });
                    isSubmit = false;
                    return;
                }

                var options = {
                    url: "${request.contextPath}/bigdata/metadata/saveRuleRelation",
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
                            var url = "${request.contextPath}/bigdata/metadata/" + id + "/dataRule";
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
            area: ['600px', '550px'],
            content: $('.layer-metadata')
        });
    }

    function deleteQualityRuleRelation(id) {
		showConfirmTips('prompt',"提示","您确定要删除数据规则吗？",function(){
            $.ajax({
                url: '${request.contextPath}/bigdata/metadata/deleteRuleRelation',
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
                        showLayerTips('success','删除成功','t');
                        // 获取id
                        var id = $('.directory-tree a.active').attr('id');
                        var url = "${request.contextPath}/bigdata/metadata/" + id + "/dataRule";
                        $("#metadataDetailDiv").load(url);
                    }
                },
                clearForm: false,
                resetForm: false,
                type: 'post',
                error: function (XMLHttpRequest, textStatus, errorThrown) {
                }//请求出错

            });
        });
    }

    /*关联表相关*/
    function editQualityRelatedTable(id) {
        var metadataId = $('.directory-tree a.active').attr('id');
        var mdType = $('.filter-item.active').attr('mdType');
        $('#metadataDiv').load('${request.contextPath}/bigdata/metadata/relatedTableEdit?id=' + id + '&metadataId=' + metadataId + "&mdType=" + mdType);
        var isSubmit = false;
        layer.open({
            type: 1,
            shade: .6,
            title: id==''?'新增关联表': '修改关联表',
            btn: ['保存', '取消'],
            yes: function (index, layero) {
                if (isSubmit) {
                    return;
                }
                isSubmit = true;
               /* y验证部分稍后：TODO*/
                var options = {
                    url: '${request.contextPath}/bigdata/metadata/saveRelatedTable',
                    dataType: 'json',
                    success: function (data) {
                        if (!data.success) {
                            showLayerTips4Confirm('error',data.message);
                            isSubmit = false;
                        } else {
                            showLayerTips('success', data.message, 't');
                            layer.close(index);
                            //重新更新列表信息
                            var metadataId = $('.directory-tree a.active').attr('id');
                            var url = '${request.contextPath}/bigdata/metadata/'+metadataId+'/relativeTable?mdType='+mdType;
                            $('#metadataDetailDiv').load(url);
                        }
                    },
                    clearForm: false,
                    resetForm: false,
                    type: 'post',
                    error: function (XMLHttpRequest, textStatus, errorThrown) {
                    }//请求出错
                };
                $('#metadataForm').ajaxSubmit(options);
            },
            area: ['400px','350px'],
            content: $('.layer-metadata')
        });
    }
    /*关联表更改关联从表后,加载对应的从表列*/
    function changeFollowerTable(e) {
        var id = $(e).val();
        var relatedColumnId = $("#followerTableColumn").val();
        var metadataId = $('.directory-tree a.active').attr('id');
        var mdType = $('.filter-item.active').attr('mdType');
        var url = '${request.contextPath}/bigdata/metadata/followerTableColumnList?id=' + id + '&metadataId=' + metadataId + "&mdType=" + mdType;
        $.get(url,function (res) {
            $("#followerTableColumn").empty();
            var selection = document.getElementById("followerTableColumn");
            for(var i=0;i<res.data.length;i++){
                let option = new Option(res.data[i].columnName, res.data[i].id);
                //  选中
                if (res.data[i].id == relatedColumnId) {
                    option.selected=true;
                }
                selection.options.add(option);
            }
        });
    }

    function deleteQuityRelatedTable(id) {
        var mdType = $('.filter-item.active').attr('mdType');
        showConfirmTips('prompt',"提示","您确定要删除该关联关系吗？",function(){
            $.ajax({
                url: '${request.contextPath}/bigdata/metadata/deleteRelatedTable',
                type: 'POST',
                data: {
                    id: id,
                },
                dataType: 'json',
                success: function (val) {
                    if (!val.success) {
                        showLayerTips4Confirm('error',val.message);
                    }
                    else {
                        showLayerTips('success','删除成功!','t');
                        // 更新列表
                        var metadataId = $('.directory-tree a.active').attr('id');
                        var url = '${request.contextPath}/bigdata/metadata/'+metadataId+'/relativeTable?mdType='+mdType;
                        $('#metadataDetailDiv').load(url);
                    }
                }
            });
        });
    }
</script>