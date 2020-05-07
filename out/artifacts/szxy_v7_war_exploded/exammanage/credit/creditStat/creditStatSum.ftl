
<div class="page-content" style="padding: 0px">
    <div class="page-sidebar xhidden">
        <input type="hidden" id="searchAcadyear" value="${year}">
        <input type="hidden" id="searchSemester" value="${semester}">
        <input type="hidden" id="gradeId" value="${gradeId!}">
        <div class="credit-search-box">
            <#if isAdmin>
            <a class="btn btn-second btn-info btn-block" onclick="sumScore()">
                <i class="sz-icon icontongji" style="margin-right: 20px;"></i>统计总分<span class="credit-btn-text"></span>
            </a>
            </#if>
            <a class="btn btn-second btn-info btn-block" onclick="importList()">
                <i class="sz-icon icondaoru" style="margin-right: 20px;"></i
                >导入补考成绩
            </a>
            <a class="btn btn-second btn-info btn-block" onclick="exportList()">
                <i class="sz-icon icondaochu" style="margin-right: 20px;"></i
                >导出补考名单
            </a>
        </div>
        <div class="page-sidebar-body xhidden" style="top:140px;">
            <div class="credit-search input-group">
                <input type="text" class="form-control" id="searchParam" value="${searchParam!}"/>
                <a onclick="searchMember()" class="input-group-addon" hidefocus="true"
                ><img src="${request.contextPath}/static/images/icons/search_gray_16x16.png"
                /></a>
            </div>
            <!--
                                sub-tree：有下一级
                                report：有上报
                                非常重要：两个状态不同时存在
                            -->
        <#if studentList?exists&& (studentList?size > 0)>
            <#if classDtos?exists&& (classDtos?size > 0)>
            <ul class="chosen-tree chosen-tree-tier1">
                <li class="sub-tree<#if searchParam??> open</#if>" >
                    <div class="chosen-tree-item" data-index="1">
                        <span class="arrow"></span>
                        <span class="name" id="gradeName">${gradeName!}</span>
                    </div>
                    <#list classDtos as item>
                    <ul class="chosen-tree chosen-tree-tier2">
                        <li class="sub-tree">

                                <div class="chosen-tree-item" data-index="11" onclick="classClick('${item.id}','${item.classType}')">
                                    <span class="arrow"></span>
                                    <span class="name">${item.className}</span>
                                </div>
                                <#list studentList as item1>
                                <#if item1.classId==item.id||item1.classId==item.id>
                                    <ul class="chosen-tree chosen-tree-tier3">
                                        <li >
                                            <div class="chosen-tree-item" data-index="111" onclick="stuClick('${item1.id}','${item1.classId}')">
                                                <a>
                                                    <span class="arrow"></span>
                                                    <span class="name">${item1.studentName}</span>
                                                </a>
                                            </div>
                                        </li>
                                    </ul>
                                </#if>
                                </#list>
                        </li>
                    </ul>
                    </#list>
                </li>
            </ul>
            </#if>
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
        </div>
    </div>

    <div class="page-content-inner" id="sumDiv">
        <div class="box box-default">
            <div class="box-body">
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
    </div>
</div>
<script>
    var isSubmit=false;
    $(function () {
        showBreadBack(function () {
            var year = $("#searchAcadyear").val();
            var semester = $("#searchSemester").val();
            var url =  '${request.contextPath}/exammanage/credit/summary?year='+year+"&selectSemester="+semester;
            $("#mydiv").load(url);
        },true,"返回");
    })
    function exportList() {
        var year = $("#searchAcadyear").val();
        var semester = $("#searchSemester").val();
        var gradeId = $("#gradeId").val();
        var url =  '${request.contextPath}/exammanage/credit/exportPatchStudent?year='+year+"&semester="+semester+"&gradeId="+gradeId
        document.location.href=url;
    }
    function importList() {
        var year = $("#searchAcadyear").val();
        var semester = $("#searchSemester").val();
        var gradeName = '${gradeName!}';
        var url =  '${request.contextPath}/exammanage/credit/import/main?gradeId='+'${gradeId}'+"&year="+year+"&gradeName="+gradeName+"&semester="+semester;
        $("#mydiv").load(url);
    }
    function changebtn() {
        $(".page-sidebar-body").attr("style","top:140px");
        $(".credit-search-box").show();
    }
    function changebtn1() {
        $(".page-sidebar-body").attr("style","top:0px");
        $(".credit-search-box").hide();
    }
    function classClick(id,classType){
        var year = $("#searchAcadyear").val();
        var semester = $("#searchSemester").val();
        var url =  '${request.contextPath}/exammanage/credit/creditStatClassSumIndex?classId='+id+"&year="+year+"&semester="+semester+"&classType="+classType;
        $("#sumDiv").load(url);
    }
    function stuClick(id,classId){
        var year = $("#searchAcadyear").val();
        var semester = $("#searchSemester").val();
        var url =  '${request.contextPath}/exammanage/credit/creditStatStuSumList?studentId='+id+"&classId="+classId+"&year="+year+"&semester="+semester;
        $("#sumDiv").load(url);
    }
    function searchMember(){
        var searchParam = $("#searchParam").val();
        var year = $("#searchAcadyear").val();
        var semester = $("#searchSemester").val();
        var gradeName = '${gradeName!}';
        var gradeId = $("#gradeId").val();
        var url =  '${request.contextPath}/exammanage/credit/creditStatSumScore?gradeId='+gradeId+"&year="+year+"&gradeName="+gradeName+"&semester="+semester+"&searchParam="+searchParam;
        $("#mydiv").load(url);
    }

    function sumScore() {
        isSubmit = true;
        var gradeId = $("#gradeId").val();
        var gradeName = $("#gradeName").html();
        var year = $("#searchAcadyear").val();
        var semester = $("#searchSemester").val();
        var url =  '${request.contextPath}/exammanage/credit/creditStatStuSum?';
        $.ajax({
            data:{'gradeId':gradeId,'year':year,'semester':semester},
            url:url,
            dataType : 'json',
            type : 'post',
            success:function (data) {
                var jsonO = data;
                isSubmit=false;
                if(!jsonO.success){
                    layerTipMsg(jsonO.success,"统计失败",jsonO.msg);
                    return;
                }else{
                    layer.msg(jsonO.msg, {
                        offset: 't',
                        time: 2000
                    });
                }
            }
            ,
            error:function () {
                isSubmit = false;
            }
        })
    }
    //收缩树
    $('.page-sidebar').on('click', '.chosen-tree-item .arrow', function(e){
        
        var $scroll = $('.page-sidebar-body'),
                $tree = $scroll.children('.chosen-tree'),
                $li = $(this).parent('.chosen-tree-item').parent('li');
        if ($li.hasClass('sub-tree')) {
            $li.toggleClass('open');
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
    });
</script>