<div class="box box-default">
    <div class="box-body clearfix" style="padding-top: 0;">
        <div class="point-pro-box">
            <div class="evaluate-item clearfix mt10">
                        <span class="layer-evaluate-label evaluate-item-left point-box-title">评分规则设置</span>
                <input type="hidden" id="semester" value="${semester!}">
                <input type="hidden" id="acadyear" value="${acadyear!}">
                <input type="hidden" id="gradeId" value="${gradeId!}">
            </div>
            <div class="evaluate-item clearfix mt10">
                <span class="layer-evaluate-label evaluate-item-left" style="width: 140px;">年级：</span>
                <div class="evaluate-item-right scaleDiv">
                    <#if gradeList?exists && (gradeList?size gt 0)>
                    <#list gradeList  as grade>
                          <span class="evaluate-select-item <#if  gradeId == grade.id>active</#if> " data-id="${grade.id!}" data-type="1" >${grade.gradeName!}</span>
                    </#list>
                    </#if>
                </div>
            </div>
            <div id="scaleModifyDiv">
            </div>
        </div>
    </div>
</div>

<script type="text/javascript">

    $(function() {
        showBreadBack(acadyearSearch,true,"评分规则");
        scaleModify();
        $(".box-body").css("min-height", $("#sidebar").height() -100);
        $(".evaluate-select-item").on("click", function() {
            $(this).siblings(".evaluate-select-item").removeClass("active");
            $(this).addClass("active");
            scaleModify();
        });
    });
    function acadyearSearch() {
        var acadyear = $('#acadyear').val();
        var semester = $('#semester').val();
        var gradeId = $(".scaleDiv").find("span.active").attr("data-id");
        var url = '${request.contextPath}/stutotality/rule/index/page?acadyear='+acadyear+"&semester="+semester+"&gradeId="+gradeId;
        $('#addScaleDiv').load(url);
    }
    //规则列表
    function scaleModify() {
        var gradeId = $(".scaleDiv").find("span.active").attr("data-id");
        var  acadyear = $("#acadyear").val();
        var  semester = $("#semester").val();
        var url = '${request.contextPath}/stutotality/rule/item/save?gradeId='+gradeId+"&acadyear="+acadyear+"&semester="+semester;
        $('#scaleModifyDiv'). load(url);
    }
</script>