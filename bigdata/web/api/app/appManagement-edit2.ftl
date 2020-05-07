<div class="row">
    <div class="col-xs-12">
        <!-- PAGE CONTENT BEGINS -->
        <div class="box box-default">
            <div class="box-body app-form-box">
                <form class="form-horizontal margin-10" role="form" method="post" id="appForm"
                      enctype="multipart/form-data">
                    <div class="form-group deve-from-title">
                        <label class="col-sm-2 control-title no-padding frombold" for="form-field-1">基本信息</label>
                        <input type="hidden" name="id" value="${app.id!}">
                        <#if audit?default(false)>
                        <input type="hidden" name="status" value="2">
                        </#if>
                    </div>
                    <div class="form-group">
                        <label for="form-field-4" class="col-sm-2 control-label no-padding"><font
                                style="color:red;">*</font>&nbsp;应用名称</label>
                        <div class="col-sm-6">
                            <input maxlength="12" type="text" name="name" value="${app.name!}" id="form-field-4"
                                   class="form-control">
                            <span class="control-disabled">不超过12个字符</span>
                        </div>
                        <span class="col-sm-4 form-tips form-tips-warning hide" id="tip_name"></span>
                    </div>

                    <div class="form-group">
                        <label for="form-field-5" class="col-sm-2 control-label no-padding"><font
                                style="color:red;">*</font>&nbsp;应用介绍</label>
                        <div class="col-sm-6">
                            <textarea style="height: 120px;" name="description" maxlength="256" id="form-field-5"
                                      class="form-control">${app.description!}</textarea>
                            <span class="control-disabled">不超过256个汉字</span>
                        </div>
                        <span class="col-sm-4 form-tips form-tips-warning hide" id="tip_description"></span>
                    </div>

                    <div class="form-group">
                        <label for="form-field-6" class="col-sm-2 control-label no-padding"><font
                                style="color:red;">*</font>&nbsp;应用图标</label>
                        <div class="col-sm-6">
                            <ul class="webuploader-imgList mt-10">
                                <li class="addition webuploader-container <#if app.iconUrl! !="">hide</#if>"
                                    id="picker">
                                    <div class="webuploader-pick"></div>
                                    <div style="position: absolute; top: -1px; left: -1px; width: 95px; height: 95px; overflow: hidden; bottom: auto; right: auto;">
                                        <input id="file" type="file" name="file" class="webuploader-element-invisible"
                                               multiple="multiple" accept="image/*">
                                        <label id="label"
                                               style="opacity: 0; width: 100%; height: 100%; display: block; cursor: pointer; background: rgb(255, 255, 255);"></label>
                                    </div>
                                </li>
                                <li class="<#if app.iconUrl?default("") =="">hide</#if>">
                                    <i class="del"></i>
                                    <img src="${app.iconUrl!}" style="width: 100px; height: 100px;">
                                    <p class="file-img"></p>
                                </li>
                            </ul>
                        </div>
                        <span class="col-sm-4 form-tips form-tips-warning hide" id="tip_file"></span>
                    </div>

                    <div class="form-group">
                        <label for="form-field-8" class="col-sm-2 control-label no-padding"><font
                                style="color:red;">*</font>&nbsp;首页地址</label>
                        <div class="col-sm-6">
                            <input type="text" maxlength="64" name="indexUrl" value="${app.indexUrl!}"
                                   class="form-control">
                        </div>
                        <span class="col-sm-4 form-tips form-tips-warning hide" id="tip_indexUrl"></span>
                    </div>

                    <div class="form-group">
                        <label for="form-field-10" class="col-sm-2 control-label no-padding">退出回调</label>
                        <div class="col-sm-6">
                            <input type="text" maxlength="64" name="invalidateUrl" value="${app.invalidateUrl!}"
                                   class="form-control">
                        </div>
                        <div class="col-sm-4 control-tips"></div>
                    </div>
                    <div class="form-group">
                        <label for="form-field-10" class="col-sm-2 control-label no-padding">登录回调</label>
                        <div class="col-sm-6">
                            <input type="text" maxlength="64" name="verifyUrl" value="${app.verifyUrl!}"
                                   class="form-control">
                        </div>
                        <div class="col-sm-4 control-tips"></div>
                    </div>

                    <div class="form-group deve-from-title">
                        <label class="col-sm-2 control-title no-padding frombold">适用信息</label>
                    </div>
                   
                    <#if app.developerId?default('') != ''>
	                    <div class="form-group">
	                        <label for="form-field-12"
	                               class="col-sm-2 control-label no-padding"><font
	                                style="color:red;">*</font>&nbsp;应用用途</label>
	                        <div class="col-sm-6">
	                            <label class="inline">
	                                <input type="checkbox" name="appTypes" value="1" class="wp appTypes"
	                                <#if app.docking>checked</#if> >
	                                <span class="lbl">单点对接</span>
	                            </label>
	                            <label class="inline">
	                                <input type="checkbox" name="appTypes" value="2" class="wp appTypes"
	                                <#if app.applyApi>checked</#if> >
	                                <span class="lbl">接口申请</span>
	                            </label>
	                        </div>
	                        <span class="col-sm-4 form-tips form-tips-warning hide" id="tip_appTypes"></span>
	                    </div>
                    </#if>

                    <div class="form-group">
                        <label for="form-field-12"
                               class="col-sm-2 control-label no-padding"><font
                                style="color:red;">*</font>&nbsp;适用单位</label>
                        <div class="col-sm-6">
                            <label class="inline">
                                <input type="checkbox" name="unitClasses" value="1" class="wp unitClasses"
                               <#if app.education>checked</#if> >
                                <span class="lbl">教育局</span>
                            </label>
                            <label class="inline">
                                <input type="checkbox" name="unitClasses" value="2" class="wp unitClasses"
                               <#if app.school>checked</#if> >
                                <span class="lbl">学校</span>
                            </label>
                        </div>
                        <span class="col-sm-4 form-tips form-tips-warning hide" id="tip_unitClasses"></span>
                    </div>

                    <div class="form-group">
                        <label for="form-field-13"
                               class="col-sm-2 control-label no-padding"><font
                                style="color:red;">*</font>&nbsp;适用用户</label>
                        <div class="col-sm-6">
                            <label class="inline">
                                <input type="checkbox" name="userTypes" value="1" class="wp userTypes"
                               <#if app.student>checked</#if> >
                                <span class="lbl">学生</span>
                            </label>
                            <label class="inline">
                                <input type="checkbox" name="userTypes" value="2" class="wp userTypes"
                               <#if app.teacher>checked</#if> >
                                <span class="lbl">教师</span>
                            </label>
                            <label class="inline">
                                <input type="checkbox" name="userTypes" value="3" class="wp userTypes"
                               <#if app.family>checked</#if> >
                                <span class="lbl">家长</span>
                            </label>
                        </div>
                        <span class="col-sm-4 form-tips form-tips-warning hide" id="tip_userTypes"></span>
                    </div>

                    <div class="form-group">
                        <label for="form-field-13"
                               class="col-sm-2 control-label no-padding"><font
                                style="color:red;">*</font>&nbsp;适用学段</label>
                        <div class="col-sm-6">
                            <label class="inline">
                                <input type="checkbox" name="sections" value="0" class="wp sections"
                               <#if app.section0>checked</#if> >
                                <span class="lbl">幼儿园</span>
                            </label>
                            <label class="inline">
                                <input type="checkbox" name="sections" value="1" class="wp sections"
                               <#if app.section1>checked</#if> >
                                <span class="lbl">小学</span>
                            </label>
                            <label class="inline">
                                <input type="checkbox" name="sections" value="2" class="wp sections"
                               <#if app.section2>checked</#if> >
                                <span class="lbl">初中</span>
                            </label>
                            <label class="inline">
                                <input type="checkbox" name="sections" value="3" class="wp sections"
                               <#if app.section3>checked</#if> >
                                <span class="lbl">高中</span>
                            </label>
                        </div>
                        <span class="col-sm-4 form-tips form-tips-warning hide" id="tip_sections"></span>
                    </div>


                    <div class="form-group">
                        <label class="col-sm-2 control-label no-padding" for="form-field-1"></label>

                        <div class="col-sm-6">
                            <button class="btn btn-blue" type="button" id="app-save">
                            <#if audit?default(false)>审核通过<#else >保存</#if>
                            </button>
                            <#if audit?default(false)>
                            <button class="btn btn-danger" type="button" id="app-unpass">审核不通过</button>
                            </#if>
                            <button class="btn btn-blue" type="button" id="app-cancel">&nbsp;取消&nbsp;</button>
                        </div>
                        <div class="col-sm-4 control-tips">
                        </div>
                    </div>
                </form>
            </div>
        </div>
        <!-- PAGE CONTENT ENDS -->
    </div><!-- /.col -->
</div>
<script>


    var Event = function () {
        this.sumbit = false;
    };

    Event.prototype = {
        modifyApp: function () {
            if (this.sumbit) {
                return;
            }
            var $this = this;
            $.ajax({
                url: '${request.contextPath}/bigdata/app/modify',
                type: 'POST',
                contentType: false,
                processData: false,
                data: new FormData(document.getElementById('appForm')),
                success: function (res) {
                    if (res.success) {
                        showLayerTips('success', '操作成功', 't', function () {
                            router.go({
                                path: '/bigdata/app/index',
                                name: '应用管理',
                                type: 'item',
                                level: 2
                            });
                        })
                    } else {
                        showLayerTips('error', res.message, 't');
                    }
                },
                error: function () {
                    $this.sumbit = false;
                    showLayerTips('error', '网络异常', 't');
                }
            })
        }
    };

    var Validator = {
        valid: true,
        validateName: function () {
            var name = $('input[name="name"]').val();
            if ($.trim(name) === '') {
                return showValidateMessage('name', '应用名称不能为空');
            }
            if ($.trim(name).length > 12) {
                return showValidateMessage('name', '应用名称长度最大12个字符');
            }
            var isOk = true;
            $.ajax({
                url: '${request.contextPath}/bigdata/app/validate',
                type: 'POST',
                data: {appName: name, id: $.trim($('input[name="id"]').val())},
                async: false,
                success: function (res) {
                    if (!res.success) {
                        isOk = false;
                        return showValidateMessage('name', '应用名称已存在');
                    }
                }
            });
            if (!isOk) {
                return false;
            }
            hideValidateMessage('name');
            return true;
        },
        validateDescription: function () {
            var name = $('textarea[name="description"]').val();
            if ($.trim(name) === '') {
                return showValidateMessage('description', '应用介绍不能为空');
            }
            if ($.trim(name).length > 256) {
                return showValidateMessage('description', '应用介绍长度最大256个字符');
            }
            if ($.trim(name).length < 100) {
                return showValidateMessage('description', '应用介绍不能少于100个字符');
            }
            hideValidateMessage('description');
            return true;
        },
        validateFile: function () {
            if ($('input[name="file"]').parent().parent().hasClass('hide')) {
                return true;
            }
            var name = $('input[name="file"]').val();
            if ($.trim(name) === '') {
                return showValidateMessage('file', '应用图标不能为空');
            }
            hideValidateMessage('file');
            return true;
        },
        validateIndexUrl: function () {
            var name = $('input[name="indexUrl"]').val();
            if ($.trim(name) === '') {
                return showValidateMessage('indexUrl', '首页地址不能为空');
            }
            hideValidateMessage('indexUrl');
            return true;
        },
        <#if app.developerId?default('') != ''>
	        validateAppTypes: function () {
	            var length = $('input.appTypes:checked').length;
	            if (length === 0) {
	                return showValidateMessage('appTypes', '请选择用途');
	            }
	            hideValidateMessage('appTypes');
	            return true;
	        },
        </#if>
        validateUnitClasses: function () {
            var length = $('input.unitClasses:checked').length;
            if (length === 0) {
                return showValidateMessage('unitClasses', '请选择适用单位');
            }
            hideValidateMessage('unitClasses');
            return true;
        },
        validateUserTypes: function () {
            var length = $('input.userTypes:checked').length;
            if (length === 0) {
                return showValidateMessage('userTypes', '请选择适用用户');
            }
            hideValidateMessage('userTypes');
            return true;
        },
        validateSections: function () {
            var $edu = $('input.unitClasses[value="1"]');
            var $sch = $('input.unitClasses[value="2"]');
            if ($edu.is(':checked') && !$sch.is(':checked')) {
                hideValidateMessage('sections');
                return true;
            }
            var length = $('input.sections:checked').length;
            if (length === 0) {
                return showValidateMessage('sections', '请选择适用学段');
            }
            hideValidateMessage('sections');
            return true;
        },
        validate: function () {
            var isOk = true;
            isOk &= this.validateName();
            isOk &= this.validateDescription();
            isOk &= this.validateFile();
            isOk &= this.validateIndexUrl();
            <#if app.developerId?default('') != ''>
               isOk &= this.validateAppTypes();
            </#if>
            isOk &= this.validateUnitClasses();
            isOk &= this.validateUserTypes();
            isOk &= this.validateSections();
            return isOk;
        }
    };

    $(function () {

        //
        $('#picker').next().children('i:first').click(function () {
            $('#picker').removeClass('hide').siblings('li').addClass('hide');
            $('#file').val('');
        });

        $('input[name="name"]').on('blur', function () {
            Validator.validateName();
        });
        $('textarea[name="description"]').on('blur', function () {
            Validator.validateDescription();
        });
        $('input[name="indexUrl"]').on('blur', function () {
            Validator.validateIndexUrl();
        });
        $('input.unitClasses').on('change', function () {
            Validator.validateUnitClasses();
            var $edu = $('input.unitClasses[value="1"]');
            var $sch = $('input.unitClasses[value="2"]');
            if ($edu.is(':checked') && !$sch.is(':checked')) {
                $('input.userTypes').prop('checked', false).parent().addClass('hide');
                $('input.userTypes[value="2"]').prop('checked', true).parent().removeClass('hide');
                $('input.sections').prop('checked', false).attr('disabled', true);
            }
            if (!$edu.is(':checked')) {
                //$('input.userTypes[value="2"]').prop('checked', false);
                $('input.userTypes').parent().removeClass('hide');
                $('input.sections').attr('disabled', false);
            }
            if ($sch.is(':checked')) {
                $('input.userTypes').parent().removeClass('hide');
                $('input.sections').attr('disabled', false);
            }
        });
        $('input.userTypes').on('change', function () {
            Validator.validateUserTypes();
        });
        $('input.sections').on('change', function () {
            Validator.validateSections();
        });

        //初始化 单位类别


        <#if app.education && !app.school>
            //$('input.unitClasses[value="1"]').parent().trigger('click');
            $('input.userTypes:not(:checked)').parent().addClass('hide');
            $('input.sections').attr('disabled', true);
        </#if>

        <#if app.school >
            $('input.userTypes').parent().removeClass('hide');
            $('input.sections').attr('disabled', false);
            //$('input.unitClasses[value="2"]').parent().trigger('click');
        </#if>


        var event = new Event();
        <#if audit?default(false)>
        $('#app-unpass').click(function () {
            $.post('${request.contextPath}/bigdata/app/modify/status/${app.id}', {appStatus: 4}, function (res) {
                if (res.success) {
                    showLayerTips('success', '操作成功', 't', function (re) {
                        router.go({
                            path: '/bigdata/app/index',
                            name: '应用管理',
                            type: 'item',
                            level: 2
                        });
                    })
                } else {
                    showLayerTips('error', '网络异常', 't');
                }
            })
        });
        </#if>

        $('#app-save').click(function () {
            if (!Validator.validate()) {
                return;
            }
            event.modifyApp();
        });
        $('#app-cancel').click(function () {
            router.go({
                path: '/bigdata/app/index',
                level: 2,
                name: '应用管理'
            })
        });

        $('#file').on('change', function (e) {
            //
            var $img = $('#picker').next().find('img');
            $('#picker').next().removeClass('hide').siblings('li').addClass('hide');
            var reader = new FileReader();
            reader.onload = function (ev) {
                $img[0].src = ev.target.result;
            };
            reader.readAsDataURL($('#file')[0].files[0]);
            Validator.validateFile();
        });
        $('#label').click(function () {
            $('#file').trigger('click');
        });
    });

    function showValidateMessage(id, message) {
        $('#tip_' + id).removeClass('hide').text(message);
        return false;
    }

    function hideValidateMessage(id) {
        $('#tip_' + id).addClass('hide')
    }
</script>