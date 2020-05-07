<div class="main-container" id="main-container">
	<div class="main-content">
		<div class="main-content-inner">
			<div class="page-content">
				<div class="box box-default box-queryStudent" >
					<div class="box-body" id="dlFind">
						<h3 class="text-center">${title!}通知单</h3>
						<div class="form-horizontal">
							<div class="form-group">
								<label class="col-sm-3 control-label no-padding-right">考生姓名：</label>
								<div class="col-sm-9">
									<input type="text" class="form-control" nullable="false" maxlength="10" id="stuName">
								</div>
							</div>
							<div class="form-group">
								<label class="col-sm-3 control-label no-padding-right">准考证号：</label>
								<div class="col-sm-9">
									<input type="text" class="form-control" nullable="false" maxlength="32" id= "admission">
								</div>
							</div>
							<div class="form-group">
								<label class="col-sm-3 control-label no-padding-right">查看类型：</label>
								<div class="col-sm-9">
										<#if typeList?exists && typeList?size gt 0 >
						                    <#list typeList as type>
						                       <label><input  type="radio" name="type" class="wp" value="${type.typeKey!}" <#if type_index == 0> checked = "true" </#if> ><span class="lbl"><#if type.typeKey == '1'>考试座位信息 <#else>考试成绩信息 </#if> </span></label>
						                    </#list>
					                    </#if>
								</div>
							</div>
							<div class="form-group">
								<div class="col-sm-12">
									<button class="btn btn-block btn-blue" onclick="doFind()">查询</button>
								</div>
							</div>
						</div>
					</div>
				</div>
			</div><!-- /.page-content -->
		</div>
	</div><!-- /.main-content -->
</div><!-- /.main-container -->

<script>
   //保存
   function doFind(){
      var studentName = $("#stuName").val();
	   var admission =  $("#admission").val();
	   var examType=$('input:radio[name="type"]:checked').val();
//	   var examType = $("#examType").val();
	   var check = checkValue('#dlFind');
		if(!check){
		 	return;
	   }
	    studentName = encodeURI(studentName);
       $.ajax({
                url:"${request.contextPath}/examinfo/stu/findExam?studentName="+studentName+"&admission="+admission+"&examType="+examType,
	            data:JSON.stringify({}),
	            clearForm : false,
	            resetForm : false,
	            dataType:'json',
	            contentType: "application/json",
	            type:'post',
	            success:function (data) {
	                if(data.success){
	                //    showSuccessMsg(data.msg);
	                    showExamIndex(studentName,admission,examType);
	                }else{
	                    showErrorMsg(data.msg);
	                }
	            }
	        });   
   
   
   
   }
</script>


