<!DOCTYPE html>
<html lang="en">

    <![endif]-->

    <!-- HTML5shiv and Respond.js for IE8 to support HTML5 elements and media queries -->

    <!--[if lte IE 8]>
    <script src="../components/html5shiv/dist/html5shiv.min.js"></script>
    <script src="../components/respond/dest/respond.min.js"></s

<body class="no-skin">
<!-- #section:basics/navbar.layout -->


<!-- /section:basics/navbar.layout -->
<div class="main-container" id="main-container">



    <!-- /section:basics/sidebar -->
    <div class="main-content">

        <div class="main-content-inner">
            <div class="page-content">
                <div class="box box-default">
                    <div class="box-header">
                        <h4 class="box-title">每月在校表现-评分项目</h4>
                    </div>
                    <div class="box-body">
                        <div class="filter">
                            <div class="filter-item">
                                <span class="filter-name">学段：</span>
                                <div class="filter-content">
                                    <select name="" id="section" class="form-control" onchange="gradeList();">
                                         <#if detailList?exists && (detailList?size gt 0) >
                                             <#list detailList as detail>
                                                 <option value="${detail.thisId!}">${detail.mcodeContent!}</option>
                                             </#list>
                                         </#if>
                                    </select>
                                </div>
                            </div>

                        </div>

                        <ul class="nav nav-tabs" role="tablist" id="gradeUl">

                        <#if  gradeList?exists && (gradeList?size gt 0)>
                            <#list gradeList as grade>
                                <li role="presentation" <#if grade_index == 0> class="active" </#if> >
                                    <a href="javascript:void(0)" role="tab" val="${grade.gradeCode!}" onclick="gradePerformItem('${grade.gradeCode!}');" data-toggle="tab">${grade.gradeName!}</a>
                                </li>
                            </#list>

                        </#if>
                        </ul>
                        <div class="tab-content">
                            <div id="aa" class="tab-pane active" role="tabpanel">
                                <div class="table-container">
                                    <div class="table-container-header text-right">
                                        <button type="button" class="btn btn-sm btn-white js-addScoreItem" onclick="editLink('','false')">添加评估项目</button>
                                        <button type="button" class="btn btn-sm btn-white" onclick="copyGradeLink();">复制到其他年级</button>
                                    </div>
                                    <div id="itemDiv">

                                    </div>
                                </div>
                            </div>
                            <div id="bb" class="tab-pane" role="tabpanel"></div>
                            <div id="cc" class="tab-pane" role="tabpanel"></div>
                        </div>
                    </div>
                </div>
            </div><!-- /.page-content -->
        </div>
    </div><!-- /.main-content -->
</div><!-- /.main-container -->
    <form id="performForm">
        <div class="layer layer-addScoreItem" id="editDiv" >



        </div>
    </form>

<!-- basic scripts -->




<!-- inline scripts related to this page -->
<script>
    function copyGradeLink() {
        var gradeCode = $("#gradeUl").find("li[class = 'active']").find("a").attr("val");
        if(gradeCode == "" ||  gradeCode == undefined ){
            showWarnMsg("没有年级信息不能复制到其他年级");
            return ;
        }
        $("#editDiv").load("${request.contextPath}/studevelop/performanceItem/copyToGrade?gradeCode="+gradeCode );
        layer.open({
            type: 1,
            offset:"t",
            shade: .5,
            title:'复制到其他年级',
            btn :['确定','取消'],
            btn1:function(index ,layero){
                doCopyGrade();
                return false;
            },
            btn2:function(index ,layro){
                layer.closeAll();
            },
            area: '360px',
            content: $(' .layer-addScoreItem')
        });

    }
    function doCopyGrade(){
        var gradeCodes = "";
        $("span.active").each(function () {
//            gradeCodes.push($(this).attr("val"));
            gradeCodes = gradeCodes + $(this).attr("val") + ",";
        })
        gradeCodes = gradeCodes.substr(0,gradeCodes.length-1);
        if(gradeCodes == ""){
            layerTipMsgWarn("提示","请选择至少一个年级");
            return;
        }
        var gradeCode = $("#gradeUl").find("li[class = 'active']").find("a").attr("val");
        var options={
            url:"${request.contextPath}/studevelop/performanceItem/copyToGradesEdit",
            dataType:"json",
            type:"post",
            data:{"itemGrade":gradeCode ,"gradeCodes":gradeCodes},
            success:function(data){
                var jsonO = data;
                if(!jsonO.success){
                    if(jsonO.code == "-11"){
                        showWarnMsg(jsonO.msg);
                    }else{
                        layerTipMsg(jsonO.success,"复制失败",jsonO.msg);
                    }

                    return;
                }else{
                    layer.closeAll();
                    layerTipMsg(jsonO.success,"复制成功",jsonO.msg);
                }
            },
            error:function(XMLHttpRequest ,textStatus,errorThrown){}

        };
        $.ajax(options);
    }
    function editLink(itemId ,copyIs){
        var gradeCode = $("#gradeUl").find("li[class = 'active']").find("a").attr("val");
        if(gradeCode == "" ||  gradeCode == undefined ){
            showWarnMsg("没有年级信息不能添加项目");
            return ;
        }

        $("#editDiv").load("${request.contextPath}/studevelop/performanceItem/getPerformItemById?itemId=" + itemId +"&copyIs="+copyIs);

        layer.open({
            type: 1,
            offset:"t",
            shade: .5,
            title:'添加评估项目',
            btn :['确定','取消'],
            closeBtn: 0,
            btn1:function(index ,layero){
                saveItem(gradeCode);
                return false;
            },
            btn2:function(index ,layro){
               var flag = true;
                var size = $("input[label='existed']").size();
                var itemId = $("#itemId").val();
                if(itemId != "" && size == 0){
                    showWarnMsg("每个评估项目必须包含至少一个等级");
                    flag =false;
                }
                return flag;

//                layer.closeAll();
            },
            area: '360px',
            content: $('.layer-addScoreItem')
        });
    }
    function saveItem(gradeCode) {
        var check = checkValue("#performForm")
        var flag = true;
        if(!check || !flag){
            return;
        }
        var options={
            url:"${request.contextPath}/studevelop/performanceItem/save",
            dataType:"json",
            type:"post",
            data:{"itemGrade":gradeCode},
            success:function(data){
                var jsonO = data;
                if(!jsonO.success){
                    layerTipMsg(jsonO.success,"保存失败",jsonO.msg);
                    return;
                }else{
                    layer.closeAll();
                    layerTipMsg(jsonO.success,"保存成功",jsonO.msg);
                    $("#itemDiv").load("${request.contextPath}/studevelop/performanceItem/list/page?gradeCode=" + gradeCode);
                }
            },
            clearForm:false,
            resetForm:false,
            error:function(XMLHttpRequest ,textStatus,errorThrown){}

        };
        $("#performForm").ajaxSubmit(options);
    }
    jQuery(document).ready(function(){
        var gradeCode = $("#gradeUl").find("li[class = 'active']").find("a").attr("val");
        gradePerformItem(gradeCode);
    })
    function gradeList() {
        var section = $("#section").val();
        var options = {
            url:"${request.contextPath}/studevelop/performanceItem/gradeList/page?section="+section,
            type:"post",
            dataType:"json",
            success:function (data) {
                if(data.length == 0 ){
                    $("#gradeUl").html("");
                    $("#itemDiv").html("");
                    return;
                }
                var len = data.length;

                var html="";
                for(var i=0;i<data.length;i++){
                    var gradeCode = data[i].gradeCode;
                    var name = data[i].gradeName;
                    html += "<li role=\"presentation\"  ";
                    if(i ==0){
                        html += " class=\"active\"  ";
                    }

                    html += " ><a href=\"javascript:void(0)\" role=\"tab\" val=\"";
                    html += gradeCode +"\" onclick=\"gradePerformItem('" + gradeCode + "');\" data-toggle=\"tab\">" +  name+ "</a>"
                }
                $("#gradeUl").html(html);
            }
        }
        $.ajax(options);

        var gradeCode = $("#gradeUl").find("li[class = 'active']").find("a").attr("val");
        debugger
        if(gradeCode != "" && gradeCode != undefined ){
            var url = "${request.contextPath}/studevelop/performanceItem/list/page";
            $("#itemDiv").load(url,{"gradeCode":gradeCode});
        }


    }
    function gradePerformItem(gradeCode){

        var url = "${request.contextPath}/studevelop/performanceItem/list/page?gradeCode=" + gradeCode;
        $("#itemDiv").load(url);
    }

</script>
</body>
</html>
