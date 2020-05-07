<#import "/fw/macro/webmacro.ftl" as w>
<title>部门信息</title>
<link rel="stylesheet" href="${request.contextPath}\static\ace\css\daterangepicker.css" />
<#-- 从dto中获取部门对象o -->
<#assign o = dto.dept />
<#assign fieldsMain = ["deptName", "deptShortName", "deptCode", "deptType", "id", "unitId", "parentId", "deptTel", "displayOrder"] />
<div class="row deptDetail" style="margin-top:10px;">
	<fieldset>
		<legend>基本信息</legend>
		<#-- 通过配置直接生成页面 -->
		<@w.initEntityColumn obj=o columnInfo=columnInfo fields=fieldsMain cols=2/>
	</fieldset>
</div>
<div class="row deptDetail" style="margin-top:10px;">
	<fieldset>
		<legend>其他信息</legend>
		<#-- <#assign fieldsOther = ["teacherId", "leaderId", "deputyHeadId", "remark", "instituteId", "areaId" ] /> -->
		<#-- 自定义页面 
		<div class="form-horizontal col-lg-6 col-sm-6 col-xs-12 col-md-6" role="form">
			<@w.selectVsqlDiv id="teacherId" value=o.teacherId columnInfo=columnInfo obj=o />
			<@w.selectVsqlDiv id="leaderId" value=o.leaderId columnInfo=columnInfo obj=o />
			<@w.selectVsqlDiv id="deputyHeadId" value=o.deputyHeadId columnInfo=columnInfo obj=o />
		</div>
		<div class="form-horizontal col-lg-6 col-sm-6 col-xs-12 col-md-6" role="form">
			<@w.inputDiv id="remark" value=o.remark columnInfo=columnInfo  />
		</div> -->
		<@w.initEntityColumn obj=o columnInfo=columnInfo fields=fields exFields=fieldsMain cols=2/> 
	</fieldset>
</div>
<div class="row" style="margin-top:10px;">
		<div class="clearfix form-actions center">
			<@w.btn btnId="dept-commit" btnClass="fa-check" btnValue="确定" />
			<@w.btn btnId="dept-close" btnClass="fa-times" btnValue="取消" />
		</div>
</div><!-- /.row -->


<!-- page specific plugin scripts -->

<!--[if lte IE 8]>
  <script src="${request.contextPath}/static/ace/js/excanvas.js"></script>
<![endif]-->

<script type="text/javascript">
// 需要用到的js脚本，延迟加载
var scripts = [null,
"${request.contextPath}/static/ace/components/moment/moment.js",
"${request.contextPath}/static/ace/components/bootstrap-daterangepicker/daterangepicker.js",
null
];
$('.page-content-area').ace_ajax('loadScripts', scripts, function() {
	// 初始化交互控件
	initOperationCheck('.deptDetail');
	// 初始化日期控件
	initCalendar(".deptDetail");
	// 取消按钮操作功能
	$("#dept-close").on("click", function(){
		doLayerOk("#dept-commit", {
		redirect:function(){gotoHash("${request.contextPath}/basedata/dept/page")},
		window:function(){layer.closeAll()}
		});		
	 });
	// 确定按钮操作功能
	$("#dept-commit").on("click", function(){
		$(this).addClass("disabled");
		var check = checkValue('.deptDetail');
		if(!check){
		 	$(this).removeClass("disabled");
		 	return;
		}
		var obj = new Object();
		// 获取此控件下所有的可输入内容，并组织成json格式
		// obj.dept，是因为url所对应的接收对象是个dto，数据是存在dto.dept
		obj.dept = JSON.parse(dealValue('.deptDetail'));
		// 提交数据
	 	$.ajax({
		    url:'${request.contextPath}/basedata/dept/save',
		    data: JSON.stringify(obj),  
		    type:'post',  
		    cache:false,  
		    contentType: "application/json",
		    success:function(data) {
		    	var jsonO = JSON.parse(data);
		 		if(!jsonO.success){
		 			swal({title: "操作失败!",
	    			text: jsonO.msg,type: "error",showConfirmButton: true,confirmButtonText: "确定"}, function(){
	    			$("#dept-commit").removeClass("disabled");
	    			});
		 		}
		 		else{
		 			// 显示成功信息
		 			layer.tips(jsonO.msg, "#dept-commit", {tips: [4, '#228B22']});
		 			// 调用封装好的函数，此函数内支持移动端和非移动端，也可以自己写，
		 			// 需要区分移动端和非移动端返回处理不一样
	 				doLayerOk("#dept-commit", {
						redirect:function(){gotoHash("${request.contextPath}/basedata/dept/page")},
						window:function(){
							setTimeout(function(){layer.closeAll();}, 300);
				 			deptTreezTreeFind();				 			
							jQuery("#deptList").jqGrid('setGridParam', {
								url : "${request.contextPath}/basedata/unit/${unitId!}/depts"
								    }).trigger("reloadGrid",[{page:1}]);
			 			}
		 			});
    			}
		     }
		});
	 });
});	
</script>
