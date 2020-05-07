<#import "/fw/macro/htmlcomponent.ftl" as htmlcom>
<#--<link rel="stylesheet" type="text/css" href="${request.contextPath}/static/fw/css/default.css"/>-->
<#--<link rel="stylesheet" href="${request.contextPath!}/static/components/datatables/media/css/jquery.dataTables.min.css"/>-->
<script src="${request.contextPath}/static/components/jquery-slimscroll/jquery.slimscroll.min.js"></script>
<script src="${request.contextPath}/static/components/datatables/media/js/jquery.dataTables.min.js"></script>
<script src="${request.contextPath}/static/components/datatables/media/js/dataTables.fixedColumns.min.js"></script>
<script src="${request.contextPath}/static/components/datatables/media/js/dataTables.rowsGroup.js"></script>
<#--<link rel="stylesheet" href="${request.contextPath}/static/css/newcss/iconfont.css" />-->
<div class="page-content" id="mydiv" style="padding: 0px">

    <div class="row">
        <div class="col-xs-12">
            <div class="box box-default">
                <div class="box-body clearfix">
                    <div >
                        <div class="filter filter-f16">
                            <div class="filter-item">
                                <span class="filter-name">学年：</span>
                                <div class="filter-content">
                                    <select class="form-control" id="searchAcadyear" name="searchAcadyear" onChange="showList1()">
                                        <#if acadyearList?exists && (acadyearList?size>0)>
                                            <#list acadyearList as item>
                                                 <option value="${item!}"<#if selectYear?default('a')==item?default('b')>selected <#elseif  semester.acadyear?default('a')==item?default('b')>selected</#if>>${item!}</option>
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
                                    <select class="form-control" id="searchSemester" name="searchSemester" onChange="showList1()">
                                    ${mcodeSetting.getMcodeSelect('DM-XQ',(semester.semester?default(0))?string,'0')}
                                    </select>
                                </div>
                            </div>
                        </div>
                        <div class="table-container">
                            <div class="table-container-body" id="showList1">
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
</div>
<script>
    $(function () {
        showList1()
    })
    function showList1(){
        var year = $("#searchAcadyear").val();
        var semester = $("#searchSemester").val();
        var url =  '${request.contextPath}/exammanage/credit/creditStatStuList?year='+year+"&semester="+semester;
        $("#showList1").load(url);
    }
</script>