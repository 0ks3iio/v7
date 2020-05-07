<div class="col-md-3 height-1of1">
    <div class="box-standard">
        <div class="box-standard-part">
            <div class="box-part-title">
                <span>数据</span>
            </div>
            <div class="box-part-content">
                <div class="directory-wrap">
                    <ul class="directory-tree single-light">
                         <#if metadataList?exists && metadataList?size gt 0>
                                 <#list metadataList as metadata>
                                     <li>
                                         <a href="javascript:void(0);" class="metadataItem" id="${metadata.id!}">
                                             <span>
                                                 <#if metadata.dbType?default('mysql') == 'mysql'>
                                                     <i class="iconfont icon-icon_mysql"></i>
                                                 <#else>
                                                     <i class="iconfont icon-icon_hbase"></i>
                                                 </#if>
                                                 <span title="${metadata.name!}">${metadata.name!}</span>
                                             </span>
                                             <div class="pos-icon">
                                                 <i class="iconfont icon-adddirectory-b js-add-directory" onclick="createMetadata('${metadata.id!}', '${metadata.dbType?default('mysql')}');event.cancelBubble=true"></i>
                                             </div>
                                         </a>
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
<div class="layer layer-initTable">
</div>
<div class="col-md-9 height-1of1">
    <#if metadataList?exists && metadataList?size gt 0>
    <ul class="nav nav-tabs nav-tabs-1 nav-tabs-top-15">
        <li class="detail-tab active" type="detailInformation" style="cursor: pointer">
            <a data-toggle="tab">详细信息</a>
        </li>
        <li class="detail-tab" type="fieldInformation" style="cursor: pointer">
            <a data-toggle="tab">字段信息</a>
        </li>
    </ul>
    </#if>
    <div class="tab-content tab-content-height slimScrollBar-made">
        <div id="metadataDetailDiv" class="tab-pane height-1of1 active">
            <div class="no-data">
                <img src="${request.contextPath}/bigdata/v3/static/images/folder-settings/img-directory.png"/>
                <div>暂无数据</div>
            </div>
        </div>
    </div>
</div>
<script>
    $(function () {
        //目录
        $('.directory-tree').on('click', 'a', function (e) {
            e.stopPropagation();

            if ($(this).hasClass('metadataItem')) {
                $('.directory-tree').find('a').removeClass('active');
                $(this).addClass('active');
                // 获取id
                var id = $(this).attr('id');
                var type = $('.detail-tab.active').attr('type');
                var mdType = $('.filter-item.active').attr('mdType');
                // 获取类型
                var url = "${request.contextPath}/bigdata/metadata/" + id + "/" + type + "?mdType=" + mdType + "&isAdmin=true";
                $("#metadataDetailDiv").load(url);
            }
        });

        $('.directory-tree').find('.metadataItem').first().click();
    })

    function createMetadata(id, dbType) {

        if (dbType == 'hbase') {
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