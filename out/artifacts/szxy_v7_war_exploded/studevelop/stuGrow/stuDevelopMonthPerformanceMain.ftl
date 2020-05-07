<!DOCTYPE html>
<html lang="en">
<head>
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1" />
    <meta charset="utf-8" />
    <title>成长手册-每月在校表现</title>

    <meta name="description" content="" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0" />
    <![endif]-->
    <link rel="stylesheet" href="${request.contextPath}/static/components/bootstrap/dist/css/bootstrap.css" />
    <link rel="stylesheet" href="${request.contextPath}/static/components/font-awesome/css/font-awesome.css" />

    <!-- page specific plugin styles -->
    <!-- <link rel="stylesheet" href="../components/layer/skin/layer.css"> -->
    <link rel="stylesheet" href="${request.contextPath}/static/components/chosen/chosen.min.css">
    <link rel="stylesheet" href="${request.contextPath}/static/components/bootstrap-datepicker/dist/css/bootstrap-datepicker3.css">
    <link rel="stylesheet" href="${request.contextPath}/static/components/datatables/media/css/jquery.dataTables.min.css">


    <link rel="stylesheet" href="${request.contextPath}/static/css/iconfont.css">
    <link rel="stylesheet" href="${request.contextPath}/static/css/components.css">
    <link rel="stylesheet" href="${request.contextPath}/static/css/page-desktop.css">
    <link rel="stylesheet" href="${request.contextPath}/static/css/pages.css">
</head>

<body class="no-skin">


    <!-- /section:basics/sidebar -->
    <div class="main-content">

        <div class="main-content-inner">
            <div class="page-content">
                <div class="box box-default">
                    <div class="box-header">
                        <h4 class="box-title">每月在校表现</h4>
                    </div>
                    <div class="box-body">
                        <form id="seach">
                        <div class="filter">
                            <div class="filter-item">
                                <span class="filter-name">学年：</span>
                                <div class="filter-content">
                                    <select name="acadyear" id="acadyear" class="form-control" onchange="doChange();" >
                                    <#if acadyearList?exists && (acadyearList?size gt 0) >
                                        <#list acadyearList as item>
                                            <option value="${item!}" <#if acadyear?default('') == item?default('')> selected </#if> >${item!}</option>
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
                                    <select name="semester" id="semester" class="form-control" onchange="doSearch();" >
                                    ${mcodeSetting.getMcodeSelect('DM-XQ',(semester?default(0))?string,'0')}
                                    </select>
                                </div>
                            </div>
                            <div class="filter-item">
                                <span class="filter-name">班级：</span>
                                <div class="filter-content">
                                    <select name="classId" id="classId" class="form-control" onchange="doShowList();" >
                                        <#list classList as class>
                                            <option value="${class.id!}" <#if classId?default('') == class.id >selected</#if>> ${class.classNameDynamic!}</option>
                                        </#list>

                                    </select>
                                </div>
                            </div>
                            <div class="filter-item">
                                <span class="filter-name">月份：</span>
                                <div class="filter-content">
                                    <select name="performMonth" id="performMonth" class="form-control" onchange="doShowList();" >
                                        <#if months?exists && months?size gt 0 >

                                            <#list months as month>
                                                <option value=" ${(month)?substring(5,7)}" <#if (month)?ends_with(performMonth?default(''))  > selected</#if>  >${month}月</option>
                                            </#list>
                                        </#if>
                                        <#--<#list 1..12 as i>-->
                                            <#--<#if (  i  gte startMonth ) && (i lte endMonth )  >-->
                                            <#--<option value="${i}" <#if currentMonth == i> selected </#if> >${i}月</option>-->
                                            <#--</#if>-->
                                        <#--</#list>-->

                                    </select>
                                </div>
                            </div>
                            <div class="filter-item">
                                <button type="button" class="btn btn-blue" onclick="doShowList();">查找</button>
                            </div>
                        </div>
                        </form>
                        <div class="explain">
                            <h4>说明</h4>
                            <p>1. 如果不改现在的等级设置，说明：A 表示最好，A>B>C>D>E</p>
                            <p>2. 标题栏中选中 A ，表示所有学生该项全都为 A </p>
                        </div>

                        <div class="table-container">
                            <div class="table-container-header text-right">
                                <button type="button" class="btn btn-sm btn-white" onclick="copyLastMouth();">复制上月</button>
                                <button type="button" onclick="doImport();" class="btn btn-sm btn-white">从Execl导入</button>
                            </div>
                            <div id="contentPerformance">
                            </div>
                            </div>
                        </div>

                    </div>
                </div>
            </div><!-- /.page-content -->
        </div>
    </div><!-- /.main-content -->
</div><!-- /.main-container -->


<!-- basic scripts -->


<script type="text/javascript">
    //if('ontouchstart' in document.documentElement) document.write("<script src='../components/_mod/jquery.mobile.custom/jquery.mobile.custom.js'>"+"<"+"/script>");

    jQuery(document).ready(function(){
        <#if !classList?exists || (classList?size == 0)>
            showWarnMsg("该学年下没有班级信息!");
        </#if>
       $("#contentPerformance").load("${request.contextPath}/studevelop/performance/list/page?"+jQuery("#seach").serialize());

    });
    function doChange() {
        <#--var acadyear = $("#acadyear").val();-->
        <#--var semester = $("#semester").val();-->

            <#--var options = {-->
                <#--url:"${request.contextPath}/studevelop/performance/classlist/page",-->
                <#--dataType:"json",-->
                <#--data:{"acadyear":acadyear},-->
                <#--type:"post",-->
                <#--async:false,-->
                <#--success:function (data) {-->
                    <#--var len =data.length;-->
                    <#--var html = "";-->
                    <#--if(len == 0){-->
<#--//                        showWarnMsg("该学年下没有班级信息!");-->
                        <#--layerTipMsg(false,"提示","该学年下没有班级信息!");-->
                    <#--}-->
                    <#--for(var i=0;i<len;i++){-->
                        <#--var id = data[i].id;-->
                        <#--var name = data[i].classNameDynamic-->
                        <#--html += "<option value=\"" +id+"\" >" +name+"</option>";-->
                    <#--}-->
                    <#--$("#classId").html(html);-->
                <#--}-->
            <#--}-->
            <#--$.ajax(options);-->
        doSearch();


    }
    function doSearch() {

        $(".model-div").load("${request.contextPath}/studevelop/performance/index/page?"+jQuery("#seach").serialize());
    }
    function doShowList() {
        $("#contentPerformance").load("${request.contextPath}/studevelop/performance/list/page?"+jQuery("#seach").serialize());
    }
    function copyLastMouth() {
        var classId = $("#classId").val();
        if(classId == "" || classId == undefined){
            showWarnMsg("没有班级信息不能复制上月");
            return ;
        }
        showConfirmMsg('复制上月会将本月已存在信息覆盖，确认复制上月？','提示',function(){
            var options = {
                url:"${request.contextPath}/studevelop/mouthPerformance/copyLastMouth/page",
                dataType:"json",
                type:"post",
                success:function (data) {

                    if(data.success){
                        layer.closeAll();
                        layerTipMsg(data.success,"复制成功",data.msg);
                        doSearch();
                        return;
                    }else{
                        layer.closeAll();
                        layerTipMsg(data.success,"复制失败",data.msg);
                        return;
                    }
                },
                error:function(XMLHttpRequest ,textStatus,errorThrown){}
            };
            $("#seach").ajaxSubmit(options);
        });

    }
    function  doImport() {
        
        <#--window.open("${request.contextPath}/studevelop/monthPerformance/importLink");-->
        $(".model-div").load("${request.contextPath}/studevelop/monthPerformance/importLink?"+jQuery("#seach").serialize());
    }
</script>


</body>
</html>
