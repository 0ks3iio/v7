<div class="layer layer-addTerm layer-change" style="display:block;" id="myDiv">
<form id="subForm">
<input type="hidden" name="id" id="id" value="${leagueRecord.id!}">
<input type="hidden" name="studentId" id="studentId" value="${leagueRecord.studentId!}">
<input type="hidden" name="acadyear" id="acadyear" value="${leagueRecord.acadyear!}">
<input type="hidden" name="semester" id="semester" value="${leagueRecord.semester!}">
	<div class="layer-body">
		<div class="filter clearfix"> 
				<div class="filter clearfix">
					<div class="filter-item">
						<label for="" class="filter-name"><span style="color:red">*</span>&nbsp;社团名称：</label>
						<div class="filter-content">
							<input type="text" class="form-control" name="leagueName" id="leagueName" maxlength="30" value="${leagueRecord.leagueName!}" nullable="false"/>
						</div>
					</div>
					
					<div class="filter-item">
						<label for="" class="filter-name"><span style="color:red">*</span>&nbsp;参加时间：</label>
						<div class="filter-content">
					    <div class="input-group">
						<input class="form-control date-picker" vtype="data" style="width: 110px" type="text" nullable="false" name="joinDate" id="joinDate" placeholder="参加时间" value="${(leagueRecord.joinDate?string('yyyy-MM-dd'))!}">
						<span class="input-group-addon">
							<i class="fa fa-calendar bigger-110"></i>
						</span>
					    </div>
						</div>
					</div>
					
					<div class="filter-item">
						<label for="" class="filter-name"><span style="color:red">*</span>&nbsp;活动内容：</label>
						<div class="filter-content">
							<input type="text" class="form-control" name="leagueContent" id="leagueContent" maxlength="200" value="${leagueRecord.leagueContent!}" nullable="false"/>
						</div>
					</div>
					
					<div class="filter-item">
						<label for="" class="filter-name">备注：</label>
						<div class="filter-content">
							<input type="text" class="form-control" name="remark" id="remark" maxlength="200" value="${leagueRecord.remark!}"/>
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
 	
 		var stuId = $("#stuId").val();
		var options = {
			url : "${request.contextPath}/studevelop/leagueRecord/save",
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
					if(stuId==""){
						LeagueList();
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
    