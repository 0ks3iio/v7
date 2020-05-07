<div class="col-md-3 height-1of1">
    <div class="box-standard">
        <div class="box-standard-part">
            <div class="box-part-title">
                <span>数据</span>
                <img src="${request.contextPath}/static/bigdata/images/model-add.png" onclick="editMetadata('');" class="pos-right-c js-add" alt=""/>
            </div>
            <div class="box-part-content">
                <div class="directory-wrap">
                    <ul class="directory-tree single-light">
                        <#if metadataTreeVOList?exists && metadataTreeVOList?size gt 0>
                            <#list metadataTreeVOList as first>
                            <li folder_id="${first.id!}">
                                <a href="javascript:void(0);" aria-expanded="true">
                                    <span class="multilevel"><i class="iconfont icon-caret-down"></i></span>
                                    <span><span>${first.name!}</span></span>
                                </a>
                                <#if first.child?exists && first.child?size gt 0>
                                    <ul class="collapse in" id="${first.id!}" aria-expanded="true" style="">
                                        <#list first.child as second>
                                            <li folder_id="${first.id!}_${second.id!}">
                                                <a href="javascript:void(0);" class="" aria-expanded="true">
                                                    <span class="multilevel"><i class="iconfont icon-caret-down"></i></span>
                                                    <span><span>${second.name!}</span></span>
                                                </a>
                                                <#if second.child?exists && second.child?size gt 0>
                                                    <ul class="collapse in" id="${first.id!}_${second.id!}" aria-expanded="true" style="">
                                                        <#list second.child as metadata>
                                                            <li <#if metadata.status?default(0) == 0>class="not-set"</#if>>
                                                                <a href="javascript:void(0);" class="metadataItem <#if metadata.isPhoenix?default(0) == 1>hasdataRule</#if>" id="${metadata.id!}" dbType="${metadata.dbType!}" isPhoenix="${metadata.isPhoenix!}" style="padding-right: 66px">
                                                                    <span>
                                                                        <#if metadata.dbType?default('mysql') == 'mysql'>
                                                                            <i class="iconfont icon-icon_mysql"></i>
                                                                        <#elseif metadata.dbType?default('mysql') == 'hbase'>
                                                                            <i class="iconfont icon-icon_hbase"></i>
                                                                        <#elseif metadata.dbType?default('mysql') == 'kylin'>
                                                                            <i class="iconfont icon-icon_kylin icon-K"></i>
                                                                        <#elseif metadata.dbType?default('mysql') == 'impala'>
                                                                            <i class="iconfont icon-icon_impala icon-I"></i>
                                                                        <#elseif metadata.dbType?default('mysql') == 'es'>
                                                                            <i class="iconfont icon-EKI"></i>
                                                                        </#if>
                                                                        <span title="${metadata.name!?html}">${metadata.name!}</span>
                                                                    </span>
                                                                    <div class="pos-icon">
                                                                        <#if metadata.dbType?default('mysql') != "kylin" && metadata.dbType?default('mysql') != "impala">
                                                                            <i class="iconfont icon-adddirectory-b js-add-directory" onclick="createMetadata('${metadata.id!}', '${metadata.dbType?default('mysql')}');return false;"></i>
                                                                        </#if>
                                                                        <i class="iconfont icon-editor-fill js-edit-directory" onclick="editMetadata('${metadata.id!}');"></i>
                                                                        <i class="iconfont icon-delete-bell js-remove-directory" onclick="deleteMetadata('${metadata.id!}');event.cancelBubble=true"></i>
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
                                <div>暂无数据，请添加</div>
                            </div>
                        </#if>
                    </ul>
                </div>
            </div>
        </div>
    </div>
</div>

<div class="col-md-9 height-1of1">
    <#if metadataTreeVOList?exists && metadataTreeVOList?size gt 0>
    <ul class="nav nav-tabs nav-tabs-1 nav-tabs-top-15">
        <li class="detail-tab active" type="detailInformation" style="cursor: pointer">
            <a data-toggle="tab">详细信息</a>
        </li>
        <li class="detail-tab" type="fieldInformation" style="cursor: pointer">
            <a data-toggle="tab">字段信息</a>
        </li>
        <li class="detail-tab nohbase es kylin impala" type="tableView" style="cursor: pointer">
            <a data-toggle="tab">视图</a>
        </li>
        <li class="detail-tab tableIndex es kylin impala" type="tableIndex" style="cursor: pointer">
            <a data-toggle="tab">索引</a>
        </li>
        <li class="detail-tab" type="dataSourceTarget" style="cursor: pointer">
            <a data-toggle="tab">数据血缘关系</a>
        </li>
        <li class="detail-tab relativeTable" type="relativeTable" style="cursor: pointer">
            <a data-toggle="tab">关联表</a>
        </li>
        <li class="detail-tab dataRule es kylin impala" type="dataRule" style="cursor: pointer">
            <a data-toggle="tab">数据规则</a>
        </li>
        <li class="detail-tab nohbase es kylin impala" type="initTable" style="cursor: pointer">
            <a data-toggle="tab">初始化</a>
        </li>
    </ul>
    </#if>
    <div class="tab-content tab-content-height slimScrollBar-made">
        <div id="metadataDetailDiv" class="tab-pane height-1of1 active scrollBar4" style="overflow-x: hidden;">
            <div class="no-data">
                <img src="${request.contextPath}/bigdata/v3/static/images/folder-settings/img-directory.png"/>
                <div>暂无数据</div>
            </div>
        </div>
    </div>
</div>
<div class="layer layer-initTable">
</div>
<script>
    $(function () {

        if ($('.directory-tree li a.active').find('i:first').hasClass('icon-icon_mysql')) {
            $('.es').show();
            $('.nohbase').show();
            $('.dataRule').show();
            $('.relativeTable').show();
        }else if ($('.directory-tree li a.active').find('i:first').hasClass('icon-icon_hbase')) {
            $('.es').show();
            $('.nohbase').hide();
            $('.relativeTable').hide();
            if ($('.directory-tree li a.active').hasClass('hasdataRule')) {
                $('.dataRule').show();
            } else {
                $('.dataRule').hide();
            }
        } else if ($('.directory-tree li a.active').find('i:first').hasClass('icon-icon_kylin')) {
            $('.kylin').hide();
            $('.relativeTable').show();
        } else if ($('.directory-tree li a.active').find('i:first').hasClass('icon-icon_impala')) {
            $('.impala').hide();
            $('.relativeTable').show();
        } else {
            $('.es').hide();
            $('.relativeTable').hide();
        }

        //目录
        $('.directory-tree').on('click', 'a', function (e) {
            e.stopPropagation();
            if ($(this).find('i:first').hasClass('icon-caret-down')) {
                $(this).find('i:first').removeClass('icon-caret-down').addClass('icon-caret-up');
            } else if ($(this).find('i:first').hasClass('icon-caret-up')){
                $(this).find('i:first').removeClass('icon-caret-up').addClass('icon-caret-down');
            }

            var folderId = $(this).parent().attr('folder_id');
            $('#' + folderId).slideToggle();
            if ($(this).hasClass('metadataItem')) {
                $("#metadataDetailDiv").html("<div class='pos-middle-center'><h4><img src='${request.contextPath}/static/ace/img/loading.gif' />加载中......</h4></div>");
                $('.directory-tree').find('a').removeClass('active');
                $(this).addClass('active');
                // 获取id
                var id = $(this).attr('id');
                var type = $('.detail-tab.active').attr('type');
                var mdType = $('.filter-item.active').attr('mdType');
                // 获取类型
                var url = "${request.contextPath}/bigdata/metadata/" + id + "/detailInformation?mdType=" + mdType;
                $("#metadataDetailDiv").load(url);
                $('.detail-tab').removeClass('active');
                $('.detail-tab:first').addClass('active');
                if ($(this).find('i:first').hasClass('icon-icon_mysql')) {
                    $('.es').show();
                    $('.nohbase').show();
                    $('.dataRule').show();
                    $('.relativeTable').show();
                } else if ($(this).find('i:first').hasClass('icon-icon_hbase')) {
                    $('.es').show();
                    $('.nohbase').hide();
                    $('.relativeTable').hide();
                    if ($(this).hasClass('hasdataRule')) {
                        $('.dataRule').show();
                    } else {
                        $('.dataRule').hide();
                    }
                } else if ($(this).find('i:first').hasClass('icon-icon_kylin')) {
                    $('.kylin').hide();
                    $('.relativeTable').show();
                } else if ($(this).find('i:first').hasClass('icon-icon_impala')) {
                    $('.impala').hide();
                    $('.relativeTable').show();
                } else {
                    $('.es').hide();
                    $('.relativeTable').hide();
                }

                if ($(this).attr('dbType') == 'hbase' && $(this).attr('isPhoenix') == 0) {
                    $('.tableIndex').addClass('hide');
                } else {
                    $('.tableIndex').removeClass('hide');
                }


            }
        });

        var lastId = '${id!}';
        if (lastId == null || lastId == '') {
            $('.directory-tree').find('.metadataItem').first().click();
        } else {
            $('.metadataItem[id="'+ lastId +'"]').click();
        }
    });

    function createMetadata(id, dbType) {

        if (dbType == 'hbase' || dbType == 'es') {
            showConfirmTips('prompt',"提示","确定要创建该元数据表吗？",function(){
                $.ajax({
                    url: '${request.contextPath}/bigdata/metadata/createTable',
                    type: 'POST',
                    data: {
                        metadataId:id
                    },
                    dataType: 'json',
                    success: function (val) {
                        if (!val.success) {
                            showLayerTips4Confirm('error',val.message);
                        }
                        else {
                            $('#' + id).parent().removeClass('not-set');
                            showLayerTips('success','创建成功!','t');
                        }
                    }
                });
            });
            return;
        }

        var url =  _contextPath + '/bigdata/metadata/' + id + '/initTable';

        $(".layer-initTable").html("<div class='pos-middle-center'><h4><img src='${request.contextPath}/static/ace/img/loading.gif' />加载中......</h4></div>");
        $(".layer-initTable").load(url);
        layer.open({
            type: 1,
            shade: .5,
            title: ['创建表','font-size:16px'],
            area: ['550px','340px'],
            btn:['执行', '取消'],
            content: $('.layer-initTable'),
            resize:true,
            yes:function (index) {
                $.ajax({
                    url: '${request.contextPath}/bigdata/metadata/createTable',
                    type: 'POST',
                    data: {
                        create_sql: ace.edit("createSql").session.getValue(),
                        metadataId:id
                    },
                    dataType: 'json',
                    success: function (val) {
                        if (!val.success) {
                            showLayerTips4Confirm('error',val.message);
                        }
                        else {
                            layer.close(index);
                            $('#' + id).parent().removeClass('not-set');
                            showLayerTips('success','执行成功!','t');
                        }
                    }
                });
            },
            cancel:function (index) {
                layer.closeAll();
            }
        });
    }
</script>