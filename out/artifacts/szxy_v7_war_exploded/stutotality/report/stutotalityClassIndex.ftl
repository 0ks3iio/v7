<div class="box box-default" >
    <div class="box-body clearfix">
        <div class="picker-table">
            <table class="table" style="margin-bottom: 10px;">
                <tbody>
                <tr>
                    <th width="150" style="vertical-align: top;">
                        年级：
                    </th>
                    <td>
                        <div class="outter" id="outter1">

                            <#if gradeId?exists>
                                <#if gradeList?exists&&(gradeList?size>0)>
                                    <#list gradeList as item>
                                        <a <#if gradeId==item.id>class="selected"</#if> onclick="gradeClick(this)" value="${item.id!}" data-id="${item.id!}"  href="javascript:;">${item.gradeName}</a>
                                    </#list>
                                </#if>
                            <#else >
                                <#if gradeList?exists&&(gradeList?size>0)>
                                    <#list gradeList as item>
                                        <a <#if item_index==0>class="selected"</#if> onclick="gradeClick(this)" value="${item.id!}" data-id="${item.id!}"  href="javascript:;">${item.gradeName}</a>
                                    </#list>
                                </#if>
                            </#if>

                        </div>
                    </td>
                    <td width="100" style="vertical-align: top;">
                    </td>
                </tr>
                <tr>
                    <th width="150" style="vertical-align: top;">
                        班级：
                    </th>
                    <td>
                        <div class="outter">
                            <div class="evaluate-outter-body" id="outter2">
                            </div>
                        </div>
                    </td>
                    <td width="100" style="vertical-align: top;">
                        <div class="outter">
                            <a class="picker-more" href="javascript:;"
                            ><span>展开</span
                                ><i class="fa fa-angle-down"></i
                                ></a>
                        </div>
                    </td>
                </tr>
                </tbody>
            </table>
        </div>
        <div id="divList">
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

<script>
    function outtersize() {
        $(".evaluate-outter-body").each(function(i,v) {
            if($(v).height() <= 34){
                $(v).parents("tr").find(".picker-more").css("display","none");
            }else{
                $(v).parents("tr").find(".picker-more").css("display","inline-block");
            }
        })
    }
    $(function () {
        outtersize();
        var gradeId = "";
        $("#outter1").find("a").each(function () {
            if($(this).attr("class")=="selected"){
                gradeId = $(this).attr("value")
            }
        })
        if(gradeId){
            getClass(gradeId);
        }
        var classId = "";
        $("#outter2").find("a").each(function () {
            if($(this).attr("class")=="selected"){
                classId = $(this).attr("value")
            }
        })
        if(!classId){
            classId="";
        }
       /* if(gradeId) {
            getAttendanceList(gradeId, classId);
        }*/
        // 展开/折叠
        $(".picker-more").on('click',function(e) {
            if ($(this).children("span").text() == "展开") {
                $(this).children("span").text("折叠");
                $(this).children(".fa").addClass("fa-angle-up").removeClass("fa-angle-down");
            } else {
                $(this).children("span").text("展开");
                $(this).children(".fa").addClass("fa-angle-down").removeClass("fa-angle-up");
            }
            $(this).parents("td").siblings("td").children(".outter").toggleClass("outter-auto");
        });
    })
    $(window).resize(function() {
        $(".point-pro-box").css("min-height", $("#sidebar").height() - 240);
        outtersize()
    })
    function gradeClick(that) {
        $("#outter1").find("a").each(function () {
            $(this).attr("class","");
        })
        $(that).attr("class","selected");
        var gradeId = $(that).attr("value")
        getClass(gradeId);
    }
    function getClass(gradeId) {
        var oldId = '${classId!}'
        var html="";
        $.ajax({
            url: "${request.contextPath}/stutotality/result/getClassesByGradeId?",
            data: {
                'gradeId': gradeId,
            },
            type: 'post',
            dataType: 'json',
            success: function (data) {
                $("#outter2").html(html);
                var classList = data.classList;
                var optionDes = data.optionDes;
                for(var i=0;i<classList.length;i++){
                    if(oldId){
                        if(classList[i].id == oldId) {
                            html = html + "<a href='#' onclick='classClick(this)'   class='selected' value='" + classList[i].id + "'  data-id='" + classList[i].id + "' >" + classList[i].classNameDynamic + "</a>";
                        }else {
                            html = html + "<a href='#' onclick='classClick(this)'  value='" + classList[i].id + "'   data-id='" + classList[i].id + "'>" + classList[i].classNameDynamic + "</a>";
                        }
                    } else{
                        if(i==0) {
                            html = html + "<a href='#' onclick='classClick(this)'   class='selected' value='" + classList[i].id + "'  data-id='" + classList[i].id + "' >" + classList[i].classNameDynamic + "</a>";
                        }else {
                            html = html + "<a href='#' onclick='classClick(this)'  value='" + classList[i].id + "'   data-id='" + classList[i].id + "'>" + classList[i].classNameDynamic + "</a>";
                        }
                    }

                    $(".typein-right-tip").mouseover(function() {
                        layer.tips(optionDes, ".typein-right-tip", {
                            tips: [3, "#317EEB"], //3指类型
                            time: 2000, //20s后自动关闭
                            maxWidth: 360
                        });
                    });
                }
                $("#outter2").html(html);
                outtersize();
                var classId = "";
                $("#outter2").find("a").each(function () {
                    if($(this).attr("class")=="selected"){
                        classId = $(this).attr("value")
                    }
                })
                if(!classId){
                    classId="";
                }
                if(gradeId) {
                    getAttendanceList(gradeId, classId);
                }
            },
            error: function (XMLHttpRequest, textStatus, errorThrown) {
            }
        });
    }

    function classClick(that) {
        $("#outter2").find("a").each(function () {
            $(this).attr("class","");
        })
        $(that).attr("class","selected");
        var gradeId = "";
        $("#outter1").find("a").each(function () {
            if($(this).attr("class")=="selected"){
                gradeId = $(this).attr("value")
            }
        })
        // if(gradeId){
        //     getClass(gradeId);
        // }
        var classId = "";
        $("#outter2").find("a").each(function () {
            if($(this).attr("class")=="selected"){
                classId = $(this).attr("value")
            }
        })
        if(!classId){
            classId="";
        }
        if(gradeId) {
            getAttendanceList(gradeId, classId);
        }
    }

    function getAttendanceList(gradeId,classId) {
        var url = '${request.contextPath}/stutotality/report/getClassTotalityReport?classId='+classId;
        $("#divList").load(url)
    }

</script>