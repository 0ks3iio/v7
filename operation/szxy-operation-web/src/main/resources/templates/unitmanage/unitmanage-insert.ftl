<#import "../macro/region.ftl" as r />
<div class="box-header">
    <h3 class="box-caption" id="titleConvert">${titleName}</h3>
</div>
		<div class="box-body">
            <form id="unitAddForm">
                <div class="form-horizontal" role="form">
                    <div class="form-group">
                        <label class="col-sm-2 control-title no-padding-right" id="baseMes"><span class="form-title">基础信息</span></label>
                        <a class="color-blue ml10 fold <#if unit.unitClass?default(1) !=2>hide</#if>">展开更多信息</a>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-2 control-label no-padding-right"><span
                                class="color-red">*</span>单位类型：</label>
                        <div class="col-sm-3">
                            <select name="unit.unitType" id="unitType" class="form-control">
							<#list unitTypeList as unitType>
                                <option value="${unitType.thisId}"
									<#if unit.unitType?default(2)?string==unitType.thisId>selected</#if>>${unitType.microcodeContent}</option>
                            </#list>
                            </select>

                        </div>
                        <label class="col-sm-2 control-label no-padding-right"><span
                                class="color-red">*</span>单位名称：</label>
                        <div class="col-sm-3">
                            <input type="text" name="unit.unitName" class="form-control" placeholder="请输入" id="unitName"
                                   value="${unit.unitName!}">
                        </div>
                    </div>

                    <div class="form-group <#if unit.unitClass?default(1)!=2>hide</#if>" id="schoolChange">
                        <label class="col-sm-2 control-label no-padding-right"><span
                                class="color-red">*</span>办学性质：</label>
                        <div class="col-sm-3">
                            <select name="school.runSchoolType" id="runSchoolType" class="form-control">
                            <#list runSchoolTypeList as runSchoolType>
                                <option value=${runSchoolType.thisId}
                                  <#if school.runSchoolType?default(811)?string==runSchoolType.thisId>selected</#if>>${runSchoolType.microcodeContent}</option>
                            </#list>
                            </select>
                        </div>

                        <label class="col-sm-2 control-label no-padding-right"><span
                                class="color-red">*</span>学校类别：</label>
                        <div class="col-sm-3">
                            <select name="school.schoolType" id="schoolType" class="form-control">
                                <#list schoolTypeList as schoolType>
                                    <option value="${schoolType.thisId}"
                                    <#if school.schoolType?default(111)?string==schoolType.thisId>selected</#if>>${schoolType.microcodeContent}</option>
                                </#list>
                            </select>
                        </div>

                    </div>
                    <div class="form-group">

                        <label class="col-sm-2 control-label no-padding-right">单位编号：</label>
                        <div class="col-sm-3">
                            <input type="text" name="unit.unionCode" class="form-control" placeholder="" id="unionCode"
                                   disabled value="${unit.unionCode!}">
                        </div>
                        <label class="col-sm-2 control-label no-padding-right" id="parentUnit"><span
                                class="color-red">*</span>上级单位：</label>
                        <div class="col-sm-3">
                            <select single name="unit.parentId" id="parentId" class="form-control"
                                    data-placeholder="未选择">
                                <option value=""></option>
                                <#list parentUnitList as parentUnit>
                                    <option value="${parentUnit.id}" option_region_code="${parentUnit.regionCode!}"
                                    <#if unit.parentId?default("")?string==parentUnit.id>selected</#if>>${parentUnit.unitName}</option>
                                </#list>
                            </select>
                        </div>
                    </div>

                    <div class="form-group">
                        <label class="col-sm-2 control-label no-padding-right"><span
                                class="color-red">*</span>所在行政地区：</label>
                        <div class="col-sm-3">
                        <@r.region dataType='unit' regionCode='' regionName=''/>
                        </div>
                        <label class="col-sm-2 control-label no-padding-right"><span
                                class="color-red">*</span>单位性质：</label>
                        <div class="col-sm-3">
                            <label class="inline">
                                <input type="radio" class="wp" name="unitExtension.usingNature" value="0"
                                       onclick="tryUseRadio()" id="tryUse"
                                       <#if (titleName=='编辑'&& unitExtension.usingNature?default(1)==1)||titleName=='试用转正式'>disabled</#if>
                                       <#if titleName!='试用转正式'&&unitExtension.usingNature?default(1)==0>checked="checked"</#if>>
                                <span class="lbl"> 试用</span>
                            </label>
                            <label class="inline">
                                <input type="radio" class="wp" name="unitExtension.usingNature" value="1"
                                       onclick="formalRadio()"
                                       <#if titleName=='编辑'&& unitExtension.usingNature?default(1)==0>disabled</#if>
                                       <#if unitExtension.usingNature?default(1)==1||titleName=='试用转正式'>checked="checked"</#if>>
                                <span class="lbl"> 正式单位</span>
                            </label>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-2 control-label no-padding-right">创建日期：</label>
                        <div class="col-sm-3 mt7"><span
                                id="creationTime">${unit.creationTime?default(.now)?string("yyyy/MM/dd")}</span></div>
                        <label class="col-sm-2 control-label no-padding-right">单位到期时间：</label>
                        <div class="col-sm-3">
                            <label class="float-left">
                                <input type="radio" class="wp expire-time-type" name="unitExtension.expireTimeType"
                                       value="0" onclick="empty()" id="permanent"
                                        <#if titleName=='编辑'&& unitExtension.expireTimeType?default(0)==1>disabled</#if>
                                        <#if unitExtension.expireTimeType?default(0)==0>checked="checked"</#if>>
                                <span class="lbl"> 永久</span>
                            </label>
                            <label class="float-left">
                                <input type="radio" class="wp expire-time-type" name="unitExtension.expireTimeType"
                                       value="1" id="custom" onclick="showTime()"
                                       <#if titleName=='编辑'&& unitExtension.expireTimeType?default(0)==0>disabled</#if>
                                       <#if unitExtension.expireTimeType?default(0)==1>checked="checked"</#if>>
                                <span class="lbl"> 自定义</span>
                            </label>
                            <div class="input-group float-left <#if unitExtension.expireTimeType?default(0)==0>hide</#if>"
                                 style="width: 50%;" id="timeDiv">
                                <input class="form-control datetimepickerEx<#if titleName=='编辑'>readonly</#if>"
                                       name="unitExtension.expireTime" type="text" id="expireTime"
                                       value="<#if unitExtension.expireTime??>${unitExtension.expireTime?string("yyyy/MM/dd")}</#if>"
                                        <#if !unitExtension.expireTime??>disabled </#if>
                                       <#if titleName=='编辑'>readonly</#if>>
                                <span class="input-group-addon">
                                    <i class="fa fa-calendar"></i>
                                </span>
                            </div>
                        </div>
                    </div>
                    <div class="form-group hide" id="contractNumber">
                        <label class="col-sm-2 control-label no-padding-right"><span class="color-red">*</span>合同编号：</label>
                        <div class="col-sm-3">
                            <input type="text" name="unitExtension.contractNumber" class="form-control"
                                   id="contractNumberVal" placeholder="" value="${unitExtension.contractNumber!}"
                            <#--<#if titleName=='编辑'>readonly</#if>-->
                            >
                        </div>
                    </div>
                    <div class="form-group hide" id="unitPrincipal">
                        <label class="col-sm-2 control-label no-padding-right">联系人：</label>
                        <div class="col-sm-8">
                            <table class="table table-bordered table-striped table-hover no-margin">
                                <thead id="add_tr">
                                <tr>
                                    <th>姓名</th>
                                    <th>类型</th>
                                    <th>电话</th>
                                    <th>备注</th>
                                    <th>操作</th>
                                </tr>
                                </thead>
                                <tbody>
                                <tr>
                                    <td colspan="5" class="text-center">
                                        <a class="table-btn color-blue" href="#" onclick="insert_row('','0','','')">+ 新增</a>
                                    </td>
                                </tr>
                                </tbody>
                            </table>
                        </div>
                    </div>
                    <div class="more" style="display: none;">
                        <div class="form-group">
                            <label class="col-sm-2 control-label no-padding-right">学校英文名称：</label>
                            <div class="col-sm-3">
                                <input type="text" value="${school.englishName!}" name="school.englishName"
                                       class="form-control" placeholder="请输入" id="englishName">
                            </div>
                            <label class="col-sm-2 control-label no-padding-right">学校校长：</label>
                            <div class="col-sm-3">
                                <input type="text" value="${school.schoolmaster!}" name="school.schoolmaster"
                                       class="form-control" placeholder="请输入" id="schoolmaster">
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-sm-2 control-label no-padding-right">法人：</label>
                            <div class="col-sm-3">
                                <input type="text" value="${school.legalPerson!}" name="school.legalPerson"
                                       class="form-control" placeholder="请输入" id="legalPerson">
                            </div>
                            <label class="col-sm-2 control-label no-padding-right">党组织负责人：</label>
                            <div class="col-sm-3">
                                <input type="text" value="${school.partyMaster!}" name="school.partyMaster"
                                       class="form-control" placeholder="请输入" id="partyMaster">
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-sm-2 control-label no-padding-right">主页地址：</label>
                            <div class="col-sm-3">
                                <input type="text" value="${school.homepage!}" name="school.homepage"
                                       class="form-control" placeholder="请输入" id="homepage">
                            </div>
                            <label class="col-sm-2 control-label no-padding-right">重点级别：</label>
                            <div class="col-sm-3">
                                <select name="school.emphases" id="emphases" class="form-control">
								<#list emphasesList as emphases>
                                    <option value=${emphases.thisId}
									<#if school.emphases?default(1)?string==emphases.thisId>selected</#if>>${emphases.microcodeContent}</option>
                                </#list>
                                </select>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-sm-2 control-label no-padding-right">建校年月：</label>
                            <div class="col-sm-3">
                                <div class="input-group">
                                    <input class="form-control datetimepickers" type="text" id="buildDate"
                                           value="${school.buildDate?default(.now)?string("yyyy/MM/dd")}">
                                    <span class="input-group-addon">
															<i class="fa fa-calendar"></i>
														</span>
                                </div>
                            </div>
                            <label class="col-sm-2 control-label no-padding-right">校庆日：</label>
                            <div class="col-sm-3">
                                <div class="input-group">
                                    <input class="form-control datetimepickers" type="text" id="anniversary"
                                           value="${school.buildDate?default(.now)?string("yyyy/MM/dd")}">
                                    <span class="input-group-addon">
															<i class="fa fa-calendar"></i>
														</span>
                                </div>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-sm-2 control-label no-padding-right">学校主管部门：</label>
                            <div class="col-sm-3">
                                <input type="text" value="${school.governor!}" name="school.governor"
                                       class="form-control" placeholder="请输入" id="governor">
                            </div>
                            <label class="col-sm-2 control-label no-padding-right">学校标识码：</label>
                            <div class="col-sm-3">
                                <input type="text" value="${school.organizationCode!}" name="school.organizationCode"
                                       class="form-control" placeholder="请输入" id="organizationCode">
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-sm-2 control-label no-padding-right">占地面积(㎡)：</label>
                            <div class="col-sm-3">
                                <input type="text" value="${school.area!}" name="school.area" class="form-control"
                                       placeholder="请输入" id="area"
                                       onKeyPress="if((event.keyCode<48 || event.keyCode>57) && event.keyCode!=46 || /\.\d\d$/.test(value))event.returnValue=false">
                            </div>
                            <label class="col-sm-2 control-label no-padding-right">建筑面积(㎡)：</label>
                            <div class="col-sm-3">
                                <input type="text" value="${school.builtupArea!}" name="school.builtupArea"
                                       class="form-control" placeholder="请输入" id="builtupArea"
                                       onKeyPress="if((event.keyCode<48 || event.keyCode>57) && event.keyCode!=46 || /\.\d\d$/.test(value))event.returnValue=false">
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-sm-2 control-label no-padding-right">绿化面积(㎡)：</label>
                            <div class="col-sm-3">
                                <input type="text" value="${school.greenArea!}" name="school.greenArea"
                                       class="form-control" placeholder="请输入" id="greenArea"
                                       onKeyPress="if((event.keyCode<48 || event.keyCode>57) && event.keyCode!=46 || /\.\d\d$/.test(value))event.returnValue=false">
                            </div>
                            <label class="col-sm-2 control-label no-padding-right">运动场面积(㎡)：</label>
                            <div class="col-sm-3">
                                <input type="text" value="${school.sportsArea!}" name="school.sportsArea"
                                       class="form-control" placeholder="请输入" id="sportsArea"
                                       onKeyPress="if((event.keyCode<48 || event.keyCode>57) && event.keyCode!=46 || /\.\d\d$/.test(value))event.returnValue=false">
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-sm-2 control-label no-padding-right">邮政编码：</label>
                            <div class="col-sm-3">
                                <input type="text" value="${school.postalCode!}" name="school.postalCode"
                                       class="form-control" placeholder="请输入" id="postalcode">
                            </div>
                            <label class="col-sm-2 control-label no-padding-right">联系电话：</label>
                            <div class="col-sm-3">
                                <input type="text" value="${school.linkPhone!}" name="school.linkPhone"
                                       class="form-control" placeholder="请输入" id="linkPhone">
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-sm-2 control-label no-padding-right">传真电话：</label>
                            <div class="col-sm-3">
                                <input type="text" value="${school.fax!}" name="school.fax" class="form-control"
                                       placeholder="请输入" id="fax">
                            </div>
                            <label class="col-sm-2 control-label no-padding-right">电子信箱：</label>
                            <div class="col-sm-3">
                                <input type="text" value="${school.email!}" name="school.email" class="form-control"
                                       placeholder="请输入" id="email">
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-sm-2 control-label no-padding-right">历史沿革：</label>
                            <div class="col-sm-8">
                                <textarea name="school.introduction" id="introduction" cols="30" rows="5"
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
                                class="form-title">账户信息</span></label><span
                            class="color-red">（同步新增该单位管理员账号，创建后不能删除）</span>
                    </div>

                    <div class="form-group userClass">
                        <label class="col-sm-2 control-label no-padding-right"><span
                                class="color-red">*</span>用户名：</label>
                        <div class="col-sm-3">
                            <input type="text" name="username" class="form-control" placeholder="" id="username">
                        </div>
                    </div>
                    <div class="form-group userClass">
                        <label class="col-sm-2 control-label no-padding-right"><span
                                class="color-red">*</span>登录密码：</label>
                        <div class="col-sm-3 mt7">
                            123456
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-2 control-label no-padding-right"></label>
                        <div class="col-sm-8">
                            <button class="btn btn-blue <#if titleName!="编辑">hide</#if>" id="toConfirm"
                                    data="${unit.id!}">确定
                            </button>
                            <button class="btn btn-blue <#if titleName=="编辑">hide</#if>" id="toNext">下一步</button>
                            <button class="btn btn-white" id="toCancel">取消</button>
                        </div>
                    </div>
                </div>
            </form>
        </div>

<script src="${springMacroRequestContext.contextPath}/static/components/chosen/chosen.jquery.js"></script>
<script src="${springMacroRequestContext.contextPath}/static/operation/js/spop.js"></script>
<link rel="stylesheet" href="${springMacroRequestContext.contextPath}/static/components/chosen/chosen.min.css">
<link rel="stylesheet" href="${springMacroRequestContext.contextPath}/static/operation/css/spop.css">

	<script>
        var provinceCode = '00';
        var cityCode = '00';
        var dictrictCode = '00';
        $(function () {
            initBootstrapValidator();
            selectChosen();
            /**
             * 单位联系人回显
             * */
            <#if unitPrincipalList??>
                <#list unitPrincipalList as list>
                    insert_row('${list.realName!}','${list.type!0}','${list.phone!}','${list.remark!}');
                </#list>
            </#if>
            /**
             * 新增 编辑 维护 试用转正式页面初始化
             * */
            if ($("#titleConvert").html() == "新增单位") {
                $('#unitType').children().eq(0).remove();
                $("#unitType").prop("disabled", false)
            } else {
                $(".userClass").addClass("hide");
                $("#unitType").prop("disabled", true)
                if ($("#unitType").val() == '2') {
                    $('#parentId').prop('disabled', true);
                    $('#parentId').trigger('chosen:updated');
                }
                if ($("#unitType").val() == '1') {
                    $('#parentId').val('');
                    $('#parentId').trigger('chosen:updated');
                    $('#parentUnit').addClass('hide');
                    $('#parentUnit').next().addClass('hide');
                }
            }
            /**
             * 合同编号以及单位联系人的显示控制
             * */
            if ($("input[name='unitExtension.usingNature'][value='1']").is(':checked')) {
                $('#contractNumber').removeClass('hide');
                $('#unitPrincipal').removeClass('hide');
            }else if ($("input[name='unitExtension.usingNature'][value='0']").is(':checked')) {
                $('#contractNumber').addClass('hide');
                $('#unitPrincipal').addClass('hide');
            }
            //----------上级单位异步加载行政区划
            if (pId != '') {
                parentRegionCode = $('option[value=' + pId + ']').attr('option_region_code');
            }
            $('#parentId').change(function () {
                $.ajaxSettings.async = false;
                if ($("#parentId").find("option:selected").val() != '') {
                    parentRegionCode = $("#parentId").find("option:selected").attr("option_region_code");
                    returnRegion(parentRegionCode);
                } else {
                    $('#regionName').val('');
                    parentRegionCode = '000000';
                }
                $.ajaxSettings.async = true;
            });
            //--------------回显行政区划
            $.ajaxSettings.async = false;
            returnRegion('${unit.regionCode!}');
            $.ajaxSettings.async = true;
            //------------------展开更多信息
            $(".fold").click(function () {
                if ($(this).text() === "展开更多信息") {
                    $(this).text("收起更多信息").parents(".form-group").siblings(".more").show()
                } else {
                    $(this).text("展开更多信息").parents(".form-group").siblings(".more").hide()
                }
            })
            //------------单位类型控制字段
            $("#unitType").on("change", function () {
                if ($("#unitType").val() == '1' || $("#unitType").val() == '2' || $("#unitType").val() == '8') {
                    $('.more').find('input,textarea').each(function () {
                        $(this).val('');
                    });
                    $("#schoolChange").addClass("hide")
                    $(".fold").addClass("hide");
                    $(".fold").parents(".form-group").siblings(".more").hide()
                } else {
                    $("#schoolChange").removeClass("hide")
                    $(".fold").text("展开更多信息")
                    $(".fold").removeClass("hide");
                }
            })
            //-----------单位名称 异步加载 生成管理员用户名
            $('#unitName').on('change', function () {
                let unitName = $('#unitName').val();
                if ($.trim(unitName) !== '') {
                    $.get(_contextPath + '/operation/unit/manage/page/turnUnitNameToPinYin?unitName=' + unitName, function (res) {
                        $('#username').val(res.username);
                    })
                }
            });
            //---------确定按钮，下一步按钮操作  取消操作按钮
            $(".col-sm-8").on("click", "#toNext", turnToNext).on("click", "#toCancel", returnUnitList)
                    .on("click", "#toConfirm", UpdateUnit);

        })

        /**
         * 控制行政区划函数
         * @param regionCode
         */
        function returnRegion(regionCode) {
            if (regionCode == null || regionCode.length < 6) {
                return;
            }
            provinceCode = regionCode.substring(0, 2);
            cityCode = regionCode.substring(2, 4);
            dictrictCode = regionCode.substring(4, 6);
            regionIni($('#regionText'));
            province($('.multi-select-layer').find('span[data-region-code=' + provinceCode + ']'));
            if (cityCode != '00') {
                city($('.multi-select-layer').find('li[data-region-code=' + provinceCode + cityCode + ']'))
                if (dictrictCode != '00') {
                    dictrict($('.multi-select-layer').find('li[data-region-code=' + provinceCode + cityCode + dictrictCode + ']'))
                } else {
                    $('#regionText').click();
                }
            } else {
                $('#regionText').click();
            }
            $('.form-title').click();
        }

        //确定按钮
        function UpdateUnit() {
            if (!doCheckValue()) {
                return;
            }
            var opUnitPrincipalDtos = new Array();
            $(".opUnitPrincipalDto").each(function () {
                var params = {
                    realName:$(this).find(".unitPrincipalRealName").val(),
                    type:$(this).find(".unitPrincipalType").val(),
                    phone:$(this).find(".unitPrincipalPhone").val(),
                    remark:$(this).find(".unitPrincipalRemark").val()
                }
                opUnitPrincipalDtos.push(params);
            });
            var url = _contextPath + '/operation/unit/manage/page/updateUnitById';
            let unitParam = $('#unitAddForm').serialize();
            unitParam = unitParam + "&unit.unitType=" + $("#unitType").val();
            $.ajax({
                url: url,
                type: 'POST',
                data: unitParam + '&' + "unit.id=" + $(this).attr("data") + '&opUnitPrincipalDtos=' + JSON.stringify(opUnitPrincipalDtos),
                success: function (res) {
                    if (res.success) {
                        spop({
                            template: "编辑成功",
                            position: 'top-center',
                            autoclose: 3000,
                            style: 'success'
                        });
                        window.setTimeout(function () {
                            $('#createUnit').addClass('hide');
                            $("#unitListContainer").removeClass('hide');
                            $('#unitAccounts').load(_contextPath + '/operation/unit/manage/page/unitList?parentId=' + ztreeId);
                        }, 3000);
                    } else {
                        spop({
                            template: "编辑失败",
                            position: 'top-center',
                            autoclose: 3000,
                            style: 'error'
                        });
                    }
                }

            })
        }

        //试用按钮
        function tryUseRadio() {
            $("#permanent").prop("disabled", true);
            $("#custom").prop("checked", true);
            $("#timeDiv").removeClass("hide");
            $('#expireTime').attr('disabled', false);
            /**
             * 对合同编号的操作
            **/
            $("#contractNumber").addClass('hide');
            $("#contractNumberVal").val('');
            $('#unitAddForm').bootstrapValidator('removeField','unitExtension.contractNumber');
            /**
             * 对单位联系人的操作
             **/
            $("#unitPrincipal").addClass('hide');
            $(".tr_del").each(function () {
                delete_row($(this));
            });
        }

        //正式按钮
        function formalRadio() {
            if ($("#tryUse").prop("disabled") == true) {
                return;
            }
            $("#permanent").prop("disabled", false);
            $("#contractNumber").removeClass('hide');
            $('#unitAddForm').bootstrapValidator('addField','unitExtension.contractNumber',{
                validators: {
                    regexp: {
                        regexp: /^[-()a-zA-Z0-9]+$/,
                        message: "合同编号 只能为数字，英文字母、短划线、括号"
                    },
                    notEmpty: {
                        message: "请输入正确的合同编号"
                    },
                    stringLength: {
                        max: 50,
                        message: "合同编号过长 最大50个字符"
                    }
                }
            });
            $("#unitPrincipal").removeClass('hide');
        }

        //永久按钮
        function empty() {
            $("#expireTime").val("");
            $("#timeDiv").addClass("hide");
            $('#expireTime').attr('disabled', true)
        }

        //自定义按钮 显示时间框
        function showTime() {
            $("#expireTime").val('<#if unitExtension.expireTime??>${unitExtension.expireTime?string("yyyy/MM/dd")}</#if>')
            $("#timeDiv").removeClass("hide");
            $('#expireTime').attr('disabled', false)
        }

        //下一步按钮函数
        function turnToNext() {
            if (!doCheckValue()) {
                return;
            }
            var url = _contextPath + '/operation/unit/manage/page/isUniqueUnicode';
            var unicodeParams = {
                parentUnitId: $("#parentId").val(),
                unitType: $("#unitType").val(),
                fullCode: $("#regionCode").val()
            }
            if ($('#unitType').val() == '2' && $("#titleConvert").html() == '新增单位') {
                $.get(url, unicodeParams, function (result) {
                    if (result.success) {
                        $("#createUnit").addClass("hide")
                        $("#unitAuthorizationContainer").removeClass("hide")
                        $("#unitAuthorizationContainer").load(_contextPath + '/operation/unit/manage/page/authorize?unitId=${unit.id!}');
                    } else {
                        opLayer.error(result.message, "警告");
                    }
                });
            } else {
                $("#createUnit").addClass("hide")
                $("#unitAuthorizationContainer").removeClass("hide")
                $("#unitAuthorizationContainer").load(_contextPath + '/operation/unit/manage/page/authorize?unitId=${unit.id!}');
            }
        }

        /**
         * 验证BootstrapValidator参数
         */
        function doCheckValue() {
            $("#unitAddForm").data("bootstrapValidator").validate();//手动触发全部验证
            return $("#unitAddForm").data("bootstrapValidator").isValid();
        }

        //动态检验所有参数
        function initBootstrapValidator() {
            $('#unitAddForm').bootstrapValidator({
                excluded:[":disabled"],
                feedbackIcons: {
                    // valid: 'glyphicon glyphicon-ok',
                    // invalid: 'glyphicon glyphicon-remove',
                    validating: 'glyphicon /*glyphicon-refresh*/'
                },
                fields: {
                    <#if titleName=='新增单位'>
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
                                url: _contextPath + '/operation/unit/manage/page/isUniqueUsername',
                                data: {"username": $('#username').val()},
                                type: 'GET',
                                message: "用户名已存在"
                            }
                        }
                    },
                    </#if>
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
                            remote: {
                                url: _contextPath + '/operation/unit/manage/page/checkUnitName',
                                data: function () {
                                    return {
                                        "unitName": $("#unitName").val(),
									<#if titleName!='新增单位'>
									"unitId": $("#toConfirm").attr("data")
                                    </#if>
                                    }
                                },
                                type: 'GET',
                                message: "单位名称已存在"
                            }
                            <#if titleName=='新增单位'>
                            , callback: {
                                callback: function (value, validator, $filed) {
                                    validator.updateStatus('username', 'VALIDATING', null)
                                    return true;
                                },
                                message: "请检查用户名"
                            }
                            </#if>
                        }
                    },
                <#if unit.parentId??>
                    <#if unit.parentId!='00000000000000000000000000000000'&&unit.parentId!=''>
                    "unit.parentId": {
                        validators: {
                            notEmpty: {
                                message: "请选择上级单位"
                            },
                            callback: {
                                callback: function (value, validator, $field) {
                                    validator.updateStatus('unit.regionCode', 'VALIDATING', null);
                                    return true;
                                }
                            }
                        }
                    },
                    </#if>
                </#if>
                    "unit.regionCode": {
                        validators: {
                            notEmpty: {
                                message: "请选择行政区划"
                            }
                        }
                    },
                    "unitExtension.expireTimeType": {
                        validators: {
                            callback: {
                                message: "",
                                callback: function (value, validator, $field) {
                                    if ($('.expire-time-type[value="1"]').is(':checked')) {
                                        validator.updateStatus('unitExtension.expireTime', 'VALIDATING', null);
                                        return true;
                                    } else {
                                        return true;
                                    }
                                }
                            }
                        }
                     },
                    "unitExtension.contractNumber":{
                        validators: {
                            regexp: {
                                regexp: /^[-()a-zA-Z0-9]+$/,
                                message: "合同编号 只能为数字，英文字母、短划线、括号"
                            },
                            notEmpty: {
                                message: "请输入正确的合同编号"
                            },
                            stringLength: {
                                max: 11,
                                message: "合同编号过长 最大11个字符"
                            }
                        }
                    },
                    "unitExtension.expireTime": {
                        trigger: 'change',
                        validators: {
                            notEmpty:{
                                message: "请选择到期时间"
                            }
                        }
                    },
                    "school.englishName": {
                        validators: {
                            regexp: {
                                regexp: /^[a-zA-Z0-9\s\']+$/,
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
                    "school.greenArea": {
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
                    "school.postalCode": {
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
                                regexp: /^[0-9]{4}\-[0-9]{7,8}$/,
                                message: "联系电话不合法 例：0571-11111111"
                            }
                        }
                    },
                    "school.fax": {
                        validators: {
                            stringLength: {
                                max: 30,
                                message: "传真过长 最大30个字符"
                            },
                            regexp: {
                                regexp: /^(\d{3,4}-)?\d{7,8}$/,
                                message: "传真电话不合法 如55666857"
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
                    "school.introduction": {
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

        //时间组件js 自定义时间 开始时间要大于当前时间
        $('.datetimepickerEx').datepicker({
            language: 'zh-CN',
            autoclose: true,
            todayHighlight: true,
            format: 'yyyy/mm/dd',
            startDate: new Date()
        }).next().on('click', function () {    //点击图标显示日历
            $(this).prev().focus();
        });
        //学校创建时间时间组件
        $('.datetimepickers').datepicker({
            language: 'zh-CN',
            autoclose: true,
            todayHighlight: true,
            format: 'yyyy/mm/dd'
        }).next().on('click', function () {    //点击图标显示日历
            $(this).prev().focus();
        });

        //模糊下拉框
        function selectChosen() {
            if ($('.chosen-select')) {
                $('.chosen-select').chosen({
                    allow_single_deselect: true,
                    disable_search_threshold: 10,
                    no_results_text: "未找到",
                });
            }
        }

        function insert_row(name,type,phone,remark){
            var n=$(".opUnitPrincipalDto").length+1;
            if (n==4){
                return;
            }
            var tr = '<tr class="opUnitPrincipalDto" data='+n+'>\n' +
                '            <td><input type="text" class="form-control unitPrincipalRealName" name="unitPrincipalRealName['+(n-1)+']" value="'+name+'"></td>\n' +
                '            <td>\n' +
                '            <select class="form-control unitPrincipalType" name="" id="">\n' +
                '            <option value="0">负责人</option>\n' +
                '            <option value="1">销售</option>\n' +
                '            <option value="2">用户</option>\n' +
                '            </select>\n' +
                '            </td>\n' +
                '            <td><input type="text" class="form-control unitPrincipalPhone" name="unitPrincipalPhone['+(n-1)+']" value="'+phone+'"></td>\n' +
                '            <td><input type="text" class="form-control unitPrincipalRemark" name="unitPrincipalRemark['+(n-1)+']" value="'+remark+'"></td>\n' +
                '            <td>\n' +
                '            <a class="table-btn color-blue tr_del" href="#" onclick="delete_row(this)">删除</a>\n' +
                '            </td>\n' +
                '            </tr>'
            $('#add_tr').append(tr);
            $('#add_tr').find('unitPrincipalType').eq(n).val(type);
            $('#unitAddForm').bootstrapValidator('addField','unitPrincipalRealName['+(n-1)+']',{
                trigger: 'keyup',
                validators: {
                    notEmpty: {
                        message: '负责人姓名不能为空'
                    },
                    stringLength: {
                        max: 32,
                        message: "用户名过长 最大32个字符"
                    }
                }
            });
            $('#unitAddForm').bootstrapValidator('addField','unitPrincipalPhone['+(n-1)+']',{
                trigger: 'keyup',
                validators: {
                    notEmpty: {
                        message: '联系电话不能为空'
                    },
                    regexp: {
                        regexp: /^1[34578]\d{9}$/,
                        message: "联系电话不合法 例：15715861763"
                    }
                }
            });
            $('#unitAddForm').bootstrapValidator('addField','unitPrincipalRemark['+(n-1)+']',{
                trigger: 'keyup',
                validators: {
                    notEmpty: {
                        message: '备注信息不能为空'
                    },
                    chineseStringLength: {
                        max: 100,
                        message: "备注过长 最大100个英文字符或50个中文字符"
                    }
                }
            });
        }
        function delete_row(obj){
            var n = $(obj).parents('tr').attr('data')-1;
            /**
             * 动态删除时无法进行bootstrapValidator验证，模拟数据使其正常后再删除
             */
            $(obj).parents('tr').find('.unitPrincipalRealName').val('张三').keyup();
            $(obj).parents('tr').find('.unitPrincipalRemark').val('验证专用').keyup();
            $(obj).parents('tr').find('.unitPrincipalPhone').val('18800000000').keyup();
            $('#unitAddForm').bootstrapValidator('removeField','unitPrincipalRealName['+n+']');
            $('#unitAddForm').bootstrapValidator('removeField','unitPrincipalPhone['+n+']');
            $('#unitAddForm').bootstrapValidator('removeField','unitPrincipalRemark['+n+']');
            $(obj).parents('tr').remove();
        }
    </script>
