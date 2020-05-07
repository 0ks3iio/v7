<#if folders?exists && folders?size gt 0>
    <#list folders as folder>
        <div class="box border mb-20 clearfix">
            <div class="box-standard-part level2">
                <div class="box-part-title">
                    <i class="iconfont icon-folder-fill"></i>
                    <span title="${folder.folderName!}">
                        ${folder.folderName!}
                    </span>
                </div>
                <div class="box-part-content">
                    <ul class="folder-box-wrap clearfix">
                        <#if folderDetailMap[folder.id]?exists>
                            <#list folderDetailMap[folder.id] as item>
                                    <li onclick="view('${item.id!}')" style="cursor: pointer"
                                        id="${item.id!}" businessId="${item.businessId!}"
                                        businessName="${item.businessName!}"
                                        businessType="${item.businessType!}"><img
                                            class="pos-left"
                                            src="${request.contextPath}/bigdata/v3/static/images/my-view/${imgMap[item.businessType]}"/><span title="${item.businessName!}">${item.businessName!}</span>
                                    </li>
                            </#list>
                        </#if>
                    </ul>
                </div>
            </div>
        </div>
    </#list>
<#else>
        <div class="no-data">
            <img src="${request.contextPath}/bigdata/v3/static/images/folder-settings/img-folder.png"/>
            <div>暂无文件夹</div>
        </div>
</#if>
<div class="key-box" data-index="">
    <ul>
        <li class="js-favorites"><i class="iconfont icon-shoucang"></i>收藏</li>
        <li class="js-share"><i class="iconfont icon-share-fill"></i>分享</li>
    </ul>
</div>
<link rel="stylesheet" href="${request.contextPath}/static/ztree/zTreeStyle/zTreeStyle.css"/>
<script src="${request.contextPath}/static/ztree/js/jquery.ztree.all-3.5.min.js"></script>
<div class="layer share-div">
    <div class="form-horizontal" id="myForm-three">
        <div class="form-group">
            <div class="col-sm-7">
                <p class="choose-num">选择分享用户&nbsp;</p>
                <!--<p class="no-padding-right bold">选择授权对象&nbsp;</p>　-->
                <div class="bs-callout bs-callout-danger">
                    <ul class="nav nav-tabs">
                        <li class="active">
                            <a href="#aa" data-toggle="tab">本单位用户</a>
                        </li>
                    </ul>
                    <div class="tab-content tree-wrap tree-tab width-1of1">
                        <div class="row no-margin">
                            <div class="filter-item col-sm-8 no-margin-right">
                                <div class="pos-rel pull-left width-1of2">
                                    <input id="tree_search_keywords" type="text"
                                           class="typeahead scrollable form-control width-1of1" autocomplete="off"
                                           data-provide="typeahead" placeholder="请输入关键词">
                                </div>
                                <div class="input-group-btn">
                                    <button id="tree_search" type="button" class="btn btn-default">
                                        <i class="fa fa-search"></i>
                                    </button>
                                </div>
                            </div>
                            <div class="col-sm-2 switch-button text-right">
                                <span>是否级联:</span>
                            </div>
                            <div class="col-sm-2 switch-button">
                                <input id="tree_cascade" name="switch-field-1" class="wp wp-switch" type="checkbox">
                                <span class="lbl"></span>
                            </div>
                        </div>
                        <div class="tab-pane active" id="aa">
                            <ul id="treeDemo-one" class="ztree"></ul>
                        </div>
                    </div>
                </div>
            </div>

            <div class="col-sm-5">
                <div class="bs-callout bs-callout-danger">
                    <p class="choose-num">已选（<span>0</span>）</p>
                    <div class="choose-item">
                        <div class="no-data">
                        <#--<img src="../images/big-data/no-choice.png"/>-->
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
<script>
    function view(folderDetailId) {
        var pForm = document.createElement("form");
        document.body.appendChild(pForm);
        pForm.action = "${request.contextPath}/bigdata/user/folder/view?folderDetailId=" + folderDetailId;
        pForm.target = "_blank";
        pForm.method = "post";
        pForm.submit();
    }

    $(function () {
        //屏幕点击
        $('body').on('click', function (e) {
            if ($('.key-box').hasClass('active')) {
                if (e.target.className !== "key-box") {
                    $('.key-box').removeClass('active');
                }
            }
        });
        <#if identity?default('admin') == 'user'>
        //右键
        $('body').on('contextmenu', '.folder-box-wrap li', function (e) {
            e.preventDefault();
            $('.key-box').addClass('active').attr('businessId', $(this).attr('businessId')).attr('detailId', $(this).attr('id')).css({
                top: e.pageY + 2,
                left: e.pageX
            });
        });
        </#if>

        //收藏
        $('.key-box').on('click', '.js-favorites', function (e) {
            var businessId = $('.key-box').attr('businessId');
            var detailId = $('.key-box').attr('detailId');
            var businessType = $('#' + detailId).attr('businessType');
            var businessName = $('#' + detailId).attr('businessName');
            $.ajax({
                url: '${request.contextPath}/bigdata/favorite/addFavorite',
                data: {
                    businessId: businessId,
                    businessType: businessType,
                    businessName: businessName
                },
                type: 'POST',
                dataType: 'json',
                success: function (val) {
                    if (!val.success) {
                        showLayerTips4Confirm('error', val.message, 't', null);
                    } else {
                        showLayerTips('success', '收藏成功', 't');
                    }
                }
            });
        });
        //分享
        $('.key-box').on('click', '.js-share', function (e) {
            var businessId = $('.key-box').attr('businessId');
            var detailId = $('.key-box').attr('detailId');
            var businessType = $('#' + detailId).attr('businessType');
            var businessName = $('#' + detailId).attr('businessName');
            shareLayer(businessId, businessType, businessName);
        });

        function shareLayer(businessId, businessType, businessName) {
            $.ajax({
                url: '${request.contextPath}/bigdata/share/getAllUser',
                type: 'GET',
                data: {businessId: businessId},
                success: function (response) {
                    $.ajax({
                        url: '${request.contextPath}/bigdata/common/tree/shareUserTreeIndex',
                        type: 'POST',
                        data: {users:response.data},
                        dataType: 'html',
                        beforeSend: function(){
                            $('.share-div').html("<div class='pos-middle-center'><h4><img src='${request.contextPath}/static/ace/img/loading.gif' />玩命的加载中......</h4></div>");
                        },
                        success: function (response) {
                            $('.share-div').html(response);
                        }
                    });
                }
            });

            var index = layer.open({
                type: 1,
                title: '用户选择',
                shade: .5,
                shadeClose: true,
                closeBtn: 1,
                btn :['确定','取消'],
                area: ['70%','550px'],
                yes:function(index, layero){
                    var teacherArray = [];
                    zTreeSelectedUserIdMap.forEach(function (value, key, map) {
                        teacherArray.push(key);
                    });
                    doShare(businessId, businessType, businessName, teacherArray);
                    layer.close(index);
                },
                content: $('.share-div')
            })
        }

        function doShare(businessId, businessType, businessName, userArray) {
            $.ajax({
                url: _contextPath + '/bigdata/share/addShare',
                type: 'POST',
                dataType: 'json',
                data: {
                    businessId: businessId,
                    businessType: businessType,
                    businessName: businessName,
                    userArray: userArray
                },
                success: function (response) {
                    if (response.success) {
                        showLayerTips('success', '分享成功', 't');
                    } else {
                        showLayerTips4Confirm('error', response.message);
                    }
                }
            });
        };
    })


</script>