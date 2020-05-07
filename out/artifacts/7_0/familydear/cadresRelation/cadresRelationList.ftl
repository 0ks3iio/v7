<#import "/fw/macro/htmlcomponent.ftl" as htmlcom>
<#assign OBJECT_STATE_RELATIONED = stack.findValue("@net.zdsoft.familydear.common.FamDearConstant@OBJECT_STATE_RELATIONED") />
<form id="mannReForm">

        <table class="table table-bordered table-striped table-hover no-margin">
            <thead>
            <tr>
                <th  width="30" ><label class="pos-rel">
                        <input type="checkbox" class="wp" id="selectAllSubject">
                        <span class="lbl"></span>
                    </label>选择
                </th>
                <th width="100">结亲对象</th>
                <th width="100">结亲村</th>
                <th width="100">类别</th>
                <th width="100" >结亲干部</th>
                <th width="110">手机号</th>
                <th width="150">创建时间</th>
                <th width="80" >结亲标记</th>
                <th width="260" >操作</th>
            </tr>
            </thead>
            <tbody id="list">
            <#if objectList?exists && (objectList?size > 0)>
                <#list objectList as item>
                    <tr>
                        <td>

                            <label class="pos-rel">
                                <input name="objectId" type="checkbox" class="wp"  value="${item.id!}">
                                <span class="lbl"></span>
                            </label>
                        </td>

                        <td>${item.name!}</td>
                        <td>${item.village!}</td>
                        <td> ${item.type!}</td>
                        <td>${item.teacherName!}</td>
                        <td>${item.mobilePhone!}</td>
                        <td>${item.creationTime!}</td>
                        <td>
                            <label class="pos-rel">
                                <input  type="checkbox" class="wp" value="" <#if item.state == OBJECT_STATE_RELATIONED >checked</#if> >
                                <span class="lbl"></span>
                            </label>
                        </td>
                        <td>
                            <a href="javascript:editObject('${item.id!}');" class="table-btn color-red">修改</a>
                            <a href="javascript:objMemberEdit('${item.id!}');" class="table-btn color-red">家庭成员</a>
                            <#if item.state == OBJECT_STATE_RELATIONED >
                                <#if isAdmin?default(false) >
                                <a href="javascript:doReleaseCadre('${item.id!}');" class="table-btn color-red">解除结亲</a>
                                </#if>
                            <#else>
                                <a href="javascript:addCadre('${item.id!}');" class="table-btn color-red">设置结亲</a>
                            </#if>

                            <a href="javascript:historyList('${item.id!}');" class="table-btn color-red">历史结亲</a>
                        </td>
                    </tr>
                </#list>
            <#else >
                <tr>
                    <td colspan="9" align="center">暂无数据</td>
                </tr>
            </#if>
            </tbody>
        </table>
        <#if objectList?exists && (objectList?size > 0)>
            <@htmlcom.pageToolBar container="#showList"/>
        </#if>
</form>
<script>
    function editObject(objId){
        var currentPageIndex = ${currentPageIndex!};
        var currentPageSize = ${currentPageSize!};
        var url = "${request.contextPath}/familydear/cadresRelation/add?objId="+objId+"&_pageIndex=" +currentPageIndex+ "&_pageSize="+currentPageSize;
        indexDiv = layerDivUrl(url,{title: "结亲对象信息",width:750,height:700});
    }
    function addCadre(id){
        var currentPageIndex = ${currentPageIndex!};
        var currentPageSize = ${currentPageSize!};
        var url = "${request.contextPath}/familydear/cadresRelation/cadreAddLink?objId="+id+"&_pageIndex=" +currentPageIndex+ "&_pageSize="+currentPageSize;
        indexDiv = layerDivUrl(url,{title: "设置结亲干部",width:750,height:630});
    }
   function doReleaseCadre(id){
       var currentPageIndex = ${currentPageIndex!};
       var currentPageSize = ${currentPageSize!};
        var url = "${request.contextPath}/familydear/cadresRelation/cadreReleaseLink?objIds="+id+"&_pageIndex=" +currentPageIndex+ "&_pageSize="+currentPageSize;
        indexDiv = layerDivUrl(url,{title: "解除结亲",width:500,height:400});
    }
    function objMemberEdit(id){
        var currentPageIndex = ${currentPageIndex!};
        var currentPageSize = ${currentPageSize!};
        var url = "${request.contextPath}/familydear/cadresRelation/objMemberLink?objId="+id+"&_pageIndex=" +currentPageIndex+ "&_pageSize="+currentPageSize;
        indexDiv = layerDivUrl(url,{title: "家庭成员",width:800,height:630});
    }
    function historyList(id){
        var url = "${request.contextPath}/familydear/cadresRelation/historyCadreLink?objId="+id;
        indexDiv = layerDivUrl(url,{title: "历史结亲",width:750,height:630});
    }




    //全选
    $('#selectAllSubject').on('click',function(){
        var total = $('#list [name="objectId"]:checkbox').length;
        var length = $('#list [name="objectId"]:checkbox:checked').length;
        if(length != total){
            $('#list [name="objectId"]:checkbox').prop("checked", "true");
            $(this).prop("checked", "true");
        }else{
            $('#list [name="objectId"]:checkbox').removeAttr("checked");
            $(this).removeAttr("checked");
        }
    });

    function　searchRelationList(){
        var objName = $('#objName').val();
        var cadreName = $('#cadreName').val();
        var options=$("#villageName option:selected");
        var villageName = "";
        if(options.val()) {
            villageName = options.text();
        }
        // var villageName = $('#villageName').val();
        var label = $('#label').val();
        var type = $('#type').val();
        var teacherId = $('#teacherId').val();
        var currentPageIndex = ${currentPageIndex!};
        var currentPageSize = ${currentPageSize!};
        var tabType = $("#tabType").val();
        var deptId = $("#deptId").val();
        var str = "?tabType="+tabType+"&objName="+objName+"&cadreName="+encodeURIComponent(encodeURIComponent(cadreName))+"&villageName="+encodeURIComponent(encodeURIComponent(villageName))+"&label="+label + "&type="+type +"&teacherId="+teacherId+"&_pageIndex="+currentPageIndex+"&_pageSize="+currentPageSize+"&deptId="+deptId;
        var url = "${request.contextPath}/familydear/cadresRelation/list"+str;

        $("#showList").load(url);
    }

    function loadReleaseDialog(objIds){
        var currentPageIndex = ${currentPageIndex!};
        var currentPageSize = ${currentPageSize!};
        var url = "${request.contextPath}/familydear/cadresRelation/cadreReleaseLink?objIds="+objIds+"&_pageIndex="+currentPageIndex+"&_pageSize="+currentPageSize;
        indexDiv = layerDivUrl(url,{title: "解除结亲",width:500,height:400});
    }

</script>