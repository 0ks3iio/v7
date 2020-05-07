<div class=" deveman-content">
<div class="row">
    <div class="col-md-12">
        <ul class="tabs tabs-basics" >
            <li class="tab active"><a class="container-item" data-type="interface-apply-container" href="javascript:void(0);">接口管理</a></li>
            <li class="tab"><a class="container-item" data-type="developer-container" href="javascript:void(0);">开发者信息</a></li>
        </ul>
        <div class="tabs-panel active" id="interface-apply-container">
            <div class="row">
                <div class="col-md-6">
                    <div class="bold deveman-basic-title">接口审核</div>
                    <ul class="tabs tabs-basics" >
                        <li class="tab active"><a class="interface-item" data-type="3" href="javascript:void(0);">审核中接口</a></li>
                        <li class="tab"><a class="interface-item" data-type="1" href="javascript:void(0);">已通过接口</a></li>
                        <li class="tab"><a class="interface-item" data-type="2" href="javascript:void(0);">未通过接口</a></li>
                    </ul>
                    <div class="tabs-panel" id="interface-container" style="display: block;">

                    </div>
                </div>
                <div class="col-md-6">
                    <div class="bold">已有应用</div>
                    <#if apps?? && apps?size gt 0>
                    <div class="deveman-app-box">
                        <#list apps as app>
                        <a href="${app.indexUrl!}" target="_blank"><img src="${app.iconUrl!}"></a>
                        </#list>
                    </div>
                    <#else>
                    <div class="no-data-common">
                        <div class="text-center">
                            <img src="${request.contextPath}/bigdata/v3/static/images/public/no-data-common-100.png"/>
                            <p class="color-999">暂无应用</p>
                        </div>
                    </div>
                    </#if>
                </div>
            </div>
        </div>
        <div class="tabs-panel" id="developer-container">
            <div class="form-horizontal margin-10" role="form">
                <div class="form-group deve-from-title">
                    <label class="col-sm-2 control-title no-padding frombold" for="form-field-1">基本信息</label>
                </div>
                <div class="form-group deveman-form-basic">
                    <label class="col-sm-2 control-label no-padding" for="form-field-1">用户名:</label>
                    <span class="showblock deveman-form-content">${developer.username!}</span>
                </div>

                <div class="form-group deveman-form-basic">
                    <label class="col-sm-2 control-label no-padding" for="form-field-1">企业名称:</label>
                    <span class="showblock deveman-form-content">${developer.unitName!}</span>
                </div>

                <div class="form-group deveman-form-basic">
                    <label class="col-sm-2 control-label no-padding" for="form-field-1">白名单:</label>
                    <span id="ip-show" class="showblock deveman-form-content" style="overflow: hidden;">${developer.ips!}</span>
                    <button type="button" class="btn btn-link showblock showhideblock " id="modify-ip">修改</button>
                    <div class="col-sm-6 deveman-form-body hideblock" id="ip-container">
                        <input id="real-ip" type="text" value="${developer.ips!}" placeholder="多个IP请用英文逗号分隔" class="form-control">
                        <button type="button" class="btn btn-primary" onclick="modifyIp(this, true);">确定</button>
                        <button type="button" class="btn btn-default" onclick="modifyIp(this, false);">取消</button>
                    </div>
                    <#--<span class="col-sm-4 form-tips form-tips-success hideblock">错误提示语</span>-->
                </div>

                <div class="form-group deveman-form-basic">
                    <label class="col-sm-2 control-label no-padding" for="form-field-1">联系人姓名:</label>
                    <span class="showblock deveman-form-content">${developer.realName!}</span>
                </div>

                <div class="form-group deveman-form-basic">
                    <label class="col-sm-2 control-label no-padding" for="form-field-1">联系人地址:</label>
                    <span class="showblock deveman-form-content">${developer.address!}</span>
                </div>

                <div class="form-group deveman-form-basic">
                    <label class="col-sm-2 control-label no-padding" for="form-field-1">手机号码:</label>
                    <span class="showblock deveman-form-content">${developer.mobilePhone!}</span>
                </div>

                <div class="form-group deveman-form-basic">
                    <label class="col-sm-2 control-label no-padding" for="form-field-1">邮箱:</label>
                    <span class="showblock deveman-form-content">${developer.email!}</span>
                </div>

                <div class="form-group deveman-form-basic">
                    <label class="col-sm-2 control-label no-padding" for="form-field-1">apKey:</label>
                    <span class="showblock deveman-form-content">${developer.apKey!}</span>
                </div>
                
                
                </form>
            </div>
        </div><!-- /.col -->

    </div><!-- /.row -->
</div>
</div>
<script>
    $(function () {
        // $('.page-content').addClass('deveman-content')
        $('.interface-item').click(function () {
            var status = $(this).data('type');
            $(this).parent('li').addClass('active').siblings('li').removeClass('active');
            $('#interface-container').load('${request.contextPath}/bigdata/developer/interface?developerId=${developer.id}&applyStatus='+status);
        });
        $('.interface-item[data-type="3"]').trigger('click');

        $('.container-item').click(function () {
            $(this).parent('li').addClass('active').siblings('li').removeClass('active');
            var id = $(this).data('type');
            $('#' + id).addClass('active').siblings('div').removeClass('active')
        });

        //update ips
        $('#modify-ip').click(function () {
            $('#ip-container').show().css('display', 'flex');
            $(this).prev().hide();
            $(this).hide();
        });
    });

    function modifyIp(el, modify) {
        var ips = $('#real-ip').val();
        if (modify) {
            $.post('${request.contextPath}/bigdata/developer/interface-apply/modify-ip', {
                ips: ips,
                id: '${developer.id!}'
            }, function (res) {
                if (res.success) {
                    $('#ip-show').text(ips);
                    showLayerTips('success', '更新成功', 't');
                } else {
                    showLayerTips('error', '更新失败', 't');
                }
            });
        }
        $('#ip-container').hide();
        $(el).parent().prev().show();
        $(el).parent().prev().prev().show();
    }
</script>