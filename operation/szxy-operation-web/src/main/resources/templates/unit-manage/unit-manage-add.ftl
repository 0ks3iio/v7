<#import "../macro/region.ftl" as r />
<#import "../macro/staticImport.ftl" as staticImport />
<@staticImport.datepicker />
<div class="box-header">
    <h3 class="box-caption" id="titleConvert">新增单位</h3>
</div>
    <div class="box-body">
        <form id="unitAddForm">
            <div class="form-horizontal" role="form">
                <div class="form-group">
                    <label class="col-sm-2 control-title no-padding-right" id="baseMes">
                        <span class="form-title">基础信息</span>
                    </label>
                    <a class="color-blue ml10 fold  <#if unit.unitClas?default(1) !=2>hide</#if>" id="unbox"
                       href="javascript:void(0);">展开更多信息</a>
                </div>

                <div class="form-group">
                    <label class="col-sm-2 control-label no-padding-right"><span class="color-red">*</span>单位类型：</label>
                    <div class="col-sm-3">
                        <select name="unit.unitType" id="unitType" class="form-control">
                            <#list unitTypeList as unitType>
                                <option value="${unitType.thisId}"
                                        <#if unit.unitType?default(2)?string==unitType.thisId>selected</#if> >${unitType.mcodeContent}</option>
                            </#list>
                        </select>
                    </div>
                    <label class="col-sm-2 control-label no-padding-right"><span class="color-red">*</span>单位名称：</label>
                    <div class="col-sm-3">

                        <input type="text" name="unit.unitName" value="${unit.unitName!}" class="form-control"
                               placeholder="请输入" id="unitName">
                        <span id="tip" style="color: red"></span>
                    </div>
                </div>

                <div class="form-group <#if unit.unitClass?default(1)==1>hide</#if>" id="schoolSpecialCode">
                    <label class="col-sm-2 control-label no-padding-right"><span
                            class="color-red">*</span>办学性质：</label>
                    <div class="col-sm-3">
                        <select name="unit.runSchoolType" id="runSchoolType" class="form-control">
                            <#list runSchoolTypeList as runSchoolType>
                                <option value=${runSchoolType.thisId} <#if unit.runSchoolType?default(811)?string==runSchoolType.thisId>selected</#if> >${runSchoolType.mcodeContent}</option>
                            </#list>
                        </select>
                    </div>
                    <label class="col-sm-2 control-label no-padding-right"><span
                            class="color-red">*</span>学校类别：</label>
                    <div class="col-sm-3">
                        <select name="unit.schoolType" id="schoolType" class="form-control">
                            <#list schoolTypeList as schoolType>
                                <option value=${schoolType.thisId} <#if unit.schoolType?default(111)?string==schoolType.thisId>selected</#if> >${schoolType.mcodeContent}</option>
                            </#list>
                        </select>
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-sm-2 control-label no-padding-right">单位编号：</label>
                    <div class="col-sm-3">
                        <input type="text" name="unit.unionCode" value="${unit.unionCode!}" class="form-control"
                               placeholder="" id="unionCode" disabled>
                    </div>
                    <label class="col-sm-2 control-label no-padding-right"><span class="color-red">*</span>上级单位：</label>
                    <div class="col-sm-3">
                        <select single name="unit.parentId" id="parentId" class="form-control chosen-select"
                                data-placeholder="未选择">
                            <option value=""></option>
                            <#list eduList as edu>
                                <option value=${edu.id} option_region_code=${edu.regionCode!}
                                        data-regionname="${edu.regionName!}"
                                        <#if edu.id==unit.parentId?default("")>selected</#if> >
                                    ${edu.unitName}
                                </option>
                            </#list>
                        </select>
                    </div>
                </div>

                <div class="form-group">
                    <div id="regionContainer" class="hide">
                        <label class="col-sm-2 control-label no-padding-right">
                            <span class="color-red">*</span>所在行政地区：
                        </label>
                        <div class="col-sm-3">
                            <@r.regionNotSelectUpperRegion fieldId="regionCode" fieldName="unit.regionCode" parentRegionCodeCall="getParentRegionCode" regionCode="${unit.regionCode!}" regionName="${unit.regionName!}" />
                        </div>
                    </div>

                    <label class="col-sm-2 control-label no-padding-right"><span class="color-red">*</span>单位性质：</label>
                    <div class="col-sm-3">
                        <label class="inline">
                            <input type="radio" name="unitExtension.usingNature" class="wp using-nature" value="0"
                                   <#if unitExtension.usingNature?default(1)==0 >checked="checked"</#if> />
                            <span class="lbl"> 试用</span>
                        </label>
                        <label class="inline">
                            <input type="radio" name="unitExtension.usingNature" class="wp using-nature" value="1"
                                   <#if unitExtension.usingNature?default(1)==1 >checked="checked"</#if> />
                            <span class="lbl"> 正式单位</span>
                        </label>
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-sm-2 control-label no-padding-right">创建日期：</label>
                    <div class="col-sm-3 mt7"><span id="creationTime">${.now?string("yyyy-MM-dd")}</span></div>
                    <label class="col-sm-2 control-label no-padding-right">到期时间：</label>
                    <div class="col-sm-3">
                        <label class="float-left">
                            <input type="radio" class="wp expire-time-type" name="unitExtension.expireTimeType"
                                   value="0" <#if unitExtension.expireTimeType?default(0)==0 >checked="checked"</#if> />
                            <span class="lbl"> 永久</span>
                        </label>
                        <label class="float-left">
                            <input type="radio" class="wp expire-time-type" name="unitExtension.expireTimeType"
                                   value="1" <#if unitExtension.expireTimeType?default(0)==1 >checked="checked"</#if> />
                            <span class="lbl"> 自定义</span>
                        </label>
                        <div class="input-group float-left <#if unitExtension.expireTimeType?default(0)==0 >hide</#if>"
                             style="width: 50%;" id="expireContainer">
                            <input class="form-control datetimepicker" name="unitExtension.expireTime" type="text"
                                   id="expireTime">
                            <span class="input-group-addon">
                                <i class="fa fa-calendar"></i>
                            </span>
                        </div>
                    </div>
                </div>
                <div class="school-parmeters hide">
                    <div class="form-group">
                        <label class="col-sm-2 control-label no-padding-right">学校英文名称：</label>
                        <div class="col-sm-3">
                            <input type="text" name="school.englishName" value="${school.englishName!}"
                                   class="form-control" placeholder="请输入" id="englishName">
                        </div>
                        <label class="col-sm-2 control-label no-padding-right">学校校长：</label>
                        <div class="col-sm-3">
                            <input type="text" name="school.schoolmaster" value="${school.schoolmaster!}"
                                   class="form-control" placeholder="请输入" id="schoolmaster">
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-2 control-label no-padding-right">法人：</label>
                        <div class="col-sm-3">
                            <input type="text" name="school.legalPerson" value="${school.legalPerson!}"
                                   class="form-control" placeholder="请输入" id="legalPerson">
                        </div>
                        <label class="col-sm-2 control-label no-padding-right">党组织负责人：</label>
                        <div class="col-sm-3">
                            <input type="text" name="school.partyMaster" value="${school.partyMaster!}"
                                   class="form-control" placeholder="请输入" id="partyMaster">
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-2 control-label no-padding-right">主页地址：</label>
                        <div class="col-sm-3">
                            <input type="text" name="school.homepage" value="${school.homepage!}" class="form-control"
                                   placeholder="请输入" id="homepage">
                        </div>
                        <label class="col-sm-2 control-label no-padding-right">重点级别：</label>
                        <div class="col-sm-3">
                            <select name="" id="emphases" class="form-control">
                                <#list emphasesList as emphases>
                                    <option value=${emphases.thisId} <#if school.emphases?default(1)?string==emphases.thisId>selected</#if> >${emphases.mcodeContent}</option>
                                </#list>
                            </select>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-2 control-label no-padding-right">建校年月：</label>
                        <div class="col-sm-3">
                            <div class="input-group">
                                <input name="school.buildDate"
                                       value="${school.buildDate?default(.now)?string("yyyy-MM-dd")}"
                                       class="form-control datetimepickers" type="text" id="buildDate">
                                <span class="input-group-addon">
                                    <i class="fa fa-calendar"></i>
                                </span>
                            </div>
                        </div>
                        <label class="col-sm-2 control-label no-padding-right">校庆日：</label>
                        <div class="col-sm-3">
                            <div class="input-group">
                                <input name="school.anniversary"
                                       value="${school.anniversary?default(.now)?string("yyyy-MM-dd")}"
                                       class="form-control datetimepickers" type="text" id="anniversary">
                                <span class="input-group-addon">
                                    <i class="fa fa-calendar"></i>
                                </span>
                            </div>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-2 control-label no-padding-right">学校主管部门：</label>
                        <div class="col-sm-3">
                            <input type="text" name="school.governor" value="${school.governor!}" class="form-control"
                                   placeholder="请输入" id="governor">
                        </div>
                        <label class="col-sm-2 control-label no-padding-right">学校标识码：</label>
                        <div class="col-sm-3">
                            <input type="text" name="school.organizationCode" value="${school.organizationCode!}"
                                   class="form-control" placeholder="请输入" id="organizationCode">
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-2 control-label no-padding-right">占地面积(㎡)：</label>
                        <div class="col-sm-3">
                            <input type="text" name="school.area" value="${school.area!}" class="form-control"
                                   placeholder="请输入" id="area"
                                   onKeyPress="if((event.keyCode<48 || event.keyCode>57) && event.keyCode!=46 || /\.\d\d$/.test(value))event.returnValue=false">
                        </div>
                        <label class="col-sm-2 control-label no-padding-right">建筑面积(㎡)：</label>
                        <div class="col-sm-3">
                            <input type="text" name="school.builtupArea" value="${school.builtupArea!}"
                                   class="form-control" placeholder="请输入" id="builtupArea"
                                   onKeyPress="if((event.keyCode<48 || event.keyCode>57) && event.keyCode!=46 || /\.\d\d$/.test(value))event.returnValue=false">
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-2 control-label no-padding-right">绿化面积(㎡)：</label>
                        <div class="col-sm-3">
                            <input type="text" name="school.greenArea" value="${school.greeArea!}" class="form-control"
                                   placeholder="请输入" id="greenArea"
                                   onKeyPress="if((event.keyCode<48 || event.keyCode>57) && event.keyCode!=46 || /\.\d\d$/.test(value))event.returnValue=false">
                        </div>
                        <label class="col-sm-2 control-label no-padding-right">运动场面积(㎡)：</label>
                        <div class="col-sm-3">
                            <input type="text" name="school.sportsArea" value="${school.sportsArea!}"
                                   class="form-control" placeholder="请输入" id="sportsArea"
                                   onKeyPress="if((event.keyCode<48 || event.keyCode>57) && event.keyCode!=46 || /\.\d\d$/.test(value))event.returnValue=false">
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-2 control-label no-padding-right">邮政编码：</label>
                        <div class="col-sm-3">
                            <input type="text" name="school.postalcode" value="${school.postalcode!}"
                                   class="form-control" placeholder="请输入" id="postalcode">
                        </div>
                        <label class="col-sm-2 control-label no-padding-right">联系电话：</label>
                        <div class="col-sm-3">
                            <input type="text" name="school.linkPhone" value="${school.linkPhone!}" class="form-control"
                                   placeholder="请输入" id="linkPhone">
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-2 control-label no-padding-right">传真电话：</label>
                        <div class="col-sm-3">
                            <input type="text" name="school.fax" value="${school.fax!}" class="form-control"
                                   placeholder="请输入" id="fax">
                        </div>
                        <label class="col-sm-2 control-label no-padding-right">电子邮箱：</label>
                        <div class="col-sm-3">
                            <input type="text" name="school.email" value="${school.email!}" class="form-control"
                                   placeholder="请输入" id="email">
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-2 control-label no-padding-right">历史沿革：</label>
                        <div class="col-sm-8">
                            <textarea name="school.instroduction" id="introduction" cols="30" rows="5"
                                      class="form-control">${school.introduction!}</textarea>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-2 control-label no-padding-right">备注：</label>
                        <div class="col-sm-8">
                            <textarea name="school.remark" id="remark" cols="30" rows="5"
                                      class="form-control">${school.remark!}</textarea>
                        </div>
                    </div>
                </div>

                <div class="form-group userClass">
                    <label class="col-sm-2 control-title no-padding-right"><span
                            class="form-title">账户信息</span></label><span class="color-red">（同步新增该单位管理员账号，创建后不能删除）</span>
                </div>

                <div class="form-group userClass">
                    <label class="col-sm-2 control-label no-padding-right"><span class="color-red">*</span>用户名：</label>
                    <div class="col-sm-3">
                        <input type="text" name="username" class="form-control" placeholder="" id="username">
                    </div>
                </div>
                <div class="form-group userClass">
                    <label class="col-sm-2 control-label no-padding-right"><span class="color-red">*</span>登录密码：</label>
                    <div class="col-sm-3 mt7">
                        123456
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-sm-2 control-label no-padding-right"></label>
                    <div class="col-sm-8">
                        <button type="button" class="btn btn-blue" onclick="update();">确定</button>
                        <a href="#/next-step" type="button" class="btn btn-blue" onclick="">下一步</a>
                        <button type="button" class="btn btn-white" id="btn_cancel">取消</button>
                    </div>
                </div>
            </div>
        </form>
    </div>
<script>
    $(function () {
        initBootstrapValidator();

        $('#unbox').on('click', function () {
            $('.school-parmeters').toggleClass('hide');
        });

        $('#unitType').on('change', function () {
            let unitType = $(this).val();
            if (unitType !== '2' && unitType !== '8') {
                $('#schoolSpecialCode').removeClass('hide');
                $('#unbox').removeClass('hide');
                $('#regionCode').attr('disabled', true);
            } else {
                $('#schoolSpecialCode').addClass('hide');
                $('#unbox').addClass('hide');
                $('#regionCode').attr('disabled', false);
            }
        });
        $('#parentId').on('change', function () {
            if ($(this).val()) {
                $('#regionContainer').removeClass('hide');
                $('#regionCode').attr('data-regioncode', $(this).find('option:selected').attr('option_region_code'))

                //判定如果选择的是学校则设置regionCode不可点击
                $('#regionCode').val($(this).find('option:selected').data('regionname'));
            }
        });
        $('#unitName').on('change', function () {
            //异步加载 生成管理员用户名
            let unitName = $(this).val();
            if ($.trim(unitName) !== '') {
                $.get(_contextPath + '/operation/unit-manage/unitAdminUsername?unitName=' + unitName, function (res) {
                    if (res.success) {
                        $('#username').val(res.username);
                    } else {
                        opLayer.warn(res.message);
                    }
                })
            }
        });
        $('.using-nature').on('click', function () {
            if ($(this).val() === '0') {
                $('.expire-time-type[value="1"]').trigger('click');
                $('.expire-time-type[value="0"]').attr('disabled', true);
                //试用状态 子系统列表不能选择正式
                $('.s-using-nature[value="1"]').attr("disabled", true);
            } else {
                $('.expire-time-type[value="0"]').attr('disabled', false);
                $('.s-using-nature[value="1"]').attr("disabled", false);
            }
        });
        $('.expire-time-type').on('change', function () {
            if ($(this).val() === '1') {
                $('#expireContainer').removeClass('hide');
            } else {
                $('#expireContainer').addClass('hide');
            }
        });

        //初始化时间组件
        $('.datetimepicker').on('change', function () {
            $('#unitAddForm').data('bootstrapValidator').updateStatus('unitExtension.expireTime', 'VALID', null)
        }).datepicker({
            language: 'zh-CN',
            autoclose: true,
            todayHighlight: true,
            format: 'yyyy-mm-dd',
            startDate: new Date(),
        }).next().on('click', function () {
            $(this).prev().focus();
        });

        routeUtils.add('/next-step', function () {

            if (!doCheckValue()) {
                return;
            }

            $('#unitListContainer').addClass('hide');
            //已经设置，则不需要二次加载
            $('#unitAuthorizationContainer').removeClass('hide');
            if ($.trim($('#unitAuthorizationContainer').html()) === '') {
                let usingNature = $('.using-nature:checked').val();
                $('#unitAuthorizationContainer').load('${springMacroRequestContext.contextPath}/operation/unit-manage/addUnit-next?unitUsingNature=' + usingNature);
            }
        });


    });

    function doCheckValue() {
        //使用bootstrapValidator的验证字段
        $("#unitAddForm").data("bootstrapValidator").validate();//手动触发全部验证
        let valid = $("#unitAddForm").data("bootstrapValidator").isValid();
        //自定义的验证字段
        //验证
        return valid;
    }

    /**
     * 点击确定直接更新
     */
    function update() {
        console.log(doCheckValue());
    }

    /**
     * 取消
     */
    function doCancelAdd() {

    }

    function initBootstrapValidator() {
        $('#unitAddForm').bootstrapValidator({
            feedbackIcons: {
                valid: 'glyphicon glyphicon-ok',
                invalid: 'glyphicon glyphicon-remove',
                validating: 'glyphicon glyphicon-refresh'
            },
            fields: {
                "username": {
                    message: "账号校验失败",
                    validators: {
                        notEmpty: {
                            message: "账号不能为空"
                        },
                        stringLength: {
                            max: 32,
                            message: "用户名过长 最大32个字符"
                        },
                        regexp: {
                            regexp: /^[a-zA-Z0-9]|\\-$/,
                            message: "用户名 只能是英文或数字"
                        },
                        remote: {
                            url: '${springMacroRequestContext.contextPath}/operation/unit-manage/username-check',
                            data: {"username": $('#username').val()},
                            type: 'GET',
                            message: "用户名已存在"
                        }
                    }
                },
                "unit.unitName": {
                    message: "单位名称不能为空",
                    validators: {
                        notEmpty: {
                            message: "单位名称不能为空"
                        },
                        chineseStringLength: {
                            max: 30,
                            message: "单位名称过长 最大30个字符或15个汉字"
                        },
                        callback: {
                            callback: function(value, validator, $filed) {
                                validator.updateStatus('username', 'VALIDATING', null)
                                return true;
                            },
                            message: "请检查用户名"
                        }
                    }
                },
                "unit.parentId": {
                    validators: {
                        notEmpty: {
                            message: "请选择上级单位"
                        },
                        callback: {
                            callback: function(value, validator, $field) {
                                validator.updateStatus('unit.regionCode', 'VALIDATING', null);
                                return true;
                            }
                        }
                    }
                },
                "unit.regionCode": {
                    validators: {
                        notEmpty: {
                            message: "请选择行政区划"
                        },
                        remote: {
                            url: "${springMacroRequestContext.contextPath}/operation/unit-manage/unitRegion-check",
                            type: 'GET',
                            data: function () {
                                return {
                                    "regionCode": $('#regionCode').attr('data-regioncode'),
                                    "unitType": $('#unitType').val(),
                                    "parentId": $('#parentId').find('option:selected').attr('value')
                                };
                            }
                        }
                    }
                },
                "unitExtension.expireTime": {
                    validators: {

                        callback: {
                            message: "请选择期限",
                            callback: function (value, validator, $field) {
                                if ($('.expire-time-type[value="1"]').is(':checked') && $.trim(value) === '') {
                                    return false;
                                } else {
                                    return true;
                                }
                            }
                        }
                    }
                },

                "school.englishName": {
                    validators: {
                        regexp: {
                            regexp: /^[a-zA-Z0-9]+$/,
                            message: "学校英文名称 只能为英文或数字"
                        },
                        stringLength: {
                            max: 180,
                            message: "学校英文名称过长 最大为180个字符"
                        }
                    }
                },
                "school.schoolmaster": {
                    validators: {
                        chineseStringLength: {
                            max: 30,
                            message: "学校校长名字过长 最大15个中文字符或30个英文字符"
                        }
                    }
                },
                "school.legalPerson": {
                    validators: {
                        chineseStringLength: {
                            max: 30,
                            message: "法人过长 最大15个中文字符或30个英文字符"
                        }
                    }
                },
                "school.partyMaster": {
                    validators: {
                        chineseStringLength: {
                            max: 30,
                            message: "党组织负责人过长 最大15个中文字符或30个英文字符"
                        }
                    }
                },
                "school.homepage": {
                    validators: {
                        chineseStringLength: {
                            max: 60,
                            message: "主页地址过长 最大60个字符"
                        }
                    }
                },
                "school.governor": {
                    validators: {
                        chineseStringLength: {
                            max: 60,
                            message: "学校主管部门过长 最大30个中文字符或60个英文字符"
                        }
                    }
                },
                "school.organizationCode": {
                    validators: {
                        stringLength: {
                            max: 45,
                            message: "学校标识码过长 最大45个字符"
                        },
                        regexp: {
                            regexp: /^[a-zA-Z0-9]+$/,
                            message: "学校标识码 只能包含英文字符和数字"
                        }
                    }
                },
                "school.area": {
                    validators: {
                        digits: {
                            message: "占地面积不合法 只能能为整数"
                        },
                        stringLength: {
                            max: 30,
                            message: "占地面积不合法 最大值为30为数字"
                        }
                    }
                },
                "school.builtupArea": {
                    validators: {
                        decimal: {
                            message: "建筑面积不合法 整数位最大10位小数位最大2位",
                            integerLength: 10,
                            decimalLength: 2
                        }
                    }
                },
                "school.greeArea": {
                    validators: {
                        decimal: {
                            message: "绿化面积不合法 整数位最大10位小数位最大2位",
                            integerLength: 10,
                            decimalLength: 2
                        }
                    }
                },
                "school.sportsArea": {
                    validators: {
                        decimal: {
                            message: "运动场面积不合法 整数位最大10位小数位最大2位",
                            integerLength: 10,
                            decimalLength: 2
                        }
                    }
                },
                "school.postalcode": {
                    validators: {
                        stringLength: {
                            max: 6,
                            min: 6,
                            message: "邮政编码长度为6位"
                        },
                        regexp: {
                            regexp: /^[0-9]{6}$/,
                            message: "邮政编码不合法"
                        }
                    }
                },
                "school.linkPhone": {
                    validators: {
                        regexp: {
                            regexp: /^[0-9]{4}\\-[0-9]{7,8}$/,
                            message: "联系电话不合法 例：0571-11111111"
                        }
                    }
                },
                "school.fax": {
                    validators: {
                        stringLength: {
                            max: 30,
                            message: "传真过长 最大30个字符"
                        }
                    }
                },
                "school.email": {
                    validators: {
                        emailAddress: {
                            message: "电子邮箱不合法",
                            multiple: false
                        },
                        chineseStringLength: {
                            max: 40,
                            message: "电子邮箱过长 最大40个字符"
                        }
                    }
                },
                "school.instroduction": {
                    validators: {
                        chineseStringLength: {
                            max: 4000,
                            message: "历史沿革过长 最大4000个英文字符或2000个中文字符"
                        }
                    }
                },
                "school.remark": {
                    validators: {
                        chineseStringLength: {
                            max: 2000,
                            message: "备注过长 最大2000个英文字符或1000个中文字符"
                        }
                    }
                }
            }
        });
    }
</script>