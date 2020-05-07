<div class="box box-default">

    <div class="box-body">
        <div class="tab-pane active">
            <div class="filter">
                <div class="filter-item">
                    <span class="filter-name">学年：</span>
                    <div class="filter-content">
                        <select class="form-control" id="acadyear" name="acadyear" onChange="changeTime()">
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
                        <select class="form-control" id="semester" name="semester" onChange="changeTime()" style="width: 80px">
                        ${mcodeSetting.getMcodeSelect('DM-XQ',(semester.semester?default(0))?string,'0')}
                        </select>
                    </div>
                </div>
                <div class="filter-item">
                    <span class="filter-name">考试名称：</span>
                    <div class="filter-content">
                        <select class="form-control" id="examId" name="examId" onChange="changeExamId()" style="width:200px;">
                            <option value="">---请选择考试---</option>
                        </select>
                    </div>
                </div>
                <div class="filter-item">
                    <span class="filter-name">所属教育局：</span>
                    <div class="filter-content">
                        <select class="form-control" id="parentSchId" name="parentSchId" onChange="showList()" style="width:200px;">
                            <option value="">---请选择考试---</option>
                        </select>
                    </div>
                </div>
                <a class="btn btn-white" onclick="enrollmentSet()">添加招生学校</a>
            </div>
            <div class="padding-10" id="subjectTabList">
            </div>
        </div>
    </div>
</div>

<script type="text/javascript">

    $(function(){
        changeTime();
        $('[data-toggle="tooltip"]').tooltip({
            container: 'body',
            trigger: 'hover'
        });

    })
    
    function enrollmentSet() {
        var url = "${request.contextPath}/exammanage/edu/enrollment/enrollmentSet";
        indexDiv = layerDivUrl(url,{title: "信息",width:500,height:200});
    }

    function changeTime(){
        var acadyear=$("#acadyear").val();
        var semester=$("#semester").val();
        //根据学年学期拿到考试id
        var examClass=$("#examId");
        $.ajax({
            url:"${request.contextPath}/exammanage/common/examList",
            data:{acadyear:acadyear,semester:semester,unitId:'${unitId!}'},
            dataType: "json",
            success: function(data){
                examClass.html("");
                if(data.length==0){
                    examClass.append("<option value='' >-----请选择考试-----</option>");
                }else{
                    for(var i = 0; i < data.length; i ++){
                        if(i==0){
                            examClass.append("<option value='"+data[i].id+"' selected='selected'>"+data[i].examName+"</option>");
                        }else{
                            examClass.append("<option value='"+data[i].id+"' >"+data[i].examName+"</option>");
                        }
                    }
                }
                changeExamId();
            }
        });
    }

    function changeExamId(){
        var acadyear=$("#acadyear").val();
        var semester=$("#semester").val();
        var examId=$("#examId").val();
        //根据学年学期拿到考试id
        var parentSchId=$("#parentSchId");
        $.ajax({
            url:"${request.contextPath}/exammanage/edu/enrollment/findParentUnit",
            data:{acadyear:acadyear,semester:semester,examId:examId},
            dataType: "json",
            success: function(data){
                parentSchId.html("");
                if(data.length==0){
                    parentSchId.append("<option value='' >-----请选择-----</option>");
                }else{
                    for(var i = 0; i < data.length; i ++){
                        if(i==0){
                            parentSchId.append("<option value='"+data[i].id+"' selected='selected'>"+data[i].unitName+"</option>");
                        }else{
                            parentSchId.append("<option value='"+data[i].id+"' >"+data[i].unitName+"</option>");
                        }
                    }
                }
                showList();
            }
        });
    }
	function showList(){
        var examId=$("#examId").val();
        var acadyear=$("#acadyear").val();
        var semester=$("#semester").val();
        var parentUnitId= $("#parentSchId").val();
        var parmUrl="?acadyear="+acadyear+"&semester="+semester+"&examId="+examId+"&unitId="+'${unitId!}'+"&parentUnitId="+parentUnitId;
        var url="${request.contextPath}/exammanage/edu/enrollment/enrollmentList"+parmUrl;
        $("#subjectTabList").load(url);

    }

</script>