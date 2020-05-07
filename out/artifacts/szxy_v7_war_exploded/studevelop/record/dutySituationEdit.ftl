<div class="layer layer-addTerm layer-change" style="display:block;" id="myDiv">
<form id="subForm">
<input type="hidden" name="id" id="id" value="${Situation.id!}">
<input type="hidden" name="studentId" id="studentId" value="${Situation.studentId!}">
<input type="hidden" name="acadyear" id="acadyear" value="${Situation.acadyear!}">
<input type="hidden" name="semester" id="semester" value="${Situation.semester!}">
	<div class="layer-body">
		<div class="filter clearfix"> 
				<div class="filter clearfix">
					<div class="filter-item">
						<label for="" class="filter-name"><span style="color:red">*</span>开始时间：</label>
						<div class="filter-content">
						    <div class="input-group">
								<input class="form-control date-picker" vtype="data" style="width: 120px" type="text" nullable="false" name="openTime" id="openTime" placeholder="开始时间" value="${(Situation.openTime?string('yyyy-MM-dd'))!}">
								<span class="input-group-addon">
									<i class="fa fa-calendar bigger-110"></i>
								</span>
						    </div>
					    </div>
					</div>
					
					<div class="filter-item">
						<label for="" class="filter-name"><span style="color:red">*</span>结束时间：</label>
						<div class="filter-content">
					    <div class="input-group">
						<input class="form-control date-picker" vtype="data" style="width: 120px" type="text" nullable="false" name="endTime" id="endTime" placeholder="结束时间" value="${(Situation.endTime?string('yyyy-MM-dd'))!}">
						<span class="input-group-addon">
							<i class="fa fa-calendar bigger-110"></i>
						</span>
					    </div>
						</div>
					</div>
					
					<div class="filter-item">
						<label for="" class="filter-name"><span style="color:red">*</span>所任职位：</label>
						<div class="filter-content">
							<input type="text" class="form-control" style="width:160px;" name="dutyName" id="dutyName" value="${Situation.dutyName!}" nullable="false" maxLength="30"/>
						</div>
					</div>
					
					<div class="filter-item">
						<label for="" class="filter-name"><span style="color:red">*</span>工作内容：</label>
						<div class="filter-content">
							<input type="text" class="form-control" name="dutyContent" style="width:160px;" id="dutyContent" value="${Situation.dutyContent!}" nullable="false" maxLength="200"/>
						</div>
					</div>
					
					<div class="filter-item">
						<label for="" class="filter-name"><span style="color:red">*</span>工作表现情况：</label>
						<div class="filter-content">
							<input type="text" class="form-control" style="width:160px;" name="dutySituation" id="dutySituation" value="${Situation.dutySituation!}" nullable="false" maxLength="300"/>
						</div>
					</div>
					
					<div class="filter-item">
						<label for="" class="filter-name">备注：</label>
						<div class="filter-content">
							<input type="text" class="form-control" style="width:160px;" name="remark" id="remark" value="${Situation.remark!}" maxLength="200"/>
						</div>
					</div>
            </div>
        </div>
	</div>
</form>
</div>	
	<div class="layer-footer">
    <button class="btn btn-lightblue" id="arrange-commit">确定</button>
    <button class="btn btn-grey" id="arrange-close">取消</button>
    </div>

<script>
	$(function(){
		//初始化日期控件
		var viewContent={
			'format' : 'yyyy-mm-dd',
			'minView' : '2'
		};
		initCalendarData("#myDiv",".date-picker",viewContent);
		//初始化多选控件
		initChosenMore("#myDiv");
		$('.date-picker').next().on("click", function(){
			$(this).prev().focus();
		});
	});
	
	$("#arrange-close").on("click", function(){
    	doLayerOk("#arrange-commit", {
    	redirect:function(){},
    	window:function(){layer.closeAll()}
    	});     
 	});
 	
 	var isSubmit=false;
 	$("#arrange-commit").on("click", function(){
 		if(isSubmit){
 			return;
 		}
 		isSubmit=true;
 		if(!checkValue('#subForm')){
 			isSubmit=false;
 			return;
 		}
 		
 	    var openTime = $("#openTime").val();
 	    var endTime = $("#endTime").val();
 		if(openTime>endTime){
			layerTipMsg(false,"提示!","开始时间不能大于结束时间!");
			isSubmit=false;
			return;
		} 

		var options = {
			url : "${request.contextPath}/studevelop/dutySituation/save",
			dataType : 'json',
			success : function(data){
	 			var jsonO = data;
		 		if(!jsonO.success){
		 			layerTipMsg(jsonO.success,"保存失败",jsonO.msg);
		 			$("#arrange-commit").removeClass("disabled");
		 			isSubmit=false;
		 			return;
		 		}else{
		 			layer.closeAll();
                    var stuId=$("#studentId").val();
					if(stuId==""){
						DutyList();
					}else{
				  		changeStuId();
					}
    			}
			},
			clearForm : false,
			resetForm : false,
			type : 'post',
			error:function(XMLHttpRequest, textStatus, errorThrown){}//请求出错 
		};
		$("#subForm").ajaxSubmit(options);
		
 	});
</script>
    