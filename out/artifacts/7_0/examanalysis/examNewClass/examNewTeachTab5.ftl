<script src="${request.contextPath}/static/components/chosen/chosen.jquery.min.js"></script>
<div class="box box-default">
    <div class="box-body">
        <div class="filter box-graybg mb10 no-padding-bottom">
            <div class="filter-item">
                <label for="" class="filter-name">学年：</label>
                <div class="filter-content">
                    <select class="form-control" id="acadyear" onChange="changeExam()">
                        <#if acadyearList?exists && acadyearList?size gt 0>
                            <#list acadyearList as item>
                                <option value="${item!}" <#if item==semester.acadyear?default("")>selected="selected"</#if>>${item!}</option>
                            </#list>
                        </#if>
                    </select>
                </div>
            </div>
            <div class="filter-item">
                <label for="" class="filter-name">学期：</label>
                <div class="filter-content">
                    <select class="form-control" id="semester" onChange="changeExam()">
                        ${mcodeSetting.getMcodeSelect('DM-XQ',(semester.semester?default(0))?string,'0')}
                    </select>
                </div>
            </div>
            <div class="filter-item">
                <label for="" class="filter-name">年级：</label>
                <div class="filter-content">
                    <select class="form-control" id="gradeCode" onChange="changeExam()">
                        <#if gradeList?exists && gradeList?size gt 0>
                            <#list gradeList as item>
                                <option value="${item.gradeCode!}" <#if item.gradeCode==gradeCode?default("")>selected="selected"</#if>>${item.gradeName!}</option>
                            </#list>
                        </#if>
                    </select>
                </div>
            </div>
            <div class="filter-item">
                <label for="" class="filter-name">考试名称：</label>
                <div class="filter-content">
                    <select vtype="selectOne" class="form-control" id="examId" onChange="changeSubjectList()">
                        <option value="">---请选择---</option>
                    </select>
                </div>
            </div>
            <div class="filter-item">
                <label for="" class="filter-name">科目：</label>
                <div class="filter-content">
                    <select class="form-control" id="subjectId" onChange="changeClassList()">
                        <option value="">---请选择---</option>
                    </select>
                </div>
            </div>
            <div class="filter-item">
                <label for="" class="filter-name">参照考试：</label>
                <div class="filter-content">
                    <select vtype="selectOne" class="form-control" id="compareExamId" onChange="searchList()">
                        <option value="">---请选择---</option>
                    </select>
                </div>
            </div>
            <#if type?default("")!="1">
                <div class="filter-item ">
                    <div class="btn-group" role="group">
                        <a type="button"  class="btn btn-blue" href="javascript:;" id="tableId" onclick="changeType('1')">报表</a>
                        <a type="button" class="btn btn-white" href="javascript:;" id="echartsId" onclick="changeType('2')">图表</a>
                    </div>
                </div>
            </#if>
            <div class="filter-item filter-item-right">
                <button class="btn btn-blue" id="exportId" onclick="searchList();">查询</button>
                <button class="btn btn-white" id="exportId" onclick="doExport();">导出</button>
            </div>
        </div>
        <input type="hidden" id="type" value="${type!}">
        <input type="hidden" id="reportType" value="${reportType!}">
        <div id="myTeachListDiv">
        </div>
    </div>
</div>
<script>
    $(function(){
        //初始化单选控件
        initChosenOne(".header_filter");
        changeExam();
    });
    function changeExam(){
        var acadyear=$("#acadyear").val();
        var semester=$("#semester").val();
        var gradeCode=$("#gradeCode").val();
        var examClass=$("#examId");
        var compareExamId=$("#compareExamId");
        $.ajax({
            url:"${request.contextPath}/exammanage/common/queryExamList",
            data:{acadyear:acadyear,semester:semester,gradeCode:gradeCode,unitId:'${unitId!}'},
            dataType: "json",
            success: function(data){
                var infolist=data.infolist;
                examClass.html("");
                examClass.chosen("destroy");
                if(infolist.length==0){
                    examClass.append("<option value='' >--请选择--</option>");
                }else{
                    var examHtml='';
                    for(var i = 0; i < infolist.length; i ++){
                        examHtml="<option value='"+infolist[i].id+"' ";
                        examHtml+=" >"+infolist[i].name+"</option>";
                        examClass.append(examHtml);
                    }
                }
                $(examClass).chosen({
                    width:'200px',
                    no_results_text:"未找到",//无搜索结果时显示的文本
                    allow_single_deselect:true,//是否允许取消选择
                    disable_search:false, //是否有搜索框出现
                    search_contains:true,//模糊匹配，false是默认从第一个匹配
                    //max_selected_options:1 //当select为多选时，最多选择个数
                });
                changeSubjectList();
            }
        });
    }
    function changeSubjectList(){
        debugger;
        var examId=$("#examId").val();
        var subjectIdClass=$("#subjectId");
        var subType="0";
        var type=$("#type").val();
        if(type && type=="3"){
            subType="1";
        }

        $.ajax({
            url:"${request.contextPath}/exammanage/common/queryExamSubList",
            data:{examId:examId,unitId:'${unitId!}',subType:subType},
            dataType: "json",
            success: function(data){
                var infolist=data.infolist;
                subjectIdClass.html("");
                if(infolist==null || infolist.length==0){
                    subjectIdClass.append("<option value='' >---请选择---</option>");
                }else{
                    var subjectHtml='';
                    for(var i = 0; i < infolist.length; i ++){
                        subjectHtml="<option value='"+infolist[i].id+"' ";
                        subjectHtml+=" >"+infolist[i].name+"</option>";
                        subjectIdClass.append(subjectHtml);
                    }
                }
                searchCompareExam();
            }
        });
    }

    function searchCompareExam() {
        var acadyear=$("#acadyear").val();
        var semester=$("#semester").val();
        var gradeCode=$("#gradeCode").val();
        var examId=$("#examId").val();
        var subjectId=$("#subjectId").val();
        var compareExamId=$("#compareExamId");
        $.ajax({
            url:"${request.contextPath}/exammanage/common/queryReferExamList",
            data:{acadyear:acadyear,examId:examId,semester:semester,gradeCode:gradeCode,unitId:'${unitId!}',subjectId:subjectId},
            dataType: "json",
            success: function(data){
                debugger;
                var infolist=data.infolist;
                compareExamId.html("");
                compareExamId.chosen("destroy");
                if(infolist==null || infolist.length==0){
                    compareExamId.append("<option value='' >--请选择--</option>");
                }else{
                    var examHtml='';
                    for(var i = 0; i < infolist.length; i ++){
                        examHtml="<option value='"+infolist[i].id+"' ";
                        examHtml+=" >"+infolist[i].name+"</option>";
                        compareExamId.append(examHtml);
                    }
                }
                $(compareExamId).chosen({
                    width:'200px',
                    no_results_text:"未找到",//无搜索结果时显示的文本
                    allow_single_deselect:true,//是否允许取消选择
                    disable_search:false, //是否有搜索框出现
                    search_contains:true,//模糊匹配，false是默认从第一个匹配
                    //max_selected_options:1 //当select为多选时，最多选择个数
                });
                searchList();
            }
        });
    }
    function searchList(){
        var examId=$("#examId").val();
        var acadyear=$("#acadyear").val();
        var semester=$("#semester").val();
        var subjectId=$("#subjectId").val();
        var gradeCode=$("#gradeCode").val();
        var type=$("#type").val();
        var reportType=$("#reportType").val();
        var compareExamId = $("#compareExamId").val();
        if(!reportType){
            reportType="1";
        }
        var c2='?examId='+examId+'&acadyear='+acadyear+'&semester='+semester+'&subjectId='+subjectId+'&gradeCode='
            +gradeCode+"&type="+type+"&reportType="+reportType+"&compareExamId="+compareExamId;
        var url='${request.contextPath}/examanalysis/examNewClass/getExamNewTeachList14/page'+c2;
        $("#myTeachListDiv").load(url);
    }
    function changeType(reportType){
        $('#reportType').attr('value',reportType);
        if(reportType && reportType=='1'){
            if(!$("#tableId").hasClass("btn-blue")){
                $("#tableId").addClass("btn-blue").removeClass("btn-white");
                $("#echartsId").removeClass("btn-blue").addClass("btn-white");
                $("#exportId").show();
            }
        }else if(reportType && reportType=='2'){
            if(!$("#echartsId").hasClass("btn-blue")){
                $("#echartsId").addClass("btn-blue").removeClass("btn-white");
                $("#tableId").addClass("btn-white").removeClass("btn-blue");
                $("#exportId").hide();
            }
        }
        searchList();
    }
</script>