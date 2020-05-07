<!DOCTYPE html>
<html lang="en">
<head>
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1" />
    <meta charset="utf-8" />
    <title>班级荣誉</title>

    <meta name="description" content="" />
    <script type="text/javascript" src="${resourceUrl}/js/LodopFuncs.js"></script>
    <![endif]-->
</head>

<body class="no-skin">


<!-- /section:basics/navbar.layout -->
<div class="main-container" id="main-container">


    <!-- /section:basics/sidebar -->
    <div class="main-content">

        <div class="main-content-inner">
            <div class="page-content">
                <div class="box box-default">
                    <form id="queryForm">
                    <div class="box-body">
                        <div >
                            <div class="filter">
                                <div class="filter-item">
                                    <span class="filter-name">学年：</span>
                                    <div class="filter-content">
                                        <select name="acadyear" id="acadyear" class="form-control" onchange="doChange();">
                                        <#if acadyearList?exists && (acadyearList?size gt 0)>
                                            <#list acadyearList as item>
                                                <option value="${item!}" <#if acadyear == item?default('')> selected </#if> >${item!}</option>
                                            </#list>
                                        <#else>
                                            <option value="">暂无数据</option>
                                        </#if>
                                        </select>
                                    </div>
                                </div>
                                <div class="filter-item">
                                    <span class="filter-name">学期：</span>
                                    <div class="filter-content">
                                        <select name="semester" id="semester" class="form-control" onchange="doSearch();">
                                        ${mcodeSetting.getMcodeSelect('DM-XQ',(semester?default(0))?string,'0')}
                                        </select>
                                    </div>
                                </div>
                                <#if isAdmin?default(false) >
                                    <div class="filter-item">
                                        <span class="filter-name">年级：</span>
                                        <div class="filter-content">
                                            <#list gradeList as grade>
                                            <input type="hidden" id="${grade.id!}Grade" value="${grade.gradeName!}">
                                            </#list>
                                            <select name="gradeId" id="gradeId" class="form-control" onchange="changeGradeId();" >
                                                <option value="" >--请选择--</option>
                                                <#list gradeList as grade>
                                                    <option value="${grade.id!}" >${grade.gradeName!}</option>
                                                </#list>

                                            </select>
                                        </div>
                                    </div>
                                </#if>

                                <div class="filter-item">
                                    <span class="filter-name">班级：</span>
                                    <div class="filter-content">

                                        <select name="classId" id="classId" class="form-control" onchange="doSearch();" >
                                            <#if isAdmin?default(false) >
                                            <option value="" >--请选择--</option>
                                            </#if>
                                        <#list classList as class>
                                            <option value="${class.id!}" >${class.className!}</option>
                                        </#list>

                                        </select>
                                    </div>
                                </div>
                                <div class="filter-item">
                                    <button type="button"  class="btn btn-blue" onclick="doSearch();">查找</button>
                                    <button type="button"  class="btn btn-blue" onclick="printArea();">打印</button>
                                    <button type="button"  class="btn btn-blue" onclick="saveAsFile();">导出</button>
                                </div>

                            </div>
                        </div>


                        <#--<div class="filter">-->
                            <#--<div class="filter-item">-->
                                <#--<button class="btn btn-blue" onclick="doUpdate();" >上传图片-->

                                <#--</button>-->
                                <#--<input type="file" id="fileUp" class="hidden">-->
                            <#--</div>-->
                        <#--</div>-->
                        <#--<div class="no-data-container">-->
                            <#--<div class="no-data">-->
										<#--<span class="no-data-img">-->
											<#--<img src="../images/growth-manual/no-img.png" alt="">-->
										<#--</span>-->
                                <#--<div class="no-data-body">-->
                                    <#--<h3>暂无图片</h3>-->
                                    <#--<p class="no-data-txt">请点击左上角的“上传图片”按钮添加</p>-->
                                <#--</div>-->
                            <#--</div>-->
                        <#--</div>-->
                    </div>
                    </form>
                </div>
                <div id="upLoadDiv">

                </div>
            </div><!-- /.page-content -->
        </div>
    </div><!-- /.main-content -->
</div><!-- /.main-container -->


<!-- basic scripts -->


<script>
    function changeGradeId(){
        var gradeId=$("#gradeId").val();
        var classClass=$("#classId");
        if(gradeId==""){
            classClass.html("");
            classClass.append("<option value='' >--请选择--</option>");
            searchList();
        }else{
            $.ajax({
                url:"${request.contextPath}/studevelop/devdocStatistic/changeClaListByGradeId",
                data:{gradeId:gradeId},
                dataType: "json",
                success: function(data){
                    var obj = eval(data);
                    var gradeName = $("#"+gradeId+"Grade").val();
                    classClass.html("");
                    if(obj.length==0){
                        classClass.append("<option value='' >--请选择--</option>");
                    }else{
                        classClass.append("<option value='' >--请选择--</option>");
                        for(var i = 0; i < obj.length; i ++){
                            classClass.append("<option value='"+obj[i].id+"' >" +gradeName+obj[i].className+"</option>");
                        }
                    }
                    doSearch();
                }
            });
        }
    }
    function doChange(){
        var acadyear = $("#acadyear").val();
        var semester = $("#semester").val();
        var url = "${request.contextPath}/studevelop/devdocStatistic/index/page?acadyear="+acadyear+"&semester="+semester;
        $('.model-div').load(url);
    }
    function  doSearch() {
        var classId = $("#classId").val();
        var gradeName = "";
        <#if isAdmin?default(false) >
            var gradeId=$("#gradeId").val();
            gradeName = $("#"+gradeId+"Grade").val();
        </#if>

        if(classId == ''){
            $("#upLoadDiv").load("${request.contextPath}/studevelop/devdocStatistic/gradeStatistic/list?gradeName="+gradeName+"&"
                    + $("#queryForm").serialize());
        }else{
            $("#upLoadDiv").load("${request.contextPath}/studevelop/devdocStatistic/claStatisticList/list?gradeName="+gradeName+"&" + $("#queryForm").serialize());
        }
    }
    function printArea(){
        LODOP=getLodop();
        LODOP.SET_PRINT_PAGESIZE(1, 0, 0,"A4");
        LODOP.SET_SHOW_MODE("LANDSCAPE_DEFROTATED",1);
        LODOP.SET_PRINT_MODE("FULL_WIDTH_FOR_OVERFLOW", true);
        LODOP.ADD_PRINT_HTM("5mm","4mm","RightMargin:4mm","BottomMargin:4mm",getPrintContent(jQuery("#upLoadDiv")));
        LODOP.PREVIEW();
    }

    function saveAsFile(){
        LODOP=getLodop();
        //LODOP.PRINT_INIT("");
        LODOP.ADD_PRINT_TABLE(100,20,500,60,getPrintContent(jQuery('#upLoadDiv')));
        //LODOP.SET_SAVE_MODE("FILE_PROMPT",false);
        LODOP.SET_SAVE_MODE("QUICK_SAVE",true);//快速生成（无表格样式,数据量较大时或许用到）
        //if (LODOP.SAVE_TO_FILE(document.getElementById("T1").value)) alert("导出成功！");
        LODOP.SAVE_TO_FILE("成长手册统计.xls");
    }
</script>
</body>
</html>
