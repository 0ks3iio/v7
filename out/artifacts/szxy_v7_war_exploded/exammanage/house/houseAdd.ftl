<style>
.layui-layer .chosen-container-multi .chosen-choices{overflow:auto;height:47px!important;}
.layui-layer .chosen-container .chosen-results{max-height:180px;}
</style>

<div class="" style="display:block;" id="myDiv">
<form id="subForm">
	<input type="hidden" name="id" id="id" value="${exammanageHouseRegister.id!}">
	<input type="hidden" name="acadyear" id="acadyear" value="${exammanageHouseRegister.acadyear!}">
	<input type="hidden" name="semester" id="semester" value="${exammanageHouseRegister.semester!}">
	<input type="hidden" name="examId" id="examId" value="${exammanageHouseRegister.examId!}">
	<input type="hidden" name="oldUnitId" id="oldUnitId" value="${exammanageHouseRegister.oldUnitId!}">
	<input type="hidden" name="oldSchoolId" id="oldSchoolId" value="${exammanageHouseRegister.oldSchoolId!}">
	<div class="main-content">
        <div class="main-content-inner">
                
                <div class="">
                       <div class="form-horizontal mt20">
                       
                       <div class="form-group">
		                       <label class="col-sm-2 control-label no-padding-right"><span style="color:red">*</span>选择学生：</label>
		                       <div class="filter-content">
                            		<select multiple="multiple" class="chnageOne" name="studentId" onChange="changeStu(this)" id="studentId" data-placeholder="学生选择" style="width: 200px; display: none;">
									    <#if enrollStudentList?? && (enrollStudentList?size>0)>
											<#list enrollStudentList as item>
												<option value="${item.studentId!}" <#if exammanageHouseRegister.studentId?? && exammanageHouseRegister.studentId==item.studentId!>selected</#if>>${item.student.studentName?default('')}</option>
											</#list>
										<#else>
											<option value="">暂无数据</option>
										</#if>
							     	</select>
                            	</div>
	                       </div>
						
						<div class="form-group">
		                       <label class="col-sm-2 control-label no-padding-right">身份证号：</label>
		                       <div class="filter-content" id="card" style="margin-top: 6px;">
				           		</div>
				         </div>
						<div class="form-group">
		                       <label class="col-sm-2 control-label no-padding-right">原学校：</label>
		                       <div class="filter-content" id="schoolName" style="margin-top: 6px;">
				           		</div>
				         </div>
						<div class="form-group">
		                       <label class="col-sm-2 control-label no-padding-right">原学校上级教育局单位：</label>
		                       <div class="filter-content" id="eduName" style="margin-top:29px;">
				           		</div>
				         </div>
						
            			<div class="form-group">
		                       <label class="col-sm-2 control-label no-padding-right" style="padding-left:2px;"><span style="color:red">*</span>转入教育局单位：</label>
		                       <div class="filter-content">
				            		<select multiple="multiple" name="newUnitId" id="newUnitId"  data-placeholder="转入教育局单位">
				                        <#if units?? && (units?size>0)>
											<#list units as item>
												<option value="${item.id!}" <#if exammanageHouseRegister.newUnitId?? && exammanageHouseRegister.newUnitId==item.id!>selected</#if>>${item.unitName?default('')}</option>
											</#list>
										<#else>
											<option value="">暂无数据</option>
										</#if>
				           			 </select>
				           		</div>
				         </div>
				         
			</div>
		<div>
	</div>
	</div>
</form>
</div>	
<div class="layer-footer" style="margin-bottom:-60px;">
	<a href="javascript:" class="btn btn-lightblue" id="arrange-commit">确定</a>
	<a href="javascript:" class="btn btn-grey" id="arrange-close">取消</a>
</div>
<script>

$('#newUnitId').chosen({
	width:'500px',
	results_height:'100px',
	multi_container_height:'100px',
	no_results_text:"未找到",//无搜索结果时显示的文本
	search_contains:true,//模糊匹配，false是默认从第一个匹配
	max_selected_options:1 //当select为多选时，最多选择个数
});
$('#studentId').chosen({
	width:'500px',
	results_height:'100px',
	multi_container_height:'100px',
	no_results_text:"未找到",//无搜索结果时显示的文本
	search_contains:true,//模糊匹配，false是默认从第一个匹配
	max_selected_options:1 //当select为多选时，最多选择个数
});

function changeStu(obj){
	var studentId = $(obj).val();
	$.ajax({
		url:"${request.contextPath}/exammanage/edu/house/stu?studentId="+studentId,
		dataType: "json",
		success: function(data){
			$("#card").html("");
			$("#schoolName").html("");
			$("#eduName").html("");
			if(data!=null){
				$("#card").html(data.identityCard);
				$("#schoolName").html(data.schoolName);
				$("#eduName").html(data.className);
			}
		}
	});
}


$(function(){
		//初始化多选控件
		initChosenMore("#myDiv");
	});


// 取消按钮操作功能
$("#arrange-close").on("click", function(){
    doLayerOk("#arrange-commit", {
    redirect:function(){},
    window:function(){layer.closeAll()}
    });     
 });
 
 
var isSubmit=false;
$("#arrange-commit").on("click", function(){	
	    
	var check = checkValue('#myDiv');
    if(!check){
        $(this).removeClass("disabled");
        isSubmit=false;
        return;
    }
	var studentId=$("#studentId").val();
	if(studentId==null){
		layerTipMsg(false,"提示","学生不能为空！");
		isSubmit=false;
		return;
	}		
	var newUnitId=$("#newUnitId").val();
	if(newUnitId==null){
		layerTipMsg(false,"提示","转入教育局单位不能为空！");
		isSubmit=false;
		return;
	}		
	
	var options = {
		url : "${request.contextPath}/exammanage/edu/house/save",
		dataType : 'json',
		success : function(data){
 			var jsonO = data;
	 		if(!jsonO.success){
	 			layerTipMsg(jsonO.success,"保存失败",jsonO.msg);
	 			$("#arrange-commit").removeClass("disabled");
	 			return;
	 		}else{
	 			layer.closeAll();
				layer.msg(jsonO.msg, {
					offset: 't',
					time: 2000
				});
			  	showStuList();
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