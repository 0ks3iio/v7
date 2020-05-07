<link rel="stylesheet" href="${request.contextPath}/static/css/newcss/iconfont.css" />
<script type="text/javascript">

    function showStu(studentId){
        var url = '${request.contextPath}/stutotality/result/getStuPhysicalQualityList?studentId='+studentId;
        $("#showTabDiv").load(url);
    }
</script>
<#import "/stutotality/result/stutotalityTreeMacro.ftl" as stutotalityTree>
<div class="page-sidebar type-in-tree-box">
    <div class="evaluate-type-edit">
        <button class="btn btn-white evaluate-100" id="type-edit" onclick="doImport()">
            批量导入
        </button>
    </div>
    <@stutotalityTree.findStuTree  showStu="showStu" />
</div>
<form id="searchFormId">
    <input type="hidden" id="classId" name="classId" value=""/>
    <input type="hidden" id="studentId" name="studentId" value=""/>
</form>

<div class="evaluate-right-content" style="padding-top: 10px;">
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
<script src="${request.contextPath}/static/components/jquery-slimscroll/jquery.slimscroll.min.js"></script>
<script src="${request.contextPath}/static/components/datatables/media/js/jquery.dataTables.min.js"></script>
<script src="${request.contextPath}/static/components/datatables/media/js/dataTables.fixedColumns.min.js"></script>
<script src="${request.contextPath}/static/components/datatables/media/js/dataTables.rowsGroup.js"></script>

<script type="text/javascript">
    var container = $("#showTabDiv").html();
    //导入
    function doImport() {
        var classId = $("#classId").val();
        if(classId == '' || classId == undefined){
            layerTipMsg(false, "请选择一个班级!", "");
            return;
        }
        var url = '${request.contextPath}/stutotality/result/main?importType='+0+"&classId="+classId;
        $("#showTabDiv").load(url);
    }

</script>