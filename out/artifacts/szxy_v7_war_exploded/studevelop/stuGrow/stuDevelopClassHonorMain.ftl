<!DOCTYPE html>
<html lang="en">
<head>
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1" />
    <meta charset="utf-8" />
    <title>班级荣誉</title>

    <meta name="description" content="" />

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
                    <div class="box-header">
                        <h4 class="box-title">班级荣誉</h4>
                    </div>
                    <form id="queryClassHornr">
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
                                <div class="filter-item">
                                    <span class="filter-name">班级：</span>
                                    <div class="filter-content">

                                        <select name="classId" id="classId" class="form-control" onchange="doSearch();" >
                                        <#list classList as class>
                                            <option value="${class.id!}" <#if class_index == 0 >selected</#if>>${class.classNameDynamic!}</option>
                                        </#list>

                                        </select>
                                    </div>
                                </div>
                            </div>
                        </div>

                        <div id="upLoadDiv">

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
            </div><!-- /.page-content -->
        </div>
    </div><!-- /.main-content -->
</div><!-- /.main-container -->


<!-- basic scripts -->


<script>
    var classNum =0;
    $(function(){
    <#if !classList?exists || (classList?size == 0)>
        showWarnMsg("该学年下没有班级信息!");
    <#else >
        $("#upLoadDiv").load("${request.contextPath}/studevelop/classHonor/show/page?" + $("#queryClassHornr").serialize());
        classNum = ${classList?size };
    </#if>

    });
    function  doSearch() {
        if(classNum > 0){
            $("#upLoadDiv").load("${request.contextPath}/studevelop/classHonor/show/page?" + $("#queryClassHornr").serialize());
        }

    }
    function doChange() {
        var acadyear = $("#acadyear").val();
        var semester = $("#semester").val();
        var flag =true;
        var options = {
            url:"${request.contextPath}/studevelop/classHonor/classlist/page",
            dataType:"json",
            data:{"acadyear":acadyear},
            type:"post",
            success:function (data) {
                var len =data.length;
                if( len == 0){
                    layerTipMsg(false,"提示","该学年下没有班级信息!");
                    $("#upLoadDiv").empty();
                    flag =false;
                }else{
                    classNum = len;
                    flag = true;
                }
                var html = "";
                for(var i=0;i<len;i++){
                    var id = data[i].id;
                    var name = data[i].classNameDynamic
                    html += "<option value=\"" +id+"\" >" +name+"</option>";
                }
                $("#classId").html(html);

            }
        }
        $.ajax(options);
        if(flag){
            doSearch();
        }



    }
</script>
</body>
</html>
