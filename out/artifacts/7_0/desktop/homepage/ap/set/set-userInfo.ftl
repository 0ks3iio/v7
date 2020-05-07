<#assign USER_LAYOUT_TWO2ONE =stack.findValue("@net.zdsoft.desktop.entity.UserSet@LAYOUT_TWO2ONE") />
<#assign USER_LAYOUT_DEFAULT =stack.findValue("@net.zdsoft.desktop.entity.UserSet@LAYOUT_DEFAULT") />
<div class="main-content-inner">
    <div class="page-content">
        <div class="row">
            <div class="col-xs-12">
                <!-- PAGE CONTENT BEGINS -->
                <div class="box box-default">
                    <div class="box-body">
                        <ul class="nav nav-tabs" role="tablist">
                        <#if  binding?default(false) >
                            <li role="presentation" class="active">
                                <a href="#zz" role="tab" data-toggle="tab">绑定帐号</a>
                            </li>
                        </#if>
                        <#if ownerType!=1>
                            <li role="presentation" <#if  binding?default(false) ><#else>class="active"</#if>>
                                <a href="#aa" role="tab" data-toggle="tab">个人信息</a>
                            </li>
                        </#if>
                            <#if updatePassword?default(true)>
                            <li role="presentation" <#if ownerType==1>class="active" </#if>>
                                <a href="#bb" role="tab" data-toggle="tab">修改密码</a>
                            </li>
                            </#if>
                            <li role="presentation" class="<#if !updatePassword?default(true)&&!binding?default(false)&&ownerType==1>active</#if>">
                                <a href="#cc" role="tab" data-toggle="tab">修改头像</a>
                            </li>
                        <#--<li role="presentation">-->
                        <#--<a href="#ee" role="tab" data-toggle="tab">系统设置</a>-->
                        <#--</li>										-->
                        <#if layoutUserSet?default(false)>
                            <li role="presentation">
                                <a href="#dd" role="tab" data-toggle="tab">布局设置</a>
                            </li>
                        </#if>

                        <#if relieveQQ?default(false)>
                            <li role="presentation">
                                <a href="#ff" role="tab" data-toggle="tab">qq和微信设置</a>
                            </li>
                        </#if>
                        
                        <#if  showUserBing?default(false) >
                            <li role="presentation">
                                <a href="#hh" role="tab" data-toggle="tab">用户绑定</a>
                            </li>
                        </#if>

                        </ul>
                        <!-- Tab panes -->
                        <div class="tab-content" id="userInfo">
                        <#if  binding?default(false) >
                            <div role="tabpanel" class="tab-pane active" id="zz">
                                <div class="row">
                                    <#if ownerType==1>
                                        <#list studentList as student >
                                            <div class="col-sm-4">
                                                <div class="box-usebind">
                                                    <div class="box-usebind-header">
                                                        <label><input type="radio" class="wp" name="bindingId"
                                                                      value="${student.id!}"><span
                                                                class="lbl"> 选择</span></label>
                                                    </div>
                                                    <div class="box-usebind-body">
                                                        <ul>
                                                            <li><span>姓名：</span>${student.studentName!}</li>
                                                            <li><span>学校：</span>${student.schoolName!}</li>
                                                            <li><span>班级：</span>${student.className!}</li>
                                                            <li>
                                                                <span>性别：</span>${mcodeSetting.getMcode("DM-XB","${student.sex!}")}
                                                            </li>
                                                        </ul>
                                                    </div>
                                                </div>
                                            </div>
                                        </#list>
                                    <#elseif ownerType==2>
                                        <#list teacherList as teacher >
                                            <#assign mobilePhone = teacher.mobilePhone>
                                            <div class="col-sm-4">
                                                <div class="box-usebind">
                                                    <div class="box-usebind-header">
                                                        <label><input type="radio" class="wp" name="bindingId"
                                                                      value="${teacher.id!}"><span
                                                                class="lbl"> 选择</span></label>
                                                    </div>
                                                    <div class="box-usebind-body">
                                                        <ul>
                                                            <li><span>姓名：</span>${teacher.teacherName!}</li>
                                                            <li><span>单位：</span>${teacher.unitName!}</li>
                                                            <li><span>部门：</span>${teacher.deptName!}</li>
                                                            <li>
                                                                <span>性别：</span>${mcodeSetting.getMcode("DM-XB","${teacher.sex!}")}
                                                            </li>
                                                            <li><span>身份证号：</span>${teacher.identityCard!}</li>
                                                            <li><span>手机号：</span>${teacher.mobilePhone!}</li>
                                                        </ul>
                                                    </div>
                                                </div>
                                            </div>
                                        </#list>
                                    <#elseif ownerType==3>
                                        <#list familyList as family >
                                            <#assign mobilePhone = family.mobilePhone>
                                            <div class="col-sm-4">
                                                <div class="box-usebind">
                                                    <div class="box-usebind-header">
                                                        <label><input type="checkbox" class="wp" name="bindingId"
                                                                      value="${family.id!}"><span class="lbl"> 选择</span></label>
                                                    </div>
                                                    <div class="box-usebind-body">
                                                        <ul>
                                                            <li>
                                                                <span>角色：</span>${family.studentName!}的${mcodeSetting.getMcode("DM-GX","${family.relation!}")}
                                                            </li>
                                                            <li><span>姓名：</span>${family.realName!}</li>
                                                            <li><span>学校：</span>${family.schoolName!}</li>
                                                            <li>
                                                                <span>性别：</span>${mcodeSetting.getMcode("DM-XB","${family.sex!}")}
                                                            </li>
                                                            <li><span>身份证号：</span>${family.identityCard!}</li>
                                                            <li><span>手机号：</span>${family.mobilePhone!}</li>
                                                        </ul>
                                                    </div>
                                                </div>
                                            </div>
                                        </#list>
                                    </#if>
                                </div>
                                <form class="form-horizontal margin-10" role="form">
                                    <div class="form-group">
                                        <label class="col-sm-2 control-label no-padding-right">*&nbsp;验证码：</label>
                                        <div class="col-sm-4">
                                            <input type="text" id="imgCode" name="imgCode" class="form-control">
                                        </div>
                                        <div class="col-sm-4">
                                            <a href="javascript:void(0)" onclick="flashCodeImg()"
                                               class="verCode-img"><img id="codeImg"
                                                                        src="${request.contextPath}/desktop/user/verifyQuestionImage?_${.now}"
                                                                        alt=""></a>
                                        </div>
                                    </div>
                                    <#if ownerType==1>
                                        <div class="form-group">
                                            <label class="col-sm-2 control-label no-padding-right">*&nbsp;身份证号码：</label>
                                            <div class="col-sm-4">
                                                <input class="form-control" type="text" id="msgCode" name="msgCode">
                                            </div>
                                        </div>
                                    <#else>
                                        <div class="form-group">
                                            <label class="col-sm-2 control-label no-padding-right">*&nbsp;短信验证码：</label>
                                            <div class="col-sm-4">
                                                <input class="form-control" type="text" id="msgCode" name="msgCode">
                                            </div>
                                            <div class="col-sm-4">
                                                <button type="button" class="btn get-code-disable btn-lightblue"
                                                        id="btnSendCode">获取短信验证码
                                                </button>
                                            </div>
                                        </div>
                                    </#if>
                                    <div class="form-group">

                                        <div class="col-sm-6 col-sm-offset-2">
                                            <button type="button" class="btn btn-long btn-blue" onclick="bindingSave()">
                                                立即绑定
                                            </button>
                                        </div>
                                    </div>
                                </form>

                            </div>
                        </#if>
                        <#if ownerType!=1>
                            <div role="tabpanel" class="tab-pane <#if  binding?default(false) ><#else>active</#if>"
                                 id="aa">
                                <form class="form-horizontal margin-10" role="form">
                                	<div class="form-group">
                                        <label class="col-sm-2 control-label no-padding">&nbsp;用户名</label>
                                        <div class="col-sm-6">
                                        ${username!}
                                        </div>
                                        <div class="col-sm-4 control-tips"></div>
                                    </div>
                                    <div class="form-group">
                                        <label class="col-sm-2 control-label no-padding">*&nbsp;姓名</label>
                                        <div class="col-sm-6">
                                            <input name="realName" msgName="姓名" nullable="false" type="text"
                                                   id="realName" class="form-control" length="20" value="${realName!}">
                                        </div>
                                        <div class="col-sm-4 control-tips"></div>
                                    </div>
                                    <div class="form-group">
                                        <label class="col-sm-2 control-label no-padding">*&nbsp;手机号码</label>
                                        <div class="col-sm-6">
                                            <input name="mobilePhone" msgName="手机号码" nullable="false" id="mobilePhone"
                                                   type="text" class="form-control" length="20" value="${mobilePhone!}">
                                        </div>
                                        <div class="col-sm-4 control-tips"></div>
                                    </div>
									<#if ownerType == 3>
									<div class="form-group">
                                        <label class="col-sm-2 control-label no-padding">&nbsp;关联学生姓名</label>
                                        <div class="col-sm-6">
                                        ${stuName!}
                                        </div>
                                        <div class="col-sm-4 control-tips"></div>
                                    </div>
                                    <div class="form-group">
                                        <label class="col-sm-2 control-label no-padding">&nbsp;学生性别</label>
                                        <div class="col-sm-6">
                                        ${mcodeSetting.getMcode('DM-XB',sex)!}
                                        </div>
                                        <div class="col-sm-4 control-tips"></div>
                                    </div>
                                    <div class="form-group">
                                        <label class="col-sm-2 control-label no-padding">&nbsp;学生班级</label>
                                        <div class="col-sm-6">
                                        ${clsName!}
                                        </div>
                                        <div class="col-sm-4 control-tips"></div>
                                    </div>
                                    <div class="form-group">
                                        <label class="col-sm-2 control-label no-padding">&nbsp;学生证件类型</label>
                                        <div class="col-sm-6">
                                        ${mcodeSetting.getMcode('DM-SFZJLX',identitycardType)!}
                                        </div>
                                        <div class="col-sm-4 control-tips"></div>
                                    </div>
                                    <div class="form-group">
                                        <label class="col-sm-2 control-label no-padding">&nbsp;学生证件号</label>
                                        <div class="col-sm-6">
                                        ${identityCard!}
                                        </div>
                                        <div class="col-sm-4 control-tips"></div>
                                    </div>
                                    <div class="form-group">
                                        <label class="col-sm-2 control-label no-padding">&nbsp;学生用户名</label>
                                        <div class="col-sm-6">
                                        ${stuusername!}
                                        </div>
                                        <div class="col-sm-4 control-tips"></div>
                                    </div>
                                    <#elseif ownerType == 2>
                                    	<div class="form-group">
	                                        <label class="col-sm-2 control-label no-padding">&nbsp;所在部门</label>
	                                        <div class="col-sm-6">
	                                        ${deptName!}
	                                        </div>
	                                        <div class="col-sm-4 control-tips"></div>
	                                    </div>
	                                    <div class="form-group">
	                                        <label class="col-sm-2 control-label no-padding" style="margin-top:5px">*&nbsp;性别</label>
	                                        <div class="col-sm-6">
	                                            <label class="inline">
	                                                <input type="radio" class="wp" name="sex" value="1"/>
	                                                <span class="lbl"> 男</span>
	                                            </label>
	                                            <label class="inline">
	                                                <input type="radio" class="wp" name="sex" value="2"/>
	                                                <span class="lbl"> 女</span>
	                                            </label>
	                                        </div>
	                                        <div class="col-sm-4 control-tips"></div>
	                                    </div>
	                                    <div class="form-group">
	                                        <label class="col-sm-2 control-label no-padding">*&nbsp;身份证号</label>
	                                        <div class="col-sm-6">
	                                            <input name="identityCard" msgName="身份证号" nullable="false" id="identityCard"
	                                                   type="text" class="form-control" value="${identityCard!}">
	                                        </div>
	                                        <div class="col-sm-4 control-tips"></div>
	                                    </div>
	                                    <#if haikou?default(false)>
	                                    <div class="form-group">
			                                <label class="col-sm-2 control-label no-padding">出生日期：</label>
			                                <div class="col-sm-6">
				                                <div class="input-group">
					                                  <input type="text" class="form-control datepicker" name="birthday" id="birthday" placeholder="出生日期" value="${(birthday?string('yyyy-MM-dd'))?if_exists}">
								                      <span class="input-group-addon">
									                         <i class="fa fa-calendar"></i>
								                      </span>
				                                </div>
			                                </div>
		                                </div>
		                                </#if>
	                                    <div class="form-group">
	                                        <label class="col-sm-2 control-label no-padding">&nbsp;民族</label>
	                                        <div class="col-sm-6">
	                                        	<div class="filter-content">
												<select name="nation" id="nation" class="form-control">
													${mcodeSetting.getMcodeSelect('DM-MZ',nation,'1')}
												</select>
												</div>
	                                        </div>
	                                        <div class="col-sm-4 control-tips"></div>
	                                    </div>
	                                    <div class="form-group">
	                                        <label class="col-sm-2 control-label no-padding">&nbsp;政治面貌</label>
	                                        <div class="col-sm-6">
	                                        	<div class="filter-content">
												<select name="polity" id="polity" class="form-control">
													${mcodeSetting.getMcodeSelect('DM-ZZMM',polity,'1')}
												</select>
												</div>
	                                        </div>
	                                        <div class="col-sm-4 control-tips"></div>
	                                    </div>
									</#if>
									<#if ownerType == 2 && updateInfo == '0'>
									<#else>
                                    <div class="form-group">
                                        <label class="col-sm-2 control-label no-padding"></label>
                                        <div class="col-sm-6">
                                            <input type="button" class="btn btn-blue save" value="保存"/>
                                        </div>
                                        <div class="col-sm-4 control-tips">
                                        </div>
                                    </div>
                                    </#if>
                                </form>
                            </div>
                        </#if>
                            <#if updatePassword?default(true)>
                            <div role="tabpanel" class="tab-pane <#if ownerType==1>active</#if> " id="bb">
                                <form class="form-horizontal margin-10" role="form">
                                   <#if isTianChang?default(false)>
                                      <span class="input-group-addon">
	                                                                                              因本平台和“安徽省安徽基础教育资源应用平台”保持账号数据同步，所以在此不提供密码修改，如需修改密码，请登录
	                                      <a href='http://www.ahedu.cn/SNS/index.php?app=public&mod=Account&act=security 
	                                      '>安徽基础教育资源应用平台-个人空间 </a>进行修改，谢谢配合。                                                    
                                      </span>
                                   <#else>
                                      <div class="form-group">
                                        <label class="col-sm-2 control-label no-padding">*&nbsp;原有密码</label>
                                        <div class="col-sm-6">
                                            <input msgName="原密码" nullable="false" id="password" name="password"
                                                   type="password" class="form-control old_password">
                                        </div>
                                        <span class="old_err" style="color: red;"></span>
                                        <div class="col-sm-4 control-tips"></div>
                                      </div>
                                      <div class="form-group">
                                        <label class="col-sm-2 control-label no-padding">*&nbsp;新密码</label>
                                        <div class="col-sm-6">
                                            <input msgName="新密码" nullable="false" id="newPassword" name="newPassword"
                                                   type="password" class="form-control new_password">
                                        </div>
                                        <span class="new_err"></span>
                                        <div class="col-sm-4 control-tips"></div>
                                    </div>
                                    <div class="form-group">
                                        <label class="col-sm-2 control-label no-padding">*&nbsp;确认密码</label>
                                        <div class="col-sm-6">
                                            <input msgName="确认密码" nullable="false" id="cfNewPassword"
                                                   name="cfNewPassword" type="password"
                                                   class="form-control confirm_password">
                                        </div>
                                        <span class="confirm_err" style="color: red;"></span>
                                        <div class="col-sm-4 control-tips"></div>
                                    </div>
                                    <div class="form-group">
                                        <label class="col-sm-2 control-label no-padding"></label>

                                        <div class="col-sm-6">
                                            <input type="button" class="btn btn-blue save" value="保存"/>

                                        </div>
                                        <div class="col-sm-4 control-tips">
                                        </div>
                                    </div>
                                   </#if>
                                </form>
                            </div>
                            </#if>
                            <div role="tabpanel" class="tab-pane <#if !updatePassword?default(true)&&!binding?default(false)&&ownerType==1>active</#if>" id="cc" style="border-bottom: solid 1px #dbdbdb;">

                            </div>
                            <div role="tabpanel" class="tab-pane" id="ee">
                                <form class="form-horizontal margin-10" role="form">
                                    <div class="form-group">
                                        <label class="col-sm-2 control-title no-padding">登录</label>
                                    </div>
                                    <div class="form-group">
                                        <label class="col-sm-2 control-label no-padding"></label>
                                        <div class="col-sm-6">
                                            <label class="inline">
                                                <input type="checkbox" class="wp" name="checkbox"/>
                                                <span class="lbl"> 登录时消息提示</span>
                                            </label>
                                        </div>
                                    </div>
                                    <div class="form-group">
                                        <label class="col-sm-2 control-title no-padding">基本信息</label>
                                    </div>
                                    <div class="form-group">
                                        <label class="col-sm-2 control-label no-padding"></label>
                                        <div class="col-sm-6">
                                            <label class="inline">
                                                <input type="checkbox" class="wp" name="checkbox"/>
                                                <span class="lbl"> 登录时消息提示</span>
                                            </label>
                                        </div>
                                        <div class="col-sm-4 control-tips"></div>
                                    </div>
                                    <div class="form-group">
                                        <label class="col-sm-2 control-label no-padding"></label>
                                        <div class="col-sm-6">
                                            <label class="inline">
                                                <input type="checkbox" class="wp" name="checkbox"/>
                                                <span class="lbl"> 登录时消息提示</span>
                                            </label>
                                        </div>
                                        <div class="col-sm-4 control-tips"></div>
                                    </div>
                                    <div class="form-group">
                                        <label class="col-sm-2 control-label no-padding"></label>
                                        <div class="col-sm-6">
                                            <br>
                                            <input type="button" class="btn btn-blue save" value="保存"/>
                                            <input type="button" class="btn btn-blue cancel" value="取消"/>
                                        </div>
                                        <div class="col-sm-4 control-tips">
                                        </div>
                                    </div>
                                </form>
                            </div>
                            <div role="tabpanel" class="tab-pane" id="dd">
                                <form class="form-horizontal margin-10" role="form">
                                <#--<div class="form-group">-->
                                <#--<label class="col-sm-2 control-title no-padding">选择布局</label>				-->
                                <#--</div>													-->
                                    <div class="form-group">
                                        <label class="col-sm-1 control-label no-padding"></label>
                                        <div class="col-sm-11">
                                            <ul class="layout-list clearfix">
                                                <li data-action="select"
                                                    <#if layout?default('') == USER_LAYOUT_TWO2ONE >class="selected"</#if>>
                                                    <span class="layout-img layout-twoToOne"></span>
                                                    <input type="hidden" value="${USER_LAYOUT_TWO2ONE!}"/>
                                                    <h5 class="layout-name">两列 2:1</h5>
                                                </li>
                                                <li data-action="select"
                                                    <#if layout?default('') == USER_LAYOUT_DEFAULT >class="selected"</#if>>
                                                    <span class="layout-img layout-twoEqual"></span>
                                                    <input type="hidden" value="${USER_LAYOUT_DEFAULT!}"/>
                                                    <h5 class="layout-name">默认平铺</h5>
                                                </li>
                                            </ul>
                                        </div>
                                    </div>
                                    <div class="form-group">
                                        <label class="col-sm-1 control-label no-padding"></label>
                                        <div class="col-sm-11">
                                            <br>
                                            <input type='button' class="btn btn-blue" id="application" value="应用"/>
                                        </div>
                                        <div class="col-sm-4 control-tips">
                                        </div>
                                    </div>
                                </form>
                            </div>
                            <div role="tabpanel" class="tab-pane" id="ff">
                                <form class="form-horizontal margin-10" role="form">
                                    <div class="form-group">
                                        <label class="col-sm-2 control-title no-padding">qq和微信设置</label>

                                    </div>
                                    <div class="login-other form-group">
                                        <a class="login-qq">&nbsp;Q&nbsp;Q</a>
                                        <input type="hidden" value="2" id="qq">
                                        <div class="col-sm-4 control-tips"></div>
                                        <input type='button' class="btn btn-blue relieveQq" value="${isRelieveQQ!}"/>
                                        <span class="confirm_err" style="color: green;"></span>
                                    </div>
                                    <div class="login-other form-group">
                                        <a class="login-wx">&nbsp;微信</a>
                                        <input type="hidden" value="1" id="wx">
                                        <div class="col-sm-4 control-tips"></div>
                                        <input type='button' class="btn btn-blue relieveWeChat"
                                               value="${isRelieveWX!}"/>
                                        <span class="confirm_err" style="color: green;"></span>
                                    </div>

                                </form>
                            </div>
                            
                            <div role="tabpanel" class="tab-pane" id="hh">
                                <input type="hidden" name="remoteUserId" id="remoteUserId" value="${sysUserBind.remoteUserId!}" />
                                <input type="hidden" name="userId" id="userId" value="${userId!}" />
                                <form class="form-horizontal margin-10" role="form">
                                      <div class="form-group">
                                        <label class="col-sm-2 control-label no-padding">*&nbsp;平台账号</label>
                                        <div class="col-sm-6">
                                            <input msgName="原密码" nullable="false" id="bindName" name="bindName" class="form-control" 
                                            value = "${sysUserBind.remoteUsername!}">
                                        </div>
                                        <span class="old_err" style="color: red;"></span>
                                        <div class="col-sm-4 control-tips"></div>
                                      </div>
                                      <div class="form-group">
                                        <label class="col-sm-2 control-label no-padding">*&nbsp;密码</label>
                                        <div class="col-sm-6">
                                            <input msgName="新密码" nullable="false" id="bindPassword" name="bindPassword" class="form-control"
                                            value = "${sysUserBind.remotePassword!}">
                                        </div>
                                        <span class="new_err"></span>
                                        <div class="col-sm-4 control-tips"></div>
                                    </div>
                                    <div class="form-group">
                                        <label class="col-sm-2 control-label no-padding"></label>
                                        <div class="col-sm-6">
                                            <input type="button" class="btn btn-blue save" value="保存"/>
                                            <#if sysUserBind.remoteUserId?default('') != ''>
                                               <input type="button" class="btn btn-blue" value="解绑" onclick="doUnBind()"/>
                                            </#if>
                                        </div>
                                        <div class="col-sm-4 control-tips">
                                        </div>
                                    </div>
                                </form>
                            </div>
                        </div>
                        <script src="${request.contextPath}/static/components/bootstrap-datepicker/dist/js/bootstrap-datepicker.js"></script>
                        <script src="${request.contextPath}/static/components/bootstrap-datepicker/dist/js/bootstrap-datepicker.min.js"></script>
                        <script src="${request.contextPath}/static/components/bootstrap-datepicker/dist/locales/bootstrap-datepicker.zh-CN.min.js"></script>
                        <script>
                            if (typeof(downTime) != "undefined") {
                                window.clearInterval(downTime);
                            }
                            sendcode('#imgCode', '#btnSendCode', 60, 5);
                            $(function () {
                                //datePickerInit();
                                $('.datepicker').datepicker({
		                              language: 'zh-CN',
		                              format: 'yyyy-mm-dd',
		                              autoclose: true
	                            }).next().on('click', function(){
		                              $(this).prev().focus();
	                            }); 
                                $("#cc").load("${request.contextPath}/zdsoft/crop/doPage", function () {
                                    $('.save').click(function () {
                                        saveUserSetting('${request.contextPath?default("")}');
                                    });
                                });
                                $("#cfNewPassword").unbind().bind("change", function () {
                                    verify2Pwd();
                                });
                                $("#password").unbind().bind("change", function () {
                                    verifyPassword('${request.contextPath}');
                                });
                                $("#newPassword").unbind().bind("change", function () {
                                    verify2Pwd();
                                })
                                var sex = "${sex!}";
                                $("input[name='sex'][value='${sex!}']").attr("checked", true);                             
                            });

                            function flashCodeImg() {
                                $("#codeImg").get(0).src = '${request.contextPath}/desktop/user/verifyQuestionImage?time_=' + Math.random();
                            }


                            function doResetUserInfo() {
                                var userName = $("#realName").val();
                                var sex = $("#sex").val();
                                var birthday = $("#birthday").val();
                                var identityCard = $("#identityCard").val();
                                var mobilePhone = $("#mobilePhone").val();

                            }

                            function test() {
                                deskTopCheckVal(".user-setting-info");
                            }

                            // 执行保存操作

                            function saveUserSetting(contextPath) {
                                var user = new Object();
                                user.realName = $("#realName").val();
                                user.sex = $("input[type='radio']:checked").val();
                                user.mobilePhone = $("#mobilePhone").val();
                                <#if ownerType == 2>
                                user.identityCard = $("#identityCard").val();
                                user.polity = $("#polity").val();
                                user.nation = $("#nation").val();
                                </#if>
                                var $active;
                                $("#userInfo").find("div").each(function () {
                                    $this = $(this);
                                    if ($this.hasClass("active")) {
                                        $active = $this;
                                        return;
                                    }
                                });
                                var id = $active.attr("id");
                                var options;
                                //user-info
                                if (id && id == "aa") {
                                    if (!deskTopCheckVal("#" + id)) {
                                        return;
                                    }
                                    var url = "";
                                    <#if haikou?default(false)>
                                         url = contextPath + "/desktop/user/info/reset?birthday="+$("#birthday").val();
                                    <#else>
                                         url = contextPath + "/desktop/user/info/reset";
                                    </#if>
                                    options = {
                                        url: url,
                                        data: JSON.stringify(user),
                                        clearForm: false,
                                        resetForm: false,
                                        dataType: 'json',
                                        type: 'post',
                                        contentType: "application/json",
                                        success: function (data) {
                                            if (data.success) {
                                                showSuccessMsg(data.msg);
                                            } else {
                                                showErrorMsg("更新个人信息失败");
                                            }
                                        }
                                    }
                                }
                                //pwd
                                else if (id == "bb") {
                                    if (!deskTopCheckVal("#" + id)) {
                                        return;
                                    }
                                    if (!verifyPassword(contextPath)) {
                                        return;
                                    }
                                <#--     var np = $("#newPassword").val();
                                     var cp = $("#cfNewPassword").val();
                                     var pw = $("#password").val();
                                     var pass = {
                                      newPwd:(np == null || np == "") ? "" : hex_md5(np)+ hex_sha1(np),
                                      confirmPwd:(cp == null || cp == "") ? "" : hex_md5(cp)+ hex_sha1(cp),
                                      password:(pw == null || pw == "") ? "" : hex_md5(pw)+ hex_sha1(pw),
                                     }
                                 -->
                                    var pass = {
                                        c3: $("#newPassword").val(),
                                        c2: $("#cfNewPassword").val(),
                                        c1: $("#password").val(),
                                    }

                                    options = {
                                        //         url:contextPath+"/desktop/user/pwd/reset?newPwd="+$("#newPassword").val()+"&confirmPwd="+$("#cfNewPassword").val()+"&password="+$("#password").val() ,
                                        url: contextPath + "/desktop/user/pwd/reset",
                                        data: JSON.stringify(pass),
                                        clearForm: false,
                                        resetForm: false,
                                        dataType: 'json',
                                        contentType: "application/json",
                                        type: 'post',
                                        success: function (data) {
                                            if (data.success) {
                                                showSuccessMsg("密码修改成功");
                                            } else {
                                                showErrorMsg(data.msg);
                                            }
                                        }
                                    }
                                }
                                //image
                                else if (id == "cc") {
                                    JsCropUtils.JsCropSave(function (data) {
                                        if (data.success) {
                                            showSuccessMsg(data.msg);
                                        } else {
                                            showErrorMsg(data.msg);
                                        }
                                    });
                                }
                                
                                else if (id == "hh") {
                                	var bindName = $("#bindName").val();
                                	var bindPassword = $("#bindPassword").val();
                                	if(bindName ==''){
                                        layerError("#hh #bindName","平台账号不能为空");
                                        return ;
                                    }else if (bindName.length > 50 ){
                                    	 layerError("#hh #bindName","账号长度不能超过50个字符");
                                         return ;
                                    }
                                	<#if showHkoa?default(false)>
	                            		if(bindPassword==''){
	                            			layerError("#hh #bindPassword","密码不能为空");
	                            			return ;
	                            		}
	                            		if(bindPassword.length >64){
	                            			layerError("#hh #bindPassword","密码长度不能超过64个字符");
	                            			return ;
	                            		}
	                            	</#if>
                                	
                                	var pass = {
                                			c1: $("#remoteUserId").val(),
                                			c2: $("#userId").val(),
                                            c3: $("#bindName").val(),
                                			c4: $("#bindPassword").val(),
                                    }
                                	options = {
                                           url: contextPath + "/desktop/user/bind/save",
                                           data: JSON.stringify(pass),
                                           clearForm: false,
                                           resetForm: false,
                                           dataType: 'json',
                                           contentType: "application/json",
                                           type: 'post',
                                           success: function (data) {
                                               if (data.success) {
                                            	   showSuccessMsgWithCall(data.msg, goHome);
                                               } else {
                                                   showErrorMsg(data.msg);
                                               }
                                           }
                                       }
                                }
                                $.ajax(options);
                            }

                            // 选择
                            $('[data-action=select]').on('click', function () {
                                if ($(this).hasClass('disabled')) {
                                    return false;
                                } else if ($(this).parent().hasClass('multiselect')) {
                                    $(this).toggleClass('selected');
                                } else {
                                    $(this).addClass('selected').siblings().removeClass('selected');
                                }
                            });

                            //应用模式设置
                            $('#application').click(function () {
                                var layout;
                                var $active;
                                $("#dd").find("ul.layout-list").find("li").each(function () {
                                    var $that = $(this);
                                    if ($that.hasClass("selected")) {
                                        $active = $that;
                                        return;
                                    }
                                });

                                layout = $active.children("input").val();
                                $("#breadDiv").removeClass("show");
                                if (!layout) {
                                    showWarnMsg("请至少选择一种布局！");
                                }
                                $.ajax({
                                    url: "${request.contextPath}/desktop/user/layout/save?layout=" + layout,
                                    contentType: "application/json",
                                    dataType: "json",
                                    type: "post",
                                    success: function (data) {
                                        if (data.success) {
                                            showSuccessMsgWithCall("布局修改成功!", goHome);
                                        } else {
                                            showErrorMsg("修改失败，请联系管理员");
                                        }
                                    }
                                })

                            })

                            function sendcode(num, obj, count, n) {
                                $(num).on('keyup focus', function () {
                                    if (!$(obj).hasClass('get-code-in')) {
                                        var imgCode = $("#imgCode").val();
                                        if (imgCode && imgCode.length > 0) {
                                            $(obj).addClass('get-code-able').removeClass('get-code-disable');
                                        } else {
                                            $(obj).removeClass('get-code-able').addClass('get-code-disable');
                                        }
                                        ;
                                    }
                                    ;
                                });


                                //因为ios应用页面是不会刷新的
                                var timeCount = count; //定义倒计时总数
                                var sendTime = n; //发送验证码次数
                                $(obj).attr('data-max', sendTime);
                                $(obj).click(function (e) {
                                    e.preventDefault();
                                    var imgCode = $("#imgCode").val();
                                    if (imgCode && imgCode.length > 0) {
                                    } else {
                                        showErrorMsg("获得短信验证码前,必须填写图片验证码");
                                        return;
                                    }
                                    if ($(this).hasClass('get-code-able')) {
                                        var start_time = new Date();
                                        start_time = start_time.getTime();//获取开始时间的毫秒数
                                        var newTime = parseInt($(obj).attr('data-max') - 1);
                                        if (newTime >= 0) {
                                            $(obj).attr('data-max', newTime).addClass('get-code-disable get-code-in').removeClass('btn-lightblue').removeClass('get-code-able').text(timeCount + "秒后重获取");
                                            $.ajax({
                                                url: '${request.contextPath}/desktop/user/registerMsgCode',
                                                data: {
                                                    "mobilePhone": '${mobilePhone!}',
                                                    "imgCode": $("#imgCode").val()
                                                },
                                                type: 'post',
                                                success: function (data) {
                                                    var jsonO = JSON.parse(data);
                                                    if (jsonO.success) {
                                                        showSuccessMsg(jsonO.msg);
                                                    } else {
                                                        flashCodeImg();
                                                        showErrorMsg(jsonO.msg);
                                                    }
                                                },
                                                error: function (XMLHttpRequest, textStatus, errorThrown) {
                                                }
                                            });

                                            downTime = setInterval(function () {
                                                //倒计时实时结束时间
                                                var end_time = new Date();
                                                end_time = end_time.getTime();
                                                //得到剩余时间
                                                var dtime = timeCount - Math.floor((end_time - start_time) / 1000);
                                                $(obj).text(dtime + "秒后重获取");
                                                if (dtime <= 0) {
                                                    $(obj).addClass('get-code-able').addClass('btn-lightblue').removeClass('get-code-disable get-code-in').text("获取验证码");
                                                    ;//启用按钮
                                                    window.clearInterval(downTime);
                                                }
                                                ;
                                            }, 1000);
                                        } else {
                                            showErrorMsg('发送次数过多，请明天再来！');
                                        }
                                        ;
                                    }
                                    ;
                                });
                            };

                            var isunBind = false;
                            function doUnBind() { 
                            	if (isunBind) {
                                    return;
                                }
                            	if(!confirm('确定解绑用户吗？')){
                            		isSubmit = false;
                            		return ;
                            	}
                            	var remoteUserId = $("#remoteUserId").val()
                                isunBind = true;
                                $.ajax({
                                    url: '${request.contextPath}/desktop/user/unbind?remoteUserId=' + remoteUserId,
                                    type: 'post',
                                    success: function (data) {
                                        var jsonO = JSON.parse(data);
                                        if (jsonO.success) {
                                            showSuccessMsgWithCall(jsonO.msg, goHome);
                                        } else {
                                            flashCodeImg();
                                            isunBind = false;
                                            showErrorMsg(jsonO.msg);
                                        }
                                    },
                                    error: function (XMLHttpRequest, textStatus, errorThrown) {
                                    }
                                });
                            }
                            
                            var isSubmit = false;

                            function bindingSave() {
                                if (isSubmit) {
                                    return;
                                }
                                var bindingIds = '';
                                $("input[name='bindingId']:checked").each(function () {
                                    if (bindingIds == '') {
                                        bindingIds += $(this).val();
                                    } else {
                                        bindingIds += ',' + $(this).val();
                                    }
                                });
                                if (bindingIds == '') {
                                    showErrorMsg('请选择要绑定的对象！');
                                    return;
                                }
                                isSubmit = true;
                                $.ajax({
                                    url: '${request.contextPath}/desktop/user/bindingSave',
                                    data: {
                                        "bindingIds": bindingIds,
                                        "msgCode": $("#msgCode").val(),
                                        "imgCode": $("#imgCode").val()
                                    },
                                    type: 'post',
                                    success: function (data) {
                                        var jsonO = JSON.parse(data);
                                        if (jsonO.success) {
                                            showSuccessMsgWithCall(jsonO.msg, goHome);
                                        } else {
                                            flashCodeImg();
                                            isSubmit = false;
                                            showErrorMsg(jsonO.msg);
                                        }
                                    },
                                    error: function (XMLHttpRequest, textStatus, errorThrown) {
                                    }
                                });
                            }

                            //解除qq和微信
                            $('.relieveQq').click(function () {
                                var isQQ = $('.relieveQq').val();
                                if (isQQ == '未绑定')
                                    return;

                                var type = $('#qq').val();
                                $.ajax({
                                    url: "${request.contextPath}/desktop/user/relieve?type=" + type,
                                    contentType: "application/json",
                                    dataType: "json",
                                    type: "post",
                                    success: function (data) {
                                        if (data.success) {
                                            showSuccessMsg(data.msg);
                                            $('.relieveQq').val('未绑定');
                                        } else {
                                            showErrorMsg(data.msg);
                                        }
                                    }
                                })
                            })
                            $('.relieveWeChat').click(function () {
                                var isWX = $('.relieveWeChat').val();
                                if (isWX == '未绑定')
                                    return;
                                var type = $('#wx').val();
                                $.ajax({
                                    url: "${request.contextPath}/desktop/user/relieve?type=" + type,
                                    contentType: "application/json",
                                    dataType: "json",
                                    type: "post",
                                    success: function (data) {
                                        if (data.success) {
                                            showSuccessMsg(data.msg);
                                            $('.relieveWeChat').val('未绑定');
                                        } else {
                                            showErrorMsg(data.msg);
                                        }
                                    }
                                })
                            })

                        </script>