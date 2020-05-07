<#import "../macro/pagination.ftl" as pagination />
<#if pages??>
    <#if pages.content?? && pages.content?size gt 0>
        <table class="table table-bordered table-striped table-hover no-margin">
            <thead>
            <tr>
                <th>序号</th>
                <th>任务执行人 </th>
                <th>任务类型</th>
                <th>任务内容</th>
                <th>创建日期</th>
                <th>完成日期</th>
                <th>任务状态</th>
                <th>创建人</th>
                <th>操作</th>
            </tr>
            </thead>
            <tbody id="taskList">
            <#list pages.content as taskList>
                <tr data=${taskList.id}>
                    <td>${taskList_index+1} </td>
                    <td>${taskList.ownerUserName!}
                    <#if taskList.isEmail==0>
                        <i class="fa fa-envelope-o color-orange"></i>
                    </#if>
                    </td>
                       <#if taskList.taskType==0>
                           <td>排课</td>
                       <#elseif taskList.taskType==1>
                           <td>分班</td>
                       <#elseif taskList.taskType==2>
                           <td>其他</td>
                       </#if>
                    <td>${taskList.content!}</td>
                    <td>${taskList.creationTime?string("yyyy-MM-dd")!}</td>
                    <td>${taskList.completionTime?string("yyyy-MM-dd")!}</td>
                    <#if taskList.state==0>
                        <td><i class="fa fa-circle color-green font-12"></i>进行中</td>
                    <#elseif taskList.state==1>
                        <td><i class="fa fa-circle color-grey font-12"></i>已完成</td>
                    <#elseif taskList.state==2>
                        <td><i class="fa fa-circle color-red font-12"></i>已超时</td>
                    </#if>
                    <td>${taskList.realName!}</td>
                    <td>
                        <a class="color-blue table-btn task-edit      <#if taskList.state==1||taskList.state==2>disabled</#if>" href="#" id="editHref">编辑</a>
                        <a class="color-blue table-btn task-complete  <#if taskList.state==1||taskList.state==2>disabled</#if>" href="#" id="completeHref">执行完成</a>
                        <a class="color-blue table-btn task-extension <#if taskList.state==1>disabled</#if>" href="#" id="delayHref">延期</a>
                        <a class="color-blue table-btn task-delete" href="#">删除</a>
                    </td>
                </tr>
            </#list>
            </tbody>
        </table>
        <@pagination.paginataion pages=pages containerId='taskList' pageCallFunction='doGetRecordAccounts' />
    <#else >
        <div class="no-data-container">
            <div class="no-data">
            <span class="no-data-img">
                <img src="${springMacroRequestContext.contextPath}/static/images/public/nodata4.png" alt="">
            </span>
                <div class="no-data-body">
                    <p class="no-data-txt">没有相关数据</p>
                </div>
            </div>
        </div>
    </#if>
</#if>

<!-- 编辑 -->
<div class="layer layer-edit">
    <div class="layer-content">
        <div class="form-horizontal" role="form">
            <div class="form-group">
                <label class="col-sm-2 control-label no-padding-right">任务类型：</label>
                <div class="col-sm-4">
                    <select id="editTaskType"class="form-control" >
                        <#if taskType??>
                            <#list taskType as type>
                                <option value="${type.taskTypeCode}">${type.taskTypeName}</option>
                            </#list>
                        </#if>
                    </select>
                </div>
                <label class="col-sm-2 control-label no-padding-right">任务执行人：</label>
                <div class="col-sm-4 mt7">
                    <input id="editOwnerUserName" type="text" disabled/>
                </div>
            </div>
            <div class="form-group">
                <label class="col-sm-2 control-label no-padding-right">完成时间：</label>
                <div class="col-sm-4 mt7">
                    <div class="input-group">
                        <input id="editCompletionTime" class="form-control datetimepicker" type="text" disabled>
                    </div>
                </div>
                <div class="col-sm-4">

                </div>
            </div>
            <div class="form-group">
                <label class="col-sm-2 control-label no-padding-right">任务内容：</label>
                <div class="col-sm-10">
                    <textarea cols="30" id="editContent"rows="5" class="form-control"></textarea>
                </div>
            </div>
        </div>
    </div>
</div>

<script>
    $(function(){
        //删除

        $('.task-delete').on('click', function(){
            var obj=$(this).parents('tr');
            serviceTaskId=obj.attr("data");
            layer.open({
                type: 1,
                shadow: 0.5,
                title:false,
                area: '350px',
                btn: ['确定', '取消'],
                content: $('.layer-delete'),
                yes:function (index) {
                    $.ajax({
                        type:'post',
                        url: "${springMacroRequestContext.contextPath}/operation/opUserLog/deleteById/"+serviceTaskId,
                        success:function (res) {
                            doGetRecordAccounts('');
                        },
                        error: function(){
                            spop({
                                template: '删除失败',
                                position  : 'top-center',
                                autoclose: 3000,
                                style: 'error'
                            });
                        }
                    })
                    layer.close(index);
                }
            });
        });

        //编辑窗口
        $('.task-edit').on('click', function(e){
            if($(e.target).hasClass("disabled")){
                $(e.target).css("text-decoration","none");
                return false;
            }else {
                var obj = $(this).parents('tr');
                serviceTaskId = obj.attr("data");
                $.ajax({
                    type: 'get',
                    url: "${springMacroRequestContext.contextPath}/operation/opUserLog/getOneServiceTask?id=" + serviceTaskId,
                    success: function (res) {
                        let oneServiceTask = res.serviceTask;
                        var time = timestampToTime(oneServiceTask.completionTime);
                        $('#editCompletionTime').val(time);
                        // var optionString= "option:contains("+oneServiceTask.ownerUserName+")";
                        // $("#editOwnerUserName").find(optionString).prop("selected",true);
                        $('#editContent').val(oneServiceTask.content);
                        $('#editTaskType').val(oneServiceTask.taskType);
                        $('#editOwnerUserName').val(oneServiceTask.ownerUserName);

                        // if (oneServiceTask.isEmail == 0) {
                        //     $('#isEmail').prop("checked", "checked");
                        //     $('#noEmail').prop("checked","");
                        // } else if (oneServiceTask.isEmail == 1) {
                        //     $('#noEmail').prop("checked", "checked");
                        //     $('#isEmail').prop("checked","");
                        // }
                        layer.open({
                            type: 1,
                            shadow: 0.5,
                            title: '编辑',
                            area: '620px',
                            btn: ['确定', '取消'],
                            content: $('.layer-edit'),
                            yes: function (index) {
                                var serviceTaskUpdateDto = getServiceTaskDto();
                                $.ajax({
                                    type: 'post',
                                    contentType: "application/json",
                                    url: "${springMacroRequestContext.contextPath}/operation/opUserLog/editTask",
                                    data: serviceTaskUpdateDto,
                                    success: function () {
                                        doGetRecordAccounts('');
                                        layer.close(index);
                                    }, error: function () {
                                        opLayer.error("编辑任务保存失败","提示");
                                    }
                                })

                            }
                        });
                    },
                    error: function () {
                        spop({
                            template: '任务加载失败',
                            position  : 'top-center',
                            autoclose: 3000,
                            style: 'error'
                        });
                    }
                })
            }
        });


        //执行完成
        $('.task-complete').on('click', function(e){
            if($(e.target).hasClass("disabled")){
                $(e.target).css("text-decoration","none");
                return false;
            } else {
                var obj = $(this).parents('tr');
                serviceTaskId = obj.attr("data");
                layer.open({
                    type: 1,
                    shadow: 0.5,
                    title: false,
                    area: '350px',
                    btn: ['确定', '取消'],
                    content: $('.layer-complete'),
                    yes: function (index) {
                        $.ajax({
                            type: 'post',
                            contentType: "application/json",
                            url: "${springMacroRequestContext.contextPath}/operation/opUserLog/completeTask?taskId=" + serviceTaskId,
                            success: function (res) {
                                doGetRecordAccounts('');
                            },
                            error: function () {
                                spop({
                                    template: '执行完成操作失败',
                                    position  : 'top-center',
                                    autoclose: 3000,
                                    style: 'error'
                                });
                            }
                        })
                        layer.close(index);
                    }
                });
            }
        });
        $('.task-extension').on('click', function(e){
            if($(e.target).hasClass("disabled")){
                $(e.target).css("text-decoration","none");
                return false;
            }else {
                var obj = $(this).parents('tr');
                serviceTaskId = obj.attr("data");
                layer.open({
                    type: 1,
                    shadow: 0.5,
                    title: false,
                    area: '350px',
                    btn: ['确定', '取消'],
                    content: $('.layer-extension'),
                    yes: function (index) {
                        $.ajax({
                            type: 'post',
                            contentType: "application/json",
                            url: "${springMacroRequestContext.contextPath}/operation/opUserLog/serviceTaskExtension/" + serviceTaskId,
                            success: function (res) {
                                doGetRecordAccounts('');
                            },
                            error: function () {
                                spop({
                                    template: '任务延期操作失败',
                                    position  : 'top-center',
                                    autoclose: 3000,
                                    style: 'error'
                                });
                            }
                        })
                        layer.close(index);
                    }
                });
            }
        });



    }


    )
    function getServiceTaskDto(originalContent,originalTaskType) {
        var serviceTaskUpdateDto;
        var taskType = $('#editTaskType').val();
        var content = $('#editContent').val();
        var mustEmail='';
        if(originalContent!=content||originalTaskType!=taskType){
            mustEmail=0;
        }else {
            mustEmail=1;
        }
        if (content != '') {
            serviceTaskUpdateDto = JSON.stringify({
                "taskType": taskType,
                "taskContent": content,
                "isEmail": mustEmail, "id": serviceTaskId
            });

            return serviceTaskUpdateDto
        }
    }

    function timestampToTime(timestamp) {
        if(timestamp==""||typeof (timestamp)=='undefined'||timestamp==null){
            return "";
        }else {
            var date = new Date(timestamp);//时间戳为10位需*1000，时间戳为13位的话不需乘1000
            Y = date.getFullYear() + '-';
            M = (date.getMonth()+1 < 10 ? '0'+(date.getMonth()+1) : date.getMonth()+1) + '-';
            D = date.getDate()<10?'0'+date.getDate():date.getDate();
            return Y+M+D;
        }
    }
</script>