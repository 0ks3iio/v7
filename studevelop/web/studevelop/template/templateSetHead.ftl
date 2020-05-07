<div class="row">
    <div class="col-xs-12">
        <div class="box-body">
            <div class="filter clearfix">
                <div class="filter-item">
                    <label for="" class="filter-name">学年：</label>
                    <div class="filter-content">
                        <select class="form-control" id="queryAcadyear" onChange="changeSchoolNotice();" style="width:168px;">
                        <#if (acadyearList?size>0)>
                            <#list acadyearList as item>
                                <option value="${item!}" <#if acadyear?default('a')==item?default('b')>selected</#if>>${item!}</option>
                            </#list>
                        </#if>
                        </select>
                    </div>
                </div>
                <div class="filter-item">
                    <label for="" class="filter-name">学期：</label>
                    <div class="filter-content">
                        <select class="form-control" id="querySemester" onChange="changeSchoolNotice();" style="width:168px;">
                        ${mcodeSetting.getMcodeSelect('DM-XQ',(semester?default(0))?string,'0')}
                        </select>
                    </div>
                </div>
                <div class="filter-item">
                    <label for="" class="filter-name">学段：</label>
                    <div class="filter-content">
                        <select vtype="selectOne" class="form-control" id="section" onChange="changeSchoolNotice();" style="width:168px;">
                        <#list sectionMap?keys as key>
                            <option value="${key!}">${sectionMap[key]}</option>
                        </#list>
                        </select>
                    </div>
                </div>

            </div>
            <div class="table-wrapper" id="schoolNoticeDiv">
            </div>
        </div>
    </div>
</div>
<script>
    $(function(){
        changeSchoolNotice();
    });


    function changeSchoolNotice(){
        var acadyear = $("#queryAcadyear").val();
        var semester = $("#querySemester").val();
        var section =  $("#section").val();
        var url = "${request.contextPath}/studevelop/templateSet/schoolNotice/edit.action?acadyear="+acadyear+"&semester="+semester +"&section="+section;
        $("#schoolNoticeDiv").load(url);
    }



</script>
