<script src="${springMacroRequestContext.contextPath}/static/components/jquery-form/jquery.form.js"></script>
<div class="box-header">
    <h3 class="box-caption">新增单位</h3>
</div>
<div class="box-body">
    <div class="form-horizontal" role="form">
        <div class="form-group">
            <label class="col-sm-2 control-title no-padding-right"><span class="form-title">授权管理</span></label>
        </div>

        <div class="form-group">
            <label class="col-sm-2 control-label no-padding-right">系统授权：</label>
            <div class="col-sm-8">
                <table class="table table-bordered table-striped table-hover no-margin">
                    <thead>
                    <tr>
                        <th width="150">系统名称</th>
                        <th width="300">是否授权</th>
                        <th>系统到期时间</th>
                        <th width="19"></th>
                    </tr>
                    </thead>
                </table>
                <div style="max-height: 500px;overflow-y: scroll;">
                    <table class="table table-bordered table-striped table-hover no-margin" id="authorizationForm">
                        <tbody>
                        <#if servers?? && servers?size gt 0>
                            <#list servers as server>
                            <tr id="${server.id}">
                                <input type="hidden" name="serverExtensions[${server_index}].serverCode"
                                       value="${server.id}"/>
                                <td>${server.name!}</td>
                                <td>
                                    <label class="no-margin-top">
                                        <input type="radio" value="-2"
                                               name="serverExtensions[${server_index}].usingNature"
                                               data-id="${server.id}"
                                               checked="checked" class="wp s-using-nature">
                                        <span class="lbl">未授权</span>
                                    </label>
                                    <label class="no-margin-top">
                                        <input type="radio" value="0"
                                               name="serverExtensions[${server_index}].usingNature"
                                               data-id="${server.id}"
                                               class="wp s-using-nature">
                                        <span class="lbl"> 试用</span>
                                    </label>
                                    <label class="no-margin-top">
                                        <input type="radio" value="1"
                                               name="serverExtensions[${server_index}].usingNature"
                                               data-id="${server.id}"
                                               <#if unitUsingNature==0>disabled="disabled"</#if>
                                               class="wp s-using-nature">
                                        <span class="lbl"> 正式</span>
                                    </label>
                                </td>
                                <td>
                                    <label class="float-left mt3">
                                        <input type="radio" class="wp s-expire-time-type" value="0" checked="checked"
                                               data-id="${server.id}"
                                               name="serverExtensions[${server_index}].expireTimeType">
                                        <span class="lbl"> 永久</span>
                                    </label>
                                    <label class="float-left mt3">
                                        <input type="radio" class="wp s-expire-time-type" value="1"
                                               data-id="${server.id}"
                                               name="serverExtensions[${server_index}].expireTimeType">
                                        <span class="lbl"> 自定义</span>
                                    </label>
                                    <div class="input-group float-left time-container" style="width: 40%;">
                                        <input class="form-control datetimepicker" type="text"
                                               name="serverExtensions[${server_index}].expireTime">
                                        <span class="input-group-addon">
                                            <i class="fa fa-calendar"></i>
                                        </span>
                                    </div>
                                </td>
                            </tr>
                            </#list>
                        </#if>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>

        <div class="form-group">
            <label class="col-sm-2 control-label no-padding-right"></label>
            <div class="col-sm-8">
                <button class="btn btn-blue" id="btn-createUnit">确定</button>
                <button class="btn btn-white">取消</button>
            </div>
        </div>
    </div>
</div>
<script>
    $(function () {

        //初始化时间组件
        $('.datetimepicker').on('change', function () {
            $(this).parent().removeClass('has-error').parent().find('small.help-block').remove();
        }).datepicker({
            language: 'zh-CN',
            autoclose: true,
            todayHighlight: true,
            format: 'yyyy-mm-dd',
            startDate: new Date(),
        }).next().on('click', function () {
            $(this).prev().focus();
        });
        //选择试用则不能选择永久期限
        $('.s-using-nature').on('change', function () {
            let id = $(this).data('id');
            if ($(this).val() === '0') {
                $('#' + id).find('input.s-expire-time-type[value="0"]').attr('disabled', true);
                $('#' + id).find('input.s-expire-time-type[value="1"]').trigger('click');
            } else {
                $('#' + id).find('input.s-expire-time-type[value="0"]').attr('disabled', false);
            }
        });
        $('.s-expire-time-type').on('change', function () {
            let id = $(this).data('id');
            if ($(this).val() === '0') {
                $('#' + id).find('div.time-container').addClass('hide');
            } else {
                $('#' + id).find('div.time-container').removeClass('hide');
            }
        });


        function doCheckValue() {
            let valid = true;
            $('.s-expire-time-type[value="1"]:checked').each(function () {
                //如果选择了授权
                let expireTimeType = $(this).parent().parent().parent().find('.s-expire-time-type:checked').val();
                let tryUnit = $('.using-nature[value="0"]').is(":checked");
                //正式单位 并且 选择了试用性质
                if (expireTimeType !== "-2" && !tryUnit) {
                    let id = $(this).data('id');
                    let $time = $('#' + id).find('input.datetimepicker');
                    if ($time.val() === '') {
                        $time.parent('div').addClass('has-error').parent().append('<small class="help-block" data-bv-validator="notEmpty" data-bv-for="expireTime[0]" data-bv-result="INVALID" style="">请选择时间</small>')
                        valid = false;
                    }
                }
            });
            return valid;
        }

        let created = false;
        /**
         * 创建单位
         * 1、校验数据是否选择正确
         * 2、整合单位数据和授权数据
         * 3、ajax 处理响应结果
         */
        $('#btn-createUnit').on('click', function () {
            if (!doCheckValue()) {
                return;
            }
            let unitInfo = $('#unitAddForm').serialize();
            let vals = [];
            $('.s-using-nature:checked').each(function () {
                vals.push($(this).attr('name') + '=' + $(this).val());
            });
            $('.s-expire-time-type:checked').each(function () {
                let val = $(this).val();
                vals.push($(this).attr('name') + '=' + val);
                if (val === "1") {
                   let $expireTime = $(this).parent().parent().find('.datetimepicker');
                   vals.push($expireTime.attr('name') + '=' + $expireTime.val());
                }
            });
            if (created) {
                return ;
            }
            created = true;
            $.ajax({
                url: '${springMacroRequestContext.contextPath}/operation/unit-manage/doSaveAndAuthorization',
                type: 'POST',
                data: unitInfo + '&' + vals.join("&"),
                success: function (res) {
                    if (res.success) {
                        opLayer.error(res.message);
                    } else {
                        opLayer.success(res.message);
                    }
                    created = false;
                }
            })
        });
    })
</script>