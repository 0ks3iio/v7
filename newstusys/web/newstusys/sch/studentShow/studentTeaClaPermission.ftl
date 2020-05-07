<#import "/fw/macro/popupMacro.ftl" as popup />

<div class="box box-default">
    <div class="box-body" id="studentContent">
        <div class="filter" id="searchDiv">
            <div class="filter-item">
                <span class="filter-name">年级：</span>
                <div class="filter-content">
                    <select name="" id="gradeIdSearch" class="form-control"  onChange="showPermList()" style="width:168px;" >
                    <#if gradeList?exists && (gradeList?size>0)>
                        <option value="">---请选择---</option>
                        <#list gradeList as item>
                            <option value="${item.id!}" <#if item.id == gradeId?default('') >selected</#if> >${item.gradeName!}</option>
                        </#list>
                    <#else>
                        <option value="">---请选择---</option>
                    </#if>
                    </select>
                </div>
            </div>

            <div class="filter-item filter-item-right">
                <div class="filter-content">
                    <a class="btn btn-blue" href="javascript:void(0);" onclick="editALL()">批量设置</a>

                </div>
            </div>

        </div>

    </div>
</div>

<#--<div class="table-container-header text-right">-->
    <#--<a class="btn btn-blue" href="javascript:void(0);" onclick="editALL()">批量设置</a>-->
<#--</div>-->
<div class="table-container-body">
    <input type="hidden" id="classIdVal" value="">
    <table class="table table-striped layout-fixed">
        <thead>
        <tr>
            <th width="5%" ><label><input type="checkbox" class="wp" id="checkAll"><span class="lbl"> 全选</span></label></th>
            <th width="10%">教学班</th>
            <th width="10%">课程名称</th>
            <th width="10%">任课老师</th>
            <th width="10%">所属年级</th>
            <th width="55%">管理人员</th>
            <input type="hidden" value="${resourceUrl}" />
        </tr>
        </thead>
        <tbody>
        <#if returnDtos?exists&&returnDtos?size gt 0>
            <#list returnDtos as item>
            <tr>
                <td>
                    <label><input type="checkbox" class="wp checked-input" value="${item.classId!}"><span class="lbl"></span></label>
                </td>
                <td>${item.className!}</td>
                <td>${item.courseName!}</td>
                <td>${item.teacherName!}</td>
                <td>${item.gradeName!}</td>
                <td>
                    <input type="hidden" id="userIds${item_index+1}" value="${item.permisionUserIds!}">
                    <input type="text" id="userName${item_index+1}" class="form-control" value="${item.permisionUserNames!}"  onclick="editTeaId('${item.classId!}','${item_index+1}')">
                </td>
            </tr>
            </#list>
        <#else>
        <tr>
            <td  colspan="6" align="center">
                暂无数据
            </td>
        <tr>
        </#if>
        </tbody>
    </table>
</div>
<div style="display: none;">
<@popup.selectMoreTeacherUser clickId="userName" id="userIds" name="userName" handler="savePermission()">
    <input type="hidden" id="userIds" value="">
    <input type="text" id="userName" class="form-control" value="">
</@popup.selectMoreTeacherUser>
</div>
<script type="text/javascript">
    var index = "";
    var isAll = false;
    $(function(){
        $("#checkAll").click(function(){
            var ischecked = false;
            if($(this).is(':checked')){
                ischecked = true;
            }
            $(".checked-input").each(function(){
                if(ischecked){
                    $(this).prop('checked',true);
                }else{
                    $(this).prop('checked',false);
                }
            });
        });
    })
    function editTeaId(classId,number){
        index=number;
        isAll = false;
        var userIds=$("#userIds"+number).val();
        var userName=$("#userName"+number).val();
        $('#userName').val(userName);
        $('#userIds').val(userIds);
        $('#classIdVal').val(classId);
        $('#userName').click();
    }
    function editALL(){
        var ids = "";
        $(".checked-input").each(function(){
            if($(this).is(':checked')){
                if(ids==''){
                    ids = $(this).val();
                }else{
                    ids+=','+$(this).val();
                }
            }
        });
        if(ids==""){
            layerTipMsg(false,"","请选择批量设置的班级");
            return;
        }
        isAll = true;
        $("#classIdVal").val(ids);
        $('#userName').val("");
        $('#userIds').val("");
        $('#userName').click();
    }
    isSubmit = false;
    function savePermission(){
        if(isSubmit){
            return;
        }
        var classIds = $("#classIdVal").val();
        var userIds = $("#userIds").val();
        var userName = $("#userName").val();
        isSubmit = true;
        var options = {
            url : "${request.contextPath}/stuwork/permission/save",
            data:{classIds:classIds,userIds:userIds,isAll:isAll,classsType:"${classType!}",permissionType:"${permissionType!}"},
            dataType : 'json',
            success : function(data){
                if(!data.success){
                    layerTipMsg(data.success,"保存失败",data.msg);
                }else{
                    if(isAll){
                        showPermList();
                    }else{
                        $("#userIds"+index).val(userIds);
                        $("#userName"+index).val(userName);
                    }
                    layerTipMsg(data.success,data.msg,"");
                }
                isSubmit = false;
            },
            type : 'post',
            error:function(XMLHttpRequest, textStatus, errorThrown){}//请求出错
        };
        $.ajax(options);
    }
    function showPermList(){
        var gradeId = $("#gradeIdSearch").val();
        var   url =  '${request.contextPath}/stuwork/permission/list.action?classType=${classType!}&permissionType=${permissionType!}'+"&gradeId="+gradeId;
        $("#showPermissionList").load(url);
    }

</script>