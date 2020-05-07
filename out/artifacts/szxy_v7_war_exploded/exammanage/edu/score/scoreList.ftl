<#if dtoList?exists && (dtoList?size > 0)>
<form id="scoreForm">
<input type="hidden" name="subjectId" value="${subjectInfo.subjectId!}"/>
<input type="hidden" name="inputType" value="${subjectInfo.inputType!}"/>
<input type="hidden" name="gradeType" value="${subjectInfo.gradeType!}"/>
<div class="table-wrapper">
	<table class="table table-bordered table-striped table-hover no-margin" id="showGradeTabble">
		<thead>
			<tr>
				<th>序号</th>
				<th>考号</th>
				<th>姓名</th>
				<th>学籍号</th>
				<th>身份证号</th>
				<th>考试成绩</th>
				<th>状态</th>
			</tr>
		</thead>
		<tbody>
				<#list dtoList as dto>
					<tr>
						<td>
						${dto_index+1!}
						<input type="hidden" name="dtoList[${dto_index}].id" value="${dto.scoreId!}"/>
						<input type="hidden" name="dtoList[${dto_index}].studentId" value="${dto.stuId!}"/>
						<input type="hidden" name="dtoList[${dto_index}].classId" value="${dto.classId!}"/>
						</td>
						<td>${dto.stuExamNum!}</td>
						<td>${dto.stuName!}</td>
						<td>${dto.stuCode!}</td>
						<td>${dto.className!}</td>
						<#if subjectInfo.inputType=='S'>
							<td>
							<input type="text" class="table-input score_class" name="dtoList[${dto_index}].score" id="score_${dto_index}" vtype = "number" nullable="false" value="${dto.score!}" placeholder="请输入考试成绩" maxLength="7">
							</td>
						<#else>
							<td>
								<select name="dtoList[${dto_index}].score" id="score_${dto_index}"  class="form-control">
							 		${mcodeSetting.getMcodeSelect('${subjectInfo.gradeType!}','${dto.score!}',"0")}
								</select>
							</td>
						</#if>
						<td>
							<select onchange="changeScore('${dto_index}');" name="dtoList[${dto_index}].scoreStatus" id="scoreStatus_${dto_index}" class="form-control"  notnull="false">
						 		${mcodeSetting.getMcodeSelect("DM-CJLX", '${dto.scoreStatus!}', "0")}
							</select>
						</td>
					</tr>
				</#list>
		</tbody>
	</table>		
</div>
</form>
<br/>
<div class="row">
	<div class="col-xs-12 text-right">
		<a href="javascript:void(0);" class="btn btn-blue" onclick="save('0');">保存</a>
		<#--<a href="javascript:void(0);" class="btn btn-blue" id="mysubmit">提交</a>-->
	</div>
</div>
<script>
function chooseTab(subjectId){
		$("#subjectId").val(subjectId);
		var examId=$("#examId").val();
		var classId=$("#classId").val();
		if(subjectId==''){
		}else{
			var c2='?examId='+examId+'&classId='+classId+"&subjectId="+subjectId;
			var url='${request.contextPath}/exammanage/edu/score/editlist'+c2;
			$(".listDiv").load(url);
		}
	}

$(function(){
    var $inp = $('input:text');
    $inp.on('keydown', function (e) {
        var key = e.which;
        if (key == 13) {
            e.preventDefault();
            var nxtIdx = $inp.index(this) + 1;
            $(":input:text:eq(" + nxtIdx + ")").focus().select();
        }
    });  
})
var isSubmit=false;
function save(state){
	if(isSubmit){
		return;
	}
	layer.closeAll();
	if(!allCheck()){
		return;
	}
	isSubmit=true;
	var classId=$("#classId").val();
	var examId=$("#examId").val();
	// 提交数据
	var options = {
		url : '${request.contextPath}/exammanage/edu/score/save',
		data:{state:state,classId:classId,examId:examId},
		dataType : 'json',
		success : function(data){
	 		if(data.success){
				layer.msg(data.msg, {
					offset: 't',
					time: 2000
				});
				chooseTab('${subjectId!}');
	 		}
	 		else{
	 			layerTipMsg(data.success,"失败",data.msg);
	 			isSubmit=false;
			}
		},
		clearForm : false,
		resetForm : false,
		type : 'post',
		error:function(XMLHttpRequest, textStatus, errorThrown){}//请求出错 
	};
	$("#scoreForm").ajaxSubmit(options);
} 
function allCheck(){
	<#if subjectInfo.inputType=='S'>
	var f=false;
	$(".score_class").each(function(){
		var index=$(this).attr("id").split("_")[1];
		if($(this).val()=='' && $("#scoreStatus_"+index).val()!=1){
			f=true;
			layer.tips('不能为空', $(this), {
				tipsMore: true,
				tips: 3
			});
		}
	});	
	if(f){
		isSubmit=false;
		return false;
	}
	var reg=/^(0|[1-9]\d{0,2})(\.\d{1,2})?$/;
	var max=${subjectInfo.fullScore!};
	
	$(".score_class").each(function(){
		var score=$(this).val();
		if(score){
			var r = score.match(reg);
			if(r==null){
				f=true;
				layer.tips('格式不正确(最多3位整数，2位小数)!', $(this), {
					tipsMore: true,
					tips: 3
				});
				return false;
			}
			var s=parseFloat(score);
			if(s>max){
				f=true;
				layer.tips('考试成绩不能超过'+max+'!', $(this), {
					tipsMore: true,
					tips: 3
				});
				return false;
			}
		}
	});	
	if(f){
		isSubmit=false;
		return false;
	}
	</#if>
	return true;
}

</script>
<#else>
 	<div class="no-data-container">
		<div class="no-data">
			<span class="no-data-img">
				<img src="${request.contextPath}/static/images/public/nodata6.png" alt="">
			</span>
			<div class="no-data-body">
				<p class="no-data-txt">暂无记录</p>
			</div>
		</div>
	</div>
</#if>