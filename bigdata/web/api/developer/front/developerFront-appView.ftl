<div class="row dev-app-box ">
    <div class="col-xs-12">
        <!-- PAGE CONTENT BEGINS -->
        <div class="box box-default">
            <div class="box-body app-form-box">
                <form class="form-horizontal margin-10" role="form" method="post" id="appForm"
                      enctype="multipart/form-data">
                    <div class="form-group deve-from-title">
                        <label class="col-sm-2 control-title no-padding frombold" for="form-field-1">基本信息</label>
                    </div>
                    <div class="form-group">
                        <label for="form-field-4" class="col-sm-2 control-label no-padding"><font
                                style="color:red;">*</font>&nbsp;应用名称</label>
                        <div class="col-sm-6">
                            <input disabled maxlength="12" type="text" name="name" value="${app.name!}" id="form-field-4"
                                   class="form-control">
                            <span class="control-disabled">不超过12个字符</span>
                        </div>
                        <span class="col-sm-4 form-tips form-tips-warning hide" id="tip_name"></span>
                    </div>

                    <div class="form-group">
                        <label for="form-field-5" class="col-sm-2 control-label no-padding"><font
                                style="color:red;">*</font>&nbsp;应用介绍</label>
                        <div class="col-sm-6">
                            <textarea disabled style="height: 120px;" name="description" maxlength="256" id="form-field-5"
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
                                <li class="">
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
                            <input disabled type="text" maxlength="128" name="indexUrl" value="${app.indexUrl!}"
                                   class="form-control">
                        </div>
                        <span class="col-sm-4 form-tips form-tips-warning hide" id="tip_indexUrl"></span>
                    </div>

                    <div class="form-group">
                        <label for="form-field-10" class="col-sm-2 control-label no-padding">退出回调</label>
                        <div class="col-sm-6">
                            <input disabled type="text" maxlength="128" name="invalidateUrl" value="${app.invalidateUrl!}"
                                   class="form-control">
                        </div>
                        <div class="col-sm-4 control-tips"></div>
                    </div>
                    <div class="form-group">
                        <label for="form-field-10" class="col-sm-2 control-label no-padding">登录回调</label>
                        <div class="col-sm-6">
                            <input disabled type="text" maxlength="128" name="verifyUrl" value="${app.verifyUrl!}"
                                   class="form-control">
                        </div>
                        <div class="col-sm-4 control-tips"></div>
                    </div>

                    <div class="form-group deve-from-title">
                        <label class="col-sm-2 control-title no-padding frombold">适用信息</label>
                    </div>


                   <div class="form-group">
                        <label for="form-field-12"
                               class="col-sm-2 control-label no-padding"><font
                                style="color:red;">*</font>&nbsp;应用用途</label>
                        <div class="col-sm-6">
                            <label class="inline">
                                <input type="checkbox" name="appTypes" value="1" class="wp appTypes"
                                <#if app.docking>checked</#if> disabled>
                                <span class="lbl">单点对接</span>
                            </label>
                            <label class="inline">
                                <input type="checkbox" name="appTypes" value="2" class="wp appTypes"
                                <#if app.applyApi>checked</#if> disabled>
                                <span class="lbl">接口申请</span>
                            </label>
                        </div>
                        <span class="col-sm-4 form-tips form-tips-warning hide" id="tip_unitClasses"></span>
                    </div>


                    <div class="form-group">
                        <label for="form-field-12"
                               class="col-sm-2 control-label no-padding"><font
                                style="color:red;">*</font>&nbsp;适用单位</label>
                        <div class="col-sm-6">
                            <label class="inline">
                                <input type="checkbox" name="unitClasses" value="1" class="wp unitClasses"
                                <#if app.education>checked</#if> disabled>
                                <span class="lbl">教育局</span>
                            </label>
                            <label class="inline">
                                <input type="checkbox" name="unitClasses" value="2" class="wp unitClasses"
                                <#if app.school>checked</#if> disabled>
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
                               <#if app.student>checked</#if> disabled>
                                <span class="lbl">学生</span>
                            </label>
                            <label class="inline">
                                <input type="checkbox" name="userTypes" value="2" class="wp userTypes"
                               <#if app.teacher>checked</#if> disabled>
                                <span class="lbl">教师</span>
                            </label>
                            <label class="inline">
                                <input type="checkbox" name="userTypes" value="3" class="wp userTypes"
                               <#if app.family>checked</#if> disabled>
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
                               <#if app.section0>checked</#if> disabled>
                                <span class="lbl">幼儿园</span>
                            </label>
                            <label class="inline">
                                <input type="checkbox" name="sections" value="1" class="wp sections"
                               <#if app.section1>checked</#if> disabled>
                                <span class="lbl">小学</span>
                            </label>
                            <label class="inline">
                                <input type="checkbox" name="sections" value="2" class="wp sections"
                               <#if app.section2>checked</#if> disabled>
                                <span class="lbl">初中</span>
                            </label>
                            <label class="inline">
                                <input type="checkbox" name="sections" value="3" class="wp sections"
                               <#if app.section3>checked</#if> disabled>
                                <span class="lbl">高中</span>
                            </label>
                        </div>
                        <span class="col-sm-4 form-tips form-tips-warning hide" id="tip_sections"></span>
                    </div>


                    <div class="form-group">
                        <label class="col-sm-2 control-label no-padding" for="form-field-1"></label>

                        <div class="col-sm-6">
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
    $('#app-cancel').click(function () {
        router.go({
            path: '/bigdata/api/developer/front/app/index',
            name: '应用管理',
            type: 'item',
            level: 0
        }, function () {
            $('.page-content').load('${springMacroRequestContext.contextPath}/bigdata/api/developer/front/app/index');
        });
    })
</script>