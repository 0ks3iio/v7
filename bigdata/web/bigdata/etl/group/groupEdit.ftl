<div class="index">
    <div class="elt-head centered">
        <ul class="steps clearfix">
        <#if steps?exists&&steps?size gt 0>
            <#list steps as step>
                <li id="${step.jobId!}"><span><i>${step.step!}</i>${step.jobName!}</span><span
                        class="wpfont icon-ellipsis"></span></li>
            </#list>
        </#if>
            <li class="add-new-job-step add-new-step"><span><i>+</i></span></li>
        </ul>
        <!--box-->
        <div class="key-box" data-index="">
            <ul>
                <li class="remove-job-step"><i class="iconfont icon-delete-bell"></i>删除</li>
                <li class="edit-job-step"><i class="iconfont icon-editor-fill"></i>修改</li>
            </ul>
        </div>
    </div>

    <div class="elt-body scrollBar4 pb-50 pt-20">
        <form id="submitForm">
            <input type="hidden" name="id" value="${group.id!}">
            <input type="hidden" name="etlType" value="${group.etlType!}">
            <input type="hidden" name="jobType" value="group">
            <!-- 基本参数 -->
            <div class="form-horizontal elt-form">
                <div class="form-group"></div>
                <div class="form-group">
                    <label class="col-sm-2 control-label no-padding-right"><font style="color:red;">*</font>名称：</label>
                    <div class="col-sm-6">
                        <input type="text" name="name" id="name" class="form-control" nullable="false" maxLength="36"
                               value="${group.name!}">
                    </div>
                </div>

                <div class="form-group">
                    <label class="col-sm-2 control-label no-padding-right"><font
                            style="color:red;">*</font>是否定时执行：</label>
                    <div class="col-sm-6">
                        <label class="switch-label">
                            <input <#if group.isSchedule?default(0) ==1>checked="true"</#if>
                                   class="wp wp-switch js-isSchedule" type="checkbox">
                            <span class="lbl"></span>
                        </label>
                        <input type="hidden" id="isSchedule" name="isSchedule" value="${group.isSchedule?default(0)}">
                    </div>
                </div>

                <div class="form-group form-shechule-param <#if group.isSchedule?default(0) ==0>hidden</#if>">
                    <label class="col-sm-2 control-label no-padding-right">定时执行参数：</label>
                    <div class="col-sm-6">
                        <input type="text" name="scheduleParam" id="scheduleParam" class="form-control" nullable="true"
                               maxLength="100" value="${group.scheduleParam!}">
                    </div>
                    <button class="btn btn-blue" type="button" id="paramBtn">参数配置实例</button>
                </div>

                <div class="form-group">
                    <label class="col-sm-2 control-label no-padding-right">备注：</label>
                    <div class="col-sm-6">
                        <input type="text" name="remark" id="remark" class="form-control" nullable="true"
                               maxLength="200" value="${group.remark!}">
                    </div>
                </div>

                <div class="form-group" style="display: none">
                    <label class="col-sm-2 control-label no-padding-right">flowChartJson：</label>
                    <div class="col-sm-6">
                        <textarea id="flowChartJson" name="flowChartJson">${group.flowChartJson!}</textarea>
                    </div>
                </div>

                <div class="form-group">
                    <label class="col-sm-2 control-label">流程图：</label>
                    <div class="col-sm-8">
                        <div style="width: 100%; display: flex; justify-content: space-between">
                            <div id="myPaletteDiv"
                                 style="width: 105px; margin-right: 2px; background-color: whitesmoke; border: solid 1px black"></div>
                            <div id="myDiagramDiv" style="flex-grow: 1; height: 500px; border: solid 1px black"></div>
                        </div>
                    </div>
                </div>
            </div>
        </form>
    </div>

    <!-- 保存按钮 -->
    <div class="save-btn-wrap centered">
        <button type="button" class="btn btn-lg btn-blue" id="groupSaveBtn">保存</button>
    </div>
</div>

<div id="paramDiv"></div>
<div id="groupJobDiv" class="layer layer-addStep"></div>
<script>
    var isSubmit = false;
    var jobIdArray = [];
    // 新增步骤
    $('.add-new-job-step').on('click', function () {
        var self = $(this);
        $('#groupJobDiv').load("${request.contextPath}/bigdata/etl/group/jobs?jobId=", function () {
            layer.open({
                type: 1,
                shade: .6,
                title: '新增步骤',
                btn: ['确定', '取消'],
                area: ['500px', '160px'],
                content: $('.layer-addStep'),
                yes: function (index, layero) {
                    var jobId = $('#jobId').val();
                    if ($.trim(jobId) == '') {
                        layer.tips("不能为空", '#jobId', {
                            tipsMore: true,
                            tips: 3
                        });
                        return;
                    }
                    if(jobIdArray.indexOf(jobId) != -1){
                        layer.tips("不能重复", '#jobId', {
                            tipsMore: true,
                            tips: 3
                        });
                        return;
                    }
                    jobIdArray.push(jobId);
                    var jobName = $('#jobId').find("option:selected").text();
                    var str = '<li id="' + jobId + '"><span><i>' + (parseInt(self.siblings().length) + 1) + '</i>' + jobName + '</span><span class="wpfont icon-ellipsis"></span></li>';
                    self.before(str);
                    layer.close(index);
                }
            });
        });
    });

    // 修改步骤
    $('.edit-job-step').on('click', function () {
        var self = $(this);
        var currentJobId = $('.steps li.active').attr('id');
        $('#groupJobDiv').load("${request.contextPath}/bigdata/etl/group/jobs?jobId=" + currentJobId, function () {
            layer.open({
                type: 1,
                shade: .6,
                title: '修改步骤',
                btn: ['确定', '取消'],
                area: ['500px', '160px'],
                content: $('.layer-addStep'),
                yes: function (index, layero) {
                    var jobId = $('#jobId').val();
                    if ($.trim(jobId) == '') {
                        layer.tips("不能为空", '#jobId', {
                            tipsMore: true,
                            tips: 3
                        });
                        return;
                    }
                    if(jobIdArray.indexOf(jobId) != -1 && jobId !=currentJobId){
                        layer.tips("不能重复", '#jobId', {
                            tipsMore: true,
                            tips: 3
                        });
                        return;
                    }
                    jobIdArray.push(jobId);

                    var num =$('#'+currentJobId).find("span i").html()
                    var jobName = $('#jobId').find("option:selected").text();
                    var str = '<li id="' + jobId + '"><span><i>' + num + '</i>' + jobName + '</span><span class="wpfont icon-ellipsis"></span></li>';
                    $('#'+currentJobId).before(str);
                    $('#'+currentJobId).remove();
                    layer.close(index);
                }
            });
        });
    });

    //设置步骤
    $('.steps').on('click', 'span.wpfont', function (e) {
        e.stopPropagation();
        $(this).closest('li').addClass('active').siblings().removeClass('active')
        $('.key-box').show().css({
            top: e.pageY,
            left: e.pageX
        });
    });

    //删除步骤
    $('.elt-head').on('click', '.remove-job-step', function () {
        var jobId = $('.steps li.active').attr('id');

        var index = jobIdArray.indexOf(jobId);
        if (index > -1) {
            jobIdArray.splice(index, 1);
        }

        $('.steps li.active').remove();

        var num=0;
        $('.steps li').each(function(){
            if(!$(this).hasClass('add-new-job-step'))
                $(this).find("span i").html(++num)
        });
    });

    //屏幕点击,设置消失
    $('body').on('click', function (e) {
        if (e.target.className !== "key-box" || e.target.className !== "wpfont") {
            $('.key-box').hide();
        }
    });

    $('.js-isSchedule').on('click', function () {
        if ($(this).prop('checked') === true) {
            $('.form-shechule-param').removeClass('hidden');
            $('.form-shechule-has-param').addClass('hidden');
            $("#isSchedule").val("1");
        } else {
            $('.form-shechule-param').addClass('hidden');
            $('.form-shechule-has-param').removeClass('hidden');
            $("#isSchedule").val("0");
        }
    })

    $("#groupSaveBtn").on("click", function () {
        if (isSubmit) {
            return;
        }
        isSubmit = true;
        var check = checkValue('#submitForm');
        if (!check) {
            isSubmit = false;
            return;
        }

        var jobIds="";

        $('.steps li').each(function(){
            if(!$(this).hasClass('add-new-job-step'))
                jobIds+=$(this).attr("id")+",";
        });

        if(jobIds == ""){
            showLayerTips('error', '步骤不能为空', 't');
            isSubmit = false;
            return;
        }

        if ($('.js-isSchedule').prop('checked') === true) {
            if ($('#scheduleParam').val() == "") {
                layer.tips("不能为空", "#scheduleParam", {
                    tipsMore: true,
                    tips: 3
                });
                isSubmit = false;
                return;
            }
        }
        saveFlowChartJson();

        var options = {
            url: "${request.contextPath}/bigdata/etl/group/save?jobIds="+jobIds,
            dataType: 'json',
            success: function (data) {
                if (!data.success) {
                    showLayerTips4Confirm('error', data.msg);
                    isSubmit = false;
                } else {
                    showLayerTips('success', data.msg, 't');
                    showList('9')
                }
            },
            clearForm: false,
            resetForm: false,
            type: 'post',
            error: function (XMLHttpRequest, textStatus, errorThrown) {
            }//请求出错
        };
        $("#submitForm").ajaxSubmit(options);
    });

    $("#paramBtn").on("click", function () {
        var url = '${request.contextPath}/bigdata/etl/paramView';
        $("#paramDiv").load(url, function () {
            layer.open({
                type: 1,
                shade: .5,
                title: ['参数配置实例说明', 'font-size:16px'],
                area: ['900px', '600px'],
                maxmin: false,
                btn: ['确定'],
                content: $('#paramDiv'),
                resize: true,
                yes: function (index) {
                    layer.closeAll();
                    $("#paramDiv").empty();
                },
                cancel: function (index) {
                    layer.closeAll();
                    $("#paramDiv").empty();
                }
            });
            $("#paramDiv").parent().css('overflow', 'auto');
        })
    });

    $(function () {
        $('body').load(init());
        <#if group.flowChartJson?exists && group.flowChartJson?length gt 0>
            loadFlowChartJson();
        </#if>
         <#if steps?exists&&steps?size gt 0>
             <#list steps as step>
                  jobIdArray.push('${step.jobId!}');
             </#list>
         </#if>

    })
</script>