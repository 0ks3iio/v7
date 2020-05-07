<#import "/fw/macro/htmlcomponent.ftl" as htmlcom>
<form id="mysavediv">
<div class="table-wrapper">
	<table class="table table-bordered table-striped table-hover no-margin">
		<thead>
				<tr>
					<th>姓名</th>
					<th>身份证号</th>
					<th>班级</th>
					<th>学号</th>
					<th>考号</th>
					<th>操作</th>
				</tr>
		</thead>
		<tbody>
			<#if stuDtoList?exists && (stuDtoList?size > 0)>
				<#list stuDtoList as dto>
					<tr>
						<td>
						<input type="hidden" name="studentDtoList[${dto_index}].id" value="${dto.student.id!}">
						${dto.student.studentName!}
						</td>
						<td>${dto.student.identityCard!}</td>
						<td>${dto.className!}</td>
						<td>${dto.student.studentCode!}</td>
						<td><input type="text" class="table-input examnum_class" name="studentDtoList[${dto_index}].examNum" id="examNum_${dto_index}" class="table-input" value="${dto.examNum!}" maxlength="20"></td>
						<td>
							<#if dto.examNum?default('')!=''>
							<a href="javascript:;" class="table-btn color-red js-delete" onclick="deleteNum('${dto.student.id!}')">删除</a>
							</#if>
						</td>
					</tr>
				</#list>
		</#if>
		</tbody>
	</table>
	<#if stuDtoList?exists && (stuDtoList?size > 0)>
		<@htmlcom.pageToolBar container=".listDiv"/>
	</#if>		
</div>
<#if stuDtoList?exists && (stuDtoList?size > 0)>
	<div class="row">
		<div class="col-xs-12">
			<a href="javascript:" class="btn btn-blue" id="testNum-save" onclick="save()">保存</a>
		</div>
	</div>
</#if>
</form>
<script>
$(function(){
    // 回车获取焦点
    //$('input:text:first').focus();
    var $inp = $('input:text');
    $inp.on('keydown', function (e) {
        var key = e.which;
        if (key == 13) {
            e.preventDefault();
            var nxtIdx = $inp.index(this) + 1;
            $(":input:text:eq(" + nxtIdx + ")").focus().select();;
        }
    });          
})
var isSubmit=false;
function save(){
	var check = checkValue('#mysavediv');
	if(!check){
		isSubmit=false;
		return;
	}
	
	var f=false;
	var reg=/^[0-9a-zA-Z]*$/;
	$(".examnum_class").each(function(){
		if(r!=""){
			var r = $(this).val().match(reg);
			if(r==null){
				f=true;
				layer.tips('格式不对，只能输入字母或者数字!', $(this), {
					tipsMore: true,
					tips: 3
				});
				return false;
			}
		}
	});	
	if(f){
		isSubmit=false;
		return;
	}
	if(isSubmit){
		return;
	}
	isSubmit=true;
	// 提交数据
	var options = {
		url : '${request.contextPath}/scoremanage/testNumber/updateTestNum?examId=${examId!}',
		dataType : 'json',
		success : function(data){
	 		if(data.success){
	 			layerTipMsg(data.success,"成功",data.msg);
				searchList("&"+getPageParameter());
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
	$("#mysavediv").ajaxSubmit(options);
}
function deleteNum(stuId){
	if(isSubmit){
		return;
	}
	$.ajax({
		url:'${request.contextPath}/scoremanage/testNumber/deleteTestNum?examId=${examId!}',
		data:{"stuId":stuId},
		success:function(data){
			layer.closeAll();
			var jsonO = JSON.parse(data);
	 		if(!jsonO.success){
    			layerTipMsg(jsonO.success,"失败",jsonO.msg);
    			isSubmit=false;
	 		}
	 		else{
	 			//成功
    			searchList("&"+getPageParameter());			
			}
		}
	});	
}
</script>