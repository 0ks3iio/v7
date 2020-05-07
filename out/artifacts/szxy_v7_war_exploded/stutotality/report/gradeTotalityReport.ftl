<#--<div>-->
    <#--<button class="btn btn-blue mr10 font-14" onclick="showpdf()">-->
        <#--导出PDF-->
    <#--</button>-->
<#--</div>-->
<div class="evaluate-item mt10">
    <div class="filter">
        <#--<div class="filter-item">-->
            <#--<span class="filter-name">班级：小一01班</span>-->
        <#--</div>-->
        <div class="filter-item lh36">
            <span class="filter-name">类型：</span>
            <div class="filter-content">
                <label class="pos-rel mr20 mb0">
                    <input type="radio" class="wp" name="1" checked/>
                    <span class="lbl" onclick="getList1()">全部</span>
                </label>
                <label class="pos-rel mr20 mb0">
                    <input type="radio" class="wp" name="1" />
                    <span class="lbl" onclick="getList2()">学生学业发展水平</span>
                </label>
                <label class="pos-rel mb0">
                    <input type="radio" class="wp" name="1" />
                    <span class="lbl" onclick="getList3()">综合素质发展水平</span>
                </label>
            </div>
        </div>
    </div>
</div>
<div class="evaluate-item" id="eva-inquiry-box">

</div>
<script>
    $(function () {
        getList1();
        $(window).resize(function() {
            $(".evaluate-right-content").css(
                    "min-height",
                    $(".type-in-tree-box").height()
                    );
        })
    })
    function getList1() {
        var gradeId = "${gradeId!}";
        $("#eva-inquiry-box").load('${request.contextPath}/stutotality/report/getStuReportList1?gradeId='+gradeId);
    }
    function getList2() {
        $("#eva-inquiry-box").load('${request.contextPath}/stutotality/report/getStuReportList2?');
    }
    function getList3() {
        $("#eva-inquiry-box").load('${request.contextPath}/stutotality/report/getStuReportList3?');
    }
</script>