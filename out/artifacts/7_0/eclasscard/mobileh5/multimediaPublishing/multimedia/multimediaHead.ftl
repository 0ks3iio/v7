<div class="head-made">
    <div class="mui-segmented-control">
        <#if allClass>
            <a id="allClassBrand" class="mui-control-item mui-active" href="#item1">全校班牌</a>
        </#if>
            <a id="myClassBrand" class="mui-control-item <#if !allClass>mui-active</#if>" href="#item2">我的班牌</a>
    </div>
</div>

<div class="tab-content-list notice-board">
    <#if allClass>
        <div id="item1" class="mui-control-content mui-active">
            <div class="box-media-wrap no-pb clearfix" id="allMultimediaListDiv">

            </div>
        </div>
    </#if>
    <div id="item2" class="mui-control-content h-1of1 <#if !allClass>mui-active</#if>">
        <#if eccInfos?exists&&eccInfos?size gt 0>
            <div class="top-tab px10">
                <div class="mui-scroll-wrapper mui-slider-indicator mui-segmented-control mui-segmented-control-inverted">
                    <div class="mui-scroll">
                        <#list eccInfos as item>
                            <a id="${item.id!}" class="mui-control-item <#if item_index == 0>mui-active</#if>" href="javascript:void(0);" onclick="showMultimediaList('${item.id!}')">
                                <#if item.className?exists>
                                    <span>${item.className!}</span>
                                <#else>
                                    <span>${item.placeName!}</span>
                                </#if>
                            </a>
                        </#list>
                    </div>
                </div>
            </div>
        </#if>
        <div class="box-media-wrap no-pb clearfix" id="myMultimediaListDiv">

        </div>
    </div>
</div>

<div class="bottom-sure bottom-sure-two no-padding">
    <div class="two-btn two-btn-two clearfix">
        <button type="button" class="mui-btn no-dashed" id="new-photos">
            <div class="clearfix">
                <img src="${request.contextPath}/static/eclasscard/mobileh5/multimediaPublishing/images/+.png" >
            </div>
            <span>新增相册</span>
        </button>
        <button type="button" class="mui-btn same-btn no-dashed" id="new-vedios">
            <span class="triangle-box"></span>
            <div class="clearfix">
                <img src="${request.contextPath}/static/eclasscard/mobileh5/multimediaPublishing/images/+.png" >
            </div>
            <span>新增视频集</span>
        </button>
    </div>
</div>
<script type="text/javascript">
    $(function(){
        <#if allClass>
            $("#allMultimediaListDiv").load("${request.contextPath}/mobile/open/eclasscard/allMultimediaList?unitId="+unitId);
        </#if>
        var eccInfoId = "";
        if ($(".top-tab").find("a").length>0) {
            eccInfoId = $(".top-tab").find("a").first().attr("id");
        }
        $("#myMultimediaListDiv").load("${request.contextPath}/mobile/open/eclasscard/myMultimediaList?eccInfoId="+eccInfoId);

        document.getElementById("new-photos").addEventListener('tap', function(e) {
            e.detail.gesture.preventDefault();
            newMultimedia(1);
        });

        document.getElementById("new-vedios").addEventListener('tap', function(e) {
            e.detail.gesture.preventDefault();
            newMultimedia(2);
        });

    })

    function showMultimediaList(eccInfoId) {
        $("#myMultimediaListDiv").load("${request.contextPath}/mobile/open/eclasscard/myMultimediaList?eccInfoId="+eccInfoId);
    }

    function showMultimedia(folderId,folderType) {
        $("#showMultimedia").load("${request.contextPath}/mobile/open/eclasscard/showMultimedia?folderId="+folderId+"&folderType="+folderType);
        $("#MultimediaDiv").attr("style","display:none");
        $("#showMultimedia").attr("style","display:block");
    }

    function newMultimedia(type) {
        var btnArray = ['取消', '确定'];
        var target = $('.tab-content-list .mui-active .box-media-wrap');
        var prompt = "输入相册集名称";
        var title = "创建相册集";
        if (type == 2) {
            prompt = "输入视频集名称";
            title = "创建视频集";
        }
        var range = 1;
        if ($("#allClassBrand").hasClass("mui-active")) {
            range = 2;
        }
        var eccInfoId = "";

        mui.prompt(' ', prompt, title, btnArray, function(e) {
            if (e.index == 1) {
                if (e.value == "" || e.value == null) {
                    $(".mui-popup-text").html("多媒体名称不可为空！");
                    return false;
                }
                if(e.value.length>25){
                    $(".mui-popup-text").html("名称内容不能超过50个字节（一个汉字为两个字节）！");
                    return false;
                }
                $.ajax({
                    url:'${request.contextPath}/mobile/open/eclasscard/multimediaSave',
                    data:{"title":e.value,"type":type,"range":range,"unitId":unitId,"eccInfoId":eccInfoId},
                    dataType : 'json',
                    success : function(data){
                        if(data.success){
                            var str ='<div class="box-media" onclick="showMultimedia(\'' + data.msg + '\',' + type + ')">';
                            str += '<div class="box-media-img clearfix bg-green">';
                            if (type == 2) {
                                str += '<img src="${request.contextPath}/static/eclasscard/mobileh5/multimediaPublishing/images/videos.png" >';
                            } else {
                                str += '<img src="${request.contextPath}/static/eclasscard/mobileh5/multimediaPublishing/images/photos.png" >';
                            }
                            str += '<div class="left-top" style="display:none"><img src="${request.contextPath}/static/eclasscard/mobileh5/multimediaPublishing/images/triangle.png" ></div></div><div class="box-media-name">' + e.value + '</div></div>';
                            $(".tab-content-list").animate( {scrollTop: 0}, 500);
                            target.prepend(str);
                            return true;
                        } else {
                            $(".mui-popup-text").html("保存失败！");
                            return false;
                        }
                    },
                    type : 'post',
                    error:function(XMLHttpRequest, textStatus, errorThrown){
                        mui.toast("保存失败！");
                    }//请求出错
                });
            }
        })
    }
</script>