<link rel="stylesheet" href="${request.contextPath}/static/css/newcss/iconfont.css" />
<script type="text/javascript">

    function showClass(classId){
        var type=$("#type").val();
        if(!classId){
            return;
        }
        if(!type){
            type="0";
            $("#type").val(type);
        }
        var resultType=$("#resultType").val();
        var itemId=$("#itemId").val();
        var optionId=$("#optionId").val();
        var itemExpand=$("#itemExpand").val();
        var url = '${request.contextPath}/stutotality/result/scoreTab/page?type='+type+"&classId="+classId+"&resultType="+resultType+"&itemId="+itemId+"&optionId="+optionId+"&itemExpand="+itemExpand;
        $("#showTabDiv").load(url);
    }
    function showStu(studentId){
        var type=$("#type").val();
        if(!type){
            type="0";
            $("#type").val(type);
        }
        var resultType=$("#resultType").val();
        var classId=$("#classId").val();
        var itemId=$("#itemId").val();
        var optionId=$("#optionId").val();
        var itemExpand=$("#itemExpand").val();
        var url = '${request.contextPath}/stutotality/result/scoreTab/page?type='+type+"&classId="+classId+"&studentId="+studentId+"&resultType="+resultType+"&itemId="+itemId+"&optionId="+optionId+"&itemExpand="+itemExpand;
        $("#showTabDiv").load(url);
    }
    function checkOneStu(studentId){
        $.ajax({
            url: "${request.contextPath}/stutotality/result/checkStuHaveOver",
            data: {'studentId': studentId},
            type: 'post',
            dataType: 'json',
            success: function (data) {
                var array = data.array;
                if (array && array.length > 0) {
                    for (var i = 0; i < array.length; i++) {
                        if(array[i].haveOver && array[i].haveOver==1){
                            $("#check"+array[i].studentId).html('<i class="typein-success-tip icon-select wpfont"></i>');
                        }
                    }
                }
                var classArray = data.classArray;
                if (classArray && classArray.length > 0) {
                    var html="";
                    for (var i = 0; i < classArray.length; i++) {
                        html="";
                        html='<i class="typein-no-tip">'+classArray[i].number+'人未评</i>';
                        $("#checkAll"+classArray[i].classId).html(html);
                    }
                }
            },
            error: function (XMLHttpRequest, textStatus, errorThrown) {}
        });
    }
    function checkStu(classId){
        $.ajax({
            url: "${request.contextPath}/stutotality/result/checkStuHaveOver",
            data: {'classId': classId},
            type: 'post',
            dataType: 'json',
            success: function (data) {
                var array = data.array;
                if (array && array.length > 0) {
                    for (var i = 0; i < array.length; i++) {
                        if(array[i].haveOver && array[i].haveOver==1){
                            $("#check"+array[i].studentId).html('<i class="typein-success-tip icon-select wpfont"></i>');
                        }
                    }
                }
                var classArray = data.classArray;
                if (classArray && classArray.length > 0) {
                    var html="";
                    for (var i = 0; i < classArray.length; i++) {
                        html="";
                        html='<i class="typein-no-tip">'+classArray[i].number+'人未评</i>';
                        $("#checkAll"+classArray[i].classId).html(html);
                    }
                }
            },
            error: function (XMLHttpRequest, textStatus, errorThrown) {}
        });
    }
    function checkGradeStu(gradeId){
        $.ajax({
            url: "${request.contextPath}/stutotality/result/checkStuHaveOver",
            data: {'gradeId': gradeId},
            type: 'post',
            dataType: 'json',
            success: function (data) {
                var array = data.classArray;
                if (array && array.length > 0) {
                    var html="";
                    for (var i = 0; i < array.length; i++) {
                        html="";
                        html='<i class="typein-no-tip">'+array[i].number+'人未评</i>';
                        $("#checkAll"+array[i].classId).html(html);
                    }
                }
            },
            error: function (XMLHttpRequest, textStatus, errorThrown) {}
        });
    }
</script>
<#import "/stutotality/result/stutotalityTreeMacro.ftl" as stutotalityTree>
<div class="page-sidebar type-in-tree-box">
    <#--<div class="evaluate-type-edit">
        <button class="btn btn-white evaluate-100" id="type-edit" onclick="importData()">
            批量导入
        </button>
    </div>-->
    <@stutotalityTree.findStuTree  showStu="showStu" haveExport=false showClass="showClass" haveTeaching="1" showNumStr="can"/><#--showNumStr="${unSignNum!}人未评"-->
</div>
<form id="searchFormId">
    <#--<input type="hidden" id="gradeId" name="gradeId" value=""/>-->
    <input type="hidden" id="classId" name="classId" value=""/>
    <input type="hidden" id="studentId" name="studentId" value=""/>
    <input type="hidden" id="type" name="type" value=""/>
    <input type="hidden" id="resultType" name="resultType" />
    <input type="hidden" id="itemId" name="itemId" />
    <input type="hidden" id="optionId" name="optionId"/>
    <input type="hidden" id="itemExpand" value="0"/><#--项目的展开与否 1展开状态  0不展开状态-->
</form>

<div class="evaluate-right-content" style="padding-top: 0;">
    <div class="evaluate-nav-box">
        <ul class="nav nav-tabs nav-tabs-1">
            <li class="active">
                <a href="#aa" data-toggle="tab" id="type0" onclick="showTab('0');">综合素质</a>
            </li>
            <li>
                <a href="#bb" data-toggle="tab" id="type1" onclick="showTab('1');">学业水平</a>
            </li>
        </ul>
    </div>
    <!---学业水平--->
    <div  id="showTabDiv">
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
    $(function () {

    })
    function showTab(type) {
        $("#type").val(type);
        if(type=="0"){
            $("#resultType").val("");
        }
        var studentId=$("#studentId").val();
        if(studentId && studentId!=""){
            showStu(studentId);
        }else{
            var classId=$("#classId").val();
            showClass(classId);
        }
    }

    /*function setUnsignNum() {
        var num=0;
        $(".chosen-tree.chosen-tree-tier3").find("i").each(function () {
            num = num+1;
        })
        var totalNum=0
        $(".chosen-tree.chosen-tree-tier3").find("div").each(function () {
            totalNum = totalNum+1;
        })
        $("#unSignNum").html(totalNum-num+"人未评");
    }*/
    function importData() {
        var classId=$("#classId").val();
        if(classId == '' || classId == undefined){
            if(searchType!="0") {
                layerTipMsg(false, "请选择一个班级!", "");
                return;
            }
        }
        var url = '${request.contextPath}/stutotality/result/main?importType='+1+"&classId="+classId;
        $("#showTabDiv").load(url);
    }

</script>