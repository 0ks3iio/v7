<div class="box box-default">
    <div class="box-body">
        <div class="filter">
            <div class="filter-item">
                <span class="filter-name">学年：</span>
                <div class="filter-content">
                    <select name="acadyear" id="acadyear" class="form-control" onchange="change2()">
					<#if acadyearList?exists && (acadyearList?size>0)>
	                    <#list acadyearList as item>
		                     <option value="${item!}" <#if semester.acadyear?default('a')==item?default('b')>selected</#if>>${item!}</option>
                        </#list>
                    <#else>
	                    <option value="">未设置</option>
                    </#if>
                    </select>
                </div>
            </div>
            <div class="filter-item">
                <span class="filter-name">学期：</span>
                <div class="filter-content">
                    <select name="semester" id="semester" class="form-control" onchange="change2()">
                    ${mcodeSetting.getMcodeSelect('DM-XQ',(semester.semester?default(0))?string,'0')}
                    </select>
                </div>
            </div>
            <div class="filter-item">
                <span class="filter-name">年级：</span>
                <div class="filter-content">
                    <select name="gradeId" id="gradeId" class="form-control" onchange="gradeChange()">
					<#if gradeList?exists && (gradeList?size>0)>
	                    <#list gradeList as item>
		                     <option value="${item.id!}">${item.gradeName!}</option>
                        </#list>
                    <#else>
	                	<option value="">暂无年级</option>
                    </#if>
                    </select>
                </div>
            </div>
            <div class="filter-item">
                <span class="filter-name">班级：</span>
                <div class="filter-content">
                    <select name="classId" id="classId" class="form-control" onchange="change2()">
					<#if clazzList?exists && (clazzList?size>0)>
	                    <#list clazzList as item>
		                     <option value="${item.id!}">${item.className!}</option>
                        </#list>
                    <#else>
	                	<option value="">暂无班级</option>
                    </#if>
                    </select>
                </div>
            </div>
            <div class="filter-item filter-item-right">
                <button class="btn btn-blue" onclick="doExport()">导出选课名单</button>
                <button class="btn btn-blue js-addCourse" onclick="selectImport()">导入选课</button>
            </div>
        </div>

        <div class="filter">
            <div class="filter-item">
                <div class="input-group input-group-search">
                    <select name="searchType" id="searchType" class="form-control">
                        <option value="2">学号</option>
                        <option value="1">姓名</option>
                    </select>
                    <div class="pos-rel pull-left">
                        <input type="text" class="typeahead scrollable form-control" autocomplete="off" data-provide="typeahead" name="searchTex" id="searchTex">
                    </div>

                    <div class="input-group-btn">
                        <a href="javascript:" type="button" class="btn btn-default" onclick="findByCondition()">
                            <i class="fa fa-search"></i>
                        </a>
                    </div>
                </div>
            </div>
        </div>

        <div id="backButton" class="btn btn-blue" style="display: none;margin-left: 18px">
            返回
        </div>

        <div id="stuSelDetail">

        </div>
    </div>
</div>

<script>
    $(function () {
        change2();
        $("#backButton").on("click",function () {
            $(this).hide();
            $(".filter").show();
            change2();
        });
    });

    function change2(){
        var acadyear = $("#acadyear option:selected").val();
        var semester = $("#semester option:selected").val();
        var classId = $("#classId option:selected").val();
        var url = "${request.contextPath}/basedata/stuselect/detail/index/page?acadyear=" + acadyear + "&semester=" + semester + "&classId=" + classId;
        $("#stuSelDetail").load(url);
    }

    function gradeChange(){
        var gradeIdVal = $("#gradeId option:selected").val();
        $.ajax({
            url:"${request.contextPath}/basedata/courseopen/classByGradeId",
            data:{"gradeId":gradeIdVal},
            dataType: "json",
            success: function(data){
                $("#classId").html("");
                var content = "";
                var jsonArr = data.jsonArr;
                if(jsonArr.length>0){
                    for(var i=0;i<jsonArr.length;i++){
                        var arrData=jsonArr[i];
                        tempContent = '<option value='+ arrData.classId +'>' + arrData.className +'</option>';
                        content += tempContent;
                    }
                }else{
                    tempContent = "<option value=''>暂无班级</option>";
                    content += tempContent;
                }
                $("#classId").html(content);
                change2();
            }
        });
    }

    function doExport(){
        var acadyear = $("#acadyear option:selected").val();
        var semester = $("#semester option:selected").val();
        var classId = $("#classId option:selected").val();
        if (classId.length < 1) {
            layer.msg('未选择班级', {offset: 't',time: 2000});
            return;
        }
        var url = "${request.contextPath}/basedata/stuselect/export?acadyear=" + acadyear + "&semester=" + semester + "&classId=" + classId;
        document.location.href=url;
    }

    function selectImport() {
        $(".filter").hide();
        $("#backButton").show();
        var acadyear = $("#acadyear option:selected").val();
        var semester = $("#semester option:selected").val();
        $("#stuSelDetail").load("${request.contextPath}/basedata/stuselect/import/main?acadyear=" + acadyear + "&semester=" + semester);
    }

    function findByCondition() {
        var flag = $("#searchType option:selected").val();
        var searchContext = $("#searchTex").val();
        if (searchContext == "") {
            $("#selectSubjectList tr").removeAttr("style");
            return;
        }
        $("#selectSubjectList tr").removeAttr("style");
        if (flag == "2") {
            $("#selectSubjectList .studentSelectCode").each(function () {
                if (searchContext != $(this).text()) {
                    $(this).parent().hide();
                }
            });
        } else if (flag == "1") {
            $("#selectSubjectList .studentSelectName").each(function () {
                if ($(this).text().indexOf(searchContext) < 0) {
                    $(this).parent().hide();
                }
            });
        }
    }
</script>