
    <div class="box box-default">
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
                                <#if gradeList?exists&&(gradeList?size>0)>
                                    <#list gradeList as item>
                                        <a <#if item_index==0>class="selected"</#if> onclick="gradeClick(this)" value="${item.id!}" href="#">${item.gradeName}</a>
                                    </#list>
                                </#if>
                                <#--<a class="selected" href="#">小一</a>-->
                                <#--<a href="#">小二</a>-->
                                <#--<a href="#">小三</a>-->
                            </div>
                        </td>
                        <td width="100" style="vertical-align: top;">
                        </td>
                        <#--<td width="100" style="vertical-align: top;">-->
                            <#--<div class="outter">-->
                                <#--<a class="picker-more" href="javascript:void(0)" onclick="classClick(this)"><span>展开</span><i class="fa fa-angle-down"></i></a>-->
                            <#--</div>-->
                        <#--</td>-->
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
                                <#--<a class="selected" href="#">01班</a>-->
                                <#--<a href="#">02班</a>-->
                                <#--<a href="#">03班</a>-->
                            </div>
                        </td>
                        <td width="100" style="vertical-align: top;">
                            <div class="outter">
                                <a class="picker-more" href="javascript:void(0)"><span>展开</span><i class="fa fa-angle-down"></i></a>
                            </div>
                        </td>
                    </tr>
                    </tbody>
                </table>
            </div>
            <div class="filter evaluate-filter">
                <div class="filter-item">
                    <div class="btn-pick-box">
                        <div class="btn btn-blue font-14" id="statState">统计全优生</div>
                        <ul class="btn-pick-ul" id="statStateItem">
                            <#if isAdmin><li onclick="statGrade()">年级</li></#if>
                            <li onclick="statClass()">班级</li>
                        </ul>
                    </div>
                </div>
                <div class="filter-item filter-item-right">
                    <span class="filter-name">全优生占比：</span>
                    <div class="filter-content">
                        <div class="form-shuzi form-shuzi-lg" style="width: 120px;" data-step="1" data-max="99">
                            <input type="text" id="percent" class="form-control evaluate-control pointnum2" numtype="" <#if percent?exists>value="${percent}" </#if> onkeypress="if(event.keyCode == 13){saveScale()}" />
                            <i class="wpfont icon-close-round-fill evaluate-shuzi-close" onclick="closenuminput(this)" style="top:2px;"></i>
                            <div class="btn btn-sm btn-default btn-block form-shuzi-add">
                                <i class="fa fa-angle-up"></i>
                            </div>
                            <div class="btn btn-sm btn-default btn-block form-shuzi-sub">
                                <i class="fa fa-angle-down"></i>
                            </div>
                        </div>
                        %
                    </div>
                </div>
            </div>
            <div class="evaluate-item" id="divList">
                <div class="no-data-container">
                    <div class="no-data">
                <span class="no-data-img">
                    <img src="${request.contextPath}/static/images/public/nodata6.png" alt="">
                </span>
                        <div class="no-data-body">
                            <p class="no-data-txt">暂无相关数据</p>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
<script>
    $(function () {
        outtersize();
        $("#outter1").find("a").each(function () {
            if($(this).attr("class")=="selected"){
                $(this).click();
            }
        })
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
        // showList1();
    })

    $(window).resize(function() {
        $(".point-pro-box").css("min-height", $("#sidebar").height() - 240);
        outtersize()
    })

    function outtersize() {
        $(".evaluate-outter-body").each(function(i,v) {
            if($(v).height() <= 34){
                $(v).parents("tr").find(".picker-more").css("display","none");
            }else{
                $(v).parents("tr").find(".picker-more").css("display","inline-block");
            }
        })
    }
    <#--function showList1(){-->
        <#--var url = '${request.contextPath}/stutotality/stat/statList?';-->
        <#--$("#divList").load(url);-->
    <#--}-->
    function saveScale() {
        var per = $("#percent").val();
        var gradeId ;
        $("#outter1").find('a').each(function () {
            if($(this).attr("class")=="selected"){
                gradeId = $(this).attr("value");
            }
        });
        if(!gradeId){
            return;
        }
        var classId ;
        $("#outter2").find('a').each(function () {
            if($(this).attr("class")=="selected"){
                classId = $(this).attr("value");
            }
        });
        var per = $("#percent").val();
        if(!per){
            per="";
        }else {
            if(per>=100){
                layer.tips('格式不正确(不能超过100)!', $("#percent"), {
                    tipsMore: true,
                    tips: 3
                });
                return;
            }
        }
        if(!classId){
            classId="";
        }
        if(!per){
            per="";
        }
        $.ajax({
            url: "${request.contextPath}/stutotality/stat/saveScale?",
            data: {
                'classId': classId,
                'gradeId':gradeId,
                'percent':per
            },
            type: 'post',
            dataType: 'json',
            success: function (data) {
                var obj=data;
                if(obj.type=="success"){

                }else if(obj.type=="error"){

                }
            },
            error: function (XMLHttpRequest, textStatus, errorThrown) {
            }
        });
    }
    
    function statClass() {
        var classId ;
        $("#outter2").find('a').each(function () {
            if($(this).attr("class")=="selected"){
                classId = $(this).attr("value");
            }
        });
        var per = $("#percent").val();
        if(!classId){
            return;
        }
        if(!per){
            per="";
        }else {
            if(per>=100){
                layer.tips('格式不正确(不能超过100)!', $("#percent"), {
                    tipsMore: true,
                    tips: 3
                });
                return;
            }
            per = per/100;
        }
        $("#statState").addClass("disabled");
        $("#statStateItem").hide();
        $.ajax({
            url: "${request.contextPath}/stutotality/stat/statResult?",
            data: {
                'classId': classId,
                'percent':per
            },
            type: 'post',
            dataType: 'json',
            success: function (data) {
                var obj=data;
                if(obj.type=="success"){
                    layer.msg("统计成功", {
                        offset: 't',
                        time: 2000
                    });
                    $("#statState").removeClass("disabled");
                    $("#statStateItem").css("display","");
                    window.setTimeout("search()",2000);
                }else if(obj.type=="error"){
                    if(obj.errorType) {
                        layer.msg(obj.errorType, {
                            offset: 't',
                            time: 2000
                        });
                    }else {
                        layer.msg("统计失败", {
                            offset: 't',
                            time: 2000
                        });
                    }
                    $("#statState").removeClass("disabled");
                    $("#statStateItem").css("display","");
                }else{
                    //循环访问结果
                    window.setTimeout(statClass(),5000);
                }
            },
            error: function (XMLHttpRequest, textStatus, errorThrown) {
            }
        });
    }

    function statGrade() {
        var per = $("#percent").val();
        var gradeId ;
        $("#outter1").find('a').each(function () {
            if($(this).attr("class")=="selected"){
                gradeId = $(this).attr("value");
            }
        });
        if(!gradeId){
            return;
        }
        var classId ;
        $("#outter2").find('a').each(function () {
            if($(this).attr("class")=="selected"){
                classId = $(this).attr("value");
            }
        });
        var per = $("#percent").val();
        if(!classId){
            classId="";
        }
        if(!per){
            per="";
        }else {
            if(per>=100){
                layer.tips('格式不正确(不能超过100)!', $("#percent"), {
                    tipsMore: true,
                    tips: 3
                });
                return;
            }
            per = per/100;
        }
        $("#statState").addClass("disabled");
        $("#statStateItem").hide();
        $.ajax({
            url: "${request.contextPath}/stutotality/stat/statResult?",
            data: {
                'gradeId': gradeId,
                'percent': per,
                'classId':classId
            },
            type: 'post',
            dataType: 'json',
            success: function (data) {
                var obj=data;
                if(obj.type=="success"){
                    layer.msg("统计成功", {
                        offset: 't',
                        time: 2000
                    });
                    $("#statState").removeClass("disabled");
                    $("#statStateItem").css("display","");
                    setTimeout("search()",2000);
                }else if(obj.type=="error"){
                    if(obj.errorType) {
                        layer.msg(obj.errorType, {
                            offset: 't',
                            time: 2000
                        });
                    }else {
                        layer.msg("统计失败", {
                            offset: 't',
                            time: 2000
                        });
                    }
                    $("#statState").removeClass("disabled");
                    $("#statStateItem").css("display","");
                }else{
                    //循环访问结果
                    window.setTimeout(statGrade(),5000);
                }
                //search();
            },
            error: function (XMLHttpRequest, textStatus, errorThrown) {
            }
        });
    }

    function gradeClick(that) {
        $("#divList").html('<div class="no-data-container">\n' +
        '                    <div class="no-data">\n' +
        '                <span class="no-data-img">\n' +
        '                    <img src="${request.contextPath}/static/images/public/nodata6.png" alt="">\n' +
        '                </span>\n' +
        '                        <div class="no-data-body">\n' +
        '                            <p class="no-data-txt">暂无相关数据</p>\n' +
        '                        </div>\n' +
        '                    </div>\n' +
        '                </div>');
        $("#outter1").find("a").each(function () {
            $(this).attr("class","");
        })
        $(that).attr("class","selected");
        var gradeId = $(that).attr("value");
        var html = "";
        $.ajax({
            url: "${request.contextPath}/stutotality/stat/getClassListByGradeId?",
            data: {
                'gradeId': gradeId,
            },
            type: 'post',
            dataType: 'json',
            success: function (data) {
                $("#outter2").html(html);
                var classList = data.classList;
                for(var i=0;i<classList.length;i++){
                    if(i==0) {
                        html = html + "<a href='#' onclick='classClick(this)' class='selected' value='" + classList[i].id + "'>" + classList[i].classNameDynamic + "</a>";
                    }else {
                        html = html + "<a href='#' onclick='classClick(this)'  value='" + classList[i].id + "'>" + classList[i].classNameDynamic + "</a>";
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
                search();
                outtersize();
            },
            error: function (XMLHttpRequest, textStatus, errorThrown) {
            }
        });
        //var itemId = $(that).attr("value");
        // search();

    }

    function classClick(that) {
        $("#outter2").find("a").each(function () {
            $(this).attr("class","");
        })
        $(that).attr("class","selected");
        search();
    }
    function search() {
        var gradeId ;
        $("#outter1").find('a').each(function () {
            if($(this).attr("class")=="selected"){
                gradeId = $(this).attr("value");
            }
        });
        var classId;
        $("#outter2").find('a').each(function () {
            if($(this).attr("class")=="selected"){
                classId = $(this).attr("value");
            }
        });
        if(gradeId&&classId){
            var url = '${request.contextPath}/stutotality/stat/statList?gradeId='+gradeId+"&classId="+classId;
            $("#divList").load(url)
        }
    }
    //清空内容
    function closenuminput(that) {
        $(that).siblings("input.form-control").val("");
        $(that).siblings("input.form-control").change();
    }
    //包含小数点数字输入框
    $('.form-shuzi input.form-control').keyup(function(){
        var num = /^\d+(\.\d{0,2})?$/;
        var max = parseFloat($(this).parent('.form-shuzi').attr("data-max"));
        if (max === undefined) {
            max = 999;
        } else{
            max = parseFloat(max);
        }
        if($(this).attr("numtype") == "int") {
            num = /^\d*$/;
        }else{
            num = /^\d+(\.\d{0,2})?$/;
        }
        if(!num.test($(this).val())){
            $(this).val('');
            $(this).change();
        };
        if($(this).val() > max) {
            $(this).val(max);
            $(this).change();
        }
    });
    $('.form-shuzi > .btn').click(function(e){
        e.preventDefault();
        var $num = $(this).siblings('input.form-control');
        var max = parseFloat($(this).parent('.form-shuzi').attr("data-max"));
        var val = $num.val();
        if (!val ) val = 0;
        var num = parseFloat(val);
        var step = $num.parent('.form-shuzi').attr('data-step');
        if (step === undefined) {
            step = 1;
        } else{
            step = parseFloat(step);
        }
        if ($(this).hasClass('form-shuzi-add')) {
            num = parseFloat(numAdd(num,step));
        } else{
            num = parseFloat(numSub(num,step));
            if (num <= 0) num = 0;
        }
        if(num > max) {
            $num.val(max);
            $num.change();
        }else{
            $num.val(num);
            $num.change();
        }
    });

    $(".form-shuzi").find("input.form-control").bind("input propertychange", function(event) {
        if($(this).val() == ""){
            $(this).siblings(".evaluate-shuzi-close").removeClass("active");
        }else{
            $(this).siblings(".evaluate-shuzi-close").addClass("active");
        }
    });
    $(".form-shuzi").find("input.form-control").on("change", function(event) {
        if($(this).val() == ""){
            $(this).siblings(".evaluate-shuzi-close").removeClass("active");
        }else{
            $(this).siblings(".evaluate-shuzi-close").addClass("active");
        }
    });

    $(".form-shuzi").find("input.form-control").focus(function() {
        if($(this).val() == ""){
            $(this).siblings(".evaluate-shuzi-close").removeClass("active");
        }else{
            $(this).siblings(".evaluate-shuzi-close").addClass("active");
        }
    })
</script>