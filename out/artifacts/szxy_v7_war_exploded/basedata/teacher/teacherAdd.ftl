<#import "/fw/macro/webmacro.ftl" as w>
<title>教师信息</title>
<link rel="stylesheet" href="${request.contextPath}\static\ace\css\daterangepicker.css" />
<#assign o = dto.teacher />
<#assign fieldsMain = ["teacherName", "teacherCode", "sex", "birthday","deptId",  "id", "unitId","identityType","identityCard", "mobilePhone","linkAddress"] />
<div class="row teacherDetail" style="margin-top:10px;">
	<fieldset>
		<legend>基本信息</legend>
		<#-- 通过配置直接生成页面 -->
		<@w.initEntityColumn obj=o columnInfo=columnInfo fields=fieldsMain cols=2/>
	</fieldset>
</div>
<div class="row userDetail" style="margin-top:10px;">
	<fieldset>
		<legend>用户信息</legend>
		<#-- 通过配置直接生成页面 -->
		<@w.initEntityColumn obj=o columnInfo=userColumnInfo fields=userFields cols=2/>
	</fieldset>
</div>
<#--
<div class="row teacherLxDetail" style="margin-top:10px;">
	<fieldset>
		<legend>联系信息</legend>
		 通过配置直接生成页面 
		<@w.initEntityColumn obj=o columnInfo=columnInfo fields=fieldsLink cols=2/>
	</fieldset>
</div>

<div class="row teacherQtDetail" style="margin-top:10px;">
	<fieldset>
		<legend>其他信息</legend>
		<#-- 通过配置直接生成页面 
		<@w.initEntityColumn obj=o columnInfo=columnInfo fields=fieldsQt  cols=2/>
	</fieldset>
</div>
-->
<div class="row" style="margin-top:10px;">
		<div class="clearfix form-actions center">
			<@w.btn btnId="teacher-commit" btnClass="fa-check" btnValue="确定" />
			<@w.btn btnId="teacher-close" btnClass="fa-times" btnValue="取消" />
		</div>
</div><!-- /.row -->



<!-- page specific plugin scripts -->

<!--[if lte IE 8]>
  <script src="${request.contextPath}/static/ace/js/excanvas.js"></script>
<![endif]-->

<script type="text/javascript">
var scripts = [null,
"${request.contextPath}/static/ace/components/moment/moment.js",
"${request.contextPath}/static/ace/components/bootstrap-daterangepicker/daterangepicker.js",
null];

$('.page-content-area').ace_ajax('loadScripts', scripts, function() {

	initOperationCheck('.teacherDetail');
	initOperationCheck('.userDetail');
	//初始化时间控件
	initCalendar(".teacherDetail,.userDetail");
	 
	$("#teacher-close").on("click", function(){
	 	doLayerOk("#teacher-close", {
			redirect:function(){gotoHash("${request.contextPath}/basedata/teacher/page")},
			window:function(){layer.closeAll()}
		});
	});

	 $("#teacher-commit").on("click", function(){
		$(this).addClass("disabled");
		var check = checkValue('.teacherDetail');
		if(!check){
		 	$(this).removeClass("disabled");
		 	return;
		}
		var obj = new Object();
		obj.teacher = JSON.parse(dealValue('.teacherDetail'));
		obj.user = JSON.parse(dealValue('.userDetail'));
	 	$.ajax({
		    url:'${request.contextPath}/basedata/teacher/save',
		    data: JSON.stringify(obj),  
		    type:'post',  
		    cache:false,  
		    contentType: "application/json",
		    success:function(data) {
		    	var jsonO = JSON.parse(data);
		 		if(!jsonO.success){
		 			swal({title: "操作失败!",
	    			text: jsonO.msg,type: "error",showConfirmButton: true,confirmButtonText: "确定"}, function(){
	    				$("#teacher-commit").removeClass("disabled");
	    			});
		 		}
		 		else{
		 			layer.tips(jsonO.msg, "#teacher-commit", {tips: [4, '#228B22']});
		 			doLayerOk("#teacher-commit", {
						redirect:function(){gotoHash("${request.contextPath}/basedata/teacher/page")},
						window:function(){
							setTimeout(function(){layer.closeAll();}, 300);
							$("#teacherList").trigger("reloadGrid");
			 			}
		 			});	
    			}
		     }
		});
	 });	
});
	
</script>
