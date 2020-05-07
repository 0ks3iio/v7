<div class="page-sidebar type-in-tree-box">
    <#if searchType=="0">
        <#if isAdmin>
            <div class="evaluate-type-edit">
                    <button class="btn btn-white evaluate-100" id="type-edit" onclick="doImport()">
                        批量导入
                    </button>
            </div>
        </#if>
    <#else >
        <div class="evaluate-type-edit">
            <button class="btn btn-white evaluate-100" id="type-edit" onclick="doImport()">
                批量导入
            </button>
        </div>
    </#if>
    <input type="hidden" class="form-control" id="selectGradeId" value=""/>
    <input type="hidden" class="form-control" id="selectClassId" value=""/>
        <#if searchType=="0">
            <#if isAdmin>
                <div class="page-sidebar-body" style="top: 60px;">
            <#else >
                <div class="page-sidebar-body">
            </#if>
        <#else >
            <div class="page-sidebar-body" style="top: 60px;">
        </#if>
        <div class="credit-search input-group evaluate-tree-group">
            <input type="text" class="form-control" id="searchParam" value="${searchParam!}" placeholder="请输入" onkeypress="if(event.keyCode == 13){searchMember()}"/>

            <i class="wpfont icon-close-round-fill evaluate-tree-close" onclick="closeinput(this)"></i>
            <a href="#" class="input-group-addon evaluate-tree-search" hidefocus="true" onclick="searchMember()">
                <i class="wpfont icon-search"></i>
            </a>
        </div>
        <div id="treeDiv">
            <ul class="chosen-tree chosen-tree-tier1">
                <#if gradeList?exists&&(gradeList?size>0)>
                    <#list gradeList as item>
                        <li class="sub-tree" <#if searchParam??> open</#if>>
                            <div class="chosen-tree-item" data-index="1" onclick="gradeClick('${item.id}')">
                                <span class="arrow"></span>
                                <span class="name" id="gradeName">${item.gradeName!}</span>
                            <#--<i class="typein-no-tip">${unSignNum!}人未评</i>-->
                            </div>
                            <#if (classListMap?size>0)>
                                <#if classListMap[item.id]?exists&&(classListMap[item.id]?size>0)>
                                    <#list classListMap[item.id] as item1>
                                        <ul class="chosen-tree chosen-tree-tier2">
                                            <li class="sub-tree" <#if searchParam??> open</#if>>

                                                <div class="chosen-tree-item" data-index="11" onclick="classClick(this,'${item1.id}')">
                                                    <span class="arrow"></span>
                                                    <span class="name">${item1.className}</span>
                                                </div>
                                            </li>
                                        </ul>
                                    </#list>
                                </#if>
                            </#if>
                        </li>
                    </#list>
                <#else >
                <div class="no-data-container">
                    <div class="no-data">
                        <span class="no-data-img">
                            <img src="${request.contextPath}/static/images/public/nodata6.png" alt="">
                        </span>
                        <div class="no-data-body">
                            <p class="no-data-txt">暂无数据</p>
                        </div>
                    </div>
                </div>
                </#if>
            </ul>
        </div>
    </div>
</div>
<div class="evaluate-right-content" style="min-width: 800px;">
    <div id="divList1" style="height: 100%">
        <div class="no-data-container">
            <div class="no-data">
                <span class="no-data-img">
                    <img src="${request.contextPath}/static/images/public/nodata6.png" alt="">
                </span>
                <div class="no-data-body">
                    <p class="no-data-txt">暂无数据</p>
                </div>
            </div>
        </div>
    </div>
</div>

<script type="text/javascript">
    if ("ontouchstart" in document.documentElement)
        document.write(
        "<script src='${request.contextPath}/static/components/_mod/jquery.mobile.custom/jquery.mobile.custom.js'>" +
        "<" +
        "/script>"
                );
</script>
<script src="${request.contextPath}/static/components/jquery-slimscroll/jquery.slimscroll.min.js"></script>
<script src="${request.contextPath}/static/components/datatables/media/js/jquery.dataTables.min.js"></script>
<script src="${request.contextPath}/static/components/datatables/media/js/dataTables.fixedColumns.min.js"></script>
<script src="${request.contextPath}/static/components/datatables/media/js/dataTables.rowsGroup.js"></script>

<script type="text/javascript">

    //树结构的代码
    $(".evaluate-tree-group").find(".form-control").bind("input propertychange", function(event) {
        if($(this).val() == ""){
            $(this).siblings(".evaluate-tree-close").removeClass("active");
        }else{
            $(this).siblings(".evaluate-tree-close").addClass("active");
        }
    });
    $(".evaluate-tree-group").find(".form-control").on("change", function(event) {
        if($(this).val() == ""){
            $(this).siblings(".evaluate-tree-close").removeClass("active");
        }else{
            $(this).siblings(".evaluate-tree-close").addClass("active");
        }
    });
    function closeinput(that) {
        $(that).siblings(".form-control").val("");
        $(that).siblings(".form-control").change();
        var url = '${request.contextPath}/stutotality/result/getStuClassList';
        $("#treeDiv").load(url);
    }
    $(function () {

    });

    function gradeClick(gradeId) {
        $("#selectGradeId").val(gradeId);
        <#--var searchType = '${searchType!}';-->
        <#--if(searchType=="3"){-->
            <#--var url = '${request.contextPath}/stutotality/report/getGradeTotalityReport?gradeId=' + gradeId;-->
            <#--$("#divList1").load(url);-->
        <#--}-->
    }

    function stuClick(studentId) {
        var gradeId = $("#selectGradeId").val();
        var classId = $("#selectClassId").val();
        var searchType = '${searchType!}';
        var url;
        if(searchType=="0") {
            url = '${request.contextPath}/stutotality/result/getStuPhysicalQualityList?studentId=' + studentId + "&gradeId=" + gradeId;
        }else if(searchType=="1"){
            url = '${request.contextPath}/stutotality/result/getStuRewardList?studentId=' + studentId + "&gradeId=" + gradeId;

        }
        else if(searchType=="2"){
            url = '${request.contextPath}/stutotality/result/getStuAcadList?studentId=' + studentId + "&gradeId=" + gradeId;
        }
        else if(searchType=="3"){
            url = '${request.contextPath}/stutotality/report/getStuTotalityReport?studentId=' + studentId + "&gradeId=" + gradeId+"&classId="+classId;
        }
        $("#divList1").load(url);
    }


    //导入
    function doImport() {
        var classId = $("#selectClassId").val();
        var searchType = '${searchType!}';
        var url;
        if(classId == '' || classId == undefined){
            layerTipMsg(false, "请选择一个班级!", "");
            return;
        }
        if(searchType=="0") {
            url = '${request.contextPath}/stutotality/result/main?importType='+0+"&classId="+classId;
        }
        else if(searchType=="1"){
            url = '${request.contextPath}/stutotality/stuReward/import/main?classId=' + classId ;
        }
        else if(searchType=="2"){
            url = '${request.contextPath}/stutotality/stuAcadList/import/main?classId=' + classId ;
        }
        else if(searchType=="3"){
            url = '${request.contextPath}/stutotality/report/getStuTotalityReport?studentId=' + studentId + "&gradeId=" + gradeId+"&classId="+classId;
        }
        <#--else if(searchType=="4"){-->
            <#--url = '${request.contextPath}/stutotality/result/stuResultImport/main?importType='+1;-->
        <#--}-->
        $("#divList1").load(url);
    }

    function classClick(that,classId) {
        $("#selectClassId").val(classId);
        var html = "";
        $(that).parent().find("ul").each(function () {
            $(this).remove();
        });
        <#--var searchType = '${searchType!}';-->
        <#--if(searchType=="3"){-->
            <#--var url = '${request.contextPath}/stutotality/report/getClassTotalityReport?classId=' + classId;-->
            <#--$("#divList1").load(url);-->
        <#--}-->
        // $(that).after("<ul>1111</ul>");
        $.ajax({
            url: "${request.contextPath}/stutotality/result/findStuListByClassId?",
            data: {
                'classId': classId,
            },
            type: 'post',
            dataType: 'json',
            success: function (data) {
                var studentList = data.studentList;
                if(studentList) {
                    if (studentList.length > 0) {
                        for (var i = 0; i < studentList.length; i++) {
                            html = html + "<ul class='chosen-tree chosen-tree-tier3'><li ><div class='chosen-tree-item' data-index='111' onclick='stuClick(" + "\"" + studentList[i].id + "\"" + ")'><a><span class='arrow'></span><span class='name'>" + studentList[i].studentName + "</span></a></div></li></ul>";
                        }
                    }
                    $(that).after(html);
                }
            },
            error: function (XMLHttpRequest, textStatus, errorThrown) {
            }
        });
    }

    function searchMember() {
        var searchParam = $("#searchParam").val();

        var html = "";
        $("#treeDiv").html(html);
        // $(that).parent().find("ul").each(function () {
        //     $(this).remove();
        // });
        // $(that).after("<ul>1111</ul>");
        if(searchParam) {
            //$(".sub-tree").attr("open");
            $.ajax({
                url: "${request.contextPath}/stutotality/result/findStuListByClassId?",
                data: {
                    'searchParam': searchParam
                },
                type: 'post',
                dataType: 'json',
                success: function (data) {
                    var studentList = data.studentList;
                    if (studentList.length > 0) {
                        for (var i = 0; i < studentList.length; i++) {
                            html = html + "<ul class='chosen-tree chosen-tree-tier3' style='cursor:pointer'><li ><div style='padding: 10px 20px' data-index='111' onclick='stuClick(" + "\"" + studentList[i].id + "\"" + ")'><a><span class='arrow'></span><span class='name'>" + studentList[i].studentName + "</span></a></div></li></ul>";
                        }
                        $("#treeDiv").html(html);
                    }else {
                        $("#treeDiv").html('<div class="no-data-container">\n' +
                        '            <div class="no-data">\n' +
                        '                    <span class="no-data-img">\n' +
                        '                        <img src="${request.contextPath}/static/images/public/nodata6.png" alt="">\n' +
                        '                    </span>\n' +
                        '                <div class="no-data-body">\n' +
                        '                    <p class="no-data-txt">暂无数据</p>\n' +
                        '                </div>\n' +
                        '            </div>\n' +
                        '        </div>');
                    }
                },
                error: function (XMLHttpRequest, textStatus, errorThrown) {
                }
            });
        }else {
            var url = '${request.contextPath}/stutotality/result/getStuClassList';
            $("#treeDiv").load(url);
        }
    }

    var isSubmit=false;
    //收缩树
    $('.page-sidebar').on('click', '.chosen-tree-item .arrow', function(e){
        var $scroll = $('.page-sidebar-body'),
                $tree = $scroll.children('.chosen-tree'),
                $li = $(this).parent('.chosen-tree-item').parent('li');
        if ($li.hasClass('sub-tree')) {
            $li.toggleClass('open');
            if($li.hasClass('open')){
                if($li.parent().hasClass('chosen-tree-tier2')){
                    $tree.width($li.find(".chosen-tree-tier3").first().find(".chosen-tree-item").width() + 90)
                }
            }
        }
        if(!$(this).parents(".page-sidebar").hasClass("xhidden")){
            $scroll.scrollLeft(500);
        }else{
            $scroll.scrollLeft(0);
        }
        var sLeft = $scroll.scrollLeft(),
                sWidth = $scroll.width(),
                tWidth = sLeft + sWidth;
        $tree.width(tWidth);
    });
    //选中树
    $('.page-sidebar').on('click', '.chosen-tree-item', function(){
        var $li = $(this).parent('li');
        $('.chosen-tree li').removeClass('active');
        $li.addClass('active');
        var $scroll = $('.page-sidebar-body'),
                $tree = $scroll.children('.chosen-tree'),
                $li = $(this).parent('li');
        if ($li.hasClass('sub-tree')) {
            $li.toggleClass('open');
            if($li.hasClass('open')){
                if($li.parent().hasClass('chosen-tree-tier2')){
                    $tree.width($li.find(".chosen-tree-tier3").first().find(".chosen-tree-item").width() + 90)
                }
            }
        }
        if(!$(this).parents(".page-sidebar").hasClass("xhidden")){
            $scroll.scrollLeft(500);
        }else{
            $scroll.scrollLeft(0);
        }
        var sLeft = $scroll.scrollLeft(),
                sWidth = $scroll.width(),
                tWidth = sLeft + sWidth;
        $tree.width(tWidth);
    });
</script>