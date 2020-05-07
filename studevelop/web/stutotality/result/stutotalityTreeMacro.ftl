<#macro findStuTree  showNumStr="" gradeId="gradeId" classId="classId" studentId="studentId" showStu="" showClass="" haveExport=true haveTeaching="0">
    <div class="credit-search input-group evaluate-tree-group">
        <input type="text" class="form-control" id="searchParam" value="${searchParam!}" placeholder="请输入" onkeypress="if(event.keyCode == 13){searchStudent()}"/>

        <i class="wpfont icon-close-round-fill evaluate-tree-close" onclick="closeinput(this)"></i>
        <a href="javascript:;" class="input-group-addon evaluate-tree-search" hidefocus="true" onclick="searchStudent()">
            <i class="wpfont icon-search"></i>
        </a>
    </div>
    <div class="page-sidebar-body" <#if haveExport?default(true)>style="top: 115px;"<#else>style="top: 56px;"</#if>>
        <#assign gradeIdOne="">
        <#assign classIdOne="">
        <div id="treeDiv">
            <ul class="chosen-tree chosen-tree-tier1">
                <#if gradeList?exists&&(gradeList?size>0)>
                    <#list gradeList as item>
                        <li class="sub-tree <#if item_index==0> open <#assign gradeIdOne='${item.id}'></#if>"><#--<#if searchParam??> open</#if>-->
                            <div class="chosen-tree-item" data-index="1" onclick="gradeClick('${item.id}')">
                                <span class="arrow"></span>
                                <span class="name" id="gradeName">${item.gradeName!}</span>
                                <#--<#if showNumStr?default("")!=""><i class="typein-no-tip">${showNumStr!}</i></#if>-->
                            </div>
                            <#if (classListMap?size>0)>
                                <#if classListMap[item.id]?exists&&(classListMap[item.id]?size>0)>
                                    <#list classListMap[item.id] as item1>
                                        <ul class="chosen-tree chosen-tree-tier2">
                                            <li class="sub-tree <#if item_index==0 && item1_index==0> open <#assign classIdOne='${item1.id}'></#if>">
                                                <div class="chosen-tree-item" data-index="11" id="Cl_${item1.id}" onclick="classClick('${item1.id}')">
                                                    <span class="arrow"></span>
                                                    <span class="name">${item1.classNameDynamic!}</span>
                                                    <#if showNumStr?default("")!="">
                                                        <div id="checkAll${item1.id!}"></div>
                                                    </#if>
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
        <div id="searchDiv" style="display:none;">

        </div>
        <input type="hidden" id="myStuId" value="">
        <script type="text/javascript">
            $(function () {
                gradeClick('${gradeIdOne!}');
                var classIdOne='${classIdOne!}';
                if(classIdOne && classIdOne!=""){
                    classClick(classIdOne,"first");
                }
                //classClick("");
            })
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
                $("#treeDiv").show();
                $("#searchDiv").hide();
            }
            function gradeClick(gradeId) {
                <#if showNumStr?default("")!="">
                    checkGradeStu(gradeId);
                </#if>
                <#--$("#${gradeId!}").val(gradeId);-->
            }
            function stuClick(studentId) {
                $("#myStuId").val(studentId)
                $("#${studentId!}").val(studentId);
                <#if showStu?default("")!="">
                    ${showStu!}(studentId);
                </#if>
            }
            function classClick(classId,first) {
                $("#${classId!}").val(classId);
                $("#${studentId!}").val("");//点击班级时 把学生id置为空
                $("#Cl_"+classId).parent().find("ul").each(function () {
                    $(this).remove();
                });
                <#if showClass?default("")!="">
                if(!first){
                    ${showClass!}(classId);
                }
                </#if>

                $.ajax({
                    url: "${request.contextPath}/stutotality/result/findStuListByClassId",
                    data: {'classId': classId},
                    type: 'post',
                    dataType: 'json',
                    success: function (data) {
                        var studentList = data.studentList;
                        var html = "";
                        var studentIdOne="";
                        if (studentList && studentList.length > 0) {
                            for (var i = 0; i < studentList.length; i++) {
                                if(i==0){
                                    studentIdOne=studentList[i].id;
                                }
                                html +="<ul class='chosen-tree chosen-tree-tier3'><li id='stuLiId"+i+"'><div class='chosen-tree-item' data-index='111' ";
                                html +="onclick='stuClick(" + "\"" + studentList[i].id + "\"" + ")'><a><span class='arrow'></span><span class='name'><div id='check";
                                html +=studentList[i].id + "' ></div>";
                                html +=studentList[i].studentName + "</span></a></div></li></ul>";
                            }
                        }
                        $("#Cl_"+classId).after(html);
                        if(first && studentIdOne && studentIdOne!=""){
                            $("#stuLiId0").addClass("active");
                            stuClick(studentIdOne);
                        }
                        <#if showNumStr?default("")!="">
                        checkStu(classId);
                        </#if>
                    },
                    error: function (XMLHttpRequest, textStatus, errorThrown) {
                    }
                });
            }

            function searchStudent() {
                var searchParam = $("#searchParam").val();
                var html = "";
                // $("#treeDiv").html(html);
                if(searchParam && $.trim(searchParam)!="") {
                    //$(".sub-tree").attr("open");
                    $("#treeDiv").hide();
                    $.ajax({
                        url: "${request.contextPath}/stutotality/result/findStuListByClassId",
                        data: {'searchParam': searchParam,'haveTeaching':'${haveTeaching!}'},
                        type: 'post',
                        dataType: 'json',
                        success: function (data) {
                            var studentList = data.studentList;
                            if (studentList && studentList.length > 0) {
                                for (var i = 0; i < studentList.length; i++) {
                                    html = html + "<ul class='chosen-tree chosen-tree-tier3' style='cursor:pointer'><li ><div style='padding: 10px 20px' data-index='111' onclick='stuClick(" + "\"" + studentList[i].id + "\"" + ")'><a><span class='arrow'></span><span class='name'>" + studentList[i].studentName + "</span></a></div></li></ul>";
                                }
                                $("#searchDiv").html(html);
                            }else {
                                $("#searchDiv").html('<div class="no-data-container">\n' +
                                    '<div class="no-data">\n' +
                                    '<span class="no-data-img">\n' +
                                    '   <img src="${request.contextPath}/static/images/public/nodata6.png" alt="">\n' +
                                    '</span>\n' +
                                    '<div class="no-data-body">\n' +
                                    '       <p class="no-data-txt">暂无数据</p>\n' +
                                    '</div>\n' +
                                    '</div>\n' +
                                    '</div>');
                            }
                            $("#searchDiv").show();
                        },
                        error: function (XMLHttpRequest, textStatus, errorThrown) {
                        }
                    });
                }else {
                    <#--var url = '${request.contextPath}/stutotality/result/getStuClassList';-->
                    $("#treeDiv").show();
                    $("#searchDiv").hide();
                }
            }

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
    </div>
</#macro>