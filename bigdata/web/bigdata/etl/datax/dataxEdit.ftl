<script src="${request.contextPath}/bigdata/v3/static/plugs/jRange/jquery.range-min.js" type="text/javascript" charset="utf-8"></script>
<div class="index" id="dataxEditDiv">
    <input type="hidden" id="jobId" name="jobId" value="${dataxJobId!}">
    <div class="elt-head centered">
        <ul class="steps clearfix">
            <#if topJobIns?exists>
                <li id="${topJobIns.id!}"><span jobInsName="${topJobIns.name!}"><i>1</i>${topJobIns.name!}</span><span class="wpfont icon-ellipsis"></span></li>
                <#if topJobIns.child?exists>
                    <#assign second = topJobIns.child/>
                    <li id="${second.id!}"><span jobInsName="${second.name!}"><i>2</i>${second.name!}</span><span class="wpfont icon-ellipsis"></span></li>
                    <#if second.child?exists>
                        <#assign third = second.child/>
                        <li id="${third.id!}"><span jobInsName="${third.name!}"><i>3</i>${third.name!}</span><span class="wpfont icon-ellipsis"></span></li>
                        <#if third.child?exists>
                            <#assign fourth = third.child/>
                            <li id="${fourth.id!}"><span jobInsName="${fourth.name!}"><i>4</i>${fourth.name!}</span><span class="wpfont icon-ellipsis"></span></li>
                            <#if fourth.child?exists>
                                <#assign fifth = fourth.child/>
                                <li id="${fifth.id!}"><span jobInsName="${fifth.name!}"><i>5</i>${fifth.name!}</span><span class="wpfont icon-ellipsis"></span></li>
                            </#if>
                        </#if>
                    </#if>
                </#if>
            </#if>
            <li class="add-new-job-instance add-new-step"><span><i>+</i></span></li>
        </ul>
        <!--box-->
        <div class="key-box" data-index="">
            <ul>
                <li class="js-remove"><i class="iconfont icon-delete-bell"></i>删除</li>
                <li class="js-edit"><i class="iconfont icon-editor-fill"></i>修改</li>
            </ul>
        </div>
    </div>

    <!-- 无数据 -->
    <!-- <div class="no-data-common">
        <div class="text-center">
            <img src="../images/public/no-data-common-100.png"/>
            <p class="color-999">暂无数据,请在上方点击‘+’按钮添加数据</p>
        </div>
    </div> -->

    <div class="wrap-1of1 centered no-data-state <#if dataxJobInsList?exists&& dataxJobInsList?size gt 0>hide</#if>">
        <div class="text-center">
            <img src="/bigdata/v3/static/images/public/no-data-common-100.png">
            <p>暂无数据,请在上方点击‘+’按钮添加数据</p>
        </div>
    </div>

</div>
<div class="layer layer-addInstance">
    <div class="form-horizontal">
        <div class="form-group">
            <label class="col-sm-2 control-label">名称：</label>
            <div class="col-sm-9">
                <input type="text" maxlength="25" id="jobInsName" class="form-control">
            </div>
        </div>
    </div>
</div>
<script type="text/javascript">
    $(function () {

        var target;
        // 新增任务实例
        $('.add-new-job-instance').on('click', function () {
            var self = $(this);
            var parentId = '';
            if ($('.steps li').length > 1) {
                parentId = $(this).prev().attr('id');
            }

            $('#jobInsName').val('');
            layer.open({
                type: 1,
                shade: .6,
                title: '新增任务配置',
                btn: ['确定', '取消'],
                area: ['500px', '160px'],
                content: $('.layer-addInstance'),
                yes: function (index, layero) {
                    if ($.trim($('#jobInsName').val()) == '') {
                        layer.tips("不能为空", '#jobInsName', {
                            tipsMore: true,
                            tips: 3
                        });
                        return;
                    }
                    // 新增任务实例
                    $.ajax({
                        url: _contextPath + '/bigdata/datax/job/saveJobInstance',
                        type: 'POST',
                        dataType: 'json',
                        data: {name: $('#jobInsName').val(), jobId:$('#jobId').val(), parentId: parentId},
                        success: function (response) {
                            if (response.success) {
                                $('.elt-head li').removeClass('active');
                                // 查看任务详情
                                $.ajax({
                                    url: _contextPath + '/bigdata/datax/job/jobConfig',
                                    type: 'POST',
                                    dataType: 'html',
                                    data: {jobInsId: response.data},
                                    success: function (response) {
                                        $('.no-data-state').hide();
                                        $('.elt-body').remove();
                                        $('.save-btn-wrap').remove();
                                        $('#dataxEditDiv').append(response);
                                    }
                                });
                                var str = '<li id="'+response.data+'" class="active"><span jobInsName="'+$('#jobInsName').val()+'"><i>' + (parseInt(self.siblings().length) + 1) + '</i>' + $('#jobInsName').val() + '</span><span class="wpfont icon-ellipsis"></span></li>';
                                self.before(str);
                            } else {
                                showLayerTips4Confirm('error', response.message);
                            }
                        }
                    });
                    layer.close(index);
                }
            });
        });
        //选择步骤
        $('.steps').on('click', 'li', function () {
            if ($(this).hasClass('add-new-job-instance') == false) {
                $(this).addClass('active').siblings().removeClass('active');
                var id = $(this).attr('id');
                // 查看任务详情
                $.ajax({
                    url: _contextPath + '/bigdata/datax/job/jobConfig',
                    type: 'POST',
                    dataType: 'html',
                    data: {jobInsId: id},
                    success: function (response) {
                        $('.no-data-state').hide();
                        $('.elt-body').remove();
                        $('.save-btn-wrap').remove();
                        $('#dataxEditDiv').append(response);
                    }
                });
            }
        });

        if ($('.steps li').length > 1) {
            $('.steps li:first').click();
        }

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
        $('.elt-head').on('click', '.js-remove', function () {
            var jobInsId = $('.steps li.active').attr('id');

            $.ajax({
                url: _contextPath + '/bigdata/datax/job/deleteJobInstance',
                type: 'POST',
                dataType: 'json',
                data: {id: jobInsId},
                success: function (response) {
                    if (response.success) {
                        $('.steps li.active').remove();
                        $('.key-box').hide();
                        $('.steps li i').each(function (ele, index) {
                            if ($(this).closest('li').hasClass('add-new-job-instance') == false) {
                                $(this).text($(this).closest('li').index() + 1);
                            }
                        });
                        console.info($('.steps li').length);
                        if ($('.steps li').length == 1) {
                            $('.elt-body').remove();
                            $('.save-btn-wrap').remove();
                            $('.no-data-state').show();
                        } else {
                            $('.steps li:first').click();
                        }
                    } else {
                        showLayerTips4Confirm('error', response.message);
                    }
                }
            });
        });
        //修改步骤
        $('body').on('click', '.js-edit', function () {
            var jobinsId = $('.steps li.active').attr('id');
            var i = $('.steps li.active span:first-child i');
            var target = $('.steps li.active span:first-child');
            var text = target.attr('jobInsName');
            var str = '<input type="text" name="" value="' + text + '"/>';
            target.text('').append(i).append(str).find('input').select().on('blur', function () {
                var $input = $(this);
                $.ajax({
                    url: _contextPath + '/bigdata/datax/job/saveJobInstance',
                    type: 'POST',
                    dataType: 'json',
                    data: {id: jobinsId, name:$input.val()},
                    success: function (response) {
                        if (response.success) {
                            target.text($input.val()).prepend(i);
                            target.attr('jobInsName', $input.val());
                            $input.remove();
                        } else {
                            showLayerTips4Confirm('error', response.message);
                            $input.remove();
                        }
                    }
                });
            });
        });
        //屏幕点击,设置消失
        $('body').on('click', function (e) {
            if (e.target.className !== "key-box" || e.target.className !== "wpfont") {
                $('.key-box').hide();
            }
        });


        // 添加参数
        $('body').on('click', '.add-parameter', function (e) {
            layer.open({
                type: 1,
                shade: .6,
                title: '选择指标',
                btn: ['确定', '删除'],
                area: ['300px', '400px'],
                content: $('.layer-choose-goal')
            });
        });
        //删除数据
        $('.elt-body').on('click', '.js-delete-data', function () {
            target = $(this).closest('.form-group');
            layer.tips('<div class="msg">\
                                <div class="mb-20">确定删除此数据吗</div>\
                                <div class="msg-btn clearfix">\
                                    <button type="button" class="btn btn-blue js-remove-target">确定</button>\
                                    <button type="button" class="btn btn-default js-cancel">取消</button>\
                                </div>\
                            </div>', this, {
                tips: 1,
                time: 4000
            });
        });


        //源数据,目标数据
        //删除数据
        $('.elt-body').on('click', '.js-delete', function () {
            target = $(this).closest('tr');
            layer.tips('<div class="msg">\
                                <div class="mb-20">确定删除此数据吗</div>\
                                <div class="msg-btn clearfix">\
                                    <button type="button" class="btn btn-blue js-remove-target">确定</button>\
                                    <button type="button" class="btn btn-default js-cancel">取消</button>\
                                </div>\
                            </div>', this, {
                tips: 1,
                time: 4000
            });
        });

        //新增数据
        $('.elt-body').on('click', '.js-add', function (e) {
            layer.open({
                type: 1,
                shade: .6,
                title: '选择指标',
                btn: ['确定', '删除'],
                area: ['300px', '400px'],
                content: $('.layer-choose-goal')
            });
        });

    })
</script>
