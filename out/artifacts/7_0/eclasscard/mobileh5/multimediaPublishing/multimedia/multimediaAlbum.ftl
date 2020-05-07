<header class="mui-bar mui-bar-nav blue-header">
    <a class="mui-action-back mui-icon mui-icon-left-nav mui-pull-left"></a>
    <h1 class="mui-title">${attachFolder.title!}</h1>
</header>

<div class="mui-content full-content">
    <div class="wrap-full">
        <ul class="mui-table-view shower-num-wrap" style="<#if folderToSize?exists>display: block<#else>display: none</#if>">
            <li class="mui-table-view-cell">
                <a class="mui-navigate-right">
                    <span class="mui-badge">已选${folderToSize!}个</span>
                    班牌展示中
                </a>
            </li>
        </ul>
        <div class="box-media-wrap box-media-wrap-simple">
            <#if attachments?exists&&attachments?size gt 0>
                <#list attachments as item>
                    <div class="box-media">
                        <div class="box-media-img clearfix bg-green">
                            <img src="images/photos.png" >
                            <div class="mui-checkbox" style="display: none">
                                <label></label>
                                <input name="checkbox" type="checkbox" value="${item.id}">
                            </div>
                        </div>
                    </div>
                </#list>
            </#if>
        </div>
    </div>

    <div id="Popover_0" class="mui-popover mui-popover-two">
        <ul class="mui-table-view">
            <li class="mui-table-view-cell"><a href="javascript:void(0);" onclick="selectTab(1)">编辑相册名称</a></li>
            <#if folderToSize?exists>
            <li class="mui-table-view-cell"><a href="javascript:void(0);" onclick="selectTab(2)">取消班牌展示</a></li>
            <#else>
            <li class="mui-table-view-cell"><a href="javascript:void(0);" onclick="selectTab(3)">设为班牌展示</a></li>
            </#if>
            <li class="mui-table-view-cell"><a href="javascript:void(0);" onclick="selectTab(4)">批量删除照片</a></li>
            <li class="mui-table-view-cell"><a href="javascript:void(0);" onclick="selectTab(5)">删除相册</a></li>
        </ul>
    </div>

    <div class="bottom-sure bottom-sure-two no-padding">
        <div class="two-btn two-btn-two clearfix">
            <button type="button" class="mui-btn ">
                <div class="clearfix">
                    <img src="images/+.png" >
                </div>
                <span>添加照片</span>
            </button>
            <button type="button" class="mui-btn">
                <span class="triangle-box"></span>
                <div class="clearfix">
                    <img src="images/=.png" >
                </div>
                <span>相册管理</span>
                <a href="#Popover_0"></a>
            </button>
        </div>
    </div>

    <div class="bottom-sure no-padding" style="display: none" id>
        <div class="two-btn">
            <button type="button" class="mui-btn mui-btn-blue">删除</button>
            <button type="button" class="mui-btn">取消</button>
        </div>
    </div>
</div>
<script>
    function selectTab(tabType) {
        mui("#Popover_0").popover('toggle');
        var btnArray = ['取消', '确定'];
        if (tabType == 1) {
            mui.prompt(' ', '输入多媒体集名称', '编辑多媒体集名称', btnArray, function(e) {
                if (e.index == 1) {
                    if (e.value == "" || e.value == null) {
                        $(".mui-popup-text").html("多媒体名称不可为空！");
                        return false;
                    }
                    if(e.value.length>25){
                        $(".mui-popup-text").html("名称内容不能超过50个字节（一个汉字为两个字节）！");
                        return false;
                    }
                    mui.toast("保存中！");
                    $.ajax({
                        url:'${request.contextPath}/mobile/open/eclasscard/multimediaSave",
                        data:{"id":"${attachFolder.id!}","title":e.value},
                        dataType : 'json',
                        type : 'post',
                        success : function(data){
                            if(data.success){
                                $(".mui-title").html(e.value);
                                return true;
                            } else {
                                $(".mui-popup-text").html("保存失败！");
                                return false;
                            }
                        },
                        error:function(XMLHttpRequest, textStatus, errorThrown){}//请求出错
                    });
                }
            })
        } else if (tabType == 2) {

        } else if (tabType == 3) {

        } else if (tabType == 4) {
            $(".mui-checkbox").attr("style","display:block");

        } else {

        }
    }
</script>