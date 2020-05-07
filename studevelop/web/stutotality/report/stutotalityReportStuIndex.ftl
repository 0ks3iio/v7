<link rel="stylesheet" href="${request.contextPath}/static/css/newcss/iconfont.css" />
<script type="text/javascript">
    function showStu(studentId){
        <#--var url = '${request.contextPath}/stutotality/report/getStuTotalityReport?studentId='+studentId;-->
        var url = '${request.contextPath}/stutotality/report/common/getStuTotalityReportPdf?studentId='+studentId;
        $("#showTabDiv").load(url);
    }
</script>
<#import "/stutotality/result/stutotalityTreeMacro.ftl" as stutotalityTree>
<div class="page-sidebar type-in-tree-box">
    <@stutotalityTree.findStuTree  showStu="showStu" haveExport=false/>
</div>
<form id="searchFormId">
    <#--<input type="hidden" id="gradeId" name="gradeId" value=""/>-->
    <input type="hidden" id="classId" name="classId" value=""/>
    <input type="hidden" id="studentId" name="studentId" value=""/>
</form>

<div class="evaluate-right-content">
    <!---学业水平--->
    <div  id="showTabDiv">

    </div>
</div>
<script src="${request.contextPath}/static/components/jquery-slimscroll/jquery.slimscroll.min.js"></script>
<script src="${request.contextPath}/static/components/datatables/media/js/jquery.dataTables.min.js"></script>
<script src="${request.contextPath}/static/components/datatables/media/js/dataTables.fixedColumns.min.js"></script>
<script src="${request.contextPath}/static/components/datatables/media/js/dataTables.rowsGroup.js"></script>
