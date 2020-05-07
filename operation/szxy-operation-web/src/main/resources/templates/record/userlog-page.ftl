<#import "../macro/staticImport.ftl" as s />
<@s.datepicker/>
<div class="page-content-inner">
<div class="box box-default">
    <div class="box-header">
        <h3 class="box-caption">服务任务</h3>
    </div>
    <div class="box-body">
        <div class="filter">
            <div class="filter-item">
                <button type="button" class="btn btn-blue js-add">新增</button>
                <button type="button" class="btn btn-default js-export">导出</button>
            </div>
            <div class="filter-item filter-item-right">
                <div class="filter-content">
                    <div class="input-group input-group-search">
                        <div class="pull-left">
                            <input type="text" id="ownerUserName" onchange="doGetRecordAccounts('');" class="form-control">
                        </div>
                        <div class="input-group-btn">
                            <button type="button" class="btn btn-default">
                                <i class="fa fa-search"></i>
                            </button>
                        </div>
                    </div>
                </div>
            </div>
            <div class="filter-item filter-item-right">
                <span class="filter-name">任务状态：</span>
                <div class="filter-content">
                    <select name="" id="state" class="form-control" onchange="doGetRecordAccounts('');">
                        <option value="">全部</option>
                        <#list taskState as type>
                            <option value="${type.taskStateCode}">${type.taskStateName}</option>
                        </#list>
                    </select>
                </div>
            </div>
            <div class="filter-item filter-item-right">
                <span class="filter-name">任务类型：</span>
                <div class="filter-content">
                    <select name="" id="taskType" class="form-control" onchange="doGetRecordAccounts('');">
                        <option value="">全部</option>
                        <#list taskType as type>
                            <option value="${type.taskTypeCode}">${type.taskTypeName}</option>
                        </#list>
                    </select>
                </div>
            </div>
        </div>
        <div id="taskList">

        </div>
    </div>
</div>
</div>


<#--新增-->
<div class="layer layer-add">
    <div class="layer-content">
        <div class="form-horizontal" role="form">
            <div class="form-group">
                <label class="col-sm-2 control-label no-padding-right">任务类型：</label>
                <div class="col-sm-4">
                    <select class="form-control" id="saveTaskType">
                        <#list taskType as type>
                            <option value="${type.taskTypeCode}">${type.taskTypeName}</option>
                        </#list>
                    </select>
                </div>
                <label class="col-sm-2 control-label no-padding-right">任务执行人：</label>
                <div class="col-sm-4">
                    <select class="form-control" id="ownerUserId" onchange="stopSendEmail();">
                        <#list userList as list>
                            <option value="${list.id}">${list.realName}</option>
                        </#list>
                    </select>
                </div>
            </div>
            <div class="form-group">
                <label class="col-sm-2 control-label no-padding-right">完成时间：</label>
                <div class="col-sm-4 mt7">
                    <div class="input-group">
                        <input id="completionTime" class="form-control datetimepicker" type="text">
                    </div>
                </div>
                <label class="col-sm-2 control-label no-padding-right">邮件告知：</label>
                <div class="col-sm-4">
                    <label><input type="radio" id="insertIsEmail" name="isEmail" class="wp" value=0 ><span class="lbl">是</span></label>
                    <label><input type="radio" id="insertNoEmail" name="isEmail" class="wp" value=1 ><span class="lbl"> 否</span></label>
                </div>
            </div>
            <div class="form-group">
                <label class="col-sm-2 control-label no-padding-right">任务内容：</label>
                <div class="col-sm-10">
                    <textarea cols="30" id="content" rows="5" class="form-control" maxlength="256"></textarea>
                </div>
            </div>
        </div>
    </div>
</div>

<!-- 导出 -->
<div class="layer layer-export">
    <div class="layer-content">
        <div class="form-horizontal" role="form">
            <div class="form-group">
                <label class="col-sm-3 control-label no-padding-right">任务创建时间：</label>
                <div class="col-sm-8">
                    <div class="input-group">
                        <input  id="exportDatetimepicker" class="form-control date-range exportDatetimepicker" type="text">
                        <span class="input-group-addon">
									<i class="fa fa-calendar"></i>
								</span>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>




<div class="layer layer-delete">
    <div class="layer-content">
        <table width="100%">
            <tr>
                <td class="text-right" valign="top">
                    <span class="fa fa-exclamation-circle color-yellow font-30 mr20"></span>
                </td>
                <td>
                    <div class="font-16 mb10">确定要删除吗？</div>
                    <div class="color-grey">删除后该条数据将不被保存</div>
                </td>
            </tr>
        </table>
    </div>
</div>

<div class="layer layer-extension">
    <div class="layer-content">
        <table width="100%">
            <tr>
                <td class="text-right" valign="top">
                    <span class="fa fa-exclamation-circle color-yellow font-30 mr20"></span>
                </td>
                <td>
                    <div class="font-16 mb10">确定要延期吗？</div>
                    <div class="color-grey">延期一次，默认时间增加三天</div>
                </td>
            </tr>
        </table>
    </div>
</div>

<div class="layer layer-complete">
    <div class="layer-content">
        <table width="100%">
            <tr>
                <td class="text-right" valign="top">
                    <span class="fa fa-exclamation-circle color-yellow font-30 mr20"></span>
                </td>
                <td>
                    <div class="font-16 mb10">确定要执行完成吗？</div>
                </td>
            </tr>
        </table>
    </div>
</div>

<script>
    $(function(){

        stopSendEmail();

        doGetRecordAccounts('');

        // 选择时间范围
        $('.datetimepicker').datepicker({
            language: 'zh-CN',
            autoclose: true,
            todayHighlight: true,
            format: 'yyyy-mm-dd',
            startDate:new Date
        })

        $('.exportDatetimepicker').datepicker({
            language: 'zh-CN',
            autoclose: true,
            todayHighlight: true,
            format: 'yyyy-mm-dd'
        })


        // 新增
        $('.js-add').on('click', function(){
            layer.open({
                type: 1,
                shadow: 0.5,
                title: '新增',
                area: '620px',
                btn: ['确定', '取消'],
                content: $('.layer-add'),
                yes:function (index) {
                    var taskType=$('#saveTaskType').val();
                    var completionTime=$('#completionTime').val();
                    console.log("completionTime======"+completionTime)
                    var content=$('#content').val();
                    var isEmail=$('input:radio[name="isEmail"]:checked').val();
                    var ownerUserId=$('#ownerUserId').val();
                    console.log(isEmail);
                    if(isEmail!=null&&completionTime!=''&&content!=''){
                        var ServiceTaskInsertDto= JSON.stringify({ "taskType": taskType,
                            "completionTime": completionTime, "content": content,
                            "isEmail":isEmail,"ownerUserId":ownerUserId});
                        $.ajax({
                            type:'post',
                            contentType: "application/json",
                            url: "${springMacroRequestContext.contextPath}/operation/opUserLog/saveTask",
                            data:ServiceTaskInsertDto,
                            success: function () {
                                doGetRecordAccounts('');
                                $('#content').val('');
                                $('#completionTime').val('');
                                layer.close(index);
                            },
                            error: function(){
                                opLayer.error("请核对新增任务后再点击添加","提示");
                            }
                        })
                    }else{
                        opLayer.error("请核对新增任务后再点击添加","提示");
                    }

                }
            })
        });

        // 导出
        $('.js-export').on('click', function(){
            layer.open({
                type: 1,
                shadow: 0.5,
                title: '导出',
                area: '620px',
                btn: ['确定', '取消'],
                content: $('.layer-export'),
                yes: function(index){
                    var time=$('#exportDatetimepicker').val();
                    window.location.href="${springMacroRequestContext.contextPath}/operation/opUserLog/export?creationTime="+time,
                    layer.close(index);
            }
            });
        });






    })

    //pageURL从分页宏的回调函数获取
    function doGetRecordAccounts(pageURL) {
        let pURL = doBuildDynamicParameter();
        if ($.trim(pageURL) !== '') {
            pURL = pURL + '&' + pageURL;
        }
        $('#taskList').load(_contextPath + '/operation/opUserLog/taskList'+pURL);
    }

    function doBuildDynamicParameter() {
        let pURL ='?';
        if($('#taskType').val()!=''){pURL+='&taskType='+$('#taskType').val()}
        if($('#ownerUserName').val()!=''){pURL+='&ownerUserName='+$('#ownerUserName').val()}
        if($('#state').val()!=''){pURL+='&state='+$('#state').val()}
        return pURL;
    }

    //查询userid与
    function stopSendEmail(){
        let userId="${userId}";
        var ownerUserId=$('#ownerUserId').val();
        if(ownerUserId==userId){
            $("#insertIsEmail").removeAttr("checked");
            $("#insertIsEmail").prop("disabled",true);
        }else{
            $("#insertIsEmail").prop("disabled",false);
        }
    }
</script>