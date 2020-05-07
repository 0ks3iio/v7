<div class="search-head text-center">
    <div class="input-group">
        <input type="text" id="businessName" onkeydown="searchEvent(event)" class="form-control form-control-lg" placeholder="请输入关键字搜索数据">
        <a href="javascript:void(0);" onclick="searchResult()" class="input-group-addon js-search"><i class="wpfont icon-search"></i></a>
    </div>
</div>
<div class="box box-default box-side clearfix">
    <div class="mb-15 mt-20 b">最新数据</div>
    <div class="box-part-content">
        <ul class="folder-box-wrap clearfix js-target">
                <#list folderDetails as item>
                    <li onclick="view('${item.id!}')" style="cursor: pointer"
                        id="${item.id!}" businessId="${item.businessId!}"
                        businessName="${item.businessName!}"
                        businessType="${item.businessType!}"><img
                            class="pos-left"
                            src="${request.contextPath}/bigdata/v3/static/images/my-view/${imgMap[item.businessType]}"/><span
                            title="${item.businessName!}">${item.businessName!}</span>
                    </li>
                </#list>
        </ul>
    </div>
</div>
<div class="box box-default box-side clearfix">
    <div class="mb-15 b">搜索结果</div>
    <div class="box-part-content" id="searchResultDiv">
        <div class="no-data-common">
            <div class="text-center">
                <img src="${request.contextPath}/bigdata/v3/static/images/public/no-data-search-100.png"/>
                <p class="color-999">请根据条件搜索数据</p>
            </div>
        </div>
    </div>
</div>
<div class="key-box" data-index="">
    <ul>
        <li class="js-favorites"><i class="iconfont icon-shoucang"></i>收藏</li>
        <li class="js-share"><i class="iconfont icon-share-fill"></i>分享</li>
    </ul>
</div>
<div class="layer share-div">
</div>
<script>
    function searchResult() {
        var businessName = $('#businessName').val();
        $.ajax({
            url: '${request.contextPath}/bigdata/user/folderDetail/search',
            type: 'POST',
            data: {businessName:businessName, identity:'${identity!}' },
            dataType: 'html',
            beforeSend: function(){
                $('#searchResultDiv').empty().html("<div class='pos-middle-center'><h4><img src='${request.contextPath}/static/ace/img/loading.gif' />玩命的加载中......</h4></div>");
            },
            success: function (response) {
                $('#searchResultDiv').empty().html(response);
            }
        });
    }

    function view(folderDetailId) {
        var pForm = document.createElement("form");
        document.body.appendChild(pForm);
        pForm.action = "${request.contextPath}/bigdata/user/folder/view?folderDetailId=" + folderDetailId;
        pForm.target = "_blank";
        pForm.method = "post";
        pForm.submit();
    }

    function searchEvent(e) {
        var ev = document.all ? window.event : e;
        if(ev.keyCode==13) {
            searchResult();
        }
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