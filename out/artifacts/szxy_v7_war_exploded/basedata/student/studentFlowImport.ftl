						<form id="studentflowin_import_upload" enctype="multipart/form-data">
						<div class="row" style="height:100%;">
							<div class="col-xs-12 col-lg-12 col-md-12 col-sm-12" >
								<!-- PAGE CONTENT BEGINS -->
									<div class="import-wrap">
										<div class="import-header">
											<h4 class="import-title">学生<#if flowType?default('99')=='1'>转入<#elseif flowType?default('99')=='0'>转出</#if>批量导入步骤：</h4>
										</div>
										<div class="import-body">
											<div class="import-step clearfix">
												<span class="import-step-num">1、</span>
												<div class="import-content">
													<p>下载模板：
													<#if flowType?default('99')=='1'>
													<a href="javascript:void(0);" class="btn-flowIn-import-template">学生转入导入模板</a>
													<#elseif flowType?default('99')=='0'>
													<a href="javascript:void(0);" class="btn-flowIn-import-template">学生转出导入模板</a>
													</#if>
													</p>
												</div>
											</div>
											<div class="import-step clearfix">
												<span class="import-step-num">2、</span>
												
												<div class="import-content">
													<p>根据模板填写信息并选择相应文件</p>
													<p>
														<span id="span1" class="upload-span" style="width:80px;"><a href="javascript:void(0)" class="">选择文件</a></span>
														<input id="imgPhoto1" type="file" class="btn btn-sm btn-lightblue" name="excel"/>
														<input id="uploadFilePath1" name="image1FileName" type="text" class="input-txt input-readonly" readonly="readonly" style="width:150px;height:30px;" value="" maxLength="125"/>
													</p>
												</div>
												
											</div>
											<div class="import-step clearfix">
												
											</div>
										</div>
										<div class="import-footer">
											<a class="btn btn-darkblue btn-studentflowin-start" href="javascript:void(0);">开始导入</a>
											<#--
											<strong>您需要先上传相应文件并选择属性，才能开始导入</strong>
											-->
										</div>
									</div>
									<!-- 导入说明开始 -->
									<div class="widget-box widget-box-lightblue2 collapsed">
										<div class="widget-header">
											<h4 class="widget-title lighter">说明</h4>
											<div class="widget-toolbar no-border">
												<a href="javascript:;" data-action="collapse">
													<i class="ace-icon fa fa-chevron-down"></i>
												</a>
											</div>
										</div>

										<div class="widget-body">
											<div class="widget-main padding-12 no-padding-top no-padding-bottom">
												<p>1、导入时，会根据班级记学科确认数据是否存在，如果系统中已经存在，则会覆盖导入列的信息，如果不存在，则会新增记录</p>
												<p>2、导入文件中请确认数值；类型的小数位数及对应的年级班级，否则可能会导致数据不对</p>
												<p>3、导入班级名称为年级名称+班级名称</p>
											</div>
										</div>
									</div><!-- 导入说明结束 -->
									<!-- 导入数据表格开始 -->
									<div id="importList-div"></div>
								<!-- PAGE CONTENT ENDS -->
							</div><!-- /.col -->
						</div><!-- /.row -->
					</div><!-- /.page-content -->
				</div>
</form>
			<a href="javascript:;" id="btn-scroll-up" class="btn-scroll-up btn btn-sm btn-inverse">
				<i class="ace-icon fa fa-angle-double-up icon-only bigger-110"></i>
			</a>
<script>
	
		var submit = false;
		<#if flowType?default('99')=='1'>
			var listUrl = '${request.contextPath}/basedata/studentFlowIn/import/list';
		<#elseif flowType?default('99')=='0'>
			var listUrl = '${request.contextPath}/basedata/studentFlowOut/import/list';
		</#if>
		var isLoop = true;
		$(document).ready(function(){
			function timer(){
				if(!isLoop){
					return ;
				}
				$("#importList-div").load(listUrl);
				setTimeout(timer,1000*61);
			}
			timer();
			
		});

	function resetFilePos(){
	$("#imgPhoto1").css({"position":"absolute","-moz-opacity":"0","opacity":"0","filter":"alpha(opacity=0)","cursor":"pointer","width":$(".upload-span a").width(),"height":$(".upload-span").height()});
	$("#imgPhoto1").offset({"left":$("#span1").offset().left});		
	$("#imgPhoto1").css({"display":""});
	}
	function initFileInput(){
			$("#span1").mouseover(function(){
				$("#imgPhoto1").offset({"top":$("#span1").offset().top });
			});
			
			$("#imgPhoto1").on("change",function(){
				var p1 = $("#imgPhoto1").val().lastIndexOf("\\");
	    		var fileName = $("#imgPhoto1").val().substring(p1+1); 
	    		var type = fileName.substring(fileName.lastIndexOf(".")+1);
	    		if($.trim(type)==''){
	    			return ;
	    		}
	    		submit = false;
	    		if($.trim(type)!='xls' && $.trim(type) != 'xlsx' ){
	    			layer.tips('文件类型错误,请选择excel文件','#uploadFilePath1',{tipsMore:true});
	    		}
				$('#uploadFilePath1').val(fileName);
			});
			
			resetFilePos();
		}
		
		function resetDiv(){
			$("#studentflowin_import_upload").parent("div").parent("div").css("height","100%");
		}
	var scripts = [null,"${request.contextPath}/static/js/jquery.form.js",null];
	$('.page-content-area').ace_ajax('loadScripts', scripts, function() {
		$("#importList-div").load(listUrl);
		initFileInput();
		resetDiv();
		//模板下载
		$(".btn-flowIn-import-template").on("click",function(){
			<#if flowType?default('99')=='1'>
				location.href = '${request.contextPath}/basedata/studentFlowIn/template';
			<#elseif flowType?default('99')=='0'>
				location.href = '${request.contextPath}/basedata/studentFlowOut/template';
			</#if>
		});
		
		<#if flowType?default('99')=='1'>
			var url = '${request.contextPath}/basedata/studentFlowIn/import';
		<#elseif flowType?default('99')=='0'>
			var url = '${request.contextPath}/basedata/studentFlowOut/import';
		</#if>
		
		$(".btn-studentflowin-start").on("click",function(){
			
			if(!checkFileVal()){
				return ;
			}
			
			if(submit){
				return ;
			}
			
			submit = true;
			
			var options = {  
       			url:url,
        		clearForm : false,
	       		resetForm : false,
        		dataType:'json',
        		type:'post',
        		success:function(data){
        			var res = data;//$.parseJSON(data);
        			if(!res.success){
        				showMsgError(res.msg,"操作失败!",function(index){
				  			layer.close(index);
							$("#importList-div").load(listUrl);
						});
						
						
        			}else{
        				// 需要区分移动端和非移动端返回处理不一样
		    			showMsgSuccess(res.msg,"操作成功!",function(index){
							var file = $("#imgPhoto1") 
							var inputFile = "<input id=\"imgPhoto1\" type=\"file\" class=\"btn btn-sm btn-lightblue\" name=\"excel\"/>";
								file.after(inputFile); 
								file.remove(); 
								$("#uploadFilePath1").val("");
								initFileInput();
				  			layer.close(index);
						});
        			}
        		}
       		};  
  			try{
     			$("#studentflowin_import_upload").ajaxSubmit(options); 
     		}catch(e){
     			//showMsgError('保存学生失败！');
     		}
		});
	});
	
	function checkFileVal(){
			var p = $("#imgPhoto1").val();
			if($.trim(p)==''){
				layer.tips('请选择要导入的文件','#uploadFilePath1',{tipsMore:true});
				return false;
			}
			var p1 = p.lastIndexOf("\\");
			
    		var fileName = $("#imgPhoto1").val().substring(p1+1); 
    		var type = fileName.substring(fileName.lastIndexOf(".")+1);
    		if(type!='xls' && type != 'xlsx'){
    			layer.tips('文件类型错误,请选择excel文件','#uploadFilePath1',{tipsMore:true});
    			return false;
    		}
    		return true;
	}
</script>