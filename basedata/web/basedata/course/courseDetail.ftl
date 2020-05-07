<#import "/fw/macro/webmacro.ftl" as w>
<title>科目信息</title>
<#assign o = dto />
<#assign fieldsMain = ["section","subjectCode","subjectName", "orderId", "isUsing","shortName", "id", "unitId","subjectType"] />
<div class="row courseDetail" style="margin-top:10px;">
		<div class="clearfix">
			<#-- 通过配置直接生成页面 -->
			<@w.initEntityColumn obj=o columnInfo=columnInfo fields=fieldsMain cols=2/>
		</div>
		<div class="clearfix form-actions center">
			<@w.btn btnId="course-commit" btnClass="fa-check" btnValue="确定"  />
			<@w.btn btnId="course-close" btnClass="fa-times" btnValue="取消" />
		</div>
</div><!-- /.row -->

<!-- page specific plugin scripts -->

<!--[if lte IE 8]>
  <script src="${request.contextPath}/static/ace/js/excanvas.js"></script>
<![endif]-->

<script type="text/javascript">
var scripts = [];
$('.page-content-area').ace_ajax('loadScripts', scripts, function() {

	initOperationCheck('.courseDetail');
	
	// 取消按钮操作功能
	$("#course-close").on("click", function(){
		doLayerOk("#course-commit", {
		redirect:function(){gotoHash("${request.contextPath}/basedata/course/index/page")},
		window:function(){layer.closeAll()}
		});		
	 });
	// 确定按钮操作功能
	$("#course-commit").on("click", function(){
		$(this).addClass("disabled");
		var check = checkValue('.courseDetail');
		if(!check){
		 	$(this).removeClass("disabled");
		 	return;
		}
		var obj = new Object();
		// 获取此控件下所有的可输入内容，并组织成json格式
		// obj.grade，是因为url所对应的接收对象是个dto，数据是存在dto.grade
		obj = JSON.parse(dealValue('.courseDetail'));
		// 提交数据
	 	$.ajax({
		    url:'${request.contextPath}/basedata/course/update',
		    data: JSON.stringify(obj),  
		    type:'post',  
		    cache:false,  
		    contentType: "application/json",
		    success:function(data) {
		    	var jsonO = JSON.parse(data);
		 		if(!jsonO.success){
		 			swal({title: "操作失败!",
	    			text: jsonO.msg,type: "error",showConfirmButton: true,confirmButtonText: "确定"}, function(){
	    			$("#gcourse-commit").removeClass("disabled");
	    			});
		 		}
		 		else{
		 			// 显示成功信息
		 			layer.tips(jsonO.msg, "#course-commit", {tips: [4, '#228B22']});
		 			// 调用封装好的函数，此函数内支持移动端和非移动端，也可以自己写，
		 			// 需要区分移动端和非移动端返回处理不一样
	 				doLayerOk("#course-commit", {
						redirect:function(){gotoHash("${request.contextPath}/basedata/course/page")},
						window:function(){
							setTimeout(function(){layer.closeAll();}, 300);
							$("#courseList").trigger("reloadGrid");
			 			}
		 			});				 			
    			}
		     }
		});
	 });
	 
	 $("#subjectName").on("blur",function(){
	 		takeShortName();
	 });
	 $("#section").on("change",function(){
	 	 takeShortName();
	 });
	 
});
function takeShortName(){
	var sname=$("#subjectName").val();
	var stion=$("#section").val();
	if(sname.trim()!="" && stion.trim()!=""){
	 	var stionText=$("#section option:selected").text();
	 	$("#shortName").val(stionText+sname);
	}else{
		$("#shortName").val("");
	}
}
	
</script>
