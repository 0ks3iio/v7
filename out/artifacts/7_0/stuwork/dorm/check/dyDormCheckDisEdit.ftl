<form id="submitForm">
<input type="hidden" id="acadyear" name="acadyear" value="${acadyear!}">
<input type="hidden" id="semesterStr" name="semester" value="${semesterStr!}">
<div class="layer layer-add">
	<div class="layer-content">
		<div class="form-horizontal">
			<div class="form-group">
				<label class="col-sm-2 control-label no-padding-right">寝室楼</label>
				<div class="col-sm-2">
					<select name="buildingId" id="buildingId" nullable="false" onchange="setRoomList()" class="form-control" style="width:100px;>
	            	<option value="">请选择</option>
	            	<#if buildingList?? && (buildingList?size>0)>
	                    <#list buildingList as building>
	                    <option value="${building.id!}">${building.name!}</option>
	                    </#list>
	                </#if> 
	                </select>
				</div>
				<label class="col-sm-1 control-label no-padding-right" style="width:100px;margin-left:0px;">寝室号</label>
				<div class="col-sm-2">
					<select name="roomId" id="roomId" nullable="false" onchange="setStudentList()" class="form-control" style="width:100px;">
	            	<option value="">请选择</option>
	            	<#if roomList?exists && (roomList?size>0)>
						<#list roomList as item>
							<option value="${item.id!}" >${item.roomName!}</option>
						</#list>
					</#if> 
                </select>
				</div>
			</div>
			<div class="form-group">
				<label class="col-sm-2 control-label no-padding-right"><font style="color:red;">*</font>违纪人</label>
				<div class="col-sm-8">
					<select name="studentId" id="studentId" nullable="false" class="form-control"  >
                		<option value="">请选择</option>
                	</select>
				</div>
			</div>
			<div class="form-group">
				<label class="col-sm-2 control-label no-padding-right"><font style="color:red;">*</font>违纪日期</label>
				<div class="col-sm-8">
					<div class="input-group">
						<input class="form-control date-picker startTime-date date-picker-time" vtype="data" type="text" nullable="false" name="checkDate" id="checkDate" placeholder="违纪日期" value="${(nowDate?string('yyyy-MM-dd'))!}">
						<span class="input-group-addon">
							<i class="fa fa-calendar bigger-110"></i>
						</span>
					</div>
				</div>
			</div>
			<div class="form-group">
				<label class="col-sm-2 control-label no-padding-right"><font style="color:red;">*</font>违纪扣分</label>
				<div class="col-sm-8">
					<input type="text" id="score" regextip="只能一位小数点"  regex="/^(\d+\.\d{1,1}|\d+)$/" vtype="number" min="0" max="10"  name="score" nullable="false" class="form-control">
				</div>
			</div>
			<div class="form-group">
				<label class="col-sm-2 control-label no-padding-right">扣分原因</label>
				<div class="col-sm-8">
					<textarea name="reason" id="reason" rows="3"  maxlength="200" class="form-control"></textarea>
				</div>
			</div>
		</div>
	</div>
</div>
</form>
<div class="layer-footer">
    <a href="javascript:" class="btn btn-lightblue" id="result-commit" onclick="doSaveRoom()">确定</a>
    <a href="javascript:" class="btn btn-lightblue" id="result-close">取消</a>
</div>
<script>
	$(function(){
		$(".layer-add").show();
		$("#result-close").on("click", function(){
		    layer.closeAll();
		 });
		//初始化单选控件
		var viewContent={
			'allow_single_deselect':'false',//是否可清除，第一个option的text必须为空才能使用
			'select_readonly':'false',//是否只读
			'width' : '150px',//输入框的宽度
			'results_height' : '150px'//下拉选择的高度
		}
		initChosenOne(".chosenClassHeaderClass","",viewContent);
		
		//初始化日期控件
		var viewContent1={
			'format' : 'yyyy-mm-dd',
			'minView' : '2'
		};
		initCalendarData("#submitForm",".date-picker",viewContent1);
		
	});
	function setStudentList(){
		var roomId=$("#roomId").val();
		var acadyear=$("#acadyear").val();
		var semesterStr=$("#semesterStr").val(); 
		$.ajax({
			url:'${request.contextPath}/stuwork/dorm/check/setStudentList',
			data: {'roomId':roomId,'acadyear':acadyear,'semesterStr':semesterStr},  
			type:'post',
			success:function(data) {
				var jsonO = JSON.parse(data);
				$("#studentId").empty(); 
				
				var studentList=jsonO.studentList;
				if(studentList!="" && studentList.length>0){
					var studentHtml='<option value="">请选择</option>';
					for(var i=0;i<studentList.length;i++){
						 var student=studentList[i];
						 studentHtml+='<option value="'+student.studentId+'">';
						 studentHtml+=student.studentName+'</option>';
					}
					$("#studentId").append(studentHtml);
				}else{
					$("#studentId").append('<option value="">请选择</option>'); 
				}
			},
	 		error : function(XMLHttpRequest, textStatus, errorThrown) {  
	 			
			}
		});
	}
	function setRoomList(){
		var buildingId=$("#buildingId").val();
		$.ajax({
			url:'${request.contextPath}/stuwork/dorm/check/setRoomList'	,
			data: {'buildingId':buildingId},  
			type:'post',
			success:function(data) {
				var jsonO = JSON.parse(data);
				$("#roomId").empty(); 
				$("#studentId").empty(); 
				
				var roomList=jsonO.roomList;
				if(roomList!="" && roomList.length>0){
					var roomHtml='<option value="">请选择</option>';
					for(var i=0;i<roomList.length;i++){
						 var room=roomList[i];
						 roomHtml+='<option value="'+room.roomId+'">';
						 roomHtml+=room.roomName+'</option>';
					}
					$("#roomId").append(roomHtml);
				}else{
					$("#roomId").append('<option value="">请选择</option>'); 
				}
				$("#studentId").append('<option value="">请选择</option>'); 
			},
	 		error : function(XMLHttpRequest, textStatus, errorThrown) {  
	 			
			}
		});
	}
	var isSubmit=false;
	function doSaveRoom(){
		if(isSubmit){
			return;
		}
		isSubmit = true;
		var check = checkValue('#submitForm');
		if(!check){
		 	isSubmit=false;
		 	return;
		}
		
		var ii = layer.load();
		var options = {
			url : "${request.contextPath}/stuwork/dorm/check/saveDis",
			dataType : 'json',
			success : function(data){
	 			var jsonO = data;
		 		if(!jsonO.success){
		 			layerTipMsg(jsonO.success,"保存失败",jsonO.msg);
		 			isSubmit = false;
		 		}else{
		 			layer.closeAll();
					layerTipMsg(jsonO.success,"保存成功",jsonO.msg);
				  	itemShowList(2);
    			}
    			layer.close(ii);
			},
			clearForm : false,
			resetForm : false,
			type : 'post',
			error:function(XMLHttpRequest, textStatus, errorThrown){}//请求出错 
		};
		$("#submitForm").ajaxSubmit(options);
	}
</script>