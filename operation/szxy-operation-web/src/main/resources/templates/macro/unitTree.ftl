<!-- 运营平台单位树 -->

<!--
    单位树 宏
    一. ztree 异步展现数据,并且默认读缓存
    二. 搜索  展示数据

    params: callback 回调函数名称(点击单位名称时)
-->
<#macro unitTree dataType callback>
<script src="${springMacroRequestContext.contextPath}/static/components/zTree/js/jquery.ztree.all.js"></script>
<script src="${springMacroRequestContext.contextPath}/static/components/zTree/js/jquery.ztree.core.js"></script>
<link rel="stylesheet" href="${springMacroRequestContext.contextPath}/static/operation/css/zTreeStyle.css"/>

<div class="page-sidebar">
    <div class="page-sidebar-header ml10 mr10">
        <div class="input-group mt20">
            <input id="searchName" type="text" class="form-control">
            <a id="search" class="input-group-addon" href="javascript:void(0);">
                <i class="fa fa-search"></i>
            </a>
        </div>
    </div>

    <div class="page-sidebar-body">
        <div class="page-sidebar-body-title">组织架构</div>
        <!--
            sub-tree：有下一级
            report：有上报
            非常重要：两个状态不同时存在
        -->

    <div class="no-data-container" id="unitTreeMacro-no-data">
        <div class="no-data">
                <span class="no-data-img">
                <img src="${springMacroRequestContext.contextPath}/static/images/public/nodata6.png" alt="">
                </span>
            <div class="no-data-body">
                <p class="no-data-txt">没有相关数据</p>
            </div>
        </div>
    </div>
    <ul class="ztree" id="unitZtree">

    </ul>
    </div>
</div>
<script type="text/javascript">
    var setting = {
        async: {
            enable: true,
            type: "get",
            url: "${springMacroRequestContext.contextPath}/operation/${dataType}/unitTree/findUnitByParentId",
            autoParam: ["id"]
        },
        view: {
            showIcon: false,
            selectedMulti: false
        },
        callback: {
            onClick: zTreeOnClick
        }
    };

    $(function () {
        $(".no-data-container").hide();
        $.fn.zTree.init($("#unitZtree"), setting);
        $("#search").click(searchNode);
        $('#searchName').on('keypress',function(event){
            if(event.keyCode == 13) {searchNode()}
        });
    })

    /**
     * 按单元名字搜索
     */
    function searchNode() {

        if($("#searchName").val().trim()==null || $("#searchName").val().trim()=='' ){
            $.fn.zTree.init($("#unitZtree"), setting);
            $("#unitZtree").show();
            $(".no-data-container").hide();
            return;
        }
        var url = _contextPath + "/operation/${dataType}/unitTree/findUnitByUnitName";
        $.get(url, {unitName:$("#searchName").val()},function (result) {

            if (result.success == true) {
                $("#unitTreeMacro-no-data").hide();
                $("#unitZtree").show();
                $.fn.zTree.init($("#unitZtree"), setting, result.unitList);
            } else {
                $("#unitZtree").hide();
                $("#unitTreeMacro-no-data").show();
            }
        });

    }

    /* 回调函数 */
    function zTreeOnClick(event, treeId, treeNode) {
        ${callback!}(treeNode.id);
    };

</script>
</#macro>



