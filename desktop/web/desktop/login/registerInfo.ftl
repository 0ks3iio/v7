<!DOCTYPE html>
<html lang="en">
	<head>
		<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1" />
		<meta charset="utf-8" />
		<title>角色-信息填写</title>

		<meta name="description" content="" />
		<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0" />

		<!-- bootstrap & fontawesome -->
		<link rel="stylesheet" href="${request.contextPath}/static/components/bootstrap/dist/css/bootstrap.css" />
		<link rel="stylesheet" href="${request.contextPath}/static/components/font-awesome/css/font-awesome.css" />

		<link rel="stylesheet" href="${request.contextPath}/static/css/components.css">
		<link rel="stylesheet" href="${request.contextPath}/static/css/basic-data.css">
		<!--[if lte IE 9]>
		  <link rel="stylesheet" href="${request.contextPath}/static/css/pages-ie.css" />
		<![endif]-->
		<!-- inline styles related to this page -->

		<!-- HTML5shiv and Respond.js for IE8 to support HTML5 elements and media queries -->

		<!--[if lte IE 8]>
			<script src="${request.contextPath}/static/components/html5shiv/dist/html5shiv.min.js"></script>
			<script src="${request.contextPath}/static/components/respond/dest/respond.min.js"></script>
		<![endif]-->
	</head>

	<body class="super">
		<div class="box box-default-dq">
			<div class="box-body-dq">
				<div class="box-title-dq">尊敬的<#if ownerType?default('1')=='2'>老师<#elseif ownerType?default('1')=='3'>家长<#elseif ownerType?default('1')=='1'>学生</#if>，您好，请填写个人信息</div>
				<form class="form-horizontal" name="registerForm" method="post" id="registerForm">
					<input type="hidden" name="ownerType" id="ownerType" value="${ownerType?default('1')}"/>
					<input type="hidden" name="ownerId" id="ownerId" value="00000000000000000000000000000000"/>
					<input type="hidden" name="userType" id="userType" value="2"/>
					<div class="form-group">
						<label class="col-sm-4 control-label" for="form-field-1">用户名：</label>

						<div class="col-sm-4">
							<input type="text" id="username" value="" name="username" maxlength="20" placeholder="以字母开头，可以使用数字和下划线，6-20个字符" class="form-control"  onblur="checkUser()">
						</div>
						<div class="col-sm-4 control-tips tip-false">
							
						</div>
					</div>
					<#if ownerType?default('1')=='2'>
					<div class="form-group">
						<label class="col-sm-4 control-label" for="form-field-2">区域选择：</label>
						<div class="col-sm-4">
							<select name="" id="form-field-subregion-1" placeholder="请选择省" class="form-control" disabled>
								<option value="${proRegion.fullCode!}">${proRegion.regionName!}</option>
							</select>
						</div>
					</div>	
					<div class="form-group">						
						<label class="col-sm-4 control-label" for="form-field-2"></label>
						<div class="col-sm-4">
							<select name="" id="form-field-subregion-2" placeholder="请选择市" class="form-control" <#if unitLevel?default(-1) gt 2>disabled</#if> onchange="changeRegion(this.value,3)" >
								<#if unitLevel?default(-1) gt 1>
									<#if cityRegion?exists>
										<option value="${cityRegion.fullCode!}">${cityRegion.regionName!}</option>
									<#else>
										<option value="">请选择市</option>
										<#list regionList as reg>
											<option value="${reg.fullCode!}">${reg.regionName!}</option>
										</#list>
									</#if>
								<#else>
									<option value="">请选择市</option>
								</#if>
							</select>
						</div>
						<div class="col-sm-4 region-tips control-tips">
						</div>
					</div>
					<div class="form-group">	
						<label class="col-sm-4 control-label" for="form-field-2"></label>
						<div class="col-sm-4">
							<select name="" id="form-field-subregion-3" placeholder="请选择区县" class="form-control" <#if unitLevel?default(-1) gt 3>disabled</#if> onchange="changeRegion(this.value,4)" >
								<#if unitLevel?default(-1) gt 2>
									<#if countyRegion?exists>
										<option value="${countyRegion.fullCode!}">${countyRegion.regionName!}</option>
									<#else>
										<option value="">请选择区县</option>
										<#list regionList as reg>
											<option value="${reg.fullCode!}">${reg.regionName!}</option>
										</#list>
									</#if>
								<#else>
									<option value="">请选择区县</option>
								</#if>
							</select>
						</div>
						<div class="col-sm-4 region-tips control-tips">
						</div>
					</div>
					<div class="form-group">
						<label class="col-sm-4 control-label" for="form-field-2">单位选择：</label>

						<div class="col-sm-4">
							<input type="hidden" name="unitId" id="unitId" value=""/>
							<select name="" id="form-field-2" placeholder="请选择所属单位" class="form-control" onchange="changeUnit(this)" >
								<option value="">请选择</option>
								<#list unitList as unit>
									<#if unit.isDeleted==0>
										<option value="${unit.id!}">${unit.unitName!}</option>
									</#if>
								</#list>
							</select>
						</div>
						<div class="col-sm-4 control-tips tip-false">
						</div>
					</div>
					</#if>
					<div class="form-group">
						<label class="col-sm-4 control-label" for="form-field-3">设置密码：</label>

						<div class="col-sm-4">
							<input type="password" id="firstPwd" name="password" value="" placeholder="6-16个字符，可以使用数字和下划线,区分大小写" class="form-control" maxlength="16" onblur="checkFirstPwd()"/>
						</div>
						<div class="col-sm-4 control-tips tip-false">
							
						</div>
					</div>
					<div class="form-group">
						<label for="form-field-4" class="col-sm-4 control-label">确认密码：</label>
						<div class="col-sm-4">
							<input type="password" class="form-control" id="secondPwd"  placeholder="请再次输入密码" maxlength="16" onblur="checkSecondPwd()"/>
						</div>
						<div class="col-sm-4 control-tips tip-false"></div>
					</div>
					<div class="form-group">
						<label for="form-field-5" class="col-sm-4 control-label">真实姓名：</label>
						<div class="col-sm-4">
							<input type="text" class="form-control" id="realName" name="realName" maxlength="50" placeholder="请输入真实姓名" onblur="checkRealName()"/>
						</div>
						<div class="col-sm-4 control-tips tip-false"></div>
					</div>
					<#if ownerType?default('1')=='1'>
					<div class="form-group">
						<label for="form-field-5" class="col-sm-4 control-label">身份证件号：</label>
						<div class="col-sm-4">
							<input type="text" class="form-control" id="identityCard" name="identityCard" placeholder="请输入身份证件号" onblur="checkIdentityCard()"/>
						</div>
						<div class="col-sm-4 control-tips tip-false"></div>
					</div>
					<#else>
					<div class="form-group">
						<label for="phoneNum" class="col-sm-4 control-label">手机号：</label>
						<div class="col-sm-4">
							<input type="text" class="form-control" id="mobilePhone" name="mobilePhone" maxlength="11" placeholder="请输入手机号码" />
						</div>
						<div class="col-sm-4 control-tips tip-false"></div>
					</div>
					</#if>
					<div class="form-group">
						<label class="col-sm-4 control-label" for="form-field-8">图片验证码：</label>

						<div class="col-sm-2">
							<input type="text" id="imgCode" name="imgCode" value="" placeholder="请填写右侧的验证码" class="form-control" onblur="checkImgCode()">
						</div>
						<div class="col-sm-2">
							<a href="javascript:void(0)" onclick="flashCodeImg()" class="verCode-img"><img id="codeImg" src="${request.contextPath}/desktop/verifyQuestionImage" alt=""></a>
						</div>
						<div class="col-sm-4 control-tips tip-false"></div>
					</div>
					<#if !(ownerType?default('1')=='1')>
					<div class="form-group">
						<label for="form-field-7" class="col-sm-4 control-label">短信验证码：</label>
						<div class="col-sm-2">
							<input type="text" class="form-control" id="msgCode" name="msgCode" placeholder="请输入短信验证码" onblur="checkMsgCode()"/>
						</div>
						<div class="col-sm-2">
							<button  type="button" class="btn get-code-disable" id="getCode">获取短信验证码</button>
						</div>
						<div class="col-sm-4 control-tips tip-false"></div>
					</div>
					</#if>
					<div class="form-group">
						<label class="col-sm-4 control-label" for="form-field-1"></label>
						<div class="col-sm-4">
							<button class="btn btn-block btn-blue" id="subBtn" type="button" onclick="onSaveUser()">立即注册</button>
						</div>
						<div class="col-sm-4 control-tips">
						</div>
					</div>
				</form>
			</div>
		</div>

		<!--[if !IE]> -->
		<script src="${request.contextPath}/static/components/jquery/dist/jquery.js"></script>
		<script src="${request.contextPath}/static/js/jquery.form.js"></script>
		<!-- <![endif]-->

		<!--[if IE]>
		<script src="${request.contextPath}/static/components/jquery.1x/dist/jquery.js"></script>
		<![endif]-->
		<script type="text/javascript">
			if('ontouchstart' in document.documentElement) document.write("<script src='${request.contextPath}/static/components/_mod/jquery.mobile.custom/jquery.mobile.custom.js'>"+"<"+"/script>");
		</script>
		<script src="${request.contextPath}/static/components/bootstrap/dist/js/bootstrap.js"></script>

		<!-- page specific plugin scripts -->
		<script src="${request.contextPath}/static/components/layer/layer.js"></script>
		<!-- 
		<script src="${request.contextPath}/static/js/jquery.SendCode.js"></script>
		inline scripts related to this page -->
		<script>
			$(function(){
				sendcode('#mobilePhone','#getCode',59,5)
				function winLoad(){
					var window_h=$(window).height();
					var login_h=$('.box-default-dq').outerHeight();
					var login_top=parseInt((window_h-login_h)/2);
					$('.box-default-dq').css('margin-top',login_top);
				};
				winLoad();
				$(window).resize(function(){
					winLoad();		
				});
			})
			
			function changeForUnit(reg,level){
				$.ajax({
		            url:'${request.contextPath}/homepage/register/regionUnitList',
		            data:{"regionCode":reg,"forUnit":"1"},
		            type:'post',
		            success:function(data){
		                var jsonO = JSON.parse(data);
		                var uns = jsonO.unitList;
		                dealUnit(uns, "form-field-subregion-"+(level-1));
		            },
		            error : function(XMLHttpRequest, textStatus, errorThrown) {
		            }
		        });
			}
			
			function dealUnit(uns,errorId){
				$('#'+errorId).parent().siblings(".control-tips").removeClass("tip-false");
			
				var uo = document.getElementById('form-field-2');
				changeUnit(uo.options[0]);
				uo.options.length=0;
                uo.options[0]=new Option('请选择','');
                if(!uns || uns.length < 1){
                	addError(errorId,"该区域下没有直属单位");
                } else {
                	for(var i=0;i<uns.length;i++){
                		uo.options[i+1]=new Option(uns[i].unitName,uns[i].id);
                	}
                }
			}
			
			function changeRegion(reg,level){
				$('.region-tips').html('');
				var errorId = "form-field-subregion-"+(level-1);
				$('#'+errorId).parent().siblings(".control-tips").removeClass("tip-false");
				
				if(reg == ''){
					if(level != 4){
						var subObj = document.getElementById('form-field-subregion-'+level);
	                	subObj.options.length=0;
	                	subObj.options[0]=new Option('请选择','');
					}
					
					var paval = $('#form-field-subregion-'+(level-2)).val();
					changeForUnit(paval,level-1);
					return;
				}
				var uo = document.getElementById('form-field-2');
				$.ajax({
		            url:'${request.contextPath}/homepage/register/regionUnitList',
		            data:{"regionCode":reg},
		            type:'post',
		            success:function(data){
		                var jsonO = JSON.parse(data);
		                var uns = jsonO.unitList;
		                dealUnit(uns, "form-field-subregion-"+(level-1));
		                if(level != 4){
		                	var res = jsonO.regionList;
		                	var subObj = document.getElementById('form-field-subregion-'+level);
		                	subObj.options.length=0;
		                	subObj.options[0]=new Option('请选择','');
		                	if(!res || res.length < 1){
		                		addError(errorId,"该区域下没有下级行政规划！");
		                	} else {
		                		for(var i=0;i<res.length;i++){
			                		subObj.options[i+1]=new Option(res[i].regionName,res[i].fullCode);
			                	}
		                	}
		                }
		            },
		            error : function(XMLHttpRequest, textStatus, errorThrown) {
		            }
		        });
			}
			
			function flashCodeImg(){
				$("#codeImg").get(0).src='${request.contextPath}/desktop/verifyQuestionImage?time_='+ Math.random();  
			}
			
			function addError(id,errormsg){
				if(!errormsg){
					errormsg='错误';
				}
				$("#"+id).parent().siblings(".control-tips").addClass("tip-false");
				$("#"+id).parent().siblings(".control-tips").html('<span class="has-error"><i class="fa fa-times-circle"></i>'+errormsg+'</span>');
			}
			
			function addSuccess(id,msg){
				if(!msg){
					msg='正确';
				}
				$("#"+id).parent().siblings(".control-tips").removeClass("tip-false");
				$("#"+id).parent().siblings(".control-tips").html('<span class="has-success"><i class="fa fa-check-circle"></i>&nbsp;'+msg+'</span>');
			}
			
			function addWarn(id){
				$("#"+id).parent().siblings(".control-tips").addClass("tip-false");
				$("#"+id).parent().siblings(".control-tips").html('<span class="has-warning"><i class="fa fa-info-circle"></i>&nbsp;警告</span>');
			}
			
			function checkRealName(){
				 var realName = $("#realName").val();
				 if(realName && realName.length>0 ){
				 	addSuccess("realName");
				 }else{
				 	addError("realName","真实姓名不能为空");
				 }
			}
			
			function checkIdentityCard(){
				var identityCard = $("#identityCard").val();
				var identityCardReg = /^[1-9]\d{7}((0\d)|(1[0-2]))(([0|1|2]\d)|3[0-1])\d{3}$|^[1-9]\d{5}[1-9]\d{3}((0\d)|(1[0-2]))(([0|1|2]\d)|3[0-1])\d{3}([0-9]|X)$/;
				if(identityCardReg.test(identityCard)){
				 	addSuccess("identityCard");
				 }else{
				 	addError("identityCard","身份证件号不符合身份证规则");
				 }
			}
			
			function checkMobilePhone(){
				var mobilePhone = $("#mobilePhone").val();
				var mobilePhoneReg = /^(((13[0-9]{1})|(15[0-9]{1})|(18[0-9]{1}))+\d{8})$/;
				if(mobilePhoneReg.test(mobilePhone)){
				 	addSuccess("mobilePhone");
				 }else{
				 	addError("mobilePhone","电话号码不符合规则");
				 }
			}
			
			function checkImgCode(){
				var imgCode = $("#imgCode").val();
				if(imgCode && imgCode.length>0 ){
					addSuccess("imgCode");
				}else{
					addError("imgCode","图片验证码不能为空");
				}
			}
			
			function checkMsgCode(){
				var msgCode = $("#msgCode").val();
				if(msgCode && msgCode.length>0 ){
					addSuccess("msgCode");
				}else{
					addError("msgCode","短信验证码不能为空");
				}
			}
			
			function checkUser(){
				 var username = $("#username").val();
				 var reg = /^[a-zA-Z]\w{5,20}$/;
				 if(reg.test(username)){
					 $.ajax({
			            url:'${request.contextPath}/homepage/register/registerCheckUser',
			            data:{"username":username},
			            type:'post',
			            success:function(data){
			                var jsonO = JSON.parse(data);
			                if(jsonO.success){
			                    addSuccess("username");
			                }else{
			                	addError("username","用户名已被注册");
			                }
			            },
			            error : function(XMLHttpRequest, textStatus, errorThrown) {
			            }
			        });
				 }else{
				 	addError("username","用户名格式不正确");
				 }
			}
			
			function checkFirstPwd(){
				 var pwdreg = /^\w{6,16}$/;
				 var pwd =$("#firstPwd").val();
				 if(pwdreg.test(pwd)){
				 	addSuccess("firstPwd");
				 	checkSecondPwd();
				 }else{
				 	addError("firstPwd","密码格式不正确");
				 }
			}
			
			function checkSecondPwd(){
 				var secondpwd = $("#secondPwd").val();
 				if(secondpwd && secondpwd.length>0 ){
	 				var pwd =$("#firstPwd").val();
	 				if(pwd==secondpwd){
						addSuccess("secondPwd");
					}else{
						addError("secondPwd","两次输入的密码不一致");
					}
				}else{
					addError("secondPwd","确认密码不能为空");
				}
			}
			
			var isSubmit=false;
			function onSaveUser(){
				$("#subBtn").parent().siblings(".control-tips").removeClass("tip-false");
				$("#subBtn").parent().siblings(".control-tips").html('');
				
				//
				checkUser();
				checkRealName();
				checkFirstPwd();
				checkSecondPwd();
				<#if (ownerType?default('1')=='2')>				
					checkUnit();
				</#if>
				<#if (ownerType?default('1')=='1')>
					checkIdentityCard();
					checkImgCode();
				<#else>
					checkMobilePhone();
					checkMsgCode();
				</#if>
				
				
				if($(".tip-false").size()>0){
					return;
				}
				
				if(isSubmit){
					return ;
				}
				
				isSubmit = true;
				
				var options = {  
	       			url:'${request.contextPath}/homepage/register/registerSaveUser',
	       			data:$("#registerForm").serialize(),
	        		clearForm : false,
		       		resetForm : false,
	        		dataType:'json',
	        		type:'post',
	        		success:function(data){
	        			var res = data;//$.parseJSON(data);
	        			<#if (ownerType?default('1')=='1')>
		        			if(res.success){
		        				window.location.href='${request.contextPath}/homepage/register/registerSuccess?ownerType=${ownerType?default('1')}&username='+$("#username").val();
		        			}else{
		        				isSubmit=false;
		        				flashCodeImg();
		        				addError("subBtn",res.msg);
		        			}
	        			<#else>
		        			if(res.success){
		        				window.location.href='${request.contextPath}/homepage/register/registerSuccess?ownerType=${ownerType?default('1')}&username='+$("#username").val()+"&mobilePhone="+$("#mobilePhone").val();
		        			}else{
		        				isSubmit=false;
		        				addError("subBtn",res.msg);
		        			}
	        			</#if>
	        		}
	       		};
	       		$.ajax(options);
			}
			
			function checkUnit(){
				var unitId = $("#unitId").val();
				if(unitId && unitId.length>0 ){
					addSuccess("unitId");
				}else{
					addError("unitId","请选择单位");
				}
			}
			
			function changeUnit(event){
				var unitId = event.value;
				$("#unitId").val(unitId);
				if(unitId!=''){
					addSuccess("unitId");
				}else{
					addError("unitId","请选择单位");
				}
			}
			
			/*
			2015-12-09
			基于jquery的发送验证码倒数功能
			用法：
			1、html：不做任何改变，只需有一个id或是class
			2、js根据id或class来控制
			sendcode('#phoneNum','#btnSendCode',60,5)
			*/
			
			function sendcode(num,obj,count,n){
				//因为ios应用页面是不会刷新的
				var timeCount = count; //定义倒计时总数
				var sendTime = n; //发送验证码次数
				$(obj).attr('data-max',sendTime);
				//正则验证手机号码有效性
				var phoneNum = /^(((13[0-9]{1})|(15[0-9]{1})|(18[0-9]{1}))+\d{8})$/;
				$(num).on('keyup focus',function(){
					if(!$(obj).hasClass('get-code-in')){
						if(!phoneNum.test($(num).val())){ 
							addError("mobilePhone","电话号码不符合规则");
							$(obj).removeClass('get-code-able').addClass('get-code-disable'); 
						}else{
							addSuccess("mobilePhone");
							$(obj).addClass('get-code-able').removeClass('get-code-disable'); 
						};
					};
				});
				$(obj).click(function(e){
					e.preventDefault();
					var imgCode = $("#imgCode").val();
					if(imgCode && imgCode.length>0 ){
					}else{
						addError("msgCode","获得短信验证码前,必须填写图片验证码");
						return;
					}
					if($(this).hasClass('get-code-able')){
						var start_time = new Date();
						start_time = start_time.getTime();//获取开始时间的毫秒数
						var newTime = parseInt($(obj).attr('data-max')-1);
						if(newTime >= 0){
							$(obj).attr('data-max',newTime).addClass('get-code-disable get-code-in').removeClass('get-code-able').text(timeCount + "秒后重获取");
							$.ajax({
					            url:'${request.contextPath}/homepage/register/registerMsgCode',
					            data:{"mobilePhone":$("#mobilePhone").val(),"imgCode":$("#imgCode").val()},
					            type:'post',
					            success:function(data){
					                var jsonO = JSON.parse(data);
					                if(jsonO.success){
					                    addSuccess("msgCode",jsonO.msg);
					                }else{
					                	flashCodeImg();
					                	addError("msgCode",jsonO.msg);
					                }
					            },
					            error : function(XMLHttpRequest, textStatus, errorThrown) {
					            }
						    });
							
							downTime = setInterval(function(){
								//倒计时实时结束时间
								var end_time = new Date();
								end_time = end_time.getTime();
								//得到剩余时间
								var dtime = timeCount - Math.floor((end_time - start_time) / 1000);
								$(obj).text(dtime + "秒后重获取");
									if(dtime <= 0){
									$(obj).addClass('get-code-able').removeClass('get-code-disable get-code-in').text("获取验证码");;//启用按钮
									window.clearInterval(downTime);
								};
							},1000);
						}else{
							alert('发送次数过多，请明天再来！');
						};
					};
				});
			};
			
		</script>
	</body>
</html>
