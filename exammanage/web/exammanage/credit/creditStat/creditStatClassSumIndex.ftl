
<div class="box box-default">
    <input type="hidden" id="classId" value="${classId}">
    <div class="box-body">
        <#-- button class="btn btn-info" onclick="exportStudent('${classId}')">导出Excel</button -->
        <h3 class="text-center">学科课程学习学分登记表</h3>
        <div class="filter">
            <div class="filter-item">
                <span class="filter-name">科目：</span>
                <div class="filter-content">
                    <select name="" id="subjectId" class="form-control" onchange="loadClassSumList()">
                        <option value="">请选择</option>
                        <#if courses?? && courses?size gt 0>
                            <#list courses as item>
                                <option value="${item.id}">${item.subjectName!}</option>
                            </#list>
                        </#if>
                    </select>
                </div>
            </div>
            <div class="filter-item">
              <span class="filter-name"
              ><span>班级：</span><span>${className!}</span></span
              >
            </div>
            <#--<div class="filter-item">-->
              <#--<span class="filter-name"-->
              <#--><span>补修时间：</span><span>2016-10-10</span></span-->
              <#-->-->
            <#--</div>-->
        </div>
        <div class="table-container" id="mySumList">

        </div>
    </div>
</div>
<script>
    $(function () {
        <#if !isAdmin>
        <#if isTeacher>
            changebtn();
        <#else >
            changebtn1();
        </#if>
        </#if>
        loadClassSumList();
        <#--var year = $("#searchAcadyear").val();-->
        <#--var semester = $("#searchSemester").val();-->
        <#--var url =  '${request.contextPath}/exammanage/credit/creditStatClassSumList?class='+id+"&year="+year+"&semester="+semester;-->
        <#--$("#mySumList").load(url);-->
    });

    function loadClassSumList(){
        var classId =$("#classId").val();
        var year = $("#searchAcadyear").val();
        var semester = $("#searchSemester").val();
        var subjectId = $("#subjectId").val();
        var url =  '${request.contextPath}/exammanage/credit/creditStatClassSumList?classId='+classId+"&subjectId="+subjectId+"&year="+year+"&semester="+semester;
        $("#mySumList").load(url);
    }

    function exportStudent(classId) {
        var year = $("#searchAcadyear").val();
        var semester = $("#searchSemester").val();
        var subjectId = $("#subjectId").val();
        var url =  '${request.contextPath}/exammanage/credit/exportPatchStudent?year='+year+"&semester="+semester+"&classId="+classId+"&subjectId="+subjectId;
        document.location.href=url;
    }
    </script>