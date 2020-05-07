<div class="box box-default">
	<div class="box-body">
		<div class="filter clearfix">
			<div class="filter-item">
				<label for="" class="filter-name">学年：</label>
				<div class="filter-content">
					<select class="form-control" id="acadyear" name="acadyear" <#if type?default('')=='1'>onChange="changeTime()"<#else>onChange="changeGradedId()"</#if>>
						<#if acadyearList?exists && (acadyearList?size>0)>
		                    <#list acadyearList as item>
			                     <option value="${item!}" <#if semester.acadyear?default('a')==item?default('b')>selected</#if>>${item!}</option>
		                    </#list>
		                <#else>
		                    <option value="">未设置</option>
		                 </#if>
					</select>
				</div>
			</div>
			<div class="filter-item">
				<label for="" class="filter-name">学期：</label>
				<div class="filter-content">
					<select class="form-control" id="semester" name="semester" <#if type?default('')=='1'>onChange="changeTime()"<#else>onChange="changeGradedId()"</#if>>
						${mcodeSetting.getMcodeSelect('DM-XQ',(semester.semester?default(0))?string,'0')}
					</select>
				</div>
			</div>
			<div class="filter-item">
				<label for="" class="filter-name">年级：</label>
				<div class="filter-content">
					<select class="form-control" id="gradeCode" name="gradeCode" onChange="changeTime()">
		                <#if gradeList?? && (gradeList?size>0)>
							<#list gradeList as item>
								<option value="${item.gradeCode!}" >${item.gradeName!}</option>
							</#list>
						</#if>
					</select>
				</div>
			</div>
			<div class="filter-item">
				<label for="" class="filter-name">考试：</label>
				<div class="filter-content">
					<select class="form-control" id="examId" name="examId" onChange="changeExamId()">
						<option value="">---请选择---</option>
					</select>
				</div>
			</div>
			<div class="filter-item">
				<label for="" class="filter-name">选考科目：</label>
				<div class="filter-content">
					<select class="form-control" id="subjectId" name="subjectId" onChange="showList()">
						<option value="">---请选择---</option>
					</select>
				</div>
			</div>
			<div class="filter-item filter-item-right">
				<div class="filter-content">
					<a class="btn btn-blue" id="countBtn" href="javascript:void(0)" onclick="conCount()">计算赋分</a>
				</div>
			</div>
			<div class="filter-item filter-item-right">	
				<a href="javascript:" class="btn btn-blue" id="exportBtn" onclick="doExport()">导出</a>
			</div>
		</div>
		<div class="tab-container">
			<div  id="scoreList">
			
			
			</div>
		</div>
	</div>
</div>

<script>
$(function(){
		$('#countBtn').hide();
		$('#exportBtn').hide();
		changeTime();
	});
function changeTime(){
	var acadyear=$("#acadyear").val();
	var semester=$("#semester").val();
	var gradeCode=$("#gradeCode").val();
	//根据学年学期拿到考试id
	var examClass=$("#examId");
	$.ajax({
		url:"${request.contextPath}/exammanage/common/examList1",
		data:{acadyear:acadyear,semester:semester,unitId:'${unitId!}',gradeCode:gradeCode},
		dataType: "json",
		success: function(data){
			examClass.html("");
			if(data.length==0){
				examClass.append("<option value='' >-----请选择-----</option>");
				$('#countBtn').hide();
				$('#exportBtn').hide();
			}else{
				for(var i = 0; i < data.length; i ++){
					if(i==0){
						examClass.append("<option value='"+data[i].id+"' selected='selected'>"+data[i].examName+"</option>");
					}else{
						examClass.append("<option value='"+data[i].id+"' >"+data[i].examName+"</option>");
					}
				}
			}
			changeExamId();
		}
	});
}
	function changeExamId(){
		var examId=$("#examId").val();
		var subjectClass = $("#subjectId");
		$.ajax({
			url:"${request.contextPath}/exammanage/common/subjectList1",
			data:{unitId:'${unitId!}',examId:examId},
			dataType: "json",
			success: function(data){
				subjectClass.html("");
				if(data.length==0){
					subjectClass.append("<option value='' >没有选考科目信息</option>");
					$('#countBtn').hide();
					$('#exportBtn').hide();
				}else{
					$('#countBtn').show();
					$('#exportBtn').show();
					for(var i = 0; i < data.length; i ++){
						if(i==0){
							subjectClass.append("<option value='"+data[i].id+"' selected='selected'>"+data[i].subjectName+"(选考)</option>");
						}else{
							subjectClass.append("<option value='"+data[i].id+"' >"+data[i].subjectName+"(选考)</option>");
						}
					}
				}
				showList();
			}
		});
	}
	
	function showList(){
		var acadyear=$("#acadyear").val();
		var semester=$("#semester").val();
		var examId=$("#examId").val();
		var subjectId=$("#subjectId").val();
		var parmUrl="?acadyear="+acadyear+"&semester="+semester+"&examId="+examId+"&subjectId="+subjectId;
		var url="${request.contextPath}/exammanage/scoreConList/list/page"+parmUrl;
		$("#scoreList").load(url);
	}
	
	function conCount(){
		var examId=$("#examId").val();
		if(examId == ""){
			return;
		}
		layer.confirm(
		"是否重新计算赋分", 
		{ btn: ['确定','取消'] }, 
		function(){
			var layerIndex = layer.load();
 			//确定按钮方法				 
		 	$.ajax({
				url:'${request.contextPath}/exammanage/conCount/count',
				data:{unitId:'${unitId!}',examId:examId},
				success:function(data){
					layer.closeAll();
					var jsonO = JSON.parse(data);
			 		if(!jsonO.success){
		    			layerTipMsg(data.success,"失败",jsonO.msg);
			 		}
			 		else{
			 			// 显示成功信息
		    			layer.msg(jsonO.msg, {
							offset: 't',
							time: 2000
						});
		    			showList();	
	    			}
				}
			});			
		}, function(){
			//取消按钮方法
			layer.closeAll();			
		 });
	}
	
	function doExport(){
		var acadyear=$("#acadyear").val();
		var semester=$("#semester").val();
		var examId=$("#examId").val();	
		var subjectId=$("#subjectId").val();
		var parmUrl="?acadyear="+acadyear+"&semester="+semester+"&examId="+examId+"&subjectId="+subjectId;
		location.href = "${request.contextPath}/exammanage/scoreConList/export"+parmUrl;
	}
</script>


